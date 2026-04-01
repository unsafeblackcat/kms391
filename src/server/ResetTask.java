package server;

import client.MapleCharacter;
import java.sql.*;
import database.DatabaseConnection;
import handling.channel.ChannelServer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import tools.Pair;
import tools.packet.CWvsContext;

public class ResetTask implements Runnable {

	public ResetTask() {
		System.out.println("[加載已完成] 啟動重置任務系統");
	}

	@Override
	public void run() {
		final long time = System.currentTimeMillis();

		SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd/HH/mm-E");
		String[] now = format.format(time).split("-");
		String week = now[1];
		String day = now[0].split("/")[2];

		// 주간
		if (week.equals("목")) {
			// 아티팩트 미션 초기화
			resetArtipactMission(now[0], time);
		}

		// 월간
		if (day.equals("01")) {
			// 환생 정보 초기화
			resetRebornInfo(now[0], time);
		}
	}

	public static void resetArtipactMission(String now, final long time) {
		String name = "Artipact";

		if (!alreadyReset(name, time)) {
			Connection con = null;
			PreparedStatement ps = null;
			try {
				ArrayList<Pair<Integer, String>> datas = new ArrayList<>();

				datas.add(new Pair(503155,
						"attendance=4;mobCount=20000;resetDate=" + now + ";attendance_lastDate=" + now));
				datas.add(new Pair(503144, "missionState=22222222"));
				datas.add(new Pair(503153, "missionState=00000000000000000000000000000000000000000"));
				datas.add(new Pair(503135,
						"missionState=2222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222"));
				datas.add(new Pair(503136,
						"missionState=2222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222"));

				con = DatabaseConnection.getConnection();
				ps = con.prepareStatement("UPDATE `questinfo` SET `customData` = ? WHERE `quest` = ?");

				for (Pair<Integer, String> data : datas) {
					ps.setString(1, data.getRight());
					ps.setInt(2, data.getLeft());
					ps.executeUpdate();
				}
				ps.close();
				con.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
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

			for (ChannelServer cs : ChannelServer.getAllInstances()) {
				if (cs == null) {
					continue;
				}

				for (MapleCharacter player : cs.getPlayerStorage().getAllCharacters().values()) {
					if (player == null) {
						continue;
					}

					player.updateInfoQuest(503155,
							"attendance=4;mobCount=20000;resetDate=" + now + ";attendance_lastDate=" + now); // 주간 접속,
																												// 레벨 범위
																												// 몬스터
																												// 처치,
																												// 초기화
																												// 일자,
																												// 마지막
																												// 주간 접속
																												// 일자
					player.updateInfoQuest(503144, "missionState=22222222"); // 일반 미션
					player.updateInfoQuest(503153, "missionState=00000000000000000000000000000000000000000"); // 보스 미션
					player.updateInfoQuest(503135,
							"missionState=2222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222"); // 스페셜
																																					// 미션
																																					// 1
					player.updateInfoQuest(503136,
							"missionState=2222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222"); // 스페셜
																																					// 미션
																																					// 2

					player.getClient().getSession()
							.writeAndFlush(CWvsContext.InfoPacket.getArtipactMsg("보스 아티팩트 미션이 초기화 되었습니다."));
				}
			}

			saveReset(name, time);

			System.out.println("[通知]BOSS 神器任務已初始化.");
		}
	}

	public static void resetRebornInfo(String now, final long time) {
		String name = "Reborn";

		if (!alreadyReset(name, time)) {
			Connection con = null;
			PreparedStatement ps = null;
			try {
				con = DatabaseConnection.getConnection();
				ps = con.prepareStatement("UPDATE `questinfo` SET `customData` = ? WHERE `quest` = ?");

				ps.setString(1, "Reborn=0;Reborn Point=0;");
				ps.setInt(2, 7777778);
				ps.executeUpdate();

				ps.close();
				con.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
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

			for (ChannelServer cs : ChannelServer.getAllInstances()) {
				if (cs == null) {
					continue;
				}

				for (MapleCharacter player : cs.getPlayerStorage().getAllCharacters().values()) {
					if (player == null) {
						continue;
					}

					player.setKeyValue(7777778, "Reborn", 0 + "");
					player.setKeyValue(7777778, "Reborn Point", 0 + "");

					player.dropMessage(5, "환생 정보가 초기화 되었습니다.");
				}
			}

			saveReset(name, time);

			System.out.println("[通知]已初始化轉生資訊.");
		}
	}

	public static boolean alreadyReset(String name, final long time) {
		boolean already = false;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd");
			String date = format.format(time);

			con = DatabaseConnection.getConnection();
			ps = con.prepareStatement("SELECT `name`, `date` FROM `resetlist` WHERE `name` = ? AND `date` = ?");
			ps.setString(1, name);
			ps.setString(2, date);
			rs = ps.executeQuery();

			if (rs.next()) {
				already = true;
			}

			rs.close();
			ps.close();
			con.close();
		} catch (SQLException ex) {
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

		return already;
	}

	public static void saveReset(String name, final long time) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd");
			String date = format.format(time);

			con = DatabaseConnection.getConnection();
			ps = con.prepareStatement("SELECT `name` FROM `resetlist` WHERE `name` = ?");
			ps.setString(1, name);
			rs = ps.executeQuery();

			if (rs.next()) {
				ps = con.prepareStatement("UPDATE `resetlist` SET `date` = ? WHERE `name` = ?");
				ps.setString(1, date);
				ps.setString(2, name);
			} else {
				ps = con.prepareStatement("INSERT INTO `resetlist` VALUES (DEFAULT, ?, ?)");
				ps.setString(1, name);
				ps.setString(2, date);
			}
			ps.executeUpdate();

			rs.close();
			ps.close();
			con.close();
		} catch (SQLException ex) {
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
	}
}
