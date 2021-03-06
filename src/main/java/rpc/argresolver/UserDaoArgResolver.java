package rpc.argresolver;

import bd.Transaction;
import bd.UserDaoFactory;
import dao.UserDao;
import org.jabsorb.localarg.LocalArgResolveException;
import org.jabsorb.localarg.LocalArgResolver;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

public class UserDaoArgResolver implements LocalArgResolver {

    @Override
    public Object resolveArg(Object o) throws LocalArgResolveException {
        UserDao userDao = null;
        try {
            HttpServletRequest request = (HttpServletRequest) o;

            Transaction transaction = (Transaction) request.getAttribute("transaction");
            userDao = UserDaoFactory.getInstance().getUserDao(transaction);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userDao;
    }
}