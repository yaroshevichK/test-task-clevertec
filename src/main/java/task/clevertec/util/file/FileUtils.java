package task.clevertec.util.file;

import task.clevertec.entity.Transaction;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

import static task.clevertec.util.Constants.OUT;

public class FileUtils {
    private static File getFile(String number) {
        File folder = new File(FilePatterns.FILE_PATH);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        File file = new File(FilePatterns.FILE_PATH + number + FilePatterns.TXT_FILE);

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

    private static void writeLabel(FileWriter fileWriter, String label) {
        try {
            int count = (FilePatterns.LENGTH_STR - label.length()) / 2;
            fileWriter.write(FilePatterns.LINE_V);
            writeSymbols(fileWriter, FilePatterns.SPACE, count);
            fileWriter.write(label);
            writeSymbols(fileWriter, FilePatterns.SPACE, count);
            fileWriter.write(FilePatterns.LINE_V);
            fileWriter.write(FilePatterns.NEW_STRING);
        } catch (IOException e) {
            //add log
        }
    }

    private static void writeString(FileWriter fileWriter, String label, String value) {
        try {
            int count = FilePatterns.LENGTH_STR - label.length() - value.length();
            fileWriter.write(FilePatterns.LINE_V);
            fileWriter.write(label);
            writeSymbols(fileWriter, FilePatterns.SPACE, count);
            fileWriter.write(value);
            fileWriter.write(FilePatterns.LINE_V);
            fileWriter.write(FilePatterns.NEW_STRING);
        } catch (IOException e) {
            //add log
        }
    }

    public static void saveCheck(Transaction transaction) {
        String number = String.valueOf(transaction.getId());
        LocalDateTime date = transaction.getDateTransaction();
        String bank = transaction.getAccount().getBank().getName();
        String typeTransaction = transaction.getTypeTransaction().getName();

        String bankTo = transaction.getAccountTransfer().getBank().getName();
        String numberAccTo = transaction.getAccountTransfer().getNumber();

        String numberAcc = transaction.getAccount().getNumber();
        String currency = transaction.getAccount().getCurrency().getName();
        Double amount = transaction.getAmount();

        File file = getFile(number);

        if (file == null) {
            //исправть на логирование
            OUT.println(FilePatterns.FILE_NOT_READ);
            return;
        }

        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(FilePatterns.SPACE);
            writeSymbols(fileWriter, FilePatterns.LINE, FilePatterns.LENGTH_STR);
            fileWriter.write(FilePatterns.NEW_STRING);

            writeLabel(fileWriter, FilePatterns.BANK_CHECK);
            writeString(fileWriter, FilePatterns.CHECK, number);

            String time = FilePatterns.TIME_FORMAT.format(date);
            writeString(fileWriter, FilePatterns.DATE_FORMAT.format(date), time);

            writeString(fileWriter, FilePatterns.TYPE_LABEL, typeTransaction);
            writeString(fileWriter, FilePatterns.BANK_FROM, bank);
            if (transaction.getAccountTransfer() != null) {
                writeString(fileWriter, FilePatterns.BANK_TO, bankTo);
            }
            writeString(fileWriter, FilePatterns.ACC_FROM, numberAcc);
            if (transaction.getAccountTransfer() != null) {
                writeString(fileWriter, FilePatterns.ACC_TO, numberAccTo);
            }
            String amountStr = FilePatterns.SUM_FORMAT.format(amount) + " " + currency;
            writeString(fileWriter, FilePatterns.AMOUNT, amountStr);

            fileWriter.write(FilePatterns.SPACE);
            writeSymbols(fileWriter, FilePatterns.LINE, FilePatterns.LENGTH_STR);

        } catch (IOException e) {
            //исправть на логирование
            OUT.println(FilePatterns.FILE_NOT_READ);
        }
    }
}
