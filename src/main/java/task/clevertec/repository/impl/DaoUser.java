package task.clevertec.repository.impl;

import task.clevertec.entity.Bank;
import task.clevertec.entity.User;
import task.clevertec.repository.IDaoUser;
import task.clevertec.repository.datasource.Connector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static task.clevertec.repository.datasource.Queries.BANK_BANK_ID;
import static task.clevertec.repository.datasource.Queries.BANK_BANK_NAME;
import static task.clevertec.repository.datasource.Queries.USER_ID;
import static task.clevertec.repository.datasource.Queries.USER_NAME;
import static task.clevertec.repository.datasource.Queries.FIRST_INDEX;
import static task.clevertec.repository.datasource.Queries.USER_BY_NAME_QUERY;

public class DaoUser extends Dao<User> implements IDaoUser {
    @Override
    public User getUserByUsername(String username) {
        try (
                Connection connection = getConnection();
                PreparedStatement statement = Connector
                        .getStatement(
                                USER_BY_NAME_QUERY,
                                connection)) {

            if (statement == null) {
                return null;
            }
            Connector.setValue(FIRST_INDEX, username, statement);
            ResultSet resultSet = Connector.execute(statement);

            if (resultSet != null && resultSet.next()) {
                return User.builder()
                        .id(resultSet.getInt(USER_ID))
                        .name(resultSet.getString(USER_NAME))
                        .currentBank(Bank
                                .builder()
                                .id(resultSet.getInt(BANK_BANK_ID))
                                .name(resultSet.getString(BANK_BANK_NAME))
                                .build()
                        )
                        .build();
            }
        } catch (SQLException e) {
            //add logger
        }

        return null;
    }
}
