package tools.packet;

import client.*;
import client.inventory.*;
import client.status.MonsterStatus;
import client.status.MonsterStatusEffect;
import constants.GameConstants;
import constants.KoreaCalendar;
import handling.channel.handler.HEXAHandler;
import handling.world.MaplePartyCharacter;
import server.MapleChatEmoticon;
import server.MapleItemInformationProvider;
import server.MapleSavedEmoticon;
import server.SecondaryStatEffect;
import server.maps.MapleMist;
import server.marriage.MarriageDataEntry;
import server.marriage.MarriageManager;
import server.movement.LifeMovementFragment;
import server.quest.MapleQuest;
import server.shops.*;
import tools.*;
import tools.data.MaplePacketLittleEndianWriter;

import java.util.*;

public class PacketHelper {

	public static final long FT_UT_OFFSET = 116445060000000000L;

	public static final long MAX_TIME = 150842304000000000L;

	public static final long ZERO_TIME = 94354848000000000L;

	public static final long PERMANENT = 150841440000000000L;

	public static final long ZERO_TIME_REVERSE = -153052018564450501L;

	public static final long getKoreanTimestamp(long realTimestamp) {
		return getTime(realTimestamp);
	}

	public static final long getMapleShopTime(int month, int day, int recharge) {
		int i;
		Calendar ocal = Calendar.getInstance();
		int nowmonth = ocal.get(2) + 1;
		int nowday = ocal.get(5);
		int maxday = ocal.getActualMaximum(5);
		day += recharge;
		if (month == 1 && nowmonth == 12) {
			month = 12;
		} else if (day > maxday) {
			day -= maxday;
		}
		if (month == 2) {
			day += 3;
		} else if (month == 4 || month == 6 || month == 9 || month == 11) {
			++day;
		}
		long mapletime = 0L;
		boolean hour = false;
		int baseday = 153225;
		GregorianCalendar baseCal = new GregorianCalendar(2020, 7, 8);
		GregorianCalendar targetCal = new GregorianCalendar(ocal.get(1), ocal.get(2) + 1, ocal.get(5));
		long diffSec = (targetCal.getTimeInMillis() - baseCal.getTimeInMillis()) / 1000L;
		long diffDays = diffSec / 86400L;
		baseday = (int) ((long) baseday + diffDays);
		for (i = 0; i < baseday; ++i) {
			mapletime += 3375000000L;
		}
		if (month - nowmonth == 0) {
			for (i = 0; i < day - nowday; ++i) {
				mapletime += 3375000000L;
			}
		}
		if (month - nowmonth > 0) {
			baseCal = new GregorianCalendar(ocal.get(1), ocal.get(2) + 1, ocal.get(5));
			targetCal = new GregorianCalendar(ocal.get(1), month, day);
			diffSec = (targetCal.getTimeInMillis() - baseCal.getTimeInMillis()) / 1000L;
			diffDays = diffSec / 86400L;
			i = 0;
			while ((long) i < diffDays) {
				mapletime += 3375000000L;
				++i;
			}
		}
		return mapletime;
	}

	public static long getPacketTimeStamp(byte tp) {
		switch (tp) {
		case 1:
			return 150842304000000000L;
		case 2:
			return 94354848000000000L;
		case 3:
			return 150841440000000000L;
		case 4:
			return -153052018564450501L;
		case 5:
			return -153052018564450501L;
		case 6:
			return 115374162703L;
		case 7:
			return 133454615941520000L;
		case 8:
			return 133160268210140000L;
		case 9:
			return 132976574400000000L;
		default:
			return tp * 10000L + 116445060000000000L;
		}
	}

	public static final long getTime(long realTimestamp) {
		if (realTimestamp == -1L) {
			return 150842304000000000L;
		}
		if (realTimestamp == -2L) {
			return 94354848000000000L;
		}
		if (realTimestamp == -3L) {
			return 150841440000000000L;
		}
		if (realTimestamp == -4L) {
			return -153052018564450501L;
		}
		if (realTimestamp == -5L) {
			return -153052018564450501L;
		}
		if (realTimestamp == -6L) {
			return 115374162703L;
		}
		if (realTimestamp == -11L) {
			return 8864707262L;
		}
		if (realTimestamp == -7L) {
			return 133454615941520000L;
		}
		if (realTimestamp == -8L) {
			return 133160268210140000L;
		}
		if (realTimestamp == -9L) {
			return 133349922195170000L;
		}
		if (realTimestamp == -10L) {
			return 133588721356260000L;
		}
		return realTimestamp * 10000L + 116445060000000000L;
	}

	public static long getFileTimestamp(long timeStampinMillis, boolean roundToMinutes) {
		long time;
		if (SimpleTimeZone.getDefault().inDaylightTime(new Date())) {
			timeStampinMillis -= 3600000L;
		}
		if (roundToMinutes) {
			time = timeStampinMillis / 1000L / 60L * 600000000L;
		} else {
			time = timeStampinMillis * 10000L;
		}
		return time + 116445060000000000L;
	}

