package servlet;

import data.User;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoginServletTest {

    private static final String ROOT_URL = "/";
    private static final String USER_LIST_PAGE = "/pages/login.jsp";

    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;

    @Before
    public void init() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);

        when(request.getSession()).thenReturn(session);
    }

    @Test
    public void doGet_userNotNull() throws Exception {
        LoginServlet loginServlet = new LoginServlet();
        when(session.getAttribute("user")).thenReturn(new User());

        loginServlet.doGet(request, response);

        verify(response).sendRedirect(ROOT_URL);
    }

    @Test
    public void doGet_userNull() throws Exception {
        LoginServlet servlet = new LoginServlet();
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        when(session.getAttribute("user")).thenReturn(null);
        when(request.getRequestDispatcher(USER_LIST_PAGE)).thenReturn(requestDispatcher);

        servlet.doGet(request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void doPost_usernameNullPasswordNull() {
        when(request.getAttribute("username")).thenReturn(null);
        when(request.getAttribute("password")).thenReturn(null);


    }
}
