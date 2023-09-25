package task.clevertec.servlet;

import com.google.gson.Gson;
import task.clevertec.entity.response.BankResponse;
import task.clevertec.service.IBankService;
import task.clevertec.service.impl.BankService;
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
import static task.clevertec.util.Constants.MSG_STATUS_OK;
import static task.clevertec.util.Constants.MSG_STATUS_WRONG;
import static task.clevertec.util.Constants.PATTERN_BANK_ID;
import static task.clevertec.util.Constants.SLASH;

@WebServlet(urlPatterns = "/banks/*")
public class ServletBank extends HttpServlet {
    private final Gson gson = new Gson();
    private final IBankService bankService = new BankService();

    @Override
    protected void doGet(final HttpServletRequest req,
                         final HttpServletResponse resp) throws IOException {

        String uri = req.getRequestURI();
        if (uri.endsWith(SLASH)) {
            uri = uri.substring(0, uri.length() - 1);
        }

        if (uri.endsWith(BANKS)) {
            List<BankResponse> banks = bankService.getAllBanks();

            PrintWriter writer = resp.getWriter();
            writer.println(gson.toJson(banks));
        } else {
            Pattern pattern = Pattern.compile(PATTERN_BANK_ID);
            Matcher matcher = pattern.matcher(uri);
            if (matcher.find()) {
                Integer id = Converter.strToInt(matcher.group(2));
                BankResponse bank = bankService.findBankById(id);

                PrintWriter writer = resp.getWriter();
                writer.println(gson.toJson(bank));
            }
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String collect = req.getReader().lines().collect(Collectors.joining());
        BankResponse bank = gson.fromJson(collect, BankResponse.class);

        boolean result = bankService.saveBank(bank);
        PrintWriter writer = resp.getWriter();
        writer.write(result ? MSG_STATUS_OK : MSG_STATUS_WRONG);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String collect = req.getReader().lines().collect(Collectors.joining());
        BankResponse bank = gson.fromJson(collect, BankResponse.class);

        boolean result = bankService.updateBank(bank);
        PrintWriter writer = resp.getWriter();
        writer.write(result ? MSG_STATUS_OK : MSG_STATUS_WRONG);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String uri = req.getRequestURI();
        if (uri.endsWith(SLASH)) {
            uri = uri.substring(0, uri.length() - 1);
        }

        Pattern pattern = Pattern.compile(PATTERN_BANK_ID);
        Matcher matcher = pattern.matcher(uri);
        if (matcher.find()) {
            Integer id = Converter.strToInt(matcher.group(2));
            boolean result = bankService.deleteBankById(id);

            PrintWriter writer = resp.getWriter();
            writer.write(result ? MSG_STATUS_OK : MSG_STATUS_WRONG);
        }
    }
}
