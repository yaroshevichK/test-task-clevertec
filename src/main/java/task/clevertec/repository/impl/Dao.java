package task.clevertec.repository.impl;

import task.clevertec.repository.IDao;
import task.clevertec.repository.datasource.ConnectionDB;

import java.sql.Connection;

public class Dao<TEntity> implements IDao<TEntity> {
    @Override
    public Connection getConnection() {
        return ConnectionDB.getInstance().getConnection();
    }
}
