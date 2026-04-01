package client.status;

import java.io.Serializable;

public enum MonsterStatus implements Serializable {
	MS_IndiePdr(0), // 1.2.391 ok.
	MS_IndieMdr(1), // 1.2.391 ok.
	MS_IndieUNK(2), // 1.2.391 ok.
	MS_IndieUNK2(3), // 1.2.391 ok.

	// 1.2.391 NEW
	MS_Indie_Unk4(4), // 1.2.391 ok.
	MS_390_Unk5(5), // 1.2.391 ok.
	MS_390_Unk6(6), // 1.2.391 ok.

	MS_Pad(7), // 1.2.391 ok.
	MS_Pdr(8), // 1.2.391 ok.
	MS_Mad(9), // 1.2.391 ok.
	MS_Mdr(10), // 1.2.391 ok.
	MS_Acc(11), // 1.2.391 ok.
	MS_Eva(12), // 1.2.391 ok.
	MS_Speed(13), // 1.2.391 ok.
	MS_Stun(14), // 1.2.391 ok.
	MS_Freeze(15), // 1.2.391 ok.
	MS_390_UNK16(16), // 1.2.391 ok.
	MS_Seal(17), // 1.2.391 ok.
	MS_Darkness(18), // 1.2.391 ok.

	// 1.2.391 NEW
	MS_390_Unk19(19), // 1.2.391 ok.

	MS_Powerup(20), // 1.2.391 ok.
	MS_Magicup(21), // 1.2.391 ok.
	MS_PGuardup(22), // 1.2.391 ok.
	MS_MGuardup(23), // 1.2.391 ok.
	MS_PImmune(24), // 1.2.391 ok.
	MS_MImmune(25), // 1.2.391 ok.
	MS_Web(26), // 1.2.391 ok.
	MS_Hardskin(27), // 1.2.391 ok.
	MS_Ambush(28), // 1.2.391 ok.
	MS_Venom(29), // 1.2.391 ok.
	MS_Blind(30), // 1.2.391 ok.
	MS_SealSkill(31), // 1.2.391 ok.
	MS_Dazil(32), // 1.2.391 ok.
	MS_PCounter(33), // 1.2.391 ok.
	MS_MCounter(34), // 1.2.391 ok.
	MS_RiseByToss(35), // 1.2.391 ok.
	MS_BodyPressure(36), // 1.2.391 ok.
	MS_Weakness(37), // 1.2.391 ok.
	MS_Showdown(38), // 1.2.391 ok.

	// 1.2.391 NEW
	MS_390_Unk39(39), // 1.2.391 ok.

	MS_MagicCrash(40), // 1.2.391 ok.

	// 1.2.391 NEW
	MS_390_Unk41(41), // 1.2.391 ok.

	MS_Puriaus(42), // 1.2.391 ok.
	MS_HitCritDamR(43), // 1.2.391 ok.
	MS_AdddamParty(44), // 1.2.391 ok.
	MS_LandCrash(45), // 1.2.391 ok.
	MS_DeadlyCharge(46), // 1.2.391 ok.
	MS_Smite(47), // 1.2.391 ok.
	MS_AdddamSkill(48), // 1.2.391 ok.
	MS_Incizing(49), // 1.2.391 ok.
	MS_DodgeBodyAttack(50), // 1.2.391 ok.
	MS_DebuffHealing(51), // 1.2.391 ok.
	MS_AdddamSkill2(52), // 1.2.391 ok.
	MS_BodyAttack(53), // 1.2.391 ok.
	MS_TempMoveAbility(54), // 1.2.391 ok.
	MS_FixAtkRBuff(55), // 1.2.391 ok.
	MS_FixdamRBuff(56), // 1.2.391 ok.
	MS_ElementDarkness(57), // 1.2.391 ok.
	MS_AreaInstallByHit(58), // 1.2.391 ok.
	MS_BMageDebuff(59), // 1.2.391 ok.
	MS_JaguarProvoke(60), // 1.2.391 ok.
	MS_JaguarBleeding(61), // 1.2.391 ok.
	MS_DarkLightning(62), // 1.2.391 ok.
	MS_PvPHelenaMark(63), // 1.2.391 ok.

	// 1.2.391 NEW
	MS_390_Unk64(64), // 1.2.391 ok.
	MS_390_Unk65(65), // 1.2.391 ok.
	MS_390_Unk66(66), // 1.2.391 ok.
	MS_390_Unk67(67), // 1.2.391 ok.

	MS_PsychicLock(68), // 1.2.391 ok.
	MS_MultiPMDR(69), // 1.2.391 ok.
	MS_PsychicGroundMark(70), // 1.2.391 ok.

	MS_ElementResetBySummon(71), // 1.2.391 ok.
	MS_CurseMark(72), // 1.2.391 ok.
	MS_Unk1(73), // 1.2.391 ok.
	MS_DragonStrike(74), // 1.2.391 ok.
	MS_Unk2(75), // 1.2.391 ok.
	MS_BlessterDamage(76), // 1.2.391 ok.
	MS_Unk4(77), // 1.2.391 ok.
	MS_Unk5(78), // 1.2.391 ok.
	MS_PopulatusTimer(79), // 1.2.391 ok.
	MS_PopulatusRing(80), // 1.2.391 ok.

