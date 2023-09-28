package task.clevertec.repository;

import task.clevertec.entity.Bank;

import java.util.List;

public interface IDaoBank extends IDao<Bank> {
    List<Bank> getBanksWithoutCurrent(Integer bankId);

    List<Bank> getAllBanks();

    Bank getBankById(Integer id);

    boolean saveBank(Bank bank);

    boolean updateBank(Bank bank);

    boolean deleteBankById(Integer id);
}
