// CONNECTOR 소스
package security.connector;

import database.DatabaseConnection;
import handling.RecvPacketOpcode;
import handling.login.handler.AutoRegister;
import java.sql.*;
import tools.data.LittleEndianAccessor;

public class ConnectorServerHandler {

	public static byte loginCheck(String id, String password) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getConnection();
			ps = con.prepareStatement("SELECT name, password FROM accounts WHERE name = ?");
			ps.setString(1, id);
			rs = ps.executeQuery();

			if (rs.next()) {
				if (password.equals(rs.getString("password"))) {
					return 0;
				} else {
					return 1;
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

		return 2;
	}

	public static final void handlePacket(RecvPacketOpcode header, LittleEndianAccessor lea, ConnectorClient c) {
		switch (header) {
		case CHECK_CONNECT: {
			c.getSession().writeAndFlush("0"); // 연결 가능

			break;
		}
		case CREATE: { // 계정 생성
			String address = c.getSession().remoteAddress().toString().split(":")[0];

			String packet = lea.readAsciiString((int) lea.available());

			String[] tempData = packet.split(":");

			String id = tempData[0];
			String password = tempData[1];

			if (AutoRegister.checkAccount(address)) {
				c.getSession().writeAndFlush("2"); // 중복 가입

				return;
			}

			if (!AutoRegister.createAccount(id, password, address)) {
				c.getSession().writeAndFlush("1"); // 가입 실패

				return;
			}

			c.getSession().writeAndFlush("0"); // 가입 성공

			break;
		}
		case LOGIN: { // 로그인
			String packet = lea.readAsciiString((int) lea.available());

			String[] tempData = packet.split(":");

			String id = tempData[0];
			String password = tempData[1];

			byte type = loginCheck(id, password);

			if (type == 2) {
				c.getSession().writeAndFlush("2"); // 로그인 실패 (미가입)

				return;
			}

			if (type == 1) {
				c.getSession().writeAndFlush("1"); // 로그인 실패 (비밀번호)

				return;
			}

			c.getSession().writeAndFlush("0"); // 로그인 성공

			break;
		}
		case CHECK_FILE: { // 클라이언트 검사
			if (c.isConnectorHeartBeatStop()) {
				c.setConnectorHeartBeatStop(false);
			}

			String address = c.getSession().remoteAddress().toString().split(":")[0];

			String clientFileHash = lea.readAsciiString((int) lea.available());

			// System.err.println("Connector HASH : " + clientFileHash);
			c.setConnectorHeartBeat(true);

			if (ConnectorServer.on) {
				if (!ConnectorServer.serverFileHash.equals(clientFileHash)) {
					c.getSession().writeAndFlush("1");

					System.err.println("[Connector Server] " + address + " 클라이언트 변조 감지, 접속 끊기");

					c.cancelConnectorHeartBeat();

					if (c.getMapleClient() != null) {
						c.getMapleClient().disconnect(true, false);
						c.getMapleClient().getSession().close();
					}

					return;
				}
			}

			c.getSession().writeAndFlush("0");

			break;
		}

		default: {
			System.out.println("Unknown Header : " + header);
		}
		}
	}
}
