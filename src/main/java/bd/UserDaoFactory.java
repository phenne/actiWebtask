package bd;

import dao.UserDao;

import java.sql.SQLException;

public class UserDaoFactory {

    private static UserDaoFactory instance = new UserDaoFactory();

    public static UserDaoFactory getInstance() {
        return instance;
    }

    public static void setInstance(UserDaoFactory instance) {
        UserDaoFactory.instance = instance;
    }

    public UserDao getUserDao() {
        return new UserDao();
    }

    public UserDao getUserDao(Transaction transaction) throws SQLException {
        UserDao userDao = new UserDao();
        userDao.associateTransaction(transaction);
        return userDao;
    }
}