package database;

import server.ServerProperties;

public class DBSetting {

	public static final int CLOSE_CURRENT_RESULT = 1;
	public static final int KEEP_CURRENT_RESULT = 2;
	public static final int CLOSE_ALL_RESULTS = 3;
	public static final int SUCCESS_NO_INFO = -2;
	public static final int EXECUTE_FAILED = -3;
	public static final int RETURN_GENERATED_KEYS = 1;
	public static final int NO_GENERATED_KEYS = 2;

	public static final String DBDriver = "org.mariadb.jdbc.Driver";
	public static final int DBMinIdle = Runtime.getRuntime().availableProcessors();
	public static final int DBMaxPool = Math.min(512, DBMinIdle * 4); // 硬限制512
	public static final long DBConTimeOut = 30_000L; // 30秒
	public static final long DBIdleTimeOut = 10L; // 10分钟（必须<maxLifetime）
	public static final String DBHost = ServerProperties.getProperty("query.host", "localhost");
	public static final String DBPort = ServerProperties.getProperty("query.port", "3306");
	public static final String DBSchema = ServerProperties.getProperty("query.schema");
	public static final String DBUser = ServerProperties.getProperty("query.user");
	public static final String DBPassword = ServerProperties.getProperty("query.password");
	public static final String DBUrl = "jdbc:mariadb://" + DBHost + ":" + DBPort + "/" + DBSchema
			+ "?useUnicode=true&characterEncoding=UTF-8";

}
