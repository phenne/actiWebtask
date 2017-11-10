package bd;

import javax.sql.DataSource;


public class DataSourceGetter {
    
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