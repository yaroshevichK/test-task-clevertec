package task.clevertec.command.impl;

import lombok.AllArgsConstructor;
import task.clevertec.entity.Account;
import task.clevertec.entity.User;
import task.clevertec.service.IAccountService;
import task.clevertec.service.ITransactionService;
import task.clevertec.service.impl.AccountService;
import task.clevertec.service.impl.TransactionService;

import java.time.LocalDate;
import java.util.List;

import static task.clevertec.util.Constants.CHECK_PERCENT_TIME;
import static task.clevertec.util.Constants.DATE_INCOME_PERCENT;

@AllArgsConstructor
public class IncomePercent extends Thread {
    private final LocalDate TODAY = LocalDate.now();
    private final IAccountService accountService = new AccountService();
    private final ITransactionService transactionService = new TransactionService();
    private User user;

    @Override
    public void run() {
        while (true) {
            synchronized (this) {
                try {
                    incomePercent();
                    wait(CHECK_PERCENT_TIME);
                } catch (InterruptedException e) {
                    //add log
                    break;
                }
            }
        }
    }

    private void incomePercent() {
        if (TODAY.equals(DATE_INCOME_PERCENT)) {
            List<Account> accounts = accountService.getAccountsWithBalance(user);
            accounts.forEach(transactionService::createIncomePercent);
        }
    }
}
