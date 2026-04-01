package database;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Tontontaki
 */
public class DatabaseBackup {

	public static String dbpath;
	public static String dbuser;
	public static String dbpass;
	public static String dbname;
	public static String encoding;
	public static int savetime;

	public static void main(String[] args) {
		try {
			Properties props = new Properties();
			FileReader fr = null;
			fr = new FileReader("Properties/database.properties");
			props.load(fr);
			dbpath = props.getProperty("query.path");
			dbuser = props.getProperty("query.user");
			dbpass = props.getProperty("query.password");
			dbname = props.getProperty("query.schema");
			encoding = props.getProperty("encoding");
			savetime = Integer.parseInt(props.getProperty("query.savetime"));
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				public void run() {
					DatabaseBackup.save();
				}
			}, 1000L, savetime);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void save() {
		try {
			Runtime runtime = Runtime.getRuntime();
			String date = (new SimpleDateFormat("yyyy년 MM월 dd일 hh시 mm분 ss초")).format(new Date());

			File backupFile = new File("sql/backup_" + date + ".sql");
			FileWriter fw = new FileWriter(backupFile);
			Process child = runtime.exec(dbpath + " --user=" + dbuser + " --password=" + dbpass
					+ " --default-character-set=" + encoding + " --lock-all-tables --opt " + dbname + "");
			InputStreamReader rs = new InputStreamReader(child.getInputStream());
			BufferedReader br = new BufferedReader(rs);
			String line;
			while ((line = br.readLine()) != null) {
				fw.write(line + "\n");
			}
			fw.close();
			rs.close();
			br.close();
			System.out.println("[通知] " + date + " 資料庫存儲已完成.");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
