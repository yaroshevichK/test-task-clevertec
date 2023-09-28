package task.clevertec.repository;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

public interface IDao<TEntity> {
    Connection getConnection();

    boolean save(String nameTable, HashMap<String, Object> fields, List<Object> values);

    boolean update(String nameTable, Integer id,
                   HashMap<String, Object> fields,
                   List<Object> values);

    boolean deleteById(String nameTable, String fieldId, Integer id);
}
