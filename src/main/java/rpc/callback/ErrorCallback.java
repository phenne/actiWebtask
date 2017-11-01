package rpc.callback;

import org.jabsorb.callback.InvocationCallback;
import rpc.callback.chain.Checker;
import rpc.callback.chain.ManagerAccessMiddleware;
import rpc.callback.chain.SessionMiddleware;
import rpc.callback.chain.UserDeletedMiddleware;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

public class ErrorCallback implements InvocationCallback {

    @Override
    public void preInvoke(Object context, Object instance, AccessibleObject accessibleObject, Object[] arguments) throws Exception {
        HttpServletRequest request = (HttpServletRequest) context;
        Method method = (Method) accessibleObject;

        SessionMiddleware sessionMiddleware = new SessionMiddleware();
        sessionMiddleware
                .linkWith(new UserDeletedMiddleware()
                        .linkWith(new ManagerAccessMiddleware()));

        Checker checker = new Checker();
        checker.setMiddleware(sessionMiddleware);
        checker.check(request, method);
    }

    @Override
    public void postInvoke(Object o, Object o1, AccessibleObject accessibleObject, Object o2) throws Exception {
        //not impl
    }
}