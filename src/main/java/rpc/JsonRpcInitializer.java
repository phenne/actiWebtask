package rpc;

import dao.UserDao;
import org.jabsorb.JSONRPCBridge;
import rpc.argresolver.UserDaoArgResolver;
import rpc.callback.ErrorCallback;
import rpc.callback.TransactionCallback;
import service.UserService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;

@WebListener
public class JsonRpcInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        UserService userService = new UserService();

        JSONRPCBridge globalBridge = JSONRPCBridge.getGlobalBridge();
        globalBridge.registerObject("userService", userService);
        globalBridge.registerCallback(new ErrorCallback(), HttpServletRequest.class);
        globalBridge.registerCallback(new TransactionCallback(), HttpServletRequest.class);
        JSONRPCBridge.registerLocalArgResolver(UserDao.class, HttpServletRequest.class, new UserDaoArgResolver());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        //not impl
    }
}