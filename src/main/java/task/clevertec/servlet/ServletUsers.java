package task.clevertec.servlet;

import com.google.gson.Gson;
import task.clevertec.entity.response.UserResponse;
import task.clevertec.service.IUserService;
import task.clevertec.service.impl.UserService;
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

import static task.clevertec.util.Constants.MSG_STATUS_OK;
import static task.clevertec.util.Constants.MSG_STATUS_WRONG;
import static task.clevertec.util.Constants.PATTERN_USER_ID;
import static task.clevertec.util.Constants.SLASH;
import static task.clevertec.util.Constants.USERS;

@WebServlet(urlPatterns = "/users/*")
public class ServletUsers extends HttpServlet {
    private final Gson gson = new Gson();
    private final IUserService userService = new UserService();

    @Override
    protected void doGet(final HttpServletRequest req,
                         final HttpServletResponse resp) throws IOException {

        String uri = req.getRequestURI();
        if (uri.endsWith(SLASH)) {
            uri = uri.substring(0, uri.length() - 1);
        }

        if (uri.endsWith(USERS)) {
            List<UserResponse> users = userService.getAllUsers();

            PrintWriter writer = resp.getWriter();
            writer.println(gson.toJson(users));
        } else {
            Pattern pattern = Pattern.compile(PATTERN_USER_ID);
            Matcher matcher = pattern.matcher(uri);
            if (matcher.find()) {
                Integer id = Converter.strToInt(matcher.group(2));
                UserResponse user = userService.findUserById(id);

                PrintWriter writer = resp.getWriter();
                writer.println(gson.toJson(user));
            }
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String collect = req.getReader().lines().collect(Collectors.joining());
        UserResponse user = gson.fromJson(collect, UserResponse.class);

        boolean result = userService.saveUser(user);
        PrintWriter writer = resp.getWriter();
        writer.write(result ? MSG_STATUS_OK : MSG_STATUS_WRONG);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String collect = req.getReader().lines().collect(Collectors.joining());
        UserResponse user = gson.fromJson(collect, UserResponse.class);

        boolean result = userService.updateUser(user);
        PrintWriter writer = resp.getWriter();
        writer.write(result ? MSG_STATUS_OK : MSG_STATUS_WRONG);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String uri = req.getRequestURI();
        if (uri.endsWith(SLASH)) {
            uri = uri.substring(0, uri.length() - 1);
        }

        Pattern pattern = Pattern.compile(PATTERN_USER_ID);
        Matcher matcher = pattern.matcher(uri);
        if (matcher.find()) {
            Integer id = Converter.strToInt(matcher.group(2));
            boolean result = userService.deleteUserById(id);

            PrintWriter writer = resp.getWriter();
            writer.write(result ? MSG_STATUS_OK : MSG_STATUS_WRONG);
        }
    }
}
