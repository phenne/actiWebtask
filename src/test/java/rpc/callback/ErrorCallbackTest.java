package rpc.callback;

import bd.Transaction;
import dao.UserDaoFactory;
import data.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import rpc.callback.chain.SessionMiddleware;
import rpc.callback.chain.UserDeletedMiddleware;
import rpc.error.SessionInvalidatedException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ErrorCallbackTest {

    private HttpServletRequest request;
    private HttpSession session;
    private Method method;

    @Before
    public void init() {
        request = mock(HttpServletRequest.class);
        session = mock(HttpSession.class);
        method = mock(Method.class);

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(new User());
    }

    @Test
    public void sessionCheck_success() throws Exception {
        SessionMiddleware sessionMiddleware = new SessionMiddleware();

        Assert.assertTrue(sessionMiddleware.check(request, method));
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

        when(session.getAttribute("user")).thenReturn(new User());
    }

    @Test
    public void userDeleted() throws Exception {
        UserDeletedMiddleware userDeletedMiddleware = new UserDeletedMiddleware();
        when(UserDaoFactory.getInstance()).thenReturn()

        userDeletedMiddleware.check();
        //you and I;
    }
}
