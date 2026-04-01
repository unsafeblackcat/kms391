package handling.channel.handler;

import client.HEXASkill;
import client.HEXAStat;
import client.MapleClient;
import client.inventory.MapleInventoryType;
import constants.GameConstants;
import java.util.ArrayList;
import server.MapleInventoryManipulator;
import server.Randomizer;
import tools.Pair;
import tools.data.LittleEndianAccessor;
import tools.packet.CField;

public class HEXAHandler {

	public static ArrayList<HEXASkill> hexaSkillData(MapleClient c) {
		ArrayList<HEXASkill> hexas = new ArrayList<>();

		int jobId = c.getPlayer().getJob();
		int jobOrder = GameConstants.jobOrders.indexOf(jobId);

		int originFirstSkill = GameConstants.getHexaOriginFirst(jobId);
		int originFirstSkillLevel = c.getPlayer().getSkillLevel(originFirstSkill);

		hexas.add(new HEXASkill((10000000 + jobOrder), originFirstSkillLevel));

		int activeFirstSkill = GameConstants.getHexaActiveFirst(jobId);
		int activeFirstSkillLevel = c.getPlayer().getSkillLevel(activeFirstSkill);

		hexas.add(new HEXASkill((20000000 + jobOrder), activeFirstSkillLevel));

		int activeSecondSkill = GameConstants.getHexaActiveSecond(c.getPlayer(), 0, true);
		int activeSecondSkillLevel = c.getPlayer().getSkillLevel(activeSecondSkill);

		hexas.add(new HEXASkill((20000050 + jobOrder), activeSecondSkillLevel));

		int enhanceFirstSkill = (GameConstants.getHexaEnhanceFirst(jobOrder)
				- GameConstants.getDifferencePatchNumber(jobId));
		int enhanceFirstSkillLevel = c.getPlayer().getSkillLevel(enhanceFirstSkill);

		hexas.add(new HEXASkill((30000000 + (jobOrder * 4) - GameConstants.getDifferencePatchNumber(jobId)),
				enhanceFirstSkillLevel));

		int enhanceSecondSkill = (GameConstants.getHexaEnhanceSecond(jobOrder)
				- GameConstants.getDifferencePatchNumber(jobId));
		int enhanceSecondSkillLevel = c.getPlayer().getSkillLevel(enhanceSecondSkill);

		hexas.add(new HEXASkill((30000001 + (jobOrder * 4) - GameConstants.getDifferencePatchNumber(jobId)),
				enhanceSecondSkillLevel));

		int enhanceThirdSkill = (GameConstants.getHexaEnhanceThird(jobOrder)
				- GameConstants.getDifferencePatchNumber(jobId));
		int enhanceThirdSkillLevel = c.getPlayer().getSkillLevel(enhanceThirdSkill);

		hexas.add(new HEXASkill((30000002 + (jobOrder * 4) - GameConstants.getDifferencePatchNumber(jobId)),
				enhanceThirdSkillLevel));

		int enhanceFourthSkill = (GameConstants.getHexaEnhanceFourth(jobOrder)
				- GameConstants.getDifferencePatchNumber(jobId));
		if (GameConstants.isXenon(c.getPlayer().getJob())
				&& GameConstants.getHexaEnhanceFourth(jobOrder) == 500004123) {
			if (c.getPlayer().getSkillLevel(500004127) > 0) {
				enhanceFourthSkill = 500004127;
				hexas.add(new HEXASkill(30000127, c.getPlayer().getSkillLevel(500004127)));
			}
			if (c.getPlayer().getSkillLevel(500004126) > 0) {
				enhanceFourthSkill = 500004126;
				hexas.add(new HEXASkill(30000126, c.getPlayer().getSkillLevel(500004126)));
			}
		}
		int enhanceFourthSkillLevel = c.getPlayer().getSkillLevel(enhanceFourthSkill);
		if (enhanceFourthSkill != 500004127 && enhanceFourthSkill != 500004126) {
			hexas.add(new HEXASkill((30000003 + (jobOrder * 4) - GameConstants.getDifferencePatchNumber(jobId)),
					enhanceFourthSkillLevel));
		}
		int solJanusSkill = 500001000;
		int solJanusSkillLevel = c.getPlayer().getSkillLevel(solJanusSkill);

		hexas.add(new HEXASkill(40000000, solJanusSkillLevel));

		return hexas;
	}

