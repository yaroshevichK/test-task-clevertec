package task.clevertec.repository.impl;

import task.clevertec.entity.Bank;
import task.clevertec.repository.IDaoBank;
import task.clevertec.repository.datasource.Connector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static task.clevertec.repository.datasource.Queries.BANK_ID;
import static task.clevertec.repository.datasource.Queries.BANK_NAME;
import static task.clevertec.repository.datasource.Queries.BANK_WITHOUT_CURR_QUERY;
import static task.clevertec.repository.datasource.Queries.FIRST_INDEX;

public class DaoBank extends Dao<Bank> implements IDaoBank {
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
}
