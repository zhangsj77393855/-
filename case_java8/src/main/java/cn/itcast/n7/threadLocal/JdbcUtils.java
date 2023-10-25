package cn.itcast.n7.threadLocal;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @Author：zsj
 * @Date：2023/8/11
 */
public class JdbcUtils {
    /// ThreadLocal对象,将Connection绑定在当前线程中
    private static final ThreadLocal<Connection> t1 = new ThreadLocal<>();
    // c3p0数据库连接池对象属性
    public static final ComboPooledDataSource ds = new ComboPooledDataSource();
    // 获取连接
    public static Connection getConnection() throws SQLException {
        Connection connection = t1.get();
        if (connection == null) {
            connection = ds.getConnection();
            t1.set(connection);
        }
        return connection;
    }

}
