package servlet;

import bd.Transaction;
import data.User;
import org.junit.Before;
import org.junit.Test;
import servlet.error.InvalidPasswordException;
import servlet.error.InvalidUsernameException;
import servlet.service.UserCheckerService;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.sql.SQLException;

import static org.mockito.Mockito.*;

public class LoginServletTest {

    private static final String ROOT_URL = "/";
    private static final String USER_LIST_PAGE = "/pages/login.jsp";

    private static final String FAIL = "fail";
    private static final String SQL_EXCEPTION_MESSAGE = "Connection error! Please try later";
    private static final String INVALID_USERNAME_MESSAGE = "Invalid username!";
    private static final String INVALID_PASSWORD_MESSAGE = "Invalid password!";

    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private LoginServlet servlet;
    private RequestDispatcher requestDispatcher;

    @Before
    public void init() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        requestDispatcher = mock(RequestDispatcher.class);
        servlet = new LoginServlet();

        when(request.getParameter("username")).thenReturn("username");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getSession()).thenReturn(session);
        when(request.getRequestDispatcher(USER_LIST_PAGE)).thenReturn(requestDispatcher);
    }

    @Test
    public void doGet_userNotNull() throws Exception {
        when(session.getAttribute("user")).thenReturn(new User());

        servlet.doGet(request, response);
        verify(response).sendRedirect(ROOT_URL);
    }

    @Test
    public void doGet_userNull() throws Exception {
        when(session.getAttribute("user")).thenReturn(null);

        servlet.doGet(request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void doPost_usernameNullPasswordNull() throws Exception {
        when(request.getParameter("username")).thenReturn(null);
        when(request.getParameter("password")).thenReturn(null);

        servlet.doPost(request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void doPost_sqlException() throws Exception {
        Transaction transaction =  mock(Transaction.class);
        when(request.getAttribute("transaction")).thenReturn(transaction);
        UserCheckerService service = mock(UserCheckerService.class);
        UserCheckerService.setInstance(service);

        when(service.check("username", "password", transaction)).thenThrow(SQLException.class);

        servlet.doPost(request, response);

        verify(request).setAttribute(FAIL, SQL_EXCEPTION_MESSAGE);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void doPost_invalidUsername() throws Exception {
        Transaction transaction = mock(Transaction.class);
        when(request.getAttribute("transaction")).thenReturn(transaction);
        UserCheckerService service = mock(UserCheckerService.class);
        UserCheckerService.setInstance(service);

        when(service.check("username", "password", transaction)).thenThrow(InvalidUsernameException.class);

        servlet.doPost(request, response);

        verify(request).setAttribute(FAIL, INVALID_USERNAME_MESSAGE);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void doPost_invalidPassword() throws Exception {
        Transaction transaction = mock(Transaction.class);
        when(request.getAttribute("transaction")).thenReturn(transaction);
        UserCheckerService service = mock(UserCheckerService.class);
        UserCheckerService.setInstance(service);

        when(service.check("username", "password", transaction)).thenThrow(InvalidPasswordException.class);

        servlet.doPost(request, response);

        verify(request).setAttribute(FAIL, INVALID_PASSWORD_MESSAGE);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void doPost_noException() throws Exception {
        Transaction transaction = mock(Transaction.class);
        when(request.getAttribute("transaction")).thenReturn(transaction);
        UserCheckerService service = mock(UserCheckerService.class);
        UserCheckerService.setInstance(service);

        User user = new User();
        when(service.check("username", "password", transaction)).thenReturn(user);

        servlet.doPost(request, response);

        verify(session).setAttribute("user", user);
        verify(response).sendRedirect(ROOT_URL);
    }
}