	public static ArrayList<HEXAStat> hexaStatData(MapleClient c) {
		ArrayList<HEXAStat> hexas = new ArrayList<>();

		int[] hexaStatList = { 50000000 };
		for (int i = 0; i < hexaStatList.length; i++) {
			if (c.getPlayer().getKeyValue(hexaStatList[i], "HEXA Stat M S") != -1) {
				hexas.add(new HEXAStat(hexaStatList[i],
						new Pair<Integer, Integer>((int) c.getPlayer().getKeyValue(hexaStatList[i], "HEXA Stat M S"),
								(int) c.getPlayer().getKeyValue(hexaStatList[i], "HEXA Stat M L")),
						new Pair<Integer, Integer>((int) c.getPlayer().getKeyValue(hexaStatList[i], "HEXA Stat A1 S"),
								(int) c.getPlayer().getKeyValue(hexaStatList[i], "HEXA Stat A1 L")),
						new Pair<Integer, Integer>((int) c.getPlayer().getKeyValue(hexaStatList[i], "HEXA Stat A2 S"),
								(int) c.getPlayer().getKeyValue(hexaStatList[i], "HEXA Stat A2 L"))));
			}
		}

		return hexas;
	}

	public static void activeHexaSkill(LittleEndianAccessor slea, MapleClient c, int type) {
		int coreId = slea.readInt();

		int jobId = c.getPlayer().getJob();
		int jobOrder = GameConstants.jobOrders.indexOf(jobId);

		int commonCoreId = 0;

		if (coreId >= 40000000) {
			commonCoreId = coreId;
		} else if (coreId >= 30000000) {
			commonCoreId = (coreId - (jobOrder * 4));
		} else {
			commonCoreId = (coreId - jobOrder);
		}
		if (commonCoreId >= 40000000) {
			c.getPlayer().gainSolErda(-7);

			MapleInventoryManipulator.removeById(c, MapleInventoryType.ETC, 4009547, 125, false, true);

			c.getPlayer().changeSkillLevel(500001000, (byte) 1, (byte) 1);
		} else if ((commonCoreId + GameConstants.getDifferencePatchNumber(jobId)) >= 30000003) {
			c.getPlayer().gainSolErda(-4);
			MapleInventoryManipulator.removeById(c, MapleInventoryType.ETC, 4009547, 75, false, true);

			if (GameConstants.isXenon(c.getPlayer().getJob())
					&& GameConstants.getHexaEnhanceFourth(jobOrder) == 500004123) {
				if (commonCoreId == 30000007) {
					c.getPlayer().changeSkillLevel((GameConstants.getHexaEnhanceFourth(jobOrder) + 4), (byte) 1,
							(byte) 1);
				} else {
					c.getPlayer().changeSkillLevel((GameConstants.getHexaEnhanceFourth(jobOrder) + 3), (byte) 1,
							(byte) 1);
				}

			} else {
				c.getPlayer().changeSkillLevel(
						(GameConstants.getHexaEnhanceFourth(jobOrder) - GameConstants.getDifferencePatchNumber(jobId)),
						(byte) 1, (byte) 1);
			}

		} else if ((commonCoreId + GameConstants.getDifferencePatchNumber(jobId)) >= 30000002) {
			c.getPlayer().gainSolErda(-4);

			MapleInventoryManipulator.removeById(c, MapleInventoryType.ETC, 4009547, 75, false, true);

			c.getPlayer().changeSkillLevel(
					(GameConstants.getHexaEnhanceThird(jobOrder) - GameConstants.getDifferencePatchNumber(jobId)),
					(byte) 1, (byte) 1);
		} else if ((commonCoreId + GameConstants.getDifferencePatchNumber(jobId)) >= 30000001) {
			c.getPlayer().gainSolErda(-4);

			MapleInventoryManipulator.removeById(c, MapleInventoryType.ETC, 4009547, 75, false, true);

			c.getPlayer().changeSkillLevel(
					(GameConstants.getHexaEnhanceSecond(jobOrder) - GameConstants.getDifferencePatchNumber(jobId)),
					(byte) 1, (byte) 1);
		} else if ((commonCoreId + GameConstants.getDifferencePatchNumber(jobId)) >= 30000000) {
			c.getPlayer().gainSolErda(-4);

			MapleInventoryManipulator.removeById(c, MapleInventoryType.ETC, 4009547, 75, false, true);

			c.getPlayer().changeSkillLevel(
					(GameConstants.getHexaEnhanceFirst(jobOrder) - GameConstants.getDifferencePatchNumber(jobId)),
					(byte) 1, (byte) 1);
		} else if (commonCoreId >= 20000050) {
			c.getPlayer().gainSolErda(-3);

			MapleInventoryManipulator.removeById(c, MapleInventoryType.ETC, 4009547, 50, false, true);

			int count = 0;
			int skillId = 0;
			while (skillId != -1) {
				skillId = GameConstants.getHexaActiveSecond(c.getPlayer(), count, false);

				if (skillId != 0) {
					c.getPlayer().changeSkillLevel(skillId, (byte) 1, (byte) 1);
				}

				count++;
			}

			ArrayList<Integer> options = GameConstants.getHexaActiveSecondOption(jobId);
			for (Integer option : options) {
				c.getPlayer().changeSkillLevel(option, (byte) 1, (byte) 1);
			}
		} else {
			c.getPlayer().gainSolErda(-3);

			MapleInventoryManipulator.removeById(c, MapleInventoryType.ETC, 4009547, 50, false, true);

			c.getPlayer().changeSkillLevel(GameConstants.getHexaActiveFirst(jobId), (byte) 1, (byte) 1);

			ArrayList<Integer> options = GameConstants.getHexaActiveFirstOption(jobId);
			for (Integer option : options) {
				c.getPlayer().changeSkillLevel(option, (byte) 1, (byte) 1);
			}
		}

		c.getSession().writeAndFlush(CField.updateHexaCore(c));
		c.getSession().writeAndFlush(CField.messageHexaCore(type, 0, new HEXASkill(coreId, 1), true));
	}

