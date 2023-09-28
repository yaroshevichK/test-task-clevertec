package task.clevertec.service;

import task.clevertec.entity.Account;
import task.clevertec.entity.FileFormat;
import task.clevertec.entity.Transaction;

import java.time.LocalDate;

public interface ITransactionService extends IService<Transaction> {
    void createIncomePercent(Account account);

    Transaction createIncomeMoney(Account account, Double amount, String note);

    Transaction createExpenseMoney(Account account, Double amount, String note);

    Transaction createTransferMoney(Account account, Double amount, Account accountTransfer, String note);

    Transaction createTransferOtherMoney(Account account, Double amount, Account accountTransfer, String note);

    String generateStatement(Account account, LocalDate dateFrom, LocalDate dateTo, FileFormat format);
}
