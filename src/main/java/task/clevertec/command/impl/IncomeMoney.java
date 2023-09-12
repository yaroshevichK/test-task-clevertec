package task.clevertec.command.impl;

import lombok.AllArgsConstructor;
import task.clevertec.command.Command;
import task.clevertec.entity.Account;
import task.clevertec.entity.Transaction;
import task.clevertec.entity.User;
import task.clevertec.service.IAccountService;
import task.clevertec.service.ITransactionService;
import task.clevertec.service.impl.AccountService;
import task.clevertec.service.impl.TransactionService;
import task.clevertec.util.file.FileUtils;
import task.clevertec.util.Converter;

import java.util.List;

import static task.clevertec.util.Constants.ACTION_MENU;
import static task.clevertec.util.Constants.CONSOLE;
import static task.clevertec.util.Constants.EMPTY_STRING;
import static task.clevertec.util.Constants.INPUT_AMOUNT;
import static task.clevertec.util.Constants.INPUT_DESCRIPTION;
import static task.clevertec.util.Constants.INPUT_ACCOUNT;
import static task.clevertec.util.Constants.MSG_STATUS_OK;
import static task.clevertec.util.Constants.MSG_STATUS_WRONG;
import static task.clevertec.util.Constants.MSG_WRONG_ACCOUNTS;
import static task.clevertec.util.Constants.MSG_WRONG_AMOUNT;
import static task.clevertec.util.Constants.MSG_WRONG_USER_ACC;
import static task.clevertec.util.Constants.OUT;

@AllArgsConstructor
public class IncomeMoney implements Command {
    private final ITransactionService transactionService = new TransactionService();
    private final IAccountService accountService = new AccountService();
    private User user;

    private String getDescription() {
        CONSOLE.nextLine();
        OUT.print(INPUT_DESCRIPTION);
        OUT.flush();
        return CONSOLE.nextLine();
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

    private Double getAmount() {
        Double amount;
        do {
            OUT.print(INPUT_AMOUNT);
            OUT.flush();
            amount = Converter.strToDouble(CONSOLE.next());

            if (amount == null) {
                OUT.println(MSG_WRONG_AMOUNT);
                continue;
            }

            if (amount == 0) {
                break;
            }

            if (amount < 0) {
                OUT.println(MSG_WRONG_AMOUNT);
            } else {
                break;
            }
        } while (true);

        return amount;
    }

    private void printAccounts(List<Account> accounts) {
        for (int i = 0; i < accounts.size(); i++) {
            OUT.printf(ACTION_MENU, i + 1, accounts.get(i).getNumber());
        }
    }

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

        Double amount = getAmount();
        if (amount == null) {
            return MSG_WRONG_AMOUNT;
        } else if (amount == 0) {
            return EMPTY_STRING;
        }

        String note = getDescription();

        try {
            Account account = accounts.get(accountId - 1);
            Transaction transaction = transactionService.createIncomeMoney(account, amount, note);
            if (transaction != null) {
                FileUtils.saveCheck(transaction);
                return MSG_STATUS_OK;
            }
        } catch (Exception e) {
            //add logger
        }
        return MSG_STATUS_WRONG;
    }
}

