package dao;

import bd.TransactionManager;
import data.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.Assert.assertEquals;

public class UserDaoTest {

    private TransactionManager transactionManager;
    private int maxId;

    @Before
    public void init() throws Exception {
        transactionManager = new TransactionManager();
        transactionManager.startTransaction();

        PreparedStatement statement = transactionManager.getConnection().prepareStatement("SELECT MAX(user_id) FROM users");
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            maxId = resultSet.getInt(1);
        }
    }

    @After
    public void after() throws Exception {
        transactionManager.rollback();
    }

    @Test
    public void generalTest() throws Exception {
        UserDao userDao = new UserDao();
        userDao.associateTransaction(transactionManager);

        User user = new User(++maxId, "name", "last", "user", "pass", true);
        userDao.insertUser(user);

        assertEquals(userDao.getUserById(maxId), user);
    }
}
