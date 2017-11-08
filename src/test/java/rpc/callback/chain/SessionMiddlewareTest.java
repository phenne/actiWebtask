package rpc.callback.chain;

import data.User;
import org.junit.Before;
import org.junit.Test;
import rpc.error.SessionInvalidatedException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.lang.reflect.Method;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SessionMiddlewareTest extends MiddlewareTest {

    private HttpServletRequest request;
    private Method method;
    private HttpSession session;
    private SessionMiddleware middleware;

    @Before
    public void init() {
        request = mock(HttpServletRequest.class);
        method = mock(Method.class);
        session = mock(HttpSession.class);
        middleware = new SessionMiddleware();

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(new User());
    }

    @Test
    public void sessionCheck_sessionNotNullAndUserNotNull() throws Exception {
        assertTrue(middleware.check(request, method));
    }

    @Test(expected = SessionInvalidatedException.class)
    public void sessionCheck_sessionNull() throws Exception {
        when(request.getSession()).thenReturn(null);

        middleware.check(request, method);
    }

    @Test(expected = SessionInvalidatedException.class)
    public void sessionCheck_userNull() throws Exception {
        when(session.getAttribute("user")).thenReturn(null);

        middleware.check(request, method);
    }
}
