package task.clevertec.service;

import task.clevertec.entity.Bank;
import task.clevertec.entity.Currency;

import java.util.List;

public interface IBankService extends IService<Bank> {
    List<Bank> getBanksWithoutCurrent(Integer bankId);
}
