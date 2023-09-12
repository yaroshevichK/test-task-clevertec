package task.clevertec.repository;

import task.clevertec.entity.Account;
import task.clevertec.entity.User;

import java.util.List;

public interface IDaoAccount extends IDao<Account> {
    List<Account> getUserAccounts(User user);

    List<Account> getUserAccountsWithBalance(User user);

    Account getOtherUserAccountByNumber(User user, String number);

    Account getOtherBankAccountByNumber(Integer bankId, String number);
}
