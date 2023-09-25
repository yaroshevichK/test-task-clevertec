package task.clevertec.service.impl;

import task.clevertec.entity.Bank;
import task.clevertec.entity.response.BankResponse;
import task.clevertec.mapper.Mapper;
import task.clevertec.mapper.impl.BankMapper;
import task.clevertec.repository.IDaoBank;
import task.clevertec.repository.impl.DaoBank;
import task.clevertec.service.IBankService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BankService implements IBankService {
    private final IDaoBank daoBank = new DaoBank();
    private final Mapper<Bank, BankResponse> bankMapper = new BankMapper();

    @Override
    public List<Bank> getBanksWithoutCurrent(Integer bankId) {
        return daoBank.getBanksWithoutCurrent(bankId);
    }

    @Override
    public List<BankResponse> getAllBanks() {
        List<Bank> banks = daoBank.getAllBanks();
        return Optional.ofNullable(banks)
                .orElse(new ArrayList<>())
                .stream()
                .map(bankMapper::entityToResponse)
                .toList();
    }

    @Override
    public BankResponse findBankById(Integer id) {
        Bank bank = daoBank.getBankById(id);
        return Optional.ofNullable(bank)
                .map(bankMapper::entityToResponse)
                .orElse(null);
    }

    @Override
    public boolean saveBank(BankResponse bankResponse) {
        return daoBank.saveBank(bankMapper.ResponseToEntity(bankResponse));
    }

    @Override
    public boolean updateBank(BankResponse bankResponse) {
        return daoBank.updateBank(bankMapper.ResponseToEntity(bankResponse));
    }

    @Override
    public boolean deleteBankById(Integer id) {
        return daoBank.deleteBankById(id);
    }
}
