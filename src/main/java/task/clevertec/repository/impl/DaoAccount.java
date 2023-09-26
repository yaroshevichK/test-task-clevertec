package task.clevertec.repository.impl;

import task.clevertec.entity.Account;
import task.clevertec.entity.Bank;
import task.clevertec.entity.Currency;
import task.clevertec.entity.User;
import task.clevertec.repository.IDaoAccount;
import task.clevertec.repository.datasource.Connector;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static task.clevertec.repository.datasource.Queries.ACCOUNTS_WITH_BALANCE_QUERY;
import static task.clevertec.repository.datasource.Queries.ACCOUNT_BALANCE;
import static task.clevertec.repository.datasource.Queries.ACCOUNT_ID;
import static task.clevertec.repository.datasource.Queries.ACCOUNT_NUMBER;
import static task.clevertec.repository.datasource.Queries.ACCOUNT_OTHER_USERS_QUERY;
import static task.clevertec.repository.datasource.Queries.ACCOUNT_TRANSFER_QUERY;
import static task.clevertec.repository.datasource.Queries.ACCOUNT_USER_ID;
import static task.clevertec.repository.datasource.Queries.ACC_CURRENCY_ID;
import static task.clevertec.repository.datasource.Queries.ACC_CURRENCY_NAME;
import static task.clevertec.repository.datasource.Queries.ACC_DATE_IN_PER;
import static task.clevertec.repository.datasource.Queries.ACC_DATE_OPEN;
import static task.clevertec.repository.datasource.Queries.BANK_BANK_ID;
import static task.clevertec.repository.datasource.Queries.BANK_BANK_NAME;
import static task.clevertec.repository.datasource.Queries.CURRENCY_NAME;
import static task.clevertec.repository.datasource.Queries.FIRST_INDEX;
import static task.clevertec.repository.datasource.Queries.GET_ACCOUNT_BY_ID;
import static task.clevertec.repository.datasource.Queries.GET_ALL_ACCOUNTS;
import static task.clevertec.repository.datasource.Queries.USER_ACCOUNT_QUERY;
import static task.clevertec.repository.datasource.Queries.USER_BANK_ID;
import static task.clevertec.repository.datasource.Queries.USER_BANK_NAME;
import static task.clevertec.repository.datasource.Queries.USER_NAME;
import static task.clevertec.util.Constants.DB_ACCOUNT;

public class DaoAccount extends Dao<Account> implements IDaoAccount {
    private HashMap<String, Object> fillAccountValues(Account account) {
        HashMap<String, Object> values = new HashMap<>();
        values.put(ACCOUNT_NUMBER, account.getNumber());
        if (account.getCurrency() != null) {
            values.put(ACC_CURRENCY_ID, account.getCurrency().getId());
        }
        if (account.getBank() != null) {
            values.put(BANK_BANK_ID, account.getBank().getId());
        }
        if (account.getUser() != null) {
            values.put(ACCOUNT_USER_ID, account.getUser().getId());
        }
        if (account.getDateOpen() != null) {
            values.put(ACC_DATE_OPEN, account.getDateOpen());
        }
        if (account.getBalance() != null) {
            values.put(ACCOUNT_BALANCE, account.getBalance());
        }
        if (account.getDateIncomePercent() != null) {
            values.put(ACC_DATE_IN_PER, account.getDateIncomePercent());
        }

        return values;
    }

    @Override
    public List<Account> getUserAccounts(User user) {
        List<Account> accounts = new ArrayList<>();
        try (
                Connection connection = getConnection();
                PreparedStatement statement = Connector
                        .getStatement(
                                USER_ACCOUNT_QUERY,
                                connection)) {

            if (statement == null) {
                return accounts;
            }
            Connector.setValue(FIRST_INDEX,
                    user.getCurrentBank().getId(),
                    statement);
            Connector.setValue(FIRST_INDEX + 1,
                    user.getId(),
                    statement);

            ResultSet resultSet = Connector.execute(statement);

            if (resultSet != null) {
                while (resultSet.next()) {
                    Date dateInPer = resultSet.getDate(ACC_DATE_IN_PER);
                    LocalDate date = Optional
                            .ofNullable(dateInPer)
                            .map(Date::toLocalDate)
                            .orElse(null);
                    LocalDate dateOpen = Optional
                            .ofNullable(resultSet.getDate(ACC_DATE_OPEN))
                            .map(Date::toLocalDate)
                            .orElse(null);
                    Currency currency = Currency.builder()
                            .id(resultSet.getInt(ACC_CURRENCY_ID))
                            .name(resultSet.getString(ACC_CURRENCY_NAME))
                            .build();
                    accounts.add(Account
                            .builder()
                            .id(resultSet.getInt(ACCOUNT_ID))
                            .number(resultSet.getString(ACCOUNT_NUMBER))
                            .currency(currency)
                            .user(user)
                            .bank(user.getCurrentBank())
                            .dateIncomePercent(date)
                            .dateOpen(dateOpen)
                            .balance(resultSet.getDouble(ACCOUNT_BALANCE))
                            .build()
                    );
                }
            }
        } catch (SQLException e) {
            //add logger
        }

        return accounts;
    }

