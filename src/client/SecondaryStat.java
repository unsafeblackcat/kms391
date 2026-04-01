package client;

import server.Randomizer;
import tools.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public enum SecondaryStat implements Serializable {
	// 1.2.391 + 0
	IndiePad(0), IndieMad(1), IndiePdd(2), IndieHp(3), IndieHpR(4), IndieMp(5), IndieMpR(6), IndieAcc(7), IndieEva(8),
	IndieJump(9), IndieSpeed(10), IndieAllStat(11), IndieAllStatR(12), IndieDodgeCriticalTime(13), IndieExp(14),
	IndieBooster(15), IndieFixedDamageR(16), PyramidStunBuff(17), PyramidFrozenBuff(18), PyramidFireBuff(19),
	PyramidBonusDamageBuff(20), IndieRelaxEXP(21), IndieStr(22), IndieDex(23), IndieInt(24), IndieLuk(25),
	IndieDamR(26), IndieScriptBuff(27), IndieMaxDamageR(28), IndieAsrR(29), IndieTerR(30), IndieCr(31), IndiePddR(32),
	IndieCD(33), IndieBDR(34), IndieStatR(35), IndieStance(36), IndieIgnoreMobPdpR(37), IndieEmpty(38), IndiePadR(39),
	IndieMadR(40), IndieEvaR(41), IndieDrainHP(42), IndiePmdR(43), IndieForceJump(44), IndieForceSpeed(45),
	IndieDamageReduce(46), IndieSummon(47), IndieReduceCooltime(48), IndieNotDamaged(49), IndieJointAttack(50),
	IndieKeyDownMoving(51), IndieUnkIllium(52), IndieEvasion(53), IndieShotDamage(54), IndieSuperStance(55),
	IndieGrandCross(56), IndieDamReduceR(57), IndieWickening1(58), IndieWickening2(59), IndieWickening3(60),
	IndieWickening4(61), IndieFloating(62), IndieUnk1(63), IndieUnk2(64), IndieDarkness(65), IndieBlockSkill(66),
	IndieUnk67(67), IndieBarrier(68), IndieUnk69(69), IndieNDR(70), IndieUnk71(71), Indie_STAT_COUNT(72),
	IndieUnk73(73), IndieUnk74(74), Pad(76), Pdd(77), Mad(78), Acc(79), Eva(80), Craft(81), Speed(82), // 확실
	Jump(83), MagicGaurd(84), DarkSight(85), // 확실
	Booster(86), PowerGaurd(87), MaxHP(88), MaxMP(89), Invincible(90), SoulArrow(91), Stun(92), // 확실
	Poison(93), // 확실
	Seal(94), // 확실
	Darkness(95), // 확실
	ComboCounter(96), // 확실
	BlessedHammer(97), // 확실
	BlessedHammer2(98), // 확실
	SnowCharge(99), // 확실
	HolySymbol(100), // 확실
	MesoUp(101), ShadowPartner(102), // 확실
	Steal(103), PickPocket(104), // 확실
	Murderous(105), Thaw(106), Weakness(107), // 확실
	Curse(108), // 확실
	Slow(109), // 확실
	Morph(110), // 확실
	Recovery(111), BasicStatUp(112), Stance(113), // 확실
	SharpEyes(114), // 확실
	ManaReflection(115), Attract(116), // 확실
	NoBulletConsume(117), // 확실
	Infinity(118), // 확실
	AdvancedBless(119), // 확실
	Illusion(120), Blind(121), Concentration(122), BanMap(123), // 확실
	MaxLevelBuff(124), MesoUpByItem(125), WealthOfUnion(126), RuneOfGreed(127), Ghost(128), // 확실
	Barrier(129), // 확실
	ReverseInput(130), // 확실
	ItemUpByItem(131), RespectPImmune(132), // 확실
	RespectMImmune(133), // 확실
	DefenseAtt(134), // 확실
	DefenseState(135), // 확실
	DojangBerserk(136), // 확실
	DojangInvincible(137), DojangShield(138), // 확실
	SoulMasterFinal(139), WindBreakerFinal(140), // 확실
	Origin_Karma_Blade_Stack(141), // 확실

	// 1.2.391 NEW
	Unk_142(142),

	// 1.2.391 + 0
	HideAttack(143), // 확실
	EventRate(144), AranCombo(145), AuraRecovery(146), UnkBuffStat1(147), BodyPressure(148), RepeatEffect(149), // 확실
	ExpBuffRate(150), StopPortion(151), // 확실
	StopMotion(152), // 확실
	Fear(153), // 확실
	HiddenPieceOn(154), // 확실
	MagicShield(155), // 확실
	SoulStone(156),

	// 1.2.391 NEW
	Unk_157(157),

	// 1.2.391 + 0
	Flying(158), Frozen(159), // 확실
	AssistCharge(160), Enrage(161), DrawBack(162), // 확실
	NotDamaged(163), // 확실
	FinalCut(164), // 확실
	HowlingParty(165), BeastFormDamage(166), Dance(167), // 확실
	EnhancedMaxHp(168), EnhancedMaxMp(169), EnhancedPad(170), EnhancedMad(171), EnhancedPdd(172), PerfectArmor(173),
	UnkBuffStat2(174), IncreaseJabelinDam(175), // 확실
	PinkbeanMinibeenMove(176), // 확실
	Sneak(177), // 테스트 필요 (Unk_178 때문에 하나씩 밀렸을 수도 있음.)

	// 1.2.391 NEW
	Unk_178(178),

	// 1.2.391 + 1
	Mechanic(179), // 확실
	BeastFormMaxHP(180), DiceRoll(181), // 확실
	BlessingArmor(182), // 확실
	DamR(183), TeleportMastery(184), // 확실
	CombatOrders(185), Beholder(186), // 확실
	DispelItemOption(187), Inflation(188), // 확실
	OnixDivineProtection(189), Web(190), // 확실
	Bless(191), // 확실
	TimeBomb(192), // 확실
	DisOrder(193), // 확실
	Thread(194), // 확실
	Team(195), // 확실
	Explosion(196), // 확실
	BuffLimit(197), STR(198), INT(199), DEX(200), LUK(201), DispelByField(202), DarkTornado(203), // 확실
	PVPDamage(204), // 확실
	PvPScoreBonus(205), PvPInvincible(206), PvPRaceEffect(207), // 확실
	WeaknessMdamage(208), // 확실
	Frozen2(209), // 확실
	PvPDamageSkill(210), AmplifyDamage(211), // 확실
	Shock(212), // 확실
	InfinityForce(213), IncMaxHP(214), IncMaxMP(215), HolyMagicShell(216), // 확실
	KeyDownTimeIgnore(217), ArcaneAim(218), MasterMagicOn(219), Asr(220), Ter(221), DamAbsorbShield(222), // 확실
	DevilishPower(223), // 확실
	Roulette(224), // 需要測試（可能因為Unk_225而一個一個延后了。）

	// 1.2.391 NEW
	Unk_225(225), // 需要測試（可能因為Unk_225而一個一個延后了。）

	// 1.2.391 + 1
	SpiritLink(226), // 확실
	AsrRByItem(227), Event(228), // 확실
	CriticalIncrease(229), DropItemRate(230), DropRate(231), ItemInvincible(232), Awake(233), ItemCritical(234),
	ItemEvade(235), Event2(236), // 확실
	DrainHp(237), IncDefenseR(238), IncTerR(239), IncAsrR(240), DeathMark(241), // 확실
	Infiltrate(242), // 확실
	Lapidification(243), // 확실
	VenomSnake(244), // 확실
	CarnivalAttack(245), CarnivalDefence(246), CarnivalExp(247), SlowAttack(248), PyramidEffect(249), // 확실
	UnkBuffStat3(250), KeyDownMoving(251), // 확실
	IgnoreTargetDEF(252), // 확실
	UNK_253(253), // 확실
	ReviveOnce(254), Invisible(255), // 확실
	EnrageCr(256), EnrageCrDamMin(257), Judgement(258), // 확실
	DojangLuckyBonus(259), PainMark(260), // 확실
	Magnet(261), // 확실
	MagnetArea(262), // 확실
	GuidedArrow(263), // 확실
	UnkBuffStat4(264), // 확실
	BlessMark(265), // 확실
	BonusAttack(266), // 확실
	UnkBuffStat5(267), // 확실
	FlowOfFight(268),

	// 1.2.391 NEW
	Unk_269(269), // 확실
	Unk_270(270), // 확실

	// 1.2.391 + 1
	ShadowMomentum(272), GrandCrossSize(273), // 확실
	LuckOfUnion(274), // 확실
	PinkBeanFighting(275), // 확실

	// 1.2.391 NEW
	Unk_276(276),

	// 1.2.391 + 1
	VampDeath(277), // 확실
	BlessingArmorIncPad(278), KeyDownAreaMoving(279), // 확실
	Larkness(280), // 확실
	StackBuff(281), // 확실
	AntiMagicShell(282), // 확실
	LifeTidal(283), // 확실
	HitCriDamR(284), SmashStack(285), // 확실
	RoburstArmor(286), ReshuffleSwitch(287), // 확실
	SpecialAction(288), // 확실
	VampDeathSummon(289), // 확실
	StopForceAtominfo(290), // 확실
	SoulGazeCriDamR(291), // 확실
	Affinity(292), PowerTransferGauge(293), // 확실
	AffinitySlug(294), // 확실
	Trinity(295), // 확실
	IncMaxDamage(296), BossShield(297), MobZoneState(298), // 확실
	GiveMeHeal(299), // 확실
	TouchMe(300), // 확실
	Contagion(301), // 확실
	ComboUnlimited(302), // 확실
	SoulExalt(303), // 확실
	IgnorePCounter(304), // 확실
	IgnoreAllCounter(305), // 확실
	IgnorePImmune(306), // 확실
	IgnoreAllImmune(307), // 확실
	UnkBuffStat6(308), // 확실
	FireAura(309), // 확실
	VengeanceOfAngel(310), HeavensDoor(311), // 확실
	Preparation(312), BullsEye(313), IncEffectHPPotion(314), IncEffectMPPotion(315), BleedingToxin(316), // 확실
	IgnoreMobDamR(317), // 확실
	Asura(318), // 확실
	MegaSmasher(319), // 확실
	FlipTheCoin(320), UnityOfPower(321), // 확실
	Stimulate(322), // 확실
	ReturnTeleport(323), // 확실
	DropRIncrease(324), // 확실
	IgnoreMobPdpR(325), // 확실
	BdR(326), // 확실
	CapDebuff(327), // 확실
	Exceed(328), DiabloicRecovery(329), FinalAttackProp(330), ExceedOverload(331), OverloadCount(332), // 확실
	Buckshot(333), FireBomb(334), // 확실
	HalfstatByDebuff(335), // 확실
	SurplusSupply(336), // 확실
	SetBaseDamage(337),

	// 1.2.391 NEW
	UNK_338(338),

	// 1.2.391 + 1
	NewFlying(339), // 확실
	AmaranthGenerator(340), // 확실
	CygnusElementSkill(341), // 확실
	StrikerHyperElectric(342), // 확실
	EventPointAbsorb(343), // 확실
	EventAssemble(344), // 확실
	StormBringer(345), AccR(346), DexR(347), Translucence(348), // 확실
	PoseType(349), // 확실
	CosmicForge(350), // 확실
	ElementSoul(351), // 확실
	CosmicOrb(352), GlimmeringTime(353), // 확실
	SolunaTime(354), WindWalk(355), SoulMP(356), // 확실
	FullSoulMP(357), // 확실
	SoulSkillDamageUp(358), ElementalCharge(359), // 확실
	Listonation(360), CrossOverChain(361), // 확실
	ChargeBuff(362), // 확실
	Reincarnation(363), // 확실
	ReincarnationAccept(364), ChillingStep(365), // 확실
	DotBasedBuff(366),

	// 1.2.391 NEW
	Unk_367(367),

	// 1.2.391 + 1
	BlessingAnsanble(368), // 테스트 필요 (Unk_367 때문에 하나씩 밀렸을 수도 있음.)
	ExtremeArchery(369), // 테스트 필요 (Unk_367 때문에 하나씩 밀렸을 수도 있음.)
	NaviFlying(370),

	// 1.2.391 NEW
	IndieNotMove(371),

	// 1.2.391 + 1
	QuiverCatridge(372), // 확실
	AdvancedQuiver(373), ImmuneBarrier(374), // 확실
	ArmorPiercing(375), // 확실
	CardinalMark(376), QuickDraw(377), BowMasterConcentration(378), TimeFastABuff(379), TimeFastBBuff(380),
	GatherDropR(381), AimBox2D(382), TrueSniping(383), // 확실
	DebuffTolerance(384), UnkBuffStat8(385), DotHealHPPerSecond(386), // 확실
	DotHealMPPerSecond(387), // 확실
	SpiritGuard(388), // 확실
	PreReviveOnce(389), SetBaseDamageByBuff(390), LimitMP(391), ReflectDamR(392), ComboTempest(393), // 확실
	MHPCutR(394), MMPCutR(395), SelfWeakness(396), ElementDarkness(397), FlareTrick(398), Ember(399), // 확실
	Dominion(400), SiphonVitality(401), DarknessAscension(402), BossWaitingLinesBuff(403), DamageReduce(404),
	ShadowServant(405), ShadowIllusion(406), KnockBack(407), // 확실
	IgnisRore(408), ComplusionSlant(409), // 확실
	JaguarSummoned(410), // 확실
	JaguarCount(411), SSFShootingAttack(412), // 확실

	// 1.2.391 NEW
	Unk_413(413),

	// 1.2.391 + 1
	ShieldAttack(414), // 확실
	DarkLighting(415), // 확실
	AttackCountX(416), // 확실
	BMageDeath(417), // 확실
	BombTime(418), // 확실
	NoDebuff(419), BattlePvP_Mike_Shield(420), BattlePvP_Mike_Bugle(421), AegisSystem(422), SoulSeekerExpert(423),
	HiddenPossession(424), ShadowBatt(425), // 확실
	MarkofNightLord(426), WizardIgnite(427), FireBarrier(428), // 확실

	// 1.2.391 + 1
	HolyUnity(430), // 확실
	DemonFrenzy(431), // 확실
	ShadowSpear(432), // 확실

	// 1.2.391 + 1
	DemonDamageAbsorbShield(433), // 확실
	Ellision(434), QuiverFullBurst(435), // 확실
	LuminousPerfusion(436), WildGrenadier(437), ChainArts_Stroke_Stack(438), // 확실

	// 1.2.391 NEW
	PVPUnk_439(439), PVPUnk_440(440), PVPUnk_441(441), // 확실
	PVPUnk_442(442), // 확실
	PVPUnk_443(443), PVPUnk_444(444), PVPUnk_445(445), PVPUnk_446(446),

	// 1.2.391 + 2
	PinkbeanAttackBuff(447), // 확실
	PinkbeanRelax(448), PinkbeanRollingGrade(449), // 확실
	PinkbeanYoYoStack(450), RandAreaAttack(451), NextAttackEnhance(452), BeyondNextAttackProb(453),
	AranCombotempastOption(454), NautilusFinalAttack(455), ViperTimeLeap(456),

	// 1.2.391 NEW
	Unk_457(457), Unk_458(458),

	// 1.2.391 + 2
	RoyalGuardState(459), // 확실
	RoyalGuardPrepare(460), MichaelSoulLink(461), // 확실
	MichaelProtectofLight(462), TryflingWarm(463), AddRange(464), KinesisPsychicPoint(465), // 확실
	KinesisPsychicOver(466), KinesisIncMastery(467), KinesisPsychicEnergeShield(468), // 확실
	BladeStance(469), // 확실
	DebuffActiveHp(470), DebuffIncHp(471), MortalBlow(472), SoulResonance(473), Fever(474), // 확실
	SikSin(475), TeleportMasteryRange(476), FixCooltime(477),

	// 1.2.391 NEW
	Unk_478(478),

	// 1.2.391 + 3
	AdrenalinBoost(479), // 확실

	// 1.2.391 + 2
	AranDrain(480), // 테스트 필요 (Unk_482 때문에 하나씩 밀렸을 수도 있음.)
	AranBoostEndHunt(481), // 테스트 필요 (Unk_482 때문에 하나씩 밀렸을 수도 있음.)

	// 1.2.391 NEW
	Unk_482(482), // 테스트 필요 (Unk_482 때문에 하나씩 밀렸을 수도 있음.)

	// 1.2.391 + 2
	RWCylinder(483), // 확실
	RWCombination(484), RWUnk(485), // 확실
	RwMagnumBlow(486), // 확실
	RwBarrier(487), // 확실
	RWBarrierHeal(488), RWMaximizeCannon(489), RWOverHeat(490), UsingScouter(491), // 확실
	RWMovingEvar(492), Stigma(493), // 확실
	InstallMaha(494), // 확실
	CooldownHeavensDoor(495), CooldownRune(496), PinPointRocket(497), Transform(498), // 확실
	EnergyBurst(499), // 확실
	Striker1st(500), // 확실
	BulletParty(501), // 확실
	SelectDice(502), // 확실
	Pray(503), // 확실
	ChainArtsFury(504), DamageDecreaseWithHP(505), PinkbeanYoYoAttackStack(506), AuraWeapon(507), AuraWeaponStack(508),
	OverloadMana(509), // 확실
	RhoAias(510), // 확실
	PsychicTornado(511), // 확실
	SpreadThrow(512), HowlingGale(513), VMatrixStackBuff(514), MiniCannonBall(515), ShadowAssult(516),
	MultipleOption(517), UnkBuffStat15(518), BlitzShield(519), // 확실
	SplitArrow(520), FreudsProtection(521), // 확실
	Overload(522), // 확실
	Spotlight(523), // 확실
	KawoongDebuff(524), // 확실
	WeaponVariety(525), // 확실
	GloryWing(526), // 확실
	ShadowerDebuff(527), // 확실
	OverDrive(528), // 확실
	Etherealform(529), // 확실
	ReadyToDie(530), // 확실
	Oblivion(531), // 확실
	CriticalReinForce(532), // 확실
	CurseOfCreation(533), // 확실
	CurseOfDestruction(534), // 확실
	BlackMageDebuff(535), // 확실
	BodyOfSteal(536), // 확실
	PapulCuss(537), // 확실
	PapulBomb(538), // 확실
	HarmonyLink(539), // 확실
	FastCharge(540), // 확실
	UnkBuffStat20(541), CrystalBattery(542), Deus(543), UnkBuffStat21(544), BattlePvP_Rude_Stack(545), // 확실
	UnkBuffStat23(546), UnkBuffStat24(547), UnkBuffStat25(548), SpectorGauge(549), // 확실
	SpectorTransForm(550), // 확실
	PlainBuff(551), // 확실
	ScarletBuff(552), // 확실
	GustBuff(553), // 확실
	AbyssBuff(554), // 확실
	ComingDeath(555), // 확실
	FightJazz(556), // 확실
	ChargeSpellAmplification(557), // 확실

	// 1.2.391 NEW
	Unk_558(558), Unk_559(559), Unk_560(560), Unk_561(561),

	// 1.2.391 + 4
	InfinitySpell(562), MagicCircuitFullDrive(563), MagicCircuitFullDriveStack(564), LinkOfArk(565),
	MemoryOfSource(566), UnkBuffStat26(567), WillPoison(568), // 확실
	UnkBuffStat27(569), UnkBuffStat28(570), // 확실
	CooltimeHolyMagicShell(571), Striker3rd(572), ComboInstict(573), Ultimatum(574), WindWall(575), // 다시 볼 것
	InfinityFlameCircle(576), // 확실
	SwordOfSoulLight(577), MarkOfPhantomStack(578), // 확실
	MarkOfPhantomDebuff(579), // 확실
	UnkBuffStat30(580), UnkBuffStat31(581), UnkBuffStat32(582), UnkBuffStat33(583), UnkBuffStat34(584),
	EventSpecialSkill(585), // 확실
	PmdReduce(586), // 확실
	ForbidOpPotion(587), ForbidEquipChange(588), // 확실
	YalBuff(589), // 확실
	IonBuff(590), // 확실
	UnkBuffStat35(591), // 확실
	DefUp(592), Protective(593), // 확실
	AncientGuidance(594), // 확실
	BattlePvP_Wonky_ChargeA(595), // 확실
	UNK_596(596), // 확실
	BattlePvP_Wonky_Awesome(597), // 확실
	UnkBuffStat42(598), // 확실
	UnkBuffStat43(599), // 확실
	UnkBuffStat44(600), UNK_601(601), // 확실
	UNK_602(602), // 확실

	// 1.2.391 NEW
	Unk_603(603), // 확실
	Unk_604(604), // 확실
	Unk_605(605),

	// 1.2.391 + 3
	unk_606(606), // 확실
	Bless5th(607),

	// 1.2.391 NEW
	Unk_608(608), // 확실

	// 1.2.391 + 3
	PinkBeanMatroCyca(609), // 확실
	UnkBuffStat46(610), // 확실
	UnkBuffStat47(611), UnkBuffStat48(612), UnkBuffStat49(613), UnkBuffStat50(614), // 확실
	PapyrusOfLuck(615), // 확실
	HoyoungThirdProperty(616), // 확실
	TidalForce(617), // 확실
	Alterego(618), AltergoReinforce(619), // 확실
	ButterflyDream(620), Sungi(621), SageWrathOfGods(622), // 확실
	EmpiricalKnowledge(623), // 확실
	UnkBuffStat52(624), UnkBuffStat53(625), // 확실
	Graffiti(626), // 확실
	DreamDowon(627),

	// 1.2.391 NEW
	Unk_628(628),

	// 1.2.391 + 3
	WillofSwordStrike(629), AdelGauge(630), Creation(631), Dike(632), Wonder(633), Restore(634), Novility(635), // 확실
	AdelResonance(636), RuneOfPure(637), // 확실
	RuneOfTransition(638), // 확실
	DuskDarkness(639), // 확실
	YellowAura(640), // 확실
	DrainAura(641), // 확실
	BlueAura(642), // 확실
	DarkAura(643), // 확실
	DebuffAura(644), // 확실
	UnionAura(645), // 확실
	IceAura(646), // 확실
	KnightsAura(647), // 확실
	ZeroAuraStr(648), // 확실
	IncarnationAura(649), // 확실
	AdventOfGods(650), Revenant(651), // 확실
	RevenantDamage(652), // 확실
	SilhouetteMirage(653), // 확실
	BlizzardTempest(654), // 확실
	PhotonRay(655), // 확실
	AbyssalLightning(656), // 확실
	Striker4th(657), RoyalKnights(658), SalamanderMischief(659),

	// 1.2.391 NEW
	Unk_660(660),

	// 1.2.391 + 3
	LawOfGravity(661), // 확실
	RepeatingCrossbowCatridge(662), CrystalGate(663), // 확실
	ThrowBlasting(664), SageElementalClone(665), DarknessAura(666), // 확실
	WeaponVarietyFinale(667), // 확실
	LiberationOrb(668), // 확실
	LiberationOrbActive(669), // 확실
	EgoWeapon(670), RelikUnboundDischarge(671), MoraleBoost(672), AfterImageShock(673), Malice(674), Possession(675),
	DeathBlessing(676), ThanatosDescent(677), // 확실

	// 1.2.391 NEW
	Unk_678(678), // 확실

	// 1.2.391 + 3
	RemainIncense(679), GripOfAgony(680), DragonPang(681), // 확실
	SerenDebuffs(682), SerenDebuff(683), SerenDebuffUnk(684), PriorPryperation(685),

	// 1.2.391 NEW
	Unk_686(686), Unk_687(687), Unk_688(688), Unk_689(689), Unk_690(690), Unk_691(691), Unk_692(692), Unk_693(693),

	// 1.2.391 + 3
	AdrenalinBoostActive(694), YetiAnger(695), // 확실
	YetiAngerMode(696), // 확실
	YetiSpicy(697), // 확실
	YetiFriendsPePe(698), PinkBeanMagicShow(699), 용맥_읽기(702), 산의씨앗(703), 산_무등(704), 흡수_강(705), // 확실
	흡수_바람(706), // 확실
	흡수_해(707), // 확실
	자유로운용맥(708),

	// 1.2.391 NEW
	Unk_709(709),

	// 1.2.391 + 3
	Lotus(710), NatureFriend(711),

	// 1.2.391 NEW
	Unk_712(712), Unk_713(713),

	// 1.2.391 + 3
	StormWimb(714), SeaSerpent(715), SerpentStone(716), SerpentScrew(717), // 확실

	// 1.2.391 NEW
	Unk_718(718),

	// 1.2.391 + 3
	Cosmos(719), // 확실

	// 1.2.391 NEW
	Piercing_Stack(720), // 확실
	Sniping_Stack(721), // 확실
	Sniping_Mode(722), // 확실
	Piercing_Mode(723), // 확실
	Quadruple_Throw_Stack(724), // 확실
	Unk_725(725), // 확실
	Mark_Of_Assassin(726), // 확실

	// 1.2.391 + 3
	ScaringSword(727), // 확실
	HolyWater(728), // 확실
	Triumph(730),

	// 1.2.391 NEW
	AngelRay_Stack(731), // 확실
	Unk_732(732), // 확실
	Unk_733(733),

	// 1.2.391 + 3
	FlashMirage(734), // 확실
	HolyBlood(735), // 확실
	OrbitalExplosion(736), PhoenixDrive(737), UnkBuffStat707(738), // 확실
	Transcendent(739), // 확실

	// 1.2.391 NEW
	Smash_Stack(740), Unk_741(741), Origin_Artificial_Evolution(742), // 확실
	Origin_Frozen_Lightning(743),

	// 1.2.391 + 3
	UltimateBPM(744), ElementalKnight(745), EquilibriumLiberation(746), SummonChakri(747), VoydEnhance(748),
	VoydBust(749), DishingBlade(750),

	// 여기 사이에 카링 디버프 스텟, 칼로스 디버프 스텟

	// 1.2.391 NEW
	Unk_751(751), Unk_752(752), // 확실
	Unk_753(753), // 확실
	Unk_754(754), // 확실
	Unk_755(755), // 확실
	Unk_756(756), // 확실
	Unk_757(757), // 확실
	Unk_758(758), // 확실
	Unk_759(759), // 확실
	Unk_760(760), Poem_Of_Storm(761), // 확실
	Unk_762(762), Unk_763(763), Unk_764(764), Unk_765(765), // 확실
	Origin_Adrenaline_Surge(766), // 확실
	Origin_Life_And_Death(767), // 확실
	Unk_768(768), Origin_Forsaken_Relic(769), // 확실
	Origin_Nightmare(770), // 확실
	Unk_771(771), Unk_772(772), // 확실
	Unk_773(773),

	// 1.2.391 + 3
	CurseEnchant(774),

	// 1.2.391 NEW
	Unk_775(775), Unk_776(776), // 확실
	Release_Pile_Bunker_Stack(777), // 확실

	// 1.2.391 + 3
	DashJump(778), DashSpeed(779), RideVehicle(780), PartyBooster(781), GuidedBullet(782), Undead(783),
	RideVehicleExpire(784), RelikGauge(785), Grave(786), CountPlus1(787);

	private static final long serialVersionUID = 0L;
	private int buffstat;
	private int first;
	private boolean stacked;
	private int disease;
	private int flag;
	private int x;
	private int y;

	private SecondaryStat(final int flag) {
		this.stacked = false;
		this.buffstat = 1 << 31 - flag % 32;
		this.setFirst(31 - (byte) Math.floor(flag / 32));
		this.setStacked(this.name().startsWith("Indie") || this.name().startsWith("Pyramid"));
		this.setFlag(flag);
	}

	private SecondaryStat(final int flag, final int disease) {
		this.stacked = false;
		this.buffstat = 1 << 31 - flag % 32;
		this.setFirst(31 - (byte) Math.floor(flag / 32));
		this.setStacked(this.name().startsWith("Indie") || this.name().startsWith("Pyramid"));
		this.setFlag(flag);
		this.disease = disease;
	}

	private SecondaryStat(final int flag, final int first, final int disease) {
		this.stacked = false;
		this.buffstat = 1 << 31 - flag % 32;
		this.setFirst(first);
		this.setFlag(flag);
		this.disease = disease;
	}

	public final int getPosition() {
		return this.getFirst();
	}

	public final int getPosition(final boolean stacked) {
		if (!stacked) {
			return this.getFirst();
		}
		switch (this.getFirst()) {
		case 16: {
			return 0;
		}
		case 15: {
			return 1;
		}
		case 14: {
			return 2;
		}
		case 13: {
			return 3;
		}
		case 12: {
			return 4;
		}
		case 11: {
			return 5;
		}
		case 10: {
			return 6;
		}
		case 9: {
			return 7;
		}
		case 8: {
			return 8;
		}
		case 7: {
			return 9;
		}
		case 6: {
			return 10;
		}
		case 5: {
			return 11;
		}
		case 4: {
			return 12;
		}
		case 3: {
			return 13;
		}
		case 2: {
			return 14;
		}
		case 1: {
			return 15;
		}
		case 0: {
			return 16;
		}
		default: {
			return 0;
		}
		}
	}

	public final int getValue() {
		return this.getBuffstat();
	}

	public final boolean canStack() {
		return this.isStacked();
	}

	public int getDisease() {
		return this.disease;
	}

	public int getX() {
		return this.x;
	}

	public void setX(final int x) {
		this.x = x;
	}

	public int getY() {
		return this.y;
	}

	public void setY(final int y) {
		this.y = y;
	}

	public static final SecondaryStat getByFlag(final int flag) {
		for (final SecondaryStat d : values()) {
			if (d.getFlag() == flag) {
				return d;
			}
		}
		return null;
	}

	public static final SecondaryStat getBySkill(final int skill) {
		for (final SecondaryStat d : values()) {
			if (d.getDisease() == skill) {
				return d;
			}
		}
		return null;
	}

	public static final List<SecondaryStat> getUnkBuffStats() {
		final List<SecondaryStat> stats = new ArrayList<SecondaryStat>();
		for (final SecondaryStat d : values()) {
			if (d.name().startsWith("UnkBuff")) {
				stats.add(d);
			}
		}
		return stats;
	}

	public static final SecondaryStat getRandom() {
		SecondaryStat dis = null;
		Block_1: while (true) {
			final SecondaryStat[] values = values();
			for (int length = values.length, i = 0; i < length; ++i) {
				dis = values[i];
				if (Randomizer.nextInt(values().length) == 0) {
					break Block_1;
				}
			}
		}
		return dis;
	}

	public static boolean isEncode4Byte(final Map<SecondaryStat, Pair<Integer, Integer>> statups) {
		final SecondaryStat[] statsToCheck = { SecondaryStat.CarnivalDefence, SecondaryStat.SpiritLink,
				SecondaryStat.DojangLuckyBonus, SecondaryStat.SoulGazeCriDamR, SecondaryStat.PowerTransferGauge,
				SecondaryStat.ReturnTeleport, SecondaryStat.ShadowPartner, SecondaryStat.SetBaseDamage,
				SecondaryStat.QuiverCatridge, SecondaryStat.ImmuneBarrier, SecondaryStat.NaviFlying,
				SecondaryStat.Dance, SecondaryStat.DotHealHPPerSecond, SecondaryStat.SetBaseDamageByBuff,
				SecondaryStat.MagnetArea, SecondaryStat.MegaSmasher, SecondaryStat.RwBarrier, SecondaryStat.VampDeath,
				SecondaryStat.RideVehicle, SecondaryStat.RideVehicleExpire, SecondaryStat.Protective,
				SecondaryStat.BlitzShield, SecondaryStat.UnkBuffStat2, SecondaryStat.HolyUnity,
				SecondaryStat.BattlePvP_Rude_Stack };

		for (final SecondaryStat stat : statsToCheck) {
			if (statups.containsKey(stat)) {
				return true;
			}
		}
		return false;
	}

	public boolean isSpecialBuff() {
		switch (this) {
		case UnkBuffStat707:
		case DashSpeed:
		case DashJump:
		case RideVehicle:
		case PartyBooster:
		case GuidedBullet:
		case Undead:
		case RideVehicleExpire:
		case RelikGauge:
		case Grave: {
			return true;
		}
		// $CASES-OMITTED$
		default: {
			return false;
		}
		}
	}

	public int getFlag() {
		return this.flag;
	}

	public void setFlag(final int flag) {
		this.flag = flag;
	}

	public boolean isItemEffect() {
		switch (this) {
		case DropItemRate:
		case ItemUpByItem:
		case MesoUpByItem:
		case ExpBuffRate:
		case WealthOfUnion:
		case LuckOfUnion: {
			return true;
		}
		// $CASES-OMITTED$
		default: {
			return false;
		}
		}
	}

	public boolean SpectorEffect() {
		switch (this) {
		case SpectorGauge:
		case SpectorTransForm:
		case PlainBuff:
		case ScarletBuff:
		case GustBuff:
		case AbyssBuff: {
			return true;
		}
		// $CASES-OMITTED$
		default: {
			return false;
		}
		}
	}

	public int getBuffstat() {
		return this.buffstat;
	}

	public void setBuffstat(final int buffstat) {
		this.buffstat = buffstat;
	}

	public int getFirst() {
		return this.first;
	}

	public void setFirst(final int first) {
		this.first = first;
	}

	public boolean isStacked() {
		return this.stacked;
	}

	public void setStacked(final boolean stacked) {
		this.stacked = stacked;
	}

	private int val;
	private int pos;

	public int getVal() {
		return val;
	}

	public int getPos() {
		return pos;
	}

}
