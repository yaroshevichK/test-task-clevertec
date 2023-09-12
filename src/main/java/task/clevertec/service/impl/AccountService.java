package task.clevertec.service.impl;

import task.clevertec.entity.Account;
import task.clevertec.entity.User;
import task.clevertec.repository.IDaoAccount;
import task.clevertec.repository.impl.DaoAccount;
import task.clevertec.service.IAccountService;

import java.util.List;

public class AccountService implements IAccountService {
    private final IDaoAccount daoAccount = new DaoAccount();

    @Override
    public List<Account> getAccountsWithBalance(User user) {
        return daoAccount.getUserAccountsWithBalance(user);
    }

    @Override
    public List<Account> getUserAccounts(User user) {
        return daoAccount.getUserAccounts(user);
    }

    @Override
    public Account getOtherUsersAccountByNumber(User user, String number) {
        return daoAccount.getOtherUserAccountByNumber(user, number);
    }

    @Override
    public Account getOtherBankAccountByNumber(Integer bankId, String number) {
        return daoAccount.getOtherBankAccountByNumber(bankId, number);
    }
}
