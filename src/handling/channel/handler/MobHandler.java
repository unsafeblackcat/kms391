package handling.channel.handler;

import client.MapleCharacter;
import client.MapleClient;
import client.SecondaryStat;
import client.SkillFactory;
import client.inventory.MapleInventoryType;
import client.status.MonsterStatus;
import client.status.MonsterStatusEffect;
import constants.GameConstants;
import handling.channel.ChannelServer;
import scripting.EventInstanceManager;
import server.MapleInventoryManipulator;
import server.Obstacle;
import server.Randomizer;
import server.Timer;
import server.field.boss.MapleBossManager;
import server.field.boss.demian.MapleFlyingSword;
import server.field.boss.lotus.MapleEnergySphere;
import server.field.boss.lucid.Butterfly;
import server.life.*;
import server.maps.*;
import server.movement.LifeMovementFragment;
import tools.Pair;
import tools.Triple;
import tools.data.LittleEndianAccessor;
import tools.packet.CField;
import tools.packet.CWvsContext;
import tools.packet.MobPacket;
import tools.packet.SLFCGPacket;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MobHandler {

	public static final void MoveMonster(LittleEndianAccessor slea, MapleClient c, MapleCharacter chr) {
		boolean isFacingLeft;
		long time = System.currentTimeMillis();
		String tr = slea.toString();

		try {
			int oid = slea.readInt();
			if (chr == null || chr.getMap() == null) {
				return;
			}
			MapleMonster monster = chr.getMap().getMonsterByOid(oid);
			if (monster == null) {
				return;
			}

			short moveid = slea.readShort();
			monster.setCustomInfo(99991, moveid, 0);
			byte bOption = slea.readByte();
			int actionAndDir = slea.readByte();
			long targetInfo = slea.readLong();
			byte unk1 = slea.readByte(); // +342
			byte unk2 = slea.readByte(); // +342
			int skillId = (int) (targetInfo & 0xFFFFL);
			int skillLevel = (int) (targetInfo >> 16 & 0xFFFFL);
			int option = (int) (targetInfo >> 32 & 0xFFFFL);
			boolean setNextSkill = true;
			int action = actionAndDir;
			action = action < 0 ? -1 : (action >>= 1);
			if (!(monster.getId() != 8645009 && monster.getId() != 8645066
					|| actionAndDir != 30 && actionAndDir != 31)) {
				chr.getMap().broadcastMessage(CField.enforceMSG("親衛隊隊長敦凱爾：接招！ 連星星都要分開的我的終極劍氣!", 272, 2000));
			}

			boolean changeController = (bOption & 0x10) == 16;
			boolean movingAttack = (bOption & 1) == 1;

			int count = slea.readByte();
			ArrayList<Point> multiTargetForBall = new ArrayList<Point>();

			for (int i = 0; i < count; ++i) {
				multiTargetForBall.add(new Point(slea.readShort(), slea.readShort()));
			}
			count = slea.readByte();
			ArrayList<Short> randTimeForAreaAttack = new ArrayList<Short>();
			for (int i = 0; i < count; ++i) {
				randTimeForAreaAttack.add(slea.readShort());
			}

			int type2 = slea.readInt();
			if (type2 == 1) { // 1.2.372 IDA
				slea.readInt();
				slea.readInt();
				slea.readInt();
				slea.readInt();
				slea.readInt();
				slea.readInt();
				slea.readInt();
				slea.readInt();
				slea.readInt();
				slea.readInt();
				slea.readInt();
			}

			slea.readByte();

			slea.readInt();

			slea.readInt();

			slea.readInt();
			slea.readInt();

			slea.readInt();
			slea.readByte();

			int tEncodedGatherDuration = slea.readInt(); // m_tEncodedGatherDuration
			// System.err.println("tEncodedGatherDuration " + tEncodedGatherDuration);
			Point startPos = slea.readPos();
			// System.err.println("startPos " + startPos);

			Point startWobble = slea.readPos();
			// System.err.println("startWobble " + startWobble);

			List<LifeMovementFragment> res = MovementParse.parseMovement(slea, 2);
			boolean stop = false;

			slea.readByte();
			slea.readInt();
			slea.readInt();
			slea.readInt();
			slea.readInt();
			slea.readInt();

			slea.readInt();

			slea.readByte();
			slea.readByte();

			boolean cannotUseSkill = (slea.readByte() == 1);

			if (monster.getStats().isBoss()) {
				MapleBossManager.changePhase(monster);
				MapleBossManager.setBlockAttack(monster);
			}

			if (!monster.isSkillForbid()) {
				if (action >= 13 && action <= 29) {
					int attackIdx = action - 13;

					if (attackIdx < monster.getStats().getAttacks().size()) {
						MobAttack attack = monster.getStats().getAttacks().get(attackIdx);

						if (attack.getAfterAttack() >= 0) {
							chr.dropMessageGM(-8,
									"mobId : " + monster.getId() + " afterattack : " + attack.getAfterAttack()
											+ " attackcount : " + attack.getAfterAttackCount() + " attackidx : "
											+ action);
							monster.getMap()
									.broadcastMessage(MobPacket.setAfterAttack(oid, attack.getAfterAttack(),
											(attack.getAfterAttack() < 0) ? 0 : attack.getAfterAttackCount(), action,
											((actionAndDir & 0x1) != 0)));
						} else if ((attackIdx == 0 || attackIdx == 1 || attackIdx == 6 || attackIdx == 7
								|| attackIdx == 12) && (monster.getId() == 8930000 || monster.getId() == 8930100)) {
							List<Point> pos = new ArrayList<>();
							pos.add(new Point(810, 443));
							pos.add(new Point(-2190, 443));
							pos.add(new Point(-1690, 443));
							pos.add(new Point(560, 443));
							pos.add(new Point(-190, 443));
							pos.add(new Point(-690, 443));
							pos.add(new Point(-1940, 443));
							pos.add(new Point(1310, 443));
							pos.add(new Point(-1190, 443));
							pos.add(new Point(1060, 443));
							pos.add(new Point(-940, 443));
							pos.add(new Point(-1440, 443));
							pos.add(new Point(1560, 443));
							pos.add(new Point(-440, 443));
							monster.getMap().broadcastMessage(MobPacket.dropStone("DropStone", pos));
						}

						for (Triple<Integer, Integer, Integer> skill : (Iterable<Triple<Integer, Integer, Integer>>) attack
								.getSkills()) {
							MobSkill msi = MobSkillFactory.getMobSkill(((Integer) skill.left).intValue(),
									((Integer) skill.mid).intValue());
							if (msi != null) {
								if (((Integer) skill.right).intValue() > 0) {
									msi.setMobSkillDelay(c.getPlayer(), monster, ((Integer) skill.right).intValue(),
											(short) 0, ((actionAndDir & 0x1) != 0));
									continue;
								}
								if (msi.getSkillAfter() > 0) {
									msi.setMobSkillDelay(c.getPlayer(), monster, msi.getSkillAfter(), (short) 0,
											((actionAndDir & 0x1) != 0));
									continue;
								}
								if (option > 0) {
									Timer.ShowTimer.getInstance().schedule(() -> {
										if (monster.isAlive()) {
											chr.dropMessageGM(-11, "op1 : " + option);
											msi.applyEffect(chr, monster, true, ((actionAndDir & 0x1) != 0));
										}
									}, option);

									continue;
								}

								if (monster.isAlive()) {
									msi.applyEffect(chr, monster, true, ((actionAndDir & 0x1) != 0));
								}
							}

						}

					}
				} else if (action >= 30 && action <= 46 && monster.getEliteGrade() <= -1) {
					if (monster.hasSkill(skillId, skillLevel)) {
						MobSkill msi = monster.getStats().getSkill(skillId, skillLevel);
						if (msi != null) {
							if (!msi.checkDealyBuff(chr, monster)) {
								monster.setNextSkill(0);
								monster.setNextSkillLvl(0);
							} else {
								if (msi.getSkillAfter() > 0) {
									msi.setMobSkillDelay(c.getPlayer(), monster, msi.getSkillAfter(), (short) 0,
											((actionAndDir & 0x1) != 0));

									monster.setNextSkill(0);
									monster.setNextSkillLvl(0);
									if (msi.getOtherSkillID() > 0) {
										Timer.ShowTimer.getInstance().schedule(() -> {
											monster.setNextSkill(msi.getOtherSkillID());
											monster.setNextSkillLvl(msi.getOtherSkillLev());
										}, (msi.getSkillAfter() + 5000));
									}
								} else if (msi.onlyOnce() && monster.getLastSkillUsed(skillId, skillLevel) != 0L) {
									if (chr.isGM()) {
										chr.dropMessage(-8, monster.getId() + " 몬스터의 " + skillId + " / " + skillLevel
												+ " 스킬이 1회용이지만, 여러번 사용되었습니다.");
									}
								} else {
									boolean dealy = true, react = true;
									int delaytime = option;
									if ((skillId == 203 && skillLevel == 1)
											|| (skillId == 242 && (skillLevel == 10 || skillLevel == 11))) {
										dealy = false;
									}
									if (skillId == 215 && skillLevel == 4) {
										delaytime = 2000;
									}
									if (skillId == 238 && (skillLevel == 4 || skillLevel == 5 || skillLevel == 9
											|| skillLevel == 10)) {
										if (skillLevel == 10 || skillLevel == 9) {
											dealy = false;
										}
										delaytime = 960;
									}
									if (skillId == 247 || skillId == 264 || skillId == 263 || skillId == 260) {
										dealy = false;
									}
									if (skillId == 170) {
										int a;
										int x;
										int b;
										int d;
										List<Point> pos;
										switch (skillLevel) {
										case 13:
											monster.getMap().broadcastMessage(MobPacket.setAfterAttack(
													monster.getObjectId(), 8, 1, 30, ((actionAndDir & 0x1) != 0)));
											break;
										case 42:
											a = ((actionAndDir & 0x1) == 0) ? 250 : -250;
											b = ((actionAndDir & 0x1) == 0) ? 500 : -500;
											d = ((actionAndDir & 0x1) == 0) ? 800 : -800;
											pos = new ArrayList<>();
											pos.add(new Point((monster.getPosition()).x + a, -80));
											pos.add(new Point((monster.getPosition()).x + b, -80));
											pos.add(new Point((monster.getPosition()).x + d, -80));
											monster.getMap().broadcastMessage(MobPacket.AfterAttacklist(monster,
													skillId, skillLevel, ((actionAndDir & 0x1) != 0), 1, pos));
											delaytime -= 500;
											break;

										case 45:
											monster.getMap().broadcastMessage(MobPacket.AfterAttack(monster, skillId,
													skillLevel, ((actionAndDir & 0x1) != 0), 1, 3));
											break;
										case 46:
											monster.getMap().broadcastMessage(MobPacket.AfterAttack(monster, skillId,
													skillLevel, ((actionAndDir & 0x1) != 0), 1, 1));
											break;
										case 47:
										case 61:
											x = ((actionAndDir & 0x1) != 0) ? -600 : 600;
											monster.getMap().broadcastMessage(MobPacket.AfterAttack(monster, skillId,
													skillLevel, ((actionAndDir & 0x1) != 0), 1, 7));
											monster.getMap().broadcastMessage(MobPacket.TeleportMonster(monster, false,
													16, new Point((monster.getPosition()).x + x, 17)));
											react = false;
											break;
										case 48:
											monster.setNextSkill(0);
											monster.setNextSkillLvl(0);
											monster.getMap().broadcastMessage(MobPacket.setAfterAttack(
													monster.getObjectId(), 2, 1, action, monster.isFacingLeft()));
											react = false;
											break;
										}
									}
									if (react) {
										if (delaytime > 0 && dealy) {
											Timer.ShowTimer.getInstance().schedule(() -> {
												if (monster.isAlive()) {
													chr.dropMessageGM(-11, "op2 : " + option);
													msi.applyEffect(chr, monster, true, ((actionAndDir & 0x1) != 0));
												}
											}, delaytime);

										} else if (monster.isAlive()) {
											msi.applyEffect(chr, monster, true, ((actionAndDir & 0x1) != 0));
										}
									}

									monster.setNextSkill(0);
									monster.setNextSkillLvl(0);
									if (msi.getOtherSkillID() > 0) {
										Timer.ShowTimer.getInstance().schedule(() -> {
											monster.setNextSkill(msi.getOtherSkillID());
											monster.setNextSkillLvl(msi.getOtherSkillLev());
										}, (option + 3000));
									}
								}

								if (msi.getAfterAttack() >= 0) {
									monster.getMap()
											.broadcastMessage(MobPacket.setAfterAttack(oid, msi.getAfterAttack(),
													msi.getAfterAttackCount(), action, ((actionAndDir & 0x1) != 0)));
								}
							}

						} else if (chr.isGM()) {
							chr.dropMessage(-8,
									monster.getId() + " 몬스터의 " + skillId + " / " + skillLevel + " 스킬이 캐싱되지 않았습니다.");
						}

					} else if (chr.isGM()) {
						chr.dropMessage(-8,
								monster.getId() + " 몬스터의 " + skillId + " / " + skillLevel + " 스킬 사용중 오류가 발생했습니다.");
					}
				} else if (action <= 46) {
					MobAttackInfo attackInfo = MobAttackInfoFactory.getMobAttackInfo(monster, action);
					if (attackInfo != null && attackInfo.getSkill() != null) {
						monster.setNextSkill(attackInfo.getSkill().getSkill());
						monster.setNextSkillLvl(attackInfo.getSkill().getLevel());
					} else if (monster.getNoSkills() > 0) {
						List<MobSkill> useableSkills = new ArrayList<>();
						for (MobSkill msi : monster.getSkills()) {

							if (time - monster.getLastSkillUsed(msi.getSkillId(), msi.getSkillLevel()) >= 0L) {

								if (monster.getHPPercent() <= msi.getHP() && !msi.isOnlyOtherSkill()
										&& !msi.checkCurrentBuff(chr, monster)
										&& (!msi.isOnlyFsm() || msi.getSkillId() == 223)) {
									if (msi.onlyOnce() || msi.getSkillId() == 247) {
										useableSkills.clear();
										useableSkills.add(msi);
										break;
									}
									useableSkills.add(msi);
								}
							}
						}

						if (!useableSkills.isEmpty()) {
							MobSkill nextSkill = useableSkills.get(Randomizer.nextInt(useableSkills.size()));
							monster.setNextSkill(nextSkill.getSkillId());
							monster.setNextSkillLvl(nextSkill.getSkillLevel());
							monster.setLastSkillUsed(nextSkill, time, nextSkill.getInterval());
						}
					}
				}
			}

			if (monster.getEliteGradeInfo().size() > 0) {
				if (monster.getEliteGrade() >= 0 && monster.getCustomValue(9999) == null) {
					if (monster.isAlive()) {
						monster.setNextSkill(
								((Integer) ((Pair) monster.getEliteGradeInfo().get(0)).getLeft()).intValue());
						monster.setNextSkillLvl(
								((Integer) ((Pair) monster.getEliteGradeInfo().get(0)).getRight()).intValue());
						MobSkill msi2 = MobSkillFactory.getMobSkill(
								((Integer) ((Pair) monster.getEliteGradeInfo().get(0)).getLeft()).intValue(),
								((Integer) ((Pair) monster.getEliteGradeInfo().get(0)).getRight()).intValue());
						msi2.applyEffect(chr, monster, true, ((actionAndDir & 0x1) != 0));
						monster.setCustomInfo(9999, 0, (int) msi2.getInterval() * 1000);
					}
				} else if (monster.getCustomValue(9999) != null && monster.getNextSkill() > 0) {
					monster.setNextSkill(0);
					monster.setNextSkillLvl(0);
				}
			}

			if (monster.getController() != null && monster.getController().getId() != c.getPlayer().getId()) {
				if (!changeController) {
					c.getSession().writeAndFlush(MobPacket.stopControllingMonster(oid));
					return;
				}
				monster.switchController(chr, true);
			}

			if (monster != null && c != null && setNextSkill) {
				c.getSession().writeAndFlush(MobPacket.moveMonsterResponse(oid, moveid, monster.getMp(), movingAttack,
						monster.getNextSkill(), monster.getNextSkillLvl(), 0));
			}

			if (monster.getId() == 8800002 || monster.getId() == 8800102) {
				MapleBossManager.ZakumArmHandler(monster, chr, actionAndDir);
			} else if (monster.getId() == 8870000 || monster.getId() == 8870100) {
				MapleBossManager.HillaHandler(monster, chr, actionAndDir);
			} else if (GameConstants.isAggressIveMonster(monster.getId())) {
				MapleBossManager.AggressIve(monster);
			} else if (monster.getId() == 8500001 || monster.getId() == 8500002 || monster.getId() == 8500011
					|| monster.getId() == 8500012 || monster.getId() == 8500021 || monster.getId() == 8500022) {
				boolean phase1 = !(monster.getId() == 8500002 || monster.getId() == 8500012
						|| monster.getId() == 8500022);

				if (phase1) {
					if (actionAndDir == 68 || actionAndDir == 69) {
						monster.getMap().broadcastMessage(MobPacket.SpeakingMonster(monster, 2, 0));
					}
				} else if (actionAndDir == 66 || actionAndDir == 67) {
					monster.getMap().broadcastMessage(MobPacket.SpeakingMonster(monster, 1, 0));
				}
			}

			if (monster.getId() == 8880000 || monster.getId() == 8880002 || monster.getId() == 8880010) {
				if ((actionAndDir == 30 || actionAndDir == 31) && bOption != 1 && bOption != 0) {

					boolean showchat = false;
					if ((monster.getPosition().getX() > 2475.0D && actionAndDir == 31)
							|| (monster.getPosition().getX() < 1090.0D && actionAndDir == 30)) {
						showchat = true;
					}
					if (showchat) {
						chr.getMap().broadcastMessage(
								MobPacket.getSmartNotice(monster.getId(), 0, 5, 1, "매그너스가 좁은 지역에 위협을 느껴 탈출을 시도합니다."));
					}
				}

				MapleBossManager.magnusHandler(monster, 1, actionAndDir);
				MapleBossManager.magnusHandler(monster, 2, actionAndDir);
			} else if ((monster.getId() == 8880111)
					? ((actionAndDir != 26 && actionAndDir != 27) || bOption == 1 || bOption != 0)
					: ((monster.getId() != 8880341 && monster.getId() != 8880301)
							|| (actionAndDir != 32 && actionAndDir != 33) || bOption == 1 || bOption != 0)) {
			}

			if (monster.getId() == 8880405 && monster.getMap().getCustomValue0(28002) != 0
					&& monster.getMap().getCustomValue0(28002) - 150 < (monster.getPosition()).x
					&& monster.getMap().getCustomValue0(28002) + 150 > (monster.getPosition()).x) {
				monster.getMap().removeCustomInfo(28002);
				monster.getMap().broadcastMessage(CField.JinHillah(8, c.getPlayer(), c.getPlayer().getMap()));
				monster.getMap().setReqTouched(0);
				Timer.MapTimer.getInstance().schedule(() -> {
					if (monster.isAlive() && monster.getMap().getAllChracater().size() > 0
							&& monster.getMap().getCandles() == monster.getMap().getLightCandles()
							&& monster.getMap().getReqTouched() == 0) {
						monster.getMap().broadcastMessage(CField.JinHillah(6, c.getPlayer(), c.getPlayer().getMap()));

						monster.getMap().broadcastMessage(CField.enforceMSG("希拉接近消失之前，必須在祭壇上連續擊打採集鍵回收靈魂.", 254, 6000));

						monster.getMap().setReqTouched(30);
					}
				}, Randomizer.rand(10000, 20000));
			}

			if (res != null && c != null && c.getPlayer() != null && monster != null) {
				MapleMap map = c.getPlayer().getMap();
				MovementParse.updatePosition(res, (AnimatedMapleMapObject) monster, -1);
				map.moveMonster(monster, monster.getPosition());
				map.broadcastMessage(chr,
						MobPacket.moveMonster(bOption, actionAndDir, targetInfo, tEncodedGatherDuration, oid, startPos,
								startWobble, res, multiTargetForBall, randTimeForAreaAttack, cannotUseSkill),
						monster.getPosition());
			}
		} catch (Exception e) {
			// System.out.println(tr);
		}
	}

	public static final void FriendlyDamage(LittleEndianAccessor slea, MapleCharacter chr) {
		MapleMap map = chr.getMap();
		if (map == null) {
			return;
		}
		MapleMonster mobfrom = map.getMonsterByOid(slea.readInt());
		slea.skip(4);
		MapleMonster mobto = map.getMonsterByOid(slea.readInt());
		if (mobto.getId() == 8220110) {
			mobto.damage(chr, 1L, true);
			if (!mobto.isAlive()) {
				chr.getMap().broadcastMessage(
						CField.startMapEffect("환상의 꽃 보호에 실패하였습니다. 킬러 비가 환상의 꽃의 기운을 빼앗고 사라집니다.", 5120124, true));
				chr.getMap().setEliteChmpmap(false);
				chr.getMap().setElitechmpcount(0);
				chr.getMap().setElitechmptype(0);
				chr.getMap().setCustomInfo(8222222, 0, 600000);
				for (MapleMonster mob : chr.getMap().getAllMonster()) {
					if (mob.getId() == 8220111) {
						chr.getMap().killMonster(mob.getId());
					}
				}
			}
		} else if (mobfrom != null && mobto != null && mobto.getStats().isFriendly()) {
			int damage = mobto.getStats().getLevel() * Randomizer.nextInt(mobto.getStats().getLevel()) / 2;
			mobto.damage(chr, damage, true);
			checkShammos(chr, mobto, map);
		}
	}

	public static final void BindMonster(LittleEndianAccessor slea, MapleClient c) {
		MapleMap map = c.getPlayer().getMap();
		if (map == null) {
			return;
		}
		MapleMonster mob = map.getMonsterByOid(slea.readInt());
	}

	public static final void MobBomb(LittleEndianAccessor slea, MapleCharacter chr) {
		MapleMap map = chr.getMap();
		if (map == null) {
			return;
		}
		MapleMonster mobfrom = map.getMonsterByOid(slea.readInt());
		slea.skip(4);
		slea.readInt();

		if (mobfrom == null || mobfrom.getBuff(MonsterStatus.MS_TimeBomb) != null)
			;
	}

	public static final void checkShammos(MapleCharacter chr, MapleMonster mobto, MapleMap map) {
		if (!mobto.isAlive() && mobto.getStats().isEscort()) {
			for (MapleCharacter chrz : map.getCharactersThreadsafe()) {
				if (chrz.getParty() != null && chrz.getParty().getLeader().getId() == chrz.getId()) {

					if (chrz.haveItem(2022698)) {
						MapleInventoryManipulator.removeById(chrz.getClient(), MapleInventoryType.USE, 2022698, 1,
								false, true);
						mobto.heal((int) mobto.getMobMaxHp(), mobto.getMobMaxMp(), true);
						return;
					}
					break;
				}
			}
			map.broadcastMessage(CWvsContext.serverNotice(6, "", "Your party has failed to protect the monster."));
			MapleMap mapp = chr.getMap().getForcedReturnMap();
			for (MapleCharacter chrz : map.getCharactersThreadsafe()) {
				chrz.changeMap(mapp, mapp.getPortal(0));
			}
		} else if (mobto.getStats().isEscort() && mobto.getEventInstance() != null) {
			mobto.getEventInstance().setProperty("HP", String.valueOf(mobto.getHp()));
		}
	}

	public static final void MonsterBomb(int oid, MapleCharacter chr) {
		MapleMonster monster = chr.getMap().getMonsterByOid(oid);

		if (monster == null || !chr.isAlive() || chr.isHidden() || monster.getLinkCID() > 0) {
			return;
		}
		byte selfd = monster.getStats().getSelfD();
		if (selfd != -1) {
			MapleMonster mob = MapleLifeFactory.getMonster(monster.getId());
			chr.getMap().killMonster(monster, chr, false, false, (byte) 2);
			if (mob.getId() == 9833959) {
				mob.setOwner(chr.getId());
				Timer.EventTimer.getInstance()
						.schedule(() -> chr.getMap().spawnMonsterOnGroundBelow(mob, monster.getPosition()), 3000L);
			}
		}
	}

	public static final void AutoAggro(int monsteroid, MapleCharacter chr) {
		if (chr == null || chr.getMap() == null || chr.isHidden()) {
			return;
		}
		MapleMonster monster = chr.getMap().getMonsterByOid(monsteroid);

		if (monster != null && chr.getTruePosition().distanceSq(monster.getTruePosition()) < 200000.0D
				&& monster.getLinkCID() <= 0) {
			if (monster.getController() != null) {
				if (chr.getMap().getCharacterById(monster.getController().getId()) == null) {
					monster.switchController(chr, true);
				} else {
					monster.switchController(monster.getController(), true);
				}
			} else {
				monster.switchController(chr, true);
			}
		}
	}

	public static final void HypnotizeDmg(LittleEndianAccessor slea, MapleCharacter chr) {
		MapleMonster mob_from = chr.getMap().getMonsterByOid(slea.readInt());
		slea.skip(4);
		int to = slea.readInt();
		slea.skip(1);
		int damage = slea.readInt();

		MapleMonster mob_to = chr.getMap().getMonsterByOid(to);

		if (mob_from != null && mob_to != null && mob_to.getStats().isFriendly()) {
			if (damage > 30000) {
				return;
			}
			mob_to.damage(chr, damage, true);
			checkShammos(chr, mob_to, chr.getMap());
		}
	}

	public static final void MobNode(LittleEndianAccessor slea, MapleCharacter chr) {
		MapleMonster mob_from = chr.getMap().getMonsterByOid(slea.readInt());
		int newNode = slea.readInt();
		int nodeSize = chr.getMap().getNodes().size();
		if (mob_from != null && nodeSize > 0) {
			MapleNodes.MapleNodeInfo mni = chr.getMap().getNode(newNode);
			if (mni == null) {
				return;
			}
			if (mni.attr == 2) {
				switch (chr.getMapId() / 100) {
				case 9211200:
				case 9211201:
				case 9211202:
				case 9211203:
				case 9211204:
					chr.getMap().talkMonster("Please escort me carefully.", 5120035, mob_from.getObjectId());
					break;
				case 9320001:
				case 9320002:
				case 9320003:
					chr.getMap().talkMonster("Please escort me carefully.", 5120051, mob_from.getObjectId());
					break;
				}
			}
			mob_from.setLastNode(newNode);
			if (chr.getMap().isLastNode(newNode)) {
				switch (chr.getMapId() / 100) {
				case 9211200:
				case 9211201:
				case 9211202:
				case 9211203:
				case 9211204:
				case 9320001:
				case 9320002:
				case 9320003:
					chr.getMap().broadcastMessage(CWvsContext.serverNotice(5, "", "Proceed to the next stage."));
					chr.getMap().removeMonster(mob_from);
					break;
				}
			}
		}
	}

	public static final void OrgelHit(LittleEndianAccessor slea, MapleCharacter chr) {
		MapleMonster Attacker = chr.getMap().getMonsterByOid(slea.readInt());
		slea.skip(4);
		MapleMonster Defender = chr.getMap().getMonsterByOid(slea.readInt());
		slea.skip(3);
		int Damage = slea.readInt();
		EventInstanceManager em = chr.getEventInstance();
		if (Attacker == null || Defender == null || em == null) {
			return;
		}
		if (Attacker.getId() >= 9833090 && Attacker.getId() <= 9833099
				&& ((Defender.getId() >= 9833070 && Defender.getId() <= 9833074) || Defender.getId() == 9833100)) {
			int HP = ((int) (Defender.getHp() - Damage) < 0) ? 0 : (int) (Defender.getHp() - Damage);
			Defender.setHp(HP);
			if (Defender.getHp() <= 0L) {
				Defender.getMap().killMonster(Defender);
			}
		}
	}

	public static final void SpiritHit(LittleEndianAccessor slea, MapleCharacter chr) {
		int asdf, i, oid = slea.readInt();
		if (chr == null || chr.getMap() == null) {
			return;
		}
		MapleMonster mob = chr.getMap().getMonsterByOid(oid);
		EventInstanceManager em = chr.getEventInstance();
		if (mob == null || em == null) {
			chr.getMap().killMonster(mob);

			return;
		}
		int life = (int) chr.getKeyValue(16215, "life");
		switch (mob.getId()) {
		case 8644201:
			chr.setKeyValue(16215, "life", "" + (life - 5));
			break;
		case 8644301:
		case 8644302:
		case 8644303:
		case 8644304:
		case 8644305:
			chr.setKeyValue(16215, "life", "" + (life - 50));
			asdf = 0;
			for (i = 0; i < 5; i++) {
				if (em.getProperty("ObjectId" + i) != null && !em.getProperty("ObjectId" + i).equals("")) {
					chr.getClient().getSession().writeAndFlush(
							SLFCGPacket.SpawnPartner(false, Integer.parseInt(em.getProperty("ObjectId" + i)), 0));
					em.setProperty("ObjectId" + i, "");
					asdf++;
				}
			}
			if (asdf == 5) {
				chr.getClient().getSession().writeAndFlush(CField.startMapEffect("", 0, false));
				chr.getClient().getSession()
						.writeAndFlush(CField.startMapEffect("친구들이... 맹독의 정령에게 당하고 말았담!", 5120175, true));
				chr.getClient().getSession()
						.writeAndFlush(CField.environmentChange("Map/Effect3.img/savingSpirit/failed", 19));
			}

			chr.setKeyValue(16215, "chase", "0");
			break;
		case 8644101:
		case 8644102:
		case 8644103:
		case 8644104:
		case 8644105:
		case 8644106:
		case 8644107:
		case 8644108:
		case 8644109:
		case 8644110:
		case 8644111:
		case 8644112:
			chr.setKeyValue(16215, "life", "" + (life - 5));
			break;
		case 8880315:
		case 8880316:
		case 8880317:
		case 8880318:
		case 8880319:
		case 8880345:
		case 8880346:
		case 8880347:
		case 8880348:
		case 8880349:
			chr.addHP((long) (-chr.getStat().getCurrentMaxHp() * 0.1D));
			break;
		}
		chr.getMap().killMonster(mob, chr, false, false, (byte) 1);

		if (chr.getKeyValue(16215, "life") <= 0L && chr.getMapId() == 921172300) {
			MapleMap target = chr.getClient().getChannelServer().getMapFactory().getMap(921172400);
			chr.changeMap(target, target.getPortal(0));
			chr.getClient().getSession().writeAndFlush(CField.environmentChange("Map/Effect2.img/event/gameover", 16));
		}
	}

	public static void AirAttack(LittleEndianAccessor slea, MapleClient c) {
	}

	public static void demianBind(LittleEndianAccessor slea, MapleClient c) {
		MapleCharacter chr = c.getPlayer();
		int result = slea.readInt();

		Map<SecondaryStat, Pair<Integer, Integer>> cancelList = new HashMap<>();

		if (result == 0 && chr.getDiseases().containsKey(SecondaryStat.Lapidification)) {
			chr.getDiseases().remove(SecondaryStat.Lapidification);

			cancelList.put(SecondaryStat.Lapidification, new Pair(Integer.valueOf(0), Integer.valueOf(0)));
			c.getSession().writeAndFlush(CWvsContext.BuffPacket.cancelBuff(cancelList, chr));
			chr.getMap().broadcastMessage(chr, CWvsContext.BuffPacket.cancelForeignBuff(chr, cancelList), false);
		}
	}

//============================================================================================
	// ============================================================================================
	/**
	 * 处理怪物被攻击时的逻辑，依据技能 ID 应用怪物技能效果。
	 *
	 * @param slea 用于读取网络数据包的 LittleEndianAccessor 对象
	 * @param c    表示客户端会话的 MapleClient 对象
	 */
	public static void demianAttacked(LittleEndianAccessor slea, MapleClient c) {
		// 从数据包中读取怪物的对象 ID
		int objectId = slea.readInt();

		// 通过对象 ID 从当前玩家所在地图中获取对应的怪物对象
		// c.getPlayer() 获取当前玩家对象
		// getMap() 获取玩家所在的地图对象
		// getMonsterByOid(objectId) 根据对象 ID 从地图中查找怪物
		MapleMonster mob = c.getPlayer().getMap().getMonsterByOid(objectId);

		// 检查获取到的怪物对象是否不为空
		if (mob != null) {
			// 从数据包中读取技能 ID，该技能 ID 表示玩家对怪物使用的技能
			int skillId = slea.readInt();

			// 检查读取到的技能 ID 是否为 214
			if (skillId == 214) {
				// 若技能 ID 为 214，则通过 MobSkillFactory 获取怪物技能
				// getMobSkill(170, 51) 从技能工厂中获取技能 ID 为 170、技能等级为 51 的怪物技能
				// applyEffect 方法用于应用该技能效果
				// 参数 c.getPlayer() 表示技能的使用者，即当前玩家
				// 参数 mob 表示技能作用的目标，即被攻击的怪物
				// 参数 true 表示技能效果立即生效
				// mob.isFacingLeft() 表示怪物的朝向，用于确定技能效果的方向
				MobSkillFactory.getMobSkill(170, 51).applyEffect(c.getPlayer(), mob, true, mob.isFacingLeft());
			}
		}
	}

	// ============================================================================================
	// ============================================================================================

	public static void useStigmaIncinerate(LittleEndianAccessor slea, MapleClient c) {
		int state = slea.readInt();
		int id = slea.readInt();
		int type2 = slea.readInt();
		int[] demians = { 8880100, 8880110, 8880101, 8880111 };
		MapleMonster demian = null;
		for (int ids : demians) {
			demian = c.getPlayer().getMap().getMonsterById(ids);
			if (demian != null) {
				break;
			}
		}
		MapleCharacter chr = c.getPlayer();
		Map<SecondaryStat, Pair<Integer, Integer>> cancelList = new HashMap<>();
		if (demian != null) {
			int stigma;
			switch (state) {

			case 1:
				stigma = chr.Stigma;
				if (chr.Stigma > 0) {
					chr.Stigma = 0;
					cancelList.put(SecondaryStat.Stigma, new Pair(Integer.valueOf(0), Integer.valueOf(0)));
					c.getSession().writeAndFlush(CWvsContext.BuffPacket.cancelBuff(cancelList, chr));
					chr.getMap().broadcastMessage(chr, CWvsContext.BuffPacket.cancelForeignBuff(chr, cancelList),
							false);
					chr.getMap().broadcastMessage(MobPacket.incinerateObject(null, false));
					c.getPlayer().getMap().broadcastMessage(MobPacket.StigmaImage(c.getPlayer(), true));
				}
				if (type2 == 1) {

					MapleCharacter target = c.getPlayer().getMap().getCharacterById(id);
					target.Stigma += stigma - 1;
					MobSkill ms = MobSkillFactory.getMobSkill(237, 1);
					ms.applyEffect(target, demian, true, demian.isFacingLeft());
					target.getMap().broadcastMessage(MobPacket.StigmaImage(target, false));
				}
				break;
			}
		}
	}

	public static void stoneAttacked(LittleEndianAccessor slea, MapleClient c) {
		MapleCharacter chr = c.getPlayer();
		if (slea.readInt() == 0 && chr.isAlive()) {
			if (chr.getMap().getId() % 1000 == 410 || chr.getMap().getId() % 1000 == 810) {
				MobSkillFactory.getMobSkill(123, 44).applyEffect(chr, null, true, false);
			} else if (chr.getMap().getId() % 1000 == 210 || chr.getMap().getId() % 1000 == 610) {
				MobSkillFactory.getMobSkill(174, 3).applyEffect(chr, null, true, false);
			}
		}
	}

	public static void jinHillahBlackHand(LittleEndianAccessor slea, MapleClient c) {
		MapleCharacter chr = c.getPlayer();
		MapleMonster mob = chr.getMap().getMonsterByOid(slea.readInt());
		if (mob != null) {
			int skillLevel = slea.readInt();
			int id = slea.readInt();
			Point pos = slea.readPos();

			if (chr.getId() == id && !chr.hasDisease(SecondaryStat.Stun, 123, 83)) {
				Rectangle rect = new Rectangle(slea.readInt(), slea.readInt(), slea.readInt(), slea.readInt());
				if (skillLevel == 2) {
					skillLevel = 1;
				}
				int act_x = 0, apply_x = 0;
				boolean minus = false;
				int[] randXs = { 0, 280, 560, 840 };
				pos.x -= 50;
				if (pos.x < 0) {
					pos.x *= -1;
					minus = true;
				}
				int arrayOfInt1[], i;
				byte b;
				for (arrayOfInt1 = randXs, i = arrayOfInt1.length, b = 0; b < i;) {
					Integer randX = Integer.valueOf(arrayOfInt1[b]);
					int calc_x = pos.x - randX.intValue();
					if (calc_x < 0) {
						calc_x *= -1;
					}
					if (act_x == 0) {
						act_x = calc_x;
					}
					if (calc_x < act_x) {
						act_x = calc_x;
						apply_x = randX.intValue();
					}
					b++;
				}

				pos.x = apply_x;
				if (minus) {
					pos.x *= -1;
				}
				MobSkill ms = MobSkillFactory.getMobSkill(123, 83);
				chr.giveDebuff(SecondaryStat.Stun, ms);
				chr.getMap().broadcastMessage(
						MobPacket.jinHillahSpirit(mob.getObjectId(), chr.getId(), rect, pos, skillLevel));

				if (chr.liveCounts() == 1) {

					for (int j = 0; j < (chr.getDeathCounts()).length; j++) {
						if (chr.getDeathCounts()[j] == 1) {
							chr.getDeathCounts()[j] = 0;

							break;
						}
					}
					chr.playerIGDead();
				} else {
					for (int j = 0; j < (chr.getDeathCounts()).length; j++) {
						if (chr.getDeathCounts()[j] == 1) {
							chr.getDeathCounts()[j] = 0;
							break;
						}
					}
					if (chr.getMap().getLightCandles() < chr.getMap().getCandles()) {

						chr.getMap().setLightCandles(chr.getMap().getLightCandles() + 1);
						chr.getMap().broadcastMessage(CField.JinHillah(1, chr, chr.getMap()));
					}
				}

				c.getSession().writeAndFlush(CField.JinHillah(3, chr, chr.getMap()));
				c.getPlayer().getMap().broadcastMessage(CField.JinHillah(10, c.getPlayer(), chr.getMap()));

				chr.dropMessageGM(6, "candle : " + chr.getMap().getCandles() + " / " + chr.getMap().getLightCandles());

				if (chr.getMap().getCandles() == chr.getMap().getLightCandles() && chr.getMap().getReqTouched() == 0) {
					chr.getMap().broadcastMessage(CField.enforceMSG("希拉接近消失之前，必須在祭壇上連續擊打採集鍵回收靈魂.", 254, 6000));
					chr.getMap().setReqTouched(30);
					chr.getMap().broadcastMessage(CField.JinHillah(6, chr, chr.getMap()));
				}
			}
		}
	}

	public static void touchAlter(LittleEndianAccessor slea, MapleClient c) {
		MapleMap map = c.getPlayer().getMap();
		if (map.getReqTouched() > 0) {
			map.setReqTouched(map.getReqTouched() - 1);
			if (map.getReqTouched() != 0) {
				map.broadcastMessage(CField.JinHillah(7, c.getPlayer(), c.getPlayer().getMap()));
			} else {
				map.broadcastMessage(CField.JinHillah(8, c.getPlayer(), c.getPlayer().getMap()));
				map.setLightCandles(0);
				map.broadcastMessage(CField.JinHillah(1, c.getPlayer(), c.getPlayer().getMap()));
				for (MapleCharacter chr : map.getAllCharactersThreadsafe()) {
					SkillFactory.getSkill(80002544).getEffect(1).applyTo(chr, false);
					chr.cancelEffectFromBuffStat(SecondaryStat.DebuffIncHp, 80002543);
					for (int i = 0; i < chr.getDeathCounts().length; ++i) {
						int dc = chr.getDeathCounts()[i];
						if (dc != 0) {
							continue;
						}
						chr.getDeathCounts()[i] = 1;
					}
					chr.getClient().getSession().writeAndFlush(CField.JinHillah(3, chr, c.getPlayer().getMap()));
					c.getPlayer().getMap()
							.broadcastMessage(CField.JinHillah(10, c.getPlayer(), c.getPlayer().getMap()));
				}
			}
		}
	}

	public static void unkJinHillia(LittleEndianAccessor slea, MapleClient c) {
	}

	public static void lucidStateChange(MapleCharacter chr) {
		MapleMonster lucid = null;
		int[] lucids = { 8880140, 8880141, 8880142, 8880150, 8880151, 8880155 };
		for (int ids : lucids) {
			lucid = chr.getMap().getMonsterById(ids);
			if (lucid != null) {
				break;
			}
		}
		if (lucid != null && chr.getMap().getLucidCount() > 0) {
			chr.getMap().setLucidCount(chr.getMap().getLucidCount() - 1);
			chr.getMap().setLucidUseCount(chr.getMap().getLucidUseCount() + 1);
			chr.getMap()
					.broadcastMessage(MobPacket.BossLucid.changeStatueState(false, chr.getMap().getLucidCount(), true));
			chr.getMap().broadcastMessage(MobPacket.BossLucid.RemoveButterfly());
			lucid.removeCustomInfo(8880140);
			chr.getMap().broadcastMessage(
					MobPacket.BossLucid.setButterflyAction(Butterfly.Mode.ERASE, new int[] { 700, -600 }));
		}
	}

	public static void mobSkillDelay(LittleEndianAccessor slea, MapleClient c) {
		int objectId = slea.readInt();
		int skillId = slea.readInt();
		int skillLevel = slea.readInt();
		slea.skip(1);
		int RectCount = slea.readInt();

		switch (skillId) {
		case 213:
		case 217:
			return;
		}

		MobSkill msi = MobSkillFactory.getMobSkill(skillId, skillLevel);
		if (c.getPlayer() != null && msi != null) {
			MapleMonster monster = c.getPlayer().getMap().getMonsterByOid(objectId);
			if (monster != null) {
				if (RectCount > 0) {
					msi.applyEffect(c.getPlayer(), monster, true, monster.isFacingLeft(), RectCount);
				} else {
					msi.applyEffect(c.getPlayer(), monster, true, monster.isFacingLeft());
				}
			}
		}
	}

	public static void spawnMistArea(LittleEndianAccessor slea, MapleClient c) {
		int mobid = slea.readInt();
		int objectId = slea.readInt();
		int skillId = slea.readInt();
		int skillLevel = slea.readInt();
		Point pos = slea.readIntPos();
		MapleMonster monster = c.getPlayer().getMap().getMonsterByOid(mobid);
		MobSkill msi = MobSkillFactory.getMobSkill(skillId, skillLevel);
		if (c.getPlayer() != null && msi != null && monster != null) {
			if (skillId == 217 && skillLevel == 21) {
				Rectangle re = new Rectangle(pos.x - 64, pos.y - 50, 128, 60);
				MapleMist mist = new MapleMist(re, monster, msi, (int) msi.getDuration());
				mist.setPosition(pos);
				monster.getMap().spawnMist(mist, true);
			} else {
				MapleMist mist = new MapleMist(msi.calculateBoundingBox(pos, monster.isFacingLeft()), monster, msi,
						10000);
				mist.setPosition(pos);
				monster.getMap().spawnMist(mist, true);
			}
		}
	}

	public static final void SpawnMoveMobMist(LittleEndianAccessor slea, MapleCharacter chr) {
		int mobid = slea.readInt();
		int skillid = slea.readInt();
		int skilllv = slea.readInt();
		int callskill = 0;
		int level = 0;
		if (skillid == 202) {
			switch (skilllv) {
			case 1:
				callskill = 131;
				level = 16;
				break;
			case 2:
				callskill = 131;
				level = 18;
				break;
			case 3:
				callskill = 131;
				level = 10;
				break;
			}

		}
		Point pos = slea.readPos();
		MapleMonster monster = chr.getMap().getMonsterByOid(mobid);

		if (monster == null) {
			return;
		}
		if (callskill > 0 && level > 0) {
			MobSkill ms = MobSkillFactory.getMobSkill(callskill, level);
			if (ms != null) {
				MapleMist mist = new MapleMist(ms.calculateBoundingBox(pos, monster.isFacingLeft()), monster, ms,
						(int) ms.getDuration());
				monster.getMap().spawnMist(mist, true);
			}
		}
	}

	public static void PapulaTusBomb(LittleEndianAccessor slea, MapleClient c, int type) {
		MapleMonster mob = null;
		for (MapleMonster monster : c.getPlayer().getMap().getAllMonster()) {
			if (monster.getId() == 8500001 || monster.getId() == 8500002 || monster.getId() == 8500011
					|| monster.getId() == 8500012 || monster.getId() == 8500021 || monster.getId() == 8500022) {
				mob = monster;
				break;
			}
		}
		if (mob != null) {
			if (type == 0) {
				c.getPlayer().getMap().setPapulratusTime(Randomizer.rand(15, 25));
				c.getPlayer().getMap().broadcastMessage(MobPacket.BossPapuLatus.PapulLatusLaser(true, 1));
				c.getPlayer().getMap().broadcastMessage(MobPacket.BossPapuLatus.PapulLatusLaser(true, 2));
			} else if (type == 1) {
				int typeed = mob.getId() % 100 / 10;
				String difical = (typeed == 0) ? "Easy" : ((typeed == 1) ? "Normal" : "Chaos");
				int percent = difical.equals("Chaos") ? 200 : (difical.equals("Normal") ? 100 : 50);
				for (MapleCharacter allchr : c.getPlayer().getMap().getAllCharactersThreadsafe()) {
					if (allchr != null && allchr.getBuffedValue(SecondaryStat.NotDamaged) == null
							&& allchr.getBuffedValue(SecondaryStat.IndieNotDamaged) == null) {
						allchr.getPercentDamage(mob, 999, 999, percent, false);
					}
				}
			}
		}
	}

	public static void PapulaTusPincers(LittleEndianAccessor slea, MapleClient c) {
		int type = slea.readInt();
		int y = slea.readInt();
		MobSkill ms1 = MobSkillFactory.getMobSkill(241, 8);
		ms1.setDuration(2000L);
		c.getPlayer().giveDebuff(SecondaryStat.Stun, ms1);
		c.getPlayer().getMap()
				.broadcastMessage(MobPacket.BossPapuLatus.PapulLatusPincers(false, c.getPlayer().getId(), type, y));
	}

	public static void PapulaTusPincersreset(LittleEndianAccessor slea, MapleClient c) {
		int type = slea.readInt();

		c.getPlayer().getMap().broadcastMessage(MobPacket.BossPapuLatus.PapulLatusPincers(false, 0, type, 0));
	}

	public static void BloodyQueenBress(LittleEndianAccessor slea, MapleClient c) {
		int modid = slea.readInt();
		int type = slea.readByte();

		c.getPlayer().getMap().broadcastMessage(MobPacket.bloodQueenAttack1(modid, type, (type == 1)));
		c.getPlayer().getMap().broadcastMessage(MobPacket.bloodQueenAttack2(modid, type, (type == 1)));
	}

	public static void BloodyQueenMirror(LittleEndianAccessor slea, MapleClient c) {
		int mobid = slea.readInt();
		boolean Chaos = (mobid == 8920005);
		int percent = Chaos ? 25 : 5;
		MapleMonster queen = null;
		for (MapleMonster monster : c.getPlayer().getMap().getAllMonster()) {
			if ((monster.getId() >= 8920100 && monster.getId() <= 8920103)
					|| (monster.getId() >= 8920000 && monster.getId() <= 8920003)) {
				queen = monster;
				break;
			}
		}
		if (queen != null) {
			List<SecondaryStat> cancellist = new ArrayList<>();
			cancellist.add(SecondaryStat.Slow);
			cancellist.add(SecondaryStat.Stance);
			cancellist.add(SecondaryStat.Attract);
			for (SecondaryStat cancel : cancellist) {
				if (c.getPlayer().hasDisease(cancel)) {
					c.getPlayer().cancelDisease(cancel);
				}
			}

			int minushp = (int) -c.getPlayer().getStat().getCurrentMaxHp();
			c.getPlayer().addHP(minushp);
			c.send(CField.ChangeFaceMotion(0, 1));
			c.send(CField.EffectPacket.showEffect(c.getPlayer(), 0, minushp, 36, 0, 0, (byte) 0, true, null, null,
					null));
			c.getPlayer().getMap().broadcastMessage(c.getPlayer(), CField.EffectPacket.showEffect(c.getPlayer(), 0,
					minushp, 36, 0, 0, (byte) 0, false, null, null, null), false);
			queen.addHp(queen.getStats().getHp() * percent / 100L, true);
		}
	}

	public static void VanVanTimeOver(LittleEndianAccessor slea, MapleClient c) {
		if ((c.getPlayer().getMapId() >= 105200120 && c.getPlayer().getMapId() <= 105200129)
				|| (c.getPlayer().getMapId() >= 105200520 && c.getPlayer().getMapId() <= 105200529)) {
			boolean suc = !(c.getPlayer().getMap().getAllMonster().size() > 0);
			if (suc) {
				MapleMap warp = ChannelServer.getInstance(c.getChannel()).getMapFactory()
						.getMap(c.getPlayer().getMapId() - 10);
				MapleMonster vanvan = warp.getMonsterById(8910000);
				if (vanvan != null && vanvan.getBuff(MonsterStatus.MS_Stun) == null) {
					vanvan.applyStatus(c, MonsterStatus.MS_Stun, new MonsterStatusEffect(80001227, 7000), 1,
							SkillFactory.getSkill(80001227).getEffect(1));
				}
			} else {
				c.send(CField.setPlayerDead());
				Timer.MapTimer.getInstance().schedule(() -> {
					if (c.getPlayer().isAlive() && ((c.getPlayer().getMapId() >= 105200120
							&& c.getPlayer().getMapId() <= 105200129)
							|| (c.getPlayer().getMapId() >= 105200520 && c.getPlayer().getMapId() <= 105200529))) {
						c.getPlayer().warp(c.getPlayer().getMapId() - 10);
					}
				}, 3000L);
			}
		} else {
			MapleMonster vanvan = c.getPlayer().getMap().getMonsterById(8910000);
			if (vanvan != null) {
				boolean suc = (c.getChannelServer().getMapFactory().getMap(c.getPlayer().getMapId() + 10)
						.getAllMonster().size() <= 0);
				c.send(CField.environmentChange("Pt", 3));
				if (suc) {
					if (vanvan.getBuff(MonsterStatus.MS_Stun) == null) {
						vanvan.applyStatus(c, MonsterStatus.MS_Stun, new MonsterStatusEffect(80001227, 7000), 1,
								SkillFactory.getSkill(80001227).getEffect(1));
					}
				} else {
					c.send(MobPacket.StopVanVanBind(vanvan.getObjectId()));
					c.send(CField.setPlayerDead());
				}
			}
		}
	}

	public static final void SpawnBellumMist(LittleEndianAccessor slea, MapleClient c) {
		int mobid = slea.readInt();
		int attacktype = slea.readInt();
		Point pos = slea.readIntPos();
		int dealy = slea.readInt();
		final MapleMonster monster = c.getPlayer().getMap().getMonsterByOid(mobid);
		if (monster == null) {
			return;
		}
		if (monster.getId() == 8930000 || monster.getId() == 8930100
				|| ((monster.getId() == 8850011 || monster.getId() == 8850111) && attacktype == 2)) {
			MobSkill ms = MobSkillFactory.getMobSkill(131, 17);
			ms.setDuration((dealy + 4000));
			if (monster.getId() == 8850011 || monster.getId() == 8850111) {
				ms = MobSkillFactory.getMobSkill(131, 13);
				ms.setDuration((dealy + 5000));
			}
			// System.out.println(ms.getDuration());
			final MapleMist mist = new MapleMist(ms.calculateBoundingBox(pos, monster.isFacingLeft()), monster, ms,
					(int) ms.getDuration());
			Timer.MapTimer.getInstance().schedule(new Runnable() {
				public void run() {
					if (mist != null) {
						monster.getMap().spawnMist(mist, true);
					}
				}
			}, dealy);
		}
	}

	public static final void RemoveObstacle(LittleEndianAccessor slea, MapleClient c) {// mush will
		int size = slea.readInt();
		for (int i = 0; i < size; ++i) {
			int objid = slea.readInt();
			int hit = slea.readInt();
			Point pos = slea.readIntPos();
			if (hit == 1) {
				Obstacle ob = null;
				for (Obstacle obstacle : c.getPlayer().getMap().getAllObstacle()) {
					if (obstacle.getObjectId() != objid) {
						continue;
					}
					ob = obstacle;
					break;
				}
				// [othello] Dusk object null error fix - 스우도 구조물 처리 해야함.
				if (ob != null && (c.getPlayer().getMapId() == 450009450 || c.getPlayer().getMapId() == 450009400)
						&& ob.getKey() == 65) { // Dusk Curse
					MobSkill ms = MobSkillFactory.getMobSkill(133, 1);
					ms.setDuration(3000L);
					c.getPlayer().giveDebuff(SecondaryStat.Undead, ms);
					break;
				}
				if (ob != null && (c.getPlayer().getMapId() == 450009450 || c.getPlayer().getMapId() == 450009400)
						&& ob.getKey() == 66) { // Dusk Stun
					MobSkill ms = MobSkillFactory.getMobSkill(123, 83);
					c.getPlayer().giveDebuff(SecondaryStat.Stun, ms);
					break;
				}
				if (ob != null && (c.getPlayer().getMapId() == 450009450 || c.getPlayer().getMapId() == 450009400)
						&& ob.getKey() == 67) { // Dusk Darkness
					MobSkill ms = MobSkillFactory.getMobSkill(121, 1);
					ms.setDuration(3000);
					c.getPlayer().giveDebuff(SecondaryStat.Darkness, ms);
					break;
				}
				if (ob != null && (c.getPlayer().getMapId() == 450008750 || c.getPlayer().getMapId() == 450008150)
						&& ob.getKey() == 64) {
					boolean already = false;
					for (MapleMist m : c.getPlayer().getMap().getAllMistsThreadsafe()) {
						if (m.getMobSkill() == null || m.getMobSkill().getSkillId() != 242) {
							continue;
						}
						already = true;
						break;
					}
					if (!already) {
						for (MapleMonster monster : c.getPlayer().getMap().getAllMonster()) {
							if (monster.getId() != 8880305) {
								continue;
							}
							MapleMist mist = new MapleMist(new Rectangle(-204, monster.getTruePosition().y, 408, 300),
									monster, MobSkillFactory.getMobSkill(242, 4), 210000000);
							mist.setPosition(monster.getPosition());
							monster.getMap().spawnMist(mist, false);
							mist = new MapleMist(new Rectangle(-204, monster.getTruePosition().y, 408, 300), monster,
									MobSkillFactory.getMobSkill(242, 4), 210000000);
							mist.setPosition(monster.getPosition());
							monster.getMap().spawnMist(mist, false);
						}
					}
				}
			}
			ArrayList<Obstacle> remove = new ArrayList<Obstacle>();
			for (Obstacle obstacle : c.getPlayer().getMap().getAllObstacle()) {
				if (obstacle.getObjectId() != objid) {
					continue;
				}
				remove.add(obstacle);
			}
			for (Obstacle obstacle : remove) {
				c.getPlayer().getMap().removeMapObject(obstacle);
			}
		}
	}

	public static final void RemoveEnergtSphere(LittleEndianAccessor slea, MapleClient c) {
		int objid = slea.readInt();
		boolean hit = (slea.readInt() == 1);
		if (hit) {
			if (c.getPlayer().getBuffedEffect(SecondaryStat.IndieNotDamaged) == null) {
				MobSkill ms = MobSkillFactory.getMobSkill(123, 63);
				ms.setDuration(2000L);
				c.getPlayer().giveDebuff(SecondaryStat.Stun, ms);
			}
		} else if (!hit) {
			MapleEnergySphere mes = c.getPlayer().getMap().getEnergySphere(objid);
			if (mes != null) {
				int num = mes.getNum();
				if (c.getPlayer().getMap().getNodez().getEnvironments().get(num) != null) {
					((MapleNodes.Environment) c.getPlayer().getMap().getNodez().getEnvironments().get(num))
							.setX(mes.getCustomx() + 91);
					((MapleNodes.Environment) c.getPlayer().getMap().getNodez().getEnvironments().get(num)).setY(-16);
					((MapleNodes.Environment) c.getPlayer().getMap().getNodez().getEnvironments().get(num))
							.setShow(true);
				}
				c.getPlayer().getMap().broadcastMessage(
						CField.getUpdateEnvironment(c.getPlayer().getMap().getNodez().getEnvironments()));
				c.getPlayer().getMap().removeMapObject((MapleMapObject) mes);
			}
		}
	}

	public static final void DemianSwordHandle(LittleEndianAccessor slea, MapleClient c) {
		int objid = slea.readInt();
		slea.readShort();
		int count = slea.readShort();
		Point pos1 = slea.readIntPos();
		Point pos2 = slea.readIntPos();
		MapleFlyingSword mfs = c.getPlayer().getMap().getFlyingSword(objid);
		int[] demians = { 8880100, 8880110, 8880101, 8880111 };

		MapleMonster demian = null;
		for (int id : demians) {
			demian = c.getPlayer().getMap().getMonsterById(id);
			if (demian != null) {
				break;
			}
		}

		if (mfs != null && demian != null && mfs.getNodes().size() > 10) {
			if (mfs.getTarget() == null) {
				mfs.setTarget(c.getRandomCharacter());
			}
			if (mfs.getNodes().size() - 1 == count) {
				mfs.tryAttack(c.getPlayer().getMap(), pos1);
			}
		}
	}
}
