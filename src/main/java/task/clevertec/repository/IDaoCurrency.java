package task.clevertec.repository;

import task.clevertec.entity.Currency;

import java.util.List;

public interface IDaoCurrency extends IDao<Currency> {
    List<Currency> getAllCurrencies();

    Currency getCurrencyById(Integer id);

    boolean saveCurrency(Currency currency);

    boolean updateCurrency(Currency currency);

    boolean deleteCurrencyById(Integer id);
}
