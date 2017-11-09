package servlet;

import bd.Transaction;
import bd.UserDaoFactory;
import dao.UserDao;
import data.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
    private LoginServlet servlet;

    @Before
    public void init() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        servlet = new LoginServlet();

        when(request.getSession()).thenReturn(session);
    }

    @Test
    public void doGet_userNotNull() throws Exception {
        when(session.getAttribute("user")).thenReturn(new User());

        servlet.doGet(request, response);
        verify(response).sendRedirect(ROOT_URL);
    }

    @Test
    public void doGet_userNull() throws Exception {
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        when(session.getAttribute("user")).thenReturn(null);
        when(request.getRequestDispatcher(USER_LIST_PAGE)).thenReturn(requestDispatcher);

        servlet.doGet(request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void doPost_usernameNullPasswordNull() throws Exception {
        when(request.getAttribute("username")).thenReturn(null);
        when(request.getAttribute("password")).thenReturn(null);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(USER_LIST_PAGE)).thenReturn(requestDispatcher);

        servlet.doPost(request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void getUser() throws Exception {
        UserDaoFactory factory = mock(UserDaoFactory.class);
        UserDaoFactory.setInstance(factory);
        UserDao userDao = mock(UserDao.class);
        Transaction transaction = mock(Transaction.class);

        when(request.getAttribute("transaction")).thenReturn(transaction);
        when(factory.getUserDao(any(Transaction.class))).thenReturn(userDao);

        servlet.getUser(request, "userName");

        verify(userDao).getUserByUsername("userName");
        verify(transaction).commit();
    }

    @Test
    public void checkUserData_userNull() throws Exception {
        String result = servlet.checkUserData(null, "pass");

        assertEquals(result, "Invalid username!");
    }

    @Test
    public void checkUserData_userPassNotEquals() throws Exception {
        User user = new User();
        user.setPassword("anotherPass");

        String result = servlet.checkUserData(user, "pass");

        assertEquals(result, "Invalid password!");
    }

    @Test
    public void checkUserData_allOk() throws Exception {
        User user = new User();
        user.setPassword("pass");

        String result = servlet.checkUserData(user, "pass");

        assertNull(result);
    }

    @Test
    public void checkResult_resultNull() throws Exception {
        User user = new User();
        servlet.checkResult(null, request, response, user);

        verify(session).setAttribute("user", user);
        response.sendRedirect(ROOT_URL);
    }

    @Test
    public void checkResult_resultNotNull() throws Exception {
        String result = "fail";
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(USER_LIST_PAGE)).thenReturn(requestDispatcher);

        servlet.checkResult(result, request, response, new User());

        verify(request).setAttribute("fail", result);
        verify(requestDispatcher).forward(request, response);
    }
}