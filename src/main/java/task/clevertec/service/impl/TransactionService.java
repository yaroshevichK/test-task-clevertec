package task.clevertec.service.impl;

import task.clevertec.entity.Account;
import task.clevertec.entity.FileFormat;
import task.clevertec.entity.Transaction;
import task.clevertec.entity.TypeTransaction;
import task.clevertec.repository.IDaoTransaction;
import task.clevertec.repository.impl.DaoTransaction;
import task.clevertec.service.ITransactionService;
import task.clevertec.util.Configuration;
import task.clevertec.util.file.FileUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static task.clevertec.util.Constants.MSG_STATUS_OK;
import static task.clevertec.util.Constants.MSG_STATUS_WRONG;
import static task.clevertec.util.Constants.NOTE_PERCENT;
import static task.clevertec.util.Constants.PROPERTIES;
import static task.clevertec.util.Constants.PROP_PERCENT;

public class TransactionService implements ITransactionService {
    private final IDaoTransaction daoTransaction = new DaoTransaction();

    private Double calculatePercent(Double amount) {
        Integer percent = Configuration.getProperty(PROPERTIES, PROP_PERCENT);
        return Optional.ofNullable(percent)
                .map(value -> Math.round(amount * value) / 100.0)
                .orElse(null);
    }

    private Transaction createTransaction(Account account) {
        return Transaction.builder()
                .dateTransaction(LocalDateTime.now())
                .typeTransaction(TypeTransaction.INCOME)
                .account(account)
                .amount(calculatePercent(account.getBalance()))
                .note(NOTE_PERCENT)
                .build();
    }

    private Transaction createTransaction(Account account, Double amount, Account accountTransfer, String note, TypeTransaction type) {
        return Transaction.builder()
                .dateTransaction(LocalDateTime.now())
                .typeTransaction(type)
                .account(account)
                .amount(amount)
                .accountTransfer(accountTransfer)
                .note(note)
                .build();
    }

    @Override
    public void createIncomePercent(Account account) {
        Transaction transaction = createTransaction(account);
        daoTransaction.saveTransactionPercent(transaction);
    }

    @Override
    public Transaction createIncomeMoney(Account account, Double amount, String note) {
        Transaction transaction = createTransaction(account, amount, null, note, TypeTransaction.INCOME);
        return daoTransaction.saveTransaction(transaction);
    }

    @Override
    public Transaction createExpenseMoney(Account account, Double amount, String note) {
        Transaction transaction = createTransaction(account, amount, null, note, TypeTransaction.EXPENSE);
        return daoTransaction.saveTransaction(transaction);
    }

    @Override
    public Transaction createTransferMoney(Account account, Double amount, Account accountTransfer, String note) {
        Transaction transaction = createTransaction(account, amount, accountTransfer, note, TypeTransaction.TRANSFER);
        return daoTransaction.saveTransferTransaction(transaction);
    }

    @Override
    public Transaction createTransferOtherMoney(Account account, Double amount, Account accountTransfer, String note) {
        Transaction transaction = createTransaction(account, amount, accountTransfer, note, TypeTransaction.TRANSFER_OTHER_BANK);
        return daoTransaction.saveTransferOtherTransaction(transaction);
    }

    @Override
    public String generateStatement(Account account, LocalDate dateFrom, LocalDate dateTo, FileFormat format) {
        LocalDateTime dateTimeFrom = Optional
                .ofNullable(dateFrom)
                .orElse(Optional
                        .ofNullable(account.getDateOpen())
                        .orElse(LocalDate.EPOCH)
                ).atStartOfDay();

        LocalDateTime dateTimeTo = Optional.ofNullable(dateTo)
                .orElse(LocalDate.now()).atTime(LocalTime.MAX);

        List<Transaction> transactions = daoTransaction
                .generateStatement(account.getId(), dateTimeFrom, dateTimeTo);

        if (transactions != null) {
            FileUtils.saveStatement(account,dateTimeFrom,dateTimeTo,transactions);
            return MSG_STATUS_OK;
        }
        return MSG_STATUS_WRONG;
    }

}
