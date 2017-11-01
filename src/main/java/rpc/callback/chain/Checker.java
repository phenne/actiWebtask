package rpc.callback.chain;

import rpc.error.RpcException;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

public class Checker {

    private Middleware middleware;

    public void setMiddleware(Middleware middleware) {
        this.middleware = middleware;
    }

    public void check(HttpServletRequest request, Method method) throws RpcException {
        middleware.check(request, method);
    }
}
