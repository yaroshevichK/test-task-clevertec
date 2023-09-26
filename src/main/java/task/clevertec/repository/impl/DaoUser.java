package task.clevertec.repository.impl;

import task.clevertec.entity.Bank;
import task.clevertec.entity.User;
import task.clevertec.repository.IDaoUser;
import task.clevertec.repository.datasource.Connector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static task.clevertec.repository.datasource.Queries.BANK_BANK_ID;
import static task.clevertec.repository.datasource.Queries.BANK_BANK_NAME;
import static task.clevertec.repository.datasource.Queries.FIRST_INDEX;
import static task.clevertec.repository.datasource.Queries.GET_ALL_USERS;
import static task.clevertec.repository.datasource.Queries.GET_USER_BY_ID;
import static task.clevertec.repository.datasource.Queries.USER_BY_NAME_QUERY;
import static task.clevertec.repository.datasource.Queries.USER_CURRENT_BANK;
import static task.clevertec.repository.datasource.Queries.USER_ID;
import static task.clevertec.repository.datasource.Queries.USER_NAME;
import static task.clevertec.util.Constants.DB_USERS;

public class DaoUser extends Dao<User> implements IDaoUser {
    private HashMap<String, Object> fillUserValues(User user) {
        HashMap<String, Object> values = new HashMap<>();
        values.put(USER_NAME, user.getName());
        if (user.getCurrentBank() != null) {
            values.put(USER_CURRENT_BANK, user.getCurrentBank().getId());
        }

        return values;
    }

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

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection
                        .prepareStatement(GET_ALL_USERS)
        ) {
            if (statement == null) {
                return users;
            }

            ResultSet resultSet = Connector.execute(statement);

            if (resultSet != null) {
                while (resultSet.next())
                    users.add(User.builder()
                            .id(resultSet.getInt(USER_ID))
                            .name(resultSet.getString(USER_NAME))
                            .currentBank(Bank
                                    .builder()
                                    .id(resultSet.getInt(BANK_BANK_ID))
                                    .name(resultSet.getString(BANK_BANK_NAME))
                                    .build()
                            )
                            .build());
            }
        } catch (SQLException e) {
            //add logger
        }

        return users;
    }

    @Override
    public User getUserById(Integer id) {
        User user;
        try (
                Connection connection = getConnection();
                PreparedStatement statement = Connector
                        .getStatement(
                                GET_USER_BY_ID,
                                connection)) {
            if (statement == null) {
                return null;
            }

            Connector.setValue(FIRST_INDEX, id, statement);
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

    @Override
    public boolean saveUser(User user) {
        HashMap<String, Object> fields = fillUserValues(user);
        List<Object> values = fields.values().stream().toList();

        return save(DB_USERS, fields, values);
    }

    @Override
    public boolean updateUser(User user) {
        HashMap<String, Object> fields = fillUserValues(user);
        List<Object> values = fields.values().stream().toList();

        return update(DB_USERS, user.getId(), fields, values);
    }

    @Override
    public boolean deleteUserById(Integer id) {
        return deleteById(DB_USERS, USER_ID, id);
    }
}
