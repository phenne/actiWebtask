package bd;

import dao.UserDao;

import java.sql.SQLException;

public class UserDaoFactory {

    private static UserDaoFactory Instance = new UserDaoFactory();

    public static UserDaoFactory getInstance() {
        return Instance;
    }

    public static void setInstance(UserDaoFactory instance) {
        Instance = instance;
    }

    public UserDao getUserDao(Transaction transaction) throws SQLException {
        UserDao userDao = new UserDao();
        userDao.associateTransaction(transaction);
        return userDao;
    }
}
