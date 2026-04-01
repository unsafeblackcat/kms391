package handling.world;

import client.*;
import client.inventory.*;
import server.MapleChatEmoticon;
import server.MapleSavedEmoticon;
import server.SecondaryStatEffect;
import server.SkillCustomInfo;
import server.quest.MapleQuest;
import tools.Pair;
import tools.Triple;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;

public class CharacterTransfer {

	public int characterid;

	public int accountid;

	public int fame;

	public int pvpExp;

	public int pvpPoints;

	public int energy;

	public int hair;

	public int secondhair;

	public int face;

	public int secondface;

	public int demonMarking;

	public int mapid;

	public int guildid;

	public int partyid;

	public int messengerid;

	public int nxCredit;

	public int ACash;

	public int MaplePoints;

	public int honourexp;

	public int honourlevel;

	public int itcafetime;

	public int mount_itemid;

	public int mount_exp;

	public int points;

	public int vpoints;

	public int marriageId;

	public int LinkMobCount;

	public int lastCharGuildId;

	public int betaclothes;

	public int returnSc;

	public int familyid;

	public int seniorid;

	public int junior1;

	public int junior2;

	public int currentrep;

	public int totalrep;

	public int battleshipHP;

	public int guildContribution;

	public int lastattendance;

	public int totalWins;

	public int totalLosses;

	public int basecolor;

	public int addcolor;

	public int baseprob;

	public int secondbasecolor;

	public int secondaddcolor;

	public int secondbaseprob;

	public int SpectorGauge;

	public byte channel;

	public byte gender;

	public byte secondgender;

	public byte gmLevel;

	public byte guildrank;

	public byte alliancerank;

	public byte clonez;

	public byte fairyExp;

	public byte cardStack;

	public byte buddysize;

	public byte world;

	public byte initialSpawnPoint;

	public byte skinColor;

	public byte secondSkinColor;

	public byte mount_level;

	public byte mount_Fatigue;

	public byte subcategory;

	public long meso;

	public long maxhp;

	public long maxmp;

	public long hp;

	public long mp;

	public long exp;

	public long lastfametime;

	public long TranferTime;

	public String name;

	public String accountname;

	public String secondPassword;

	public String BlessOfFairy;

	public String BlessOfEmpress;

	public String chalkboard;

	public String tempIP;

	public String auth;

	public short level;

	public short str;

	public short dex;

	public short int_;

	public short luk;

	public short remainingAp;

	public short hpApUsed;

	public short job;

	public short fatigue;

	public short soulCount;

	public Object inventorys;

	public Object skillmacro;

	public Object storage;

	public Object cs;

	public Object anticheat;

	public Object innerSkills;

	public Object artipacts;

	public Object choicepotential;

	public Object returnscroll;

	public Object memorialcube;

	public int[] savedlocation;

	public int[] wishlist;

	public int[] rocks;

	public int[] remainingSp;

	public int[] regrocks;

	public int[] hyperrocks;

	public MapleImp[] imps;

	public MaplePet[] pets = new MaplePet[3];

	public Map<Byte, Integer> reports = new LinkedHashMap<>();

	public Map<SecondaryStat, List<Pair<Integer, SecondaryStatEffect>>> indietemp = new HashMap<>();

	public List<Pair<Integer, Boolean>> stolenSkills;

	public Map<Integer, Pair<Byte, Integer>> keymap;

	public List<Integer> famedcharacters = null;

	public List<Integer> battledaccs = null;

	public List<Integer> extendedSlots = null;

	public List<Integer> exceptionList = null;

	public List<Item> rebuy = null;

	public List<Item> auctionitems = null;

	public List<Core> cores = null;

	public List<AvatarLook> coodination = null;

	public List<VMatrix> matrixs = null;

	public List<Equip> symbol = null;

	public List<Triple<Skill, SkillEntry, Integer>> linkskills = null;

	public final Map<MapleTrait.MapleTraitType, Integer> traits = new EnumMap<>(MapleTrait.MapleTraitType.class);

	public final Map<CharacterNameAndId, Boolean> buddies = new LinkedHashMap<>();

	public final List<MapleUnion> unions = new ArrayList<>();

	public final Map<Integer, Object> Quest = new LinkedHashMap<>();

	public Map<Integer, String> InfoQuest;

	public ScheduledFuture<?> secondaryStatEffectTimer;

	public final Map<Integer, SkillEntry> Skills = new LinkedHashMap<>();

	public final Map<Integer, Integer> customValue = new HashMap<>();

	public Map<String, String> keyValues = new HashMap<>();
	public Map<String, String> keyValues_boss = new HashMap<>();
	public Timer DFRecoveryTimer;

	public int reborns;
	public int dojorank;
	public boolean dressUp;

	public int apstorage;

	public boolean login;

	public boolean energycharge;

	public List<MapleMannequin> hairRoom;

