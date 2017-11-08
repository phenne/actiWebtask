package rpc.callback.chain;

import dao.UserDao;
import data.User;
import org.junit.Before;
import org.junit.Test;
import rpc.error.ActiveUserDeletedException;
import rpc.error.UnknownErrorException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.sql.SQLException;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserDeletedMiddlewareTest extends MiddlewareTest {

    private HttpServletRequest request;
    private HttpSession session;
    private Method method;
    private UserDao userDao;
    private UserDeletedMiddleware middleware;

    @Before
    public void init() {
        request = mock(HttpServletRequest.class);
        session = mock(HttpSession.class);
        method = mock(Method.class);
        userDao = mock(UserDao.class);

        User sessionUser = new User();
        sessionUser.setId(1);

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(sessionUser);

        middleware = new UserDeletedMiddleware(userDao);
    }

    @Test
    public void userDeleted_success() throws Exception {
        when(userDao.getUserById(anyInt())).thenReturn(new User());

        assertTrue(middleware.check(request, method));
    }

    @Test(expected = ActiveUserDeletedException.class)
    public void userDeleted_currentUserNull() throws Exception {
        when(userDao.getUserById(anyInt())).thenReturn(null);

        middleware.check(request, method);
    }

    @Test(expected = UnknownErrorException.class)
    public void userDeleted_sqlException() throws Exception {
        when(userDao.getUserById(anyInt())).thenThrow(SQLException.class);

        middleware.check(request, method);
    }
}
