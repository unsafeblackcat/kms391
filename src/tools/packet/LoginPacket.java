package tools.packet;

import client.AvatarLook;
import client.MapleCharacter;
import client.MapleClient;
import constants.GameConstants;
import constants.JobConstants;
import constants.ServerConstants;
import handling.SendPacketOpcode;
import handling.channel.ChannelServer;
import handling.login.LoginServer;
import tools.CurrentTime;
import tools.HexTool;
import tools.data.MaplePacketLittleEndianWriter;

import java.util.List;
import java.util.Map;
import java.util.Set;
import server.Randomizer;
import tools.data.LittleEndianAccessor;

public class LoginPacket {

	private static final String version;

	static {
		int ret = 0;
		ret ^= (ServerConstants.MAPLE_VERSION & 0x7FFF);
		ret ^= (1 << 15);
		ret ^= ((ServerConstants.MAPLE_PATCH & 0xFF) << 16);
		version = String.valueOf(ret);
	}

	public static final byte[] initializeConnection(final short mapleVersion, final byte[] sendIv, final byte[] recvIv,
			final boolean ingame) {
		final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
		int ret = 0;
		ret ^= (mapleVersion & 0x7FFF);
		ret ^= (ServerConstants.check << 15);
		ret ^= ((ServerConstants.MAPLE_PATCH & 0xFF) << 16);
		String.valueOf(ret);

		int packetsize = ingame ? 16 : (42 + 2 + version.length());

		mplew.writeShort(packetsize);

		if (!ingame) {
			mplew.writeShort(291);
			mplew.writeMapleAsciiString(version);
			mplew.write(recvIv);
			mplew.write(sendIv);
			mplew.write(1); // locale
			mplew.write(0); // single thread loading
		}

		mplew.writeShort(291);
		mplew.writeInt(mapleVersion);
		mplew.write(recvIv);
		mplew.write(sendIv);
		mplew.write(1); // locale

		if (!ingame) {
			mplew.writeInt(mapleVersion * 100 + ServerConstants.MAPLE_PATCH);
			mplew.writeInt(mapleVersion * 100 + ServerConstants.MAPLE_PATCH);
			mplew.writeInt(0); // unknown
			mplew.write(false);
			mplew.write(false);
		}

		mplew.write(5);
		return mplew.getPacket();
	}

	public static final byte[] getHotfix() {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
		mplew.writeShort(SendPacketOpcode.HOTFIX.getValue());
		mplew.write(0);
		return mplew.getPacket();
	}

