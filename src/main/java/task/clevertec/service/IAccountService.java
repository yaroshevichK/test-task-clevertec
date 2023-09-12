package task.clevertec.service;

import task.clevertec.entity.Account;
import task.clevertec.entity.User;

import java.util.List;

public interface IAccountService extends IService<Account> {
    List<Account> getAccountsWithBalance(User user);

    List<Account> getUserAccounts(User user);

    Account getOtherUsersAccountByNumber(User user, String number);

    Account getOtherBankAccountByNumber(Integer bankId, String number);
}
