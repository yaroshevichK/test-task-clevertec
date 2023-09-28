package task.clevertec.repository.impl;

import task.clevertec.repository.IDao;
import task.clevertec.repository.datasource.ConnectionDB;
import task.clevertec.repository.datasource.Connector;
import task.clevertec.repository.datasource.DataQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import static task.clevertec.repository.datasource.Queries.DELETE_QUERY;
import static task.clevertec.repository.datasource.Queries.FIRST_INDEX;

public class Dao<TEntity> implements IDao<TEntity> {
    @Override
    public Connection getConnection() {
        return ConnectionDB.getInstance().getConnection();
    }

    @Override
    public boolean save(String nameTable, HashMap<String, Object> fields, List<Object> values) {
        Connection connection = getConnection();
        PreparedStatement statement = null;

        try {
            connection.setAutoCommit(false);

            String query = DataQuery.getInsertQuery(fields, nameTable);
            statement = Connector.getStatementInsert(query, connection);
            Connector.setValues(values, statement);
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
    public boolean update(String nameTable, Integer id,
                          HashMap<String, Object> fields,
                          List<Object> values) {

        Connection connection = getConnection();
        PreparedStatement statement = null;

        try {
            connection.setAutoCommit(false);

            String query = DataQuery.getUpdateQuery(fields, nameTable);
            statement = Connector.getStatement(query, connection);
            Connector.setValues(values, statement);
            Connector.setValue(values.size() + 1, id, statement);
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
    public boolean deleteById(String nameTable, String fieldId, Integer id) {
        String query = String.format(DELETE_QUERY, nameTable, fieldId);
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