	public List<MapleMannequin> faceRoom;

	public List<MapleMannequin> skinRoom;

	public final Map<Integer, SkillCustomInfo> customInfo = new LinkedHashMap<>();

	public List<MapleSavedEmoticon> savedEmoticon;

	public List<MapleChatEmoticon> emoticonTabs;

	public List<Triple<Long, Integer, Short>> emoticons;
	public int getCharacterValue;

	private List<Pair<Integer, Short>> emoticonBookMarks;

	public CharacterTransfer() {
		this.famedcharacters = new ArrayList<>();
		this.battledaccs = new ArrayList<>();
		this.extendedSlots = new ArrayList<>();
		this.exceptionList = new ArrayList<>();
		this.rebuy = new ArrayList<>();
		this.cores = new ArrayList<>();
		this.coodination = new ArrayList<>();
		this.matrixs = new ArrayList<>();
		this.symbol = new ArrayList<>();
		this.auctionitems = new ArrayList<>();
		this.linkskills = new ArrayList<>();
		this.InfoQuest = new LinkedHashMap<>();
		this.keymap = new LinkedHashMap<>();
		this.hairRoom = new ArrayList<>();
		this.faceRoom = new ArrayList<>();
		this.skinRoom = new ArrayList<>();
		this.savedEmoticon = new CopyOnWriteArrayList<>();
		this.emoticonTabs = new CopyOnWriteArrayList<>();
		this.emoticons = new CopyOnWriteArrayList<>();
		this.emoticonBookMarks = new CopyOnWriteArrayList<>();
	}

