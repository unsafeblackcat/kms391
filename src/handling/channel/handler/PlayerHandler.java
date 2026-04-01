package handling.channel.handler;

import client.*;
import client.inventory.*;
import client.status.MonsterStatus;
import client.status.MonsterStatusEffect;
import constants.GameConstants;
import constants.ServerConstants;
import constants.programs.ControlUnit;
import database.DatabaseConnection;
import handling.RecvPacketOpcode;
import handling.SendPacketOpcode;
import handling.channel.ChannelServer;
import handling.channel.handler.FishingHandler;
import handling.channel.handler.boss.BossHandler;
import handling.login.LoginInformationProvider;
import handling.world.MapleParty;
import handling.world.MaplePartyCharacter;
import handling.world.World;
import provider.MapleData;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;
import scripting.EventInstanceManager;
import scripting.NPCScriptManager;
import server.Timer;
import server.*;
import server.events.MapleSnowball;
import server.field.boss.will.SpiderWeb;
import server.field.skill.*;
import server.games.BloomingRace;
import server.games.DetectiveGame;
import server.life.*;
import server.maps.ForceAtom;
import server.maps.*;
import server.movement.LifeMovementFragment;
import server.polofritto.FrittoDancing;
import server.polofritto.FrittoEagle;
import server.quest.MapleQuest;
import tools.*;
import tools.data.LittleEndianAccessor;
import tools.data.MaplePacketLittleEndianWriter;
import tools.packet.*;

import java.awt.*;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
import scripting.PortalScriptManager;

public class PlayerHandler {

	public static long connectorCheckLong;

	public static long getConnectorCheckLong() {
		connectorCheckLong++;
		return connectorCheckLong;
	}

	public static long resetConnectorCheckLong() {
		connectorCheckLong = 0L;
		return connectorCheckLong;
	}

	public static boolean isFinisher(int skillid) {
		switch (skillid) {
		case 400011027:
			return true;
		}
		return false;
	}

	public static void randomMonsterAttack(LittleEndianAccessor slea, MapleClient c) {
		int size = slea.readInt();

		for (int i = 0; i < size; i++) {
			int monsterId = slea.readInt();

			MapleMonster monster = c.getPlayer().getMap().getMonsterByOid(monsterId);

			if (monster != null) {
				monster.stigmaStack = 0;
			}
		}
	}

	public static void ChangeSkillMacro(LittleEndianAccessor slea, MapleCharacter chr) {
		int num = slea.readByte();
		for (int i = 0; i < num; i++) {
			String name = slea.readMapleAsciiString();
			int shout = slea.readByte();
			int skill1 = slea.readInt();
			int skill2 = slea.readInt();
			int skill3 = slea.readInt();
			SkillMacro macro = new SkillMacro(skill1, skill2, skill3, name, shout, i);
			chr.updateMacros(i, macro);
		}
	}

	public static final void ChangeKeymap(LittleEndianAccessor slea, MapleCharacter chr) {
		if (slea.available() > 8L && chr != null) {
			int a = slea.readShort(); // 1.2.391 Changed.
			slea.skip(4); // 1.2.391 Changed.
			if (a != 0) {
				return;
			}

			int numChanges = slea.readByte(); // 1.2.391 Changed.
			for (int i = 0; i < numChanges; i++) {
				int key = slea.readByte(); // 1.2.391 Changed.
				byte type = slea.readByte();
				int action = slea.readInt();
				if (type == 1 && action >= 1000) {
					Skill skil = SkillFactory.getSkill(action);
					if (skil != null && ((!skil.isFourthJob() && !skil.isBeginnerSkill() && skil.isInvisible()
							&& chr.getSkillLevel(skil) <= 0) || (action >= 91000000 && action < 100000000))) {
						continue;
					}
				}
				if (action != 26) {
					chr.changeKeybinding(key, type, action);
				}
				continue;
			}
		} else if (chr != null) {
			int type = slea.readByte();
			int data = slea.readInt();
			switch (type) {
			case 1:
				if (data <= 0) {
					chr.getQuest_Map().remove(MapleQuest.getInstance(122221));
					break;
				}
				chr.getQuestNAdd(MapleQuest.getInstance(122221)).setCustomData(String.valueOf(data));
				break;
			case 2:
				if (data <= 0) {
					chr.getQuest_Map().remove(MapleQuest.getInstance(122223));
					break;
				}
				chr.getQuestNAdd(MapleQuest.getInstance(122223)).setCustomData(String.valueOf(data));
				break;
			}
		}
	}

	public static void SelectReincarnation(LittleEndianAccessor slea, MapleClient c) {
		int type = slea.readInt();
		c.getPlayer().setSkillCustomInfo(1321020, type, 0);
		c.getPlayer().addCooldown(1321020, System.currentTimeMillis(), 1000);
		c.send(CField.skillCooldown(1321020, 1000));
		SkillFactory.getSkill(1321020).getEffect(c.getPlayer().getSkillLevel(1321020)).applyTo(c.getPlayer());
	}

	public static void selectSolJanus(LittleEndianAccessor slea, MapleClient c) {
		int skillId = slea.readInt();

		if (skillId == 500001001) { // HEXA 솔 야누스 구현
			for (MapleSummon summon : c.getPlayer().getSummons()) {
				if (summon.getSkill() == 500001002 || summon.getSkill() == 500001003
						|| summon.getSkill() == 500001004) {
					c.getPlayer().removeSummon(summon);
					c.getPlayer().getMap().broadcastMessage(CField.SummonPacket.removeSummon(summon, true));
				}
			}
		}

		c.getPlayer().setKeyValue(21770, "500001000", skillId + "");
		c.getPlayer().setKeyValue(21770, "pr0", 0 + "");
	}

	public static final void UseTitle(LittleEndianAccessor slea, MapleClient c, MapleCharacter chr) {
		if (chr == null || chr.getMap() == null) {
			return;
		}
		int itemId = slea.readInt();
		Item toUse = chr.getInventory(MapleInventoryType.SETUP).getItem((short) slea.readInt());
		if (toUse == null || (itemId != 0 && toUse.getItemId() != itemId)) {
			return;
		}
		if (itemId <= 0) {
			chr.setKeyValue(19019, "id", "0");
			chr.setKeyValue(19019, "date", "0");
		} else {
			chr.setKeyValue(19019, "expired", "0");
			chr.setKeyValue(19019, "id", String.valueOf(itemId));
			chr.setKeyValue(19019, "date", "2079/01/01 00:00:00:000");
			chr.setQuestAdd(MapleQuest.getInstance(7290), (byte) 1, String.valueOf(itemId));
		}
		chr.getMap().broadcastMessage(chr, CField.showTitle(chr.getId(), itemId), false);
		c.getSession().writeAndFlush(CWvsContext.enableActions(chr));
		chr.getStat().recalcLocalStats(chr);
	}

	public static final void UseChair(MapleClient c, MapleCharacter chr, LittleEndianAccessor slea) {
		int itemId = slea.readInt();
		slea.skip(1);
		slea.skip(4);
		Point pos = new Point(slea.readInt(), slea.readInt());
		slea.skip(4);
		if (chr == null || chr.getMap() == null) {
			return;
		}

		/*
		 * if (itemId == 3014028) { //레벨의자 String Special = slea.readMapleAsciiString();
		 * chr.setChairText(Special); return; }
		 */
		if (GameConstants.isTextChair(itemId)) {
			String Special = slea.readMapleAsciiString();
			chr.setChairText(Special);
			return;
		}
		if (itemId == 3015440 || itemId == 3015650 || itemId == 3015651) {
			int maxmeso = slea.readInt();
			chr.getMap().broadcastMessage(SLFCGPacket.MesoChairPacket(chr.getId(), chr.getMesoChairCount(), itemId));
			ScheduledFuture<?> qwer = Timer.ShowTimer.getInstance().register(() -> {
				if (chr != null && chr.getChair() != 0) {
					chr.UpdateMesoChairCount(maxmeso);
				}
			}, 2000L);
			chr.setMesoChairTimer(qwer);
		}
		chr.setChair(itemId);
		if (itemId / 100 == 30162) {
			List<MapleSpecialChair.MapleSpecialChairPlayer> players = new ArrayList<>();
			MapleSpecialChair chair = new MapleSpecialChair(itemId, new Rectangle(pos.x - 142, pos.y - 410, 284, 420),
					pos, chr, players);
			int[] randEmotions = { 2, 10, 14, 17 };
			chair.addPlayer(chr, randEmotions[Randomizer.nextInt(randEmotions.length)]);
			chair.addPlayer(null, -1);
			chair.addPlayer(null, -1);
			chair.addPlayer(null, -1);
			chr.getMap().spawnSpecialChair(chair);
		}
		chr.getMap().broadcastMessage(chr, CField.showChair(chr, itemId), false);
		if (itemId == FishingHandler.FishingChair && chr.getMapId() == FishingHandler.FishingMap) {
			// progress=1;4035000=99

			int quantity = chr.getItemQuantity(4035000, false);

			if (quantity > 0) {
				c.getSession().writeAndFlush(CField.fishing(-1));
				chr.setKeyValue(100393, "4035000", String.valueOf(quantity));
				chr.setKeyValue(100393, "progress", "0");
				c.getSession().writeAndFlush(CField.fishing(0));
			}
		}
		if (chr.getMapId() == ServerConstants.fishMap) {
			chr.lastChairPointTime = System.currentTimeMillis();
			chr.getClient().getSession()
					.writeAndFlush(CField.UIPacket.detailShowInfo("의자에 앉아 있을시 1분마다 5포인트가 자동으로 수급됩니다.", false));
			chr.getClient().getSession().writeAndFlush(SLFCGPacket.playSE("Sound/MiniGame.img/14thTerra/reward"));
		}
		if (chr.getMapId() == ServerConstants.warpMap) {
			chr.getClient().getSession().writeAndFlush(
					SLFCGPacket.BlackLabel("#fn나눔고딕 ExtraBold##fs32##b#e잠수포인트 적립을 시작합니다.", 100, 1000, 4, 0, 0, 1, 4));
			chr.setSkillCustomInfo(chr.getMapId(), 0L, 60000L);
		}
		c.getSession().writeAndFlush(CWvsContext.enableActions(chr));
	}

	public static final void CancelChair(short id, MapleClient c, MapleCharacter chr) {
		if (id == -1) {
			int itemId = chr.getChair();
			if (chr.getMesoChairTimer() != null) {
				chr.getMesoChairTimer().cancel(true);
				chr.setMesoChairTimer((ScheduledFuture<?>) null);
			}
			chr.setChairText((String) null);
			chr.setChair(0);
			if (itemId / 100 == 30162) {
				for (MapleSpecialChair chair : chr.getMap().getAllSpecialChairs()) {
					if (chair.getOwner().getId() == chr.getId()) {
						chair.getPlayers().remove(chr);
						for (MapleSpecialChair.MapleSpecialChairPlayer player : chair.getPlayers()) {
							if (player.getPlayer() != null) {
								MapleCharacter target = c.getChannelServer().getPlayerStorage()
										.getCharacterById(player.getPlayer().getId());
								if (target != null) {
									target.setChair(0);
									target.getMap()
											.broadcastMessage(CField.specialChair(target, false, false, false, chair));
									target.getClient().getSession().writeAndFlush(CField.cancelChair(-1, target));
									if (target.getMap() != null) {
										target.getMap().broadcastMessage(target, CField.showChair(target, 0), false);
									}
								}
							}
						}
						chr.getMap().broadcastMessage(CField.specialChair(chr, false, false, false, chair));
						chr.getMap().removeMapObject(chair);
						break;
					}
					for (MapleSpecialChair.MapleSpecialChairPlayer player : chair.getPlayers()) {
						if (player.getPlayer() != null && player.getPlayer().getId() == chr.getId()) {
							player.setPlayer(null);
							player.setEmotion(-1);
							chr.getMap().broadcastMessage(CField.specialChair(chr, false, false, false, chair));
						}
					}
				}
			}
			c.getSession().writeAndFlush(CField.cancelChair(-1, chr));
			if (chr.getMap() != null) {
				chr.getMap().broadcastMessage(chr, CField.showChair(chr, 0), false);
			}
		} else {
			chr.setChair(id);
			c.getSession().writeAndFlush(CField.cancelChair(id, chr));
		}

		if (chr.getMapId() == FishingHandler.FishingMap) {
			c.getSession().writeAndFlush(CField.fishing(-1));
		}

		if (chr.getMapId() == ServerConstants.fishMap) {
			chr.getClient().getSession()
					.writeAndFlush(SLFCGPacket.OnYellowDlg(3003302, 2500, "#face1#자동 포인트 수급이 종료됐어요!", ""));
			chr.getClient().getSession().writeAndFlush(SLFCGPacket.playSE("Sound/MiniGame.img/Timer"));
		}
		if (chr.getMapId() == ServerConstants.warpMap) {
			chr.getClient().getSession().writeAndFlush(
					SLFCGPacket.BlackLabel("#fn나눔고딕 ExtraBold##fs32##b#e잠수포인트 적립을 그만둡니다.", 100, 1000, 4, 0, 0, 1, 4));
			chr.removeSkillCustomInfo(chr.getMapId());
		}
		c.getSession().writeAndFlush(CWvsContext.enableActions(chr));
	}

	public static final void TrockAddMap(LittleEndianAccessor slea, MapleClient c, MapleCharacter chr) {
		byte addrem = slea.readByte();
		byte vip = slea.readByte();
		if (vip == 1) {
			if (addrem == 0) {
				chr.deleteFromRegRocks(slea.readInt());
			} else if (addrem == 1) {
				if (!FieldLimitType.VipRock.check(chr.getMap().getFieldLimit())) {
					chr.addRegRockMap();
				} else {
					chr.dropMessage(1, "This map is not available to enter for the list.");
				}
			}
		} else if (vip == 2) {
			if (addrem == 0) {
				chr.deleteFromRocks(slea.readInt());
			} else if (addrem == 1) {
				if (!FieldLimitType.VipRock.check(chr.getMap().getFieldLimit())) {
					chr.addRockMap();
				} else {
					chr.dropMessage(1, "This map is not available to enter for the list.");
				}
			}
		} else if (vip == 3) {
			if (addrem == 0) {
				chr.deleteFromHyperRocks(slea.readInt());
			} else if (addrem == 1) {
				if (!FieldLimitType.VipRock.check(chr.getMap().getFieldLimit())) {
					chr.addHyperRockMap();
				} else {
					chr.dropMessage(1, "This map is not available to enter for the list.");
				}
			}
		}
		c.getSession().writeAndFlush(CSPacket.OnMapTransferResult(chr, vip, (addrem == 0)));
	}

	public static final void charInfoRequestMe(MapleClient c) {
		if (c.getPlayer() == null || c.getPlayer().getMap() == null) {
			return;
		}

		if (c.getPlayer() != null) {
			c.getSession().writeAndFlush(CWvsContext.charInfoMe(c.getPlayer()));
			c.getPlayer().setLastCharGuildId(c.getPlayer().getGuildId());
		}
	}

	public static final void charInfoRequestOther(int objectid, String name, MapleClient c) {
		if (c.getPlayer() == null || c.getPlayer().getMap() == null) {
			return;
		}

		MapleCharacter player = c.getPlayer().getMap().getCharacterByName(name);

		if (player != null) {
			c.getSession().writeAndFlush(CWvsContext.charInfoOther(player));
			c.getPlayer().setLastCharGuildId(player.getGuildId());
		}
	}

	public static final void TakeDamage(LittleEndianAccessor slea, MapleClient c, MapleCharacter chr) {
		slea.skip(4);
		slea.skip(4);
		byte type = slea.readByte();
		slea.skip(1);
		slea.skip(1);
		int damage = slea.readInt();
		slea.skip(2);
		boolean isDeadlyAttack = false;
		boolean pPhysical = false;
		int oid = 0;
		int monsteridfrom = 0;
		int fake = 0;
		int mpattack = 0;
		int skillid = 0;
		int pID = 0;
		int pDMG = 0;
		byte direction = 0;
		byte pType = 0;
		Point pPos = new Point(0, 0);
		MapleMonster attacker = null;
		if (chr == null || chr.getMap() == null) {
			c.getSession().writeAndFlush((Object) CWvsContext.enableActions(chr));
			return;
		}
		if (chr.isGM() && chr.isInvincible() || chr.getBuffedValue(1320019)) {
			c.getSession().writeAndFlush((Object) CWvsContext.enableActions(chr));
			return;

		}
		if (c.getPlayer().getBattleGroundChr() != null && c.getPlayer().getMapId() == 921174100) {
			if (c.getPlayer().getBattleGroundChr().getHp() - damage > 0) {
				EnumMap<MapleStat, Long> hpmpupdate = new EnumMap<MapleStat, Long>(MapleStat.class);
				c.getPlayer().getBattleGroundChr().setHp(c.getPlayer().getBattleGroundChr().getHp() - damage);
				hpmpupdate.put(MapleStat.HP, Long.valueOf(c.getPlayer().getBattleGroundChr().getHp()));
				if (hpmpupdate.size() > 0) {
					c.getSession()
							.writeAndFlush((Object) CWvsContext.updatePlayerStats(hpmpupdate, false, c.getPlayer()));
				}
			}
			c.getPlayer().getMap().broadcastMessage(CField.damagePlayer(240, damage, c.getPlayer().getId(), damage));
			return;
		}
		if (type == -5 && c.getPlayer().getBuffedValue(SecondaryStat.DarkSight) != null
				&& !c.getPlayer().getBuffedValue(400001023) && c.getPlayer().getSkillCustomValue0(4001003) < 5L) {
			c.getPlayer().setSkillCustomInfo(4001003, c.getPlayer().getSkillCustomValue0(4001003) + 1L, 0L);
		}
		if (damage > 0 && chr.getBuffedEffect(SecondaryStat.HolyMagicShell) != null) {
			if (chr.getHolyMagicShell() > 1) {
				chr.setHolyMagicShell((byte) (chr.getHolyMagicShell() - 1));
				HashMap<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
				statups.put(SecondaryStat.HolyMagicShell,
						new Pair<Integer, Integer>(Integer.valueOf(chr.getHolyMagicShell()),
								(int) chr.getBuffLimit(chr.getBuffSource(SecondaryStat.HolyMagicShell))));
				chr.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups,
						chr.getBuffedEffect(SecondaryStat.HolyMagicShell), chr));
			} else {
				chr.cancelEffectFromBuffStat(SecondaryStat.HolyMagicShell);
			}
		}
		if (Randomizer.isSuccess(50) && GameConstants.isXenon(chr.getJob())
				&& chr.getBuffedValue(SecondaryStat.AegisSystem) != null && chr.getSkillCustomValue(36110004) == null) {
			Rectangle box;
			MapleAtom atom = new MapleAtom(false, chr.getId(), 5, true, 36110004, chr.getTruePosition().x,
					chr.getTruePosition().y);
			atom.setDwFirstTargetId(oid);
			atom.addForceAtom(new ForceAtom(0, 35, 5, Randomizer.rand(80, 120), (short) Randomizer.rand(0, 500)));
			atom.addForceAtom(new ForceAtom(0, 36, 5, Randomizer.rand(80, 120), (short) Randomizer.rand(0, 500)));
			atom.addForceAtom(new ForceAtom(0, 37, 5, Randomizer.rand(80, 120), (short) Randomizer.rand(0, 500)));
			if (chr.getSummon(400041044) != null
					&& (box = new Rectangle(chr.getSummon((int) 400041044).getTruePosition().x - 320,
							chr.getSummon((int) 400041044).getTruePosition().y - 490, 640, 530))
							.contains(chr.getTruePosition())) {
				for (int i = 0; i < 5; ++i) {
					atom.addForceAtom(
							new ForceAtom(0, 38 + i, 5, Randomizer.rand(80, 120), (short) Randomizer.rand(0, 500)));
				}
			}
			c.getPlayer().getMap().spawnMapleAtom(atom);
			chr.setSkillCustomInfo(36110004, 0L, 1500L);
		}
		chr.checkSpecialCoreSkills("hitCount", 0, null);
		PlayerStats stats = chr.getStat();
		if (type > -2) {
			Skill bx;
			int bof;
			SecondaryStatEffect counterAttack;
			SecondaryStatEffect revenge;
			SecondaryStatEffect bodyOfSteal;
			MobAttackInfo attackInfo;
			oid = slea.readInt();
			slea.skip(24);
			slea.readInt();
			slea.readInt();
			slea.readInt();
			monsteridfrom = slea.readInt();
			slea.readByte();
			slea.readInt();
			slea.readInt();
			slea.readInt();
			attacker = chr.getMap().getMonsterByOid(oid);
			direction = slea.readByte();
			if (attacker == null) {
				chr.addHP(-damage);
				MapleMonster ordi = MapleLifeFactory.getMonster(monsteridfrom);
				MobAttackInfo attackInfo2 = MobAttackInfoFactory.getMobAttackInfo(ordi, 0);
				if (attackInfo2 != null && attackInfo2.getDiseaseSkill() != 0) {
					MobSkillFactory.getMobSkill(attackInfo2.getDiseaseSkill(), attackInfo2.getDiseaseLevel())
							.applyEffect(chr, ordi, false, false);
				}
				chr.getMap().broadcastMessage(chr, CField.damagePlayer(chr.getId(), type, damage, monsteridfrom,
						direction, skillid, pDMG, pPhysical, pID, pType, pPos, (byte) 0, 0, fake), false);
				return;
			}
			if (attacker.getId() != monsteridfrom || attacker.getLinkCID() > 0 || attacker.isFake()
					|| attacker.getStats().isFriendly()) {
				return;
			}
			if (chr.getBuffedValue(400051009) && (double) chr.getStat().getCurrentMaxHp() * 0.9 <= (double) damage) {
				chr.cancelEffectFromBuffStat(SecondaryStat.IndieSummon, 400051009);
				return;
			}
			if (chr.getBuffedValue(400031030)
					&& (attackInfo = MobAttackInfoFactory.getMobAttackInfo(attacker, type)) != null) {
				int damagePercent = attackInfo.getFixDamR();
				int windWall = Math.max(0, chr.getBuffedValue(SecondaryStat.WindWall)
						- damagePercent * chr.getBuffedEffect(SecondaryStat.WindWall).getZ());
				if (windWall > 1) {
					chr.setBuffedValue(SecondaryStat.WindWall, windWall);
					HashMap<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
					statups.put(SecondaryStat.WindWall,
							new Pair<Integer, Integer>(windWall, (int) chr.getBuffLimit(400031030)));
					chr.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups,
							chr.getBuffedEffect(SecondaryStat.WindWall), chr));
				} else {
					chr.cancelEffectFromBuffStat(SecondaryStat.WindWall);
				}
				return;
			}
			if (chr.getBuffedEffect(SecondaryStat.SilhouetteMirage) != null && chr.silhouetteMirage > 0) {
				int damagePercent;
				SecondaryStatEffect eff = chr.getBuffedEffect(SecondaryStat.SilhouetteMirage);
				MobAttackInfo attackInfo3 = MobAttackInfoFactory.getMobAttackInfo(attacker, type);
				if (attackInfo3 != null && ((damagePercent = attackInfo3.getFixDamR()) >= 50
						|| chr.getStat().getHp() - (long) damage <= 0L)) {
					--chr.silhouetteMirage;
					eff.applyTo(chr, false);
					return;
				}
			}
			if (c.getPlayer().getBuffedValue(162120038)) {
				int shiled = (int) chr.getSkillCustomValue0(162120038);
				if (shiled - damage <= 0) {
					damage -= shiled;
					chr.cancelEffect(chr.getBuffedEffect(162120038));
				} else {
					chr.setSkillCustomInfo(162120038, shiled - damage, 0L);
					chr.getBuffedEffect(162120038).applyTo(chr, false, (int) chr.getBuffLimit(162120038));
					damage = 0;
				}
			}
			if (damage > 0 && chr.getSkillLevel(5120011) > 0) {
				SecondaryStatEffect eff = SkillFactory.getSkill(5120011).getEffect(chr.getSkillLevel(5120011));
				if (Randomizer.isSuccess(eff.getProp())) {
					chr.getClient().getSession().writeAndFlush((Object) CField.EffectPacket.showEffect(chr, 0, 5120011,
							4, 0, 0, (byte) (chr.isFacingLeft() ? 1 : 0), true, chr.getPosition(), null, null));
					chr.getMap().broadcastMessage(chr, CField.EffectPacket.showEffect(chr, 0, 5120011, 4, 0, 0,
							(byte) (chr.isFacingLeft() ? 1 : 0), false, chr.getPosition(), null, null), false);
					int shiled = damage * eff.getY() / 100;
					damage -= shiled;
				}
			}
			if (damage > 0 && chr.getSkillLevel(5110010) > 0) {
				SecondaryStatEffect eff = SkillFactory.getSkill(5110010).getEffect(chr.getSkillLevel(5110010));
				int shiled = damage * eff.getDamAbsorbShieldR() / 100;
				damage -= shiled;
			}
			if (chr.getBuffedValue(SecondaryStat.DemonDamageAbsorbShield) != null && damage > 0
					&& chr.getSkillCustomValue0(400001016) > 0L) {
				SecondaryStatEffect effect = SkillFactory.getSkill(400001016).getEffect(chr.getSkillLevel(400001013));
				chr.setSkillCustomInfo(400001016, chr.getSkillCustomValue0(400001016) - 1L, 0L);
				if (chr.getSkillCustomValue0(400001016) <= 0L) {
					chr.cancelEffectFromBuffStat(SecondaryStat.DemonDamageAbsorbShield);
				} else {
					long duration = chr.getBuffLimit(400001016);
					effect.applyTo(chr, (int) duration);
				}
			}
			if (damage > 0 && attacker.getId() == 8870100 && attacker.getCustomValue0(8870100) > 0L) {
				long healhp = attacker.getStats().getHp() / 100L * 5L;
				attacker.heal((int) healhp, 0L, false);
				attacker.getMap().broadcastMessage(MobPacket.HillaDrainActive(attacker.getObjectId()));
				attacker.getMap().broadcastMessage(MobPacket.showBossHP(attacker));
			}
			if (chr.getSpiritGuard() > 0 && damage > 0) {
				chr.setSpiritGuard(chr.getSpiritGuard() - 1);
				if (chr.getSpiritGuard() == 0) {
					chr.cancelEffectFromBuffStat(SecondaryStat.SpiritGuard);
				} else {
					HashMap<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
					statups.put(SecondaryStat.SpiritGuard, new Pair<Integer, Integer>(chr.getSpiritGuard(),
							(int) chr.getBuffLimit(chr.getBuffSource(SecondaryStat.SpiritGuard))));
					chr.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups,
							chr.getBuffedEffect(SecondaryStat.SpiritGuard), chr));
				}
			}
			if (chr.getBuffedValue(50001214)) {
				SkillFactory.getSkill(50001214).getEffect(chr.getSkillLevel(50001214)).applyTo(chr,
						(int) chr.getBuffLimit(50001214));
			}
			if (chr.getBuffedValue(151111005)) {
				if (GameConstants.isAdel(chr.getJob()) && chr.getBuffedOwner(151111005) == chr.getId()) {
					int 실드량 = 0;
					실드량 = damage / 100 * chr.getBuffedEffect(151111005).getY();
					c.send(CField.Novilityshiled(실드량));
				} else {
					int finaldam;
					MapleCharacter leader = chr.getMap().getCharacterById(chr.getBuffedOwner(151111005));
					if (leader != null && leader.getBuffedValue(151111005)
							&& (long) (finaldam = damage / 100 * leader.getBuffedEffect(151111005).getX()) < leader
									.getStat().getHp()) {
						leader.addHP(-finaldam);
						leader.getClient()
								.send(CField.Novilityshiled(damage / 100 * leader.getBuffedEffect(151111005).getY()));
					}
				}
			}
			if (!attacker.getStats().isBoss() && chr.getBuffedValue(142001007) && chr.PPoint > 0) {
				damage = (int) ((double) damage - Math.floor(damage * 60 / 100));
				chr.givePPoint(142001007);
			}
			if (!attacker.getStats().isBoss() && chr.getBuffedValue(SecondaryStat.DamAbsorbShield) != null) {
				int damr = chr.getBuffedEffect(SecondaryStat.DamAbsorbShield).getX();
				if (chr.getSkillLevel(23120046) > 0) {
					damr += SkillFactory.getSkill(23120046).getEffect(1).getX();
				}
				damage = (int) ((double) damage - Math.floor(damage * damr / 100));
			}
			if (chr.getBuffedValue(51111008) && chr.getBuffedOwner(51111008) != chr.getId()) {
				int finaldam;
				int dam = 0;
				MapleCharacter leader = chr.getMap().getCharacterById(chr.getBuffedOwner(51111008));
				if (leader != null && leader.getBuffedValue(51111008) && (dam = damage / 100 * 20) > 0
						&& (long) (finaldam = damage / 100 * leader.getBuffedEffect(51111008).getQ()) < leader.getStat()
								.getHp()
						&& (long) finaldam < leader.getStat().getCurrentMaxHp()
								* (long) leader.getBuffedEffect(51111008).getV()) {
					leader.getClient().send(CField.DamagePlayer2(finaldam));
					damage -= dam;
				}
			}
			if (chr.getBuffedEffect(SecondaryStat.BodyOfSteal) != null
					&& chr.bodyOfSteal < (bodyOfSteal = chr.getBuffedEffect(SecondaryStat.BodyOfSteal)).getY()) {
				++chr.bodyOfSteal;
				HashMap<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
				statups.put(SecondaryStat.BodyOfSteal, new Pair<Integer, Integer>(chr.bodyOfSteal,
						(int) chr.getBuffLimit(chr.getBuffSource(SecondaryStat.BodyOfSteal))));
				chr.getClient().getSession()
						.writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, bodyOfSteal, chr));
			}
			if (chr.getSkillLevel(1320011) > 0 && chr.getBuffedEffect(SecondaryStat.Beholder) != null
					&& (revenge = SkillFactory.getSkill(1320011).getEffect(chr.getTotalSkillLevel(1320011)))
							.makeChanceResult()) {
				chr.getClient().getSession().writeAndFlush(
						(Object) CField.SummonPacket.BeholderRevengeAttack(chr, revenge.getDamage(), oid));
			}

			if (chr.getSkillLevel(400011047) > 0) {
				SecondaryStatEffect darknessaura = SkillFactory.getSkill(400011047)
						.getEffect(chr.getSkillLevel(400011047));
				if (chr.getBuffedValue(400011047) && chr.getSkillCustomValue0(400011047) > 0L) {
					long duration = chr.getBuffLimit(400011047);
					int shield = (int) chr.getSkillCustomValue0(400011048);
					chr.setSkillCustomInfo(400011048, chr.getSkillCustomValue0(400011047) - (long) damage, 0L);
					if (chr.getSkillCustomValue0(400011047) <= 0L) {
						chr.setSkillCustomInfo(400011047, 0L, 0L);
						damage -= shield;
					} else {
						damage = (int) ((long) damage - chr.getSkillCustomValue0(400011047));
					}
					darknessaura.applyTo(chr, false, false);
				}
			}

			if (chr.getBuffedValue(400011127) && chr.getSkillCustomValue0(400011127) > 0L
					&& (long) damage < chr.getStat().getCurrentMaxHp()) {
				long duration = chr.getBuffLimit(SecondaryStat.IndieBarrier, 400011127);
				chr.setSkillCustomInfo(400011127, chr.getSkillCustomValue0(400011127) - (long) damage, 0L);
				damage = chr.getSkillCustomValue0(400011127) < (long) damage
						? (int) ((long) damage - chr.getSkillCustomValue0(400011127))
						: 0;
				if (chr.getSkillCustomValue0(400011127) <= 0L) {
					chr.removeSkillCustomInfo(400011127);
					chr.cancelEffectFromBuffStat(SecondaryStat.IndieBarrier);
				} else {
					chr.getBuffedEffect(400011127).applyTo(chr, false, (int) duration);
				}
			}
			if (chr.getSkillLevel(37000006) > 0) {
				int 실드량 = (int) chr.getSkillCustomValue0(37000006);
				실드량 = chr.getSkillLevel(37120009) > 0 ? (damage /= 2) : damage / 100 * 4;
				if ((long) 실드량 > chr.getStat().getCurrentMaxHp()) {
					실드량 = (int) chr.getStat().getCurrentMaxHp();
				}
				chr.setSkillCustomInfo(37000006, 실드량, 0L);
				if (실드량 != 0) {
					if (chr.getBuffedValue(37000006)) {
						HashMap<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
						statups.put(SecondaryStat.RwBarrier,
								new Pair<Integer, Integer>((int) chr.getSkillCustomValue0(37000006), 0));
						chr.getClient()
								.send(CWvsContext.BuffPacket.giveBuff(statups, chr.getBuffedEffect(37000006), chr));
					} else {
						SkillFactory.getSkill(37000006).getEffect(chr.getSkillLevel(37000006)).applyTo(chr);
					}
				}
			}
			// 디펜스 폼 fix
			if (chr.getSkillLevel(5120011) > 0
					&& (counterAttack = SkillFactory.getSkill(5120011).getEffect(chr.getSkillLevel(5120011)))
							.makeChanceResult()) {
				counterAttack.applyTo(chr, false, 3000);
			}
			if (chr.getSkillLevel(5220012) > 0
					&& (counterAttack = SkillFactory.getSkill(5220012).getEffect(chr.getSkillLevel(5220012)))
							.makeChanceResult()) {
				counterAttack.applyTo(chr, false);
			}
			if (chr.getBuffedEffect(SecondaryStat.Dike) != null && chr.getBuffedEffect(151121011) == null
					&& (long) ((int) (System.currentTimeMillis() % 1000000000L))
							- chr.getBuffedEffect(SecondaryStat.Dike).getStarttime() <= 1000L) {
				damage = 0;
				chr.getClient().getSession().writeAndFlush((Object) CField.EffectPacket.showEffect(chr, 0, 151121004,
						10, 0, 0, (byte) (chr.isFacingLeft() ? 1 : 0), true, chr.getPosition(), null, null));
				chr.getMap().broadcastMessage(chr, CField.EffectPacket.showEffect(chr, 0, 151121004, 10, 0, 0,
						(byte) (chr.isFacingLeft() ? 1 : 0), false, chr.getPosition(), null, null), false);
			}
			if (chr.getBuffedEffect(SecondaryStat.Etherealform) != null) {
				if (GameConstants.isKinesis(chr.getJob())) {
					chr.addHP(-chr.getBuffedEffect(SecondaryStat.Etherealform).getY());
				} else {
					chr.addMP(-chr.getBuffedEffect(SecondaryStat.Etherealform).getX());
				}
				damage = 0;
			}
			if (chr.getBuffedEffect(SecondaryStat.RoyalGuardPrepare) != null) {
				boolean isHexa = chr.getSkillLevel(51141002) > 0;
				c.getSession().writeAndFlush((Object) CField.RoyalGuardDamage());
				if (chr.getSkillLevel(51120003) > 0) {
					SkillFactory.getSkill(51120003).getEffect(chr.getSkillLevel(51120003)).applyTo(chr, false);
				}
				if (chr.getRoyalStack() >= 3 && chr.getSkillLevel(51110009) > 0) {
					if (chr.getRoyalStack() < 5 && chr.getBuffedValue(isHexa ? 51141002 : 51001005)) {
						chr.setRoyalStack((byte) (chr.getRoyalStack() + 1));
					}
					SkillFactory.getSkill(isHexa ? 51141002 : 51001005)
							.getEffect(chr.getSkillLevel(isHexa ? 51141002 : 51001005)).applyTo(chr);
				} else if (chr.getRoyalStack() <= 3 && chr.getSkillLevel(isHexa ? 51141002 : 51001005) > 0) {
					if (chr.getRoyalStack() < 3 && chr.getBuffedValue(isHexa ? 51141002 : 51001005)) {
						chr.setRoyalStack((byte) (chr.getRoyalStack() + 1));
					}
					SkillFactory.getSkill(isHexa ? 51141002 : 51001005)
							.getEffect(chr.getSkillLevel(isHexa ? 51141002 : 51001005)).applyTo(chr);
				}
				chr.cancelEffectFromBuffStat(SecondaryStat.RoyalGuardPrepare);
				if (chr.getParty() != null) {
					for (MaplePartyCharacter chr1 : chr.getParty().getMembers()) {
						MapleCharacter curChar = chr.getClient().getChannelServer().getPlayerStorage()
								.getCharacterById(chr1.getId());
						if (curChar == null || !curChar.getBuffedValue(51111008)) {
							continue;
						}
						curChar.cancelEffectFromBuffStat(SecondaryStat.MichaelSoulLink);
						SkillFactory.getSkill(51111008).getEffect(chr.getSkillLevel(51111008)).applyTo(chr, curChar);
					}
				}
			}
			if (chr.getBuffedValue(400001050) && chr.getSkillCustomValue0(400001050) == 400001053L) {
				SecondaryStatEffect effect6 = SkillFactory.getSkill(400001050).getEffect(chr.getSkillLevel(400001050));
				chr.removeSkillCustomInfo(400001050);
				long duration = chr.getBuffLimit(400001050);
				effect6.applyTo(chr, false, (int) duration);
			}
			if (type != -1 && damage > 0
					&& (attackInfo = MobAttackInfoFactory.getMobAttackInfo(attacker, type)) != null) {
				MobSkill skill = MobSkillFactory.getMobSkill(attackInfo.getDiseaseSkill(),
						attackInfo.getDiseaseLevel());
				if (skill != null && (damage == -1 || damage > 0)) {
					skill.applyEffect(chr, attacker, false, attacker.isFacingLeft());
				}
				// [othello] - 익스트림 보스
				if (attacker.getId() == 0x878178 || attacker.getId() == 8880524) {
					if (chr.getSkillCustomValue0(80002625) == 1L) {
						MobSkillFactory.getMobSkill(249, 1).applyEffect(chr, attacker, false, attacker.isFacingLeft());
					} else if (chr.getSkillCustomValue0(80002625) == 2L) {
						MobSkillFactory.getMobSkill(249, 2).applyEffect(chr, attacker, false, attacker.isFacingLeft());
					}
				}
				if ((attacker.getId() == 8920002 || attacker.getId() == 8920102 && type == 1)
						&& chr.hasDisease(SecondaryStat.FireBomb)) {
					while (chr.hasDisease(SecondaryStat.FireBomb)) {
						chr.cancelDisease(SecondaryStat.FireBomb);
					}
				}
				attacker.setMp(attacker.getMp() - attackInfo.getMpCon());
			}
			skillid = slea.readInt();
			pDMG = slea.readInt();
			byte defType = slea.readByte();
			slea.skip(1);
			if (defType == 1 && (bof = chr.getTotalSkillLevel(bx = SkillFactory.getSkill(31110008))) > 0) {
				SecondaryStatEffect eff = bx.getEffect(bof);
				if (Randomizer.nextInt(100) <= eff.getX()) {
					chr.addHP((long) eff.getY() * chr.getStat().getCurrentMaxHp() / 100L);
					chr.handleForceGain(oid, 31110008);
				}
			}
			if (skillid != 0 || chr.getSkillLevel(14120010) > 0) {
				pPhysical = slea.readByte() > 0;
				pID = slea.readInt();
				pType = slea.readByte();
				slea.skip(4);
				if (slea.available() > 4L) {
					pPos = slea.readPos();
				}
				if (pID != 14120010) {
					attacker.damage(chr, pDMG, true, skillid);
				} else {
					damage -= damage
							* SkillFactory.getSkill(pID).getEffect(chr.getSkillLevel(14120010)).getIgnoreMobDamR()
							/ 100;
				}
				if (skillid == 31101003) {
					attacker.applyStatus(c, MonsterStatus.MS_Stun,
							new MonsterStatusEffect(skillid,
									SkillFactory.getSkill(31101003).getEffect(chr.getSkillLevel(31101003))
											.getSubTime()),
							1, SkillFactory.getSkill(31101003).getEffect(chr.getSkillLevel(31101003)));
				}
			}
		} else if (type == -3) {
			if (chr.getMapId() == 993192600 && !chr.isGM()) {
				c.getPlayer().giveDebuff(SecondaryStat.Stun, MobSkillFactory.getMobSkill(123, 94));
			}
			if (chr.getBuffedEffect(SecondaryStat.RoyalGuardPrepare) != null) {
				boolean isHexa = chr.getSkillLevel(51141002) > 0;
				c.getSession().writeAndFlush((Object) CField.RoyalGuardDamage());
				if (chr.getSkillLevel(51120003) > 0) {
					SkillFactory.getSkill(51120003).getEffect(chr.getSkillLevel(51120003)).applyTo(chr, false);
				}
				if (chr.getRoyalStack() >= 3 && chr.getSkillLevel(51110009) > 0) {
					if (chr.getRoyalStack() < 5) {
						chr.setRoyalStack((byte) (chr.getRoyalStack() + 1));
					}
					SkillFactory.getSkill(isHexa ? 51141002 : 51001005)
							.getEffect(chr.getSkillLevel(isHexa ? 51141002 : 51001005)).applyTo(chr);
				} else if (chr.getRoyalStack() <= 3 && chr.getSkillLevel(isHexa ? 51141002 : 51001005) > 0) {
					if (chr.getRoyalStack() < 3) {
						chr.setRoyalStack((byte) (chr.getRoyalStack() + 1));
					}
					SkillFactory.getSkill(isHexa ? 51141002 : 51001005)
							.getEffect(chr.getSkillLevel(isHexa ? 51141002 : 51001005)).applyTo(chr);
				}
				chr.cancelEffectFromBuffStat(SecondaryStat.RoyalGuardPrepare);
				if (chr.getParty() != null) {
					for (MaplePartyCharacter chr1 : chr.getParty().getMembers()) {
						MapleCharacter curChar = chr.getClient().getChannelServer().getPlayerStorage()
								.getCharacterById(chr1.getId());
						if (curChar == null || !curChar.getBuffedValue(51111008)) {
							continue;
						}
						curChar.cancelEffectFromBuffStat(SecondaryStat.MichaelSoulLink);
						SkillFactory.getSkill(51111008).getEffect(chr.getSkillLevel(51111008)).applyTo(chr, curChar);
					}
				}
			}
		} else if (type == -4 && (c.getPlayer().getMapId() == 350060600 || c.getPlayer().getMapId() == 350060900)) {
			c.getPlayer().giveDebuff(SecondaryStat.Slow, MobSkillFactory.getMobSkill(126, 3));
		}
		if (damage == -1) {
			MapleCharacter leader;
			if (chr.getBuffedValue(400041047)) {
				MapleCharacter leader2 = chr.getMap().getCharacterById(chr.getBuffedOwner(400041047));
				if (leader2 != null) {
					leader2.gainXenonSurplus((short) 1, SkillFactory.getSkill(400041047));
				}
			} else if (chr.getBuffedValue(36121014)
					&& (leader = chr.getMap().getCharacterById(chr.getBuffedOwner(36121014))) != null) {
				leader.gainXenonSurplus((short) 1, SkillFactory.getSkill(36121014));
			}
			if (GameConstants.isNightLord(chr.getJob())) {
				fake = 4120002;
			} else if (GameConstants.isShadower(chr.getJob())) {
				fake = 4220002;
			} else if (GameConstants.isMercedes(chr.getJob())) {
				fake = chr.getSkillLevel(23110004) > 0 ? 23110004 : 23000001;
			} else if (GameConstants.isPhantom(chr.getJob())) {
				fake = 24110004;
			} else if (GameConstants.isNightWalker(chr.getJob())) {
				fake = 14120010;
			} else if (GameConstants.isHoyeong(chr.getJob())) {
				fake = 164101006;
			} else if (GameConstants.isXenon(chr.getJob())) {
				fake = 36120005;
			} else if (GameConstants.isBowMaster(chr.getJob())) {
				SecondaryStatEffect effect;
				fake = 3120007;
				if (chr.getSkillLevel(3110007) > 0
						&& (effect = SkillFactory.getSkill(3110007).getEffect(chr.getSkillLevel(3110007)))
								.makeChanceResult()) {
					chr.getClient().getSession().writeAndFlush((Object) CField.getDotge());
					chr.setSkillCustomInfo(3310005, 0L, 1000L);
				}
			} else if (GameConstants.isMarksMan(chr.getJob())) {
				SecondaryStatEffect effect;
				fake = 3220006;
				if (chr.getSkillLevel(3210007) > 0
						&& (effect = SkillFactory.getSkill(3210007).getEffect(chr.getSkillLevel(3210007)))
								.makeChanceResult()) {
					chr.getClient().getSession().writeAndFlush((Object) CField.getDotge());
					chr.setSkillCustomInfo(3310005, 0L, 1000L);
				}
			} else if (GameConstants.isPathFinder(chr.getJob())) {
				SecondaryStatEffect effect;
				fake = 3320011;
				if (chr.getSkillLevel(3310005) > 0
						&& (effect = SkillFactory.getSkill(3310005).getEffect(chr.getSkillLevel(3310005)))
								.makeChanceResult()) {
					chr.getClient().getSession().writeAndFlush((Object) CField.getDotge());
					chr.setSkillCustomInfo(3310005, 0L, 1000L);
				}
			} else if (GameConstants.isWildHunter(chr.getJob())) {
				fake = 33101005;
				if (chr.getSkillLevel(33110008) > 0) {
					chr.getClient().getSession().writeAndFlush((Object) CField.getDotge());
					chr.setSkillCustomInfo(3310005, 0L, 1000L);
				}
			} else if (type == -1 && chr.getJob() == 122 && attacker != null
					&& chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -10) != null
					&& chr.getTotalSkillLevel(1220006) > 0) {
				SecondaryStatEffect eff = SkillFactory.getSkill(1220006).getEffect(chr.getTotalSkillLevel(1220006));
				attacker.applyStatus(c, MonsterStatus.MS_Stun, new MonsterStatusEffect(1220006, eff.getDuration()), 1,
						eff);
				fake = 1220006;
			}
			if (fake > 0) {
				chr.getMap().broadcastMessage(chr, CField.facialExpression(chr, 2), false);
			}
		} else if (damage < -1) {
			c.getSession().writeAndFlush((Object) CWvsContext.enableActions(chr));
			return;
		}
		if (damage == 0) {
			if (chr.getBuffedEffect(SecondaryStat.BlessingArmor) != null) {
				if (chr.getBuffedValue(SecondaryStat.BlessingArmor) == 1) {
					chr.addCooldown(1210016, System.currentTimeMillis(),
							chr.getBuffedEffect(SecondaryStat.BlessingArmor).getCooldown(chr));
					chr.cancelEffectFromBuffStat(SecondaryStat.BlessingArmor);
					chr.cancelEffectFromBuffStat(SecondaryStat.BlessingArmorIncPad);
				} else {
					chr.setBuffedValue(SecondaryStat.BlessingArmor,
							chr.getBuffedValue(SecondaryStat.BlessingArmor) - 1);
					HashMap<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
					statups.put(SecondaryStat.BlessingArmor,
							new Pair<Integer, Integer>(chr.getBuffedValue(SecondaryStat.BlessingArmor),
									(int) chr.getBuffLimit(chr.getBuffSource(SecondaryStat.BlessingArmor))));
					statups.put(SecondaryStat.BlessingArmorIncPad,
							new Pair<Integer, Integer>(chr.getBuffedValue(SecondaryStat.BlessingArmorIncPad),
									(int) chr.getBuffLimit(chr.getBuffSource(SecondaryStat.BlessingArmorIncPad))));
					chr.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups,
							chr.getBuffedEffect(SecondaryStat.BlessingArmor), chr));
				}
			}
			if (chr.getBuffedEffect(SecondaryStat.HolyMagicShell) != null) {
				if (chr.getHolyMagicShell() > 1) {
					chr.setHolyMagicShell((byte) (chr.getHolyMagicShell() - 1));
					HashMap<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
					statups.put(SecondaryStat.HolyMagicShell,
							new Pair<Integer, Integer>(Integer.valueOf(chr.getHolyMagicShell()),
									(int) chr.getBuffLimit(chr.getBuffSource(SecondaryStat.HolyMagicShell))));
					chr.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups,
							chr.getBuffedEffect(SecondaryStat.HolyMagicShell), chr));
				} else {
					chr.cancelEffectFromBuffStat(SecondaryStat.HolyMagicShell);
				}
			}
			if (chr.getSkillLevel(13110026) > 0) {
				SkillFactory.getSkill(13110026).getEffect(chr.getSkillLevel(13110026)).applyTo(chr, false);
				return;
			}
			if (chr.getBuffedValue(36111003)) {
				if (chr.stackbuff == 1) {
					chr.cancelEffect(chr.getBuffedEffect(SecondaryStat.StackBuff));
				} else {
					chr.getBuffedEffect(SecondaryStat.StackBuff).applyTo(chr, false);
				}
			}
			if (chr.getBuffedValue(SecondaryStat.DarkSight) == null && chr.getMapId() / 10000 != 10904
					&& chr.getMapId() / 10000 != 91013 && chr.getMapId() / 10000 != 91015) {
				int[] skills;
				for (int skill : skills = new int[] { 4210015, 0x421211 }) {
					if (chr.getSkillLevel(skill) <= 0 || chr.getSkillLevel(4001003) <= 0 || !Randomizer
							.isSuccess(SkillFactory.getSkill(skill).getEffect(chr.getSkillLevel(skill)).getX())) {
						continue;
					}
					SkillFactory.getSkill(4001003).getEffect(chr.getSkillLevel(4001003)).applyTo(chr, false);
					break;
				}
			}
			if (chr.getSkillLevel(4330009) > 0 && chr.getSkillCustomValue(4330009) == null) {
				SkillFactory.getSkill(4330009).getEffect(chr.getSkillLevel(4330009)).applyTo(chr, false);
				chr.getClient().getSession().writeAndFlush((Object) CField.getDotge());
				chr.setSkillCustomInfo(4330009, 0L, 5000L);
			}
		}
		if ((chr.getJob() == 2711 || chr.getJob() == 2712) && chr.getSkillLevel(27110007) > 0) {
			Skill skill = SkillFactory.getSkill(27110007);
			int critical = chr.getSkillLevel(skill);
			EnumMap<SecondaryStat, Pair<Integer, Integer>> statups = new EnumMap<SecondaryStat, Pair<Integer, Integer>>(
					SecondaryStat.class);
			if (chr.getStat().getHp() / chr.getStat().getCurrentMaxHp() * 100L < chr.getStat().getMp()
					/ chr.getStat().getCurrentMaxMp(chr) * 100L) {
				statups.put(SecondaryStat.LifeTidal, new Pair<Integer, Integer>(2, 0));
				c.getSession().writeAndFlush(
						(Object) CWvsContext.BuffPacket.giveBuff(statups, skill.getEffect(critical), chr));
			} else if (chr.getStat().getHp() / chr.getStat().getCurrentMaxHp() * 100L > chr.getStat().getMp()
					/ chr.getStat().getCurrentMaxMp(chr) * 100L && critical > 0) {
				statups.put(SecondaryStat.LifeTidal, new Pair<Integer, Integer>(1, 0));
				c.getSession().writeAndFlush(
						(Object) CWvsContext.BuffPacket.giveBuff(statups, skill.getEffect(critical), chr));
			}
		}
		if (pPhysical && skillid == 1201007 && chr.getTotalSkillLevel(1201007) > 0) {
			if ((damage -= pDMG) > 0) {
				SecondaryStatEffect eff = SkillFactory.getSkill(1201007).getEffect(chr.getTotalSkillLevel(1201007));
				long enemyDMG = Math.min((long) (damage * (eff.getY() / 100)), attacker.getMobMaxHp() / 2L);
				if (enemyDMG > (long) pDMG) {
					enemyDMG = pDMG;
				}
				if (enemyDMG > 1000L) {
					enemyDMG = 1000L;
				}
				attacker.damage(chr, enemyDMG, true, 1201007);
			} else {
				damage = 1;
			}
		}
		if (damage > 0) {
			SecondaryStatEffect blessingArmor;
			block262: {
				if (attacker != null) {
					// 检查 getName() 的返回值是否为 null
					String attackerName = attacker.getStats().getName();
					if (attackerName != null && attackerName.equals("윌")) {
						switch (type) {
						case 4: {
							c.getPlayer().setSkillCustomInfo(8880302, 0L, 5000L);
							break;
						}
						case 5: {
							chr.setMoonGauge(Math.max(0, chr.getMoonGauge() - 3));
							chr.getClient().getSession()
									.writeAndFlush((Object) MobPacket.BossWill.addMoonGauge(chr.getMoonGauge()));
						}
						}
					}
					if (attacker.getId() == 8900002 || attacker.getId() == 8900102) {
						MobSkill ms = MobSkillFactory.getMobSkill(120, 1);
						ms.setDuration(4000L);
						c.getPlayer().giveDebuff(SecondaryStat.Seal, ms);
					}
					MapleMonster seren = chr.getMap().getMonsterById(8880602);
					MapleMonster serenDawn = chr.getMap().getMonsterById(8880603);
					switch (attacker.getId()) {
					case 8880600: {
						chr.addSerenGauge(type == 2 ? 150 : (type == 1 ? 100 : 150));
						break;
					}
					case 8880601: {
						chr.addSerenGauge(150);
						break;
					}
					case 8880613: {
						MobSkill ms = MobSkillFactory.getMobSkill(120, 1);
						ms.setDuration(3000L);
						c.getPlayer().giveDebuff(SecondaryStat.Seal, ms);
						break;
					}
					case 8880602: {
						chr.addSerenGauge(1000);
						break;
					}
					case 8880603: {
						chr.addSerenGauge(100);
						if (seren == null || serenDawn == null) {
							seren.gainShield(seren.getStats().getHp() / 100L, seren.getShield() <= 0L, 0);
						}
						serenDawn.getMap().broadcastMessage(
								MobPacket.BossSeren.SerenChangePhase("Mob/8880603.img/info/shield", 2, serenDawn));
						break;
					}
					case 8880604: {
						chr.addSerenGauge(100);
						if (seren == null || serenDawn == null) {
							seren.gainShield(seren.getStats().getHp() / 100L, seren.getShield() <= 0L, 0);
						}
						serenDawn.getMap().broadcastMessage(
								MobPacket.BossSeren.SerenChangePhase("Mob/8880603.img/info/shield", 2, serenDawn));
						break;
					}
					case 8880605:
					case 8880606: {
						if (seren == null || serenDawn == null) {
							seren.gainShield(seren.getStats().getHp() / 100L, seren.getShield() <= 0L, 0);
						}
						serenDawn.getMap().broadcastMessage(
								MobPacket.BossSeren.SerenChangePhase("Mob/8880603.img/info/shield", 2, serenDawn));
						break;
					}
					case 8880607: {
						chr.addSerenGauge(type == 2 ? 200 : (type == 4 ? 200 : 100));
						break;
					}
					case 8880608: {
						chr.addSerenGauge(100);
						break;
					}
					case 8880609: {
						chr.addSerenGauge(type == 2 ? 200 : (type == 4 ? 200 : 100));
					}
					}
					// [othello] - 익스트림 보스
					MapleMonster extremeSeren = chr.getMap().getMonsterById(8880632);
					MapleMonster extremeSerenDawn = chr.getMap().getMonsterById(8880633);
					switch (attacker.getId()) {
					case 8880630: {
						chr.addSerenGauge(type == 2 ? 150 : (type == 1 ? 100 : 150));
						break;
					}
					case 8880631: {
						chr.addSerenGauge(150);
						break;
					}
					case 8880643: {
						MobSkill ms = MobSkillFactory.getMobSkill(120, 1);
						ms.setDuration(3000L);
						c.getPlayer().giveDebuff(SecondaryStat.Seal, ms);
						break;
					}
					case 8880632: {
						chr.addSerenGauge(1000);
						break;
					}
					case 8880633: {
						chr.addSerenGauge(100);
						if (extremeSeren != null && extremeSerenDawn == null) {
							extremeSeren.gainShield(extremeSeren.getStats().getHp() / 100L,
									extremeSeren.getShield() <= 0L, 0);
						}
						if (extremeSerenDawn != null) {
							extremeSerenDawn.getMap().broadcastMessage(MobPacket.BossSeren
									.SerenChangePhase("Mob/8880603.img/info/shield", 2, extremeSerenDawn));
						}
						break;
					}
					case 8880634: {
						chr.addSerenGauge(100);
						if (extremeSeren != null && extremeSerenDawn == null) {
							extremeSeren.gainShield(extremeSeren.getStats().getHp() / 100L,
									extremeSeren.getShield() <= 0L, 0);
						}
						if (extremeSerenDawn != null) {
							extremeSerenDawn.getMap().broadcastMessage(MobPacket.BossSeren
									.SerenChangePhase("Mob/8880603.img/info/shield", 2, extremeSerenDawn));
						}
						break;
					}
					case 8880635:
					case 8880636: {
						if (extremeSeren != null && extremeSerenDawn == null) {
							extremeSeren.gainShield(extremeSeren.getStats().getHp() / 100L,
									extremeSeren.getShield() <= 0L, 0);
						}
						if (extremeSerenDawn != null) {
							extremeSerenDawn.getMap().broadcastMessage(MobPacket.BossSeren
									.SerenChangePhase("Mob/8880603.img/info/shield", 2, extremeSerenDawn));
						}
						break;
					}
					case 8880637: {
						chr.addSerenGauge(type == 2 ? 200 : (type == 4 ? 200 : 100));
						break;
					}
					case 8880638: {
						chr.addSerenGauge(100);
						break;
					}
					case 8880639: {
						chr.addSerenGauge(type == 2 ? 200 : (type == 4 ? 200 : 100));
					}
					}

					for (MapleSummon sum : chr.getMap().getAllSummonsThreadsafe()) {
						MapleCharacter owner;
						if ((owner = sum.getOwner()) == null) {
							continue;
						}
						boolean damaged = false;
						int a;

						try {
							a = (int) ((double) owner.getBuffedEffect(sum.getSkill()).getX() * 0.01) * damage;
						} catch (NullPointerException ex) {
							a = 0;
						}
						int refDmg = a;
						ArrayList<Pair<Integer, List<Long>>> allDamage = new ArrayList<Pair<Integer, List<Long>>>();
						ArrayList<Long> dmg = new ArrayList<Long>();
						dmg.add((long) damage * 13L);
						allDamage.add(new Pair(attacker.getObjectId(), dmg));
						if (sum.getPosition().x - 370 < chr.getPosition().x
								&& sum.getPosition().x + 370 > chr.getPosition().x
								&& sum.getPosition().y - 235 < chr.getPosition().y
								&& sum.getPosition().y + 235 > chr.getPosition().y) {
							if (owner.getId() == chr.getId()) {
								if (attacker.getStats().getHp() / 2L > (long) (damage * 13)) {
									damaged = true;
								}
							} else if (chr.getParty() != null && owner.getParty() != null
									&& chr.getParty().getId() == owner.getParty().getId()
									&& owner.getMapId() == chr.getMapId()
									&& attacker.getStats().getHp() / 2L > (long) (damage * 13)) {
								damaged = true;
							}
						}
						if (!damaged) {
							continue;
						}
						owner.getMap().broadcastMessage(CField.SummonPacket.summonAttack(sum, sum.getSkill(),
								(byte) -120, (byte) 17, allDamage, owner.getLevel(), sum.getPosition(), true));
						attacker.damage(owner, refDmg, true);

					}
				} else if (type == -5) {
					slea.skip(41);
					int objid = slea.readInt();
					for (Obstacle o : chr.getMap().getAllObstacle()) {
						if (o.getObjectId() != objid || !o.isEffect()) {
							continue;
						}
						switch (o.getKey()) {
						case 48:
						case 49:
						case 50:
						case 51:
						case 52: {
							MobSkill ms = MobSkillFactory.getMobSkill(123, 63);
							ms.setDuration(2000L);
							c.getPlayer().giveDebuff(SecondaryStat.Stun, ms);
							break;
						}
						case 65:
						case 84: {
							MobSkill ms = MobSkillFactory.getMobSkill(121, 1);
							ms.setDuration(3000L);
							c.getPlayer().giveDebuff(SecondaryStat.Darkness, ms);
							break;
						}
						case 66:
						case 72:
						case 79: {
							int skilllv = o.getKey() == 72 ? 85 : 1;
							MobSkill ms = MobSkillFactory.getMobSkill(123, skilllv);
							ms.setDuration(3000L);
							c.getPlayer().giveDebuff(SecondaryStat.Stun, ms);
							break;
						}
						case 67: {
							MobSkill ms = MobSkillFactory.getMobSkill(133, 1);
							ms.setDuration(3000L);
							c.getPlayer().giveDebuff(SecondaryStat.Undead, ms);
							break;
						}
						case 73: {
							int skilllv = o.getKey() == 73 ? 26 : 1;
							MobSkill ms = MobSkillFactory.getMobSkill(121, skilllv);
							ms.setDuration(3000L);
							c.getPlayer().giveDebuff(SecondaryStat.Weakness, ms);
							break;
						}
						case 74:
						case 83: {
							int skilllv = o.getKey() == 74 ? 40 : 1;
							MobSkill ms = MobSkillFactory.getMobSkill(120, skilllv);
							ms.setDuration(3000L);
							c.getPlayer().giveDebuff(SecondaryStat.Seal, ms);
							break;
						}
						case 75: {
							MobSkillFactory.getMobSkill(249, 1).applyEffect(chr, attacker, true, true);
							break;
						}
						case 76: {
							MobSkill ms = MobSkillFactory.getMobSkill(132, 1);
							ms.setDuration(3000L);
							c.getPlayer().giveDebuff(SecondaryStat.ReverseInput, ms);
							break;
						}
						default: {
							if (c.getPlayer().isGM()) {
								System.out.println("맵패턴 : " + o.getKey());
								break;
							}
							break block262;
						}
						}
						break;
					}
				}
			}
			if (chr.getBuffedValue(SecondaryStat.Morph) != null) {
				chr.cancelMorphs();
			}
			/*
			 * if (chr.getBuffedValue(SecondaryStat.MesoGuard) != null && (long) damage <=
			 * chr.getMeso() && (long) damage < chr.getStat().getCurrentMaxHp()) {
			 * SecondaryStatEffect eff = chr.getBuffedEffect(SecondaryStat.PickPocket); int
			 * max = SkillFactory.getSkill(4211006).getEffect(chr.getSkillLevel(4211006)).
			 * getBulletCount(); if (chr.getSkillLevel(4220045) > 0) { max +=
			 * SkillFactory.getSkill(4220045).getEffect(chr.getSkillLevel(4220045)).
			 * getBulletCount(); } if (damage *
			 * chr.getBuffedEffect(SecondaryStat.MesoGuard).getX() / 100 > 0) {
			 * chr.gainMeso(-damage * chr.getBuffedEffect(SecondaryStat.MesoGuard).getX() /
			 * 100, false); if (eff != null) { int suc = Randomizer.rand(1, 5); for (int i =
			 * 0; i < suc; ++i) { int 기준; Point pos = new Point(chr.getTruePosition().x,
			 * chr.getTruePosition().y); int delay = 208; int plus = 120 * i; delay += plus;
			 * if (suc % 2 == 0) { 기준 = suc / 2; if (i < 기준) { pos.x -= 18 * (기준 - i); }
			 * else if (i >= 기준) { pos.x += 18 * (i - 기준); } } else { 기준 = suc / 2; if (i <
			 * 기준) { pos.x -= 18 * (기준 - i); } else if (i > 기준) { pos.x += 18 * (i - 기준); }
			 * } if (chr.getPickPocket().size() >= max) { continue; }
			 * chr.getMap().spawnMesoDrop(1, chr.getMap().calcDropPos(pos,
			 * chr.getTruePosition()), chr, chr, false, (byte) 0, delay);
			 * chr.getClient().getSession().writeAndFlush((Object)
			 * CWvsContext.BuffPacket.giveBuff(eff.getStatups(), eff, chr)); } } } }
			 */
			if (chr.getBuffedValue(1210016)) {
				SecondaryStatEffect blessingArmor2 = SkillFactory.getSkill(1210016)
						.getEffect(chr.getSkillLevel(1210016));
				chr.addSkillCustomInfo(1210016, -1L);
				if (chr.getSkillCustomValue0(1210016) > 0L) {
					blessingArmor2.applyTo(chr, (int) chr.getBuffLimit(1210016));
				} else {
					chr.cancelEffect(chr.getBuffedEffect(1210016));
				}
			} else if (!chr.getBuffedValue(1210016) && chr.getSkillLevel(1210016) > 0
					&& (blessingArmor = SkillFactory.getSkill(1210016).getEffect(chr.getSkillLevel(1210016)))
							.makeChanceResult()
					&& chr.getCooldownLimit(1210016) == 0L) {
				chr.setSkillCustomInfo(1210016, blessingArmor.getX(), 0L);
				chr.addCooldown(1210016, System.currentTimeMillis(), blessingArmor.getCooldown(chr));
				blessingArmor.applyTo(chr, false);
			}
			if (chr.getBuffedValue(400011011)) {
				MapleCharacter buffowner = chr.getMap().getCharacter(chr.getBuffedOwner(400011011));
				SecondaryStatEffect eff = buffowner.getBuffedEffect(SecondaryStat.RhoAias);
				if (buffowner.getRhoAias() > 1) {
					buffowner.setRhoAias(buffowner.getRhoAias() - 1);
					HashMap<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
					statups.put(SecondaryStat.RhoAias,
							new Pair<Integer, Integer>(eff.getX(), (int) buffowner.getBuffLimit(400011011)));
					buffowner.getClient().getSession()
							.writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, eff, buffowner));
					buffowner.getMap().broadcastMessage(buffowner,
							CWvsContext.BuffPacket.giveForeignBuff(buffowner, statups, eff), false);
				} else {
					chr.cancelEffectFromBuffStat(SecondaryStat.RhoAias);
				}
			} else if (chr.getBuffedValue(SecondaryStat.SiphonVitality) != null) {
				chr.setSkillCustomInfo(14120011, chr.getSkillCustomValue0(14120011) - (long) damage, 0L);
				if (chr.getSkillCustomValue0(14120011) <= 0L) {
					chr.setSkillCustomInfo(14120011, 0L, 0L);
					chr.cancelEffectFromBuffStat(SecondaryStat.Protective);
				} else {
					SkillFactory.getSkill(14120011).getEffect(chr.getSkillLevel(14120009)).applyTo(chr,
							(int) chr.getBuffLimit(14120011));
				}
			}
			if (chr.getBuffedValue(162001005)) {
				chr.addMP(-40L);
				chr.getClient().getSession().writeAndFlush((Object) CField.EffectPacket.showEffect(chr, 0, 162001005,
						10, 0, 0, (byte) (chr.isFacingLeft() ? 1 : 0), true, chr.getTruePosition(), null, null));
				chr.getMap().broadcastMessage(chr, CField.EffectPacket.showEffect(chr, 0, 162001005, 10, 0, 0,
						(byte) (chr.isFacingLeft() ? 1 : 0), false, chr.getTruePosition(), null, null), false);
			}
			boolean damaged = false;
			if (type == -1 || type == 0 || type == 1) { // 매직가드 미적용 mobattack - 물리 마법
				/*
				 * if (chr.getBuffedValue(SecondaryStat.BlessOfDarkness) != null) { attacker =
				 * (MapleMonster) chr.getMap().getMapObject(oid, MapleMapObjectType.MONSTER); if
				 * (attacker != null && chr.getBlessofDarkness() > 0) {
				 * chr.setBlessofDarkness((byte) (chr.getBlessofDarkness() - 1));
				 * HashMap<SecondaryStat, Pair<Integer, Integer>> statups = new
				 * HashMap<SecondaryStat, Pair<Integer, Integer>>();
				 * chr.getClient().send(CWvsContext.BuffPacket.giveBuff(statups,
				 * SkillFactory.getSkill(27101003).getEffect(chr.getSkillLevel(27101003)),
				 * chr)); chr.getMap().broadcastMessage(chr,
				 * CWvsContext.BuffPacket.giveForeignBuff(chr, statups,
				 * SkillFactory.getSkill(27101003).getEffect(chr.getSkillLevel(27101003))),
				 * false); } } else
				 */

				if (chr.getBuffedValue(SecondaryStat.MagicGaurd) != null || chr.getSkillLevel(27000003) > 0
						|| chr.getSkillLevel(12000024) > 0) {
					int hploss = 0;
					int mploss = 0;
					if (isDeadlyAttack) {
						if (stats.getHp() > 1L) {
							hploss = (int) (stats.getHp() - 1L);
						}
						if (stats.getMp() > 1L) {
							mploss = (int) (stats.getMp() - 1L);
						}
						if (chr.getBuffedValue(SecondaryStat.Infinity) != null) {
							mploss = 0;
						}
						chr.addMPHP(-hploss, -mploss);
					} else {
						if (chr.getSkillLevel(27000003) > 0) {
							Skill skill = SkillFactory.getSkill(27000003);
							SecondaryStatEffect eff = skill.getEffect(chr.getSkillLevel(skill));
							mploss = (int) ((double) damage * ((double) eff.getX() / 100.0)) + mpattack;
						} else if (chr.getBuffedEffect(SecondaryStat.MagicGaurd) != null) {
							mploss = (int) ((double) damage
									* (chr.getBuffedValue(SecondaryStat.MagicGaurd).doubleValue() / 100.0)) + mpattack;
						}
						hploss = damage - mploss;
						if (chr.getBuffedValue(SecondaryStat.Infinity) != null) {
							mploss = 0;
						} else if (chr.getSkillLevel(12000024) > 0) {
							SecondaryStatEffect eff = SkillFactory.getSkill(12000024)
									.getEffect(chr.getSkillLevel(12000024));
							mploss = (int) ((double) damage * ((double) eff.getX() / 100.0));
							hploss = damage - mploss + mpattack;
						} else if ((long) mploss > stats.getMp()) {
							mploss = (int) stats.getMp();
							hploss = damage - mploss + mpattack;
						}
						chr.addMPHP(-hploss, -mploss);
					}
					damaged = true;
				} else if (chr.getBuffedEffect(SecondaryStat.PowerTransferGauge) != null) {
					damaged = true;
					if ((long) damage < chr.getStat().getCurrentMaxHp()) {
						if (chr.getBarrier() < damage) {
							chr.addHP(-(damage -= chr.getBarrier()));
							chr.setBarrier(0);
							chr.cancelEffectFromBuffStat(SecondaryStat.PowerTransferGauge);
						} else {
							chr.setBarrier(chr.getBarrier() - damage);
							chr.getBuffedEffect(SecondaryStat.PowerTransferGauge).applyTo(chr, false,
									(int) chr.getBuffLimit(chr.getBuffSource(SecondaryStat.PowerTransferGauge)));
						}
					}

				} else if (chr.getSkillLevel(3210013) > 0) {
					// System.err.println("a데미지리버싱");
					damaged = true;
					Skill skill = SkillFactory.getSkill(3210013);
					SecondaryStatEffect eff = skill.getEffect(chr.getSkillLevel(skill));
					if ((long) damage < chr.getStat().getCurrentMaxHp()) {
						if (chr.getBarrier() < damage) {
							chr.addHP(-(damage -= chr.getBarrier()));
							chr.setBarrier(0);
							chr.cancelEffectFromBuffStat(SecondaryStat.DamAbsorbShield);
						} else {
							chr.setBarrier(chr.getBarrier() - damage);
							chr.getBuffedEffect(SecondaryStat.DamAbsorbShield).applyTo(chr, false,
									(int) chr.getBuffLimit(chr.getBuffSource(SecondaryStat.DamAbsorbShield)));
						}
						chr.getClient().getSession()
								.writeAndFlush((Object) CField.EffectPacket.showEffect(chr, 0, 3210013, 10, 0, 0,
										(byte) (chr.isFacingLeft() ? 1 : 0), true, chr.getPosition(), null, null));
						chr.getMap().broadcastMessage(chr,
								CField.EffectPacket.showEffect(chr, 0, 3210013, 10, 0, 0,
										(byte) (chr.isFacingLeft() ? 1 : 0), false, chr.getPosition(), null, null),
								false);

					}
				}

				if (chr.getBuffedValue(152000009) && chr.blessMark > 0) {
					damage = damage * 30 / 100;
					--chr.blessMark;
					if (chr.blessMark <= 0) {
						chr.cancelEffect(chr.getBuffedEffect(152000009));
					} else {
						chr.getBuffedEffect(152000009).applyTo(chr, false);
					}
					chr.getClient().getSession()
							.writeAndFlush((Object) CField.EffectPacket.showEffect(chr, 0, 152100011, 10, 0, 0,
									(byte) (chr.isFacingLeft() ? 1 : 0), true, chr.getPosition(), null, null));
					chr.getMap().broadcastMessage(chr, CField.EffectPacket.showEffect(chr, 0, 152100011, 10, 0, 0,
							(byte) (chr.isFacingLeft() ? 1 : 0), false, chr.getPosition(), null, null), false);
				}
			}
			if (!damaged) {
				if (isDeadlyAttack) {
					chr.addMPHP(stats.getHp() > 1L ? -(stats.getHp() - 1L) : 0L,
							stats.getMp() > 1L ? -(stats.getMp() - 1L) : 0L);
				} else {
					chr.addMPHP(-damage, -mpattack);
				}
				if (chr.getBuffedValue(80001479)) {
					chr.cancelEffectFromBuffStat(SecondaryStat.IndiePadR, 80001479);
					chr.cancelEffectFromBuffStat(SecondaryStat.IndieMadR, 80001479);
				}
			} else if (chr.getBuffedValue(80001479)) {
				chr.cancelEffectFromBuffStat(SecondaryStat.IndiePadR, 80001479);
				chr.cancelEffectFromBuffStat(SecondaryStat.IndieMadR, 80001479);
			}
		}
		byte offset = 0;
		int offset_d = 0;
		if (slea.available() == 1L) {
			offset = slea.readByte();
			if (offset == 1 && slea.available() >= 4L) {
				offset_d = slea.readInt();
			}
			if (offset < 0 || offset > 2) {
				offset = 0;
			}
		}
		chr.getMap().broadcastMessage(chr, CField.damagePlayer(chr.getId(), type, damage, monsteridfrom, direction,
				skillid, pDMG, pPhysical, pID, pType, pPos, offset, offset_d, fake), false);
	}

	/**
	 * 处理战神职业（Aran）连击数的方法
	 * 
	 * @param c       MapleClient 对象，代表客户端连接
	 * @param chr     MapleCharacter 对象，代表玩家角色
	 * @param skillid 技能的 ID
	 */
	public static final void AranCombo(MapleClient c, MapleCharacter chr, int skillid) {
		// 检查玩家角色是否存在，玩家职业是否为战神系列（2000 - 2112），且玩家没有激活技能 21111030 的 buff
		if (chr != null && chr.getJob() >= 2000 && chr.getJob() <= 2112 && !chr.getBuffedValue(21111030)) {
			// 根据技能 ID 和玩家技能等级获取技能效果
			SecondaryStatEffect skill = SkillFactory.getSkill(skillid).getEffect(chr.getSkillLevel(skillid));
			// 从技能效果中获取本次攻击应增加的连击数
			int toAdd = skill.getAttackCount();
			// 获取玩家当前的连击数
			short combo = chr.getCombo();
			// 获取当前时间戳
			long curr = System.currentTimeMillis();
			// 计算当前连击数对应的能力等级（每 50 连击为一个等级）
			int ability = combo / 50;
			// 增加连击数，并确保连击数不超过 30000
			combo = (short) Math.min(30000, combo + toAdd);
			// 更新玩家最后一次连击的时间
			chr.setLastCombo(curr);
			// 如果连击数达到或超过 1000，将连击数限制为 1000
			if (combo >= 1000) {
				combo = 1000;
			}
			// 更新玩家的连击数
			chr.setCombo(combo);
			// 向客户端发送更新后的连击数信息
			c.getSession().writeAndFlush(CField.aranCombo(combo));
			// 检查玩家是否学习了技能 21000000，并且连击数对应的能力等级发生了变化
			if (chr.getSkillLevel(21000000) > 0 && ability != combo / 50) {
				// 应用技能 21000000 的效果到玩家身上
				SkillFactory.getSkill(21000000).getEffect(chr.getSkillLevel(21000000)).applyTo(chr, false);
			}
			// 如果连击数达到或超过 1000
			if (combo >= 1000) {
				// 获取技能 21111030
				Skill ad = SkillFactory.getSkill(21111030);
				// 获取技能 21111030 的效果
				SecondaryStatEffect effect = ad.getEffect(1);
				// 应用技能 21111030 的效果到玩家身上，参数 true 可能表示某种特殊应用方式
				effect.applyTo(chr, true);
			}
		}
	}

	public static void AndroidEar(MapleClient c, LittleEndianAccessor slea) {
		MapleAndroid android = c.getPlayer().getAndroid();
		if (android == null) {
			c.getPlayer().dropMessage(1, "發生未知錯誤.");
			c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
			return;
		}
		short slot = slea.readShort();
		Item item = c.getPlayer().getInventory(MapleInventoryType.USE).getItem(slot);
		if (item != null && item.getItemId() == 2892000) {
			android.setEar(!android.getEar());
			c.getPlayer().updateAndroid();
			MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, true);
			c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
		} else {
			c.getPlayer().dropMessage(1, "發生未知錯誤.");
			c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
		}
	}

	public static final void LossAranCombo(MapleClient c, MapleCharacter chr, int toAdd) {
		if (chr != null && chr.getJob() >= 2000 && chr.getJob() <= 2112) {
			short combo = chr.getCombo();
			long curr = System.currentTimeMillis();
			if (combo <= 0) {
				combo = 0;
			}
			combo = (short) Math.min(30000, combo - toAdd);
			chr.setLastCombo(curr);
			chr.setCombo(combo);
			SkillFactory.getSkill(21000000).getEffect(chr.getSkillLevel(21000000)).applyTo(chr, false);
			c.getSession().writeAndFlush(CField.aranCombo(combo));
		}
	}

	public static final void BossWarp(LittleEndianAccessor slea, MapleCharacter chr) {
		slea.skip(8);
		int mapid = slea.readInt();
		MapleMap target = chr.getClient().getChannelServer().getMapFactory().getMap(mapid);
		int size = 0;
		if (chr.getParty() != null) {
			MapleCharacter curChar;
			for (MaplePartyCharacter chrz : chr.getParty().getMembers()) {
				curChar = chr.getClient().getChannelServer().getPlayerStorage().getCharacterById(chrz.getId());
				if (curChar == null || curChar.getMapId() != chr.getMapId()
						|| curChar.getClient().getChannel() != chr.getClient().getChannel()) {
					continue;
				}
				++size;
			}
			if (size == chr.getParty().getMembers().size()) {
				for (MaplePartyCharacter chrz : chr.getParty().getMembers()) {
					curChar = chr.getClient().getChannelServer().getPlayerStorage().getCharacterById(chrz.getId());
					if (curChar == null || curChar.getClient().getChannel() != chr.getClient().getChannel()) {
						continue;
					}
					curChar.getPlayer().setKeyValue(210406, "Return_BossMap", curChar.getMapId() + "");
					curChar.changeMap(target, target.getPortal(0));
				}
			} else {
				chr.dropMessage(5, "모든 파티원과 같은 곳에 있어야 이동이 가능 합니다.");
			}
		} else {
			chr.getPlayer().setKeyValue(210406, "Return_BossMap", chr.getMapId() + "");
			chr.changeMap(target, target.getPortal(0));
		}
		chr.getClient().getSession().writeAndFlush((Object) CWvsContext.enableActions(chr));
	}

	public static final void BossMatching(LittleEndianAccessor slea, MapleCharacter chr) {
		int type = slea.readInt();
		int semitype = slea.readInt();
		int mapid = -1;
		switch (type) {
		case 0:
			mapid = 105100100;
			break;
		case 1:
			mapid = 211042300;
			break;
		case 3:
			mapid = 401060000;
			break;
		case 4:
			mapid = 262000000;
			break;
		case 5:
			mapid = 221030900;
			break;
		case 6:
			mapid = 220080000;
			break;
		case 7:
			mapid = 105200000;
			break;
		case 8:
			mapid = 105200000;
			break;
		case 9:
			mapid = 105200000;
			break;
		case 10:
			mapid = 105200000;
			break;
		case 11:
			mapid = 211070000;
			break;
		case 12:
			mapid = 240040700;
			break;
		case 13:
			mapid = 272000000;
			break;
		case 14:
			mapid = 270040000;
			break;
		case 15:
			mapid = 271041000;
			break;
		case 16:
			mapid = 350060300;
			break;
		case 17:
			mapid = 105300303;
			break;
		case 18:
			mapid = 450004000;
			break;
		case 19:
			mapid = 450007240;
			break;
		case 20:
			mapid = 450009301;
			break;
		case 21:
			mapid = 450011990;
			break;
		case 22:
			mapid = 450012200;
			break;
		case 23:
			mapid = 450012500;
			break;
		default:
			System.out.println("해당 보스와 연결된 맵이 없습니다. type : 0x" + Integer.toHexString(type).toUpperCase() + "");
			break;
		}
		if (mapid != -1) {
			MapleMap target = chr.getClient().getChannelServer().getMapFactory().getMap(mapid);
			if (chr.getParty() != null) {
				for (MaplePartyCharacter chrz : chr.getParty().getMembers()) {
					MapleCharacter curChar = chr.getClient().getChannelServer().getPlayerStorage()
							.getCharacterById(chrz.getId());
					if (curChar != null && (curChar.getMapId() == chr.getMapId()
							|| curChar.getEventInstance() == chr.getEventInstance())) {
						curChar.getPlayer().setKeyValue(210406, "Return_BossMap", curChar.getMapId() + "");
						curChar.changeMap(target, target.getPortal(0));
					}
				}
			} else {
				chr.getPlayer().setKeyValue(210406, "Return_BossMap", chr.getMapId() + "");
				chr.changeMap(target, target.getPortal(0));
			}
		}
		chr.getClient().getSession().writeAndFlush(CField.UIPacket.closeUI(7));
		chr.getClient().getSession().writeAndFlush(CWvsContext.enableActions(chr));
	}

	public static final void BlessOfDarkness(MapleCharacter chr) {
		if (chr.getBlessofDarkness() < 3) {
			chr.setBlessofDarkness((byte) (chr.getBlessofDarkness() + 1));
		} else {
			chr.setBlessofDarkness((byte) 3);
		}
		if (chr.getSkillLevel(27101003) > 0) {
			Map<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<>();
			// statups.put(SecondaryStat.BlessOfDarkness, new Pair<>(Integer.valueOf(1),
			// Integer.valueOf(0)));
			chr.getClient().send(CWvsContext.BuffPacket.giveBuff(statups,
					SkillFactory.getSkill(27101003).getEffect(chr.getSkillLevel(27101003)), chr));
			chr.getMap().broadcastMessage(chr, CWvsContext.BuffPacket.giveForeignBuff(chr, statups,
					SkillFactory.getSkill(27101003).getEffect(chr.getSkillLevel(27101003))), false);
			chr.getClient().getSession().writeAndFlush(CField.EffectPacket.showEffect(chr, 0, 27101003, 1, 0, 0,
					(byte) (chr.isFacingLeft() ? 1 : 0), true, chr.getPosition(), null, null));
			chr.getMap().broadcastMessage(chr, CField.EffectPacket.showEffect(chr, 0, 27101003, 1, 0, 0,
					(byte) (chr.isFacingLeft() ? 1 : 0), false, chr.getPosition(), null, null), false);
			chr.getStat().recalcLocalStats(chr);
		}
	}

	public static final void UseItemEffect(int itemId, MapleClient c, MapleCharacter chr) {
		chr.setKeyValue(27038, "itemid", itemId + "");
		Item toUse = chr.getInventory(MapleInventoryType.CASH).findById(itemId);
		if (toUse == null || toUse.getItemId() != itemId || toUse.getQuantity() < 1) {
			c.getSession().writeAndFlush(CWvsContext.enableActions(chr));
			return;
		}
		if (itemId != 5510000) {
			chr.setItemEffect(itemId);
		}
		chr.getMap().broadcastMessage(chr, CField.itemEffect(chr.getId(), itemId), false);
	}

	public static final void CancelItemEffect(int id, MapleCharacter chr) {
		chr.cancelEffect(MapleItemInformationProvider.getInstance().getItemEffect(-id));
	}

	public static final void CancelBuffHandler(LittleEndianAccessor slea, MapleCharacter chr) {
		int sourceid = slea.readInt();
		if (sourceid == 13101022) {
			sourceid = chr.getBuffedEffect(SecondaryStat.TryflingWarm) != null
					? chr.getBuffedEffect(SecondaryStat.TryflingWarm).getSourceId()
					: sourceid;
		}
		if (sourceid == 1241502) {
			if (chr.getPoisonStack() > 5) {
				chr.setPoisonStack((byte) (chr.getPoisonStack() - 5));

				SecondaryStatEffect drain = SkillFactory.getSkill(2100009).getEffect(1);
				HashMap<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
				statups.put(SecondaryStat.DotBasedBuff,
						new Pair<Integer, Integer>(Integer.valueOf(chr.getPoisonStack()), 0));
				drain.setDuration(0);
				chr.getClient().getSession().writeAndFlush(CWvsContext.BuffPacket.giveBuff(statups, drain, chr));
			}
		}

		ArrayList<SecondaryStat> stats = new ArrayList<SecondaryStat>();
		if (chr == null || chr.getMap() == null || SkillFactory.getSkill(sourceid) == null || sourceid == 400011091
				|| sourceid == 80003059) {
			return;
		}
		int level = chr.getSkillLevel(GameConstants.getLinkedSkill(sourceid));
		SecondaryStatEffect effect = SkillFactory.getSkill(sourceid).getEffect(level);
		if (effect.getSourceId() == 1221054) {
			int time = (int) (chr.getBuffLimit(1221054) / (long) (effect.getY() * 1000));
			int reduce = time * (effect.getX() * 1000);
			chr.changeCooldown(1221054, -reduce);
			MapleCharacter holyunitychr = chr.getMap().getCharacter((int) chr.getSkillCustomValue0(400011003));
			if (holyunitychr != null) {
				while (holyunitychr.getBuffedValue(1221054)) {
					holyunitychr.cancelEffect(holyunitychr.getBuffedEffect(1221054));
				}
			}
		}
		if (effect.getSourceId() == 3321042) {
			chr.cancelEffectFromBuffStat(SecondaryStat.CurseEnchant, 3321042);
		}

		if (effect.getSourceId() == 400051334) {
			chr.cancelEffectFromBuffStat(SecondaryStat.IndieNotDamaged, 400051334);
		} else {
			chr.cancelEffect(effect, stats);
		}
		chr.getMap().broadcastMessage(chr, CField.skillCancel(chr, sourceid), false);
		if (SkillFactory.getSkill(sourceid).isChargeSkill()) {
			chr.setKeyDownSkill_Time(System.currentTimeMillis());
		}
		if (effect.getCooldown(chr) > 0 && !chr.skillisCooling(sourceid)
				&& GameConstants.isAfterCooltimeSkill(sourceid)) {
			chr.getClient().getSession()
					.writeAndFlush((Object) CField.skillCooldown(sourceid, effect.getCooldown(chr)));
			chr.addCooldown(sourceid, System.currentTimeMillis(), effect.getCooldown(chr));
		}
		if (sourceid == 400041009) {
			SkillFactory.getSkill(400041009).getEffect(level).applyTo(chr, false, true);
			SkillFactory.getSkill(Randomizer.rand(400041011, 400041015)).getEffect(level).applyTo(chr, false, true);
		} else if (sourceid == 20031205) {
			int cool = (int) (1500L * chr.getSkillCustomValue0(20031205));
			chr.getClient().getSession().writeAndFlush((Object) CField.skillCooldown(sourceid, cool));
			chr.addCooldown(sourceid, System.currentTimeMillis(), cool);
			chr.removeSkillCustomInfo(sourceid);
		} else if (sourceid == 25111005) {
			chr.getClient().getSession().writeAndFlush((Object) CField.skillCooldown(25111012, 10000));
			chr.addCooldown(25111012, System.currentTimeMillis(), 10000L);
		} else if (sourceid == 25141001) { // 오리진 호신강림 구현
			chr.getClient().getSession().writeAndFlush((Object) CField.skillCooldown(25111005, 10000));
			chr.addCooldown(25111005, System.currentTimeMillis(), 10000L);
		}
	}

	public static final void NameChanger(boolean isspcheck, LittleEndianAccessor slea, MapleClient c) {
		if (isspcheck) {
			String secondPassword = slea.readMapleAsciiString();
			if (c.CheckSecondPassword(secondPassword) || c.SecondPwUse == 1) {
				c.getSession().writeAndFlush(CField.NameChanger((byte) 9, 4034803));
			} else {
				c.getSession().writeAndFlush(CField.NameChanger((byte) 10));
			}
		} else {
			int chrid = slea.readInt();
			byte status = slea.readByte();
			int itemuse = slea.readInt();
			String oriname = slea.readMapleAsciiString();
			String newname = slea.readMapleAsciiString();
			if (c.getPlayer().getId() != chrid) {
				c.getSession().writeAndFlush(CField.NameChanger((byte) 2));
				return;
			}
			if (itemuse != 4034803) {
				c.getSession().writeAndFlush(CField.NameChanger((byte) 2));
				return;
			}
			if (status != 1) {
				c.getSession().writeAndFlush(CField.NameChanger((byte) 2));
				return;
			}
			if (!c.getPlayer().getName().equals(oriname)) {
				c.getSession().writeAndFlush(CField.NameChanger((byte) 2));
				return;
			}
			if (c.getPlayer().haveItem(4034803, 1)) {
				if (MapleCharacterUtil.canCreateChar(newname)) {
					if (MapleCharacterUtil.isEligibleCharNameTwo(newname, c.getPlayer().isGM())
							&& !LoginInformationProvider.getInstance().isForbiddenName(newname)) {
						if (MapleCharacterUtil.getIdByName(newname) == -1) {
							MapleInventoryManipulator.removeById(c, MapleInventoryType.ETC, 4034803, 1, false, false);
							c.getChannelServer().getPlayerStorage().deregisterPlayer(c.getPlayer());
							ControlUnit.동접제거(c.getPlayer().getName());
							c.getPlayer().setName(newname);
							ControlUnit.동접추가(c.getPlayer().getName());
							c.getChannelServer().getPlayerStorage().registerPlayer(c.getPlayer());
							MapleCharacter.saveNameChange(newname, c.getPlayer().getId());
							for (MapleUnion union : c.getPlayer().getUnions().getUnions()) {
								if (union.getCharid() == c.getPlayer().getId()) {
									union.setName(newname);
								}
							}
							c.getSession().writeAndFlush(CField.NameChanger((byte) 0));
							c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
						} else {
							c.getSession().writeAndFlush(CField.NameChanger((byte) 7));
						}
					} else {
						c.getSession().writeAndFlush(CField.NameChanger((byte) 6));
					}
				} else {
					c.getSession().writeAndFlush(CField.NameChanger((byte) 2));
				}
			} else {
				c.getSession().writeAndFlush(CField.NameChanger((byte) 3));
			}
		}
	}

	public static final void CancelMech(LittleEndianAccessor slea, MapleCharacter chr) {
		if (chr == null) {
			return;
		}
		int sourceid = slea.readInt();
		int level = slea.readInt();
		if (sourceid % 10000 < 1000 && SkillFactory.getSkill(sourceid) == null) {
			sourceid += 1000;
		}
		Skill skill = SkillFactory.getSkill(sourceid);
		if (skill == null) {
			return;
		}
		if (skill.isChargeSkill()) {
			chr.setKeyDownSkill_Time(0L);
			chr.getMap().broadcastMessage(chr, CField.skillCancel(chr, sourceid), false);
		} else {
			chr.cancelEffect(skill.getEffect(level));
		}
		if (skill.getEffect(level).getCooldown(chr) > 0) {
			chr.getClient().getSession()
					.writeAndFlush(CField.skillCooldown(sourceid, skill.getEffect(level).getCooldown(chr)));
			chr.addCooldown(sourceid, System.currentTimeMillis(), skill.getEffect(level).getCooldown(chr));
		}
	}

	public static final void SkillEffect(LittleEndianAccessor slea, MapleCharacter chr) {
		int skillId = slea.readInt();
		int level = slea.readInt();
		slea.skip(4);
		int a = slea.readByte();
		if (a == 1) {
			slea.skip(8);
		}
		short display = slea.readShort();
		byte unk = slea.readByte();
		if (display == -1 && unk == -1) {
			slea.skip(1);
			display = slea.readShort();
			unk = slea.readByte();
		}
		chr.dropMessageGM(6, "SkillEffect SkillId : " + skillId);
		Skill skill = SkillFactory.getSkill(GameConstants.getLinkedSkill(skillId));
		if (chr == null || skill == null || chr.getMap() == null) {
			return;
		}
		if (GameConstants.isCain(chr.getJob())) {
			chr.handleRemainIncense(skillId, true);
		} else if (GameConstants.isZero(chr.getJob()) && skillId == 101110102) {
			chr.ZeroSkillCooldown(skillId);
		}
		int skilllevel_serv = chr.getTotalSkillLevel(skill);
		if (skillId == 400031064) {
			chr.cancelEffect(SkillFactory.getSkill(400031062).getEffect(skilllevel_serv));
		}
		boolean apply = true, cooltime = true;
		SecondaryStatEffect eff = skill.getEffect(skilllevel_serv);
		SecondaryStatEffect effect = SkillFactory.getSkill(skillId).getEffect(level);
		if (skilllevel_serv > 0 && skilllevel_serv == level) {
			Map<MapleStat, Long> hpmpupdate;
			long mpchange;
			if (skill.isChargeSkill()) {
				chr.setKeyDownSkill_Time(System.currentTimeMillis());
			}
			switch (skillId) {
			case 162121022:
				chr.setSkillCustomInfo(162121022, eff.getQ(), 0L);
				break;
			case 135001020:
				chr.setSkillCustomInfo(13500, 0L, 0L);
				SkillFactory.getSkill(135001005).getEffect(1).applyTo(chr, (int) chr.getBuffLimit(135001005));
				break;
			case 155111306:
			case 155121341:
			case 400051080:
			case 400051334:
				if (!chr.getBuffedValue(155000007)) {
					SkillFactory.getSkill(155000007).getEffect(1).applyTo(chr);
				}
				if (skillId == 400051080) {
					chr.setSkillCustomInfo(skillId, 1L, 0L);
				}
				break;
			case 33101005:
				chr.setLinkMid(slea.readInt(), 0);
				break;
			case 400011091:
				eff = SkillFactory.getSkill(skillId)
						.getEffect(chr.getSkillLevel(GameConstants.getLinkedSkill(skillId)));
				chr.combinationBuff = 15;
				SkillFactory.getSkill(37120012).getEffect(chr.getSkillLevel(37120012)).applyTo(chr, false);
				chr.setSkillCustomInfo(400011091, 1L, 0L);
				chr.Cylinder(skillId);
				break;
			case 151121004:
				chr.setSkillCustomInfo(151121004, 8L, 0L);
				break;
			case 400051040:
				chr.getMap().broadcastMessage(chr, CField.EffectPacket.showEffect(chr, 400051040, 400051040, 1, 0, 0,
						(byte) (chr.isFacingLeft() ? 1 : 0), false, chr.getPosition(), null, null), false);
				break;

			case 164121042:
				hpmpupdate = new EnumMap<>(MapleStat.class);
				mpchange = SkillFactory.getSkill(skillId).getEffect(chr.getSkillLevel(skillId)).getMPRCon();
				chr.getStat().setMp(chr.getStat().getMp() - chr.getStat().getCurrentMaxMp(chr) / 100L * mpchange, chr);
				hpmpupdate.put(MapleStat.MP, Long.valueOf(chr.getStat().getMp()));
				if (hpmpupdate.size() > 0) {
					chr.getClient().getSession().writeAndFlush(CWvsContext.updatePlayerStats(hpmpupdate, false, chr));
				}
				chr.setSkillCustomInfo(164121042, (eff.getY() - 1), 0L);
				break;
			case 400041009:
				chr.removeSkillCustomInfo(400041009);
				chr.removeSkillCustomInfo(400041010);
				break;
			case 63121040:
				chr.handleStackskill(skillId, true);
				break;
			case 37121052:
				chr.setSkillCustomInfo(37121052, 1L, 0L);
				break;
			}
		}
		if (skillId - 64001009 >= -2 && skillId - 64001009 <= 2) {
			return;
		}
		chr.getMap().broadcastMessage(chr, CField.skillEffect(chr, skillId, skilllevel_serv, display, unk), false);
		if ((skillId >= 3321034 && skillId <= 3321040) || (skillId >= 400011110 && skillId <= 400011111)
				|| skillId == 400041053) {
			effect.applyTo(chr);
		} else if (apply && skillId != 4341002 && skillId != 2321001 && skillId != 3111013 && skillId != 5311002
				&& skillId != 11121052 && skillId != 14121003 && skillId != 22171083) {
			eff.applyTo(chr);
			if (skillId == 21120018 || skillId == 21120019) {
				int skillid = chr.getBuffedValue(21110016) ? 21110016 : (chr.getBuffedValue(21121058) ? 21121058 : 0);
				if (skillid > 0) {
					long du = chr.getBuffLimit(skillid);
					SkillFactory.getSkill(skillid).getEffect(chr.getSkillLevel(skillid)).applyTo(chr, false, (int) du);
				}
			}
		}
		if (cooltime && eff != null && !eff.ignoreCooldown(chr) && !chr.skillisCooling(skillId)
				&& !chr.skillisCooling(skill.getId()) && eff.getCooldown(chr) > 0 && skillId != 3111013
				&& skillId != 22171083 && !GameConstants.isAfterCooltimeSkill(skillId)) {
			chr.addCooldown(skillId, System.currentTimeMillis(), eff.getCooldown(chr));
			chr.getClient().getSession().writeAndFlush(CField.skillCooldown(skillId, eff.getCooldown(chr)));
		}
	}

	public static final void SpecialMove(final LittleEndianAccessor slea, final MapleClient c,
			final MapleCharacter chr) {
		if (chr == null || chr.getMap() == null || slea.available() < 9L) {
			c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
			return;
		}

		Point pos = new Point(slea.readShort(), slea.readShort());
		int skillid = slea.readInt();

		if (ServerConstants.DEBUG_RECEIVE) {
			switch (skillid) {
			default:
				System.err.println("[SpecialMove A] " + skillid + " / " + slea);
				break;
			}
		}

		slea.skip(4); // 1.2.390 ++

		if (skillid == 35001002) {
			SkillFactory.getSkill(30000227).getEffect(chr.getSkillLevel(1)).applyTo(chr);
		}
		if (skillid == 23111008) {
			SecondaryStatEffect eff = SkillFactory.getSkill(skillid).getEffect(c.getPlayer().getSkillLevel(skillid));
			eff.applyTo(c.getPlayer(), 0);
		}
		if (skillid == 13121017) {
			SecondaryStatEffect eff = SkillFactory.getSkill(13121017).getEffect(chr.getSkillLevel(13121017));
			eff.applyTo(chr, eff.getDuration());
		}
		if (skillid == 400001064) {
			SecondaryStatEffect eff = SkillFactory.getSkill(400001064).getEffect(chr.getSkillLevel(400001064));
			eff.applyTo(chr, eff.getDuration());
		}
		if (skillid == 100001284) { // 초월자의 의지
			SecondaryStatEffect eff = SkillFactory.getSkill(skillid).getEffect(c.getPlayer().getSkillLevel(skillid));
			c.getSession().writeAndFlush(CField.skillCooldown(skillid, eff.getCooldown(c.getPlayer())));
		}
		if (skillid == 152121011) {
			MapleSummon summon = c.getPlayer().getSummon(152101000);
			summon.setEnergy(150);
		}

		// 엔젤 오브 리브라 - 복수의 천사 소환
		if (skillid == 400021032) {
			SecondaryStatEffect eff = SkillFactory.getSkill(400021033)
					.getEffect(c.getPlayer().getSkillLevel(400021033));
			MapleSummon tosummon = new MapleSummon(chr, 400021033, chr.getTruePosition(), SummonMovementType.FOLLOW,
					(byte) 0, eff.getDuration());
			chr.addSummon(tosummon);
			chr.getMap().spawnSummon(tosummon, 30000);
		}
		if (skillid == 5221029) {
			chr.setSkillCustomInfo(5221029, 0L, 6000L);
			chr.signofbomb = true;
		}
		if (skillid == 61101002) {
			if (chr.getSkillLevel(61120007) > 0) {
				skillid = 61120007;
			} else if (chr.getBuffedValue(SecondaryStat.Morph) != null) {
				if (chr.getBuffSource(SecondaryStat.Morph) == 61111008) {
					skillid = 61110211;
				} else {
					skillid = 61121217;
				}
			}
		} else if (skillid == 12001028 || skillid == 12001027 || skillid == 12001029) {
			c.getPlayer().getMap().broadcastMessage(CField.FireWork(chr));
		} else if (skillid == 13101022) {
			if (chr.getSkillLevel(13141001) > 0) { // 트라이플링 윔 VI 구현
				skillid = 13141001;
			}
		} else if (skillid == 51001006) {
			skillid = 51001009;
		} else if (skillid == 51141003) { // 로얄 가드 VI 구현
			skillid = 51141006;
		} else if (skillid == 21141500) { // 오리진 아드레날린 서지 구현
			skillid = 21141506;
		}
		if (skillid == 4221018 || skillid == 4211003) {
			if (chr.getBuffedValue(SecondaryStat.PickPocket) != null) {
				chr.cancelEffect(chr.getBuffedEffect(SecondaryStat.PickPocket));
			}
		}
		if (skillid == 5221022) {
			if (chr.getBuffedValue(5221022) && chr.getBuffedValue(5221027)) {
				if (chr.getBattleship() <= 0) {
					skillid = 5221022;
					chr.setBattleship(5221022);
				} else {
					if (chr.getBattleship() == 5221022) {
						skillid = 5221027;
						chr.setBattleship(5221027);
					} else {
						skillid = 5221022;
						chr.setBattleship(5221022);
					}
				}
			} else {
				if (!chr.getBuffedValue(5221022)) {
					skillid = 5221022;
				} else if (!chr.getBuffedValue(5221027)) {
					skillid = 5221027;
				}
			}
		}
		if (skillid == 5241002) { // 배틀쉽 봄버 VI 구현
			if (chr.getBuffedValue(5241002) && chr.getBuffedValue(5241003)) {
				if (chr.getBattleship() <= 0) {
					skillid = 5241002;
					chr.setBattleship(5241002);
				} else {
					if (chr.getBattleship() == 5241002) {
						skillid = 5241003;
						chr.setBattleship(5241003);
					} else {
						skillid = 5241002;
						chr.setBattleship(5241002);
					}
				}
			} else {
				if (!chr.getBuffedValue(5241002)) {
					skillid = 5241002;
				} else if (!chr.getBuffedValue(5241003)) {
					skillid = 5241003;
				}
			}
		}
		if (GameConstants.isZeroSkill(skillid)) {
			slea.skip(1);
		}
		if (GameConstants.isKinesis(chr.getJob())) {
			chr.givePPoint(skillid);
		}
		if (skillid == 14001026) {
			SecondaryStatEffect eff = SkillFactory.getSkill(14110032).getEffect(chr.getSkillLevel(14110032));
			int count = chr.getMomentumCount();
			if (count < 3) {
				chr.setMomentumCount(count + 1);
			}
			eff.applyTo(chr, eff.getDuration());
		}
		if (skillid == 5011007) { // 몽키와 함께라면!
			if (c.getPlayer().getKeyValue(7786, "sw") == -1 || c.getPlayer().getKeyValue(7786, "sw") != 1) {
				c.getPlayer().setKeyValue(7786, "sw", "1");
			} else {
				c.getPlayer().setKeyValue(7786, "sw", "0");
			}
			chr.getMap().broadcastMessage(CField.MonkeyTogether(88, c.getPlayer().getKeyValue(7786, "sw") == 1));
		}
		if (skillid == 5311013 || skillid == 5341002) { // 미니 캐논볼 VI 구현
			final Map<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
			long count = c.getPlayer().getSkillCustomValue(skillid);
			c.getPlayer().setSkillCustomInfo(skillid, count - 1, 0);
			SecondaryStatEffect yoyo = SkillFactory.getSkill(skillid).getEffect(1);
			statups.put(SecondaryStat.MiniCannonBall, new Pair<Integer, Integer>((int) count, 0));
			c.getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, yoyo, c.getPlayer()));
			chr.getClient().getSession().writeAndFlush(CField.rangeAttack(skillid,
					Arrays.asList(new RangeAttack(skillid + 1, chr.getTruePosition(), 1, 330, 1))));
		}

		int skillLevel = slea.readInt();

		final Skill skill = SkillFactory.getSkill(skillid);
		if (skill == null || (GameConstants.isAngel(skillid) && chr.getStat().equippedSummon % 10000 != skillid % 10000)
				|| (chr.inPVP() && skill.isPVPDisabled())) {
			c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
			return;
		}
		boolean linkcool = true;
		if (GameConstants.isCain(chr.getJob())) {
			linkcool = chr.handleCainSkillCooldown(skillid);
		}
		if (((linkcool && chr.getTotalSkillLevel(GameConstants.getLinkedSkill(skillid)) <= 0)
				|| (chr.getTotalSkillLevel(GameConstants.getLinkedSkill(skillid)) != skillLevel && linkcool))
				&& !GameConstants.isMulungSkill(skillid) && !GameConstants.isPyramidSkill(skillid)
				&& chr.getTotalSkillLevel(GameConstants.getLinkedSkill(skillid)) <= 0 && !GameConstants.isAngel(skillid)
				&& !GameConstants.isFusionSkill(skillid)) {
			if (!GameConstants.isExceptionSpecialMove(skillid)) {
				return;
			}
		}
		switch (skillid) {
		case 2211012:
		case 14111030:
		case 27111101:
		case 162111002:
		case 400001025:
		case 400001026:
		case 400001027:
		case 400001028:
		case 400001029:
		case 400001030:
		case 400021122: {
			linkcool = false;
			break;
		}
		}
		// 이거 그냥 심심해서 구현 해놓음
		switch (skillid) {
		case 20031203:
			c.getPlayer().warp(150000000);
			c.getPlayer().dropMessage(5, "크리스탈가든 으로 귀환 하였습니다.");
			break;
		case 1281:
			c.getPlayer().warp(4000030);
			c.getPlayer().dropMessage(5, "사우스페리로 귀환 하였습니다.");
			break;
		case 400001046:
		case 400001047:
		case 400001048:
		case 400001049:
		case 400001050:
		case 400001042:
		case 400001043:
			chr.메이플용사--;
			HashMap<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
			statups.put(SecondaryStat.Bless5th, new Pair<Integer, Integer>(chr.메이플용사, 0));
			break;
		case 400011000:
			chr.메이플용사--;
			HashMap<SecondaryStat, Pair<Integer, Integer>> statups1 = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
			statups1.put(SecondaryStat.AuraWeapon, new Pair<Integer, Integer>(chr.메이플용사, 0));
			break;
		}

		skillLevel = chr.getTotalSkillLevel(GameConstants.getLinkedSkill(skillid));
		final SecondaryStatEffect effect = chr.inPVP() ? skill.getPVPEffect(skillLevel) : skill.getEffect(skillLevel);

		if (effect.isMPRecovery() && chr.getStat().getHp() < (chr.getStat().getMaxHp() / 100) * 10) {
			c.getPlayer().dropMessage(5, "You do not have the HP to use this skill.");
			c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
			return;
		}
		if (GameConstants.isNightWalker(chr.getJob()) && chr.getBuffedEffect(SecondaryStat.DarkSight) != null
				&& (skillid != 14001026 & skillid != 10001253)) {
			chr.cancelEffectFromBuffStat(SecondaryStat.DarkSight);
		}
		final SecondaryStatEffect linkEffect = SkillFactory.getSkill(GameConstants.getLinkedSkill(skillid))
				.getEffect(skillLevel);
		if (linkEffect.getCooldown(chr) > 0 && effect.getSourceId() != 35111002 && effect.getSourceId() != 11141002
				&& effect.getSourceId() != 151100002 && effect.getSourceId() != 20031205 && !effect.ignoreCooldown(chr)
				&& (linkEffect.getSourceId() < 400041003 || linkEffect.getSourceId() > 400041005) && linkcool) {
			if (chr.skillisCooling(linkEffect.getSourceId()) && !GameConstants.isCooltimeKeyDownSkill(skillid)
					&& !GameConstants.isNoApplySkill(skillid) && !chr.getBuffedValue(skillid) && skillid != 155001104
					&& skillid != 155001204 && /* chr.unstableMemorize != skillid && */ skillid != 400001010
					&& skillid != 400001011 && skillid != 5121055 && skillid != 400020046) {
				c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
				return;
			}
			if (chr.getBuffedValue(20040219) || chr.getBuffedValue(20040220)) {
				if (skill.isHyper() || !GameConstants.isLuminous(skillid / 10000)) {
					c.getSession().writeAndFlush((Object) CField.skillCooldown(skillid, linkEffect.getCooldown(chr)));
					chr.addCooldown(skillid, System.currentTimeMillis(), linkEffect.getCooldown(chr));
				}
			} else if (skillid == 25121133) {
				c.getSession().writeAndFlush((Object) CField.skillCooldown(25121133, linkEffect.getCooldown(chr)));
				chr.addCooldown(25121133, System.currentTimeMillis(), linkEffect.getCooldown(chr));
			} else if ((!GameConstants.isAfterCooltimeSkill(skillid) || skillid == 33111013) && !chr.memoraizecheck) {
				c.getSession().writeAndFlush((Object) CField.skillCooldown(skillid, linkEffect.getCooldown(chr)));
				chr.addCooldown(skillid, System.currentTimeMillis(), linkEffect.getCooldown(chr));
			}
			if (chr.memoraizecheck) {
				chr.memoraizecheck = false;
			} else if (skillid == 400001021) {
				chr.memoraizecheck = true;
			}
		} else if (skillid == 400011001 && !chr.getBuffedValue(400011001) && !chr.getBuffedValue(400011002)) {
			c.getSession().writeAndFlush((Object) CField.skillCooldown(skillid, linkEffect.getX() * 1000));
		}
		if (GameConstants.isPhantom(chr.getJob())) {
			for (final Pair<Integer, Boolean> sk : chr.getStolenSkills()) {
				if (sk.left == skillid && sk.right) {
					int cooltime = 0;
					switch (sk.left) {
					case 1121054:
					case 2321054: {
						cooltime = 300;
						break;
					}
					case 1221054: {
						cooltime = 700;
						break;
					}
					case 3121054:
					case 4121054: {
						cooltime = 120;
						break;
					}
					case 1321054:
					case 3221054: {
						cooltime = 180;
						break;
					}
					case 5221054: {
						cooltime = 60;
						break;
					}
					case 2121054:
					case 5121054: {
						cooltime = 75;
						break;
					}
					case 2221054: {
						cooltime = 90;
						break;
					}
					}
					if (cooltime > 0) {
						cooltime *= 1000;
						c.getSession().writeAndFlush((Object) CField.skillCooldown(skillid, cooltime));
						chr.addCooldown(skillid, System.currentTimeMillis(), cooltime);
						break;
					}
					continue;
				}
			}
		}
		if (skillid == 2321016) {
			if (c.getPlayer().skillisCooling(skillid)) {
				final SecondaryStatEffect effz = SkillFactory.getSkill(skillid).getEffect(chr.getSkillLevel(skillid));
				int int_ = c.getPlayer().getStat().getTotalInt() / effz.getS();
				c.getPlayer().changeCooldown(skillid, Math.max(-effz.getV2() * 1000, -effz.getW2() * int_ * 1000));
			}
		}
		if (skillid == 31211004) {
			chr.startDiabolicRecovery(effect);
		}

		if (GameConstants.isSoulSummonSkill(skillid)) {
			chr.useSoulSkill();
		}
		if (skillid == 36121007) {
			chr.getClient().getSession().writeAndFlush((Object) CField.TimeCapsule(skillid));
			chr.setChair(3010587);
			chr.getMap().broadcastMessage(chr, CField.showChair(chr, 3010587), false);
		}

		final AttackInfo ret = new AttackInfo();

		ret.skill = skillid;
		ret.skilllevel = skillLevel;

		if (ServerConstants.DEBUG_RECEIVE) {
			switch (ret.skill) {
			default:
				System.err.println("[SpecialMove B] " + ret.skill + " / " + slea);
				break;
			}
		}

		slea.readInt();
		slea.readInt();

		GameConstants.calcAttackPosition(slea, ret); // 1.2.390 OK.

		final int unk = slea.readByte();
		final int plus = slea.readByte();
		slea.readByte();

		slea.readShort();
		slea.readInt();
		slea.readByte();
		slea.readInt();

		if (skillid == 3101009) {
			if (chr.getBuffedValue(3101009)) {
				final byte quiver = chr.getQuiverType();
				if (chr.getBuffedValue(3121016) || chr.getBuffedValue(400031028)) {
					c.getSession().writeAndFlush((Object) CWvsContext.enableActions(chr));
					return;
				}
				if (chr.getRestArrow()[quiver - 1] > 0) {
					chr.setQuiverType((byte) ((quiver == 2) ? 1 : (quiver + 1)));
					int arrowcount = 0;
					int arrowcount2 = 0;
					arrowcount = chr.getRestArrow()[0];
					arrowcount2 = chr.getRestArrow()[1];
					chr.getClient().getSession()
							.writeAndFlush((Object) CField.EffectPacket.showEffect(chr, 0, 3101009, 57,
									chr.getQuiverType() - 1, 0, (byte) (chr.isFacingLeft() ? 1 : 0), true,
									chr.getPosition(), null, null));
					chr.getMap()
							.broadcastMessage(chr,
									CField.EffectPacket.showEffect(chr, 0, 3101009, 57, chr.getQuiverType() - 1, 0,
											(byte) (chr.isFacingLeft() ? 1 : 0), false, chr.getPosition(), null, null),
									false);
					effect.applyTo(chr, false);
				} else {
					final int type = chr.getQuiverType() - 1;
					chr.getClient().getSession().writeAndFlush((Object) CField.EffectPacket.showEffect(chr, 0, 3101009,
							57, type, 0, (byte) (chr.isFacingLeft() ? 1 : 0), true, chr.getPosition(), null, null));
					chr.getMap().broadcastMessage(chr, CField.EffectPacket.showEffect(chr, 0, 3101009, 57, type, 0,
							(byte) (chr.isFacingLeft() ? 1 : 0), false, chr.getPosition(), null, null), false);
					effect.applyTo(chr, false);
				}
				c.getSession().writeAndFlush((Object) CWvsContext.enableActions(chr));
				return;
			}
		} else if (skillid == 12101025) {
			pos = slea.readPos();
			slea.readPos();
			slea.readByte();
			final int warp = slea.readByte();
			c.send(CWvsContext.onSkillUseResult(0));
			c.send(CField.fireBlink(chr.getId(), pos));
			chr.getMap().broadcastMessage(chr, CField.fireBlinkMulti(chr, warp != 0), false);
		}

		chr.checkFollow();
		if (ret.plusPosition2 != null) {
			pos = ret.plusPosition2;
		}
		if (skillid == 2111013) {
			SkillFactory.getSkill(2111013).getEffect(chr.getSkillLevel(2111013)).applyTo(chr, chr.getPosition());
		}

		if (ServerConstants.DEBUG_RECEIVE) {
			switch (ret.skill) {
			default:
				System.err.println("[SpecialMove C] " + ret.skill + " / " + slea);
				break;
			}
		}

		slea.skip(1); // 1.2.390 ++
		slea.readMapleAsciiString(); // 1.2.390 ++

		if (slea.available() != 4) {
			slea.skip(4); // 1.2.390 ++
		} else {
			slea.skip(3); // 1.2.390 ++
		}

		if (c.getPlayer().getSkillLevel(4241000) > 0) { // 분쇄 VI 구현
			if (skillid == 4221006) { // 연막탄
				c.getPlayer().hexaMasterySkillStack += 3;

				if (c.getPlayer().hexaMasterySkillStack > 5) {
					c.getPlayer().hexaMasterySkillStack = 5;

				}

				EnumMap<SecondaryStat, Pair<Integer, Integer>> statupsT = new EnumMap<SecondaryStat, Pair<Integer, Integer>>(
						SecondaryStat.class);
				statupsT.put(SecondaryStat.Smash_Stack,
						new Pair<Integer, Integer>(c.getPlayer().hexaMasterySkillStack, 0));
				SecondaryStatEffect effT = SkillFactory.getSkill(4241004)
						.getEffect(c.getPlayer().getSkillLevel(4241000));
				c.getPlayer().getClient().getSession()
						.writeAndFlush(CWvsContext.BuffPacket.giveBuff(statupsT, effT, c.getPlayer()));
			} else if (skillid == 4221052) { // 베일 오브 섀도우
				c.getPlayer().hexaMasterySkillStack += 2;

				if (c.getPlayer().hexaMasterySkillStack > 5) {
					c.getPlayer().hexaMasterySkillStack = 5;

				}

				EnumMap<SecondaryStat, Pair<Integer, Integer>> statupsT = new EnumMap<SecondaryStat, Pair<Integer, Integer>>(
						SecondaryStat.class);
				statupsT.put(SecondaryStat.Smash_Stack,
						new Pair<Integer, Integer>(c.getPlayer().hexaMasterySkillStack, 0));
				SecondaryStatEffect effT = SkillFactory.getSkill(4241004)
						.getEffect(c.getPlayer().getSkillLevel(4241000));
				c.getPlayer().getClient().getSession()
						.writeAndFlush(CWvsContext.BuffPacket.giveBuff(statupsT, effT, c.getPlayer()));
			}
		}

		if (ServerConstants.DEBUG_RECEIVE) {
			switch (ret.skill) {
			default:
				System.err.println("[SpecialMove D] " + ret.skill + " / " + slea);
				break;
			}
		}

		Label_14276: {
			switch (skillid) {
			case 63141000: // 폴링 더스트 VI 구현
			case 63121002: {
				chr.handleStackskill(skillid, true);
				break;
			}
			case 11121018: {
				final List<SecondAtom> atoms2 = new ArrayList<SecondAtom>();
				final List<Integer> list = new ArrayList<Integer>();
				final int size = slea.readByte();
				for (int a = 0; a < size; ++a) {
					list.add(slea.readInt());
				}
				slea.skip(3);
				final int x = slea.readInt();
				final int y = slea.readInt();
				int i = 0;
				int delay = 0;

				int x_[] = { -156, -97, -2, 93, 152, 152, -93, -2, -97, -156 };
				int y_[] = { -102, -183, -214, -183, -102, -102, 79, 110, 79, -2 };

				int mobid = 0;
				while (i < chr.getCosmicCount()) {
					mobid = ((i < size) ? list.get(i) : mobid);
					atoms2.add(new SecondAtom(0x2A, c.getPlayer().getId(), mobid, 420 + delay, skillid, 2000, 0, 1,
							new Point(x + x_[i], y + y_[i]), Arrays.asList(0, 0)));
					++i;
					delay += 60;
				}
				if (chr.getCosmicCount() <= 9) { // 코스믹 버스트 쿨타임 처리
					int reduce = 1000 * chr.getCosmicCount();
					chr.changeCooldown(11121018, -reduce);
				}
				chr.getMap().spawnSecondAtom(chr, atoms2, 0);
				chr.setCosmicCount(0);
				break;
			}
			case 11121054: { // 코스믹 포지
				final SecondaryStatEffect eff = SkillFactory.getSkill(skillid).getEffect(chr.getSkillLevel(skillid));
				int duration = 0;

				for (int i = 0; i < chr.getCosmicCount(); i++) {
					duration += 20000;
				}
				eff.applyTo(chr, eff.getDuration() + duration);
				chr.setCosmicCount(10);
				break;
			}
			case 11111029: { // 코스믹 샤워
				final SecondaryStatEffect eff = SkillFactory.getSkill(skillid).getEffect(chr.getSkillLevel(skillid));
				int duration = 0;

				for (int i = 0; i < chr.getCosmicCount(); i++) {
					duration += 3000;
				}
				eff.applyTo(chr, eff.getDuration() + duration);
				chr.setCosmicCount(0);
				break;
			}
			case 11141002: { // 코스믹 샤워 VI 구현
				final SecondaryStatEffect eff = SkillFactory.getSkill(skillid).getEffect(chr.getSkillLevel(skillid));
				int duration = 0;

				for (int i = 0; i < chr.getCosmicCount(); i++) {
					duration += 3000;
				}
				eff.applyTo(chr, eff.getDuration() + duration);
				chr.addCoolTime(skillid, 30000);
				chr.setCosmicCount(0);
				break;
			}
			case 400011142: {
				final SecondaryStatEffect eff = SkillFactory.getSkill(skillid).getEffect(chr.getSkillLevel(skillid));
				// eff.applyTo(chr, eff.getDuration());
				SkillFactory.getSkill(skillid).getEffect(skillLevel).applyTo(c.getPlayer(), eff.getDuration());
				chr.setCosmicCount(0);
				break;
			}
			case 12120013: {
				final SecondaryStatEffect eff = SkillFactory.getSkill(skillid).getEffect(chr.getSkillLevel(skillid));
				eff.applyTo(chr, 300000);
				break;
			}
			case 12120014: {
				final SecondaryStatEffect eff = SkillFactory.getSkill(skillid).getEffect(chr.getSkillLevel(skillid));
				eff.applyTo(chr, 300000);
				break;
			}
			case 23111008: { // 엘리멘탈 나이트
				int elementSummons = 23111009 + Randomizer.rand(0, 2);
				SecondaryStatEffect eff = SkillFactory.getSkill(elementSummons)
						.getEffect(c.getPlayer().getSkillLevel(elementSummons));
				MapleSummon tosummon = new MapleSummon(chr, elementSummons, chr.getTruePosition(),
						SummonMovementType.BIRD_FOLLOW, (byte) 0, eff.getDuration());
				chr.addSummon(tosummon);
				chr.getMap().spawnSummon(tosummon, eff.getDuration());
				eff.applyTo(c.getPlayer(), eff.getDuration());
				if (elementSummons == tosummon.getSkill()) {

					return;
				}
				break;
			}

			case 400020046: {
				Point pos123 = slea.readPos();
				final MapleMist newmist2 = new MapleMist(effect.calculateBoundingBox(pos123, chr.isFacingLeft()),
						c.getPlayer(), effect, effect.getDuration(), (byte) (chr.isFacingLeft() ? 1 : 0));
				newmist2.setPosition(pos123);
				c.getPlayer().getMap().spawnMist(newmist2, false);
				effect.applyTo(chr);
				break;
			}
			case 5121010: {
				Integer[] bufflist = new Integer[] { 5121010, 80002282, 2321055, 2023661, 2023662, 2023663, 2023664,
						2023665, 2023666, 2450064, 2450134, 2450038, 2450124, 2450147, 2450148, 2450149, 2023558,
						2003550, 2023556, 2003551 };
				if (chr.getParty() != null) {
					for (MaplePartyCharacter pc : chr.getParty().getMembers()) {
						if (!pc.getPlayer().getBuffedValue(5121010)) {
							for (MapleCoolDownValueHolder i : pc.getPlayer().getCooldowns()) {
								if (!Arrays.asList(bufflist).contains(i.skillId)
										&& !SkillFactory.getSkill(i.skillId).isHyper()
										&& i.skillId / 10000 <= pc.getPlayer().getJob()) { // 오류
									pc.getPlayer().removeCooldown(i.skillId);
									pc.getPlayer().getClient().getSession()
											.writeAndFlush(CField.skillCooldown(i.skillId, 0));
								}
							}
							pc.getPlayer().addCooldown(5121010, System.currentTimeMillis(), 180000);
							SkillFactory.getSkill(5121010).getEffect(chr.getSkillLevel(5121010)).applyTo(pc.getPlayer(),
									false, false);
						}
					}
				} else {
					if (!chr.getBuffedValue(5121010)) {
						for (MapleCoolDownValueHolder i : chr.getCooldowns()) {
							if (!Arrays.asList(bufflist).contains(i.skillId)
									&& !SkillFactory.getSkill(i.skillId).isHyper()
									&& i.skillId / 10000 <= chr.getJob()) {
								chr.removeCooldown(i.skillId);
								chr.getClient().getSession().writeAndFlush(CField.skillCooldown(i.skillId, 0));
							}
						}
						chr.addCooldown(5121010, System.currentTimeMillis(), 180000);
						SkillFactory.getSkill(5121010).getEffect(chr.getSkillLevel(5121010)).applyTo(chr, false, false);
					}
				}
				break;
			}
			case 162121042: {
				if (chr.getSkillCustomValue0(162121042) > 0L) {
					chr.addSkillCustomInfo(162121042, -1L);
					if (chr.getSkillCustomValue(162121142) == null) {
						chr.setSkillCustomInfo(162121142, 0L, effect.getW() * 1000);
					}
				}
				final Map<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
				statups.put(SecondaryStat.자유로운용맥,
						new Pair<Integer, Integer>((int) chr.getSkillCustomValue0(162121042), 0));
				chr.getClient().getSession()
						.writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, effect, chr));
				final List<SecondAtom> atoms = new ArrayList<SecondAtom>();
				atoms.add(new SecondAtom(20, c.getPlayer().getId(), 0, 0, 162101000, 0, 0, 0, pos, Arrays.asList(8)));
				c.getPlayer().spawnSecondAtom(atoms);
				break;
			}
			case 162121043: {
				final MapleMist newmist = new MapleMist(effect.calculateBoundingBox(pos, ret.rlType == 1),
						c.getPlayer(), effect, effect.getDuration(), ret.rlType);
				newmist.setPosition(pos);
				newmist.setDelay(2);
				chr.getMap().spawnMist(newmist, false);
				SkillFactory.getSkill(162121044).getEffect(skillLevel).applyTo(c.getPlayer());
				break;
			}
			case 400021123: {
				final List<RangeAttack> skills = new ArrayList<RangeAttack>();
				skills.add(new RangeAttack(400021128, ret.plusPosition2, (int) ((ret.rlType > 0) ? 1 : 0), 5850, 8));
				skills.add(new RangeAttack(400021127, ret.plusPosition2, (int) ((ret.rlType > 0) ? 1 : 0), 3150, 4));
				skills.add(new RangeAttack(400021126, ret.plusPosition2, (int) ((ret.rlType > 0) ? 1 : 0), 2250, 4));
				skills.add(new RangeAttack(400021125, ret.plusPosition2, (int) ((ret.rlType > 0) ? 1 : 0), 1560, 4));
				skills.add(new RangeAttack(400021124, ret.plusPosition2, (int) ((ret.rlType > 0) ? 1 : 0), 630, 4));
				c.send(CField.rangeAttack(400021123, skills));
				effect.applyTo(chr);
				break;
			}

			case 162121003: {
				final int onskill = chr.getBuffedValue(162101003) ? 162101003 : 162121012;
				if (chr.getBuffedValue(onskill)) {
					chr.cancelEffect(chr.getBuffedEffect(onskill));
				}
				effect.applyTo(chr);
				break;
			}
			case 162101001:
			case 162121000: {
				if (chr.getSkillLevel(162110007) > 0) {
					SkillFactory.getSkill(162110007).getEffect(c.getPlayer().getSkillLevel(162110007))
							.applyTo(c.getPlayer());
					break;
				}
				break;
			}
			case 162101003:
			case 162121012: {
				pos = slea.readPos();
				slea.readByte();
				final List<RangeAttack> skills2 = new ArrayList<RangeAttack>();
				if (chr.getBuffedValue(162121003)) {
					chr.cancelEffect(chr.getBuffedEffect(162121003));
				}
				if (skillid == 162101003) {
					for (int m = 0; m < 8; ++m) {
						skills2.add(new RangeAttack(162101004, c.getPlayer().getPosition(), 0, 0, 1));
					}
				} else {
					for (int m = 0; m < 3; ++m) {
						skills2.add(new RangeAttack(162121014, c.getPlayer().getPosition(), 0, 0, 1));
						skills2.add(new RangeAttack(162121013, c.getPlayer().getPosition(), 0, 0, 1));
						skills2.add(new RangeAttack(162121013, c.getPlayer().getPosition(), 0, 0, 1));
					}
				}
				effect.applyTo(chr, pos);
				c.send(CField.rangeAttack(skillid, skills2));
				break;
			}

			case 162121010: {
				final List<SecondAtom> atoms2 = new ArrayList<SecondAtom>();
				final List<Integer> list = new ArrayList<Integer>();
				final int size = slea.readByte();
				for (int a = 0; a < size; ++a) {
					list.add(slea.readInt());
				}
				slea.skip(3);
				final int unks = slea.readInt();
				final int unks2 = slea.readInt();
				int i = 0;
				int mobid = 0;
				while (i < 5) {
					mobid = ((i < size) ? list.get(i) : mobid);
					atoms2.add(new SecondAtom(23, c.getPlayer().getId(), mobid, 1200 + i * 120, skillid, 4000,
							20 + i * 60, 1,
							new Point(ret.plusPosition2.x + ((ret.rlType == 1) ? 55 : -55), ret.plusPosition2.y - 90),
							Arrays.asList(ret.plusPosition2.x, ret.plusPosition2.y)));
					++i;
				}
				chr.getClient().getSession().writeAndFlush((Object) CField.EffectPacket.showEffect(chr, 0, 162121010, 1,
						0, 0, (byte) (chr.isFacingLeft() ? 1 : 0), true, ret.plusPosition2, null, null));
				chr.getMap().broadcastMessage(chr, CField.EffectPacket.showEffect(chr, 0, 162121010, 1, 0, 0,
						(byte) (chr.isFacingLeft() ? 1 : 0), false, ret.plusPosition2, null, null), false);
				chr.getMap().spawnSecondAtom(chr, atoms2, 0);
				break;
			}
			case 162111005:
			case 400021122: {
				if (skillid == 400021122) {
					c.getSession().writeAndFlush((Object) CField.skillCooldown(skillid, linkEffect.getCooldown(chr)));
					chr.addCooldown(skillid, System.currentTimeMillis(), linkEffect.getCooldown(chr));
				}
				final List<SecondAtom> atoms2 = new ArrayList<SecondAtom>();
				final List<Integer> list = new ArrayList<Integer>();
				final int size = slea.readByte();
				for (int a = 0; a < size; ++a) {
					list.add(slea.readInt());
				}
				int mobid2 = 0;
				for (int j = 0; j < 7 + ((skillid == 400021122) ? 0 : size); ++j) {
					mobid2 = ((j < size) ? list.get(j) : mobid2);
					atoms2.add(
							new SecondAtom(0x16, c.getPlayer().getId(), mobid2, 1140, skillid, 4000, 0, 1,
									new Point(ret.plusPosition2.x + Randomizer.rand(-200, 200),
											ret.plusPosition2.y + Randomizer.rand(-350, -50)),
									Arrays.asList(new Integer[0])));
					if (skillid == 162111005 && j >= effect.getZ()) {
						break;
					}
				}
				chr.getMap().spawnSecondAtom(chr, atoms2, 0);
				break;
			}
			case 162111002: {
				slea.skip(4);
				pos = slea.readIntPos();
				final List<SecondAtom> atoms2 = new ArrayList<SecondAtom>();
				if (chr.getMap().getSecondAtom(chr.getId(), skillid) != null) {
					c.getSession().writeAndFlush((Object) CWvsContext.onSkillUseResult(skillid));
					atoms2.add(new SecondAtom(32, c.getPlayer().getId(), 0, 0, skillid, 90000, 0, 1, ret.plusPosition2,
							Arrays.asList(chr.getMap().getSecondAtom(chr.getId(), skillid).getObjectId())));
					effect.applyTo(chr);
					chr.getMap().spawnSecondAtom(chr, atoms2, 0);
					break;
				}
				atoms2.add(new SecondAtom(31, c.getPlayer().getId(), 0, 0, skillid, 10000, 0, 1, ret.plusPosition2,
						Arrays.asList(0)));
				chr.getMap().spawnSecondAtom(chr, atoms2, 0);
				c.getSession().writeAndFlush((Object) CField.skillCooldown(skillid, linkEffect.getCooldown(chr)));
				chr.addCooldown(skillid, System.currentTimeMillis(), linkEffect.getCooldown(chr));
				break;
			}
			case 162101012: {
				if (chr.getSkillCustomValue0(162101012) > 0L) {
					chr.addSkillCustomInfo(162101012, -1L);
					if (chr.getSkillCustomValue(162101112) == null) {
						chr.setSkillCustomInfo(162101112, 0L, effect.getZ() * 1000);
					}
				}
				final Map<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
				statups.put(SecondaryStat.산의씨앗,
						new Pair<Integer, Integer>((int) chr.getSkillCustomValue0(162101012), 0));
				chr.getClient().getSession()
						.writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, effect, chr));
				effect.applyTo(chr);
				break;
			}
			case 162101010:
			case 162111000:
			case 162111003:
			case 162121018: {
				if (skillid == 162121018 && chr.getBuffedValue(162121009)) {
					chr.cancelEffect(chr.getBuffedEffect(162121009));
				}
				effect.applyTo(chr, ret.plusPosition2);
				break;
			}
			case 162121017: {
				for (MapleMist mist : chr.getMap().getAllMistsThreadsafe()) {
					if (mist != null && mist.getSourceSkill() != null && mist.getSourceSkill().getId() == 162121018) {
						chr.getMap().removeMistByOwner(chr, 162121018);
						break;
					}
				}
				SkillFactory.getSkill(162121018).getEffect(skillLevel).applyTo(chr, ret.plusPosition2);
			}
			case 162101011:
			case 162121019: {
				// 나중에 고치기
				for (int size2 = slea.readByte(), k = 0; k < size2; ++k) {
					slea.readInt();
				}
				slea.skip(3);
				final int unks3 = slea.readInt();
				final int unk2 = slea.readInt();
				final MapleMist mist = chr.getMap().getMist(chr.getId(),
						(skillid == 162101011) ? 162101010 : 162121018);
				final Point pos2 = (mist != null) ? mist.getPosition() : pos;
				final List<SecondAtom> atoms3 = new ArrayList<SecondAtom>();
				for (int l = 0; l < ((skillid == 162101011) ? 4 : 5); ++l) {
					atoms3.add(new SecondAtom(0x15, c.getPlayer().getId(), 0, 0, skillid, 4000, 0, 1,
							new Point(pos2.x + Randomizer.rand(-250, 250), pos2.y + Randomizer.rand(-460, 0)),
							Arrays.asList(unks3, unk2)));
				}
				c.getPlayer().spawnSecondAtom(atoms3);
				break;
			}
			case 162121009: {
				final int onskill = chr.getBuffedValue(162101009) ? 162101009 : 162121018;
				if (chr.getBuffedValue(onskill)) {
					chr.cancelEffect(chr.getBuffedEffect(onskill));
				}
				chr.getMap().removeMist(onskill);
				effect.applyTo(chr);
				break;
			}
			case 162121006: {
				final int onskill = chr.getBuffedValue(162101006) ? 162101006 : 162121015;
				if (chr.getBuffedValue(onskill)) {
					chr.cancelEffect(chr.getBuffedEffect(onskill));
				}
				effect.applyTo(chr);
				break;
			}
			case 162101006:
			case 162121015: {
				pos = slea.readPos();
				final boolean facing = slea.readByte() == 1;
				if (chr.getBuffedValue(162121006)) {
					chr.cancelEffect(chr.getBuffedEffect(162121006));
				}
				effect.applyTo(chr, pos);
				break;
			}

			case 400011031: {
				if (chr.getBuffedValue(400011016)) {
					chr.changeCooldown(400011031, -effect.getCooldown(chr) / 2);
				}
				effect.applyTo(chr);
				break;
			}
			case 400001025:
			case 400001026:
			case 400001027:
			case 400001028:
			case 400001029:
			case 400001030: {
				final SecondaryStatEffect effz = SkillFactory.getSkill(400001024)
						.getEffect(chr.getSkillLevel(400001024));
				chr.getClient().send(CField.skillCooldown(effz.getSourceId(), effz.getCooldown(chr)));
				chr.addCooldown(effz.getSourceId(), System.currentTimeMillis(), effz.getCooldown(chr));
				effect.applyTo(chr);
				break;
			}
			case 101100101: {
				chr.ZeroSkillCooldown(skillid);
				effect.applyTo(chr);
				break;
			}
			case 27121012: {
				c.removeClickedNPC();
				NPCScriptManager.getInstance().dispose(c);
				c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 2007, "Luminus_Skill_Select2");
				break;
			}
			case 131001019: {
				pos = slea.readPos();
				for (int size2 = Randomizer.rand(effect.getX(), effect.getY()), k = 0; k < size2; ++k) {
					final int x = Randomizer.rand(pos.x - 700, pos.x + 700);
					final int y = Randomizer.rand(pos.y - 400, pos.y + 400);
					final MapleSummon summon = new MapleSummon(chr, 131001019, new Point(x, y),
							SummonMovementType.STATIONARY, (byte) 0, effect.getDuration());
					chr.getMap().spawnSummon(summon, effect.getDuration());
					chr.addSummon(summon);
				}
				effect.applyTo(chr);
				break;
			}
			case 131001026: {
				chr.removeSkillCustomInfo(skillid);
				effect.applyTo(chr, false);
				SkillFactory.getSkill(131003026).getEffect(1).applyTo(chr);
				break;
			}
			case 131001107:
			case 131001207: {
				final Point pos3 = slea.readPos();
				slea.skip(4);
				final boolean left = slea.readByte() == 1;
				final MapleMist newmist2 = new MapleMist(effect.calculateBoundingBox(pos3, left), c.getPlayer(), effect,
						effect.getDuration(), (byte) (left ? 1 : 0));
				newmist2.setPosition(pos3);
				newmist2.setDelay(12);
				c.getPlayer().getMap().spawnMist(newmist2, false);
				effect.applyTo(chr);
				break;
			}
			case 131001017: {
				final MapleSummon summon2 = chr.getSummon(131001017);
				final MapleSummon summon3 = chr.getSummon(131002017);
				if (summon2 != null && summon3 != null) {
					skillid = 131003017;
				} else if (summon2 != null && summon3 == null) {
					skillid = 131002017;
				} else {
					skillid = 131001017;
				}
				SkillFactory.getSkill(skillid).getEffect(1).applyTo(chr);
				break;
			}
			case 400011015: {
				if (chr.getBuffedValue(skillid)) {
					for (final MapleMonster monster : chr.getMap().getAllMonster()) {
						if (chr.getTruePosition().x + effect.getLt().x < monster.getTruePosition().x
								&& chr.getTruePosition().x - effect.getLt().x > monster.getTruePosition().x
								&& chr.getTruePosition().y + effect.getLt().y < monster.getTruePosition().y
								&& chr.getTruePosition().y - effect.getLt().y > monster.getTruePosition().y
								&& monster.getBuff(MonsterStatus.MS_Speed) == null) {
							final List<Pair<MonsterStatus, MonsterStatusEffect>> applys = new ArrayList<Pair<MonsterStatus, MonsterStatusEffect>>();
							applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_Speed,
									new MonsterStatusEffect(effect.getSourceId(), 6000, -99L)));
							monster.applyStatus(chr.getClient(), applys, effect);
						}
					}
					return;
				}
				effect.applyTo(chr);
				break;
			}
			case 400011087: {
				SkillFactory.getSkill(400011083).getEffect(1).applyTo(chr, (int) chr.getBuffLimit(400011083));
				break;
			}
			case 135001012: {
				final MapleMist newmist = new MapleMist(effect.calculateBoundingBox(ret.plusPosition2, ret.rlType == 1),
						c.getPlayer(), effect, effect.getDuration(), ret.rlType);
				newmist.setPosition(ret.plusPosition2);
				c.getPlayer().getMap().spawnMist(newmist, false);
				effect.applyTo(chr);
				break;
			}
			case 152111003: {
				chr.cancelEffect(chr.getBuffedEffect(SecondaryStat.CrystalBattery));
				chr.cancelEffect(chr.getBuffedEffect(152101000));
				chr.cancelEffect(chr.getBuffedEffect(152101008));
				effect.applyTo(chr);
				break;
			}
			case 152121041: {
				final Point pos4 = slea.readPos();
				effect.applyTo(chr, pos4, 6000);
				break;
			}
			case 152121005: {
				chr.cancelEffect(chr.getBuffedEffect(152001003));
				chr.cancelEffect(chr.getBuffedEffect(152101008));
				effect.applyTo(chr);
				for (int i2 = 0; i2 < 5; ++i2) {
					final MapleSummon summon4 = new MapleSummon(chr, 152121006,
							new Point(chr.getPosition().x, chr.getPosition().y), SummonMovementType.FOLLOW, (byte) 0,
							effect.getDuration());
					chr.getMap().spawnSummon(summon4, effect.getDuration());
					chr.addSummon(summon4);
				}
				break;
			}
			case 152101004: {
				slea.skip(5);
				final short x2 = slea.readShort();
				final short y2 = slea.readShort();
				int oid = 0;
				for (final MapleSummon summon : chr.getSummons()) {
					if (summon.getSkill() == 152101000) {
						oid = summon.getObjectId();
					}
				}
				chr.getMap().broadcastMessage(CField.CrystalTeleport(chr, oid, new Point(x2, y2), skillid));
				break;
			}
			case 152101003: {
				slea.skip(5);
				final short x3 = slea.readShort();
				final short y3 = slea.readShort();
				int oid = 0;
				int markinaoid = 0;
				for (final MapleSummon summon5 : chr.getSummons()) {
					if (summon5.getSkill() == 152101000) {
						oid = summon5.getObjectId();
					} else {
						if (summon5.getSkill() != 152101008) {
							continue;
						}
						markinaoid = summon5.getObjectId();
					}
				}
				if (chr.getBuffedValue(152101008)) {
					chr.getMap().broadcastMessage(CField.MarkinaMoveAttack(chr, markinaoid));
					chr.getMap().broadcastMessage(CField.CrystalControl(chr, markinaoid, new Point(x3, y3), 152101008));
				}
				chr.getMap().broadcastMessage(CField.CrystalControl(chr, oid, new Point(x3, y3), skillid));
				break;
			}
			case 150011074: {
				final int rskillid = Randomizer.rand(150011075, 150011078);
				SkillFactory.getSkill(rskillid).getEffect(1).applyTo(chr);
				break;
			}
			case 155101006: {
				if (!chr.getBuffedValue(155000007)) {
					SkillFactory.getSkill(155000007).getEffect(1).applyTo(chr);
				} else {
					chr.cancelEffect(SkillFactory.getSkill(155000007).getEffect(1));
				}
				c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
				break;
			}
			case 164121015: {
				final SecondaryStatEffect eff = SkillFactory.getSkill(164121008)
						.getEffect(chr.getSkillLevel(164121008));
				final int stack = (int) chr.getSkillCustomValue0(164121009);
				for (final MapleSummon s : chr.getMap().getAllSummonsThreadsafe()) {
					if (s != null && s.getOwner().getId() == chr.getId() && s.getSkill() == 164121008) {
						s.removeSummon(chr.getMap(), false);
						chr.getSummons().remove(s);
					}
				}
				if (chr.getParty() != null) {
					for (final MaplePartyCharacter chr2 : chr.getParty().getMembers()) {
						final MapleCharacter curChar = chr.getClient().getChannelServer().getPlayerStorage()
								.getCharacterById(chr2.getId());
						if (curChar != null && curChar.getMapId() == chr.getMapId()) {
							curChar.addHP(curChar.getStat().getCurrentMaxHp() / 100L * (stack * eff.getY()), true,
									false);
							curChar.addMP(curChar.getStat().getCurrentMaxMp(chr) / 100L * (stack * eff.getY()));
						}
					}
				} else {
					chr.addHP(chr.getStat().getCurrentMaxHp() / 100L * (stack * eff.getY()), true, false);
					chr.addMP(chr.getStat().getCurrentMaxMp(chr) / 100L * (stack * eff.getY()));
				}
				chr.removeSkillCustomInfo(164121008);
				chr.removeSkillCustomInfo(164121009);
				break;
			}
			case 164121011:
			case 164121012: {
				c.getSession().writeAndFlush((Object) CField.skillCooldown(164121006, 5000));
				chr.addCooldown(164121006, System.currentTimeMillis(), 5000L);
				if (skillid == 164121011) {
					effect.applyTo(chr);
					break;
				}
				for (final MapleSummon summon4 : chr.getMap().getAllSummonsThreadsafe()) {
					if (summon4.getSkill() == 164121011) {
						summon4.removeSummon(chr.getMap(), false);
						break;
					}
				}
				c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
				break;
			}
			case 37120059: {
				final List<RangeAttack> skills = new ArrayList<RangeAttack>();
				if (chr.getSkillCustomValue0(37121052) >= 1L) {
					skills.add(new RangeAttack(37120055, chr.getPosition(), -1, 0, 1));
				}
				if (chr.getSkillCustomValue0(37121052) >= 2L) {
					skills.add(new RangeAttack(37120056, chr.getPosition(), -1, 0, 1));
				}
				if (chr.getSkillCustomValue0(37121052) >= 3L) {
					skills.add(new RangeAttack(37120057, chr.getPosition(), -1, 0, 1));
				}
				if (chr.getSkillCustomValue0(37121052) >= 4L) {
					skills.add(new RangeAttack(37120058, chr.getPosition(), -1, 0, 1));
				}
				chr.getClient().send(CField.rangeAttack(37121052, skills));
				c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
				chr.removeSkillCustomInfo(37121052);
				break;
			}
			case 37121005: {
				chr.cancelEffectFromBuffStat(SecondaryStat.RwBarrier, 37000006);
				if (chr.getSkillCustomValue0(37000006) > 0L) {
					chr.addHP(chr.getStat().getCurrentMaxHp() / 2L + chr.getSkillCustomValue0(skillid));
				} else {
					chr.addHP(chr.getStat().getCurrentMaxHp() / 2L);
				}
				if (chr.getSkillLevel(37120050) > 0) {
					chr.changeCooldown(37121005, -40000);
				}
				chr.removeSkillCustomInfo(37000006);
				effect.applyTo(chr);
				break;
			}
			case 400011116: {
				chr.setSkillCustomInfo(400011116, effect.getY(), 0L);
				effect.applyTo(chr);
				break;
			}
			// 비숍리마스터 코딩
			case 2321015: {
				short size = slea.readShort();
				int plusduration = chr.getStat().getTotalInt() / 2500;
				for (int i = 0; i < size; i++) {
					final Point pos2 = slea.readIntPos();
					final MapleMist newmist2 = new MapleMist(effect.calculateBoundingBox(pos2, chr.isFacingLeft()),
							c.getPlayer(), effect, 5000 + (5000 * plusduration), (byte) (chr.isFacingLeft() ? 1 : 0));
					newmist2.setPosition(pos2);
					c.getPlayer().getMap().spawnMist(newmist2, false);
					effect.applyTo(chr);
				}
				chr.홀리워터스택 -= size;
				HashMap<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
				statups.put(SecondaryStat.HolyWater, new Pair<Integer, Integer>((int) chr.홀리워터스택, 0));
				chr.getClient().getSession().writeAndFlush(CWvsContext.BuffPacket.giveBuff(statups, null, chr));
				break;
			}
			case 37001001:
			case 37101001:
			case 37111003:
			case 37000010: {
				chr.Cylinder(37000010);
				effect.applyTo(chr);
				break;
			}
			case 400011103: {
				chr.setSkillCustomInfo(400011091, chr.getSkillCustomValue0(400011091) + 1L, 0L);
				chr.Cylinder(skillid);
				c.getSession().writeAndFlush((Object) CWvsContext.enableActions(chr));
				break;
			}
			case 400051068: {
				chr.removeSkillCustomInfo(400051069);
				chr.setSkillCustomInfo(400051068, effect.getX(), 0L);
				chr.MechCarrier(2000, false);
				effect.applyTo(chr);
				break;
			}
			case 33001001: {
				effect.applyTo(chr);
				break;
			}
			case 33111013:
			case 33121016: {
				final Point pos4 = slea.readPos();
				slea.skip(4);
				final boolean facing2 = slea.readByte() == 1;
				final MapleMist newmist2 = new MapleMist(effect.calculateBoundingBox(pos4, facing2), c.getPlayer(),
						effect, effect.getDuration(), (byte) (facing2 ? 1 : 0));
				newmist2.setPosition(pos4);
				c.getPlayer().getMap().spawnMist(newmist2, false);
				effect.applyTo(chr);
				break;
			}
			case 400021087: {
				chr.removeSkillCustomInfo(400021087);
				effect.applyTo(chr);
				break;
			}
			case 400021047: {
				final Point pos3 = slea.readPos();
				final Point pos5 = slea.readPos();
				final int facing3 = slea.readShort();
				slea.skip(1);
				final int type2 = slea.readInt();
				Vmatrixstackbuff(c, true, null);
				effect.applyTo(chr);
				if (type2 == 1) {
					Vmatrixstackbuff(c, true, null);
					final MapleSummon summon = new MapleSummon(chr, 400021047,
							(facing3 == 0) ? new Point(chr.getPosition().x + 700, chr.getPosition().y)
									: new Point(chr.getPosition().x - 600, chr.getPosition().y),
							SummonMovementType.STATIONARY, (byte) 0, effect.getDuration());
					chr.getMap().spawnSummon(summon, effect.getDuration());
					chr.addSummon(summon);
					break;
				}
				break;
			}
			case 400011010: {
				if (chr.getBuffedValue(skillid)) {
					chr.cancelEffect(effect);
					c.getSession().writeAndFlush((Object) CField.skillCooldown(skillid, linkEffect.getZ() * 1000));
					chr.addCooldown(skillid, System.currentTimeMillis(), linkEffect.getZ() * 1000);
					break;
				}
				effect.applyTo(chr);
				break;
			}
			case 400011112: {
				chr.setSkillCustomInfo(400011112, 0L, 0L);
				effect.applyTo(chr);
				break;
			}
			case 400001050: {
				c.getPlayer().removeSkillCustomInfo(400001050);
				effect.applyTo(chr);
				break;
			}
			case 31101002: {
				chr.setSkillCustomInfo(31101002, 0L, 700L);
				c.getSession().writeAndFlush((Object) CField.skillCooldown(skillid, linkEffect.getCooldown(chr)));
				chr.addCooldown(skillid, System.currentTimeMillis(), linkEffect.getCooldown(chr));
				break;
			}
			case 51111004: {
				effect.applyTo(chr);
				if (chr.getParty() != null) {
					for (final MaplePartyCharacter chr3 : chr.getParty().getMembers()) {
						final MapleCharacter curChar2 = chr.getClient().getChannelServer().getPlayerStorage()
								.getCharacterById(chr3.getId());
						if (curChar2 != null && curChar2.getBuffedValue(51111008)) {
							curChar2.cancelEffectFromBuffStat(SecondaryStat.MichaelSoulLink);
							SkillFactory.getSkill(51111008).getEffect(chr.getSkillLevel(51111008)).applyTo(chr,
									curChar2);
						}
					}
					break;
				}
				break;
			}
			case 51120057: {
				final Point pos4 = slea.readPos();
				slea.skip(4);
				final boolean facing2 = slea.readByte() == 1;
				boolean active = false;
				if (c.getPlayer().getInfoQuest(1544) == null) {
					active = true;
				} else if (c.getPlayer().getInfoQuest(1544).contains("51121009=0")) {
					active = true;
				}
				if (active) {
					final SecondaryStatEffect a2 = SkillFactory.getSkill(51120057)
							.getEffect(chr.getSkillLevel(51120057));
					final MapleMist newmist3 = new MapleMist(a2.calculateBoundingBox(pos4, facing2), c.getPlayer(), a2,
							a2.getDuration(), (byte) (facing2 ? 1 : 0));
					newmist3.setPosition(pos4);
					c.getPlayer().getMap().removeMist(skillid);
					c.getPlayer().getMap().spawnMist(newmist3, false);
				}
				effect.applyTo(chr);
				break;
			}
			case 15121054: {
				chr.removeCooldown(15120003);
				effect.applyTo(chr);
				break;
			}
			case 14121054: {
				if (chr.getBuffedValue(14111024)) {
					chr.cancelEffect(chr.getBuffedEffect(14111024));
				}
				SkillFactory.getSkill(14111024).getEffect(20).applyTo(chr);
				final MapleSummon tosummon2 = new MapleSummon(chr, 14121055, chr.getTruePosition(),
						SummonMovementType.ShadowServant, (byte) 0, effect.getDuration());
				chr.addSummon(tosummon2);
				chr.getMap().spawnSummon(tosummon2, effect.getDuration());
				final MapleSummon tosummon3 = new MapleSummon(chr, 14121056, chr.getTruePosition(),
						SummonMovementType.ShadowServant, (byte) 0, effect.getDuration());
				chr.addSummon(tosummon3);
				chr.getMap().spawnSummon(tosummon3, effect.getDuration());
				effect.applyTo(chr);
				break;
			}
			case 400011055: { // 엘리시온
				// chr.removeCooldown(11121052);
				if (!chr.getBuffedValue(skillid)) {
					chr.EllisionStack = 0;
					effect.applyTo(chr);
				}
				int count = 0;
				int[] skills = { 11001022, 11120019, 11110031, 11100034, 11121054 };

				for (int i = 0; i < skills.length; i++) {
					if (chr.getSkillLevel(skills[i]) > 0) {
						if (skills[i] == 11001022) {
							count += 2;
						} else if (skills[i] == 11121054) {
							if (chr.getBuffedValue(11121054) != false) {
								count += 5;
							}
						} else {
							count += 1;
						}
					}
					if (count > 10) {
						count = 10;
					}
				}
				chr.setCosmicCount(count);
				break;
			}
			case 400001043: {
				chr.removeSkillCustomInfo(skillid);
				effect.applyTo(chr);
				break;
			}
			case 400031066: { //
				final List<SecondAtom2> at = SkillFactory.getSkill(400031066).getSecondAtoms();
				chr.removeSkillCustomInfo(400031066);
				chr.removeSkillCustomInfo(400031067);
				chr.removeSkillCustomInfo(400031068);
				chr.createSecondAtom(at, new Point(chr.getPosition().x,
						chr.getMap().getFootholds().findBelow(chr.getPosition()).getY1()));
				chr.cancelEffect(effect);
				break;
			}
			case 63001002: {
				chr.getMap().broadcastMessage(chr, CField.EffectPacket.showEffect(chr, 0, skillid, 1, 0, 0,
						(byte) (chr.isFacingLeft() ? 1 : 0), false, chr.getPosition(), null, null), false);
				effect.applyTo(chr);
				break;
			}
			case 63001004: {
				chr.getMap().broadcastMessage(chr, CField.EffectPacket.showEffect(chr, 0, skillid, 1, 0, 0,
						slea.readByte(), false, new Point(slea.readShort(), slea.readShort()), null, null), false);
				effect.applyTo(chr);
				break;
			}
			case 63101104:
			case 63141109: { // [발현] 스캐터링 샷 VI 구현
				final List<SecondAtom2> at = SkillFactory.getSkill(skillid).getSecondAtoms();
				int count = slea.readByte();
				if (count >= 6) {
					count = 5;
				}
				for (int m = 0; m < count; ++m) {
					at.get(m).setTarget(slea.readInt());
				}
				chr.getMap().broadcastMessage(chr, CField.EffectPacket.showEffect(chr, 0, skillid, 1, 0, 0,
						(byte) (chr.isFacingLeft() ? 1 : 0), false, chr.getPosition(), null, null), false);
				chr.createSecondAtom(at, chr.getPosition());
				effect.applyTo(chr);
				break;
			}
			case 63101001: {
				chr.handlePossession(3);
				effect.applyTo(chr);
				break;
			}
			case 400041002: {
				SecondaryStatEffect effect2 = SkillFactory.getSkill(skillid).getEffect(chr.getSkillLevel(skillid));
				if (chr.getSkillCustomValue0(skillid) > 0L) {
					if (chr.getSkillCustomValue0(skillid) == 3L) {
						effect2 = SkillFactory.getSkill(400041003).getEffect(chr.getSkillLevel(skillid));
					} else if (chr.getSkillCustomValue0(skillid) == 2L) {
						effect2 = SkillFactory.getSkill(400041004).getEffect(chr.getSkillLevel(skillid));
					} else if (chr.getSkillCustomValue0(skillid) == 1L) {
						effect2 = SkillFactory.getSkill(400041005).getEffect(chr.getSkillLevel(skillid));
					}
					effect2.applyTo(chr);
					chr.removeCooldown(skillid);
					break;
				}
				effect.applyTo(chr);
				break;
			}
			case 3311002:
			case 3311003:
			case 3321006:
			case 3321007: {
				Vmatrixstackbuff(c, true, slea);
				break;
			}
			case 2321007: {
				final int type3 = slea.readInt();
				final boolean a3 = chr.getBuffedValue(2321054);
				long healhp = chr.getStat().getCurrentMaxHp() / 100L * 20L;
				if (a3) {
					healhp = chr.getStat().getCurrentMaxHp() / 100L * 20L / 100L * 40L;
				}
				if (type3 == 48 && chr.getParty() != null && chr.getMapId() != 450013700) {
					for (final MaplePartyCharacter chr4 : chr.getParty().getMembers()) {
						final MapleCharacter curChar3 = chr.getClient().getChannelServer().getPlayerStorage()
								.getCharacterById(chr4.getId());
						if (curChar3 != null && chr.getMapId() == curChar3.getMapId() && curChar3.isAlive()) {
							if (curChar3.hasDisease(SecondaryStat.Undead)) {
								curChar3.addHP(-healhp);
							} else {
								curChar3.addHP(healhp);
							}
						}
					}
				}
				if (chr.isAlive()) {
					if (chr.hasDisease(SecondaryStat.Undead)) {
						chr.addHP(-healhp);
					} else {
						chr.addHP(healhp);
					}
				}
				effect.applyTo(chr);
				break;
			}
			case 2001009: {
				if (chr.getBuffedValue(2201009)) {
					final int y4 = chr.getPosition().y;
					final int x_max = chr.getPosition().x;
					final int x_min = x_max - 200;
					final int x_center = (x_max - x_min) / 2 + x_min;
					final SecondaryStatEffect a4 = SkillFactory.getSkill(2201009).getEffect(chr.getSkillLevel(2201009));
					if (Randomizer.isSuccess(60)) {
						MapleMist mist2 = new MapleMist(
								a4.calculateBoundingBox(new Point(x_max, y4), chr.isFacingLeft()), chr, a4, 6000,
								(byte) (chr.isFacingLeft() ? 1 : 0));
						mist2.setDelay(1);
						chr.getMap().spawnMist(mist2, false);
						mist2 = new MapleMist(a4.calculateBoundingBox(new Point(x_min, y4), chr.isFacingLeft()), chr,
								a4, 6000, (byte) (chr.isFacingLeft() ? 1 : 0));
						mist2.setDelay(1);
						chr.getMap().spawnMist(mist2, false);
						mist2 = new MapleMist(a4.calculateBoundingBox(new Point(x_center, y4), chr.isFacingLeft()), chr,
								a4, 6000, (byte) (chr.isFacingLeft() ? 1 : 0));
						mist2.setDelay(1);
						chr.getMap().spawnMist(mist2, false);
					}
					chr.getClient().getSession().writeAndFlush((Object) CWvsContext.enableActions(chr));
					break;
				}
				effect.applyTo(chr);
				break;
			}
			case 1121010: {
				chr.handleOrbconsume(1121015);
				effect.applyTo(chr);
				break;
			}
			case 1221054: {
				chr.GiveHolyUnityBuff(effect, skillid);
				effect.applyTo(chr);
				break;
			}
			case 1221015: {
				chr.GiveHolyUnityBuff(effect, skillid);
				effect.applyTo(chr);
				break;
			}
			case 400011131: {
				final byte size3 = slea.readByte();
				final MapleAtom atom = new MapleAtom(false, chr.getId(), 67, true, 400011131, chr.getTruePosition().x,
						chr.getTruePosition().y);
				final List<Integer> monsters = new ArrayList<Integer>();
				for (int i3 = 0; i3 < size3; ++i3) {
					monsters.add(slea.readInt());
				}
				final short delay = slea.readShort();
				for (int j = 0; j < size3; ++j) {
					atom.addForceAtom(new ForceAtom(0, 1, 12, 62, delay, chr.getTruePosition()));
				}
				slea.skip(1);
				atom.setDwUnknownByte(slea.readByte());
				atom.setDwUnknownInteger(unk);
				atom.setDwTargets(monsters);
				chr.getMap().spawnMapleAtom(atom);
				Vmatrixstackbuff(c, true, null);
				break;
			}
			case 1321015: {
				chr.removeCooldown(1321013);
				effect.applyToBuff(chr);
				break;
			}
			case 400001011: {
				final List<Triple<Integer, Integer, Integer>> mobList = new ArrayList<Triple<Integer, Integer, Integer>>();
				chr.getClient().getSession()
						.writeAndFlush((Object) CField.bonusAttackRequest(400001011, mobList, true, 0, new int[0]));
				chr.cancelEffect(chr.getBuffedEffect(400001010));
				break;
			}
			case 1211013: {
				pos = slea.readPos();
				chr.setPosition(pos);
				final byte count2 = slea.readByte();
				SecondaryStatEffect bonusTime = null;
				if (chr.getSkillLevel(1220043) > 0) {
					bonusTime = SkillFactory.getSkill(1220043).getEffect(chr.getSkillLevel(1220043));
				}
				SecondaryStatEffect bonusChance = null;
				if (chr.getSkillLevel(1220044) > 0) {
					bonusChance = SkillFactory.getSkill(1220044).getEffect(chr.getSkillLevel(1220044));
				}
				SecondaryStatEffect enhance = null;
				if (chr.getSkillLevel(1220045) > 0) {
					enhance = SkillFactory.getSkill(1220045).getEffect(chr.getSkillLevel(1220045));
				}
				MonsterStatus ms = null;
				for (byte i4 = 0; i4 < count2; ++i4) {
					final MapleMonster monster2 = chr.getMap().getMonsterByOid(slea.readInt());
					final List<Triple<MonsterStatus, MonsterStatusEffect, Integer>> statusz = new ArrayList<Triple<MonsterStatus, MonsterStatusEffect, Integer>>();
					if (monster2 != null) {
						ms = MonsterStatus.MS_IndiePdr;
						statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Integer>(ms,
								new MonsterStatusEffect(skillid,
										effect.getDuration() + ((bonusTime != null) ? bonusTime.getDuration() : 0)),
								effect.getX() + ((enhance != null) ? enhance.getX() : 0)));
						ms = MonsterStatus.MS_IndieMdr;
						statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Integer>(ms,
								new MonsterStatusEffect(skillid,
										effect.getDuration() + ((bonusTime != null) ? bonusTime.getDuration() : 0)),
								effect.getX() + ((enhance != null) ? enhance.getX() : 0)));
						ms = MonsterStatus.MS_Pad;
						statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Integer>(ms,
								new MonsterStatusEffect(skillid,
										effect.getDuration() + ((bonusTime != null) ? bonusTime.getDuration() : 0)),
								effect.getX() + ((enhance != null) ? enhance.getX() : 0)));
						ms = MonsterStatus.MS_Mad;
						statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Integer>(ms,
								new MonsterStatusEffect(skillid,
										effect.getDuration() + ((bonusTime != null) ? bonusTime.getDuration() : 0)),
								effect.getX() + ((enhance != null) ? enhance.getX() : 0)));
						ms = MonsterStatus.MS_Blind;
						statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Integer>(ms,
								new MonsterStatusEffect(skillid,
										effect.getDuration() + ((bonusTime != null) ? bonusTime.getDuration() : 0)),
								effect.getZ() + ((enhance != null) ? enhance.getY() : 0)));
						final List<Pair<MonsterStatus, MonsterStatusEffect>> applys2 = new ArrayList<Pair<MonsterStatus, MonsterStatusEffect>>();
						for (final Triple<MonsterStatus, MonsterStatusEffect, Integer> status : statusz) {
							if (status.left != null && status.mid != null && Randomizer.isSuccess(
									effect.getProp() + ((bonusChance != null) ? bonusChance.getProp() : 0))) {
								status.mid.setValue(status.right);
								applys2.add(new Pair<MonsterStatus, MonsterStatusEffect>(status.left, status.mid));
							}
						}
						monster2.applyStatus(c, applys2, effect);
					}
				}
				effect.applyToBuff(chr);
				break;
			}
			case 1121016:
			case 1221014:
			case 1321014:
			case 51111005: {
				pos = slea.readPos();
				chr.setPosition(pos);
				for (byte size3 = slea.readByte(), i5 = 0; i5 < size3; ++i5) {
					final MapleMonster mob = chr.getMap().getMonsterByOid(slea.readInt());
					if (mob != null) {
						mob.applyStatus(c, MonsterStatus.MS_MagicCrash,
								new MonsterStatusEffect(effect.getSourceId(), effect.getDuration()),
								effect.getDuration(), effect);
					}
				}
				effect.applyToBuff(chr);
				break;
			}
			case 2121052: { // 메기도 플레임
				List<MapleMonster> monsters = new ArrayList<>();
				int size = slea.readByte();
				boolean sort = false;
				for (byte i = 0; i < size; ++i) {
					MapleMonster mob = chr.getMap().getMonsterByOid(slea.readInt());

					monsters.add(mob);
				}
				slea.skip(3);

				List<SecondAtom2> at = SkillFactory.getSkill(2121052).getSecondAtoms();
				List<SecondAtom2> spawn = new ArrayList<>();
				int index = 0;
				for (SecondAtom2 atom : at) {
					atom.setTarget(monsters.get(0).getObjectId());
					if (index < 3) {
						spawn.add(atom);
					}
					index++;
				}
				chr.setSkillCustomInfo(skillid, 3, 0);
				chr.createSecondAtom(spawn, slea.readIntPos(), chr.isFacingLeft());
				effect.applyToBuff(chr);
				break;
			}

			case 27111101: {
				pos = slea.readPos();
				chr.setPosition(pos);
				if (chr.getMapId() != 450013700 && chr.getParty() != null) {
					for (final MaplePartyCharacter chr3 : chr.getParty().getMembers()) {
						if (chr3.isOnline() && chr3.getMapid() == chr.getMapId()) {
							final MapleCharacter chr5 = chr.getClient().getChannelServer().getPlayerStorage()
									.getCharacterByName(chr3.getName());
							if (chr5 == null || !chr3.isOnline()
									|| chr.getTruePosition().x + 450 <= chr5.getTruePosition().x
									|| chr.getTruePosition().x - 450 >= chr5.getTruePosition().x
									|| chr.getTruePosition().y + 400 <= chr5.getTruePosition().y
									|| chr.getTruePosition().y - 400 >= chr5.getTruePosition().y) {
								continue;
							}
							chr5.getClient().getSession()
									.writeAndFlush((Object) CField.EffectPacket.showEffect(chr5, 0, skillid, 4, 0, 0,
											(byte) ((chr5.getTruePosition().x > pos.x) ? 1 : 0), true,
											chr5.getPosition(), null, null));
							chr5.getMap().broadcastMessage(chr5,
									CField.EffectPacket.showEffect(chr5, 0, skillid, 4, 0, 0,
											(byte) ((chr5.getTruePosition().x > pos.x) ? 1 : 0), false,
											chr5.getPosition(), null, null),
									false);
							if (chr5.hasDisease(SecondaryStat.Undead)) {
								chr5.addHP(-chr.getStat().AfterStatWatk(chr) * 8);
							} else {
								chr5.addHP(chr.getStat().AfterStatWatk(chr) * 8);
							}
							if (chr5.getDisease(SecondaryStat.GiveMeHeal) == null) {
								continue;
							}
							chr5.cancelDisease(SecondaryStat.GiveMeHeal);
						}
					}
				}
				if (chr.hasDisease(SecondaryStat.Undead)) {
					chr.addHP(-chr.getStat().AfterStatWatk(chr) * 8);
				} else {
					chr.addHP(chr.getStat().AfterStatWatk(chr) * 8);
				}
				effect.applyTo(chr);
				break;
			}
			case 2301002: {
				int inside = 0;
				int cool = 4000;
				pos = slea.readPos();
				if (chr.getMapId() != 450013700 && chr.getParty() != null) {
					for (final MaplePartyCharacter chr2 : chr.getParty().getMembers()) {
						if (chr2.isOnline() && chr.getId() != chr2.getPlayer().getId()
								&& chr2.getMapid() == chr.getMapId()) {
							final MapleCharacter chr6 = chr.getClient().getChannelServer().getPlayerStorage()
									.getCharacterByName(chr2.getName());
							if (chr6 == null || !chr2.getPlayer().isAlive()
									|| chr.getTruePosition().x + 450 <= chr6.getTruePosition().x
									|| chr.getTruePosition().x - 450 >= chr6.getTruePosition().x
									|| chr.getTruePosition().y + 300 <= chr6.getTruePosition().y
									|| chr.getTruePosition().y - 300 >= chr6.getTruePosition().y) {
								continue;
							}
							chr6.getClient().getSession()
									.writeAndFlush((Object) CField.EffectPacket.showEffect(chr6, 0, skillid, 4, 0, 0,
											(byte) ((chr6.getTruePosition().x > pos.x) ? 1 : 0), true,
											chr6.getPosition(), null, null));
							chr6.getMap().broadcastMessage(chr6,
									CField.EffectPacket.showEffect(chr6, 0, skillid, 4, 0, 0,
											(byte) ((chr6.getTruePosition().x > pos.x) ? 1 : 0), false,
											chr6.getPosition(), null, null),
									false);
							++inside;
							if (chr6.hasDisease(SecondaryStat.Undead)) {
								chr6.addHP(-chr.getStat().AfterStatWatk(chr) * 3);
							} else {
								chr6.addHP(chr.getStat().AfterStatWatk(chr) * 3);
							}
							if (chr6.getDisease(SecondaryStat.GiveMeHeal) == null) {
								continue;
							}
							chr6.cancelDisease(SecondaryStat.GiveMeHeal);
						}
					}
					if (inside > 1) {
						cool -= 2000;
					}
				}
				c.getSession().writeAndFlush((Object) CField.skillCooldown(skillid, cool));
				chr.addCooldown(skillid, System.currentTimeMillis(), cool);
				if (chr.hasDisease(SecondaryStat.Undead)) {
					chr.addHP(-chr.getStat().AfterStatWatk(chr) * 3);
				} else {
					chr.addHP(chr.getStat().AfterStatWatk(chr) * 3);
				}
				effect.applyTo(chr);
				break;
			}
			case 3011004:
			case 3300002:
			case 3321003: // 카디널 디스차지
			case 3341004: // 카디널 디스차지 VI
			{
				slea.skip(4);
				byte size = slea.readByte();
				final List<ForceAtom> atoms = new ArrayList<ForceAtom>();
				final List<Integer> objectIds = new ArrayList<Integer>();
				for (int i = 0; i < size; i++) {
					objectIds.add(slea.readInt());
					slea.skip(4); // monsterid
					slea.skip(39); // ??
				}
				effect.applyTo(chr);
				boolean success = chr.getSkillLevel(3341002) > 0 ? Randomizer.isSuccess(41) : false;
				MapleAtom atom = new MapleAtom(false, chr.getId(), success ? 87 : 57, true, success ? 3341005 : 3310004,
						chr.getTruePosition().x, chr.getTruePosition().y);
				List<Integer> monsters = new ArrayList<>();

				effect.applyTo(chr);
				c.getSession().writeAndFlush(CWvsContext.enableActions(chr, true, false));
				if (chr.문양 == 2 && chr.getSkillLevel(3310004) > 0 && !objectIds.isEmpty()) {
					SecondaryStatEffect editionalBlast = SkillFactory.getSkill(3310004)
							.getEffect(chr.getSkillLevel(3310004));
					int bullet = editionalBlast.getBulletCount();
					if (chr.getBuffedValue(3321034)) {
						++bullet;
					}
					boolean check = chr.getSkillLevel(3341002) > 0 ? success : editionalBlast.makeChanceResult();
					if (check) {
						for (byte i = 0; i < bullet; i++) {
							MapleMapObject mob = null;
							if (objectIds.size() > i) {
								mob = chr.getMap().getMonsterByOid(objectIds.get(i));
							}
							monsters.add(mob != null ? mob.getObjectId() : 0);
							atom.addForceAtom(new ForceAtom(2, 0x2A, 4, 0, 0x3C,
									mob != null ? mob.getTruePosition() : chr.getTruePosition()));
						}
						atom.setDwTargets(monsters);
						chr.getMap().spawnMapleAtom(atom);
					}
				}
				atom = new MapleAtom(false, chr.getId(), 56, true, skillid, chr.getTruePosition().x,
						chr.getTruePosition().y);
				monsters.clear();
				for (byte i = 0; i < objectIds.size(); i++) {
					monsters.add(objectIds.get(i));
					atom.addForceAtom(new ForceAtom(2, 0x17, 0xA, Randomizer.rand(5, 15), 0x3C));
				}
				atom.setDwTargets(monsters);
				chr.getMap().spawnMapleAtom(atom);

				MapleCharacter.문양(c, skillid);
				break;
			}
			case 4111009:
			case 5201008:
			case 14110031:
			case 14111025: {
				final int projectile = slea.readInt();
				if (!MapleInventoryManipulator.removeById(c, MapleInventoryType.USE, projectile,
						effect.getBulletConsume(), false, true)) {
					chr.dropMessage(5, "\ubd88\ub9bf\uc774 \ubd80\uc871\ud569\ub2c8\ub2e4.");
				} else {
					effect.applyTo(chr, projectile);
				}
				c.getSession().writeAndFlush((Object) CWvsContext.enableActions(chr));
				break;
			}
			case 4221019:
			case 4211006:
			case 4241006:
			case 4241007: {
				if (chr.getBuffedValue(4221018)) {
					SkillFactory.getSkill(4221020).getEffect(1).applyTo(chr, false, false);
				}
				chr.getClient().getSession().writeAndFlush((Object) CField.EffectPacket.showEffect(chr, 0, skillid, 1,
						0, 0, (byte) (chr.isFacingLeft() ? 1 : 0), true, chr.getTruePosition(), null, null));
				chr.getMap().broadcastMessage(chr, CField.EffectPacket.showEffect(chr, 0, skillid, 1, 0, 0,
						(byte) (chr.isFacingLeft() ? 1 : 0), false, chr.getTruePosition(), null, null), false);
				final SecondaryStatEffect effect2 = SkillFactory.getSkill(4210014).getEffect(1);
				final List<MapleMapObject> drops = c.getPlayer().getMap().getMapObjectsInRange(
						c.getPlayer().getPosition(), 500000.0, Arrays.asList(MapleMapObjectType.ITEM));
				final List<MapleMapItem> remove = new ArrayList<MapleMapItem>();
				for (final MapleMapItem o : c.getPlayer().getPickPocket()) {
					boolean active2 = false;
					for (final MapleMapItem o2 : c.getPlayer().getMap().getAllItems()) {
						if (o2.isPickpoket() && o2.getObjectId() == o.getObjectId()) {
							active2 = true;
							break;
						}
					}
					if (!active2) {
						remove.add(o);
					}
				}
				for (final MapleMapItem o : remove) {
					c.getPlayer().getPickPocket().remove(o);
				}
				final List<MapleMapItem> allmesos = new ArrayList<MapleMapItem>();
				int max = effect.getBulletCount();
				if (chr.getSkillLevel(4220045) > 0) {
					max += SkillFactory.getSkill(4220045).getEffect(chr.getSkillLevel(4220045)).getBulletCount();
				}
				for (int i = 0; i < drops.size(); ++i) {
					final MapleMapItem drop = (MapleMapItem) drops.get(i);
					if (drop.isPickpoket() && drop.getOwner() == c.getPlayer().getId() && allmesos.size() < max) {
						allmesos.add(drop);
					}
				}
				final MapleAtom atom3 = new MapleAtom(false, chr.getId(), chr.getBuffedValue(4221018) ? 75 : 12, true,
						4210014, chr.getTruePosition().x, chr.getTruePosition().y);
				final List<Integer> monsters3 = new ArrayList<Integer>();
				final List<MapleMonster> mobs = new ArrayList<MapleMonster>();
				for (final MapleMonster mob3 : chr.getMap().getAllMonster()) {
					if (chr.getTruePosition().x + effect2.getLt().x < mob3.getTruePosition().x
							&& chr.getTruePosition().x - effect2.getLt().x > mob3.getTruePosition().x
							&& chr.getTruePosition().y + 550 > mob3.getTruePosition().y
							&& chr.getTruePosition().y - 550 < mob3.getTruePosition().y && mob3.isAlive()) {
						mobs.add(mob3);
					}
				}
				if (mobs.isEmpty()) {
					break Label_14276;
				}
				for (int i8 = 0; i8 < allmesos.size(); ++i8) {
					final int randmob_remove = Randomizer.rand(0, Math.max(0, mobs.size() - 1));
					monsters3.add((mobs.get(randmob_remove) != null) ? mobs.get(randmob_remove).getObjectId() : 0);
					atom3.addForceAtom(new ForceAtom(1, 42 + (Randomizer.nextBoolean() ? 1 : 0), 4,
							Randomizer.rand(10, 65), 300, allmesos.get(i8).getTruePosition()));
				}
				for (int i8 = 0; i8 < allmesos.size(); ++i8) {
					chr.getMap()
							.broadcastMessage(CField.removeItemFromMap(allmesos.get(i8).getObjectId(), 0, chr.getId()));
					chr.getMap().removeMapObject(allmesos.get(i8));
					chr.RemovePickPocket(allmesos.get(i8));
				}
				effect.applyTo(chr);
				c.getSession().writeAndFlush((Object) CWvsContext.enableActions(chr, true, false));
				if (monsters3.isEmpty()) {
					return;
				}
				final SecondaryStatEffect eff2 = chr.getBuffedEffect(SecondaryStat.PickPocket);
				atom3.setDwTargets(monsters3);
				chr.getMap().spawnMapleAtom(atom3);
				c.getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(eff2.getStatups(), eff2, chr));
				break;
			}
			case 4221052: {
				c.getPlayer().setSkillCustomInfo(4221052, chr.getPosition().x, 12000L);
				c.getSession().writeAndFlush((Object) CWvsContext.onSkillUseResult(skillid));
				effect.applyTo(chr);
				break;
			}
			case 4331006: {
				c.getSession().writeAndFlush((Object) CWvsContext.onSkillUseResult(skillid));
				effect.applyTo(chr);
				break;
			}
			case 11111023: {
				slea.skip(1); // 1.2.390 ++
				slea.readMapleAsciiString(); // 1.2.390 ++
				slea.skip(4); // 1.2.390 ++
				pos = slea.readPos();
				chr.setPosition(pos);
				for (byte size3 = slea.readByte(), i5 = 0; i5 < size3; ++i5) {
					final MapleMonster mob = chr.getMap().getMonsterByOid(slea.readInt());
					final List<Pair<MonsterStatus, MonsterStatusEffect>> applys3 = new ArrayList<Pair<MonsterStatus, MonsterStatusEffect>>();
					int duration = effect.getDuration();
					if (mob != null) {
						if (chr.getSkillLevel(11120043) > 0) {
							duration += SkillFactory.getSkill(11120043).getEffect(1).getDuration();
						}
						applys3.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_TrueSight,
								new MonsterStatusEffect(effect.getSourceId(), duration,
										(chr.getSkillLevel(11120045) > 0)
												? (-SkillFactory.getSkill(11120044).getEffect(1).getW())
												: 0L)));
						applys3.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_IndieUNK,
								new MonsterStatusEffect(effect.getSourceId(), duration, effect.getS())));
						if (chr.getSkillLevel(11120045) > 0) {
							applys3.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_IndiePdr,
									new MonsterStatusEffect(effect.getSourceId(), duration,
											-(effect.getV() + SkillFactory.getSkill(11120045).getEffect(1).getW()))));
							applys3.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_IndieMdr,
									new MonsterStatusEffect(effect.getSourceId(), duration,
											-(effect.getV() + SkillFactory.getSkill(11120045).getEffect(1).getW()))));
						} else {
							applys3.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_IndiePdr,
									new MonsterStatusEffect(effect.getSourceId(), duration, -effect.getV())));
							applys3.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_IndieMdr,
									new MonsterStatusEffect(effect.getSourceId(), duration, -effect.getV())));
						}
						mob.applyStatus(c, applys3, effect);
					}
				}
				effect.applyToBuff(chr);
				break;
			}
			case 22140013: {
				chr.addCooldown(22111017, System.currentTimeMillis(), 2000L);
				c.getSession().writeAndFlush((Object) CField.skillCooldown(22111017, 2000));
				effect.applyToBuff(chr);
				break;
			}
			case 22110013: {
				for (byte size3 = slea.readByte(), i5 = 0; i5 < size3; ++i5) {
					final MapleMonster mob = chr.getMap().getMonsterByOid(slea.readInt());
					if (mob != null) {
						mob.applyStatus(c, MonsterStatus.MS_Weakness,
								new MonsterStatusEffect(effect.getSourceId(), effect.getDuration()), effect.getX(),
								effect);
					}
				}
				chr.addCooldown(22111017, System.currentTimeMillis(), 2000L);
				c.getSession().writeAndFlush((Object) CField.skillCooldown(22111017, 2000));
				effect.applyToBuff(chr);
				break;
			}
			case 22141017:
			case 22170070: {
				final List<MapleMagicWreck> removes = new ArrayList<MapleMagicWreck>();
				final List<Integer> monsters4 = new ArrayList<Integer>();
				final List<MapleMapObject> mobjects = c.getPlayer().getMap().getMapObjectsInRange(
						c.getPlayer().getPosition(), 600000.0, Arrays.asList(MapleMapObjectType.MONSTER));
				final MapleAtom atom2 = new MapleAtom(false, chr.getId(), (skillid == 22141017) ? 23 : 24, true,
						skillid, chr.getTruePosition().x, chr.getTruePosition().y);
				for (final MapleMagicWreck mw : chr.getMap().getAllFieldThreadsafe()) {
					if (mw.getChr().getId() == chr.getId() && mw.getSourceid() == skillid) {
						atom2.addForceAtom(new ForceAtom(1, Randomizer.rand(40, 44), Randomizer.rand(3, 5),
								Randomizer.rand(7, 330), 500, mw.getTruePosition()));
						removes.add(mw);
						final int randmob_remove2 = Randomizer.rand(0, mobjects.size() - 1);
						monsters4.add(mobjects.get(randmob_remove2).getObjectId());
					}
				}
				if (monsters4.isEmpty()) {
					effect.applyTo(chr);
					return;
				}
				atom2.setDwTargets(monsters4);
				chr.getMap().spawnMapleAtom(atom2);
				c.getPlayer().getMap().broadcastMessage(CField.removeMagicWreck(chr, removes));
				for (final MapleMagicWreck re : removes) {
					chr.getMap().getWrecks().remove(re);
					chr.getMap().removeMapObject(re);
				}
				effect.applyTo(chr);
				break;
			}
			case 22170064: {
				for (final MapleMist mist3 : chr.getMap().getAllMistsThreadsafe()) {
					if (mist3.getSourceSkill() != null && mist3.getSourceSkill().getId() == 22170093
							&& chr.getId() == mist3.getOwnerId()) {
						chr.getMap().broadcastMessage(CField.removeMist(mist3));
						chr.getMap().removeMapObject(mist3);
						break;
					}
				}
				SkillFactory.getSkill(22170093).getEffect(20).applyTo(chr);
				chr.addCooldown(22111017, System.currentTimeMillis(), 2000L);
				c.getSession().writeAndFlush((Object) CField.skillCooldown(22111017, 2000));
				c.getPlayer().getMap().broadcastMessage(c.getPlayer(), CField.EffectPacket.showEffect(c.getPlayer(), 0,
						skillid, 1, 0, 0, (byte) 0, false, null, null, null), false);
				break;
			}
			case 24121007: {
				pos = slea.readPos();
				final byte size3 = slea.readByte();
				final List<Pair<MonsterStatus, MonsterStatusEffect>> mses = new ArrayList<Pair<MonsterStatus, MonsterStatusEffect>>();
				for (int m = 0; m < size3; ++m) {
					final MapleMonster life = chr.getMap().getMonsterByOid(slea.readInt());
					if (life != null) {
						if (life.isBuffed(MonsterStatus.MS_PImmune)) {
							mses.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_PImmune,
									life.getBuff(MonsterStatus.MS_PImmune)));
						}
						if (life.isBuffed(MonsterStatus.MS_MImmune)) {
							mses.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_MImmune,
									life.getBuff(MonsterStatus.MS_MImmune)));
						}
						if (life.isBuffed(MonsterStatus.MS_PCounter)) {
							mses.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_PCounter,
									life.getBuff(MonsterStatus.MS_PCounter)));
						}
						if (life.isBuffed(MonsterStatus.MS_MCounter)) {
							mses.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_MCounter,
									life.getBuff(MonsterStatus.MS_MCounter)));
						}
						if (mses.size() > 0) {
							life.cancelStatus(mses);
						}
					}
				}
				effect.applyToBuff(chr);
				break;
			}
			case 25100002: {
				effect.applyTo(chr);
				for (byte size3 = slea.readByte(), i5 = 0; i5 < size3; ++i5) {
					final MapleMonster mob = chr.getMap().getMonsterByOid(slea.readInt());
					final List<Pair<MonsterStatus, MonsterStatusEffect>> applys3 = new ArrayList<Pair<MonsterStatus, MonsterStatusEffect>>();
					if (mob != null) {
						applys3.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_Speed,
								new MonsterStatusEffect(effect.getSourceId(), 5000, -linkEffect.getS())));
						mob.applyStatus(c, applys3, effect);
					}
				}
				break;
			}
			case 31221001: {
				pos = slea.readPos();
				chr.setPosition(pos);
				final MapleAtom atom4 = new MapleAtom(false, chr.getId(), 3, true, 31221014, chr.getTruePosition().x,
						chr.getTruePosition().y);
				final List<Integer> monsters4 = new ArrayList<Integer>();
				for (byte i9 = 0; i9 < 2; ++i9) {
					monsters4.add(slea.readInt());
					atom4.addForceAtom(new ForceAtom(3, Randomizer.rand(10, 20), Randomizer.rand(20, 35),
							Randomizer.rand(50, 65), 660));
				}
				effect.applyTo(chr);
				c.getSession().writeAndFlush((Object) CWvsContext.enableActions(chr, true, false));
				atom4.setDwTargets(monsters4);
				chr.getMap().spawnMapleAtom(atom4);
				break;
			}
			case 31241000: { // 실드 체이싱 VI 구현
				pos = slea.readPos();
				chr.setPosition(pos);
				final MapleAtom atom4 = new MapleAtom(false, chr.getId(), 84, true, 31241001, chr.getTruePosition().x,
						chr.getTruePosition().y);
				final List<Integer> monsters4 = new ArrayList<Integer>();
				for (byte i9 = 0; i9 < 3; ++i9) {
					monsters4.add(slea.readInt());
					atom4.addForceAtom(new ForceAtom(3, Randomizer.rand(10, 20), Randomizer.rand(20, 35),
							Randomizer.rand(50, 65), 660));
				}
				effect.applyTo(chr);
				c.getSession().writeAndFlush((Object) CWvsContext.enableActions(chr, true, false));
				atom4.setDwTargets(monsters4);
				chr.getMap().spawnMapleAtom(atom4);
				break;
			}
			case 30001061: {
				final int objectId = slea.readInt();
				final MapleMonster mob4 = chr.getMap().getMonsterByOid(objectId);
				if (mob4 != null) {
					final boolean success = mob4.getId() >= 9304000 && mob4.getId() < 9305000;
					c.getSession().writeAndFlush((Object) CField.EffectPacket.showEffect(chr, 0, skillid, 1, 0, 0,
							(byte) (success ? 1 : 0), true, null, null, null));
					chr.getMap().broadcastMessage(chr, CField.EffectPacket.showEffect(chr, 0, skillid, 1, 0, 0,
							(byte) (success ? 1 : 0), false, null, null, null), chr.getTruePosition());
					chr.getMap().broadcastMessage(MobPacket.catchMonster(mob4.getObjectId(), (byte) (success ? 1 : 0)));
					if (success) {
						final int jaguarid = GameConstants.getJaguarType(mob4.getId());
						String info = chr.getInfoQuest(23008);
						for (int i = 0; i <= 8; ++i) {
							if (!info.contains(i + "=1")) {
								if (i == jaguarid) {
									info = info + i + "=1;";
								}
							}
						}
						chr.updateInfoQuest(23008, info);
						chr.updateInfoQuest(123456, String.valueOf(jaguarid * 10));
						chr.getMap().killMonster(mob4, chr, true, false, (byte) 1);
						c.getSession().writeAndFlush((Object) CWvsContext.updateJaguar(chr));
					} else {
						chr.dropMessage(5,
								"\ubaac\uc2a4\ud130\uc758 \uccb4\ub825\uc774 \ub108\ubb34 \ub9ce\uc544 \ud3ec\ud68d\ud560 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4.");
					}
				}
				SkillFactory.getSkill(33110014).getEffect(chr.getSkillLevel(33110014)).applyTo(chr, true);
				chr.dropMessage(5, "\ud3ec\ud68d\uc5d0 \uc131\uacf5\ud558\uc600\uc2b5\ub2c8\ub2e4.");
				break;
			}
			case 33141006: // 클로우 컷 VI 구현
			case 33001016:
			case 33141007: // 프로보크 VI 구현
			case 33001025:
			case 33141008: // 크로스 로드 VI 구현
			case 33101115:
			case 33141010: // 소닉붐 VI 구현
			case 33111015:
			case 33141011: // 재규어 소울 VI 구현
			case 33121017:
			case 33141012: // 램피지 애즈 원 VI 구현
			case 33121255: {
				pos = slea.readPos();
				chr.setPosition(pos);
				if (skillid == 33001016 || skillid == 33111015 || skillid == 33141010 || skillid == 33101115
						|| skillid == 33141008) {
					if (skillid == 33101115 || skillid == 33141008) {
						c.getSession().writeAndFlush(
								(Object) CField.skillCooldown(skillid == 33141008 ? 33141009 : 33101215, 7000));
						chr.addCooldown(skillid == 33141008 ? 33141009 : 33101215, System.currentTimeMillis(), 7000L);
					}
					if (chr.getSkillLevel(33120048) > 0) {
						chr.changeCooldown(skillid, -1000);
					}
				} else if (skillid == 33001025 || skillid == 33141007) {
					final List<MapleMapObject> objs = chr.getMap().getMapObjectsInRange(chr.getTruePosition(), 500000.0,
							Arrays.asList(MapleMapObjectType.MONSTER));
					final List<MapleMapObject> skill2 = new ArrayList<MapleMapObject>();
					if (!objs.isEmpty()) {
						for (int m = 0; m < 10 && objs.size() > m; ++m) {
							skill2.add(objs.get(m));
						}
						for (final MapleMapObject mobs2 : skill2) {
							final List<Pair<MonsterStatus, MonsterStatusEffect>> datas = new ArrayList<Pair<MonsterStatus, MonsterStatusEffect>>();
							final MapleMonster mon = chr.getMap().getMonsterByOid(mobs2.getObjectId());
							datas.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_JaguarProvoke,
									new MonsterStatusEffect(skillid,
											mon.getStats().isBoss() ? (effect.getDuration() / 2) : effect.getDuration(),
											1L)));
							datas.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_DodgeBodyAttack,
									new MonsterStatusEffect(skillid,
											mon.getStats().isBoss() ? (effect.getDuration() / 2) : effect.getDuration(),
											1L)));
							((MapleMonster) mobs2).applyStatus(c, datas, effect);
						}
					}
				}
				c.getSession().writeAndFlush((Object) CField.jaguarAttack(skillid));
				c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
				break;
			}
			case 35141003: // 호밍 미사일 VI 구현
			case 35101002:
			case 35110017:
			case 35120017: {
				boolean isHexa = (skillid == 35141003);
				byte size3 = slea.readByte();
				int bulletCount = SkillFactory.getSkill(35101002).getEffect(skillLevel).getBulletCount();
				if (chr.getSkillLevel(35120017) > 0) {
					bulletCount += 5;
				}
				final MapleAtom atom5 = new MapleAtom(false, chr.getId(), isHexa ? 97 : 20, true, skillid,
						chr.getTruePosition().x, chr.getTruePosition().y);
				final List<Integer> monsters5 = new ArrayList<Integer>();
				for (byte i10 = 0; i10 < size3; ++i10) {
					monsters5.add(slea.readInt());
					atom5.addForceAtom(new ForceAtom(2, 50, Randomizer.rand(10, 15), Randomizer.rand(0, 25), 500));
				}
				if (chr.getBuffedValue(35111003)) {
					while (size3 < bulletCount) {
						++size3;
						atom5.addForceAtom(new ForceAtom(2, 50, Randomizer.rand(10, 15), Randomizer.rand(0, 25), 500));
					}
				}
				if (chr.getBuffedEffect(SecondaryStat.BombTime) != null) {
					for (byte i10 = 0; i10 < chr.getBuffedEffect(SecondaryStat.BombTime).getX(); ++i10) {
						atom5.addForceAtom(new ForceAtom(2, 50, Randomizer.rand(10, 15), Randomizer.rand(0, 25), 500));
					}
				}
				if (chr.getBuffedEffect(400051041) != null) {
					for (byte i10 = 0; i10 < chr.getBuffedEffect(400051041).getX(); ++i10) {
						atom5.addForceAtom(new ForceAtom(2, 50, Randomizer.rand(10, 15), Randomizer.rand(0, 25), 500));
					}
				}
				atom5.setDwTargets(monsters5);
				chr.getMap().spawnMapleAtom(atom5);
				effect.applyTo(chr);
				c.getSession().writeAndFlush((Object) CWvsContext.enableActions(chr, true, false));
				break;
			}
			case 5111017: {
				chr.서펜트스톤 = 0;
				chr.cancelEffectFromBuffStat(SecondaryStat.SerpentStone, 5111017);
				SkillFactory.getSkill(5110020).getEffect(1).applyTo(chr, false, false);
				break;
			}
			case 65111100: { // 오리진 그랜드 피날레 구현
				boolean isHexa = chr.getBuffedValue(65141501);
				pos = slea.readPos();
				chr.setPosition(pos);
				byte count = slea.readByte();
				MapleAtom atom = new MapleAtom(false, chr.getId(), isHexa ? 93 : 3, true, isHexa ? 65141502 : 65111007,
						chr.getTruePosition().x, chr.getTruePosition().y);
				ArrayList<Integer> monsters = new ArrayList<Integer>();
				int mobid = 0;
				if (count == 1) {
					mobid = slea.readInt();
					monsters.add(mobid);
					atom.addForceAtom(new ForceAtom(1, Randomizer.rand(10, 20), Randomizer.rand(40, 65), 0, 500));
				}
				for (byte i = 0; i < count; i = (byte) (i + 1)) {
					monsters.add(mobid);
					atom.addForceAtom(new ForceAtom(1, Randomizer.rand(10, 20), Randomizer.rand(40, 65), 0, 500));
				}
				effect.applyTo(chr);
				c.getSession().writeAndFlush((Object) CWvsContext.enableActions(chr, true, false));
				chr.getClient().send(CField.lockSkill(skillid));
				atom.setDwTargets(monsters);
				chr.getMap().spawnMapleAtom(atom);
				// chr.Recharge(skillid);
				// chr.getClient().send(CField.unlockSkill());
				break;
			}
			case 65141001: { // 소울 시커 VI 구현
				boolean isHexa = chr.getBuffedValue(65141501);
				pos = slea.readPos();
				chr.setPosition(pos);
				byte count = slea.readByte();
				MapleAtom atom = new MapleAtom(false, chr.getId(), isHexa ? 93 : 3, true, 65141003,
						chr.getTruePosition().x, chr.getTruePosition().y);
				ArrayList<Integer> monsters = new ArrayList<Integer>();
				int mobid = 0;
				if (count == 1) {
					mobid = slea.readInt();
					monsters.add(mobid);
					atom.addForceAtom(new ForceAtom(1, Randomizer.rand(10, 20), Randomizer.rand(40, 65), 0, 500));
				}
				for (byte i = 0; i < count; i = (byte) (i + 1)) {
					monsters.add(mobid);
					atom.addForceAtom(new ForceAtom(1, Randomizer.rand(10, 20), Randomizer.rand(40, 65), 0, 500));
				}
				effect.applyTo(chr);
				c.getSession().writeAndFlush((Object) CWvsContext.enableActions(chr, true, false));
				chr.getClient().send(CField.lockSkill(skillid));
				atom.setDwTargets(monsters);
				chr.getMap().spawnMapleAtom(atom);
				// chr.Recharge(skillid);
				break;
			}
			case 131001106: {
				effect.applyTo(chr);
				final byte count2 = slea.readByte();
				MonsterStatus ms2 = null;
				final MonsterStatusEffect mse = new MonsterStatusEffect(skillid, effect.getDuration());
				for (byte i6 = 0; i6 < count2; ++i6) {
					final MapleMonster monster3 = chr.getMap().getMonsterByOid(slea.readInt());
					final List<Triple<MonsterStatus, MonsterStatusEffect, Integer>> statusz2 = new ArrayList<Triple<MonsterStatus, MonsterStatusEffect, Integer>>();
					if (monster3 != null) {
						ms2 = MonsterStatus.MS_IndiePdr;
						statusz2.add(new Triple<MonsterStatus, MonsterStatusEffect, Integer>(ms2, mse, effect.getZ()));
						ms2 = MonsterStatus.MS_IndieMdr;
						statusz2.add(new Triple<MonsterStatus, MonsterStatusEffect, Integer>(ms2, mse, effect.getZ()));
						final List<Pair<MonsterStatus, MonsterStatusEffect>> applys4 = new ArrayList<Pair<MonsterStatus, MonsterStatusEffect>>();
						for (final Triple<MonsterStatus, MonsterStatusEffect, Integer> status2 : statusz2) {
							if (status2.left != null && status2.mid != null && Randomizer.isSuccess(effect.getProp())) {
								status2.mid.setValue(status2.right);
								applys4.add(new Pair<MonsterStatus, MonsterStatusEffect>(status2.left, status2.mid));
							}
						}
						monster3.applyStatus(c, applys4, effect);
					}
				}
				break;
			}
			case 400021001: { // 도트 퍼니셔
				int xMin = c.getPlayer().getPosition().x - 500;
				int xMax = c.getPlayer().getPosition().x + 500;
				int yMin = c.getPlayer().getPosition().y - 550;
				int yMax = c.getPlayer().getPosition().y - 150;

				List<MapleMapObject> mobs_objects = c.getPlayer().getMap().getMapObjectsInRange(
						c.getPlayer().getPosition(), 320000, Arrays.asList(MapleMapObjectType.MONSTER));
				int addball = 0;
				int moid = 0;
				for (int i = 0; i < mobs_objects.size(); i++) {
					MapleMonster mob = (MapleMonster) mobs_objects.get(i);
					moid = mob.getObjectId();
					Iterator<Ignition> lp = mob.getIgnitions().iterator();// .getStati().entrySet().iterator();
					boolean moab = false;
					while (lp.hasNext()) {
						Ignition zz = lp.next();
						if (zz.getOwnerId() == c.getPlayer().getId()) {
							if (!moab) {
								moab = true;
								addball++;
							}
						}
					}
				}

				MapleAtom atom = new MapleAtom(false, chr.getId(), 28, true, 400021001, chr.getTruePosition().x,
						chr.getTruePosition().y);
				List<Integer> monsters = new ArrayList<>();

				for (int i = 0; i < 15 + addball; i++) {
					monsters.add(0);
					ForceAtom atoms = new ForceAtom(moid, Randomizer.rand(41, 44), Randomizer.rand(3, 4),
							Randomizer.rand(10, 360), (short) 720,
							new Point(Randomizer.rand(xMin, xMax), Randomizer.rand(yMin, yMax)));
					List<Integer> mobid = new ArrayList<>();
					mobid.add(moid);
					atom.setDwTargets(mobid);
					atom.addForceAtom(atoms);
				}
				effect.applyTo(chr);
				c.getSession().writeAndFlush(CWvsContext.enableActions(chr, true, false));

				chr.getMap().spawnMapleAtom(atom);
				break;
			}
			case 400021030: {
				final SecondaryStatEffect sub = SkillFactory.getSkill(400021031).getEffect(skillLevel);
				slea.skip(5);
				final int size5 = slea.readInt();
				final List<Point> posz = new ArrayList<Point>();
				for (int i3 = 0; i3 < size5; ++i3) {
					final Point poss = new Point(slea.readInt(), slea.readInt());
					posz.add(poss);
				}
				int i3 = 737;
				int zz2 = -2310;
				for (final Point poss2 : posz) {
					i3 += 350;
					final MapleMist mist4 = new MapleMist(sub.calculateBoundingBox(poss2, chr.isFacingLeft()), chr, sub,
							zz2, (byte) (chr.isFacingLeft() ? 1 : 0));
					mist4.setDelay(26);
					mist4.setPosition(poss2);
					mist4.setEndTime(i3);
					zz2 += 350;
					chr.getMap().spawnMist(mist4, false);
				}
				effect.applyTo(chr);
				c.getSession().writeAndFlush((Object) CWvsContext.enableActions(chr));
				break;
			}
			case 36111008: {
				c.getPlayer().gainXenonSurplus((short) 10, SkillFactory.getSkill(30020232));
				c.getSession().writeAndFlush((Object) CWvsContext.enableActions(chr));
				break;
			}
			case 151001001: {
				chr.getMap().broadcastMessage(SkillPacket.CreateSubObtacle(chr, 151001001));
				break;
			}
			case 151141002: { // 샤드 VI 구현
				chr.getMap().broadcastMessage(SkillPacket.CreateSubObtacle(chr, 151141002));
				break;
			}
			case 151111002: {
				effect.applyTo(chr);
				c.getSession().writeAndFlush((Object) CWvsContext.onSkillUseResult(0));
				break;
			}
			case 151111004: {
				chr.에테르핸들러(chr, -20, skillid, false);
				effect.applyTo(chr);
				break;
			}
			case 400011108: {
				for (int i2 = 0; i2 < 18; ++i2) {
					final MapleMagicSword ms3 = new MapleMagicSword(chr, skillid, chr.활성화된소드, 31000, true);
					if (i2 == 0) {
						chr.setSkillCustomInfo(400011108, 1L, 0L);
					}
					if (i2 == 1) {
						chr.setSkillCustomInfo(400011108, 0L, 0L);
					}
					chr.getMap().spawnMagicSword(ms3, chr, true);
				}
				effect.applyTo(chr);
				break;
			}
			case 151101003: {
				final int objectId = slea.readInt();
				final MapleSummon summon4 = chr.getMap().getSummonByOid(objectId);
				if (summon4 != null && chr.getSkillLevel(151120034) == 0) {
					summon4.removeSummon(chr.getMap(), false);
				}
				if (chr.getSkillLevel(151120034) > 0) {
					chr.에테르핸들러(chr, 20, skillid, false);
					break;
				}
				break;
			}
			case 151111003:
			case 151141004: { // 오더 VI
				chr.getMap().broadcastMessage(SkillPacket.RemoveSubObtacle(chr, (chr.에테르소드 - 1) * 10));
				chr.getMap().broadcastMessage(SkillPacket.RemoveSubObtacle(chr, chr.에테르소드 * 10));
				chr.에테르소드 -= 2;
				if (chr.에테르소드 <= 0) {
					chr.에테르소드 = 0;
				}
				if (chr.에테르소드 <= 0) {
					chr.에테르소드 = 0;
				}
				if (chr.활성화된소드 <= 0) {
					chr.활성화된소드 = 0;
				}
				chr.에테르핸들러(chr, -100, skillid, false);
				++chr.활성화된소드;
				final MapleMagicSword ms4 = new MapleMagicSword(chr, skillid, chr.활성화된소드, 40000, false);
				chr.getMap().spawnMagicSword(ms4, chr, false);
				++chr.활성화된소드;
				final MapleMagicSword ms3 = new MapleMagicSword(chr, skillid, chr.활성화된소드, 40000, false);
				chr.getMap().spawnMagicSword(ms3, chr, false);
				break;
			}
			case 151101006: {
				if (chr.getBuffedValue(skillid)) {
					chr.cancelEffectFromBuffStat(SecondaryStat.Creation);
					c.getSession().writeAndFlush((Object) CWvsContext.enableActions(chr));
					break;
				}
				chr.에테르핸들러(chr, 0, skillid, true);
				effect.applyTo(chr);
				break;
			}
			case 151100002: {
				if (c.getPlayer().getSkillCustomValue0(151121041) > 0L) {
					c.getPlayer().addSkillCustomInfo(151121041, -1L);
					final Point pos4 = slea.readPos();
					effect.applyTo(chr, pos4);
					break;
				}
				break;
			}
			case 151121041: {
				for (final Point point : ret.mistPoints) {
					final MapleMist newmist2 = new MapleMist(
							effect.calculateBoundingBox(point, c.getPlayer().isFacingLeft()), c.getPlayer(), effect,
							1020, (byte) (c.getPlayer().isFacingLeft() ? 1 : 0));
					newmist2.setPosition(point);
					c.getPlayer().getMap().spawnMist(newmist2, false);
				}
				c.getPlayer().setSkillCustomInfo(151121041, 5L, 0L);
				break;
			}
			case 152001001:
			case 152120001: {
				pos = slea.readPos();
				slea.skip(7);
				final MapleAtom atom4 = new MapleAtom(false, chr.getId(), 36, true, skillid, chr.getTruePosition().x,
						chr.getTruePosition().y);
				atom4.setDwFirstTargetId(0);
				final ForceAtom fa = new ForceAtom(2, 50, 50, 0, 470);
				chr.addSkillCustomInfo(skillid, 1L);
				fa.setnAttackCount((int) chr.getSkillCustomValue0(skillid));
				atom4.addForceAtom(fa);
				atom4.setDwUnknownPoint(slea.readInt());
				effect.applyTo(chr);
				c.getSession().writeAndFlush((Object) CWvsContext.enableActions(chr, true, false));
				chr.getClient().getSession().writeAndFlush((Object) CField.EffectPacket.showEffect(chr, 0, skillid, 1,
						0, 0, (byte) (chr.isFacingLeft() ? 1 : 0), true, pos, null, null));
				chr.getMap().broadcastMessage(chr, CField.EffectPacket.showEffect(chr, 0, skillid, 1, 0, 0,
						(byte) (chr.isFacingLeft() ? 1 : 0), false, pos, null, null), false);
				chr.getMap().spawnMapleAtom(atom4);
				break;
			}
			case 152110004: {
				slea.skip(9);
				final MapleAtom atom4 = new MapleAtom(false, chr.getId(), 37, true, 152110004, chr.getTruePosition().x,
						chr.getTruePosition().y);
				atom4.setDwFirstTargetId(0);
				final ForceAtom fa = new ForceAtom(1, 46, 60, 7, 300);
				chr.addSkillCustomInfo(skillid, 1L);
				fa.setnAttackCount((int) chr.getSkillCustomValue0(skillid));
				atom4.addForceAtom(fa);
				atom4.setDwUnknownPoint(slea.readInt());
				c.getSession().writeAndFlush((Object) CField.skillCooldown(152110004, 200));
				chr.addCooldown(152110004, System.currentTimeMillis(), 200L);
				effect.applyTo(chr);
				c.getSession().writeAndFlush((Object) CWvsContext.enableActions(chr, true, false));
				chr.getClient().getSession().writeAndFlush((Object) CField.EffectPacket.showEffect(chr, 0, skillid, 1,
						0, 0, (byte) (chr.isFacingLeft() ? 1 : 0), true, pos, null, null));
				chr.getMap().broadcastMessage(chr, CField.EffectPacket.showEffect(chr, 0, skillid, 1, 0, 0,
						(byte) (chr.isFacingLeft() ? 1 : 0), false, pos, null, null), false);
				chr.getMap().spawnMapleAtom(atom4);
				break;
			}
			case 155001103: {
				final List<Integer> moblist1 = new ArrayList<Integer>();
				final List<Integer> moblist2 = new ArrayList<Integer>();
				final List<Integer> moblist3 = new ArrayList<Integer>();
				final List<Integer> moblist4 = new ArrayList<Integer>();
				for (byte count3 = slea.readByte(), i4 = 0; i4 < count3; ++i4) {
					final int oid2 = slea.readInt();
					moblist1.add(oid2);
					moblist2.add(oid2);
					moblist3.add(oid2);
					moblist4.add(oid2);
				}
				if (chr.getSpellCount(4) <= 0) {
					moblist1.clear();
				}
				if (chr.getSpellCount(3) <= 0) {
					moblist2.clear();
				}
				if (chr.getSpellCount(2) <= 0) {
					moblist3.clear();
				}
				if (chr.getSpellCount(1) <= 0) {
					moblist4.clear();
				}
				chr.getMap().broadcastMessage(
						SkillPacket.SpawnSpell(chr.getId(), moblist1, moblist2, moblist3, moblist4, 0));
				chr.useSpell();
				effect.applyTo(chr);
				break;
			}
			case 155111207: {
				final List<MapleMagicWreck> removes = new ArrayList<MapleMagicWreck>();
				final List<Integer> monsters4 = new ArrayList<Integer>();
				slea.skip(3);
				final MapleAtom atom5 = new MapleAtom(false, chr.getId(), 48, true, 155111207, chr.getTruePosition().x,
						chr.getTruePosition().y);
				for (final MapleMagicWreck wreck : chr.getMap().getAllFieldThreadsafe()) {
					if (wreck != null) {
						final ForceAtom at2 = new ForceAtom(0, Randomizer.rand(40, 49), 60, Randomizer.rand(6, 9), 0,
								wreck.getTruePosition());
						at2.setnMaxHitCount(8);
						atom5.addForceAtom(at2);
						removes.add(wreck);
					}
				}
				atom5.setDwUserOwner(chr.getId());
				chr.getMap().spawnMapleAtom(atom5);
				for (final MapleMagicWreck wreck : removes) {
					chr.getMap().RemoveMagicWreck(wreck);
				}
				effect.applyTo(chr);
				break;
			}
			case 160001075:
			case 160011075: {
				boolean use = false;
				if (c.getPlayer().getKeyValue(7786, "sw") != 1L) {
					use = true;
					c.getPlayer().setKeyValue(7786, "sw", "1");
				} else {
					c.getPlayer().setKeyValue(7786, "sw", "0");
				}
				chr.getMap().broadcastMessage(CField.updateShapeShift(chr.getId(), use));
				c.getSession().writeAndFlush((Object) CWvsContext.enableActions(chr, true, false));
				chr.equipChanged();
				break;
			}
			//// 여기서 부터
			case 400021044: // 플레임 디스차지
			{
				final MapleSummon summon6 = chr.getSummon(12120014);
				if (summon6 != null) {
					final SecondaryStatEffect effect3 = SkillFactory.getSkill(400021042)
							.getEffect(chr.getSkillLevel(400021042));
					final MapleAtom atom5 = new MapleAtom(false, chr.getId(), 3, true, 400021045,
							summon6.getTruePosition().x, summon6.getTruePosition().y);
					final List<Integer> monsters5 = new ArrayList<Integer>();
					final int totalcreate = effect3.getQ() + (chr.getIgnition() - 2) * effect3.getS2();
					for (byte i4 = 0; i4 < totalcreate; ++i4) {
						monsters5.add(0);
						atom5.addForceAtom(new ForceAtom(6, Randomizer.rand(15, 23), Randomizer.rand(32, 37),
								Randomizer.rand(13, 332), 720));
					}
					effect.applyTo(chr);
					atom5.setDwTargets(monsters5);
					chr.setIgnition(0);
					chr.getMap().spawnMapleAtom(atom5);
					break;
				}
				break;
			}

			case 400021068: {
				final Point pos3 = slea.readPos();
				final MapleSummon s2 = chr.getSummon(152101000);
				if (s2 != null) {
					if (!chr.getSummons().isEmpty()) {
						final List<MapleSummon> toRemove = new ArrayList<MapleSummon>();
						MapleSummon summon7 = null;
						MapleSummon summon8 = null;
						for (final MapleSummon summon9 : chr.getSummons()) {
							if (summon9.getSkill() == 400021068) {
								if (summon7 == null) {
									summon7 = summon9;
								} else if (summon8 == null) {
									summon8 = summon9;
								}
								if (summon7 != null && summon8 != null) {
									break;
								}
								continue;
							}
						}
						if (summon7 != null && summon8 != null) {
							if (summon7.getStartTime() > summon8.getStartTime()) {
								toRemove.add(summon8);
							} else {
								toRemove.add(summon7);
							}
							for (final MapleSummon summon9 : toRemove) {
								summon9.removeSummon(chr.getMap(), false);
							}
						}
					}
					Vmatrixstackbuff(c, true, null);
					final MapleSummon summon10 = new MapleSummon(chr, 400021068, pos3,
							SummonMovementType.ShadowServantExtend, (byte) 7, effect.getDuration());
					chr.addSummon(summon10);
					chr.getMap().spawnSummon(summon10, effect.getDuration());
					effect.applyTo(chr);
					break;
				}
				break;
			}
			case 400021088: { // 어비셜 라이트닝
				final int objectId = ret.acrossPosition.height;
				final SpecialPortal object = (SpecialPortal) chr.getMap().getMapObject(objectId,
						MapleMapObjectType.SPECIAL_PORTAL);
				if (object != null) {
					chr.getMap().removeSpecialPortal(chr, object);
				}
				chr.getClient().getSession().writeAndFlush((Object) CField.EffectPacket.showEffect(chr, 0, skillid, 10,
						0, 0, (byte) (chr.isFacingLeft() ? 1 : 0), true, chr.getTruePosition(), null, null, ret));
				break;
			}
			case 400021099: { // 크리스탈 게이트
				if (!chr.getBuffedValue(400021099)) {
					chr.setSkillCustomInfo(400021099, effect.getProp(), 0L);
				}
				if (chr.getSkillCustomValue0(400021099) > 0L) {
					chr.addSkillCustomInfo(400021099, -1L);
				}
				final List<SpecialPortal> sp = new ArrayList<SpecialPortal>();
				final SpecialPortal s3 = new SpecialPortal(chr.getId(), 1, 400021100, chr.getMapId(),
						ret.plusPosition2.x, ret.plusPosition2.y,
						(int) (chr.getBuffedValue(400021099) ? chr.getBuffLimit(400021099) : effect.getDuration()));
				s3.setObjectId((int) (900000000L + chr.getSkillCustomValue0(400021099)));
				sp.add(s3);
				chr.getMap().spawnSpecialPortal(chr, sp);
				effect.applyTo(chr, slea.readPos(), ret.rlType);
				break;
			}
			case 400031000: {
				final MapleAtom atom4 = new MapleAtom(false, chr.getId(), 27, true, 400031000, chr.getTruePosition().x,
						chr.getTruePosition().y);
				final List<Integer> monsters4 = new ArrayList<Integer>();
				monsters4.add(0);
				final ForceAtom forceAtom = new ForceAtom(1, 40, 3, 90, 840);
				atom4.addForceAtom(forceAtom);
				effect.applyTo(chr);
				c.getSession().writeAndFlush((Object) CWvsContext.enableActions(chr, true, false));
				atom4.setDwTargets(monsters4);
				chr.getMap().spawnMapleAtom(atom4);
				break;
			}
			case 400031022: {
				final MapleAtom atom4 = new MapleAtom(false, chr.getId(), 34, true, 400031022, chr.getTruePosition().x,
						chr.getTruePosition().y);
				final List<Integer> monsters4 = new ArrayList<Integer>();
				for (int m = 0; m < effect.getX(); ++m) {
					monsters4.add(0);
					atom4.addForceAtom(new ForceAtom(Randomizer.nextBoolean() ? 1 : 3, Randomizer.rand(30, 60), 10,
							Randomizer.nextBoolean() ? Randomizer.rand(0, 5) : Randomizer.rand(180, 185), 720,
							chr.getTruePosition()));
				}
				effect.applyTo(chr);
				c.getSession().writeAndFlush((Object) CWvsContext.enableActions(chr, true, false));
				atom4.setDwTargets(monsters4);
				chr.getMap().spawnMapleAtom(atom4);
				break;
			}
			case 400041000: { // 베놈 버스트
				int mobsize = 0, skillid2 = 0, intervar = 0;
				long damage = 0, realduration2 = 0, dotdamage = 0, totaldamage = 0;
				for (MapleMonster monster : chr.getMap().getAllMonster()) {
					if (monster != null) {
						if (monster.getBuff(MonsterStatus.MS_Burned) != null) {
							skillid2 = monster.getBuff(MonsterStatus.MS_Burned).getSkill();
							realduration2 = ((monster.getBuff(MonsterStatus.MS_Burned).getStartTime()
									+ monster.getBuff(MonsterStatus.MS_Burned).getDuration())
									- System.currentTimeMillis());
							for (Ignition ig : monster.getIgnitions()) {
								if (ig.getSkill() == skillid2) {
									intervar = ig.getInterval();
									dotdamage = ig.getDamage();
									break;
								}
							}
							totaldamage = dotdamage * (realduration2 / 10) / intervar;
							chr.getMap()
									.broadcastMessage(MobPacket.NujukDamage(monster, chr, totaldamage, 400041000, 6));
							chr.getClient().getSession().writeAndFlush(
									CField.bonusAttackRequest(400041030, new ArrayList<>(), false, 0, new int[0]));
							if (monster.getBuff(MonsterStatus.MS_Burned) != null) {
								monster.cancelStatus(MonsterStatus.MS_Burned, monster.getBuff(400040000));
							}
							mobsize++;
							if (mobsize == 12) {
								break;
							}
						}
					}
				}

				effect.applyTo(chr);
				break;
			}
			case 400041021: {
				final int izz = skillid;
				effect.applyTo(chr);
				c.getSession().writeAndFlush((Object) CWvsContext.onSkillUseResult(izz));
				break;
			}
			case 400041022: {
				final byte size3 = slea.readByte();
				chr.useBlackJack = false;
				final MapleAtom atom = new MapleAtom(false, chr.getId(), 33, true, 400041023, chr.getTruePosition().x,
						chr.getTruePosition().y);
				chr.setSkillCustomInfo(400041080, 21L, 0L);
				atom.setDwFirstTargetId(slea.readInt());
				slea.skip(3);
				atom.setSearchX(slea.readInt());
				atom.setSearchX1(slea.readInt());
				for (int m = 0; m < 3; ++m) {
					final ForceAtom at3 = new ForceAtom(33, Randomizer.rand(30, 50), Randomizer.rand(5, 15),
							Randomizer.rand(55, 250), 760);
					atom.addForceAtom(at3);
				}
				effect.applyTo(chr);
				c.getSession().writeAndFlush((Object) CWvsContext.enableActions(chr, true, false));
				chr.getMap().spawnMapleAtom(atom);
				if (plus == 2) {
					chr.removeSkillCustomInfo(400041022);
					chr.getMap().broadcastMessage(
							CField.SetForceAtomTarget(400041023, c.getPlayer().getId(), 3, atom.getDwFirstTargetId()));
					chr.useBlackJack = true;
					break;
				}
				break;
			}
			case 400041080: {
				final int objid = slea.readInt();
				chr.removeSkillCustomInfo(400041022);
				chr.useBlackJack = true;
				c.getPlayer().getMap()
						.broadcastMessage(CField.SetForceAtomTarget(400041023, c.getPlayer().getId(), 3, objid));
				c.getSession().writeAndFlush((Object) CWvsContext.enableActions(chr));
				break;
			}
			case 400041024: {
				chr.useBlackJack = true;
				c.getPlayer().getMap().broadcastMessage(CField.blackJack(c.getPlayer(), skillid, slea.readPos()));
				break;
			}
			// 포톤레이
			case 400041057: {

				chr.photonRay = 0;
				effect.applyTo(chr);
				break;
			}
			case 400041058: {
				List<AdelProjectile> atoms = new ArrayList<>();
				slea.readInt();
				slea.readInt();
				slea.readShort();

				if (chr.getBuffedEffect(SecondaryStat.PhotonRay) != null) {

					Map<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<>();
					statups.put(SecondaryStat.PhotonRay,
							new Pair<>(Integer.valueOf(1), Integer.valueOf((int) chr.getBuffLimit(400041057))));
					chr.getClient().getSession().writeAndFlush(CWvsContext.BuffPacket.giveBuff(statups,
							chr.getBuffedEffect(SecondaryStat.PhotonRay), chr));
					chr.getMap().broadcastMessage(chr, CWvsContext.BuffPacket.giveForeignBuff(chr, statups,
							chr.getBuffedEffect(SecondaryStat.PhotonRay)), false);
				}

				int bullet = 15 + chr.PhotonRay_plus;
				for (int i = 0; i < 30; i++) {
					AdelProjectile sa = new AdelProjectile(9, chr.getId(), chr.getObjectId(), 400041058, 3600, 0, 1,
							chr.getPosition(), new ArrayList<>());
					for (int j = 0; j < bullet; j++) {
						atoms.add(sa);
					}
				}

				if (!atoms.isEmpty()) {
					chr.getMap().spawnAdelProjectile(chr, atoms, false);
				}
				chr.cancelEffectFromBuffStat(SecondaryStat.PhotonRay);
				return;
			}

			case 400051025: {
				pos = new Point(slea.readInt(), slea.readInt());
				c.getPlayer().getMap().broadcastMessage(
						CField.ICBM(false, skillid, effect.calculateBoundingBox(pos, c.getPlayer().isFacingLeft())));
				effect.applyTo(chr, pos);
				break;
			}
			case 400051074: {
				chr.setSkillCustomInfo(400051074, 20L, 0L);
				c.send(CField.fullMaker(20, 60000));
				effect.applyTo(chr);
				break;
			}
			case 400001021: { // 언스테이블 메모라이즈
				double Reduce_Rand;
				int Reduce_Per = 0;
				Reduce_Rand = (int) (Math.random() * (100 - 1 + 1) + 1);
				if (Reduce_Rand <= 1) {
					Reduce_Per = 20;
				} else if (Reduce_Rand >= 2 && Reduce_Rand <= 7) {
					Reduce_Per = 23;
				} else if (Reduce_Rand >= 8 && Reduce_Rand <= 13) {
					Reduce_Per = 2;
				} else if (Reduce_Rand >= 14 && Reduce_Rand <= 19) {
					Reduce_Per = 25;
				} else if (Reduce_Rand >= 20 && Reduce_Rand <= 29) {
					Reduce_Per = 27;
				} else if (Reduce_Rand >= 30 && Reduce_Rand <= 40) {
					Reduce_Per = 30;
				} else if (Reduce_Rand >= 41 && Reduce_Rand <= 52) {
					Reduce_Per = 33;
				} else if (Reduce_Rand >= 53 && Reduce_Rand <= 64) {
					Reduce_Per = 35;
				} else if (Reduce_Rand >= 65 && Reduce_Rand <= 76) {
					Reduce_Per = 38;
				} else if (Reduce_Rand >= 77 && Reduce_Rand <= 84) {
					Reduce_Per = 40;
				} else if (Reduce_Rand >= 85 && Reduce_Rand <= 90) {
					Reduce_Per = 45;
				} else if (Reduce_Rand >= 91 && Reduce_Rand <= 95) {
					Reduce_Per = 50;
				} else if (Reduce_Rand >= 96 && Reduce_Rand <= 97) {
					Reduce_Per = 55;
				} else if (Reduce_Rand >= 98 && Reduce_Rand <= 99) {
					Reduce_Per = 60;
				} else if (Reduce_Rand >= 100) {
					Reduce_Per = 65;
				}

				c.getSession().writeAndFlush((Object) CField.unstableMemorize(chr.unstableMemorize));
				effect.applyToBuff(chr);
				c.getSession().writeAndFlush((Object) CWvsContext.enableActions(chr, true, false));
				chr.changeCooldown(400001021, effect.getCooldown(c.getPlayer()) * -Reduce_Per / 100);
				break;
			}
			// 블러드 피스트
			case 400011038: {
				effect.applyToBuff(chr);
				break;
			}
			case 400011135: {
				for (int size2 = slea.readInt(), k = 0; k < size2; ++k) {
					final Point poz = new Point(slea.readInt(), slea.readInt());
					effect.applyTo(chr, poz);
				}
				break;
			}
			// [SEX] 400031051 스킬 설정
			case 400031051: {
				effect.applyTo(chr);
				final Point[] positions = { new Point(chr.getTruePosition().x - effect.getX(), chr.getTruePosition().y),
						new Point(chr.getTruePosition().x + effect.getX(), chr.getTruePosition().y),
						new Point(chr.getTruePosition().x, chr.getTruePosition().y + effect.getY()) };
				for (int k = 0; k < effect.getU(); ++k) {
					final Point spawn_pos = positions[k];
					effect.applyTo(chr, false, spawn_pos);
				}
				c.getSession().writeAndFlush(CField.Torrent(skillid, skillLevel, positions)); // New skill
				break;
			}
			default: {
				if (ServerConstants.DEBUG_RECEIVE) {
					switch (ret.skill) {
					default:
						System.err.println("[SpecialMove E] " + ret.skill + " / " + slea);
						break;
					}
				}

				if (skillid == 64001012) {
					for (int i2 = 64001007; i2 < 64001012; ++i2) {
						while (chr.getBuffedValue(i2)) {
							chr.cancelEffect(chr.getBuffedEffect(i2));
						}
					}
					slea.skip(3);
					final byte direction = slea.readByte();
					pos = new Point(slea.readInt(), slea.readInt());
					final int oldskillid = slea.readInt();
					chr.getClient().getSession().writeAndFlush((Object) CField.EffectPacket.showEffect(chr, oldskillid,
							skillid, 1, 0, 0, direction, true, pos, null, null));
					chr.getMap().broadcastMessage(chr, CField.EffectPacket.showEffect(chr, oldskillid, skillid, 1, 0, 0,
							direction, false, pos, null, null), false);
					effect.applyTo(chr);
					break;
				}
				if (skillid == 400041041) {
					for (final MapleMist mist3 : chr.getMap().getAllMistsThreadsafe()) {
						if (mist3.getSourceSkill().getId() == 400041041) {
							chr.getMap().removeMist(400041041);
							break;
						}
					}
					effect.applyTo(c.getPlayer(), ret.plusPosition2, ret.rlType);
					break;
				}
				if (skillid == 131001025) {
					pos = slea.readPos();
					final byte rltype = slea.readByte();
					slea.skip(2);
					final int objectId2 = slea.readInt();
					final MapleMonster maelstrom = chr.getMap().getMonsterByOid(objectId2);
					if (maelstrom == null) {
						System.out.println("maelstrom error");
						break;
					}
					chr.maelstrom = maelstrom.getId();
					effect.applyTo(c.getPlayer(), maelstrom.getTruePosition(), true, rltype);
					break;
				} else {
					if (skillid == 35111002) {
						final int type4 = slea.readInt();
						if (type4 == 1) {
							pos = slea.readPos();
							effect.applyTo(chr, new Point(slea.readPos()));
							slea.readByte();
							effect.applyTo(chr, new Point(slea.readPos()));
						} else {
							final byte tesla = slea.readByte();
							if (tesla == 2) {
								slea.skip(8);
							}
							pos = slea.readPos();
						}
					}

					if (skillid == 400021047 || skillid == 5210015) {
						pos = slea.readPos();
					} else if (slea.available() == 12L) {
						pos = slea.readPos();
					} else if (slea.available() == 11L) {
						slea.skip(4);
						pos = slea.readPos();
					} else if (slea.available() <= 9L && slea.available() >= 5L) {
						if (skillid != 11141002) {
							pos = slea.readPos();
						}
					}
					if (skill.getId() == 1121054) {
						chr.발할라검격 = 12;
					}

					if (effect.isMagicDoor()) {
						if (!FieldLimitType.MysticDoor.check(chr.getMap().getFieldLimit())) {
							effect.applyTo(c.getPlayer(), pos);
						}
					} else if (!effect.isMist() && slea.available() >= 5) { // Summon Skill Check
						pos = slea.readPos();
						byte rltype = slea.readByte();
						effect.applyTo(c.getPlayer(), pos, rltype, true);
					} else if (skillid != 400011015 || !chr.getBuffedValue(400011015)) {
						final byte rltype = slea.readByte();

						effect.applyTo(c.getPlayer(), pos, rltype, true);

						if (skillid == 13141501) { // 오리진 미스트랄 스프링 구현
							List<SecondAtom> atoms = new ArrayList<SecondAtom>();
							for (int i = 0; i < 13; i++) {
								atoms.add(new SecondAtom(47, chr.getId(), 0, 13141502, 3000, 0, 1,
										new Point(pos.x + Randomizer.rand(-400, 400),
												pos.y + Randomizer.rand(-550, -50)),
										Arrays.asList(0)));
							}

							for (int i = 0; i < 5; i++) {
								atoms.add(new SecondAtom(48, chr.getId(), 0, 13141503, 3000, 0, 1,
										new Point(pos.x + Randomizer.rand(-400, 400),
												pos.y + Randomizer.rand(-550, -50)),
										Arrays.asList(0)));
							}

							for (int i = 0; i < 3; i++) {
								atoms.add(new SecondAtom(49, chr.getId(), 0, 13141504, 3000, 0, 1,
										new Point(pos.x + Randomizer.rand(-400, 400),
												pos.y + Randomizer.rand(-550, -50)),
										Arrays.asList(0)));
							}
							chr.spawnSecondAtom(atoms);
						}

						if (skillid == 25141003) { // 오리진 호신강림 구현
							chr.getClient().getSession().writeAndFlush((Object) CField.skillCooldown(25111005, 15000));
							chr.addCooldown(25111005, System.currentTimeMillis(), 15000L);

							SecondaryStatValueHolder mbsvh = chr.checkBuffStatValueHolder(SecondaryStat.IndieSummon,
									25141506);
							mbsvh.value = 0;

							SecondaryStatEffect eff = chr.getBuffedEffect(25141506);

							int resetTime = (int) (30000
									- (System.currentTimeMillis() % 1000000000L - eff.getStarttime()));

							EnumMap<SecondaryStat, Pair<Integer, Integer>> statups = new EnumMap<SecondaryStat, Pair<Integer, Integer>>(
									SecondaryStat.class);
							statups.put(SecondaryStat.IndieSummon, new Pair<Integer, Integer>(1, resetTime));
							chr.getClient().getSession()
									.writeAndFlush(CWvsContext.BuffPacket.giveBuff(statups, eff, chr));
						}

						if (skillid == 162141002) { // 분출 : 너울이는 강 VI 구현
							chr.getClient().getSession().writeAndFlush(CField.lalaHexaWavingRiver());
						}
					}

					if (skill.getId() == 1121054 && chr.getBuffedEffect(SecondaryStat.ComboCounter) != null) {
						final EnumMap<SecondaryStat, Pair<Integer, Integer>> stat = new EnumMap<SecondaryStat, Pair<Integer, Integer>>(
								SecondaryStat.class);
						stat.put(SecondaryStat.ComboCounter, new Pair<Integer, Integer>(11, 0));
						chr.setBuffedValue(SecondaryStat.ComboCounter, 11);
						chr.getClient().getSession().writeAndFlush(CWvsContext.BuffPacket.giveBuff(stat,
								SkillFactory.getSkill(1101013).getEffect(chr.getSkillLevel(1101013)), chr));
						chr.getMap().broadcastMessage(chr, CWvsContext.BuffPacket.giveForeignBuff(chr, stat,
								SkillFactory.getSkill(1101013).getEffect(chr.getSkillLevel(1101013))), false);
						break;
					}
					if (skill.getId() == 400011012) {
						SkillFactory.getSkill(400011013).getEffect(chr.getSkillLevel(400011012)).applyTo(c.getPlayer(),
								pos);
						SkillFactory.getSkill(400011014).getEffect(chr.getSkillLevel(400011012)).applyTo(c.getPlayer(),
								pos);
						break;
					}
					break;
				}
			}
			}
		}
	}

	public static final void closeRangeAttack(LittleEndianAccessor slea, MapleClient c, MapleCharacter chr,
			boolean energy) {
		if (chr == null) {
			return;
		}
		if (chr.getMap() == null) {
			return;
		}
		AttackInfo attack = DamageParse.parseDmgM(slea, chr, energy);
		if (attack == null) {
			c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
			return;
		}
		boolean mirror = (chr.getBuffedValue(SecondaryStat.ShadowPartner) != null
				|| chr.getBuffedValue(SecondaryStat.Buckshot) != null);
		double maxdamage = chr.getStat().getCurrentMaxBaseDamage();
		Item shield = c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) -10);
		int skillLevel = 0;
		SecondaryStatEffect effect = null;
		Skill skill = null;
		if (GameConstants.isLinkMap(chr.getMapId()) && attack.skill == 80001770) {
			return;
		}
		if (attack.skill != 0) {
			SecondaryStatEffect dark;
			if (GameConstants.isKinesis(chr.getJob()) && attack.skill == 400021074) {
				chr.givePPoint(attack.skill);
			}
			skill = SkillFactory.getSkill(attack.skill);
			if (skill == null || (GameConstants.isAngel(attack.skill)
					&& (chr.getStat()).equippedSummon % 10000 != attack.skill % 10000)) {
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				return;
			}
			skillLevel = chr.getTotalSkillLevel(skill);
			effect = attack.getAttackEffect(chr, skillLevel, skill);
			if (effect == null) {
				return;
			}
			if (attack.skill == 4221016) {
				if (chr.getBuffedValue(4221020)) {
					chr.cancelEffectFromBuffStat(SecondaryStat.Murderous, 4221020);
				}
			}
			if (GameConstants.isKadena(chr.getJob())) {
				if (chr.getBuffedValue(64001001)) {
					chr.cancelEffectFromBuffStat(SecondaryStat.BeyondNextAttackProb);
				} else if (attack.skill == 64121001) {
					List<Pair<Integer, Integer>> WeponList = new ArrayList<>();
					WeponList.add(new Pair<>(Integer.valueOf(0), Integer.valueOf(64121002)));
					WeponList.add(new Pair<>(Integer.valueOf(1), Integer.valueOf(64001002)));
					WeponList.add(new Pair<>(Integer.valueOf(1), Integer.valueOf(64001012)));
					WeponList.add(new Pair<>(Integer.valueOf(2), Integer.valueOf(64101002)));
					WeponList.add(new Pair<>(Integer.valueOf(2), Integer.valueOf(64101008)));
					WeponList.add(new Pair<>(Integer.valueOf(3), Integer.valueOf(64101001)));
					WeponList.add(new Pair<>(Integer.valueOf(4), Integer.valueOf(64111002)));
					WeponList.add(new Pair<>(Integer.valueOf(5), Integer.valueOf(64111003)));
					WeponList.add(new Pair<>(Integer.valueOf(6), Integer.valueOf(64111004)));
					WeponList.add(new Pair<>(Integer.valueOf(6), Integer.valueOf(64111012)));
					WeponList.add(new Pair<>(Integer.valueOf(7), Integer.valueOf(64121021)));
					WeponList.add(new Pair<>(Integer.valueOf(7), Integer.valueOf(64121022)));
					WeponList.add(new Pair<>(Integer.valueOf(7), Integer.valueOf(64121023)));
					WeponList.add(new Pair<>(Integer.valueOf(7), Integer.valueOf(64121024)));
					WeponList.add(new Pair<>(Integer.valueOf(8), Integer.valueOf(64121003)));
					WeponList.add(new Pair<>(Integer.valueOf(8), Integer.valueOf(64121011)));
					WeponList.add(new Pair<>(Integer.valueOf(8), Integer.valueOf(64121016)));
					if (chr.getBuffedEffect(SecondaryStat.WeaponVariety) == null) {
						chr.weaponChanges1.clear();
					}
					for (Pair<Integer, Integer> info : WeponList) {
						if (((Integer) info.left).intValue() != 0 && !chr.weaponChanges1.containsKey(info.left)
								&& chr.weaponChanges1.size() < 8) {
							chr.weaponChanges1.put((Integer) info.left, (Integer) info.right);
						}
					}
					SkillFactory.getSkill(64120006).getEffect(chr.getSkillLevel(64120006)).applyTo(chr, 2147483647);
				} else if (attack.skill == 64121002) {
					SkillFactory.getSkill(64120006).getEffect(chr.getSkillLevel(64120006)).applyTo(chr);
				}
			}
			if (attack.skill == 61121105 || attack.skill == 61121222 || attack.skill == 24121052) {
				for (Point mistPos : attack.mistPoints) {
					effect.applyTo(chr, false, mistPos);
				}
			} else if (attack.skill - 64001009 >= -2 && attack.skill - 64001009 <= 2) {
				effect.applyTo(chr, attack.chain, true, true);
			} else if (attack.skill == 400011084) {
				SkillFactory.getSkill(attack.skill).getEffect(attack.skilllevel).applyTo(chr, attack.position, false);
			} else if (attack.skill != 400011050 && attack.skill != 400041002 && attack.skill != 400041003
					&& attack.skill != 400041004 && attack.skill != 400041005 && attack.skill != 400011081
					&& attack.skill != 400011058 && attack.skill != 400011109 && attack.skill != 151101010
					&& attack.skill != 151101008 && attack.skill != 151101007 && attack.skill != 151101006
					&& attack.skill != 31101002 && attack.skill != 51001005 && attack.summonattack == 0
					&& attack.skill != 400031066 && attack.skill != 400031064 && attack.skill != 400001010
					&& attack.skill != 400001011 && attack.skill != 400031000 && attack.skill != 400011089
					&& attack.skill != 400041021 && attack.skill != 400011121 && attack.skill != 27101202
					&& (attack.skill == 4341002
							|| (GameConstants.isCooltimeKeyDownSkill(attack.skill)
									&& chr.getCooldownLimit(GameConstants.getLinkedSkill(attack.skill)) == 0L)
							|| (!GameConstants.isCooltimeKeyDownSkill(attack.skill)
									&& !GameConstants.isNoDelaySkill(attack.skill)
									&& !GameConstants.isNoApplySkill(attack.skill)))
					&& attack.skill != 400011087 && attack.skill != 11121052 && attack.skill != 11121055
					&& attack.skill != 11121056 && attack.skill != 400011056 && attack.skill != 400011142 && !energy) {
				if (GameConstants.isDemonSlash(attack.skill)) {
					skill.getEffect(attack.skilllevel).applyTo(chr);
				} else if (attack.skill == 36101001) {
					skill.getEffect(attack.skilllevel).applyTo(chr, false);
				} else if (!skill.isFinalAttack()) {
					if (chr.getBuffedValue(400011055) && effect.getSourceId() == 400011055) {
						chr.getClient().getSession().writeAndFlush(CWvsContext.enableActions(chr));
						return;
					}

					effect.applyTo(chr, attack.position);
				}
			}
			if (GameConstants.isPathFinder(chr.getJob())) {
				if (attack.skill != 400031051 && attack.skill != 3301008 && attack.skill != 400031039
						&& attack.skill != 400031040 && attack.targets > 0) {
					MapleCharacter.렐릭게이지(chr.getClient(), attack.skill);
				}
				if (attack.skill == 3321005 && attack.targets > 0 && (c.getPlayer()).문양 == 1) {
					SecondaryStatEffect editionalDischarge = SkillFactory.getSkill(3300005)
							.getEffect(chr.getSkillLevel(3300005));
					MapleAtom atom = new MapleAtom(false, chr.getId(), 57, true, 3300005, (chr.getTruePosition()).x,
							(chr.getTruePosition()).y);
					List<MapleMapObject> objs = c.getPlayer().getMap().getMapObjectsInRange(
							c.getPlayer().getTruePosition(), 400000.0D,
							Arrays.asList(new MapleMapObjectType[] { MapleMapObjectType.MONSTER }));
					List<Integer> monsters = new ArrayList<>();
					MapleMonster mob = null;
					if (!objs.isEmpty() && editionalDischarge.makeChanceResult()) {
						int bullet = editionalDischarge.getBulletCount();
						if (chr.getBuffedValue(3321034)) {
							bullet++;
						}
						byte i;
						for (i = 0; i < bullet; i = (byte) (i + 1)) {
							if (objs.size() > 0 && mob == null) {
								mob = chr.getMap().getMonsterByOid(
										((MapleMapObject) objs.get(Randomizer.rand(0, objs.size() - 1))).getObjectId());
							}
							monsters.add(Integer.valueOf((mob != null) ? mob.getObjectId() : 0));
							atom.addForceAtom(new ForceAtom(1, Randomizer.rand(1, 43), Randomizer.rand(1, 4), 0, 60,
									chr.getTruePosition()));
						}
						atom.setDwTargets(monsters);
						atom.setSearchX(104);
						atom.setSearchY(2);
						atom.setSearchX1(500);
						atom.setSearchY1(200);
						atom.setnDuration(2);
						chr.getMap().spawnMapleAtom(atom);
					}
				}
				if (attack.skill != 3011004 && attack.skill != 3300002 && attack.skill != 3321003) {
					MapleCharacter.문양(chr.getClient(), attack.skill);
				}
			} else if (GameConstants.isEunWol(chr.getJob())) {
				if (chr.getBuffedValue(25101009)
						&& SkillFactory.getSkill(25100009).getSkillList().contains(Integer.valueOf(attack.skill))
						&& attack.targets > 0) {
					boolean isHexa = chr.getBuffedValue(25141506);
					int skillid_ = (chr.getSkillLevel(25120110) > 0) ? 25120110 : 25100009;
					int suc = isHexa ? 40
							: (chr.getSkillLevel(25120154) > 0) ? 35 : ((chr.getSkillLevel(25120115) > 0) ? 25 : 15);
					if (attack.skill == 400051079) {
						MapleAtom atom = new MapleAtom(false, chr.getId(), isHexa ? 68 : 13, true,
								isHexa ? 25141505 : 25120115, (chr.getTruePosition()).x, (chr.getTruePosition()).y);
						List<MapleMapObject> objs = chr.getMap().getMapObjectsInRange(chr.getTruePosition(), 500000.0D,
								Arrays.asList(new MapleMapObjectType[] { MapleMapObjectType.MONSTER }));
						List<Integer> targets = new ArrayList<>();
						for (int i = 0; i < ((attack.skill == 400051079) ? (effect.getV2() / 3) : 1); i++) {
							targets.add(Integer
									.valueOf((objs.size() > i) ? ((MapleMapObject) objs.get(i)).getObjectId() : 0));
							atom.addForceAtom(new ForceAtom(2, 15, 25, 50, 630));
						}
						atom.setnFoxSpiritSkillId(attack.skill);
						atom.setDwTargets(targets);
						chr.getMap().spawnMapleAtom(atom);
					} else if (Randomizer.isSuccess(suc)) {
						chr.getClient().getSession().writeAndFlush(CField.EffectPacket.showEffect(chr, skillid_,
								skillid_, 1, 0, 0, (byte) 0, true, null, null, null));
						chr.getMap().broadcastMessage(chr, CField.EffectPacket.showEffect(chr, skillid_, skillid_, 1, 0,
								0, (byte) 0, false, null, null, null), false);
						MapleAtom atom = new MapleAtom(false, chr.getId(), isHexa ? 68 : 13, true,
								isHexa ? 25141505 : (skillid_ == 25100009) ? 25100010 : 25120115,
								(chr.getTruePosition()).x, (chr.getTruePosition()).y);
						atom.addForceAtom(new ForceAtom((skillid_ == 25100009) ? 1 : 2, Randomizer.rand(10, 20),
								Randomizer.rand(20, 40), Randomizer.rand(40, 50), 630));
						chr.getMap().spawnMapleAtom(atom);
					}
				}

			} else if (GameConstants.isCain(chr.getJob())) {
				chr.handleRemainIncense(attack.skill, false);
			} else if (GameConstants.isMichael(chr.getJob())) {
				if (!chr.skillisCooling(400011032) && (attack.skill == 400011032 || attack.skill == 400011033
						|| attack.skill == 400011034 || attack.skill == 400011035 || attack.skill == 400011036
						|| attack.skill == 400011037 || attack.skill == 400011067)) {
					SecondaryStatEffect effect2 = SkillFactory.getSkill(400011032)
							.getEffect(chr.getSkillLevel(400011032));
					chr.setSkillCustomInfo(400011033, 0L, 5000L);
					if (chr.getBuffedValue(SecondaryStat.RoyalGuardState) != null) {
						boolean isHexa = chr.getSkillLevel(51141002) > 0;
						long duration = chr.getBuffLimit(isHexa ? 51141002 : 51001005);
						duration += (effect2.getQ() * 1000);
						if (duration >= 12000L) {
							duration = 12000L;
						}
						SkillFactory.getSkill(isHexa ? 51141002 : 51001005)
								.getEffect(chr.getSkillLevel(isHexa ? 51141002 : 51001005))
								.applyTo(chr, false, (int) duration);
					}
				}
			} else if (GameConstants.isZero(chr.getJob())) {
				if (chr.getSkillLevel(100000267) > 0) {
					switch (attack.skill) {
					case 101000200:
					case 101000201:
					case 101001200:
					case 101100200:
					case 101100201:
					case 101101200:
					case 101110200:
					case 101110202:
					case 101110203:
					case 101111200:
					case 101120201:
					case 101120202:
					case 101120204:
					case 101121200:
						if (chr.getGender() == 0) {
							if (chr.RapidTimeDetect < 10) {
								chr.RapidTimeDetect = (byte) (chr.RapidTimeDetect + 1);
							}
							if (attack.asist == 0) {
								chr.ZeroSkillCooldown(attack.skill);
							}
							if (chr.getBuffedEffect(SecondaryStat.Bless5th) != null) {
								MapleMonster monster = null;
								Iterator<AttackPair> iterator = attack.allDamage.iterator();
								if (iterator.hasNext()) {
									AttackPair a = iterator.next();
									monster = chr.getMap().getMonsterByOid(a.objectId);
								}
								if (chr.getSkillCustomValue0(400001050) > 0L && monster != null) {
									chr.removeSkillCustomInfo(400001050);
									List<Triple<Integer, Integer, Integer>> mobList = new ArrayList<>();
									mobList.add(new Triple<>(Integer.valueOf(monster.getObjectId()),
											Integer.valueOf(89), Integer.valueOf(0)));
									chr.getClient().getSession().writeAndFlush(
											CField.bonusAttackRequest(400001056, mobList, false, 0, new int[0]));
								} else {
									chr.setSkillCustomInfo(400001050, 1L, 0L);
								}
							}
							SecondaryStatEffect eff = SkillFactory.getSkill(100000276)
									.getEffect(chr.getSkillLevel(100000267));
							eff.applyTo(chr);
						}
						break;
					case 101000100:
					case 101000101:
					case 101001100:
					case 101100100:
					case 101100101:
					case 101101100:
					case 101110100:
					case 101110103:
					case 101111100:
					case 101120100:
					case 101120102:
					case 101120104:
					case 101121100:
						if (chr.getGender() == 1) {
							if (chr.RapidTimeStrength < 10) {
								chr.RapidTimeStrength = (byte) (chr.RapidTimeStrength + 1);
							}
							if (attack.asist == 0) {
								chr.ZeroSkillCooldown(attack.skill);
							}
							if (chr.getBuffedEffect(SecondaryStat.Bless5th) != null) {
								MapleMonster monster = null;
								Iterator<AttackPair> iterator = attack.allDamage.iterator();
								if (iterator.hasNext()) {
									AttackPair a = iterator.next();
									monster = chr.getMap().getMonsterByOid(a.objectId);
								}
								if (chr.getSkillCustomValue0(400001050) > 0L && monster != null) {
									chr.removeSkillCustomInfo(400001050);
									List<Triple<Integer, Integer, Integer>> mobList = new ArrayList<>();
									mobList.add(new Triple<>(Integer.valueOf(monster.getObjectId()),
											Integer.valueOf(89), Integer.valueOf(0)));
									chr.getClient().getSession().writeAndFlush(
											CField.bonusAttackRequest(400001056, mobList, false, 0, new int[0]));
								} else {
									chr.setSkillCustomInfo(400001050, 1L, 0L);
								}
							}
							SecondaryStatEffect eff = SkillFactory.getSkill(100000277)
									.getEffect(chr.getSkillLevel(100000267));
							eff.applyTo(chr);
						}
						break;
					}
				}

				if (attack.skill == 101120104) {
					SecondaryStatEffect a = SkillFactory.getSkill(attack.skill)
							.getEffect(c.getPlayer().getSkillLevel(101120104));
					MapleMist newmist = new MapleMist(
							a.calculateBoundingBox(attack.position, c.getPlayer().isFacingLeft()), c.getPlayer(), a,
							5000, (byte) (c.getPlayer().isFacingLeft() ? 1 : 0));
					newmist.setPosition(attack.position);
					c.getPlayer().getMap().spawnMist(newmist, false);
				} else if (attack.skill == 400011098 || attack.skill == 400011100) {
					SecondaryStatEffect a = SkillFactory.getSkill(attack.skill)
							.getEffect(c.getPlayer().getSkillLevel(attack.skill));
					MapleMist newmist = new MapleMist(
							a.calculateBoundingBox(chr.getPosition(), c.getPlayer().isFacingLeft()), c.getPlayer(), a,
							a.getCooldown(chr), (byte) (c.getPlayer().isFacingLeft() ? 1 : 0));
					newmist.setPosition(chr.getPosition());
					newmist.setDelay(0);
					c.getPlayer().getMap().spawnMist(newmist, false);
				}
			} else if (GameConstants.isPinkBean(chr.getJob())
					&& chr.getBuffedEffect(SecondaryStat.PinkbeanMinibeenMove) != null && Randomizer.isSuccess(15)) {
				int size = 0;
				for (MapleSummon s : chr.getMap().getAllSummons(131002015)) {
					if (s.getOwner().getId() == chr.getId()) {
						size++;
					}
				}
				if (size < 3) {
					MapleSummon tosummon = new MapleSummon(chr, 131002015, chr.getTruePosition(),
							SummonMovementType.BIRD_FOLLOW, (byte) 0, effect.getDuration());
					chr.addSummon(tosummon);
					chr.getMap().spawnSummon(tosummon, 10000);
				}
			}
			switch (attack.skill) {
			case 164121042:
				chr.setSkillCustomInfo(164121042, chr.getSkillCustomValue0(164121042) - 1L, 0L);
				if (chr.getSkillCustomValue0(164121042) <= 0L) {
					chr.removeSkillCustomInfo(164121042);
				}
				break;
			case 164101002:
			case 164111006:
			case 164111010:
			case 164121002:
				dark = SkillFactory.getSkill(164101006).getEffect(chr.getSkillLevel(164101006));
				dark.applyTo(chr);
				break;
			case 61101002:
			case 61110211:
			case 61120007:
			case 61121217:
			case 61141006: // 윌 오브 소드 VI 구현
			case 61141007: // 윌 오브 소드 VI 구현
				effect = attack.getAttackEffect(chr, chr.getSkillLevel(attack.skill), skill);
				DamageParse.applyAttack(attack, skill, c.getPlayer(), maxdamage, effect,
						mirror ? AttackType.NON_RANGED_WITH_MIRROR : AttackType.NON_RANGED, false, energy);
				chr.cancelEffectFromBuffStat(SecondaryStat.StopForceAtominfo);
				break;
			}
			if (!chr.skillisCooling(attack.skill) && (attack.skill == 155120000 || attack.skill == 155110000)) {
				c.getSession().writeAndFlush(CField.skillCooldown(155001102, 2200));
			}
//=============================================================================
			// 這段代碼會使角色英雄釋放技能自動啓用80002632破壞的雅達巴特,禁用后需要手動點擊技能窗口啓用
			/*
			 * // 제네시스 버프적용 if (chr.getSkillLevel(80002632) > 0) { Item weapon_item =
			 * chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11); if
			 * (weapon_item != null) { String weapon_name =
			 * MapleItemInformationProvider.getInstance().getName(weapon_item.getItemId());
			 * if (weapon_name != null && weapon_name.startsWith("제네시스 ") &&
			 * !chr.getBuffedValue(80002632) && !chr.skillisCooling(80002632)) {
			 * SecondaryStatEffect effcts = SkillFactory.getSkill(80002632)
			 * .getEffect(chr.getSkillLevel(80002632)); effcts.applyTo(chr);
			 * c.getSession().writeAndFlush(CField.skillCooldown(80002632, 90000));
			 * chr.addCooldown(80002632, System.currentTimeMillis(), 90000L); } } }
			 */
//=============================================================================

			if (chr.getSkillLevel(80002416) > 0) {
				Item weapon_item = chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11);
				if (weapon_item != null) {
					String weapon_name = MapleItemInformationProvider.getInstance().getName(weapon_item.getItemId());
					if (weapon_name != null && weapon_name.startsWith("아스칼론 ") && !chr.getBuffedValue(1221054)
							&& !chr.skillisCooling(1221054) && c.getPlayer().getSkillCustomValue0(1221054) == 0L) {
						SecondaryStatEffect effcts = SkillFactory.getSkill(1221054)
								.getEffect(chr.getSkillLevel(1221054));
						chr.setSkillCustomInfo(1221054, 1, 300000L);
						effcts.applyTo(chr);
					} else if (weapon_name != null && weapon_name.startsWith("아스칼론 ")
							&& !chr.getBuffedValue(32121017)) {
						SecondaryStatEffect effcts2 = SkillFactory.getSkill(32121017)
								.getEffect(chr.getSkillLevel(32121017));
						chr.addBuffCheck = 1;
						effcts2.applyTo(chr);
					}
				}
			}

			if (GameConstants.isMarksMan(chr.getJob())) {
				if (chr.getBuffedEffect(SecondaryStat.RepeatingCrossbowCatridge) != null) {

					if (chr.repeatingCrossbowCatridge > 1) {
						chr.repeatingCrossbowCatridge--;
						Map<SecondaryStat, Pair<Integer, Integer>> map = new HashMap<>();
						map.put(SecondaryStat.RepeatingCrossbowCatridge,
								new Pair<>(Integer.valueOf(chr.repeatingCrossbowCatridge), Integer.valueOf((int) chr
										.getBuffLimit(chr.getBuffSource(SecondaryStat.RepeatingCrossbowCatridge)))));
						chr.getClient().getSession().writeAndFlush(CWvsContext.BuffPacket.giveBuff(map,
								chr.getBuffedEffect(SecondaryStat.RepeatingCrossbowCatridge), chr));
					}

					if (chr.repeatingCrossbowCatridge <= 1) {
						chr.cancelEffectFromBuffStat(SecondaryStat.RepeatingCrossbowCatridge);
					}

				}
			}

			if (attack.isLink && (attack.skill < 400051059 || attack.skill > 400051067) && attack.skill != 0) {
				if (chr.getCooldownLimit(15101028) == 0) {
					chr.getClient().getSession().writeAndFlush(CField.getSeaWave(chr));
				}
			}

			// 칼리
			if (GameConstants.isKhali(chr.getJob())) {
				// 아스트라
				if (MapleSkillManager.isKhaliAttackSkills(attack.skill) && !c.getPlayer().getBuffedValue(400041087)) {

					List<RangeAttack> skills = new ArrayList<>();
					skills.clear();
					int id = (chr.getSkillLevel(400041087) > 0) ? 400041087 : 400041087;
					skills.add(new RangeAttack(id, chr.getTruePosition(),
							((attack.facingleft >>> 4 & 0xF) == 0) ? 0 : 1, 0, 8));
					chr.getClient().getSession().writeAndFlush(CField.rangeAttack(attack.skill, skills));
					c.getPlayer().getMap().broadcastMessage(CField.artsAstra(400041087));
				}

				// 디시빙 블레이드
				if ((MapleSkillManager.isKhaliAttackSkills(attack.skill)
						|| MapleSkillManager.isKhaliHexSkills(attack.skill)) && attack.targets > 0
						&& !c.getPlayer().getBuffedValue(154110005)) {
					SecondaryStatEffect blade = SkillFactory.getSkill(154110005)
							.getEffect(chr.getSkillLevel(154110005));
					blade.applyTo(chr, blade.getDuration());
				}
			}

			if (GameConstants.isKhali(chr.getJob())) {
				int chance = 100;
				if (chr.getSkillLevel(154120010) > 0) {
					chance = 100;
				}
				if ((chr.getBuffedValue(154111000) && attack.targets > 0)
						&& MapleSkillManager.isKhaliVoydSkills(attack.skill)) { // 서먼 차크리
					if (Randomizer.isSuccess(chance)) {
						for (MapleMonster m : c.getPlayer().getMap().getAllMonster()) {
							chr.SummonChakriHandler(chr, m.getTruePosition(), false);
							continue;
						}

					}
				}

			}

			// 소마 리마스터
			if (GameConstants.isSoulMaster(chr.getJob()) && attack.skill != 400001018) {
				if (GameConstants.isElementSoul(attack.skill)) {
					int pose = chr.getBuffedValue(SecondaryStat.PoseType);
					SecondaryStatEffect eff = SkillFactory.getSkill(11001027).getEffect(chr.getSkillLevel(11001027));
					SecondaryStatEffect eff2 = SkillFactory.getSkill(11001030).getEffect(chr.getSkillLevel(11001030));
					int[] passiveSkill = { 11001022, 11100034, 11110031, 11120019 };
					if (pose > 0 && attack.targets > 0 && chr.getBuffedValue(SecondaryStat.ElementSoul) != null) {
						int prop = 0;
						int maxcount = 0;
						int count = c.getPlayer().getCosmicCount();
						if (pose != chr.getSkillCustomValue0(11001022)) {
							for (int i = 0; i < passiveSkill.length; i++) { // 확률
								if (chr.getSkillLevel(passiveSkill[i]) > 0) {
									SecondaryStatEffect skills = SkillFactory.getSkill(passiveSkill[i])
											.getEffect(chr.getSkillLevel(passiveSkill[i]));
									prop += skills.getProp();
									maxcount += skills.getX();
								}
							}
							if (chr.getBuffedValue(SecondaryStat.CosmicForge) != null) {
								SecondaryStatEffect ef = SkillFactory.getSkill(11121054)
										.getEffect(chr.getSkillLevel(11121054));
								maxcount = maxcount + ef.getX();
							}
							if (Randomizer.isSuccess(prop)) {
								if (count < maxcount) {
									c.getPlayer().setCosmicCount(count + 1);
								} else {
									c.getPlayer().setCosmicCount(maxcount);
								}
							}
							eff.applyTo(chr, eff.getDuration());
							eff2.applyTo(chr, eff2.getDuration());
						}
					}
					chr.setSkillCustomInfo(11001022, pose, 0);
				}

				// 솔루나 타임시 폴링 문 > 라이징 선 , 라이징선 > 폴링 문
				if (chr.getBuffedValue(SecondaryStat.GlimmeringTime) != null) {
					SecondaryStatEffect eff = SkillFactory.getSkill(11001024).getEffect(chr.getSkillLevel(11001024));
					SecondaryStatEffect eff2 = SkillFactory.getSkill(11001025).getEffect(chr.getSkillLevel(11001025));

					if (chr.getBuffedValue(11001025)) {
						chr.cancelEffect(chr.getBuffedEffect(11001025));
						eff.applyTo(chr, eff.getDuration());
					} else {
						chr.cancelEffect(chr.getBuffedEffect(11001024));
						eff2.applyTo(chr, eff2.getDuration());
					}
				}
				if (attack.skill == 11101030) {
					chr.setCosmicCount(0);
				}
			}

			boolean linkcool = true;
			if (GameConstants.isCain(chr.getJob())) {
				linkcool = chr.handleCainSkillCooldown(attack.skill);
			}
			switch (attack.skill) {
			case 11121018:
			case 24121005:
			case 25111005:
			case 25141001: // 오리진 소혼 장막 구현
			case 25141003: // 오리진 소혼 장막 구현
			case 25111012:
			case 61101002:
			case 155121004:
			case 162111005:
			case 162111006:
			case 400011135:
			case 400021122:
			case 400031033:
			case 400051008:
				linkcool = false;
				break;
			case 21110018:
				linkcool = false;
				effect = SkillFactory.getSkill(21111019).getEffect(attack.skilllevel);
				c.getSession().writeAndFlush(CField.skillCooldown(21111018, effect.getCooldown(chr)));
				chr.addCooldown(21111018, System.currentTimeMillis(), effect.getCooldown(chr));
				break;
			}
			if (GameConstants.isZero(chr.getJob()) && attack.asist == 1
					&& SkillFactory.getSkill(attack.skill).getCategoryIndex() != chr.getGender()) {
				linkcool = false;
			}
			if (effect.getCooldown(chr) > 0 && !effect.ignoreCooldown(chr)
					&& (attack.skill < 400041003 || attack.skill > 400041005) && linkcool && attack.skill != 1111016) {
				if (!energy && chr.skillisCooling(effect.getSourceId())
						&& !GameConstants.isCooltimeKeyDownSkill(effect.getSourceId())
						&& !GameConstants.isNoApplySkill(effect.getSourceId())
						&& !GameConstants.isLinkedSkill(attack.skill) && !chr.getBuffedValue(effect.getSourceId())
						&& attack.skill != 15101028) {
					c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
					return;
				}
				if (effect.getSourceId() == 15111022 || effect.getSourceId() == 15120003) {
					if (!chr.getBuffedValue(15121054)) {
						c.getSession()
								.writeAndFlush(CField.skillCooldown(effect.getSourceId(), effect.getCooldown(chr)));
						chr.addCooldown(effect.getSourceId(), System.currentTimeMillis(), effect.getCooldown(chr));
					}
				} else if (effect.getSourceId() == 1321013) {
					if (!chr.getBuffedValue(1321015) && chr.getBuffedEffect(SecondaryStat.Reincarnation) == null) {
						c.getSession()
								.writeAndFlush(CField.skillCooldown(effect.getSourceId(), effect.getCooldown(chr)));
						chr.addCooldown(effect.getSourceId(), System.currentTimeMillis(), effect.getCooldown(chr));
					}
					// 크로스 더 스틱스 일때 , 엘리시움 버프값 안 받고 있으면 쿨 돌림?
				} else if (effect.getSourceId() == 11121055) {
					if (chr.getBuffedEffect(SecondaryStat.Ellision) == null) {
						c.getSession()
								.writeAndFlush(CField.skillCooldown(effect.getSourceId(), effect.getCooldown(chr)));
						chr.addCooldown(effect.getSourceId(), System.currentTimeMillis(), effect.getCooldown(chr));
					}
				} else if (chr.getBuffedValue(20040219) || chr.getBuffedValue(20040220)) {
					if (skill.isHyper() || !GameConstants.isLuminous(effect.getSourceId() / 10000)) {
						c.getSession()
								.writeAndFlush(CField.skillCooldown(effect.getSourceId(), effect.getCooldown(chr)));
						chr.addCooldown(effect.getSourceId(), System.currentTimeMillis(), effect.getCooldown(chr));
					}
				} else if (attack.skill == 400011079 || attack.skill == 400011081 || attack.skill == 400011080
						|| attack.skill == 400011082) {
					if (chr.getBuffedEffect(SecondaryStat.WillofSwordStrike) != null) {
						if (chr.ignoreDraco > 0 && (attack.skill == 400011079 || attack.skill == 400011080)) {
							chr.ignoreDraco--;
							if (chr.ignoreDraco <= 0) {
								chr.cancelEffectFromBuffStat(SecondaryStat.WillofSwordStrike);
							} else {
								Map<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<>();
								statups.put(SecondaryStat.WillofSwordStrike,
										new Pair<>(Integer.valueOf(chr.ignoreDraco), Integer.valueOf((int) chr
												.getBuffLimit(chr.getBuffSource(SecondaryStat.WillofSwordStrike)))));
								chr.getClient().getSession().writeAndFlush(CWvsContext.BuffPacket.giveBuff(statups,
										chr.getBuffedEffect(SecondaryStat.WillofSwordStrike), chr));
							}
						}
					} else if (attack.skill == 400011079 || attack.skill == 400011080) {
						c.getSession()
								.writeAndFlush(CField.skillCooldown(effect.getSourceId(), effect.getCooldown(chr)));
						chr.addCooldown(effect.getSourceId(), System.currentTimeMillis(), effect.getCooldown(chr));
					}
				} else if (!chr.skillisCooling(effect.getSourceId())
						&& !GameConstants.isAutoAttackSkill(effect.getSourceId())
						&& !GameConstants.isAfterCooltimeSkill(effect.getSourceId())) {
					if (effect.getSourceId() != 4241002) { // 분쇄 VI 구현
						c.getSession()
								.writeAndFlush(CField.skillCooldown(effect.getSourceId(), effect.getCooldown(chr)));
						chr.addCooldown(effect.getSourceId(), System.currentTimeMillis(), effect.getCooldown(chr));
					} else {
						if (chr.hexaMasterySkillStack <= 0) { // 분쇄 VI 구현
							c.getSession()
									.writeAndFlush(CField.skillCooldown(effect.getSourceId(), effect.getCooldown(chr)));
							chr.addCooldown(effect.getSourceId(), System.currentTimeMillis(), effect.getCooldown(chr));
						}
					}
				}
			}
		}
		if (!energy) {
			if ((chr.getMapId() == 109060000 || chr.getMapId() == 109060002 || chr.getMapId() == 109060004)
					&& attack.skill == 0) {
				MapleSnowball.MapleSnowballs.hitSnowball(chr);
			}
		}
		chr.checkFollow();
		if (attack.skill != 80001762) { // 천둥의 룬 임시 처리
			if (!chr.isHidden()) {
				chr.getMap().broadcastMessage(chr, CField.addAttackInfo(0, chr, attack), chr.getTruePosition());
			} else {
				chr.getMap().broadcastGMMessage(chr, CField.addAttackInfo(0, chr, attack), false);
			}
		}

		if (GameConstants.isBattleMage(chr.getJob())) {
			if (chr.getBuffedValue(SecondaryStat.BMageDeath) != null && !chr.skillisCooling(32001114)) {
				int duration = (chr.getSkillLevel(32120019) > 0) ? 5000
						: ((chr.getSkillLevel(32110017) > 0) ? 6000
								: ((chr.getSkillLevel(32100010) > 0) ? 8000 : 9000));
				if (chr.getSkillCustomValue0(32120019) > 0L) {
					chr.setDeath((byte) 0);
					chr.addCooldown(32001114, System.currentTimeMillis(), duration);
					chr.removeSkillCustomInfo(32120019);
					for (MapleSummon summon : chr.getSummons()) {
						if (summon.getSkill() == chr.getBuffSource(SecondaryStat.BMageDeath)) {
							chr.getClient().getSession().writeAndFlush(CField.SummonPacket.DeathAttack(summon));
							break;
						}
					}
					Map<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<>();
					statups.put(SecondaryStat.BMageDeath,
							new Pair<>(Integer.valueOf(chr.getDeath()), Integer.valueOf(0)));
					chr.getClient().getSession().writeAndFlush(CWvsContext.BuffPacket.giveBuff(statups,
							chr.getBuffedEffect(SecondaryStat.BMageDeath), chr));
				}
			}
		} else if (GameConstants.isBlaster(chr.getJob())) {
			List<RangeAttack> skills = new ArrayList<>();
			if (attack.skill == 37001000 || attack.skill == 37101000) {
				skills.clear();
				int id = (chr.getSkillLevel(37100007) > 0) ? 37100007 : 37000007;
				skills.add(new RangeAttack(id, chr.getTruePosition(), ((attack.facingleft >>> 4 & 0xF) == 0) ? 0 : 1, 0,
						1));
				chr.getClient().getSession().writeAndFlush(CField.rangeAttack(attack.skill, skills));
			}
			if (attack.skill == 37121000) {
				skills.clear();
				skills.add(new RangeAttack(37120001, chr.getTruePosition(),
						((attack.facingleft >>> 4 & 0xF) == 0) ? 0 : 1, 0, 1));
				chr.getClient().send(CField.rangeAttack(attack.skill, skills));
			}
			if (attack.skill == 37000011 || attack.skill == 37000012 || attack.skill == 37000013
					|| attack.skill == 37001002 || attack.skill == 37141002) { // 릴리즈 파일 벙커 VI 구현
				skills.clear();
				skills.add(new RangeAttack(37000008, chr.getTruePosition(),
						((attack.facingleft >>> 4 & 0xF) == 0) ? 0 : 1, 0, 1));
				if (chr.getJob() >= 3710) {
					skills.add(new RangeAttack(37100009, chr.getTruePosition(),
							((attack.facingleft >>> 4 & 0xF) == 0) ? 0 : 1, 0, 1));
				}
				if (chr.getJob() >= 3711) {
					skills.add(new RangeAttack(37110010, chr.getTruePosition(),
							((attack.facingleft >>> 4 & 0xF) == 0) ? 0 : 1, 0, 1));
				}
				if (chr.getJob() >= 3712) {
					skills.add(new RangeAttack(37120013, chr.getTruePosition(),
							((attack.facingleft >>> 4 & 0xF) == 0) ? 0 : 1, 0, 1));
				}
				if (chr.getSkillLevel(37141002) > 0) { // 릴리즈 파일 벙커 VI 구현
					skills.add(new RangeAttack(37141003, chr.getTruePosition(),
							((attack.facingleft >>> 4 & 0xF) == 0) ? 0 : 1, 0, 1));
					skills.add(new RangeAttack(37141004, chr.getTruePosition(),
							((attack.facingleft >>> 4 & 0xF) == 0) ? 0 : 1, 0, 1));
					skills.add(new RangeAttack(37141005, chr.getTruePosition(),
							((attack.facingleft >>> 4 & 0xF) == 0) ? 0 : 1, 0, 1));
					skills.add(new RangeAttack(37141006, chr.getTruePosition(),
							((attack.facingleft >>> 4 & 0xF) == 0) ? 0 : 1, 0, 1));
				}
				chr.getClient().send(CField.rangeAttack(attack.skill, skills));
			}
			if (attack.skill == 400011028 || attack.skill == 37121003) {
				chr.combinationBuff = 10;
				SkillFactory.getSkill(37120012).getEffect(c.getPlayer().getSkillLevel(37120012)).applyTo(c.getPlayer());
			}
			if (attack.skill == 400011019 || attack.skill == 37001004 || attack.skill == 37000005
					|| attack.skill == 37000009 || attack.skill == 37001002 || attack.skill == 37100008
					|| attack.skill == 37121004 || (attack.skill >= 37120014 && attack.skill <= 37120019)
					|| attack.skill == 37120023 || attack.skill == 37001002 || attack.skill == 37000011
					|| attack.skill == 37000012 || attack.skill == 37000013) {
				chr.Cylinder(attack.skill);
			}
			if (chr.bullet > 0 && chr.getBuffedValue(400011017) && attack.skill != 400011019
					&& (attack.skill == 37001000 || attack.skill == 37101000 || attack.skill == 37110001
							|| attack.skill == 37120002 || attack.skill == 37121000 || attack.skill == 37121003
							|| attack.skill == 37121052)) {
				List<Triple<Integer, Integer, Integer>> mobList = new ArrayList<>();
				chr.getClient().getSession()
						.writeAndFlush(CField.bonusAttackRequest(400011019, mobList, true, 0, new int[0]));
			}
			if (attack.skill == 37110001) {
				for (MapleMist mist : chr.getMap().getAllMistsThreadsafe()) {
					if (mist.getSourceSkill().getId() == 37110002) {
						chr.getMap().removeMist(37110002);
						break;
					}
				}
				SkillFactory.getSkill(37110002)
						.getEffect(chr.getTotalSkillLevel(GameConstants.getLinkedSkill(attack.skill)))
						.applyTo(chr, attack.position);
			}
			if (chr.getCylinderGauge() > 0) {
				skills.clear();
				skills.add(new RangeAttack(37000007, chr.getTruePosition(),
						((attack.facingleft >>> 4 & 0xF) == 0) ? 0 : 1, 0, 1));
				chr.getClient().getSession().writeAndFlush(CField.rangeAttack(attack.skill, skills));
			}
		} else if (GameConstants.isPinkBean(chr.getJob()) && attack.skill == 131001010 && attack.summonattack == 0) {
			Vmatrixstackbuff(c, true, null);
		}

		DamageParse.applyAttack(attack, skill, c.getPlayer(), maxdamage, effect,
				mirror ? AttackType.NON_RANGED_WITH_MIRROR : AttackType.NON_RANGED, false, energy);
	}

	public static final void BuffAttack(LittleEndianAccessor slea, MapleClient c, MapleCharacter chr) {
		if (chr == null) {
			return;
		}
		if (chr.getMap() == null) {
			return;
		}
		if (chr.getMapId() == ServerConstants.warpMap) {
			return;
		}
		AttackInfo attack = DamageParse.parseDmgB(slea, chr);
		if (attack == null) {
			c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
			return;
		}
		boolean mirror = (chr.getBuffedValue(SecondaryStat.ShadowPartner) != null
				|| chr.getBuffedValue(SecondaryStat.Buckshot) != null);
		double maxdamage = chr.getStat().getCurrentMaxBaseDamage();
		Item shield = c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) -10);
		int skillLevel = 0;
		SecondaryStatEffect effect = null;
		Skill skill = null;
		if (attack.skill != 0) {
			skill = SkillFactory.getSkill(GameConstants.getLinkedSkill(attack.skill));
			if (skill == null || (GameConstants.isAngel(attack.skill)
					&& (chr.getStat()).equippedSummon % 10000 != attack.skill % 10000)) {
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				return;
			}
			skillLevel = chr.getTotalSkillLevel(skill);
			effect = attack.getAttackEffect(chr, skillLevel, skill);
			if (effect == null) {
				return;
			}
			if (GameConstants.isDemonAvenger(chr.getJob()) && GameConstants.isExceedAttack(attack.skill)) {
				if (chr.getSkillLevel(31220044) > 0) {
					if (chr.getExceed() < 18) {
						chr.gainExceed((short) 1);
					}
				} else if (chr.getExceed() < 20) {
					chr.gainExceed((short) 1);
				}
				chr.handleExceedAttack(attack.skill);
			}
			switch (attack.skill) {
			case 61101002:
			case 61110211:
			case 61120007:
			case 61121217:
			case 61141006: // 윌 오브 소드 VI 구현
			case 61141007: // 윌 오브 소드 VI 구현
				effect = attack.getAttackEffect(chr, chr.getSkillLevel(attack.skill), skill);
				DamageParse.applyAttack(attack, skill, c.getPlayer(), maxdamage, effect,
						mirror ? AttackType.NON_RANGED_WITH_MIRROR : AttackType.NON_RANGED, false, false);
				chr.cancelEffectFromBuffStat(SecondaryStat.StopForceAtominfo);
				break;
			}
			if (chr.getSkillLevel(80002632) > 0) {
				// 检查角色是否拥有技能ID为80002632的技能且技能等级大于0
				Item weapon_item = chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11);
				if (weapon_item != null) {
					// 获取角色装备的武器（装备栏位置-11为武器槽）并确保武器存在
					String weapon_name = MapleItemInformationProvider.getInstance().getName(weapon_item.getItemId());
					if (weapon_name != null && weapon_name.startsWith("創世 ") && !chr.skillisCooling(80002632)
							&& !chr.getBuffedValue(80002632)) {
						// 检查武器名称是否以"제네시스 "开头，且技能不在冷却中，且角色没有该技能的增益效果
						SecondaryStatEffect effcts = SkillFactory.getSkill(80002632)
								.getEffect(chr.getSkillLevel(80002632));
						effcts.applyTo(chr);
						// 获取技能效果并应用到角色身上

						c.getSession().writeAndFlush(CField.skillCooldown(80002632, 90000));
						chr.addCooldown(80002632, System.currentTimeMillis(), 90000L);
						// 发送技能冷却时间数据包给客户端，并在服务器端记录技能冷却时间（90秒）
					}
				}
			}
			if (chr.getSkillLevel(80002416) > 0) {
				Item weapon_item = chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11);
				if (weapon_item != null) {
					String weapon_name = MapleItemInformationProvider.getInstance().getName(weapon_item.getItemId());
					if (weapon_name != null && weapon_name.startsWith("아스칼론 ") && !chr.getBuffedValue(1221054)
							&& !chr.skillisCooling(1221054) && c.getPlayer().getSkillCustomValue0(1221054) == 0L) {
						SecondaryStatEffect effcts = SkillFactory.getSkill(1221054)
								.getEffect(chr.getSkillLevel(1221054));
						chr.setSkillCustomInfo(1221054, 1, 300000L);
						effcts.applyTo(chr);
					} else if (weapon_name != null && weapon_name.startsWith("아스칼론 ")
							&& !chr.getBuffedValue(32121017)) {
						SecondaryStatEffect effcts2 = SkillFactory.getSkill(32121017)
								.getEffect(chr.getSkillLevel(32121017));
						chr.addBuffCheck = 1;
						effcts2.applyTo(chr);
					}
				}
			}
			if (effect.getCooldown(chr) > 0 && !effect.ignoreCooldown(chr)
					&& !GameConstants.isExceptionSkill(chr.getJob(), attack.skill) && attack.skill != 162111006) {
				if (chr.skillisCooling(attack.skill) && !GameConstants.isCooltimeKeyDownSkill(attack.skill)
						&& !GameConstants.isNoApplySkill(attack.skill) && !GameConstants.isLinkedSkill(attack.skill)
						&& !chr.getBuffedValue(attack.skill)) {
					c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
					return;
				}
				if (attack.skill == 15111022 || attack.skill == 15120003) {
					if (!chr.getBuffedValue(15121054)) {
						c.getSession().writeAndFlush(CField.skillCooldown(attack.skill, effect.getCooldown(chr)));
						chr.addCooldown(attack.skill, System.currentTimeMillis(), effect.getCooldown(chr));
					}
				} else if (attack.skill == 1321013) {
					if (!chr.getBuffedValue(1321015) && chr.getBuffedEffect(SecondaryStat.Reincarnation) == null) {
						c.getSession().writeAndFlush(CField.skillCooldown(attack.skill, effect.getCooldown(chr)));
						chr.addCooldown(attack.skill, System.currentTimeMillis(), effect.getCooldown(chr));
					}
				} else if (chr.getBuffedValue(20040219) || chr.getBuffedValue(20040220)) {
					if (skill.isHyper() || !GameConstants.isLuminous(attack.skill / 10000)) {
						c.getSession().writeAndFlush(CField.skillCooldown(attack.skill, effect.getCooldown(chr)));
						chr.addCooldown(attack.skill, System.currentTimeMillis(), effect.getCooldown(chr));
					}
				} else if (!chr.skillisCooling(attack.skill)) {
					c.getSession().writeAndFlush(CField.skillCooldown(attack.skill, effect.getCooldown(chr)));
					chr.addCooldown(attack.skill, System.currentTimeMillis(), effect.getCooldown(chr));
				}
				if (GameConstants.isLinkedSkill(attack.skill)
						&& !chr.skillisCooling(GameConstants.getLinkedSkill(attack.skill))) {
					c.getSession().writeAndFlush(
							CField.skillCooldown(GameConstants.getLinkedSkill(attack.skill), effect.getCooldown(chr)));
					chr.addCooldown(GameConstants.getLinkedSkill(attack.skill), System.currentTimeMillis(),
							effect.getCooldown(chr));
				}
			}
		}
		if ((chr.getMapId() == 109060000 || chr.getMapId() == 109060002 || chr.getMapId() == 109060004)
				&& attack.skill == 0) {
			MapleSnowball.MapleSnowballs.hitSnowball(chr);
		}
		int numFinisherOrbs = 0;
		Integer comboBuff = chr.getBuffedValue(SecondaryStat.ComboCounter);
		if (isFinisher(attack.skill)) {
			if (comboBuff != null) {
				numFinisherOrbs = comboBuff.intValue() - 1;
			}
			if (numFinisherOrbs <= 0) {
				return;
			}
			chr.handleOrbconsume(attack.skill);
		}
		chr.checkFollow();
		if (!chr.isHidden()) {
			chr.getMap().broadcastMessage(chr, CField.addAttackInfo(4, chr, attack), chr.getTruePosition());
		} else {
			chr.getMap().broadcastGMMessage(chr, CField.addAttackInfo(4, chr, attack), false);
		}
		DamageParse.applyAttack(attack, skill, c.getPlayer(), maxdamage, effect,
				mirror ? AttackType.NON_RANGED_WITH_MIRROR : AttackType.NON_RANGED, true, false);
	}

	public static final void rangedAttack(LittleEndianAccessor slea, MapleClient c, MapleCharacter chr) { // 원거리 공격
		double basedamage;
		if (chr == null) {
			return;
		}
		if (chr.getMap() == null) {
			return;
		}
		if (chr.getMapId() == ServerConstants.warpMap) {
			return;
		}
		AttackInfo attack = DamageParse.parseDmgR(slea, chr);
		if (attack == null) {
			c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
			return;
		}
		int bulletCount = 1, skillLevel = 0;
		SecondaryStatEffect effect = null;
		Skill skill = null;
		boolean AOE = (attack.skill == 4111004);
		boolean noBullet = ((chr.getJob() >= 300 && chr.getJob() <= 322 && chr.getTotalSkillLevel(3000002) > 0)
				|| (chr.getJob() >= 510 && chr.getJob() <= 512) || (chr.getJob() >= 3500 && chr.getJob() <= 3512)
				|| GameConstants.isCannon(chr.getJob()) || GameConstants.isPhantom(chr.getJob())
				|| GameConstants.isMercedes(chr.getJob()) || GameConstants.isZero(chr.getJob())
				|| GameConstants.isXenon(chr.getJob()) || GameConstants.isKaiser(chr.getJob())
				|| GameConstants.isAngelicBuster(chr.getJob()) || GameConstants.isKadena(chr.getJob())
				|| GameConstants.isPathFinder(chr.getJob()) || GameConstants.isYeti(chr.getJob()));

		if (!noBullet) { // 화살 소모 안하는 직업이 아닐 때
			// todo 패시브 스킬이 찍혀있다면 noBullet은 참이 되어야 한다.
			if (chr.getSkillLevel(13100028) > 0) {
				noBullet = true;
			}
			if (chr.getSkillLevel(4110016) > 0) {
				noBullet = true;
			}
			if (chr.getSkillLevel(4321002) > 0) {
				noBullet = true;
			}
			if (chr.getSkillLevel(33100017) > 0) {
				noBullet = true;
			}
			if (chr.getSkillLevel(14110031) > 0) {
				noBullet = true;
			}
		}

		if (attack.skill != 0) {
			skill = SkillFactory.getSkill(GameConstants.getLinkedSkill(attack.skill));
			if (skill == null || (GameConstants.isAngel(attack.skill)
					&& (chr.getStat()).equippedSummon % 10000 != attack.skill % 10000) && (attack.skill != 3141004)) {
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				c.getPlayer().dropMessage(6, "Range Skill Null!");
				return;
			}
			skillLevel = chr.getTotalSkillLevel(skill);
			effect = attack.getAttackEffect(chr, skillLevel, skill);
			if (effect == null) {
				return;
			}
			if (attack.skill == 61121105 || attack.skill == 61121222 || attack.skill == 24121052) {
				for (Point mistPos : attack.mistPoints) {
					skill.getEffect(skillLevel).applyTo(chr, false, mistPos);
				}
			} else if (attack.skill - 64001009 >= -2 && attack.skill - 64001009 <= 2) {
				SkillFactory.getSkill(attack.skill).getEffect(skillLevel).applyTo(chr, attack.chain, true, true);
			} else if (attack.summonattack == 0 && !GameConstants.isNoDelaySkill(attack.skill)
					&& !GameConstants.isNoApplySkill(attack.skill) && !skill.isFinalAttack()) {
				skill.getEffect(skillLevel).applyTo(chr, attack.position);
			}
			switch (attack.skill) {
			case 1077:
			case 1078:
			case 1079:
			case 11077:
			case 11078:
			case 11079:
			case 3111004:
			case 3211004:
			case 4121003:
			case 4121016:
			case 4121017:
			case 4121052:
			case 4221003:
			case 5121002:
			case 11101004:
			case 13101005:
			case 13101020:
			case 13111007:
			case 14101006:
			case 15111007:
			case 21000004:
			case 21001009:
			case 21100004:
			case 21100007:
			case 21110004:
			case 21110011:
			case 21110027:
			case 21110028:
			case 21120006:
			case 22110025:
			case 33101002:
			case 33101007:
			case 33121001:
			case 33121002:
			case 33121052:
			case 35121054:
			case 51001004:
			case 51111007:
			case 51121008:
			case 400010000:
				AOE = true;
				bulletCount = effect.getAttackCount();
				break;
			case 5220023:
			case 5220024:
			case 5220025:
			case 35111004:
			case 35121005:
			case 35121013:
				AOE = true;
				bulletCount = 6;
				break;
			case 5211008:
			case 5221017:
			case 5221052:
			case 13001020:
			case 13111020:
			case 13121002:
			case 13121052:
				bulletCount = effect.getAttackCount();
				break;
			default:
				bulletCount = effect.getBulletCount();
				break;
			}
			if (noBullet && effect.getBulletCount() < effect.getAttackCount()) {
				bulletCount = effect.getAttackCount();
			}
			if (chr.getSkillLevel(80002632) > 0) {
				Item weapon_item = chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11);
				if (weapon_item != null) {
					String weapon_name = MapleItemInformationProvider.getInstance().getName(weapon_item.getItemId());
					if (weapon_name != null && weapon_name.startsWith("제네시스") && !chr.skillisCooling(80002632)
							&& !chr.getBuffedValue(80002632)) {
						SecondaryStatEffect effcts = SkillFactory.getSkill(80002632)
								.getEffect(chr.getSkillLevel(80002632));
						effcts.applyTo(chr);
						c.getSession().writeAndFlush(CField.skillCooldown(80002632, 90000));
						chr.addCooldown(80002632, System.currentTimeMillis(), 90000L);
					}
				}
			}
			if (chr.getSkillLevel(80002416) > 0) {
				Item weapon_item = chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11);
				if (weapon_item != null) {
					String weapon_name = MapleItemInformationProvider.getInstance().getName(weapon_item.getItemId());
					if (weapon_name != null && weapon_name.startsWith("아스칼론 ") && !chr.getBuffedValue(1221054)
							&& !chr.skillisCooling(1221054) && c.getPlayer().getSkillCustomValue0(1221054) == 0L) {
						SecondaryStatEffect effcts = SkillFactory.getSkill(1221054)
								.getEffect(chr.getSkillLevel(1221054));
						chr.setSkillCustomInfo(1221054, 1, 300000L);
						effcts.applyTo(chr);
					} else if (weapon_name != null && weapon_name.startsWith("아스칼론 ")
							&& !chr.getBuffedValue(32121017)) {
						SecondaryStatEffect effcts2 = SkillFactory.getSkill(32121017)
								.getEffect(chr.getSkillLevel(32121017));
						chr.addBuffCheck = 1;
						effcts2.applyTo(chr);
					}
				}
			}
			boolean linkcool = true;
			if (GameConstants.isCain(chr.getJob())) {
				linkcool = chr.handleCainSkillCooldown(attack.skill);
			}
			switch (attack.skill) {
			case 400031033:
				linkcool = false;
				break;
			}
			if (effect.getCooldown(chr) > 0 && !effect.ignoreCooldown(chr)
					&& !GameConstants.isExceptionSkill(chr.getJob(), attack.skill) && linkcool) {
				if (chr.skillisCooling(effect.getSourceId())
						&& !GameConstants.isCooltimeKeyDownSkill(effect.getSourceId())
						&& !GameConstants.isNoApplySkill(attack.skill)
						&& !GameConstants.isLinkedSkill(effect.getSourceId())
						&& !chr.getBuffedValue(effect.getSourceId())) {
					c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
					return;
				}
				if (effect.getSourceId() == 15111022 || effect.getSourceId() == 15120003) {
					if (!chr.getBuffedValue(15121054)) {
						c.getSession()
								.writeAndFlush(CField.skillCooldown(effect.getSourceId(), effect.getCooldown(chr)));
						chr.addCooldown(effect.getSourceId(), System.currentTimeMillis(), effect.getCooldown(chr));
					}
				} else if (effect.getSourceId() == 3221007) {
					if (chr.getSkillLevel(3220051) == 0) {
						c.getSession()
								.writeAndFlush(CField.skillCooldown(effect.getSourceId(), effect.getCooldown(chr)));
						chr.addCooldown(effect.getSourceId(), System.currentTimeMillis(), effect.getCooldown(chr));
					}
				} else if (effect.getSourceId() == 1321013) {
					if (!chr.getBuffedValue(1321015) && chr.getBuffedEffect(SecondaryStat.Reincarnation) == null) {
						c.getSession()
								.writeAndFlush(CField.skillCooldown(effect.getSourceId(), effect.getCooldown(chr)));
						chr.addCooldown(effect.getSourceId(), System.currentTimeMillis(), effect.getCooldown(chr));
					}
				} else if (chr.getBuffedValue(20040219) || chr.getBuffedValue(20040220)) {
					if (skill.isHyper() || !GameConstants.isLuminous(effect.getSourceId() / 10000)) {
						c.getSession()
								.writeAndFlush(CField.skillCooldown(effect.getSourceId(), effect.getCooldown(chr)));
						chr.addCooldown(effect.getSourceId(), System.currentTimeMillis(), effect.getCooldown(chr));
					}
				} else if (!chr.skillisCooling(effect.getSourceId())
						&& !GameConstants.isAutoAttackSkill(effect.getSourceId())
						&& !GameConstants.isAfterCooltimeSkill(effect.getSourceId())) {
					c.getSession().writeAndFlush(CField.skillCooldown(effect.getSourceId(), effect.getCooldown(chr)));
					chr.addCooldown(effect.getSourceId(), System.currentTimeMillis(), effect.getCooldown(chr));
				}
			}
		}
		if (attack.skill == 400031033) {
			Vmatrixstackbuff(chr.getClient(), true, null);
		}

		if (attack.skill == 4121017) {
			if (c.getPlayer().getCooldownLimit(4121020) == 0) {
				final List<MapleMapObject> objs = c.getPlayer().getMap().getMapObjectsInRange(
						new Point(attack.position.x + ((attack.display & 0x8000) == 0 ? 230 : -230), attack.position.y),
						200000, Arrays.asList(MapleMapObjectType.MONSTER));

				List<AdelProjectile> atoms = new ArrayList<>();
				List<Point> points = new ArrayList<>();
				int durations[] = { 3110, 2930, 2990, 2870, 3050, 2810 };
				int startXs[] = { 35, 0, 330, 150, 180, 210 };
				int createDelays[] = { 1110, 930, 990, 870, 1050, 810 };
				int delays[] = { 1230, 1050, 1110, 990, 1170, 930 };
				points.add(new Point(325, -145));
				points.add(new Point(230, -176));
				points.add(new Point(135, -145));
				points.add(new Point(320, -45));
				points.add(new Point(230, 0));
				points.add(new Point(150, -45));

				for (int i = 0; i < 6; i++) {
					Point diff = points.get(i);
					AdelProjectile sword = new AdelProjectile(0x21, c.getPlayer().getId(),
							objs.size() == 0 ? 0 : objs.get(Randomizer.nextInt(objs.size())).getObjectId(), 4121020,
							durations[i], (attack.facingleft & 0x8000) == 0 ? startXs[i] : (-startXs[i]), 1,
							new Point(attack.position.x + ((attack.facingleft & 0x8000) == 0 ? diff.x : (-diff.x)),
									attack.position.y + diff.y),
							new ArrayList<>());
					sword.setCreateDelay(createDelays[i]);
					sword.setDelay(delays[i]);
					atoms.add(sword);
				}

				SkillFactory.getSkill(4121017).getEffect(c.getPlayer().getSkillLevel(4121017)).applyTo(c.getPlayer(),
						false);
				c.getPlayer().getMap().spawnAdelProjectile(c.getPlayer(), atoms, false);
				c.getPlayer().addCooldown(4121020, System.currentTimeMillis(), 5000);
			}
		}

		Integer ShadowPartner = chr.getBuffedValue(SecondaryStat.ShadowPartner);
		if (ShadowPartner != null) {
			bulletCount *= 2;
		}
		int projectile = 0, visProjectile = 0;
		Item ipp = chr.getInventory(MapleInventoryType.USE).getItem(attack.slot);
		if (chr.getAttackitem() == null && ipp != null) {
			chr.setAttackitem(ipp);
		} else if (ipp != null && chr.getAttackitem().getItemId() != ipp.getItemId()) {
			chr.setAttackitem(ipp);
		}
		if (!AOE && chr.getBuffedValue(SecondaryStat.SoulArrow) == null && !noBullet) { // 여기서 이걸 스킵해줘야하는데 스킵이 안되니까
																						// 겉뎀나는거임 에러나서
			if (attack.item == 0 && ipp != null) {
				attack.item = ipp.getItemId();
			} else if (ipp == null || ipp.getItemId() != attack.item) { // 여기에 걸려버리는거임 화살 뭘소모할지 모르니까
				return; // 리턴돼서 데미지 처리를 안함
			}
			projectile = ipp.getItemId();
			if (attack.csstar > 0) {
				if (chr.getInventory(MapleInventoryType.CASH).getItem(attack.csstar) == null) {
					return;
				}
				visProjectile = chr.getInventory(MapleInventoryType.CASH).getItem(attack.csstar).getItemId();
				if (chr.getV("csstar") == null) {
					chr.addKV("csstar", visProjectile + "");
				} else if (Integer.parseInt(chr.getV("csstar")) != visProjectile) {
					chr.addKV("csstar", visProjectile + "");
				}
			} else {
				visProjectile = projectile;
			}
			if (chr.getBuffedValue(SecondaryStat.NoBulletConsume) == null && chr.getSkillLevel(5200016) <= 0) {
				int bulletConsume = bulletCount;
				if (effect != null && effect.getBulletConsume() != 0) {
					bulletConsume = effect.getBulletConsume() * ((ShadowPartner != null) ? 2 : 1);
				}
				if (chr.getJob() == 412 && bulletConsume > 0
						&& ipp.getQuantity() < MapleItemInformationProvider.getInstance().getSlotMax(projectile)) {
					Skill expert = SkillFactory.getSkill(4120010);
					if (chr.getTotalSkillLevel(expert) > 0) {
						SecondaryStatEffect eff = expert.getEffect(chr.getTotalSkillLevel(expert));
						if (eff.makeChanceResult()) {
							ipp.setQuantity((short) (ipp.getQuantity() + 1));
							c.getSession().writeAndFlush(CWvsContext.InventoryPacket
									.updateInventorySlot(MapleInventoryType.USE, ipp, false));
							bulletConsume = 0;
							c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
						}
					}
				}
				if (bulletConsume > 0 && !MapleInventoryManipulator.removeById(c, MapleInventoryType.USE, projectile,
						bulletConsume, false, true)) {
					chr.dropMessage(5, "You do not have enough arrows/bullets/stars.");
					return;
				}
			}
		} else if (chr.getJob() >= 3500 && chr.getJob() <= 3512) {
			visProjectile = 2333000;
		} else if (GameConstants.isCannon(chr.getJob())) {
			visProjectile = 2333001;
		}
		int projectileWatk = 0;
		if (projectile != 0) {
			projectileWatk = MapleItemInformationProvider.getInstance().getWatkForProjectile(projectile);
		}
		PlayerStats statst = chr.getStat();
		switch (attack.skill) {
		case 4001344:
		case 4121007:
		case 14001004:
		case 14111005:
			basedamage = Math.max(statst.getCurrentMaxBaseDamage(),
					statst.getTotalLuk() * 5.0F * (statst.getTotalWatk() + projectileWatk) / 100.0F);
			break;
		case 4111004:
			basedamage = 53000.0D;
			break;
		default:
			basedamage = statst.getCurrentMaxBaseDamage();
			switch (attack.skill) {
			case 3101005:
				basedamage *= effect.getX() / 100.0D;
				break;
			}
			break;
		}
		if (effect != null) {
			basedamage *= (effect.getDamage() + statst.getDamageIncrease(attack.skill)) / 100.0D;
			long money = effect.getMoneyCon();
			if (money != 0L) {
				if (money > chr.getMeso()) {
					money = chr.getMeso();
				}
				chr.gainMeso(-money, false);
			}
		}
		for (Item item : chr.getInventory(MapleInventoryType.CASH).newList()) {
			if (item.getItemId() / 1000 == 5021) {
				attack.item = item.getItemId();
			}
		}
		chr.checkFollow();
		if (!chr.isHidden()) {
			if (attack.skill == 3211006) {
				chr.getMap().broadcastMessage(chr, CField.addAttackInfo(2, chr, attack), chr.getTruePosition());
			} else {
				chr.getMap().broadcastMessage(chr, CField.addAttackInfo(1, chr, attack), chr.getTruePosition());
			}
		} else if (attack.skill == 3211006) {
			chr.getMap().broadcastGMMessage(chr, CField.addAttackInfo(2, chr, attack), false);
		} else {
			chr.getMap().broadcastGMMessage(chr, CField.addAttackInfo(1, chr, attack), false);
		}
		DamageParse.applyAttack(attack, skill, chr, basedamage, effect,
				(ShadowPartner != null) ? AttackType.RANGED_WITH_SHADOWPARTNER : AttackType.RANGED, false, false);
	}

	public static final void MagicDamage(LittleEndianAccessor slea, MapleClient c, MapleCharacter chr, boolean chilling,
			boolean orbital) {
		if (chr == null || chr.getMap() == null) {
			return;
		}
		if (chr.getMapId() == ServerConstants.warpMap) {
			return;
		}

		AttackInfo attack = DamageParse.parseDmgMa(slea, chr, chilling, orbital);
		if (attack == null) {
			c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
			return;
		}
		Skill skill = SkillFactory.getSkill(GameConstants.getLinkedSkill(attack.skill));
		if (skill == null || (GameConstants.isAngel(attack.skill)
				&& (chr.getStat()).equippedSummon % 10000 != attack.skill % 10000)) {
			c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
			return;
		}
		int skillLevel = chr.getTotalSkillLevel(skill);
		SecondaryStatEffect effect = attack.getAttackEffect(chr, skillLevel, skill);
		if (effect == null) {
			return;
		}

		if (GameConstants.isFPMage(chr.getJob())) {
			if (chr.getMap().getMist(chr.getId(), 2111013) != null) {
				// chr.getMap().broadcastMessage(CField.PoisonRegionMetastasis(2111013, 2111014,
				// chr.getPositionX(), (byte)0));
				// System.out.println("das");
			}
		}
		if (attack.skill == 2121011 || attack.skill == 2111003
				|| (!GameConstants.isNoDelaySkill(attack.skill) && !GameConstants.isNoApplySkill(attack.skill)
						&& !orbital && !GameConstants.isFusionSkill(attack.skill))) {
			if (GameConstants.is_evan_force_skill(attack.skill)) {
				if (!chr.skillisCooling(attack.skill)) {
					skill.getEffect(skillLevel).applyTo(chr);
				}
			} else if (attack.skill == 2111003) {
				if (chr.getFlameHeiz() == null) {
					SkillFactory.getSkill(2111003).getEffect(chr.getSkillLevel(2121011)).applyTo(chr,
							attack.plusPosition3);
				} else {
					SkillFactory.getSkill(2111003).getEffect(chr.getSkillLevel(2121011)).applyTo(chr,
							chr.getFlameHeiz());
					chr.setFlameHeiz((Point) null);
				}
			} else if (attack.summonattack == 0 && !skill.isFinalAttack() && attack.skill != 400021064) {
				skill.getEffect(skillLevel).applyTo(chr);
			}
		}
		double maxdamage = (chr.getStat().getCurrentMaxBaseDamage()
				* (effect.getDamage() + chr.getStat().getDamageIncrease(attack.skill))) / 100.0D;
		if (GameConstants.isPyramidSkill(attack.skill)) {
			maxdamage = 1.0D;
		} else if (GameConstants.isBeginnerJob(skill.getId() / 10000) && skill.getId() % 10000 == 1000) {
			maxdamage = 40.0D;
		}
		if (GameConstants.isKinesis(chr.getJob())) {
			if (attack.skill != 142120003 && attack.skill != 142111002 && attack.skill != 142110003
					&& attack.skill != 142001002 && attack.skill != 142141000 && attack.skill != 142111007
					&& attack.skill != 142120000 && attack.skill != 142120001 && attack.skill != 142120002
					&& attack.skill != 400021048 && attack.skill != 400021008) { // 얼티메이트-메테리얼 VI 구현
				chr.givePPoint(attack.skill);
			} else if (attack.skill == 142120001) {
				boolean bossattack = false;
				if (chr.getSkillLevel(142120033) > 0) {
					for (AttackPair a : attack.allDamage) {
						MapleMonster m1 = MapleLifeFactory.getMonster(a.monsterId);
						if (m1.getStats().isBoss()) {
							bossattack = true;
							break;
						}
					}
					if (attack.skill == 142120001 && bossattack) {
						chr.givePPoint((byte) 1);
					}
				}
			}
		}
		if (chr.getSkillLevel(80002632) > 0) {
			Item weapon_item = chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11);
			if (weapon_item != null) {
				String weapon_name = MapleItemInformationProvider.getInstance().getName(weapon_item.getItemId());
				if (weapon_name != null && weapon_name.startsWith("제네시스 ") && !chr.skillisCooling(80002632)
						&& !chr.getBuffedValue(80002632)) {
					SecondaryStatEffect effcts = SkillFactory.getSkill(80002632).getEffect(chr.getSkillLevel(80002632));
					effcts.applyTo(chr);
					c.getSession().writeAndFlush(CField.skillCooldown(80002632, 90000));
					chr.addCooldown(80002632, System.currentTimeMillis(), 90000L);
				}
			}
		}
		if (chr.getSkillLevel(80002416) > 0) {
			Item weapon_item = chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11);
			if (weapon_item != null) {
				String weapon_name = MapleItemInformationProvider.getInstance().getName(weapon_item.getItemId());
				if (weapon_name != null && weapon_name.startsWith("아스칼론 ") && !chr.getBuffedValue(1221054)
						&& !chr.skillisCooling(1221054) && c.getPlayer().getSkillCustomValue0(1221054) == 0L) {
					SecondaryStatEffect effcts = SkillFactory.getSkill(1221054).getEffect(chr.getSkillLevel(1221054));
					chr.setSkillCustomInfo(1221054, 1, 300000L);
					effcts.applyTo(chr);
				} else if (weapon_name != null && weapon_name.startsWith("아스칼론 ") && !chr.getBuffedValue(32121017)) {
					SecondaryStatEffect effcts2 = SkillFactory.getSkill(32121017)
							.getEffect(chr.getSkillLevel(32121017));
					chr.addBuffCheck = 1;
					effcts2.applyTo(chr);
				}
			}
		}
		if (effect.getCooldown(chr) > 0 && !effect.ignoreCooldown(chr)
				&& !GameConstants.isExceptionSkill(chr.getJob(), attack.skill) && attack.skill != 142101009
				&& attack.skill != 12120011 && attack.skill != 2220014 && attack.skill != 2120013
				&& attack.skill != 400021004) {
			if (chr.skillisCooling(effect.getSourceId()) && !GameConstants.isCooltimeKeyDownSkill(effect.getSourceId())
					&& !GameConstants.isNoApplySkill(attack.skill) && !GameConstants.isLinkedSkill(effect.getSourceId())
					&& !chr.getBuffedValue(effect.getSourceId()) && chr.unstableMemorize != effect.getSourceId()) {
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				return;
			}
			if (effect.getSourceId() == 15111022 || effect.getSourceId() == 15120003) {
				if (!chr.getBuffedValue(15121054)) {
					c.getSession().writeAndFlush(CField.skillCooldown(effect.getSourceId(), effect.getCooldown(chr)));
					chr.addCooldown(effect.getSourceId(), System.currentTimeMillis(), effect.getCooldown(chr));
				}
			} else if (effect.getSourceId() == 1321013) {
				if (!chr.getBuffedValue(1321015) && chr.getBuffedEffect(SecondaryStat.Reincarnation) == null) {
					c.getSession().writeAndFlush(CField.skillCooldown(effect.getSourceId(), effect.getCooldown(chr)));
					chr.addCooldown(effect.getSourceId(), System.currentTimeMillis(), effect.getCooldown(chr));
				}
			} else if (chr.getBuffedValue(20040219) || chr.getBuffedValue(20040220)) {
				if (skill.isHyper() || !GameConstants.isLuminous(effect.getSourceId() / 10000)) {
					c.getSession().writeAndFlush(CField.skillCooldown(effect.getSourceId(), effect.getCooldown(chr)));
					chr.addCooldown(effect.getSourceId(), System.currentTimeMillis(), effect.getCooldown(chr));
				}
			} else if (!chr.skillisCooling(effect.getSourceId())
					&& !GameConstants.isAutoAttackSkill(effect.getSourceId())
					&& !GameConstants.isAfterCooltimeSkill(effect.getSourceId()) && !chr.memoraizecheck) {
				c.getSession().writeAndFlush(CField.skillCooldown(effect.getSourceId(), effect.getCooldown(chr)));
				chr.addCooldown(effect.getSourceId(), System.currentTimeMillis(), effect.getCooldown(chr));
			}
		}
		if (chr.memoraizecheck) {
			chr.memoraizecheck = false;
		}
		chr.checkFollow();
		if (!chr.isHidden()) {
			chr.getMap().broadcastMessage(chr, CField.addAttackInfo(3, chr, attack), chr.getTruePosition());
		} else {
			chr.getMap().broadcastGMMessage(chr, CField.addAttackInfo(3, chr, attack), false);
		}
		if (GameConstants.isBattleMage(chr.getJob())) {
			if (chr.getBuffedValue(SecondaryStat.BMageDeath) != null && !chr.skillisCooling(32001114)) {
				int duration = (chr.getSkillLevel(32120019) > 0) ? 5000
						: ((chr.getSkillLevel(32110017) > 0) ? 6000
								: ((chr.getSkillLevel(32100010) > 0) ? 8000 : 9000));
				if (chr.getSkillCustomValue0(32120019) > 0L) {
					chr.setDeath((byte) 0);
					chr.addCooldown(32001114, System.currentTimeMillis(), duration);
					chr.removeSkillCustomInfo(32120019);
					for (MapleSummon summon : chr.getSummons()) {
						if (summon.getSkill() == chr.getBuffSource(SecondaryStat.BMageDeath)) {
							chr.getClient().getSession().writeAndFlush(CField.SummonPacket.DeathAttack(summon));
							break;
						}
					}
					Map<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<>();
					statups.put(SecondaryStat.BMageDeath,
							new Pair<>(Integer.valueOf(chr.getDeath()), Integer.valueOf(0)));
					chr.getClient().getSession().writeAndFlush(CWvsContext.BuffPacket.giveBuff(statups,
							chr.getBuffedEffect(SecondaryStat.BMageDeath), chr));
				}
			}
		} else if (GameConstants.isEvan(chr.getJob())) {
			if (attack.isLink) {
				SkillFactory.getSkill(22110016).getEffect(1).applyTo(chr);
			}
		} else if (GameConstants.isFlameWizard(chr.getJob())) {

			if (orbital && attack.targets > 0 && attack.skill != 12120010) {
				long stack = chr.getSkillCustomValue0(12101024);
				if (stack == 10) {
					ArrayList<Triple<Integer, Integer, Integer>> finalMobList = new ArrayList();
					for (int i = 0; i < attack.targets; i++) {
						int oid = attack.allDamage.get(i).objectId;
						if (chr.getMap().getMonsterByOid(oid) != null) {
							finalMobList.add(new Triple(oid, 0, 0));
						}
					}
					if (finalMobList.size() > 0) {
						c.getSession()
								.writeAndFlush(CField.bonusAttackRequest(12101030, finalMobList, false, 0, new int[0]));
					}
					chr.setSkillCustomInfo(12101024, 0, 0);
				} else {
					chr.setSkillCustomInfo(12101024, stack + 1, 0);
				}
			}
		}

		DamageParse.applyAttackMagic(attack, skill, chr, effect, maxdamage);
	}

	public static final void DropMeso(int meso, MapleCharacter chr) {
		if (!chr.isAlive() || meso < 10 || meso > 50000 || meso > chr.getMeso()) {
			chr.getClient().getSession().writeAndFlush(CWvsContext.enableActions(chr));
			return;
		}
		chr.gainMeso(-meso, false, false);
		chr.getMap().spawnMesoDrop(meso, chr.getTruePosition(), chr, chr, true, (byte) 0);
	}

	public static final void ChangeAndroidEmotion(int emote, MapleCharacter chr) {
		if (emote > 0 && chr != null && chr.getMap() != null && !chr.isHidden() && emote <= 17
				&& chr.getAndroid() != null) {
			chr.getMap().broadcastMessage(CField.showAndroidEmotion(chr.getId(), emote));
		}
	}

	public static final void MoveAndroid(LittleEndianAccessor slea, MapleClient c, MapleCharacter chr) {
		slea.skip(8);
		int unk1 = slea.readInt();
		int unk2 = slea.readInt();
		List<LifeMovementFragment> res = MovementParse.parseMovement(slea, 3);
		if (res != null && chr != null && res.size() != 0 && chr.getMap() != null && chr.getAndroid() != null) {
			Point pos = new Point(chr.getAndroid().getPos());
			chr.getAndroid().updatePosition(res);
			chr.getMap().broadcastMessage(chr, CField.moveAndroid(chr.getId(), pos, res, unk1, unk2), false);
		}
	}

	public static final void MoveHaku(LittleEndianAccessor slea, MapleClient c, MapleCharacter chr) {
		slea.skip(12);
		List<LifeMovementFragment> res = MovementParse.parseMovement(slea, 3);
		if (res != null && chr != null && res.size() != 0 && chr.getMap() != null && chr.getAndroid() != null) {
			Point pos = new Point(chr.getAndroid().getPos());
			chr.getHaku().updatePosition(res);
			chr.getMap().broadcastMessage(chr, CField.moveHaku(chr.getId(), pos, res), false);
		}
	}

	public static final void ChangeEmotion(int emote, MapleCharacter chr) {
		if (emote > 7) {
			int emoteid = 5159992 + emote;
			MapleInventoryType type = GameConstants.getInventoryType(emoteid);
			if (chr.getInventory(type).findById(emoteid) == null) {
				return;
			}
		}
		if (emote > 0 && chr != null && chr.getMap() != null && !chr.isHidden()) {
			chr.getMap().broadcastMessage(chr, CField.facialExpression(chr, emote), false);
		}
	}

	public static final void BlackMageBallRecv(LittleEndianAccessor slea, MapleCharacter chr) {
		int type = slea.readInt();

		if (chr.isAlive()) {
			Map<SecondaryStat, Pair<Integer, Integer>> diseases = new EnumMap<>(SecondaryStat.class);
			switch (type) {
			case 1:
				diseases.put(SecondaryStat.CurseOfCreation, new Pair<>(Integer.valueOf(4), Integer.valueOf(6000)));
				break;
			case 2:
				diseases.put(SecondaryStat.CurseOfDestruction, new Pair<>(Integer.valueOf(10), Integer.valueOf(6000)));
				break;
			}
			if ((chr.hasDisease(SecondaryStat.CurseOfCreation) && type == 2)
					|| (chr.hasDisease(SecondaryStat.CurseOfDestruction) && type == 1)) {
				chr.setDeathCount((byte) (chr.getDeathCount() - 1));
				chr.getClient().getSession().writeAndFlush(CField.BlackMageDeathCountEffect());
				chr.getClient().getSession().writeAndFlush(CField.getDeathCount(chr.getDeathCount()));
				chr.dispelDebuffs();
				if (chr.getDeathCount() > 0) {
					chr.addHP(-chr.getStat().getCurrentMaxHp() * 30L / 100L);
					if (chr.isAlive()) {
						MobSkillFactory.getMobSkill(120, 39).applyEffect(chr, null, true, false);
					}
				} else {
					chr.addHP(-chr.getStat().getCurrentMaxHp());
				}
			}
			chr.giveDebuff(diseases, MobSkillFactory.getMobSkill(249, (chr.getBlackMageWB() == 2) ? 1 : 2));
		}
	}

	public static final void Heal(LittleEndianAccessor slea, MapleCharacter chr) {
		if (chr == null) {
			return;
		}
		slea.readInt();
		if (slea.available() >= 8L) {
			slea.skip(4);
		}
		int healHP = slea.readShort();
		int healMP = slea.readShort();
		PlayerStats stats = chr.getStat();
		if (stats.getHp() <= 0L && chr.getBattleGroundChr() != null) {
			return;
		}
		long now = System.currentTimeMillis();
		if (healHP != 0 && chr.canHP(now + 1000L) && chr.isAlive() && !chr.getMap().isTown()) {
			if (healHP > stats.getHealHP()) {
				healHP = (int) stats.getHealHP();
			}
			chr.addHP(healHP);
		}
		if (healMP != 0 && !GameConstants.isDemonSlayer(chr.getJob()) && chr.canMP(now + 1000L) && chr.isAlive()
				&& !chr.getMap().isTown()) {
			if (healMP > stats.getHealMP()) {
				healMP = (int) stats.getHealMP();
			}
			chr.addMP(healMP);
		}
	}

	public static void ApplyLinkPresets(final LittleEndianAccessor slea, final MapleClient c) {
		final byte size = slea.readByte();
		for (int i = 0; i < size; ++i) {
			final int unlinkskill = slea.readInt();
			UnlinkSkill(unlinkskill, c);
		}
		final byte size2 = slea.readByte();
		for (int q = 0; q < size2; ++q) {
			final int skillid = slea.readInt();
			final int sendid = slea.readInt();
			slea.skip(12);
			LinkSkill(skillid, sendid, c.getPlayer().getId(), c);
		}
	}

	public static final void MovePlayer(LittleEndianAccessor slea, MapleClient c, MapleCharacter chr) {
		List<LifeMovementFragment> res;
		if (chr == null) {
			return;
		}

		chr.setLastMovement(System.currentTimeMillis());
		slea.skip(22);
		Point Original_Pos = chr.getPosition();
		try {
			res = MovementParse.parseMovement(slea, 1);
		} catch (ArrayIndexOutOfBoundsException e) {
			// System.out.println("unk movement type \n : " + a);
			return;
		}
		if (res != null && c.getPlayer().getMap() != null) {
			boolean samepos;
			MapleMap map = c.getPlayer().getMap();
			if (chr.isHidden()) {
				chr.setLastRes(res);
				c.getPlayer().getMap().broadcastGMMessage(chr, CField.movePlayer(chr.getId(), res, Original_Pos),
						false);
			} else {
				c.getPlayer().getMap().broadcastMessage(c.getPlayer(),
						CField.movePlayer(chr.getId(), res, Original_Pos), false);
			}
			MovementParse.updatePosition(res, chr, 0);
			Point pos = chr.getTruePosition();
			map.movePlayer(chr, pos);
			if (chr.getFollowId() > 0 && chr.isFollowOn() && chr.isFollowInitiator()) {
				MapleCharacter fol = map.getCharacterById(chr.getFollowId());
				if (fol != null) {
					Point original_pos = fol.getPosition();
					MovementParse.updatePosition(res, fol, 0);
					map.movePlayer(fol, pos);
					map.broadcastMessage(fol, CField.movePlayer(fol.getId(), res, original_pos), false);
				} else {
					chr.checkFollow();
				}
			}
			int count = c.getPlayer().getFallCounter();
			boolean bl = samepos = pos.y > c.getPlayer().getOldPosition().y
					&& Math.abs(pos.x - c.getPlayer().getOldPosition().x) < 5;
			if (samepos && (pos.y > map.getBottom() + 250 || map.getFootholds().findBelow(pos) == null)) {
				if (count > 5) {
					c.getPlayer().changeMap(map, map.getPortal(0));
					c.getPlayer().setFallCounter(0);
				} else {
					c.getPlayer().setFallCounter(++count);
				}
			} else if (count > 0) {
				c.getPlayer().setFallCounter(0);
			}
			if (c.getPlayer().getMap().getId() == 450013500 && c.getPlayer().getMap().isBlackMage3thSkill()
					&& c.getPlayer().getPosition().x >= 85) {
				c.getPlayer().BlackMage3thDamage();
			}
			c.getPlayer().setOldPosition(pos);
		}
	}

	public static final void ChangeMapSpecial(String portal_name, MapleClient c, MapleCharacter chr) {
		if (chr == null || chr.getMap() == null) {
			return;
		}
		MaplePortal portal = chr.getMap().getPortal(portal_name);
		if (portal != null) {
			c.getPlayer().dropMessageGM(6, "PortalName : " + portal.getScriptName() + "");
			c.removeClickedNPC();
			NPCScriptManager.getInstance().dispose(c);
			c.getSession().writeAndFlush(CWvsContext.enableActions(chr));
			if (portal.getScriptName().equals("banbanGoInside")) {
				String portalname = "Pt0" + c.getPlayer().getMap().getCustomValue0(8910000);
				if (portal.getName().equals(portalname)) {
					portal.enterPortal(c);
				} else {
					c.removeClickedNPC();
					NPCScriptManager.getInstance().dispose(c);
					c.getSession().writeAndFlush(CWvsContext.enableActions(chr));
				}
			} else if (portal.getScriptName().contains("base_slime") && c.getPlayer().getMapId() == 160040000) {
				MapleMap mapz = ChannelServer.getInstance(c.getChannel()).getMapFactory().getMap(160040000);
				c.getPlayer().changeMap(mapz, mapz.getPortal(0));
				c.getSession().writeAndFlush(MobPacket.BossSlime.SlimePacket(8, mapz));
				c.getSession().writeAndFlush(MobPacket.BossSlime.SlimePacket(3, mapz));
			} else if (portal.getScriptName().contains("Blooming") && c.getPlayer().getMapId() == 993192600) {
				c.getPlayer().dropMessageGM(6, "PortalNam125e : " + portal.getName() + "");
				if (portal.getName().contains("ch01_")) {
					String warp = "ch01_" + BloomingRace.getRandPortal1();
					if (warp.equals(portal.getName())) {
						c.send(CField.fireBlink(chr.getId(), new Point(1798, 533)));
						c.send(SLFCGPacket.BloomingRaceAchieve(19));
						c.getPlayer().addKV("BloomingRaceAchieve", "19");
					} else {
						c.send(CField.instantMapWarp(chr, (byte) 0));
					}
				} else if (portal.getName().contains("next01")) {
					String warp = "next01_" + BloomingRace.getRandPortal2();
					if (warp.equals(portal.getName())) {
						c.send(CField.fireBlink(chr.getId(), new Point(3483, 534)));
						c.getPlayer().addKV("BloomingRaceAchieve", "39");
						c.send(SLFCGPacket.BloomingRaceAchieve(39));
					} else {
						c.send(CField.fireBlink(chr.getId(), new Point(1798, 533)));
					}
				} else if (portal.getName().contains("ch02_")) {
					String warp = "ch02_" + BloomingRace.getRandPortal3();
					if (warp.equals(portal.getName())) {
						c.send(CField.fireBlink(chr.getId(), new Point(5009, 532)));
						c.getPlayer().addKV("BloomingRaceAchieve", "59");
						c.send(SLFCGPacket.BloomingRaceAchieve(59));
					} else {
						c.send(CField.fireBlink(chr.getId(), new Point(4150, -129)));
					}
				} else if (portal.getName().contains("final00")) {
					c.send(CField.fireBlink(chr.getId(), new Point(6762, 534)));
					c.getPlayer().addKV("BloomingRaceAchieve", "79");
					c.send(SLFCGPacket.BloomingRaceAchieve(79));
				} else if (portal.getName().contains("clear00")) {
					c.send(CField.fireBlink(chr.getId(), new Point(9410, 534)));
					c.getPlayer().addKV("BloomingRaceAchieve", "128");
					c.send(SLFCGPacket.BloomingRaceAchieve(128));
				} else if (portal.getName().contains("goal00")) {
					BloomingRace.setRank(BloomingRace.getRank() + 1);
					if (BloomingRace.getRank() >= 1 && BloomingRace.getRank() <= 3) {
						BloomingRace.getRankList().add(chr);
						c.getPlayer().getMap().broadcastMessage(SLFCGPacket
								.BloomingRaceRanking((BloomingRace.getRank() == 1), BloomingRace.getRankList()));
					}
					c.send(CField.fireBlink(chr.getId(), new Point(9426, -965)));
					c.getPlayer().addKV("BloomingRaceRank", BloomingRace.getRank() + "");
					c.send(CField.enforceMSG("끝까지 올라왔어! 나에게 말을 걸어줘. 보상을 받기 위해 이동시켜줄게.", 287, 3500));
					c.send(CField.environmentChange("Effect/EventEffect.img/2021BloomingRace/success", 19));
				}
				c.getSession().writeAndFlush(CWvsContext.enableActions(chr));
			} else {
				portal.enterPortal(c);
			}
		} else {
			c.removeClickedNPC();
			NPCScriptManager.getInstance().dispose(c);
			c.getSession().writeAndFlush(CWvsContext.enableActions(chr));
		}
	}

	public static final void ChangeMap(LittleEndianAccessor slea, MapleClient c, MapleCharacter chr) {
		if (chr == null || chr.getMap() == null) {
			return;
		}

		if (chr.getMapId() == 100000203 && chr.getPvpStatus()) {
			c.getPlayer().dropMessage(5, "PVP도중에는 나가실 수 없습니다.");
			c.getSession().writeAndFlush(CWvsContext.enableActions(chr));
			return;
		}
		if ((chr.getMapId() == 931050800 || chr.getMapId() == 931050810 || chr.getMapId() == 931050820)
				&& chr.getEventInstance() != null) {
			c.getPlayer().dropMessage(5, "보스레이드 도중에는 나가실 수 없습니다. 나가시려면 문 교수를 통해 나가주세요.");
			c.getSession().writeAndFlush(CWvsContext.enableActions(chr));
			return;
		}
		// 6차 구현
		if (chr.getLevel() >= 260) {
			for (MapleSummon summon : chr.getSummons()) { // 清除六转召唤物（如Hexa矩阵召唤兽）
				int originSkillId = GameConstants.getHexaOriginFirst(chr.getJob());

				if ((((summon.getSkill() / originSkillId) == 1) && ((summon.getSkill() % originSkillId) < 10))
						|| summon.getSkill() == 500001002 || summon.getSkill() == 500001003
						|| summon.getSkill() == 500001004) {
					chr.removeSummon(summon);
					chr.getMap().broadcastMessage(CField.SummonPacket.removeSummon(summon, true));
				}
			}
		}
		if (slea.available() != 0L) {
			slea.skip(15);
			boolean isDead = slea.readByte() == 1;
			int targetid = slea.readInt();
			MaplePortal portal = chr.getMap().getPortal(slea.readMapleAsciiString());
			if (slea.available() >= 6L) {
				slea.readInt();
			}
			if (chr.getMapId() == 180010003) {
				chr.getStat().setHp(50L, chr);
				MapleMap map = chr.getMap();
				MapleMap to = null;
				if (map.getForcedReturnId() != 999999999 && map.getForcedReturnMap() != null) {
					to = map.getForcedReturnMap();
				} else {
					to = map.getReturnMap();
				}
				chr.changeMap(to, to.getPortal(0));
				NPCScriptManager.getInstance().start(c, 2007);
				return;
			}
			boolean wheel = (slea.readShort() > 0 && chr.haveItem(5510000, 1, false, true)
					&& chr.getMapId() / 1000000 != 925);
			boolean bufffreezer = (slea.readByte() > 0 && chr.haveItem(5133000, 1, false, true));
			if (bufffreezer) {
				int buffFreezer;
				if (c.getPlayer().itemQuantity(5133000) > 0) {
					buffFreezer = 5133000;
				} else {
					buffFreezer = 5133001;
				}
				c.getPlayer().setUseBuffFreezer(true);
				boolean practice = false;
				if (c.getPlayer().getV("bossPractice") != null
						&& Integer.parseInt(c.getPlayer().getV("bossPractice")) == 1) {
					practice = true;
				}
				if (!practice) {
					c.getPlayer().removeItem(buffFreezer, -1);
				}
				c.getSession().writeAndFlush(CField.buffFreezer(buffFreezer, practice));
			}
			MapleQuest.getInstance(1097).forceStart(chr, 0, bufffreezer ? "1" : "0");
			if (targetid != -1 && !chr.isAlive()) {
				chr.setStance(0);
				if (chr.getEventInstance() != null && chr.getEventInstance().revivePlayer(chr) && chr.isAlive()) {
					return;
				}
				// 버프프리저 사라짐
				if (!chr.isUseBuffFreezer()) {
					chr.cancelAllBuffs_();
				}
				if (wheel) {
					MapleInventoryManipulator.removeById(c, MapleInventoryType.CASH, 5510000, 1, true, false);
					c.getSession().writeAndFlush(CField.EffectPacket.showWheelEffect(5510000));
					chr.getStat().setHp(chr.getStat().getCurrentMaxHp(), chr);
					MapleMap to = chr.getMap();

					if (ServerConstants.worldbossmap == chr.getMapId()) {
						chr.sethottimeboss(false);
						c.getSession().writeAndFlush(CField.sendDuey((byte) 28, null, null));
					}

					chr.changeMap(to, to.getPortal(0));
				} else if (chr.getDeathCount() > 0 || chr.liveCounts() > 0) {
					chr.getStat().setHp(chr.getStat().getCurrentMaxHp(), chr);
					chr.getStat().setMp(chr.getStat().getCurrentMaxMp(chr), chr);
					MapleMap to = chr.getMap();
					if (chr.getMapId() == 272020200) {
						to = ChannelServer.getInstance(c.getChannel()).getMapFactory().getMap(272020400);
					} else if (chr.getMapId() == 262030300 || chr.getMapId() == 262031300) {
						to = ChannelServer.getInstance(c.getChannel()).getMapFactory().getMap(chr.getMapId() + 10);
					} else if (chr.getMapId() == 105200120 || chr.getMapId() == 105200520) {
						to = ChannelServer.getInstance(c.getChannel()).getMapFactory().getMap(chr.getMapId() - 10);
					}
					chr.changeMap(to, to.getPortal(0));
				} else {
					chr.getStat().setHp((short) (int) chr.getStat().getCurrentMaxHp(), chr);
					MapleMap map = chr.getMap();
					MapleMap to = null;
					if (map.getForcedReturnId() != 999999999 && map.getForcedReturnMap() != null) {
						to = map.getForcedReturnMap();
					} else {
						to = map.getReturnMap();
					}
					chr.changeMap(to, to.getPortal(0));
				}
				SkillFactory.getSkill(80000329).getEffect(chr.getSkillLevel(80000329)).applyTo(chr, false);
			} else if (targetid != -1 && chr.isIntern()) {
				MapleMap to = ChannelServer.getInstance(c.getChannel()).getMapFactory().getMap(targetid);
				if (to != null) {
					chr.changeMap(to, to.getPortal(0));
				} else {
					chr.dropMessage(5, "映射為空。 使用！ 改為warp<mapid>.");
				}
			} else if (targetid != -1 && !chr.isIntern()) {
				int divi = chr.getMapId() / 100;
				boolean unlock = false, warp = false;
				if (divi == 9130401) {
					warp = (targetid / 100 == 9130400 || targetid / 100 == 9130401);
					if (targetid / 10000 != 91304) {
						warp = true;
						unlock = true;
						targetid = 130030000;
					}
				} else if (divi == 9130400) {
					warp = (targetid / 100 == 9130400 || targetid / 100 == 9130401);
					if (targetid / 10000 != 91304) {
						warp = true;
						unlock = true;
						targetid = 130030000;
					}
				} else if (divi == 9140900) {
					warp = (targetid == 914090011 || targetid == 914090012 || targetid == 914090013
							|| targetid == 140090000);
				} else if (divi == 9120601 || divi == 9140602 || divi == 9140603 || divi == 9140604
						|| divi == 9140605) {
					warp = (targetid == 912060100 || targetid == 912060200 || targetid == 912060300
							|| targetid == 912060400 || targetid == 912060500 || targetid == 3000100);
					unlock = true;
				} else if (divi == 9101500) {
					warp = (targetid == 910150006 || targetid == 101050010);
					unlock = true;
				} else if (divi == 9140901 && targetid == 140000000) {
					unlock = true;
					warp = true;
				} else if (divi == 9240200 && targetid == 924020000) {
					unlock = true;
					warp = true;
				} else if (targetid == 980040000 && divi >= 9800410 && divi <= 9800450) {
					warp = true;
				} else if (divi == 9140902 && (targetid == 140030000 || targetid == 140000000)) {
					unlock = true;
					warp = true;
				} else if (divi == 9000900 && targetid / 100 == 9000900 && targetid > chr.getMapId()) {
					warp = true;
				} else if (divi / 1000 == 9000 && targetid / 100000 == 9000) {
					unlock = (targetid < 900090000 || targetid > 900090004);
					warp = true;
				} else if (divi / 10 == 1020 && targetid == 1020000) {
					unlock = true;
					warp = true;
				} else if (chr.getMapId() == 900090101 && targetid == 100030100) {
					unlock = true;
					warp = true;
				} else if (chr.getMapId() == 2010000 && targetid == 104000000) {
					unlock = true;
					warp = true;
				} else if (chr.getMapId() == 106020001 || chr.getMapId() == 106020502) {
					if (targetid == chr.getMapId() - 1) {
						unlock = true;
						warp = true;
					}
				} else if (chr.getMapId() == 0 && targetid == 10000) {
					unlock = true;
					warp = true;
				} else if (chr.getMapId() == 931000011 && targetid == 931000012) {
					unlock = true;
					warp = true;
				} else if (chr.getMapId() == 931000021 && targetid == 931000030) {
					unlock = true;
					warp = true;
				}
				if (unlock) {
					c.getSession().writeAndFlush(CField.UIPacket.IntroDisableUI(false));
					c.getSession().writeAndFlush(CField.UIPacket.IntroLock(false));
					c.getSession().writeAndFlush(CWvsContext.enableActions(chr));
				}
				if (warp) {
					MapleMap to = ChannelServer.getInstance(c.getChannel()).getMapFactory().getMap(targetid);
					chr.changeMap(to, to.getPortal(0));
				}
			} else if (portal != null) {
				if (chr.getMapId() == 993192600) {
					if (BloomingRace.isStart()) {
						chr.getClient().send(CField.instantMapWarp(chr, (byte) 0));
						chr.getMap().movePlayer(c.getPlayer(),
								new Point(c.getPlayer().getMap().getPortal(0).getPosition()));
					} else {
						chr.dropMessage(5, "지금은 포탈이 닫혀있습니다.");
					}
					c.getSession().writeAndFlush(CWvsContext.enableActions(chr));
				} else if (GameConstants.isContentsMap(chr.getMapId())) {
					MaplePortal target = chr.getMap().getPortal(portal.getTarget());
					if (target != null) {
						chr.getClient().send(CField.instantMapWarp(chr, (byte) target.getId()));
					}
					c.getSession().writeAndFlush(CWvsContext.enableActions(chr));
				} else {
					portal.enterPortal(c);
				}
			} else {
				c.getSession().writeAndFlush(CWvsContext.enableActions(chr));
			}
		}
	}
// BOSS传送门 传送门

	public static final void InnerPortal(LittleEndianAccessor slea, MapleClient c, MapleCharacter chr) {
		if (chr == null || chr.getMap() == null) {
			return;
		}
		String portalName = slea.readMapleAsciiString();

		MaplePortal portal = chr.getMap().getPortal(portalName);
		int toX = slea.readShort();
		int toY = slea.readShort();

		if (portal == null) {// 如果portal对象为空，则写入并刷新允许对客户端会话执行操作的数据包，然后返回方法。
			c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
			return;
		}

		// 如果portal的位置与chr的实际位置之间的距离的平方大于22500，或者如果chr不是GM（游戏主节点），则写入并刷新允许对客户端会话进行操作的数据包，然后返回方法。
		if (portal.getPosition().distanceSq(chr.getTruePosition()) > 22500.0D && !chr.isGM()) {
			c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
			return;
		}
		// 如果portal的名称是“east01”、“west00”或“pt00”之一，则通过portalScriptManager的实例为portal运行脚本。
		if (portal.getName().equals("east00") || portal.getName().equals("east01") || portal.getName().equals("west00")
				|| portal.getName().equals("pt00") || portal.getName().equals("market00") || // 自由市场
				portal.getName().equals("pt2_221030910") || // 卡熊
				portal.getName().equals("pt_hillah") || // 希拉
				portal.getName().equals("pt_magnusDoor") || // 麦格纳斯
				portal.getName().equals("lioncastleout") || // 班.雷昂
				portal.getName().equals("out_cygnus") || // 希纳斯
				portal.getName().equals("pt_portalNPC1") || // 阿卡伊勒
				portal.getName().equals("hid00") || // 鲁比塔斯
				portal.getName().equals("pt_bh_bossOut") || // 斯乌普通
				portal.getName().equals("pt_bh_bossOutN") || // 斯乌困难
				portal.getName().equals("pt_will_out") || // 威尔普通 困难
				portal.getName().equals("ptDemianOut") || // 戴米安普通
				portal.getName().equals("ptDemianOut_R") || // 戴米安 困难
				portal.getName().equals("pt_jinHillah_out") || // 真.希拉普通 困难 地狱 极限
				portal.getName().equals("pt_GC_out") || // 敦凯尔 普通 困难 地狱 极限
				portal.getName().equals("pt_BM1_bossOut") || // 至暗魔晶
				portal.getName().equals("pt_00") || // 黑魔法师 普通 困难 地狱 极限
				portal.getName().equals("out00") || // 赛伦 守护天使绿水灵
				portal.getName().equals("ptKalosOut") || // 卡洛斯
				portal.getName().equals("in00") || // 钓鱼 怪物公園00
				portal.getName().equals("in01") || // 怪物公園01
				portal.getName().equals("in02") || // 怪物公園02
				portal.getName().equals("in03") || // 怪物公園03
				portal.getName().equals("ptKaringOut") || // 卡琳1
				portal.getName().equals("exit00")) // 未知活動地圖
		{
			PortalScriptManager.getInstance().executePortalScript(portal, c);
		}
		// 如果portal的名称为“next00”，则只有当chr是团队的领导者时，才会通过portalScriptManager的实例为portal运行脚本。
		if (portal.getName().equals("next00")) { // 下一个地图 希拉 鲁塔常用 副本多地图
			if (c.getPlayer().getParty().getLeader().getId() == chr.getId()) {
				PortalScriptManager.getInstance().executePortalScript(portal, c);
			}
		}
		// 这段代码很有可能是游戏服务器端的逻辑。根据portal（传送门）的检查和条件，向客户端发送允许动作的数据包或运行特定脚本。

		int bossNumber = -1;

		if (c.getPlayer().getMapId() == 105100100 && portal.getName().equals("OutPerrion")) { // 巨大蝙蝠
			bossNumber = 0;
		} else if (c.getPlayer().getMapId() == 211042300 && portal.getName().equals("ps00")) { // 扎昆
			bossNumber = 1;
		} else if (c.getPlayer().getMapId() == 262030000 && portal.getName().equals("in00")) { // 希拉
			bossNumber = 3;
		} else if (c.getPlayer().getMapId() == 105200000 && portal.getName().equals("boss00")) { // 皮埃尔
			bossNumber = 4;
		} else if (c.getPlayer().getMapId() == 105200000 && portal.getName().equals("boss01")) { // 半半
			bossNumber = 5;
		} else if (c.getPlayer().getMapId() == 105200000 && portal.getName().equals("boss02")) { // 血腥女王
			bossNumber = 6;
		} else if (c.getPlayer().getMapId() == 105200000 && portal.getName().equals("boss03")) { // 贝伦
			bossNumber = 7;
		} else if (c.getPlayer().getMapId() == 211070000 && portal.getName().equals("lionCastleenter")) { // 班.雷昂
			bossNumber = 8;
		} else if (c.getPlayer().getMapId() == 272020110 && portal.getName().equals("AkayrumEnter")) { // 阿卡伊勒
			bossNumber = 9;
		} else if (c.getPlayer().getMapId() == 401060000 && portal.getName().equals("enter_magnusDoor")) { // 马格纳斯
			bossNumber = 10;
		} else if (c.getPlayer().getMapId() == 271040000 && portal.getName().equals("in00")) { // 堕落希纳斯
			bossNumber = 12;
		} else if (c.getPlayer().getMapId() == 350060300 && portal.getName().equals("bossIn00")) { // 斯乌
			bossNumber = 13;
		} else if (c.getPlayer().getMapId() == 105300303 && portal.getName().equals("pt_enter")) { // 戴米安
			bossNumber = 15;
		} else if (c.getPlayer().getMapId() == 220080000 && portal.getName().equals("in00")) { // 帕普拉图斯
			bossNumber = 22;
		} else if (c.getPlayer().getMapId() == 450011990 && portal.getName().equals("inMaze")) { // 真.希拉
			bossNumber = 24;
		} else if (c.getPlayer().getMapId() == 450012500 && portal.getName().equals("in_boss")) { // 黑魔法师
			bossNumber = 25;
		} else if (c.getPlayer().getMapId() == 450009301 && portal.getName().equals("in00")) { // 至暗魔晶
			bossNumber = 26;
		} else if (c.getPlayer().getMapId() == 450012200 && portal.getName().equals("in00")) { // 敦凯尔
			bossNumber = 27;
		}

		if (bossNumber != -1) {
			c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
			c.getSession().writeAndFlush(BossPacket.openBossUI(bossNumber));
		}

		if (bossNumber == -1) {
			chr.getMap().movePlayer(chr, new Point(toX, toY));
			chr.checkFollow();
			if (chr.getKeyValue(51351, "startquestid") == 49018L) {
				chr.setKeyValue(51351, "queststat", "3");
				chr.getClient()
						.send(CWvsContext.updateSuddenQuest((int) chr.getKeyValue(51351, "midquestid"), false,
								PacketHelper.getKoreanTimestamp(System.currentTimeMillis()) + 600000000L,
								"count=1;Quest=" + chr.getKeyValue(51351, "startquestid") + ";state=3;"));
			}
			c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
		}
	}

	public static Class<?> getBossPacketClass() throws ClassNotFoundException {
		return Class.forName("tools.packet.BossPacket");
	}

	public static final void snowBall(LittleEndianAccessor slea, MapleClient c) {
		c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
	}

	public static final void MirrorDungeon(LittleEndianAccessor slea, MapleClient c) {
		String d = slea.readMapleAsciiString();
		NPCScriptManager.getInstance().start(c, 2003, "EnterDungeon", d);
	}

	public static final void UpdateMedalDisplay(LittleEndianAccessor slea, MapleCharacter chr) {
		byte state = slea.readByte();
		chr.setKeyValue(101149, "1007", String.valueOf(state));
		chr.getMap().broadcastMessage(CField.showMedalDisplay(chr, state));
	}

	public static final void UpdateTitleDisplay(LittleEndianAccessor slea, MapleCharacter chr) {
		byte state = slea.readByte();
		chr.setKeyValue(101149, "1009", String.valueOf(state));
		chr.getMap().broadcastMessage(CField.showTitleDisplay(chr, state));
	}

	public static final void UpdateDamageSkin(LittleEndianAccessor slea, MapleClient c, MapleCharacter chr) {
		byte type = slea.readByte();

		int maxSize = (int) (chr.getKeyValue(13191, "skinroom") == -1 ? 0 : chr.getKeyValue(13191, "skinroom"));
		int size = (int) (chr.getKeyValue(13191, "skins") == -1 ? 0 : chr.getKeyValue(13191, "skins"));

		switch (type) {
		case 0:
			chr.setKeyValue(13191, size + "", chr.getKeyValue(7293, "damage_skin") + "");
			chr.setKeyValue(13191, "skins", (size + 1) + "");
			break;
		case 1:
			chr.setKeyValue(13191, size + "", -1 + "");
			chr.setKeyValue(13191, "skins", (size - 1) + "");
			break;
		case 2:
			if (maxSize <= size) {
				c.getSession().writeAndFlush(CWvsContext.enableActions(chr));
				return;
			}

			int index = slea.readShort();
			int skinid = slea.readShort();

			chr.setKeyValue(7293, "damage_skin", skinid + "");
			chr.dropMessage(5, "데미지 스킨이 변경되었습니다.");
			chr.getMap().broadcastMessage(CField.showForeignDamageSkin(chr, skinid));
			break;
		}

		chr.getClient().getSession().writeAndFlush(CField.damageSkin391((byte) 4, chr));
		chr.getClient().getSession().writeAndFlush(CField.damageSkin391(type, chr));
		c.getSession().writeAndFlush(CWvsContext.enableActions(chr));
	}

	static int Rank = 0;

	public static final void ChangeInner(LittleEndianAccessor slea, MapleClient c) {
		int preset = slea.readByte();
		int rank = ((InnerSkillValueHolder) c.getPlayer().getInnerSkills().get(preset).get(0)).getRank();
		int count = slea.readInt();
		int a = (((InnerSkillValueHolder) c.getPlayer().getInnerSkills().get(preset).get(0)).getRank() == 0) ? 100
				: ((((InnerSkillValueHolder) c.getPlayer().getInnerSkills().get(preset).get(0)).getRank() == 1) ? 200
						: ((((InnerSkillValueHolder) c.getPlayer().getInnerSkills().get(preset).get(0)).getRank() == 2)
								? 1500
								: 8000));
		int plus = (((InnerSkillValueHolder) c.getPlayer().getInnerSkills().get(preset).get(0)).getRank() == 2) ? 1500
				: 3000;
		int plus2 = (((InnerSkillValueHolder) c.getPlayer().getInnerSkills().get(preset).get(0)).getRank() == 2) ? 4000
				: 8000;
		int consume = a + ((count == 1) ? plus : ((count == 2) ? plus2 : 0));
		c.getPlayer().addHonorExp(-consume);
		List<InnerSkillValueHolder> newValues = new LinkedList<>();
		int i = 1;
		// 00 02 00 00 00 02 B1 1D 2C 04 1D 02 03 B0 1D 2C 04 19 02

		int line = (count >= 1) ? slea.readByte() : 0;
		slea.skip(6);
		int line2 = (count >= 2) ? slea.readByte() : 0;
		boolean check_rock = false;
		InnerSkillValueHolder ivholder = null;
		InnerSkillValueHolder ivholder2 = null;
		for (InnerSkillValueHolder isvh : c.getPlayer().getInnerSkills().get(preset)) {
			switch (count) {
			case 1:
				check_rock = (line == i);
				break;
			case 2:
				check_rock = (line == i || line2 == i);
				break;
			default:
				check_rock = false;
				break;
			}
			if (check_rock) {
				newValues.add(isvh);
				if (ivholder == null) {
					ivholder = isvh;
				} else if (ivholder2 == null) {
					ivholder2 = isvh;
				}
			} else if (ivholder == null) {
				int nowrank = -1;
				int rand = Randomizer.nextInt(100);
				nowrank = isvh.getRank();
				if (isvh.getRank() == 3) {
					nowrank = 3;
				} else if (isvh.getRank() == 2) {
					if (rand < 1) {
						nowrank = 3;
					} else {
						nowrank = 2;
					}
				} else if (isvh.getRank() == 1) {
					if (rand < 3) {
						nowrank = 2;
					} else {
						nowrank = 1;
					}
				} else if (rand < 5) {
					nowrank = 1;
				} else {
					nowrank = 0;
				}
				// [SEX] 어빌리티 재설정
				ivholder = InnerAbillity.getInstance().renewSkill(nowrank, false, preset);
				boolean breakout = false;
				while (!breakout) {
					if (count != 0) {
						if (count == 1) {
							// c.getPlayer().dropMessage(5, "" +
							// c.getPlayer().getInnerSkills().get(preset).size() + " line : " + line);
							if (ivholder.getSkillId() == c.getPlayer().getInnerSkills().get(preset).get(line - 1)
									.getSkillId()) {
								ivholder = InnerAbillity.getInstance().renewSkill(nowrank, false, preset);
								continue;
							}
							breakout = true;
							continue;
						}
						if (ivholder.getSkillId() == c.getPlayer().getInnerSkills().get(preset).get(line - 1)
								.getSkillId()
								|| ivholder.getSkillId() == c.getPlayer().getInnerSkills().get(preset).get(line2 - 1)
										.getSkillId()) {
							ivholder = InnerAbillity.getInstance().renewSkill(nowrank, false, preset);
							continue;
						}
						breakout = true;
						continue;
					}
					ivholder = InnerAbillity.getInstance().renewSkill(nowrank, false, preset);
					breakout = true;
				}
				newValues.add(ivholder);
			} else if (ivholder2 == null) {
				ivholder2 = InnerAbillity.getInstance()
						.renewSkill((ivholder.getRank() == 0) ? 0 : (ivholder.getRank() - 1), false, preset);
				boolean breakout = false;
				while (!breakout) {
					breakout = true;
					if (count != 0) {
						if (count == 1) {
							if (ivholder2.getSkillId() == c.getPlayer().getInnerSkills().get(preset).get(line - 1)
									.getSkillId()) {
								ivholder2 = InnerAbillity.getInstance().renewSkill(
										(ivholder.getRank() == 0) ? 0 : (ivholder.getRank() - 1), false, preset);
								breakout = false;
							}
						} else if (ivholder2.getSkillId() == c.getPlayer().getInnerSkills().get(preset).get(line - 1)
								.getSkillId()
								|| ivholder2.getSkillId() == c.getPlayer().getInnerSkills().get(preset).get(line - 1)
										.getSkillId()) {
							ivholder2 = InnerAbillity.getInstance().renewSkill(
									(ivholder.getRank() == 0) ? 0 : (ivholder.getRank() - 1), false, preset);
							breakout = false;
						}
					}
					if (ivholder.getSkillId() == ivholder2.getSkillId()) {
						ivholder2 = InnerAbillity.getInstance()
								.renewSkill((ivholder.getRank() == 0) ? 0 : (ivholder.getRank() - 1), false, preset);
						breakout = false;
					}
				}
				newValues.add(ivholder2);
			} else {
				InnerSkillValueHolder ivholder3 = InnerAbillity.getInstance()
						.renewSkill((ivholder.getRank() == 0) ? 0 : (ivholder.getRank() - 1), false, preset);
				while (ivholder.getSkillId() == ivholder3.getSkillId()
						|| ivholder2.getSkillId() == ivholder3.getSkillId()) {
					ivholder3 = InnerAbillity.getInstance()
							.renewSkill((ivholder.getRank() == 0) ? 0 : (ivholder.getRank() - 1), false, preset);
				}
				newValues.add(ivholder3);
			}
			c.getPlayer().changeSkillLevel(SkillFactory.getSkill(isvh.getSkillId()), (byte) 0, (byte) 0);
			i++;
		}

		c.getPlayer().getInnerSkills().get(preset).clear();

		for (InnerSkillValueHolder isvh : newValues) {
			c.getPlayer().getInnerSkills().get(preset).add(isvh);
		}

		c.getPlayer().getClient().getSession()
				.writeAndFlush(CField.updateInnerAbility(c.getPlayer().getInnerSkills().get(preset), preset));

		c.getPlayer().dropMessage(5, "어빌리티 재설정에 성공 하였습니다.");

		c.getPlayer().getStat().recalcLocalStats(c.getPlayer());
	}

	public static void absorbingRegen(LittleEndianAccessor slea, MapleClient c) {
		ForceAtom forceAtom;
		int skillId;
		if (c.getPlayer() == null) {
			return;
		}
		String sl = slea.toString();
		int wsize = slea.readInt();
		int uu = slea.readInt();
		int fsize = slea.readInt();
		int dam = 0;
		block20: for (int i = 0; i < fsize; ++i) {
			skillId = slea.readInt();
			int attackCount = slea.readInt();
			int x = slea.readInt();
			int y = slea.readInt();
			switch (skillId) {
			case 152120001: {
				ArrayList<Integer> moblist = new ArrayList<Integer>();
				for (MapleMonster m : c.getPlayer().getMap().getAllMonster()) {
					if (x - 200 >= m.getPosition().x || x + 200 <= m.getPosition().x) {
						continue;
					}
					moblist.add(m.getObjectId());
				}
				Collections.shuffle(moblist);
				for (int i3 = 0; i3 < 2; ++i3) {
					MapleAtom atom = new MapleAtom(false, c.getPlayer().getId(), 39, true, 152120002, x, y);
					forceAtom = new ForceAtom(0, Randomizer.rand(54, 67), Randomizer.rand(5, 6),
							Randomizer.rand(38, 93), 0, new Point(x, y));
					if (!moblist.isEmpty()) {
						atom.setDwTargets(moblist);
					}
					atom.addForceAtom(forceAtom);
					c.getPlayer().getMap().spawnMapleAtom(atom);
				}
				continue block20;
			}
			}
		}
		int doNextObjectid = 0;
		skillId = 0;
		block23: for (int i = 0; i < wsize; ++i) {
			SecondaryStatEffect effect;
			MapleCharacter chr;
			int attackCount = slea.readInt();
			slea.skip(1);
			int nextObjectId = slea.readInt();
			slea.skip(4);
			int prevObjectId = slea.readInt();
			int x = slea.readInt();
			int y = slea.readInt();
			slea.skip(1);
			skillId = slea.readInt();
			if ((skillId == 14141007 || skillId == 14141010) && slea.available() > 8L) {
				doNextObjectid = slea.readInt();
				x = slea.readInt();
				y = slea.readInt();
			} else if ((skillId == 14000029 || skillId == 14110035) && slea.available() > 8L) {
				doNextObjectid = slea.readInt();
				x = slea.readInt();
				y = slea.readInt();
			} else if (skillId == 31221014 || skillId == 31241001) { // 실드 체이싱 VI 구현
				slea.skip(4);
			} else if (skillId == 400011058 || skillId == 400011059) {
				skillId = slea.readInt();
				Point pos1 = slea.readIntPos();
				dam = slea.readInt();
				SecondaryStatEffect a = SkillFactory.getSkill(400011060)
						.getEffect(c.getPlayer().getSkillLevel(400011060));
				if (pos1 != null) {
					MapleMist mist = new MapleMist(a.calculateBoundingBox(pos1, c.getPlayer().isFacingLeft()),
							c.getPlayer(), a, 2000, (byte) (c.getPlayer().isFacingLeft() ? 1 : 0));
					mist.setPosition(pos1);
					mist.setDelay(0);
					mist.setEndTime(2000);
					mist.setDamup(dam);
					c.getPlayer().getMap().spawnMist(mist, false);
				}
			} else if (skillId == 400041023) {
				slea.skip(13);
			}
			switch (skillId) {
			case 0: {
				if (!GameConstants.isDemonSlayer(c.getPlayer().getJob())) {
					break;
				}

				if (c.getPlayer().getStat().getMp() >= c.getPlayer().getStat().getCurrentMaxMp(c.getPlayer())) {
					SecondaryStatEffect effNightmare = c.getPlayer().getBuffedEffect(SecondaryStat.Origin_Nightmare);
					if (effNightmare != null) {
						c.getPlayer().hexaMasterySkillStack += attackCount;

						if (c.getPlayer().hexaMasterySkillStack >= 500) {
							c.getPlayer().hexaMasterySkillStack = 500;
						}

						int resetTime = (int) (20000
								- (System.currentTimeMillis() % 1000000000L - effNightmare.getStarttime()));

						EnumMap<SecondaryStat, Pair<Integer, Integer>> statups = new EnumMap<SecondaryStat, Pair<Integer, Integer>>(
								SecondaryStat.class);
						statups.put(SecondaryStat.Origin_Nightmare,
								new Pair<Integer, Integer>(c.getPlayer().hexaMasterySkillStack, resetTime));
						SecondaryStatEffect eff = SkillFactory.getSkill(31141501)
								.getEffect(c.getPlayer().getSkillLevel(31141501));
						c.getPlayer().getClient().getSession()
								.writeAndFlush(CWvsContext.BuffPacket.giveBuff(statups, eff, c.getPlayer()));
						break;
					}
				}

				c.getPlayer().addMP(attackCount, true);
				break;
			}
			case 131003016: {
				c.getPlayer().getClient().send(CField.EffectPacket.showEffect(c.getPlayer(), 0, 131003016, 1, 0, 0,
						(byte) (c.getPlayer().isFacingLeft() ? 1 : 0), true, new Point(x, y), null, null));
				c.getPlayer().getMap().broadcastMessage(c.getPlayer(),
						CField.EffectPacket.showEffect(c.getPlayer(), 0, 131003016, 1, 0, 0,
								(byte) (c.getPlayer().isFacingLeft() ? 1 : 0), false, new Point(x, y), null, null),
						false);
				break;
			}
			case 400021069: {
				long duration = c.getPlayer().getBuffLimit(skillId);
				c.getPlayer().getBuffedEffect(skillId).applyTo(c.getPlayer(), false,
						(int) (duration += (long) attackCount));
				return;
			}
			case 400001036: // 에르다 샤워
				int cooldown = SkillFactory.getSkill(400001036).getEffect(c.getPlayer().getSkillLevel(400001036))
						.getCooldown(c.getPlayer());
				c.getPlayer().changeCooldown(skillId, -2000 * wsize);
				c.send(CField.skillCooldown(400001036, cooldown - 2000));
				c.getPlayer().setSkillCustomValue(skillId, 0);
				break;
			case 152001001:
			case 152110004:
			case 152120001:
			case 400011131: {
				c.getPlayer().getMap().broadcastMessage(c.getPlayer(), CField.RemoveAtom(c.getPlayer(), 1, attackCount),
						false);
				break;
			}
			case 152120008: {
				MapleMonster mob = c.getPlayer().getMap().getMonsterByOid(prevObjectId);
				if (mob != null) {
					int max;
					SecondaryStatEffect curseMark = SkillFactory.getSkill(152000010)
							.getEffect(c.getPlayer().getSkillLevel(152000010));
					int n = c.getPlayer().getSkillLevel(152100012) > 0 ? 5
							: (max = c.getPlayer().getSkillLevel(152110010) > 0 ? 3 : 1);
					if (mob.getBuff(152000010) == null && mob.getCustomValue0(152000010) > 0L) {
						mob.removeCustomInfo(152000010);
					}
					if (mob.getCustomValue0(152000010) < (long) n) {
						mob.addSkillCustomInfo(152000010, 1L);
					}
					mob.applyStatus(c, MonsterStatus.MS_CurseMark,
							new MonsterStatusEffect(152000010, curseMark.getDuration()),
							(int) mob.getCustomValue0(152000010), curseMark);
					break;
				}
				chr = c.getPlayer().getMap().getCharacter(prevObjectId);
				if (chr == null) {
					break;
				}
				chr.blessMarkSkill = 152120012;
				SkillFactory.getSkill(152000009).getEffect(c.getPlayer().getSkillLevel(152000009)).applyTo(chr);
				chr.getClient().getSession().writeAndFlush((Object) CField.EffectPacket.showEffect(chr, 0, 152120008, 4,
						0, 0, (byte) (chr.isFacingLeft() ? 1 : 0), true, chr.getTruePosition(), null, null));
				chr.getMap().broadcastMessage(chr, CField.EffectPacket.showEffect(chr, 0, 152120008, 4, 0, 0,
						(byte) (chr.isFacingLeft() ? 1 : 0), false, chr.getTruePosition(), null, null), false);
				break;
			}
			}
			if (skillId != 400041023) {
				if (c.getPlayer() == null || SkillFactory.getSkill(skillId) == null) {
					if (uu != 0) {
						FileoutputUtil.log("AbsorbingRegen.txt",
								"원래 스킬아이디 : " + uu + "가 " + skillId + "로 리턴됌 \r\n" + sl);
					}
					return;
				}
				MapleMonster tmob = c.getPlayer().getMap().getMonsterByOid(nextObjectId);
				if (tmob == null && skillId != 400041023) {
					return;
				}
			}
			if ((effect = SkillFactory.getSkill(skillId)
					.getEffect(c.getPlayer().getSkillLevel(GameConstants.getLinkedSkill(skillId)))) == null) {
				if (uu != 0) {
					FileoutputUtil.log("AbsorbingRegen.txt", "원래 스킬아이디 : " + uu + "가 " + skillId + "로 리턴됌 \r\n" + sl);
				}
				return;
			}
			switch (skillId) {
			case 25100010:
			case 25120115: {
				if (attackCount >= effect.getZ()) {
					continue block23;
				}
				MapleAtom atom = new MapleAtom(true, nextObjectId, 4, true, skillId, x, y);
				atom.setDwUserOwner(c.getPlayer().getId());
				forceAtom = new ForceAtom(skillId == 25100010 ? 4 : 5, Randomizer.rand(41, 42), Randomizer.rand(4, 4),
						Randomizer.rand(5, 300), 0);
				forceAtom.setnAttackCount(attackCount + 1);
				atom.setDwFirstTargetId(nextObjectId);
				atom.addForceAtom(forceAtom);
				c.getPlayer().getMap().spawnMapleAtom(atom);
				continue block23;
			}
			case 25141505: { // 오리진 호신강림 구현
				if (attackCount >= effect.getZ()) {
					continue block23;
				}
				MapleAtom atom = new MapleAtom(true, nextObjectId, 69, true, skillId, x, y);
				atom.setDwUserOwner(c.getPlayer().getId());
				forceAtom = new ForceAtom(4, Randomizer.rand(41, 42), Randomizer.rand(4, 4), Randomizer.rand(5, 300),
						0);
				forceAtom.setnAttackCount(attackCount + 1);
				atom.setDwFirstTargetId(nextObjectId);
				atom.addForceAtom(forceAtom);
				c.getPlayer().getMap().spawnMapleAtom(atom);
				continue block23;
			}
			case 14000029: {
				int attackLimit = 5;

				int[] skills = { 14100027, 14110029, 14120008 };
				for (int skill : skills) {
					if (c.getPlayer().getSkillLevel(skill) > 0) {
						attackLimit += SkillFactory.getSkill(skill).getEffect(c.getPlayer().getSkillLevel(skill))
								.getY();
					}
				}

				if (attackCount >= attackLimit || prevObjectId == doNextObjectid) {
					continue block23;
				}

				MapleAtom atom = new MapleAtom(true, prevObjectId, 16, true, skillId, x, y);
				atom.setDwUserOwner(c.getPlayer().getId());
				forceAtom = new ForceAtom(2, 1, 5, Randomizer.rand(45, 90), 10);
				forceAtom.setnAttackCount(attackCount + 1);
				atom.setDwFirstTargetId(doNextObjectid);
				atom.addForceAtom(forceAtom);
				c.getPlayer().getMap().spawnMapleAtom(atom);

				continue block23;
			}
			case 14110035: {
				int attackLimit = 5;

				int[] skills = { 14100027, 14110029, 14120008 };
				for (int skill : skills) {
					if (c.getPlayer().getSkillLevel(skill) > 0) {
						attackLimit += SkillFactory.getSkill(skill).getEffect(c.getPlayer().getSkillLevel(skill))
								.getY();
					}
				}

				if (attackCount >= attackLimit || prevObjectId == doNextObjectid) {
					continue block23;
				}

				MapleAtom atom = new MapleAtom(true, prevObjectId, 16, true, skillId, x, y);
				atom.setDwUserOwner(c.getPlayer().getId());
				forceAtom = new ForceAtom(2, 1, 5, Randomizer.rand(45, 90), 10);
				forceAtom.setnAttackCount(attackCount + 1);
				atom.setDwFirstTargetId(doNextObjectid);
				atom.addForceAtom(forceAtom);
				c.getPlayer().getMap().spawnMapleAtom(atom);

				continue block23;
			}
			case 14141007: {
				int attackLimit = 5;

				int[] skills = { 14100027, 14110029, 14120008 };
				for (int skill : skills) {
					if (c.getPlayer().getSkillLevel(skill) > 0) {
						attackLimit += SkillFactory.getSkill(skill).getEffect(c.getPlayer().getSkillLevel(skill))
								.getY();
					}
				}

				if (attackCount >= attackLimit || prevObjectId == doNextObjectid) {
					continue block23;
				}

				MapleAtom atom = new MapleAtom(true, prevObjectId, 16, true, skillId, x, y);
				atom.setDwUserOwner(c.getPlayer().getId());
				forceAtom = new ForceAtom(2, 1, 5, Randomizer.rand(45, 90), 10);
				forceAtom.setnAttackCount(attackCount + 1);
				atom.setDwFirstTargetId(doNextObjectid);
				atom.addForceAtom(forceAtom);
				c.getPlayer().getMap().spawnMapleAtom(atom);

				continue block23;
			}
			case 14141010: {
				int attackLimit = 5;

				int[] skills = { 14100027, 14110029, 14120008 };
				for (int skill : skills) {
					if (c.getPlayer().getSkillLevel(skill) > 0) {
						attackLimit += SkillFactory.getSkill(skill).getEffect(c.getPlayer().getSkillLevel(skill))
								.getY();
					}
				}

				if (attackCount >= attackLimit || prevObjectId == doNextObjectid) {
					continue block23;
				}

				System.out.println(prevObjectId + "/" + doNextObjectid);

				MapleAtom atom = new MapleAtom(true, prevObjectId, 16, true, skillId, x, y);
				atom.setDwUserOwner(c.getPlayer().getId());
				forceAtom = new ForceAtom(2, 1, 5, Randomizer.rand(45, 90), 10);
				forceAtom.setnAttackCount(attackCount + 1);
				atom.setDwFirstTargetId(doNextObjectid);
				atom.addForceAtom(forceAtom);
				c.getPlayer().getMap().spawnMapleAtom(atom);

				continue block23;
			}
			case 31221014: {
				int maxcount = effect.getZ()
						+ SkillFactory.getSkill(31220050).getEffect(c.getPlayer().getSkillLevel(31220050)).getZ();
				if (attackCount >= maxcount) {
					continue block23;
				}
				MapleAtom atom = new MapleAtom(true, nextObjectId, 4, true, 31221014, x, y);
				atom.setDwUserOwner(c.getPlayer().getId());
				forceAtom = new ForceAtom(3, Randomizer.rand(40, 45), Randomizer.rand(3, 4), Randomizer.rand(10, 340),
						0);
				forceAtom.setnAttackCount(attackCount + 1);
				atom.setDwFirstTargetId(nextObjectId);
				atom.addForceAtom(forceAtom);
				c.getPlayer().getMap().spawnMapleAtom(atom);
				continue block23;
			}
			case 31241001: { // 실드 체이싱 VI 구현
				int maxcount = effect.getZ()
						+ SkillFactory.getSkill(31220050).getEffect(c.getPlayer().getSkillLevel(31220050)).getZ();
				if (attackCount >= maxcount) {
					continue block23;
				}
				MapleAtom atom = new MapleAtom(true, nextObjectId, 85, true, 31241001, x, y);
				atom.setDwUserOwner(c.getPlayer().getId());
				forceAtom = new ForceAtom(3, Randomizer.rand(40, 45), Randomizer.rand(3, 4), Randomizer.rand(10, 340),
						0);
				forceAtom.setnAttackCount(attackCount + 1);
				atom.setDwFirstTargetId(nextObjectId);
				atom.addForceAtom(forceAtom);
				c.getPlayer().getMap().spawnMapleAtom(atom);
				continue block23;
			}
			case 65111007: {
				if (attackCount >= 8) {
					continue block23;
				}
				int prop = effect.getProp();
				if (c.getPlayer().getSkillLevel(65120044) > 0) {
					prop += SkillFactory.getSkill(65120044).getEffect(1).getProp();
				}
				if (!Randomizer.isSuccess(prop)) {
					continue block23;
				}
				MapleAtom atom = new MapleAtom(true, nextObjectId, 4, true, 65111007, x, y);
				atom.setDwUserOwner(c.getPlayer().getId());
				forceAtom = new ForceAtom(1, Randomizer.rand(40, 45), Randomizer.rand(3, 4), Randomizer.rand(10, 340),
						0);
				forceAtom.setnAttackCount(attackCount + 1);
				atom.setDwFirstTargetId(nextObjectId);
				atom.addForceAtom(forceAtom);
				c.getPlayer().getMap().spawnMapleAtom(atom);
				continue block23;
			}
			case 65141003: { // 소울 시커 VI 구현
				if (attackCount >= 8) {
					continue block23;
				}
				int prop = effect.getProp();
				if (c.getPlayer().getSkillLevel(65120044) > 0) {
					prop += SkillFactory.getSkill(65120044).getEffect(1).getProp();
				}
				if (!Randomizer.isSuccess(prop)) {
					continue block23;
				}
				MapleAtom atom = new MapleAtom(true, nextObjectId, 4, true, 65141003, x, y);
				atom.setDwUserOwner(c.getPlayer().getId());
				forceAtom = new ForceAtom(1, Randomizer.rand(40, 45), Randomizer.rand(3, 4), Randomizer.rand(10, 340),
						0);
				forceAtom.setnAttackCount(attackCount + 1);
				atom.setDwFirstTargetId(nextObjectId);
				atom.addForceAtom(forceAtom);
				c.getPlayer().getMap().spawnMapleAtom(atom);
				continue block23;
			}
			case 65141502: { // 오리진 그랜드 피날레 구현
				if (attackCount >= 8) {
					continue block23;
				}
				int prop = effect.getProp();
				if (c.getPlayer().getSkillLevel(65120044) > 0) {
					prop += SkillFactory.getSkill(65120044).getEffect(1).getProp();
				}
				if (!Randomizer.isSuccess(prop)) {
					continue block23;
				}
				MapleAtom atom = new MapleAtom(true, nextObjectId, 94, true, 65141502, x, y);
				atom.setDwUserOwner(c.getPlayer().getId());
				forceAtom = new ForceAtom(1, Randomizer.rand(40, 45), Randomizer.rand(3, 4), Randomizer.rand(10, 340),
						0);
				forceAtom.setnAttackCount(attackCount + 1);
				atom.setDwFirstTargetId(nextObjectId);
				atom.addForceAtom(forceAtom);
				c.getPlayer().getMap().spawnMapleAtom(atom);
				continue block23;
			}
			case 65120011: {
				if (attackCount >= 8) {
					continue block23;
				}
				int prop = 85;
				if (c.getPlayer().getSkillLevel(65120044) > 0) {
					prop += SkillFactory.getSkill(65120044).getEffect(1).getProp();
				}
				if (!Randomizer.isSuccess(prop)) {
					continue block23;
				}
				MapleAtom atom = new MapleAtom(true, nextObjectId, 26, true, 65120011, x, y);
				atom.setDwUserOwner(c.getPlayer().getId());
				forceAtom = new ForceAtom(1, Randomizer.rand(40, 45), Randomizer.rand(3, 4), Randomizer.rand(10, 340),
						0);
				forceAtom.setnAttackCount(attackCount + 1);
				atom.setDwFirstTargetId(nextObjectId);
				atom.addForceAtom(forceAtom);
				c.getPlayer().getMap().spawnMapleAtom(atom);
				continue block23;
			}
			case 65141004: { // 소울 시커 엑스퍼트 VI 구현
				if (attackCount >= 8) {
					continue block23;
				}
				int prop = 85;
				if (c.getPlayer().getSkillLevel(65120044) > 0) {
					prop += SkillFactory.getSkill(65120044).getEffect(1).getProp();
				}
				if (!Randomizer.isSuccess(prop)) {
					continue block23;
				}
				boolean isHexaBuff = c.getPlayer().getBuffedValue(65141501);
				MapleAtom atom = new MapleAtom(true, nextObjectId, isHexaBuff ? 94 : 26, true, 65141004, x, y);
				atom.setDwUserOwner(c.getPlayer().getId());
				forceAtom = new ForceAtom(1, Randomizer.rand(40, 45), Randomizer.rand(3, 4), Randomizer.rand(10, 340),
						0);
				forceAtom.setnAttackCount(attackCount + 1);
				atom.setDwFirstTargetId(nextObjectId);
				atom.addForceAtom(forceAtom);
				c.getPlayer().getMap().spawnMapleAtom(atom);
				continue block23;
			}
			case 400041023: {
				MapleAtom atom;
				effect = SkillFactory.getSkill(400041022)
						.getEffect(c.getPlayer().getSkillLevel(GameConstants.getLinkedSkill(skillId)));
				if (c.getPlayer().useBlackJack) {
					c.getPlayer().addSkillCustomInfo(400041022, 1L);
					if (c.getPlayer().getSkillCustomValue0(400041022) < 3L) {
						continue block23;
					}
					c.getPlayer().getMap()
							.broadcastMessage(CField.blackJack(c.getPlayer(), 400041080, new Point(x, y)));
					continue block23;
				}
				if (attackCount < effect.getZ() && nextObjectId != 0) {
					c.getPlayer().addSkillCustomInfo(400041080, -1L);
					atom = new MapleAtom(true, nextObjectId, 33, true, 400041023, x, y);
					atom.setDwUserOwner(c.getPlayer().getId());
					forceAtom = new ForceAtom(33, Randomizer.rand(60, 70), Randomizer.rand(10, 10),
							Randomizer.rand(5, 360), 0);
					forceAtom.setnAttackCount(attackCount + 1);
					atom.setDwFirstTargetId(nextObjectId);
					atom.addForceAtom(forceAtom);
					c.getPlayer().getMap().spawnMapleAtom(atom);
					continue block23;
				}
				c.getPlayer().getMap().broadcastMessage(CField.blackJack(c.getPlayer(), 400041024, new Point(x, y)));
				continue block23;
			}
			case 400021045: {
				chr = c.getPlayer();
				SecondaryStatEffect effect2 = SkillFactory.getSkill(400021042).getEffect(chr.getSkillLevel(400021042));
				if (attackCount >= effect2.getU2()) {
					continue block23;
				}
				MapleAtom mapleAtom = new MapleAtom(true, nextObjectId, 4, true, 400021045,
						x + Randomizer.rand(-100, 100), y + Randomizer.rand(-100, 100));
				mapleAtom.setDwUserOwner(c.getPlayer().getId());
				forceAtom = new ForceAtom(6, Randomizer.rand(39, 44), Randomizer.rand(3, 4), Randomizer.rand(13, 332),
						0);
				forceAtom.setnAttackCount(attackCount + 1);
				mapleAtom.setDwFirstTargetId(nextObjectId);
				mapleAtom.addForceAtom(forceAtom);
				c.getPlayer().getMap().spawnMapleAtom(mapleAtom);
				continue block23;
			}
			}
		}
	}

	public static void ZeroScrollUI(int scroll, MapleClient c) {
		c.getSession().writeAndFlush(CField.ZeroScroll(scroll));
	}

	public static void ZeroScrollLucky(LittleEndianAccessor slea, MapleClient c) {
		int s_type = slea.readInt();
		int pos = slea.readShort();
		c.getPlayer().setZeroCubePosition(pos);
		Equip equip1 = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) -10);
		Equip equip2 = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11);
		Equip nEquip = equip1;
		Equip nEquip2 = equip2;
		if (equip1.getItemId() > 1560000 && equip1.getItemId() < 1570000 && equip2.getItemId() > 1572000
				&& equip2.getItemId() < 1573000) {
			InventoryHandler.UseUpgradeScroll(null, (short) (byte) pos, (short) (byte) nEquip.getPosition(), (byte) 0,
					c, c.getPlayer());
			InventoryHandler.UseUpgradeScroll(null, (short) (byte) pos, (short) (byte) nEquip2.getPosition(), (byte) 0,
					c, c.getPlayer());
			c.getPlayer().setZeroCubePosition(-1);
		}
	}

	public static void ZeroScroll(LittleEndianAccessor slea, MapleClient c) {
		int s_type = slea.readInt();
		int pos = slea.readInt();
		slea.skip(8);
		int s_pos = slea.readInt();
		c.getSession().writeAndFlush(CField.ZeroScrollSend(s_pos));
	}

	public static void ZeroScrollStart(RecvPacketOpcode header, LittleEndianAccessor slea, MapleClient c) {
		c.getSession().writeAndFlush(CField.ZeroScrollStart());
	}

	public static void ZeroWeaponInfo(LittleEndianAccessor slea, MapleClient c) {
		MapleCharacter player = c.getPlayer();
		Item alpha = c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) -10);
		int action = 1, level = 0, type = 0, itemid = 0, quantity = 0;
		switch (alpha.getItemId()) {
		case 1562000:
			type = 1;
			level = 100;
			break;
		case 1562001:
			type = 2;
			level = 110;
			break;
		case 1562002:
			type = 2;
			level = 120;
			break;
		case 1562003:
			type = 2;
			level = 130;
			break;
		case 1562004:
			type = 4;
			level = 140;
			break;
		case 1562005:
			type = 5;
			level = 150;
			break;
		case 1562006:
			type = 6;
			level = 160;
			break;
		case 1562007:
			type = 7;
			level = 160;
			itemid = 4310216;
			quantity = 1;
			break;
		case 1562008:
			type = 8;
			level = 200;
			itemid = 4310217;
			quantity = 1;
			break;
		case 1562009:
			type = 9;
			level = 200;
			itemid = 4310260;
			quantity = 1;
			break;
		case 1562010:
			type = 9;
			level = 200;
			itemid = 4310260;
			quantity = 1;
			break;
		}
		if (player.getLevel() < level) {
			action = 0;
		}
		c.getSession().writeAndFlush(CField.WeaponInfo(type, level, action, alpha.getItemId(), itemid, quantity));
	}

	public static void ZeroWeaponLevelUp(LittleEndianAccessor slea, MapleClient c) {
		MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
		slea.skip(7);
		Item alpha = c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11);
		Item beta = c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) -10);
		byte betatype = 11;
		byte alphatype = 12;
		Equip nalphatype = (Equip) alpha;
		Equip nbetatype = (Equip) beta;
		if (nbetatype.getItemId() == 1562007) {
			if (!c.getPlayer().haveItem(4310216, 1)) {
				return;
			}
		} else if (nbetatype.getItemId() == 1562008) {
			if (!c.getPlayer().haveItem(4310217, 1)) {
				return;
			}
		} else if (nbetatype.getItemId() == 1562009 && !c.getPlayer().haveItem(4310260, 1)) {
			return;
		}
		if (ii.getReqLevel(nbetatype.getItemId() + 1) > c.getPlayer().getLevel()) {
			c.getPlayer().dropMessage(1, "等級不足，無法進行武器成長.");
			c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
			return;
		}
		long fire1 = nalphatype.getFire();
		long fire2 = nbetatype.getFire();
		nalphatype.resetRebirth(ii.getReqLevel(nalphatype.getItemId()));
		nbetatype.resetRebirth(ii.getReqLevel(nbetatype.getItemId()));
		nbetatype.setItemId(nbetatype.getItemId() + 1);
		nalphatype.setItemId(nalphatype.getItemId() + 1);
		if (nbetatype.getItemId() == 1562001) {
			nalphatype.setWatk((short) 100);
			nbetatype.setWatk((short) 102);
			nbetatype.setWdef((short) 80);
			nbetatype.setMdef((short) 35);
			nalphatype.addUpgradeSlots((byte) 7);
			nbetatype.addUpgradeSlots((byte) 7);
		} else if (nbetatype.getItemId() == 1562002) {
			nalphatype.addWatk((short) 3);
			nbetatype.addWatk((short) 3);
			nbetatype.addWdef((short) 10);
			nbetatype.addMdef((short) 5);
		} else if (nbetatype.getItemId() == 1562003) {
			nalphatype.addWatk((short) 2);
			nbetatype.addWatk((short) 2);
			nbetatype.addWdef((short) 10);
			nbetatype.addMdef((short) 5);
		} else if (nbetatype.getItemId() == 1562004) {
			nalphatype.addWatk((short) 7);
			nbetatype.addWatk((short) 7);
			nbetatype.addWdef((short) 10);
			nbetatype.addMdef((short) 5);
		} else if (nbetatype.getItemId() == 1562005) {
			nalphatype.addStr((short) 8);
			nalphatype.addDex((short) 4);
			nalphatype.addWatk((short) 5);
			nalphatype.addAcc((short) 50);
			nalphatype.addUpgradeSlots((byte) 1);
			nbetatype.addStr((short) 8);
			nbetatype.addDex((short) 4);
			nbetatype.addWatk((short) 7);
			nbetatype.addWdef((short) 10);
			nbetatype.addMdef((short) 5);
			nbetatype.addAcc((short) 50);
			nbetatype.addUpgradeSlots((byte) 1);
		} else if (nbetatype.getItemId() == 1562006) {
			nalphatype.addStr((short) 27);
			nalphatype.addDex((short) 16);
			nalphatype.addWatk((short) 18);
			nalphatype.addAcc((short) 50);
			nbetatype.addStr((short) 27);
			nbetatype.addDex((short) 16);
			nbetatype.addWatk((short) 18);
			nbetatype.addWdef((short) 10);
			nbetatype.addMdef((short) 5);
			nbetatype.addAcc((short) 50);
		} else if (nbetatype.getItemId() == 1562007) {
			nalphatype.addStr((short) 5);
			nalphatype.addDex((short) 20);
			nalphatype.addWatk((short) 34);
			nalphatype.addAcc((short) 20);
			nalphatype.addBossDamage((byte) 30);
			nalphatype.addIgnoreWdef((short) 10);
			nbetatype.addStr((short) 5);
			nbetatype.addDex((short) 20);
			nbetatype.addWatk((short) 34);
			nbetatype.addWdef((short) 20);
			nbetatype.addMdef((short) 10);
			nbetatype.addAcc((short) 20);
			nbetatype.addBossDamage((byte) 30);
			nbetatype.addIgnoreWdef((short) 10);
		} else if (nbetatype.getItemId() == 1562008) {
			nalphatype.addStr((short) 20);
			nalphatype.addDex((short) 20);
			nalphatype.addWatk((short) 34);
			nalphatype.addAcc((short) 20);
			nbetatype.addStr((short) 20);
			nbetatype.addDex((short) 20);
			nbetatype.addWatk((short) 34);
			nbetatype.addWdef((short) 10);
			nbetatype.addMdef((short) 10);
			nbetatype.addAcc((short) 20);
			MapleInventoryManipulator.removeById(c, MapleInventoryType.ETC, 4310216, 1, false, false);
		} else if (nbetatype.getItemId() == 1562009) {
			nalphatype.addStr((short) 40);
			nalphatype.addDex((short) 40);
			nalphatype.addWatk((short) 90);
			nalphatype.addAcc((short) 20);
			nalphatype.addIgnoreWdef((short) 10);
			nbetatype.addStr((short) 40);
			nbetatype.addDex((short) 40);
			nbetatype.addWatk((short) 90);
			nbetatype.addWdef((short) 40);
			nbetatype.addMdef((short) 40);
			nbetatype.addAcc((short) 20);
			nbetatype.addIgnoreWdef((short) 10);
			MapleInventoryManipulator.removeById(c, MapleInventoryType.ETC, 4310217, 1, false, false);
		} else if (nbetatype.getItemId() == 1562010) {
			nalphatype.addStr((short) 50);
			nalphatype.addDex((short) 50);
			nalphatype.addWatk((short) 44);
			nalphatype.addAcc((short) 20);
			nbetatype.addStr((short) 50);
			nbetatype.addDex((short) 50);
			nbetatype.addWatk((short) 45);
			nbetatype.addWdef((short) 50);
			nbetatype.addMdef((short) 50);
			nbetatype.addAcc((short) 20);
			MapleInventoryManipulator.removeById(c, MapleInventoryType.ETC, 4310260, 1, false, false);
		}
		if (fire1 > 0L && fire2 > 0L) {
			nalphatype.refreshFire(nalphatype, fire1, false);
			nbetatype.refreshFire(nbetatype, fire2, false);
		}
		c.getSession().writeAndFlush(CField.WeaponLevelUp());
		c.getSession().writeAndFlush(
				CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIP, nalphatype));
		c.getSession().writeAndFlush(
				CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIP, nbetatype));
	}

	public static void ZeroTag(LittleEndianAccessor slea, MapleClient c) {
		MapleCharacter player = c.getPlayer();
		slea.skip(4);
		player.setSkillCustomInfo(101000201, slea.readInt(), 0L);
		long nowhp = player.getStat().getHp();
		player.getMap().broadcastMessage(CField.MultiTag(player));
		c.getSession().writeAndFlush(CField.ZeroTag(player, player.getSecondGender(), (int) nowhp,
				(int) player.getStat().getCurrentMaxHp()));
		if (player.getGender() == 0 && player.getSecondGender() == 1) {
			player.timePieces.clear();

			player.setGender((byte) 1);
			player.setSecondGender((byte) 0);
		} else if (player.getGender() == 1 && player.getSecondGender() == 0) {
			player.setGender((byte) 0);
			player.setSecondGender((byte) 1);
			player.armorSplit = 0;
		}

		// 타임 피스 구현 [S]
		ArrayList<MapleMagicWreck> mws = new ArrayList<>();
		for (MapleMagicWreck mw : player.getMap().getWrecks()) {
			if (mw != null && mw.getChr().getId() == player.getId()) {
				if (mw.getSourceid() == 101141012 || mw.getSourceid() == 101141013) {
					mws.add(mw);
				}
			}
		}

		for (MapleMagicWreck mw : mws) {
			SecondAtom2 sa = SkillFactory.getSkill(mw.getSourceid()).getSecondAtoms().get(0);
			sa.setDataIndex(mw.getSourceid() == 101141012 ? 52 : 54);

			player.createSecondAtom(sa, mw.getPosition(), false);

			player.getMap().RemoveMagicWreck(mw);
		}
		// 타임 피스 구현 [E]

		player.getStat().recalcLocalStats(player);
		if (player.getGender() == 0) {
			if (player.getBuffedValue(SecondaryStat.ImmuneBarrier) != null) {
				while (player.getBuffedValue(101120109)) {
					player.cancelEffect(player.getBuffedEffect(101120109));
				}
			}
			player.setSkillCustomInfo(101112, (int) nowhp, 0L);
			player.setSkillCustomInfo(101114, (int) player.getStat().getCurrentMaxHp(), 0L);
			if (player.getSkillCustomValue0(101113) > 0L) {
				player.getStat().setHp(player.getSkillCustomValue0(101113), player);
			}
		} else {
			player.setSkillCustomInfo(101113, (int) nowhp, 0L);
			player.setSkillCustomInfo(101115, (int) player.getStat().getCurrentMaxHp(), 0L);
			if (player.getSkillCustomValue0(101112) > 0L) {
				player.getStat().setHp(player.getSkillCustomValue0(101112), player);
			}
		}
		player.getMap().broadcastMessage(player, CField.ZeroTagUpdateCharLook(player), player.getPosition());
	}

	public static void ZeroTagRemove(MapleClient c) {
		c.getPlayer().getMap().broadcastMessage(c.getPlayer(), CField.MultiTagRemove(c.getPlayer().getId()), false);
	}

	public static void subActiveSkills(LittleEndianAccessor slea, MapleClient c) {
		int mobId;
		SecondaryStatEffect effect;
		MapleMonster mob;
		int skillid = slea.readInt();
		switch (skillid) {
		case 1201012:
			slea.skip(4);
			mobId = slea.readInt();
			effect = SkillFactory.getSkill(skillid).getEffect(c.getPlayer().getTotalSkillLevel(skillid));
			mob = c.getPlayer().getMap().getMonsterByOid(mobId);
			if (mob != null && effect.makeChanceResult()) {
				mob.applyStatus(c, MonsterStatus.MS_Freeze, new MonsterStatusEffect(skillid, effect.getDuration()), 1,
						effect);
			}
			return;
		}
		if (c.getPlayer().isGM()) {
			c.getPlayer().dropMessage(5, "SUB ACTIVE SKILL : " + skillid);
		}
	}

	public static void ZeroClothes(LittleEndianAccessor slea, MapleClient c) {
		MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
		MapleCharacter chr = c.getPlayer();
		int kind = slea.readInt();
		byte check = slea.readByte();
		int dst = -100, dst2 = -1500, secondkind = 0;
		switch (kind) {
		case 0:
			secondkind = 0;
			break;
		case 3:
			secondkind = 0;
			break;
		case 4:
			secondkind = 3;
			break;
		case 6:
			secondkind = 8;
			break;
		case 7:
			secondkind = 9;
			break;
		case 8:
			secondkind = 6;
			break;
		case 9:
			secondkind = 4;
			break;
		case 11:
			secondkind = 7;
			break;
		case 12:
			secondkind = 10;
			break;
		default:
			secondkind = kind;
			break;
		}
		dst -= kind;
		dst2 -= secondkind;
		Equip source = (Equip) ii.getEquipById((int) chr.getSkillCustomValue0(10112));
		Equip target2 = (Equip) chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) dst2);
		if (check == 1) {
			if (target2 == null) {
				int id = chr.getInventory(MapleInventoryType.CODY).getNextFreeSlot();
				chr.gainItem(source.getItemId(), 1);
				source = (Equip) chr.getInventory(MapleInventoryType.CODY).getItem((short) id);
				source.setFinalStrike(true);
				source.setPosition((short) dst2);
				chr.getInventory(MapleInventoryType.CODY).removeSlot((short) id);
				chr.getInventory(MapleInventoryType.EQUIPPED).addFromDB(source);
				c.send(CWvsContext.InventoryPacket.UpedateInventoryItem(dst2, id));
				chr.equipChanged();
			} else if (target2 != null) {
				MapleInventoryManipulator.unequip(c, (short) dst2,
						chr.getInventory(MapleInventoryType.CODY).getNextFreeSlot(), MapleInventoryType.CODY);
				int id = chr.getInventory(MapleInventoryType.CODY).getNextFreeSlot();
				chr.gainItem(source.getItemId(), 1);
				source = (Equip) chr.getInventory(MapleInventoryType.CODY).getItem((short) id);
				source.setFinalStrike(true);
				chr.getInventory(MapleInventoryType.CODY).removeSlot((short) id);
				source.setPosition((short) dst2);
				chr.getInventory(MapleInventoryType.EQUIPPED).addFromDB(source);
				c.send(CWvsContext.InventoryPacket.UpedateInventoryItem(dst2, id));
				chr.equipChanged();
			}
		}
	}

	public static void FieldAttackObjAttack(LittleEndianAccessor slea, MapleCharacter chr) {
		short type = slea.readShort();
		int cid = slea.readInt();
		int key = slea.readInt();
		if (chr == null) {
			return;
		}
		if (chr.getId() != cid) {
			return;
		}
		if (type == 0) {
			byte type2 = slea.readByte();
			Point pos = slea.readPos();
			Point oldPos = null;
			if (type2 == 5) {
				oldPos = slea.readPos();
			}
			short unk1 = slea.readShort();
			int sourceid = slea.readInt();
			int level = slea.readInt();
			int duration = slea.readInt();
			short unk2 = slea.readShort();
			chr.getMap().broadcastMessage(CField.B2BodyResult(chr, cid, type, (short) type2, key, pos, oldPos, unk1,
					sourceid, level, duration, unk2, chr.isFacingLeft(), 0, 0, null));
			chr.getMap().broadcastMessage(CField.spawnSubSummon(type, key));
		} else if (type == 3) {
			int sourceid = slea.readInt();
			int level = slea.readInt();
			int unk3 = slea.readInt();
			int unk4 = slea.readInt();
			chr.getMap().broadcastMessage(CField.B2BodyResult(chr, cid, type, (short) 0, key, null, null, (short) 0,
					sourceid, level, 0, (short) 0, chr.isFacingLeft(), unk3, unk4, null));
			chr.getMap().broadcastMessage(CField.spawnSubSummon(type, key));
		} else if (type == 4) {
			Point pos = slea.readPos();
			slea.skip(4);
			if (slea.available() > 0L) {
				int sourceid = slea.readInt();

				if (sourceid == 95001000 && chr.getSkillLevel(3141004) > 0) { // 애로우 플래터 VI 구현
					sourceid = 95001016;
				}

				boolean facingleft = (slea.readByte() == 1);
				slea.skip(18); // 362 +8
				short type2 = slea.readShort();
				short unk1 = slea.readShort();
				short unk2 = slea.readShort();
				byte unk3 = slea.readByte();
				String unk = "";
				if (unk3 > 0) {
					unk = slea.readMapleAsciiString();
				}
				int unk4 = slea.readInt();
				Point oldPos = new Point(slea.readInt(), slea.readInt());
				MapleFieldAttackObj fao = new MapleFieldAttackObj(chr, sourceid, facingleft, pos, type2 * 1000);
				if (chr.getFao() == null) {
					chr.setFao(fao);
				}
				chr.getMap().broadcastMessage(CField.spawnSubSummon(type, key));
				chr.getMap().broadcastMessage(CField.B2BodyResult(chr, cid, type, type2, key, pos, oldPos, unk1,
						sourceid, 0, 0, unk2, facingleft, unk3, unk4, unk));
				if (sourceid == 400031033) {
					Vmatrixstackbuff(chr.getClient(), true, null);
				}
			} else {
				return;
			}
		}
	}

	public static void FieldAttackObjAction(LittleEndianAccessor slea, MapleCharacter chr) {
		boolean isLeft = (slea.readByte() > 0);
		int x = slea.readInt();
		int y = slea.readInt();
		boolean disable = chr.isDominant();
		for (MapleMapObject obj : chr.getMap().getMapObjectsInRange(chr.getTruePosition(), Double.POSITIVE_INFINITY,
				Arrays.asList(new MapleMapObjectType[] { MapleMapObjectType.FIELD }))) {
			MapleFieldAttackObj fao = (MapleFieldAttackObj) obj;
			if (fao.getChr().getId() == chr.getId()) {
				List<MapleFieldAttackObj> removes = new ArrayList<>();
				removes.add(fao);
				chr.getMap().broadcastMessage(CField.AttackObjPacket.ObjRemovePacketByList(removes));
				chr.getMap().removeMapObject(fao);
				break;
			}
		}
		if (chr.getFao() != null) {
			chr.getFao().setFacingleft(isLeft);
			chr.getFao().setPosition(new Point(x, y));
			chr.getMap().spawnFieldAttackObj(chr.getFao());
			chr.setDominant(!disable);
		}
	}

	public static void OrbitalFlame(LittleEndianAccessor slea, MapleClient c) {
		MapleCharacter chr = c.getPlayer();
		int tempskill = slea.readInt();
		byte level = slea.readByte();
		int direction = slea.readShort();

		chr.getClient().getSession().writeAndFlush(CField.EffectPacket.showEffect(chr, 0, 12120006, 1, 0, 0,
				(byte) (chr.isFacingLeft() ? 1 : 0), true, chr.getTruePosition(), null, null));
		chr.getMap().broadcastMessage(chr, CField.EffectPacket.showEffect(chr, 0, 12120006, 1, 0, 0,
				(byte) (chr.isFacingLeft() ? 1 : 0), false, chr.getTruePosition(), null, null), false);

	}

	public static void VoydPressure(LittleEndianAccessor slea, MapleCharacter chr) {
		List<Byte> arrays = new ArrayList<>();
		byte size = slea.readByte();
		for (int i = 0; i < size; i++) {
			arrays.add(Byte.valueOf(slea.readByte()));
		}
		chr.getMap().broadcastMessage(chr, CField.showVoydPressure(chr.getId(), arrays), false);
	}

	public static void absorbingSword(LittleEndianAccessor slea, MapleCharacter chr) {// mush will
		int skill = slea.readInt();
		int mobSize = slea.readInt();
		MapleAtom atom = new MapleAtom(false, chr.getId(),
				skill == 61141006 ? 101 : skill == 400011058 || skill == 400011059 ? 32 : 2, true, skill,
				chr.getTruePosition().x, chr.getTruePosition().y);
		ArrayList<Integer> monsters = new ArrayList<Integer>();
		for (int i = 0; i < mobSize; ++i) {
			monsters.add(slea.readInt());
			atom.addForceAtom(new ForceAtom(chr.getBuffedValue(61141006) ? 1 : chr.getBuffedValue(61121217) ? 4 : 2,
					chr.getBuffedValue(61141006) ? 17 : 18, Randomizer.rand(20, 40), 0,
					(short) Randomizer.rand(1000, 1500)));
		}
		while (atom.getForceAtoms().size() < (chr.getBuffedValue(61141006) ? 1
				: chr.getBuffedValue(61120007) || chr.getBuffedValue(61121217) ? 5 : 3)) {
			atom.addForceAtom(new ForceAtom(chr.getBuffedValue(61141006) ? 1 : chr.getBuffedValue(61121217) ? 4 : 2,
					chr.getBuffedValue(61141006) ? 17 : 18, Randomizer.rand(20, 40), 0,
					(short) Randomizer.rand(1000, 1500)));
		}
		if (skill != 0) {
			chr.cancelEffectFromBuffStat(SecondaryStat.StopForceAtominfo);

			atom.setDwTargets(monsters);
			chr.getMap().spawnMapleAtom(atom);
		}
		if (skill == 400011058 || skill == 400011059) {
			chr.getClient().getSession().writeAndFlush((Object) CField.skillCooldown(skill,
					SkillFactory.getSkill(400011058).getEffect(chr.getSkillLevel(400011058)).getCooldown(chr)));
			chr.addCooldown(skill, System.currentTimeMillis(),
					SkillFactory.getSkill(400011058).getEffect(chr.getSkillLevel(400011058)).getCooldown(chr));
		}
	}

	public static void DressUpRequest(MapleCharacter chr, LittleEndianAccessor slea) {
		int code = slea.readInt();

		switch (code) {
		case 5010093:
			chr.setDressup(false);
			chr.getMap().broadcastMessage(CField.updateCharLook(chr, chr.getDressup()));
			chr.getMap().broadcastMessage(CField.updateDress(code, chr));
			break;
		case 5010094:
			chr.setDressup(true);
			chr.getMap().broadcastMessage(CField.updateCharLook(chr, chr.getDressup()));
			chr.getMap().broadcastMessage(CField.updateDress(code, chr));
			break;
		}
	}

	public static final void DressUpTime(LittleEndianAccessor rh, MapleClient c) {
		byte type = rh.readByte();
		if (type == 1) {
			if (GameConstants.isAngelicBuster(c.getPlayer().getJob())) {
				c.getPlayer().getMap().broadcastMessage(c.getPlayer(),
						CField.updateCharLook(c.getPlayer(), c.getPlayer().getDressup()), false);
			}
		} else {
			c.getPlayer().getMap().broadcastMessage(c.getPlayer(),
					CField.updateCharLook(c.getPlayer(), c.getPlayer().getDressup()), false);
		}
	}

	public static final void test(LittleEndianAccessor slea, MapleClient c) {
		slea.skip(6);
		c.getSession().writeAndFlush(CWvsContext.Test());
		c.getSession().writeAndFlush(CWvsContext.Test1());
	}

	public static final void PsychicGrabPreparation(LittleEndianAccessor slea, MapleClient c, boolean shot) {
		Map<Integer, List<PsychicGrabEntry>> grab = new LinkedHashMap<>();
		int skillid = slea.readInt();
		short unk_ = slea.readShort();
		int id = slea.readInt();
		int unk1 = slea.readInt();
		if (!shot) {
			byte firstsize = 1;
			int i = 1;
			while (firstsize > 0) {
				firstsize = slea.readByte();
				if (firstsize > 0) {
					int a = slea.readInt();
					int b = slea.readInt();
					int mobid = slea.readInt();
					int unk2 = slea.readInt();
					short secondsize = slea.readShort();
					slea.skip(2);
					byte unk = slea.readByte();
					Rectangle rect = new Rectangle(slea.readInt(), slea.readInt(), slea.readInt(), slea.readInt());
					grab.put(Integer.valueOf(i), new ArrayList<>());
					MapleMonster monster = c.getPlayer().getMap().getMonsterByOid(mobid);
					long mobhp = (mobid > 0 && monster != null) ? monster.getHp() : 100L;
					long mobmaxhp = (mobid > 0 && monster != null) ? monster.getMobMaxHp() : 100L;
					((List<PsychicGrabEntry>) grab.get(Integer.valueOf(i))).add(new PsychicGrabEntry(firstsize, a,
							-1 - a + i, mobid, secondsize, mobhp, mobmaxhp, unk, rect, unk2));
				}
				i++;
			}
			c.getPlayer().getMap()
					.broadcastMessage(CWvsContext.PsychicGrab(c.getPlayer().getId(), skillid, unk_, id, grab));
			grab.clear();
		} else if (skillid == 142110003 || skillid == 142110004 || skillid == 142120002 || skillid == 142120001) {
			List<Integer> grab_ = new ArrayList<>();
			int subskillid = skillid - 2;
			byte a = slea.readByte();
			int b = slea.readInt();
			if (skillid == 142110003 || skillid == 142120001) {
				subskillid = slea.readInt();
				slea.skip(4);
			}
			int size = slea.readInt();
			for (int i = 0; i < size; i++) {
				grab_.add(Integer.valueOf(slea.readInt()));
			}
			c.getPlayer().getMap().broadcastMessage(
					CWvsContext.PsychicGrabAttack(c.getPlayer().getId(), skillid, subskillid, unk_, id, a, b, grab_));
		}
		c.getPlayer().givePPoint(skillid);
	}

	public static void MatrixSkill(LittleEndianAccessor slea, MapleClient c) {
		SecondaryStatValueHolder lightning;
		SecondaryStatEffect eff;
		Map<SecondaryStat, Pair<Integer, Integer>> statups;
		MapleCharacter chr = c.getPlayer();
		if (chr == null) {
			return;
		}

		int skillid = slea.readInt();
		int level = slea.readInt();
		AttackInfo ret = new AttackInfo();
		ret.skill = skillid;
		ret.skilllevel = level;
		GameConstants.calcAttackPosition(slea, ret);

		int unk1 = slea.readInt();
		int unk2 = slea.readInt();
		int bullet = slea.readInt();
		slea.skip(1); // 1.2.373
		slea.skip(4);
		slea.skip(1);
		slea.skip(4);
		slea.skip(1);
		slea.skip(1); // 1.2.388

		slea.readPos();
		slea.skip(1);
		if (skillid == 400051008) {
			slea.skip(1); // 빅 휴즈 기간틱 캐논 볼
		}
		slea.readPos();
		slea.skip(4);
		slea.skip(4);
		if (skillid == 3321004 || skillid == 3321038 || skillid == 400051016 || skillid == 400021028
				|| skillid == 400051003 || skillid == 27121201 || skillid == 3001004 || skillid == 63111003
				|| skillid == 63111103 || skillid == 64111004 || skillid == 3301008 || skillid == 400041018
				|| skillid == 37111006 || skillid == 152120003 || skillid == 400011081 || skillid == 400011082
				|| skillid == 400021070 || skillid == 152121004 || skillid == 400011004 || skillid == 400041043
				|| skillid == 400041020 || skillid == 63101004 || skillid == 63101100 || skillid == 12120022
				|| skillid == 12120023 || skillid == 400031026 || skillid == 400031058) { // 에인션트 아스트라
			slea.skip(3);
		} else if (skillid == 154121001) {
			slea.readInt();
			slea.readInt();
			slea.readInt();
			slea.readByte();
			slea.readByte();
			slea.readPos();
			slea.readPos();
			slea.readPos();
			slea.skip(3);
		} else if (skillid == 3311010) {
			slea.skip(19);
		} else if (skillid == 400031034) {
			slea.skip(19);
		} else {
			slea.skip(2);
		}

		List<Integer> data = new ArrayList<>();
		boolean enable2 = (slea.readByte() == 1);
		if (enable2) {
			data.add(slea.readInt());
			data.add(slea.readInt());
			data.add(slea.readInt());
			data.add(slea.readInt());
			data.add(slea.readInt());
			data.add(slea.readInt());
			data.add((int) slea.readByte()); // othello

		}

		if (skillid == 2341000 // 엔젤레이 VI 구현
				|| skillid == 3241006 // 인핸스 피어싱 VI 구현
				|| skillid == 3241007 // 얼티밋 피어싱 VI 구현
				|| skillid == 63141009 // 스캐터링 샷 VI 구현
		) {
			slea.skip(55); // 1.2.391 Changed.
		}

		List<MatrixSkill> skills = GameConstants.matrixSkills(slea);
		Skill skill = SkillFactory.getSkill(skillid);
		SecondaryStatEffect effect = skill.getEffect(chr.getSkillLevel(skillid));
		c.getSession().writeAndFlush(CWvsContext.MatrixSkill(skillid, level, skills));
		chr.getMap().broadcastMessage(chr,
				CWvsContext.MatrixSkillMulti(chr, skillid, level, unk1, unk2, bullet, enable2, data, skills), false);
		if (effect.getCooldown(chr) > 0) {
			c.getSession().writeAndFlush(CField.skillCooldown(skillid, effect.getCooldown(chr)));
			chr.addCooldown(skillid, System.currentTimeMillis(), effect.getCooldown(chr));
		}
		if (!GameConstants.isNoApplySkill(skillid)) {
			effect.applyTo(chr, false);
		}
		if (skillid == 400031026) {
			chr.getMap().broadcastMessage(chr, CField.skillCancel(chr, 0), false);
		}
		if (skillid == 400021048) {
			chr.givePPoint(skillid);
		}
		if (GameConstants.isCain(c.getPlayer().getJob())) {
			c.getPlayer().handleRemainIncense(skillid, true);
		}
		switch (skillid) {
		case 12120023: // 인페르노라이즈 쿨타임
			chr.addCoolTime(12120022, 30000);
			chr.addCoolTime(12120023, 30000);
			chr.addCoolTime(12121002, 30000);
			break;
		case 151101001:
			chr.addSkillCustomInfo(151121041, 1L);
			c.getPlayer().에테르핸들러(c.getPlayer(), -15, skillid, false);
			break;
		case 37111006:
			chr.Cylinder(skillid);
			break;
		case 400021070:
			chr.peaceMaker = effect.getW();
			break;
		case 151100002:
			if (c.getPlayer().getSkillLevel(151120034) <= 0) {
				c.getPlayer().에테르핸들러(c.getPlayer(), -15, skillid, false);
			}
			break;
		case 400051016:
			chr.lightning -= 2;
			if (chr.lightning < 0) {
				chr.lightning = 0;
			}
			lightning = chr.checkBuffStatValueHolder(SecondaryStat.CygnusElementSkill);
			if (lightning != null) {
				lightning.effect.applyTo(chr, false);
			}
			break;
		case 63101100:
		case 63111103:
			chr.handleCainSkillCooldown(skillid);
			break;
		case 63141009: // 스캐터링 샷 VI
		case 63101004:
		case 63111003:
			chr.handleCainSkillCooldown(skillid);
			chr.handleStackskill(skillid, true);
			break;
		case 400051003: // 라이트닝 폼
			if (chr.transformEnergyOrb <= 0) {
				return;
			}
			eff = SkillFactory.getSkill(400051002).getEffect(chr.getSkillLevel(400051002));
			final MapleCharacter mapleCharacter2 = chr;
			--mapleCharacter2.transformEnergyOrb;
			if (chr.transformEnergyOrb == 0) {
				chr.transformEnergyOrb = -1;
			}
			statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
			statups.put(SecondaryStat.Transform,
					new Pair<Integer, Integer>(chr.transformEnergyOrb, (int) chr.getBuffLimit(400051002)));
			c.getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, eff, chr));
			chr.getMap().broadcastMessage(chr, CWvsContext.BuffPacket.giveForeignBuff(chr, statups, eff), false);
			SkillFactory.getSkill(400051003).getEffect(chr.getSkillLevel(400051002)).applyTo(chr);
			break;
		case 400051008:
		case 400051042:
			c.getPlayer().setBHGCCount(c.getPlayer().getBHGCCount() - 1);
			Vmatrixstackbuff(c, true, slea);
			break;
		case 3301008:
		case 3311010:
			MapleCharacter.렐릭게이지(c, skillid);
			break;
		/*
		 * case 400031056: { if
		 * (chr.getBuffedEffect(SecondaryStat.RepeatingCrossbowCatridge) != null) {
		 * System.err.println("chr.repeatingCrossbowCatridge " +
		 * chr.repeatingCrossbowCatridge); if (chr.repeatingCrossbowCatridge > 1) {
		 * chr.repeatingCrossbowCatridge--; Map<SecondaryStat, Pair<Integer, Integer>>
		 * map = new HashMap<>(); map.put(SecondaryStat.RepeatingCrossbowCatridge, new
		 * Pair<>(Integer.valueOf(chr.repeatingCrossbowCatridge), Integer.valueOf((int)
		 * chr.getBuffLimit(chr.getBuffSource(SecondaryStat.RepeatingCrossbowCatridge)))
		 * ));
		 * chr.getClient().getSession().writeAndFlush(CWvsContext.BuffPacket.giveBuff(
		 * map, chr.getBuffedEffect(SecondaryStat.RepeatingCrossbowCatridge), chr));
		 * break; }
		 * chr.cancelEffectFromBuffStat(SecondaryStat.RepeatingCrossbowCatridge); }
		 * break; }
		 */
		}
	}

	public static void UpdateSymbol(LittleEndianAccessor slea, MapleClient c, int plus) {
		try {
			long needmeso;
			int pos = slea.readInt() * -1;
			Equip item = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) pos);
			boolean ArcneSymbol = GameConstants.isArcaneSymbol(item.getItemId());
			int nextupgrade = ArcneSymbol ? GameConstants.ArcaneNextUpgrade(item.getArcLevel())
					: GameConstants.AutNextUpgrade(item.getArcLevel());
			long l = needmeso = ArcneSymbol ? GameConstants.needArcSymbolMeso(item.getItemId(), item.getArcLevel())
					: GameConstants.needAutSymbolMeso(item.getItemId(), item.getArcLevel());
			if (item.getArcEXP() >= nextupgrade) {
				if (c.getPlayer().getMeso() >= needmeso) {
					int astats = 100;
					if (!ArcneSymbol) {
						astats *= 2;
					}
					int stat = ArcneSymbol ? 10 * (item.getArcLevel() + 2) : item.getArc() + 10;
					c.getPlayer().gainMeso(-needmeso, false);
					item.setArcEXP(item.getArcEXP() - nextupgrade);
					item.setArcLevel(item.getArcLevel() + 1);
					item.setArc((short) (item.getArc() + 10));
					if (GameConstants.isXenon(c.getPlayer().getJob())) {
						int stats = 39;
						if (!ArcneSymbol) {
							stats *= 2;
						}
						item.setStr((short) (item.getStr() + stats));
						item.setDex((short) (item.getDex() + stats));
						item.setLuk((short) (item.getLuk() + stats));
					} else if (GameConstants.isDemonAvenger(c.getPlayer().getJob())) {
						int stats = 175;
						if (!ArcneSymbol) {
							stats *= 2;
						}
						item.setHp((short) (item.getHp() + stats));
					} else if (GameConstants.isWarrior(c.getPlayer().getJob())) {
						item.setStr((short) (item.getStr() + astats));
					} else if (GameConstants.isMagician(c.getPlayer().getJob())) {
						item.setInt((short) (item.getInt() + astats));
					} else if (GameConstants.isArcher(c.getPlayer().getJob())
							|| GameConstants.isCaptain(c.getPlayer().getJob())
							|| GameConstants.isMechanic(c.getPlayer().getJob())
							|| GameConstants.isAngelicBuster(c.getPlayer().getJob())) {
						item.setDex((short) (item.getDex() + astats));
					} else if (GameConstants.isThief(c.getPlayer().getJob())) {
						item.setLuk((short) (item.getLuk() + astats));
					} else if (GameConstants.isPirate(c.getPlayer().getJob())) {
						item.setStr((short) (item.getStr() + astats));
					}
					c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.updateInventoryItem(false,
							MapleInventoryType.EQUIP, item));
					c.getSession().writeAndFlush(CWvsContext.UseSymbolLevelup(item));
				} else {
					c.getPlayer().dropMessage(1, "金幣不足.");
					c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
				}
			} else {
				c.getPlayer().dropMessage(1, "所需成長值不足.");
				c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void SymbolExp(LittleEndianAccessor slea, MapleClient c) {
		try {
			Equip source = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(slea.readShort());
			if (source == null) {
				return;
			}
			int baseid = GameConstants.isArcaneSymbol(source.getItemId()) ? -1600
					: (GameConstants.isAuthenticSymbol(source.getItemId()) ? -1700 : 0);// 原初徽章经验转移
			Equip target = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) baseid);
			if (target != null && source.getItemId() != target.getItemId()) {
				target = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) (baseid - 1));
			}
			if (target == null) {
				target = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) (baseid - 1));
			}
			if (target != null && source.getItemId() != target.getItemId()) {
				target = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) (baseid - 2));
			}
			if (target == null) {
				target = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) (baseid - 2));
			}
			if (target != null && source.getItemId() != target.getItemId()) {
				target = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) (baseid - 3));
			}
			if (target == null) {
				target = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) (baseid - 3));
			}
			if (target != null && source.getItemId() != target.getItemId()) {
				target = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) (baseid - 4));
			}
			if (target == null) {
				target = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) (baseid - 4));
			}
			if (target != null && source.getItemId() != target.getItemId()) {
				target = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) (baseid - 5));
			}
			if (target == null) {
				return;
			}
			if (source.getItemId() != target.getItemId()) {
				return;
			}
			target.setArcEXP(target.getArcEXP() + source.getArcEXP() / 2 + 1);
			c.getPlayer().getInventory(MapleInventoryType.EQUIP).removeSlot(source.getPosition());
			c.getSession().writeAndFlush(CWvsContext.InventoryPacket.clearInventoryItem(MapleInventoryType.EQUIP,
					source.getPosition(), false));
			c.getPlayer().getSymbol().remove(source);
			c.getSession().writeAndFlush(
					CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIP, target));
			c.getSession().writeAndFlush(CWvsContext.UseSymbolLevelup(target));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void SymbolMultiExp(LittleEndianAccessor slea, MapleClient c) {
		try {
			int itemid = slea.readInt();
			int exp = slea.readInt();
			int havecount = slea.readInt();
			Equip target = null;
			if (GameConstants.isArcaneSymbol(itemid)) {
				target = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) -1600);// 神秘徽章(Authentic
																												// Symbol)的智能装备槽位查找算法
				if (target != null && itemid != target.getItemId()) {
					target = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) -1601);
				}
				if (target == null) {
					target = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) -1601);
				}
				if (target != null && itemid != target.getItemId()) {
					target = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) -1602);
				}
				if (target == null) {
					target = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) -1602);
				}
				if (target != null && itemid != target.getItemId()) {
					target = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) -1603);
				}
				if (target == null) {
					target = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) -1603);
				}
				if (target != null && itemid != target.getItemId()) {
					target = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) -1604);
				}
				if (target == null) {
					target = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) -1604);
				}
				if (target != null && itemid != target.getItemId()) {
					target = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) -1605);
				}
			} else {
				target = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) -1700);// 原初徽章(Authentic
																												// Symbol)的智能装备槽位查找算法
				if (target != null && itemid != target.getItemId()) {
					target = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) -1701);
				}
				if (target == null) {
					target = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) -1701);
				}
				if (target != null && itemid != target.getItemId()) {
					target = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) -1702);
				}
				if (target == null) {
					target = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) -1702);
				}
				if (target != null && itemid != target.getItemId()) {
					target = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) -1703);
				}
				if (target == null) {
					target = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) -1703);
				}
				if (target != null && itemid != target.getItemId()) {
					target = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) -1704);
				}
				if (target == null) {
					target = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) -1704);
				}
				if (target != null && itemid != target.getItemId()) {
					target = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) -1705);
				}
			}
			if (target == null) {
				return;
			}
			if (itemid != target.getItemId()) {
				return;
			}
			List<Equip> removeitems = new ArrayList<>();
			for (Map.Entry<Short, Item> item : c.getPlayer().getInventory(MapleInventoryType.EQUIP).lists()
					.entrySet()) {
				if (((Item) item.getValue()).getItemId() == itemid && ((Equip) item.getValue()).getArcEXP() == 1
						&& ((Equip) item.getValue()).getArcLevel() == 1 && removeitems.size() < havecount) {
					Equip source = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP)
							.getItem(((Short) item.getKey()).shortValue());
					removeitems.add(source);
				}
			}
			if (removeitems.size() != havecount) {
				FileoutputUtil.log("Log_Packet_Except.rtf", c.getPlayer().getName() + " 캐릭터 심볼 비정상 사용발견");
			}
			target.setArcEXP(target.getArcEXP() + removeitems.size());
			for (Equip item : removeitems) {
				c.getPlayer().getInventory(MapleInventoryType.EQUIP).removeSlot(item.getPosition());
				c.getSession().writeAndFlush(CWvsContext.InventoryPacket.clearInventoryItem(MapleInventoryType.EQUIP,
						item.getPosition(), false));
				c.getPlayer().getSymbol().remove(item);
			}
			c.getSession().writeAndFlush(
					CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIP, target));
			c.getSession().writeAndFlush(CWvsContext.UseSymbolLevelup(target));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void UnlinkSkill(int skillid, MapleClient c) {
		if (skillid == 80000055) {
			Map<Integer, Integer> skills = new HashMap<>();
			for (int i = 80000066; i <= 80000070; i++) {
				if (c.getPlayer().getTotalSkillLevel(i) > 0) {
					for (Triple<Skill, SkillEntry, Integer> a : c.getPlayer().getLinkSkills()) {
						if (((Skill) a.left).getId() == i) {
							skills.put(Integer.valueOf(i), (Integer) a.right);
						}
					}
					c.getPlayer().changeSingleSkillLevel(SkillFactory.getSkill(i), 0, (byte) 0);
					c.getSession().writeAndFlush(CWvsContext.Unlinkskillunlock(i, 0));
				}
			}
			c.getPlayer().changeSingleSkillLevel(SkillFactory.getSkill(skillid), 0, (byte) 0);
			c.getSession().writeAndFlush(CWvsContext.Unlocklinkskill(skillid, skills));
		} else if (skillid == 80000329) {
			Map<Integer, Integer> skills = new HashMap<>();
			for (int i = 80000333; i <= 80000335; i++) {
				if (c.getPlayer().getTotalSkillLevel(i) > 0) {
					for (Triple<Skill, SkillEntry, Integer> a : c.getPlayer().getLinkSkills()) {
						if (((Skill) a.left).getId() == i) {
							skills.put(Integer.valueOf(i), (Integer) a.right);
						}
					}
					c.getPlayer().changeSingleSkillLevel(SkillFactory.getSkill(i), 0, (byte) 0);
					c.getSession().writeAndFlush(CWvsContext.Unlinkskillunlock(i, 0));
				}
			}
			if (c.getPlayer().getTotalSkillLevel(80000378) > 0) {
				skills.put(Integer.valueOf(80000378), Integer.valueOf(c.getPlayer().getId()));
				for (Triple<Skill, SkillEntry, Integer> a : c.getPlayer().getLinkSkills()) {
					if (((Skill) a.left).getId() == 80000378) {
						skills.put(Integer.valueOf(80000378), (Integer) a.right);
					}
				}
				c.getPlayer().changeSingleSkillLevel(SkillFactory.getSkill(80000378), 0, (byte) 0);
				c.getSession().writeAndFlush(CWvsContext.Unlinkskillunlock(80000378, 0));
			}
			c.getPlayer().changeSingleSkillLevel(SkillFactory.getSkill(skillid), 0, (byte) 0);
			c.getSession().writeAndFlush(CWvsContext.Unlocklinkskill(skillid, skills));
		} else if (skillid == 80002758) {
			Map<Integer, Integer> skills = new HashMap<>();
			for (int i = 80002759; i <= 80002761; i++) {
				if (c.getPlayer().getTotalSkillLevel(i) > 0) {
					for (Triple<Skill, SkillEntry, Integer> a : c.getPlayer().getLinkSkills()) {
						if (((Skill) a.left).getId() == i) {
							skills.put(Integer.valueOf(i), (Integer) a.right);
						}
					}
					c.getPlayer().changeSingleSkillLevel(SkillFactory.getSkill(i), 0, (byte) 0);
					c.getSession().writeAndFlush(CWvsContext.Unlinkskillunlock(i, 0));
				}
			}
			c.getPlayer().changeSingleSkillLevel(SkillFactory.getSkill(skillid), 0, (byte) 0);
			c.getSession().writeAndFlush(CWvsContext.Unlocklinkskill(skillid, skills));
		} else if (skillid == 80002762) {
			Map<Integer, Integer> skills = new HashMap<>();
			for (int i = 80002763; i <= 80002765; i++) {
				if (c.getPlayer().getTotalSkillLevel(i) > 0) {
					for (Triple<Skill, SkillEntry, Integer> a : c.getPlayer().getLinkSkills()) {
						if (((Skill) a.left).getId() == i) {
							skills.put(Integer.valueOf(i), (Integer) a.right);
						}
					}
					c.getPlayer().changeSingleSkillLevel(SkillFactory.getSkill(i), 0, (byte) 0);
					c.getSession().writeAndFlush(CWvsContext.Unlinkskillunlock(i, 0));
				}
			}
			c.getPlayer().changeSingleSkillLevel(SkillFactory.getSkill(skillid), 0, (byte) 0);
			c.getSession().writeAndFlush(CWvsContext.Unlocklinkskill(skillid, skills));
		} else if (skillid == 80002766) {
			Map<Integer, Integer> skills = new HashMap<>();
			for (int i = 80002767; i <= 80002769; i++) {
				if (c.getPlayer().getTotalSkillLevel(i) > 0) {
					for (Triple<Skill, SkillEntry, Integer> a : c.getPlayer().getLinkSkills()) {
						if (((Skill) a.left).getId() == i) {
							skills.put(Integer.valueOf(i), (Integer) a.right);
						}
					}
					c.getPlayer().changeSingleSkillLevel(SkillFactory.getSkill(i), 0, (byte) 0);
					c.getSession().writeAndFlush(CWvsContext.Unlinkskillunlock(i, 0));
				}
			}
			c.getPlayer().changeSingleSkillLevel(SkillFactory.getSkill(skillid), 0, (byte) 0);
			c.getSession().writeAndFlush(CWvsContext.Unlocklinkskill(skillid, skills));
		} else if (skillid == 80002770) {
			Map<Integer, Integer> skills = new HashMap<>();
			for (int i = 80002771; i <= 80002773; i++) {
				if (c.getPlayer().getTotalSkillLevel(i) > 0) {
					for (Triple<Skill, SkillEntry, Integer> a : c.getPlayer().getLinkSkills()) {
						if (((Skill) a.left).getId() == i) {
							skills.put(Integer.valueOf(i), (Integer) a.right);
						}
					}
					c.getPlayer().changeSingleSkillLevel(SkillFactory.getSkill(i), 0, (byte) 0);
					c.getSession().writeAndFlush(CWvsContext.Unlinkskillunlock(i, 0));
				}
			}
			c.getPlayer().changeSingleSkillLevel(SkillFactory.getSkill(skillid), 0, (byte) 0);
			c.getSession().writeAndFlush(CWvsContext.Unlocklinkskill(skillid, skills));
		} else if (skillid == 80002774) {
			Map<Integer, Integer> skills = new HashMap<>();
			for (int i = 80002775; i <= 80002776; i++) {
				if (c.getPlayer().getTotalSkillLevel(i) > 0) {
					for (Triple<Skill, SkillEntry, Integer> a : c.getPlayer().getLinkSkills()) {
						if (((Skill) a.left).getId() == i) {
							skills.put(Integer.valueOf(i), (Integer) a.right);
						}
					}
					c.getPlayer().changeSingleSkillLevel(SkillFactory.getSkill(i), 0, (byte) 0);
					c.getSession().writeAndFlush(CWvsContext.Unlinkskillunlock(i, 0));
				}
			}
			if (c.getPlayer().getTotalSkillLevel(80000000) > 0) {
				skills.put(Integer.valueOf(80000378), Integer.valueOf(c.getPlayer().getId()));
				for (Triple<Skill, SkillEntry, Integer> a : c.getPlayer().getLinkSkills()) {
					if (((Skill) a.left).getId() == 80000000) {
						skills.put(Integer.valueOf(80000000), (Integer) a.right);
					}
				}
				c.getPlayer().changeSingleSkillLevel(SkillFactory.getSkill(80000000), 0, (byte) 0);
				c.getSession().writeAndFlush(CWvsContext.Unlinkskillunlock(80000000, 0));
			}
			c.getPlayer().changeSingleSkillLevel(SkillFactory.getSkill(skillid), 0, (byte) 0);
			c.getSession().writeAndFlush(CWvsContext.Unlocklinkskill(skillid, skills));
		} else {
			int cid = 0;
			for (Triple<Skill, SkillEntry, Integer> a : c.getPlayer().getLinkSkills()) {
				if (((Skill) a.left).getId() == skillid) {
					cid = (Integer) a.right;
				}
			}

			c.getPlayer().changeSingleSkillLevel(SkillFactory.getSkill(skillid), 0, (byte) 0);
			c.getSession().writeAndFlush(CWvsContext.Unlinkskillunlock(skillid, 0));
			c.getSession().writeAndFlush(CWvsContext.Unlinkskill(skillid, cid));
		}

		c.getPlayer().getStat().recalcLocalStats(c.getPlayer());
	}

	public static void LinkSkill(int skillid, int sendid, int recvid, MapleClient c) {
		if (c.getPlayer().getTotalSkillLevel(skillid) > 0) {
			c.getPlayer().dropMessage(6, "동일한 링크를 중복해서 착용하실 수 없습니다.");
			c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
		}
		int skilllevel = (MapleCharacter.loadCharFromDB(sendid, c, false).getLevel() >= 120) ? 2 : 1;
		int totalskilllv = 0;
		int ordinarySkill = 0;
		byte odrinaryMaxLevel = 0;
		if (skillid >= 80000066 && skillid <= 80000070) {
			ordinarySkill = 80000055;
			odrinaryMaxLevel = 10;
		} else if ((skillid >= 80000333 && skillid <= 80000335) || skillid == 80000378) {
			ordinarySkill = 80000329;
			odrinaryMaxLevel = 8;
		} else if (skillid >= 80002759 && skillid <= 80002761) {
			ordinarySkill = 80002758;
			odrinaryMaxLevel = 6;
		} else if (skillid >= 80002763 && skillid <= 80002765) {
			ordinarySkill = 80002762;
			odrinaryMaxLevel = 6;
		} else if (skillid >= 80002767 && skillid <= 80002769) {
			ordinarySkill = 80002766;
			odrinaryMaxLevel = 6;
		} else if (skillid >= 80002771 && skillid <= 80002773) {
			ordinarySkill = 80002770;
			odrinaryMaxLevel = 6;
		} else if ((skillid >= 80002775 && skillid <= 80002776) || skillid == 80000000) {
			ordinarySkill = 80002774;
			odrinaryMaxLevel = 6;
		}
		if (ordinarySkill > 0) {
			totalskilllv = skilllevel + c.getPlayer().getSkillLevel(ordinarySkill);
			c.getPlayer().changeSingleSkillLevel(SkillFactory.getSkill(skillid), skilllevel, (byte) 2);
			c.getPlayer().changeSingleSkillLevel(SkillFactory.getSkill(ordinarySkill), totalskilllv, odrinaryMaxLevel);
		} else {
			c.getPlayer().changeSingleSkillLevel(SkillFactory.getSkill(skillid), skilllevel, (byte) 2);
		}
		c.getSession().writeAndFlush(CWvsContext.Unlinkskillunlock(skillid, 1));
		c.getSession()
				.writeAndFlush(CWvsContext.Linkskill(skillid, sendid, c.getPlayer().getId(), skilllevel, totalskilllv));
		c.getPlayer().getStat().recalcLocalStats(c.getPlayer());
	}

	public static void IncreaseDuration(MapleCharacter player, int skillid) {
		if (skillid == 400051006 && player.bulletParty < 6) {
			SecondaryStatValueHolder bulletParty = player.checkBuffStatValueHolder(SecondaryStat.BulletParty);
			if (bulletParty != null) {
				player.bulletParty++;
				player.updateEffect(bulletParty.effect, SecondaryStat.BulletParty, 1000);
			}
		}
	}

	public static void Respawn(MapleClient c) {
		MapleCharacter chr = c.getPlayer();
		if (chr.getDeathCount() > 0 || chr.liveCounts() > 0) {
			MapleMap to = chr.getMap();
			chr.changeMap(to, to.getPortal(0));
		} else {
			chr.getStat().setHp(50L, chr);
			MapleMap map = chr.getMap();
			MapleMap to = null;
			if (map.getForcedReturnId() != 999999999 && map.getForcedReturnMap() != null) {
				to = map.getForcedReturnMap();
			} else {
				to = map.getReturnMap();
			}
			chr.changeMap(to, to.getPortal(0));
		}
	}

	public static void RainBowRushStart(MapleClient c) {
		if (c.getPlayer().isRainBowRush()) {
			c.getPlayer().setRainbowRushStart(System.currentTimeMillis());
			c.getSession().writeAndFlush(CField.UIPacket.getRainBowRushStart());
		}
	}

	public static void RainBowRushTimer(LittleEndianAccessor slea, MapleClient c) {
		if (c.getPlayer().isRainBowRush()) {
			int time = slea.readInt();
			c.getPlayer().setRainbowRushTime(time);
			if (time >= 127000) {
				RainBowRushDead(c);
			}
		}
	}

	public static void RainBowRushDead(MapleClient c) {
		if (c.getPlayer().isRainBowRush()) {
			int jam = 10 * c.getPlayer().getRainbowRushTime() / 1000 * 100 / 120 / 100;
			if (jam >= 10) {
				jam = 10;
			}
			c.getSession().writeAndFlush(CField.UIPacket.getRainBowResult(jam, c.getPlayer().getRainbowRushTime()));
			c.getPlayer().setRainbowRushTime(0);
		}
	}

	public static void RainBowRushReturnMap(MapleClient c) {
		if (c.getPlayer().isRainBowRush()) {
			c.getPlayer().setRainBowRush(false);
			c.getPlayer().warp(ServerConstants.warpMap);
		}
	}

	public static void RespawnLucid(LittleEndianAccessor slea, MapleClient c) {
		MapleCharacter chr = c.getPlayer();
		slea.readByte();
		if (chr.getDeathCount() > 0 || chr.liveCounts() > 0) {
			int x, y, respawnx;
			MaplePortal mp;

			if (chr.getDeathCount() > 0) {
				c.getSession().writeAndFlush(CField.getDeathCount(chr.getDeathCount()));
			}
			chr.getStat().setHp(chr.getStat().getCurrentMaxHp(), chr);
			chr.getStat().setMp(chr.getStat().getCurrentMaxMp(chr), chr);
			chr.updateSingleStat(MapleStat.HP, chr.getStat().getCurrentMaxHp());
			chr.updateSingleStat(MapleStat.MP, chr.getStat().getCurrentMaxMp(chr));
			switch (chr.getMapId()) {
			case 450004150:
			case 450004250:
			case 450004450:
			case 450004550:
			case 450004750:
			case 450004850:
				x = (chr.getMap().getId() == 450004150 || chr.getMap().getId() == 450004450
						|| chr.getMap().getId() == 450004750) ? 157 : (Randomizer.nextBoolean() ? 316 : 1027);
				y = (chr.getMap().getId() == 450004150 || chr.getMap().getId() == 450004450
						|| chr.getMap().getId() == 450004750) ? 48 : (Randomizer.nextBoolean() ? -855 : -842);
				chr.getMap().broadcastMessage(CField.onUserTeleport(chr.getId(), x, y));
				break;
			case 450013100:
				chr.getMap().broadcastMessage(CField.onUserTeleport(chr.getId(), -285, 85));
				break;
			case 450013300:
				chr.getMap().broadcastMessage(CField.onUserTeleport(chr.getId(), -461, 88));
				break;
			case 220080100:
			case 220080200:
			case 220080300:
				chr.getMap().broadcastMessage(CField.onUserTeleport(chr.getId(), -920, 160));
				break;
			case 350060400:
			case 350060500:
			case 350060600:
			case 350060700:
			case 350060800:
			case 350060900:
				respawnx = (chr.getMapId() == 350060400 || chr.getMapId() == 350060700) ? -2 : -440;
				chr.getMap().broadcastMessage(CField.onUserTeleport(chr.getId(), respawnx, -23));
				if (chr.getMapId() == 350060400 || chr.getMapId() == 350060700) {
					for (MapleMonster mob : chr.getMap().getAllMonster()) {
						if (mob.getId() != 8950100 && mob.getId() != 8950000) {
							chr.getMap().killMonsterType(mob, 1);
						}
					}
				}
				break;
			case 350160100:
			case 350160140:
				respawnx = (chr.getMapId() == 350160100 || chr.getMapId() == 350160100) ? 212 : 187;
				chr.getMap().broadcastMessage(CField.onUserTeleport(chr.getId(), respawnx, 10));
				break;
			case 450008750:
				mp = chr.getMap().getPortal("sp");
				if (mp != null) {
					c.getSession().writeAndFlush(CField.onUserTeleport((mp.getPosition()).x, (chr.getPosition()).y));
					break;
				}
				c.getSession().writeAndFlush(CField.onUserTeleport((chr.getPosition()).x, (chr.getPosition()).y));
				break;
			default:
				mp = chr.getMap().getPortal("sp");
				if (mp != null) {
					c.getSession().writeAndFlush(CField.onUserTeleport((mp.getPosition()).x, (mp.getPosition()).y));
					break;
				}
				c.getSession().writeAndFlush(CField.onUserTeleport((chr.getPosition()).x, (chr.getPosition()).y));
				break;
			}
			SkillFactory.getSkill(80000329).getEffect(chr.getSkillLevel(80000329)).applyTo(chr, false);
			if (GameConstants.isDemonAvenger(chr.getJob())) {
				chr.updateExceed(chr.getExceed());
			}
		} else {
			c.getPlayer().warpAfterScreenTaskCancel();
			c.getPlayer().bossTaskCancel();

			chr.getStat().setHp((short) (int) chr.getStat().getCurrentMaxHp(), chr);
			MapleMap map = chr.getMap();
			MapleMap to = null;
			if (map.getForcedReturnId() != 999999999 && map.getForcedReturnMap() != null) {
				to = map.getForcedReturnMap();
			} else {
				to = map.getReturnMap();
			}
			chr.changeMap(to, to.getPortal(0));
		}
	}

	public static void megaSmasherRequest(LittleEndianAccessor slea, MapleClient c) {
		MapleCharacter chr = c.getPlayer();
		boolean start = (slea.readByte() == 1);
		if (start) {
			SecondaryStatEffect effect = SkillFactory.getSkill(400041007).getEffect(chr.getSkillLevel(400041007));
			chr.setSkillCustomInfo(400041007, System.currentTimeMillis(), 0L);
			chr.isMegaSmasherCharging = true;
			chr.getClient().send(CField.skillCooldown(400041007, effect.getCooldown(chr)));
			chr.addCooldown(400041007, System.currentTimeMillis(), effect.getCooldown(chr));
		} else {
			if (!chr.isMegaSmasherCharging) {
				while (chr.getBuffedValue(400041007)) {
					chr.cancelEffect(chr.getBuffedEffect(400041007));
				}
				return;
			}
			chr.isMegaSmasherCharging = false;
			while (chr.getBuffedValue(400041007)) {
				chr.cancelEffect(chr.getBuffedEffect(400041007));
			}
			SecondaryStatEffect effect = SkillFactory.getSkill(400041007).getEffect(chr.getSkillLevel(400041007));
			int maxChargeTime = effect.getDuration() + effect.getZ() * 1000;
			int chargeTime = Math.min(maxChargeTime,
					effect.getDuration() + (int) ((System.currentTimeMillis() - chr.getSkillCustomValue0(400041007))
							/ (effect.getY() * 1000) * 1000L));
			effect.applyTo(chr, false, chargeTime);
			chr.setBuffedValue(SecondaryStat.MegaSmasher, 400041007, 1);
			chr.removeSkillCustomInfo(400041007);
			chr.getClient().getSession().writeAndFlush(CField.skillCooldown(400041007, effect.getCooldown(chr)));
			chr.addCooldown(400041007, System.currentTimeMillis(), effect.getCooldown(chr));
		}
	}

	public static void SoulMatch(LittleEndianAccessor slea, MapleClient c) {
		slea.skip(4);
		int state = slea.readInt();
		if (state == 1) {
			c.getPlayer().dropMessage(6, "보스 입장이 시작됩니다.");
		}
		for (List<Pair<Integer, MapleCharacter>> souls : c.getChannelServer().getSoulmatch()) {
			for (Pair<Integer, MapleCharacter> soulz : souls) {
				if (((MapleCharacter) soulz.right).equals(c.getPlayer())) {
					c.getChannelServer().getSoulmatch().remove(souls);
				}
			}
		}
		c.getSession().writeAndFlush(CField.UIPacket.closeUI(184));
	}

	public static void DailyGift(MapleClient c) {
		int date = Integer.parseInt(c.getKeyValue("dailyGiftDay"));
		int complete = Integer.parseInt(c.getKeyValue("dailyGiftComplete"));
		if (complete == 0) {
			if (date >= GameConstants.dailyItems.size()) {
				c.getSession().writeAndFlush(CField.dailyGift(c.getPlayer(), 3, 0));
				return;
			}
			DailyGiftItemInfo item = GameConstants.dailyItems.get(date);
			int itemId = item.getItemId();
			int quantity = item.getQuantity();
			if (item.getItemId() == 0 && item.getSN() > 0) {
				CashItemInfo cashItem = CashItemFactory.getInstance().getItem(item.getSN());
				itemId = cashItem.getId();
				quantity = cashItem.getCount();
			}
			if (itemId == 4310291) {
				c.getPlayer().AddStarDustCoin(1, quantity);
			} else {
				Item addItem;
				if (!MapleInventoryManipulator.checkSpace(c, itemId, quantity, "")) {
					c.getSession().writeAndFlush(CField.dailyGift(c.getPlayer(), 7, 0));
					return;
				}
				if (GameConstants.getInventoryType(itemId) == MapleInventoryType.EQUIP) {
					addItem = MapleItemInformationProvider.getInstance().getEquipById(itemId);
				} else {
					addItem = new Item(itemId, (short) 0, (short) quantity, 0);
				}
				if (MapleItemInformationProvider.getInstance().isCash(itemId)) {
					addItem.setUniqueId(MapleInventoryIdentifier.getInstance());
				}
				MapleInventoryManipulator.addbyItem(c, addItem);
			}
			c.setKeyValue("dailyGiftDay", String.valueOf(date + 1));
			c.setKeyValue("dailyGiftComplete", "1");
			c.getSession().writeAndFlush(CWvsContext.updateDailyGift("count=" + c.getKeyValue("dailyGiftComplete")
					+ ";day=" + c.getKeyValue("dailyGiftDay") + ";date=" + c.getPlayer().getKeyValue(16700, "date")));
			c.getSession().writeAndFlush(CField.dailyGift(c.getPlayer(), 2, itemId));
			c.getSession().writeAndFlush(CField.dailyGift(c.getPlayer(), 0, itemId));
		} else {
			c.getSession().writeAndFlush(CField.dailyGift(c.getPlayer(), 5, 0));
		}
	}

	public static void PlayerWeaponMotionChange(LittleEndianAccessor slea, MapleClient c) {
		MapleCharacter character = c.getPlayer();
		int type = slea.readInt();
		switch (type) {
		case 0: // 기본무기
			character.addKV("Weapon_motion", "0");
			break;
		case 1: // 한손무기
			character.addKV("Weapon_motion", "1");
			break;
		case 2: // 두손무기
			character.addKV("Weapon_motion", "2");
			break;
		case 3: // 건
			character.addKV("Weapon_motion", "3");
			break;

		}
		character.getClient().getSession().writeAndFlush(CField.WeaponMotionChange(character.getId(), type));
		character.updateInfoQuest(27042,
				"use=" + character.getV("Weapon_motion") + ";use=" + character.getV("Weapon_motion"));
	}

	public static void ShadowServentExtend(LittleEndianAccessor slea, MapleClient c) {
		int skillid = slea.readInt();
		for (MapleSummon s : c.getPlayer().getSummons()) {
			if (s.getMovementType() == SummonMovementType.ShadowServantExtend && s.getChangePositionCount() < 3) {
				s.setChangePositionCount((byte) (s.getChangePositionCount() + 1));
				Point summonpos = s.getTruePosition();
				c.getSession().writeAndFlush(CField.ShadowServentExtend(summonpos));
				c.getSession()
						.writeAndFlush(CField.ShadowServentRefresh(c.getPlayer(), s, 3 - s.getChangePositionCount()));
				c.send(CField.EffectPacket.showEffect(c.getPlayer(), skillid, skillid, 10, 0, 0, (byte) 0, true, null,
						null, null));
				c.getPlayer().getMap().broadcastMessage(c.getPlayer(), CField.EffectPacket.showEffect(c.getPlayer(),
						skillid, skillid, 10, 0, 0, (byte) 0, false, null, null, null), false);
				s.setPosition(c.getPlayer().getTruePosition());
				c.getPlayer().setPosition(summonpos);
			}
		}
	}

	public static void joker(MapleClient c) {
		c.getPlayer().setSkillCustomInfo(400041010, c.getPlayer().getSkillCustomValue0(400041010) + 1L, 0L);
		for (int i = 0; i < 14; i++) {
			MapleAtom atom = new MapleAtom(false, c.getPlayer().getId(), 1, true, 400041010,
					(c.getPlayer().getTruePosition()).x, (c.getPlayer().getTruePosition()).y);
			ForceAtom forceAtom = new ForceAtom(2, Randomizer.rand(16, 26), Randomizer.rand(7, 11),
					Randomizer.nextInt(4) + 5, 0);
			forceAtom.setnAttackCount(forceAtom.getnAttackCount() + 1);
			atom.addForceAtom(forceAtom);
			c.getPlayer().getMap().spawnMapleAtom(atom);
		}
	}

	public static void activePrayBuff(MapleClient c) {
		MapleCharacter player = c.getPlayer();
		MapleParty party = player.getParty();
		SecondaryStatEffect effect = player.getBuffedEffect(SecondaryStat.Pray);
		Map<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<>();
		long starttime = System.currentTimeMillis();
		if (effect != null) {
			int int_ = player.getStat().getTotalInt();
			int at = 0;
			int incPMdR = effect.getQ() + Math.min(effect.getW(), int_ / effect.getQ2());
			int incRecovery = int_ / effect.getY();
			int incBooster = Math.min(int_ / effect.getU(), 3);
			int incRecoveryHP = Math.min(effect.getZ(), incRecovery + effect.getX());
			int incRecoveryMP = Math.min(effect.getZ(), incRecovery + effect.getX());
			incRecoveryHP = (int) (player.getStat().getCurrentMaxHp() * incRecoveryHP * 0.01D);
			incRecoveryMP = (int) (player.getStat().getCurrentMaxMp(player) * incRecoveryMP * 0.01D);
			if (player.isAlive()) {
				player.addMPHP(incRecoveryHP, incRecoveryMP);
			}
			if (c.getPlayer().getKeyValue(800023, "indiepmer") > 0) {
				at += c.getPlayer().getKeyValue(800023, "indiepmer");
			}
			if (incPMdR > 0) {
				statups.put(SecondaryStat.IndiePmdR, new Pair<>(Integer.valueOf(incPMdR), Integer.valueOf(2000)));
			}
			if (incBooster > 0) {
				statups.put(SecondaryStat.IndieBooster,
						new Pair<>(Integer.valueOf(-incBooster), Integer.valueOf(2000)));
			}
			player.cancelEffectFromBuffStat(SecondaryStat.IndiePmdR, effect.getSourceId());
			player.cancelEffectFromBuffStat(SecondaryStat.IndieBooster, effect.getSourceId());
			for (Map.Entry<SecondaryStat, Pair<Integer, Integer>> statup : statups.entrySet()) {
				player.registerEffect(effect, starttime, statup, false, player.getId());
			}
			c.getSession().writeAndFlush(CWvsContext.BuffPacket.giveBuff(statups, effect, player));
			if (party != null) {
				for (MaplePartyCharacter pc : party.getMembers()) {
					if (pc.isOnline() && pc.getMapid() == player.getMapId() && pc.getChannel() == c.getChannel()) {
						MapleCharacter victim = c.getChannelServer().getPlayerStorage()
								.getCharacterByName(pc.getName());
						if (victim != null) {
							if (victim.isAlive() && player.getId() != victim.getId()) {
								if (effect.calculateBoundingBox(player.getTruePosition(), player.isFacingLeft())
										.contains(victim.getTruePosition())) {
									incRecoveryHP = (int) (victim.getStat().getMaxHp() * incRecoveryHP * 0.01D);
									incRecoveryMP = (int) (victim.getStat().getMaxMp() * incRecoveryMP * 0.01D);
									victim.addMPHP(incRecoveryHP, incRecoveryMP);
									statups.clear();
									if (incPMdR > 0) {
										statups.put(SecondaryStat.IndiePmdR,
												new Pair<>(Integer.valueOf(incPMdR), Integer.valueOf(2000)));
									}
									if (incBooster > 0) {
										statups.put(SecondaryStat.IndieBooster,
												new Pair<>(Integer.valueOf(-incBooster), Integer.valueOf(2000)));
									}
									victim.cancelEffectFromBuffStat(SecondaryStat.IndiePmdR, effect.getSourceId());
									victim.cancelEffectFromBuffStat(SecondaryStat.IndieBooster, effect.getSourceId());
									for (Map.Entry<SecondaryStat, Pair<Integer, Integer>> statup : statups.entrySet()) {
										victim.registerEffect(effect, starttime, statup, false, victim.getId());
									}
									victim.getClient().getSession()
											.writeAndFlush(CWvsContext.BuffPacket.giveBuff(statups, effect, player));
								}
								continue;
							}
							if (!effect.calculateBoundingBox(player.getTruePosition(), player.isFacingLeft())
									.contains(victim.getTruePosition()) && victim.getBuffedValue(400021003)) {
								victim.cancelEffectFromBuffStat(SecondaryStat.IndiePmdR, effect.getSourceId());
								victim.cancelEffectFromBuffStat(SecondaryStat.IndieBooster, effect.getSourceId());
							}
						}
					}
				}
			}
		}
	}

	public static void InhumanSpeed(LittleEndianAccessor slea, MapleClient c) {
		int objectId = slea.readInt();
		slea.readInt();
		if (c.getPlayer().getBuffedValue(400031020) || c.getPlayer().getBuffedValue(400031021)) {
			MapleAtom atom = new MapleAtom(false, c.getPlayer().getId(), 31, true,
					c.getPlayer().getBuffedValue(400031020) ? 400031020 : 400031021, 0, 0);
			atom.setDwFirstTargetId(objectId);
			atom.addForceAtom(new ForceAtom(1, 12, 15, 70, 0));
			c.getPlayer().getMap().spawnMapleAtom(atom);
		}
	}

	public static void CreateKinesisPsychicArea(LittleEndianAccessor rm, MapleClient c) {
		int nAction = rm.readInt();
		int ActionSpeed = rm.readInt();
		int PsychicAreaKey = rm.readInt();
		int LocalKey = rm.readInt();
		int SkillID = rm.readInt();
		short SLV = rm.readShort();
		int DurationTime = rm.readInt();
		byte second = rm.readByte();
		short SkeletonFieldPathIdx = rm.readShort();
		short SkeletonAniIdx = rm.readShort();
		short SkeletonLoop = rm.readShort();
		int mask8 = rm.readInt();
		int mask9 = rm.readInt();
		SecondaryStatEffect eff = SkillFactory.getSkill(SkillID).getEffect(SLV);
		eff.applyTo(c.getPlayer(), false);
		c.getPlayer().getMap()
				.broadcastMessage(CWvsContext.OnCreatePsychicArea(c.getPlayer().getId(), nAction, ActionSpeed, LocalKey,
						SkillID, SLV, PsychicAreaKey, DurationTime, second, SkeletonFieldPathIdx, SkeletonAniIdx,
						SkeletonLoop, mask8, mask9));
		if (SkillID != 142101009) {
			c.getPlayer().givePPoint(SkillID);
		}
		if (SkillID == 142121005) {
			c.getPlayer().setSkillCustomInfo(SkillID, 1L, 0L);
		}
		if (eff.getCooldown(c.getPlayer()) > 0) {
			c.getSession().writeAndFlush(CField.skillCooldown(SkillID, eff.getCooldown(c.getPlayer())));
			c.getPlayer().addCooldown(SkillID, System.currentTimeMillis(), eff.getCooldown(c.getPlayer()));
		}
	}

	public static void touchMist(LittleEndianAccessor slea, MapleClient c) {
		MapleCharacter chr = c.getPlayer();
		if (chr == null || chr.getMap() == null) {
			return;
		}
		slea.readByte();
		int objectId = slea.readInt();
		int skillId = slea.readInt();
		int x = slea.readInt(), y = slea.readInt();
		MapleMist mist = (MapleMist) chr.getMap().getMapObject(objectId, MapleMapObjectType.MIST);
		if (mist == null) {
			return;
		}
		switch (skillId) {
		case 162111000:
			SkillFactory.getSkill(80003059).getEffect(mist.getOwner().getSkillLevel(skillId)).applyTo(mist.getOwner(),
					chr);
			break;
		case 2311011:
			if (chr.getBuffedEffect(SecondaryStat.DebuffIncHp) == null) {
				if (c.getPlayer().getHolyPountinOid() != objectId) {
					c.getPlayer().setHolyPountin((byte) 0);
				} else {
					c.getPlayer().setHolyPountin((byte) (c.getPlayer().getHolyPountin() + 1));
				}
				c.getPlayer().addHP(c.getPlayer().getStat().getMaxHp() / 100L * mist.getSource().getX());
				c.getPlayer().setHolyPountinOid(objectId);
			}
			break;
		case 400051076:
			chr.getMap().removeMist(mist);
			if (chr.getSkillLevel(400051074) > 0) {
				SkillFactory.getSkill(400051077).getEffect(chr.getSkillLevel(400051074)).applyTo(chr, false);
			}
			break;
		case 2321015:
			chr.getMap().removeMist(mist);
			int plusduration = chr.getStat().getTotalInt() / 2500;
			c.getPlayer().addHP(c.getPlayer().getStat().getMaxHp() / 20
					+ ((c.getPlayer().getStat().getMaxHp() / 20) * plusduration));
		}
	}

	public static void UpdateJaguar(LittleEndianAccessor slea, MapleClient c) {
		slea.skip(4);
		int changed = slea.readInt();
		c.getPlayer().updateInfoQuest(123456, String.valueOf((changed + 1) * 10));
		c.getSession().writeAndFlush(CWvsContext.updateJaguar(c.getPlayer()));
		c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
	}

	public static void auraWeapon(LittleEndianAccessor slea, MapleClient c) {
		int skillid = slea.readInt();
	}

	public static void removeMist(LittleEndianAccessor slea, MapleClient c) {
		int skillid = slea.readInt();
		c.getPlayer().getMap().removeMist(skillid);
		c.getPlayer().cancelEffectFromBuffStat(SecondaryStat.IndiePadR, 80001455);
		c.getPlayer().cancelEffectFromBuffStat(SecondaryStat.IndieMadR, 80001455);
		if (skillid == 400031012) {
			ArrayList<Triple<Integer, Integer, Integer>> finalMobList = new ArrayList<Triple<Integer, Integer, Integer>>();
			c.getSession()
					.writeAndFlush((Object) CField.bonusAttackRequest(skillid + 1, finalMobList, true, 0, new int[0]));
		}
	}

	// 피스메이커 핸들러쪽
	public static void PeaceMaker(LittleEndianAccessor slea, MapleClient c) {
		int skillid = slea.readInt();
		byte i;
		slea.skip(8);
		Point pos1 = slea.readIntPos();
		Point pos2 = slea.readIntPos();
		int count = slea.readInt();
		SecondaryStatEffect effect = SkillFactory.getSkill(skillid).getEffect(c.getPlayer().getSkillLevel(skillid));
		int plus = SkillFactory.getSkill(400021070).getEffect(c.getPlayer().getSkillLevel(40021070)).getQ2()
				+ (slea.readInt()
						* SkillFactory.getSkill(400021070).getEffect(c.getPlayer().getSkillLevel(40021070)).getW2());
		/*
		 * for (i = 0; i < count; i = (byte) (i + 1)) { int chrid = slea.readInt(); if
		 * ((c.getPlayer()).peaceMaker > 0) { MapleCharacter target =
		 * c.getPlayer().getMap().getCharacterById(chrid); effect.applyTo(c.getPlayer(),
		 * target, false); } }
		 */
	}

	public static void PeaceMaker2(LittleEndianAccessor slea, MapleClient c) {
		int skillid = slea.readInt();
		c.getPlayer().getMap().broadcastMessage(c.getPlayer(), CField.skillCancel(c.getPlayer(), skillid), false);
	}

	public static void DemonFrenzy(MapleClient c) {
		if (c.getPlayer().getBuffedEffect(400011010) == null) {
			return;
		}
		SecondaryStatEffect Frenzy = c.getPlayer().getBuffedEffect(400011010);
		MapleFoothold fh = c.getPlayer().getMap().getFootholds().findBelow(c.getPlayer().getTruePosition());
		if (Frenzy == null || c.getPlayer().getSkillCustomValue(400010010) != null
				|| (c.getPlayer().getPosition()).y < fh.getY1() || !c.getPlayer().isAlive()) {
			return;
		}
		if (c.getPlayer().getStat().getHp() > c.getPlayer().getStat().getCurrentMaxHp() * Frenzy.getQ2() / 100L) {
			long hp = Frenzy.getY();
			if (c.getPlayer().getStat().getHp() - hp > 0L) {
				c.getPlayer().addHP(-hp, false, true);
			}
		}
		Point pos = new Point((c.getPlayer().getPosition()).x, (c.getPlayer().getPosition()).y);
		SecondaryStatEffect a = SkillFactory.getSkill(400010010).getEffect(c.getPlayer().getSkillLevel(400010010));
		Rectangle bounds = a.calculateBoundingBox(
				new Point((c.getPlayer().getTruePosition()).x, (c.getPlayer().getTruePosition()).y),
				c.getPlayer().isFacingLeft());
		MapleMist mist = new MapleMist(new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height), c.getPlayer(), a,
				5000, (byte) (c.getPlayer().isFacingLeft() ? 1 : 0));
		mist.setPosition(pos);
		mist.setDelay(0);
		c.getPlayer().getMap().spawnMist(mist, false);
		c.getPlayer().setSkillCustomInfo(400010010, 0L, 1000L);
		Frenzy.applyTo(c.getPlayer(), false);
	}

	public static void keydownSkillManagement(LittleEndianAccessor slea, MapleClient c) {
		int skillid = slea.readInt();
		MapleCharacter chr = c.getPlayer();
		if (chr == null) {
			return;
		}
		Map<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<>();
		if (skillid >= 3321034 && skillid <= 3321040) {
			SecondaryStatEffect eff = SkillFactory.getSkill(skillid)
					.getEffect(chr.getSkillLevel(GameConstants.getLinkedSkill(skillid)));
			chr.energy -= eff.getForceCon();
			if (chr.energy < 0) {
				chr.energy = 0;
			}
			statups.put(SecondaryStat.RelikGauge, new Pair<>(Integer.valueOf(chr.energy), Integer.valueOf(0)));
			c.getSession().writeAndFlush(CWvsContext.BuffPacket.giveBuff(statups, null, chr));
		} else if (skillid == 400021086) {
			Vmatrixstackbuff(c, true, null);
		} else if (chr.getBuffedEffect(SecondaryStat.GrandCrossSize, skillid) != null) {
			SecondaryStatEffect eff = chr.getBuffedEffect(skillid);
			chr.addHP(-((int) (chr.getStat().getHp() * eff.getT() / 100.0D)));
			Integer value = c.getPlayer().getBuffedValue(SecondaryStat.GrandCrossSize);
			if (c.getPlayer().getBuffLimit(400011072) <= 7000L && value.intValue() == 1) {
				chr.setBuffedValue(SecondaryStat.GrandCrossSize, 2);
				statups.put(SecondaryStat.GrandCrossSize,
						new Pair<>(Integer.valueOf(2), Integer.valueOf((int) chr.getBuffLimit(skillid))));
				statups.put(SecondaryStat.Speed,
						new Pair<>(Integer.valueOf(-60), Integer.valueOf((int) chr.getBuffLimit(skillid))));
				c.getSession().writeAndFlush(CWvsContext.BuffPacket.giveBuff(statups, eff, chr));
				c.getPlayer().getMap().broadcastMessage(c.getPlayer(),
						CWvsContext.BuffPacket.giveForeignBuff(c.getPlayer(), statups, eff), false);
			}
		} else if (skillid == 63121008) {
			SecondaryStatEffect eff = SkillFactory.getSkill(skillid).getEffect(chr.getSkillLevel(skillid));
			chr.addMP(-(chr.getStat().getCurrentMaxMp(chr) / 100L * eff.getMPRCon()));
			if (chr.getSkillLevel(63120039) > 0) {
				eff = SkillFactory.getSkill(63120039).getEffect(chr.getSkillLevel(63120039));
				chr.addHP(chr.getStat().getCurrentMaxHp() / 100L * eff.getX());
			}
		} else if (skillid == 3321038) {
			SecondaryStatEffect eff = SkillFactory.getSkill(3321039).getEffect(chr.getSkillLevel(3321039));
			SkillFactory.getSkill(3321039).getEffect(chr.getSkillLevel(3321039)).applyTo(chr);
			chr.getClient().getSession().writeAndFlush(CField.rangeAttack(skillid,
					Arrays.asList(new RangeAttack(3321039, chr.getTruePosition(), 1, 330, 1))));

		}
	}

	public static void subSkillEffect(LittleEndianAccessor slea, MapleCharacter chr) {
		int newx = slea.readInt();
		int newy = slea.readInt();
		int oldx = slea.readInt();
		int oldy = slea.readInt();
		int delay = slea.readInt();
		int skillId = slea.readInt();
		int unk = slea.readInt();
		byte facingleft = slea.readByte();
		slea.skip(4);
		int objectId = slea.readInt();
		slea.skip(2);
		int num = -1;
		if (slea.available() >= 8L) {
			slea.readInt();
			num = slea.readInt();
		}
		Skill skill = SkillFactory.getSkill(GameConstants.getLinkedSkill(skillId));
		if (chr == null || skill == null || chr.getMap() == null) {
			return;
		}
		byte skilllevel_serv = (byte) chr.getTotalSkillLevel(skill);
		if (skillId == 400031003 || skillId == 400031004) {
			SecondaryStatEffect yoyo = SkillFactory.getSkill(400031003).getEffect(1);
			if (chr.getSkillCustomValue(400031333) == null) {
				chr.setSkillCustomInfo(400031333, 0L, (yoyo.getX() * 1000));
			}
			if (chr.getSkillCustomValue(400031334) != null) {
				chr.getMap().broadcastMessage(CField.removeProjectile((int) chr.getSkillCustomValue0(400031334)));
				chr.removeSkillCustomInfo(400031334);
			}
			if (chr.getSkillCustomValue(400031334) == null) {
				chr.setSkillCustomInfo(400031334, objectId, 10000L);
			}
			Vmatrixstackbuff(chr.getClient(), true, slea, (skillId == 400031003) ? 1 : 2);
		} else if (skillId == 400031036) {
			chr.setSkillCustomInfo(400031036, 1L, 0L);
			MapleCharacter.렐릭게이지(chr.getClient(), skillId);
		} else if (skillId == 400031067) {
			chr.setSkillCustomInfo(400031067, 1L, 0L);
			MapleCharacter.렐릭게이지(chr.getClient(), skillId);
		} else if (skillId == 61111100 || skillId == 61111218 || skillId == 61111113) {
			if (chr.getSkillCustomValue(61111100) == null) {
				chr.setSkillCustomInfo(61111100, objectId, 0L);
			} else if (chr.getSkillCustomValue(61111110) == null) {
				chr.setSkillCustomInfo(61111110, objectId, 0L);
			} else if (chr.getSkillCustomValue(61111100) != null && chr.getSkillCustomValue(61111110) != null) {
				chr.getMap().broadcastMessage(CField.removeProjectile((int) chr.getSkillCustomValue0(61111100)));
				chr.setSkillCustomInfo(61111100, chr.getSkillCustomValue0(61111110), 0L);
				chr.setSkillCustomInfo(61111110, objectId, 0L);
			}
		}
		chr.getMap().broadcastMessage(chr, CField.showProjectileEffect(chr, newx, newy, delay, skillId, skilllevel_serv,
				unk, facingleft, objectId, num), false);
		if (skillId != 14111024 && skillId != 14101028) {
			chr.getClient().getSession().writeAndFlush(CField.EffectPacket.showEffect(chr, 0, skillId, 1, 0, 0,
					(byte) (chr.isFacingLeft() ? 1 : 0), true, chr.getTruePosition(), null, null));
		}
		if (!GameConstants.isLinkMap(chr.getMapId())) {
			chr.getMap().broadcastMessage(chr, CField.EffectPacket.showEffect(chr, 0, skillId, 1, 0, 0,
					(byte) (chr.isFacingLeft() ? 1 : 0), false, chr.getTruePosition(), null, null), false);
		}
		if (skill.getEffect(skilllevel_serv).getCooldown(chr) > 0 && chr.getCooldownLimit(skillId) == 0L) {
			chr.giveCoolDowns(skillId, System.currentTimeMillis(), skill.getEffect(skilllevel_serv).getCooldown(chr));
			chr.getClient().getSession()
					.writeAndFlush(CField.skillCooldown(skillId, skill.getEffect(skilllevel_serv).getCooldown(chr)));
		}
		if (skillId == 64101002) {
			chr.wingDagger = true;
		}
		skill.getEffect(skilllevel_serv).applyTo(chr, false);
	}

	public static void cancelSubEffect(LittleEndianAccessor slea, MapleCharacter chr) {
		int objid = slea.readInt();
		chr.getMap().broadcastMessage(chr, CField.removeProjectileEffect(chr.getId(), objid), false);
		slea.readByte();
		int skillid = slea.readInt();
		if (skillid == 61111100) {
			if (chr.getSkillCustomValue(61111100) != null) {
				if (chr.getSkillCustomValue0(61111100) == objid) {
					chr.removeSkillCustomInfo(61111100);
				}
			} else if (chr.getSkillCustomValue(61111110) != null && chr.getSkillCustomValue0(61111110) == objid) {
				chr.removeSkillCustomInfo(61111110);
			}
		} else if (skillid == 400031036 || skillid == 400031067) {
			SkillFactory.getSkill(3311009).getEffect(chr.getSkillLevel(3311009)).applyTo(chr);
		}
	}

	public static void changeSubEffect(LittleEndianAccessor slea, MapleCharacter chr) {
		chr.getMap().broadcastMessage(chr, CField.updateProjectileEffect(chr.getId(), slea.readInt(), slea.readInt(),
				slea.readInt(), slea.readInt(), slea.readByte()), false);
	}

	public static void LinkofArk(LittleEndianAccessor slea, MapleCharacter player) {
	}

	public static void FlowOfFight(MapleCharacter player) {
		if (player != null) {
			if (player.getSkillLevel(80000268) > 0) {
				player.FlowofFight = Math.min(6, player.FlowofFight + 1);
				SkillFactory.getSkill(80000268).getEffect(player.getSkillLevel(80000268)).applyTo(player, false);
			} else if (player.getSkillLevel(150000017) > 0) {
				player.FlowofFight = Math.min(6, player.FlowofFight + 1);
				SkillFactory.getSkill(150000017).getEffect(player.getSkillLevel(150000017)).applyTo(player, false);
			}
		}
	}

	public static void TowerChair(LittleEndianAccessor slea, MapleClient c) {
		List<Integer> chairs = new ArrayList<>();
		for (int a = 0; a < 6; a++) {
			int val = slea.readInt();
			if (val == 0) {
				break;
			}
			chairs.add(Integer.valueOf(val));
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < chairs.size(); i++) {
			sb.append(i);
			sb.append('=');
			sb.append(chairs.get(i));
			if (i != chairs.size() - 1) {
				sb.append(';');
			}
		}
		c.getPlayer().updateInfoQuest(7266, sb.toString());
		c.getSession().writeAndFlush(SLFCGPacket.TowerChairSaveDone());
	}

	public static void HandleCellClick(int number, MapleClient c) {
		if (c.getPlayer().getBingoGame().getRanking().contains(c.getPlayer()))
			;
		int[][] table = c.getPlayer().getBingoGame().getTable(c.getPlayer());
		c.getSession().writeAndFlush(SLFCGPacket.BingoCheckNumber(number));
		int jj = 0;
		for (int y = 0; y < 5; y++) {
			for (int k = 0; k < 5; k++) {
				if (table[k][y] == number) {
					table[k][y] = 255;
				}
			}
		}
		int temp = 0;
		for (int j = 0; j < 5; j++) {
			for (int k = 0; k < 5; k++) {
				if (table[k][j] == 255 || table[k][j] == 0) {
					temp++;
				}
			}
			if (temp == 5) {
				c.getSession().writeAndFlush(SLFCGPacket.BingoDrawLine(j * 5, 0, number));
			}
			temp = 0;
		}
		temp = 0;
		for (int x = 0; x < 5; x++) {
			for (int k = 0; k < 5; k++) {
				if (table[x][k] == 255 || table[x][k] == 0) {
					temp++;
				}
			}
			if (temp == 5) {
				c.getSession().writeAndFlush(SLFCGPacket.BingoDrawLine(x, 1, number));
			}
			temp = 0;
		}
		int crossCnt = 0;
		int rcrossCnt = 0;
		for (int i = 0; i < 5; i++) {
			if (table[i][i] == 255 || table[i][i] == 0) {
				crossCnt++;
			}
			if (table[i][4 - i] == 255 || table[i][4 - i] == 0) {
				rcrossCnt++;
			}
			if (crossCnt == 5) {
				c.getSession().writeAndFlush(SLFCGPacket.BingoDrawLine(1, 2, number));
			}
			if (rcrossCnt == 5) {
				c.getSession().writeAndFlush(SLFCGPacket.BingoDrawLine(1, 3, number));
			}
		}
	}

	public static void HandleHundredDetectiveGame(LittleEndianAccessor slea, MapleClient c) {
		String input = String.valueOf(slea.readInt());
		DetectiveGame game = c.getPlayer().getDetectiveGame();
		int asdf = game.getAnswer(c.getPlayer());
		String Answer = String.valueOf(c.getPlayer().getDetectiveGame().getAnswer(c.getPlayer()));
		int result = 0;
		for (int a = 0; a < 3; a++) {
			char inputchar = input.charAt(a);
			char answerchar = Answer.charAt(a);
			if (inputchar == answerchar) {
				result += 10;
			} else if (Answer.contains(String.valueOf(inputchar))) {
				result++;
			}
		}
		c.getPlayer().getDetectiveGame().addAttempt(c.getPlayer());
		c.getSession().writeAndFlush(SLFCGPacket.HundredDetectiveGameResult(Integer.valueOf(input).intValue(), result));
		if (result == 30) {
			c.getPlayer().getDetectiveGame().addRank(c.getPlayer());
		}
	}

	public static void HandlePlatformerEnter(LittleEndianAccessor slea, MapleClient c) {
		int Stage = slea.readInt();
		int Map = 993001000 + Stage * 10;
		c.getPlayer().warp(Map);
		c.getSession().writeAndFlush(CField.getClock(600));
		if (c.getPlayer().getPlatformerTimer() != null) {
			c.getPlayer().getPlatformerTimer().cancel(false);
		}
		ScheduledFuture<?> a = Timer.ShowTimer.getInstance().schedule(() -> {
			if (c.getPlayer().getMapId() == Map) {
				c.getPlayer().warp(993001000);
			}
			c.getPlayer().setPlatformerTimer((ScheduledFuture<?>) null);
		}, 600000L);
		c.getPlayer().setPlatformerTimer(a);
		c.getPlayer().setPlatformerStageEnter(System.currentTimeMillis());
		c.getSession().writeAndFlush(SLFCGPacket.PlatformerStageInfo(Stage));
		c.getSession().writeAndFlush(SLFCGPacket.playSE("Sound/MiniGame.img/multiBingo/start"));
		c.getSession().writeAndFlush(CField.environmentChange("event/start", 19));
		c.getSession().writeAndFlush(CField.UIPacket.closeUI(1112));
		c.getPlayer().setKeyValue(18838, "count", (c.getPlayer().getKeyValue(18838, "count") - 1L) + "");
		switch (Stage) {
		case 1:
			c.getSession().writeAndFlush(SLFCGPacket.CameraCtrl(13, new int[] { 0, 1000, 400, 0 }));
			c.getSession().writeAndFlush(SLFCGPacket.CameraCtrl(11, new int[] { 5 }));
			c.getSession().writeAndFlush(
					SLFCGPacket.OnYellowDlg(9070203, 2000, "대시는 가고 싶은 방향으로 방향키 연속! 두 번! 이다...후후...", ""));
			c.getSession().writeAndFlush(CField.enforceMSG("대시를 사용하여 골인 지점으로 가는거다!?", 215, 5000));
			break;
		case 2:
			c.getSession().writeAndFlush(SLFCGPacket.CameraCtrl(13, new int[] { 0, 1000, 400, 0 }));
			c.getSession().writeAndFlush(SLFCGPacket.CameraCtrl(11, new int[] { 5 }));
			c.getSession().writeAndFlush(SLFCGPacket.OnYellowDlg(9070201, 2000, "앞의 장애물은 대시 중 점프로 쉽게 넘을 수 있을거야!", ""));
			c.getSession().writeAndFlush(CField.enforceMSG("대시 중 점프를 하면 높이, 멀리 뛸 수 있어!", 214, 5000));
			break;
		case 3:
			c.getSession().writeAndFlush(SLFCGPacket.CameraCtrl(13, new int[] { 0, 1000, 400, 0 }));
			c.getSession().writeAndFlush(SLFCGPacket.CameraCtrl(11, new int[] { 5 }));
			c.getSession().writeAndFlush(
					SLFCGPacket.OnYellowDlg(9070201, 2000, "대시 중 방향키 위를 먼저 누르고 점프하면 더 쉬워! 편한 방식을 찾아 보자", ""));
			c.getSession().writeAndFlush(CField.enforceMSG("점프 중 방향키 위를 유지하면 높이 뛰어 오를 수 있어!", 214, 5000));
			break;
		case 4:
			c.getSession().writeAndFlush(SLFCGPacket.CameraCtrl(11, new int[] { 3 }));
			c.getSession().writeAndFlush(SLFCGPacket.CameraCtrl(13, new int[] { 0, 0, 0, 200 }));
			c.getSession()
					.writeAndFlush(SLFCGPacket.OnYellowDlg(9070202, 2000, "점프 중 방향키를 누르면 원하는 방향으로 공중 제어가 가능해.", ""));
			c.getSession().writeAndFlush(CField.enforceMSG("점프 중 좌우 방향키를 입력하면 공중에서 자세 제어가 가능해. 히힛.", 213, 5000));
			break;
		case 5:
			c.getSession()
					.writeAndFlush(SLFCGPacket.OnYellowDlg(9070201, 2000, "점프 중 방향키를 누르면 원하는 방향으로 공중 제어가 가능해.", ""));
			c.getSession().writeAndFlush(CField.enforceMSG("전에도 말했듯이 대시 중 방향키 위를 먼저 누르고 점프해도 괜찮아", 214, 5000));
			break;
		case 6:
			c.getSession().writeAndFlush(SLFCGPacket.CameraCtrl(11, new int[] { 3 }));
			c.getSession().writeAndFlush(SLFCGPacket.CameraCtrl(13, new int[] { 0, 0, 0, 200 }));
			c.getSession().writeAndFlush(SLFCGPacket.OnYellowDlg(9070200, 2000, "음... 더러운 것에 닿는다고 해도 죽는건 아니다...", ""));
			c.getSession().writeAndFlush(CField.enforceMSG("공중에서 방향키 좌우를 눌러 더러운 것을 피해라.", 212, 5000));
			break;
		case 7:
			c.getSession().writeAndFlush(SLFCGPacket.CameraCtrl(13, new int[] { 0, 1000, 400, 0 }));
			c.getSession().writeAndFlush(SLFCGPacket.CameraCtrl(11, new int[] { 5 }));
			break;
		case 8:
			NPCScriptManager.getInstance().start(c, "Obstacle");
			c.getSession().writeAndFlush(SLFCGPacket.CameraCtrl(15, new int[] { 1000, 600, 0 }));
			c.getSession().writeAndFlush(SLFCGPacket.CameraCtrl(13, new int[] { 0, 1000, 300, 0 }));
			c.getSession().writeAndFlush(SLFCGPacket.CameraCtrl(11, new int[] { 5 }));
			break;
		case 9:
			NPCScriptManager.getInstance().start(c, "FootHoldMove");
			c.getSession().writeAndFlush(SLFCGPacket.CameraCtrl(13, new int[] { 0, 1000, 400, 350 }));
			c.getSession().writeAndFlush(CField.enforceMSG("상하로 움직이는 발판을 이용해서 목적지에 도달해 봐. 히힛.", 213, 5000));
			c.getSession().writeAndFlush(
					SLFCGPacket.OnYellowDlg(9070202, 2000, "발판이 적절한 위치에 있을 때 점프하는게 좋을거야.\r\n막 뛰면 안 된다구.", ""));
			c.getSession().writeAndFlush(SLFCGPacket.OnYellowDlg(9070201, 2000, "점프는 예술과 기술이니까~", ""));
			break;
		case 10:
			c.getSession().writeAndFlush(CField.enforceMSG("상승하는 발판 위에서 더러운 것을 피해라.", 212, 5000));
			c.getSession()
					.writeAndFlush(SLFCGPacket.OnYellowDlg(9070200, 2000, "우선 아래에 보이는 긴 발판에 올라서 봐라. 그럼 움직일 거야.", ""));
			c.getSession().writeAndFlush(SLFCGPacket.OnYellowDlg(9070200, 2000, "그 뒤는 알아서 해라.", ""));
			break;
		case 11:
			c.getSession().writeAndFlush(CField.enforceMSG("점프 기술을 활용하여 목적지에 도달해 보자~", 214, 5000));
			c.getSession().writeAndFlush(
					SLFCGPacket.OnYellowDlg(9070201, 2000, "점프 중 위키를 잘 활용해 봐. 의외로 지름길도 있으니 잘 찾아가고.", ""));
			break;
		case 12:
			c.getSession().writeAndFlush(SLFCGPacket.CameraCtrl(13, new int[] { 0, 1000, 400, 0 }));
			c.getSession().writeAndFlush(SLFCGPacket.CameraCtrl(11, new int[] { 5 }));
			c.getSession().writeAndFlush(CField.enforceMSG("세 번째 발판에서 최대한 멀리 뛰어 봐~", 214, 5000));
			c.getSession()
					.writeAndFlush(SLFCGPacket.OnYellowDlg(9070201, 2000, "세 번째 발판에서 최대한 멀리 뛰어 봐. 15미터 이상이면 합격!", ""));
			NPCScriptManager.getInstance().start(c, "Obstacle2");
			break;
		case 13:
			c.getSession().writeAndFlush(CField.enforceMSG("점프 기술을 활용해서 더러운 것을 피해 나아가라.", 212, 5000));
			c.getSession().writeAndFlush(SLFCGPacket.OnYellowDlg(9070200, 2000, "장애물과 장애물의 중간 지점 쯤에서 점프해 봐라.", ""));
			c.getSession().writeAndFlush(SLFCGPacket.OnYellowDlg(9070200, 2000, "못 넘는 곳이 있으면 점프중 방향키 위를 잊지 마.", ""));
			break;
		case 14:
			c.getSession().writeAndFlush(CField.enforceMSG("상승하는 발판 위에서 더러운 것을 피해라.", 212, 5000));
			c.getSession().writeAndFlush(SLFCGPacket.OnYellowDlg(9070200, 2000, "알지? 아래의 긴 발판으로 내려가 봐.", ""));
			break;
		case 15:
			c.getSession().writeAndFlush(SLFCGPacket.CameraCtrl(15, new int[] { 1000, 600, 0 }));
			c.getSession().writeAndFlush(SLFCGPacket.CameraCtrl(13, new int[] { 0, 1000, 300, 0 }));
			c.getSession().writeAndFlush(SLFCGPacket.CameraCtrl(11, new int[] { 5 }));
			c.getSession().writeAndFlush(CField.enforceMSG("연속 대시 점프로 목적지에 도달하는거다!", 215, 5000));
			c.getSession().writeAndFlush(
					SLFCGPacket.OnYellowDlg(9070203, 2000, "대쉬 점프로 넘는거다!\r\n가끔은 연속 점프보다 잠깐 멈추는게 유리할 수도 있다!", ""));
			break;
		case 16:
			c.getSession().writeAndFlush(SLFCGPacket.CameraCtrl(11, new int[] { 3 }));
			c.getSession().writeAndFlush(SLFCGPacket.CameraCtrl(13, new int[] { 0, 0, 0, 200 }));
			c.getSession().writeAndFlush(CField.enforceMSG("더러운 것을 피해서 목적지에 도달해 봐. 히힛.", 213, 5000));
			c.getSession().writeAndFlush(
					SLFCGPacket.OnYellowDlg(9070202, 2000, "여긴 두 가지 방법이 있다. 낙하하며 좌우키를 번갈아가며 누르는 것.", ""));
			c.getSession()
					.writeAndFlush(SLFCGPacket.OnYellowDlg(9070202, 2000, "혹은 점프 중 반대 방향으로 힘을 주어 수직으로 떨어지는 것.", ""));
			c.getSession().writeAndFlush(SLFCGPacket.OnYellowDlg(9070202, 2000, "선택은 네 몫이야. 히힛.", ""));
			break;
		case 17:
			c.getSession().writeAndFlush(SLFCGPacket.CameraCtrl(13, new int[] { 0, 1000, 400, 0 }));
			c.getSession().writeAndFlush(SLFCGPacket.CameraCtrl(11, new int[] { 5 }));
			c.getSession().writeAndFlush(CField.enforceMSG("낙하하는 운석을 피해 골인 지점까지 도달하는거다!", 215, 5000));
			c.getSession()
					.writeAndFlush(SLFCGPacket.OnYellowDlg(9070203, 2000, "익숙한 곳일거다! 가끔은 과감히 맞으면서 돌파하는 것도 남자답지", ""));
			NPCScriptManager.getInstance().start(c, "Obstacle3");
			break;
		case 18:
			c.getSession().writeAndFlush(CField.enforceMSG("좌우의 곰을 30회 반복해서 터치하는거다!", 215, 5000));
			c.getSession().writeAndFlush(SLFCGPacket.OnYellowDlg(9070203, 2000, "좌우의 잠자는 곰에 반복해서 닿아라! 총 30회다!", ""));
			break;
		case 19:
			c.getSession().writeAndFlush(SLFCGPacket.CameraCtrl(13, new int[] { 0, 1000, 400, 0 }));
			c.getSession().writeAndFlush(SLFCGPacket.CameraCtrl(11, new int[] { 5 }));
			c.getSession().writeAndFlush(CField.enforceMSG("나무를 이용하여 목적지에 도달해 봐~", 214, 5000));
			c.getSession().writeAndFlush(
					SLFCGPacket.OnYellowDlg(9070201, 2000, "큰 나무를 조심해. 부딪히면 아프니까... 나무 위로 올라설 수 있다는 걸 명심해 둬.", ""));
			break;
		case 20:
			c.getSession().writeAndFlush(CField.enforceMSG("발판이 사라지는 숲을 돌파해 봐~", 214, 5000));
			c.getSession().writeAndFlush(SLFCGPacket.OnYellowDlg(9070201, 2000,
					"인간들은 인내의 숲이란 곳에서 수련을 한다며?\r\n그런데 너무 쉬운 것 같더라. 히히.\r\n발판이 사라지는 정도면 재밌지 않겠어?.", ""));
			break;
		case 21:
			c.getSession().writeAndFlush(CField.enforceMSG("장애물을 피해 상쾌하게 달려가는거야! 히힛.", 213, 5000));
			c.getSession().writeAndFlush(SLFCGPacket.OnYellowDlg(9070202, 2000,
					"큰 나무는 그냥 지나갈 수 없어.\r\n하지만 점프로 올라갈 수 있지. 높이 점프할 땐 새를 조심하라구.", ""));
			break;
		case 22:
			c.getSession().writeAndFlush(CField.enforceMSG("공중 제어를 활용하여 발판을 올라 보자. 히힛.", 213, 5000));
			c.getSession().writeAndFlush(
					SLFCGPacket.OnYellowDlg(9070202, 2000, "힌트를 주자면... 높고 길게 뛰어서 힘이 다할때쯤 뛴 반대 방향으로 돌아와. 꽤 어려울거야.", ""));
			break;
		case 23:
			c.getSession().writeAndFlush(SLFCGPacket.CameraCtrl(15, new int[] { 1000, 600, 0 }));
			c.getSession().writeAndFlush(SLFCGPacket.CameraCtrl(13, new int[] { 0, 1000, 300, 0 }));
			c.getSession().writeAndFlush(SLFCGPacket.CameraCtrl(11, new int[] { 5 }));
			c.getSession().writeAndFlush(CField.enforceMSG("종합적인 이동 능력을 시험해 봐라.", 212, 5000));
			c.getSession().writeAndFlush(
					SLFCGPacket.OnYellowDlg(9070200, 2000, "얼마나 수련이 잘 되었는지 확인해 봐라. 너무 괴로워서 울지도 모르겠군.", ""));
			break;
		case 24:
			c.getSession().writeAndFlush(SLFCGPacket.CameraCtrl(15, new int[] { 1000, 600, 0 }));
			c.getSession().writeAndFlush(SLFCGPacket.CameraCtrl(13, new int[] { 0, 1000, 300, 0 }));
			c.getSession().writeAndFlush(SLFCGPacket.CameraCtrl(11, new int[] { 5 }));
			c.getSession().writeAndFlush(CField.enforceMSG("닿으면 사라지는 발판을 재빠르게 넘어가 봐. 히힛.", 213, 5000));
			c.getSession().writeAndFlush(SLFCGPacket.OnYellowDlg(9070202, 2000, "1초 정도 발판에 머무르면 사라지니까 조심해. 히힛.", ""));
			break;
		case 25:
			c.getSession().writeAndFlush(SLFCGPacket.CameraCtrl(13, new int[] { 0, 1000, 400, 0 }));
			c.getSession().writeAndFlush(SLFCGPacket.CameraCtrl(11, new int[] { 5 }));
			c.getSession().writeAndFlush(CField.enforceMSG("연속 대시 점프로 장애물을 넘어가 봐~", 214, 5000));
			c.getSession().writeAndFlush(SLFCGPacket.OnYellowDlg(9070201, 2000, "너무 급하게 달려가지 말고 속도를 조절할 땐 조절해~", ""));
			break;
		case 26:
			c.getSession().writeAndFlush(SLFCGPacket.CameraCtrl(13, new int[] { 0, 1000, 400, 0 }));
			c.getSession().writeAndFlush(SLFCGPacket.CameraCtrl(11, new int[] { 5 }));
			c.getSession().writeAndFlush(CField.enforceMSG("모든 점프 기술을 활용하여 더러운 것을 피해 가는거다!", 215, 5000));
			c.getSession().writeAndFlush(SLFCGPacket.OnYellowDlg(9070203, 2000, "더러운 것에 당하느니 천천히 생각하면서 가라!", ""));
			break;
		case 27:
			c.getSession().writeAndFlush(SLFCGPacket.CameraCtrl(11, new int[] { 3 }));
			c.getSession().writeAndFlush(SLFCGPacket.CameraCtrl(13, new int[] { 0, 0, 0, 200 }));
			c.getSession().writeAndFlush(CField.enforceMSG("낙하 중 좌우 방향키로 공중 제어를 할 수 있다. 더러운 건 피해야지.", 212, 5000));
			c.getSession().writeAndFlush(SLFCGPacket.OnYellowDlg(9070200, 2000, "여긴... 음. 할 말이 없다. 하하.", ""));
			c.getSession().writeAndFlush(SLFCGPacket.OnYellowDlg(9070202, 2000, "실제로 돌파할 수 있긴 한거야?", ""));
			c.getSession().writeAndFlush(SLFCGPacket.OnYellowDlg(9070201, 2000, "한 사람이 있다고 하네요...", ""));
			break;
		case 28:
			c.getSession().writeAndFlush(CField.enforceMSG("점프 중 좌우 방향키로 소멸하는 발판을 돌파...해라.", 212, 5000));
			c.getSession().writeAndFlush(SLFCGPacket.OnYellowDlg(9070200, 2000, "나도 이런 정신나간 곳이 존재한다는게 믿기지 않는다.", ""));
			break;
		case 29:
			c.getSession().writeAndFlush(CField.enforceMSG("상승하는 발판에서 더러운 것을 피해라.", 212, 5000));
			c.getSession().writeAndFlush(SLFCGPacket.OnYellowDlg(9070200, 2000, "우선 아래에 있는 발판으로 내려가. 이 패턴 익숙하지?", ""));
			break;
		case 30:
			c.getSession().writeAndFlush(SLFCGPacket.CameraCtrl(15, new int[] { 1000, 600, 0 }));
			c.getSession().writeAndFlush(SLFCGPacket.CameraCtrl(13, new int[] { 0, 0, 0, 200 }));
			c.getSession().writeAndFlush(CField.enforceMSG("독수리를 피해 골인 지점까지 도달해라.", 212, 5000));
			c.getSession().writeAndFlush(SLFCGPacket.OnYellowDlg(9070200, 2000, "독수리는 보기 보다 판정 영역이 작다. 할 수 있겠지?", ""));
			break;
		case 31:
			c.getSession().writeAndFlush(CField.enforceMSG("점프대로 점프하고 공중에서 제어해 봐. 히힛.", 213, 5000));
			c.getSession()
					.writeAndFlush(SLFCGPacket.OnYellowDlg(9070202, 2000, "평범하게 생긴 발판도 밟으면 통통 튀어 오를 수 있다구. 히힛.", ""));
			c.getSession().writeAndFlush(SLFCGPacket.OnYellowDlg(9070202, 2000, "이 곳의 공중 제어는 조금 불쾌한 느낌일지도. 히힛.", ""));
			break;
		case 32:
			c.getSession().writeAndFlush(SLFCGPacket.CameraCtrl(13, new int[] { 0, 1000, 400, 350 }));
			c.getSession().writeAndFlush(CField.enforceMSG("발판 위에서 중심을 잘 잡으며 잘 피해봐. 꼭 피해야 해.", 214, 5000));
			c.getSession().writeAndFlush(SLFCGPacket.OnYellowDlg(9070201, 2000, "발판 안은 위험해.", ""));
			NPCScriptManager.getInstance().start(c, "FootHoldMove2");
			break;
		case 33:
			c.getSession().writeAndFlush(CField.enforceMSG("더러운 것을 공중에서 화려하게 피해 봐~", 214, 5000));
			c.getSession()
					.writeAndFlush(SLFCGPacket.OnYellowDlg(9070201, 2000, "상향 점프와 공중 제어를 잘 이용해야 해. 나처럼 섬세한 점프!", ""));
			break;
		case 34:
			c.getSession().writeAndFlush(SLFCGPacket.CameraCtrl(11, new int[] { 3 }));
			c.getSession().writeAndFlush(SLFCGPacket.CameraCtrl(13, new int[] { 0, 0, 0, 200 }));
			c.getSession().writeAndFlush(CField.enforceMSG("공중에서 잘 움직이는 것 뿐이다. 알아서 피하고 싶을거다", 212, 5000));
			c.getSession()
					.writeAndFlush(SLFCGPacket.OnYellowDlg(9070200, 2000, "세상엔 더러운 것도 위험한 것도 있다. 넌 이겨낼 수 있을거다.", ""));
			NPCScriptManager.getInstance().start(c, "Obstacle4");
			break;
		case 35:
			c.getSession().writeAndFlush(CField.enforceMSG("능력을 한 번 시험해 봐라.", 212, 5000));
			c.getSession()
					.writeAndFlush(SLFCGPacket.OnYellowDlg(9070200, 2000, "이 곳은 종합적인 능력을 시험하는 곳이지. 길을 따라 가면 된다.", ""));
			break;
		case 36:
			c.getSession().writeAndFlush(SLFCGPacket.CameraCtrl(13, new int[] { 0, 1000, 400, 350 }));
			c.getSession().writeAndFlush(SLFCGPacket.OnYellowDlg(9070203, 2000, "한 치의 실수도 용납되지 않는 곳이다. 잔인하군...", ""));
			NPCScriptManager.getInstance().start(c, "FootHoldMove3");
			break;
		case 37:
			c.getSession().writeAndFlush(SLFCGPacket.CameraCtrl(15, new int[] { 1000, 600, 0 }));
			c.getSession().writeAndFlush(SLFCGPacket.CameraCtrl(13, new int[] { 0, 0, 0, 200 }));
			c.getSession().writeAndFlush(CField.enforceMSG("점프대를 이용해서 공중 자세 제어를 해 봐.", 212, 5000));
			c.getSession().writeAndFlush(SLFCGPacket.OnYellowDlg(9070201, 2000, "여기를 지나갈 수 있다면 트리플 악셀도 가능할거야.", ""));
			break;
		case 38:
			c.getSession().writeAndFlush(SLFCGPacket.CameraCtrl(15, new int[] { 1000, 600, 0 }));
			c.getSession().writeAndFlush(SLFCGPacket.CameraCtrl(13, new int[] { 0, 1000, 300, 0 }));
			c.getSession().writeAndFlush(SLFCGPacket.CameraCtrl(11, new int[] { 5 }));
			c.getSession().writeAndFlush(CField.enforceMSG("이동하는 발판 위에서 공중 제어 점프로 화려하게 피해 봐라.", 212, 5000));
			c.getSession().writeAndFlush(SLFCGPacket.OnYellowDlg(9070200, 2000, "앞에 보이는 발판에 올라서면 발판이 움직일거다.", ""));
			break;
		case 39:
			c.getSession().writeAndFlush(CField.enforceMSG("상승하는 발판 위에서 더러운 것을 피해내라.", 212, 5000));
			c.getSession().writeAndFlush(SLFCGPacket.OnYellowDlg(9070200, 2000, "일단 아래의 발판으로 내려가는건 알지?", ""));
			break;
		case 40:
			c.getSession().writeAndFlush(CField.enforceMSG("종합 시험이다. 멘탈 붕괴에 유의해라.", 212, 5000));
			c.getSession().writeAndFlush(SLFCGPacket.OnYellowDlg(9070202, 2000, "이 스테이지가 이렇게 어렵습니다~ 히힛.", ""));
			break;
		}
	}

	public static void HandlePlatformerExit(LittleEndianAccessor slea, MapleClient c) {
		switch (slea.readByte()) {
		case 12:
			if (c.getPlayer().getPlatformerTimer() != null) {
				c.getPlayer().getPlatformerTimer().cancel(false);
				c.getPlayer().setPlatformerTimer((ScheduledFuture<?>) null);
			}
			c.getPlayer().setPlatformerStageEnter(0L);
			c.getPlayer().warp(993001000);
			return;
		case 18:
			c.removeClickedNPC();
			NPCScriptManager.getInstance().start(c, 2007, "union_rade");
			return;
		}
	}

	public static void HandleResolution(LittleEndianAccessor slea, MapleClient c) {
		switch (slea.readByte()) {
		case 6:
			c.getPlayer().setResolution(1920, 1200);
			return;
		case 5:
			c.getPlayer().setResolution(1920, 1080);
			return;
		case 4:
			c.getPlayer().setResolution(1280, 720);
			return;
		case 3:
			c.getPlayer().setResolution(1366, 768);
			return;
		case 2:
			c.getPlayer().setResolution(1024, 768);
			return;
		case 1:
			c.getPlayer().setResolution(800, 600);
			return;
		}
		c.disconnect(true, false, false);
		c.getSession().close();
	}

	public static void ExitSpecialGame(MapleClient c) throws SQLException {
		long temp, temp2;
		switch (c.getPlayer().getMapId()) {
		case 921172300:
			c.getPlayer().warp(921172400);
			c.getSession().writeAndFlush(CField.environmentChange("Map/Effect2.img/event/gameover", 16));
			return;
		case 921171000:
			c.getPlayer().warp(921171100);
			temp = Long.valueOf(c.getPlayer().getKeyValue(15901, "stage")).longValue();
			temp2 = Long.valueOf(c.getPlayer().getKeyValue(15901, "selectedStage")).longValue();
			if (temp == temp2) {
				c.getSession().writeAndFlush(CField.environmentChange("Map/Effect2.img/event/gameover", 16));
			} else if (temp > temp2) {
				c.getSession().writeAndFlush(CField.environmentChange("Map/Effect3.img/hungryMuto/Clear", 16));
				c.getSession().writeAndFlush(SLFCGPacket.DreamBreakerMsg((temp - 1L) + "스테이지 클리어!"));
			}
			DreamBreakerRank.EditRecord(c.getPlayer().getName(),
					Long.valueOf(c.getPlayer().getKeyValue(15901, "best")).longValue(),
					Long.valueOf(c.getPlayer().getKeyValue(15901, "besttime")).longValue());
			return;
		case 921172000:
		case 921172100:
			for (MapleMonster mon : c.getPlayer().getMap().getAllMonster()) {
				if (mon.getOwner() == c.getPlayer().getId()) {
					c.getPlayer().getMap().killMonster(mon, -1);
				}
			}
			c.getPlayer().setUnionEndTime(System.currentTimeMillis());
			if (c.getPlayer().getMapId() == 921172000) {
				c.getPlayer().warp(921172200);
			} else {
				c.getPlayer().warp(921172201);
			}
			return;
		case 993192600:
			c.getPlayer().warp(993192501);
			return;
		case 450001400:
			c.getPlayer().warp(450001550);
			c.send(CField.environmentChange("Map/Effect.img/killing/fail", 16));
			return;
		case 993194500:
			c.getPlayer().warp(993194401);
			c.send(CField.environmentChange("Map/Effect.img/killing/fail", 16));
			return;
		}
		c.getPlayer().dropMessageGM(6, "該按鈕將在ExitSpecial Game中處理.");
	}

	public static void HandleDreamBreakerSkill(MapleClient c, int SkillId) {
		try {
			int dream = (int) c.getPlayer().getKeyValue(15901, "dream");
			EventInstanceManager em = c.getPlayer().getEventInstance();
			if (em == null) {
				return;
			}
			switch (SkillId) {
			case 0:
				if (dream >= 200) {
					c.getPlayer().setKeyValue(15901, "dream", String.valueOf(dream - 200));
					c.getSession().writeAndFlush(SLFCGPacket.DreamBreakerMsg("게이지 홀드! 5초동안 게이지의 이동이 멈춥니다!"));
					em.setProperty("gaugeHold", "true");
					Timer.MapTimer.getInstance().schedule(() -> em.setProperty("gaugeHold", "false"), 5000L);
					break;
				}
				c.getSession().writeAndFlush(SLFCGPacket.DreamBreakerMsg("드림 포인트가 부족하여 스킬을 사용할 수 없습니다."));
				break;
			case 1:
				if (dream >= 300) {
					c.getPlayer().setKeyValue(15901, "dream", String.valueOf(dream - 300));
					List<MapleMonster> Orgels = new ArrayList<>();
					for (MapleMonster m : c.getPlayer().getMap().getAllMonster()) {
						if (m.getId() >= 9833080 && m.getId() <= 9833084) {
							Orgels.add(m);
						}
					}
					if (Orgels.size() > 0) {
						c.getPlayer().getMap().killMonster(Orgels.get(Randomizer.nextInt(Orgels.size())), c.getPlayer(),
								false, false, (byte) 1);
						c.getSession().writeAndFlush(SLFCGPacket.DreamBreakerMsg("자각의 종소리를 울려 한 곳의 오르골이 깨어났습니다!"));
						break;
					}
					c.getSession().writeAndFlush(SLFCGPacket.DreamBreakerMsg("모든 오르골이 이미 깨어있는 상태입니다."));
					break;
				}
				c.getSession().writeAndFlush(SLFCGPacket.DreamBreakerMsg("드림 포인트가 부족하여 스킬을 사용할 수 없습니다."));
				break;
			case 2:
				if (dream >= 400) {
					c.getPlayer().setKeyValue(15901, "dream", String.valueOf(dream - 400));
					c.getSession().writeAndFlush(SLFCGPacket.DreamBreakerMsg("꿈속의 헝겊인형이 소환되어 몬스터들을 도발합니다!"));
					MapleMonster m = MapleLifeFactory.getMonster(9833100);
					m.setHp(m.getStats().getHp());
					m.getStats().setHp(m.getStats().getHp());
					c.getPlayer().getMap().spawnMonsterOnGroundBelow(m, c.getPlayer().getPosition());
					break;
				}
				c.getSession().writeAndFlush(SLFCGPacket.DreamBreakerMsg("드림 포인트가 부족하여 스킬을 사용할 수 없습니다."));
				break;
			case 3:
				if (dream >= 900) {
					c.getPlayer().setKeyValue(15901, "dream", String.valueOf(dream - 900));
					c.getSession().writeAndFlush(SLFCGPacket.DreamBreakerMsg("숙면의 오르골을 공격하던 모든 몬스터가 사라졌습니다!"));
					for (MapleMonster m : c.getPlayer().getMap().getAllMonster()) {
						switch (m.getId()) {
						case 9833070:
						case 9833071:
						case 9833072:
						case 9833073:
						case 9833074:
						case 9833080:
						case 9833081:
						case 9833082:
						case 9833083:
						case 9833084:
						case 9833100:
							continue;
						}
						c.getPlayer().getMap().killMonster(m, c.getPlayer(), false, false, (byte) 1);
					}
					em.setProperty("stopSpawn", "true");
					Timer.MapTimer.getInstance().schedule(() -> em.setProperty("stopSpawn", "false"), 10000L);
					break;
				}
				c.getSession().writeAndFlush(SLFCGPacket.DreamBreakerMsg("드림 포인트가 부족하여 스킬을 사용할 수 없습니다."));
				break;
			}
			c.getSession().writeAndFlush(SLFCGPacket.DreamBreakeLockSkill(SkillId));
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			c.getSession().writeAndFlush(SLFCGPacket.DreamBreakeSkillRes());
		}
	}

	public static void HandleBingoClick(MapleClient c) {
		if (c.getPlayer().getBingoGame().getBingoTimer() == null
				|| c.getPlayer().getBingoGame().getBingoTimer().isCancelled()) {
			return;
		}
		c.getPlayer().getBingoGame().addRank(c.getPlayer());
	}

	public static void ExitBlockGame(LittleEndianAccessor rh, MapleClient c) {
		c.getSession().writeAndFlush(SLFCGPacket.BlockGameCommandPacket(3));
		c.getPlayer().setBlockCount(0);
		Timer.ShowTimer.getInstance().schedule(() -> {
			c.getSession().writeAndFlush(CField.UIPacket.IntroDisableUI(false));
			c.getSession().writeAndFlush(CField.UIPacket.IntroLock(false));
			ChannelServer cserv = c.getChannelServer();
			MapleMap target = cserv.getMapFactory().getMap(993017000);
			c.getPlayer().changeMap(target, target.getPortal(0));
			c.getPlayer().gainItem(4310185, c.getPlayer().getBlockCoin());
			c.getPlayer().setBlockCoin(0);
		}, 3500L);
	}

	public static void HandleBlockGameRes(LittleEndianAccessor rh, MapleClient c) {
		byte type = rh.readByte();
		if (type == 3) {
			c.getSession().writeAndFlush(SLFCGPacket.BlockGameCommandPacket(3));
			c.getPlayer().setBlockCount(0);
			Timer.ShowTimer.getInstance().schedule(() -> {
				c.getSession().writeAndFlush(CField.UIPacket.IntroDisableUI(false));
				c.getSession().writeAndFlush(CField.UIPacket.IntroLock(false));
				ChannelServer cserv = c.getChannelServer();
				MapleMap target = cserv.getMapFactory().getMap(993017000);
				c.getPlayer().changeMap(target, target.getPortal(0));
				c.getPlayer().gainItem(4310184, c.getPlayer().getBlockCoin());
				c.getPlayer().setBlockCoin(0);
			}, 3500L);
		} else {
			c.getPlayer().addBlockCoin((type == 2) ? 2 : 1);
			int block = c.getPlayer().getBlockCount() + 1;
			c.getPlayer().setBlockCount(block);
			if (block % 10 == 0) {
				int velocity = 100 + block / 10 * 30;
				int misplaceallowance = 1 + block / 10;
				switch (block) {
				case 70:
					c.getSession().writeAndFlush(SLFCGPacket.WeatherAddPacket(1));
					c.getSession().writeAndFlush(CField.musicChange("Bgm45/Time Is Gold"));
					break;
				case 100:
					c.getSession().writeAndFlush(CField.musicChange("Bgm45/Demian Spine"));
					break;
				case 120:
					c.getSession().writeAndFlush(SLFCGPacket.WeatherRemovePacket(1));
					c.getSession().writeAndFlush(SLFCGPacket.WeatherAddPacket(2));
					break;
				}
				c.getSession().writeAndFlush(SLFCGPacket.BlockGameControlPacket(velocity, misplaceallowance));
			}
		}
	}

	public static final void GuideWarp(LittleEndianAccessor slea, MapleCharacter chr) {
		byte type = slea.readByte();

		switch (type) {
		case 0x00: // 빠른이동
			chr.warp(slea.readInt());
			break;
		case 0x01:
			break;
		case 0x02:
			break;
		case 0x03: // 빠른이동 저장
			int size = slea.readInt();
			for (int i = 0; i < size; i++) {
				slea.readInt(); // 저장된 맵 타입 (아직안함)
			}
			break;
		case 0x04: // 길라잡이 보상
			int gift = slea.readInt();
			break;
		default:
			break;
		}
	}

	public static final void useMannequin(LittleEndianAccessor slea, MapleCharacter chr) {
		slea.skip(4);
		byte type = slea.readByte();
		byte result = slea.readByte();
		byte slot = slea.readByte();
		byte temp = -1;
		int itemId = 0;
		boolean second = false;
		if (slea.available() < 4L) {
			temp = slea.readByte();
			if (slea.available() >= 1L) {
				second = (slea.readByte() == 1);
			}
		} else if (slea.available() == 4L) {
			itemId = slea.readInt();
		}
		if (GameConstants.isAngelicBuster(chr.getJob())) {
			second = chr.getDressup();
		}
		if (GameConstants.isZero(chr.getJob())) {
			second = (chr.getGender() == 1 && chr.getSecondGender() == 0);
		}
		int[] banhair = { 30070, 30071, 30072, 30073, 30074, 30075, 30076, 30077, 30080, 30081, 30082, 30083, 30084,
				30085, 30086, 30087 };
		if (type == 0) {
			if (result == 1) {
				if (itemId == 5680222) {
					chr.getHairRoom().add(new MapleMannequin(0, -1, 0, 0));
					chr.removeItem(itemId, -1);
					chr.getClient().getSession().writeAndFlush(
							CWvsContext.mannequin(type, result, (byte) 3, (byte) chr.getHairRoom().size(), null));
					chr.getClient().getSession()
							.writeAndFlush(CWvsContext.mannequin(type, result, (byte) 5, slot, null));
				}
			} else if (result == 2) {
				MapleMannequin hair = chr.getHairRoom().get(slot);
				for (int h : banhair) {
					if (chr.getHair() == h || (second && chr.getSecondHair() == h)) {
						chr.dropMessage(1, "無法保存此髮型.");
						return;
					}
				}
				hair.setValue(second ? chr.getSecondHair() : chr.getHair());
				hair.setBaseProb(second ? chr.getSecondBaseProb() : chr.getBaseProb());
				hair.setBaseColor(second ? chr.getSecondBaseColor() : chr.getBaseColor());
				hair.setAddColor(second ? chr.getSecondAddColor() : chr.getAddColor());
				chr.getClient().getSession().writeAndFlush(CWvsContext.mannequin(type, result, (byte) 2, slot, hair));
				chr.getClient().getSession().writeAndFlush(CWvsContext.mannequinRes(type, result, 1));
			} else if (result == 3) {
				MapleMannequin hair = chr.getHairRoom().get(slot);
				hair.setValue(0);
				hair.setBaseProb(-1);
				hair.setBaseColor(0);
				hair.setAddColor(0);
				chr.getClient().getSession().writeAndFlush(CWvsContext.mannequin(type, result, (byte) 2, slot, hair));
				chr.getClient().getSession().writeAndFlush(CWvsContext.mannequinRes(type, result, 1));
			} else if (result == 4) {
				MapleMannequin hair = chr.getHairRoom().get(slot);
				int oldHair = second ? chr.getSecondHair() : chr.getHair();
				int mBaseProb = second ? chr.getSecondBaseProb() : chr.getBaseProb();
				int mBaseColor = second ? chr.getSecondBaseColor() : chr.getBaseColor();
				int mAddColor = second ? chr.getSecondAddColor() : chr.getAddColor();
				if (second) {
					chr.setSecondHair(hair.getValue());
					chr.setSecondBaseProb(hair.getBaseProb());
					chr.setSecondBaseColor(hair.getBaseColor());
					chr.setSecondAddColor(hair.getAddColor());
					chr.updateSingleStat(MapleStat.HAIR, chr.getHair());
				} else {
					chr.setHair(hair.getValue());
					chr.setBaseProb(hair.getBaseProb());
					chr.setBaseColor(hair.getBaseColor());
					chr.setAddColor(hair.getAddColor());
					chr.updateSingleStat(MapleStat.HAIR, chr.getHair());
				}
				hair.setValue(oldHair);
				hair.setBaseProb(mBaseProb);
				hair.setBaseColor(mBaseColor);
				hair.setAddColor(mAddColor);
				chr.getClient().getSession().writeAndFlush(CWvsContext.mannequin(type, result, (byte) 2, slot, hair));
				chr.getClient().getSession().writeAndFlush(CWvsContext.enableActions(chr));
				chr.getClient().getSession().writeAndFlush(CWvsContext.mannequin(type, result, (byte) 4, slot, null));
				chr.equipChanged();
			}
		} else if (type == 1) {
			if (result == 1) {
				if (itemId == 5680222) {
					chr.getFaceRoom().add(new MapleMannequin(0, -1, -1, 0));
					chr.removeItem(itemId, -1);
					chr.getClient().getSession().writeAndFlush(
							CWvsContext.mannequin(type, result, (byte) 3, (byte) chr.getFaceRoom().size(), null));
					chr.getClient().getSession()
							.writeAndFlush(CWvsContext.mannequin(type, result, (byte) 5, slot, null));
				}
			} else if (result == 2) {
				MapleMannequin face = chr.getFaceRoom().get(slot);
				face.setValue(second ? chr.getSecondFace() : chr.getFace());
				chr.getClient().getSession().writeAndFlush(CWvsContext.mannequin(type, result, (byte) 2, slot, face));
				chr.getClient().getSession().writeAndFlush(CWvsContext.mannequinRes(type, result, 1));
			} else if (result == 3) {
				MapleMannequin face = chr.getFaceRoom().get(slot);
				face.setValue(0);
				face.setBaseProb(-1);
				face.setBaseColor(0);
				face.setAddColor(0);
				chr.getClient().getSession().writeAndFlush(CWvsContext.mannequin(type, result, (byte) 2, slot, face));
				chr.getClient().getSession().writeAndFlush(CWvsContext.mannequinRes(type, result, 1));
			} else if (result == 4) {
				MapleMannequin face = chr.getFaceRoom().get(slot);
				int oldFace = second ? chr.getSecondFace() : chr.getFace();
				if (second) {
					chr.setSecondFace(face.getValue());
					chr.updateSingleStat(MapleStat.FACE, chr.getSecondFace());
				} else {
					chr.setFace(face.getValue());
					chr.updateSingleStat(MapleStat.FACE, chr.getFace());
				}
				face.setValue(oldFace);
				chr.getClient().getSession().writeAndFlush(CWvsContext.mannequin(type, result, (byte) 2, slot, face));
				chr.getClient().getSession().writeAndFlush(CWvsContext.enableActions(chr));
				chr.getClient().getSession().writeAndFlush(CWvsContext.mannequin(type, result, (byte) 4, slot, null));
				chr.equipChanged();
			}
		} else if (type == 2) {
			if (result == 1) {
				if (itemId == 5680222) {
					chr.getSkinRoom().add(new MapleMannequin(0, -1, -1, 0));
					chr.removeItem(itemId, -1);
					chr.getClient().getSession().writeAndFlush(
							CWvsContext.mannequin(type, result, (byte) 3, (byte) chr.getSkinRoom().size(), null));
					chr.getClient().getSession()
							.writeAndFlush(CWvsContext.mannequin(type, result, (byte) 5, slot, null));
				}
			} else if (result == 2) {
				MapleMannequin skin = chr.getSkinRoom().get(slot);
				int value = second ? chr.getSecondSkinColor() : chr.getSkinColor();
				skin.setValue(value + 12000);
				chr.getClient().getSession().writeAndFlush(CWvsContext.mannequin(type, result, (byte) 2, slot, skin));
				chr.getClient().getSession().writeAndFlush(CWvsContext.mannequinRes(type, result, 1));
			} else if (result == 3) {
				MapleMannequin skin = chr.getSkinRoom().get(slot);
				skin.setValue(0);
				skin.setBaseProb(-1);
				skin.setBaseColor(0);
				skin.setAddColor(0);
				chr.getClient().getSession().writeAndFlush(CWvsContext.mannequin(type, result, (byte) 2, slot, skin));
				chr.getClient().getSession().writeAndFlush(CWvsContext.mannequinRes(type, result, 1));
			} else if (result == 4) {
				MapleMannequin skin = chr.getSkinRoom().get(slot);
				int oldSkin = second ? chr.getSecondSkinColor() : chr.getSkinColor();
				if (second) {
					chr.setSecondSkinColor((byte) (skin.getValue() - 12000));
					chr.updateSingleStat(MapleStat.SKIN, chr.getSecondSkinColor());
				} else {
					chr.setSkinColor((byte) (skin.getValue() - 12000));
					chr.updateSingleStat(MapleStat.SKIN, chr.getSkinColor());
				}
				skin.setValue(oldSkin);
				chr.getClient().getSession().writeAndFlush(CWvsContext.mannequin(type, result, (byte) 2, slot, skin));
				chr.getClient().getSession().writeAndFlush(CWvsContext.enableActions(chr));
				chr.getClient().getSession().writeAndFlush(CWvsContext.mannequin(type, result, (byte) 4, slot, null));
				chr.equipChanged();
			}
		}
	}

	public static void UseChooseAbility(LittleEndianAccessor slea, MapleClient c) {
		slea.skip(4);

		byte type = slea.readByte();

		if (c.getPlayer().innerCirculator == null) {
			return;
		}

		if (type == 1) {
			slea.readInt();

			int preset = slea.readByte();

			for (InnerSkillValueHolder inner : c.getPlayer().getInnerSkills().get(preset)) {
				c.getPlayer().changeSkillLevel(inner.getSkillId(), (byte) 0, (byte) 0);
			}

			c.getPlayer().getInnerSkills().get(preset).clear();

			for (InnerSkillValueHolder inner : c.getPlayer().innerCirculator) {
				c.getPlayer().getInnerSkills().get(preset).add(inner);
				c.getPlayer().changeSkillLevel(inner.getSkillId(), inner.getSkillLevel(), inner.getMaxLevel());
			}

			c.getSession().writeAndFlush(CField.updateInnerAbility(c.getPlayer().getInnerSkills().get(preset), preset));
		}

		c.getPlayer().innerCirculator = null;
	}

	public static void EnterDungen(LittleEndianAccessor mplew, MapleClient c) {
		String d = mplew.readMapleAsciiString();
		NPCScriptManager.getInstance().start(c, ServerConstants.csNpc, "EnterDungeon", d);
	}

	public static void ICBM(LittleEndianAccessor slea, MapleClient c) {
		slea.skip(4);
		int skill = slea.readInt();
		slea.skip(4);
		SecondaryStatEffect eff = SkillFactory.getSkill(skill).getEffect(c.getPlayer().getSkillLevel(skill));
		if (eff.getCooldown(c.getPlayer()) > 0 && c.getPlayer().getCooldownLimit(skill) == 0L) {
			c.getPlayer().addCooldown(skill, System.currentTimeMillis(), eff.getCooldown(c.getPlayer()));
			c.getSession().writeAndFlush(CField.skillCooldown(skill, eff.getCooldown(c.getPlayer())));
			if (GameConstants.isLinkedSkill(skill)
					&& c.getPlayer().getCooldownLimit(GameConstants.getLinkedSkill(skill)) == 0L) {
				c.getPlayer().addCooldown(GameConstants.getLinkedSkill(skill), System.currentTimeMillis(),
						eff.getCooldown(c.getPlayer()));
				c.getSession().writeAndFlush(
						CField.skillCooldown(GameConstants.getLinkedSkill(skill), eff.getCooldown(c.getPlayer())));
			}
		}
		short size = slea.readShort();
		for (int i = 0; i < size; i++) {
			Rectangle poz = new Rectangle(slea.readInt(), slea.readInt(), slea.readInt(), slea.readInt());
			MapleMist mist = new MapleMist(poz, c.getPlayer(), eff, 1300,
					(byte) (c.getPlayer().isFacingLeft() ? 1 : 0));
			mist.setDelay(0);
		}
	}

	public static void DimentionSword(LittleEndianAccessor slea, MapleClient c) {
		int skillId = slea.readInt();
		SecondaryStatEffect eff = SkillFactory.getSkill(skillId).getEffect(c.getPlayer().getSkillLevel(skillId));
		if (eff.getCooldown(c.getPlayer()) > 0 && c.getPlayer().getCooldownLimit(skillId) == 0L) {
			c.getPlayer().addCooldown(skillId, System.currentTimeMillis(), eff.getCooldown(c.getPlayer()));
			c.getSession().writeAndFlush(CField.skillCooldown(skillId, eff.getCooldown(c.getPlayer())));
			if (GameConstants.isLinkedSkill(skillId)
					&& c.getPlayer().getCooldownLimit(GameConstants.getLinkedSkill(skillId)) == 0L) {
				c.getPlayer().addCooldown(GameConstants.getLinkedSkill(skillId), System.currentTimeMillis(),
						eff.getCooldown(c.getPlayer()));
				c.getSession().writeAndFlush(
						CField.skillCooldown(GameConstants.getLinkedSkill(skillId), eff.getCooldown(c.getPlayer())));
			}
		}
		if (skillId == 400011090 && c.getPlayer().getBuffedValue(400011090)) {
			int duration = eff.getDuration();
			duration += (int) (c.getPlayer().getBuffedStarttime(SecondaryStat.IndieSummon, 400011090).longValue()
					- System.currentTimeMillis());
			duration /= 5;
			c.getPlayer().cancelEffectFromBuffStat(SecondaryStat.IndieSummon, 400011090);
			SkillFactory.getSkill(400011102).getEffect(c.getPlayer().getSkillLevel(400011090)).applyTo(c.getPlayer(),
					false, duration);
		} else if (skillId == 400051046) {
			for (MapleSummon summon : c.getPlayer().getMap().getAllSummonsThreadsafe()) {
				if (summon.getSkill() == 400051046 && c.getPlayer().getId() == summon.getOwner().getId()) {
					summon.setSpecialSkill(true);
					summon.setLastAttackTime(System.currentTimeMillis());
					c.getPlayer().getClient().getSession().writeAndFlush(CField.SummonPacket.DeathAttack(summon, 10));
					break;
				}
			}
		}
	}

	public static void cancelAfter(LittleEndianAccessor slea, MapleClient c) {
		int skillid = slea.readInt();
		byte type = slea.readByte();
		MapleCharacter chr = c.getPlayer();
		chr.getMap().broadcastMessage(chr, CField.skillCancel(chr, skillid), false);
		Skill skill = SkillFactory.getSkill(skillid);
		if (skillid == 400051041 && type > 0) {
			chr.cancelEffect(chr.getBuffedEffect(skillid));
			skill.getEffect(chr.getSkillLevel(400051041)).applyTo(chr, false);
		}
		// 에인션트 아스트라
		if (skillid == 3321038 && type > 0) {
			// chr.cancelEffect(chr.getBuffedEffect(skillid));
			c.getPlayer().getMap().broadcastMessage(c.getPlayer(), CField.skillCancel(c.getPlayer(), skillid), false);
			skill.getEffect(chr.getSkillLevel(3321038)).applyTo(chr, false);
		}
		if (skillid == 14121004 || skillid == 400021061) {
			if (chr.getBuffedEffect(skillid) != null) {
				chr.cancelEffect(chr.getBuffedEffect(skillid));
			}
		} else if (skillid == 151121004 || skillid == 162121022) {
			if (c.getPlayer().getSkillCustomValue0(skillid) > 0L) {
				SecondaryStatEffect effect = SkillFactory.getSkill(skillid)
						.getEffect(c.getPlayer().getSkillLevel(skillid));
				c.getPlayer().removeCooldown(skillid);
				double cooldown = c.getPlayer().getSkillCustomValue0(skillid) * 3.5D * 1000.0D;
				c.getPlayer().addCooldown(skillid, System.currentTimeMillis(),
						(effect.getCooldown(c.getPlayer()) - (int) cooldown));
				c.getSession().writeAndFlush(
						CField.skillCooldown(skillid, effect.getCooldown(c.getPlayer()) - (int) cooldown));
				c.getPlayer().removeSkillCustomInfo(skillid);
				if (skillid == 162121022 && c.getPlayer().getBuffedValue(162120038)) {
					c.getPlayer().cancelEffect(c.getPlayer().getBuffedEffect(162120038), (List<SecondaryStat>) null,
							true);
					SkillFactory.getSkill(162120038).getEffect(1).applyTo(c.getPlayer(), false, 1000);
				}
			}
		} else if (skillid == 164121042) {
			SecondaryStatEffect effect = SkillFactory.getSkill(skillid).getEffect(c.getPlayer().getSkillLevel(skillid));
			double cooldown = c.getPlayer().getSkillCustomValue0(164121042) * effect.getT() * 1000.0D;
			c.getPlayer().removeCooldown(skillid);
			c.getPlayer().addCooldown(skillid, System.currentTimeMillis(),
					(effect.getCooldown(c.getPlayer()) - (int) cooldown));
			c.getSession()
					.writeAndFlush(CField.skillCooldown(skillid, effect.getCooldown(c.getPlayer()) - (int) cooldown));
			c.getPlayer().removeSkillCustomInfo(164121042);
		}
		if (skill.isChargeSkill()) {
			chr.setKeyDownSkill_Time(0L);
		}
	}

	public static void autoSkill(LittleEndianAccessor slea, MapleClient c) {
		final MapleCharacter character = c.getPlayer();
		int skillid = slea.readInt();

		character.setAutoSkill(skillid, !character.isAutoSkill(skillid));

	}

	public static void PoisonNova(LittleEndianAccessor slea, MapleClient c) {
		List<Integer> novas = new ArrayList<>();
		int size = slea.readInt();
		for (int i = 0; i < size; i++) {
			novas.add(Integer.valueOf(slea.readInt()));
		}
		c.getPlayer().setPosionNovas(novas);
	}

	public static void useMoonGauge(MapleClient c) {// mush will
		if (c.getPlayer().getMapId() == 450008150 || c.getPlayer().getMapId() == 450008750) {
			String name = c.getPlayer().getTruePosition().y > -1000 ? "ptup" : "ptdown";
			c.getPlayer().setMoonGauge(Math.max(0, c.getPlayer().getMoonGauge() - 45));
			c.getSession().writeAndFlush(MobPacket.BossWill.addMoonGauge(c.getPlayer().getMoonGauge()));
			c.getSession().writeAndFlush(MobPacket.BossWill.teleport());
			c.getSession().writeAndFlush(CField.portalTeleport(name));
		} else if (c.getPlayer().getMapId() == 450008250 || c.getPlayer().getMapId() == 450008850) {

			c.getPlayer().cancelEffectFromBuffStat(SecondaryStat.DebuffIncHp);

			c.getPlayer().setMoonGauge(Math.max(0, c.getPlayer().getMoonGauge() - 50));
			c.getSession().writeAndFlush(MobPacket.BossWill.addMoonGauge(c.getPlayer().getMoonGauge()));
			c.getSession().writeAndFlush(MobPacket.BossWill.cooldownMoonGauge(7000));

			Timer.ShowTimer.getInstance().schedule(() -> {
				SkillFactory.getSkill(80002404).getEffect(1).applyTo(c.getPlayer(), true);
			}, 7000);
		} else if (c.getPlayer().getMapId() == 450008350 || c.getPlayer().getMapId() == 450008950) {

			c.getPlayer().setMoonGauge(Math.max(0, c.getPlayer().getMoonGauge() - 5));
			c.getPlayer().clearWeb = 2;
			c.getSession().writeAndFlush(MobPacket.BossWill.addMoonGauge(c.getPlayer().getMoonGauge()));
			c.getSession().writeAndFlush(MobPacket.BossWill.cooldownMoonGauge(5000));

			Timer.ShowTimer.getInstance().schedule(() -> {
				c.getPlayer().clearWeb = 0;
			}, 5000);
		}
	}

	public static void touchSpider(LittleEndianAccessor slea, MapleClient c) {
		SpiderWeb web = (SpiderWeb) c.getPlayer().getMap().getMapObject(slea.readInt(), MapleMapObjectType.WEB);
		if (c.getPlayer().clearWeb > 0) {
			try {
				c.getPlayer().getMap().broadcastMessage(MobPacket.BossWill.willSpider(false, web));
				c.getPlayer().getMap().removeMapObject(web);
				--c.getPlayer().clearWeb;
			} catch (Throwable throwable) {
			}
		} else if (c.getPlayer().getBuffedValue(SecondaryStat.NotDamaged) == null
				&& c.getPlayer().getBuffedValue(SecondaryStat.IndieNotDamaged) == null && c.getPlayer().isAlive()) {
			c.send(CField.DamagePlayer2((int) (c.getPlayer().getStat().getCurrentMaxHp() / 100L) * 30));
			c.getPlayer().setSkillCustomInfo(8880302, 0L, 5000L);
			if (!c.getPlayer().hasDisease(SecondaryStat.Seal)) {
				MobSkill ms1 = MobSkillFactory.getMobSkill(120, 40);
				ms1.setDuration(5000L);
				c.getPlayer().giveDebuff(SecondaryStat.Seal, ms1);
			}
		}
	}

	public static void SkillToCrystal(LittleEndianAccessor slea, MapleClient c) {
		int skillId = slea.readInt();
		MapleSummon summon = c.getPlayer().getSummon(152101000);
		int attack = 0;
		int max = 0;
		if (summon == null) {
			return;
		}
		if (skillId == 152001001 || skillId == 152120001 || skillId == 152120002 || skillId == 152121004) {
			if (c.getPlayer().getSkillLevel(152110001) > 0) {
				attack = 152110001;
			} else if (c.getPlayer().getSkillLevel(152100001) > 0) {
				attack = 152100001;
			}
		} else if (skillId == 152001002 || skillId == 152120003) {
			if (c.getPlayer().getSkillLevel(152110002) > 0) {
				attack = 152110002;
			} else if (c.getPlayer().getSkillLevel(152100002) > 0) {
				attack = 152100002;
			}
		}
		max = c.getPlayer().getSkillLevel(152110008) <= 0 ? 30 : 150;
		if (skillId == 152001001 || skillId == 152120001 || skillId == 152120002) {
			summon.setEnergy(Math.min(max,
					summon.getEnergy() + (c.getPlayer().getBuffedValue(SecondaryStat.FastCharge) != null ? 2 : 1)));
		} else if (skillId == 152121004) {
			summon.setEnergy(Math.min(max,
					summon.getEnergy() + (c.getPlayer().getBuffedValue(SecondaryStat.FastCharge) != null ? 6 : 3)));
		} else if (skillId == 152001002 || skillId == 152120003) {
			summon.setEnergy(Math.min(max,
					summon.getEnergy() + (c.getPlayer().getBuffedValue(SecondaryStat.FastCharge) != null ? 4 : 2)));
		}
		int cristalLevel = 152110008;
		if (c.getPlayer().getSkillLevel(152120014) > 0) {
			cristalLevel = 152120014;
		}
		if (summon.getEnergy() >= 150 && !c.getPlayer().getBuffedValue(cristalLevel)) {
			SkillFactory.getSkill(cristalLevel).getEffect(c.getPlayer().getSkillLevel(cristalLevel))
					.applyTo(c.getPlayer());
		}
		SecondaryStatEffect attackEff = SkillFactory.getSkill(attack).getEffect(c.getPlayer().getSkillLevel(attack));
		if (!c.getPlayer().skillisCooling(attack)) {
			c.getPlayer().addCooldown(attack, System.currentTimeMillis(), attackEff.getCooldown(c.getPlayer()));
			c.getSession().writeAndFlush((Object) CField.skillCooldown(attack, attackEff.getCooldown(c.getPlayer())));
			if (attack == 152110001) {
				c.getPlayer().getMap().broadcastMessage(CField.SummonPacket.specialSummon2(summon, attack));
			}
			c.getPlayer().getMap().broadcastMessage(CField.SummonPacket.ElementalRadiance(summon, 3));
		}
		if (summon.getEnergy() >= 30 && summon.getCrystalSkills().size() == 0
				|| summon.getEnergy() >= 60 && summon.getCrystalSkills().size() == 1
				|| summon.getEnergy() >= 90 && summon.getCrystalSkills().size() == 2
				|| summon.getEnergy() >= 150 && summon.getCrystalSkills().size() == 3) {
			summon.getCrystalSkills().add(true);
			c.getPlayer().getMap().broadcastMessage(CField.SummonPacket.transformSummon(summon, 2));
			c.getPlayer().getMap().broadcastMessage(CField.SummonPacket.ElementalRadiance(summon, 2));
			c.getPlayer().getMap().broadcastMessage(CField.SummonPacket.specialSummon(summon, 3));
		} else {
			c.getPlayer().getMap().broadcastMessage(CField.SummonPacket.ElementalRadiance(summon, 2));
			c.getPlayer().getMap().broadcastMessage(CField.SummonPacket.specialSummon(summon, 2));
		}
		c.getPlayer().getMap().broadcastMessage(CField.SummonPacket.ElementalRadiance(summon, 3));
	}

	public static void buffFreezer(LittleEndianAccessor slea, MapleClient c) {
		int buffFreezer;
		slea.skip(4);
		boolean use = (slea.readByte() == 1);
		if (c.getPlayer().itemQuantity(5133000) > 0) {
			buffFreezer = 5133000;
		} else {
			buffFreezer = 5133001;
		}
		if (use) {
			c.getPlayer().setUseBuffFreezer(true);
			c.getPlayer().removeItem(buffFreezer, -1);
		}
		c.getSession().writeAndFlush(CField.buffFreezer(buffFreezer, use));
	}

	public static void quickSlot(LittleEndianAccessor slea, MapleClient c) {
		int i = 0;
		if (c.getPlayer() != null) {
			while (slea.available() >= 4L) {
				c.getPlayer().setKeyValue(333333, "quick" + i, String.valueOf(slea.readInt()));
				i++;
			}
		}
	}

	public static void unlockTrinity(MapleClient c) {
		if (GameConstants.isAngelicBuster(c.getPlayer().getJob()) && c.getPlayer().getSkillLevel(65121101) > 0) {
			// [SEX] 주석 한줄제거 퀵슬롯
			c.send(CField.unlockSkill());
			c.send(CField.EffectPacket.showNormalEffect(c.getPlayer(), 49, true));
			c.getPlayer().getMap().broadcastMessage(c.getPlayer(),
					CField.EffectPacket.showNormalEffect(c.getPlayer(), 49, false), false);
		}
	}

	public static void checkCoreSecondpw(LittleEndianAccessor slea, MapleClient c) {
		int type = slea.readInt();
		if (type == 1 || c.SecondPwUse == 1 || type == 0 || type == 2) {
			c.getSession().writeAndFlush(CWvsContext.openCore());
		} else {
			String secondpw = slea.readMapleAsciiString();
			if (c.CheckSecondPassword(secondpw)) {
				c.getSession().writeAndFlush(CWvsContext.openCore());
			}
		}
	}

	public static void inviteChair(LittleEndianAccessor slea, MapleClient c) {
		int targetId = slea.readInt();
		MapleCharacter target = c.getPlayer().getMap().getCharacterById(targetId);
		if (target != null) {
			c.getSession().writeAndFlush(CField.inviteChair(7));
			target.getClient().getSession().writeAndFlush(CField.requireChair(c.getPlayer().getId()));
		} else {
			c.getSession().writeAndFlush(CField.inviteChair(8));
		}
	}

	public static void resultChair(LittleEndianAccessor slea, MapleClient c) {
		int targetId = slea.readInt();
		int result = slea.readInt();
		if (result == 7) {
			MapleCharacter target = c.getPlayer().getMap().getCharacterById(targetId);
			if (target != null) {
				c.getSession().writeAndFlush(CField.resultChair(target.getChair(), 0));
				MapleSpecialChair chair = null;
				for (MapleSpecialChair chairz : target.getMap().getAllSpecialChairs()) {
					if (chairz.getOwner().getId() == target.getId()) {
						chair = chairz;
						break;
					}
				}
				if (chair != null) {
					int[] randEmotions = { 2, 10, 14, 17 };
					chair.updatePlayer(c.getPlayer(), randEmotions[Randomizer.nextInt(randEmotions.length)]);
					c.getPlayer().setChair(target.getChair());
					c.getPlayer().getMap().broadcastMessage(c.getPlayer(),
							CField.showChair(c.getPlayer(), c.getPlayer().getChair()), false);
					c.getPlayer().getMap()
							.broadcastMessage(CField.specialChair(c.getPlayer(), true, false, true, chair));
				}
			} else {
				c.getSession().writeAndFlush(CField.resultChair(targetId, 1));
			}
		} else {
			c.getSession().writeAndFlush(CField.resultChair(targetId, 1));
		}
	}

	public static void bloodFist(LittleEndianAccessor slea, MapleClient c) {
		int skill = slea.readInt();
		c.getPlayer().addHP(-c.getPlayer().getStat().getHp() * Objects.requireNonNull(SkillFactory.getSkill(skill))
				.getEffect(c.getPlayer().getSkillLevel(skill)).getX() / 100L);
	}

	public static void updateMist(LittleEndianAccessor slea, MapleClient c) {
		int skillId = slea.readInt();
		int skillLevel = slea.readInt();
		Point pos = slea.readPos();
		if (skillId == 400031037) {
			skillId = 400031040;
		}
		SecondaryStatEffect effect = SkillFactory.getSkill(skillId).getEffect(skillLevel);
		for (MapleMist mist : c.getPlayer().getMap().getAllMistsThreadsafe()) {
			if (mist.getSource() != null && mist.getSource().getSourceId() == skillId) {
				c.getPlayer().getMap().broadcastMessage(CField.removeMist(mist));
				mist.setPosition(pos);
				mist.setBox(effect.calculateBoundingBox(pos, c.getPlayer().isFacingLeft()));
				c.getPlayer().getMap().broadcastMessage(CField.spawnMist(mist));
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer(), true, false));
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer(), false, true));
			}
		}
	}

	public static void BlesterSkill(LittleEndianAccessor slea, MapleClient c, boolean cancel) {
		int skillid = slea.readInt();
		SecondaryStatEffect eff = SkillFactory.getSkill(GameConstants.getLinkedSkill(skillid))
				.getEffect(c.getPlayer().getSkillLevel(GameConstants.getLinkedSkill(skillid)));
		if (c.getPlayer().getBuffedValue(SecondaryStat.RWCombination) == null) {
			(c.getPlayer()).acaneAim = 0;
		}
		if (skillid == 37121004 && cancel) {
			c.getPlayer().CylinderBuff(skillid, true);
			c.getPlayer().cancelEffectFromBuffStat(SecondaryStat.NotDamaged, 37121004);
		} else if (!cancel && (skillid == 37100002 || skillid == 37110001 || skillid == 37110004 || skillid == 37101000
				|| skillid == 37100002 || skillid == 37110001 || skillid == 37110004 || skillid == 37101000
				|| skillid == 37101001 || skillid == 37111003)) {
			if (skillid != 37101000 && skillid != 37101001 && skillid != 37111003) {
				if ((c.getPlayer()).bullet > 6) {

					(c.getPlayer()).bullet = 6;
				} else if ((c.getPlayer()).bullet < 0) {
					(c.getPlayer()).bullet = 0;
				}
				c.getPlayer().Cylinder(skillid);
			}
			if (skillid == 37100002 || skillid == 37110004) {
				eff.applyTo(c.getPlayer());
			}
			if (c.getPlayer().getSkillLevel(37110009) > 0) {
				if (c.getPlayer().getSkillLevel(37120012) > 0) {
					SkillFactory.getSkill(37120012).getEffect(c.getPlayer().getSkillLevel(37120012))
							.applyTo(c.getPlayer());
				} else {
					SkillFactory.getSkill(37110009).getEffect(c.getPlayer().getSkillLevel(37110009))
							.applyTo(c.getPlayer());
				}
			}
		}
	}

	public static void openHasteBox(LittleEndianAccessor slea, MapleCharacter chr) {
		int id;
		String[] boxIds;
		int items[][], item[];
		byte state = slea.readByte();
		switch (state) {
		case 0:
			id = slea.readInt();
			boxIds = new String[] { "M1", "M2", "M3", "M4", "M5", "M6" };
			if (chr.getKeyValue(500862, boxIds[id]) != 1L) {
				chr.dropMessage(1, "出現錯誤，請聯系GM.");
				return;
			}
			items = new int[][] { { 4001832, 500 }, { 4001126, 100 } };
			item = items[Randomizer.nextInt(items.length)];
			chr.setKeyValue(500862, "openBox", String.valueOf(chr.getKeyValue(500862, "openBox") + 1L));
			chr.setKeyValue(500862, "booster", String.valueOf(chr.getKeyValue(500862, "booster") + 1L));
			if (chr.getKeyValue(500862, "openBox") == 6L) {
				chr.setKeyValue(500862, "str", "오늘의 일일 미션을 모두 완료하였습니다!");
			} else {
				chr.setKeyValue(500862, "str", chr.getKeyValue(500862, "openBox") + "단계 상자 도전 중! 일일 미션 1개를 완료하세요!");
			}
			chr.getClient().getSession().writeAndFlush(
					CField.EffectPacket.showEffect(chr, item[0], item[1], 8, 0, 1, (byte) 0, true, null, null, null));
			chr.getClient().getSession().writeAndFlush(CField.NPCPacket.getNPCTalk(0, (byte) 0,
					"#b#e<헤이스트 상자>#n#k에서 #b#e#i" + item[0] + ":# #t" + item[0] + ":# " + item[1] + "개#n#k를 획득하였다!",
					"00 01", (byte) 57));
			break;
		}
	}

	public static void spotlightBuff(LittleEndianAccessor slea, MapleClient c) {
		MapleCharacter chr = c.getPlayer();
		if (chr == null) {
			return;
		}
		byte state = slea.readByte();
		int stack = slea.readInt();
		if (state == 1 && chr.getSkillLevel(400051018) > 0) {
			SkillFactory.getSkill(400051027).getEffect(chr.getSkillLevel(400051018)).applyTo(chr, false, stack);
		} else if (chr.getBuffedEffect(400051027) != null) {
			chr.cancelEffect(c.getPlayer().getBuffedEffect(400051027));
		}
	}

	public static void showICBM(LittleEndianAccessor slea, MapleCharacter player) {
		player.getMap().broadcastMessage(player, CField.showICBM(player.getId(), slea.readInt(), slea.readInt()),
				false);
	}

	public static void arkGauge(int readInt, MapleCharacter chr) {
		int gauge = readInt;

		if (chr != null) {
			chr.SpectorGauge = 1000;
			Map<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<>();
			statups.put(SecondaryStat.SpectorGauge, new Pair<>(Integer.valueOf(1), Integer.valueOf(0)));
			chr.getClient().getSession().writeAndFlush(CWvsContext.BuffPacket.giveBuff(statups, null, chr));
			return;
		}

		if (chr != null && GameConstants.isArk(chr.getJob())) {
			if (chr.getBuffedValue(155000007)) {
				if (!chr.getBuffedValue(400051334) || chr.gagenominus) {
					chr.SpectorGauge = Math.max(0, chr.SpectorGauge - 23);
					if (chr.SpectorGauge == 0) {
						chr.SpectorGauge = -1;
						Map<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<>();
						statups.put(SecondaryStat.SpectorGauge, new Pair<>(Integer.valueOf(1), Integer.valueOf(0)));
						chr.getClient().getSession().writeAndFlush(CWvsContext.BuffPacket.giveBuff(statups,
								chr.getBuffedEffect(SecondaryStat.SpectorGauge), chr));
						chr.addCooldown(155001008, System.currentTimeMillis(), 20000L);
						chr.getClient().getSession().writeAndFlush(CField.skillCooldown(155001008, 20000));
						chr.cancelEffect(SkillFactory.getSkill(155000007).getEffect(1));
						chr.SpectorGauge = 0;
					} else {
						Map<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<>();
						statups.put(SecondaryStat.SpectorGauge, new Pair<>(Integer.valueOf(1), Integer.valueOf(0)));
						chr.getClient().getSession().writeAndFlush(CWvsContext.BuffPacket.giveBuff(statups,
								chr.getBuffedEffect(SecondaryStat.SpectorGauge), chr));
					}
				}
			} else if (!chr.skillisCooling(155001008)) {
				int plus = 13;
				if (chr.getSkillLevel(155120034) > 0) {
					plus = (int) (plus + plus * 0.1D);
				}
				chr.SpectorGauge = Math.min(1000, gauge + plus);
				Map<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<>();
				statups.put(SecondaryStat.SpectorGauge, new Pair<>(Integer.valueOf(1), Integer.valueOf(0)));
				chr.getClient().getSession().writeAndFlush(CWvsContext.BuffPacket.giveBuff(statups, null, chr));
			}
		}
	}

	public static void quickPass(LittleEndianAccessor slea, MapleClient c) {
		int tt = slea.readInt();
		int type = slea.readInt();
		boolean left = (slea.readByteToInt() == 1);
		if (c.getPlayer().getInventory(MapleInventoryType.EQUIP).getNumFreeSlot() < 3) {
			c.getPlayer().dropMessage(1, "請至少空出3個裝備欄.");
			return;
		}
		if (c.getPlayer().getDonationPoint() < 1500L) {
			c.getPlayer().dropMessage(1, "贊助點數不足.");
			return;
		}
		if (left) {
			int j = Integer.parseInt(c.getPlayer().getV("arcane_quest_" + (type + 2))) + 1;
			int i = (int) (c.getPlayer().getKeyValue(39051, "c" + type) + 1L);
			c.getPlayer().addKV("arcane_quest_" + (type + 2), String.valueOf(j));
			c.getPlayer().setKeyValue(39051, "c" + type, String.valueOf(i));
		} else {
			int i = (int) (c.getPlayer().getKeyValue(39052, "c" + type) + 1L);
			switch (type) {
			case 0:
				c.getPlayer().dropMessage(1, "이용 불가능합니다.");
				return;
			case 1:
				c.getPlayer().addKV("muto", String.valueOf(Integer.parseInt(c.getPlayer().getV("muto")) + 1));
				c.getPlayer().setKeyValue(39052, "c1", String.valueOf(i));
				break;
			case 2:
				c.getPlayer().setKeyValue(20190131, "play",
						String.valueOf(c.getPlayer().getKeyValue(20190131, "play")) + '\001');
				c.getPlayer().setKeyValue(39052, "c2", String.valueOf(i));
				break;
			case 3:
				c.getPlayer().setKeyValue(16215, "play",
						String.valueOf(c.getPlayer().getKeyValue(16215, "play")) + '\001');
				c.getPlayer().setKeyValue(39052, "c3", String.valueOf(i));
				break;
			}
		}
		c.getPlayer().gainItem(1712001 + type, 1);
		c.getPlayer().gainItem(1712001 + type, 1);
		c.getPlayer().gainItem(1712001 + type, 1);
		c.getPlayer().gainDonationPoint(-1500);
		c.getPlayer().dropMessage(1, "已使用點數完成每日任務.");
	}

	public static void CannonBall(LittleEndianAccessor slea, MapleClient c) {
		int skillid = slea.readInt();
		int count = slea.readInt();
		HashMap<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
		SecondaryStatEffect cannonball = SkillFactory.getSkill(skillid).getEffect(1);
		statups.put(SecondaryStat.MiniCannonBall, new Pair<Integer, Integer>((int) count, 0));
		c.getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, cannonball, c.getPlayer()));
	}

	public static void selectDice(LittleEndianAccessor slea, MapleClient c) {
		int dice = slea.readInt();
		c.getPlayer().setDice(dice);
		SecondaryStatEffect effect = SkillFactory.getSkill(400051000)
				.getEffect(c.getPlayer().getTotalSkillLevel(400051000));
		effect.applyTo(c.getPlayer(), false, dice, true);
	}

	public static void battleStatistics(LittleEndianAccessor slea, MapleClient c) {
		c.getSession().writeAndFlush(CField.battleStatistics());
	}

	public static void goldCompleteByPass(MapleClient c) {
		try {
			if (Integer.parseInt(c.getCustomData(253, "day")) == Integer.parseInt(c.getCustomData(253, "cMaxDay"))) {
				c.getPlayer().dropMessage(1, "已完成所有日期的簽到.");
				return;
			}
			if (c.getPlayer().getDonationPoint() < 3000L) {
				c.getPlayer().dropMessage(1, "需要3000贊助點數.");
				return;
			}
			int value = (CurrentTime.getDay() == 6 || CurrentTime.getDay() == 7) ? 2 : 1;
			int k = Math.min(135, Integer.parseInt(c.getCustomData(253, "day")) + value);
			for (Triple<Integer, Integer, Integer> item : GameConstants.chariotItems) {
				if ((value == 1) ? (((Integer) item.left).intValue() == k)
						: (((Integer) item.left).intValue() == k || ((Integer) item.left).intValue() == k - 1)) {
					Item addItem;
					if (!MapleInventoryManipulator.checkSpace(c, ((Integer) item.mid).intValue(),
							((Integer) item.right).intValue(), "")) {
						c.getPlayer().dropMessage(1, "領取獎勵所需的背包空間不足.");
						c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
						return;
					}
					if (((Integer) item.mid).intValue() == 4310291) {
						c.getPlayer().dropMessage(1, "已獲得獎勵.");
						// c.getPlayer().AddStarDustCoin(1, ((Integer) item.right).intValue());
						break;
					}
					if (GameConstants.getInventoryType(((Integer) item.mid).intValue()) == MapleInventoryType.EQUIP
							|| GameConstants
									.getInventoryType(((Integer) item.mid).intValue()) == MapleInventoryType.CODY) {
						addItem = MapleItemInformationProvider.getInstance()
								.getEquipById(((Integer) item.mid).intValue());
					} else {
						int quantity = ((Integer) item.right).intValue();
						addItem = new Item(((Integer) item.mid).intValue(), (short) 0, (short) quantity);
					}
					if (addItem != null) {
						MapleInventoryManipulator.addbyItem(c, addItem);
						c.getPlayer().dropMessage(1, "已獲得獎勵.");
					}
					break;
				}
			}
			c.setCustomData(253, "complete", "1");
			c.setCustomData(253, "day", String.valueOf(k));
			int j = Integer.parseInt(c.getCustomData(253, "passCount")) - value;
			c.setCustomData(253, "passCount", String.valueOf(j));
			StringBuilder z = new StringBuilder(c.getCustomData(254, "passDate"));
			c.getPlayer().gainDonationPoint(-3000);
			if (value == 2) {
				c.setCustomData(254, "passDate", z.replace(k - 2, k - 1, "1").toString());
			}
			c.setCustomData(254, "passDate", z.replace(k - 1, k, "1").toString());
			c.getSession().writeAndFlush(CField.getGameMessage(18, "황금마차 골든패스를 사용했습니다."));
			c.getPlayer().dropMessage(1, "已完成簽到，蓋了" + value + "個印章.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void eventUIResult(LittleEndianAccessor slea, MapleClient c) {
		int objectId;
		short id;
		int typed, idx;
		short type = slea.readShort();
		int mapId = slea.readInt();
		switch (type) {
		case 53:
			c.removeClickedNPC();
			NPCScriptManager.getInstance().start(c, 2012041);
			break;
		case 17:
			c.removeClickedNPC();
			NPCScriptManager.getInstance().start(c, 2007, "union_raid");
			break;
		case 18:
			c.getPlayer().setKeyValue(18772, "id", String.valueOf(mapId));
			c.getPlayer().changeMap(921172100, 0);
			break;
		case 19: // 유니온 코인 수령
			c.removeClickedNPC();
			NPCScriptManager.getInstance().start(c, 2007, "union_raid");
			break;
		case 29:
			objectId = slea.readInt();
			idx = slea.readInt();
			break;
		case 38:
			id = slea.readShort();
			if (id == 0) {
				NPCScriptManager.getInstance().start(c, "hotelMaple");
				break;
			}
			if (id == 1) {
				idx = slea.readInt();
				int[][] items = { { 2631527, 20 }, { 2631878, 1 }, { 2430218, 2 } };
				if (items.length > idx && c.getPlayer().getKeyValue(501045, "lv") >= (idx + 1)) {
					if (c.getPlayer().getKeyValue(501045, "reward" + idx) == 0L) {
						if (MapleInventoryManipulator.checkSpace(c, items[idx][0], items[idx][1], "")) {
							c.getPlayer().setKeyValue(501045, "reward" + idx, "1");
							c.getPlayer().gainItem(items[idx][0], items[idx][1]);
							break;
						}
						c.getPlayer().dropMessage(1, "背包空間不足.");
						break;
					}
					c.getPlayer().dropMessage(1, "背包空間不足.");
				}
				break;
			}
			if (id == 2) {
				idx = slea.readInt();
				NPCScriptManager.getInstance().start(c, idx, "hotelMapleSkill");
			}
			break;
		case 41: {
			idx = slea.readInt();
			if (idx == 1200) {
				NPCScriptManager.getInstance().start(c, 9062632, "UI08"); // 후원
			}

			if (idx == 1300) {
				final MapleMap mapz = ChannelServer.getInstance(c.getChannel()).getMapFactory().getMap(120043000);
				c.getPlayer().setDeathCount((byte) 0);
				c.getPlayer().changeMap(mapz, mapz.getPortal(0));
				c.getPlayer().dispelDebuffs();
			}
			// 當前使用的快捷鍵
			if (idx == 1100) { // 上方選項欄
				NPCScriptManager.getInstance().start(c, 9062875, "9062875"); // 事件NPC
			} else if (idx == 1101) {
				NPCScriptManager.getInstance().start(c, 9062277, "9062277"); // 商店NPC
			} else if (idx == 1102) {
				NPCScriptManager.getInstance().start(c, 9063145, "9063145"); // 傳送NPC
			}
			if (idx == 1103) { // 上方選項欄
				NPCScriptManager.getInstance().start(c, 1530042, "1530042"); // 時裝搭配NPC
			} else if (idx == 1104) {
				NPCScriptManager.getInstance().start(c, 9062871, "1530050"); // 便利功能NPC
			} else if (idx == 1105) {
				NPCScriptManager.getInstance().start(c, 9062872, "9062872"); // 遊戲內容NPC
			}
			// c.getSession().writeAndFlush(CField.playSound("Sound/SoundEff.img/glory_POA/finish"));
			break;
		}
		case 46:
			if (c.getPlayer().getKeyValue(501229, "state") != 1L) {
				NPCScriptManager.getInstance().start(c, "NEO_Exploration1");
			}
			break;
		case 49:
			c.removeClickedNPC();
			NPCScriptManager.getInstance().dispose(c);
			c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
			typed = slea.readInt();
			if (typed == 0) {
				int selection = slea.readInt();
				c.getPlayer().setSkillCustomInfo(501378, selection, 0L);
				NPCScriptManager.getInstance().start(c, "BloomingForest_GiveItem");
				break;
			}
			if (typed == 1) {
				if (c.getPlayer().getKeyValue(501378, "tuto") != 1L) {
					NPCScriptManager.getInstance().start(c, "BloomingForest_SkillTuto");
					break;
				}
				int selection = slea.readInt();
				c.getPlayer().setSkillCustomInfo(501378, selection, 0L);
				NPCScriptManager.getInstance().start(c, "BloomingForest_Skill");
				break;
			}
			if (typed == 2) {
				NPCScriptManager.getInstance().start(c, "BloomingForest_GiveSun");
			}
			break;
		case 51:
			if (c.canClickNPC() && NPCScriptManager.getInstance().getCM(c) == null) {
				typed = slea.readInt();
				int selection = slea.readInt();
				c.getPlayer().setSkillCustomInfo(501378, selection, 0L);
				if (typed == 0) {
					NPCScriptManager.getInstance().start(c, "MapleLive_DailyQuest");
					break;
				}
				if (typed == 1) {
					NPCScriptManager.getInstance().start(c, "MapleLive_WeekQuest");
					break;
				}
				if (typed == 2) {
					NPCScriptManager.getInstance().start(c, "MapleLive_MonthQuest");
					break;
				}
				if (typed == 3) {
					NPCScriptManager.getInstance().start(c, "MapleLive_GiveItem");
				}
				break;
			}
			c.removeClickedNPC();
			NPCScriptManager.getInstance().dispose(c);
			c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
			break;
		case 64:
			int unk = slea.readInt();
			int number = slea.readInt();

			if (number == 0) {
				// NPCScriptManager.getInstance().start(c, 9062294); // 이벤트 모음
				// c.send(CField.UIPacket.closeUI(1334));
			} else if (number == 1) {
				NPCScriptManager.getInstance().start(c, 9062639); // 상점
				// c.send(CField.UIPacket.closeUI(1334));
			} else if (number == 2) {
				NPCScriptManager.getInstance().start(c, 9062294); // 이동
				// c.send(CField.UIPacket.closeUI(1334));
			} else if (number == 3) { // 코디
				NPCScriptManager.getInstance().start(c, 1052208); // 코디
				// c.send(CField.UIPacket.closeUI(1334));
			} else if (number == 4) {
				NPCScriptManager.getInstance().start(c, 9010044); // 편의
				// c.send(CField.UIPacket.closeUI(1334));
			} else if (number == 5) {
				NPCScriptManager.getInstance().start(c, 9062605); // 컨텐츠
				// c.send(CField.UIPacket.closeUI(1334));
			}
			break;
		}
		c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
	}

	public static void wiilMoon(LittleEndianAccessor slea, MapleClient c) { // mush will
		slea.skip(8);
		int state = slea.readInt();
		if (state == 1) {
			MapleMonster mob;
			MapleMist moon = null;
			for (MapleMist mi : c.getPlayer().getMap().getAllMistsThreadsafe()) {
				if (mi.getMobSkill() == null || mi.getMobSkill().getSkillId() != 242
						|| mi.getMobSkill().getSkillLevel() != 4) {
					continue;
				}
				moon = mi;
			}
			Point pos = new Point(slea.readInt(), slea.readInt());
			if (moon != null) {
				if (pos.y < 0 && moon.getBox().y == -2301) {
					return;
				}
				if (pos.y >= 0 && moon.getBox().y == -122) {
					return;
				}
			}
			if ((mob = c.getPlayer().getMap().getMonsterById(pos.y < 0 ? 8880304 : 8880303)) == null) {
				mob = c.getPlayer().getMap().getMonsterById(pos.y < 0 ? 8880344 : 8880343);
			}
			if (mob != null) {
				MapleMist mist = new MapleMist(new Rectangle(-204, pos.y < 0 ? -2301 : -122, 408, 300), mob,
						MobSkillFactory.getMobSkill(242, 4), 30000);
				if (pos.y < 0) {
					mist.setPosition(new Point(0, -2021));
				} else {
					mist.setPosition(new Point(0, 158));
				}
				c.getPlayer().getMap().spawnMist(mist, false);
			}
		}
		System.out.println("wiil moon");
	}

	public static void removeSecondAtom(LittleEndianAccessor slea, MapleClient c) {
		int size = slea.readInt();

		List<SecondAtom> removeSecondAtomList = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			int objectId = slea.readInt();
			slea.skip(4);
			if (c.getPlayer() != null && c.getPlayer().getMap() != null) {
				if (c.getPlayer().getSecondAtom(objectId) != null) {
					SecondAtom removeSecondAtom = c.getPlayer().removeSecondAtom(objectId);

					if (removeSecondAtom != null) {
						removeSecondAtomList.add(removeSecondAtom);
					}
				} else {
					c.getPlayer().getMap().removeSecondAtom(c.getPlayer(), objectId);
				}
			}
		}

		int respawnSkillId = 13141501;
		if (c.getPlayer().getBuffedValue(respawnSkillId)) { // 오리진 미스트랄 스프링 구현
			c.getPlayer().hexaSkillTimer = Timer.WorldTimer.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					if (c.getPlayer().getBuffedValue(respawnSkillId)) {
						for (SecondAtom sa : removeSecondAtomList) {
							Point point = c.getPlayer().getSummon(respawnSkillId).getPosition();

							sa.setPoint(new Point(point.x + Randomizer.rand(-400, 400),
									point.y + Randomizer.rand(-550, -50)));
						}

						c.getPlayer().spawnSecondAtom(removeSecondAtomList);
					}
				}
			}, 3000);
		}
	}

	public static void InfoSecondAtom(LittleEndianAccessor slea, MapleClient c) {
		int charid = slea.readInt();
		int type = slea.readInt();
		int objectId = slea.readInt();
		int info = slea.readInt();
		MapleSecondAtom atom = null;
		try {
			atom = c.getPlayer().getMap().getFindSecondAtoms(objectId);
		} catch (Exception exception) {
		}
		if (atom != null && atom.getSecondAtoms().getSourceId() == 63101006) {
			if (charid != info) {
				c.getPlayer().getMap().broadcastMessage(SkillPacket.AttackSecondAtom(c.getPlayer(), objectId, 1));
			} else if (charid == info) {
				atom.setLastAttackTime(System.currentTimeMillis());
			}
		}

	}

	public static void ropeConnect(LittleEndianAccessor slea, MapleClient c) {
		if (c.getPlayer() != null && c.getPlayer().getMap() != null) {
			int skillId = slea.readInt();
			short skilllv = slea.readShort();
			if (c.getPlayer().getSkillLevel(skillId) == skilllv) {
				if (skillId == 4221052) {
					int x = slea.readInt();
					int i = slea.readInt();
				} else if (slea.available() >= 12L) {
					int delay = slea.readInt();
					int x = slea.readInt();
					int y = slea.readInt();
					int unk = 0;
					if (slea.available() > 2L) {
						unk = slea.readInt();
					}
					Point pos = new Point(x, y);
					c.getPlayer().getMap().broadcastMessage(c.getPlayer(), CField.EffectPacket.showEffect(c.getPlayer(),
							delay, skillId, 1, unk, 0, (byte) 0, false, pos, "", null), false);
				}
			}
		}
	}

	public static void psychicUltimateRecv(LittleEndianAccessor slea, MapleClient c) {
		Skill skill = SkillFactory.getSkill(142121005);
		if (c.getPlayer() != null && c.getPlayer().getSkillLevel(142121005) > 0
				&& c.getPlayer().getSkillCustomValue0(142121005) == 1L) {
			c.getPlayer().removeSkillCustomInfo(142121005);
			SecondaryStatEffect effect = skill.getEffect(c.getPlayer().getSkillLevel(142121005));
			c.getPlayer().addCooldown(142121005, System.currentTimeMillis(), effect.getCooldown(c.getPlayer()));
			c.getSession().writeAndFlush(CField.skillCooldown(142121005, effect.getCooldown(c.getPlayer())));
		}
	}

	public static void warpGuildMap(LittleEndianAccessor slea, MapleCharacter player) {
		SimpleDateFormat sdf;
		if (player == null) {
			return;
		}
		int id = slea.readInt();
		switch (id) {
		case 26015:
			player.changeMap(200000301, 0);
			break;
		case 7860:
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			player.setKeyValue(id, "returnMap", player.getMapId() + "");
			player.warp(910001000);
			player.setKeyValue(id, "coolTime",
					sdf.format(Long.valueOf((new Date()).getTime() + 1800000L)).replaceAll("-", "/") + "");
			break;
		}
	}

	public static void BlackMageRecv(LittleEndianAccessor slea, MapleClient c) {
		int type = slea.readInt();
		if (type != 3) {
			c.send(CField.getSelectPower(8, 39));
			SkillFactory.getSkill(80002625).getEffect(1).applyTo(c.getPlayer());
		}
		Timer.EtcTimer.getInstance().schedule(() -> c.send(CField.getSelectPower(9, 39)), 4000L);
	}

	public static void fpsShootRequest(LittleEndianAccessor slea, MapleClient c) {
		slea.skip(4);
		slea.skip(4);
		if (c.getPlayer() != null) {
			FrittoEagle eagle = c.getPlayer().getFrittoEagle();
			if (eagle != null) {
				int size = slea.readInt();
				eagle.shootResult(c);
				for (int i = 0; i < size; i++) {
					int objectId = slea.readInt();
					slea.skip(14);
					MapleMap map = c.getPlayer().getMap();
					if (map != null) {
						MapleMonster mob = map.getMonsterByOid(objectId);
						if (mob != null) {
							eagle.addScore(mob, c);
							map.killMonster(mob);
						}
					}
				}
			}
		}
	}

	public static void courtshipCommand(LittleEndianAccessor slea, MapleClient c) {
		boolean success = (slea.readByte() == 1);
		if (success && c.getPlayer() != null) {
			FrittoDancing fd = c.getPlayer().getFrittoDancing();
			if (fd != null) {
				c.getPlayer().setKeyValue(15143, "score",
						String.valueOf(c.getPlayer().getKeyValue(15143, "score") + 1L));
				if (c.getPlayer().getKeyValue(15143, "score") >= 10L) {
					fd.finish(c);
				}
			}
		}
	}

	public static void forceInfo(LittleEndianAccessor slea, MapleClient c) {
		String str = slea.readMapleAsciiString();
		if (c.getPlayer().isGMName(ServerConstants.GM_NAME)) {
			System.out.println("[Error] " + str);
			FileoutputUtil.log("Log_ForceAtom.rtf", str);
		}
	}

	public static void vSkillSpecial(final LittleEndianAccessor slea, final MapleClient c) {
		Map<SecondaryStat, Pair<Integer, Integer>> statups;
		int[] Skill;
		long duration;
		final int size, selskill;
		List<SpecialPortal> atoms;
		long l1;
		slea.skip(4); // test
		final int skillId = slea.readInt();
		slea.skip(4);
		final MapleCharacter chr = c.getPlayer();
		if (chr == null || chr.getMap() == null) {
			return;
		}
		if (chr.getSkillLevel(GameConstants.getLinkedSkill(skillId)) < 0) {
			return;
		}
		SecondaryStatEffect effect = SkillFactory.getSkill(skillId)
				.getEffect(chr.getSkillLevel(GameConstants.getLinkedSkill(skillId)));
		switch (skillId) {
		case 164121041:

			(c.getPlayer()).energy = Math
					.min((c.getPlayer()).energy + c.getPlayer().getBuffedEffect(SecondaryStat.Sungi).getX(), 100);
			(c.getPlayer()).scrollGauge = Math.min(900,
					(c.getPlayer()).scrollGauge + c.getPlayer().getBuffedEffect(SecondaryStat.Sungi).getY());
			statups = new HashMap<>();
			statups.put(SecondaryStat.TidalForce,
					new Pair<>(Integer.valueOf((c.getPlayer()).energy), Integer.valueOf(0)));
			c.send(CWvsContext.BuffPacket.giveBuff(statups, null, c.getPlayer()));
			break;
		case 400011109:
			c.getPlayer().addMP(
					-((long) ((c.getPlayer().getStat().getCurrentMaxMp(c.getPlayer()) / 100L) * effect.getMpR())));
			break;
		case 400041031:
			c.getSession().writeAndFlush(CField.rangeAttack(effect.getSourceId(), Arrays.asList(
					new RangeAttack[] { new RangeAttack(400041031, c.getPlayer().getTruePosition(), 0, 0, 9) })));
			break;
		case 400001050:
			Skill = new int[] { 400001051, 400001053, 400001054, 400001055 };
			selskill = Skill[Randomizer.rand(0, Skill.length - 1)];
			c.getPlayer().setSkillCustomInfo(400001050, selskill, 0L);
			c.getSession().writeAndFlush(CField.EffectPacket.showEffect(c.getPlayer(), 0, selskill, 1, 0, 0,
					(byte) (c.getPlayer().isFacingLeft() ? 1 : 0), true, c.getPlayer().getTruePosition(), null, null));
			c.getPlayer().getMap().broadcastMessage(c.getPlayer(),
					CField.EffectPacket.showEffect(c.getPlayer(), 0, selskill, 1, 0, 0,
							(byte) (c.getPlayer().isFacingLeft() ? 1 : 0), false, c.getPlayer().getTruePosition(), null,
							null),
					false);
			l1 = c.getPlayer().getBuffLimit(400001050);
			effect.applyTo(c.getPlayer(), false, (int) l1);
			if (selskill == 400001051) {
				if (GameConstants.isDemonSlayer(c.getPlayer().getJob())) {
					c.getPlayer().addMP(c.getPlayer().getStat().getCurrentMaxMp(c.getPlayer()) / 100L * effect.getY(),
							true);
					break;
				}
				if (GameConstants.isKinesis(c.getPlayer().getJob())) {
					c.getPlayer().givePPoint(400001051);
					break;
				}
				if (GameConstants.isDemonAvenger(c.getPlayer().getJob())) {
					c.getPlayer().addHP(c.getPlayer().getStat().getCurrentMaxHp() / 100L * effect.getY());
				}
			}
			break;
		case 400001043:
			duration = c.getPlayer().getBuffLimit(skillId);
			c.getPlayer().addHP(c.getPlayer().getStat().getCurrentMaxHp() / 100L * effect.getY());
			if (c.getPlayer().getSkillCustomValue0(400001043) < effect.getW()) {
				c.getPlayer().setSkillCustomInfo(400001043,
						c.getPlayer().getSkillCustomValue0(400001043) + effect.getDamage(), 0L);
			}
			effect.applyTo(c.getPlayer(), (int) duration);
			break;
		case 400011047:
			if (chr.getBuffedValue(400011047) && chr.getBuffedValue(1301007)) {
				int maxshiled = (int) (chr.getStat().getCurrentMaxHp() / 100L * chr.getBuffedEffect(400011047).getY());
				chr.setSkillCustomInfo(400011048, maxshiled, 0L);
				chr.getBuffedEffect(400011047).applyTo(chr, false);
			}
			break;

		case 400021089: // 어비셜 라이트닝
			int sizes = slea.readInt();
			int pos_x = chr.getTruePosition().x + Randomizer.rand(0, 50);
			int pos_y = chr.getTruePosition().y;

			final List<SpecialPortal> sp = new ArrayList<SpecialPortal>();
			final SpecialPortal s3 = new SpecialPortal(chr.getId(), 2, 400021089, chr.getMapId(), pos_x, pos_y,
					(int) (chr.getBuffedValue(400021088) ? chr.getBuffLimit(400021088) : effect.getDuration()));
			s3.setObjectId((int) (900000000L + chr.getSkillCustomValue0(400021088)));
			sp.add(s3);
			chr.getMap().spawnSpecialPortal(chr, sp);
			break;

		case 400021100: // 크리스탈 게이트
			if (chr.getBuffedEffect(SecondaryStat.CrystalGate) != null && !chr.getBuffedValue(400021100)) {
				effect.applyTo(chr, false);
			}
			break;

		}
	}

	public static void Revenant(LittleEndianAccessor slea, MapleClient c) {
		int id = slea.readInt();
		MapleCharacter chra = c.getPlayer();
		SecondaryStatEffect effect = SkillFactory.getSkill(400011112).getEffect(chra.getSkillLevel(400011112));
		long duration = chra.getBuffLimit(400011112);
		int savedamage = (int) chra.getSkillCustomValue0(400011112);
		if (savedamage > 0) {
			chra.setSkillCustomInfo(400011112, (savedamage > 0) ? (savedamage - savedamage / 100 * effect.getQ2()) : 0L,
					0L);
			Map<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<>();
			statups.put(SecondaryStat.Revenant, new Pair<>(Integer.valueOf(1), Integer.valueOf((int) duration)));
			chra.getClient().getSession().writeAndFlush(
					CWvsContext.BuffPacket.giveBuff(statups, chra.getBuffedEffect(SecondaryStat.Revenant), chra));
		}
	}

	public static void Revenantend(LittleEndianAccessor slea, MapleClient c) {
		slea.skip(12);
		int skillid = slea.readInt();
		int minushp = slea.readInt();
		int plushp = slea.readInt();
		minushp += plushp;
		if (c.getPlayer().getSkillCustomValue0(400011129) > 0L) {
			c.getPlayer().addHP(-minushp, 400011129);
		}
		c.getPlayer().setSkillCustomInfo(400011129, c.getPlayer().getSkillCustomValue0(400011129) - 1L, 0L);
		if (c.getPlayer().getSkillCustomValue0(400011129) <= 0L) {
			c.getPlayer().removeSkillCustomInfo(400011112);
			while (c.getPlayer().getBuffedValue(400011129)) {
				c.getPlayer().cancelEffect(c.getPlayer().getBuffedEffect(SecondaryStat.RevenantDamage));
			}
		} else {
			SkillFactory.getSkill(400011129).getEffect(c.getPlayer().getSkillLevel(400011112)).applyTo(c.getPlayer(),
					false);
		}
	}

	public static void photonRay(LittleEndianAccessor slea, MapleClient c) {
		int type = slea.readInt();
		MapleCharacter chr = c.getPlayer();
		if (chr == null) {
			return;
		}
		if (chr.getBuffedEffect(SecondaryStat.PhotonRay) != null) {
			chr.photonRay = type;
			Map<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<>();
			statups.put(SecondaryStat.PhotonRay,
					new Pair<>(Integer.valueOf(1), Integer.valueOf((int) chr.getBuffLimit(400041057))));
			chr.getClient().getSession().writeAndFlush(
					CWvsContext.BuffPacket.giveBuff(statups, chr.getBuffedEffect(SecondaryStat.PhotonRay), chr));
			chr.getMap().broadcastMessage(chr,
					CWvsContext.BuffPacket.giveForeignBuff(chr, statups, chr.getBuffedEffect(SecondaryStat.PhotonRay)),
					false);
		}
	}

	public static void crystalGate(LittleEndianAccessor slea, MapleClient c) {
		int objectId = slea.readInt();
		Point pos = new Point(slea.readInt(), slea.readInt());
		MapleCharacter chr = c.getPlayer();
		if (chr == null || chr.getMap() == null || chr.getBuffedEffect(SecondaryStat.CrystalGate) == null
				|| chr.getMap().getMapObject(objectId, MapleMapObjectType.SPECIAL_PORTAL) == null) {
			return;
		}
		chr.getMap().movePlayer(chr, pos);
		chr.checkFollow();
	}

	public static void cancelBuffForce(MapleClient c) {
		MapleCharacter chr = c.getPlayer();
		if (chr != null) {

		}
	}

	public static void CommandLockAction(LittleEndianAccessor slea, MapleClient c) {
		String a;
		int num;
		Map<SecondaryStat, Pair<Integer, Integer>> localstatups;
		String skillname;
		SecondaryStatEffect effect;
		String str1;
		int ui, skillid = slea.readInt();
		String info = c.getPlayer().getInfoQuest(1544);
		String info2 = c.getPlayer().getInfoQuest(7786);
		switch (skillid) {

		// 어퍼차지 -커맨드 락
		case 400041032:
		case 1001011:
		case 3001007:
			if (c.getPlayer().getKeyValue(1544, "" + skillid + "") != 1L) {
				c.getPlayer().setKeyValue(1544, "" + skillid + "", "1");
				c.getPlayer().setKeyValue(1548, "視覺開關", "1");
			} else {
				c.getPlayer().setKeyValue(1544, "" + skillid + "", "0");
				c.getPlayer().setKeyValue(1548, "視覺開關", "0");
			}
			return;

		}
	}

	public static void CommandLockAction2(LittleEndianAccessor slea, MapleClient c) {
		String a;
		int num;
		Map<SecondaryStat, Pair<Integer, Integer>> localstatups;
		String skillname;
		SecondaryStatEffect effect;
		String str1;
		int ui, skillid = slea.readInt();
		String info = c.getPlayer().getInfoQuest(1544);
		String info2 = c.getPlayer().getInfoQuest(7786);

		switch (skillid) {
		case 21141500: { // 오리진 아드레날린 서지 구현
			if (c.getPlayer().getKeyValue(1544, "" + skillid + "") != 1L) {
				c.getPlayer().setKeyValue(1544, "" + skillid + "", "1");
			} else {
				c.getPlayer().setKeyValue(1544, "" + skillid + "", "0");
			}
			break;
		}
		case 14121052:
		case 14001026:
			if (c.getPlayer().getKeyValue(1544, "" + skillid + "") != 1L) {
				c.getPlayer().setKeyValue(1544, "" + skillid + "", "1");
			} else {
				c.getPlayer().setKeyValue(1544, "" + skillid + "", "0");
			}
			break;
		case 400021032:
			effect = SkillFactory.getSkill(skillid).getEffect(c.getPlayer().getTotalSkillLevel(skillid));
			if (c.getPlayer().getKeyValue(1544, "" + skillid + "") != 1L) {
				c.getPlayer().setKeyValue(1544, "" + skillid + "", "1");
			} else {
				c.getPlayer().setKeyValue(1544, "" + skillid + "", "0");
			}
			break;
		case 3011005:
			effect = SkillFactory.getSkill(skillid).getEffect(c.getPlayer().getTotalSkillLevel(skillid));
			if (c.getPlayer().getKeyValue(1544, "" + skillid + "") != 1L) {
				c.getPlayer().setKeyValue(1544, "" + skillid + "", "1");
			} else {
				c.getPlayer().setKeyValue(1544, "" + skillid + "", "0");
			}
			return;
		case 400021095: // 스파이럴 오브 마나
			a = "ev0";
			if (c.getPlayer().getKeyValue(21770, a) != 1L) {
				c.getPlayer().setKeyValue(21770, a, "1");
			} else {
				c.getPlayer().setKeyValue(21770, a, "0");
			}
			return;
		case 400051072: // 트리니티 퓨전
			a = "ab0";
			if (c.getPlayer().getKeyValue(21770, a) != 1L) {
				c.getPlayer().setKeyValue(21770, a, "1");
			} else {
				c.getPlayer().setKeyValue(21770, a, "0");
			}
			return;

		case 100001266:
			a = "z0";
			if (c.getPlayer().getKeyValue(21770, a) != 1L) {
				c.getPlayer().setKeyValue(21770, a, "1");
			} else {
				c.getPlayer().setKeyValue(21770, a, "0");
			}
			return;

		case 135001003:
			a = "yt0";
			if (c.getPlayer().getKeyValue(21770, a) != 1L) {
				c.getPlayer().setKeyValue(21770, a, "1");
			} else {
				c.getPlayer().setKeyValue(21770, a, "0");
			}
			return;

		case 30001068:
		case 35001006:
			num = (skillid == 35001006) ? 0 : 1;
			str1 = "mc" + num;
			if (c.getPlayer().getKeyValue(21770, str1) != 1L) {
				c.getPlayer().setKeyValue(21770, str1, "1");
			} else {
				c.getPlayer().setKeyValue(21770, str1, "0");
			}
			return;

		case 400041035:
			if (c.getPlayer().getKeyValue(1544, "" + skillid + "") != 1L) {
				c.getPlayer().setKeyValue(1544, "" + skillid + "", "1");
				c.getPlayer().getMap()
						.broadcastMessage(CField.getRefreshQuestInfo(c.getPlayer().getId(), 1, skillid, 3));
			} else {
				c.getPlayer().setKeyValue(1544, "" + skillid + "", "0");
				c.getPlayer().getMap()
						.broadcastMessage(CField.getRefreshQuestInfo(c.getPlayer().getId(), 0, skillid, 3));
			}
			return;
		case 80003016:
		case 80003025:
		case 80003046:
			skillname = SkillFactory.getSkillName(skillid);
			ui = (skillid == 80003046) ? 1297 : 1291;
			if ((c.getPlayer().getMap().isTown() || !c.getPlayer().getMap().isLevelMob(c.getPlayer()))
					&& !c.getPlayer().getBuffedValue(skillid)) {
				if (ui > 0) {
					c.getSession().writeAndFlush(CField.UIPacket.closeUI(ui));
				}
				c.getPlayer().dropMessage(5, "레벨 범위 몬스터가 없거나 " + skillname + "을 사용할 수 없는 곳입니다.");
			} else if (c.getPlayer().getBuffedValue(skillid)) {
				c.getPlayer().cancelEffect(c.getPlayer().getBuffedEffect(skillid));
				if (ui > 0) {
					if (skillid == 80003046) {
						c.send(SLFCGPacket.FollowNpctoSkill(false, 9062524, 0));
						c.send(SLFCGPacket.FollowNpctoSkill(false, 9062525, 0));
						c.send(SLFCGPacket.FollowNpctoSkill(false, 9062526, 0));
					}
					c.getSession().writeAndFlush(CField.UIPacket.closeUI(ui));
				}
				c.getPlayer().dropMessage(5, skillname + "이 비활성화되었습니다.");
			} else {
				SkillFactory.getSkill(skillid).getEffect(c.getPlayer().getSkillLevel(1)).applyTo(c.getPlayer(), 0);
				if (ui > 0) {
					c.getSession().writeAndFlush(CField.UIPacket.openUI(ui));
				}
			}
			return;

		case 20040217:
		case 20040219:
			if (c.getPlayer().getKeyValue(1544, "" + skillid + "") != 1L) {
				c.getPlayer().setKeyValue(1544, "" + skillid + "", "1");
				c.getPlayer().getMap()
						.broadcastMessage(CField.getRefreshQuestInfo(c.getPlayer().getId(), 1, skillid, 1));
			} else {
				c.getPlayer().setKeyValue(1544, "" + skillid + "", "0");
				c.getPlayer().getMap()
						.broadcastMessage(CField.getRefreshQuestInfo(c.getPlayer().getId(), 0, skillid, 1));
			}
			return;
		// 커맨드 - 이볼브 템페스트 400021095
		case 400031036:
			effect = SkillFactory.getSkill(skillid).getEffect(c.getPlayer().getTotalSkillLevel(skillid));
			if (c.getPlayer().getKeyValue(1544, "" + skillid + "") != 1L) {
				c.getPlayer().setKeyValue(1544, "" + skillid + "", "1");
				c.getPlayer().setKeyValue(1548, "視覺開關", "1");
			} else {
				c.getPlayer().setKeyValue(1544, "" + skillid + "", "0");
				c.getPlayer().setKeyValue(1548, "視覺開關", "0");
			}
			return;

		// 커맨드 - 드래곤 블레이즈
		case 400011118:
			effect = SkillFactory.getSkill(skillid).getEffect(c.getPlayer().getTotalSkillLevel(skillid));
			if (c.getPlayer().getKeyValue(1544, "" + skillid + "") != 1L) {
				c.getPlayer().setKeyValue(1544, "" + skillid + "", "1");
				c.getPlayer().setKeyValue(1548, "視覺開關", "1");
			} else {
				c.getPlayer().setKeyValue(1544, "" + skillid + "", "0");
				c.getPlayer().setKeyValue(1548, "視覺開關", "0");
			}
			return;
		// 커스텀 커맨드 - 에로우 플래터
		case 33121114: // 와일드 발칸
		case 3111013:
			effect = SkillFactory.getSkill(3111013).getEffect(c.getPlayer().getTotalSkillLevel(3111013));
			if (c.getPlayer().getKeyValue(1544, "" + skillid + "") != 1L) {
				c.getPlayer().setKeyValue(1544, "" + skillid + "", "1");
				c.getPlayer().setKeyValue(1548, "視覺開關", "1");
			} else {
				c.getPlayer().setKeyValue(1544, "" + skillid + "", "0");
				c.getPlayer().setKeyValue(1548, "視覺開關", "0");
			}
			return;

		case 400041032:
		case 1001011:
		case 3001007:
			if (c.getPlayer().getKeyValue(1544, "" + skillid + "") != 1L) {
				c.getPlayer().setKeyValue(1544, "" + skillid + "", "1");
				c.getPlayer().setKeyValue(1548, "視覺開關", "1");
			} else {
				c.getPlayer().setKeyValue(1544, "" + skillid + "", "0");
				c.getPlayer().setKeyValue(1548, "視覺開關", "0");
			}
			return;

		case 1101013:
			effect = SkillFactory.getSkill(1101013).getEffect(c.getPlayer().getTotalSkillLevel(1101013)); // 获取连击强化技能(1101013)的效果对象，使用玩家的实际技能等级
			if (c.getPlayer().getKeyValue(1544, "" + skillid + "") != 1L) { // 检查技能状态开关：1544是存储技能开关状态的键值 //
																			// 若开关未激活(值不为1)，则激活连击强化状态
				c.getPlayer().setKeyValue(1544, "" + skillid + "", "1"); // 标记技能为激活状态
				c.getPlayer().setKeyValue(1548, "視覺開關", "1"); // 启用增益特效显示(韩文字面意为"Buff Effect")
			} else {
				c.getPlayer().setKeyValue(1544, "" + skillid + "", "0"); // 若开关已激活，则关闭连击强化状态 标记技能为关闭状态
				c.getPlayer().setKeyValue(1548, "視覺開關", "0"); // 禁用增益特效显示
			}
			if (c.getPlayer().getBuffedValue(SecondaryStat.ComboCounter) != null) { // 检查玩家当前是否有连击计数器效果
				Map<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<>();
				statups.put(SecondaryStat.ComboCounter, new Pair<>(
						c.getPlayer().getBuffedValue(SecondaryStat.ComboCounter), Integer.valueOf(2147483647)));// 创建状态增强映射，准备延长连击计数器持续时间//
																												// 添加连击计数器状态，保持当前连击数不变，但将持续时间设为最大值(2147483647毫秒≈68年)
				c.getSession().writeAndFlush(CWvsContext.BuffPacket.giveBuff(statups, effect, c.getPlayer())); // 向技能释放者发送本地增益包，更新UI显示
				c.getPlayer().getMap().broadcastMessage(c.getPlayer(), // 向地图内其他玩家广播增益效果，确保视觉同步
						CWvsContext.BuffPacket.giveForeignBuff(c.getPlayer(), statups, effect), false);
			}
			return;
		case 1111015:
		case 1211017:
		case 1311017:
		case 15101021:
			if (c.getPlayer().getKeyValue(1544, "" + skillid + "") != 1L) {
				c.getPlayer().setKeyValue(1544, "" + skillid + "", "1");
			} else {
				c.getPlayer().setKeyValue(1544, "" + skillid + "", "0");
			}
			return;
		case 13111023:
			if (info == "") {
				c.getPlayer().updateInfoQuest(1544, "alba=1");
				c.getPlayer().getMap()
						.broadcastMessage(CField.getRefreshQuestInfo(c.getPlayer().getId(), 1, skillid, 1));
			} else if (info.contains("=0")) {
				c.getPlayer().updateInfoQuest(1544, "alba=1");
				c.getPlayer().getMap()
						.broadcastMessage(CField.getRefreshQuestInfo(c.getPlayer().getId(), 1, skillid, 1));
			} else {
				c.getPlayer().updateInfoQuest(1544, "alba=0");
				c.getPlayer().getMap()
						.broadcastMessage(CField.getRefreshQuestInfo(c.getPlayer().getId(), 0, skillid, 1));
			}
			return;
		case 35101002:
			if (info == "") {
				c.getPlayer().updateInfoQuest(1544, "35101002=1;");
			} else if (info.contains("=0")) {
				c.getPlayer().updateInfoQuest(1544, "35101002=1;");
			} else {
				c.getPlayer().updateInfoQuest(1544, "35101002=0;");
			}
			return;
		case 400051008: // 캐논볼 감속모드
			if (info == "") {
				c.getPlayer().updateInfoQuest(1544, "400051008=1;");
			} else if (info.contains("=0")) {
				c.getPlayer().updateInfoQuest(1544, "400051008=1;");
			} else {
				c.getPlayer().updateInfoQuest(1544, "400051008=0;");
			}
			return;
		case 400031003: // 하울링 게일 감속모드
			if (info == "") {
				c.getPlayer().updateInfoQuest(1544, "400031003=1;");
			} else if (info.contains("=0")) {
				c.getPlayer().updateInfoQuest(1544, "400031003=1;");
			} else {
				c.getPlayer().updateInfoQuest(1544, "400031003=0;");
			}
			return;
		case 32001016:
			if (info == "") {
				c.getPlayer().updateInfoQuest(1544, "32001016=1;");
				c.getPlayer().getMap()
						.broadcastMessage(CField.getRefreshQuestInfo(c.getPlayer().getId(), 1, skillid, 0));
			} else if (info.contains("=0")) {
				c.getPlayer().updateInfoQuest(1544, "32001016=1;");
				c.getPlayer().getMap()
						.broadcastMessage(CField.getRefreshQuestInfo(c.getPlayer().getId(), 1, skillid, 0));
			} else {
				c.getPlayer().updateInfoQuest(1544, "32001016=0;");
				c.getPlayer().getMap()
						.broadcastMessage(CField.getRefreshQuestInfo(c.getPlayer().getId(), 0, skillid, 0));
			}
			return;
		case 51121009:
			if (info == "") {
				c.getPlayer().updateInfoQuest(1544, "51121009=1;");
			} else if (info.contains("=0")) {
				c.getPlayer().updateInfoQuest(1544, "51121009=1;");
			} else {
				c.getPlayer().updateInfoQuest(1544, "51121009=0;");
			}
			return;
		case 400011121:
			if (info == "") {
				c.getPlayer().updateInfoQuest(1544, "400011121=1;");
			} else if (info.contains("=0")) {
				c.getPlayer().updateInfoQuest(1544, "400011121=1;");
			} else {
				c.getPlayer().updateInfoQuest(1544, "400011121=0;");
			}
			return;
		}
		if (skillid == 164000010) {
			if (c.getPlayer().getKeyValue(21770, "at2") != 1L) {
				c.getPlayer().setKeyValue(21770, "at2", "1");
			} else {
				c.getPlayer().setKeyValue(21770, "at2", "0");
			}
		} else if (skillid == 164001004) {
			if (c.getPlayer().getKeyValue(21770, "at1") != 1L) {
				c.getPlayer().setKeyValue(21770, "at1", "1");
			} else {
				c.getPlayer().setKeyValue(21770, "at1", "0");
			}
		} else if (skillid == 164121005) {
			if (c.getPlayer().getKeyValue(21770, "at0") != 1L) {
				c.getPlayer().setKeyValue(21770, "at0", "1");
			} else {
				c.getPlayer().setKeyValue(21770, "at0", "0");
			}
		} else if (skillid == 151001004) {
			info = c.getPlayer().getInfoQuest(21770);
			if (info == "") {
				c.getPlayer().updateInfoQuest(21770, "lw0=1;");
			} else if (info.contains("=0")) {
				c.getPlayer().updateInfoQuest(21770, "lw0=1;");
			} else {
				c.getPlayer().updateInfoQuest(21770, "lw0=0;");
			}
		} else if (skillid == 30010110) {
			info = c.getPlayer().getInfoQuest(21770);
			if (info == "") {
				c.getPlayer().updateInfoQuest(21770, "ds0=1;");
			} else if (info.contains("=0")) {
				c.getPlayer().updateInfoQuest(21770, "ds0=1;");
			} else {
				c.getPlayer().updateInfoQuest(21770, "ds0=0;");
			}
		} else if (skillid == 37101001 || skillid == 37111003) {
			info = c.getPlayer().getInfoQuest(1544);
			info2 = (skillid == 37101001) ? "bl1" : "bl0";
			if (c.getPlayer().getKeyValue(1544, "" + info2 + "") != 1L) {
				c.getPlayer().setKeyValue(1544, "" + info2 + "", "1");
			} else {
				c.getPlayer().setKeyValue(1544, "" + info2 + "", "0");
			}
		} else if (skillid == 155001103 || skillid == 155111207) {
			if (c.getPlayer().getKeyValue(1544, String.valueOf(skillid)) == 1L) {
				c.getPlayer().setKeyValue(1544, String.valueOf(skillid), String.valueOf(0));
			} else {
				c.getPlayer().setKeyValue(1544, String.valueOf(skillid), String.valueOf(1));
			}

		} else {
			String id = "";
			List<Pair<Byte, Byte>> list = new ArrayList<>();
			info = c.getPlayer().getInfoQuest(21770);
			String[] info_ = (info == "") ? null : info.split(";");
			info = "";
			int i;
			for (i = 0; i < 10; i++) {
				list.add(i, new Pair<>(Byte.valueOf((byte) -1), Byte.valueOf((byte) -1)));
			}
			if (info_ != null) {
				for (i = 0; i < info_.length; i++) {
					list.remove(Byte.parseByte(info_[i].split("=")[0]) - 1);
					list.add(Byte.parseByte(info_[i].split("=")[0]) - 1,
							new Pair<>(Byte.valueOf(Byte.parseByte(info_[i].split("=")[0])),
									Byte.valueOf(Byte.parseByte(info_[i].split("=")[1]))));
				}
			}
			switch (skillid) {
			case 21001009:
				id = "1";
				break;
			case 21101011:
				id = "2";
				break;
			case 21101016:
				id = "3";
				break;
			case 21101017:
				id = "4";
				break;
			case 21111017:
				id = "5";
				break;
			case 21111019:
				id = "6";
				break;
			case 21111021:
				id = "7";
				break;
			case 21120023:
				id = "8";
				break;
			case 21120019:
				id = "9";
				break;
			case 400011031:
				id = "10";
				break;
			}
			if (id == "") {
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				return;
			}
			boolean changed = false;
			int j;
			for (j = 0; j < list.size(); j++) {
				if (((Byte) ((Pair) list.get(j)).getLeft()).equals(Byte.valueOf(Byte.parseByte(id)))) {
					byte value = (byte) ((((Byte) ((Pair) list.get(j)).getRight()).byteValue() == 0) ? 1 : 0);
					list.remove(j);
					list.add(j, new Pair<>(Byte.valueOf(Byte.parseByte(id)), Byte.valueOf(value)));
					changed = true;
					break;
				}
			}
			if (!changed) {
				list.remove(Byte.parseByte(id) - 1);
				list.add(Byte.parseByte(id) - 1, new Pair<>(Byte.valueOf(Byte.parseByte(id)), Byte.valueOf((byte) 1)));
			}
			for (j = 0; j < list.size(); j++) {
				if (((Byte) ((Pair) list.get(j)).getLeft()).byteValue() != -1) {
					if (info == "") {
						info = (new StringBuilder()).append(((Pair) list.get(j)).getLeft()).append("=")
								.append(((Pair) list.get(j)).getRight()).toString();
					} else {
						info = info + ";" + ((Pair) list.get(j)).getLeft() + "=" + ((Pair) list.get(j)).getRight();
					}
				}
			}
			c.getPlayer().updateInfoQuest(21770, info);
		}
	}

	public static void Vmatrixstackbuff(MapleClient c, boolean use, LittleEndianAccessor slea) {
		Vmatrixstackbuff(c, use, slea, 0);
	}

	public static void Vmatrixstackbuff(MapleClient c, boolean use, LittleEndianAccessor slea, int count1) {
		if (c.getPlayer() == null) {
			return;
		}
		HashMap<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
		int skillid = 0;
		if (slea != null && slea.available() >= 4L && !use) {
			skillid = slea.readInt();
		}
		if (GameConstants.isPinkBean(c.getPlayer().getJob())) {
			long count = 0L;
			if (c.getPlayer().getSkillCustomValue(131001010) != null) {
				count = c.getPlayer().getSkillCustomValue(131001010);
			}
			if (use && count > 0L) {
				c.getPlayer().setSkillCustomInfo(131001010, count - 1L, 0L);
			} else if (!use && count < 8L) {
				c.getPlayer().setSkillCustomInfo(131001010, count + 1L, 0L);
			}
			count = c.getPlayer().getSkillCustomValue(131001010);
			SecondaryStatEffect yoyo = SkillFactory.getSkill(131001010).getEffect(1);
			statups.put(SecondaryStat.PinkbeanYoYoStack, new Pair<Integer, Integer>((int) count, 0));
			c.getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, yoyo, c.getPlayer()));
		} else if (c.getPlayer().getJob() == 312) { // FalshMirazu
			SecondaryStatEffect flashMirazu = SkillFactory.getSkill(3111015)
					.getEffect(c.getPlayer().getSkillLevel(3111015));
			long count = 0L;
			count = c.getPlayer().플레시미라주스택;
			statups.put(SecondaryStat.FlashMirage, new Pair<Integer, Integer>((int) count, 0));
			c.getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, flashMirazu, c.getPlayer()));

		} else if ((c.getPlayer().getJob() == 531 || c.getPlayer().getJob() == 532)) {
			boolean isHexa = c.getPlayer().getSkillLevel(5341002) > 0;
			long count = 0;
			if (c.getPlayer().getSkillCustomValue(isHexa ? 5341002 : 5311013) != null) {
				count = c.getPlayer().getSkillCustomValue(isHexa ? 5341002 : 5311013);
			} else if (c.getPlayer().getSkillCustomValue(isHexa ? 5341002 : 5311013) == null) {
				c.getPlayer().setSkillCustomInfo(isHexa ? 5341002 : 5311013, count, 0);
			}
			if (c.getPlayer().getSkillCustomValue(isHexa ? 5341002 : 5311013) != 5) {
				if (use && count > 0L) {
					c.getPlayer().setSkillCustomInfo(isHexa ? 5341002 : 5311013, count - 1L, 0L);
				} else if (!use && count < 5L) {
					c.getPlayer().setSkillCustomInfo(isHexa ? 5341002 : 5311013, count + 1L, 0L);
				}
				count = c.getPlayer().getSkillCustomValue(isHexa ? 5341002 : 5311013);
				SecondaryStatEffect yoyo = SkillFactory.getSkill(isHexa ? 5341002 : 5311013).getEffect(1);
				statups.put(SecondaryStat.MiniCannonBall, new Pair<Integer, Integer>((int) count, 0));
				c.getSession().writeAndFlush(CWvsContext.BuffPacket.giveBuff(statups, yoyo, c.getPlayer()));
			}
			if (c.getPlayer().getJob() == 532) {
				long counts = 0;
				if (c.getPlayer().getSkillCustomValue(400051008) != null) {
					counts = c.getPlayer().getSkillCustomValue(400051008);
				} else if (c.getPlayer().getSkillCustomValue(400051008) == null) {
					c.getPlayer().setSkillCustomInfo(400051008, counts, 0);
				}
				if (c.getPlayer().getSkillCustomValue(400051008) != 3
						&& c.getPlayer().getSkillCustomValue(isHexa ? 5341002 : 5311013) >= 3) {
					if (use && counts > 0L) {
						c.getPlayer().setSkillCustomInfo(400051008, counts - 1, 0L);
					} else if (!use && c.getPlayer().getBHGCCount() < 3L) {
						c.getPlayer().setBHGCCount(c.getPlayer().getBHGCCount() + 1);
						// c.getPlayer().setSkillCustomInfo(400051008, counts + 1, 0L);
					}
				}

				// counts = c.getPlayer().getSkillCustomValue(400051008);
				SecondaryStatEffect yoyo = SkillFactory.getSkill(400051008).getEffect(1);
				statups.put(SecondaryStat.VMatrixStackBuff,
						new Pair<Integer, Integer>((int) c.getPlayer().getBHGCCount(), 0));
				c.getSession().writeAndFlush(CWvsContext.BuffPacket.giveBuff(statups, yoyo, c.getPlayer()));
			}
			// 하울링 게일 스택체크
		} else if (c.getPlayer().getJob() == 1312) {
			SecondaryStatEffect yoyo = SkillFactory.getSkill(400031003).getEffect(1);
			long count = 0L;
			if (!use && c.getPlayer().getSkillCustomValue(400031004) != null) {
				return;
			}
			if (c.getPlayer().getSkillCustomValue(400031003) != null) {
				count = c.getPlayer().getSkillCustomValue(400031003);
			}
			if (use && count > 0L) {
				c.getPlayer().setSkillCustomInfo(400031003, count - (long) count1, 0L);
			} else if (!use && count < 3L) { // 하울링 게일 스택 오버플로우
				c.getPlayer().setSkillCustomInfo(400031003, count + 1L, 0L);
				c.getPlayer().setSkillCustomInfo(400031004, 0L, yoyo.getX() * 1000);
			}
			count = c.getPlayer().getSkillCustomValue(400031003);
			statups.put(SecondaryStat.HowlingGale, new Pair<Integer, Integer>((int) count, 0));
			c.getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, yoyo, c.getPlayer()));
		} // 작업 - 플레임 위자드 인피니티 플레임 서클
		else if (c.getPlayer().getJob() == 1212) {
			SecondaryStatEffect InfinityFlameCircle = SkillFactory.getSkill(400021072).getEffect(1);
			long count = 0L;
			if (!use && c.getPlayer().getSkillCustomValue(400021072) != null) {
				return;
			}
			if (c.getPlayer().getSkillCustomValue(400021072) != null) {
				count = c.getPlayer().getSkillCustomValue(400021072);
			}
			if (use) {
				c.getPlayer().setSkillCustomInfo(400021072, count - (long) count1, 0L);
			} else if (!use && count < 10L) {
				c.getPlayer().setSkillCustomInfo(400021072, count + 1L, 0L);
				c.getPlayer().setSkillCustomInfo(400021072, 0L, InfinityFlameCircle.getX() * 1000);
			}
			count = c.getPlayer().getSkillCustomValue(400021072);
			statups.put(SecondaryStat.InfinityFlameCircle, new Pair<Integer, Integer>((int) count, 0));
			c.getSession().writeAndFlush(
					(Object) CWvsContext.BuffPacket.giveBuff(statups, InfinityFlameCircle, c.getPlayer()));
		} else if (c.getPlayer().getJob() == 3312) {
			long count = 0L;
			if (c.getPlayer().getSkillCustomValue(400031032) != null) {
				count = c.getPlayer().getSkillCustomValue(400031032);
			}
			if (use && count > 0L) {
				c.getPlayer().setSkillCustomInfo(400031032, count - 1L, 0L);
			} else if (!use && count < 8L) {
				c.getPlayer().setSkillCustomInfo(400031032, count + 1L, 0L);
			}
			count = c.getPlayer().getSkillCustomValue(400031032);
			SecondaryStatEffect yoyo = SkillFactory.getSkill(400031032).getEffect(1);
			statups.put(SecondaryStat.WildGrenadier, new Pair<Integer, Integer>((int) count, 0));
			c.getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, yoyo, c.getPlayer()));
		} else if (c.getPlayer().getJob() == 232) {
			long count = 0L;
			if (c.getPlayer().getSkillCustomValue(400021086) != null) {
				count = c.getPlayer().getSkillCustomValue(400021086);
			}
			if (use && count > 0L) {
				c.getPlayer().setSkillCustomInfo(400021086, count - 1L, 0L);
			} else if (!use && count < 8L) {
				c.getPlayer().setSkillCustomInfo(400021086, count + 1L, 0L);
			}
			count = c.getPlayer().getSkillCustomValue(400021086);
			SecondaryStatEffect yoyo = SkillFactory.getSkill(400021086).getEffect(1);
			statups.put(SecondaryStat.VMatrixStackBuff, new Pair<Integer, Integer>((int) count, 0));
			c.getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, yoyo, c.getPlayer()));
			// 마이티 묠니르
		} else if (c.getPlayer().getJob() == 122) {
			long count = 0L;
			if (c.getPlayer().getSkillCustomValue(400011131) != null) {
				count = c.getPlayer().getSkillCustomValue(400011131);
			}
			if (use && count > 0L) {
				c.getPlayer().setSkillCustomInfo(400011131, count - 1L, 0L);
			} else if (!use && count < 3L) { // [othello] 마이티 묠니르 스택 추가
				c.getPlayer().setSkillCustomInfo(400011131, count + 1L, 0L);
			}
			count = c.getPlayer().getSkillCustomValue(400011131);
			SecondaryStatEffect yoyo = SkillFactory.getSkill(400011131).getEffect(1);
			statups.put(SecondaryStat.VMatrixStackBuff, new Pair<Integer, Integer>((int) count, 0));
			c.getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, yoyo, c.getPlayer()));
			// 메이플월드 여신의 축복
		} else if (c.getPlayer().getJob() == 3212) {
			long count = 0L;
			if (c.getPlayer().getSkillCustomValue(400001042) != null) {
				count = c.getPlayer().getSkillCustomValue(400001042);
			}
			if (use && count > 0L) {
				c.getPlayer().setSkillCustomInfo(400001042, count - 1L, 0L);
			} else if (!use && count < 3L) {
				c.getPlayer().setSkillCustomInfo(400001042, count + 1L, 0L);
			}
			count = c.getPlayer().getSkillCustomValue(400001042);
			SecondaryStatEffect yoyo = SkillFactory.getSkill(400001042).getEffect(1);
			statups.put(SecondaryStat.VMatrixStackBuff, new Pair<Integer, Integer>((int) count, 0));
			c.getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, yoyo, c.getPlayer()));

		} else if (c.getPlayer().getJob() == 3212) {
			long count = 0L;
			if (c.getPlayer().getSkillCustomValue(400021047) != null) {
				count = c.getPlayer().getSkillCustomValue(400021047);
			}
			if (use && count > 0L) {
				c.getPlayer().setSkillCustomInfo(400021047, count - 1L, 0L);
			} else if (!use && count < 4L) {
				c.getPlayer().setSkillCustomInfo(400021047, count + 1L, 0L);
			}
			count = c.getPlayer().getSkillCustomValue(400021047);
			SecondaryStatEffect yoyo = SkillFactory.getSkill(400021047).getEffect(1);
			statups.put(SecondaryStat.VMatrixStackBuff, new Pair<Integer, Integer>((int) count, 0));
			c.getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, yoyo, c.getPlayer()));
		} else if (c.getPlayer().getJob() == 512) {
			SecondaryStatEffect effect = SkillFactory.getSkill(400051042)
					.getEffect(c.getPlayer().getSkillLevel(400051042));
			long count = 0L;
			if (c.getPlayer().getSkillCustomValue(400051042) != null) {
				count = c.getPlayer().getSkillCustomValue(400051042);
			}
			if (use && count > 0L) {
				c.getPlayer().setSkillCustomInfo(400051042, count - 1L, 0L);
			} else if (!use && count < 6L) {
				c.getPlayer().setSkillCustomInfo(400051042, count + 1L, 0L);
			}
			count = c.getPlayer().getSkillCustomValue(400051042);
			statups.put(SecondaryStat.VMatrixStackBuff, new Pair<Integer, Integer>((int) count, 0));
			c.getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, effect, c.getPlayer()));
		} else if (GameConstants.isPathFinder(c.getPlayer().getJob())) {
			SecondaryStatEffect effect = SkillFactory.getSkill(3321006).getEffect(c.getPlayer().getSkillLevel(3321006));
			long count = 0L;
			if (c.getPlayer().getSkillCustomValue(3321006) != null) {
				count = c.getPlayer().getSkillCustomValue(3321006);
			}
			if (use && count > 0L) {
				c.getPlayer().setSkillCustomInfo(3321006, count - 1L, 0L);
			} else if (!use && count < 5L) {
				c.getPlayer().setSkillCustomInfo(3321006, count + 1L, 0L);
			}
			count = c.getPlayer().getSkillCustomValue(3321006);
			statups.put(SecondaryStat.VMatrixStackBuff, new Pair<Integer, Integer>((int) count, 0));
			c.getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, effect, c.getPlayer()));
		} else if (c.getPlayer().getJob() == 15212) {
			SecondaryStatEffect effect = SkillFactory.getSkill(400021068).getEffect(1);
			long count = 0L;
			if (c.getPlayer().getSkillCustomValue(400021068) != null) {
				count = c.getPlayer().getSkillCustomValue(400021068);
			}
			if (use && count > 0L) {
				c.getPlayer().setSkillCustomInfo(400021068, count - 1L, 0L);
			} else if (!use && count < 2L) {
				c.getPlayer().setSkillCustomInfo(400021068, count + 1L, 0L);
			}
			count = c.getPlayer().getSkillCustomValue(400021068);
			statups.put(SecondaryStat.VMatrixStackBuff, new Pair<Integer, Integer>((int) count, 0));
			c.getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, effect, c.getPlayer()));
		} else if (c.getPlayer().getJob() == 6412) {
			SecondaryStatEffect effect = SkillFactory.getSkill(400041074).getEffect(1);
			long count = 0L;
			if (c.getPlayer().getSkillCustomValue(400041074) != null) {
				count = c.getPlayer().getSkillCustomValue(400041074);
			}
			if (use && count > 0L) {
				c.getPlayer().setSkillCustomInfo(400041074, count - 1L, 0L);
			} else if (!use && count < 3L) {
				c.getPlayer().setSkillCustomInfo(400041074, count + 1L, 0L);
			}
			count = c.getPlayer().getSkillCustomValue(400041074);
			statups.put(SecondaryStat.WeaponVarietyFinale, new Pair<Integer, Integer>((int) count, 0));
			c.getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, effect, c.getPlayer()));
		} else if (GameConstants.isYeti(c.getPlayer().getJob())) {
			SecondaryStatEffect effect = SkillFactory.getSkill(135001007).getEffect(1);
			long count = 0L;
			if (c.getPlayer().getSkillCustomValue(135001007) != null) {
				count = c.getPlayer().getSkillCustomValue(135001007);
			}
			if (use && count > 0L) {
				c.getPlayer().setSkillCustomInfo(135001007, count - 1L, 0L);
			} else if (!use
					&& count < (long) (c.getPlayer().getBuffedEffect(SecondaryStat.YetiAngerMode) != null ? 3 : 2)) {
				c.getPlayer().setSkillCustomInfo(135001007, count + 1L, 0L);
			}
			effect.applyTo(c.getPlayer());
		}
	}

	public static void SilhouEtteMirage(MapleCharacter player, LittleEndianAccessor slea) {
		int skillid = slea.readInt();
		if (player.getBuffedValue(400031053) && player.getSkillCustomValue0(400031053) < 2L) {
			player.setSkillCustomInfo(400031053, player.getSkillCustomValue0(400031053) + 1L, 0L);
			player.getBuffedEffect(400031053).applyTo(player);
		}
	}

	public static void ChangeDragonImg(MapleCharacter chr, LittleEndianAccessor slea) {
		int unk = slea.readInt();
		int skillid = slea.readInt();
		int skilllevel = slea.readInt();
		chr.getMap().broadcastMessage(chr, CField.getDragonForm(chr, unk, skillid, skilllevel), false);
	}

	public static void AttackDragonImg(MapleCharacter chr, LittleEndianAccessor slea) {
		int skillid = slea.readInt();
		int skilllevel = slea.readInt();
		Point pos = slea.readIntPos();
		Point pos1 = slea.readIntPos();
		chr.getMap().broadcastMessage(chr, CField.getDragonAttack(chr, skillid, skilllevel, pos, pos1), false);
	}

	public static void PhantomShroud(LittleEndianAccessor slea, MapleClient c) {
		int skillid = slea.readInt();
		if (skillid == 20031205) {
			c.getPlayer().setSkillCustomInfo(skillid, c.getPlayer().getSkillCustomValue0(skillid) + 1L, 0L);
		}
	}

	public static void LiftBreak(MapleCharacter player, LittleEndianAccessor slea) {
		slea.skip(4);
		slea.skip(1);
		slea.skip(4);
		slea.skip(4);
		slea.skip(1);
		int skillid = slea.readInt();
		if (skillid == 64121013 || skillid == 64121014 || skillid == 64121015) {
			player.cancelEffect(player.getBuffedEffect(64120006), (List<SecondaryStat>) null, true);
			SkillFactory.getSkill(64120006).getEffect(player.getSkillLevel(64120006)).applyTo(player);
		}
		if (skillid == 400051078 || skillid == 63121008 || skillid == 64121013) {
			slea.skip(4);
			slea.skip(4);
			slea.skip(2);
			slea.skip(4);
		} else if (skillid == 400031025) { // [othello] 차지드 애로우
			slea.readInt();
			slea.readInt();
			slea.skip(1);
			slea.skip(1);
			slea.skip(4);
		} else if (skillid == 400051070) { // [othello] 하울링 피스트
			slea.readInt();
			slea.readInt();
			slea.skip(1);
			slea.skip(1);
			slea.skip(4);
			slea.skip(1);
			slea.skip(1);
			slea.skip(4);
			SecondaryStatEffect effect = player.getBuffedEffect(SecondaryStat.IndieNotDamaged);
			Map<SecondaryStat, Pair<Integer, Integer>> localstatups = new HashMap<>();
			localstatups.clear();
			localstatups.put(SecondaryStat.IndieNotDamaged,
					new Pair<>(Integer.valueOf(player.getId()), Integer.valueOf((int) player.getBuffLimit(400051070))));
			player.getClient().getSession()
					.writeAndFlush(CWvsContext.BuffPacket.giveBuff(localstatups, effect, player));

		} else {
			slea.skip(4);
			slea.skip(4);
			slea.skip(2);
			slea.skip(4);
			slea.skip(4);
			slea.skip(1);
			int count = slea.readInt();
			slea.skip(4);

			if (skillid != 11121055 && skillid != 11121056 && skillid != 400011056 && skillid != 400011065) {
				SecondaryStatEffect effect = SkillFactory.getSkill(skillid).getEffect(player.getSkillLevel(skillid));
				if (effect != null) {
					player.changeCooldown(skillid, -((int) (count * effect.getT() * 1000.0D)));
					if (GameConstants.isSoulMaster(player.getJob()) && player.getBuffedValue(11001030)) { // 소마 직업만 나오게
						int Cosmiccount = player.getCosmicCount();
						if (Cosmiccount <= 9) { // 최대 10개 일때까지
							player.setCosmicCount(Cosmiccount + 1); // [othello] 엘리시온 시 코스믹 오브 증가
						}
					}
				}
			}

			if (skillid == 154121011) { // [othello] 보이드 블리츠 다크사이트
				Map<SecondaryStat, Pair<Integer, Integer>> localstatups = new HashMap<>();
				SecondaryStatEffect effect = SkillFactory.getSkill(154121011)
						.getEffect(player.getSkillLevel(154121011));
				localstatups.clear();
				localstatups.put(SecondaryStat.DarkSight, new Pair<>(Integer.valueOf(player.getId()), 3000));
				player.getClient().getSession()
						.writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(localstatups, effect, player));
				SkillFactory.getSkill(154121011).getEffect(player.getSkillLevel(154121011)).applyTo(player);

			}
		}
	}

	public static void Magunmblow(LittleEndianAccessor slea, MapleClient c) {
		int skillid = slea.readInt();
		SecondaryStatEffect effect = SkillFactory.getSkill(skillid).getEffect(c.getPlayer().getSkillLevel(skillid));
		if (skillid == 37121052 && c.getPlayer().getSkillCustomValue(skillid).longValue() < 4L) {
			c.getPlayer().addSkillCustomInfo(skillid, 1L);
			effect.applyTo(c.getPlayer());
		} else if (skillid == 131001020) {
			effect.applyTo(c.getPlayer(), false);
		}
	}

	public static void activeRestoreBuff(MapleClient c) {
		if (c != null) {
			MapleCharacter player = c.getPlayer();
			if (player.getParty() != null) {
				MapleParty party = player.getParty();
				SecondaryStatEffect effect = player.getBuffedEffect(SecondaryStat.Restore);
				if (effect != null) {
					c.getPlayer().getClient().getSession()
							.writeAndFlush(CField.EffectPacket.showEffect(c.getPlayer(), 0, 400011109, 7, 0, 0,
									(byte) (c.getPlayer().isFacingLeft() ? 1 : 0), true,
									c.getPlayer().getTruePosition(), null, null));
					c.getPlayer().getMap().broadcastMessage(c.getPlayer(),
							CField.EffectPacket.showEffect(c.getPlayer(), 0, 400011109, 7, 0, 0,
									(byte) (c.getPlayer().isFacingLeft() ? 1 : 0), false,
									c.getPlayer().getTruePosition(), null, null),
							false);
					c.getPlayer().addHP((long) ((c.getPlayer().getStat().getCurrentMaxHp() / 100L) * effect.getT()));
					if (party != null) {
						for (MaplePartyCharacter pc : party.getMembers()) {
							MapleCharacter victim = c.getChannelServer().getPlayerStorage()
									.getCharacterByName(pc.getName());
							if (victim != null && victim.getId() != c.getPlayer().getId()
									&& (player.getTruePosition()).x + (effect.getLt()).x < (victim.getTruePosition()).x
									&& (player.getTruePosition()).x - (effect.getLt()).x > (victim.getTruePosition()).x
									&& (player.getTruePosition()).y + (effect.getLt()).y < (victim.getTruePosition()).y
									&& (player.getTruePosition()).y
											- (effect.getLt()).y > (victim.getTruePosition()).y) {
								victim.getClient().getSession()
										.writeAndFlush(CField.EffectPacket.showEffect(victim, 0, 400011109, 4, 0, 0,
												(byte) (victim.isFacingLeft() ? 1 : 0), true, victim.getTruePosition(),
												null, null));
								victim.getMap().broadcastMessage(victim,
										CField.EffectPacket.showEffect(victim, 0, 400011109, 4, 0, 0,
												(byte) (victim.isFacingLeft() ? 1 : 0), false, victim.getTruePosition(),
												null, null),
										false);
								victim.addHP((long) ((victim.getStat().getCurrentMaxHp() / 100L) * effect.getT()));
							}
						}
					}
				}
			}
		}
	}

	public static void HarmonyLink(LittleEndianAccessor slea, MapleClient c) {
		List<MapleMonster> moblist = new ArrayList<>();
		List<MapleCharacter> chrlist = new ArrayList<>();
		List<Triple<MonsterStatus, MonsterStatusEffect, Long>> statusz = new ArrayList<>();
		int skillid = slea.readInt();
		slea.readInt();
		Point pos = slea.readPos();
		int mobsize = slea.readInt();
		for (int i = 0; i < mobsize; i++) {
			MapleMonster mob = c.getPlayer().getMap().getMonsterByOid(slea.readInt());
			if (mob != null) {
				moblist.add(mob);
			}
		}
		int chrsize = slea.readInt();
		for (int j = 0; j < chrsize; j++) {
			MapleCharacter chr = c.getPlayer().getMap().getCharacterById(slea.readInt());
			if (chr != null) {
				chrlist.add(chr);
			}
		}
		if (skillid == 152111007) {
			if (moblist != null) {
				for (MapleMonster mob : moblist) {
					SecondaryStatEffect curseMark = SkillFactory.getSkill(152000010)
							.getEffect(c.getPlayer().getSkillLevel(152000010));
					int max = (c.getPlayer().getSkillLevel(152100012) > 0) ? 5
							: ((c.getPlayer().getSkillLevel(152110010) > 0) ? 3 : 1);
					if (mob.getBuff(152000010) == null && mob.getCustomValue0(152000010) > 0L) {
						mob.removeCustomInfo(152000010);
					}
					if (mob.getCustomValue0(152000010) < max) {
						mob.addSkillCustomInfo(152000010, 1L);
					}
					mob.applyStatus(c, MonsterStatus.MS_CurseMark,
							new MonsterStatusEffect(152000010, curseMark.getDuration()),
							(int) mob.getCustomValue0(152000010), curseMark);
				}
			}
			if (chrlist != null) {
				for (MapleCharacter chr : chrlist) {
					SecondaryStatEffect HarmonyLink = SkillFactory.getSkill(skillid)
							.getEffect(c.getPlayer().getSkillLevel(skillid));
					chr.addHP(chr.getStat().getCurrentMaxHp() / 100L * HarmonyLink.getDamage());
					chr.getClient().getSession().writeAndFlush(CField.EffectPacket.showEffect(chr, 0, 152111007, 4, 0,
							0, (byte) (chr.isFacingLeft() ? 1 : 0), true, chr.getTruePosition(), null, null));
					chr.getMap().broadcastMessage(chr,
							CField.EffectPacket.showEffect(chr, 0, 152111007, 4, 0, 0,
									(byte) (chr.isFacingLeft() ? 1 : 0), false, chr.getTruePosition(), null, null),
							false);
					if (c.getPlayer().getSkillLevel(152120012) > 0) {
						chr.blessMarkSkill = 152120012;
					} else if (c.getPlayer().getSkillLevel(152110009) > 0) {
						chr.blessMarkSkill = 152110009;
					} else if (c.getPlayer().getSkillLevel(152000007) > 0) {
						chr.blessMarkSkill = 152000007;
					}
					SkillFactory.getSkill(152000009).getEffect(c.getPlayer().getSkillLevel(152000009)).applyTo(chr);
				}
			}
		}
	}

	public static void ForceAtomEffect(LittleEndianAccessor slea, MapleClient c) {
		int unk2 = 0;
		boolean left = false;
		int type = slea.readInt();
		int atomid = slea.readInt();
		int unk = slea.readInt();
		if (type == 2) {
			unk2 = slea.readInt();
			left = (slea.readByte() == 1);
		}
		Point pos1 = new Point(0, 0);
		Point pos2 = new Point(0, 0);
		if (slea.available() > 4L) {
			pos1 = slea.readPos();
		}
		if (slea.available() > 4L) {
			pos2 = slea.readPos();
		}
		c.getPlayer().getMap().broadcastMessage(c.getPlayer(),
				CField.ForceAtomEffect(c.getPlayer(), atomid, (type == 1) ? 3 : type, unk, unk2, left, pos1, pos2),
				false);
	}

	public static void SelectHolyUnity(LittleEndianAccessor slea, MapleClient c) {
		MapleCharacter chr = c.getPlayer().getMap().getCharacter(slea.readInt());
		MapleCharacter beforechr = c.getPlayer().getMap()
				.getCharacter((int) c.getPlayer().getSkillCustomValue0(400011003));
		Point mypos = slea.readIntPos();
		Point targetpos = slea.readIntPos();
		if (!chr.getBuffedValue(400011021)) {
			SecondaryStatEffect effect = c.getPlayer().getBuffedEffect(SecondaryStat.HolyUnity);
			Rectangle box = effect.calculateBoundingBox(c.getPlayer().getPosition(), true);
			Rectangle box1 = effect.calculateBoundingBox(c.getPlayer().getPosition(), false);
			if (box.contains(targetpos) || box1.contains(targetpos)) {
				if (beforechr != null) {
					while (beforechr.getBuffedValue(400011021)) {
						beforechr.cancelEffect(beforechr.getBuffedEffect(400011021));
					}
				}
				Map<SecondaryStat, Pair<Integer, Integer>> localstatups = new HashMap<>();
				localstatups.clear();
				localstatups.put(SecondaryStat.HolyUnity, new Pair<>(Integer.valueOf(chr.getId()),
						Integer.valueOf((int) c.getPlayer().getBuffLimit(400011003))));
				c.send(CWvsContext.BuffPacket.giveBuff(localstatups, effect, c.getPlayer()));
				c.getPlayer().getMap().broadcastMessage(c.getPlayer(),
						CWvsContext.BuffPacket.giveForeignBuff(c.getPlayer(), localstatups, effect), false);
				chr.getMap().broadcastMessage(chr,
						CField.EffectPacket.showEffect(chr, 0, 400011003, 1, 0, 0,
								(byte) (((chr.getTruePosition()).x > (chr.getPosition()).x) ? 1 : 0), false,
								chr.getPosition(), null, null),
						false);
				c.getPlayer().setSkillCustomInfo(400011003, chr.getId(), 0L);
				SkillFactory.getSkill(400011021).getEffect(c.getPlayer().getSkillLevel(400011003)).applyTo(
						c.getPlayer(), chr, false, chr.getPosition(), (int) c.getPlayer().getBuffLimit(400011003),
						(byte) 0, false);
			} else {
				c.getPlayer().dropMessage(5, "파티원 " + chr.getName() + "님을 결속할 수 없습니다.");
			}
		}
	}

	/**
	 * 处理 Tangyoon 烹饪相关操作的方法
	 * 
	 * @param slea 包含客户端发送数据的 LittleEndianAccessor 对象
	 * @param c    代表客户端会话的 MapleClient 对象
	 */
	public static void TangyoonCooking(final LittleEndianAccessor slea, final MapleClient c) {
		// 从客户端会话中获取当前玩家角色
		MapleCharacter chr = c.getPlayer();

		// 从 slea 中读取一个整数，作为怪物 ID
		int mobid = slea.readInt();

		// 用于存储成功烹饪所需的怪物 ID 数组，初始化为 null
		int[] success = null;

		// 用于存储当前选择的食谱名称，初始化为空字符串
		String Recipe = "";

		// 用于存储要显示给玩家的提示信息，初始化为空字符串
		String talk = "";

		// 根据玩家的 TangyoonCooking 键值来确定选择的食谱
		switch ((int) chr.getKeyValue(2498, "TangyoonCooking")) {
		// 0 代表选择了 "돼지고기볶음"（炒猪肉）食谱
		case 0:
			// 初始化成功烹饪该食谱所需的怪物 ID 数组
			success = new int[] { 9300654, 9300655, 9300656, 9300657, 9300658 };
			// 设置食谱名称
			Recipe = "炒猪肉";
			break;
		// 1 代表选择了 "달팽이요리"（蜗牛料理）食谱
		case 1:
			success = new int[] { 9300659, 9300660, 9300661, 9300662, 9300663 };
			Recipe = "蝸牛料理";
			break;
		// 2 代表选择了 "해파리냉채"（凉拌水母）食谱
		case 2:
			success = new int[] { 9300664, 9300665, 9300666, 9300667, 9300668 };
			Recipe = "水母凉菜";
			break;
		// 3 代表选择了 "버섯칼국수"（蘑菇刀削面）食谱
		case 3:
			success = new int[] { 9300669, 9300670, 9300671, 9300672, 9300673 };
			Recipe = "蘑菇刀削麵";
			break;
		// 4 代表选择了 "슬라임푸딩"（史莱姆布丁）食谱
		case 4:
			success = new int[] { 9300674, 9300675, 9300676, 9300677, 9300678 };
			Recipe = "泥布丁";
			break;
		// 处理非法的 TangyoonCooking 值
		default:
			talk = "選擇的料理不正確."; // 提示玩家选择的食谱无效
			// 向客户端发送粉色游戏消息
			c.getSession().writeAndFlush(CField.getGameMessage(11, talk));
			// 向客户端发送顶部提示消息
			c.getSession().writeAndFlush(CWvsContext.getTopMsg(talk));
			return; // 结束方法执行
		}

		// 获取玩家的 TangyoonCookingClass 键值，作为烹饪等级
		int cookingClass = (int) chr.getKeyValue(2498, "TangyoonCookingClass");

		// 检查烹饪等级是否在有效范围内
		if (cookingClass < 1 || cookingClass > success.length) {
			talk = "料理類值無效."; // 提示玩家烹饪等级无效
			c.getSession().writeAndFlush(CField.getGameMessage(11, talk));
			c.getSession().writeAndFlush(CWvsContext.getTopMsg(talk));
			return; // 结束方法执行
		}

		// 检查玩家选择的怪物 ID 是否与当前食谱和烹饪等级所需的怪物 ID 匹配
		if (mobid == success[cookingClass - 1]) {
			// 匹配成功，提示玩家可以期待美味的料理
			talk = "決定在「" + Recipe + "」裡加入" + getMobName(mobid) + "了。看來可以期待做出美味的料理呢。";
		} else {
			// 匹配失敗，提示玩家鍋裡有奇怪的味道
			talk = "決定在「" + Recipe + "」裡加入" + getMobName(mobid) + "了。不知為何，鍋子裡好像散發出奇怪的味道。";
			// 設定 TangyoonBoss 鍵值為 "1"
			c.getPlayer().setKeyValue(2498, "TangyoonBoss", "1");
		}
		// 向客户端发送粉色游戏消息
		c.getSession().writeAndFlush(CField.getGameMessage(11, talk));
		// 向客户端发送顶部提示消息
		c.getSession().writeAndFlush(CWvsContext.getTopMsg(talk));

		// 检查烹饪等级是否为 4
		if (cookingClass == 4) {
			// 向客户端发送绿色提示信息，告知玩家被任命为火焰怪物猎人
			c.getSession()
					.writeAndFlush(CField.UIPacket.greenShowInfo("\"" + c.getPlayer().getName() + "\"您被任命為火焰狼怪物獵人。."));
			// 获取指定地图对象
			MapleMap map = c.getChannelServer().getMapFactory().getMap(912080100);
			int a = 0;
			// 在地图上随机位置生成 5 个怪物
			for (int i = 0; i < 5; i++) {
				// 生成一个 -281 到 725 之间的随机数作为怪物的 x 坐标
				a = Randomizer.rand(-281, 725);
				// 在指定位置下方生成怪物
				map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9300679), new Point(a, 150));
			}
			// 向客户端发送正常效果显示消息
			c.getSession().writeAndFlush(CField.EffectPacket.showNormalEffect(c.getPlayer(), 54, true));
			// 调用 GameConstants 类的方法删除怪物
			GameConstants.TangYoonMobDelete(c, mobid, false, true, false);
		} else {
			// 烹饪等级不为 4，调用 GameConstants 类的方法生成怪物
			GameConstants.TangyoonMobSpawn(c, mobid, false);
		}
	}

	public static String getMobName(int mobid) {
		MapleData data = null;
		MapleDataProvider dataProvider = MapleDataProviderFactory
				.getDataProvider(new File(System.getProperty("wz") + "/" + "String.wz"));
		String ret = "";
		data = dataProvider.getData("Mob.img");
		List<Pair<Integer, String>> mobPairList = new LinkedList<>();
		for (MapleData mobIdData : data.getChildren()) {
			mobPairList.add(new Pair<Integer, String>(Integer.parseInt(mobIdData.getName()),
					MapleDataTool.getString(mobIdData.getChildByPath("name"), "NO-NAME")));
		}
		for (Pair<Integer, String> mobPair : mobPairList) {
			if (mobPair.getLeft() == mobid) {
				ret = mobPair.getRight();
			}
		}
		return ret;
	}

	public static void TangyoonSalt(final LittleEndianAccessor slea, final MapleClient c) {
		c.getSession().writeAndFlush(CField.achievementRatio(100));
		slea.skip(1); // 소금양
		int salt = slea.readByte();
		if (salt != 1) {
			c.getPlayer().setKeyValue(2498, "TangyoonBoss", "1");
		}
		boolean fail = (int) c.getPlayer().getKeyValue(2498, "TangyoonBoss") == 1;
		String talk = "";
		int mobid = 0;
		int x = 0;
		if ((int) c.getPlayer().getKeyValue(2498, "TangyoonBoss") == 1) {
			talk = "製作了可怕的料理。請擊敗被氣味吸引而來的垃圾桶。";
			x = 1320;
			mobid = 9300681;
			// 4033668
			fail = true;
		} else {
			talk = "成功製作了美味的料理。請擊敗被氣味吸引而來的食神。";
			x = 523;
			mobid = 9300680;
			fail = false;
		}
		c.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(mobid), new Point(x, -69));
		c.getSession().writeAndFlush(TangyoonBOSS(fail)); // pink
		c.getSession().writeAndFlush(CField.getGameMessage(11, talk)); // pink
		c.getSession().writeAndFlush(CWvsContext.getTopMsg(talk));
		GameConstants.TangYoonMobDelete(c, mobid, false, false, true);
	}

	public static byte[] TangyoonBOSS(boolean fail) {
		final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
		mplew.writeShort(SendPacketOpcode.BOSS_ENV.getValue());
		mplew.write(0x13);
		if (fail) {
			mplew.writeMapleAsciiString("tangyoon/trash");
		} else {
			mplew.writeMapleAsciiString("tangyoon/food");
		}
		mplew.writeInt(3000);
		return mplew.getPacket();
	}

	public static void PyretBless(LittleEndianAccessor slea, MapleClient c) {
		int skillid = slea.readInt();
		if (c.getPlayer().getKeyValue(1548, "onoff") != 1L) {
			c.getPlayer().setKeyValue(1548, "onoff", "1");
		} else {
			c.getPlayer().setKeyValue(1548, "onoff", "0");
		}
	}

	public static void EnterTrueRoom(MapleClient c) {
		c.getPlayer().removeSkillCustomInfo(179875);
		c.setKeyValue("LIE_DETECTOR_WARPCOUNT", (Integer.parseInt(c.getKeyValue("LIE_DETECTOR_WARPCOUNT")) + 1) + "");
		int count = Integer.parseInt(c.getKeyValue("LIE_DETECTOR_WARPCOUNT"));
		int warptimed = (count == 1) ? 60
				: ((count == 2) ? 180
						: ((count == 3) ? 1800
								: ((count == 4) ? 3600
										: ((count == 5) ? 10800
												: ((count == 6) ? 21600 : ((count == 7) ? 43200 : 86400))))));
		c.setKeyValue("LIE_DETECTOR_WARP",  warptimed + "");
		c.getPlayer().warp(993073000); 
		c.getPlayer().getWorldGMMsg(c.getPlayer(),  "進入真相之房。帳號編號 : " + c.getAccID()); 
		FileoutputUtil.log(FileoutputUtil. 真相之房日誌, 
		        "[入場] 帳號編號 : " + c.getAccID()  + " | 角色編號 : " + c.getPlayer().getId() 
		        + " | 角色暱稱 : " + c.getPlayer().getName()  + " 進入真相之房");
		c.send(CField.getTrueRoomClock(warptimed  * 1000));
		c.getPlayer().getClient().send(CWvsContext.enableActions(c.getPlayer())); 
	}

	public static void ZeroShockWave(LittleEndianAccessor slea, MapleClient c) {
		slea.skip(4);
		int skillid = slea.readInt();
		Point pos = slea.readPos();
		slea.skip(1);
		SecondaryStatEffect a = SkillFactory.getSkill(skillid).getEffect(c.getPlayer().getSkillLevel(skillid));
		MapleMist newmist = new MapleMist(a.calculateBoundingBox(pos, c.getPlayer().isFacingLeft()), c.getPlayer(), a,
				3000, (byte) (c.getPlayer().isFacingLeft() ? 1 : 0));
		newmist.setPosition(pos);
		newmist.setDelay(0);
		c.getPlayer().getMap().spawnMist(newmist, false);
	}

	public static void PoisonLegion(LittleEndianAccessor slea, MapleClient c) {
		SecondaryStatEffect Effect = SkillFactory.getSkill(2111013).getEffect(c.getPlayer().getSkillLevel(2111013));
		byte Unk = slea.readByte();
		int Size = slea.readInt();
		for (int i = 0; i < Size; i++) {
			Point Position = slea.readIntPos();
			MapleMist mist = new MapleMist(Effect.calculateBoundingBox(Position, c.getPlayer().isFacingLeft()),
					c.getPlayer(), Effect, 30000, (byte) (c.getPlayer().isFacingLeft() ? 1 : 0));
			mist.setPosition(Position);
			mist.setStartTime(System.currentTimeMillis());
			c.getPlayer().getMap().spawnMist(mist, false);
		}
	}

	public static void ZeroLuckyScroll(MapleClient c, LittleEndianAccessor slea) {
		MapleQuest quest;
		String stringa;
		MapleQuestStatus queststatus;
		MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
		slea.skip(4);
		int slot = slea.readShort();
		slea.skip(2);
		Item beta = c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) -10);
		Item scroll = c.getPlayer().getInventory(MapleInventoryType.USE).getItem((short) slot);
		Equip nbetatype = (Equip) beta;
		switch (scroll.getItemId()) {
		case 2048900:
		case 2048901:
		case 2048902:
		case 2048903:
		case 2048904:
		case 2048905:
		case 2048906:
		case 2048907:
		case 2048912:
		case 2048913:
		case 2048915:
		case 2048918:
			if (!Randomizer.isSuccess(ii.getSuccess(scroll.getItemId(), c.getPlayer(), nbetatype))) {
				c.getPlayer().getMap().broadcastMessage(c.getPlayer(),
						CField.showSoulScrollEffect(c.getPlayer().getId(), (byte) 0, false, nbetatype), true);
				break;
			}
			quest = MapleQuest.getInstance(41907);
			stringa = String.valueOf(GameConstants.getLuckyInfofromItemId(scroll.getItemId()));
			c.getPlayer().setKeyValue(46523, "luckyscroll", stringa);
			queststatus = new MapleQuestStatus(quest, 1);
			queststatus.setCustomData((stringa == null) ? "0" : stringa);
			c.getPlayer().updateQuest(queststatus, true);
			c.getPlayer().getMap().broadcastMessage(c.getPlayer(),
					CField.showSoulScrollEffect(c.getPlayer().getId(), (byte) 1, false, nbetatype), true);
			MapleInventoryManipulator.removeById(c.getPlayer().getClient(), MapleInventoryType.USE, scroll.getItemId(),
					1, true, false);
			break;
		}
		c.getPlayer().getClient().send(CWvsContext.enableActions(c.getPlayer()));
	}

	public static void KinesisGround(MapleClient c, LittleEndianAccessor slea) {
		int skillid = slea.readInt();
		short unk = slea.readShort();
		int posx = slea.readInt();
		slea.skip(1);
		int posy = slea.readInt();
		slea.skip(4);
		int size = slea.readShort();
		for (int i = 0; i < size; i++) {
			int mobid = slea.readInt();
			MapleMonster monster = c.getPlayer().getMap().getMonsterByOid(mobid);
			SecondaryStatEffect eff = SkillFactory.getSkill(skillid).getEffect(c.getPlayer().getSkillLevel(skillid));
			List<Triple<MonsterStatus, MonsterStatusEffect, Long>> statusz = new ArrayList<>();
			List<Pair<MonsterStatus, MonsterStatusEffect>> applys = new ArrayList<>();
			if ((skillid == 142111006 || skillid == 142120003) && monster != null) {
				int effect = (skillid == 142120003) ? 3 : 2;
				if (c.getPlayer().getSkillLevel(142120036) > 0) {
					effect *= 2;
				}
				if (monster.getBuff(skillid) == null) {
					statusz.add(new Triple<>(MonsterStatus.MS_PsychicGroundMark,
							new MonsterStatusEffect(skillid, eff.getDuration()), Long.valueOf(size * effect)));
					statusz.add(new Triple<>(MonsterStatus.MS_Speed,
							new MonsterStatusEffect(skillid, eff.getDuration()), Long.valueOf(-size)));
					statusz.add(new Triple<>(MonsterStatus.MS_IndieMdr,
							new MonsterStatusEffect(skillid, eff.getDuration()), Long.valueOf(-(size * effect))));
					statusz.add(new Triple<>(MonsterStatus.MS_IndiePdr,
							new MonsterStatusEffect(skillid, eff.getDuration()), Long.valueOf(-(size * effect))));
					for (Triple<MonsterStatus, MonsterStatusEffect, Long> status : statusz) {
						if (status.left != null && status.mid != null) {
							((MonsterStatusEffect) status.mid).setValue(((Long) status.right).longValue());
							applys.add(new Pair<>((MonsterStatus) status.left, (MonsterStatusEffect) status.mid));
						}
					}
					monster.applyStatus(c, applys, eff);
				}
			}
		}
		slea.skip(2);
		if (skillid == 400021008) {
			MapleCharacter player = c.getPlayer();
			SecondaryStatEffect eff = SkillFactory.getSkill(400021008).getEffect(player.getSkillLevel(400021008));
			player.setSkillCustomInfo(400021008, player.getSkillCustomValue0(400021008) + 1L, 0L);
			if (player.getSkillCustomValue0(400021008) >= 20L) {
				player.setSkillCustomInfo(400021008, 0L, 0L);
				player.setSkillCustomInfo(400021009, player.getSkillCustomValue0(400021009) + 1L, 0L);
				int duration = (int) player.getBuffLimit(400021008);
				eff.applyTo(player, duration);
			}
		}
	}

	public static void MedalReissuance(MapleClient c, LittleEndianAccessor slea) {
		int questid = slea.readInt();
		int itemid = slea.readInt();
		if (c.getPlayer().getQuestStatus(questid) == 2) {
			MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
			if (!MapleInventoryManipulator.checkSpace(c, itemid, 1, "")) {
				return;
			}
			c.getPlayer().gainItem(itemid, 1);
			c.getPlayer().gainMeso(-100L, false);
		}
	}

	public static void PinkBeanRollingGrade(MapleClient c, LittleEndianAccessor slea) {
		SkillFactory.getSkill(131001004).getEffect(c.getPlayer().getSkillLevel(131001004)).applyTo(c.getPlayer(),
				false);
	}

	public static void DebuffObjHit(MapleClient c, LittleEndianAccessor slea) {
		int type = slea.readInt();
		int id = slea.readInt();
		if (c.getPlayer().isAlive() && c.getPlayer().getBuffedEffect(SecondaryStat.NotDamaged) == null
				&& c.getPlayer().getBuffedEffect(SecondaryStat.IndieNotDamaged) == null) {
			c.getPlayer().giveDebuff(SecondaryStat.GiveMeHeal, MobSkillFactory.getMobSkill(182, 1));
		}
	}

	public static void NoteHandle(MapleClient c, LittleEndianAccessor slea) {
		String name;
		String msg;
		int ch;
		MapleCharacter target;
		switch (slea.readByte()) {
		case 0: // 보내기
			name = slea.readMapleAsciiString();
			msg = slea.readMapleAsciiString();
			ch = World.Find.findChannel(name);
			target = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(name);
			if (target != null) {
				target.getClient().send(CSPacket.NoteHandler(16, 0));
				c.getPlayer().sendNote(name, msg, 6, c.getPlayer().getId());
				c.getPlayer().sendNote(name, msg, 7, c.getPlayer().getId());
				c.getPlayer().showsendNote();
				c.send(CSPacket.NoteHandler(8, 0));
				int id = 0;
				Connection con = null;
				PreparedStatement ps = null;
				ResultSet rs = null;
				try {
					con = DatabaseConnection.getConnection();
					ps = con.prepareStatement(
							"SELECT * FROM `notes` WHERE `from` = ? AND `message` = ? AND `type` = 7");
					ps.setString(1, name);
					ps.setString(2, msg);
					rs = ps.executeQuery();
					if (rs.next()) {
						id = rs.getInt("id");
					}
					rs.close();
					ps.close();
					con.close();
				} catch (SQLException e) {
					System.err.println("Error getting character default" + e);
				} finally {
					try {
						if (ps != null) {
							ps.close();
						}
						if (rs != null) {
							rs.close();
						}
						if (con != null) {
							con.close();
						}
					} catch (Exception exception) {
					}
					c.send(CSPacket.SendNote(id, target.getId(), c.getPlayer(), name, msg, System.currentTimeMillis()));
				}
			}
			break;
		case 4:
			c.getPlayer().showNote();
			break;
		}
	}

	public static void NoteHandler(MapleClient c, LittleEndianAccessor slea) {
		int id;
		switch (slea.readByte()) {
		case 2:
			slea.skip(4);
			id = slea.readInt();
			c.send(CSPacket.NoteHandler(11, id));
			c.getPlayer().deleteNote(id, 0);
			break;
		case 3:
			slea.skip(4);
			id = slea.readInt();
			c.send(CSPacket.NoteHandler(13, id));
			c.getPlayer().deleteNote(id, 0);
			break;
		case 5:
			slea.skip(1);
			id = slea.readInt();
			c.getPlayer().showNotes(id);
			break;
		}
	}

	public static void MapleCabiNet(MapleClient c, LittleEndianAccessor slea) {
		List<MapleCabinet> cabinet;
		int get;
		boolean give;
		int i;
		switch (slea.readInt()) {
		case 0:
			cabinet = c.getCabiNet();
			Collections.reverse(cabinet);
			if (!cabinet.isEmpty()) {
				List<MapleCabinet> remove = new ArrayList<>();
				boolean auto = false;
				for (MapleCabinet ca : cabinet) {
					if (ca.getSaveTime() <= PacketHelper.getKoreanTimestamp(System.currentTimeMillis())
							|| ca.getDelete() == 1) {
						if (ca.getSaveTime() <= PacketHelper.getKoreanTimestamp(System.currentTimeMillis())) {
							auto = true;
						}
						remove.add(ca);
					}
				}
				boolean change = false;
				for (MapleCabinet cr : remove) {
					change = true;
					cabinet.remove(cr);
					if (auto) {
						c.getPlayer().dropMessage(5, "[通知]保管箱中的 "
								+ MapleItemInformationProvider.getInstance().getName(cr.getItemid()) + " 因保管期滿而被删除.");
					}
				}
				if (change) {
					Collections.reverse(cabinet);
					c.saveCabiNet(cabinet);
				}
			}
			c.send(CField.getMapleCabinetList(cabinet, false, 0, false));
			break;
		case 1:
			cabinet = c.getCabiNet();
			if (!cabinet.isEmpty()) {
				List<MapleCabinet> remove = new ArrayList<>();
				boolean auto = false;
				for (MapleCabinet ca : cabinet) {
					if (ca.getSaveTime() <= PacketHelper.getKoreanTimestamp(System.currentTimeMillis())
							|| ca.getDelete() == 1) {
						if (ca.getSaveTime() <= PacketHelper.getKoreanTimestamp(System.currentTimeMillis())) {
							auto = true;
						}
						remove.add(ca);
					}
				}
				boolean change = false;
				for (MapleCabinet cr : remove) {
					change = true;
					cabinet.remove(cr);
					if (auto) {
						c.getPlayer().dropMessage(5, "“[通知]保管箱中的 "
								+ MapleItemInformationProvider.getInstance().getName(cr.getItemid()) + " “已在保管期滿時删除.");
					}
				}
				if (change) {
					c.saveCabiNet(cabinet);
				}
			}
			break;
		case 4:
			get = slea.readInt();
			give = true;
			for (i = 1; i <= 6; i++) {
				if (c.getPlayer().getInventory(MapleInventoryType.getByType((byte) i)).getNextFreeSlot() <= -1) {
					give = false;
					break;
				}
			}
			if (give) {
				List<MapleCabinet> list1 = c.getCabiNet();
				List<MapleCabinet> look = new ArrayList<>();
				Collections.reverse(list1);
				if (((MapleCabinet) list1.get(get - 1)).getPlayerid() > 0) {
					/*
					 * if (((MapleCabinet) list1.get(get - 1)).getPlayerid() !=
					 * c.getPlayer().getId()) { // 임시 처리 c.getPlayer().dropMessage(1, "해당 아이템은 <" +
					 * ((MapleCabinet) list1.get(get - 1)).getName() + "> 캐릭터로 수령이 가능 합니다.");
					 * c.send(CWvsContext.enableActions(c.getPlayer())); return; } 共享倉庫？
					 */
					if (((MapleCabinet) list1.get(get - 1)).getItemid() / 1000000 != 1) {
						c.getPlayer().gainItem(((MapleCabinet) list1.get(get - 1)).getItemid(),
								(short) ((MapleCabinet) list1.get(get - 1)).getCount());
					} else {
						Calendar ocal = Calendar.getInstance();
						int day2 = ocal.get(7);
						int check = (day2 == 1) ? 1
								: ((day2 == 2) ? 7
										: ((day2 == 3) ? 6
												: ((day2 == 4) ? 5
														: ((day2 == 5) ? 4
																: ((day2 == 6) ? 3 : ((day2 == 7) ? 2 : 0))))));
						Equip item = (Equip) MapleItemInformationProvider.getInstance()
								.getEquipById(((MapleCabinet) list1.get(get - 1)).getItemid());
						Calendar targetCal = new GregorianCalendar(ocal.get(1), ocal.get(2), ocal.get(5));
						item.setExpiration(targetCal.getTimeInMillis() + (86400000 * check));
						if (item.getItemId() == 1672083) {
							item.setState((byte) 20);
							item.setLines((byte) 3);
							item.setPotential1(40601);
							item.setPotential2(30291);
							item.setPotential3(42061);
							item.setPotential4(42060);
							item.setPotential5(42060);
							item.setEnhance((byte) 15);
						} else if (item.getItemId() == 1672085 || item.getItemId() == 1672086) {
							item.setState((byte) 20);
							item.setLines((byte) 2);
							item.setPotential1(40601);
							item.setPotential2(30291);
							item.setEnhance((byte) 15);
						}
						MapleInventoryManipulator.addbyItem(c, item);
					}
				} else {
					c.getPlayer().gainItem(((MapleCabinet) list1.get(get - 1)).getItemid(),
							(short) ((MapleCabinet) list1.get(get - 1)).getCount());
				}
				c.send(CWvsContext.InfoPacket.getShowItemGain(((MapleCabinet) list1.get(get - 1)).getItemid(),
						(short) ((MapleCabinet) list1.get(get - 1)).getCount(), true));
				for (MapleCabinet change : c.getCabiNet()) {
					if (change.getItemid() == ((MapleCabinet) list1.get(get - 1)).getItemid()
							&& change.getDelete() == 0) {
						change.setDelete(1);
						break;
					}
				}
				Collections.reverse(list1);
				c.saveCabiNet(list1);
				for (MapleCabinet add : c.getCabiNet()) {
					if (add.getDelete() == 0) {
						look.add(add);
					}
				}
				Collections.reverse(look);
				c.send(CField.getMapleCabinetList(look, true, get, false));
				break;
			}
			c.getPlayer().dropMessage(1, "請清空背包空間後重試.");
			c.send(CWvsContext.enableActions(c.getPlayer()));
			break;
		}
	}

	public static void ExpPocket(MapleClient c, LittleEndianAccessor slea) {
		c.removeClickedNPC();
		NPCScriptManager.getInstance().dispose(c);
		c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
		NPCScriptManager.getInstance().start(c, 2007, "ExpPocket");
	}

	public static void ChatEmoticon(LittleEndianAccessor slea, MapleClient c) {
		short slot1, s1;
		int emoticon;
		short slot, slot2, s2;
		String str;
		MapleSavedEmoticon em;
		byte type = slea.readByte();
		switch (type) {
		case 1:
		case 9:
			slot1 = slea.readShort();
			slot2 = slea.readShort();
			c.getSession().writeAndFlush(CField.getChatEmoticon(type, slot1, slot2, 0, ""));
			break;
		case 2:
			s1 = slea.readShort();
			c.getPlayer().getEmoticonTabs().remove(s1 - 1);
			c.getSession().writeAndFlush(CField.getChatEmoticon(type, s1, (short) 0, 0, ""));
			break;
		case 5:
			emoticon = slea.readInt();
			s2 = c.getPlayer().getEmoticonFreeSlot();
			c.getPlayer().getEmoticonBookMarks().add(new Pair<>(Integer.valueOf(emoticon), Short.valueOf(s2)));
			for (MapleChatEmoticon a : c.getPlayer().getEmoticonTabs()) {
				if (emoticon / 10000 == a.getEmoticonid()) {
					a.getBookmarks().add(new Pair<>(Integer.valueOf(emoticon), Short.valueOf(s2)));
					break;
				}
			}
			c.getSession().writeAndFlush(CField.getChatEmoticon(type, s2, (short) 0, emoticon, ""));
			break;
		case 6:
			emoticon = slea.readInt();
			for (Pair<Integer, Short> a : c.getPlayer().getEmoticonBookMarks()) {
				if (((Integer) a.left).intValue() == emoticon) {
					c.getPlayer().getEmoticonBookMarks().remove(a);
					break;
				}
			}
			label44: for (MapleChatEmoticon a : c.getPlayer().getEmoticonTabs()) {
				for (Pair<Integer, Short> b : a.getBookmarks()) {
					if (((Integer) b.left).intValue() == emoticon) {
						a.getBookmarks().remove(b);
						break label44;
					}
				}
			}
			c.getSession().writeAndFlush(CField.getChatEmoticon(type, (short) 0, (short) 0, emoticon, ""));
			break;
		case 8:
			emoticon = slea.readInt();
			str = slea.readMapleAsciiString();
			em = new MapleSavedEmoticon(c.getPlayer().getId(), emoticon, str);
			c.getPlayer().getSavedEmoticon().add(em);
			c.getSession().writeAndFlush(CField.getChatEmoticon(type, (short) c.getPlayer().getSavedEmoticon().size(),
					(short) 0, emoticon, str));
			break;
		case 10:
			slot = slea.readShort();
			c.getPlayer().getSavedEmoticon().remove(slot - 1);
			c.getSession().writeAndFlush(CField.getChatEmoticon(type, slot, (short) 0, 0, ""));
			break;
		}
		c.getPlayer().getEmoticons().clear();
		ChatEmoticon.LoadChatEmoticons(c.getPlayer(), c.getPlayer().getEmoticonTabs());
	}

	public static void applySpecialCoreSkills(MapleCharacter chr) {
		long time = System.currentTimeMillis();
		boolean eff = false;
		Pair<Integer, Integer> coredata = chr.getEquippedSpecialCore();
		if (coredata == null) {
			return;
		}
		int coreId = ((Integer) coredata.left).intValue();
		int skillId = ((Integer) coredata.right).intValue();
		chr.dropMessageGM(5, coreId + "?" + skillId);
		switch (coreId) {
		case 30000008:
		case 30000020:
			if (!chr.skillisCooling(skillId) && Randomizer.isSuccess(1, 1000)) {
				SecondaryStatEffect effect = SkillFactory.getSkill(skillId)
						.getEffect(chr.getSkillLevel(chr.getSkillLevel(skillId)));
				effect.applyTo(chr);
				chr.addCooldown(skillId, time, 30000L);
				eff = true;
			}
			break;
		case 30000009:
		case 30000012:
		case 30000016:
		case 30000018:
		case 30000023:
			if (!chr.skillisCooling(skillId)) {
				SecondaryStatEffect effect = SkillFactory.getSkill(skillId)
						.getEffect(chr.getSkillLevel(chr.getSkillLevel(skillId)));
				effect.applyTo(chr);
				chr.addCooldown(skillId, time, 120000L);
				eff = true;
			}
			break;
		}
		if (eff) {
			chr.getClient().getSession()
					.writeAndFlush(CField.EffectPacket.showEffect(chr, "Effect/CharacterEff.img/VMatrixSP"));
		}
	}

	public static void ItemSortLock(MapleClient c, LittleEndianAccessor slea) {
		boolean isOn = false;
		if (!isOn) {
			c.getPlayer().getClient().getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
			return;
		}

		slea.readInt();
		int InvType = slea.readInt();
		int slot = slea.readInt();
		byte isLock = slea.readByte();
		boolean lock = false;

		if (isLock == 1) {
			lock = true;
		} else {
			lock = false;
		}

		MapleInventoryType type = MapleInventoryType.getByType((byte) InvType);
		Item item = c.getPlayer().getInventory(type).getItem((short) slot);

		switch (InvType) {
		case 1: // Equip
			c.getPlayer().getClient().getSession().writeAndFlush(CWvsContext.InventoryPacket.ItemSortLockEquip(type,
					(short) slot, item.getItemId(), item.getQuantity(), lock));
			break;
		case 2: // Consume
			c.getPlayer().getClient().getSession().writeAndFlush(CWvsContext.InventoryPacket.ItemSortLockConsume(type,
					(short) slot, item.getItemId(), item.getQuantity(), lock));
			break;
		case 3: // Install
			c.getPlayer().getClient().getSession().writeAndFlush(CWvsContext.InventoryPacket.ItemSortLockInstall(type,
					(short) slot, item.getItemId(), item.getQuantity(), lock));
			break;
		case 4: // Etc
			c.getPlayer().getClient().getSession().writeAndFlush(CWvsContext.InventoryPacket.ItemSortLockEtc(type,
					(short) slot, item.getItemId(), item.getQuantity(), lock));
			break;
		case 5: // Cash
			c.getPlayer().getClient().getSession().writeAndFlush(CWvsContext.InventoryPacket.ItemSortLockCash(type,
					(short) slot, item.getItemId(), item.getQuantity(), lock));
			break;
		case 6: // Cody
			c.getPlayer().getClient().getSession().writeAndFlush(CWvsContext.InventoryPacket.ItemSortLockCody(type,
					(short) slot, item.getItemId(), item.getQuantity(), lock));
			break;
		}
		c.getPlayer().getClient().getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
	}

	public static void HitErdaSpectrum(MapleClient c, LittleEndianAccessor slea) {
		MapleCharacter chr = c.getPlayer();
		chr.addSkillCustomInfo(450001400, 10L);
		c.send(SLFCGPacket.ErdaSpectrumGauge((int) c.getPlayer().getSkillCustomValue0(450001400),
				(int) c.getPlayer().getSkillCustomValue0(8641018), 0));
		chr.getClient().send(SLFCGPacket.EventSkillOnEffect((chr.getPosition()).x, (chr.getPosition()).y, 2, 10));
	}

	public static void ActErdaSpectrum(MapleClient c, LittleEndianAccessor slea) {
		c.getPlayer().getMap().setCustomInfo(450001400, 1, 0);
		c.getPlayer().removeSkillCustomInfo(450001401);
		c.getPlayer().getMap().killAllMonsters(true);
		MapleMonster monster = MapleLifeFactory.getMonster(8641018);
		c.getPlayer().getMap().spawnMonsterOnGroundBelow(monster, new Point(483, 47));
		monster.setController(c.getPlayer());
		monster.setSchedule(Timer.MobTimer.getInstance().register(() -> {
			c.getPlayer().addSkillCustomInfo(450001400, -1L);
			c.getPlayer().setSkillCustomInfo(450001402, Randomizer.rand(0, 2), 0L);
			if (monster.getCustomValue(450001401) == null) {
				monster.setCustomInfo(450001401, 0, Randomizer.rand(7000, 10000));
				int x = Randomizer.rand(-100, 1000);
				c.send(SLFCGPacket.ErdaSpectrumArea(new int[] { 31, 23804, 0, 3000, -120, -154, 120, 5, x, 47 }));

			}
			c.send(SLFCGPacket.ErdaSpectrumGauge((int) c.getPlayer().getSkillCustomValue0(450001400),
					(int) c.getPlayer().getSkillCustomValue0(8641018),
					(int) c.getPlayer().getSkillCustomValue0(450001402)));
			if (c.getPlayer().getSkillCustomValue0(450001400) <= 0L) {
				c.getPlayer().removeSkillCustomInfo(450001402);
				c.getPlayer().getMap().killAllMonsters(true);
				c.getPlayer().getMap().setCustomInfo(450001400, 0, 0);
				c.send(SLFCGPacket.ErdaSpectrumType(2));
				c.getPlayer().setSkillCustomInfo(450001401, 0L, Randomizer.rand(5000, 7000));
			}
		}, 1000L));
		c.send(SLFCGPacket.ErdaSpectrumType(3));
		c.send(CField.UIPacket.detailShowInfo("艾爾達凝聚器已激活。攻擊凝聚器可提取對應其顏色的艾爾達能量。", 3, 20, 0));
		c.send(CField.getGameMessage(11, "艾爾達凝聚器已激活。攻擊凝聚器可提取對應其顏色的艾爾達能量。"));
	}

	public static void BallErdaSpectrum(MapleClient c, LittleEndianAccessor slea) {
		int type = slea.readInt();
		int count = slea.readInt();
		MapleMonster mob = c.getPlayer().getMap().getMonsterByOid(slea.readInt());
		int add = 0;
		if (type == 0) {
			if (mob.getId() == 8641019 || mob.getId() == 8641021) {
				add = (mob.getId() == 8641019) ? 1 : 2;
			}
		} else if (mob.getId() == 8641020 || mob.getId() == 8641021) {
			add = (mob.getId() == 8641020) ? 1 : 2;
		}
		c.getPlayer().addSkillCustomInfo(8641018, add);
		c.send(SLFCGPacket.ErdaSpectrumGauge((int) c.getPlayer().getSkillCustomValue0(450001400),
				(int) c.getPlayer().getSkillCustomValue0(8641018),
				(int) c.getPlayer().getSkillCustomValue0(450001402)));
		c.getPlayer().getMap().killMonsterType(mob, 0);
		if (c.getPlayer().getSkillCustomValue0(8641018) >= 10L) {
			c.getPlayer().removeSkillCustomInfo(8641018);
			c.getPlayer().removeSkillCustomInfo(450001401);
			int warp = Randomizer.isSuccess(100) ? 450001450 : 450001500;
			c.getPlayer().warp(warp);
			if (warp == 450001450) {
				c.send(SLFCGPacket.ErdaSpectrumType(2));
				c.send(SLFCGPacket.ErdaSpectrumGauge((int) c.getPlayer().getSkillCustomValue0(450001400), 0, 50));
				c.send(SLFCGPacket.ErdaSpectrumSetting(120000, 0));
				c.send(CField.UIPacket.detailShowInfo("被埃爾達引導而來的模樣。周圍藍光一閃，隨即消失。", 3, 20, 6));
				c.send(CField.startMapEffect("  在埃爾達凝聚器過載前，請阻止周圍怪物的增加！ ", 5120025, true));
				Timer.EtcTimer.getInstance().schedule(() -> c.send(CField.removeMapEffect()), 5000L);
			}
		}
	}

	public static void InfinityFlameCircle(MapleClient client, LittleEndianAccessor slea) {
		int count = slea.readInt();
		Vmatrixstackbuff(client, false, slea, count);
	}

	public static void AfterCancel2(MapleClient c, LittleEndianAccessor slea) {
		int skillid = slea.readInt();
		int type = slea.readByte();
		if (skillid == 400011091 && c.getPlayer().getBuffedValue(skillid)) {
			c.getPlayer().cancelEffect(c.getPlayer().getBuffedEffect(skillid));
			c.getPlayer().cancelEffect(c.getPlayer().getBuffedEffect(37120012));
			(c.getPlayer()).combinationBuff = 10;
			SkillFactory.getSkill(37120012).getEffect(c.getPlayer().getSkillLevel(37120012)).applyTo(c.getPlayer(),
					false);
		}
	}

	public static void SpPortalUse(MapleClient c, LittleEndianAccessor slea) {
		int id = slea.readInt();
		String path = slea.readMapleAsciiString();
		c.send(CField.SpPortal(id, path));
	}

	public static void JobChange(LittleEndianAccessor slea, MapleClient c, MapleCharacter chr) {
		if (c.getPlayer().getJob() / 100 == 4) {
			Equip test2 = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) -10);
			if (test2 != null) {
				chr.dropMessage(1, "盾牌 輔助武器 副手必須關閉.");
				return;
			}
		}
		int BeforeJob = chr.getJob();
		int AfterJob = slea.readInt();
		int unk = slea.readByte();
		switch (BeforeJob) {
		case 112:
		case 122:
		case 132:
		case 212:
		case 222:
		case 232:
		case 312:
		case 322:
		case 333:
		case 412:
		case 422:
		case 434:
		case 512:
		case 522:
		case 532:
			break;
		default:
			return;
		}
		if (BeforeJob / 100 != AfterJob / 100 || BeforeJob / 1000 != 0 || AfterJob / 1000 != 0) {
			chr.dropMessage(1, "職業轉換錯誤1");
			return;
		}
		long needmeso = 0L;
		if (c.getPlayer().getLevel() <= 105) {
			needmeso = 10000000L;
		} else {
			needmeso = (10000000 + (c.getPlayer().getLevel() - 105) * (c.getPlayer().getLevel() - 105) * 50000);
		}
		if (needmeso > c.getPlayer().getMeso()) {
			chr.dropMessage(1, "缺少自由轉職的方法.");
			return;
		}
		for (Core corez : c.getPlayer().getCore()) {
			corez.setState(1);
			corez.setPosition(-1);
		}
		MatrixHandler.calcSkillLevel(c.getPlayer(), -1);
		chr.getClient().send(CWvsContext.UpdateCore(chr, 1));
		chr.getClient().send(CField.UIPacket.closeUI(3));
		chr.dispel();
		for (Pair<SecondaryStat, SecondaryStatValueHolder> data : chr.getEffects()) {
			SecondaryStatValueHolder mbsvh = (SecondaryStatValueHolder) data.right;
			if (SkillFactory.getSkill(mbsvh.effect.getSourceId()) != null && mbsvh.effect.getSourceId() != 80002282
					&& mbsvh.effect.getSourceId() != 2321055) {
				chr.cancelEffect(mbsvh.effect, Arrays.asList(new SecondaryStat[] { (SecondaryStat) data.left }));
			}
		}
		chr.gainMeso(-needmeso, false, true);
		for (Skill sk : chr.getSkills().keySet()) {
			if (PacketHelper.jobskill(c.getPlayer(), sk.getId())) {
				chr.changeSkillLevel(sk.getId(), (byte) 0, (byte) 0);
			}
		}
		c.getPlayer().AutoTeachSkillZero();
		chr.changeJob(AfterJob);
		c.getPlayer().AutoTeachSkill();
		c.removeClickedNPC();
		NPCScriptManager.getInstance().dispose(c);
		c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
	}

	public static void LaraPoint(MapleClient c, LittleEndianAccessor slea) {
		int type = slea.readByte();
		int type2 = slea.readInt();
		int pointT = slea.readByte();
		Point pos = slea.readIntPos();
		List<SecondAtom> atoms = new ArrayList<>();
		atoms.add(new SecondAtom(20, c.getPlayer().getId(), 0, 0, 162101000, 0, 0, 0, pos,
				Arrays.asList(new Integer[] { Integer.valueOf(pointT), Integer.valueOf(type2) })));
		c.getPlayer().spawnSecondAtom(atoms);
	}

	public static void UseSecondAtom(MapleClient c, LittleEndianAccessor slea) {
		int type = slea.readInt();
		int objid = slea.readInt();
		int unk = slea.readInt();
		int unk1 = slea.readInt();
		int unk2 = slea.readInt();
		int unk3 = slea.readInt();
		int unk4 = slea.readInt();
		SecondAtom sa = c.getPlayer().getSecondAtom(objid);
		if (sa != null && unk4 == 1) {
			c.getSession().writeAndFlush(CField.skillCooldown(162121001,
					SkillFactory.getSkill(162121001).getEffect(c.getPlayer().getSkillLevel(162121001)).getU2() * 1000));
			c.getPlayer().addCooldown(162121001, System.currentTimeMillis(),
					(SkillFactory.getSkill(162121001).getEffect(c.getPlayer().getSkillLevel(162121001)).getU2()
							* 1000));
		}
	}

	public static void Lotus(LittleEndianAccessor slea, MapleClient c) {
		/*
		 * if (c.getPlayer().Lotus) {
		 * SkillFactory.getSkill(400001062).getEffect(1).applyTo(c.getPlayer());
		 * c.getPlayer().Lotus = false; SecondaryStatEffect effect =
		 * SkillFactory.getSkill(400001061).getEffect(c.getPlayer().getSkillLevel(
		 * 400001061));
		 * 
		 * c.getPlayer().addCooldown(400001061, System.currentTimeMillis(),
		 * effect.getW() * 1000);
		 * c.getSession().writeAndFlush(CField.skillCooldown(400001061, effect.getW() *
		 * 1000)); }
		 */
	}

	public static void Lotus2(LittleEndianAccessor slea, MapleClient c) {
	}

	public static void FlashMirage(LittleEndianAccessor slea, MapleClient c) {
		slea.skip(1);
		List<SecondAtom2> eff = SkillFactory.getSkill(3111016).getSecondAtoms();
		int size = slea.readInt();
		for (int i = 0; i < size; i++) {
			int x = slea.readInt();
			int y = slea.readInt();
			byte direction = slea.readByte();
			int rotate = slea.readInt();
			slea.skip(4);
			int finally_x = slea.readInt();
			c.send(CField.EffectPacket.showEffect(c.getPlayer(), 0, 3111015, 84, rotate, finally_x, direction, true,
					new Point(x, y), null, null));
			c.getPlayer().getMap().broadcastMessage(c.getPlayer(), CField.EffectPacket.showEffect(c.getPlayer(), 0,
					3111015, 84, rotate, finally_x, direction, false, new Point(x, y), null, null), false);
			c.getPlayer().createSecondAtom(eff.get(3), new Point(x, y), true);
		}
	}

	public static void MemoryChoice(LittleEndianAccessor slea, MapleClient c) {
		int skillid = slea.readInt();
		c.getPlayer().getClient().getSession().writeAndFlush(SkillPacket.메모리초이스(skillid));
		c.getPlayer().unstableMemorize = skillid;
		c.getSession().writeAndFlush((Object) CField.skillCooldown(400001063, 10000));
		c.getPlayer().addCooldown(400001063, System.currentTimeMillis(), 10000);
	}

	public static void CodyPresetApply(LittleEndianAccessor slea, MapleClient c) {
		List<Pair<Integer, Integer>> list = new ArrayList<>();
		MapleCharacter chr = c.getPlayer();
		// slea.readInt(); // 1.2.390 --
		int preset = slea.readInt();
		// int slot = slea.readInt(); // 1.2.390 --
		chr.resetCodyPreset(preset);

		c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
		c.getPlayer().dropMessage(1, "預設已變更.");

		chr.setKeyValue(913, "preset", preset + "");
		chr.updateInfoQuest(27043,
				"preset0=" + chr.getKeyValue(913, "preset") + ";preset1=" + chr.getKeyValue(913, "preset") + ";preset2="
						+ chr.getKeyValue(913, "preset") + ";preset_new=" + chr.getKeyValue(913, "preset"));
		c.getSession().writeAndFlush((Object) (CField.CodyPreset(c.getPlayer(), preset, (byte) 1))); // 1.2.390 --
	}

	public static void dotgerecv(LittleEndianAccessor slea, MapleClient c) {
		int skillid = slea.readInt();
		slea.skip(4);
		SecondaryStatEffect effect;
		if (c.getPlayer().getSkillLevel(skillid) > 0
				&& (effect = SkillFactory.getSkill(skillid).getEffect(c.getPlayer().getSkillLevel(skillid)))
						.makeChanceResult()) {
			c.getPlayer().getClient().getSession().writeAndFlush((Object) CField.getDotge());
			c.getPlayer().setSkillCustomInfo(3310005, 0L, 1000L);

		}

	}

	public static void CurseEnchant(LittleEndianAccessor slea, MapleClient c) {
		int skillid = slea.readInt();
		Map<SecondaryStat, Pair<Integer, Integer>> localstatups = new HashMap<>();
		SecondaryStatEffect effect = SkillFactory.getSkill(skillid)
				.getEffect(c.getPlayer().getSkillLevel(c.getPlayer().getSkillLevel(skillid)));
		localstatups.clear();

		if (c.getPlayer().getBuffedValue(skillid)) {
			final Map<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
			statups.put(SecondaryStat.CurseEnchant, new Pair<Integer, Integer>(50, 0));
			c.getSession().writeAndFlush(CWvsContext.BuffPacket.cancelBuff(statups, c.getPlayer()));
			c.getPlayer().cancelEffect(c.getPlayer().getBuffedEffect(SecondaryStat.CurseEnchant));
		} else {
			localstatups.put(SecondaryStat.CurseEnchant, new Pair<>(Integer.valueOf(c.getPlayer().getId()),
					Integer.valueOf((int) c.getPlayer().getBuffLimit(skillid))));
			c.send(CWvsContext.BuffPacket.giveBuff(localstatups, effect, c.getPlayer()));
			SkillFactory.getSkill(skillid).getEffect(c.getPlayer().getSkillLevel(skillid)).applyTo(c.getPlayer(),
					c.getPlayer(), false, c.getPlayer().getPosition(), (int) c.getPlayer().getBuffLimit(skillid),
					(byte) 0, false);
		}
	}

	public static void CurseEnchantSelect(LittleEndianAccessor slea, MapleClient c) {
		int type = slea.readInt();
		Map<SecondaryStat, Pair<Integer, Integer>> localstatups = new HashMap<>();
		SecondaryStatEffect effect = SkillFactory.getSkill(3321042)
				.getEffect(c.getPlayer().getSkillLevel(c.getPlayer().getSkillLevel(3321042)));
		localstatups.clear();
		localstatups.put(SecondaryStat.CurseEnchant,
				new Pair<>(type, Integer.valueOf((int) c.getPlayer().getBuffLimit(3321042))));
		c.send(CWvsContext.BuffPacket.giveBuff(localstatups, effect, c.getPlayer()));
		SkillFactory.getSkill(3321042).getEffect(c.getPlayer().getSkillLevel(3321042)).applyTo(c.getPlayer(),
				c.getPlayer(), false, c.getPlayer().getPosition(), (int) c.getPlayer().getBuffLimit(3321042), (byte) 0,
				false);

	}

	public static void bless5th(LittleEndianAccessor slea, MapleClient c) {
		MapleCharacter chr = c.getPlayer();
		if (chr == null) {
			return;
		}
		final int MAX_COUNT = 2;
		long lastUsedTime = 0;
		final long COOLDOWN_TIME = 180000; // 180 seconds in milliseconds

		long currentTime = System.currentTimeMillis();
		if (chr.메이플용사 < MAX_COUNT) {
			chr.메이플용사++;
			chr.오라웨폰++;
		}

		int skill = slea.readInt();
		slea.skip(4);
		int stack = chr.메이플용사;
		boolean check = true;
		HashMap<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
		c.getSession().writeAndFlush(CField.V_BLESS(skill, (byte) 1));
		if (skill == 400001050) {
			int[] skills = { 400001051, 400001053, 400001054, 400001055 };
			chr.nextBlessSkill = skills[Randomizer.nextInt(skills.length)];
			Map<SecondaryStat, Pair<Integer, Integer>> localstatups = new HashMap<>();
			localstatups.put(SecondaryStat.Bless5th,
					new Pair<>(stack, Integer.valueOf((int) chr.getBuffLimit(400001050))));
			c.getSession()
					.writeAndFlush(CWvsContext.BuffPacket.giveBuff(localstatups, chr.getBuffedEffect(400001050), chr));
			c.getSession().writeAndFlush(CField.EffectPacket.showEffect(chr, 0, chr.nextBlessSkill, 1, 0, 0,
					(byte) (chr.isFacingLeft() ? 1 : 0), true, null, null, null));
			chr.getMap().broadcastMessage(chr, CField.EffectPacket.showEffect(chr, 0, chr.nextBlessSkill, 1, 0, 0,
					(byte) (chr.isFacingLeft() ? 1 : 0), true, null, null, null), false);
		}
		if (skill == 400001042 || skill == 400001043 || skill == 400001046) { // 여신의 축복
			SecondaryStatEffect yoyo = SkillFactory.getSkill(400001042).getEffect(1);
			statups.put(SecondaryStat.Bless5th, new Pair<Integer, Integer>(stack, 0));
			c.getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, yoyo, c.getPlayer()));

		}

		if (skill == 400001037) {
			SecondaryStatEffect yoyo = SkillFactory.getSkill(400001042).getEffect(1);
			statups.put(SecondaryStat.VMatrixStackBuff, new Pair<Integer, Integer>(chr.오라웨폰, 0));
			statups.put(SecondaryStat.MagicCircuitFullDrive, new Pair<Integer, Integer>(chr.오라웨폰, 0));
			c.getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, yoyo, c.getPlayer()));
		}

		if (skill == 400011000) {
			SecondaryStatEffect yoyo = SkillFactory.getSkill(400011000).getEffect(1);
			statups.put(SecondaryStat.AuraWeapon, new Pair<Integer, Integer>(chr.오라웨폰, 0));
			c.getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, yoyo, c.getPlayer()));
		}
	}

	public static void ChargeSeed(LittleEndianAccessor slea, MapleClient c) {
		int skillId = slea.readInt();
		int unk = slea.readInt();

		SecondaryStatEffect effect = SkillFactory.getSkill(skillId).getEffect(c.getPlayer().getSkillLevel(skillId));
		int maxStack = 0;

		switch (skillId) {
		case 64141000: // 체인아츠:스트로크 VI 구현
			maxStack = 3;
			break;
		case 162101012:
			maxStack = effect.getW2();
			break;
		case 162111006:
			maxStack = effect.getV();
			break;
		case 162121042:
			maxStack = effect.getU();
			break;
		}

		c.getPlayer().getClient().getSession().writeAndFlush(CField.V_BLESS(skillId, (byte) 1));

		if (maxStack > 0) {
			int prevStack = c.getPlayer().getSkillCustomValues(skillId);
			if (prevStack < maxStack) {
				c.getPlayer().setSkillCustomValue(skillId, prevStack + 1);

				if (skillId == 64141000) { // 체인아츠:스트로크 VI 구현
					EnumMap<SecondaryStat, Pair<Integer, Integer>> statups = new EnumMap<SecondaryStat, Pair<Integer, Integer>>(
							SecondaryStat.class);
					statups.put(SecondaryStat.ChainArts_Stroke_Stack, new Pair<Integer, Integer>(prevStack + 1, 0));
					SecondaryStatEffect eff = SkillFactory.getSkill(skillId)
							.getEffect(c.getPlayer().getSkillLevel(skillId));
					c.getSession().writeAndFlush(CWvsContext.BuffPacket.giveBuff(statups, eff, c.getPlayer()));
				} else {
					effect.applyTo(c.getPlayer(), false);
				}
			}
		}
	}

	// 거탐
	public static void LieDetector(LittleEndianAccessor slea, MapleClient c) {
		slea.skip(4);

		String txt = slea.readMapleAsciiString();

		if (c.getLIEDETECT() == null) {
			c.send(CWvsContext.LieDetector(c, 0, c.getLieDectctCount()));
		} else if (c.getLIEDETECT().equals(txt)) {
			c.setLIEDETECT(null);
			c.setLieDectctCount(0);
			c.getPlayer().removeSkillCustomInfo(179875);
			c.send(CWvsContext.LieDetector(c, 2, 0));
			c.getPlayer().getMap().broadcastMessage(CWvsContext.LieDetectorMulti(c.getPlayer().getId(), 0));
		} else if (c.getLieDectctCount() > 3) {
			EnterTrueRoom(c);
		} else {
			c.setLieDectctCount(c.getLieDectctCount() + 1);
			c.send(CWvsContext.LieDetector(c, 0, c.getLieDectctCount()));
		}
	}

	// 거탐
	public static void SendLieDetector(LittleEndianAccessor slea, MapleClient c) {
		String name = slea.readMapleAsciiString();
		MapleCharacter chr = c.getPlayer().getMap().getCharacterByName(name);
		if (chr != null) {
			c.sendLieDetoctor(chr, true);
			c.getPlayer().gainItem(2190000, -1);
		}
	}

	public static void LieDetecTer(MapleClient c) {
		long time = System.currentTimeMillis();
		int lietime = 1200 * 1000;
		if (c.getPlayer().getLevel() >= 220 && Randomizer.isSuccess(10, 1000) && !c.getPlayer().getMap().isTown()
				&& c.getPlayer().getChair() == 0 && c.getPlayer().getEventInstance() == null
				&& c.getPlayer().getMap().getId() != ServerConstants.warpMap
				&& !GameConstants.isContentsMap(c.getPlayer().getMapId())) {
			if (c.getKeyValue("LIE_DETECT") == null) {
				c.sendLieDetoctor(c.getPlayer(), false);
			} else {
				if ((time - Long.parseLong(c.getKeyValue("LIE_DETECT")) >= lietime
						&& c.getPlayer().getSkillCustomValue(179875) == null) || c.getKeyValue("LIE_DETECT") == null) {
					c.sendLieDetoctor(c.getPlayer(), false);
				}
			}
			if (c.getKeyValue("LIE_DETECTOR_WARP") != null) {
				c.setKeyValue("LIE_DETECTOR_WARP", (Integer.parseInt(c.getKeyValue("LIE_DETECTOR_WARP")) - 1) + "");
				if (Integer.parseInt(c.getKeyValue("LIE_DETECTOR_WARP")) <= 1) {
					c.removeKeyValue("LIE_DETECTOR_WARP");
					c.getPlayer().warp(ServerConstants.warpMap);
				}
			}
		}
	}

	public static void IceAura(LittleEndianAccessor slea, MapleClient c) { // Mist
		int objectId = slea.readInt();
		int skillid = slea.readInt();

		MapleMist mist = null;
		for (MapleMist mists : c.getPlayer().getMap().getAllMistsThreadsafe()) {
			if (mists.getSourceSkill() != null && c.getPlayer().getId() == mists.getOwnerId()
					&& mists.getSourceSkill().getId() == skillid) {
				mist = mists;
				break;
			}
		}

		if (mist != null) {
			if (skillid == 2221055) {
				if (c.getPlayer().getParty() != null) {
					for (MaplePartyCharacter pchr : c.getPlayer().getParty().getMembers()) {
						MapleCharacter other = c.getChannelServer().getPlayerStorage()
								.getCharacterByName(pchr.getName());
						if (other != null && mist.getBox().contains(other.getPosition())
								&& !other.getBuffedValue(2221054)) {
							SkillFactory.getSkill(2221054).getEffect(c.getPlayer().getSkillLevel(2221054))
									.applyTo(mist.getOwner(), other);
						}
					}
				} else if (!c.getPlayer().getBuffedValue(2221054)) {
					SkillFactory.getSkill(2221054).getEffect(c.getPlayer().getSkillLevel(2221054))
							.applyTo(c.getPlayer());
				}

				for (MapleMonster mob : c.getPlayer().getMap().getAllMonster()) {
					if (mist.getBox().contains(mob.getPosition()) && mob.isAlive()
							&& mob.getBuff(MonsterStatus.MS_Speed) == null && mob.getFreezingOverlap() > 0) {
						mob.setFreezingOverlap((byte) 0);
						if (mob.getFreezingOverlap() <= 0) {
							mob.cancelStatus(MonsterStatus.MS_Speed, null);
						}
						if (mob.getFreezingOverlap() < 5) {
							mob.setFreezingOverlap((byte) (mob.getFreezingOverlap() + 1));
						}
						MonsterStatusEffect effect = new MonsterStatusEffect(2221054, 8000);
						mob.applyStatus(c, MonsterStatus.MS_Speed, effect, mist.getSource().getV(), mist.getSource());
					}
				}
			}
		}
	}

	public static void DojangHandler(LittleEndianAccessor slea, MapleClient c) {
		int type = slea.readInt();

		if (type == 0x65) {
			c.getPlayer().getMap().broadcastMessage(CField.DojangFieldSetting(slea.readInt(), slea.readInt()));
		} else if (type == 0x66) {
			int seletedMonster = 0;
			int[][] monsterList = {
					// BOSS 傷害減免, BOSS, 傷害減免, 普通
					{ 9834020, 9834021, 9834022, 9834023 }, { 9834024, 9834025, 9834026, 9834027 },
					{ 9834028, 9834029, 9834030, 9834031 } };
			int unk = slea.readInt();
			if (unk == 2 || unk == 3) {
				MapleMap map = c.getPlayer().getMap();
				double range = Double.POSITIVE_INFINITY;
				byte animation = 1;
				for (MapleMapObject monstermo : map.getMapObjectsInRange(c.getPlayer().getPosition(), range,
						Arrays.asList(new MapleMapObjectType[] { MapleMapObjectType.MONSTER }))) {
					MapleMonster mob = (MapleMonster) monstermo;
					map.killMonster(mob, c.getPlayer(), false, false, animation);
					if (unk != 3) {
						return;
					}
				}
				return;
			}
			int size = slea.readInt(); // 몬스터 크기
			int level = slea.readInt(); // 몬스터 레벨
			slea.skip(8);
			int defense = slea.readInt(); // 방어율
			slea.skip(4); // 방어율2
			slea.skip(4); // 모름
			slea.skip(4); // 모름2
			int attribute = slea.readInt();
			int monsterType = slea.readInt(); // 몬스터 타입 (0 : 일반 / 1 : 보스)
			long monsterHP = slea.readLong(); // 몬스터 체력

			if (monsterType == 0) {
				if (attribute == 1) {
					seletedMonster = monsterList[size][2];
				} else {
					seletedMonster = monsterList[size][3];
				}
			} else {
				if (attribute == 1) {
					seletedMonster = monsterList[size][0];
				} else {
					seletedMonster = monsterList[size][1];
				}
			}
			MapleMonster monster = MapleLifeFactory.getMonster(seletedMonster);
			monster.setHp(monsterHP);
			monster.changeLevel((short) level);
			c.getPlayer().getMap().spawnMonsterOnGroundBelow(monster, new Point(slea.readInt(), slea.readInt()));
		} else if (type == 0x67) {
			c.getPlayer().getStat().heal(c.getPlayer());
		}
	}
}
