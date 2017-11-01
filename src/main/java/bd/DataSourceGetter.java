package bd;

import com.mysql.cj.jdbc.MysqlDataSource;

public class DataSourceGetter {

    private static final String URL = "jdbc:mysql://localhost:3306/user_list?useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static MysqlDataSource dataSource = null;

    public static MysqlDataSource getDataSource() {
        if (dataSource == null) {
            dataSource = new MysqlDataSource();
            dataSource.setUser("admin");
            dataSource.setPassword("admin");
            dataSource.setUrl(URL);
        }
        return dataSource;
    }
}