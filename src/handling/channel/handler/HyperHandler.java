package handling.channel.handler;

import client.*;
import tools.data.LittleEndianAccessor;
import tools.packet.CWvsContext;

import java.util.HashMap;

public class HyperHandler {

	public static int[] table = new int[] { 0, 1, 2, 4, 8, 10, 15, 20, 25, 30, 35, 50, 65, 80, 95, 110 };

	public static void getHyperSkill(LittleEndianAccessor rh, MapleClient c) {
		String value = rh.readMapleAsciiString();
		int rate = 0;
		int lvl = rh.readInt();
		int sp = rh.readInt();
		if (value.equals("needHyperStatLv") || value.equals("incHyperStat")) {
			if (value.equals("honorLeveling")) {
				return;
			}
			int up = 0;
			if (value.equals("incHyperStat")) {
				if (lvl >= 140 && lvl <= 149) {
					up = 3;
				} else if (lvl >= 150 && lvl <= 159) {
					up = 4;
				} else if (lvl >= 160 && lvl <= 169) {
					up = 5;
				} else if (lvl >= 170 && lvl <= 179) {
					up = 6;
				} else if (lvl >= 180 && lvl <= 189) {
					up = 7;
				} else if (lvl >= 190 && lvl <= 199) {
					up = 8;
				} else if (lvl >= 200 && lvl <= 209) {
					up = 9;
				} else if (lvl >= 210 && lvl <= 219) {
					up = 10;
				} else if (lvl >= 220 && lvl <= 229) {
					up = 11;
				} else if (lvl >= 230 && lvl <= 239) {
					up = 12;
				} else if (lvl >= 240 && lvl <= 249) {
					up = 13;
				} else if (lvl >= 250 && lvl <= 259) {
					up = 14;
				} else if (lvl >= 260 && lvl <= 269) {
					up = 15;
				} else if (lvl >= 270 && lvl <= 300) {
					up = 16;
				}
			}
			c.getSession().writeAndFlush(CWvsContext.updateHyperSp(value, lvl, sp,
					value.equals("incHyperStat") ? up : (value.equals("needHyperStatLv") ? HyperHandler.table[lvl] : 1),
					true));
		} else if (value.equals("hyper")) {
			int table = 0;
			boolean unk = false;
			if (lvl >= 28 && lvl <= 40) {
				unk = true;
				table = (sp == 1) ? ((lvl == 28 || lvl == 32 || lvl == 38) ? 1 : 0)
						: ((lvl == 28 || lvl == 30 || lvl == 33 || lvl == 36 || lvl == 38) ? 1 : 0);
			}
			c.getSession().writeAndFlush(CWvsContext.updateHyperSp(value, lvl, sp, table, unk));
		} else {
			if (value.equals("honorLeveling")) {
				return;
			}
			if (!value.equals("hyper") && !value.equals("incHyperStat")) {
				if (value.startsWith("9200") || value.startsWith("9201") || value.startsWith("9203")
						|| value.startsWith("9204")) {
					rate = 100;
				} else {
					if (value.equals("honorLeveling")) {
						c.getSession().writeAndFlush(
								CWvsContext.updateSpecialStat(value, lvl, sp, c.getPlayer().getHonourNextExp()));
						return;
					}
					rate = Math.max(0,
							100 - (lvl + 1 - c.getPlayer().getProfessionLevel(Integer.parseInt(value))) * 20);
				}
				c.getSession().writeAndFlush(CWvsContext.updateSpecialStat(value, lvl, sp, rate));
			}
		}
	}

