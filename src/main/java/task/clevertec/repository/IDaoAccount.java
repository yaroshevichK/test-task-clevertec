package task.clevertec.repository;

import task.clevertec.entity.Account;
import task.clevertec.entity.User;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

public interface IDaoAccount extends IDao<Account> {
    List<Account> getUserAccounts(User user);

    List<Account> getUserAccountsWithBalance(User user);

    Account getOtherUserAccountByNumber(User user, String number);

    Account getOtherBankAccountByNumber(Integer bankId, String number);

    List<Account> getAllAccounts();

    Account getAccountById(Integer id);

    boolean saveAccount(Account account);

    boolean updateAccount(Account account);

    boolean deleteAccountById(Integer id);

    HashMap<String, Double> generateStatement(Integer id, LocalDateTime dateFrom, LocalDateTime dateTo);
}
