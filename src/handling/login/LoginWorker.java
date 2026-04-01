package handling.login;

import client.MapleClient;

import constants.ServerConstants;
import handling.channel.ChannelServer;
import tools.packet.CWvsContext;
import tools.packet.LoginPacket;
import java.util.Map;

import static handling.login.handler.CharLoginHandler.ServerListRequest;

public class LoginWorker {

	public static void registerClient(final MapleClient c, final String id, String pwd) {
		if (ServerConstants.isReboot) {
			c.getSession().write(CWvsContext.serverNotice(1, "", "현재 서버가 리붓 중 입니다."));
			c.getSession().write(LoginPacket.getLoginFailed(20));
			return;
		}

		if (LoginServer.isAdminOnly() && !c.isGm() && !c.isLocalhost()) {
			c.getSession().writeAndFlush(CWvsContext.serverNotice(1, "", "현재 서버가 점검 중 입니다."));
			c.getSession().writeAndFlush(LoginPacket.getLoginFailed(21));
			return;
		}

		/*
		 * // CONNECTOR 소스 [S] if (c.getConnectorClient() == null) {
		 * c.getSession().writeAndFlush(CWvsContext.serverNotice(1, "",
		 * "전용 접속기로만 접속이 가능합니다."));
		 * c.getSession().writeAndFlush(LoginPacket.getLoginFailed(21)); return; }
		 * 
		 * if (c.getConnectorClient().isConnectorHeartBeatStop()) {
		 * c.getSession().writeAndFlush(CWvsContext.serverNotice(1, "",
		 * "전용 접속기가 응답하지 않습니다."));
		 * c.getSession().writeAndFlush(LoginPacket.getLoginFailed(21)); return; } //
		 * CONNECTOR 소스 [E]
		 * 
		 * // CRC 소스 [S] if (c.getCRCClient().isCRCHeartBeatStop()) {
		 * c.getSession().writeAndFlush(CWvsContext.serverNotice(1, "",
		 * "클라이언트가 응답하지 않습니다."));
		 * c.getSession().writeAndFlush(LoginPacket.getLoginFailed(21)); return; }
		 * 
		 * String systemSerialNumber = c.getCRCClient().getSystemSerialNumber(); /* if
		 * (systemSerialNumber.length() < 128) {
		 * c.getSession().writeAndFlush(CWvsContext.serverNotice(1, "",
		 * "시스템 정보 변조로 인해 로그인에 실패 하였습니다."));
		 * c.getSession().writeAndFlush(LoginPacket.getLoginFailed(21)); return; }
		 * 
		 * if (CRCServerHandler.checkSystemBan(systemSerialNumber)) {
		 * c.getSession().writeAndFlush(CWvsContext.serverNotice(1, "",
		 * "운영 정책 위반 및 불법 프로그램 이용 등으로 인해 게임 이용이 제한된 상태입니다."));
		 * c.getSession().writeAndFlush(LoginPacket.getLoginFailed(21)); return; }
		 * 
		 * c.getCRCClient().saveSystemSerialNumbers(systemSerialNumber); // CRC 소스 [E]
		 */

		if (System.currentTimeMillis() - lastUpdate > 600000) { // Update once every 10 minutes
			lastUpdate = System.currentTimeMillis();
			final Map<Integer, Integer> load = ChannelServer.getChannelLoad();
			int usersOn = 0;
			if (load == null || load.size() <= 0) { // In an unfortunate event that client logged in before load
				lastUpdate = 0;
				c.getSession().writeAndFlush(LoginPacket.getLoginFailed(7));
				return;
			}
			LoginServer.setLoad(load, usersOn);
			lastUpdate = System.currentTimeMillis();
		}
		if (c.finishLogin() == 0) {
			c.getSession().writeAndFlush(LoginPacket.checkLogin());
			// c.getSession().writeAndFlush(LoginPacket.successLogin());
			c.getSession().writeAndFlush(LoginPacket.getAuthSuccessRequest(c, id, pwd));
			ServerListRequest(c, false);
		} else {
			c.getSession().writeAndFlush(LoginPacket.getLoginFailed(7));
			return;
		}
	}

	private static long lastUpdate = 0L;
}
