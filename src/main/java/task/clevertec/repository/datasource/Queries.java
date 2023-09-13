package task.clevertec.repository.datasource;

import task.clevertec.util.Configuration;

import static task.clevertec.util.Constants.DB;
import static task.clevertec.util.Constants.NAME_DB;

public class Queries {
    public static final int FIRST_INDEX = 1;
    public static final String COMMA = ",";
    public static final String CHAR_VALUE = "?";
    public static final String SPACE = " ";

    //fields
    public static final String USER_ID = "id";
    public static final String USER_NAME = "name";
    public static final String BANK_BANK_ID = "bank_id";
    public static final String BANK_BANK_NAME = "bank_name";

    public static final String ACC_DATE_IN_PER = "date_income_percent";
    public static final String ACC_DATE_OPEN = "date_open";
    public static final String ACC_CURRENCY_ID = "currency_id";
    public static final String ACC_CURRENCY_NAME = "currency_name";
    public static final String ACCOUNT_ID = "id";
    public static final String ACCOUNT_NUMBER = "number";
    public static final String ACCOUNT_BALANCE = "balance";
    public static final String BANK_ID = "id";
    public static final String BANK_NAME = "name";
    public static final String CURRENCY_ID = "id";
    public static final String CURRENCY_NAME = "name";
    public static final String TRANSACTION_ID = "id";
    public static final String TRANSACTION_DATE = "date_transaction";
    public static final String TRANSACTION_TYPE = "type_transaction";
    public static final String TRANSACTION_ACC = "account_id";
    public static final String TRANSACTION_AMOUNT = "amount";
    public static final String TRANSACTION_NOTE = "note";
    public static final String TRANSACTION_ACC_TRANSFER = "account_transfer";

    //queries
    public static final String GET_CURRENT_DB = "SELECT * FROM " +
            "pg_database where datname='" +
            Configuration.getProperty(DB, NAME_DB) +
            "'";

    public static final String INSERT_QUERY =
            "INSERT INTO %s (%s) VALUES (%s)";

    public static final String USER_BY_NAME_QUERY =
            "SELECT users.id, users.name, " +
                    "bank.id AS bank_id, bank.name AS bank_name " +
                    "FROM users " +
                    "JOIN bank ON users.current_bank = bank.id " +
                    "WHERE users.name = ?";

    public static final String ACCOUNT_TRANSFER_QUERY = "SELECT * FROM account " +
            "JOIN currency c on account.currency_id = c.id " +
            "WHERE account.bank_id = ? AND account.number = ? AND account.user_id <> ?";

    public static final String USER_ACCOUNT_QUERY = "SELECT " +
            "account.id, account.number, account.date_income_percent, " +
            "account.currency_id, currency.name as currency_name, " +
            "account.date_open, account.balance " +
            "FROM account " +
            "LEFT JOIN currency on currency.id = account.currency_id " +
            "WHERE account.bank_id = ? AND account.user_id = ? ORDER BY account.id";

    public static final String ACCOUNTS_WITH_BALANCE_QUERY = "SELECT " +
            "account.id, account.number, account.date_income_percent, " +
            "account.balance, account.currency_id, " +
            "currency.name as currency_name FROM account " +
            "JOIN currency c on account.currency_id = c.id " +
            "WHERE bank_id = ? AND user_id = ? AND balance > 0 " +
            "AND (income_percent < ? OR income_percent is null)";

    public static final String ACCOUNT_OTHER_USERS_QUERY = "SELECT " +
            "account.id, account.number, " +
            "account.currency_id, currency.name as currency_name, " +
            "account.bank_id,bank.name as bank_name FROM account " +
            "LEFT JOIN currency on currency.id = account.currency_id " +
            "LEFT JOIN bank on bank.id = account.bank_id " +
            "WHERE account.bank_id <> ? AND account.number = ?";

    public static final String BANK_WITHOUT_CURR_QUERY =
            "SELECT * FROM bank WHERE id <> ?";

    public static final String CURRENCIES_QUERY = "SELECT * FROM currency";

    public static final String UPD_ACC_DATE_BALANCE_QUERY =
            "UPDATE account SET income_percent = ?, " +
                    "balance = balance + ? WHERE id = ?";

    public static final String UPD_ACC_BALANCE_QUERY =
            "UPDATE account SET balance = balance + ? WHERE id = ?";

    public static final String TRANSACTIONS_QUERY = "SELECT * FROM transaction " +
            "WHERE account_id = ? AND date_transaction between ? AND ? " +
            "ORDER BY date_transaction";
}