    @Override
    public List<Account> getUserAccountsWithBalance(User user) {
        List<Account> accounts = new ArrayList<>();
        try (
                Connection connection = getConnection();
                PreparedStatement statement = Connector
                        .getStatement(
                                ACCOUNTS_WITH_BALANCE_QUERY,
                                connection)) {

            if (statement == null) {
                return accounts;
            }

            int index = FIRST_INDEX;
            Connector.setValue(index,
                    user.getCurrentBank().getId(),
                    statement);
            index++;
            Connector.setValue(index,
                    user.getId(),
                    statement);
            index++;
            Connector.setValue(index,
                    LocalDate.now(),
                    statement);

            ResultSet resultSet = Connector.execute(statement);

            if (resultSet != null) {
                while (resultSet.next()) {
                    LocalDate date = Optional
                            .ofNullable(resultSet.getDate(ACC_DATE_IN_PER))
                            .map(Date::toLocalDate)
                            .orElse(null);
                    Currency currency = Currency.builder()
                            .id(resultSet.getInt(ACC_CURRENCY_ID))
                            .name(resultSet.getString(ACC_CURRENCY_NAME))
                            .build();
                    Account account = Account
                            .builder()
                            .id(resultSet.getInt(ACCOUNT_ID))
                            .number(resultSet.getString(ACCOUNT_NUMBER))
                            .currency(currency)
                            .user(user)
                            .bank(user.getCurrentBank())
                            .dateIncomePercent(date)
                            .balance(resultSet.getDouble(ACCOUNT_BALANCE))
                            .build();
                    accounts.add(account);
                }
            }
        } catch (SQLException e) {
            //add logger
        }

        return accounts;
    }

    @Override
    public Account getOtherUserAccountByNumber(User user, String number) {
        try (
                Connection connection = getConnection();
                PreparedStatement statement = Connector
                        .getStatement(
                                ACCOUNT_TRANSFER_QUERY,
                                connection)) {

            if (statement != null) {
                int index = FIRST_INDEX;
                Connector.setValue(index,
                        user.getCurrentBank().getId(),
                        statement);
                index++;
                Connector.setValue(index,
                        number,
                        statement);
                index++;
                Connector.setValue(index,
                        user.getId(),
                        statement);

                ResultSet resultSet = Connector.execute(statement);

                if (resultSet != null && resultSet.next()) {
                    Currency currency = Currency.builder()
                            .id(resultSet.getInt(ACC_CURRENCY_ID))
                            .name(resultSet.getString(CURRENCY_NAME))
                            .build();
                    return Account
                            .builder()
                            .id(resultSet.getInt(ACCOUNT_ID))
                            .number(resultSet.getString(ACCOUNT_NUMBER))
                            .currency(currency)
                            .bank(user.getCurrentBank())
                            .build();
                }
            }
        } catch (SQLException e) {
            //add logger
        }

        return null;
    }

    @Override
    public Account getOtherBankAccountByNumber(Integer bankId, String number) {
        try (
                Connection connection = getConnection();
                PreparedStatement statement = Connector
                        .getStatement(
                                ACCOUNT_OTHER_USERS_QUERY,
                                connection)) {

            if (statement != null) {
                Connector.setValue(FIRST_INDEX,
                        bankId,
                        statement);
                Connector.setValue(FIRST_INDEX + 1,
                        number,
                        statement);

                ResultSet resultSet = Connector.execute(statement);

                if (resultSet != null && resultSet.next()) {
                    Currency currency = Currency.builder()
                            .id(resultSet.getInt(ACC_CURRENCY_ID))
                            .name(resultSet.getString(ACC_CURRENCY_NAME))
                            .build();
                    Bank bank = Bank.builder()
                            .id(resultSet.getInt(BANK_BANK_ID))
                            .name(resultSet.getString(BANK_BANK_NAME))
                            .build();
                    return Account
                            .builder()
                            .id(resultSet.getInt(ACCOUNT_ID))
                            .number(resultSet.getString(ACCOUNT_NUMBER))
                            .currency(currency)
                            .bank(bank)
                            .build();
                }
            }
        } catch (SQLException e) {
            //add logger
        }

        return null;
    }

