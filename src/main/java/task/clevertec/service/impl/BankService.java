package task.clevertec.service.impl;

import task.clevertec.entity.Bank;
import task.clevertec.repository.IDaoBank;
import task.clevertec.repository.impl.DaoBank;
import task.clevertec.service.IBankService;

import java.util.List;

public class BankService implements IBankService {
    private final IDaoBank daoBank = new DaoBank();

    @Override
    public List<Bank> getBanksWithoutCurrent(Integer bankId) {
        return daoBank.getBanksWithoutCurrent(bankId);
    }
}
