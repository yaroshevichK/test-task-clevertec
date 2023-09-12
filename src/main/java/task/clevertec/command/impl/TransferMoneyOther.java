package task.clevertec.command.impl;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import task.clevertec.command.Command;
import task.clevertec.entity.Account;
import task.clevertec.entity.Bank;
import task.clevertec.entity.Currency;
import task.clevertec.entity.Transaction;
import task.clevertec.entity.User;
import task.clevertec.service.IAccountService;
import task.clevertec.service.IBankService;
import task.clevertec.service.ICurrencyService;
import task.clevertec.service.ITransactionService;
import task.clevertec.service.impl.AccountService;
import task.clevertec.service.impl.BankService;
import task.clevertec.service.impl.CurrencyService;
import task.clevertec.service.impl.TransactionService;
import task.clevertec.util.file.FileUtils;
import task.clevertec.util.Converter;

import java.util.List;

import static task.clevertec.util.Constants.ACTION_MENU;
import static task.clevertec.util.Constants.CONSOLE;
import static task.clevertec.util.Constants.EMPTY_STRING;
import static task.clevertec.util.Constants.INPUT_ACCOUNT;
import static task.clevertec.util.Constants.INPUT_ACCOUNT_TRANSFER;
import static task.clevertec.util.Constants.INPUT_AMOUNT;
import static task.clevertec.util.Constants.INPUT_BANK;
import static task.clevertec.util.Constants.INPUT_CURRENCY;
import static task.clevertec.util.Constants.INPUT_DESCRIPTION;
import static task.clevertec.util.Constants.MSG_STATUS_OK;
import static task.clevertec.util.Constants.MSG_STATUS_WRONG;
import static task.clevertec.util.Constants.MSG_WRONG_ACCOUNTS;
import static task.clevertec.util.Constants.MSG_WRONG_AMOUNT;
import static task.clevertec.util.Constants.MSG_WRONG_BANK;
import static task.clevertec.util.Constants.MSG_WRONG_CURRENCY;
import static task.clevertec.util.Constants.MSG_WRONG_LIST_BANK;
import static task.clevertec.util.Constants.MSG_WRONG_USER_ACC;
import static task.clevertec.util.Constants.OUT;
import static task.clevertec.util.Constants.ZERO_STRING;

@AllArgsConstructor
public class TransferMoneyOther implements Command {
    private final ITransactionService transactionService = new TransactionService();
    private final IAccountService accountService = new AccountService();
    private final ICurrencyService currencyService = new CurrencyService();
    private final IBankService bankService = new BankService();
    private User user;

    private void printAccounts(List<Account> accounts) {
        for (int i = 0; i < accounts.size(); i++) {
            OUT.printf(ACTION_MENU, i + 1, accounts.get(i).getNumber());
        }
    }

    private void printCurrencies(List<Currency> currencies) {
        for (int i = 0; i < currencies.size(); i++) {
            OUT.printf(ACTION_MENU, i + 1, currencies.get(i).getName());
        }
    }

    private void printBanks(List<Bank> banks) {
        for (int i = 0; i < banks.size(); i++) {
            OUT.printf(ACTION_MENU, i + 1, banks.get(i).getName());
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

    private Integer chooseCurrency(List<Currency> currencies) {
        Integer currencyId;
        do {
            printCurrencies(currencies);

            OUT.print(INPUT_CURRENCY);
            OUT.flush();
            currencyId = Converter.strToInt(CONSOLE.next());

            if (currencyId == null) {
                OUT.println(MSG_WRONG_CURRENCY);
                continue;
            }
            if (currencyId == 0) {
                break;
            }
            if (currencyId < 0 || currencyId > currencies.size()) {
                OUT.println(MSG_WRONG_CURRENCY);
            } else {
                break;
            }
        } while (true);

        return currencyId;
    }

    private Integer chooseBank(List<Bank> banks) {
        Integer bankId;
        do {
            printBanks(banks);

            OUT.print(INPUT_BANK);
            OUT.flush();
            bankId = Converter.strToInt(CONSOLE.next());

            if (bankId == null) {
                OUT.println(MSG_WRONG_BANK);
                continue;
            }
            if (bankId == 0) {
                break;
            }
            if (bankId < 0 || bankId > banks.size()) {
                OUT.println(MSG_WRONG_BANK);
            } else {
                break;
            }
        } while (true);

        return bankId;
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

    private String getDescription() {
        CONSOLE.nextLine();
        OUT.print(INPUT_DESCRIPTION);
        OUT.flush();
        return CONSOLE.nextLine();
    }


    public String execute() {
        List<Account> accounts = accountService.getUserAccounts(user);
        List<Currency> currencies = currencyService.getCurrencies();
        List<Bank> banks = bankService.getBanksWithoutCurrent(user.getCurrentBank().getId());

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

        Double amount = getAmount();
        if (amount == null) {
            return MSG_WRONG_AMOUNT;
        } else if (amount == 0) {
            return EMPTY_STRING;
        }

        Account accountTransfer;
        String number;
        do {
            CONSOLE.nextLine();
            OUT.print(INPUT_ACCOUNT_TRANSFER);
            OUT.flush();
            number = CONSOLE.next();

            if (ZERO_STRING.equals(number)) {
                return EMPTY_STRING;
            }
            if (StringUtils.isNotBlank(number)) {
                accountTransfer = accountService.getOtherBankAccountByNumber(user.getCurrentBank().getId(), number);
                break;
            }
        } while (true);

        if (accountTransfer == null) {
            accountTransfer = Account.builder()
                    .number(number)
                    .build();

            if (currencies.size() == 0) {
                return MSG_WRONG_CURRENCY;
            }
            Integer currencyId = chooseCurrency(currencies);
            if (currencyId == null) {
                return MSG_WRONG_CURRENCY;
            } else if (currencyId == 0) {
                return EMPTY_STRING;
            }
            accountTransfer.setCurrency(currencies.get(currencyId - 1));

            if (banks.size() == 0) {
                return MSG_WRONG_LIST_BANK;
            }
            Integer bankId = chooseBank(banks);
            if (bankId == null) {
                return MSG_WRONG_BANK;
            } else if (bankId == 0) {
                return EMPTY_STRING;
            }
            accountTransfer.setBank(banks.get(currencyId - 1));
        }

        String note = getDescription();

        try {
            Transaction transaction = transactionService.createTransferOtherMoney(account, amount, accountTransfer, note);
            if (transaction != null) {
                FileUtils.saveCheck(transaction);
                return MSG_STATUS_OK;
            }
        } catch (Exception e) {
            //add log
        }
        return MSG_STATUS_WRONG;
    }
}

