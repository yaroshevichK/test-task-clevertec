package task.clevertec.service;

import task.clevertec.entity.response.BankResponse;
import task.clevertec.entity.Bank;

import java.util.List;

public interface IBankService extends IService<Bank> {
    List<Bank> getBanksWithoutCurrent(Integer bankId);

    List<BankResponse> getAllBanks();

    BankResponse findBankById(Integer id);

    boolean saveBank(BankResponse bankResponse);

    boolean updateBank(BankResponse bankResponse);

    boolean deleteBankById(Integer id);
}
