package handling.login.handler;

import constants.KoreaCalendar;
import database.DatabaseConnection;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class AutoRegister {

	private static final int ACCOUNTS_PER_IP = 999; // 修改記錄 ？？允許創建的賬號數量999
	public static final boolean autoRegister = true; // enable = true or disable = false

	public static boolean success = false; // DONT CHANGE

	public static boolean getAccountExists(String login) {
		boolean accountExists = false;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getConnection();
			ps = con.prepareStatement("SELECT name FROM accounts WHERE name = ?");
			ps.setString(1, login);
			rs = ps.executeQuery();
			if (rs.first()) {
				accountExists = true;
			}
			ps.close();
			rs.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (rs != null) {
					rs.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return accountExists;
	}

	protected static String toUni(String kor) throws UnsupportedEncodingException {
		return new String(kor.getBytes("KSC5601"), "8859_1");
	}

	public static boolean checkAccount(String ip) {
		int checkCount = 0;

		String tempIp = ip.replaceAll("/", "");
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getConnection();
			ps = con.prepareStatement("SELECT * FROM accounts WHERE SessionIP = ?");
			ps.setString(1, tempIp);
			rs = ps.executeQuery();

			while (rs.next()) {
				checkCount++;
			}

			rs.close();
			ps.close();
			con.close();
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException ex) {
			}
		}

		return (checkCount >= ACCOUNTS_PER_IP);
	}

	public static boolean createAccount(String login, String pwd, String eip) {
		Connection con;
		String sockAddr = eip;
		boolean success = false;
		try {
			con = DatabaseConnection.getConnection();
		} catch (Exception ex) {
			ex.printStackTrace();
			return success;
		}
		try {
			PreparedStatement ipc = con.prepareStatement("SELECT SessionIP FROM accounts WHERE SessionIP = ?");
			try {
				ipc.setString(1, sockAddr.substring(1, sockAddr.lastIndexOf(':')));
				ResultSet rs = ipc.executeQuery();
				// if (!rs.first() || (rs.last() && rs.getRow() < 1))//修改記錄？？刪除此條件判斷 允許創建多個賬號
				try {
					PreparedStatement ps = con.prepareStatement(
							"INSERT INTO accounts (name, password, email, birthday, macs, SessionIP) VALUES (?, ?, ?, ?, ?, ?)");
					try {
						ps.setString(1, login);
						ps.setString(2, pwd);
						ps.setString(3, "no@email.provided");
						ps.setString(4, "2008-04-07");
						ps.setString(5, "00-00-00-00-00-00");
						ps.setString(6, sockAddr.substring(1, sockAddr.lastIndexOf(':')));
						ps.executeUpdate();
						ps.close();
						if (ps != null) {
							ps.close();
						}
					} catch (Throwable throwable) {
						if (ps != null)
							try {
								ps.close();
							} catch (Throwable throwable1) {
								throwable.addSuppressed(throwable1);
							}
						throw throwable;
					}
					success = true;
				} catch (SQLException ex2) {
					ex2.printStackTrace();
					boolean bool = success;
					if (ipc != null) {
						ipc.close();
					}
					return bool;
				}
				rs.close();
				ipc.close();
				if (ipc != null) {
					ipc.close();
				}
			} catch (Throwable throwable) {
				if (ipc != null)
					try {
						ipc.close();
					} catch (Throwable throwable1) {
						throwable.addSuppressed(throwable1);
					}
				throw throwable;
			}
		} catch (SQLException ex3) {
			ex3.printStackTrace();
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (SQLException e2) {
					e2.printStackTrace();
				}
		}
		return success;
	}

	public static boolean getDiscordExists(String discord) {
		boolean discordExists = false;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getConnection();
			ps = con.prepareStatement("SELECT discord FROM accounts WHERE discord = ?");
			ps.setString(1, discord);
			rs = ps.executeQuery();
			if (rs.first()) {
				discordExists = true;
			}
			ps.close();
			rs.close();
			con.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (rs != null) {
					rs.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return discordExists;
	}

	public static boolean connectorRegister(String id, String pw, String ip) {
		boolean create = false;
		KoreaCalendar kc = new KoreaCalendar();
		Connection con = null;
		PreparedStatement ps = null, psa = null, psb = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getConnection();
			ps = con.prepareStatement("SELECT * FROM koreareg WHERE ip = ?");
			ps.setString(1, ip);
			rs = ps.executeQuery();
			if (!rs.next()) {
				psa = con.prepareStatement(
						"INSERT INTO accounts (name, password, email, birthday, macs, SessionIP) VALUES (?, ?, ?, ?, ?, ?)");
				psa.setString(1, id);
				psa.setString(2, pw);
				psa.setString(3, "no@email.provided");
				psa.setString(4, "2008-04-07");
				psa.setString(5, "00-00-00-00-00-00");
				psa.setString(6, ip);
				psa.executeUpdate();
				psa.close();

				psb = con.prepareStatement("INSERT INTO koreareg (id, ip, time) VALUES (?, ?, ?)");
				psb.setString(1, id);
				psb.setString(2, ip);
				psb.setString(3, kc.getYears() + kc.getMonths() + kc.getDays());
				psb.executeUpdate();
				psb.close();
				create = true;
			}
			rs.close();
			ps.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (psa != null) {
					psa.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException se2) {
				se2.printStackTrace();
			}
		}
		return create;
	}

}
