package task.clevertec.repository.datasource;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Connector {
    public static PreparedStatement getStatement(
            final String query,
            final Connection connection) {

        if (connection != null) {
            try {
                return connection.prepareStatement(query);
            } catch (SQLException e) {
                //add logger
            }
        }
        return null;
    }

    public static PreparedStatement getStatementInsert(
            final String query,
            final Connection connection) {

        if (connection != null) {
            try {
                return connection.prepareStatement(query,
                        Statement.RETURN_GENERATED_KEYS);
            } catch (SQLException e) {
                //add logger
            }
        }
        return null;
    }

    public static ResultSet execute(final PreparedStatement statement) {
        try {
            return statement.executeQuery();
        } catch (SQLException e) {
            //add logger
        }
        return null;
    }

    public static void commit(final Connection connection,
                              final PreparedStatement statement) {
        try {
            statement.executeUpdate();
        } catch (SQLException e) {
            //add logger
            try {
                connection.rollback();
            } catch (SQLException ex) {
                //add logger
            }
        }
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                //add logger
            }
        }
    }

    public static void closeStatement(PreparedStatement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                //add logger
            }
        }
    }


    public static <T> void setValue(
            final int index,
            final T value,
            final PreparedStatement statement
    ) {

        try {
            if (value instanceof Integer) {
                statement.setInt(index, (Integer) value);
            } else if (value instanceof Double) {
                statement.setDouble(index, (Double) value);
            } else if (value instanceof LocalDateTime) {
                Timestamp time = Timestamp.valueOf((LocalDateTime) value);
                statement.setTimestamp(index, time);
            } else if (value instanceof LocalDate) {
                statement.setDate(index, Date.valueOf((LocalDate) value));
            } else {
                statement.setString(index, String.valueOf(value));
            }
        } catch (SQLException e) {
            //add logger
        }
    }

    public static void setValues(
            final List<Object> values,
            final PreparedStatement statement
    ) {
        int i = 1;
        for (Object value : values) {
            setValue(i, value, statement);
            i++;
        }
    }
}
