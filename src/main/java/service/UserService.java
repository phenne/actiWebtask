package service;

import dao.UserDao;
import data.User;
import rpc.ManagerAccessRequired;
import rpc.error.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public class UserService {

    @ManagerAccessRequired
    public void createUser(User user, UserDao userDao) throws RpcException {
        try {
            userDao.insertUser(user);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new UserAlreadyExistsException(ErrorType.USER_ALREADY_EXISTS.name());
        } catch (Exception e) {
            throw new UnknownErrorException(ErrorType.UNKNOWN_ERROR.name());
        }
    }

    @ManagerAccessRequired
    public void editUser(User user, UserDao userDao) throws RpcException {
        try {
            if (userDao.updateUser(user) == 0) {
                throw new UserDeletedException(ErrorType.USER_DELETED.name());
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new UserAlreadyExistsException(ErrorType.USER_ALREADY_EXISTS.name());
        } catch (SQLException e) {
            throw new UnknownErrorException(ErrorType.UNKNOWN_ERROR.name());
        }
    }

    @ManagerAccessRequired
    public void deleteUser(int id, UserDao userDao) throws RpcException {

        try {
            if (userDao.deleteUser(id) == 0) {
                throw new UserDeletedException(ErrorType.USER_DELETED.name());
            }
        } catch (SQLException e) {
            throw new UnknownErrorException(ErrorType.UNKNOWN_ERROR.name());
        }
    }

    public User getCurrentUser(HttpServletRequest request) throws UnknownErrorException {
        return (User) request.getAttribute("user");
    }

    public List<User> getAllUsers(UserDao userDao) throws UnknownErrorException {
        try {
            return userDao.getAllUsers();
        } catch (SQLException e) {
            throw new UnknownErrorException(ErrorType.UNKNOWN_ERROR.name());
        }
    }
}