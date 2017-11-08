package rpc.callback.chain;

import data.User;
import rpc.ManagerAccessRequired;
import rpc.error.ErrorType;
import rpc.error.ManagerAccessException;
import rpc.error.RpcException;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

public class ManagerAccessMiddleware extends Middleware {

    @Override
    public boolean check(HttpServletRequest request, Method method) throws RpcException {
        User user = (User) request.getSession().getAttribute("user");
        if (method.isAnnotationPresent(ManagerAccessRequired.class) && !user.isManager()) {
            throw new ManagerAccessException();
        }
        return checkNext(request, method);
    }
}