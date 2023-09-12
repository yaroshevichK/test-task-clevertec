package task.clevertec.service.impl;

import task.clevertec.entity.Currency;
import task.clevertec.repository.IDaoCurrency;
import task.clevertec.repository.impl.DaoCurrency;
import task.clevertec.service.ICurrencyService;

import java.util.List;

public class CurrencyService implements ICurrencyService {
    private final IDaoCurrency daoCurrency = new DaoCurrency();

    @Override
    public List<Currency> getCurrencies() {
        return daoCurrency.getCurrencies();
    }
}
