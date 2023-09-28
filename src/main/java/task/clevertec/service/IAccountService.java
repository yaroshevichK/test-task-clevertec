package task.clevertec.service;

import task.clevertec.entity.Account;
import task.clevertec.entity.User;
import task.clevertec.entity.response.AccountResponse;

import java.time.LocalDate;
import java.util.List;

public interface IAccountService extends IService<Account> {
    List<Account> getAccountsWithBalance(User user);

    List<Account> getUserAccounts(User user);

    Account getOtherUsersAccountByNumber(User user, String number);

    Account getOtherBankAccountByNumber(Integer bankId, String number);

    List<AccountResponse> getAllAccounts();

    AccountResponse findAccountById(Integer id);

    boolean saveAccount(AccountResponse account);

    boolean updateAccount(AccountResponse accountResponse);

    boolean deleteAccountById(Integer id);

    boolean generateStatement(AccountResponse account, LocalDate dateFrom, LocalDate dateTo);
}
