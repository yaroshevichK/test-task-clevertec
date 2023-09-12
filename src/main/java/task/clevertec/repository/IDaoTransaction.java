package task.clevertec.repository;

import task.clevertec.entity.TypeTransaction;
import task.clevertec.entity.Transaction;

public interface IDaoTransaction extends IDao<TypeTransaction> {
    void saveTransactionPercent(Transaction transaction);

    Transaction saveTransaction(Transaction transaction);

    Transaction saveTransferTransaction(Transaction transaction);

    Transaction saveTransferOtherTransaction(Transaction transaction);
}
