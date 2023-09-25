package task.clevertec.service;

import task.clevertec.entity.Currency;
import task.clevertec.entity.response.CurrencyResponse;

import java.util.List;

public interface ICurrencyService extends IService<Currency> {
    List<Currency> getCurrencies();

    List<CurrencyResponse> getAllCurrencies();

    CurrencyResponse findCurrencyById(Integer id);

    boolean saveCurrency(CurrencyResponse currencyResponse);

    boolean updateCurrency(CurrencyResponse currencyResponse);

    boolean deleteCurrencyById(Integer id);
}
