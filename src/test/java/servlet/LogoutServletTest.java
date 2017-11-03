package servlet;

import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LogoutServletTest {

    private static final String LOGIN_URL = "/login";

    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;

    @Before
    public void init() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
    }

    @Test
    public void doPost_invalidateSession() throws Exception {
        LogoutServlet servlet = new LogoutServlet();
        when(request.getSession(false)).thenReturn(session);

        servlet.doPost(request, response);

        verify(response).sendRedirect(LOGIN_URL);
    }

    @Test
    public void doPost_sessionNull() throws Exception {
        LogoutServlet servlet = new LogoutServlet();
        when(request.getSession(false)).thenReturn(null);

        servlet.doPost(request, response);

        verify(response).sendRedirect(LOGIN_URL);
    }
}
