package rpc.callback.chain;

import bd.TransactionManager;
import dao.UserDao;
import data.User;
import rpc.error.ActiveUserDeletedException;
import rpc.error.ErrorType;
import rpc.error.RpcException;
import rpc.error.UnknownErrorException;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.sql.SQLException;

public class UserDeletedMiddleware extends Middleware {

    @Override
    public boolean check(HttpServletRequest request, Method method) throws RpcException {
        try {
            TransactionManager transaction = (TransactionManager) request.getAttribute("transaction");
            UserDao userDao = new UserDao();
            userDao.associateTransaction(transaction);

            User sessionUser = (User) request.getSession().getAttribute("user");
            User currentUser = userDao.getUserById(sessionUser.getId());

            if (currentUser == null) {
                request.getSession().removeAttribute("user");
                throw new ActiveUserDeletedException(ErrorType.CURRENT_USER_DELETED.name());
            }

            request.getSession().setAttribute("user", currentUser);
            return checkNext(request, method);
        } catch (SQLException e) {
            throw new UnknownErrorException(ErrorType.UNKNOWN_ERROR.name());
        }
    }
}