	public static final byte[] SessionCheck(int value) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
		mplew.writeShort(SendPacketOpcode.SESSION_CHECK.getValue());
		mplew.writeInt(value);
		return mplew.getPacket();
	}

	public static byte[] onOpcodeEncryption(int nBlockSize, byte[] aBuffer) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
		mplew.writeShort(SendPacketOpcode.OPCODE_ENCRYPTION.getValue());

		mplew.writeInt(aBuffer.length);
		mplew.write(aBuffer);

		return mplew.getPacket();
	}

	public static byte[] heartBeatFirst(int recv) {
		MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
		packet.writeShort(SendPacketOpcode.HEARTBEAT_SEND_FIRST.getValue());

		packet.writeInt(recv);
		packet.write(1);

		return packet.getPacket();
	}

	public static byte[] heartBeat() {
		MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
		packet.writeShort(SendPacketOpcode.HEARTBEAT_SEND.getValue());

		packet.writeLong(CurrentTime.ClientKoreanTime());
		packet.writeLong(CurrentTime.ClientKoreanTime2());

		return packet.getPacket();
	}

	public static final byte[] debugClient() {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
		mplew.writeShort(SendPacketOpcode.DEBUG_CLIENT.getValue());
		mplew.write(HexTool.getByteArrayFromHexString(
				"2D 00 00 00 06 00 00 00 91 00 00 00 00 00 00 00 03 00 41 6C 6C 0F 00 00 00 A3 00 00 00 40 9C 00 00 18 00 50 72 6F 6A 65 63 74 7C 43 65 6E 74 65 72 7C 57 6F 72 6C 64 49 44 7C 30 10 00 00 00 A3 00 00 00 40 9C 00 00 18 00 50 72 6F 6A 65 63 74 7C 43 65 6E 74 65 72 7C 57 6F 72 6C 64 49 44 7C 33 11 00 00 00 A3 00 00 00 30 75 00 00 18 00 50 72 6F 6A 65 63 74 7C 43 65 6E 74 65 72 7C 57 6F 72 6C 64 49 44 7C 35 12 00 00 00 A3 00 00 00 30 75 00 00 19 00 50 72 6F 6A 65 63 74 7C 43 65 6E 74 65 72 7C 57 6F 72 6C 64 49 44 7C 31 36 13 00 00 00 A4 00 00 00 78 00 00 00 18 00 50 72 6F 6A 65 63 74 7C 43 65 6E 74 65 72 7C 57 6F 72 6C 64 49 44 7C 30 14 00 00 00 A4 00 00 00 78 00 00 00 18 00 50 72 6F 6A 65 63 74 7C 43 65 6E 74 65 72 7C 57 6F 72 6C 64 49 44 7C 33 15 00 00 00 A4 00 00 00 78 00 00 00 18 00 50 72 6F 6A 65 63 74 7C 43 65 6E 74 65 72 7C 57 6F 72 6C 64 49 44 7C 35 16 00 00 00 A4 00 00 00 78 00 00 00 19 00 50 72 6F 6A 65 63 74 7C 43 65 6E 74 65 72 7C 57 6F 72 6C 64 49 44 7C 31 36 1E 00 00 00 B9 00 00 00 00 00 00 00 03 00 41 6C 6C 2F 00 00 00 A3 00 00 00 30 75 00 00 19 00 50 72 6F 6A 65 63 74 7C 43 65 6E 74 65 72 7C 57 6F 72 6C 64 49 44 7C 34 39 30 00 00 00 A3 00 00 00 30 75 00 00 19 00 50 72 6F 6A 65 63 74 7C 43 65 6E 74 65 72 7C 57 6F 72 6C 64 49 44 7C 34 38 31 00 00 00 A3 00 00 00 30 75 00 00 19 00 50 72 6F 6A 65 63 74 7C 43 65 6E 74 65 72 7C 57 6F 72 6C 64 49 44 7C 35 32 3C 00 00 00 A3 00 00 00 30 75 00 00 18 00 50 72 6F 6A 65 63 74 7C 43 65 6E 74 65 72 7C 57 6F 72 6C 64 49 44 7C 31 3E 00 00 00 A4 00 00 00 78 00 00 00 18 00 50 72 6F 6A 65 63 74 7C 43 65 6E 74 65 72 7C 57 6F 72 6C 64 49 44 7C 31 46 00 00 00 A3 00 00 00 30 75 00 00 19 00 50 72 6F 6A 65 63 74 7C 43 65 6E 74 65 72 7C 57 6F 72 6C 64 49 44 7C 35 34 49 00 00 00 10 01 00 00 02 00 00 00 03 00 41 6C 6C 4B 00 00 00 3D 01 00 00 00 5C 26 05 03 00 41 6C 6C 4D 00 00 00 C9 00 00 00 01 00 00 00 03 00 41 6C 6C 51 00 00 00 51 00 00 00 14 00 00 00 03 00 41 6C 6C 57 00 00 00 A4 00 00 00 78 00 00 00 19 00 50 72 6F 6A 65 63 74 7C 43 65 6E 74 65 72 7C 57 6F 72 6C 64 49 44 7C 34 36 58 00 00 00 A4 00 00 00 78 00 00 00 19 00 50 72 6F 6A 65 63 74 7C 43 65 6E 74 65 72 7C 57 6F 72 6C 64 49 44 7C 34 35 59 00 00 00 A3 00 00 00 40 9C 00 00 19 00 50 72 6F 6A 65 63 74 7C 43 65 6E 74 65 72 7C 57 6F 72 6C 64 49 44 7C 34 35 5B 00 00 00 47 01 00 00 EC FF FF FF 03 00 41 6C 6C 5C 00 00 00 48 01 00 00 01 00 00 00 03 00 41 6C 6C 69 00 00 00 F3 00 00 00 07 00 00 00 19 00 50 72 6F 6A 65 63 74 7C 43 65 6E 74 65 72 7C 57 6F 72 6C 64 49 44 7C 34 35 6B 00 00 00 A3 00 00 00 00 7D 00 00 19 00 50 72 6F 6A 65 63 74 7C 43 65 6E 74 65 72 7C 57 6F 72 6C 64 49 44 7C 34 36 6F 00 00 00 A3 00 00 00 30 75 00 00 19 00 50 72 6F 6A 65 63 74 7C 43 65 6E 74 65 72 7C 57 6F 72 6C 64 49 44 7C 34 34 79 00 00 00 F3 00 00 00 07 00 00 00 19 00 50 72 6F 6A 65 63 74 7C 43 65 6E 74 65 72 7C 57 6F 72 6C 64 49 44 7C 34 36 7A 00 00 00 F2 00 00 00 DC 05 00 00 03 00 41 6C 6C 7E 00 00 00 8C 01 00 00 00 00 00 00 03 00 41 6C 6C 85 00 00 00 F1 00 00 00 01 00 00 00 03 00 41 6C 6C 86 00 00 00 B1 01 00 00 00 00 00 00 03 00 41 6C 6C 88 00 00 00 AF 01 00 00 01 00 00 00 03 00 41 6C 6C 89 00 00 00 81 00 00 00 64 00 00 00 03 00 41 6C 6C 8A 00 00 00 2F 01 00 00 01 00 00 00 03 00 41 6C 6C 8B 00 00 00 BE 01 00 00 00 00 00 00 03 00 41 6C 6C 8D 00 00 00 B4 01 00 00 00 00 00 00 03 00 41 6C 6C 8F 00 00 00 AE 01 00 00 00 00 00 00 03 00 41 6C 6C 90 00 00 00 B9 01 00 00 96 00 00 00 03 00 41 6C 6C 91 00 00 00 9D 04 00 00 00 00 00 00 03 00 41 6C 6C 93 00 00 00 E9 01 00 00 00 00 00 00 03 00 41 6C 6C A4 00 00 00 41 02 00 00 01 00 00 00 03 00 41 6C 6C A5 00 00 00 08 02 00 00 00 00 00 00 03 00 41 6C 6C A9 00 00 00 5D 02 00 00 58 1B 00 00 03 00 41 6C 6C"));
		return mplew.getPacket();
	}

	public static final byte[] debugClient2(boolean first) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
		mplew.writeShort(SendPacketOpcode.DEBUG_CLIENT2.getValue());

		mplew.write(first ? 5 : 7);

		if (!first) {
			mplew.write(HexTool.getByteArrayFromHexString(
					"29 00 00 00 69 B2 1C E6 46 1D 69 48 90 5B 85 52 2A 77 B4 18 8A DE 74 40 2A EE E5 53 6E 50 A9 68 53 6B 1A 32 B5 47 82 06 F1 52 14 7C 00"));
		}

		return mplew.getPacket();
	}

	public static final byte[] HackShield() {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
		mplew.writeShort(SendPacketOpcode.HACKSHIELD.getValue());
		mplew.write(1);
		mplew.write(0);
		return mplew.getPacket();
	}

	public static final byte[] check27() {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
		mplew.writeShort(0x27);
		return mplew.getPacket();
	}

	public static final byte[] enableLogin() {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
		mplew.writeShort(SendPacketOpcode.ENABLE_LOGIN.getValue());
		return mplew.getPacket();
	}

	public static final byte[] checkLogin() {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
		mplew.writeShort(SendPacketOpcode.CHECK_LOGIN.getValue());
		mplew.write(0);
		mplew.write(1);
		mplew.write(1);
		return mplew.getPacket();
	}

	public static final byte[] successLogin() {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
		mplew.writeShort(SendPacketOpcode.SUCCESS_LOGIN.getValue());
		mplew.writeInt(0);
		mplew.writeInt(0);
		mplew.writeInt(1);
		mplew.writeInt(1);
		return mplew.getPacket();
	}

	public static final byte[] ingameNeeded1() {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
		mplew.writeShort(SendPacketOpcode.INGAME_NEEDED1.getValue());

		return mplew.getPacket();
	}

	public static final byte[] ingameNeeded2() {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
		mplew.writeShort(SendPacketOpcode.INGAME_NEEDED2.getValue());

		mplew.write(HexTool.getByteArrayFromHexString(
				"03 00 00 00 06 00 00 00 FC 03 00 00 2A 00 32 30 30 30 3A 31 7C 33 35 30 30 3A 32 7C 35 30 30 30 3A 33 7C 36 35 30 30 3A 34 7C 38 30 30 30 3A 35 7C 39 39 39 39 39 3A 36 03 00 41 6C 6C 13 00 00 00 1C 05 00 00 11 00 73 65 74 5F 69 6E 74 7C 31 30 34 31 33 30 31 34 36 03 00 41 6C 6C 14 00 00 00 19 05 00 00 0D 00 42 6C 61 63 6B 4C 69 73 74 7C 30 2C 35 03 00 41 6C 6C"));

		return mplew.getPacket();
	}

	public static final byte[] ingameNeeded3() {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
		mplew.writeShort(SendPacketOpcode.INGAME_NEEDED3.getValue());

		mplew.write(HexTool.getByteArrayFromHexString(
				"04 00 00 00 01 00 00 00 63 00 00 00 D0 07 00 00 00 00 00 00 00 00 00 00 64 00 00 00 C7 00 00 00 A0 0F 00 00 00 00 00 00 00 00 00 00 C8 00 00 00 03 01 00 00 40 1F 00 00 05 00 00 00 F4 01 00 00 04 01 00 00 2C 01 00 00 98 3A 00 00 05 00 00 00 E8 03 00 00"));

		return mplew.getPacket();
	}

	public static final byte[] getPing() {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
		mplew.writeShort(SendPacketOpcode.PING.getValue());

		return mplew.getPacket();
	}

	public static final byte[] getIngamePing() {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
		mplew.writeShort(SendPacketOpcode.INGAME_PING.getValue());

		mplew.write(HexTool.getByteArrayFromHexString(
				"90 FB 46 41 01 00 00 00 C0 09 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 A4 2E 00 00 90 01 00 00 89 54 24 10 48 89 4C 24 08 48 83 EC 18 C7 04 24 00 00 00 00 EB 0A 8B 44 24 28 FF C8 89 44 24 28 83 7C 24 28 01 72 25 48 8B 44 24 20 0F B6 00 33 44 24 20 8B 0C 24 03 C8 8B C1 89 04 24 48 8B 44 24 20 48 FF C0 48 89 44 24 20 EB CA 8B 04 24 48 83 C4 18 C3 CC CC CC CC CC CC CC CC CC CC CC CC 48 89 4C 24 08 48 81 EC A8 00 00 00 48 C7 84 24 80 00 00 00 FE FF FF FF C7 44 24 20 00 00 00 00 48 8D 05 79 FF FF FF 48 89 44 24 38 48 8D 05 FD 00 00 00 48 89 44 24 48 48 8B 44 24 38 48 8B 4C 24 48 48 2B C8 48 8B C1 48 89 44 24 28 48 8B 54 24 28 48 8D 4C 24 30 E8 64 E8 FF FF 90 48 8D 44 24 30 48 89 44 24 50 48 8B 44 24 50 48 89 44 24 58 48 8B 44 24 58 48 8B 00 48 89 44 24 60 4C 8B 44 24 28 48 8B 54 24 38 48 8B 4C 24 60 E8 2E 1B 2F 00 48 8D 44 24 30 48 89 44 24 68 4C 8D 44 24 28 48 8B 54 24 68 48 8D 8C 24 88 00 00 00 E8 8D E7 FF FF 48 89 44 24 40 48 8B 44 24 40 48 89 44 24 70 48 8B 84 24 B0 00 00 00 48 8B 54 24 70 48 8B C8 E8 E9 80 FD FF 48 8B 44 24 40 48 83 C0 08 48 89 44 24 78 48 8B 84 24 B0 00 00 00 48 8B 4C 24 78 8B 09 89 48 08 8B 44 24 20 83 C8 01 89 44 24 20 48 8D 84 24 88 00 00 00 48 8B C8 E8 6E 67 FE FF 90 48 8D 4C 24 30 E8 63 67 FE FF 90 48 8B 84 24 B0 00 00 00 48 81 C4 A8 00 00 00 C3 CC CC 50 79 42 44 01 00 00 00 80 00 00 00 01 3E 7C 02 00 00 00 00 00 BF 5A 34 00 00 00 00 00"));

		return mplew.getPacket();
	}

	public static final byte[] startCheckFiveMinute() {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
		mplew.writeShort(SendPacketOpcode.START_CHECK_FIVE_MINUTE.getValue());

		return mplew.getPacket();
	}

	public static final byte[] checkFiveMinute(LittleEndianAccessor slea) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
		mplew.writeShort(SendPacketOpcode.CHECK_FIVE_MINUTE.getValue());

		int recv = slea.readInt();

		mplew.writeInt(recv);

		mplew.writeLong(CurrentTime.ClientKoreanTime());
		mplew.writeLong(CurrentTime.ClientKoreanTime2());

		return mplew.getPacket();
	}

	public static final byte[] getAuthSuccessRequest(MapleClient client, String id, String pwd) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
		mplew.writeShort(SendPacketOpcode.LOGIN_STATUS.getValue());
		mplew.write(0);
		mplew.writeShort(0);
		mplew.writeMapleAsciiString(id);
		mplew.writeInt(client.getAccID());
		mplew.writeInt(0);
		mplew.writeInt(client.getAccID());
		mplew.write(0);
		mplew.writeInt(0);
		mplew.writeInt(0);
		mplew.writeInt(26);
		mplew.write(2);
		mplew.write((client.getChatBlockedTime() > 0L) ? 1 : 0);
		mplew.writeLong(client.getChatBlockedTime());
		mplew.write(0);
		mplew.writeShort(0);
		mplew.write(0);
		mplew.writeShort(1); // 1.2.390 ++
		mplew.writeShort(0);
		mplew.write(true);
		mplew.write(35);
		for (JobConstants.LoginJob j : JobConstants.LoginJob.values()) {
			mplew.write(j.getFlag());
			mplew.writeShort(j.getFlag());
		}
		mplew.write(0);
		mplew.writeInt(-1);
		mplew.writeZeroBytes(100);
		return mplew.getPacket();
	}

	public static final byte[] getLoginOtp(int what) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(16);
		mplew.writeShort(SendPacketOpcode.LOGIN_STATUS.getValue());
		mplew.writeInt(23);
		mplew.writeShort(what);
		return mplew.getPacket();
	}

	public static final byte[] getLoginFailed(int reason) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(16);
		mplew.writeShort(SendPacketOpcode.LOGIN_STATUS.getValue());
		mplew.writeInt(reason);
		mplew.writeShort(0);
		return mplew.getPacket();
	}

	public static final byte[] getPermBan(byte reason) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(16);
		mplew.writeShort(SendPacketOpcode.LOGIN_STATUS.getValue());
		mplew.writeShort(2);
		mplew.writeInt(0);
		mplew.writeShort(reason);
		mplew.write(HexTool.getByteArrayFromHexString("01 01 01 01 00"));
		return mplew.getPacket();
	}

	public static final byte[] getTempBan(long timestampTill, byte reason) {
		MaplePacketLittleEndianWriter w = new MaplePacketLittleEndianWriter(17);
		w.writeShort(SendPacketOpcode.LOGIN_STATUS.getValue());
		w.write(2);
		w.write(HexTool.getByteArrayFromHexString("00 00 00 00 00"));
		w.write(reason);
		w.writeLong(timestampTill);
		return w.getPacket();
	}

	public static final byte[] deleteCharResponse(int cid, int state) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
		mplew.writeShort(SendPacketOpcode.DELETE_CHAR_RESPONSE.getValue());
		mplew.writeInt(cid);
		mplew.write(state);
		if (state == 69) {
			mplew.write(0);
			mplew.write(0);
			mplew.write(0);
			mplew.write(0);
			mplew.writeInt(0);
		} else if (state == 71) {
			mplew.write(0);
		}
		mplew.write(0);
		mplew.write(0);
		return mplew.getPacket();
	}

	public static final byte[] secondPwError(byte mode) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(3);
		mplew.writeShort(SendPacketOpcode.SECONDPW_ERROR.getValue());
		mplew.write(mode);
		return mplew.getPacket();
	}

	public static byte[] enableRecommended() {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
		mplew.writeShort(SendPacketOpcode.ENABLE_RECOMMENDED.getValue());
		mplew.writeInt(0);
		return mplew.getPacket();
	}

	public static byte[] sendRecommended(int world, String message) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
		mplew.writeShort(SendPacketOpcode.SEND_RECOMMENDED.getValue());
		mplew.write((message != null) ? 1 : 0);
		if (message != null) {
			mplew.writeInt(world);
			mplew.writeMapleAsciiString(message);
		}
		return mplew.getPacket();
	}

	public static final byte[] Test() {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
		mplew.writeShort(0xFF00);

		for (int a = 0; a < 100000; a++) {
			mplew.write(0);
		}

		return mplew.getPacket();
	}

	public static final byte[] Test2() {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
		mplew.writeShort(0xFF00);

		for (int a = 0; a < 1500; a++) {
			mplew.write(0);
		}

		return mplew.getPacket();
	}

	public static final byte[] getServerList(int serverId, Map<Integer, Integer> channelLoad) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
		mplew.writeShort(SendPacketOpcode.SERVERLIST.getValue());
		mplew.write(serverId);
		String worldName = LoginServer.getServerName();
		mplew.writeMapleAsciiString(worldName);
		mplew.writeInt(0);
		mplew.writeInt(0);
		mplew.write(0);
		mplew.write(1);
		mplew.writeMapleAsciiString(LoginServer.getEventMessage());
		int lastChannel = 1;
		Set<Integer> channels = channelLoad.keySet();
		for (int i = 30; i > 0; i--) {
			if (channels.contains(Integer.valueOf(i))) {
				lastChannel = i;
				break;
			}
		}
		mplew.write(0);
		mplew.write(lastChannel);
		for (int j = 1; j <= lastChannel; j++) {
			int load;
			if (ChannelServer.getInstance(j) != null) {
				load = Math.max(1, ChannelServer.getInstance(j).getPlayerStorage().getAllCharacters().size());
			} else {
				load = 1;
			}
			mplew.writeMapleAsciiString(worldName + ((j == 1) ? ("-" + j) : ((j == 2) ? "- 20세이상" : ("-" + (j - 1)))));
			mplew.writeInt(load);
			mplew.write(serverId);
			mplew.write(j - 1);
			mplew.write(0);
		}
		mplew.writeShort(0);
		mplew.writeInt(0);
		mplew.write(1);
		mplew.writeInt(0);
		return mplew.getPacket();
	}

	public static final byte[] LeavingTheWorld() {
		MaplePacketLittleEndianWriter w = new MaplePacketLittleEndianWriter();
		w.writeShort(SendPacketOpcode.LEAVING_WORLD.getValue());
		w.write(3);
		w.writeMapleAsciiString("main");
		w.write(1);
		w.writeZeroBytes(8);
		w.writeMapleAsciiString("sub");
		w.writeZeroBytes(9);
		w.writeMapleAsciiString("sub_2");
		w.writeZeroBytes(9);
		w.write(1);
		return w.getPacket();
	}

	public static final byte[] getEndOfServerList() {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
		mplew.writeShort(SendPacketOpcode.SERVERLIST.getValue());
		mplew.write(255);
		int advertisement = 0;
		mplew.write(advertisement);
		for (int i = 0; i < advertisement; i++) {
			mplew.writeMapleAsciiString("");
			mplew.writeMapleAsciiString("");
			mplew.writeInt(5000);
			mplew.writeInt(310);
			mplew.writeInt(60);
			mplew.writeInt(235);
			mplew.writeInt(538);
		}
		mplew.write(0);
		mplew.writeInt(-1);
		mplew.writeInt(-1); // 1.2.390 Changed.
		return mplew.getPacket();
	}

	public static final byte[] getServerStatus(int status) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
		mplew.writeShort(SendPacketOpcode.SERVERSTATUS.getValue());
		mplew.writeShort(status);
		return mplew.getPacket();
	}

	public static final byte[] checkOTP(int status) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
		mplew.writeShort(SendPacketOpcode.CHECK_OTP.getValue());
		mplew.write(status);
		return mplew.getPacket();
	}

	public static final byte[] getCharList(MapleClient c, String secondpw, List<MapleCharacter> chars, int charslots,
			byte nameChange) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
		mplew.writeShort(SendPacketOpcode.CHARLIST.getValue());
		mplew.write(0);
		mplew.writeMapleAsciiString("");
		mplew.writeLong(PacketHelper.getKoreanTimestamp(System.currentTimeMillis())); // 1.2.390 Changed.
		mplew.writeInt(0); // 1.2.390 ++ (월드 정보)
		mplew.writeInt(c.getChannel() - 1); // 1.2.390 ++
		mplew.writeInt(1);
		mplew.writeInt(1);
		mplew.writeInt(1);
		mplew.writeInt(charslots);
		mplew.write(0);
		mplew.write(0);
		mplew.writeInt(0);
		// mplew.write(0); 1.2.391 --

		mplew.writeInt(chars.size());
		for (final MapleCharacter chr : chars) {
			mplew.writeInt(chr.getId());
		}

		mplew.write(chars.size());
		for (MapleCharacter chr : chars) {
			addCharEntry(mplew, chr, (!chr.isGM() && chr.getLevel() >= 30), false);
		}

		mplew.write(((secondpw != null && secondpw.length() > 0) || c.getSecondPw() == 1) ? 1
				: ((secondpw != null && secondpw.length() <= 0) ? 2 : 0));
		mplew.write(c.getSecondPw());
		mplew.write((c.getSecondPw() == 1) ? 0 : 1); // 캐릭터 생성 2차 비번 유무
		mplew.writeInt(charslots);
		mplew.writeInt(0);
		mplew.writeInt(-1);
		mplew.writeLong(1); // 휴면 계정 처리 패킷

		mplew.writeInt(1);

		mplew.write(0);
		mplew.write(0);

		mplew.writeInt(1);
		mplew.writeInt(0); // 월드리프 버튼
		mplew.writeInt(0);

		mplew.write(c.getSecondPw()); // 게임시작, 캐릭터 삭제 2차 비밀번호
		mplew.write(0);
		mplew.write(0);
		mplew.write(0);

		mplew.write(0);
		mplew.write(0);

		mplew.writeInt(1); // 1.2.371 ++

		return mplew.getPacket();
	}

	public static final byte[] addNewCharEntry(MapleCharacter chr, boolean worked) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
		mplew.writeShort(SendPacketOpcode.ADD_NEW_CHAR_ENTRY.getValue());

		mplew.write(worked ? 0 : 1);
		mplew.writeInt(0);

		addCharEntry(mplew, chr, false, false);

		// mplew.write(0); // 1.2.390 --
		// mplew.writeInt(-1); // 1.2.390 --
		mplew.write(0);

		return mplew.getPacket();
	}

	public static final byte[] charNameResponse(String charname, boolean nameUsed) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
		mplew.writeShort(SendPacketOpcode.CHAR_NAME_RESPONSE.getValue());
		mplew.writeMapleAsciiString(charname);
		mplew.write(nameUsed ? 1 : 0);
		return mplew.getPacket();
	}

	private static final void addCharEntry(MaplePacketLittleEndianWriter mplew, MapleCharacter chr, boolean ranking,
			boolean viewAll) {
		PacketHelper.addCharStats(mplew, chr);

		mplew.writeInt(0);
		mplew.writeLong(0);
		mplew.writeInt(0);
		mplew.writeInt(0);
		mplew.writeInt(1);

		// mplew.write(0); // 1.2.390 --
		if (GameConstants.isZero(chr.getJob())) {
			byte gender = chr.getGender(), secondGender = chr.getSecondGender();

			chr.setGender((byte) 0);
			chr.setSecondGender((byte) 1);
			AvatarLook.encodeAvatarLook(mplew, chr, true, false, false);

			chr.setGender((byte) 1);
			chr.setSecondGender((byte) 0);
			AvatarLook.encodeAvatarLook(mplew, chr, true, true, true);

			chr.setGender(gender);
			chr.setSecondGender(secondGender);
		} else {
			AvatarLook.encodeAvatarLook(mplew, chr, true, false, false);
		}
	}

	public static final byte[] getSecondPasswordConfirm(byte op) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
		mplew.writeShort(SendPacketOpcode.AUTH_STATUS_WITH_SPW.getValue());
		mplew.write(op);
		if (op == 0) {
			mplew.write(true);
			mplew.write(35);
			for (JobConstants.LoginJob j : JobConstants.LoginJob.values()) {
				mplew.write(j.getFlag());
				mplew.writeShort(j.getFlag());
			}
		}
		// mplew.writeZeroBytes(100);
		return mplew.getPacket();
	}

	public static byte[] NewSendPasswordWay(MapleClient c) {
		MaplePacketLittleEndianWriter w = new MaplePacketLittleEndianWriter();
		w.writeShort(SendPacketOpcode.NEW_PASSWORD_CHECK.getValue());
		int a = (c.getSecondPassword() == null || c.getSecondPassword().equals("초기화")) ? 0
				: ((c.getSecondPassword() != null) ? 1 : 0);
		w.write(a);
		w.write(0);
		return w.getPacket();
	}

	public static byte[] skipNewPasswordCheck(MapleClient c) {
		MaplePacketLittleEndianWriter w = new MaplePacketLittleEndianWriter();
		w.writeShort(SendPacketOpcode.SKIP_NEW_PASSWORD_CHECK.getValue());
		w.write(1);
		return w.getPacket();
	}

	public static final byte[] getSecondPasswordResult(boolean success) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
		mplew.writeShort(SendPacketOpcode.AUTH_STATUS_WITH_SPW_RESULT.getValue());
		mplew.write(success ? 0 : 20);
		return mplew.getPacket();
	}

	public static final byte[] MapleExit() {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
		mplew.writeShort(SendPacketOpcode.MAPLE_EXIT.getValue());
		return mplew.getPacket();
	}

	public static final byte[] Activate() {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
		mplew.writeShort(SendPacketOpcode.ACTIVATE.getValue());
		return mplew.getPacket();
	}

	public static byte[] ChannelBackImg(boolean isSunday) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
		mplew.writeShort(SendPacketOpcode.CHANNEL_BACK_IMG.getValue());
		if (isSunday) {
			mplew.writeMapleAsciiString("victoriaCup2");
		} else {
			mplew.writeMapleAsciiString("victoriaCup2");
		}
		return mplew.getPacket();
	}

	public static byte[] getSelectedChannelFailed(byte data, int ch) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
		mplew.writeShort(SendPacketOpcode.SELECT_CHANNEL_LIST.getValue());
		mplew.write(data);
		mplew.writeShort(0);
		mplew.writeInt(ch);
		mplew.writeInt(-1);

		return mplew.getPacket();
	}

	public static byte[] getCharacterLoad() {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
		mplew.writeShort(SendPacketOpcode.CHARACTER_LOAD.getValue());
		mplew.writeInt(10);
		mplew.writeInt(483224);
		mplew.writeInt(0);
		mplew.writeInt(0);
		mplew.writeInt(3);
		return mplew.getPacket();
	}

	public static byte[] getSelectedChannelResult(int ch) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
		mplew.writeShort(SendPacketOpcode.SELECT_CHANNEL_LIST.getValue());
		mplew.write(0);
		mplew.writeShort(0);
		mplew.writeInt(ch);
		mplew.writeInt((ch == 47) ? 1 : -1);
		return mplew.getPacket();
	}

	public static byte[] getSelectedWorldResult(int world) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
		mplew.writeShort(SendPacketOpcode.SELECTED_WORLD.getValue());
		mplew.writeInt(world);
		return mplew.getPacket();
	}

	public static final byte[] getKeyGuardResponse(String Key) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
		mplew.writeShort(SendPacketOpcode.LOG_OUT.getValue());
		mplew.writeMapleAsciiString(Key);
		mplew.writeInt(0); // 1.2.391 ++
		return mplew.getPacket();
	}

	public static final byte[] OTPChange(int type) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
		mplew.writeShort(SendPacketOpcode.OTP_CHANGE.getValue());
		mplew.write(3);
		mplew.write(type);
		return mplew.getPacket();
	}

	public static final byte[] getAuthSuccessRequest(MapleClient client) {
		MaplePacketLittleEndianWriter w = new MaplePacketLittleEndianWriter();
		w.writeShort(SendPacketOpcode.LOGIN_STATUS.getValue());
		w.write(0);
		w.writeShort(0);
		w.writeMapleAsciiString(client.getAccountName());
		w.writeLong(System.currentTimeMillis());
		w.writeInt(client.getAccID());
		w.write(client.isGm() ? 1 : 0);
		w.writeInt(client.isGm() ? 512 : 0);
		w.writeInt(0);
		w.writeInt(18);
		w.write(1);
		w.write((client.getChatBlockedTime() > 0L) ? 1 : 0);
		w.writeLong(client.getChatBlockedTime());
		w.write(1);
		w.write(0);
		w.writeMapleAsciiString("");
		w.write(true);
		w.write(35);
		for (JobConstants.LoginJob j : JobConstants.LoginJob.values()) {
			w.write(j.getFlag());
			w.writeShort(j.getFlag());
		}
		w.write(0);
		w.writeShort(1);

		w.write(0);
		w.writeInt(-1);
		return w.getPacket();
	}

	public static final byte[] getCharEndRequest(MapleClient client, String Acc, String Pwd, boolean Charlist) {
		MaplePacketLittleEndianWriter w = new MaplePacketLittleEndianWriter();
		w.writeShort(SendPacketOpcode.CHAR_END_REQUEST.getValue());
		w.write(0);
		w.writeShort(0);
		w.writeInt(client.getAccID());
		w.write(client.isGm() ? 1 : 0);
		w.writeInt(client.isGm() ? 32 : 0);
		w.writeInt(10);
		w.writeInt(20);
		w.write(99);
		w.write((client.getChatBlockedTime() > 0L) ? 1 : 0);
		w.writeLong(client.getChatBlockedTime());
		w.writeMapleAsciiString(Pwd);
		w.writeMapleAsciiString(Acc);
		w.writeMapleAsciiString("");
		w.write(true);
		w.write(35);
		for (JobConstants.LoginJob j : JobConstants.LoginJob.values()) {
			w.write(j.getFlag());
			w.writeShort(j.getFlag());
		}
		w.write(0);
		w.writeInt(-1);
		w.write(Charlist);
		w.write(0);
		return w.getPacket();
	}

	public static final void sendCRCCheckFileList(final MapleClient c) {
		MaplePacketLittleEndianWriter packet = new MaplePacketLittleEndianWriter();
		packet.writeShort(SendPacketOpcode.CRC_CHECK_FILE.getValue());
		Map<Integer, String> selectedMap = ServerConstants.fastLoad;
		int size = selectedMap.size();
		packet.writeInt(size);
		for (int a = 0; a < size; a++) {
			packet.writeMapleAsciiString(selectedMap.get(a + 1));
		}
		c.getSession().writeAndFlush(packet.getPacket());
	}
}
