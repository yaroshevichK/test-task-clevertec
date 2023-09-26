package task.clevertec.service.impl;

import task.clevertec.entity.Account;
import task.clevertec.entity.User;
import task.clevertec.entity.response.AccountResponse;
import task.clevertec.mapper.Mapper;
import task.clevertec.mapper.impl.AccountMapper;
import task.clevertec.repository.IDaoAccount;
import task.clevertec.repository.impl.DaoAccount;
import task.clevertec.service.IAccountService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountService implements IAccountService {
    private final IDaoAccount daoAccount = new DaoAccount();
    private final Mapper<Account, AccountResponse> accountMapper =
            new AccountMapper();

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

    @Override
    public List<AccountResponse> getAllAccounts() {
        List<Account> accounts = daoAccount.getAllAccounts();
        return Optional.ofNullable(accounts)
                .orElse(new ArrayList<>())
                .stream()
                .map(accountMapper::entityToResponse)
                .toList();
    }

    @Override
    public AccountResponse findAccountById(Integer id) {
        Account account = daoAccount.getAccountById(id);
        return Optional.ofNullable(account)
                .map(accountMapper::entityToResponse)
                .orElse(null);
    }

    @Override
    public boolean saveAccount(AccountResponse account) {
        return daoAccount.saveAccount(accountMapper.ResponseToEntity(account));
    }

    @Override
    public boolean updateAccount(AccountResponse accountResponse) {
        return daoAccount.updateAccount(accountMapper.ResponseToEntity(accountResponse));
    }

    @Override
    public boolean deleteAccountById(Integer id) {
        return daoAccount.deleteAccountById(id);
    }
}
