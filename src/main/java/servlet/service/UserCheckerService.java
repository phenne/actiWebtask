package servlet.service;

import bd.Transaction;
import bd.UserDaoFactory;
import dao.UserDao;
import data.User;
import servlet.error.InvalidPasswordException;
import servlet.error.InvalidUsernameException;

import java.sql.SQLException;

public class UserCheckerService {

    private static UserCheckerService instance = new UserCheckerService();

    public static UserCheckerService getInstance() {
        return instance;
    }

    public static void setInstance(UserCheckerService instance) {
        UserCheckerService.instance = instance;
    }

    public User check(String userName, String password, Transaction transaction) throws SQLException, InvalidUsernameException, InvalidPasswordException {
        UserDao userDao = UserDaoFactory.getInstance().getUserDao(transaction);
        User user = userDao.getUserByUsername(userName);

        if (user == null) {
            throw new InvalidUsernameException();
        } else if (!user.getPassword().equals(password)) {
            throw new InvalidPasswordException();
        }
        return user;
    }
}
