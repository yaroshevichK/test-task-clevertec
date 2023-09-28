package task.clevertec.mapper.impl;

import task.clevertec.entity.response.BankResponse;
import task.clevertec.entity.Bank;
import task.clevertec.mapper.Mapper;

public class BankMapper implements Mapper<Bank, BankResponse> {
    @Override
    public Bank ResponseToEntity(BankResponse bankResponse) {
        return Bank.builder()
                .id(bankResponse.getId())
                .name(bankResponse.getName())
                .build();
    }

    @Override
    public BankResponse entityToResponse(Bank bank) {
        return BankResponse.builder()
                .id(bank.getId())
                .name(bank.getName())
                .build();
    }
}
