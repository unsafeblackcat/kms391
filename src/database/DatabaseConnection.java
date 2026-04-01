package database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

public class DatabaseConnection {

	private static HikariDataSource dataSource;

	public static synchronized void init() {
		try {
			HikariConfig config = new HikariConfig();
			config.setDriverClassName(DBSetting.DBDriver);
			config.setJdbcUrl(DBSetting.DBUrl);
			config.setUsername(DBSetting.DBUser);
			config.setPassword(DBSetting.DBPassword);
			config.setMinimumIdle(DBSetting.DBMinIdle);
			config.setMaximumPoolSize(DBSetting.DBMaxPool);
			config.setConnectionTimeout(DBSetting.DBConTimeOut);
			config.setIdleTimeout(TimeUnit.MINUTES.toMillis(DBSetting.DBIdleTimeOut));
			dataSource = new HikariDataSource(config);
		} catch (Exception exception) {
			System.err.println("[FAIL] [Loading database]" + exception.toString());
			System.exit(1);
		}
		System.out.println("[啟動成功][正在加載資料庫]");
	}

	public static void closeObject(Connection con) {
		try {
			con.close();
		} catch (Exception ex) {
		} finally {
			con = null;
		}
	}

	public static synchronized void shutdown() {
		try {
			dataSource.close();
		} catch (Exception e) {
		}

		dataSource = null;
	}

	public static Connection getConnection() throws SQLException {
		final Connection con = dataSource.getConnection();
		return con;
	}

	public static int getActiveConnections() {
		return dataSource.getHikariPoolMXBean().getActiveConnections();
	}

	public static int getIdleConnections() {
		return dataSource.getHikariPoolMXBean().getIdleConnections();
	}
}
