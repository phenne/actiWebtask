package service;

import dao.UserDao;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class UserServiceTest {

    @Before
    public void init() {
        UserDao userDaoMock = Mockito.mock(UserDao.class);
    }

    @Test
    public void generalTest() {
        UserService userService = new UserService();
    }
}
