package service;

import dao.UserDao;
import data.User;
import org.junit.Before;
import org.junit.Test;
import rpc.error.UnknownErrorException;
import rpc.error.UserAlreadyExistsException;
import rpc.error.UserDeletedException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserDao userDao;
    private User managerUser;
    private User regularUser;
    private List<User> users = new ArrayList<>();
    private UserService userService;

    @Before
    public void init() {
        userService = new UserService();

        userDao = mock(UserDao.class);

        managerUser = new User(1, "name1", "lastName1", "userName1", "pass1", true);
        regularUser = new User(2, "name2", "lastName2", "userName2", "pass2", false);

        users.add(managerUser);
        users.add(regularUser);
    }

    @Test
    public void getUser_success() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getSession()).thenReturn(session);
        userService.getCurrentUser(request);
        verify(session).getAttribute("user");
    }

    @Test
    public void getAllUsers_success() throws Exception {
        userService.getAllUsers(userDao);
        verify(userDao).getAllUsers();
    }

    @Test(expected = UnknownErrorException.class)
    public void getAllUsers_exception() throws Exception {
        when(userDao.getAllUsers()).thenThrow(SQLException.class);

        userService.getAllUsers(userDao);
    }

    @Test
    public void createUser_success() throws Exception {
        User toInsert = new User();
        userService.createUser(toInsert, userDao);
        verify(userDao).insertUser(toInsert);
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void createUser_sqlIntegrityConstraintViolationException() throws Exception {
        when(userDao.insertUser(any(User.class))).thenThrow(SQLIntegrityConstraintViolationException.class);
        User toInsert = new User();

        userService.createUser(toInsert, userDao);
    }

    @Test(expected = UnknownErrorException.class)
    public void createUser_exception() throws Exception {
        when(userDao.insertUser(any(User.class))).thenThrow(SQLException.class);
        User toInsert = new User();

        userService.createUser(toInsert, userDao);
    }

    @Test
    public void editUser() throws Exception {
        when(userDao.updateUser(any(User.class))).thenReturn(1);
        User userToEdit = new User();

        userService.editUser(userToEdit, userDao);
        verify(userDao).updateUser(userToEdit);
    }

    @Test(expected = UserDeletedException.class)
    public void editUser_userDeleted() throws Exception {
        when(userDao.updateUser(any(User.class))).thenReturn(0);
        User requestUser = new User();

        userService.editUser(requestUser, userDao);
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void editUser_userAlreadyExists() throws Exception {
        when(userDao.updateUser(any(User.class))).thenThrow(SQLIntegrityConstraintViolationException.class);
        User requestUser = new User();

        userService.editUser(requestUser, userDao);
    }

    @Test(expected = UnknownErrorException.class)
    public void editUser_sqlException() throws Exception {
        when(userDao.updateUser(any(User.class))).thenThrow(SQLException.class);
        User requestUser = new User();

        userService.editUser(requestUser, userDao);
    }

    @Test
    public void deleteUser_success() throws Exception {
        User sessionUser = new User();
        sessionUser.setId(10);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(sessionUser);
        when(userDao.deleteUser(anyInt())).thenReturn(1);

        userService.deleteUser(999, userDao, request);

        verify(userDao).deleteUser(999);
    }

    @Test(expected = UserDeletedException.class)
    public void deleteUser_userDeleted() throws Exception {
        User sessionUser = new User();
        sessionUser.setId(10);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(sessionUser);

        when(userDao.deleteUser(anyInt())).thenReturn(0);

        userService.deleteUser(999, userDao, request);
    }

    @Test(expected = UnknownErrorException.class)
    public void deleteUser_unknownError() throws Exception {
        User sessionUser = new User();
        sessionUser.setId(10);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(sessionUser);
        when(userDao.deleteUser(anyInt())).thenThrow(SQLException.class);

        userService.deleteUser(1, userDao, request);
    }

    private User getUserById(int id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
