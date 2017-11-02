package dao;

import bd.Transaction;
import data.User;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UserDaoTest {

    private Transaction transaction;
    private int maxId;

    @Before
    public void init() throws Exception {
        transaction = new Transaction();
        transaction.startTransaction();

        PreparedStatement statement = transaction.getConnection().prepareStatement("SELECT MAX(user_id) FROM users");
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            maxId = resultSet.getInt(1);
        }
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
        UserDao userDao = new UserDao();
        userDao.associateTransaction(transaction);


        User user = new User();
        user.setUserName("user");
        user.setFirstName("first");
        user.setLastName("last");
        user.setPassword("pass");
        user.setManager(true);

        userDao.insertUser(user);

        User insertedUser = userDao.getUserByUsername("user");

        assertNotNull(insertedUser.getId());
        assertEquals(user.getUserName(), insertedUser.getUserName());
        assertEquals(user.getFirstName(), insertedUser.getFirstName());
        assertEquals(user.getLastName(), insertedUser.getLastName());
        assertEquals(user.getPassword(), insertedUser.getPassword());
        assertEquals(user.isManager(), insertedUser.isManager());
    }

    @Test
    public void updateUser_passNotChanged() throws Exception {
        UserDao userDao = new UserDao();
        userDao.associateTransaction(transaction);

        User requestUser = new User();
        requestUser.setId(maxId);
        requestUser.setUserName("changedUsername");
        requestUser.setFirstName("changedFName");
        requestUser.setLastName("changedLName");
        requestUser.setManager(true);

        userDao.updateUser(requestUser);
        User updatedUser = userDao.getUserById(maxId);

        assertEquals(requestUser.getId(), updatedUser.getId());
        assertEquals(requestUser.getUserName(), updatedUser.getUserName());
        assertEquals(requestUser.getFirstName(), updatedUser.getFirstName());
        assertEquals(requestUser.getLastName(), updatedUser.getLastName());
        assertEquals(requestUser.isManager(), updatedUser.isManager());
    }

    @Test
    public void updateUser_passChanged() throws Exception {
        UserDao userDao = new UserDao();
        userDao.associateTransaction(transaction);

        User requestUser = new User();
        requestUser.setId(maxId);
        requestUser.setUserName("changedUsername");
        requestUser.setFirstName("changedFName");
        requestUser.setLastName("changedLName");
        requestUser.setManager(true);
        requestUser.setPassword("changedPass");

        userDao.updateUser(requestUser);
        User updatedUser = userDao.getUserById(maxId);

        assertEquals(requestUser.getId(), updatedUser.getId());
        assertEquals(requestUser.getUserName(), updatedUser.getUserName());
        assertEquals(requestUser.getFirstName(), updatedUser.getFirstName());
        assertEquals(requestUser.getLastName(), updatedUser.getLastName());
        assertEquals(requestUser.getPassword(), updatedUser.getPassword());
        assertEquals(requestUser.isManager(), updatedUser.isManager());
    }

    @Test
    public void deleteUser() throws Exception {
        UserDao userDao = new UserDao();
        userDao.associateTransaction(transaction);

        int count = userDao.countUsers();
        userDao.deleteUser(maxId);

        assertEquals(count - 1, userDao.getAllUsers().size());
    }
}
