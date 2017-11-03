package servlet;

import data.User;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class MainServletTest {

    private static final String LOGIN_URL = "/login";
    private static final String USER_LIST_PAGE = "/pages/userList.jsp";

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
    public void doGet_sessionNotNullAndUserNotNull() throws Exception {
        MainServlet servlet = new MainServlet();

        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(USER_LIST_PAGE)).thenReturn(requestDispatcher);
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(new User());
        servlet.doGet(request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void doGet_sessionNull() throws Exception {
        MainServlet servlet = new MainServlet();
        when(request.getSession()).thenReturn(null);

        servlet.doGet(request, response);
        verify(response).sendRedirect(LOGIN_URL);
    }

    @Test
    public void doGet_userNull() throws Exception {
        MainServlet servlet = new MainServlet();
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(null);

        servlet.doGet(request, response);
        verify(response).sendRedirect(LOGIN_URL);
    }
}