package task.clevertec.repository;

import task.clevertec.entity.Currency;

import java.util.List;

public interface IDaoCurrency extends IDao<Currency> {
    List<Currency> getCurrencies();
}
