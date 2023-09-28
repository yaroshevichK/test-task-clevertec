package task.clevertec.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import task.clevertec.entity.response.AccountResponse;
import task.clevertec.service.IAccountService;
import task.clevertec.service.impl.AccountService;
import task.clevertec.util.Converter;
import task.clevertec.util.LocalDateAdapter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static task.clevertec.util.Constants.ACCOUNTS;
import static task.clevertec.util.Constants.MSG_STATUS_OK;
import static task.clevertec.util.Constants.MSG_STATUS_WRONG;
import static task.clevertec.util.Constants.PATTERN_ACCOUNT_ID;
import static task.clevertec.util.Constants.SLASH;

@WebServlet(urlPatterns = "/accounts/*")
public class ServletAccounts extends HttpServlet {
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();
    private final IAccountService accountService = new AccountService();

    @Override
    protected void doGet(final HttpServletRequest req,
                         final HttpServletResponse resp) throws IOException {

        String uri = req.getRequestURI();
        if (uri.endsWith(SLASH)) {
            uri = uri.substring(0, uri.length() - 1);
        }

        if (uri.endsWith(ACCOUNTS)) {
            List<AccountResponse> accounts = accountService.getAllAccounts();

            PrintWriter writer = resp.getWriter();
            writer.println(gson.toJson(accounts));
        } else {
            Pattern pattern = Pattern.compile(PATTERN_ACCOUNT_ID);
            Matcher matcher = pattern.matcher(uri);
            if (matcher.find()) {
                Integer id = Converter.strToInt(matcher.group(2));
                AccountResponse account = accountService.findAccountById(id);

                PrintWriter writer = resp.getWriter();
                writer.println(gson.toJson(account));
            }
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String collect = req.getReader().lines().collect(Collectors.joining());
        AccountResponse account = gson.fromJson(collect, AccountResponse.class);

        boolean result = accountService.saveAccount(account);
        PrintWriter writer = resp.getWriter();
        writer.write(result ? MSG_STATUS_OK : MSG_STATUS_WRONG);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String collect = req.getReader().lines().collect(Collectors.joining());
        AccountResponse account = gson.fromJson(collect, AccountResponse.class);

        boolean result = accountService.updateAccount(account);
        PrintWriter writer = resp.getWriter();
        writer.write(result ? MSG_STATUS_OK : MSG_STATUS_WRONG);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String uri = req.getRequestURI();
        if (uri.endsWith(SLASH)) {
            uri = uri.substring(0, uri.length() - 1);
        }

        Pattern pattern = Pattern.compile(PATTERN_ACCOUNT_ID);
        Matcher matcher = pattern.matcher(uri);
        if (matcher.find()) {
            Integer id = Converter.strToInt(matcher.group(2));
            boolean result = accountService.deleteAccountById(id);

            PrintWriter writer = resp.getWriter();
            writer.write(result ? MSG_STATUS_OK : MSG_STATUS_WRONG);
        }
    }
}