	public CharacterTransfer(MapleCharacter chr) {
		this.characterid = chr.getId();
		this.accountid = chr.getAccountID();
		this.accountname = chr.getClient().getAccountName();
		this.secondPassword = chr.getClient().getSecondPassword();
		this.channel = (byte) chr.getClient().getChannel();
		this.nxCredit = chr.getCSPoints(1);
		this.ACash = 0;
		this.MaplePoints = chr.getCSPoints(2);
		this.vpoints = chr.getVPoints();
		this.stolenSkills = chr.getStolenSkills();
		this.name = chr.getName();
		this.fame = chr.getFame();
		this.gender = chr.getGender();
		this.secondgender = chr.getSecondGender();
		this.level = chr.getLevel();
		this.str = chr.getStat().getStr();
		this.dex = chr.getStat().getDex();
		this.int_ = chr.getStat().getInt();
		this.luk = chr.getStat().getLuk();
		this.hp = chr.getStat().getHp();
		this.mp = chr.getStat().getMp();
		this.maxhp = chr.getStat().getMaxHp();
		this.maxmp = chr.getStat().getMaxMp();
		this.exp = chr.getExp();
		this.hpApUsed = chr.getHpApUsed();
		this.remainingAp = chr.getRemainingAp();
		this.remainingSp = chr.getRemainingSps();
		this.meso = chr.getMeso();
		this.pvpExp = chr.getTotalBattleExp();
		this.pvpPoints = chr.getBattlePoints();
		this.itcafetime = chr.getInternetCafeTime();
		this.reborns = chr.getReborns();
		this.apstorage = chr.getAPS();
		this.skinColor = chr.getSkinColor();
		this.secondSkinColor = chr.getSecondSkinColor();
		this.job = chr.getJob();
		this.hair = chr.getHair();
		this.secondhair = chr.getSecondHair();
		this.face = chr.getFace();
		this.secondface = chr.getSecondFace();
		this.demonMarking = chr.getDemonMarking();
		this.mapid = chr.getMapId();
		this.initialSpawnPoint = chr.getInitialSpawnpoint();
		this.marriageId = chr.getMarriageId();
		this.world = chr.getWorld();
		this.guildid = chr.getGuildId();
		this.guildrank = chr.getGuildRank();
		this.guildContribution = chr.getGuildContribution();
		this.lastattendance = chr.getLastAttendance();
		this.alliancerank = chr.getAllianceRank();
		this.gmLevel = (byte) chr.getGMLevel();
		this.LinkMobCount = chr.getLinkMobCount();
		this.points = chr.getPoints();
		this.fairyExp = chr.getFairyExp();
		this.cardStack = chr.getCardStack();
		this.pets = chr.getPets();
		this.subcategory = chr.getSubcategory();
		this.imps = chr.getImps();
		this.fatigue = chr.getFatigue();
		this.currentrep = chr.getCurrentRep();
		this.totalrep = chr.getTotalRep();
		this.totalWins = chr.getTotalWins();
		this.totalLosses = chr.getTotalLosses();
		this.battleshipHP = chr.currentBattleshipHP();
		this.tempIP = chr.getClient().getTempIP();
		this.rebuy = chr.getRebuy();
		this.cores = chr.getCore();
		this.matrixs = chr.getMatrixs();
		this.symbol = chr.getSymbol();
		this.basecolor = chr.getBaseColor();
		this.addcolor = chr.getAddColor();
		this.baseprob = chr.getBaseProb();
		this.secondbasecolor = chr.getSecondBaseColor();
		this.secondaddcolor = chr.getSecondAddColor();
		this.secondbaseprob = chr.getSecondBaseProb();
		this.linkskills = chr.getLinkSkills();
		this.choicepotential = chr.choicepotential;
		this.returnscroll = chr.returnscroll;
		this.memorialcube = chr.memorialcube;
		this.returnSc = chr.returnSc;
		this.lastCharGuildId = chr.getLastCharGuildId();
		this.betaclothes = chr.getBetaClothes();
		this.energy = chr.energy;
		this.energycharge = chr.energyCharge;
		this.hairRoom = chr.getHairRoom();
		this.faceRoom = chr.getFaceRoom();
		this.skinRoom = chr.getSkinRoom();
		this.emoticons = chr.getEmoticons();
		this.emoticonTabs = chr.getEmoticonTabs();
		this.savedEmoticon = chr.getSavedEmoticon();
		this.SpectorGauge = chr.SpectorGauge;
		this.keyValues.putAll(chr.getKeyValues());
		this.keyValues_boss.putAll(chr.getKeyValues_boss());
		this.dojorank = chr.dojorank;
		this.dressUp = chr.getDressup();
		chr.getSecondaryStatEffectTimer().cancel(true);
		for (MapleTrait.MapleTraitType t : MapleTrait.MapleTraitType.values()) {
			this.traits.put(t, Integer.valueOf(chr.getTrait(t).getTotalExp()));
		}
		for (BuddylistEntry qs : chr.getBuddylist().getBuddies()) {
			this.buddies.put(
					new CharacterNameAndId(qs.getCharacterId(), qs.getAccountId(), qs.getName(), qs.getRepName(),
							qs.getLevel(), qs.getJob(), qs.getGroupName(), qs.getMemo()),
					Boolean.valueOf(qs.isVisible()));
		}
		for (MapleUnion union : chr.getUnions().getUnions()) {
			this.unions.add(union);
		}
		this.buddysize = chr.getBuddyCapacity();
		this.partyid = (chr.getParty() == null) ? -1 : chr.getParty().getId();
		if (chr.getMessenger() != null) {
			this.messengerid = chr.getMessenger().getId();
		} else {
			this.messengerid = 0;
		}
		this.InfoQuest = chr.getInfoQuest_Map();
		for (Map.Entry<MapleQuest, MapleQuestStatus> qs : chr.getQuest_Map().entrySet()) {
			this.Quest.put(Integer.valueOf(((MapleQuest) qs.getKey()).getId()), qs.getValue());
		}
		this.inventorys = chr.getInventorys();
		for (Map.Entry<Skill, SkillEntry> qs : chr.getSkills().entrySet()) {
			this.Skills.put(Integer.valueOf(((Skill) qs.getKey()).getId()), qs.getValue());
		}
		for (Map.Entry<Integer, Integer> cv : chr.getSkillCustomValues2().entrySet()) {
			this.customValue.put(cv.getKey(), cv.getValue());
		}
		for (Map.Entry<Integer, SkillCustomInfo> ci : chr.getSkillCustomValues().entrySet()) {
			this.customInfo.put(ci.getKey(), ci.getValue());
		}
		this.BlessOfFairy = chr.getBlessOfFairyOrigin();
		this.BlessOfEmpress = chr.getBlessOfEmpressOrigin();
		this.chalkboard = chr.getChalkboard();
		this.skillmacro = chr.getMacros();
		this.coodination = chr.getCoodination();
		this.keymap = chr.getKeyLayout().Layout();
		this.savedlocation = chr.getSavedLocations();
		this.wishlist = chr.getWishlist();
		this.rocks = chr.getRocks();
		this.regrocks = chr.getRegRocks();
		this.hyperrocks = chr.getHyperRocks();
		this.famedcharacters = chr.getFamedCharacters();
		this.battledaccs = chr.getBattledCharacters();
		this.lastfametime = chr.getLastFameTime();
		this.storage = chr.getStorage();
		this.cs = chr.getCashInventory();
		this.honourexp = chr.getHonourExp();
		this.honourlevel = chr.getHonorLevel();
		this.innerSkills = chr.getInnerSkills();
		this.artipacts = chr.getArtipacts();
		this.extendedSlots = chr.getExtendedSlots();
		this.exceptionList = chr.getExceptionList();
		MapleMount mount = chr.getMount();
		this.mount_itemid = mount.getItemId();
		this.mount_Fatigue = mount.getFatigue();
		this.mount_level = mount.getLevel();
		this.mount_exp = mount.getExp();
		this.TranferTime = System.currentTimeMillis();
		this.login = chr.getClient().isFirstlogin();
	}
}
