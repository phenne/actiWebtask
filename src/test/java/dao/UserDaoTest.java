package dao;

import bd.Transaction;
import data.User;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class UserDaoTest {

    private Transaction transaction;
    private UserDao userDao;
    private User user = new User();

    @Before
    public void init() throws Exception {
        transaction = new Transaction();
        userDao = new UserDao();
        userDao.associateTransaction(transaction);
        userDao.deleteAll();

        user = new User();
        user.setUserName("user");
        user.setFirstName("first");
        user.setLastName("last");
        user.setPassword("pass");
        user.setManager(true);
    }

    @After
    public void after() throws Exception {
        transaction.rollback();
    }

    @Test
    public void getAllUsers() throws Exception {
        UserDao userDao = new UserDao();
        userDao.associateTransaction(transaction);

        int count = userDao.countUsers();

        Assert.assertEquals(userDao.getAllUsers().size(), count);
    }

    @Test
    public void insertUser() throws Exception {
        int id = userDao.insertUser(user);
        user.setId(id);

        User insertedUser = userDao.getUserById(id);

        assertEquals(user, insertedUser);
    }

    @Test
    public void updateUser_passNotChanged() throws Exception {
        String pass = user.getPassword();
        int id = userDao.insertUser(user);

        User requestUser = new User();
        requestUser.setId(id);
        requestUser.setUserName("changedUsername");
        requestUser.setFirstName("changedFName");
        requestUser.setLastName("changedLName");
        requestUser.setManager(false);

        userDao.updateUser(requestUser);

        User updatedUser = userDao.getUserById(id);
        requestUser.setPassword(pass);

        assertEquals(requestUser, updatedUser);
    }

    @Test
    public void updateUser_passChanged() throws Exception {
        int id = userDao.insertUser(user);

        User requestUser = new User();
        requestUser.setId(id);
        requestUser.setUserName("changedUsername");
        requestUser.setFirstName("changedFName");
        requestUser.setLastName("changedLName");
        requestUser.setManager(true);
        requestUser.setPassword("changedPass");

        userDao.updateUser(requestUser);

        User updatedUser = userDao.getUserById(id);

        assertEquals(requestUser, updatedUser);
    }

    @Test
    public void deleteUser() throws Exception {
        User user2 = new User();
        user2.setUserName("user2");
        user2.setFirstName("name2");
        user2.setLastName("last2");
        user2.setPassword("pass2");
        user2.setManager(true);

        int id = userDao.insertUser(user);
        user.setId(id);
        int idToDelete = userDao.insertUser(user2);

        userDao.deleteUser(idToDelete);

        List<User> list = new ArrayList<>();
        list.add(user);


        assertEquals(userDao.getAllUsers(), list);
    }

    @Test
    public void getUserById_userExists() throws Exception {
        int id = userDao.insertUser(user);
        user.setId(id);

        User userById = userDao.getUserById(id);
        assertEquals(user, userById);
    }

    @Test
    public void getUserById_userNotExists() throws Exception {
        int id = userDao.insertUser(user);
        user.setId(id);

        User userById = userDao.getUserById(id + 1);
        assertNull(userById);
    }

    @Test
    public void getUserByUsername_userExists() throws Exception {
        int id = userDao.insertUser(user);
        user.setId(id);

        User userByUsername = userDao.getUserByUsername(user.getUserName());
        assertEquals(user, userByUsername);
    }

    @Test
    public void getUserByUsername_userNotExists() throws Exception {
        assertNull(userDao.getUserByUsername("notExistingUsername"));
    }

    @Test
    public void getAllUsers_noUsers() throws Exception {
        List<User> users = userDao.getAllUsers();
        assertEquals(0, users.size());
    }

    @Test
    public void getAllUsers_oneUser() throws Exception {
        int id = userDao.insertUser(user);
        user.setId(id);

        List<User> users = userDao.getAllUsers();

        assertEquals(1, users.size());
        assertEquals(user, users.get(0));
    }

    @Test
    public void getAllUsers_severalUsers() throws Exception {
        User user2 = new User();
        user2.setUserName("user2");
        user2.setFirstName("name2");
        user2.setLastName("last2");
        user2.setPassword("pass2");
        user2.setManager(false);

        int id1 = userDao.insertUser(user);
        user.setId(id1);
        int id2 = userDao.insertUser(user2);
        user2.setId(id2);

        Set<User> users = new HashSet<>();
        users.add(user);
        users.add(user2);

        Set<User> bdUsers = new HashSet<>(userDao.getAllUsers());

        assertEquals(users, bdUsers);
    }
}
