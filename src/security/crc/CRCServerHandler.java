// CRC 소스
package security.crc;

import database.DatabaseConnection;
import handling.RecvPacketOpcode;
import java.sql.*;
import tools.data.LittleEndianAccessor;

public class CRCServerHandler {

	public static long findAccountId(String accountName) {
		long accountId = -1;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getConnection();
			ps = con.prepareStatement("SELECT id, name FROM accounts WHERE name = ?");
			ps.setString(1, accountName);
			rs = ps.executeQuery();

			if (rs.next()) {
				accountId = rs.getLong("id");
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

		return accountId;
	}

	public static String findAccountName(long accountId) {
		String accountName = "";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getConnection();
			ps = con.prepareStatement("SELECT id, name FROM accounts WHERE id = ?");
			ps.setLong(1, accountId);
			rs = ps.executeQuery();

			if (rs.next()) {
				accountName = rs.getString("name");
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

		return accountName;
	}

	public static long findPlayerId(String name) {
		long playerId = -1;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getConnection();
			ps = con.prepareStatement("SELECT accountid, name FROM characters WHERE name = ?");
			ps.setString(1, name);
			rs = ps.executeQuery();

			if (rs.next()) {
				playerId = rs.getLong("accountid");
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

		return playerId;
	}

	public static final boolean checkSystemBan(String hash) {
		boolean check = false;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getConnection();
			ps = con.prepareStatement("SELECT * FROM systemban");
			rs = ps.executeQuery();

			while (rs.next()) {
				if (hash.equals(rs.getString("systemSerialNumber"))) {
					check = true;
					break;
				}
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

		return check;
	}

	public static final String getSystemSerialNumber(String accountName) {
		String serialNumber = "No Data";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getConnection();
			ps = con.prepareStatement("SELECT * FROM accounts WHERE name = ?");
			ps.setString(1, accountName);
			rs = ps.executeQuery();

			if (rs.next()) {
				serialNumber = rs.getString("systemSerialNumber");
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

		return serialNumber;
	}

	public static final void systemBan(String hash) {
		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = DatabaseConnection.getConnection();
			ps = con.prepareStatement("INSERT INTO systemban VALUES (DEFAULT, ?)");
			ps.setString(1, hash);
			ps.executeUpdate();
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

			} catch (SQLException ex) {
			}
		}
	}

	public static final void systemUnBan(String hash) {
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = DatabaseConnection.getConnection();
			ps = con.prepareStatement("DELETE FROM systemban WHERE systemSerialNumber = ?");
			ps.setString(1, hash);
			ps.executeUpdate();
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
			} catch (SQLException ex) {
			}
		}
	}

	public static final void handlePacket(RecvPacketOpcode header, LittleEndianAccessor lea, CRCClient c) {
		switch (header) {
		case CHECK_CRC: { // 메모리 검사
			if (c.isCRCHeartBeatStop()) {
				c.setCRCHeartBeatStop(false);
			}

			String address = c.getSession().remoteAddress().toString().split(":")[0];

			String packet = lea.readAsciiString((int) lea.available());

			String[] tempHash = packet.split(":");

			String systemSerialHash = tempHash[0];
			String clientMemoryHash = tempHash[1];

			// System.err.println("CRC HASH : " + clientMemoryHash);
			boolean isLoggedIn = (c.getMapleClient().getAccID() != -1);

			if (c.getSystemSerialNumber().equals("No Data")) {
				c.setSystemSerialNumbers(systemSerialHash);
			}

			if (checkSystemBan(systemSerialHash)) {
				c.getSession().writeAndFlush("2");

				System.err.println("[CRC Server] " + address + " System Ban 감지, 접속 끊기");

				c.cancelCRCHeartBeat();

				if (c.getMapleClient() != null) {
					c.getMapleClient().disconnect(true, false);
					c.getMapleClient().getSession().close();
				}

				return;
			}

			c.setCRCHeartBeat(false);

			c.getSession().writeAndFlush("0");

			break;
		}

		default: {
			System.out.println("Unknown Header : " + header);
		}
		}
	}
}