	public static byte[] sendPacket(String args) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
		mplew.write(HexTool.getByteArrayFromHexString(args));
		return mplew.getPacket();
	}

	public static void addQuestInfo(MaplePacketLittleEndianWriter mplew, MapleCharacter chr) {
		List<MapleQuestStatus> started = chr.getStartedQuests();
		mplew.write(1);

		mplew.writeShort(started.size());
		for (MapleQuestStatus q : started) {
			mplew.writeInt(q.getQuest().getId());
			if (q.hasMobKills()) {
				StringBuilder sb = new StringBuilder();
				for (Iterator<Integer> i$ = q.getMobKills().values().iterator(); i$.hasNext();) {
					int kills = ((Integer) i$.next()).intValue();
					sb.append(StringUtil.getLeftPaddedStr(String.valueOf(kills), '0', 3));
				}
				mplew.writeMapleAsciiString(sb.toString());
				continue;
			}
			mplew.writeMapleAsciiString((q.getCustomData() == null) ? "" : q.getCustomData());
		}

		mplew.write(1);

		List<MapleQuestStatus> completed = chr.getCompletedQuests();
		mplew.writeShort(completed.size());
		for (MapleQuestStatus q : completed) {
			mplew.writeInt(q.getQuest().getId());
			mplew.writeLong(getTime(q.getCompletionTime()));
		}
	}

	public static boolean jobskill(MapleCharacter chr, int skillid) {
		if (GameConstants.isPhantom(chr.getJob())) {
			return true;
		}
		if (GameConstants.isAdventurer(chr.getJob()) && skillid < 1000000) {
			return true;
		}
		if (skillid == 30001068 && GameConstants.isBattleMage(chr.getJob())) {
			return false;
		}
		boolean fame = GameConstants.JobCodeCheck(skillid / 10000, chr.getJob());
		if (skillid / 10000 == 100 && GameConstants.isWarrior(chr.getJob())) {
			return true;
		}
		if (skillid / 10000 == 200 && GameConstants.isMagician(chr.getJob())) {
			return true;
		}
		if (skillid / 10000 == 300 && GameConstants.isArcher(chr.getJob())) {
			return true;
		}
		if (skillid / 10000 == 400 && GameConstants.isThief(chr.getJob())) {
			return true;
		}
		if (skillid / 10000 == 500 && GameConstants.isPirate(chr.getJob()) && !GameConstants.isCannon(chr.getJob())) {
			return true;
		}
		if (skillid / 10000 == 1000 && GameConstants.isSoulMaster(chr.getJob())) {
			return true;
		}
		if (skillid / 10000 == 1000 && GameConstants.isFlameWizard(chr.getJob())) {
			return true;
		}
		if (skillid / 10000 == 1000 && GameConstants.isWindBreaker(chr.getJob())) {
			return true;
		}
		if (skillid / 10000 == 1000 && GameConstants.isNightWalker(chr.getJob())) {
			return true;
		}
		if (skillid / 10000 == 1000 && GameConstants.isStriker(chr.getJob())) {
			return true;
		}
		if (skillid / 10000 == 3000 && GameConstants.isBlaster(chr.getJob())) {
			return true;
		}
		if (skillid / 10000 == 3000 && GameConstants.isMechanic(chr.getJob())) {
			return true;
		}
		if (skillid / 10000 == 3000 && GameConstants.isBattleMage(chr.getJob())) {
			return true;
		}
		if (skillid / 10000 == 3000 && GameConstants.isWildHunter(chr.getJob())) {
			return true;
		}
		if (skillid / 10000 == 3001 && GameConstants.isDemonAvenger(chr.getJob())) {
			return true;
		}
		if (skillid / 10000 == 13500 && GameConstants.isYeti(chr.getJob())) {
			return true;
		}
		if (skillid / 10000 == 13100 && GameConstants.isPinkBean(chr.getJob())) {
			return true;
		}
		if (PlayerStats.getSkillByJob(12, chr.getJob()) == skillid
				|| PlayerStats.getSkillByJob(73, chr.getJob()) == skillid) {
			return true;
		}
		if (skillid == 150020079 || skillid == 150021251) {
			return false;
		}
		if (fame) {
			return true;
		}
		return false;
	}

	public static final void addSkillInfo(MaplePacketLittleEndianWriter mplew, MapleCharacter chr) {
		mplew.write(1);

		Map<Skill, SkillEntry> skills = chr.getSkills();
		mplew.writeShort(skills.size());

		for (Map.Entry<Skill, SkillEntry> skill : skills.entrySet()) {
			mplew.writeInt(((Skill) skill.getKey()).getId());
			mplew.writeInt(((SkillEntry) skill.getValue()).skillevel);
			mplew.writeLong(getTime(((SkillEntry) skill.getValue()).expiration));

			if (SkillFactory.sub_60A550(((Skill) skill.getKey()).getId())) {
				mplew.writeInt(((SkillEntry) skill.getValue()).masterlevel);
			}
		}

		mplew.writeShort(0);

		List<Triple<Skill, SkillEntry, Integer>> linkskills = chr.getLinkSkills();

		mplew.writeInt(linkskills.size());

		for (Triple<Skill, SkillEntry, Integer> linkskill : linkskills) {
			addLinkSkillInfo(mplew, ((Skill) linkskill.getLeft()).getId(), ((Integer) linkskill.getRight()).intValue(),
					(chr.getSkillLevel(((Skill) linkskill.getLeft()).getId()) == 0)
							? ((Integer) linkskill.getRight()).intValue()
							: chr.getId(),
					((SkillEntry) linkskill.getMid()).skillevel);
		}

		mplew.write((int) chr.getKeyValue(2498, "hyperstats"));

		for (int i = 0; i <= 2; i++) {
			mplew.writeInt(chr.loadHyperStats(i).size());

			for (MapleHyperStats mhsz : chr.loadHyperStats(i)) {
				mplew.writeInt(mhsz.getPosition());
				mplew.writeInt(mhsz.getSkillid());
				mplew.writeInt(mhsz.getSkillLevel());
			}
		}
	}

	public static final void addLinkSkillInfo(MaplePacketLittleEndianWriter mplew, int skillid, int sendid, int recvid,
			int level) {
		mplew.writeInt(sendid);
		mplew.writeInt(recvid);
		mplew.writeInt(skillid);
		mplew.writeShort(level);
		mplew.writeLong(getTime(-2L));
		mplew.writeInt(1);
	}

	public static final void addCoolDownInfo(MaplePacketLittleEndianWriter mplew, MapleCharacter chr) {
		List<MapleCoolDownValueHolder> cd = chr.getCooldowns();
		mplew.writeShort(cd.size());
		for (MapleCoolDownValueHolder cooling : cd) {
			mplew.writeInt(cooling.skillId);
			mplew.writeInt((int) (cooling.length + cooling.startTime - System.currentTimeMillis()) / 1000);
		}
	}

	public static final void addRocksInfo(MaplePacketLittleEndianWriter mplew, MapleCharacter chr) {
		int[] mapz = chr.getRegRocks();
		for (int i = 0; i < 5; i++) {
			mplew.writeInt(mapz[i]);
		}
		int[] map = chr.getRocks();
		for (int j = 0; j < 10; j++) {
			mplew.writeInt(map[j]);
		}
		int[] maps = chr.getHyperRocks();
		for (int k = 0; k < 13; k++) {
			mplew.writeInt(maps[k]);
		}
	}

	public static final void addRingInfo(MaplePacketLittleEndianWriter mplew, MapleCharacter chr) {
		Triple<List<MapleRing>, List<MapleRing>, List<MapleRing>> aRing = chr.getRings(true);
		List<MapleRing> cRing = aRing.getLeft();
		mplew.writeShort(cRing.size());
		for (MapleRing ring : cRing) {
			mplew.writeInt(ring.getPartnerChrId());
			mplew.writeAsciiString(ring.getPartnerName(), 13);
			mplew.writeLong(ring.getRingId());
			mplew.writeLong(ring.getPartnerRingId());
		}
		List<MapleRing> fRing = aRing.getMid();
		mplew.writeShort(fRing.size());
		for (MapleRing ring : fRing) {
			mplew.writeInt(ring.getPartnerChrId());
			mplew.writeAsciiString(ring.getPartnerName(), 13);
			mplew.writeLong(ring.getRingId());
			mplew.writeLong(ring.getPartnerRingId());
			mplew.writeInt(ring.getItemId());
		}
		List<MapleRing> mRing = aRing.getRight();
		mplew.writeShort(mRing.size());
		for (MapleRing ring : mRing) {
			MarriageDataEntry data = MarriageManager.getInstance().getMarriage(chr.getMarriageId());
			if (data == null) {
				System.out.println(chr.getName() + " 캐릭터는 웨딩 데이터가 존재하지 않음.");
				mplew.writeZeroBytes(48);
				continue;
			}
			mplew.writeInt(data.getMarriageId());
			mplew.writeInt((data.getBrideId() == chr.getId()) ? data.getBrideId() : data.getGroomId());
			mplew.writeInt((data.getBrideId() == chr.getId()) ? data.getGroomId() : data.getBrideId());
			mplew.writeShort((data.getStatus() == 2) ? 3 : data.getStatus());
			mplew.writeInt(ring.getItemId());
			mplew.writeInt(ring.getItemId());
			mplew.writeAsciiString((data.getBrideId() == chr.getId()) ? data.getBrideName() : data.getGroomName(), 13);
			mplew.writeAsciiString((data.getBrideId() == chr.getId()) ? data.getGroomName() : data.getBrideName(), 13);
		}
	}

	public static void addInventoryInfo(MaplePacketLittleEndianWriter mplew, MapleCharacter chr,
			boolean change_load_type) {
		mplew.write(chr.getInventory(MapleInventoryType.EQUIP).getSlotLimit());
		mplew.write(chr.getInventory(MapleInventoryType.USE).getSlotLimit());
		mplew.write(chr.getInventory(MapleInventoryType.SETUP).getSlotLimit());
		mplew.write(chr.getInventory(MapleInventoryType.ETC).getSlotLimit());
		mplew.write(chr.getInventory(MapleInventoryType.CASH).getSlotLimit());
		mplew.write(chr.getInventory(MapleInventoryType.CODY).getSlotLimit());

		mplew.writeShort(0); // 1.2.390 ++
		mplew.write(change_load_type);
		MapleInventory iv = chr.getInventory(MapleInventoryType.EQUIPPED);
		List<Item> equipped = iv.newList();
		Collections.sort(equipped);
		for (Item item : equipped) {
			if (item.getPosition() < 0 && item.getPosition() > -100) {
				addItemPosition(mplew, item, false, false);
				addItemInfo(mplew, item, chr);
			}
		}

		mplew.writeShort(0);
		if (!change_load_type) {
			iv = chr.getInventory(MapleInventoryType.EQUIP);
			for (Item item : iv.newList()) {
				if (GameConstants.isArcaneSymbol(item.getItemId())
						|| GameConstants.isAuthenticSymbol(item.getItemId())) {// 将徽章添加到玩家的专属集合
					chr.getSymbol().add((Equip) item);
				}
				addItemPosition(mplew, item, false, false);
				addItemInfo(mplew, item, chr);
			}
		}

		mplew.writeShort(0);
		if (!change_load_type) {
			for (Item item : equipped) {
				if (item.getPosition() <= -1000 && item.getPosition() > -1100) {
					addItemPosition(mplew, item, false, false);
					addItemInfo(mplew, item, chr);
				}
			}
		}

		mplew.writeShort(0);
		if (!change_load_type) {
			for (Item item : equipped) {
				if (item.getPosition() <= -1100 && item.getPosition() > -1200) {
					addItemPosition(mplew, item, false, false);
					addItemInfo(mplew, item, chr);
				}
			}
		}

		mplew.writeShort(0);
		if (!change_load_type) {
			for (Item item : equipped) {
				if (item.getPosition() <= -1400 && item.getPosition() > -1500) {
					addItemPosition(mplew, item, false, false);
					addItemInfo(mplew, item, chr);
				}
			}
		}

		mplew.writeShort(0);
		if (!change_load_type) {
			for (Item item : equipped) {
				if (item.getPosition() <= -5000 && item.getPosition() >= -5002) {
					addItemPosition(mplew, item, false, false);
					addItemInfo(mplew, item, chr);
				}
			}
		}

		mplew.writeShort(0);
		for (Item item : equipped) {
			if (item.getPosition() <= -1600 && item.getPosition() > -1700) {
				chr.getSymbol().add((Equip) item);
				addItemPosition(mplew, item, false, false);
				addItemInfo(mplew, item, chr);
			}
		}

		mplew.writeShort(0);
		for (Item item : equipped) {
			if (item.getPosition() <= -1700 && item.getPosition() > -1800) {
				chr.getSymbol().add((Equip) item);
				addItemPosition(mplew, item, false, false);
				addItemInfo(mplew, item, chr);
			}
		}

		mplew.writeShort(0);
		if (!change_load_type) {
			for (Item item : equipped) {
				if (item.getPosition() <= -1200 && item.getPosition() > -1300) {
					addItemPosition(mplew, item, false, false);
					addItemInfo(mplew, item, chr);
				}
			}
		}

		mplew.writeShort(0);
		if (!change_load_type) {
			for (Item item : equipped) {
				if (item.getPosition() <= -1300 && item.getPosition() > -1400) {
					addItemPosition(mplew, item, false, false);
					addItemInfo(mplew, item, chr);
				}
			}
		}

		mplew.writeShort(0);
		if (!change_load_type) {
			for (Item item : equipped) {
				if (item.getPosition() <= -1500 && item.getPosition() > -1600) {
					addItemPosition(mplew, item, false, false);
					addItemInfo(mplew, item, chr);
				}
			}
		}

		mplew.writeShort(0);
		mplew.write(change_load_type);
		for (Item item : equipped) {
			if (item.getPosition() <= -100 && item.getPosition() > -1000) {
				addItemPosition(mplew, item, false, false);
				addItemInfo(mplew, item, chr);
			}
		}

		mplew.writeShort(0);
		if (!change_load_type) {
			iv = chr.getInventory(MapleInventoryType.CODY);
			for (Item item : iv.newList()) {
				addItemPosition(mplew, item, false, false);
				addItemInfo(mplew, item, chr);
			}
		}

		// 프리셋 1페
		mplew.writeShort(0); // 1.2.367 ++

		// 프리셋 2페
		mplew.writeShort(0); // 1.2.367 ++

		// 프리셋 3페
		mplew.writeShort(0); // 1.2.367 ++

		mplew.writeShort(0); // 1.2.390 ++

		mplew.writeShort(0); // 1.2.390 ++

		mplew.writeShort(0); // 1.2.390 ++

		mplew.writeShort(0); // 1.2.390 ++
		if (!change_load_type) {
			iv = chr.getInventory(MapleInventoryType.USE);
			for (Item item : iv.newList()) {
				addItemPosition(mplew, item, false, false);
				addItemInfo(mplew, item, chr);
			}
		}

		mplew.writeShort(0);
		if (!change_load_type) {
			iv = chr.getInventory(MapleInventoryType.SETUP);
			for (Item item : iv.newList()) {
				addItemPosition(mplew, item, false, false);
				addItemInfo(mplew, item, chr);
			}
		}

		mplew.writeShort(0);
		if (!change_load_type) {
			iv = chr.getInventory(MapleInventoryType.ETC);
			for (Item item : iv.newList()) {
				if (item.getPosition() < 100) {
					addItemPosition(mplew, item, false, false);
					addItemInfo(mplew, item, chr);
				}
			}
		}

		mplew.writeShort(0);
		if (!change_load_type) {
			iv = chr.getInventory(MapleInventoryType.CASH);
			for (Item item : iv.newList()) {
				addItemPosition(mplew, item, false, false);
				addItemInfo(mplew, item, chr);
			}
		}

		mplew.writeShort(0);
		for (int z = 0; z < 3; z++) {
			int a = 0;
			mplew.writeInt(a);
			for (int k = 0; k < a; k++) {
				mplew.writeInt(chr.getExtendedSlots().size());
				mplew.writeInt(0);
				for (int i = 0; i < chr.getExtendedSlots().size(); i++) {
					for (Item item : chr.getInventory(MapleInventoryType.ETC).list()) {
						if (item.getPosition() > 10000 && item.getPosition() < 10200) {
							mplew.writeInt(i);
							addItemInfo(mplew, item, chr);
						}
					}
					mplew.writeInt(-1);
				}
			}
		}

		mplew.writeInt(0);
		mplew.writeInt(0);
		mplew.write(0);
	}

	public static final void addCharStats(MaplePacketLittleEndianWriter mplew, MapleCharacter chr) {
		for (int i = 0; i < 2; ++i) {
			mplew.writeInt(chr.getId());
		}

		mplew.writeInt(0);
		mplew.writeAsciiString(chr.getName(), 13);
		mplew.write(chr.getGender());
		mplew.write(chr.getSkinColor());
		mplew.writeInt(chr.getFace());
		if (chr.getBaseColor() != -1) {
			mplew.writeInt(chr.getHair() / 10 * 10 + chr.getBaseColor());
		} else {
			mplew.writeInt(chr.getHair());
		}

		mplew.writeInt(chr.getLevel());
		mplew.writeShort(chr.getJob());

		chr.getStat().connectData(mplew);

		mplew.writeShort(chr.getRemainingAp());

		int size = chr.getRemainingSpSize();
		if (GameConstants.isSeparatedSp(chr.getJob())) {
			mplew.write(size);
			for (int i = 0; i < chr.getRemainingSps().length; i++) {
				if (chr.getRemainingSp(i) > 0) {
					mplew.write(i + 1);
					mplew.writeInt(chr.getRemainingSp(i));
				}
			}
		} else {
			mplew.writeShort(chr.getRemainingSp());
		}

		mplew.writeLong(chr.getExp());

		mplew.writeInt(chr.getFame());
		mplew.writeInt(GameConstants.isZero(chr.getJob()) ? chr.getStat().getMp() : 0);
		mplew.writeInt(chr.getMapId());

		mplew.write(chr.getInitialSpawnpoint());
		mplew.writeShort(chr.getSubcategory());

		if (GameConstants.isDemonSlayer(chr.getJob()) || GameConstants.isXenon(chr.getJob())
				|| GameConstants.isDemonAvenger(chr.getJob()) || GameConstants.isArk(chr.getJob())
				|| GameConstants.isHoyeong(chr.getJob())) {
			mplew.writeInt(chr.getDemonMarking());
		}

		mplew.write(0);
		mplew.writeLong(PacketHelper.getTime(-2));

		for (MapleTrait.MapleTraitType t : MapleTrait.MapleTraitType.values()) {
			mplew.writeInt(chr.getTrait(t).getTotalExp());
		}

		for (MapleTrait.MapleTraitType t : MapleTrait.MapleTraitType.values()) {
			mplew.writeInt(chr.getTrait(t).getExp());
		}

		mplew.write(0);
		mplew.writeLong(getTime(-2L));

		KoreaCalendar kc = new KoreaCalendar();
		String now = kc.getYears() + kc.getMonths() + kc.getDays();
		mplew.writeInt(Integer.parseInt(now));
		mplew.writeInt(0);
		mplew.write(10);
		mplew.writeInt(0);

		mplew.write(5);
		mplew.write(5);

		mplew.writeInt(0);

		mplew.writeLong(PacketHelper.getTime(-1L));
		mplew.writeLong(PacketHelper.getTime(-2L));
		mplew.writeLong(PacketHelper.getTime(-2L));

		mplew.writeInt(0);
		mplew.writeInt(0);
		mplew.writeInt(0);
		mplew.write(0);
	}

	public static final void addExpirationTime(MaplePacketLittleEndianWriter mplew, long time) {
		mplew.writeLong(getTime(time));
	}

	public static void addItemPosition(MaplePacketLittleEndianWriter mplew, Item item, boolean trade, boolean bagSlot) {
		if (item == null) {
			mplew.write(0);
			return;
		}
		short pos = item.getPosition();
		if (pos <= -1) {
			pos = (short) (pos * -1);
			if (pos > 100 && pos < 1000) {
				pos = (short) (pos - 100);
			}
		}
		if (bagSlot) {
			mplew.writeInt(pos % 100 - 1);
		} else {
			mplew.writeShort(pos);
		}
	}

	public static final void addItemInfo(MaplePacketLittleEndianWriter mplew, Item item) {
		addItemInfo(mplew, item, null);
	}

	public static final void addItemInfo(MaplePacketLittleEndianWriter mplew, Item item, MapleCharacter chr) {
		mplew.write((item.getPet() != null) ? 3 : item.getType());
		mplew.writeInt(item.getItemId());
		boolean hasUniqueId = (item.getUniqueId() > 0L && !GameConstants.isMarriageRing(item.getItemId())
				&& item.getItemId() / 10000 != 166);
		mplew.write(hasUniqueId ? 1 : 0);
		if (hasUniqueId) {
			mplew.writeLong(item.getUniqueId());
		}
		if (item.getPet() != null) {
			addPetItemInfo(mplew, chr, item, item.getPet(), true, false);
		} else {
			addExpirationTime(mplew, item.getExpiration());
			mplew.writeInt((chr == null) ? -1 : chr.getExtendedSlots().indexOf(Integer.valueOf(item.getItemId())));
			mplew.write((item.getType() == 1 || item.getItemId() == 4001886));

			if (item.getType() == 1) {
				Equip equip = Equip.calculateEquipStats((Equip) item);
				addEquipStats(mplew, equip);
				addEquipBonusStats(mplew, equip, hasUniqueId, chr);
				if (equip.getItemId() / 1000 == 1662) {
					addAndroidLooks(mplew, equip);
				}
				mplew.writeShort(0);
				mplew.writeShort(0);
				mplew.writeShort(0);

				boolean isStarForce = (equip.getEnhance() > 0);
				mplew.write(isStarForce);
				if (isStarForce) {
					addEquipStarForceStats(mplew, equip);
				}
			} else if (item.getItemId() == 4001886) {
				if (item.getReward() != null) {
					mplew.writeInt(item.getQuantity());
					mplew.writeMapleAsciiString(item.getOwner());
					mplew.write(0);
					mplew.writeZeroBytes(11); // 1.2.388 ++
					mplew.writeLong(item.getReward().getObjectId());
					mplew.writeInt(0);
				} else {
					mplew.writeInt(item.getQuantity());
					mplew.writeMapleAsciiString(item.getOwner());
					mplew.write(0);
					mplew.writeLong(1L);
					mplew.writeInt(0);
				}
			} else {
				mplew.writeShort(item.getQuantity());
				mplew.writeMapleAsciiString(item.getOwner());
				mplew.writeShort(item.getFlag());
				mplew.write(0); // 1.2.373 ++
				mplew.writeInt(0);
				mplew.writeZeroBytes(11);
				if (GameConstants.isThrowingStar(item.getItemId()) || GameConstants.isBullet(item.getItemId())
						|| item.getItemId() / 10000 == 287) {
					mplew.writeLong((item.getInventoryId() <= 0L) ? -1L : item.getInventoryId());
				}
			}
		}
	}

	public static void addEquipStarForceStats(MaplePacketLittleEndianWriter mplew, Equip equip) {
		int head = 0;
		if (equip.getStats().size() > 0) {
			for (EquipStat stat : equip.getStats()) {
				if (stat == EquipStat.STR || stat == EquipStat.DEX || stat == EquipStat.INT || stat == EquipStat.LUK
						|| stat == EquipStat.WATK || stat == EquipStat.MATK) {
					head |= stat.getValue();
				}
			}
		}
		mplew.writeInt(head);
		// System.out.println(head + "/N");
		if (head != 0) {
			if (equip.getStats().contains(EquipStat.STR)) {
				// System.out.println("1 : " + equip.getStarforceStr());
				mplew.writeShort(equip.getStarforceStr());
			}
			if (equip.getStats().contains(EquipStat.DEX)) {
				// System.out.println("2 : " + equip.getStarforceDex());
				mplew.writeShort(equip.getStarforceDex());
			}
			if (equip.getStats().contains(EquipStat.INT)) {
				// System.out.println("3 : " + equip.getStarforceInt());
				mplew.writeShort(equip.getStarforceInt());
			}
			if (equip.getStats().contains(EquipStat.LUK)) {
				// System.out.println("4 : " + equip.getStarforceLuk());
				mplew.writeShort(equip.getStarforceLuk());
			}
			if (equip.getStats().contains(EquipStat.WATK)) {
				// System.out.println("5 : " + equip.getStarforceWatk());
				mplew.writeShort(equip.getStarforceWatk());
			}
			if (equip.getStats().contains(EquipStat.MATK)) {
				// System.out.println("6 : " + equip.getStarforceMatk());
				mplew.writeShort(equip.getStarforceMatk());
			}
		}
	}

	public static void addEquipStats(MaplePacketLittleEndianWriter mplew, Equip equip) {
		int head = 0;
		if (equip.getStats().size() > 0) {
			for (EquipStat stat : equip.getStats()) {
				head |= stat.getValue();
			}
		}
		mplew.writeInt(head);
		// System.out.println(head + "/N");
		if (head != 0) {
			if (equip.getStats().contains(EquipStat.STR)) {
				// System.out.println("1 : " + equip.getTotalStr());
				mplew.writeShort(equip.getTotalStr() - equip.getStarforceStr());
			}
			if (equip.getStats().contains(EquipStat.DEX)) {
				// System.out.println("2 : " + equip.getTotalDex());
				mplew.writeShort(equip.getTotalDex() - equip.getStarforceDex());
			}
			if (equip.getStats().contains(EquipStat.INT)) {
				// System.out.println("3 : " + equip.getTotalInt());
				mplew.writeShort(equip.getTotalInt() - equip.getStarforceInt());
			}
			if (equip.getStats().contains(EquipStat.LUK)) {
				// System.out.println("4 : " + equip.getTotalLuk());
				mplew.writeShort(equip.getTotalLuk() - equip.getStarforceLuk());
			}
			if (equip.getStats().contains(EquipStat.MHP)) {
				// System.out.println("5 : " + equip.getTotalHp());
				mplew.writeShort(equip.getTotalHp());
			}
			if (equip.getStats().contains(EquipStat.MMP)) {
				// System.out.println("6 : " + equip.getTotalMp());
				mplew.writeShort(equip.getTotalMp());
			}
			if (equip.getStats().contains(EquipStat.WATK)) {
				// System.out.println("7 : " + equip.getTotalWatk());
				mplew.writeShort(equip.getTotalWatk() - equip.getStarforceWatk());
			}
			if (equip.getStats().contains(EquipStat.MATK)) {
				// System.out.println("8 : " + equip.getTotalMatk());
				mplew.writeShort(equip.getTotalMatk() - equip.getStarforceMatk());
			}
			if (equip.getStats().contains(EquipStat.WDEF)) {
				// System.out.println("9 : " + equip.getTotalWdef());
				mplew.writeShort(equip.getTotalWdef());
			}
			if (equip.getStats().contains(EquipStat.MDEF)) {
				// System.out.println("10 : " + equip.getTotalMdef());
				mplew.writeShort(equip.getTotalMdef());
			}
			if (equip.getStats().contains(EquipStat.ACC)) {
				// System.out.println("11 : " + equip.getTotalAcc());
				mplew.writeShort(equip.getTotalAcc());
			}
			if (equip.getStats().contains(EquipStat.AVOID)) {
				// System.out.println("12 : " + equip.getTotalAvoid());
				mplew.writeShort(equip.getTotalAvoid());
			}
			if (equip.getStats().contains(EquipStat.HANDS)) {
				// System.out.println("13 : " + equip.getHands());
				mplew.writeShort(equip.getHands());
			}
			if (equip.getStats().contains(EquipStat.SPEED)) {
				// System.out.println("14 : " + equip.getSpeed());
				mplew.writeShort(equip.getSpeed());
			}
			if (equip.getStats().contains(EquipStat.JUMP)) {
				// System.out.println("15 : " + equip.getJump());
				mplew.writeShort(equip.getJump());
			}
		}
		addEquipSpecialStats(mplew, equip);
	}

	public static void addEquipSpecialStats(MaplePacketLittleEndianWriter mplew, Equip equip) {
		int head = 0;
		if (equip.getSpecialStats().size() > 0) {
			for (EquipSpecialStat stat : equip.getSpecialStats()) {
				head |= stat.getValue();
			}
		}
		mplew.writeInt(head);
		// System.out.println(head + "/S");
		if (head != 0) {
			if (equip.getSpecialStats().contains(EquipSpecialStat.SLOTS)) {
				// System.out.println("1 : " + equip.getUpgradeSlots());
				mplew.write(equip.getUpgradeSlots());
			}
			if (equip.getSpecialStats().contains(EquipSpecialStat.LEVEL)) {
				// System.out.println("2 : " + equip.getLevel());
				mplew.write(equip.getLevel());
			}
			if (equip.getSpecialStats().contains(EquipSpecialStat.FLAG)) {
				// System.out.println("3 : " + equip.getFlag());
				mplew.writeShort(equip.getFlag());
			}
			if (equip.getSpecialStats().contains(EquipSpecialStat.INC_SKILL)) {
				// System.out.println("4 : " + ((equip.getIncSkill() > 0) ? 1 : 0));
				mplew.write((equip.getIncSkill() > 0) ? 1 : 0);
			}
			if (equip.getSpecialStats().contains(EquipSpecialStat.ITEM_LEVEL)) {
				// System.out.println("5 : " + Math.max(equip.getBaseLevel(),
				// equip.getEquipLevel()));
				mplew.write(Math.max(equip.getBaseLevel(), equip.getEquipLevel()));
			}
			if (equip.getSpecialStats().contains(EquipSpecialStat.ITEM_EXP)) {
				// System.out.println("6 : " + equip.getExpPercentage());
				mplew.writeLong((equip.getExpPercentage() * 100000));
			}
			if (equip.getSpecialStats().contains(EquipSpecialStat.DURABILITY)) {
				// System.out.println("7 : " + equip.getDurability());
				mplew.writeInt(equip.getDurability());
			}
			if (equip.getSpecialStats().contains(EquipSpecialStat.VICIOUS_HAMMER)) {
				// System.out.println("8 : " + equip.getViciousHammer());
				mplew.writeInt(equip.getViciousHammer());
			}
			if (equip.getSpecialStats().contains(EquipSpecialStat.PVP_DAMAGE)) {
				// System.out.println("9 : " + equip.getPVPDamage());
				mplew.writeShort(equip.getPVPDamage());
			}
			if (equip.getSpecialStats().contains(EquipSpecialStat.DOWNLEVEL)) {
				// System.out.println("10 : " + equip.getReqLevel());
				mplew.write(-equip.getReqLevel());
			}
			if (equip.getSpecialStats().contains(EquipSpecialStat.ENHANCT_BUFF)) {
				// System.out.println("11 : " + equip.getEnchantBuff());
				mplew.writeShort(equip.getEnchantBuff());
			}
			if (equip.getSpecialStats().contains(EquipSpecialStat.DURABILITY_SPECIAL)) {
				// System.out.println("12 : " + equip.getDurability());
				mplew.writeInt(equip.getDurability());
			}
			if (equip.getSpecialStats().contains(EquipSpecialStat.REQUIRED_LEVEL)) {
				// System.out.println("13 : " + equip.getReqLevel());
				mplew.write(equip.getReqLevel());
			}
			if (equip.getSpecialStats().contains(EquipSpecialStat.YGGDRASIL_WISDOM)) {
				// System.out.println("14 : " + equip.getYggdrasilWisdom());
				mplew.write(equip.getYggdrasilWisdom());
			}
			if (equip.getSpecialStats().contains(EquipSpecialStat.FINAL_STRIKE)) {
				// System.out.println("15 : " + equip.getFinalStrike());
				mplew.write(equip.getFinalStrike());
			}
			if (equip.getSpecialStats().contains(EquipSpecialStat.IndieBdr)) {
				// System.out.println("16 : " + equip.getBossDamage());
				mplew.write(equip.getBossDamage());
			}
			if (equip.getSpecialStats().contains(EquipSpecialStat.IGNORE_PDR)) {
				// System.out.println("17 : " + equip.getIgnorePDR());
				mplew.write(equip.getIgnorePDR());
			}
			if (equip.getSpecialStats().contains(EquipSpecialStat.TOTAL_DAMAGE)) {
				// System.out.println("18 : " + equip.getTotalDamage());
				mplew.write(equip.getTotalDamage());
			}
			if (equip.getSpecialStats().contains(EquipSpecialStat.ALL_STAT)) {
				// System.out.println("19 : " + equip.getAllStat());
				mplew.write(equip.getAllStat());
			}
			if (equip.getSpecialStats().contains(EquipSpecialStat.KARMA_COUNT)) {
				// System.out.println("20 : " + equip.getKarmaCount());
				mplew.write(equip.getKarmaCount());
			}
			if (equip.getSpecialStats().contains(EquipSpecialStat.REBIRTH_FIRE)) {
				// System.out.println("21 : " + equip.getFire());
				mplew.writeLong(equip.getFire());
			}
			if (equip.getSpecialStats().contains(EquipSpecialStat.EQUIPMENT_TYPE)) {
				// System.out.println("22 : " + ((equip.getEquipmentType() == 1) ? 306 :
				// ((equip.getEquipmentType() == 2) ? 344 : ((equip.getEquipmentType() == 3) ?
				// 282 : equip.getEquipmentType()))));
				mplew.writeInt((equip.getEquipmentType() == 1) ? 306
						: ((equip.getEquipmentType() == 2) ? 344
								: ((equip.getEquipmentType() == 3) ? 282 : equip.getEquipmentType())));
			}
		}
	}

	public static void addEquipBonusStats(MaplePacketLittleEndianWriter mplew, Equip equip, boolean hasUniqueId,
			MapleCharacter chr) {
		mplew.writeAsciiString(equip.getOwner(), 13);

		mplew.write(equip.getState());
		mplew.write(equip.getEnhance());
		mplew.writeShort(equip.getPotential1());
		mplew.writeShort(equip.getPotential2());
		mplew.writeShort(equip.getPotential3());
		mplew.writeShort(equip.getPotential4());
		mplew.writeShort(equip.getPotential5());
		mplew.writeShort(equip.getPotential6());
		mplew.writeShort(equip.getMoru());

		if (!hasUniqueId) {
			mplew.writeLong((equip.getInventoryId() <= 0L) ? -1L : equip.getInventoryId());
		}
		mplew.writeLong((equip.getUniqueId() == -1L) ? 0L : equip.getUniqueId());
		if (equip.getOptionExpiration() > 0L) {
			addExpirationTime(mplew, equip.getOptionExpiration());
		} else {
			mplew.writeLong(getTime(-2L));
		}

		MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
		if (ii.isCash(equip.getItemId())) {
			if (equip.getItemId() / 1000000 == 1) {
				mplew.writeInt(equip.getEquipmentType());
				mplew.writeInt(equip.getCoption1());
				mplew.writeInt(equip.getCoption2());
				mplew.writeInt(equip.getCoption3());
				mplew.writeInt(0); // 1.2.390 ++
				mplew.writeInt(0); // 1.2.390 ++
				mplew.writeInt(0); // 1.2.390 ++
				mplew.writeInt(0); // 1.2.390 ++
			}
		} else {
			mplew.writeZeroBytes(32); // 1.2.390 Changed. (16 -> 32)
		}

		mplew.writeShort(equip.getSoulName());
		mplew.writeShort(equip.getSoulEnchanter());
		mplew.writeShort(equip.getSoulPotential());
		if (GameConstants.isArcaneSymbol(equip.getItemId())) {
			mplew.writeShort(equip.getArc());
			mplew.writeInt(equip.getArcEXP());
			mplew.writeShort(equip.getArcLevel());
		} else if (GameConstants.isAuthenticSymbol(equip.getItemId())) {// 将徽章装备的成长数据序列化到网络封包(mplew)中
			mplew.writeShort(equip.getArc());
			mplew.writeInt(equip.getArcEXP());
			mplew.writeShort(equip.getArcLevel());
		}
		mplew.writeShort(-1);
		mplew.writeLong(getTime(-1L));
		mplew.writeLong(getTime(-2L));
		mplew.writeLong(getTime(-1L));
	}

	public static void addAndroidLooks(MaplePacketLittleEndianWriter mplew, Equip equip) {
		MapleAndroid and = equip.getAndroid();
		if (and == null) {
			mplew.write(0);
			mplew.write(0);
			mplew.writeShort(0);
			mplew.writeShort(0);
			mplew.writeShort(0);
			mplew.writeShort(0);
			mplew.writeMapleAsciiString("안드로이드");
			mplew.writeInt(0);
			mplew.writeLong(getTime(-2L));
		} else {
			mplew.write(and.getSkin());
			mplew.write(0);
			mplew.writeShort(and.getHair());
			mplew.writeShort(0);
			mplew.writeShort(and.getFace());
			mplew.writeShort(0);
			mplew.writeMapleAsciiString(and.getName());
			mplew.writeInt(and.getEar() ? 0 : 1);
			mplew.writeLong(getTime(-2L));

		}
	}

	public static final void serializeMovementList(MaplePacketLittleEndianWriter lew,
			List<LifeMovementFragment> moves) {
		lew.writeShort(moves.size());
		for (LifeMovementFragment move : moves) {
			move.serialize(lew);
		}
	}

	public static final void addAnnounceBox(MaplePacketLittleEndianWriter mplew, MapleCharacter chr) {
		if (chr.getPlayerShop() != null && chr.getPlayerShop().isOwner(chr) && chr.getPlayerShop().getShopType() != 1
				&& chr.getPlayerShop().isAvailable()) {
			addInteraction(mplew, chr.getPlayerShop());
		} else {
			mplew.write(0);
		}
	}

	public static final void addInteraction(MaplePacketLittleEndianWriter mplew, IMaplePlayerShop shop) {
		mplew.write(shop.getGameType());
		mplew.writeInt(((AbstractPlayerStore) shop).getObjectId());
		mplew.writeMapleAsciiString(shop.getDescription());
		if (shop.getShopType() != 1) {
			mplew.write((shop.getPassword().length() > 0) ? 1 : 0);
		}
		if (shop.getItemId() == 5250500) {
			mplew.write(0);
		} else if (shop.getItemId() == 4080100) {
			mplew.write(((MapleMiniGame) shop).getPieceType());
		} else if (shop.getItemId() >= 4080000 && shop.getItemId() < 4080100) {
			mplew.write(((MapleMiniGame) shop).getPieceType());
		} else {
			mplew.write(shop.getItemId() % 10);
		}
		mplew.write(shop.getSize());
		mplew.write(shop.getMaxSize());
		if (shop.getShopType() != 1) {
			mplew.write(shop.isOpen() ? 0 : 1);
		}
		ChatPacket(mplew, shop.getOwnerName(), "[미니룸]" + shop.getDescription());
		mplew.writeInt(0);
		mplew.writeInt(0);
		mplew.writeShort(0);
	}

	public static final void addDamageSkinInfo(MaplePacketLittleEndianWriter mplew, MapleCharacter chr) {
		int maxSize = (int) (chr.getKeyValue(13191, "skinroom") == -1 ? 0 : chr.getKeyValue(13191, "skinroom"));

		mplew.writeInt(maxSize);

		int size = (int) (chr.getKeyValue(13191, "skins") == -1 ? 0 : chr.getKeyValue(13191, "skins"));

		mplew.writeInt(size);
		for (int i = 0; i < size; i++) {
			mplew.writeInt(chr.getKeyValue(13191, i + ""));
			mplew.writeInt(0);
			mplew.writeLong(PacketHelper.getTime(-2));
		}

		int damageSkinCode = (int) (chr.getKeyValue(7293, "damage_skin") == -1 ? 0
				: chr.getKeyValue(7293, "damage_skin"));
		mplew.writeInt(damageSkinCode);
		mplew.writeInt(0);
		mplew.writeLong(PacketHelper.getTime(-2));

		for (int i = 0; i < 2; i++) {
			mplew.writeInt(-1);
			mplew.writeInt(0);
			mplew.writeLong(PacketHelper.getTime(-2));
		}
	}

	public static final void addCharacterInfo(MaplePacketLittleEndianWriter mplew, MapleCharacter chr) {
		mplew.write(0);

		for (int i = 0; i < 3; i++) {
			mplew.writeInt(-3);
		}

		mplew.write(0);
		mplew.writeInt(0);
		mplew.write(0);

		addCharStats(mplew, chr);

		mplew.write(chr.getBuddylist().getCapacity());

		if (chr.getBlessOfFairyOrigin() != null) {
			mplew.write(1);
			mplew.writeMapleAsciiString(chr.getBlessOfFairyOrigin());
		} else {
			mplew.write(0);
		}

		if (chr.getBlessOfEmpressOrigin() != null) {
			mplew.write(1);
			mplew.writeMapleAsciiString(chr.getBlessOfEmpressOrigin());
		} else {
			mplew.write(0);
		}

		MapleQuestStatus ultExplorer = chr.getQuestNoAdd(MapleQuest.getInstance(111111));
		if (ultExplorer != null && ultExplorer.getCustomData() != null) {
			mplew.write(1);
			mplew.writeMapleAsciiString(ultExplorer.getCustomData());
		} else {
			mplew.write(0);
		}

		mplew.writeShort(0); // 1.2.390 ++

		mplew.writeLong(PacketHelper.getTime(-2)); // 1.2.391 ++

		// mplew.writeInt(0); // 데미지 스킨 슬롯 사이즈 1.2.391 ++
		// mplew.writeInt(0); // 데미지 스킨 보유 사이즈 1.2.391 ++
		// mplew.writeInt(0); // 1.2.391 ++
		// mplew.writeInt(0); // 1.2.391 ++
		// for (int i = 0; i < 2; i++) { // 1.2.391 ++
		// mplew.writeLong(PacketHelper.getTime(-2));
		// mplew.writeInt(-1);
		// mplew.writeInt(0);
		// }
		// mplew.writeLong(PacketHelper.getTime(-2)); // 1.2.390 ++
		addDamageSkinInfo(mplew, chr); // 1.2.391 ++

		mplew.writeLong(chr.getMeso());

		mplew.writeInt(0); // 1.2.391 ++

		addInventoryInfo(mplew, chr, false);

		addSkillInfo(mplew, chr);

		addCoolDownInfo(mplew, chr);

		mplew.writeZeroBytes(30);

		addQuestInfo(mplew, chr);

		mplew.writeShort(0);

		addRingInfo(mplew, chr);

		addRocksInfo(mplew, chr);

		chr.questInfoPacket(mplew);

		mplew.writeShort(0);
		mplew.write(true);

		chr.specialQustInfoPacket(mplew);

		mplew.writeInt(1);
		mplew.writeInt(chr.getAccountID());
		mplew.writeInt(-1);

		if (GameConstants.isWildHunter(chr.getJob())) {
			addJaguarInfo(mplew, chr);
		}

		if (GameConstants.isZero(chr.getJob())) {
			addZeroInfo(mplew, chr);
		}

		mplew.writeShort(0);
		mplew.writeShort(0);

		// 팬텀이 아닐 때에 인트 21번 돌음 (1 ~ 21)
		addStealSkills(mplew, chr);

		addAbilityInfo(mplew, chr);

		mplew.writeShort(0);

		addHonorInfo(mplew, chr);

		mplew.write(1);
		mplew.writeShort(0);

		mplew.write((chr.returnscroll != null));
		if (chr.returnscroll != null) {
			addItemInfo(mplew, chr.returnscroll);
			mplew.writeInt(chr.returnSc);
		}

		boolean tr = GameConstants.isAngelicBuster(chr.getJob());
		mplew.writeInt(tr ? chr.getSecondFace() : 0);
		int hair = chr.getSecondHair();
		if (chr.getSecondBaseColor() != -1) {
			hair = chr.getSecondHair() / 10 * 10 + chr.getSecondBaseColor();
		}
		mplew.writeInt(tr ? hair : 0);
		mplew.writeInt(tr ? 1051812 : 0);
		mplew.write(tr ? chr.getSecondSkinColor() : 0);
		mplew.write(tr ? (chr.getDressup() ? 1 : 0) : 0); // 1.2.390 ++
		mplew.writeInt(tr ? 1103663 : 0);

		mplew.write(false); // (chr.choicepotential != null && chr.memorialcube != null)

		/*
		 * if (chr.choicepotential != null && chr.memorialcube != null) {
		 * addItemInfo(mplew, chr.choicepotential);
		 * mplew.writeInt(chr.memorialcube.getItemId());
		 * mplew.writeInt(chr.choicepotential.getPosition()); }
		 */
		mplew.writeLong(0);

		mplew.writeLong(0);
		mplew.writeInt(0);
		mplew.writeInt(0);
		mplew.writeShort(0);
		mplew.writeInt(0);

		mplew.writeLong(getTime(-2));
		mplew.writeInt(0);
		mplew.writeInt(chr.getId());

		mplew.writeInt(0);
		mplew.writeInt(0);
		mplew.writeInt(0);
		mplew.writeLong(getTime(-2));

		mplew.writeInt(0); // 1.2.390 Changed. 0 -> 10

		mplew.writeInt(0);
		mplew.writeInt(0);
		mplew.writeLong(0L);

		mplew.write(0); // 1.2.390 ++
		mplew.write(0); // 1.2.390 ++

		mplew.writeShort(0); // CustomData 1 Size
		mplew.writeShort(0); // CustomData 2 Size
		mplew.writeShort(0); // 1.2.390 ++

		mplew.writeInt(0); // 1.2.390 ++

		mplew.writeShort(0); // 1.2.391 ++

		addMatrixInfo(mplew, chr);

		addHexaSkillInfo(mplew, chr.getClient()); // 1.2.390 ++

		addHexaStatInfo(mplew, chr.getClient()); // 1.2.390 ++

		addUnionArtipactInfo(mplew, chr.getClient()); // 1.2.390 ++

		mplew.writeInt(0); // 1.2.391 ++

		mplew.writeInt(chr.getAccountID());
		mplew.writeInt(chr.getId());

		mplew.writeInt(0); // 1.2.390 ++
		mplew.writeInt(0); // 1.2.390 ++
		mplew.writeInt(0);// 1.2.390 ++
		mplew.writeLong(getTime(-2)); // 1.2.390 ++

		int CustomDataSize4 = 0;
		mplew.writeInt(CustomDataSize4); // CustomData 4 Size 1.2.390 ++
		for (int i = 0; i < CustomDataSize4; i++) {
			mplew.writeInt(0);
			mplew.write(0);
			mplew.write(0);
			mplew.writeLong(0);

			int type = 0;
			mplew.writeInt(type);
			if (type == 42) {
				mplew.writeMapleAsciiString("");
			} else {
				mplew.writeLong(0);
			}
		}

		mplew.writeInt(0); // 1.2.390 ++
		mplew.writeInt(0); // 1.2.390 ++

		addHairRoomInfo(mplew, chr);

		addFaceRoomInfo(mplew, chr);

		addSkinRoomInfo(mplew, chr);

		mplew.writeInt(chr.getEmoticons().size());
		for (Triple<Long, Integer, Short> em : chr.getEmoticons()) {
			mplew.writeInt((em.mid).intValue());
			mplew.writeInt((em.mid).intValue());
			mplew.writeLong((em.left).longValue());
			mplew.writeShort((em.right).shortValue());
		}

		int count = 0;

		mplew.writeInt(chr.getEmoticonTabs().size());
		for (MapleChatEmoticon em : chr.getEmoticonTabs()) {
			mplew.writeShort(++count);
			mplew.writeInt(em.getEmoticonid());
		}

		mplew.writeShort(8);

		count = 0;

		mplew.writeInt(chr.getSavedEmoticon().size());
		for (MapleSavedEmoticon em : chr.getSavedEmoticon()) {
			mplew.writeShort(++count);
			mplew.writeInt(em.getEmoticonid());
			mplew.writeAsciiString(em.getText(), 21);
		}

		mplew.writeInt(chr.getSavedEmoticon().size());
		for (MapleSavedEmoticon em : chr.getSavedEmoticon()) {
			mplew.writeMapleAsciiString(em.getText());
			mplew.writeInt(em.getEmoticonid());
			mplew.writeAsciiString(em.getText(), 21);
		}

		mplew.writeInt(0);
	}

	public static void addHairRoomInfo(MaplePacketLittleEndianWriter mplew, MapleCharacter chr) {
		mplew.write(36); // 1.2.390 Changed. (24 -> 36)

		mplew.write(chr.getHairRoom().size());

		for (int i = 1; i <= 42; i++) { // 1.2.390 Changed. (30 -> 42)
			if (chr.getHairRoom().size() < i) {
				mplew.write(false);
			} else {
				mplew.write(i);
				MapleMannequin hair = chr.getHairRoom().get(i - 1);
				mplew.write(0);
				mplew.writeInt(hair.getValue());
			}
		}
	}

	public static void addFaceRoomInfo(MaplePacketLittleEndianWriter mplew, MapleCharacter chr) {
		mplew.write(36); // 1.2.390 Changed. (24 -> 36)

		mplew.write(chr.getFaceRoom().size());

		for (int i = 1; i <= 42; i++) { // 1.2.390 Changed. (30 -> 42)
			if (chr.getFaceRoom().size() < i) {
				mplew.write(false);
			} else {
				mplew.write(i);
				MapleMannequin face = chr.getFaceRoom().get(i - 1);
				mplew.write(0);
				mplew.writeInt(face.getValue());
			}
		}
	}

	public static void addSkinRoomInfo(MaplePacketLittleEndianWriter mplew, MapleCharacter chr) {
		mplew.write(12); // 1.2.390 Changed. (6 -> 12)

		mplew.write(chr.getSkinRoom().size());

		for (int i = 1; i <= 42; i++) { // 1.2.390 Changed. (30 -> 42)
			if (chr.getSkinRoom().size() < i) {
				mplew.write(false);
			} else {
				mplew.write(i);
				MapleMannequin skin = chr.getSkinRoom().get(i - 1);
				mplew.write(0);
				mplew.writeInt(skin.getValue());
			}
		}
	}

	public static void addMatrixInfo(MaplePacketLittleEndianWriter mplew, MapleCharacter chr) {
		mplew.writeInt(chr.getCore().size());
		for (Core m : chr.getCore()) {
			mplew.writeLong(m.getCrcId());
			mplew.writeInt(m.getCoreId());
			mplew.writeInt(m.getLevel());
			mplew.writeInt(m.getExp());
			mplew.writeInt(m.getState());
			mplew.writeInt(m.getSkill1());
			mplew.writeInt(m.getSkill2());
			mplew.writeInt(m.getSkill3());
			mplew.writeInt(m.getPosition());
			mplew.writeLong((m.getPeriod() > 0L) ? getTime(m.getPeriod()) : getTime(-1L));
			mplew.write(m.isLock());
		}

		mplew.writeInt(chr.getMatrixs().size());
		for (VMatrix matrix : chr.getMatrixs()) {
			mplew.writeInt(matrix.getId());
			mplew.writeInt(matrix.getPosition());
			mplew.writeInt(matrix.getLevel());
			mplew.write(matrix.isUnLock());
		}
	}

	public static void addHexaSkillInfo(MaplePacketLittleEndianWriter mplew, MapleClient c) {
		ArrayList<HEXASkill> hexas = HEXAHandler.hexaSkillData(c);

		mplew.writeInt(hexas.size());
		for (HEXASkill hexa : hexas) {
			mplew.writeInt(hexa.getCoreId()); // HEXA 코어 ID
			mplew.writeInt(hexa.getLevel()); // HEXA 코어 LEVEL
			mplew.write((hexa.getLevel() > 0)); // HEXA 코어 0 : 비활성화, 1 : 활성화
			mplew.write(0); // Unk
		}

		mplew.write(0); // Unk
		mplew.writeInt(0); // Unk
	}

	public static void addHexaStatInfo(MaplePacketLittleEndianWriter mplew, MapleClient c) {
		ArrayList<HEXAStat> hexas = HEXAHandler.hexaStatData(c);

		mplew.writeInt(hexas.size());
		for (HEXAStat hexa : hexas) {
			mplew.writeInt(hexa.getCoreId());

			for (int i = 0; i < 2; i++) {
				mplew.writeInt(hexa.getCoreId());

				mplew.writeInt(i);

				mplew.writeInt((i == 0 ? 1
						: (c.getPlayer().getKeyValue((hexa.getCoreId() + 666666), "HEXA Stat M S") == -1) ? 0 : 1)); // 저장
																														// 여부

				int j = 0;
				for (Pair<Integer, Integer> data : hexa.getStatDatas()) {
					if (i == 0) {
						mplew.writeInt(data.left);
						mplew.writeInt(data.right);
					} else {
						switch (j) {
						case 0:
							mplew.writeInt(c.getPlayer().getKeyValue((hexa.getCoreId() + 666666), "HEXA Stat M S"));
							mplew.writeInt(
									((c.getPlayer().getKeyValue((hexa.getCoreId() + 666666), "HEXA Stat M L") != -1)
											? c.getPlayer().getKeyValue((hexa.getCoreId() + 666666), "HEXA Stat M L")
											: 0));
							break;
						case 1:
							mplew.writeInt(c.getPlayer().getKeyValue((hexa.getCoreId() + 666666), "HEXA Stat A1 S"));
							mplew.writeInt(
									((c.getPlayer().getKeyValue((hexa.getCoreId() + 666666), "HEXA Stat A1 L") != -1)
											? c.getPlayer().getKeyValue((hexa.getCoreId() + 666666), "HEXA Stat A1 L")
											: 0));
							break;
						case 2:
							mplew.writeInt(c.getPlayer().getKeyValue((hexa.getCoreId() + 666666), "HEXA Stat A2 S"));
							mplew.writeInt(
									((c.getPlayer().getKeyValue((hexa.getCoreId() + 666666), "HEXA Stat A2 L") != -1)
											? c.getPlayer().getKeyValue((hexa.getCoreId() + 666666), "HEXA Stat A2 L")
											: 0));
							break;
						}

						j++;
					}
				}
			}
		}

		mplew.writeInt(hexas.size());
		for (HEXAStat hexa : hexas) {
			mplew.writeInt(hexa.getCoreId());
			mplew.writeInt(hexa.getCoreId());
			mplew.writeInt(c.getPlayer().getKeyValue(hexa.getCoreId(), "HEXA Stat EQUIP INDEX")); // 장착 중인 HEXA 스텟 INDEX
		}

		mplew.writeInt(0); // Unk
		mplew.writeInt(0); // Unk
	}

	public static void addUnionArtipactInfo(MaplePacketLittleEndianWriter mplew, MapleClient c) {
		int artipactSize = c.getPlayer().getArtipacts().size();

		mplew.writeInt(artipactSize);
		for (int i = 0; i < artipactSize; i++) {
			mplew.writeInt(i);
			mplew.writeInt(i);
			mplew.writeInt(c.getPlayer().getArtipacts().get(i).getLevel());

			int skillSize = c.getPlayer().getArtipacts().get(i).getSkills().size();
			for (int j = 0; j < skillSize; j++) {
				mplew.writeInt(c.getPlayer().getArtipacts().get(i).getSkills().get(j));
			}

			mplew.writeLong(PacketHelper.getTime(c.getPlayer().getArtipacts().get(i).getExpirationTime()));
		}
	}

	public static void addZeroInfo(MaplePacketLittleEndianWriter mplew, MapleCharacter chr) {
		mplew.writeShort(-1);
		mplew.write(chr.getGender());
		mplew.writeInt(chr.getStat().getHp());
		mplew.writeInt(chr.getStat().getMp());
		mplew.write(chr.getSecondSkinColor());
		int hair = chr.getSecondHair();
		if (chr.getSecondBaseColor() != -1) {
			hair = chr.getSecondHair() / 10 * 10 + chr.getSecondBaseColor();
		}
		mplew.writeInt(hair);
		mplew.writeInt(chr.getSecondFace());
		mplew.writeInt(chr.getStat().getMaxHp());
		mplew.writeInt(chr.getStat().getMaxMp());
		mplew.writeInt(0);
		mplew.writeInt(0); // 1.2.372 ++
	}

	public static void addAbilityInfo(MaplePacketLittleEndianWriter mplew, MapleCharacter chr) {
		List<List<InnerSkillValueHolder>> holders = chr.getInnerSkills();

		// Ability Preset Packet 1.2.390 ++
		for (List<InnerSkillValueHolder> holder : holders) {
			mplew.writeShort(holder.size());

			for (int i = 0; i < holder.size(); i++) {
				mplew.write(i + 1);
				mplew.writeInt(((InnerSkillValueHolder) holder.get(i)).getSkillId());
				mplew.write(((InnerSkillValueHolder) holder.get(i)).getSkillLevel());
				mplew.write(((InnerSkillValueHolder) holder.get(i)).getRank());
			}
		}
	}

	public static void addHonorInfo(MaplePacketLittleEndianWriter mplew, MapleCharacter chr) {
		mplew.writeInt(chr.getHonorLevel());
		mplew.writeInt(chr.getHonourExp());
	}

	public static void addStolenSkills(MaplePacketLittleEndianWriter mplew, MapleCharacter chr, int jobNum) {
		int count = 0;
		if (chr.getStolenSkills() != null) {
			for (Pair<Integer, Boolean> sk : chr.getStolenSkills()) {
				if (GameConstants.getJobNumber(((Integer) sk.left).intValue()) == jobNum) {
					mplew.writeInt(((Integer) sk.left).intValue());
					count++;

					if (count >= GameConstants.getNumSteal(jobNum)) {
						break;
					}
				}
			}
		}

		// 21번 순환 (Int)
		while (count < GameConstants.getNumSteal(jobNum)) {
			mplew.writeInt(0);
			count++;
		}
	}

	public static void addChosenSkills(MaplePacketLittleEndianWriter mplew, MapleCharacter chr) {
		for (int i = 1; i <= 5; i++) {
			boolean found = false;
			if (chr.getStolenSkills() != null) {
				for (Pair<Integer, Boolean> sk : chr.getStolenSkills()) {
					if (GameConstants.getJobNumber(((Integer) sk.left).intValue()) == i
							&& ((Boolean) sk.right).booleanValue()) {
						mplew.writeInt(((Integer) sk.left).intValue());
						found = true;
						break;
					}
				}
			}

			if (!found) {
				mplew.writeInt(0);
			}
		}
	}

	public static void addStealSkills(MaplePacketLittleEndianWriter mplew, MapleCharacter chr) {
		for (int i = 1; i <= 5; i++) {
			addStolenSkills(mplew, chr, i);
		}

		addChosenSkills(mplew, chr);
	}

	public static final void addPetItemInfo(MaplePacketLittleEndianWriter mplew, MapleCharacter player, Item item,
			MaplePet pet, boolean unequip, boolean petLoot) {
		if (item == null) {
			mplew.writeLong(getKoreanTimestamp((long) (System.currentTimeMillis() * 1.5D)));
		} else {
			if (item.getExpiration() <= 0L) {
				item.setExpiration(System.currentTimeMillis() + 7776000000L);
			}
			addExpirationTime(mplew, (item.getExpiration() <= System.currentTimeMillis()) ? -1L : item.getExpiration());
		}
		mplew.writeInt(-1);
		mplew.write(1);
		mplew.writeAsciiString((pet.getName() == null) ? "" : pet.getName(), 13);
		mplew.write(pet.getLevel());
		mplew.writeShort(pet.getCloseness());
		mplew.write(pet.getFullness());

		if (item == null) {
			mplew.writeLong(getKoreanTimestamp((long) (System.currentTimeMillis() * 1.5D)));
		} else {
			mplew.writeLong(getTime(item.getExpiration()));
		}

		mplew.writeShort(0);
		mplew.writeShort(pet.getFlags());
		mplew.writeInt(0);
		mplew.writeShort((item != null && ItemFlag.KARMA_USE.check(item.getFlag())) ? 1 : 0);
		mplew.write(unequip ? 0 : (player.getPetIndex(pet) + 1));

		mplew.writeInt(-1); // 1.2.390 Changed. (0 -> -1)
		mplew.writeShort(pet.getPetSize()); // 1.2.390 ++
		// mplew.write(0); // 1.2.390 --
		// mplew.write(1); // 1.2.390 --

		mplew.writeInt(0);
		mplew.writeShort(0);
		// mplew.write(pet.getWonderGrade()); // 1.2.390 Changed. (Short -> Byte)
	}

	public static void addShopInfo(MaplePacketLittleEndianWriter mplew, MapleShop shop, MapleClient c) {
		MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
		mplew.writeInt(0);
		mplew.writeInt(0);
		mplew.writeMapleAsciiString("");
		mplew.writeInt(0);
		mplew.writeShort(shop.getItems().size() + c.getPlayer().getRebuy().size());
		for (MapleShopItem item : shop.getItems()) {
			addShopItemInfo(mplew, item, shop, ii, null, c.getPlayer());
		}
		int i = 0;
		for (Item item : c.getPlayer().getRebuy()) {
			i++;
			addShopItemInfo(mplew,
					new MapleShopItem(0, (short) 1000, item.getItemId(), (int) ii.getPrice(item.getItemId()), 0,
							(byte) 0, (short) item.getQuantity(), 0, i, 0, 0, 0, 0, 0, 0, 0),
					shop, ii, item, c.getPlayer());
		}
		if (shop.getNpcId() == 9001212) {
			c.send(CField.NPCPacket.BossRewardSetting());
		}
	}

	public static void addShopItemInfo(MaplePacketLittleEndianWriter mplew, MapleShopItem item, MapleShop shop,
			MapleItemInformationProvider ii, Item i, MapleCharacter chr) {
		if (shop.getQuestEx() != 0) {
			int quantity = (int) chr.getKeyValue(shop.getQuestEx() + 100000, item.getShopItemId() + "_buyed");
			if (quantity <= -1) {
				quantity = 0;
			}
			mplew.writeInt(item.getBuyQuantity() - quantity);
		} else {
			mplew.writeInt(item.getBuyQuantity());
		}
		mplew.writeInt(item.getItemId());
		mplew.writeInt(item.getTab());
		if (shop.getQuestEx() != 0) {
			int quantity = (int) chr.getKeyValue(shop.getQuestEx() + 100000, item.getShopItemId() + "_buyed");
			if (quantity <= -1) {
				quantity = 0;
			}
			mplew.writeInt(item.getBuyQuantity() - quantity);
		} else {
			mplew.writeInt(item.getBuyQuantity());
		}
		mplew.writeInt(0);
		mplew.writeInt(item.getPeriod() * 1440);
		if (shop.getCoinKey() > 0) {
			mplew.writeLong(0);
			mplew.writeInt(0);
			mplew.writeInt(0);
		} else if (item.getPriceQuantity() > 0) {
			mplew.writeLong(0);
			mplew.writeInt(item.getPrice());
			mplew.writeInt(item.getPriceQuantity());
		} else {
			mplew.writeLong(item.getPrice());
			mplew.writeInt(0);
			mplew.writeInt(0);
		}
		mplew.writeInt(shop.getCoinKey());
		mplew.writeInt((shop.getCoinKey() > 0) ? item.getPrice() : 0L);
		mplew.write((shop.getSaleString().length() > 0));
		if (shop.getSaleString().length() > 0) {
			mplew.writeInt(30);
			mplew.write(1);
			mplew.write(0);
			mplew.writeMapleAsciiString("");
			mplew.writeInt(0);
			mplew.writeMapleAsciiString("");
			mplew.writeLong(getTime(-2L));
			mplew.writeLong(getTime(-2L));
			mplew.writeInt(0);
		}
		mplew.write(0);
		mplew.writeInt(0);
		mplew.write(item.getReCharge() > 0);
		if (item.getReCharge() > 0) {
			mplew.writeInt(0);
			mplew.writeLong(getMapleShopTime(item.getReChargeMonth(), item.getResetday(), item.getReCharge()));
		}
		mplew.writeInt((item.getReChargeCount() > 0) ? item.getReChargeCount() : 0);
		mplew.writeShort(0);
		mplew.writeShort(0);
		mplew.write((item.getReCharge() > 0) ? 1 : 0);
		mplew.writeInt(0); // 1.2.390 ++
		mplew.writeLong(getTime(-2L));
		mplew.writeLong(getTime(-1L));
		mplew.writeInt(0);
		mplew.writeShort(1);
		mplew.write(0);
		mplew.writeInt(0); // 1.2.390 ++
		mplew.writeInt(0); // 1.2.390 ++
		mplew.writeShort(0); // 1.2.390 ++
		mplew.writeInt(shop.getQuestEx());
		mplew.writeMapleAsciiString(shop.getShopString());
		mplew.writeInt(item.getItemRate());
		mplew.writeInt(0);
		mplew.write(0);
		mplew.writeMapleAsciiString("");
		if (!GameConstants.isThrowingStar(item.getItemId()) && !GameConstants.isBullet(item.getItemId())) {
			mplew.writeShort((item.getQuantity() > 1) ? item.getQuantity() : 1);
			mplew.writeShort(item.getBuyable());
		} else {
			mplew.write(HexTool.getByteArrayFromHexString("9A 99 99 99 99 99"));
			mplew.writeShort(BitTools.doubleToShortBits(ii.getPrice(item.getItemId())));
			mplew.writeShort(ii.getSlotMax(item.getItemId()));
		}
		mplew.write(0); // 1.2.390 ++
		mplew.write((i == null) ? 0 : 1);
		if (i != null) {
			addItemInfo(mplew, i);
		}
	}

	public static final void addJaguarInfo(MaplePacketLittleEndianWriter mplew, MapleCharacter chr) {
		mplew.write(chr.getInfoQuest(123456).equals("") ? 0 : Byte.parseByte(chr.getInfoQuest(123456)));
		for (int i = 0; i < 5; i++) {
			mplew.writeInt(0);
		}
	}

	public static void writeMonsterMask(MaplePacketLittleEndianWriter mplew,
			List<Pair<MonsterStatus, MonsterStatusEffect>> statups) {
		int[] mask = new int[5];
		for (Pair<MonsterStatus, MonsterStatusEffect> statup : statups) {
			mask[((MonsterStatus) statup.left).getPosition()
					- 1] = mask[((MonsterStatus) statup.left).getPosition() - 1]
							| ((MonsterStatus) statup.left).getValue();
		}
		for (int i = mask.length; i >= 1; i--) {
			mplew.writeInt(mask[i - 1]);
		}
	}

	public static void writeMonsterMaskT(MaplePacketLittleEndianWriter mplew,
			Collection<Pair<MonsterStatus, MonsterStatusEffect>> statups) {
		int[] mask = new int[5];
		for (Pair<MonsterStatus, MonsterStatusEffect> statup : statups) {
			mask[((MonsterStatus) statup.left).getPosition()
					- 1] = mask[((MonsterStatus) statup.left).getPosition() - 1]
							| ((MonsterStatus) statup.left).getValue();
		}
		for (int i = mask.length; i >= 1; i--) {
			mplew.writeInt(mask[i - 1]);
		}
	}

	public static void writeBuffMask(MaplePacketLittleEndianWriter mplew, List<SecondaryStat> statups) {
		int[] mask = new int[31];
		for (SecondaryStat statup : statups) {
			mask[statup.getPosition() - 1] = mask[statup.getPosition() - 1] | statup.getValue();
		}
		for (int i = mask.length; i >= 1; i--) {
			mplew.writeInt(mask[i - 1]);
		}
	}

	public static void writeBuffMask(MaplePacketLittleEndianWriter mplew,
			Collection<Pair<SecondaryStat, Pair<Integer, Integer>>> statups) {
		int[] mask = new int[31];
		for (Pair<SecondaryStat, Pair<Integer, Integer>> statup : statups) {
			mask[((SecondaryStat) statup.left).getPosition()
					- 1] = mask[((SecondaryStat) statup.left).getPosition() - 1]
							| ((SecondaryStat) statup.left).getValue();
		}
		for (int i = mask.length; i >= 1; i--) {
			mplew.writeInt(mask[i - 1]);
		}
	}

	public static List<Pair<SecondaryStat, Pair<Integer, Integer>>> sortBuffStats(
			Map<SecondaryStat, Pair<Integer, Integer>> statups) {
		boolean changed;
		List<Pair<SecondaryStat, Pair<Integer, Integer>>> statvals = new ArrayList<>();
		for (Map.Entry<SecondaryStat, Pair<Integer, Integer>> stat : statups.entrySet()) {
			statvals.add(new Pair<>(stat.getKey(), stat.getValue()));
		}
		do {
			changed = false;
			int i = 0;
			int k = 1;
			for (int iter = 0; iter < statvals.size() - 1; iter++) {
				Pair<SecondaryStat, Pair<Integer, Integer>> a = statvals.get(i);
				Pair<SecondaryStat, Pair<Integer, Integer>> b = statvals.get(k);
				if (a != null && b != null && ((SecondaryStat) a.left).getFlag() > ((SecondaryStat) b.left).getFlag()) {
					Pair<SecondaryStat, Pair<Integer, Integer>> swap = new Pair<>((SecondaryStat) a.left,
							(Pair<Integer, Integer>) a.right);
					statvals.remove(i);
					statvals.add(i, b);
					statvals.remove(k);
					statvals.add(k, swap);
					changed = true;
				}
				i++;
				k++;
			}
		} while (changed);
		return statvals;
	}

	public static List<Pair<SecondaryStat, List<SecondaryStatValueHolder>>> sortIndieBuffStats(
			Map<SecondaryStat, List<SecondaryStatValueHolder>> statups) {
		boolean changed;
		List<Pair<SecondaryStat, List<SecondaryStatValueHolder>>> statvals = new ArrayList<>();
		for (Map.Entry<SecondaryStat, List<SecondaryStatValueHolder>> stat : statups.entrySet()) {
			statvals.add(new Pair<>(stat.getKey(), stat.getValue()));
		}
		do {
			changed = false;
			int i = 0;
			int k = 1;
			for (int iter = 0; iter < statvals.size() - 1; iter++) {
				Pair<SecondaryStat, List<SecondaryStatValueHolder>> a = statvals.get(i);
				Pair<SecondaryStat, List<SecondaryStatValueHolder>> b = statvals.get(k);
				if (a != null && b != null && ((SecondaryStat) a.left).getFlag() > ((SecondaryStat) b.left).getFlag()) {
					Pair<SecondaryStat, List<SecondaryStatValueHolder>> swap = new Pair<>((SecondaryStat) a.left,
							(List<SecondaryStatValueHolder>) a.right);
					statvals.remove(i);
					statvals.add(i, b);
					statvals.remove(k);
					statvals.add(k, swap);
					changed = true;
				}
				i++;
				k++;
			}
		} while (changed);
		return statvals;
	}

	public static List<Pair<MonsterStatus, MonsterStatusEffect>> sortMBuffStats(
			List<Pair<MonsterStatus, MonsterStatusEffect>> statups) {
		List<Pair<MonsterStatus, MonsterStatusEffect>> statvals = new LinkedList<>();
		for (Pair<MonsterStatus, MonsterStatusEffect> ms : statups) {
			if (ms != null) {
				statvals.add(new Pair<>(ms.getLeft(), ms.getRight()));
			}
		}
		while (true) {
			boolean changed = false;
			int i = 0;
			int k = 1;
			for (int iter = 0; iter < statvals.size() - 1; iter++) {
				Pair<MonsterStatus, MonsterStatusEffect> a = statvals.get(i);
				Pair<MonsterStatus, MonsterStatusEffect> b = statvals.get(k);
				if (a != null && b != null && ((MonsterStatus) a.left).getFlag() > ((MonsterStatus) b.left).getFlag()) {
					Pair<MonsterStatus, MonsterStatusEffect> swap = new Pair<>((MonsterStatus) a.left,
							(MonsterStatusEffect) a.right);
					statvals.remove(i);
					statvals.add(i, b);
					statvals.remove(k);
					statvals.add(k, swap);
					changed = true;
				}
				i++;
				k++;
			}
			if (!changed) {
				return statvals;
			}
		}
	}

	public static void ArcaneSymbol(MaplePacketLittleEndianWriter mplew, Item item) {
		Equip equip = (Equip) item;
		mplew.writeInt(0);
		mplew.writeInt(equip.getArcLevel());
		mplew.writeInt(equip.getArcLevel() * equip.getArcLevel() + 11);
		mplew.writeLong((12440000 + 6600000 * equip.getArcLevel()));
		mplew.writeLong(0L);
		for (byte i = 0; i < 12; i = (byte) (i + 1)) {
			mplew.writeShort(0);
		}
	}

	public static void encodeForRemote(MaplePacketLittleEndianWriter mplew,
			Map<SecondaryStat, Pair<Integer, Integer>> statups, MapleCharacter chr) {
		if (statups.containsKey(SecondaryStat.Speed)) { // 1.2.391 ok.
			mplew.write(chr.getBuffedSkill(SecondaryStat.Speed));
		}
		if (statups.containsKey(SecondaryStat.ComboCounter)) { // 1.2.391 ok.
			mplew.write(chr.getBuffedSkill(SecondaryStat.ComboCounter));
		}
		if (statups.containsKey(SecondaryStat.BlessedHammer)) { // 1.2.391 ok.
			mplew.writeShort(chr.getElementalCharge());
			mplew.writeInt((chr.getBuffSource(SecondaryStat.BlessedHammer) == 0) ? 400011052
					: chr.getBuffSource(SecondaryStat.BlessedHammer));
		}
		if (statups.containsKey(SecondaryStat.SnowCharge)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.SnowCharge));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.SnowCharge));
		}
		if (statups.containsKey(SecondaryStat.ElementalCharge)) { // 1.2.391 ok.
			mplew.writeShort(chr.getElementalCharge());
		}
		if (statups.containsKey(SecondaryStat.Stun)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.Stun));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Stun));
		}
		if (statups.containsKey(SecondaryStat.PinkbeanMinibeenMove)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.PinkbeanMinibeenMove));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.PinkbeanMinibeenMove));
		}
		if (statups.containsKey(SecondaryStat.Shock)) { // 1.2.391 ok.
			mplew.write(chr.getBuffedSkill(SecondaryStat.Shock));
		}
		if (statups.containsKey(SecondaryStat.Darkness)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.Darkness));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Darkness));
		}
		if (statups.containsKey(SecondaryStat.Seal)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.Seal));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Seal));
		}
		if (statups.containsKey(SecondaryStat.Weakness)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.Weakness));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Weakness));
		}
		if (statups.containsKey(SecondaryStat.WeaknessMdamage)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.WeaknessMdamage));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.WeaknessMdamage));
		}
		if (statups.containsKey(SecondaryStat.Curse)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.Curse));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Curse));
		}
		if (statups.containsKey(SecondaryStat.Slow)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.Slow));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Slow));
		}
		if (statups.containsKey(SecondaryStat.PvPRaceEffect)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.PvPRaceEffect));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.PvPRaceEffect));
		}
		if (statups.containsKey(SecondaryStat.TimeBomb)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.TimeBomb));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.TimeBomb));
		}
		if (statups.containsKey(SecondaryStat.Team)) { // 1.2.391 ok.
			mplew.write(chr.getBuffedSkill(SecondaryStat.Team));
		}
		if (statups.containsKey(SecondaryStat.DisOrder)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.DisOrder));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.DisOrder));
		}
		if (statups.containsKey(SecondaryStat.Thread)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.Thread));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Thread));
		}
		if (statups.containsKey(SecondaryStat.Poison)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.Poison));
		}
		if (statups.containsKey(SecondaryStat.Poison)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.Poison));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Poison));
		}
		if (statups.containsKey(SecondaryStat.ShadowPartner)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.ShadowPartner));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.ShadowPartner));
		}
		if (statups.containsKey(SecondaryStat.Morph)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.Morph));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Morph));
		}
		if (statups.containsKey(SecondaryStat.Ghost)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.Ghost));
		}
		if (statups.containsKey(SecondaryStat.Attract)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.Attract));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Attract));
		}
		if (statups.containsKey(SecondaryStat.Magnet)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.Magnet));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Magnet));
		}
		if (statups.containsKey(SecondaryStat.MagnetArea)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.MagnetArea));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.MagnetArea));
		}
		if (statups.containsKey(SecondaryStat.NoBulletConsume)) { // 1.2.391 ok.
			mplew.writeInt(chr.getBuffSource(SecondaryStat.NoBulletConsume));
		}
		if (statups.containsKey(SecondaryStat.BanMap)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.BanMap));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.BanMap));
		}
		if (statups.containsKey(SecondaryStat.Barrier)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.Barrier));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Barrier));
		}
		if (statups.containsKey(SecondaryStat.DojangShield)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.DojangShield));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.DojangShield));
		}
		if (statups.containsKey(SecondaryStat.ReverseInput)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.ReverseInput));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.ReverseInput));
		}
		if (statups.containsKey(SecondaryStat.RespectPImmune)) { // 1.2.391 ok.
			mplew.writeInt(chr.getBuffSource(SecondaryStat.RespectPImmune));
		}
		if (statups.containsKey(SecondaryStat.RespectMImmune)) { // 1.2.391 ok.
			mplew.writeInt(chr.getBuffSource(SecondaryStat.RespectMImmune));
		}
		if (statups.containsKey(SecondaryStat.DefenseAtt)) { // 1.2.391 ok.
			mplew.writeInt(chr.getBuffSource(SecondaryStat.DefenseAtt));
		}
		if (statups.containsKey(SecondaryStat.DefenseState)) { // 1.2.391 ok.
			mplew.writeInt(chr.getBuffSource(SecondaryStat.DefenseState));
		}
		if (statups.containsKey(SecondaryStat.DojangBerserk)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.DojangBerserk));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.DojangBerserk));
		}
		if (statups.containsKey(SecondaryStat.RepeatEffect)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.RepeatEffect));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.RepeatEffect));
		}
		if (statups.containsKey(SecondaryStat.StopPortion)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.StopPortion));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.StopPortion));
		}
		if (statups.containsKey(SecondaryStat.StopMotion)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.StopMotion));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.StopMotion));
		}
		if (statups.containsKey(SecondaryStat.Fear)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.Fear));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Fear));
		}
		if (statups.containsKey(SecondaryStat.MagicShield)) { // 1.2.391 ok.
			mplew.writeInt(chr.getBuffSource(SecondaryStat.MagicShield));
		}
		if (statups.containsKey(SecondaryStat.Frozen)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.Frozen));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Frozen));
		}
		if (statups.containsKey(SecondaryStat.Frozen2)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.Frozen2));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Frozen2));
		}
		if (statups.containsKey(SecondaryStat.Web)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.Web));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Web));
		}
		if (statups.containsKey(SecondaryStat.DrawBack)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.DrawBack));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.DrawBack));
		}
		if (statups.containsKey(SecondaryStat.FinalCut)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.FinalCut));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.FinalCut));
		}
		if (statups.containsKey(SecondaryStat.Mechanic)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.Mechanic));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Mechanic));
		}
		if (statups.containsKey(SecondaryStat.Inflation)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.Inflation));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Inflation));
		}
		if (statups.containsKey(SecondaryStat.Explosion)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.Explosion));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Explosion));
		}
		if (statups.containsKey(SecondaryStat.DarkTornado)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.DarkTornado));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.DarkTornado));
		}
		if (statups.containsKey(SecondaryStat.AmplifyDamage)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.AmplifyDamage));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.AmplifyDamage));
		}
		if (statups.containsKey(SecondaryStat.HideAttack)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.HideAttack));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.HideAttack));
		}
		if (statups.containsKey(SecondaryStat.DevilishPower)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.DevilishPower));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.DevilishPower));
		}
		if (statups.containsKey(SecondaryStat.SpiritLink)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.SpiritLink));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.SpiritLink));
		}
		if (statups.containsKey(SecondaryStat.Event)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.Event));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Event));
		}
		if (statups.containsKey(SecondaryStat.Event2)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.Event2));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Event2));
		}
		if (statups.containsKey(SecondaryStat.DeathMark)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.DeathMark));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.DeathMark));
		}
		if (statups.containsKey(SecondaryStat.PainMark)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.PainMark));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.PainMark));
		}
		if (statups.containsKey(SecondaryStat.Lapidification)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.Lapidification));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Lapidification));
		}
		if (statups.containsKey(SecondaryStat.VampDeath)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.VampDeath));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.VampDeath));
		}
		if (statups.containsKey(SecondaryStat.VampDeathSummon)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.VampDeathSummon));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.VampDeathSummon));
		}
		if (statups.containsKey(SecondaryStat.VenomSnake)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.VenomSnake));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.VenomSnake));
		}
		if (statups.containsKey(SecondaryStat.PyramidEffect)) { // 1.2.391 ok.
			mplew.writeInt(chr.getBuffSource(SecondaryStat.PyramidEffect));
		}
		if (statups.containsKey(SecondaryStat.PinkbeanRollingGrade)) { // 1.2.391 ok.
			mplew.write(chr.getBuffedSkill(SecondaryStat.PinkbeanRollingGrade));
		}
		if (statups.containsKey(SecondaryStat.IgnoreTargetDEF)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.IgnoreTargetDEF));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.IgnoreTargetDEF));
		}
		if (statups.containsKey(SecondaryStat.UNK_253)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.UNK_253));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.UNK_253));
		}
		if (statups.containsKey(SecondaryStat.Invisible)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.Invisible));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Invisible));
		}
		if (statups.containsKey(SecondaryStat.Judgement)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.Judgement));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Judgement));
		}
		if (statups.containsKey(SecondaryStat.KeyDownAreaMoving)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.KeyDownAreaMoving));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.KeyDownAreaMoving));
		}
		if (statups.containsKey(SecondaryStat.StackBuff)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.StackBuff));
		}
		if (statups.containsKey(SecondaryStat.Larkness)) { // 1.2.391 ok.
			mplew.writeShort(2);
			mplew.writeInt(chr.getBuffedValue(20040219) ? 20040219
					: (chr.getBuffedValue(20040220) ? 20040220 : chr.getBuffSource(SecondaryStat.Larkness)));
		}
		if (statups.containsKey(SecondaryStat.ReshuffleSwitch)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.ReshuffleSwitch));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.ReshuffleSwitch));
		}
		if (statups.containsKey(SecondaryStat.SpecialAction)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.SpecialAction));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.SpecialAction));
		}
		if (statups.containsKey(SecondaryStat.StopForceAtominfo)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.StopForceAtominfo));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.StopForceAtominfo));
		}
		if (statups.containsKey(SecondaryStat.SoulGazeCriDamR)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.SoulGazeCriDamR));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.SoulGazeCriDamR));
		}
		if (statups.containsKey(SecondaryStat.PowerTransferGauge)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.PowerTransferGauge));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.PowerTransferGauge));
		}
		if (statups.containsKey(SecondaryStat.BlitzShield)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.BlitzShield));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.BlitzShield));
		}
		if (statups.containsKey(SecondaryStat.AffinitySlug)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.AffinitySlug));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.AffinitySlug));
		}
		if (statups.containsKey(SecondaryStat.SoulExalt)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.SoulExalt));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.SoulExalt));
		}
		if (statups.containsKey(SecondaryStat.HiddenPieceOn)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.HiddenPieceOn));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.HiddenPieceOn));
		}
		if (statups.containsKey(SecondaryStat.SmashStack)) { // 1.2.391 ok.
			mplew.writeShort(chr.getKaiserCombo());
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.MobZoneState)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.MobZoneState));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.MobZoneState));
		}
		if (statups.containsKey(SecondaryStat.GiveMeHeal)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.GiveMeHeal));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.GiveMeHeal));
		}
		if (statups.containsKey(SecondaryStat.TouchMe)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.TouchMe));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.TouchMe));
		}
		if (statups.containsKey(SecondaryStat.Contagion)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.Contagion));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Contagion));
		}
		if (statups.containsKey(SecondaryStat.Contagion)) { // 1.2.391 ok.
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Contagion));
		}
		if (statups.containsKey(SecondaryStat.ComboUnlimited)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.ComboUnlimited));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.ComboUnlimited));
		}
		if (statups.containsKey(SecondaryStat.IgnorePCounter)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.IgnorePCounter));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.IgnorePCounter));
		}
		if (statups.containsKey(SecondaryStat.IgnoreAllCounter)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.IgnoreAllCounter));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.IgnoreAllCounter));
		}
		if (statups.containsKey(SecondaryStat.IgnorePImmune)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.IgnorePImmune));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.IgnorePImmune));
		}
		if (statups.containsKey(SecondaryStat.IgnoreAllImmune)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.IgnoreAllImmune));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.IgnoreAllImmune));
		}
		if (statups.containsKey(SecondaryStat.UnkBuffStat6)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.UnkBuffStat6));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.UnkBuffStat6));
		}
		if (statups.containsKey(SecondaryStat.FireAura)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.FireAura));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.FireAura));
		}
		if (statups.containsKey(SecondaryStat.HeavensDoor)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.HeavensDoor));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.HeavensDoor));
		}
		if (statups.containsKey(SecondaryStat.DamAbsorbShield)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.DamAbsorbShield));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.DamAbsorbShield));
		}
		if (statups.containsKey(SecondaryStat.AntiMagicShell)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.AntiMagicShell));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.AntiMagicShell));
		}
		if (statups.containsKey(SecondaryStat.NotDamaged)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.NotDamaged));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.NotDamaged));
		}
		if (statups.containsKey(SecondaryStat.BleedingToxin)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.BleedingToxin));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.BleedingToxin));
		}
		if (statups.containsKey(SecondaryStat.WindBreakerFinal)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.WindBreakerFinal));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.WindBreakerFinal));
		}
		if (statups.containsKey(SecondaryStat.Origin_Karma_Blade_Stack)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.Origin_Karma_Blade_Stack));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Origin_Karma_Blade_Stack));
		}
		if (statups.containsKey(SecondaryStat.IgnoreMobDamR)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.IgnoreMobDamR));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.IgnoreMobDamR));
		}
		if (statups.containsKey(SecondaryStat.Asura)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.Asura));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Asura));
		}
		if (statups.containsKey(SecondaryStat.MegaSmasher)) { // 1.2.391 ok.
			mplew.writeInt(chr.getBuffedSkill(SecondaryStat.MegaSmasher));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.MegaSmasher));
		}
		if (statups.containsKey(SecondaryStat.MegaSmasher)) { // 1.2.391 ok.
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.UnityOfPower)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.UnityOfPower));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.UnityOfPower));
		}
		if (statups.containsKey(SecondaryStat.Stimulate)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.Stimulate));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Stimulate));
		}
		if (statups.containsKey(SecondaryStat.ReturnTeleport)) { // 1.2.391 ok.
			mplew.write(chr.getBuffedSkill(SecondaryStat.ReturnTeleport));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.ReturnTeleport));
		}
		if (statups.containsKey(SecondaryStat.CapDebuff)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.CapDebuff));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.CapDebuff));
		}
		if (statups.containsKey(SecondaryStat.OverloadCount)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.OverloadCount));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.OverloadCount));
		}
		if (statups.containsKey(SecondaryStat.FireBomb)) { // 1.2.391 ok.
			mplew.write(chr.getBuffedSkill(SecondaryStat.FireBomb));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.FireBomb));
		}
		if (statups.containsKey(SecondaryStat.SurplusSupply)) { // 1.2.391 ok.
			mplew.write(chr.getBuffedSkill(SecondaryStat.SurplusSupply));
		}
		if (statups.containsKey(SecondaryStat.NewFlying)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.NewFlying));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.NewFlying));
		}
		if (statups.containsKey(SecondaryStat.NaviFlying)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.NaviFlying));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.NaviFlying));
		}
		if (statups.containsKey(SecondaryStat.AmaranthGenerator)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.AmaranthGenerator));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.AmaranthGenerator));
		}
		if (statups.containsKey(SecondaryStat.CygnusElementSkill)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.CygnusElementSkill));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.CygnusElementSkill));
		}
		if (statups.containsKey(SecondaryStat.StrikerHyperElectric)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.StrikerHyperElectric));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.StrikerHyperElectric));
		}
		if (statups.containsKey(SecondaryStat.EventPointAbsorb)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.EventPointAbsorb));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.EventPointAbsorb));
		}
		if (statups.containsKey(SecondaryStat.EventAssemble)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.EventAssemble));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.EventAssemble));
		}
		if (statups.containsKey(SecondaryStat.Translucence)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.Translucence));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Translucence));
		}
		if (statups.containsKey(SecondaryStat.PoseType)) { // // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedValue(11121012) ? 1 : chr.getBuffedSkill(SecondaryStat.PoseType));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.PoseType));
		}
		if (statups.containsKey(SecondaryStat.CosmicForge)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.CosmicForge));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.CosmicForge));
		}
		if (statups.containsKey(SecondaryStat.ElementSoul)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.ElementSoul));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.ElementSoul));
		}
		if (statups.containsKey(SecondaryStat.GlimmeringTime)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.GlimmeringTime));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.GlimmeringTime));
		}
		if (statups.containsKey(SecondaryStat.ChargeBuff)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.ChargeBuff));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.ChargeBuff));
		}
		if (statups.containsKey(SecondaryStat.Beholder)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.Beholder));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Beholder));
		}
		if (statups.containsKey(SecondaryStat.QuiverCatridge)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.QuiverCatridge));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.QuiverCatridge));
		}
		if (statups.containsKey(SecondaryStat.ImmuneBarrier)) { // 1.2.391 ok.
			mplew.writeInt(chr.getBuffSource(SecondaryStat.ImmuneBarrier));
		}
		if (statups.containsKey(SecondaryStat.ImmuneBarrier)) { // 1.2.391 ok.
			mplew.writeInt(chr.getBuffSource(SecondaryStat.ImmuneBarrier));
		}
		if (statups.containsKey(SecondaryStat.FullSoulMP)) { // 1.2.391 ok.
			mplew.writeInt(0);
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.AntiMagicShell)) { // 1.2.391 ok.
			mplew.write(chr.getAntiMagicShell());
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.Dance)) { // 1.2.391 ok.
			mplew.writeInt(chr.getBuffedSkill(SecondaryStat.Dance));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Dance));
		}
		if (statups.containsKey(SecondaryStat.SpiritGuard)) { // 1.2.391 ok.
			mplew.writeInt(chr.getBuffedSkill(SecondaryStat.SpiritGuard));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.SpiritGuard));
		}
		if (statups.containsKey(SecondaryStat.DemonDamageAbsorbShield)) { // 1.2.391 ok.
			mplew.writeInt(chr.getSkillCustomValue0(400001016));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.DemonDamageAbsorbShield));
		}
		if (statups.containsKey(SecondaryStat.ComboTempest)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.ComboTempest));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.ComboTempest));
		}
		if (statups.containsKey(SecondaryStat.HalfstatByDebuff)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.HalfstatByDebuff));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.HalfstatByDebuff));
		}
		if (statups.containsKey(SecondaryStat.ComplusionSlant)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.ComplusionSlant));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.ComplusionSlant));
		}
		if (statups.containsKey(SecondaryStat.JaguarSummoned)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.JaguarSummoned));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.JaguarSummoned));
		}
		if (statups.containsKey(SecondaryStat.BombTime)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.BombTime));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.BombTime));
		}
		if (statups.containsKey(SecondaryStat.Transform)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.Transform));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Transform));
		}
		if (statups.containsKey(SecondaryStat.EnergyBurst)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.EnergyBurst));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.EnergyBurst));
		}
		if (statups.containsKey(SecondaryStat.Striker1st)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.Striker1st));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Striker1st));
		}
		if (statups.containsKey(SecondaryStat.BulletParty)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.BulletParty));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.BulletParty));
		}
		if (statups.containsKey(SecondaryStat.SelectDice)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.SelectDice));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.SelectDice));
		}
		if (statups.containsKey(SecondaryStat.Pray)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.Pray));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Pray));
		}
		if (statups.containsKey(SecondaryStat.DarkLighting)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.DarkLighting));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.DarkLighting));
		}
		if (statups.containsKey(SecondaryStat.AttackCountX)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.AttackCountX));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.AttackCountX));
		}
		if (statups.containsKey(SecondaryStat.FireBarrier)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.FireBarrier));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.FireBarrier));
		}
		if (statups.containsKey(SecondaryStat.KeyDownMoving)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.KeyDownMoving));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.KeyDownMoving));
		}
		if (statups.containsKey(SecondaryStat.MichaelSoulLink)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.MichaelSoulLink));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.MichaelSoulLink));
		}
		if (statups.containsKey(SecondaryStat.KinesisPsychicEnergeShield)) { // 1.2.391 ok.
			mplew.writeInt(chr.getBuffedSkill(SecondaryStat.KinesisPsychicEnergeShield));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.KinesisPsychicEnergeShield));
		}
		if (statups.containsKey(SecondaryStat.BladeStance)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.BladeStance));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.BladeStance));
		}
		if (statups.containsKey(SecondaryStat.BladeStance)) { // 1.2.391 ok.
			mplew.writeInt(chr.getBuffSource(SecondaryStat.BladeStance));
		}
		if (statups.containsKey(SecondaryStat.Fever)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.Fever));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Fever));
		}
		if (statups.containsKey(SecondaryStat.AdrenalinBoost)) { // 1.2.391 ok.
			mplew.writeInt(chr.getBuffSource(SecondaryStat.AdrenalinBoost));
		}
		if (statups.containsKey(SecondaryStat.RwBarrier)) { // 1.2.391 ok.
			mplew.writeInt(chr.getBuffSource(SecondaryStat.RwBarrier));
		}
		if (statups.containsKey(SecondaryStat.RWUnk)) { // 1.2.391 ok.
			mplew.writeInt(chr.getBuffSource(SecondaryStat.RWUnk));
		}
		if (statups.containsKey(SecondaryStat.RwMagnumBlow)) { // 1.2.391 ok.
			mplew.writeInt(chr.getBuffSource(SecondaryStat.RwMagnumBlow));
		}
		if (statups.containsKey(SecondaryStat.SerpentScrew)) { // 1.2.391 ok.
			mplew.writeInt(chr.getBuffSource(SecondaryStat.SerpentScrew));
		}
		if (statups.containsKey(SecondaryStat.Unk_753)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.Unk_753));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Unk_753));
		}
		if (statups.containsKey(SecondaryStat.Cosmos)) { // 1.2.391 ok.
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Cosmos));
		}
		if (statups.containsKey(SecondaryStat.GuidedArrow)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.GuidedArrow));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.GuidedArrow));
		}
		if (statups.containsKey(SecondaryStat.UnkBuffStat4)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.UnkBuffStat4));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.UnkBuffStat4));
		}
		if (statups.containsKey(SecondaryStat.BlessMark)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.BlessMark));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.BlessMark));
		}
		if (statups.containsKey(SecondaryStat.BonusAttack)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.BonusAttack));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.BonusAttack));
		}
		if (statups.containsKey(SecondaryStat.UnkBuffStat5)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.UnkBuffStat5));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.UnkBuffStat5));
		}
		if (statups.containsKey(SecondaryStat.Stigma)) { // 1.2.391 ok.
			mplew.writeShort(chr.Stigma);
			mplew.writeInt(7);
		}
		if (statups.containsKey(SecondaryStat.HolyUnity)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.HolyUnity));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.HolyUnity));
		}
		if (statups.containsKey(SecondaryStat.RhoAias)) { // 1.2.391 ok.
			mplew.writeInt(chr.getBuffedSkill(SecondaryStat.RhoAias));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.RhoAias));
		}
		if (statups.containsKey(SecondaryStat.PsychicTornado)) { // 1.2.391 ok.
			mplew.writeInt(chr.getBuffSource(SecondaryStat.PsychicTornado));
		}
		if (statups.containsKey(SecondaryStat.InstallMaha)) { // 1.2.391 ok.
			mplew.writeInt(chr.getBuffSource(SecondaryStat.InstallMaha));
		}
		if (statups.containsKey(SecondaryStat.OverloadMana)) { // 1.2.391 ok.
			mplew.writeInt(chr.getBuffSource(SecondaryStat.OverloadMana));
		}
		if (statups.containsKey(SecondaryStat.TrueSniping)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.TrueSniping));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.TrueSniping));
		}
		if (statups.containsKey(SecondaryStat.KawoongDebuff)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.KawoongDebuff));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.KawoongDebuff));
		}
		if (statups.containsKey(SecondaryStat.Spotlight)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.Spotlight));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Spotlight));
		}
		if (statups.containsKey(SecondaryStat.Overload)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.Overload));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Overload));
		}
		if (statups.containsKey(SecondaryStat.FreudsProtection)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.FreudsProtection));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.FreudsProtection));
		}
		if (statups.containsKey(SecondaryStat.BlessedHammer2)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.BlessedHammer2));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.BlessedHammer2));
		}
		if (statups.containsKey(SecondaryStat.OverDrive)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.OverDrive));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.OverDrive));
		}
		if (statups.containsKey(SecondaryStat.Etherealform)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.Etherealform));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Etherealform));
		}
		if (statups.containsKey(SecondaryStat.ReadyToDie)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.ReadyToDie));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.ReadyToDie));
		}
		if (statups.containsKey(SecondaryStat.Oblivion)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.Oblivion));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Oblivion));
		}
		if (statups.containsKey(SecondaryStat.CriticalReinForce)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.CriticalReinForce));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.CriticalReinForce));
		}
		if (statups.containsKey(SecondaryStat.CurseOfCreation)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.CurseOfCreation));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.CurseOfCreation));
		}
		if (statups.containsKey(SecondaryStat.CurseOfDestruction)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.CurseOfDestruction));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.CurseOfDestruction));
		}
		if (statups.containsKey(SecondaryStat.BlackMageDebuff)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.BlackMageDebuff));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.BlackMageDebuff));
		}
		if (statups.containsKey(SecondaryStat.BodyOfSteal)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.BodyOfSteal));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.BodyOfSteal));
		}
		if (statups.containsKey(SecondaryStat.GloryWing)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.GloryWing));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.GloryWing));
		}
		if (statups.containsKey(SecondaryStat.PapulCuss)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.PapulCuss));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.PapulCuss));
		}
		if (statups.containsKey(SecondaryStat.PapulCuss)) { // 1.2.391 ok.
			mplew.writeInt(chr.getBuffSource(SecondaryStat.PapulCuss));
		}
		if (statups.containsKey(SecondaryStat.PapulBomb)) { // 1.2.391 ok.
			mplew.writeInt(chr.getBuffedSkill(SecondaryStat.PapulBomb));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.PapulBomb));
		}
		if (statups.containsKey(SecondaryStat.HarmonyLink)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.HarmonyLink));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.HarmonyLink));
		}
		if (statups.containsKey(SecondaryStat.FastCharge)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.FastCharge));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.FastCharge));
		}
		if (statups.containsKey(SecondaryStat.SpectorTransForm)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.SpectorTransForm));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.SpectorTransForm));
		}
		if (statups.containsKey(SecondaryStat.ComingDeath)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.ComingDeath));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.ComingDeath));
		}
		if (statups.containsKey(SecondaryStat.FightJazz)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.FightJazz));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.FightJazz));
		}
		if (statups.containsKey(SecondaryStat.ChargeSpellAmplification)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.ChargeSpellAmplification));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.ChargeSpellAmplification));
		}
		if (statups.containsKey(SecondaryStat.WillPoison)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.WillPoison));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.WillPoison));
		}
		if (statups.containsKey(SecondaryStat.PapulBomb)) { // 1.2.391 ok.
			mplew.writeInt(chr.getBuffSource(SecondaryStat.PapulBomb));
		}
		if (statups.containsKey(SecondaryStat.GrandCrossSize)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.GrandCrossSize));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.GrandCrossSize));
		}
		if (statups.containsKey(SecondaryStat.Protective)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.Protective));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Protective));
		}
		if (statups.containsKey(SecondaryStat.UNK_596)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.UNK_596));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.UNK_596));
		}
		if (statups.containsKey(SecondaryStat.BattlePvP_Wonky_Awesome)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.BattlePvP_Wonky_Awesome));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.BattlePvP_Wonky_Awesome));
		}
		if (statups.containsKey(SecondaryStat.UnkBuffStat42)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.UnkBuffStat42));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.UnkBuffStat42));
		}
		if (statups.containsKey(SecondaryStat.UnkBuffStat43)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.UnkBuffStat43));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.UnkBuffStat43));
		}
		if (statups.containsKey(SecondaryStat.UNK_601)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.UNK_601));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.UNK_601));
		}
		if (statups.containsKey(SecondaryStat.UNK_602)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.UNK_602));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.UNK_602));
		}
		if (statups.containsKey(SecondaryStat.Unk_603)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.Unk_603));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Unk_603));
		}
		if (statups.containsKey(SecondaryStat.Unk_604)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.Unk_604));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Unk_604));
		}
		if (statups.containsKey(SecondaryStat.PinkBeanMatroCyca)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.PinkBeanMatroCyca));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.PinkBeanMatroCyca));
		}
		if (statups.containsKey(SecondaryStat.UnkBuffStat50)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.UnkBuffStat50));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.UnkBuffStat50));
		}
		if (statups.containsKey(SecondaryStat.AltergoReinforce)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.AltergoReinforce));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.AltergoReinforce));
		}
		if (statups.containsKey(SecondaryStat.YalBuff)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.YalBuff));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.YalBuff));
		}
		if (statups.containsKey(SecondaryStat.IonBuff)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.IonBuff));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.IonBuff));
		}
		if (statups.containsKey(SecondaryStat.UnkBuffStat53)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.UnkBuffStat53));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.UnkBuffStat53));
		}
		if (statups.containsKey(SecondaryStat.Graffiti)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.Graffiti));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Graffiti));
		}
		if (statups.containsKey(SecondaryStat.QuiverFullBurst)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.QuiverFullBurst));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.QuiverFullBurst));
		}
		if (statups.containsKey(SecondaryStat.Novility)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.Novility));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Novility));
		}
		if (statups.containsKey(SecondaryStat.RuneOfPure)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.RuneOfPure));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.RuneOfPure));
		}
		if (statups.containsKey(SecondaryStat.RuneOfTransition)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.RuneOfTransition));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.RuneOfTransition));
		}
		if (statups.containsKey(SecondaryStat.DuskDarkness)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.DuskDarkness));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.DuskDarkness));
		}
		if (statups.containsKey(SecondaryStat.YellowAura)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.YellowAura));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.YellowAura));
		}
		if (statups.containsKey(SecondaryStat.DrainAura)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.DrainAura));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.DrainAura));
		}
		if (statups.containsKey(SecondaryStat.BlueAura)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.BlueAura));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.BlueAura));
		}
		if (statups.containsKey(SecondaryStat.DarkAura)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.DarkAura));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.DarkAura));
		}
		if (statups.containsKey(SecondaryStat.DebuffAura)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.DebuffAura));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.DebuffAura));
		}
		if (statups.containsKey(SecondaryStat.UnionAura)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.UnionAura));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.UnionAura));
		}
		if (statups.containsKey(SecondaryStat.IceAura)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.IceAura));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.IceAura));
		}
		if (statups.containsKey(SecondaryStat.KnightsAura)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.KnightsAura));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.KnightsAura));
		}
		if (statups.containsKey(SecondaryStat.ZeroAuraStr)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.ZeroAuraStr));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.ZeroAuraStr));
		}
		if (statups.containsKey(SecondaryStat.IncarnationAura)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.IncarnationAura));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.IncarnationAura));
		}
		if (statups.containsKey(SecondaryStat.BlizzardTempest)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.BlizzardTempest));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.BlizzardTempest));
		}
		if (statups.containsKey(SecondaryStat.PhotonRay)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.PhotonRay));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.PhotonRay));
		}
		if (statups.containsKey(SecondaryStat.DarknessAura)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.DarknessAura));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.DarknessAura));
		}
		if (statups.containsKey(SecondaryStat.SilhouetteMirage)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.SilhouetteMirage));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.SilhouetteMirage));
		}
		if (statups.containsKey(SecondaryStat.LiberationOrbActive)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.LiberationOrbActive));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.LiberationOrbActive));
		}
		if (statups.containsKey(SecondaryStat.ThanatosDescent)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.ThanatosDescent));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.ThanatosDescent));
		}
		if (statups.containsKey(SecondaryStat.Unk_678)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.Unk_678));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Unk_678));
		}
		if (statups.containsKey(SecondaryStat.YetiAnger)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.YetiAnger));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.YetiAnger));
		}
		if (statups.containsKey(SecondaryStat.YetiSpicy)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.YetiSpicy));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.YetiSpicy));
		}
		if (statups.containsKey(SecondaryStat.흡수_강)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.흡수_강));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.흡수_강));
		}
		if (statups.containsKey(SecondaryStat.흡수_바람)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.흡수_바람));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.흡수_바람));
		}
		if (statups.containsKey(SecondaryStat.흡수_해)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.흡수_해));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.흡수_해));
		}
		if (statups.containsKey(SecondaryStat.Unk_725)) { // 1.2.391 ok.
			mplew.writeShort(0);
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.ScaringSword)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.ScaringSword));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.ScaringSword));
		}
		if (statups.containsKey(SecondaryStat.FlashMirage)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.FlashMirage));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.FlashMirage));
		}
		if (statups.containsKey(SecondaryStat.HolyBlood)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.HolyBlood));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.HolyBlood));
		}
		if (statups.containsKey(SecondaryStat.Infinity)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.Infinity));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Infinity));
		}
		if (statups.containsKey(SecondaryStat.TeleportMastery)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.TeleportMastery));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.TeleportMastery));
		}
		if (statups.containsKey(SecondaryStat.ChillingStep)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.ChillingStep));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.ChillingStep));
		}
		if (statups.containsKey(SecondaryStat.BlessingArmor)) { // 1.2.391 ok.
			mplew.writeInt(chr.getBuffSource(SecondaryStat.BlessingArmor));
		}
		if (statups.containsKey(SecondaryStat.UnkBuffStat707)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.UnkBuffStat707));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.UnkBuffStat707));
		}
		if (statups.containsKey(SecondaryStat.Transcendent)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.Transcendent));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Transcendent));
		}
		if (statups.containsKey(SecondaryStat.Origin_Artificial_Evolution)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.Origin_Artificial_Evolution));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Origin_Artificial_Evolution));
		}
		if (statups.containsKey(SecondaryStat.DemonFrenzy)) { // 1.2.391 ok.
			mplew.writeInt(chr.getBuffedSkill(SecondaryStat.DemonFrenzy));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.DemonFrenzy));
		}
		if (statups.containsKey(SecondaryStat.CrystalGate)) { // 1.2.391 ok.
			mplew.writeInt(chr.getBuffedSkill(SecondaryStat.CrystalGate));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.CrystalGate));
		}
		if (statups.containsKey(SecondaryStat.KinesisPsychicPoint)) { // 1.2.391 ok.
			mplew.writeInt(chr.getBuffedSkill(SecondaryStat.KinesisPsychicPoint));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.KinesisPsychicPoint));
		}
		if (statups.containsKey(SecondaryStat.Unk_754)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.Unk_754));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Unk_754));
		}
		if (statups.containsKey(SecondaryStat.Unk_755)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.Unk_755));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Unk_755));
		}
		if (statups.containsKey(SecondaryStat.Unk_756)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.Unk_756));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Unk_756));
		}
		if (statups.containsKey(SecondaryStat.Unk_732)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.Unk_732));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Unk_732));
		}
		if (statups.containsKey(SecondaryStat.Unk_757)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.Unk_757));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Unk_757));
		}
		if (statups.containsKey(SecondaryStat.Unk_758)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.Unk_758));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Unk_758));
		}
		if (statups.containsKey(SecondaryStat.Unk_752)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.Unk_752));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Unk_752));
		}
		if (statups.containsKey(SecondaryStat.Unk_759)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.Unk_759));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Unk_759));
		}
		if (statups.containsKey(SecondaryStat.Unk_765)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.Unk_765));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Unk_765));
		}
		if (statups.containsKey(SecondaryStat.Origin_Adrenaline_Surge)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.Origin_Adrenaline_Surge));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Origin_Adrenaline_Surge));
		}
		if (statups.containsKey(SecondaryStat.Unk_776)) { // 1.2.391 ok.
			mplew.writeInt(chr.getBuffedSkill(SecondaryStat.Unk_776));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Unk_776));
		}

		mplew.write(statups.containsKey(SecondaryStat.DefenseAtt)); // 1.2.391 ok.
		mplew.write(statups.containsKey(SecondaryStat.DefenseState)); // 1.2.391 ok.
		mplew.write(statups.containsKey(SecondaryStat.PVPDamage)); // 1.2.391 ok.
		mplew.writeInt((chr.energyCharge && chr.getKeyValue(1544, "5100015") <= 0L) ? 5120018 : 0); // 1.2.391 ok.

		if (statups.containsKey(SecondaryStat.CurseOfCreation)) { // 1.2.391 ok.
			mplew.writeInt(chr.getDisease(SecondaryStat.CurseOfCreation).getValue());
		}
		if (statups.containsKey(SecondaryStat.CurseOfDestruction)) { // 1.2.391 ok.
			mplew.writeInt(chr.getDisease(SecondaryStat.CurseOfDestruction).getValue());
		}
		if (statups.containsKey(SecondaryStat.PoseType)) { // 1.2.391 ok.
			mplew.write(0);
			mplew.write(1);
		}
		if (statups.containsKey(SecondaryStat.PVPUnk_441)) { // 1.2.391 ok.
			mplew.writeInt(10);
			mplew.writeInt(chr.getBuffSource(SecondaryStat.PVPUnk_441));
			mplew.writeInt(chr.getId());
		}
		if (statups.containsKey(SecondaryStat.PVPUnk_442)) { // 1.2.391 ok.
			mplew.writeInt(0);
			mplew.writeInt(0);
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.PVPUnk_444)) { // 1.2.391 ok.
			mplew.writeInt(0);
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.MichaelSoulLink)) { // 1.2.391 ok.
			boolean isParty = (chr.getParty() != null);

			int size = 0;
			if (isParty) {
				for (MaplePartyCharacter chr1 : chr.getParty().getMembers()) {
					MapleCharacter chr2 = chr.getMap().getCharacter(chr1.getId());
					if (chr1.isOnline() && chr2.getBuffedValue(51111008)) {
						size++;
					}
				}
			}

			mplew.writeInt(isParty ? chr.getParty().getMembers().size() : 1);
			mplew.write((size >= 2) ? 0 : 1);
			mplew.writeInt(isParty ? chr.getParty().getId() : 0);
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.AdrenalinBoost)) { // 1.2.391 ok.
			mplew.write(chr.getBuffedSkill(SecondaryStat.AdrenalinBoost));
		}
		if (statups.containsKey(SecondaryStat.Stigma)) { // 1.2.391 ok.
			mplew.writeInt(7);
		}
		if (statups.containsKey(SecondaryStat.HolyUnity)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.HolyUnity));
		}
		if (statups.containsKey(SecondaryStat.DemonFrenzy)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.DemonFrenzy));
		}
		if (statups.containsKey(SecondaryStat.ShadowSpear)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedSkill(SecondaryStat.ShadowSpear));
		}
		if (statups.containsKey(SecondaryStat.RhoAias)) { // 1.2.391 ok.
			mplew.writeInt(
					(chr.getBuffedOwner(400011011) == chr.getId()) ? chr.getId() : chr.getBuffedOwner(400011011));

			SecondaryStatEffect effect = chr.getBuffedEffect(SecondaryStat.RhoAias);

			if (effect != null) {
				if (chr.getRhoAias() <= effect.getY()) {
					mplew.writeInt(3);
				} else if (chr.getRhoAias() <= effect.getY() + effect.getW()) {
					mplew.writeInt(2);
				} else {
					mplew.writeInt(1);
				}

				mplew.writeInt(chr.getRhoAias());

				if (chr.getRhoAias() <= effect.getY()) {
					mplew.writeInt(3);
				} else if (chr.getRhoAias() <= effect.getY() + effect.getW()) {
					mplew.writeInt(2);
				} else {
					mplew.writeInt(1);
				}
			} else {
				mplew.writeInt(0);
				mplew.writeInt(0);
				mplew.writeInt(0);
			}
		}
		if (statups.containsKey(SecondaryStat.VampDeath)) { // 1.2.391 ok.
			mplew.writeInt(chr.getBuffSource(SecondaryStat.VampDeath));
		}
		if (statups.containsKey(SecondaryStat.GloryWing)) { // 1.2.391 ok.
			mplew.writeInt(chr.canUseMortalWingBeat ? 1 : 0);
			mplew.writeInt(1);
		}
		if (statups.containsKey(SecondaryStat.BlessMark)) { // 1.2.391 ok.
			mplew.writeInt(chr.getBuffedSkill(SecondaryStat.BlessMark));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.BlessMark));
		}
		if (statups.containsKey(SecondaryStat.BattlePvP_Rude_Stack)) { // 1.2.391 ok.
			mplew.writeInt(80002338);
			mplew.writeInt(80002338);
			mplew.writeInt(80002338);
		}
		if (statups.containsKey(SecondaryStat.UnkBuffStat35)) { // 1.2.391 ok.
			mplew.writeInt(chr.getBuffSource(SecondaryStat.UnkBuffStat35));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.UnkBuffStat35));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.UnkBuffStat35));
		}
		if (statups.containsKey(SecondaryStat.BattlePvP_Wonky_ChargeA)) { // 1.2.391 ok.
			mplew.writeInt(chr.getBuffSource(SecondaryStat.BattlePvP_Wonky_ChargeA));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.BattlePvP_Wonky_ChargeA));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.BattlePvP_Wonky_ChargeA));
		}
		if (statups.containsKey(SecondaryStat.BattlePvP_Wonky_Awesome)) { // 1.2.391 ok.
			mplew.writeInt(chr.getBuffSource(SecondaryStat.BattlePvP_Wonky_Awesome));
		}

		if (statups.containsKey(SecondaryStat.StopForceAtominfo)) { // 1.2.391 ok.
			mplew.writeInt((chr.getBuffSource(SecondaryStat.StopForceAtominfo) == 61121217) ? 4
					: ((chr.getBuffSource(SecondaryStat.StopForceAtominfo) == 61110211) ? 3
							: ((chr.getBuffSource(SecondaryStat.StopForceAtominfo) != 61101002
									&& chr.getBuffSource(SecondaryStat.StopForceAtominfo) != 61110211
									&& chr.getBuffSource(SecondaryStat.StopForceAtominfo) != 61141006) ? 2 : 1)));
			mplew.writeInt((chr.getBuffSource(SecondaryStat.StopForceAtominfo) != 61101002
					&& chr.getBuffSource(SecondaryStat.StopForceAtominfo) != 61110211) ? 5 : 3);
			mplew.writeInt((chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11) == null) ? 0
					: chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11).getItemId());
			mplew.writeInt((chr.getBuffSource(SecondaryStat.StopForceAtominfo) != 61101002
					&& chr.getBuffSource(SecondaryStat.StopForceAtominfo) != 61110211) ? 5 : 3);
			mplew.writeZeroBytes((chr.getBuffSource(SecondaryStat.StopForceAtominfo) != 61101002
					&& chr.getBuffSource(SecondaryStat.StopForceAtominfo) != 61110211) ? 20 : 12);
		} else {
			mplew.writeInt(0);
			mplew.writeInt(0);
			mplew.writeInt(0);
			mplew.writeInt(0);
		}

		for (Map.Entry<SecondaryStat, Pair<Integer, Integer>> stat : statups.entrySet()) { // 1.2.391 ok.
			if (!((SecondaryStat) stat.getKey()).canStack() && ((SecondaryStat) stat.getKey()).isSpecialBuff()) {
				mplew.writeInt(((Integer) ((Pair) stat.getValue()).left).intValue());
				mplew.writeInt(chr.getBuffSource(stat.getKey()));
				if (stat.getKey() == SecondaryStat.PartyBooster) {
					mplew.write(1);
					mplew.writeInt(chr.getBuffedEffect(stat.getKey()).getStarttime());
				} else if (stat.getKey() == SecondaryStat.UnkBuffStat707) {
					mplew.write(chr.energyCharge);
				}
				mplew.write(0);
				mplew.writeInt(0);
				if (stat.getKey() == SecondaryStat.GuidedBullet) {
					mplew.writeInt(chr.guidedBullet);
					mplew.writeInt(0);
					continue;
				}
				if (stat.getKey() == SecondaryStat.RideVehicleExpire || stat.getKey() == SecondaryStat.PartyBooster
						|| stat.getKey() == SecondaryStat.DashJump || stat.getKey() == SecondaryStat.DashSpeed) {
					mplew.writeShort(((Integer) ((Pair) stat.getValue()).right).intValue() / 1000);
					continue;
				}
				if (stat.getKey() == SecondaryStat.Grave) {
					mplew.writeInt(chr.graveObjectId);
					mplew.writeInt(0);
				}
			}
		}

		List<Pair<SecondaryStat, Pair<Integer, Integer>>> newstatups = sortBuffStats(statups);
		CWvsContext.BuffPacket.encodeIndieTempStat(mplew, newstatups, chr); // 1.2.391 ok.

		if (statups.containsKey(SecondaryStat.KawoongDebuff)) { // 1.2.391 ok.
			mplew.writeInt(chr.getBuffSource(SecondaryStat.KawoongDebuff));
		}
		if (statups.containsKey(SecondaryStat.KeyDownMoving)) { // 1.2.391 ok.
			mplew.writeInt(chr.getBuffedSkill(SecondaryStat.KeyDownMoving));
		}
		if (statups.containsKey(SecondaryStat.WillPoison)) { // 1.2.391 ok.
			mplew.writeInt(100);
		}
		if (statups.containsKey(SecondaryStat.ComboCounter)) { // 1.2.391 ok.
			mplew.writeInt((chr.getKeyValue(1548, "視覺開關") == 1L) ? 1 : 0);
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.UnkBuffStat50)) { // 1.2.391 ok.
			mplew.writeInt(chr.getBuffSource(SecondaryStat.UnkBuffStat50));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.UnkBuffStat50));
			mplew.writeInt(chr.getBuffSource(SecondaryStat.UnkBuffStat50));
		}
		if (statups.containsKey(SecondaryStat.Graffiti)) { // 1.2.391 ok.
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Graffiti));
		}

		mplew.write(0); // 1.2.391 ok.

		if (statups.containsKey(SecondaryStat.Novility)) { // 1.2.391 ok.
			mplew.writeInt(chr.getId());
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.YellowAura)) { // 1.2.391 ok.
			mplew.writeInt(chr.getId());
			mplew.writeInt(1);
		}
		if (statups.containsKey(SecondaryStat.DrainAura)) { // 1.2.391 ok.
			mplew.writeInt(chr.getId());
			mplew.writeInt(1);
		}
		if (statups.containsKey(SecondaryStat.BlueAura)) { // 1.2.391 ok.
			mplew.writeInt(chr.getId());
			mplew.writeInt(1);
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.DarkAura)) { // 1.2.391 ok.
			mplew.writeInt(chr.getId());
			mplew.writeInt(1);
		}
		if (statups.containsKey(SecondaryStat.DebuffAura)) { // 1.2.391 ok.
			mplew.writeInt(chr.getId());
			mplew.writeInt(1);
		}
		if (statups.containsKey(SecondaryStat.UnionAura)) { // 1.2.391 ok.
			mplew.writeInt(chr.getId());
			mplew.writeInt(1);
		}
		if (statups.containsKey(SecondaryStat.IceAura)) { // 1.2.391 ok.
			mplew.writeInt(chr.getId());
			mplew.writeInt(1);
		}
		if (statups.containsKey(SecondaryStat.KnightsAura)) { // 1.2.391 ok.
			mplew.writeInt(chr.getId());
			mplew.writeInt(1);
		}
		if (statups.containsKey(SecondaryStat.ZeroAuraStr)) { // 1.2.391 ok.
			mplew.writeInt(chr.getId());
			mplew.writeInt(1);
		}
		if (statups.containsKey(SecondaryStat.IncarnationAura)) { // 1.2.391 ok.
			mplew.writeInt(chr.getId());
			mplew.writeInt(1);
		}
		if (statups.containsKey(SecondaryStat.BlizzardTempest)) { // 1.2.391 ok.
			mplew.writeInt(chr.getId());
		}
		if (statups.containsKey(SecondaryStat.PhotonRay)) { // 1.2.391 ok.
			mplew.writeInt(chr.photonRay);
		}
		if (statups.containsKey(SecondaryStat.SilhouetteMirage)) { // 1.2.391 ok.
			mplew.writeInt(chr.getSkillCustomValue0(400031053));
		}
		if (statups.containsKey(SecondaryStat.Infinity)) { // 1.2.391 ok.
			mplew.writeInt(0);
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.YetiSpicy)) { // 1.2.391 ok.
			mplew.writeInt(chr.getBuffSource(SecondaryStat.YetiSpicy));
		}
		if (statups.containsKey(SecondaryStat.Unk_755)) { // 1.2.391 ok.
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Unk_755));
		}
		if (statups.containsKey(SecondaryStat.Unk_759)) { // 1.2.391 ok.
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Unk_759));
			mplew.writeInt(0);
			mplew.writeInt(0);
			mplew.writeInt(0);
			mplew.writeInt(0);
			mplew.writeInt(0);
			mplew.writeInt(0);
			mplew.writeInt(0);
			mplew.writeInt(0);
			mplew.writeInt(0);
			mplew.writeInt(0);
			mplew.writeInt(0);
			mplew.writeInt(0);
			mplew.writeInt(0);
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.Unk_604)) { // 1.2.391 ok.
			mplew.writeInt(chr.getBuffSource(SecondaryStat.Unk_604));
		}
	}

	// [SEX] 채팅패킷 전체수정
	public static void ChatPacket(MaplePacketLittleEndianWriter mplew, String name, String chat, int id) {
		mplew.writeMapleAsciiString(name);
		mplew.writeMapleAsciiString(chat);
		mplew.writeInt(0);
		mplew.writeInt(0);
		mplew.write(-1);
		mplew.writeInt(0);
	}

	// 수정시 ChatPacket(mplew, name, chat, 0); 한줄만 추가
	public static void ChatPacket(MaplePacketLittleEndianWriter mplew, String name, String chat) {
		mplew.writeMapleAsciiString(name);
		mplew.writeMapleAsciiString(chat);
		mplew.writeInt(0);
		mplew.writeInt(0);
		mplew.write(-1);
		mplew.writeInt(0);
	}

	public static void chairPacket(final MaplePacketLittleEndianWriter mplew, final MapleCharacter chr,
			final int itemId) {
		final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
		if (ii.getItemInformation(itemId) == null) {
			System.out.println(itemId + " null chair Packet.");
			return;
		}
		final String chairType;
		final String type = chairType = ii.getItemInformation(itemId).chairType; // 타입 다시 나누기

		switch (itemId) {
		case 3014028: // 레벨 의자
			mplew.writeMapleAsciiString(chr.getChairText());
			mplew.writeMapleAsciiString(chr.getName());
			mplew.writeMapleAsciiString(chr.getChairText());
			mplew.writeInt(0);
			mplew.writeInt(0);
			mplew.writeInt(0);
			mplew.writeInt(0);
			mplew.write(0);
			mplew.writeMapleAsciiString(chr.getName());
			mplew.writeInt(808956);
			mplew.writeInt(0);
			break;
		}
		switch (chairType) {
		case "timeChair": {
			mplew.writeInt(0);
			break;
		}
		case "popChair": {
			mplew.writeInt(0);
			break;
		}
		case "starForceChair": {
			break;
		}
		case "trickOrTreatChair": {
			mplew.writeInt(0);
			mplew.writeInt(0);
			break;
		}
		case "celebChair": {
			mplew.writeInt(0);
			break;
		}
		case "randomChair": {
			break;
		}
		case "identityChair": {
			mplew.write(true);
			mplew.writeLong(getTime(System.currentTimeMillis()));
			break;
		}
		case "mirrorChair": {
			break;
		}
		case "popButtonChair": {
			mplew.writeInt(chr.getFame());
			break;
		}
		case "rollingHouseChair": {
			mplew.writeInt(itemId);
			mplew.writeInt(0);
			break;
		}
		case "androidChair": {
			break;
		}
		case "mannequinChair": {
			mplew.writeInt(chr.getHairRoom().size());
			for (final MapleMannequin hr : chr.getHairRoom()) {
				mplew.write(0);
				mplew.write(0);
				mplew.writeInt(hr.getValue());
				mplew.write(hr.getBaseColor());
				mplew.write(hr.getAddColor());
				mplew.write(hr.getBaseProb());
			}
			break;
		}
		case "rotatedSleepingBagChair": {
			break;
		}
		case "eventPointChair": {
			break;
		}
		case "hashTagChair": {
			for (int i = 0; i < 18; ++i) {
				mplew.writeMapleAsciiString("");
			}
			break;
		}
		case "petChair": {
			for (int i = 0; i < 3; ++i) {
				final MaplePet pet = chr.getPet(i);
				if (pet != null) {
					mplew.writeInt(pet.getPetItemId());
					mplew.writeInt(pet.getPos().x);
					mplew.writeInt(pet.getPos().y);
				} else {
					mplew.writeInt(0);
					mplew.writeInt(0);
					mplew.writeInt(0);
				}
			}
			break;
		}
		case "charLvChair": {
			mplew.writeMapleAsciiString(chr.getChairText());
			mplew.writeMapleAsciiString(chr.getName());
			mplew.writeMapleAsciiString(chr.getChairText());
			mplew.writeInt(0);
			mplew.writeInt(0);
			mplew.writeInt(0);
			mplew.writeInt(0);
			mplew.write(0);
			mplew.writeMapleAsciiString(chr.getName());
			mplew.writeInt(808956);
			mplew.writeInt(0);
			break;
		}
		case "scoreChair": {
			mplew.writeInt(0);
			break;
		}
		case "arcaneForceChair": {
			break;
		}
		case "scaleAvatarChair": {
			mplew.write(itemId == 3018465);
			if (itemId == 3018465) {
				mplew.writeInt(chr.getKeyValue(100466, "Floor"));
				break;
			}
			break;
		}
		case "wasteChair": {
			mplew.writeLong(chr.getMesoChairCount());
			break;
		}
		case "2019rollingHouseChair": {
			mplew.writeInt(0);
			mplew.writeInt(0);
			mplew.writeInt(0);
			break;
		}
		case "unionRankChair": {
			mplew.write(false);
			break;
		}
		case "yetiChair": {
			mplew.writeInt(0);
			mplew.writeInt(0);
			mplew.writeInt(0);
			break;
		}
		case "worldLvChair":
		case "atkPwrChair":
		case "worldLvChairNonshowLevel": {
			mplew.write(false);
			break;
		}
		default: {
			if (GameConstants.isTextChair(itemId)) {
				mplew.writeMapleAsciiString(chr.getChairText());
				ChatPacket(mplew, chr.getName(), "[의자]" + chr.getChairText(), chr.getId());
				mplew.writeInt(0); // 1.2.366 ++
				break;
			}
			if (GameConstants.isTowerChair(itemId)) {
				final String towerchair = chr.getInfoQuest(7266);
				if (towerchair.equals("")) {
					mplew.writeInt(0);
				} else {
					final String[] temp = towerchair.split(";");
					mplew.writeInt(temp.length);
					for (int a = 0; a < temp.length; ++a) {
						final int chairid = Integer.parseInt(temp[a].substring(2));
						mplew.writeInt(chairid);
					}
				}
				break;
			}
			if (itemId == 3015440 || itemId == 3015650 || itemId == 3015651 || itemId == 3015897
					|| (itemId == 3018430 | itemId == 3018450)) {
				mplew.writeLong(chr.getMesoChairCount());
				break;
			}
			break;
		}
		}
	}

	public static void encodeForLocal(MaplePacketLittleEndianWriter mplew, SecondaryStatEffect effect,
			Map<SecondaryStat, Pair<Integer, Integer>> statups, MapleCharacter chr,
			List<Pair<SecondaryStat, Pair<Integer, Integer>>> newstatups) {
		if (statups.containsKey(SecondaryStat.SoulMP)) { // 1.2.391 ok.
			mplew.writeInt(1000);
			mplew.writeInt(effect.getSourceId());
		}
		if (statups.containsKey(SecondaryStat.FullSoulMP)) { // 1.2.391 ok.
			mplew.writeInt(effect.getSourceId());
		}

		int a = 0;

		mplew.writeShort(a); // 1.2.391 ok.
		for (int i = 0; i < a; i++) { // 1.2.391 ok.
			mplew.writeInt(0);
			mplew.write(0);
		}

		mplew.write(0); // 1.2.391 ok.
		mplew.write(0); // 1.2.391 ok.
		mplew.write(0); // 1.2.391 ok.
		mplew.writeInt(statups.containsKey(SecondaryStat.Etherealform) ? -29 : 0); // 1.2.391 ok.

		if (statups.containsKey(SecondaryStat.DiceRoll)) { // 1.2.391 ok.
			giveDice(mplew, effect, chr);
		}
		if (statups.containsKey(SecondaryStat.CurseOfCreation)) { // 1.2.391 ok.
			mplew.writeInt(10);
		}
		if (statups.containsKey(SecondaryStat.CurseOfDestruction)) { // 1.2.391 ok.
			mplew.writeInt(15);
		}
		if (statups.containsKey(SecondaryStat.UnkBuffStat28)) { // 1.2.391 ok.
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.KeyDownMoving)) { // 1.2.391 ok.
			if (effect.getSourceId() == 131001020) {
				mplew.writeInt((int) chr.getSkillCustomValue0(effect.getSourceId()));
			} else {
				mplew.writeInt(1);
			}
		}
		if (statups.containsKey(SecondaryStat.PinkbeanRollingGrade)) { // 1.2.391 ok.
			mplew.write(1);
		}
		if (statups.containsKey(SecondaryStat.Judgement)) { // 1.2.391 ok.
			if (((Integer) ((Pair) statups.get(SecondaryStat.Judgement)).left).intValue() == 1) {
				mplew.writeInt(effect.getV());
			} else if (((Integer) ((Pair) statups.get(SecondaryStat.Judgement)).left).intValue() == 2) {
				mplew.writeInt(effect.getW());
			} else if (((Integer) ((Pair) statups.get(SecondaryStat.Judgement)).left).intValue() == 3) {
				mplew.writeInt((effect.getX() << 8) + effect.getY());
			} else {
				mplew.writeInt(0);
			}
		}
		if (statups.containsKey(SecondaryStat.Infinity)) { // 1.2.391 ok.
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.StackBuff)) { // 1.2.391 ok.
			mplew.write(chr.stackbuff);
		}
		if (statups.containsKey(SecondaryStat.Trinity)) { // 1.2.391 ok.
			mplew.write((int) chr.getSkillCustomValue0(chr.getSkillLevel(65141000) > 0 ? 65141000 : 65121101));
		}
		if (statups.containsKey(SecondaryStat.ElementalCharge)) { // 1.2.391 ok.
			mplew.write(chr.getElementalCharge());
			mplew.writeShort(effect.getY() * chr.getElementalCharge());
			mplew.write(effect.getU() * chr.getElementalCharge());
			mplew.write(effect.getW() * chr.getElementalCharge());
		}
		if (statups.containsKey(SecondaryStat.LifeTidal)) { // 1.2.391 ok.
			switch (((Integer) ((Pair) statups.get(SecondaryStat.LifeTidal)).left).intValue()) {
			case 1:
				mplew.writeInt(effect.getX());
				break;
			case 2:
				mplew.writeInt(effect.getProp());
				break;
			case 3:
				mplew.writeInt(chr.getStat().getCurrentMaxHp());
				break;
			default:
				mplew.writeInt(0);
				break;
			}
		}
		if (statups.containsKey(SecondaryStat.AntiMagicShell)) { // 1.2.391 ok.
			mplew.write(chr.getAntiMagicShell());
			mplew.writeInt(effect.getDuration());
		}
		if (statups.containsKey(SecondaryStat.Larkness)) { // 1.2.391 ok.
			mplew.writeInt(effect.getSourceId());
			mplew.writeInt(effect.getDuration());
			mplew.writeInt((effect.getSourceId() == 20040216 || effect.getSourceId() == 20040217) ? 0
					: ((effect.getSourceId() == 20040219) ? 20040220 : 20040219));
			mplew.writeInt(effect.getDuration());
			mplew.writeInt((effect.getSourceId() == 20040217) ? 10000 : -1);
			mplew.writeInt((effect.getSourceId() == 20040216) ? -1 : 1);
			mplew.writeInt((chr.getSkillLevel(400021005) > 0 && !chr.getUseTruthDoor()
					&& (effect.getSourceId() == 20040219 || effect.getSourceId() == 20040220)) ? 1 : 0);
		}
		if (statups.containsKey(SecondaryStat.IgnoreTargetDEF)) { // 1.2.391 ok.
			mplew.writeInt(chr.lightning);
		}
		if (statups.containsKey(SecondaryStat.UNK_253)) { // 1.2.391 ok.
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.StopForceAtominfo)) { // 1.2.391 ok.
			mplew.writeInt((effect.getSourceId() == 61121217) ? 4
					: ((effect.getSourceId() == 61110211) ? 3
							: ((effect.getSourceId() != 61101002 && effect.getSourceId() != 61110211
									&& effect.getSourceId() != 61141006) ? 2 : 1)));
			mplew.writeInt((effect.getSourceId() != 61101002 && effect.getSourceId() != 61110211) ? 5 : 3);
			mplew.writeInt((chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11) == null) ? 0
					: chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11).getItemId());

			int size = (effect.getSourceId() != 61101002 && effect.getSourceId() != 61110211) ? 5 : 3;

			mplew.writeInt(size);

			if (size > 0) {
				for (int i = 0; i < size; i++) {
					mplew.writeInt(0);
				}
			}
		}
		if (statups.containsKey(SecondaryStat.SmashStack)) { // 1.2.391 ok.
			mplew.writeInt((chr.getKaiserCombo() >= 300) ? 2 : ((chr.getKaiserCombo() >= 100) ? 1 : 0));
			mplew.writeInt(0);
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.MobZoneState)) { // 1.2.391 ok.
			mplew.writeInt(((Integer) ((Pair) statups.get(SecondaryStat.MobZoneState)).right).intValue());
			mplew.writeInt(-1); // 1.2.391 -- ? (확인 필요)
		}
		if (statups.containsKey(SecondaryStat.IncreaseJabelinDam)) { // 1.2.391 ok.
			int size = 2;

			mplew.writeInt(size);
			mplew.writeInt(152120001);
			mplew.writeInt(400021000);
		}
		if (statups.containsKey(SecondaryStat.Slow)) { // 1.2.391 ok.
			mplew.write(0);
		}
		if (statups.containsKey(SecondaryStat.IgnoreMobPdpR)) { // 1.2.391 ok.
			mplew.write(0);
		}
		if (statups.containsKey(SecondaryStat.BdR)) { // 1.2.391 ok.
			mplew.write(0);
		}
		if (statups.containsKey(SecondaryStat.DropRIncrease)) { // 1.2.391 ok.
			mplew.writeInt(0);
			mplew.write(0);
		}
		if (statups.containsKey(SecondaryStat.PoseType)) { // 1.2.391 ok.
			mplew.write(chr.getBuffedValue(11101031));
			mplew.write(chr.getBuffedValue(11101031));
		}
		if (statups.containsKey(SecondaryStat.Beholder)) { // 1.2.391 ok.
			mplew.writeInt(chr.getBeholderSkill1());
		}
		if (statups.containsKey(SecondaryStat.CrossOverChain)) { // 1.2.391 ok.
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.ImmuneBarrier)) { // 1.2.391 ok.
			mplew.writeInt(statups.get(SecondaryStat.ImmuneBarrier).left);
		}
		if (statups.containsKey(SecondaryStat.Stance)) { // 1.2.391 ok.
			mplew.writeInt(chr.발할라검격);
		}
		if (statups.containsKey(SecondaryStat.SharpEyes)) { // 1.2.391 ok.
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.AdvancedBless)) { // 1.2.391 ok.
			if (chr.getSkillLevel(2320050) > 0) {
				mplew.writeInt(SkillFactory.getSkill(2320050).getEffect(1).getBdR());
			} else {
				mplew.writeInt(0);
			}

			mplew.writeInt(30);
		}
		if (statups.containsKey(SecondaryStat.Infiltrate)) { // 1.2.391 ok.
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.SoulExalt)) { // 1.2.391 ok.
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.Bless)) { // 1.2.391 ok.
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.DotHealHPPerSecond)) { // 1.2.391 ok.
			mplew.writeInt(180);
		}
		if (statups.containsKey(SecondaryStat.DotHealMPPerSecond)) { // 1.2.391 ok.
			mplew.writeInt(180);
		}
		if (statups.containsKey(SecondaryStat.SpiritGuard)) { // 1.2.391 ok.
			mplew.writeInt(chr.getSpiritGuard());
		}
		if (statups.containsKey(SecondaryStat.DemonDamageAbsorbShield)) { // 1.2.391 ok.
			mplew.writeInt(chr.getSkillCustomValue0(400001016));
		}
		if (statups.containsKey(SecondaryStat.KnockBack)) { // 1.2.391 ok.
			mplew.writeInt(0);
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.ShieldAttack)) { // 1.2.391 ok.
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.SSFShootingAttack)) { // 1.2.391 ok.
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.PVPUnk_441)) { // 1.2.391 ok.
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.PVPUnk_442)) { // 1.2.391 ok.
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.PinkbeanAttackBuff)) { // 1.2.391 ok.
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.RoyalGuardState)) { // 1.2.391 ok.
			mplew.writeInt(((Integer) ((Pair) statups.get(SecondaryStat.IndiePad)).left).intValue());
			mplew.writeInt(((Integer) ((Pair) statups.get(SecondaryStat.RoyalGuardState)).left).intValue());
		}
		if (statups.containsKey(SecondaryStat.MichaelSoulLink)) { // 1.2.391 ok.
			boolean isParty = (chr.getParty() != null);

			int size = 0;

			if (isParty) {
				for (MaplePartyCharacter chr1 : chr.getParty().getMembers()) {
					MapleCharacter chr2 = chr.getMap().getCharacter(chr1.getId());

					if ((chr1.isOnline() && chr2.getBuffedValue(51111008))
							|| ((chr.getTruePosition()).x + (effect.getLt()).x < (chr2.getTruePosition()).x
									&& (chr.getTruePosition()).x - (effect.getLt()).x > (chr2.getTruePosition()).x
									&& (chr.getTruePosition()).y + (effect.getLt()).y < (chr2.getTruePosition()).y
									&& (chr.getTruePosition()).y - (effect.getLt()).y > (chr2.getTruePosition()).y)) {
						size++;
					}
				}
			}

			mplew.writeInt(isParty ? chr.getParty().getMembers().size() : 1);
			mplew.write((size >= 2) ? 0 : 1);
			mplew.writeInt(isParty ? chr.getParty().getId() : 0);
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.AdrenalinBoost)) { // 1.2.391 ok.
			mplew.write((int) chr.getSkillCustomValue0(21110016));
		}
		if (statups.containsKey(SecondaryStat.RWCylinder)) { // 1.2.391 ok.
			mplew.write(chr.getBullet());
			mplew.writeShort(chr.getCylinderGauge());
			mplew.write((int) chr.getSkillCustomValue0(400011091));
		}
		if (statups.containsKey(SecondaryStat.BodyOfSteal)) { // 1.2.391 ok.
			mplew.writeInt(chr.bodyOfSteal);
		}
		if (statups.containsKey(SecondaryStat.RwMagnumBlow)) { // 1.2.391 ok.
			mplew.writeShort(0);
			mplew.write(0);
		}
		if (statups.containsKey(SecondaryStat.BladeStance)) { // 1.2.391 ok.
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.DarkSight)) { // 1.2.391 ok.
			mplew.writeInt(1000000);
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.Stigma)) { // 1.2.391 ok.
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.BonusAttack)) { // 1.2.391 ok.
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.ArmorPiercing)) { // 1.2.391 ok.
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.Ember)) { // 1.2.391 ok.
			mplew.writeInt(chr.getIgnition());
		}
		if (statups.containsKey(SecondaryStat.PickPocket)) { // 1.2.391 ok.
			mplew.writeInt(chr.getPickPocket().size());
		}
		if (statups.containsKey(SecondaryStat.HolyUnity)) { // 1.2.391 ok.
			mplew.writeShort(effect.getLevel());
		}
		if (statups.containsKey(SecondaryStat.DemonFrenzy)) { // 1.2.391 ok.
			mplew.writeShort(chr.getBuffedValue(SecondaryStat.DemonFrenzy));
		}
		if (statups.containsKey(SecondaryStat.ShadowSpear)) { // 1.2.391 ok.
			mplew.writeShort(0);
		}
		if (statups.containsKey(SecondaryStat.RhoAias)) { // 1.2.391 ok.
			mplew.writeInt(chr.getBuffedOwner(400011011) == chr.getId() ? chr.getId() : chr.getBuffedOwner(400011011));

			if (chr.getRhoAias() <= effect.getY()) {
				mplew.writeInt(3);
			} else if (chr.getRhoAias() <= effect.getY() + effect.getW()) {
				mplew.writeInt(2);
			} else {
				mplew.writeInt(1);
			}

			mplew.writeInt(chr.getRhoAias());

			if (chr.getRhoAias() <= effect.getY()) {
				mplew.writeInt(3);
			} else if (chr.getRhoAias() <= effect.getY() + effect.getW()) {
				mplew.writeInt(2);
			} else {
				mplew.writeInt(1);
			}
		}
		if (statups.containsKey(SecondaryStat.VampDeath)) { // 1.2.391 ok.
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.HolyMagicShell)) { // 1.2.391 ok.
			mplew.writeInt((chr.getSkillLevel(2320045) > 0 ? SkillFactory.getSkill(2320045).getEffect(1).getW() : 0)
					+ effect.getW());
		}

		for (Pair<SecondaryStat, Pair<Integer, Integer>> stat : newstatups) { // 1.2.391 ok.
			if (!((SecondaryStat) stat.left).canStack() && ((SecondaryStat) stat.left).isSpecialBuff()) {
				mplew.writeInt(((Integer) ((Pair) stat.right).left).intValue());
				mplew.writeInt(
						(stat.left == SecondaryStat.UnkBuffStat707) ? 0 : chr.getBuffSource((SecondaryStat) stat.left));
				if (stat.left == SecondaryStat.PartyBooster) {
					mplew.write(1);
					mplew.writeInt(effect.getStarttime());
				} else if (stat.left == SecondaryStat.UnkBuffStat707) {
					mplew.write(chr.energyCharge);
				}
				mplew.write(0);
				mplew.writeInt(0);
				if (stat.left == SecondaryStat.GuidedBullet) {
					mplew.writeInt(chr.guidedBullet);
					mplew.writeInt(0);
					continue;
				}
				if (stat.left == SecondaryStat.RideVehicleExpire || stat.left == SecondaryStat.PartyBooster
						|| stat.left == SecondaryStat.DashJump || stat.left == SecondaryStat.DashSpeed) {
					mplew.writeShort(((Integer) ((Pair) stat.right).right).intValue() / 1000);
					continue;
				}
				if (stat.left == SecondaryStat.Grave) {
					mplew.writeInt(chr.graveObjectId);
					mplew.writeInt(0);
				}
			}
		}

		CWvsContext.BuffPacket.encodeIndieTempStat(mplew, newstatups, chr); // 1.2.391 ok.

		if (statups.containsKey(SecondaryStat.UsingScouter)) { // 1.2.391 ok.
			mplew.writeInt(effect.getSourceId());
		}
		if (statups.containsKey(SecondaryStat.KawoongDebuff)) { // 1.2.391 ok.
			mplew.writeInt(effect.getSourceId());
		}
		if (statups.containsKey(SecondaryStat.GloryWing)) { // 1.2.391 ok.
			mplew.writeInt(chr.canUseMortalWingBeat ? 1 : 0);
			mplew.writeInt(1);
		}
		if (statups.containsKey(SecondaryStat.BlessMark)) { // 1.2.391 ok.
			mplew.writeInt(chr.blessMarkSkill);
			switch (chr.blessMarkSkill) {
			case 152000007: {
				mplew.writeInt(3);
				break;
			}
			case 152110009: {
				mplew.writeInt(6);
				break;
			}
			case 152120012: {
				mplew.writeInt(10);
			}
			}
		}
		if (statups.containsKey(SecondaryStat.ShadowerDebuff)) { // 1.2.391 ok.
			mplew.writeInt(chr.shadowerDebuffOid);
		}
		if (statups.containsKey(SecondaryStat.WeaponVariety)) { // 1.2.391 ok.
			int flag = 0;
			if (chr.getWeaponChanges().contains(64001002)) {
				++flag;
			}
			if (chr.getWeaponChanges().contains(64101001)) {
				flag += 2;
			}
			if (chr.getWeaponChanges().contains(64101002)) {
				flag += 4;
			}
			if (chr.getWeaponChanges().contains(64111002)) {
				flag += 8;
			}
			if (chr.getWeaponChanges().contains(64111003)) {
				flag += 16;
			}
			if (chr.getWeaponChanges().contains(64111012)) {
				flag += 32;
			}
			if (chr.getWeaponChanges().contains(64121003) || chr.getWeaponChanges().contains(64121011)
					|| chr.getWeaponChanges().contains(64121016)) {
				flag += 64;
			}
			if (chr.getWeaponChanges().contains(64121021) || chr.getWeaponChanges().contains(64121022)
					|| chr.getWeaponChanges().contains(64121023) || chr.getWeaponChanges().contains(64121024)) {
				flag += 128;
			}
			mplew.writeInt(flag);
		}
		if (statups.containsKey(SecondaryStat.Overload)) { // 1.2.391 ok.
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.SpectorGauge)) { // 1.2.391 ok.
			mplew.writeInt(chr.SpectorGauge);
		}
		if (statups.containsKey(SecondaryStat.PlainBuff)) { // 1.2.391 ok.
			mplew.writeInt(chr.getSkillCustomValue0(155001100) * 2L);
			mplew.writeInt(chr.getSkillCustomValue0(155001101) * 2L);
		}
		if (statups.containsKey(SecondaryStat.ScarletBuff)) { // 1.2.391 ok.
			mplew.writeInt(chr.getSkillCustomValue0(155101100));
			mplew.writeInt(chr.getSkillCustomValue0(155101101));
		}
		if (statups.containsKey(SecondaryStat.GustBuff)) { // 1.2.391 ok.
			mplew.writeInt(chr.getSkillCustomValue0(155111102));
			mplew.writeInt(chr.getSkillCustomValue0(155111103));
		}
		if (statups.containsKey(SecondaryStat.AbyssBuff)) { // 1.2.391 ok.
			mplew.writeInt(chr.getSkillCustomValue0(155121102));
			mplew.writeInt(chr.getSkillCustomValue0(155121103));
		}
		if (statups.containsKey(SecondaryStat.WillPoison)) { // 1.2.391 ok.
			mplew.writeInt(30);
		}
		if (statups.containsKey(SecondaryStat.InfinityFlameCircle)) { // 1.2.391 ok.
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.MarkOfPhantomStack)) { // 1.2.391 ok.
			mplew.writeInt(chr.getMarkofPhantom());
		}
		if (statups.containsKey(SecondaryStat.MarkOfPhantomDebuff)) { // 1.2.391 ok.
			mplew.writeInt(chr.getMarkOfPhantomOid());
		}
		if (statups.containsKey(SecondaryStat.ShadowBatt)) { // 1.2.391 ok.
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.EventSpecialSkill)) { // 1.2.391 ok.
			mplew.writeInt(0);
			mplew.writeInt(0);
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.UnkBuffStat46)) { // 1.2.391 ok.
			mplew.writeInt(0);
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.PapyrusOfLuck)) { // 1.2.391 ok.
			mplew.writeInt(0);
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.PmdReduce)) { // 1.2.391 ok.
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.ForbidEquipChange)) { // 1.2.391 ok.
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.ComboCounter)) { // 1.2.391 ok.
			mplew.writeInt(chr.getKeyValue(1548, "視覺開關") == 1L ? 1 : 0);
			mplew.writeInt(0);
		}

		if (statups.containsKey(SecondaryStat.unk_606)) { // 1.2.391 ok.
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.AncientGuidance)) { // 1.2.391 ok.
			mplew.writeInt(0);
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.BattlePvP_Wonky_ChargeA)) { // 1.2.391 ok.
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.BattlePvP_Wonky_Awesome)) { // 1.2.391 ok.
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.HolySymbol)) { // 1.2.391 ok.
			mplew.writeInt(chr.getId());
			mplew.writeInt(effect.getLevel());
			mplew.writeInt(0);
			mplew.writeInt(0);
			mplew.write(chr.getSkillCustomValue0(2311004) == 0L ? 1 : 0);
			mplew.write(0);
			mplew.writeInt(chr.getSkillCustomValue0(2311004) == 0L ? (int) chr.getSkillCustomValue0(2320048) : 0);
		}
		if (statups.containsKey(SecondaryStat.UnkBuffStat50)) { // 1.2.391 ok.
			mplew.writeInt(0);
			mplew.writeInt(0);
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.HoyoungThirdProperty)) { // 1.2.391 ok.
			mplew.writeInt(chr.useJi ? 1 : 0);
			mplew.writeInt(chr.useIn ? 1 : 0);
		}
		if (statups.containsKey(SecondaryStat.TidalForce)) { // 1.2.391 ok.
			mplew.writeInt(chr.scrollGauge);
		}
		if (statups.containsKey(SecondaryStat.SageWrathOfGods)) { // 1.2.391 ok.
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.EmpiricalKnowledge)) { // 1.2.391 ok.
			mplew.writeInt(chr.empiricalKnowledge == null ? 0 : chr.empiricalKnowledge.getObjectId());
		}
		if (statups.containsKey(SecondaryStat.Graffiti)) { // 1.2.391 ok.
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.Novility)) { // 1.2.391 ok.
			mplew.writeInt(chr.getSkillCustomValue0(151111005));
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.Revenant)) { // 1.2.391 ok.
			mplew.writeInt(chr.getSkillCustomValue0(400011112));
		}
		if (statups.containsKey(SecondaryStat.RevenantDamage)) { // 1.2.391 ok.
			mplew.writeInt(chr.getSkillCustomValue0(400011112) + chr.getStat().getCurrentMaxHp() / 100L * 25L);
			mplew.writeInt(chr.getSkillCustomValue0(400011129));
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.SilhouetteMirage)) { // 1.2.391 ok.
			mplew.writeInt(chr.getSkillCustomValue0(400031053));
		}
		if (statups.containsKey(SecondaryStat.YellowAura)) { // 1.2.391 ok.
			mplew.writeInt(chr.getSkillCustomValue0(32001016));
			mplew.writeInt(chr.getSkillCustomValue0(32001016) != (long) chr.getId() ? 0 : 1);
		}
		if (statups.containsKey(SecondaryStat.DrainAura)) { // 1.2.391 ok.
			mplew.writeInt(chr.getSkillCustomValue0(32101009));
			mplew.writeInt(chr.getSkillCustomValue0(32101009) != (long) chr.getId() ? 0 : 1);
		}
		if (statups.containsKey(SecondaryStat.BlueAura)) { // 1.2.391 ok.
			mplew.writeInt(chr.getSkillCustomValue0(32111012));
			mplew.writeInt(chr.getSkillCustomValue0(32111012) != (long) chr.getId() ? 0 : 1);
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.DarkAura)) { // 1.2.391 ok.
			mplew.writeInt(chr.getSkillCustomValue0(32121017));
			mplew.writeInt(chr.getSkillCustomValue0(32121017) != (long) chr.getId() ? 0 : 1);
		}
		if (statups.containsKey(SecondaryStat.DebuffAura)) { // 1.2.391 ok.
			mplew.writeInt(chr.getId());
			mplew.writeInt(1);
		}
		if (statups.containsKey(SecondaryStat.UnionAura)) { // 1.2.391 ok.
			mplew.writeInt(chr.getSkillCustomValue0(400021006));
			mplew.writeInt(chr.getSkillCustomValue0(400021006) != (long) chr.getId() ? 0 : 1);
		}
		if (statups.containsKey(SecondaryStat.IceAura)) { // 1.2.391 ok.
			mplew.writeInt(chr.getSkillCustomValue0(2221054));
			mplew.writeInt(chr.getSkillCustomValue0(2221054) != (long) chr.getId() ? 0 : 1);
		}
		if (statups.containsKey(SecondaryStat.KnightsAura)) { // 1.2.391 ok.
			mplew.writeInt(chr.getSkillCustomValue0(1211014));
			mplew.writeInt(chr.getSkillCustomValue0(1211014) != (long) chr.getId() ? 0 : 1);
		}
		if (statups.containsKey(SecondaryStat.ZeroAuraStr)) { // 1.2.391 ok.
			mplew.writeInt(chr.getId());
			mplew.writeInt(1);
		}
		if (statups.containsKey(SecondaryStat.IncarnationAura)) { // 1.2.391 ok.
			mplew.writeInt(chr.getSkillCustomValue0(63121044));
			mplew.writeInt(chr.getSkillCustomValue0(63121044) != (long) chr.getId() ? 0 : 1);
		}
		if (statups.containsKey(SecondaryStat.BlizzardTempest)) { // 1.2.391 ok.
			mplew.writeInt(1);
		}

		if (statups.containsKey(SecondaryStat.PhotonRay)) { // 1.2.391 ok.
			mplew.writeInt(chr.photonRay);
		}
		if (statups.containsKey(SecondaryStat.AbyssalLightning)) { // 1.2.391 ok.
			try {
				mplew.writeInt(chr.getMap().SpecialPortalSize(chr.getId()).size());
			} catch (Exception e) {
				mplew.writeInt(0);
			}
		}
		if (statups.containsKey(SecondaryStat.LawOfGravity)) { // 1.2.391 ok.
			mplew.writeInt(chr.lawOfGravity);
		}
		if (statups.containsKey(SecondaryStat.CrystalGate)) { // 1.2.391 ok.
			mplew.writeInt(chr.getSkillCustomValue0(400021099));
		}
		if (statups.containsKey(SecondaryStat.HolyWater)) { // 1.2.391 ok.
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.WeaponVarietyFinale)) { // 1.2.391 ok.
			mplew.writeInt(chr.getSkillCustomValue0(64121020));
		}
		if (statups.containsKey(SecondaryStat.LiberationOrb)) { // 1.2.391 ok.
			mplew.writeInt(chr.getSkillCustomValue0(400021107));
			mplew.writeInt(chr.getSkillCustomValue0(400021108));
		}
		if (statups.containsKey(SecondaryStat.DarknessAura)) { // 1.2.391 ok.
			mplew.writeInt(chr.getSkillCustomValue0(400011047));
		}
		if (statups.containsKey(SecondaryStat.SerpentScrew)) { // 1.2.391 ok.
			mplew.writeInt(0);
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.LiberationOrbActive)) { // 1.2.391 ok.
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.ThanatosDescent)) { // 1.2.391 ok.
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.DragonPang)) { // 1.2.391 ok.
			mplew.writeInt(0);
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.Magnet)) { // 1.2.391 ok.
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.YetiAnger)) { // 1.2.391 ok.
			mplew.writeInt(chr.getSkillCustomValue0(135001005));
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.YetiAngerMode)) { // 1.2.391 ok.
			mplew.writeInt(chr.getSkillCustomValue0(13500));
		}
		if (statups.containsKey(SecondaryStat.YetiSpicy)) { // 1.2.391 ok.
			mplew.writeInt(chr.getSkillCustomValue0(13500));
		}
		if (statups.containsKey(SecondaryStat.LuckOfUnion) && effect.getSourceId() == 135001009) { // 1.2.391 ok.
			mplew.writeInt(1);
			mplew.write(1);
		}
		if (statups.containsKey(SecondaryStat.PinkBeanFighting)) { // 1.2.391 ok.
			mplew.writeInt(chr.getSkillCustomValue0(13500));
			mplew.write(0);
		}
		if (statups.containsKey(SecondaryStat.NewFlying) && effect.getSourceId() == 80003059) { // 1.2.391 ok.
			MapleMist m = chr.getMap().getMist(chr.getBuffedOwner(80003059), 162111000);

			mplew.writeInt(m != null ? m.getObjectId() : 0);
		}
		if (statups.containsKey(SecondaryStat.NewFlying) && effect.getSourceId() == 36141502) { // 1.2.391 ok.
			mplew.writeInt(1);
		}
		if (statups.containsKey(SecondaryStat.Reincarnation)) { // 1.2.391 ok.
			mplew.writeInt(chr.getReinCarnation());
		}
		if (statups.containsKey(SecondaryStat.QuiverFullBurst)) { // 1.2.391 ok.
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.ElementSoul)) { // 1.2.391 ok.
			mplew.writeInt(0);
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.Unk_756)) { // 1.2.391 ok.
			mplew.writeInt(0);
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.Unk_757)) { // 1.2.391 ok.
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.Unk_754)) { // 1.2.391 ok.
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.Unk_755)) { // 1.2.391 ok.
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.Unk_759)) { // 1.2.391 ok.
			mplew.writeInt(0);
			mplew.writeInt(0);
			mplew.writeInt(0);
			mplew.writeInt(0);
			mplew.writeInt(0);
			mplew.writeInt(0);
			mplew.writeInt(0);
			mplew.writeInt(0);
			mplew.writeInt(0);
			mplew.writeInt(0);
			mplew.writeInt(0);
			mplew.writeInt(0);
			mplew.writeInt(0);
			mplew.writeInt(0);
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.Unk_608)) { // 1.2.391 ok.
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.BMageDeath)) { // 1.2.391 ok.
			mplew.writeInt(effect.getSourceId() == 32141000 ? chr.hexaMasterySkillStack : 0);
		}
		if (statups.containsKey(SecondaryStat.Unk_772)) { // 1.2.391 ok.
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.Origin_Nightmare)) { // 1.2.391 ok.
			mplew.writeInt(statups.get(SecondaryStat.Origin_Nightmare).left);
		}
		if (statups.containsKey(SecondaryStat.Unk_269)) { // 1.2.391 ok.
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.Unk_270)) { // 1.2.391 ok.
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.UnkBuffStat707)) { // 1.2.391 ok.
			mplew.writeInt(0);
		}
		if (statups.containsKey(SecondaryStat.Unk_604)) { // 1.2.391 ok.
			mplew.writeInt(0);
		}
	}

	private static void giveDice(MaplePacketLittleEndianWriter mplew, SecondaryStatEffect effect, MapleCharacter chr) {
		int doubledice, dice, thirddice;
		if (chr.getDice() >= 100) {
			thirddice = chr.getDice() / 100;
			doubledice = (chr.getDice() - thirddice * 100) / 10;
			dice = chr.getDice() - chr.getDice() / 10 * 10;
		} else {
			thirddice = 1;
			doubledice = chr.getDice() / 10;
			dice = chr.getDice() - doubledice * 10;
		}

		if (dice == 3 || doubledice == 3 || thirddice == 3) {
			if (dice == 3 && doubledice == 3 && thirddice == 3) {
				mplew.writeInt(15 * 3);
			} else if ((dice == 3 && doubledice == 3) || (dice == 3 && thirddice == 3)
					|| (thirddice == 3 && doubledice == 3)) {
				mplew.writeInt(15 * 2);
			} else {
				mplew.writeInt(15);
			}
		} else {
			mplew.writeInt(0);
		}

		if (dice == 4 || doubledice == 4 || thirddice == 4) {
			if (dice == 4 && doubledice == 4 && thirddice == 4) {
				mplew.writeInt(effect.getCr() + 15);
			} else if ((dice == 4 && doubledice == 4) || (dice == 4 && thirddice == 4)
					|| (thirddice == 4 && doubledice == 4)) {
				mplew.writeInt(effect.getCr() + 10);
			} else {
				mplew.writeInt(effect.getCr());
			}
		} else {
			mplew.writeInt(0);
		}
		mplew.writeInt(0);// 안씀 crr

		mplew.writeInt(0);// 안씀
		mplew.writeInt(0);// 안씀
		mplew.writeInt(0);// 안씀
		mplew.writeInt(0);// 안씀
		if (dice == 2 || doubledice == 2 || thirddice == 2) {
			if (dice == 2 && doubledice == 2 && thirddice == 2) {
				mplew.writeInt(effect.getWDEFRate() + 15);
			} else if ((dice == 2 && doubledice == 2) || (dice == 2 && thirddice == 2)
					|| (thirddice == 2 && doubledice == 2)) {
				mplew.writeInt(effect.getWDEFRate() + 10);
			} else {
				mplew.writeInt(effect.getWDEFRate());
			}
		} else {
			mplew.writeInt(0);
		}
		mplew.writeInt(0); // 안씀
		mplew.writeInt(0);// 안씀
		mplew.writeInt(0);// 안씀
		if (dice == 5 || doubledice == 5 || thirddice == 5) {
			if (dice == 5 && doubledice == 5 && thirddice == 5) {
				mplew.writeInt(effect.getDAMRate() + 15);
			} else if ((dice == 5 && doubledice == 5) || (dice == 5 && thirddice == 5)
					|| (thirddice == 5 && doubledice == 5)) {
				mplew.writeInt(effect.getDAMRate() + 10);
			} else {
				mplew.writeInt(effect.getDAMRate());
			}
		} else {
			mplew.writeInt(0);
		}
		mplew.writeInt(0);// 안씀
		mplew.writeInt(0);// 안씀
		mplew.writeInt(0);// 안씀
		mplew.writeInt(0);// 안씀
		if (dice == 6 || doubledice == 6 || thirddice == 6) {
			if (dice == 6 && doubledice == 6 && thirddice == 6) {
				mplew.writeInt(effect.getEXPRate() + 15);
			} else if ((dice == 6 && doubledice == 6) || (dice == 6 && thirddice == 6)
					|| (thirddice == 6 && doubledice == 6)) {
				mplew.writeInt(effect.getEXPRate() + 10);
			} else {
				mplew.writeInt(effect.getEXPRate());
			}
		} else {
			mplew.writeInt(0);
		}
		if (dice == 7 || doubledice == 7 || thirddice == 7) {
			if (dice == 7 && doubledice == 7 && thirddice == 7) {
				mplew.writeInt(effect.getIgnoreMob() + 15);
			} else if ((dice == 7 && doubledice == 7) || (dice == 7 && thirddice == 7)
					|| (thirddice == 7 && doubledice == 7)) {
				mplew.writeInt(effect.getIgnoreMob() + 10);
			} else {
				mplew.writeInt(effect.getIgnoreMob());
			}
		} else {
			mplew.writeInt(0);
		}
		mplew.writeInt(0);
		mplew.writeInt(0);
		mplew.writeInt(0);
	}
}
