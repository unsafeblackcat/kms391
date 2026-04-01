// CRC 소스
package security.crc;

import client.MapleClient;
import database.DatabaseConnection;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ScheduledFuture;
import server.Timer;

public class CRCClient {

	public static final AttributeKey<CRCClient> CRCKEY = AttributeKey.valueOf("crc_netty");

	private Channel session;

	private ScheduledFuture<?> crcHeartBeatTask;
	private boolean crcHeartBeatStop;
	private boolean crcHeartBeat;
	private String tempSerialNumbers;

	public CRCClient(Channel session, boolean crcHeartBeatStop) {
		this.session = session;

		this.crcHeartBeatStop = crcHeartBeatStop;
	}

	public Channel getSession() {
		return session;
	}

	public MapleClient getMapleClient() {
		return CRCServer.mapleClients.get(session.remoteAddress().toString().split(":")[0]);
	}

	public boolean isCRCHeartBeat() {
		return crcHeartBeat;
	}

	public void setCRCHeartBeat(boolean crcHeartBeat) {
		this.crcHeartBeat = crcHeartBeat;
	}

	public void cancelCRCHeartBeat() {
		if (this.crcHeartBeatTask != null) {
			this.crcHeartBeatTask.cancel(true);
			this.crcHeartBeatTask = null;
		}

		this.setCRCHeartBeatStop(true);
	}

	public boolean isCRCHeartBeatStop() {
		return crcHeartBeatStop;
	}

	public void setCRCHeartBeatStop(boolean crcHeartBeatStop) {
		this.crcHeartBeatStop = crcHeartBeatStop;
	}

	public void checkCRCHeartBeat() {
		crcHeartBeatTask = Timer.PingTimer.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (crcHeartBeatTask != null) {
					if (!isCRCHeartBeatStop()) {
						if (!isCRCHeartBeat()) {
							cancelCRCHeartBeat();

							System.err.println("[CRC Server] " + session.remoteAddress().toString().split(":")[0]
									+ " 하트비트 우회 감지, 접속 끊기");

							getMapleClient().disconnect(true, false);
							getMapleClient().getSession().close();

							getSession().close();
							return;
						}

						setCRCHeartBeat(false);
					}

					checkCRCHeartBeat();
				}
			}
		}, 15000);
	}

	public String getSystemSerialNumber() {
		if (tempSerialNumbers != null) {
			return tempSerialNumbers;
		}

		String tempSerialNumbers = null;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getConnection();
			ps = con.prepareStatement(
					"SELECT id, systemSerialNumber FROM accounts WHERE id = " + getMapleClient().getAccID());
			rs = ps.executeQuery();

			if (rs.next()) {
				tempSerialNumbers = rs.getString("systemSerialNumber");
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

		if (tempSerialNumbers == null) {
			tempSerialNumbers = "No Data";
		}

		return tempSerialNumbers;
	}

	public void setSystemSerialNumbers(String tempSerialNumbers) {
		this.tempSerialNumbers = tempSerialNumbers;
	}

	public void saveSystemSerialNumbers(String tempSerialNumbers) {
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = DatabaseConnection.getConnection();
			ps = con.prepareStatement("UPDATE accounts SET systemSerialNumber = '" + tempSerialNumbers + "' WHERE id = "
					+ getMapleClient().getAccID());
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

	public void banCRC() {
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = DatabaseConnection.getConnection();
			ps = con.prepareStatement("UPDATE accounts SET banned = 1, banreason = ? WHERE id = ?");
			ps.setString(1, "[CRC Server] 메모리 변조 감지");
			ps.setInt(2, getMapleClient().getAccID());
			ps.executeUpdate();
			ps.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
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

	public long getCharacterNameToAccountId(String name) {
		long ret = -1;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getConnection();
			ps = con.prepareStatement("SELECT `accountid`, `name` FROM `characters` WHERE name = '" + name + "'");
			rs = ps.executeQuery();

			if (rs.next()) {
				ret = rs.getLong("accountid");
			}

			rs.close();
			ps.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
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

		return ret;
	}

	public ArrayList<Long> getAllPlayerIds() { // 중복 접속 방지
		ArrayList<Long> ids = new ArrayList<>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getConnection();
			ps = con.prepareStatement("SELECT id, accountid FROM characters WHERE accountid = ?");
			ps.setLong(1, getMapleClient().getAccID());
			rs = ps.executeQuery();

			while (rs.next()) {
				ids.add(rs.getLong("id"));
			}

			rs.close();
			ps.close();
			con.close();
		} catch (Exception ex) {
			ex.printStackTrace();
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

		return ids;
	}
}
