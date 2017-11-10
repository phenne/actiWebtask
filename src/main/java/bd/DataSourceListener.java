package bd;

import javax.annotation.Resource;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;

@WebListener
public class DataSourceListener implements ServletContextListener {

    @Resource(name = "jdbc/userlist")
    private DataSource dataSource;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        DataSourceGetter.getInstance().setDataSource(dataSource);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        //not impl
    }
}
