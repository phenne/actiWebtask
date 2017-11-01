package rpc.callback.chain;

import rpc.error.RpcException;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

public abstract class Middleware {

    private Middleware next;

    public Middleware linkWith(Middleware next) {
        this.next = next;
        return next;
    }

    public abstract boolean check(HttpServletRequest request, Method method) throws RpcException;

    protected boolean checkNext(HttpServletRequest request, Method method) throws RpcException {
        if (next == null) {
            return true;
        }
        return next.check(request, method);
    }
}