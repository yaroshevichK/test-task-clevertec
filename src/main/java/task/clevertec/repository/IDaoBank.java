package task.clevertec.repository;

import task.clevertec.entity.Bank;

import java.util.List;

public interface IDaoBank extends IDao<Bank> {
    List<Bank> getBanksWithoutCurrent(Integer bankId);

}