	// 1.2.391 NEW
	MS_390_Unk80(81), // 1.2.391 ok.
	MS_390_Unk81(82), // 1.2.391 ok.

	MS_PVPRude_Stack(83), // 1.2.391 ok.

	MS_BahamutLightElemAddDam(84), // 1.2.391 ok.
	MS_BossPropPlus(85), // 1.2.391 ok.
	MS_MultiDamSkill(86), // 1.2.391 ok.
	MS_RWLiftPress(87), // 1.2.391 ok.
	MS_RWChoppingHammer(88), // 1.2.391 ok.
	MS_TimeBomb(89), // 1.2.391 ok.
	MS_Treasure(90), // 1.2.391 ok.
	MS_AddEffect(91), // 1.2.391 ok.
	MS_TheSeedBuff(92), // 1.2.391 ok.

	// 1.2.391 NEW
	MS_390_Unk92(93), // 1.2.391 ok.
	MS_390_Unk93(94), // 1.2.391 ok.

	MS_CriticalBind_N(95), // 1.2.391 ok.
	MS_Explosion(96), // 1.2.391 ok.

	// 1.2.391 NEW
	MS_390_Unk96(97), // 1.2.391 ok.
	MS_390_Unk97(98), // 1.2.391 ok.
	MS_390_Unk98(99), // 1.2.391 ok.
	MS_390_Unk99(100), // 1.2.391 ok.
	MS_390_Unk100(101), // 1.2.391 ok.

	MS_PowerImmune(102), // 1.2.391 ok.

	// 1.2.391 NEW
	MS_390_Unk102(103), // 1.2.391 ok.

	MS_HangOver(104), // 1.2.391 ok.
	MS_PopulatusInvincible(105), // 1.2.391 ok.

	// 1.2.391 NEW
	MS_390_Unk105(106), // 1.2.391 ok.
	MS_390_Unk106(107), // 1.2.391 ok.

	MS_Burned(108), // 1.2.391 ok.
	MS_BalogDisable(109), // 1.2.391 ok.
	MS_ExchangeAttack(110), // 1.2.391 ok.
	MS_AddBuffStat(111), // 1.2.391 ok.
	MS_LinkTeam(112), // 1.2.391 ok.
	MS_SoulExplosion(113), // 1.2.391 ok.
	MS_SeperateSoulP(114), // 1.2.391 ok.
	MS_SeperateSoulC(115), // 1.2.391 ok.
	MS_TrueSight(116), // 1.2.391 ok.
	MS_Laser(117), // 1.2.391 ok.
	MS_StatResetSkill(118), // 1.2.391 ok.

	// 1.2.391 NEW
	MS_390_Unk118(119), // 1.2.391 ok.
	MS_390_Unk119(120), // 1.2.391 ok.
	MS_390_Unk120(121), // 1.2.391 ok.
	MS_390_Unk121(122), // 1.2.391 ok.
	MS_390_Unk122(123), // 1.2.391 ok.
	MS_Origin_Bind(124), // 1.2.391 ok.
	MS_390_Unk124(125), // 1.2.391 ok.
	MS_390_Unk125(126), // 1.2.391 ok.
	MS_390_Unk126(127), // 1.2.391 ok.
	MS_390_Unk127(128), // 1.2.391 ok.
	MS_390_Unk128(129), // 1.2.391 ok.
	MS_390_Unk129(130); // 1.2.391 ok.

	static final long serialVersionUID = 0L;

	private final int i;

	private final int first;

	private final int flag;

	private final boolean end;

	private boolean stacked;

	MonsterStatus(int flag) {
		this.i = 1 << 31 - flag % 32;
		this.first = 5 - (byte) (int) Math.floor((flag / 32));
		this.flag = flag;
		this.end = false;
		setStacked(name().startsWith("MS_Indie"));
	}

	public int getPosition() {
		return this.first;
	}

	public boolean isEmpty() {
		return this.end;
	}

	public int getValue() {
		return this.i;
	}

	public int getFlag() {
		return this.flag;
	}

	public boolean isStacked() {
		return this.stacked;
	}

	public void setStacked(boolean stacked) {
		this.stacked = stacked;
	}

	public static boolean IsMovementAffectingStat(MonsterStatus skill) {
		switch (skill) {
		case MS_Stun:
		case MS_Speed:
		case MS_Freeze:
		case MS_Origin_Bind:
		case MS_RiseByToss:
		case MS_Smite:
		case MS_TempMoveAbility:
		case MS_StatResetSkill:
		case MS_RWLiftPress:
		case MS_AdddamSkill2:
		case MS_PCounter:
		case MS_MCounter:
			return true;
		}
		return false;
	}
}