	public static void enhanceHexaSkill(LittleEndianAccessor slea, MapleClient c, int type) {
		int coreId = slea.readInt();
		int beforeLevel = slea.readInt();
		int afterLevel = slea.readInt();
		int solErdaQuantity = slea.readInt();
		int solErdaPieceQuantity = slea.readInt();

		int jobId = c.getPlayer().getJob();
		int jobOrder = GameConstants.jobOrders.indexOf(jobId);

		int commonCoreId = 0;

		if (coreId >= 40000000) {
			commonCoreId = coreId;
		} else if (coreId >= 30000000) {
			commonCoreId = (coreId - (jobOrder * 4));
		} else {
			commonCoreId = (coreId - jobOrder);
		}

		c.getPlayer().gainSolErda(-solErdaQuantity);

		MapleInventoryManipulator.removeById(c, MapleInventoryType.ETC, 4009547, solErdaPieceQuantity, false, true);

		if (commonCoreId >= 40000000) {
			c.getPlayer().changeSkillLevel(500001000, (byte) afterLevel, (byte) afterLevel);
		} else if ((commonCoreId + GameConstants.getDifferencePatchNumber(jobId)) >= 30000003) {
			if (GameConstants.isXenon(c.getPlayer().getJob())
					&& GameConstants.getHexaEnhanceFourth(jobOrder) == 500004123) {
				if (commonCoreId == 30000007) {
					c.getPlayer().changeSkillLevel((GameConstants.getHexaEnhanceFourth(jobOrder) + 4),
							(byte) afterLevel, (byte) afterLevel);
				} else {
					c.getPlayer().changeSkillLevel((GameConstants.getHexaEnhanceFourth(jobOrder) + 3),
							(byte) afterLevel, (byte) afterLevel);
				}
			} else {
				c.getPlayer().changeSkillLevel(
						(GameConstants.getHexaEnhanceFourth(jobOrder) - GameConstants.getDifferencePatchNumber(jobId)),
						(byte) afterLevel, (byte) afterLevel);
			}
		} else if ((commonCoreId + GameConstants.getDifferencePatchNumber(jobId)) >= 30000002) {
			c.getPlayer().changeSkillLevel(
					(GameConstants.getHexaEnhanceThird(jobOrder) - GameConstants.getDifferencePatchNumber(jobId)),
					(byte) afterLevel, (byte) afterLevel);
		} else if ((commonCoreId + GameConstants.getDifferencePatchNumber(jobId)) >= 30000001) {
			c.getPlayer().changeSkillLevel(
					(GameConstants.getHexaEnhanceSecond(jobOrder) - GameConstants.getDifferencePatchNumber(jobId)),
					(byte) afterLevel, (byte) afterLevel);
		} else if ((commonCoreId + GameConstants.getDifferencePatchNumber(jobId)) >= 30000000) {
			c.getPlayer().changeSkillLevel(
					(GameConstants.getHexaEnhanceFirst(jobOrder) - GameConstants.getDifferencePatchNumber(jobId)),
					(byte) afterLevel, (byte) afterLevel);
		} else if (commonCoreId >= 20000050) {
			int count = 0;
			int skillId = 0;
			while (skillId != -1) {
				skillId = GameConstants.getHexaActiveSecond(c.getPlayer(), count, false);

				if (skillId != 0) {
					c.getPlayer().changeSkillLevel(skillId, (byte) afterLevel, (byte) afterLevel);
				}

				count++;
			}

			ArrayList<Integer> options = GameConstants.getHexaActiveSecondOption(jobId);
			for (Integer option : options) {
				c.getPlayer().changeSkillLevel(option, (byte) afterLevel, (byte) afterLevel);
			}
		} else if (commonCoreId >= 20000000) {
			c.getPlayer().changeSkillLevel(GameConstants.getHexaActiveFirst(jobId), (byte) afterLevel,
					(byte) afterLevel);

			ArrayList<Integer> options = GameConstants.getHexaActiveFirstOption(jobId);
			for (Integer option : options) {
				c.getPlayer().changeSkillLevel(option, (byte) afterLevel, (byte) afterLevel);
			}
		} else {
			c.getPlayer().changeSkillLevel(GameConstants.getHexaOriginFirst(jobId), (byte) afterLevel,
					(byte) afterLevel);
		}

		c.getSession().writeAndFlush(CField.updateHexaCore(c));
		c.getSession()
				.writeAndFlush(CField.messageHexaCore(type, beforeLevel, new HEXASkill(coreId, afterLevel), true));
	}

