package task.clevertec.repository.impl;

import task.clevertec.entity.Currency;
import task.clevertec.repository.IDaoCurrency;
import task.clevertec.repository.datasource.Connector;
import task.clevertec.repository.datasource.DataQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static task.clevertec.repository.datasource.Queries.CURRENCY_ID;
import static task.clevertec.repository.datasource.Queries.CURRENCY_NAME;
import static task.clevertec.repository.datasource.Queries.DELETE_QUERY;
import static task.clevertec.repository.datasource.Queries.FIRST_INDEX;
import static task.clevertec.repository.datasource.Queries.GET_ALL_QUERY;
import static task.clevertec.repository.datasource.Queries.GET_BY_ID_QUERY;
import static task.clevertec.util.Constants.DB_CURRENCY;

public class DaoCurrency extends Dao<Currency> implements IDaoCurrency {
    private HashMap<String, Object> fillCurrencyValues(Currency currency) {
        HashMap<String, Object> values = new HashMap<>();
        values.put(CURRENCY_NAME, currency.getName());

        return values;
    }

    @Override
    public List<Currency> getAllCurrencies() {
        List<Currency> currencies = new ArrayList<>();
        String query = String.format(GET_ALL_QUERY, DB_CURRENCY);
        try (
                Connection connection = getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(query)
        ) {
            if (statement == null) {
                return currencies;
            }

            ResultSet resultSet = Connector.execute(statement);

            if (resultSet != null) {
                while (resultSet.next())
                    currencies.add(Currency
                            .builder()
                            .id(resultSet.getInt(CURRENCY_ID))
                            .name(resultSet.getString(CURRENCY_NAME))
                            .build()
                    );
            }
        } catch (SQLException e) {
            //add logger
        }

        return currencies;
    }

    @Override
    public Currency getCurrencyById(Integer id) {
        Currency currency;
        String query = String.format(GET_BY_ID_QUERY, DB_CURRENCY);
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
                return Currency
                        .builder()
                        .id(resultSet.getInt(CURRENCY_ID))
                        .name(resultSet.getString(CURRENCY_NAME))
                        .build();
        } catch (SQLException e) {
            //add logger
        }

        return null;
    }

    @Override
    public boolean saveCurrency(Currency currency) {
        HashMap<String, Object> values = fillCurrencyValues(currency);
        List<Object> queryValue = values.values().stream().toList();

        Connection connection = getConnection();
        PreparedStatement statement = null;

        try {
            connection.setAutoCommit(false);

            String query = DataQuery.getInsertQuery(values, DB_CURRENCY);
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
    public boolean updateCurrency(Currency currency) {
        HashMap<String, Object> values = fillCurrencyValues(currency);
        List<Object> queryValue = values.values().stream().toList();

        Connection connection = getConnection();
        PreparedStatement statement = null;

        try {
            connection.setAutoCommit(false);

            String query = DataQuery.getUpdateQuery(values, DB_CURRENCY);
            statement = Connector.getStatement(query, connection);
            Connector.setValues(queryValue, statement);
            Connector.setValue(queryValue.size() + 1, currency.getId(), statement);
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
    public boolean deleteCurrencyById(Integer id) {
        String query = String.format(DELETE_QUERY, DB_CURRENCY, CURRENCY_ID);
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
