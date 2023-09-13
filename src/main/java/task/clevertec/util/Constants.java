package task.clevertec.util;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Scanner;

public class Constants {
    //properties
    public static final String PROPERTY_FILE = "application.yaml";
    public static final String DB = "db";
    public static final String INIT = "init";
    public static final String PROPERTIES = "properties";
    public static final String PROP_DB = "database";
    public static final String PROP_TABLES = "tables";
    public static final String URL = "url";
    public static final String USER = "user";
    public static final String PASSWORD = "password";
    public static final String NAME_DB = "nameDb";
    public static final String POOL_SIZE = "maxPoolSize";
    public static final String POOL_STATEMENTS = "maxStatements";
    public static final String PROP_PERCENT = "percent";

    //general
    public static final Scanner CONSOLE = new Scanner(System.in, StandardCharsets.UTF_8);

    public static final PrintWriter OUT = new PrintWriter(System.out, true, StandardCharsets.UTF_8);

    public static final String NOTE_PERCENT = "Начисление процентов";
    public static final LocalDate DATE_INCOME_PERCENT = getEndMonth();

    private static LocalDate getEndMonth() {
        LocalDate today = LocalDate.now();
        return today.withDayOfMonth(today.lengthOfMonth());
    }

    public static final int CHECK_PERCENT_TIME = 30000;
    public static final String EMPTY_STRING = "";
    public static final String ZERO_STRING = "0";
    public static final int FIRST_DAY = 1;


    //menu
    public static final String STR_MENU = "Меню:";
    public static final String STR_SIGN_IN = "1. Авторизация";
    public static final String STR_SIGN_OUT = "0. Выход из приложения";
    public static final String STR_STATEMENT = "%d. Выписка из банка";
    public static final String ACTION_MENU = "%d. %s\n";
    public static final String STR_MENU_CANCEL = "0. Отмена";
    public static final String STR_CANCEL = "Отмена действия";

    //msg
    public static final String MSG_WRONG_INIT = "Ошибка инициализации данных";
    public static final String MSG_WRONG_CONN = "Ошибка подключения к базе данных";
    public static final String MSG_WRONG_READ = "Ошибка чтения файла";
    public static final String MSG_WRONG_NUMBER_MENU = "Не верный пункт меню!";
    public static final String MSG_SIGN_OUT = "Вы вышли из приложения";
    public static final String MSG_WRONG_USER = "Ошибка авторизации";
    public static final String MSG_STATUS_OK = "Выполнено успешно";
    public static final String MSG_STATUS_WRONG = "Операция не выполнена";
    public static final String MSG_WRONG_AMOUNT = "Не верно введена сумма";
    public static final String MSG_WRONG_USER_ACC = "Выбран не существующий счет";
    public static final String MSG_WRONG_BANK = "Не верный номер банка";
    public static final String MSG_WRONG_ACCOUNTS = "Счета отсутствуют";
    public static final String MSG_WRONG_CURRENCY = "Список валют пуст";
    public static final String MSG_WRONG_LIST_BANK = "Список банков пуст";
    public static final String MSG_WRONG_ACC = "Указанный счет клиента отсутствует";
    public static final String MSG_WRONG_MONTH = "Не верно введен месяц";
    public static final String MSG_WRONG_YEAR = "Не верно введен год";
    public static final String MSG_WRONG_RES = "Результат не получен";
    public static final String MSG_WRONG = "Неизвестная ошибка";

    //input message
    public static final String INPUT_NUMBER_MENU = "Введите номер меню:";
    public static final String INPUT_USERNAME = "Введите имя пользователя:";
    public static final String INPUT_ACCOUNT = "Введите номер счета (0 - для отмены):";
    public static final String INPUT_ACCOUNT_TRANSFER = "Введите номер счета получателя (0 - для отмены):";
    public static final String INPUT_DESCRIPTION = "Введите примечание:";
    public static final String INPUT_AMOUNT = "Введите сумму (0 - для отмены):";
    public static final String INPUT_BANK = "Выберите банк:";
    public static final String INPUT_CURRENCY = "Введите номер валюты (0 - для отмены):";
    public static final String INPUT_MONTH = "Введите номер месяца (0 - для отмены):";
    public static final String INPUT_YEAR = "Введите 4 цифры года не более текущего (0 - для отмены):";
    public static final String INPUT_DATE_OPEN = "Дата открытия счета: ";

    //database
    public static final String DB_TRANSACTION = "transaction";
    public static final String DB_ACCOUNT = "account";
}