	public static void HyperStatHandler(LittleEndianAccessor slea, MapleClient c) {
		final MapleCharacter chr = c.getPlayer();
		final int pos = slea.readInt();
		final int skillid = slea.readInt();
		final Skill skill = SkillFactory.getSkill(skillid);
		final int maxlevel = skill.getMaxLevel();
		final int curLevel = c.getPlayer().getSkillLevel(skill);
		final boolean create = chr.getKeyValue(2424, pos + "/" + skillid) < 0;
		if (curLevel + 1 <= maxlevel) {
			c.getPlayer().changeSingleSkillLevel(skill, (byte) curLevel + 1, (byte) maxlevel);
			if (create) {
				c.getPlayer().addHyperStats(pos, skillid, curLevel + 1);
			} else {
				c.getPlayer().UpdateHyperStats(pos, skillid, curLevel + 1);
			}
			c.getSession().writeAndFlush(CWvsContext.updateHyperPresets(c.getPlayer(), pos, (byte) 1));
			chr.setKeyValue(2424, pos + "/" + skillid, "" + (curLevel + 1));
		} else {
			c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
		}
	}

	public static void ResetHyperStatHandler(LittleEndianAccessor slea, MapleClient c) {
		int pos = slea.readInt();
		long price = 10000000L;
		MapleCharacter chr = c.getPlayer();
		if (chr.getMeso() < price) {
			chr.dropMessage(1, "缺少方法.");
			c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
			return;
		}
		HashMap<Skill, SkillEntry> sa = new HashMap<>();
		for (Skill skil : chr.getSkills().keySet()) {
			if (skil.getId() / 100 == 800004) {
				sa.put(skil, new SkillEntry(0, (byte) skil.getMaxLevel(), -1L));
			}
		}
		for (MapleHyperStats right : chr.loadHyperStats(pos)) {
			chr.resetHyperStats(right.getPosition(), right.getSkillid());
		}
		chr.gainMeso(-price, false);
		chr.changeSkillsLevel(sa);
		c.getSession().writeAndFlush(CWvsContext.updateHyperPresets(c.getPlayer(), pos, (byte) 1));
		c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
	}

	public static void HyperStatPresets(LittleEndianAccessor slea, MapleClient c) {
		MapleCharacter chr = c.getPlayer();
		slea.skip(4);
		int pos = slea.readInt();
		HashMap<Skill, SkillEntry> hps = new HashMap<>();
		for (MapleHyperStats left : chr.loadHyperStats((int) chr.getKeyValue(2498, "hyperstats"))) { // 이전 하이퍼스탯 삭제 후
			hps.put(SkillFactory.getSkill(left.getSkillid()),
					new SkillEntry(0, (byte) SkillFactory.getSkill(left.getSkillid()).getMaxLevel(), -1));
		}
		for (MapleHyperStats right : chr.loadHyperStats(pos)) { // 이후 하이퍼스탯 추가
			hps.put(SkillFactory.getSkill(right.getSkillid()), new SkillEntry(right.getSkillLevel(),
					(byte) SkillFactory.getSkill(right.getSkillid()).getMaxLevel(), -1));
		}
		chr.changeSkillsLevel(hps);
		c.getSession().writeAndFlush(CWvsContext.updateHyperPresets(c.getPlayer(), pos, (byte) 0));
		chr.setKeyValue(2498, "hyperstats", "" + pos);
		c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
//        for (int i=4; i<=6; i++) {
//            c.getSession().writeAndFlush(CWvsContext.updateHyperPresets(c.getPlayer(), 0, (byte) i));
//        }
	}

	public static void ResetHyperSkill(MapleClient c) {
		long price = 100000L;
		MapleCharacter chr = c.getPlayer();
		if (chr.getMeso() < price) {
			chr.dropMessage(1, "缺少方法.");
			c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
			return;
		}
		HashMap<Skill, SkillEntry> sa = new HashMap<>();
		for (Skill skil : chr.getSkills().keySet()) {
			if (skil.isHyper()) {
				sa.put(skil, new SkillEntry(0, (byte) skil.getMaxLevel(), -1L));
			}
		}
		chr.gainMeso(-price, false);
		chr.changeSkillsLevel(sa);
	}
}
