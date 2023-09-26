package task.clevertec.repository.impl;

import task.clevertec.entity.Bank;
import task.clevertec.repository.IDaoBank;
import task.clevertec.repository.datasource.Connector;

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
        HashMap<String, Object> fields = fillBankValues(bank);
        List<Object> values = fields.values().stream().toList();

        return save(DB_BANK, fields, values);
    }

    @Override
    public boolean updateBank(Bank bank) {
        HashMap<String, Object> fields = fillBankValues(bank);
        List<Object> values = fields.values().stream().toList();

        return update(DB_BANK, bank.getId(), fields, values);
    }

    @Override
    public boolean deleteBankById(Integer id) {
        return deleteById(DB_BANK, BANK_ID, id);
    }
}
