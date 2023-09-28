package task.clevertec.repository.impl;

import org.apache.commons.lang3.StringUtils;
import task.clevertec.entity.Account;
import task.clevertec.entity.Transaction;
import task.clevertec.entity.TypeTransaction;
import task.clevertec.repository.IDaoTransaction;
import task.clevertec.repository.datasource.Connector;
import task.clevertec.repository.datasource.DataQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static task.clevertec.repository.datasource.Queries.ACCOUNT_NUMBER;
import static task.clevertec.repository.datasource.Queries.ACC_CURRENCY_ID;
import static task.clevertec.repository.datasource.Queries.BANK_BANK_ID;
import static task.clevertec.repository.datasource.Queries.FIRST_INDEX;
import static task.clevertec.repository.datasource.Queries.SPACE;
import static task.clevertec.repository.datasource.Queries.TRANSACTIONS_QUERY;
import static task.clevertec.repository.datasource.Queries.TRANSACTION_ACC;
import static task.clevertec.repository.datasource.Queries.TRANSACTION_ACC_TRANSFER;
import static task.clevertec.repository.datasource.Queries.TRANSACTION_AMOUNT;
import static task.clevertec.repository.datasource.Queries.TRANSACTION_DATE;
import static task.clevertec.repository.datasource.Queries.TRANSACTION_ID;
import static task.clevertec.repository.datasource.Queries.TRANSACTION_NOTE;
import static task.clevertec.repository.datasource.Queries.TRANSACTION_TYPE;
import static task.clevertec.repository.datasource.Queries.UPD_ACC_BALANCE_QUERY;
import static task.clevertec.repository.datasource.Queries.UPD_ACC_DATE_BALANCE_QUERY;
import static task.clevertec.util.Constants.DB_ACCOUNT;
import static task.clevertec.util.Constants.DB_TRANSACTION;

public class DaoTransaction extends Dao<TypeTransaction> implements IDaoTransaction {
    private String getInsertQuery(HashMap<String, Object> values, String table) {
        return DataQuery.getInsertQuery(values, table);
    }

    private HashMap<String, Object> fillAccountValues(Account account) {
        HashMap<String, Object> values = new HashMap<>();

        values.put(ACCOUNT_NUMBER, account.getNumber());
        values.put(ACC_CURRENCY_ID, account.getCurrency().getId());
        values.put(BANK_BANK_ID, account.getBank().getId());

        return values;
    }

    private HashMap<String, Object> fillValues(Transaction transaction) {
        HashMap<String, Object> values = new HashMap<>();

        values.put(TRANSACTION_DATE, transaction.getDateTransaction());
        values.put(TRANSACTION_TYPE, transaction.getTypeTransaction());
        values.put(TRANSACTION_ACC, transaction.getAccount().getId());
        values.put(TRANSACTION_AMOUNT, transaction.getAmount());
        if (StringUtils.isNotBlank(transaction.getNote())) {
            values.put(TRANSACTION_NOTE, transaction.getNote());
        }
        TypeTransaction type = transaction.getTypeTransaction();
        if (type == TypeTransaction.TRANSFER ||
                type == TypeTransaction.TRANSFER_OTHER_BANK) {
            values.put(TRANSACTION_ACC_TRANSFER,
                    transaction.getAccountTransfer().getId());
        }

        return values;
    }

    private HashMap<String, Object> fillTransferValues(Transaction transaction) {
        HashMap<String, Object> values = new HashMap<>();

        values.put(TRANSACTION_DATE, transaction.getDateTransaction());
        values.put(TRANSACTION_TYPE, TypeTransaction.INCOME);
        values.put(TRANSACTION_ACC, transaction.getAccountTransfer().getId());
        values.put(TRANSACTION_AMOUNT, transaction.getAmount());
        String username = transaction.getAccount().getUser().getName();
        String note = StringUtils.isBlank(transaction.getNote()) ?
                username : transaction.getNote() + SPACE + username;
        values.put(TRANSACTION_NOTE, note);

        return values;
    }

