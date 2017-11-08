package rpc.callback.chain;

import rpc.error.RpcException;
import rpc.error.SessionInvalidatedException;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

public class SessionMiddleware extends Middleware {

    @Override
    public boolean check(HttpServletRequest request, Method method) throws RpcException {
        if (request.getSession() == null || request.getSession().getAttribute("user") == null) {
            throw new SessionInvalidatedException();
        }
        return checkNext(request, method);
    }
}