package task.clevertec.command.impl;

import lombok.AllArgsConstructor;
import task.clevertec.command.Command;
import task.clevertec.entity.Account;
import task.clevertec.entity.FileFormat;
import task.clevertec.entity.Period;
import task.clevertec.entity.User;
import task.clevertec.service.IAccountService;
import task.clevertec.service.ITransactionService;
import task.clevertec.service.impl.AccountService;
import task.clevertec.service.impl.TransactionService;
import task.clevertec.util.Converter;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static task.clevertec.util.Constants.ACTION_MENU;
import static task.clevertec.util.Constants.CONSOLE;
import static task.clevertec.util.Constants.EMPTY_STRING;
import static task.clevertec.util.Constants.FIRST_DAY;
import static task.clevertec.util.Constants.INPUT_ACCOUNT;
import static task.clevertec.util.Constants.INPUT_DATE_OPEN;
import static task.clevertec.util.Constants.INPUT_MONTH;
import static task.clevertec.util.Constants.INPUT_NUMBER_MENU;
import static task.clevertec.util.Constants.INPUT_YEAR;
import static task.clevertec.util.Constants.MSG_WRONG_ACCOUNTS;
import static task.clevertec.util.Constants.MSG_WRONG_MONTH;
import static task.clevertec.util.Constants.MSG_WRONG_NUMBER_MENU;
import static task.clevertec.util.Constants.MSG_WRONG_USER_ACC;
import static task.clevertec.util.Constants.MSG_WRONG_YEAR;
import static task.clevertec.util.Constants.OUT;
import static task.clevertec.util.Constants.STR_MENU_CANCEL;

@AllArgsConstructor
public class Statement implements Command {
    private final IAccountService accountService = new AccountService();
    private final ITransactionService transactionService = new TransactionService();
    private User user;


    private void printMenu() {
        for (Period period : Period.values()) {
            OUT.printf(ACTION_MENU, period.ordinal() + 1, period.getName());
        }
        OUT.println(STR_MENU_CANCEL);
    }

    private static void printFormat() {
        for (FileFormat format : FileFormat.values()) {
            OUT.printf(ACTION_MENU, format.ordinal() + 1, format);
        }
        OUT.println(STR_MENU_CANCEL);
    }

    private void printAccounts(List<Account> accounts) {
        for (int i = 0; i < accounts.size(); i++) {
            OUT.printf(ACTION_MENU, i + 1, accounts.get(i).getNumber());
        }
    }

    private Integer chooseAccount(List<Account> accounts) {
        Integer accountId;
        do {
            printAccounts(accounts);

            OUT.print(INPUT_ACCOUNT);
            OUT.flush();
            accountId = Converter.strToInt(CONSOLE.next());

            if (accountId == null) {
                OUT.println(MSG_WRONG_USER_ACC);
                continue;
            }

            if (accountId == 0) {
                break;
            }

            if (accountId < 0 || accountId > accounts.size()) {
                OUT.println(MSG_WRONG_USER_ACC);
            } else {
                break;
            }

        } while (true);

        return accountId;
    }

    private Integer choosePeriod() {
        Integer period;
        do {
            printMenu();

            OUT.print(INPUT_NUMBER_MENU);
            OUT.flush();
            period = Converter.strToInt(CONSOLE.next());

            if (period == null) {
                OUT.println(MSG_WRONG_NUMBER_MENU);
                continue;
            }
            if (period == 0) {
                break;
            }

            if (period < 0 || period > Period.values().length) {
                OUT.println(MSG_WRONG_NUMBER_MENU);
            } else {
                break;
            }
        }
        while (true);

        return period;
    }

