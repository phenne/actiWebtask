package bd;

import com.mysql.cj.jdbc.MysqlDataSource;

import javax.sql.DataSource;


public class DataSourceGetter {

//    // TODO: 10.11.2017 скрипты и конфиги
//    private static final String URL = "jdbc:mysql://localhost:3306/user_list?useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
//    private static MysqlDataSource dataSource = null;

    private DataSourceGetter() {
    }

    private static class Holder {
        public static final DataSourceGetter INSTANCE = new DataSourceGetter();
    }

    public static DataSourceGetter getInstance() {
        return Holder.INSTANCE;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private DataSource dataSource;

    public DataSource getDataSource() {
        System.out.println("hallo!");
        return dataSource;
    }
}