	public static void activeHexaStat(LittleEndianAccessor slea, MapleClient c, int type) {
		int coreId = slea.readInt();

		c.getPlayer().gainSolErda(-5);

		MapleInventoryManipulator.removeById(c, MapleInventoryType.ETC, 4009547, 10, false, true);

		switch (coreId) {
		case 50000000:
			c.getPlayer().setKeyValue(coreId, "HEXA Stat M S", slea.readInt() + "");
			c.getPlayer().setKeyValue(coreId, "HEXA Stat M L", 0 + "");
			c.getPlayer().setKeyValue(coreId, "HEXA Stat A1 S", slea.readInt() + "");
			c.getPlayer().setKeyValue(coreId, "HEXA Stat A1 L", 0 + "");
			c.getPlayer().setKeyValue(coreId, "HEXA Stat A2 S", slea.readInt() + "");
			c.getPlayer().setKeyValue(coreId, "HEXA Stat A2 L", 0 + "");
			break;
		}

		HEXAStat hexa = new HEXAStat(coreId,
				new Pair<Integer, Integer>((int) c.getPlayer().getKeyValue(coreId, "HEXA Stat M S"),
						(int) c.getPlayer().getKeyValue(coreId, "HEXA Stat M L")),
				new Pair<Integer, Integer>((int) c.getPlayer().getKeyValue(coreId, "HEXA Stat A1 S"),
						(int) c.getPlayer().getKeyValue(coreId, "HEXA Stat A1 L")),
				new Pair<Integer, Integer>((int) c.getPlayer().getKeyValue(coreId, "HEXA Stat A2 S"),
						(int) c.getPlayer().getKeyValue(coreId, "HEXA Stat A2 L")));

		c.getPlayer().setKeyValue(hexa.getCoreId(), "HEXA Stat EQUIP INDEX", 0 + "");

		c.getPlayer().changeSkillLevel(50071000, (byte) 1, (byte) 1);

		c.getSession().writeAndFlush(CField.activeHexaStat(c));
		c.getSession().writeAndFlush(CField.messageHexaCore(type, 0, hexa, true));
	}

	public static void enhanceHexaStat(LittleEndianAccessor slea, MapleClient c, int type) {
		int coreId = slea.readInt();

		int mainStatLevel = (int) c.getPlayer().getKeyValue(coreId, "HEXA Stat M L");

		int quantity = 0;
		int chance = 0;

		switch (mainStatLevel) {
		case 0:
		case 1:
		case 2:
			quantity = 10;
			chance = 35;
			break;
		case 3:
		case 4:
		case 5:
		case 6:
			quantity = 20;
			chance = 20;
			break;
		case 7:
			quantity = 30;
			chance = 15;
			break;
		case 8:
			quantity = 30;
			chance = 10;
			break;
		case 9:
			quantity = 50;
			chance = 5;
			break;
		case 10:
			quantity = 50;
			chance = 0;
			break;
		}

		MapleInventoryManipulator.removeById(c, MapleInventoryType.ETC, 4009547, quantity, false, true);

		if (Randomizer.isSuccess(chance)) {
			c.getPlayer().setKeyValue(coreId, "HEXA Stat M L",
					(int) (c.getPlayer().getKeyValue(coreId, "HEXA Stat M L") + 1) + "");
		} else {
			if (Randomizer.isSuccess(50)) {
				if (c.getPlayer().getKeyValue(coreId, "HEXA Stat A1 L") >= 10) {
					c.getPlayer().setKeyValue(coreId, "HEXA Stat A2 L",
							(int) (c.getPlayer().getKeyValue(coreId, "HEXA Stat A2 L") + 1) + "");
				} else {
					c.getPlayer().setKeyValue(coreId, "HEXA Stat A1 L",
							(int) (c.getPlayer().getKeyValue(coreId, "HEXA Stat A1 L") + 1) + "");
				}
			} else {
				if (c.getPlayer().getKeyValue(coreId, "HEXA Stat A2 L") >= 10) {
					c.getPlayer().setKeyValue(coreId, "HEXA Stat A1 L",
							(int) (c.getPlayer().getKeyValue(coreId, "HEXA Stat A1 L") + 1) + "");
				} else {
					c.getPlayer().setKeyValue(coreId, "HEXA Stat A2 L",
							(int) (c.getPlayer().getKeyValue(coreId, "HEXA Stat A2 L") + 1) + "");
				}
			}
		}

		c.getSession().writeAndFlush(CField.activeHexaStat(c));
		c.getSession().writeAndFlush(CField.messageHexaCore(type, 0, null, true));
	}