    @Override
    public void saveTransactionPercent(Transaction transaction) {
        HashMap<String, Object> values = fillValues(transaction);
        Connection connection = getConnection();
        PreparedStatement statement = null;

        try {
            connection.setAutoCommit(false);
            String query = getInsertQuery(values, DB_TRANSACTION);
            statement = Connector.getStatementInsert(query, connection);
            List<Object> queryValue = values.values().stream().toList();
            Connector.setValues(queryValue, statement);
            statement.executeUpdate();

            query = UPD_ACC_DATE_BALANCE_QUERY;
            statement = Connector.getStatementInsert(query, connection);
            int i = FIRST_INDEX;
            Connector.setValue(i, LocalDate.now(), statement);
            i++;
            Double amount = (Double) values.get(TRANSACTION_AMOUNT);
            Connector.setValue(i, amount, statement);
            i++;
            Object accountId = values.get(TRANSACTION_ACC);
            Connector.setValue(i, accountId, statement);
            statement.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                //add logger
            }
        } finally {
            Connector.closeStatement(statement);
            Connector.closeConnection(connection);
        }
    }

    @Override
    public Transaction saveTransaction(Transaction transaction) {
        HashMap<String, Object> values = fillValues(transaction);
        Connection connection = getConnection();
        PreparedStatement statement = null;

        try {
            connection.setAutoCommit(false);
            String query = getInsertQuery(values, DB_TRANSACTION);
            statement = Connector.getStatementInsert(query, connection);
            List<Object> queryValue = values.values().stream().toList();
            Connector.setValues(queryValue, statement);
            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet != null && resultSet.next()) {
                Integer pKey = resultSet.getInt(FIRST_INDEX);
                transaction.setId(pKey);
            }

            query = UPD_ACC_BALANCE_QUERY;
            statement = Connector.getStatementInsert(query, connection);
            Double amount = (Double) values.get(TRANSACTION_AMOUNT);

            if (transaction.getTypeTransaction() == TypeTransaction.EXPENSE) {
                Connector.setValue(FIRST_INDEX, -amount, statement);
            } else {
                Connector.setValue(FIRST_INDEX, amount, statement);
            }

            Object accountId = values.get(TRANSACTION_ACC);
            Connector.setValue(FIRST_INDEX + 1, accountId, statement);
            statement.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            transaction = null;
            try {
                connection.rollback();
            } catch (SQLException ex) {
                //add logger
            }
        } finally {
            Connector.closeStatement(statement);
            Connector.closeConnection(connection);
        }

        return transaction;
    }

    @Override
    public Transaction saveTransferTransaction(Transaction transaction) {
        HashMap<String, Object> values = fillValues(transaction);
        Connection connection = getConnection();
        PreparedStatement statement = null;

        try {
            connection.setAutoCommit(false);
            String query = getInsertQuery(values, DB_TRANSACTION);
            statement = Connector.getStatementInsert(query, connection);
            List<Object> queryValue = values.values().stream().toList();
            Connector.setValues(queryValue, statement);
            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet != null && resultSet.next()) {
                Integer pKey = resultSet.getInt(FIRST_INDEX);
                transaction.setId(pKey);
            }

            query = UPD_ACC_BALANCE_QUERY;
            statement = Connector.getStatementInsert(query, connection);
            Double amount = (Double) values.get(TRANSACTION_AMOUNT);
            Connector.setValue(FIRST_INDEX, -amount, statement);
            Object accountId = values.get(TRANSACTION_ACC);
            Connector.setValue(FIRST_INDEX + 1, accountId, statement);
            statement.executeUpdate();

            values = fillTransferValues(transaction);
            query = getInsertQuery(values, DB_TRANSACTION);
            statement = Connector.getStatementInsert(query, connection);
            queryValue = values.values().stream().toList();
            Connector.setValues(queryValue, statement);
            statement.executeUpdate();

            query = UPD_ACC_BALANCE_QUERY;
            statement = Connector.getStatementInsert(query, connection);
            amount = (Double) values.get(TRANSACTION_AMOUNT);
            Connector.setValue(FIRST_INDEX, amount, statement);
            accountId = transaction.getAccountTransfer().getId();
            Connector.setValue(FIRST_INDEX + 1, accountId, statement);
            statement.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            transaction = null;
            try {
                connection.rollback();
            } catch (SQLException ex) {
                //add logger
            }
        } finally {
            Connector.closeStatement(statement);
            Connector.closeConnection(connection);
        }

        return transaction;
    }


    @Override
    public Transaction saveTransferOtherTransaction(Transaction transaction) {
        Connection connection = getConnection();
        PreparedStatement statement = null;

        try {
            connection.setAutoCommit(false);
            //создание счета клиента
            if (transaction.getAccountTransfer().getId() == null) {
                HashMap<String, Object> accountValues =
                        fillAccountValues(transaction.getAccountTransfer());
                String query = getInsertQuery(accountValues, DB_ACCOUNT);
                statement = Connector.getStatementInsert(query, connection);
                List<Object> queryValue = accountValues.values().stream().toList();
                Connector.setValues(queryValue, statement);
                statement.executeUpdate();

                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet != null && resultSet.next()) {
                    Integer pKey = resultSet.getInt(FIRST_INDEX);
                    transaction.getAccountTransfer().setId(pKey);
                }
            }

            HashMap<String, Object> values = fillValues(transaction);
            String query = getInsertQuery(values, DB_TRANSACTION);
            statement = Connector.getStatementInsert(query, connection);
            List<Object> queryValue = values.values().stream().toList();
            Connector.setValues(queryValue, statement);
            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet != null && resultSet.next()) {
                Integer pKey = resultSet.getInt(FIRST_INDEX);
                transaction.setId(pKey);
            }

            query = UPD_ACC_BALANCE_QUERY;
            statement = Connector.getStatementInsert(query, connection);
            Double amount = (Double) values.get(TRANSACTION_AMOUNT);
            Connector.setValue(FIRST_INDEX, -amount, statement);
            Object accountId = values.get(TRANSACTION_ACC);
            Connector.setValue(FIRST_INDEX + 1, accountId, statement);
            statement.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            transaction = null;
            try {
                connection.rollback();
            } catch (SQLException ex) {
                //add logger
            }
        } finally {
            Connector.closeStatement(statement);
            Connector.closeConnection(connection);
        }

        return transaction;
    }

    @Override
    public List<Transaction> generateStatement(Integer accountId, LocalDateTime dateFrom, LocalDateTime dateTo) {
        List<Transaction> transactions = new ArrayList<>();
        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection
                        .prepareStatement(TRANSACTIONS_QUERY)
        ) {
            if (statement == null) {
                return transactions;
            }

            Connector.setValue(FIRST_INDEX, accountId, statement);
            Connector.setValue(FIRST_INDEX + 1, dateFrom, statement);
            Connector.setValue(FIRST_INDEX + 2, dateTo, statement);
            ResultSet resultSet = Connector.execute(statement);

            if (resultSet != null) {
                while (resultSet.next()) {
                    LocalDateTime date = Optional
                            .ofNullable(resultSet.getTimestamp(TRANSACTION_DATE))
                            .map(Timestamp::toLocalDateTime)
                            .orElse(null);
                    TypeTransaction typeTransaction = TypeTransaction.valueOf(resultSet.getString(TRANSACTION_TYPE));
                    Transaction transaction = Transaction.builder()
                            .id(resultSet.getInt(TRANSACTION_ID))
                            .dateTransaction(date)
                            .amount(resultSet.getDouble(TRANSACTION_AMOUNT))
                            .note(resultSet.getString(TRANSACTION_NOTE))
                            .typeTransaction(typeTransaction)
                            .build();
                    transactions.add(transaction);
                }
            }
        } catch (SQLException e) {
            //add logger
        }
        return transactions;
    }
}
