package dao;

import bd.Transaction;

import java.sql.SQLException;

public class UserDaoFactory {

    public static final class UserDaoFactoryHolder {
        public static final UserDaoFactory INSTANCE = new UserDaoFactory();
    }

    public static UserDaoFactory getInstance() {
        return UserDaoFactoryHolder.INSTANCE;
    }

    public UserDao createUserDao() {
        return new UserDao();
    }

    public UserDao createTransactionalUserDao(Transaction transaction) throws SQLException {
        UserDao userDao = new UserDao();
        userDao.associateTransaction(transaction);
        return userDao;
    }
}