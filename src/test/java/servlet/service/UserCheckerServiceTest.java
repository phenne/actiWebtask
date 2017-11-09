package servlet.service;

import bd.Transaction;
import bd.UserDaoFactory;
import dao.UserDao;
import data.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import servlet.error.InvalidPasswordException;
import servlet.error.InvalidUsernameException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserCheckerServiceTest {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    private Transaction transaction;
    private UserDao userDao;
    private UserCheckerService service;

    @Before
    public void init() throws Exception {
        transaction = mock(Transaction.class);
        userDao = mock(UserDao.class);
        service = new UserCheckerService();

        UserDaoFactory factory = mock(UserDaoFactory.class);
        UserDaoFactory.setInstance(factory);
        when(factory.getUserDao(any(Transaction.class))).thenReturn(userDao);
    }

    @Test(expected = InvalidUsernameException.class)
    public void check_userNull() throws Exception {
        when(userDao.getUserByUsername(USERNAME)).thenReturn(null);

        service.check(USERNAME, PASSWORD, transaction);
    }

    @Test(expected = InvalidPasswordException.class)
    public void check_passwordNotEquals() throws Exception {
        User user = new User();
        user.setPassword("wrongPassword");

        when(userDao.getUserByUsername(USERNAME)).thenReturn(user);

        service.check(USERNAME, PASSWORD, transaction);
    }

    @Test
    public void check_success() throws Exception {
        User user = new User();
        user.setPassword(PASSWORD);

        when(userDao.getUserByUsername(USERNAME)).thenReturn(user);

        User check = service.check(USERNAME, PASSWORD, transaction);

        Assert.assertSame(check, user);
    }
}
