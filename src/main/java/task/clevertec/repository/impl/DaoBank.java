package task.clevertec.repository.impl;

import task.clevertec.entity.Bank;
import task.clevertec.repository.IDaoBank;
import task.clevertec.repository.datasource.Connector;
import task.clevertec.repository.datasource.DataQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static task.clevertec.repository.datasource.Queries.BANK_ID;
import static task.clevertec.repository.datasource.Queries.BANK_NAME;
import static task.clevertec.repository.datasource.Queries.BANK_WITHOUT_CURR_QUERY;
import static task.clevertec.repository.datasource.Queries.DELETE_QUERY;
import static task.clevertec.repository.datasource.Queries.FIRST_INDEX;
import static task.clevertec.repository.datasource.Queries.GET_ALL_QUERY;
import static task.clevertec.repository.datasource.Queries.GET_BY_ID_QUERY;
import static task.clevertec.util.Constants.DB_BANK;

public class DaoBank extends Dao<Bank> implements IDaoBank {
    private HashMap<String, Object> fillBankValues(Bank bank) {
        HashMap<String, Object> values = new HashMap<>();
        values.put(BANK_NAME, bank.getName());

        return values;
    }

    @Override
    public List<Bank> getBanksWithoutCurrent(Integer bankId) {
        List<Bank> banks = new ArrayList<>();
        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection
                        .prepareStatement(BANK_WITHOUT_CURR_QUERY)
        ) {
            if (statement == null) {
                return banks;
            }

            Connector.setValue(FIRST_INDEX, bankId, statement);
            ResultSet resultSet = Connector.execute(statement);

            if (resultSet != null) {
                while (resultSet.next())
                    banks.add(Bank
                            .builder()
                            .id(resultSet.getInt(BANK_ID))
                            .name(resultSet.getString(BANK_NAME))
                            .build()
                    );
            }
        } catch (SQLException e) {
            //add logger
        }

        return banks;
    }

    @Override
    public List<Bank> getAllBanks() {
        List<Bank> banks = new ArrayList<>();
        String query = String.format(GET_ALL_QUERY, DB_BANK);
        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection
                        .prepareStatement(query)
        ) {
            if (statement == null) {
                return banks;
            }

            ResultSet resultSet = Connector.execute(statement);

            if (resultSet != null) {
                while (resultSet.next())
                    banks.add(Bank
                            .builder()
                            .id(resultSet.getInt(BANK_ID))
                            .name(resultSet.getString(BANK_NAME))
                            .build()
                    );
            }
        } catch (SQLException e) {
            //add logger
        }

        return banks;
    }

    @Override
    public Bank getBankById(Integer id) {
        Bank bank;
        String query = String.format(GET_BY_ID_QUERY, DB_BANK);
        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection
                        .prepareStatement(query)
        ) {
            if (statement == null) {
                return null;
            }

            Connector.setValue(FIRST_INDEX, id, statement);
            ResultSet resultSet = Connector.execute(statement);

            if (resultSet != null && resultSet.next())
                return Bank
                        .builder()
                        .id(resultSet.getInt(BANK_ID))
                        .name(resultSet.getString(BANK_NAME))
                        .build();
        } catch (SQLException e) {
            //add logger
        }

        return null;
    }

    @Override
    public boolean saveBank(Bank bank) {
        HashMap<String, Object> values = fillBankValues(bank);
        List<Object> queryValue = values.values().stream().toList();

        Connection connection = getConnection();
        PreparedStatement statement = null;

        try {
            connection.setAutoCommit(false);

            String query = DataQuery.getInsertQuery(values, DB_BANK);
            statement = Connector.getStatementInsert(query, connection);
            Connector.setValues(queryValue, statement);
            statement.executeUpdate();

            Integer pKey = null;
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet != null && resultSet.next()) {
                pKey = resultSet.getInt(FIRST_INDEX);
            }

            connection.commit();
            return pKey != null;
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

        return false;
    }

    @Override
    public boolean updateBank(Bank bank) {
        HashMap<String, Object> values = fillBankValues(bank);
        List<Object> queryValue = values.values().stream().toList();

        Connection connection = getConnection();
        PreparedStatement statement = null;

        try {
            connection.setAutoCommit(false);

            String query = DataQuery.getUpdateQuery(values, DB_BANK);
            statement = Connector.getStatement(query, connection);
            Connector.setValues(queryValue, statement);
            Connector.setValue(queryValue.size() + 1, bank.getId(), statement);
            statement.executeUpdate();

            connection.commit();
            return true;
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

        return false;
    }

    @Override
    public boolean deleteBankById(Integer id) {
        String query = String.format(DELETE_QUERY, DB_BANK, BANK_ID);
        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection
                        .prepareStatement(query)
        ) {
            if (statement == null) {
                return false;
            }

            Connector.setValue(FIRST_INDEX, id, statement);
            Connector.execute(statement);

            return true;
        } catch (SQLException e) {
            //add logger
        }

        return false;
    }
}
