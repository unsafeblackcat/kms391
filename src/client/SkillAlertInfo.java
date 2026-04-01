package client;

import handling.SendPacketOpcode;
import tools.data.LittleEndianAccessor;
import tools.data.MaplePacketLittleEndianWriter;

public class SkillAlertInfo {
	enum KeyType {
		PREFIX("skill_alert"), SKILL_ID("skill_id"), STATE("state");

		private String key;

		KeyType(String key) {
			this.key = key;
		}

		@Override
		public String toString() {
			return this.key;
		}
	}

	private static final int MAX_INDEX = 6;
	private int skillId;
	private int index;
	private boolean isEnabled;

	public SkillAlertInfo(int skillId, int index, boolean isEnabled) {
		this.skillId = skillId;
		this.index = index;
		this.isEnabled = isEnabled;
	}

	public SkillAlertInfo(int index) {
		this(0, index, false);
	}

	public int getSkillId() {
		return skillId;
	}

	public int getIndex() {
		return index;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public void encode(MaplePacketLittleEndianWriter mplew) {
		mplew.writeInt(getSkillId());
		mplew.writeInt(getIndex());
		mplew.write((byte) (isEnabled() ? 1 : 0));
	}

	public void apply(MapleCharacter character) {
		setSkillId(character, this);
		setEnabled(character, this);
	}

	public static void handlePacket(LittleEndianAccessor lea, MapleClient client) {
		try {
			final SkillAlertType type = SkillAlertType.getFromType(lea.readInt());

			// 新增类型校验（关键修复点）
			if (type == SkillAlertType.NOT_DEFINED) {
				System.out.println(String.format(" 收到未知技能提醒类型，客户端IP：%s", client.getSession().remoteAddress()));
				return;
			}

			final SkillAlertInfo info = processAlertInfo(lea, type);
			final SkillAlertInfo swapInfo = (type == SkillAlertType.CHANGE_POSITION) ? processAlertInfo(lea, type)
					: null;

			if (info != null) {
				info.apply(client.getPlayer());
				if (swapInfo != null) {
					swapInfo.apply(client.getPlayer());
				}
				client.getSession().writeAndFlush(encode(type, info, swapInfo));
			}
		} catch (Exception e) {
			System.out.println(" 技能提醒处理异常：" + e.getMessage());
			e.printStackTrace(); // 打印完整堆栈
		}
	}

	private static SkillAlertInfo processAlertInfo(LittleEndianAccessor lea, SkillAlertType type) {
		switch (type) {
		case ADD:
		case UPDATE_STATE:
			return new SkillAlertInfo(lea.readInt(), lea.readInt(), lea.readByte() == 1);
		case REMOVE:
			return new SkillAlertInfo(lea.readInt());
		case CHANGE_POSITION:
			return new SkillAlertInfo(lea.readInt(), lea.readInt(), lea.readByte() == 1);
		// $CASES-OMITTED$
		default: // 包含NOT_DEFINED的情况
			return null;
		}
	}

	private static byte[] encode(SkillAlertType type, SkillAlertInfo info, SkillAlertInfo swapInfo) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
		mplew.writeShort(SendPacketOpcode.SKILL_ALERT.getValue());
		mplew.writeInt(type.getType());

		switch (type) {
		case ADD:
		case UPDATE_STATE:
			info.encode(mplew);
			break; // 修复缺失的break语句
		case REMOVE:
			mplew.writeInt(info.getIndex());
			break;
		case CHANGE_POSITION:
			info.encode(mplew);
			swapInfo.encode(mplew);
			break;
		case NOT_DEFINED: // 新增处理分支
			System.out.println("Attempting  to encode undefined alert type");
			break;
		}
		return mplew.getPacket();
	}

	public static void encodeForCharInfo(MaplePacketLittleEndianWriter mplew, MapleCharacter character) {
		for (int i = 0; i < MAX_INDEX; i++) {
			mplew.writeInt(getSkillId(character, i));
		}

		for (int i = 0; i < MAX_INDEX; i++) {
			mplew.write((isEnabled(character, i) ? (byte) 1 : (byte) 0));
		}
	}

	private static void setEnabled(MapleCharacter character, int index, boolean state) {
		character.addKV(getKey(KeyType.STATE, index), String.valueOf(state));
	}

	private static void setEnabled(MapleCharacter character, SkillAlertInfo info) {
		setEnabled(character, info.getIndex(), info.isEnabled());
	}

	private static boolean isEnabled(MapleCharacter character, int index) {
		return Boolean.parseBoolean(getValue(character, getKey(KeyType.STATE, index), Boolean.toString(false)));
	}

	private static void setSkillId(MapleCharacter character, int index, int skillId) {
		character.addKV(getKey(KeyType.SKILL_ID, index), String.valueOf(skillId));
	}

	private static void setSkillId(MapleCharacter character, SkillAlertInfo info) {
		setSkillId(character, info.getIndex(), info.getSkillId());
	}

	private static int getSkillId(MapleCharacter character, int index) {
		return Integer.parseInt(getValue(character, getKey(KeyType.SKILL_ID, index), String.valueOf(0)));
	}

	private static String getValue(MapleCharacter character, String key, String defaultValue) {
		if (character.getV(key) == null) {
			character.addKV(key, defaultValue);
		}
		return character.getV(key);
	}

	private static String getKey(KeyType keyType, int index) {
		switch (keyType) {
		case PREFIX:
			return String.format("%s_%s", keyType, index);
		// $CASES-OMITTED$
		default:
			return String.format("%s_%s", getKey(KeyType.PREFIX, index), keyType);
		}
	}
}
