package task.clevertec.util.file;

import task.clevertec.util.Configuration;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;

import static task.clevertec.util.Constants.PROPERTIES;

public class FilePatterns {
    public static final String FILE_NOT_READ = "File not read";
    public static final int LENGTH_STR = 40;
    public static final String NEW_STRING = "\n";
    public static final String LINE_V = "|";
    public static final String SPACE = " ";
    public static final String LINE = "-";
    public static final String TYPE_LABEL = "Тип транзакции:";
    public static final String BANK_FROM = "Банк отправителя:";
    public static final String BANK_TO = "Банк получателя:";
    public static final String ACC_FROM = "Счет отправителя:";
    public static final String ACC_TO = "Счет получателя:";

    public static final String BANK_CHECK = "Банковский чек";
    public static final String CHECK = "Чек:";
    public static final String AMOUNT = "Сумма:";

    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");
    public static final DecimalFormat SUM_FORMAT = new DecimalFormat("#.00#");
    public static final String FILE_PATH = Configuration.getProperty(PROPERTIES, "check-path");
    public static final String TXT_FILE = ".txt";
}
