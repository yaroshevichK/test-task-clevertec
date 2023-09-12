package task.clevertec.repository.impl;

import task.clevertec.entity.Currency;
import task.clevertec.repository.IDaoCurrency;
import task.clevertec.repository.datasource.Connector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static task.clevertec.repository.datasource.Queries.CURRENCIES_QUERY;
import static task.clevertec.repository.datasource.Queries.CURRENCY_ID;
import static task.clevertec.repository.datasource.Queries.CURRENCY_NAME;

public class DaoCurrency extends Dao<Currency> implements IDaoCurrency {
    @Override
    public List<Currency> getCurrencies() {
        List<Currency> currencies = new ArrayList<>();
        try (
                Connection connection = getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(CURRENCIES_QUERY)
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
}
