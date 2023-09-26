package task.clevertec.mapper.impl;

import task.clevertec.entity.Currency;
import task.clevertec.entity.response.CurrencyResponse;
import task.clevertec.mapper.Mapper;

public class CurrencyMapper implements Mapper<Currency, CurrencyResponse> {
    @Override
    public Currency ResponseToEntity(CurrencyResponse currencyResponse) {
        return Currency.builder()
                .id(currencyResponse.getId())
                .name(currencyResponse.getName())
                .build();
    }

    @Override
    public CurrencyResponse entityToResponse(Currency currency) {
        return CurrencyResponse.builder()
                .id(currency.getId())
                .name(currency.getName())
                .build();
    }
}
