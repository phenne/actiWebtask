package service;

import dao.UserDao;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class UserServiceTest {

    private UserDao userDao;

    @Before
    public void init() {
        userDao = mock(UserDao.class);
    }

    @Test
    public void getUser() {
        UserService userService = new UserService();

    }

    @Test
    public void generalTest() throws Exception {
        UserService userService = new UserService();


    }
}
