package bd;

import java.sql.Connection;
import java.sql.SQLException;

public class Transaction implements AutoCloseable {

    private Connection connection;

    /**
     * Initializing connection for dao classes
     *
     * @throws SQLException
     */
    public void startTransaction() throws SQLException {
        connection = DataSourceGetter.getDataSource().getConnection();
        connection.setAutoCommit(false);
    }

    /**
     * Commit the connection and close it
     *
     * @throws SQLException
     */
    public void commit() throws SQLException {
        try {
            connection.commit();
        } finally {
            connection.close();
        }
    }

    /**
     * Rollback the connection and close it
     *
     * @throws SQLException
     */
    public void rollback() throws SQLException {
        try {
            connection.rollback();
        } finally {
            connection.close();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

