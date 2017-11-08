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
        when(session.getAttribute("user")).thenReturn(managerUser);

        assertSame(userService.getCurrentUser(request), managerUser);
    }

    @Test
    public void getAllUsers_success() throws Exception {
        when(userDao.getAllUsers()).thenReturn(users);

        assertSame(userService.getAllUsers(userDao), users);
    }

    @Test(expected = UnknownErrorException.class)
    public void getAllUsers_exception() throws Exception {
        when(userDao.getAllUsers()).thenThrow(SQLException.class);

        userService.getAllUsers(userDao);
    }

    @Test
    public void createUser_success() throws Exception {
        doAnswer(invocation -> {
            User createdUser = invocation.getArgument(0);
            users.add(createdUser);
            return null;
        }).when(userDao).insertUser(any(User.class));

        when(userDao.getAllUsers()).thenReturn(
                users
        );
        User toInsert = new User(3, "insName", "insLastName", "intUserName", "insPass", true);

        userService.createUser(toInsert, userDao);

        List<User> allUsers = userService.getAllUsers(userDao);
        User createdUser = allUsers.get(allUsers.size() - 1);

        assertSame(createdUser, toInsert);

        users.remove(users.size() - 1);
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void createUser_sqlIntegrityConstraintViolationException() throws Exception {
        doThrow(SQLIntegrityConstraintViolationException.class)
                .when(userDao).insertUser(any(User.class));

        User toInsert = new User(3, "insName", "insLastName", "intUserName", "insPass", true);

        userService.createUser(toInsert, userDao);
    }

    @Test(expected = UnknownErrorException.class)
    public void createUser_exception() throws Exception {
        doThrow(SQLException.class)
                .when(userDao).insertUser(any(User.class));

        User toInsert = new User(3, "insName", "insLastName", "intUserName", "insPass", true);

        userService.createUser(toInsert, userDao);
    }

    @Test
    public void editUser_successWithoutPassChange() throws Exception {
        doAnswer(invocation -> {
            User requestUser = invocation.getArgument(0);
            User userToUpdate = getUserById(requestUser.getId());

            userToUpdate.setUserName(requestUser.getUserName());
            userToUpdate.setFirstName(requestUser.getFirstName());
            userToUpdate.setLastName(requestUser.getLastName());
            userToUpdate.setManager(requestUser.isManager());
            return 1;
        }).when(userDao).updateUser(any(User.class));

        User requestUser = new User();
        requestUser.setUserName("newUserName");
        requestUser.setFirstName("newFirstName");
        requestUser.setLastName("newLastName");
        requestUser.setId(managerUser.getId());
        requestUser.setManager(false);

        userService.editUser(requestUser, userDao);

        User userById = getUserById(requestUser.getId());
        assertEquals(requestUser.getUserName(), userById.getUserName());
        assertEquals(requestUser.getFirstName(), userById.getFirstName());
        assertEquals(requestUser.getLastName(), userById.getLastName());
        assertEquals(requestUser.isManager(), userById.isManager());
    }

    @Test(expected = UserDeletedException.class)
    public void editUser_userDeleted() throws Exception {
        when(userDao.updateUser(any(User.class))).thenReturn(0);
        User requestUser = new User(3, "fN", "lN", "uN", "pssw", false);

        userService.editUser(requestUser, userDao);
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void editUser_userAlreadyExists() throws Exception {
        when(userDao.updateUser(any(User.class))).thenThrow(SQLIntegrityConstraintViolationException.class);
        User requestUser = new User(3, "fN", "lN", "userName2", "pssw", false);

        userService.editUser(requestUser, userDao);
    }

    @Test(expected = UnknownErrorException.class)
    public void editUser_sqlException() throws Exception {
        when(userDao.updateUser(any(User.class))).thenThrow(SQLException.class);
        User requestUser = new User(3, "fN", "lN", "userName2", "pssw", false);

        userService.editUser(requestUser, userDao);
    }

    @Test
    public void deleteUser_success() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute("user")).thenReturn(new User());

        doAnswer(invocation -> {
            int id = invocation.getArgument(0);
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getId() == id) {
                    users.remove(i);
                }
            }
            return 1;
        }).when(userDao).deleteUser(anyInt());

        int initSize = users.size();

        userService.deleteUser(2, userDao, request);
        assertEquals(initSize - 1, users.size());
    }

    @Test(expected = UserDeletedException.class)
    public void deleteUser_userDeleted() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute("user")).thenReturn(new User());
        when(userDao.deleteUser(anyInt())).thenReturn(0);

        userService.deleteUser(1, userDao, request);
    }

    @Test(expected = UnknownErrorException.class)
    public void deleteUser_unknownError() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute("user")).thenReturn(new User());
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
