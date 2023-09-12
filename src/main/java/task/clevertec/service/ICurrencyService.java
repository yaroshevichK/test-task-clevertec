package task.clevertec.service;

import task.clevertec.entity.Currency;

import java.util.List;

public interface ICurrencyService extends IService<Currency> {
    List<Currency> getCurrencies();
}
