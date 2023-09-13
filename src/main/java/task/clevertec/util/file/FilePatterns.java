package task.clevertec.util.file;

import task.clevertec.util.Configuration;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;

import static task.clevertec.util.Constants.PROPERTIES;

public class FilePatterns {
    public static final String FILE_NOT_READ = "File not read";
    public static final int LENGTH_STR_CHECK = 40;
    public static final int LENGTH_STR_STMT = 70;
    public static final int LINE_V_STMT = 30;
    public static final String NEW_STRING = "\n";
    public static final String LINE_V = "|";
    public static final String SPACE = " ";
    public static final String LINE = "-";
    public static final String COMMA = ", ";
    public static final String TYPE_LABEL = "Тип транзакции:";
    public static final String BANK_FROM = "Банк отправителя:";
    public static final String BANK_TO = "Банк получателя:";
    public static final String ACC_FROM = "Счет отправителя:";
    public static final String ACC_TO = "Счет получателя:";

    public static final String BANK_CHECK = "Банковский чек";
    public static final String CHECK = "Чек:";
    public static final String AMOUNT = "Сумма:";
    public static final String STATEMENT = "Выписка";
    public static final String USER="Клиент";
    public static final String ACCOUNT="Счет";
    public static final String CURRENCY="Валюта";
    public static final String DATE_OPEN="Дата открытия";
    public static final String PERIOD="Период";
    public static final String DATE_STMT="Дата и время формирования";
    public static final String BALANCE="Остаток";
    public static final String HEADER_DATE="Дата";
    public static final String HEADER_NOTE="Примечание";
    public static final String HEADER_AMOUNT="Сумма";

    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public static final DateTimeFormatter FULL_TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");
    public static final DateTimeFormatter DATE_STMT_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    public static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");
    public static final DecimalFormat SUM_FORMAT = new DecimalFormat("#.00#");
    public static final DecimalFormat BALANCE_FORMAT = new DecimalFormat("#.00");
    public static final String PERIOD_STR ="%s - %s";
    public static final String PATH_CHECK = Configuration.getProperty(PROPERTIES, "check-path");
    public static final String PATH_STMT = Configuration.getProperty(PROPERTIES, "statement-path");
    public static final String PATH_STMT_MONEY = Configuration.getProperty(PROPERTIES, "statement-money-path");
    public static final String TXT_FILE = ".txt";
    public static final String FILE_STMT = "statement.txt";
}
