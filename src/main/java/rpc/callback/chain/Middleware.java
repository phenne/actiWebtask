package rpc.callback.chain;

import data.User;
import rpc.error.RpcException;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

public abstract class Middleware {

    private User user;
    private Middleware next;

    public Middleware linkWith(Middleware next) {
        this.next = next;
        return next;
    }

    public abstract boolean check(HttpServletRequest request, Method method) throws RpcException;

    protected boolean checkNext(HttpServletRequest request, Method method) {
        return next == null || next.checkNext(request, method);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}