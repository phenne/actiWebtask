package service;

import dao.UserDao;
import data.User;
import rpc.ManagerAccessRequired;
import rpc.error.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public class UserService {

    @ManagerAccessRequired
    public void createUser(User user, UserDao userDao) throws RpcException {
        try {
            userDao.insertUser(user);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new UserAlreadyExistsException();
        } catch (Exception e) {
            throw new UnknownErrorException();
        }
    }

    @ManagerAccessRequired
    public void editUser(User user, UserDao userDao) throws RpcException {
        try {
            if (userDao.updateUser(user) == 0) {
                throw new UserDeletedException();
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new UserAlreadyExistsException();
        } catch (SQLException e) {
            throw new UnknownErrorException();
        }
    }

    @ManagerAccessRequired
    public void deleteUser(int id, UserDao userDao, HttpServletRequest request) throws RpcException {

        try {
            User user = (User) request.getSession().getAttribute("user");
            if (user.getId() == id) {
                throw new UnknownErrorException();
            }
            if (userDao.deleteUser(id) == 0) {
                throw new UserDeletedException();
            }
        } catch (SQLException e) {
            throw new UnknownErrorException();
        }
    }

    public User getCurrentUser(HttpServletRequest request) {
        return (User) request.getSession().getAttribute("user");
    }

    public List<User> getAllUsers(UserDao userDao) throws RpcException {
        try {
            return userDao.getAllUsers();
        } catch (Exception e) {
            throw new UnknownErrorException();
        }
    }
}