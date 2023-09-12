package task.clevertec.repository;

import java.sql.Connection;

public interface IDao<TEntity> {
    Connection getConnection();
}
