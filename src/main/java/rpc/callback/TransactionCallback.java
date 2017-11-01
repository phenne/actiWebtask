package rpc.callback;

import bd.TransactionManager;
import org.jabsorb.callback.ErrorInvocationCallback;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.AccessibleObject;
import java.sql.SQLException;

public class TransactionCallback implements ErrorInvocationCallback {

    @Override
    public void preInvoke(Object context, Object instance, AccessibleObject accessibleObject, Object[] arguments) throws Exception {
        //not impl
    }

    @Override
    public void postInvoke(Object context, Object instance, AccessibleObject accessibleObject, Object result) throws Exception {
        HttpServletRequest request = (HttpServletRequest) context;
        TransactionManager transaction = (TransactionManager) request.getAttribute("transaction");

        transaction.commit();
    }

    @Override
    public void invocationError(Object context, Object instance, AccessibleObject accessibleObject, Throwable error) {
        HttpServletRequest request = (HttpServletRequest) context;
        TransactionManager transaction = (TransactionManager) request.getAttribute("transaction");

        try {
            transaction.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
