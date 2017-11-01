package dao;

import bd.TransactionManager;
import data.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    private Connection connection;

    /**
     * Associate this userDao with given transactionManager, should be called before any operation.
     *
     * @param transactionManager
     * @throws SQLException
     */
    public void associateTransaction(TransactionManager transactionManager) throws SQLException {
        if (transactionManager.getConnection() == null) {
            transactionManager.startTransaction();
        }
        this.connection = transactionManager.getConnection();
    }

    public static void closeQuietly(AutoCloseable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() throws SQLException {
        PreparedStatement statement = null;
        List<User> users = new ArrayList<>();

        try {
            statement = connection.prepareStatement("SELECT * FROM users");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                users.add(new User(
                        resultSet.getInt("user_id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getBoolean("is_manager")));
            }
            return users;
        } finally {
            closeQuietly(statement);
        }
    }

    public void insertUser(User user) throws SQLException {
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement("INSERT INTO users(first_name, last_name, username, password, is_manager) " +
                    "VALUES (?, ?, ?, ?, ?)");

            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getUserName());
            statement.setString(4, user.getPassword());
            statement.setBoolean(5, user.isManager());

            statement.executeUpdate();
        } finally {
            closeQuietly(statement);
        }
    }

    public int updateUser(User user) throws SQLException {
        PreparedStatement statement = null;

        try {
            if (user.getPassword().isEmpty()) {
                statement = connection.prepareStatement("UPDATE users SET first_name = ?, last_name = ?, username = ?, is_manager = ? WHERE user_id = ?");
                statement.setBoolean(4, user.isManager());
                statement.setInt(5, user.getId());
            } else {
                statement = connection.prepareStatement("UPDATE users SET first_name = ?, last_name = ?, username = ?, password = ?, is_manager = ? WHERE user_id = ?");
                statement.setString(4, user.getPassword());
                statement.setBoolean(5, user.isManager());
                statement.setInt(6, user.getId());
            }

            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getUserName());

            return statement.executeUpdate();
        } finally {
            closeQuietly(statement);
        }
    }

    public int deleteUser(int id) throws SQLException {
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement("DELETE FROM users WHERE user_id = ?");
            statement.setInt(1, id);

            return statement.executeUpdate();
        } finally {
            closeQuietly(statement);
        }
    }

    public User getUserById(int id) throws SQLException {

        PreparedStatement statement = null;
        User user = null;

        try {
            statement = connection.prepareStatement("SELECT * FROM users WHERE user_id = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                user = new User(
                        resultSet.getInt("user_id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getBoolean("is_manager"));
            }
            return user;
        } finally {
            closeQuietly(statement);
        }
    }

    public User getUserByUsername(String username) throws SQLException {

        PreparedStatement statement = null;
        User user = null;

        try {
            statement = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                user = new User(
                        resultSet.getInt("user_id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getBoolean("is_manager"));
            }
            return user;
        } finally {
            closeQuietly(statement);
        }
    }
}
