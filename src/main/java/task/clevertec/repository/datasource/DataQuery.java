package task.clevertec.repository.datasource;


import java.util.HashMap;
import java.util.stream.Collectors;

import static task.clevertec.repository.datasource.Queries.BANK_ID;
import static task.clevertec.repository.datasource.Queries.CHAR_VALUE;
import static task.clevertec.repository.datasource.Queries.COMMA;
import static task.clevertec.repository.datasource.Queries.DELIMITER_UPD;
import static task.clevertec.repository.datasource.Queries.DELIM_LAST_UPD;
import static task.clevertec.repository.datasource.Queries.INSERT_QUERY;
import static task.clevertec.repository.datasource.Queries.UPDATE_QUERY;

public class DataQuery {
    public static String getInsertQuery(
            final HashMap<String, Object> mapValues, String nameTable) {

        String fields = String.join(COMMA,
                mapValues.keySet());

        String values = mapValues.keySet()
                .stream()
                .map(s -> CHAR_VALUE)
                .collect(Collectors.joining(COMMA));

        return String.format(INSERT_QUERY,
                nameTable, fields, values);
    }

    public static String getUpdateQuery(
            HashMap<String, Object> mapValues,
            String nameTable) {

        String fields = String.join(DELIMITER_UPD,
                mapValues.keySet()) + DELIM_LAST_UPD;

        return String.format(UPDATE_QUERY,
                nameTable, fields, BANK_ID);
    }
}
