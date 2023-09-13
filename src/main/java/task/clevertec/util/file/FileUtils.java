package task.clevertec.util.file;

import org.apache.commons.lang3.StringUtils;
import task.clevertec.entity.Account;
import task.clevertec.entity.Currency;
import task.clevertec.entity.Transaction;
import task.clevertec.entity.TypeTransaction;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static task.clevertec.util.Constants.EMPTY_STRING;
import static task.clevertec.util.Constants.OUT;
import static task.clevertec.util.file.FilePatterns.*;

public class FileUtils {
    private static File getCheckFile(String number) {
        File folder = new File(PATH_CHECK);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        File file = new File(PATH_CHECK + number + TXT_FILE);

        if (!file.exists()) {
            try {
                file.createNewFile();
                return file;
            } catch (IOException e) {
                //add log
                return null;
            }
        }
        return file;
    }

    private static File getStatementFileTxt() {
        File folder = new File(PATH_STMT);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        File file = new File(PATH_STMT + FILE_STMT);

        if (!file.exists()) {
            try {
                file.createNewFile();
                return file;
            } catch (IOException e) {
                //add log
                return null;
            }
        }
        return file;
    }

    private static void writeSymbols(FileWriter fileWriter, String value, int count) {
        try {
            fileWriter.write(value.repeat(count));
        } catch (IOException e) {
            //add log
        }
    }

    private static void writeLabel(FileWriter fileWriter, String label, int length) {
        try {
            int count = (length - label.length()) / 2;
            fileWriter.write(LINE_V);
            writeSymbols(fileWriter, SPACE, count);
            fileWriter.write(label);
            writeSymbols(fileWriter, SPACE, count);
            fileWriter.write(LINE_V);
            fileWriter.write(NEW_STRING);
        } catch (IOException e) {
            //add log
        }
    }

    private static void writeAccLabel(FileWriter fileWriter, String label, int length) {
        try {
            int count = (length - label.length()) / 2;
            writeSymbols(fileWriter, SPACE, count);
            fileWriter.write(label);
            writeSymbols(fileWriter, SPACE, count);
            fileWriter.write(NEW_STRING);
        } catch (IOException e) {
            //add log
        }
    }

    private static void writeString(FileWriter fileWriter, String label, String value) {
        try {
            int count = LENGTH_STR_CHECK - label.length() - value.length();
            fileWriter.write(LINE_V);
            fileWriter.write(label);
            writeSymbols(fileWriter, SPACE, count);
            fileWriter.write(value);
            fileWriter.write(LINE_V);
            fileWriter.write(NEW_STRING);
        } catch (IOException e) {
            //add log
        }
    }

    private static void writeAccString(FileWriter fileWriter, String label, String value) {
        try {
            fileWriter.write(label);
            int count = LINE_V_STMT - label.length();
            writeSymbols(fileWriter, SPACE, count);
            fileWriter.write(LINE_V);
            fileWriter.write(value);
            fileWriter.write(NEW_STRING);
        } catch (IOException e) {
            //add log
        }
    }

    private static void writeHeader(FileWriter fileWriter) {
        try {
            writeSymbols(fileWriter, SPACE, 4);
            fileWriter.write(HEADER_DATE);
            writeSymbols(fileWriter, SPACE, 4);
            fileWriter.write(LINE_V);

            int count = (LENGTH_STR_STMT - 27 - HEADER_NOTE.length()) / 2;
            writeSymbols(fileWriter, SPACE, count);
            fileWriter.write(HEADER_NOTE);
            writeSymbols(fileWriter, SPACE, count);

            fileWriter.write(LINE_V);
            writeSymbols(fileWriter, SPACE, 4);
            fileWriter.write(HEADER_AMOUNT);
            writeSymbols(fileWriter, SPACE, 4);
            fileWriter.write(NEW_STRING);
        } catch (IOException e) {
            //add log
        }
    }

    private static void writeTransaction(FileWriter fileWriter, Transaction transaction, Currency currency) {
        try {
            String date = DATE_STMT_FORMAT.format(transaction.getDateTransaction());
            fileWriter.write(SPACE);
            fileWriter.write(date);
            fileWriter.write(SPACE);
            fileWriter.write(LINE_V);

            TypeTransaction type = transaction.getTypeTransaction();
            String note = transaction.getNote();
            if (StringUtils.isBlank(transaction.getNote())) {
                note = type.getName();
            }
            int count = (LENGTH_STR_STMT - 27 - note.length());
            fileWriter.write(note);
            writeSymbols(fileWriter, SPACE, count);

            String amountStr;
            if (transaction.getTypeTransaction() == TypeTransaction.INCOME) {
                amountStr = BALANCE_FORMAT.format(transaction.getAmount()) +
                        SPACE + currency.getName();
            } else {
                amountStr = BALANCE_FORMAT.format(-transaction.getAmount()) +
                        SPACE + currency.getName();
            }
            fileWriter.write(LINE_V);
            fileWriter.write(amountStr);
            fileWriter.write(NEW_STRING);
        } catch (IOException e) {
            //add log
        }
    }

