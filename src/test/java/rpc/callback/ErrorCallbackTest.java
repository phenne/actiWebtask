package rpc.callback;

import dao.UserDao;
import data.User;
import org.junit.Before;
import org.junit.Test;
import rpc.ManagerAccessRequired;
import rpc.callback.chain.ManagerAccessMiddleware;
import rpc.callback.chain.SessionMiddleware;
import rpc.callback.chain.UserDeletedMiddleware;
import rpc.error.ActiveUserDeletedException;
import rpc.error.ManagerAccessException;
import rpc.error.SessionInvalidatedException;
import rpc.error.UnknownErrorException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.sql.SQLException;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ErrorCallbackTest {

    private HttpServletRequest request;
    private HttpSession session;
    private Method method;
    private User sessionUser;

    @Before
    public void init() {
        request = mock(HttpServletRequest.class);
        session = mock(HttpSession.class);
        method = mock(Method.class);

        when(request.getSession()).thenReturn(session);

        sessionUser = new User();
        sessionUser.setId(1);
        when(session.getAttribute("user")).thenReturn(sessionUser);
    }

    @Test
    public void sessionCheck_sessionNotNullAndUserNotNull() throws Exception {
        SessionMiddleware sessionMiddleware = new SessionMiddleware();

        assertTrue(sessionMiddleware.check(request, method));
    }

    @Test(expected = SessionInvalidatedException.class)
    public void sessionCheck_sessionNull() throws Exception {
        SessionMiddleware sessionMiddleware = new SessionMiddleware();
        when(request.getSession()).thenReturn(null);

        sessionMiddleware.check(request, method);

        when(request.getSession()).thenReturn(session);
    }

    @Test(expected = SessionInvalidatedException.class)
    public void sessionCheck_userNull() throws Exception {
        SessionMiddleware sessionMiddleware = new SessionMiddleware();
        when(session.getAttribute("user")).thenReturn(null);

        sessionMiddleware.check(request, method);

        when(session.getAttribute("user")).thenReturn(sessionUser);
    }

    @Test
    public void userDeleted_success() throws Exception {
        UserDao userDao = mock(UserDao.class);
        UserDeletedMiddleware userDeletedMiddleware = new UserDeletedMiddleware(userDao);
        when(userDao.getUserById(anyInt())).thenReturn(new User());

        assertTrue(userDeletedMiddleware.check(request, method));
    }

    @Test(expected = ActiveUserDeletedException.class)
    public void userDeleted_currentUserNull() throws Exception {
        UserDao userDao = mock(UserDao.class);
        UserDeletedMiddleware userDeletedMiddleware = new UserDeletedMiddleware(userDao);
        when(userDao.getUserById(anyInt())).thenReturn(null);

        userDeletedMiddleware.check(request, method);
    }

    @Test(expected = UnknownErrorException.class)
    public void userDeleted_sqlException() throws Exception {
        UserDao userDao = mock(UserDao.class);
        UserDeletedMiddleware userDeletedMiddleware = new UserDeletedMiddleware(userDao);
        when(userDao.getUserById(anyInt())).thenThrow(SQLException.class);

        userDeletedMiddleware.check(request, method);
    }

    @Test
    public void managerAccess_methodAnnotatedAndUserManager() throws Exception {
        sessionUser.setManager(true);
        when(method.isAnnotationPresent(ManagerAccessRequired.class)).thenReturn(true);

        ManagerAccessMiddleware managerAccessMiddleware = new ManagerAccessMiddleware();
        assertTrue(managerAccessMiddleware.check(request, method));
    }

    @Test(expected = ManagerAccessException.class)
    public void managerAccess_userNotManager() throws Exception {
        sessionUser.setManager(false);
        when(method.isAnnotationPresent(ManagerAccessRequired.class)).thenReturn(true);

        ManagerAccessMiddleware managerAccessMiddleware = new ManagerAccessMiddleware();

        managerAccessMiddleware.check(request, method);
    }

    @Test
    public void managerAccess_methodNotAnnotated() throws Exception {
        sessionUser.setManager(true);
        when(method.isAnnotationPresent(ManagerAccessRequired.class)).thenReturn(false);

        ManagerAccessMiddleware managerAccessMiddleware = new ManagerAccessMiddleware();
        assertTrue(managerAccessMiddleware.check(request, method));
    }
}
