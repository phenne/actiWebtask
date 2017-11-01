package rpc.callback.chain;

import rpc.ManagerAccessRequired;
import rpc.error.ErrorType;
import rpc.error.ManagerAccessException;
import rpc.error.RpcException;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

public class ManagerAccessMiddleware extends Middleware {

    @Override
    public boolean check(HttpServletRequest request, Method method) throws RpcException {
        if (method.isAnnotationPresent(ManagerAccessRequired.class) && !getUser().isManager()) {
            throw new ManagerAccessException(ErrorType.MANAGER_ACCESS_REQUIRED.name());
        }
        return checkNext(request, method);
    }
}
