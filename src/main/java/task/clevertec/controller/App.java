package task.clevertec.controller;

import task.clevertec.command.Command;
import task.clevertec.command.CommandFactory;
import task.clevertec.command.impl.IncomePercent;
import task.clevertec.command.impl.SignIn;
import task.clevertec.command.impl.Statement;
import task.clevertec.entity.TypeTransaction;
import task.clevertec.entity.User;
import task.clevertec.repository.datasource.ConnectionDB;
import task.clevertec.util.Converter;

import java.io.IOException;

import static task.clevertec.util.Constants.ACTION_MENU;
import static task.clevertec.util.Constants.CONSOLE;
import static task.clevertec.util.Constants.INPUT_NUMBER_MENU;
import static task.clevertec.util.Constants.MSG_SIGN_OUT;
import static task.clevertec.util.Constants.MSG_WRONG_INIT;
import static task.clevertec.util.Constants.MSG_WRONG_NUMBER_MENU;
import static task.clevertec.util.Constants.OUT;
import static task.clevertec.util.Constants.STR_MENU;
import static task.clevertec.util.Constants.STR_SIGN_OUT;
import static task.clevertec.util.Constants.STR_STATEMENT;

public class App {
    public static void main(String[] args) {
        boolean initResult = ConnectionDB.getInstance().initDatabase();
        if (!initResult) {
            OUT.println(MSG_WRONG_INIT);
            System.exit(0);
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {
            //add log
        }

        User authUser = auth();
        incomePercent(authUser);
        runningApp(authUser);
    }

    private static User auth() {
        return new SignIn().getAuthUser();
    }

    private static void incomePercent(User authUser) {
        Thread checkPercent = new IncomePercent(authUser);
        checkPercent.setDaemon(true);
        checkPercent.start();
    }

    private static void printMenu() {
        OUT.println(STR_MENU);
        for (TypeTransaction type : TypeTransaction.values()) {
            OUT.printf(ACTION_MENU, type.ordinal() + 1, type.getName());
        }
        int count = TypeTransaction.values().length;
        OUT.println(String.format(STR_STATEMENT, count + 1));
        OUT.println(STR_SIGN_OUT);
    }

    private static void runningApp(User authUser) {
        int count = TypeTransaction.values().length;
        do {
            printMenu();
            OUT.print(INPUT_NUMBER_MENU);
            OUT.flush();
            Integer numMenu = Converter.strToInt(CONSOLE.next());

            if (numMenu == null) {
                OUT.println(MSG_WRONG_NUMBER_MENU);
                continue;
            }
            if (numMenu == 0) {
                OUT.println(MSG_SIGN_OUT);
                System.exit(0);
            }

            if (numMenu == count + 1) {
                Command command = new Statement(authUser);
                String result = command.execute();
                OUT.println(result);
                continue;
            }

            Command command = CommandFactory.getCommand(numMenu, authUser);
            if (command == null) {
                OUT.println(MSG_WRONG_NUMBER_MENU);
            } else {
                String result = command.execute();
                OUT.println(result);
            }
        } while (true);
    }
}
