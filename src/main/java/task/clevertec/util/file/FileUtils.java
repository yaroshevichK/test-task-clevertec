package task.clevertec.util.file;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import org.apache.commons.lang3.StringUtils;
import task.clevertec.entity.Account;
import task.clevertec.entity.Currency;
import task.clevertec.entity.Transaction;
import task.clevertec.entity.TypeTransaction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static task.clevertec.util.Constants.EMPTY_STRING;
import static task.clevertec.util.Constants.OUT;
import static task.clevertec.util.file.FilePatterns.*;

public class FileUtils {
    private static File getFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            try {
                if (!(file.getParentFile().exists() ||
                        file.getParentFile().mkdirs())) {
                    return null;
                }
                return file.createNewFile() ? file : null;
            } catch (IOException e) {
                //add log
                return null;
            }
        }
        return file;
    }

    private static String getSymbols(String value, int count) {
        return value.repeat(count);
    }

    private static String getLabel(String label, int length) {
        int count = (length - label.length()) / 2;
        return getSymbols(SPACE, count) +
                label +
                getSymbols(SPACE, count);
    }

    private static String getCheckString(String label, String value) {
        int count = LENGTH_STR_CHECK - label.length() - value.length();
        return LINE_V +
                label +
                getSymbols(SPACE, count) +
                value +
                LINE_V;
    }

    private static String getStmtString(String label, String value) {
        int count = LINE_V_STMT - label.length();
        return label +
                getSymbols(SPACE, count) +
                LINE_V +
                value;

    }

    private static String getHeader() {
        int count = (LENGTH_STR_STMT - 27 - HEADER_NOTE.length()) / 2;

        return getSymbols(SPACE, 4) +
                HEADER_DATE +
                getSymbols(SPACE, 4) +
                LINE_V +
                getSymbols(SPACE, count) +
                HEADER_NOTE +
                getSymbols(SPACE, count) +
                LINE_V +
                getSymbols(SPACE, 4) +
                HEADER_AMOUNT +
                getSymbols(SPACE, 4);
    }

    private static void addAccountString(Table table, String label, String value) {
        table.addCell(new Cell()
                .setBorder(Border.NO_BORDER)
                .add(new Paragraph(label)));
        table.addCell(new Cell()
                .setBorder(Border.NO_BORDER)
                .add(new Paragraph(LINE_V + value)));
    }

    private static void addToTxt(FileWriter fileWriter, String value, boolean newString) {
        try {
            fileWriter.write(value);
            if (newString) {
                fileWriter.write(NEW_STRING);
            }
        } catch (IOException e) {
            //add log
        }
    }

    private static void addToPdf(Document document, String value) {
        PdfFont font = null;
        try {
            font = PdfFontFactory.createFont(FONT, CP1251, true);
        } catch (IOException e) {
            //добавить логирование
        }
        if (font == null) {
            document.add(new Paragraph(value));
        } else {
            document.add(new Paragraph(value).setFont(font).setTextAlignment(TextAlignment.CENTER));
        }
    }

    private static void addTransactionTxt(FileWriter fileWriter, Transaction transaction, Currency currency) {
        String date = DATE_STMT_FORMAT.format(transaction.getDateTransaction());
        TypeTransaction type = transaction.getTypeTransaction();
        String note = transaction.getNote();
        if (StringUtils.isBlank(transaction.getNote())) {
            note = type.getName();
        }
        int count = (LENGTH_STR_STMT - 27 - note.length());
        String amountStr;
        if (transaction.getTypeTransaction() == TypeTransaction.INCOME) {
            amountStr = BALANCE_FORMAT.format(transaction.getAmount()) +
                    SPACE + currency.getName();
        } else {
            amountStr = BALANCE_FORMAT.format(-transaction.getAmount()) +
                    SPACE + currency.getName();
        }

        addToTxt(fileWriter, SPACE + date + SPACE + LINE_V, false);
        addToTxt(fileWriter, note, false);
        addToTxt(fileWriter, getSymbols(SPACE, count), false);
        addToTxt(fileWriter, LINE_V, false);
        addToTxt(fileWriter, amountStr, true);
    }

    private static void addTransactionPdf(Transaction transaction, Currency currency, Table table) {
        String date = DATE_STMT_FORMAT.format(transaction.getDateTransaction());
        TypeTransaction type = transaction.getTypeTransaction();
        String note = transaction.getNote();
        if (StringUtils.isBlank(transaction.getNote())) {
            note = type.getName();
        }

        String amountStr;
        if (transaction.getTypeTransaction() == TypeTransaction.INCOME) {
            amountStr = BALANCE_FORMAT.format(transaction.getAmount()) +
                    SPACE + currency.getName();
        } else {
            amountStr = BALANCE_FORMAT.format(-transaction.getAmount()) +
                    SPACE + currency.getName();
        }

        table.addCell(new Cell()
                .setBorder(Border.NO_BORDER)
                .add(new Paragraph(SPACE + date)));
        table.addCell(new Cell()
                .setBorder(Border.NO_BORDER)
                .add(new Paragraph(LINE_V + note)));
        table.addCell(new Cell()
                .setBorder(Border.NO_BORDER)
                .add(new Paragraph(LINE_V + amountStr)));
    }

    public static void saveCheck(Transaction transaction) {
        String number = String.valueOf(transaction.getId());
        LocalDateTime date = transaction.getDateTransaction();
        String time = FULL_TIME_FORMAT.format(date);
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
        String amountStr = SUM_FORMAT.format(amount) + SPACE + currency;

        File file = getFile(PATH_CHECK + number + TXT_FILE);
        if (file == null) {
            //исправть на логирование
            OUT.println(FILE_NOT_READ);
            return;
        }

        try (FileWriter fileWriter = new FileWriter(file)) {
            addToTxt(fileWriter, SPACE + getSymbols(LINE, LENGTH_STR_CHECK), true);

            addToTxt(fileWriter, LINE_V, false);
            addToTxt(fileWriter, getLabel(BANK_CHECK, LENGTH_STR_CHECK), false);
            addToTxt(fileWriter, LINE_V, true);
            addToTxt(fileWriter, getCheckString(CHECK, number), true);
            addToTxt(fileWriter, getCheckString(DATE_FORMAT.format(date), time), true);
            addToTxt(fileWriter, getCheckString(TYPE_LABEL, typeTransaction), true);
            addToTxt(fileWriter, getCheckString(BANK_FROM, bank), true);
            if (transaction.getAccountTransfer() != null) {
                addToTxt(fileWriter, getCheckString(BANK_TO, bankTo), true);
            }
            addToTxt(fileWriter, getCheckString(ACC_FROM, numberAcc), true);
            if (transaction.getAccountTransfer() != null) {
                addToTxt(fileWriter, getCheckString(ACC_TO, numberAccTo), true);
            }

            addToTxt(fileWriter, getCheckString(AMOUNT, amountStr), true);

            addToTxt(fileWriter, SPACE + getSymbols(LINE, LENGTH_STR_CHECK), true);
        } catch (IOException e) {
            //исправть на логирование
            OUT.println(FILE_NOT_READ);
        }
    }

    public static void saveStatementTxt(Account account, LocalDateTime dateFrom, LocalDateTime dateTo, List<Transaction> transactions) {
        File file = getFile(PATH_STMT + FILE_STMT_TXT);
        if (file == null) {
            //исправть на логирование
            OUT.println(FILE_NOT_READ);
            return;
        }

        try (FileWriter fileWriter = new FileWriter(file)) {
            String currency = account.getCurrency().getName();
            String dateOpen = DATE_STMT_FORMAT.format(account.getDateOpen());
            String dateFromStr = DATE_STMT_FORMAT.format(dateFrom);
            String dateToStr = DATE_STMT_FORMAT.format(dateTo);
            String date = DATE_STMT_FORMAT.format(LocalDateTime.now());
            String time = TIME_FORMAT.format(LocalDateTime.now());
            String amountStr = BALANCE_FORMAT.format(account.getBalance()) +
                    SPACE + account.getCurrency().getName();
            String period = String.format(PERIOD_STR, dateFromStr, dateToStr);

            addToTxt(fileWriter, SPACE + getSymbols(LINE, LENGTH_STR_STMT), true);
            addToTxt(fileWriter, getLabel(STATEMENT, LENGTH_STR_STMT), true);
            addToTxt(fileWriter, getLabel(account.getBank().getName(),
                    LENGTH_STR_STMT), true);
            addToTxt(fileWriter, getStmtString(USER, account.getUser().getName()), true);
            addToTxt(fileWriter, getStmtString(ACCOUNT, account.getNumber()), true);
            addToTxt(fileWriter, getStmtString(CURRENCY, currency), true);
            addToTxt(fileWriter, getStmtString(DATE_OPEN, dateOpen), true);
            addToTxt(fileWriter, getStmtString(PERIOD, period), true);
            addToTxt(fileWriter, getStmtString(DATE_STMT, date + COMMA + time), true);
            addToTxt(fileWriter, getStmtString(BALANCE, amountStr), true);
            addToTxt(fileWriter, getHeader(), true);
            addToTxt(fileWriter, getSymbols(LINE, LENGTH_STR_STMT), true);

            for (Transaction transaction : transactions) {
                addTransactionTxt(fileWriter, transaction, account.getCurrency());
            }

        } catch (IOException e) {
            //исправть на логирование
            OUT.println(FILE_NOT_READ);
        }
    }

    public static void saveStatementPdf(Account account, LocalDateTime dateFrom, LocalDateTime dateTo, List<Transaction> transactions) {
        File file = getFile(PATH_STMT + FILE_STMT_PDF);
        if (file == null) {
            //исправть на логирование
            OUT.println(FILE_NOT_READ);
            return;
        }

        float[] colAcc = {1, 1};
        Table tableAcc = new Table(colAcc);
        float[] col = {1, 4, 2};
        Table table = new Table(col);
        setPropTable(table);
        setPropTable(tableAcc);

        try {
            PdfWriter writer = new PdfWriter(String.valueOf(file));
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);

            String currency = account.getCurrency().getName();
            String dateOpen = DATE_STMT_FORMAT.format(account.getDateOpen());
            String dateFromStr = DATE_STMT_FORMAT.format(dateFrom);
            String dateToStr = DATE_STMT_FORMAT.format(dateTo);
            String date = DATE_STMT_FORMAT.format(LocalDateTime.now());
            String time = TIME_FORMAT.format(LocalDateTime.now());
            String amountStr = BALANCE_FORMAT.format(account.getBalance()) +
                    SPACE + account.getCurrency().getName();
            String period = String.format(PERIOD_STR, dateFromStr, dateToStr);

            addToPdf(document, SPACE + getSymbols(LINE, LENGTH_STR_STMT));

            addToPdf(document, STATEMENT);
            addToPdf(document, account.getBank().getName()
            );

            addAccountString(tableAcc, USER, account.getUser().getName());
            addAccountString(tableAcc, ACCOUNT, account.getNumber());
            addAccountString(tableAcc, CURRENCY, currency);
            addAccountString(tableAcc, DATE_OPEN, dateOpen);
            addAccountString(tableAcc, PERIOD, period);
            addAccountString(tableAcc, DATE_STMT, date + COMMA + SPACE + time);
            addAccountString(tableAcc, BALANCE, amountStr);

            document.add(tableAcc);

            table.addCell(new Cell()
                    .setBorder(Border.NO_BORDER)
                    .add(new Paragraph(HEADER_DATE)));
            table.addCell(new Cell()
                    .setBorder(Border.NO_BORDER)
                    .add(new Paragraph(LINE_V + HEADER_NOTE)));
            table.addCell(new Cell()
                    .setBorder(Border.NO_BORDER)
                    .add(new Paragraph(LINE_V + HEADER_AMOUNT)));

            addToPdf(document, getSymbols(LINE, LENGTH_STR_STMT));

            for (Transaction transaction : transactions) {
                addTransactionPdf(transaction, account.getCurrency(), table);
            }
            if (transactions.size() > 0) {
                document.add(table);
            }

            document.close();
        } catch (FileNotFoundException e) {
            //исправть на логирование
            OUT.println(FILE_NOT_READ);
        }
    }

    private static void setPropTable(Table table) {
        try {
            table.setFont(PdfFontFactory.createFont(FONT, CP1251, true));
        } catch (IOException e) {
            //
        }
    }
}