    private Integer chooseFormat() {
        Integer formatNumber;
        do {
            printFormat();

            OUT.print(INPUT_NUMBER_MENU);
            OUT.flush();
            formatNumber = Converter.strToInt(CONSOLE.next());

            if (formatNumber == null) {
                OUT.println(MSG_WRONG_NUMBER_MENU);
                continue;
            }
            if (formatNumber == 0) {
                break;
            }

            if (formatNumber < 0 || formatNumber > Period.values().length) {
                OUT.println(MSG_WRONG_NUMBER_MENU);
            } else {
                break;
            }
        }
        while (true);

        return formatNumber;
    }

    private Integer chooseMonth(LocalDate dateOpen) {
        Integer month;
        do {
            if (dateOpen != null) {
                OUT.println(INPUT_DATE_OPEN + dateOpen);
            }
            OUT.print(INPUT_MONTH);
            OUT.flush();
            month = Converter.strToInt(CONSOLE.next());

            if (month == null) {
                OUT.println(MSG_WRONG_MONTH);
                continue;
            }

            if (month == 0) {
                break;
            }

            if (month < 0 || month > 12) {
                OUT.println(MSG_WRONG_MONTH);
            } else {
                break;
            }
        } while (true);

        return month;
    }

    private Integer chooseYear(LocalDate dateOpen) {
        Integer year;
        do {
            if (dateOpen != null) {
                OUT.println(INPUT_DATE_OPEN + dateOpen);
            }
            OUT.print(INPUT_YEAR);
            OUT.flush();
            year = Converter.strToInt(CONSOLE.next());

            if (year == null) {
                OUT.println(MSG_WRONG_YEAR);
                continue;
            }

            if (year == 0) {
                break;
            }

            if (year < 0 || year > LocalDate.now().getYear()) {
                OUT.println(MSG_WRONG_YEAR);
            } else {
                break;
            }
        } while (true);

        return year;
    }

    @Override
    public String execute() {
        List<Account> accounts = accountService.getUserAccounts(user);
        if (accounts.size() == 0) {
            return MSG_WRONG_ACCOUNTS;
        }
        Integer accountId = chooseAccount(accounts);
        if (accountId == null) {
            return MSG_WRONG_USER_ACC;
        } else if (accountId == 0) {
            return EMPTY_STRING;
        }
        Account account = accounts.get(accountId - 1);

        Integer periodId = choosePeriod();
        if (periodId == null) {
            return MSG_WRONG_NUMBER_MENU;
        } else if (periodId == 0) {
            return EMPTY_STRING;
        }
        Period period = Period.getType(periodId - 1);
        if (period == null) {
            return MSG_WRONG_NUMBER_MENU;
        }

        Integer month = null;
        Integer year = null;
        if (period == Period.MONTH) {
            month = chooseMonth(account.getDateOpen());
            if (month == null) {
                return MSG_WRONG_MONTH;
            } else if (month == 0) {
                return EMPTY_STRING;
            }
        }

        if (period == Period.MONTH || period == Period.YEAR) {
            year = chooseYear(account.getDateOpen());
            if (year == null) {
                return MSG_WRONG_YEAR;
            } else if (year == 0) {
                return EMPTY_STRING;
            }
        }

        LocalDate dateFrom = null;
        LocalDate dateTo = null;

        if (period == Period.MONTH) {
            dateFrom = LocalDate.of(year, month, FIRST_DAY);
            dateTo = dateFrom.withDayOfMonth(dateFrom.lengthOfMonth());
        }
        if (period == Period.YEAR) {
            dateFrom = LocalDate.of(year, Month.JANUARY, FIRST_DAY);
            dateTo = dateFrom.withDayOfYear(dateFrom.lengthOfYear());
        }

        Integer formatNumber = chooseFormat();
        if (formatNumber == null) {
            return MSG_WRONG_NUMBER_MENU;
        } else if (formatNumber == 0) {
            return EMPTY_STRING;
        }
        FileFormat fileFormat = FileFormat.values()[formatNumber - 1];
        if (fileFormat == null) {
            return MSG_WRONG_NUMBER_MENU;
        }

        return transactionService.generateStatement(account, dateFrom, dateTo, fileFormat);
    }
}
