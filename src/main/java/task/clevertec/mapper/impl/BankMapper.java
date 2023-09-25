package task.clevertec.mapper.impl;

import task.clevertec.entity.response.BankResponse;
import task.clevertec.entity.Bank;
import task.clevertec.mapper.Mapper;

public class BankMapper implements Mapper<Bank, BankResponse> {
    @Override
    public Bank ResponseToEntity(BankResponse response) {
        return Bank.builder()
                .id(response.getId())
                .name(response.getName())
                .build();
    }

    @Override
    public BankResponse entityToResponse(Bank entity) {
        return BankResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }
}
