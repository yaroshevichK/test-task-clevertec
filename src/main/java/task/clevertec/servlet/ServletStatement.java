package task.clevertec.servlet;

import task.clevertec.entity.response.AccountResponse;
import task.clevertec.service.IAccountService;
import task.clevertec.service.impl.AccountService;
import task.clevertec.util.Converter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static task.clevertec.util.Constants.DATE_FROM;
import static task.clevertec.util.Constants.DATE_TO;
import static task.clevertec.util.Constants.MSG_STATUS_OK;
import static task.clevertec.util.Constants.MSG_STATUS_WRONG;
import static task.clevertec.util.Constants.PATTERN_STMT;
import static task.clevertec.util.Constants.SLASH;
import static task.clevertec.util.Constants.STATEMENT;

@WebServlet(urlPatterns = STATEMENT)
public class ServletStatement extends HttpServlet {
    private final IAccountService accountService = new AccountService();

    @Override
    protected void doGet(final HttpServletRequest req,
                         final HttpServletResponse resp) throws IOException {

        String uri = req.getRequestURI();
        if (uri.endsWith(SLASH)) {
            uri = uri.substring(0, uri.length() - 1);
        }
        Pattern pattern = Pattern.compile(PATTERN_STMT);
        Matcher matcher = pattern.matcher(uri);
        if (matcher.find()) {
            Integer id = Converter.strToInt(matcher.group(2));
            AccountResponse account = accountService.findAccountById(id);

            String dateFromS = req.getParameter(DATE_FROM);
            String dateToS = req.getParameter(DATE_TO);
            LocalDate dateFrom = Optional.ofNullable(dateFromS)
                    .map(LocalDate::parse)
                    .orElse(null);
            LocalDate dateTo = Optional.ofNullable(dateToS)
                    .map(LocalDate::parse)
                    .orElse(null);

            boolean result = accountService.generateStatement(account, dateFrom, dateTo);
            PrintWriter writer = resp.getWriter();
            writer.write(result ? MSG_STATUS_OK : MSG_STATUS_WRONG);
        }
    }
}
