package task.clevertec.servlet;

import com.google.gson.Gson;
import task.clevertec.entity.response.BankResponse;
import task.clevertec.entity.response.CurrencyResponse;
import task.clevertec.service.ICurrencyService;
import task.clevertec.service.impl.BankService;
import task.clevertec.service.impl.CurrencyService;
import task.clevertec.util.Converter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static task.clevertec.util.Constants.BANKS;
import static task.clevertec.util.Constants.CURRENCIES;
import static task.clevertec.util.Constants.MSG_STATUS_OK;
import static task.clevertec.util.Constants.MSG_STATUS_WRONG;
import static task.clevertec.util.Constants.PATTERN_BANK_ID;
import static task.clevertec.util.Constants.PATTERN_CURR_ID;
import static task.clevertec.util.Constants.SLASH;

@WebServlet(urlPatterns = "/currencies/*")
public class ServletCurrency extends HttpServlet {
    private final Gson gson = new Gson();
    private final ICurrencyService currencyService = new CurrencyService();

    @Override
    protected void doGet(final HttpServletRequest req,
                         final HttpServletResponse resp) throws IOException {

        String uri = req.getRequestURI();
        if (uri.endsWith(SLASH)) {
            uri = uri.substring(0, uri.length() - 1);
        }

        if (uri.endsWith(CURRENCIES)) {
            List<CurrencyResponse> currencies = currencyService.getAllCurrencies();

            PrintWriter writer = resp.getWriter();
            writer.println(gson.toJson(currencies));
        } else {
            Pattern pattern = Pattern.compile(PATTERN_CURR_ID);
            Matcher matcher = pattern.matcher(uri);
            if (matcher.find()) {
                Integer id = Converter.strToInt(matcher.group(2));
                CurrencyResponse currency = currencyService.findCurrencyById(id);

                PrintWriter writer = resp.getWriter();
                writer.println(gson.toJson(currency));
            }
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String collect = req.getReader().lines().collect(Collectors.joining());
        CurrencyResponse currency = gson.fromJson(collect, CurrencyResponse.class);

        boolean result = currencyService.saveCurrency(currency);
        PrintWriter writer = resp.getWriter();
        writer.write(result ? MSG_STATUS_OK : MSG_STATUS_WRONG);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String collect = req.getReader().lines().collect(Collectors.joining());
        CurrencyResponse currency = gson.fromJson(collect,CurrencyResponse.class);

        boolean result = currencyService.updateCurrency(currency);
        PrintWriter writer = resp.getWriter();
        writer.write(result ? MSG_STATUS_OK : MSG_STATUS_WRONG);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String uri = req.getRequestURI();
        if (uri.endsWith(SLASH)) {
            uri = uri.substring(0, uri.length() - 1);
        }

        Pattern pattern = Pattern.compile(PATTERN_CURR_ID);
        Matcher matcher = pattern.matcher(uri);
        if (matcher.find()) {
            Integer id = Converter.strToInt(matcher.group(2));
            boolean result = currencyService.deleteCurrencyById(id);

            PrintWriter writer = resp.getWriter();
            writer.write(result ? MSG_STATUS_OK : MSG_STATUS_WRONG);
        }
    }
}
