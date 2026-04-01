/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package constants.Connector;

import constants.ServerConstants;
import database.DatabaseConnection;
import handling.login.handler.AutoRegister;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import server.control.MapleEtcControl;
import tools.Pair;
import tools.Triple;

/**
 *
 * @author KoreaDev <koreadev2@nate.com> & sakura24<x-s-s-@nate.com>
 *
 */
public class KoreaHandler {

	public static long UnixTime() {
		Calendar c = Calendar.getInstance();
		return c.getTimeInMillis() / 1000;
	}

	protected static String toUni(String kor) throws UnsupportedEncodingException {
		return new String(kor.getBytes("8859_1"), "KSC5601");
	}//

	public static void sendPacket(ChannelHandlerContext ctx, String packet) {
		ctx.writeAndFlush(Unpooled.copiedBuffer(packet, CharsetUtil.UTF_8));
	}

	public static void LoginRequest(String[] msg, ChannelHandlerContext ctx) {
		Connection con = null;
		boolean login = false;
		String id = msg[1];
		String pw = msg[2];
		try {
			con = DatabaseConnection.getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT * FROM accounts WHERE name=? AND password=?");
			ps.setString(1, id);
			ps.setString(2, pw);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				System.out.println("[通知]已從 " + ctx.channel().remoteAddress().toString().split(":")[0] + " 連接到遊戲服務器.");
				sendPacket(ctx, "登入,成功");
			} else {
				System.out.println("[通知]已從" + ctx.channel().remoteAddress().toString().split(":")[0] + " 連接到遊戲服務器失敗.");
				sendPacket(ctx, "登入,失敗");
			}

			rs.close();
			ps.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void RegisterRequest(String[] msg, ChannelHandlerContext ctx) {
		String id = msg[1];
		String pw = msg[2];
		if (AutoRegister.getAccountExists(id) == true) {
			sendPacket(ctx, "회원가입,존재");
		} else if (AutoRegister.connectorRegister(id, pw, ctx.channel().remoteAddress().toString().split(":")[0])) {
			sendPacket(ctx, "회원가입,성공");
		} else {
			sendPacket(ctx, "회원가입,실패");
		}
	}

	static void CRCSkill(String[] header, ChannelHandlerContext ctx) {
		String packet = "";
		try {
			FileInputStream setting = new FileInputStream("Connector/skill.properties");
			Properties setting_ = new Properties();
			setting_.load(setting);
			setting.close();
			List<String> wzName = new ArrayList<>();
			List<Long> wzSize = new ArrayList<>();
			for (Object a : setting_.stringPropertyNames()) {
				wzName.add(a.toString());
			}
			for (Object b : setting_.values()) {
				wzSize.add(Long.parseLong(b.toString()));
			}
			for (int i = 0; i < wzName.size(); i++) {
				packet += wzName.get(i) + "=" + wzSize.get(i) + ",";
			}
			sendPacket(ctx, "crcSkill," + packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static void CRCCharacter(String[] header, ChannelHandlerContext ctx) {
		String packet = "";
		try {
			FileInputStream setting = new FileInputStream("Connector/character.properties");
			Properties setting_ = new Properties();
			setting_.load(setting);
			setting.close();
			List<String> wzName = new ArrayList<>();
			List<Long> wzSize = new ArrayList<>();
			for (Object a : setting_.stringPropertyNames()) {
				wzName.add(a.toString());
			}
			for (Object b : setting_.values()) {
				wzSize.add(Long.parseLong(b.toString()));
			}
			for (int i = 0; i < wzName.size(); i++) {
				packet += wzName.get(i) + "=" + wzSize.get(i) + ",";
			}
			sendPacket(ctx, "crcCharacter," + packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static void CRCItem(String[] header, ChannelHandlerContext ctx) {
		String packet = "";
		try {
			FileInputStream setting = new FileInputStream("Connector/item.properties");
			Properties setting_ = new Properties();
			setting_.load(setting);
			setting.close();
			List<String> wzName = new ArrayList<>();
			List<Long> wzSize = new ArrayList<>();
			for (Object a : setting_.stringPropertyNames()) {
				wzName.add(a.toString());
			}
			for (Object b : setting_.values()) {
				wzSize.add(Long.parseLong(b.toString()));
			}
			for (int i = 0; i < wzName.size(); i++) {
				packet += wzName.get(i) + "=" + wzSize.get(i) + ",";
			}
			sendPacket(ctx, "crcItem," + packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static void CRCEffect(String[] header, ChannelHandlerContext ctx) {
		String packet = "";
		try {
			FileInputStream setting = new FileInputStream("Connector/effect.properties");
			Properties setting_ = new Properties();
			setting_.load(setting);
			setting.close();
			List<String> wzName = new ArrayList<>();
			List<Long> wzSize = new ArrayList<>();
			for (Object a : setting_.stringPropertyNames()) {
				wzName.add(a.toString());
			}
			for (Object b : setting_.values()) {
				wzSize.add(Long.parseLong(b.toString()));
			}
			for (int i = 0; i < wzName.size(); i++) {
				packet += wzName.get(i) + "=" + wzSize.get(i) + ",";
			}
			sendPacket(ctx, "crcEffect," + packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void GameStart(String[] header, ChannelHandlerContext ctx) {
		delLogin(header[1]);
		ServerConstants.cons.put(ctx.channel().remoteAddress().toString().split(":")[0],
				new Triple<>(header[1], header[2], ctx.channel().remoteAddress().toString().split(":")[0]));
		for (int i = 0; i < MapleEtcControl.lastHeartBeatTime.size(); i++) {
			if (MapleEtcControl.lastHeartBeatTime.get(i).left.equals(header[1])) {
				MapleEtcControl.lastHeartBeatTime.remove(i);
			}
		}
		MapleEtcControl.lastHeartBeatTime.add(new Pair<>(header[1], UnixTime()));
		ServerConstants.consHeartBeat.add(new Pair<>(header[1], UnixTime()));
		Connection con = null;
		try {
			con = DatabaseConnection.getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT * FROM korealogin WHERE id = ?");
			ps.setString(1, header[1]);
			ResultSet rs = ps.executeQuery();
			if (!rs.next()) {
				PreparedStatement pse = con.prepareStatement("INSERT INTO korealogin (id, ip, time) VALUES (?,?,?)");
				pse.setString(1, header[1]);
				pse.setString(2, ctx.channel().remoteAddress().toString().split(":")[0]);
				pse.setLong(3, UnixTime());
				pse.executeUpdate();
				pse.close();
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
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		System.out.println("[通知]已從 " + ctx.channel().remoteAddress().toString().split(":")[0] + " 連接到遊戲.");
	}

	public static void delLogin(String id) {
		Connection con = null;
		try {
			con = DatabaseConnection.getConnection();
			PreparedStatement ps = con.prepareStatement("DELETE FROM korealogin WHERE id = ?");
			ps.setString(1, id);
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
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}

	public static void Notice(String[] msg, ChannelHandlerContext ctx) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader("Connector/notice.properties"));
			String Notice = "";
			for (int i = 0; i < 9; i++) {
				Notice += reader.readLine() + "=";
			}
			reader.close();

			String[] notice = Notice.split("=");
			Notice = "";
			for (String a : notice) {
				if (a.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*")) {
					Notice += a + ",";
				} else if (a.matches(" ")) {
					Notice += a + ",";
				}
			}
			sendPacket(ctx, "notice," + Notice);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	static void HeartBeat(String[] msg, ChannelHandlerContext ctx) {
		String id = msg[1];
		long time = UnixTime();
		for (int i = 0; i < ServerConstants.consHeartBeat.size(); i++) {
			if (ServerConstants.consHeartBeat.get(i).left.equals(id)) {
				ServerConstants.consHeartBeat.remove(i);
			}
		}
		ServerConstants.consHeartBeat.add(new Pair<>(id, time));
	}

	static void CRCEtc(String[] header, ChannelHandlerContext ctx) {
		String packet = "";
		try {
			FileInputStream setting = new FileInputStream("Connector/etc.properties");
			Properties setting_ = new Properties();
			setting_.load(setting);
			setting.close();
			List<String> wzName = new ArrayList<>();
			List<Long> wzSize = new ArrayList<>();
			for (Object a : setting_.stringPropertyNames()) {
				wzName.add(a.toString());
			}
			for (Object b : setting_.values()) {
				wzSize.add(Long.parseLong(b.toString()));
			}
			for (int i = 0; i < wzName.size(); i++) {
				packet += wzName.get(i) + "=" + wzSize.get(i) + ",";
			}
			sendPacket(ctx, "crcEtc," + packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