	public static void saveHexaStat(LittleEndianAccessor slea, MapleClient c, int type) {
		int coreId = slea.readInt();

		c.getPlayer().setKeyValue((coreId + 666666), "HEXA Stat M S",
				c.getPlayer().getKeyValue(coreId, "HEXA Stat M S") + "");
		c.getPlayer().setKeyValue((coreId + 666666), "HEXA Stat M L",
				c.getPlayer().getKeyValue(coreId, "HEXA Stat M L") + "");
		c.getPlayer().setKeyValue((coreId + 666666), "HEXA Stat A1 S",
				c.getPlayer().getKeyValue(coreId, "HEXA Stat A1 S") + "");
		c.getPlayer().setKeyValue((coreId + 666666), "HEXA Stat A1 L",
				c.getPlayer().getKeyValue(coreId, "HEXA Stat A1 L") + "");
		c.getPlayer().setKeyValue((coreId + 666666), "HEXA Stat A2 S",
				c.getPlayer().getKeyValue(coreId, "HEXA Stat A2 S") + "");
		c.getPlayer().setKeyValue((coreId + 666666), "HEXA Stat A2 L",
				c.getPlayer().getKeyValue(coreId, "HEXA Stat A2 L") + "");

		c.getSession().writeAndFlush(CField.activeHexaStat(c));
		c.getSession().writeAndFlush(CField.messageHexaCore(type, 0, null, true));
	}

	public static void changeHexaStatSlot(LittleEndianAccessor slea, MapleClient c, int type) {
		int coreId = slea.readInt();
		int index = slea.readInt();

		c.getPlayer().setKeyValue(coreId, "HEXA Stat EQUIP INDEX", index + "");

		c.getSession().writeAndFlush(CField.activeHexaStat(c));
		c.getSession().writeAndFlush(CField.messageHexaCore(type, 0, null, true));
	}

	public static void resetHexaStat(LittleEndianAccessor slea, MapleClient c, int type) {
		int coreId = slea.readInt();

		c.getPlayer().setKeyValue(coreId, "HEXA Stat M L", 0 + "");
		c.getPlayer().setKeyValue(coreId, "HEXA Stat A1 L", 0 + "");
		c.getPlayer().setKeyValue(coreId, "HEXA Stat A2 L", 0 + "");

		c.getPlayer().gainMeso(-10000000, true);

		c.getSession().writeAndFlush(CField.activeHexaStat(c));
		c.getSession().writeAndFlush(CField.messageHexaCore(type, 0, null, true));
	}

	public static void changeHexaStat(LittleEndianAccessor slea, MapleClient c, int type) {
		slea.skip(12);

		int coreId = slea.readInt();

		c.getPlayer().gainMeso(-100000000, true);

		c.getPlayer().setKeyValue(coreId, "HEXA Stat M S", slea.readInt() + "");
		c.getPlayer().setKeyValue(coreId, "HEXA Stat A1 S", slea.readInt() + "");
		c.getPlayer().setKeyValue(coreId, "HEXA Stat A2 S", slea.readInt() + "");

		c.getSession().writeAndFlush(CField.activeHexaStat(c));
		c.getSession().writeAndFlush(CField.messageHexaCore(type, 0, null, true));
	}

	public static void checkSecondPW(LittleEndianAccessor slea, MapleClient c, int type) {
		String secondPW = slea.readMapleAsciiString();

		c.getSession().writeAndFlush(CField.messageHexaCore(type, 0, null, c.CheckSecondPassword(secondPW)));
	}
}