    public static void saveCheck(Transaction transaction) {
        String number = String.valueOf(transaction.getId());
        LocalDateTime date = transaction.getDateTransaction();
        String bank = transaction.getAccount().getBank().getName();
        String typeTransaction = transaction.getTypeTransaction().getName();

        String bankTo = Optional.ofNullable(transaction.getAccountTransfer())
                .map(acc -> acc.getBank().getName())
                .orElse(EMPTY_STRING);
        String numberAccTo = Optional.ofNullable(transaction.getAccountTransfer())
                .map(Account::getNumber)
                .orElse(EMPTY_STRING);

        String numberAcc = transaction.getAccount().getNumber();
        String currency = transaction.getAccount().getCurrency().getName();
        Double amount = transaction.getAmount();

        File file = getCheckFile(number);

        if (file == null) {
            //исправть на логирование
            OUT.println(FILE_NOT_READ);
            return;
        }

        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(SPACE);
            writeSymbols(fileWriter, LINE, LENGTH_STR_CHECK);
            fileWriter.write(NEW_STRING);

            writeLabel(fileWriter, BANK_CHECK, LENGTH_STR_CHECK);
            writeString(fileWriter, CHECK, number);

            String time = FULL_TIME_FORMAT.format(date);
            writeString(fileWriter, DATE_FORMAT.format(date), time);

            writeString(fileWriter, TYPE_LABEL, typeTransaction);
            writeString(fileWriter, BANK_FROM, bank);
            if (transaction.getAccountTransfer() != null) {
                writeString(fileWriter, BANK_TO, bankTo);
            }
            writeString(fileWriter, ACC_FROM, numberAcc);
            if (transaction.getAccountTransfer() != null) {
                writeString(fileWriter, ACC_TO, numberAccTo);
            }
            String amountStr = SUM_FORMAT.format(amount) + SPACE + currency;
            writeString(fileWriter, AMOUNT, amountStr);

            fileWriter.write(SPACE);
            writeSymbols(fileWriter, LINE, LENGTH_STR_CHECK);

        } catch (IOException e) {
            //исправть на логирование
            OUT.println(FILE_NOT_READ);
        }
    }

    public static void saveStatement(Account account, LocalDateTime dateFrom, LocalDateTime dateTo, List<Transaction> transactions) {
        File file = getStatementFileTxt();

        if (file == null) {
            //исправть на логирование
            OUT.println(FILE_NOT_READ);
            return;
        }

        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(SPACE);
            writeSymbols(fileWriter, LINE, LENGTH_STR_STMT);
            fileWriter.write(NEW_STRING);
            writeAccLabel(fileWriter, STATEMENT, LENGTH_STR_STMT);
            writeAccLabel(fileWriter, account.getBank().getName(),
                    LENGTH_STR_STMT);

            String currency = account.getCurrency().getName();
            String dateOpen = DATE_STMT_FORMAT.format(account.getDateOpen());
            String dateFromStr = DATE_STMT_FORMAT.format(dateFrom);
            String dateToStr = DATE_STMT_FORMAT.format(dateTo);
            String date = DATE_STMT_FORMAT.format(LocalDateTime.now());
            String time = TIME_FORMAT.format(LocalDateTime.now());
            String amountStr = BALANCE_FORMAT.format(account.getBalance()) +
                    SPACE + account.getCurrency().getName();

            String period = String.format(PERIOD_STR, dateFromStr, dateToStr);
            writeAccString(fileWriter, USER, account.getUser().getName());
            writeAccString(fileWriter, ACCOUNT, account.getNumber());
            writeAccString(fileWriter, CURRENCY, currency);
            writeAccString(fileWriter, DATE_OPEN, dateOpen);
            writeAccString(fileWriter, PERIOD, period);
            writeAccString(fileWriter, DATE_STMT, date + COMMA + time);
            writeAccString(fileWriter, BALANCE, amountStr);

            writeHeader(fileWriter);
            writeSymbols(fileWriter, LINE, LENGTH_STR_STMT);
            fileWriter.write(NEW_STRING);

            for (Transaction transaction : transactions) {
                writeTransaction(fileWriter, transaction, account.getCurrency());
            }

        } catch (IOException e) {
            //исправть на логирование
            OUT.println(FILE_NOT_READ);
        }
    }
}
