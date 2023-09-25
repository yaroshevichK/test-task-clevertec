package task.clevertec.mapper.impl;

import task.clevertec.entity.Currency;
import task.clevertec.entity.response.CurrencyResponse;
import task.clevertec.mapper.Mapper;

public class CurrencyMapper implements Mapper<Currency, CurrencyResponse> {
    @Override
    public Currency ResponseToEntity(CurrencyResponse response) {
        return Currency.builder()
                .id(response.getId())
                .name(response.getName())
                .build();
    }

    @Override
    public CurrencyResponse entityToResponse(Currency entity) {
        return CurrencyResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }
}
