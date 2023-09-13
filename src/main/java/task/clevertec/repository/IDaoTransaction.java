package task.clevertec.repository;

import task.clevertec.entity.TypeTransaction;
import task.clevertec.entity.Transaction;

import java.time.LocalDateTime;
import java.util.List;

public interface IDaoTransaction extends IDao<TypeTransaction> {
    void saveTransactionPercent(Transaction transaction);

    Transaction saveTransaction(Transaction transaction);

    Transaction saveTransferTransaction(Transaction transaction);

    Transaction saveTransferOtherTransaction(Transaction transaction);

    List<Transaction> generateStatement(Integer accountId, LocalDateTime dateFrom, LocalDateTime dateTo);
}
