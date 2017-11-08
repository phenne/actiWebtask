package rpc.callback.chain;

import data.User;
import org.junit.Before;
import org.junit.Test;
import rpc.ManagerAccessRequired;
import rpc.error.ManagerAccessException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ManagerAccessMiddlewareTest extends MiddlewareTest {

    private HttpServletRequest request;
    private Method method;
    private User sessionUser;
    private ManagerAccessMiddleware middleware;

    @Before
    public void init() {
        request = mock(HttpServletRequest.class);
        method = mock(Method.class);
        HttpSession session = mock(HttpSession.class);
        sessionUser = new User();
        middleware = new ManagerAccessMiddleware();

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(sessionUser);
    }

    @Test
    public void managerAccess_methodAnnotatedAndUserManager() throws Exception {
        sessionUser.setManager(true);
        when(method.isAnnotationPresent(ManagerAccessRequired.class)).thenReturn(true);

        assertTrue(middleware.check(request, method));
    }

    @Test(expected = ManagerAccessException.class)
    public void managerAccess_userNotManager() throws Exception {
        sessionUser.setManager(false);
        when(method.isAnnotationPresent(ManagerAccessRequired.class)).thenReturn(true);

        middleware.check(request, method);
    }

    @Test
    public void managerAccess_methodNotAnnotated() throws Exception {
        sessionUser.setManager(true);
        when(method.isAnnotationPresent(ManagerAccessRequired.class)).thenReturn(false);

        assertTrue(middleware.check(request, method));
    }
}