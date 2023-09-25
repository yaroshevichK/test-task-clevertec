package task.clevertec.service.impl;

import task.clevertec.entity.Currency;
import task.clevertec.entity.response.CurrencyResponse;
import task.clevertec.mapper.Mapper;
import task.clevertec.mapper.impl.CurrencyMapper;
import task.clevertec.repository.IDaoCurrency;
import task.clevertec.repository.impl.DaoCurrency;
import task.clevertec.service.ICurrencyService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyService implements ICurrencyService {
    private final IDaoCurrency daoCurrency = new DaoCurrency();
    private final Mapper<Currency, CurrencyResponse> currencyMapper =
            new CurrencyMapper();

    @Override
    public List<Currency> getCurrencies() {
        return daoCurrency.getAllCurrencies();
    }


    @Override
    public List<CurrencyResponse> getAllCurrencies() {
        List<Currency> currencies = daoCurrency.getAllCurrencies();
        return Optional.ofNullable(currencies)
                .orElse(new ArrayList<>())
                .stream()
                .map(currencyMapper::entityToResponse)
                .toList();
    }

    @Override
    public CurrencyResponse findCurrencyById(Integer id) {
        Currency currency = daoCurrency.getCurrencyById(id);
        return Optional.ofNullable(currency)
                .map(currencyMapper::entityToResponse)
                .orElse(null);
    }

    @Override
    public boolean saveCurrency(CurrencyResponse currencyResponse) {
        return daoCurrency.saveCurrency(currencyMapper.ResponseToEntity(currencyResponse));
    }

    @Override
    public boolean updateCurrency(CurrencyResponse currencyResponse) {
        return daoCurrency.updateCurrency(currencyMapper.ResponseToEntity(currencyResponse));
    }

    @Override
    public boolean deleteCurrencyById(Integer id) {
        return daoCurrency.deleteCurrencyById(id);
    }

}
