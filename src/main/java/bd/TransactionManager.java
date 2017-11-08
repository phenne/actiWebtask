package bd;

import dao.UserDao;

import java.sql.SQLException;

public class TransactionManager {

    private static class Holder {
        public static final TransactionManager INSTANCE = new TransactionManager();
    }

    public static TransactionManager getInstance() {
        return Holder.INSTANCE;
    }

    public UserDao getAssociatedUserDao(Transaction transaction) throws SQLException {
        UserDao userDao = new UserDao();
        userDao.associateTransaction(transaction);
        return userDao;
    }
}