    @Override
    public List<Account> getAllAccounts() {
        List<Account> accounts = new ArrayList<>();
        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection
                        .prepareStatement(GET_ALL_ACCOUNTS)
        ) {
            if (statement == null) {
                return accounts;
            }

            ResultSet resultSet = Connector.execute(statement);

            if (resultSet != null) {
                while (resultSet.next()) {
                    Date dateInPer = resultSet.getDate(ACC_DATE_IN_PER);
                    LocalDate date = Optional
                            .ofNullable(dateInPer)
                            .map(Date::toLocalDate)
                            .orElse(null);
                    LocalDate dateOpen = Optional
                            .ofNullable(resultSet.getDate(ACC_DATE_OPEN))
                            .map(Date::toLocalDate)
                            .orElse(null);
                    Currency currency = Currency.builder()
                            .id(resultSet.getInt(ACC_CURRENCY_ID))
                            .name(resultSet.getString(ACC_CURRENCY_NAME))
                            .build();
                    Integer bankId = resultSet.getInt(BANK_BANK_ID);
                    String bankName = resultSet.getString(BANK_BANK_NAME);
                    Bank bank = Bank.builder()
                            .id(bankId)
                            .name(bankName)
                            .build();
                    Integer userBankId = resultSet.getInt(USER_BANK_ID);
                    String userBankName = resultSet.getString(USER_BANK_NAME);
                    Bank userBank = Bank.builder()
                            .id(userBankId)
                            .name(userBankName)
                            .build();
                    User user = User.builder()
                            .id(resultSet.getInt(ACCOUNT_USER_ID))
                            .name(resultSet.getString(USER_NAME))
                            .currentBank(userBank)
                            .build();
                    accounts.add(Account
                            .builder()
                            .id(resultSet.getInt(ACCOUNT_ID))
                            .number(resultSet.getString(ACCOUNT_NUMBER))
                            .currency(currency)
                            .bank(bank)
                            .user(user)
                            .dateIncomePercent(date)
                            .dateOpen(dateOpen)
                            .balance(resultSet.getDouble(ACCOUNT_BALANCE))
                            .build()
                    );
                }

            }
        } catch (SQLException e) {
            //add logger
        }

        return accounts;
    }

    @Override
    public Account getAccountById(Integer id) {
        Account account;
        try (
                Connection connection = getConnection();
                PreparedStatement statement = Connector
                        .getStatement(
                                GET_ACCOUNT_BY_ID,
                                connection)) {
            if (statement == null) {
                return null;
            }

            Connector.setValue(FIRST_INDEX, id, statement);
            ResultSet resultSet = Connector.execute(statement);

            if (resultSet != null && resultSet.next()) {
                Date dateInPer = resultSet.getDate(ACC_DATE_IN_PER);
                LocalDate date = Optional
                        .ofNullable(dateInPer)
                        .map(Date::toLocalDate)
                        .orElse(null);
                LocalDate dateOpen = Optional
                        .ofNullable(resultSet.getDate(ACC_DATE_OPEN))
                        .map(Date::toLocalDate)
                        .orElse(null);
                Currency currency = Currency.builder()
                        .id(resultSet.getInt(ACC_CURRENCY_ID))
                        .name(resultSet.getString(ACC_CURRENCY_NAME))
                        .build();
                Integer bankId = resultSet.getInt(BANK_BANK_ID);
                String bankName = resultSet.getString(BANK_BANK_NAME);
                Bank bank = Bank.builder()
                        .id(bankId)
                        .name(bankName)
                        .build();
                Integer userBankId = resultSet.getInt(USER_BANK_ID);
                String userBankName = resultSet.getString(USER_BANK_NAME);
                Bank userBank = Bank.builder()
                        .id(userBankId)
                        .name(userBankName)
                        .build();
                User user = User.builder()
                        .id(resultSet.getInt(ACCOUNT_USER_ID))
                        .name(resultSet.getString(USER_NAME))
                        .currentBank(userBank)
                        .build();
                return Account
                        .builder()
                        .id(resultSet.getInt(ACCOUNT_ID))
                        .number(resultSet.getString(ACCOUNT_NUMBER))
                        .currency(currency)
                        .bank(bank)
                        .user(user)
                        .dateIncomePercent(date)
                        .dateOpen(dateOpen)
                        .balance(resultSet.getDouble(ACCOUNT_BALANCE))
                        .build();
            }
        } catch (SQLException e) {
            //add logger
        }

        return null;
    }

    @Override
    public boolean saveAccount(Account account) {
        HashMap<String, Object> fields = fillAccountValues(account);
        List<Object> values = fields.values().stream().toList();

        return save(DB_ACCOUNT, fields, values);
    }

    @Override
    public boolean updateAccount(Account account) {
        HashMap<String, Object> fields = fillAccountValues(account);
        List<Object> values = fields.values().stream().toList();

        return update(DB_ACCOUNT, account.getId(), fields, values);
    }

    @Override
    public boolean deleteAccountById(Integer id) {
        return deleteById(DB_ACCOUNT, ACCOUNT_ID, id);
    }
}
