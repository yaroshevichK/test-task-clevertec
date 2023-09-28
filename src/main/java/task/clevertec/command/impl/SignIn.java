package task.clevertec.command.impl;

import task.clevertec.util.Converter;
import task.clevertec.entity.User;
import task.clevertec.service.IUserService;
import task.clevertec.service.impl.UserService;

import static task.clevertec.util.Constants.CONSOLE;
import static task.clevertec.util.Constants.INPUT_NUMBER_MENU;
import static task.clevertec.util.Constants.INPUT_USERNAME;
import static task.clevertec.util.Constants.MSG_SIGN_OUT;
import static task.clevertec.util.Constants.MSG_WRONG_NUMBER_MENU;
import static task.clevertec.util.Constants.MSG_WRONG_USER;
import static task.clevertec.util.Constants.OUT;
import static task.clevertec.util.Constants.STR_MENU;
import static task.clevertec.util.Constants.STR_SIGN_IN;
import static task.clevertec.util.Constants.STR_SIGN_OUT;

public class SignIn {
    private final IUserService userService = new UserService();

    public User getAuthUser() {
        do {
            printMenu();
            OUT.print(INPUT_NUMBER_MENU);
            OUT.flush();
            String next = CONSOLE.next();
            Integer numMenu = Converter.strToInt(next);


            if (numMenu == null) {
                OUT.println(MSG_WRONG_NUMBER_MENU);
                continue;
            }

            switch (numMenu) {
                case 1 -> {
                    OUT.print(INPUT_USERNAME);
                    OUT.flush();
                    String username = CONSOLE.next();

                    User user = userService.getUser(username);

                    if (user != null) {
                        return user;
                    } else {
                        OUT.println(MSG_WRONG_USER);
                    }
                }
                case 0 -> {
                    OUT.println(MSG_SIGN_OUT);
                    System.exit(0);
                }
                default -> OUT.println(MSG_WRONG_NUMBER_MENU);
            }

        } while (true);
    }

    private void printMenu() {
        OUT.println(STR_MENU);
        OUT.println(STR_SIGN_IN);
        OUT.println(STR_SIGN_OUT);
    }
}
