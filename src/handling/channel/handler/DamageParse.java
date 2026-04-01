/*
 * Decompiled with CFR 0.150.
 */
package handling.channel.handler;

import client.*;
import client.inventory.Item;
import client.inventory.MapleInventoryType;
import client.status.MonsterStatus;
import client.status.MonsterStatusEffect;
import constants.GameConstants;
import constants.ServerConstants;
import handling.world.MaplePartyCharacter;
import server.Randomizer;
import server.SecondaryStatEffect;
import server.field.skill.MapleMagicWreck;
import server.field.skill.MapleSecondAtom;
import server.field.skill.SecondAtom;
import server.life.Element;
import server.life.MapleLifeFactory;
import server.life.MapleMonster;
import server.life.MapleMonsterStats;
import server.maps.ForceAtom;
import server.maps.*;
import tools.*;
import tools.data.LittleEndianAccessor;
import tools.packet.CField;
import tools.packet.CWvsContext;
import tools.packet.MobPacket;
import tools.packet.SkillPacket;

import java.awt.*;
import java.util.List;
import java.util.*;

public class DamageParse {

	public static void applyDebuff(MapleCharacter player, MapleMonster monster, SecondaryStatEffect effect,
			ArrayList<Triple<MonsterStatus, MonsterStatusEffect, Long>> statusz, int attackSkillId) {
		switch (attackSkillId) {
		case 15141000: { // 섬멸 VI 구현
			statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Speed,
					new MonsterStatusEffect(attackSkillId,
							effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
					Long.valueOf(effect.getX())));
			break;
		}
		case 23121052: // 래쓰 오브 엔릴 구현
		case 23141003: // 래쓰 오브 엔릴 VI 구현
		case 23141004: { // 래쓰 오브 엔릴 VI : 스피릿 인챈트 구현
			if (!monster.getStats().isBoss()) {
				break;
			}

			statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_DodgeBodyAttack,
					new MonsterStatusEffect(attackSkillId, 60000), 1L));
			break;
		}
		case 31141000: { // 데몬 임팩트 VI 구현
			statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Speed,
					new MonsterStatusEffect(attackSkillId,
							effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
					Long.valueOf(effect.getX())));
			break;
		}
		case 31141001: { // 데몬 임팩트 VI : 데몬 체인 구현
			statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_IndiePdr,
					new MonsterStatusEffect(attackSkillId,
							effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
					Long.valueOf(-effect.getX())));
			statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_IndieMdr,
					new MonsterStatusEffect(attackSkillId,
							effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
					Long.valueOf(-effect.getX())));
			statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Pad,
					new MonsterStatusEffect(attackSkillId,
							effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
					Long.valueOf(-effect.getX())));
			statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Mad,
					new MonsterStatusEffect(attackSkillId,
							effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
					Long.valueOf(-effect.getX())));
			statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Blind,
					new MonsterStatusEffect(attackSkillId,
							effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
					Long.valueOf(-effect.getZ())));
			break;
		}
		case 33141500: // 오리진 네이쳐스 빌리프 구현
		case 33141501: // 오리진 네이쳐스 빌리프 구현
		case 33141502: { // 오리진 네이쳐스 빌리프 구현
			if (monster.getBuff(MonsterStatus.MS_JaguarBleeding) == null && monster.getAnotherByte() > 0) {
				monster.setAnotherByte(0);
			}

			if (monster.getAnotherByte() == 0) {
				monster.setAnotherByte(1);
				statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_JaguarBleeding,
						new MonsterStatusEffect(33000036, 15000), (long) monster.getAnotherByte()));
				break;
			}

			if (Randomizer.isSuccess(100)) {
				if (monster.getAnotherByte() < 3) {
					monster.setAnotherByte(monster.getAnotherByte() + 1);
				}
				statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_JaguarBleeding,
						new MonsterStatusEffect(33000036, 15000), (long) monster.getAnotherByte()));
				break;
			}
			break;
		}
		}
	}

	public static void applyHexaSkill(MapleCharacter player, MapleMonster monster, AttackInfo attack,
			SecondaryStatEffect effect) {
		// 6차 구현
		switch (attack.skill) {
		case 80003365: { // 오리진 바인드 구현
			ArrayList<Pair<MonsterStatus, MonsterStatusEffect>> applys = new ArrayList<Pair<MonsterStatus, MonsterStatusEffect>>();

			applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_Freeze,
					new MonsterStatusEffect(attack.skill, 10000, 1L)));
			applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_Origin_Bind,
					new MonsterStatusEffect(attack.skill, 10000, 1L)));
			monster.applyStatus(player.getClient(), applys, effect);

			SecondaryStatEffect eff = SkillFactory.getSkill(attack.skill).getEffect(player.getSkillLevel(attack.skill));
			eff.applyTo(player);

			player.getMap().broadcastMessage(MobPacket.monsterResistOriginBind(monster, player, 100, attack.skill));
			break;
		}
		case 5101012:
		case 5111002:
		case 5121007:
		case 400051002:
		case 400051003:
		case 400051004:
		case 400051005:
		case 400051042:
		case 400051070:
		case 400051071:
		case 5141000:
		case 5141001: { // 오리진 리버레이트 넵투누스 구현
			int attackId = 5141503;

			if (player.getBuffedValue(5141501) && !player.skillisCooling(attackId) && player.getBuffedValue(5101017)) {
				ArrayList<RangeAttack> attacks = new ArrayList<>();

				attacks.add(new RangeAttack(attackId, player.getPosition(), player.isFacingLeft() ? 0 : 1, 0, 1));
				player.getClient().getSession().writeAndFlush(CField.rangeAttack(5141501, attacks));

				attacks.clear();

				attacks.add(
						new RangeAttack(attackId + 3, player.getPosition(), player.isFacingLeft() ? 0 : 1, 1860, 0));
				attacks.add(
						new RangeAttack(attackId + 2, player.getPosition(), player.isFacingLeft() ? 0 : 1, 1140, 0));
				attacks.add(new RangeAttack(attackId + 2, player.getPosition(), player.isFacingLeft() ? 0 : 1, 840, 0));
				attacks.add(new RangeAttack(attackId + 1, player.getPosition(), player.isFacingLeft() ? 0 : 1, 540, 0));
				player.getClient().getSession().writeAndFlush(CField.rangeAttack(attackId, attacks));

				player.addCoolTime(attackId, 4000);
			}
			break;
		}
		case 1001005:
		case 1001010:
		case 1001011:
		case 1201013:
		case 1201015:
		case 1211012:
		case 1211018:
		case 1221009:
		case 1221011:
		case 1221019:
		case 1221020:
		case 1221021:
		case 1221052:
		case 400011072:
		case 400011131:
		case 400011132:
		case 1241000:
		case 1241003:
		case 1241004:
		case 1241500:
		case 1241503: { // 폴링 저스티스 발동 스킬 목록
			int skillId = 1241005;

			// 폴링 저스티스 구현 해야 함
			break;
		}
		default: {
			break;
		}
		}

		if (player.getBuffedValue(1241501)) { // 오리진 세이크리드 바스티온 구현
			int attackId = 1241502;

			if (!player.skillisCooling(attackId)) {
				ArrayList<RangeAttack> attacks = new ArrayList<>();

				attacks.add(new RangeAttack(attackId, monster.getPosition(),
						((attack.facingleft >>> 4 & 0xF) == 0) ? 0 : 1, 0, 0));
				player.getClient().getSession().writeAndFlush(CField.rangeAttack(attackId, attacks));

				player.addCoolTime(attackId, 500);
			}
		}

		if (player.getBuffedValue(14141502)) { // 오리진 사일런스 구현
			int attackId = 14141501;

			if (!player.skillisCooling(attackId)) {
				int count = 0;

				for (int i = 0; i < 3; i++) {
					SecondAtom2 sa = SkillFactory.getSkill(14141501).getSecondAtoms().get(0);
					sa.setTarget(monster.getObjectId());

					player.createSecondAtom(sa, new Point(monster.getPosition().x, monster.getPosition().y), false);

					count++;
				}

				player.addCoolTime(attackId, 3000);
			}
		}
	}

	public static void applyAddAttack(MapleCharacter player, MapleMonster monster, AttackInfo attack) {
		if (player.getSkillLevel(15110025) > 0) { // 연쇄 구현
			if (attack.isLink) {
				if (attack.allDamage.size() > 0) {
					if (player.hexaMasterySkillStack < 3) {
						player.hexaMasterySkillStack++;
					}

					SkillFactory.getSkill(15110025).getEffect(15110025).applyTo(player);
				}
			}
		}

		if (player.getSkillLevel(15141000) > 0) { // 섬멸 VI 구현
			if (attack.skill == 15141000) {
				ArrayList<MapleMonster> monsters = new ArrayList<>();

				for (AttackPair oned : attack.allDamage) {
					MapleMonster mob = player.getMap().getMonsterByOid(oned.objectId);

					if (mob != null && mob.isAlive()) {
						monsters.add(mob);
					}
				}

				if (monsters.size() >= 0) {
					player.getClient().getSession()
							.writeAndFlush(SkillPacket.hexaStackMonster(monsters, 15141000, 90000));
				}
			} else {
				if (attack.skill != 15141001 && attack.skill != 15141002) {
					ArrayList<MapleMonster> monsters = new ArrayList<>();

					for (AttackPair oned : attack.allDamage) {
						MapleMonster mob = player.getMap().getMonsterByOid(oned.objectId);

						if (mob != null && mob.isAlive()) {
							monsters.add(mob);
						}
					}

					if (monsters.size() >= 0) {
						int skillId = attack.skill == 15111022 || attack.skill == 15120003 || attack.skill == 400051016
								|| attack.skill == 400051060 || attack.skill == 400051096 || attack.skill == 15141500
								|| attack.skill == 15141501 ? 15141002 : 15141001;

						player.getClient().getSession().writeAndFlush(
								SkillPacket.hexaStackAttackMonster(monsters, skillId, attack.skill, 290));
					}
				}
			}
		}

		if (player.getBuffedValue(SecondaryStat.Unk_733) != null) { // 오리진 어센던트 셰이드 구현
			if (attack.allDamage.size() > 0) {
				if (!player.skillisCooling(3141502)) {
					ArrayList<Triple<Integer, Integer, Integer>> mobList = new ArrayList<>();

					player.getClient().getSession()
							.writeAndFlush(CField.bonusAttackRequest(3141502, mobList, true, 0, null));

					player.addCoolTime(3141502, 3000);
				}
			}
		}

		if (attack.skill == 3141000) { // 폭풍의 시 VI 구현
			if (attack.allDamage.size() > 0) {
				player.hexaMasterySkillStack++;

				EnumMap<SecondaryStat, Pair<Integer, Integer>> statups = new EnumMap<SecondaryStat, Pair<Integer, Integer>>(
						SecondaryStat.class);
				statups.put(SecondaryStat.Poem_Of_Storm, new Pair<Integer, Integer>(player.hexaMasterySkillStack, 0));
				SecondaryStatEffect eff = SkillFactory.getSkill(attack.skill)
						.getEffect(player.getSkillLevel(attack.skill));
				player.getClient().getSession().writeAndFlush(CWvsContext.BuffPacket.giveBuff(statups, eff, player));
			}
		} else if (attack.skill == 3141001) { // 폭풍의 시 VI : 난사 모드 구현
			if (attack.allDamage.size() > 0) {
				player.hexaMasterySkillStack--;

				EnumMap<SecondaryStat, Pair<Integer, Integer>> statups = new EnumMap<SecondaryStat, Pair<Integer, Integer>>(
						SecondaryStat.class);
				statups.put(SecondaryStat.Poem_Of_Storm, new Pair<Integer, Integer>(player.hexaMasterySkillStack, 0));
				SecondaryStatEffect eff = SkillFactory.getSkill(attack.skill)
						.getEffect(player.getSkillLevel(attack.skill));
				player.getClient().getSession().writeAndFlush(CWvsContext.BuffPacket.giveBuff(statups, eff, player));
			}
		}

		if (attack.skill == 3241000) { // 스나이핑 VI 구현
			if (attack.allDamage.size() > 0) {
				EnumMap<SecondaryStat, Pair<Integer, Integer>> statups = new EnumMap<SecondaryStat, Pair<Integer, Integer>>(
						SecondaryStat.class);
				statups.put(SecondaryStat.Sniping_Stack, new Pair<Integer, Integer>(1, 0));
				SecondaryStatEffect eff = SkillFactory.getSkill(3210016).getEffect(player.getSkillLevel(3210016));
				player.getClient().getSession().writeAndFlush(CWvsContext.BuffPacket.giveBuff(statups, eff, player));
			}
		} else if (attack.skill == 3241001) { // 인핸스 스나이핑 VI 구현
			if (attack.allDamage.size() > 0) {
				EnumMap<SecondaryStat, Pair<Integer, Integer>> statups = new EnumMap<SecondaryStat, Pair<Integer, Integer>>(
						SecondaryStat.class);
				statups.put(SecondaryStat.Sniping_Mode, new Pair<Integer, Integer>(1, 0));
				SecondaryStatEffect eff = SkillFactory.getSkill(3210016).getEffect(player.getSkillLevel(3210016));
				player.getClient().getSession().writeAndFlush(CWvsContext.BuffPacket.giveBuff(statups, eff, player));

				statups = new EnumMap<SecondaryStat, Pair<Integer, Integer>>(SecondaryStat.class);
				statups.put(SecondaryStat.Sniping_Stack, new Pair<Integer, Integer>(0, 0));
				eff = SkillFactory.getSkill(3210016).getEffect(player.getSkillLevel(3210016));
				player.getClient().getSession().writeAndFlush(CWvsContext.BuffPacket.giveBuff(statups, eff, player));
			}
		} else if (attack.skill == 3241003) { // 얼티밋 스나이핑 VI 구현
			if (attack.allDamage.size() > 0) {
				EnumMap<SecondaryStat, Pair<Integer, Integer>> statups = new EnumMap<SecondaryStat, Pair<Integer, Integer>>(
						SecondaryStat.class);
				statups.put(SecondaryStat.Sniping_Mode, new Pair<Integer, Integer>(0, 0));
				SecondaryStatEffect eff = SkillFactory.getSkill(3210016).getEffect(player.getSkillLevel(3210016));
				player.getClient().getSession().writeAndFlush(CWvsContext.BuffPacket.giveBuff(statups, eff, player));

				statups = new EnumMap<SecondaryStat, Pair<Integer, Integer>>(SecondaryStat.class);
				statups.put(SecondaryStat.Sniping_Stack, new Pair<Integer, Integer>(0, 0));
				eff = SkillFactory.getSkill(3210016).getEffect(player.getSkillLevel(3210016));
				player.getClient().getSession().writeAndFlush(CWvsContext.BuffPacket.giveBuff(statups, eff, player));
			}
		}

		if (attack.skill == 3241010) { // 피어싱 VI 구현
			if (attack.allDamage.size() > 0) {
				EnumMap<SecondaryStat, Pair<Integer, Integer>> statups = new EnumMap<SecondaryStat, Pair<Integer, Integer>>(
						SecondaryStat.class);
				statups.put(SecondaryStat.Piercing_Stack, new Pair<Integer, Integer>(1, 0));
				SecondaryStatEffect eff = SkillFactory.getSkill(3210016).getEffect(player.getSkillLevel(3210016));
				player.getClient().getSession().writeAndFlush(CWvsContext.BuffPacket.giveBuff(statups, eff, player));
			}
		} else if (attack.skill == 3241006) { // 인핸스 피어싱 VI 구현
			if (attack.allDamage.size() > 0) {
				EnumMap<SecondaryStat, Pair<Integer, Integer>> statups = new EnumMap<SecondaryStat, Pair<Integer, Integer>>(
						SecondaryStat.class);
				statups.put(SecondaryStat.Piercing_Mode, new Pair<Integer, Integer>(1, 0));
				SecondaryStatEffect eff = SkillFactory.getSkill(3210016).getEffect(player.getSkillLevel(3210016));
				player.getClient().getSession().writeAndFlush(CWvsContext.BuffPacket.giveBuff(statups, eff, player));

				statups = new EnumMap<SecondaryStat, Pair<Integer, Integer>>(SecondaryStat.class);
				statups.put(SecondaryStat.Piercing_Stack, new Pair<Integer, Integer>(0, 0));
				eff = SkillFactory.getSkill(3210016).getEffect(player.getSkillLevel(3210016));
				player.getClient().getSession().writeAndFlush(CWvsContext.BuffPacket.giveBuff(statups, eff, player));
			}
		} else if (attack.skill == 3241007) { // 얼티밋 피어싱 VI 구현
			if (attack.allDamage.size() > 0) {
				EnumMap<SecondaryStat, Pair<Integer, Integer>> statups = new EnumMap<SecondaryStat, Pair<Integer, Integer>>(
						SecondaryStat.class);
				statups.put(SecondaryStat.Piercing_Mode, new Pair<Integer, Integer>(0, 0));
				SecondaryStatEffect eff = SkillFactory.getSkill(3210016).getEffect(player.getSkillLevel(3210016));
				player.getClient().getSession().writeAndFlush(CWvsContext.BuffPacket.giveBuff(statups, eff, player));

				statups = new EnumMap<SecondaryStat, Pair<Integer, Integer>>(SecondaryStat.class);
				statups.put(SecondaryStat.Piercing_Stack, new Pair<Integer, Integer>(0, 0));
				eff = SkillFactory.getSkill(3210016).getEffect(player.getSkillLevel(3210016));
				player.getClient().getSession().writeAndFlush(CWvsContext.BuffPacket.giveBuff(statups, eff, player));
			}
		}

		if (attack.skill == 4141000) { // 쿼드러플 스로우 VI 구현
			if (attack.allDamage.size() > 0) {
				player.hexaMasterySkillStack++;

				EnumMap<SecondaryStat, Pair<Integer, Integer>> statups = new EnumMap<SecondaryStat, Pair<Integer, Integer>>(
						SecondaryStat.class);
				statups.put(SecondaryStat.Quadruple_Throw_Stack,
						new Pair<Integer, Integer>(player.hexaMasterySkillStack, 0));
				SecondaryStatEffect eff = SkillFactory.getSkill(4141000).getEffect(player.getSkillLevel(4141000));
				player.getClient().getSession().writeAndFlush(CWvsContext.BuffPacket.giveBuff(statups, eff, player));
			}
		} else if (attack.skill == 4141001) { // 쿼드러플 스로우 VI 구현
			player.hexaMasterySkillStack = 0;

			EnumMap<SecondaryStat, Pair<Integer, Integer>> statups = new EnumMap<SecondaryStat, Pair<Integer, Integer>>(
					SecondaryStat.class);
			statups.put(SecondaryStat.Quadruple_Throw_Stack,
					new Pair<Integer, Integer>(player.hexaMasterySkillStack, 0));
			SecondaryStatEffect eff = SkillFactory.getSkill(4141000).getEffect(player.getSkillLevel(4141000));
			player.getClient().getSession().writeAndFlush(CWvsContext.BuffPacket.giveBuff(statups, eff, player));
		}

		if (player.getBuffedValue(SecondaryStat.Origin_Forsaken_Relic) != null) { // 오리진 포세이큰 렐릭 구현
			if (!player.skillisCooling(3341503)) {
				if (attack.allDamage.size() > 0) {
					ArrayList<RangeAttack> attacks = new ArrayList<>();

					attacks.add(new RangeAttack(3341503, monster.getPosition(),
							((attack.facingleft >>> 4 & 0xF) == 0) ? 0 : 1, 180, 1));
					attacks.add(new RangeAttack(3341503, monster.getPosition(),
							((attack.facingleft >>> 4 & 0xF) == 0) ? 0 : 1, 360, 1));
					attacks.add(new RangeAttack(3341503, monster.getPosition(),
							((attack.facingleft >>> 4 & 0xF) == 0) ? 0 : 1, 540, 1));
					player.getClient().getSession().writeAndFlush(CField.rangeAttack(3341503, attacks));

					player.addCoolTime(3341503, 10000);
				}
			}
		}

		if (attack.skill == 4241003 && player.hexaMasterySkillStack > 0) { // 분쇄 VI 구현
			player.hexaMasterySkillStack--;

			if (player.hexaMasterySkillStack > 0) {
				EnumMap<SecondaryStat, Pair<Integer, Integer>> statupsT = new EnumMap<SecondaryStat, Pair<Integer, Integer>>(
						SecondaryStat.class);
				statupsT.put(SecondaryStat.Smash_Stack, new Pair<Integer, Integer>(player.hexaMasterySkillStack, 0));
				SecondaryStatEffect effT = SkillFactory.getSkill(4241004).getEffect(player.getSkillLevel(4241000));
				player.getClient().getSession().writeAndFlush(CWvsContext.BuffPacket.giveBuff(statupsT, effT, player));
			} else if (player.hexaMasterySkillStack <= 0) {
				EnumMap<SecondaryStat, Pair<Integer, Integer>> statupsT = new EnumMap<SecondaryStat, Pair<Integer, Integer>>(
						SecondaryStat.class);
				statupsT.put(SecondaryStat.Smash_Stack, new Pair<Integer, Integer>(player.hexaMasterySkillStack, 0));
				player.getClient().getSession().writeAndFlush(CWvsContext.BuffPacket.cancelBuff(statupsT, player));
			}
		}

		if (player.getBuffedValue(SecondaryStat.Origin_Life_And_Death) != null) { // 오리진 생사여탈 구현
			if (!player.skillisCooling(4141501)) {
				if (attack.allDamage.size() > 0) {
					ArrayList<MapleMonster> mobs = (ArrayList<MapleMonster>) player.getMap().getAllMonster();
					for (int x = 0; x < mobs.size(); x++) {
						for (int y = 1; y < mobs.size(); y++) {
							if (mobs.get(x).getMobMaxHp() < mobs.get(y).getMobMaxHp()) {
								MapleMonster tempMob = mobs.get(x);
								mobs.set(x, mobs.get(y));
								mobs.set(y, tempMob);
							}
						}
					}

					ArrayList<RangeAttack> attacks = new ArrayList<>();

					attacks.add(
							new RangeAttack(4141501, mobs.isEmpty() ? player.getPosition() : mobs.get(0).getPosition(),
									((attack.facingleft >>> 4 & 0xF) == 0) ? 0 : 1, 0, 1));
					attacks.add(
							new RangeAttack(4141501, mobs.isEmpty() ? player.getPosition() : mobs.get(0).getPosition(),
									((attack.facingleft >>> 4 & 0xF) == 0) ? 0 : 1, 0, 1));
					attacks.add(
							new RangeAttack(4141501, mobs.isEmpty() ? player.getPosition() : mobs.get(0).getPosition(),
									((attack.facingleft >>> 4 & 0xF) == 0) ? 0 : 1, 0, 1));
					attacks.add(
							new RangeAttack(4141501, mobs.isEmpty() ? player.getPosition() : mobs.get(0).getPosition(),
									((attack.facingleft >>> 4 & 0xF) == 0) ? 0 : 1, 0, 1));
					attacks.add(
							new RangeAttack(4141501, mobs.isEmpty() ? player.getPosition() : mobs.get(0).getPosition(),
									((attack.facingleft >>> 4 & 0xF) == 0) ? 0 : 1, 0, 1));
					player.getClient().getSession().writeAndFlush(CField.rangeAttack(4141502, attacks));

					player.addCoolTime(4141501, 10000);
				}
			}
		}

		SecondaryStatEffect effKarma = player.getBuffedEffect(SecondaryStat.Origin_Karma_Blade_Stack);
		if (effKarma != null && attack.skill != 4361503) { // 오리진 카르마 블레이드 구현
			if (attack.allDamage.size() > 0) {
				if (player.hexaMasterySkillStack > 0) {
					player.hexaMasterySkillStack--;

					int resetTime = (int) (20000
							- (System.currentTimeMillis() % 1000000000L - effKarma.getStarttime()));
					EnumMap<SecondaryStat, Pair<Integer, Integer>> statups = new EnumMap<SecondaryStat, Pair<Integer, Integer>>(
							SecondaryStat.class);
					statups.put(SecondaryStat.Origin_Karma_Blade_Stack,
							new Pair<Integer, Integer>(player.hexaMasterySkillStack, resetTime));
					player.getClient().getSession()
							.writeAndFlush(CWvsContext.BuffPacket.giveBuff(statups, effKarma, player));

					Item weapon = player.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11);
					player.getMap().broadcastMessage(CField.finalAttackRequest(1, 4361501, 4341054,
							((weapon.getItemId() - 1000000) / 10000), monster));

					if (player.hexaMasterySkillStack <= 0) {
						statups = new EnumMap<SecondaryStat, Pair<Integer, Integer>>(SecondaryStat.class);
						statups.put(SecondaryStat.Origin_Karma_Blade_Stack,
								new Pair<Integer, Integer>(player.hexaMasterySkillStack, 0));
						player.getClient().getSession()
								.writeAndFlush(CWvsContext.BuffPacket.cancelBuff(statups, player));

						ArrayList<RangeAttack> skills = new ArrayList<RangeAttack>();
						skills.add(new RangeAttack(4361502, player.getPosition(),
								((attack.facingleft >>> 4 & 0xF) == 0) ? 0 : 1, 0, 0));
						player.getClient().getSession().writeAndFlush(CField.rangeAttack(4361502, skills));
					}
				}
			}
		}

		if (player.getSkillLevel(11141003) > 0) { // 솔루나 파워 구현
			if (player.getBuffedEffect(SecondaryStat.GlimmeringTime) != null) {
				if (GameConstants.isSolaAndLunaSkill(attack.skill)) {
					if (attack.allDamage.size() > 0) {
						if (!player.skillisCooling(11141003)) {
							ArrayList<RangeAttack> skills = new ArrayList<RangeAttack>();
							skills.add(new RangeAttack(11141000, new Point(0, 0),
									((attack.facingleft >>> 4 & 0xF) == 0) ? 0 : 1, 0, 1));
							player.getClient().getSession().writeAndFlush(CField.rangeAttack(11141000, skills));

							player.addCoolTime(11141003, 20000);
						}
					}
				}
			}
		}

		if (player.getSkillLevel(21141000) > 0) { // 비욘더 VI 구현
			if (attack.allDamage.size() > 0) {
				if (!player.skillisCooling(21141003)) {
					ArrayList<Triple<Integer, Integer, Integer>> mobList = new ArrayList<>();

					player.getClient().getSession()
							.writeAndFlush(CField.bonusAttackRequest(21141003, mobList, true, 0, null));

					player.addCoolTime(21141003,
							player.getBuffedEffect(SecondaryStat.AdrenalinBoost) != null ? 2000 : 4000);
				}
			}
		}

		if (player.getSkillLevel(23141000) > 0) { // 이슈타르의 링 VI : 이슈타르의 낙인 구현
			if (attack.allDamage.size() > 0) {
				Skill skill = SkillFactory.getSkill(23141000);

				if (skill.getSkillList().contains(attack.skill)) {
					if (skill.getSkillList2().contains(attack.skill)) {
						monster.attackCount2++;

						if (monster.attackCount2 >= 12) {
							monster.attackCount2 = 0;

							monster.stigmaStack++;

							if (monster.stigmaStack >= 6) {
								monster.stigmaStack = 6;
							}

							ArrayList<MapleMonster> monsters = new ArrayList<>();

							for (MapleMonster mob : player.getMap().getAllMonster()) {
								if (mob != null) {
									if (mob.stigmaStack > 0) {
										monsters.add(mob);
									}
								}
							}

							ArrayList<MapleMonster> attackMonsters = new ArrayList<>();

							attackMonsters.add(monster);

							player.getClient().getSession()
									.writeAndFlush(SkillPacket.stackMonster(monsters, 23141000, 23141001, 5, 90000));

							if (monster.stigmaStack < 6) {
								player.getClient().getSession()
										.writeAndFlush(SkillPacket.stackAttackMonster(attackMonsters, 23141001, 0, 0));
							} else {
								monster.stigmaStack = 0;

								player.getClient().getSession()
										.writeAndFlush(SkillPacket.stackAttackMonster(attackMonsters, 23141002, 5, 0));
								player.getClient().getSession().writeAndFlush(
										SkillPacket.stackMonster(monsters, 23141000, 23141001, 5, 90000));
							}
						}
					} else {
						monster.attackCount++;

						if (monster.attackCount >= 6) {
							monster.attackCount = 0;

							monster.stigmaStack++;

							if (monster.stigmaStack >= 6) {
								monster.stigmaStack = 6;
							}

							ArrayList<MapleMonster> monsters = new ArrayList<>();

							for (MapleMonster mob : player.getMap().getAllMonster()) {
								if (mob != null) {
									if (mob.stigmaStack > 0) {
										monsters.add(mob);
									}
								}
							}

							ArrayList<MapleMonster> attackMonsters = new ArrayList<>();

							attackMonsters.add(monster);

							player.getClient().getSession()
									.writeAndFlush(SkillPacket.stackMonster(monsters, 23141000, 23141001, 5, 90000));

							if (monster.stigmaStack < 6) {
								player.getClient().getSession()
										.writeAndFlush(SkillPacket.stackAttackMonster(attackMonsters, 23141001, 0, 0));
							} else {
								monster.stigmaStack = 0;

								player.getClient().getSession()
										.writeAndFlush(SkillPacket.stackAttackMonster(attackMonsters, 23141002, 5, 0));
								player.getClient().getSession().writeAndFlush(
										SkillPacket.stackMonster(monsters, 23141000, 23141001, 5, 90000));
							}
						}
					}
				}
			}
		}

		if (player.getBuffedValue(25141506)) { // 오리진 호신강림 구현
			if (attack.skill == 25141501 || attack.skill == 25141502 || attack.skill == 25141503) {
				if (!player.skillisCooling(400051043)) {
					for (int i = 0; i < 6; i++) {
						MapleAtom atom = new MapleAtom(false, player.getId(), 68, true, 25141505,
								player.getPosition().x, player.getPosition().y);
						atom.addForceAtom(new ForceAtom(2, Randomizer.rand(10, 20), Randomizer.rand(20, 40),
								Randomizer.rand(40, 50), 630));
						player.getMap().spawnMapleAtom(atom);
					}

					ArrayList<Triple<Integer, Integer, Integer>> mobList = new ArrayList<>();

					player.getClient().getSession()
							.writeAndFlush(CField.bonusAttackRequest(25141504, mobList, true, 0, null));
				}
			}
		}

		if (player.getBuffedEffect(SecondaryStat.Origin_Artificial_Evolution) != null) { // 오리진 아티피셜 에볼루션 구현
			if (attack.allDamage.size() > 0) {
				ArrayList<RangeAttack> skills = new ArrayList<RangeAttack>();
				skills.add(new RangeAttack(36141501, player.getPosition(),
						((attack.facingleft >>> 4 & 0xF) == 0) ? 0 : 1, 360, 1));
				player.getClient().getSession().writeAndFlush(CField.rangeAttack(36141500, skills));
			}
		}

		if (player.getSkillLevel(37141002) > 0) {
			if (attack.skill == 37141002) { // 릴리즈 파일 벙커 VI 구현
				if (player.hexaMasterySkillStack < 5) {
					player.hexaMasterySkillStack++;

					EnumMap<SecondaryStat, Pair<Integer, Integer>> statups = new EnumMap<SecondaryStat, Pair<Integer, Integer>>(
							SecondaryStat.class);
					statups.put(SecondaryStat.Release_Pile_Bunker_Stack,
							new Pair<Integer, Integer>(player.hexaMasterySkillStack, 0));
					SecondaryStatEffect eff = SkillFactory.getSkill(37141002).getEffect(player.getSkillLevel(37141002));
					player.getClient().getSession()
							.writeAndFlush(CWvsContext.BuffPacket.giveBuff(statups, eff, player));
				}
			}

			if (attack.skill == 37141007) { // 버스트 파일 벙커 구현
				player.hexaMasterySkillStack = 0;

				EnumMap<SecondaryStat, Pair<Integer, Integer>> statups = new EnumMap<SecondaryStat, Pair<Integer, Integer>>(
						SecondaryStat.class);
				statups.put(SecondaryStat.Release_Pile_Bunker_Stack,
						new Pair<Integer, Integer>(player.hexaMasterySkillStack, 0));
				SecondaryStatEffect eff = SkillFactory.getSkill(37141002).getEffect(player.getSkillLevel(37141002));
				player.getClient().getSession().writeAndFlush(CWvsContext.BuffPacket.giveBuff(statups, eff, player));
			}
		}

		if (attack.skill == 64141003) { // 체인아크:스트로크 VI 구현
			player.setSkillCustomValue(64141000, player.getSkillCustomValues(64141000) - 1);

			EnumMap<SecondaryStat, Pair<Integer, Integer>> statups = new EnumMap<SecondaryStat, Pair<Integer, Integer>>(
					SecondaryStat.class);
			statups.put(SecondaryStat.ChainArts_Stroke_Stack,
					new Pair<Integer, Integer>(player.getSkillCustomValues(64141000), 0));
			SecondaryStatEffect eff = SkillFactory.getSkill(64141000).getEffect(player.getSkillLevel(64141000));
			player.getClient().getSession().writeAndFlush(CWvsContext.BuffPacket.giveBuff(statups, eff, player));
		}

		int timePieceId = 101141012;
		int timePieceLevel = player.getSkillLevel(timePieceId);
		if (timePieceLevel > 0 && attack.asist > 0) { // 타임 피스 구현
			if (attack.allDamage.size() > 0) {
				player.hexaMasterySkillStack++;

				if (player.hexaMasterySkillStack >= 4) {
					player.hexaMasterySkillStack = 0;

					boolean isBeta = (player.getGender() == 1);
					for (int i = 0; i < 3; i++) {
						if (player.getMap().getWrecks().size() >= 10) {
							break;
						}

						MapleMagicWreck mw = new MapleMagicWreck(player, isBeta ? timePieceId + 1 : timePieceId,
								new Point(player.getPosition().x + Randomizer.rand(-200, 200),
										player.getPosition().y - Randomizer.rand(50, 250)),
								20000);

						player.getMap().spawnMagicWreck(mw);
					}
				}
			}
		}

		if (player.getSkillLevel(5141000) > 0) { // 피스트 인레이지 VI 구현
			if (attack.skill == 5111019) {
				if (attack.allDamage.size() > 0) {
					SkillFactory.getSkill(5141001).getEffect(player.getSkillLevel(5141001)).applyTo(player);
				}
			}
		}

		if (player.getSkillLevel(5241000) > 0) { // 래피드 파이어 VI 구현
			if (attack.skill == 5241000) {
				if (attack.allDamage.size() > 0) {
					player.hexaMasterySkillStack++;

					Map<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<>();
					statups.put(SecondaryStat.Unk_772, new Pair<>(player.hexaMasterySkillStack, Integer.valueOf(0)));
					player.getClient().getSession().writeAndFlush(CWvsContext.BuffPacket.giveBuff(statups,
							player.getBuffedEffect(SecondaryStat.BMageDeath), player));
				}
			}

			if (attack.skill == 5241001) {
				player.hexaMasterySkillStack = 0;

				Map<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<>();
				statups.put(SecondaryStat.Unk_772, new Pair<>(player.hexaMasterySkillStack, Integer.valueOf(0)));
				player.getClient().getSession().writeAndFlush(CWvsContext.BuffPacket.giveBuff(statups,
						player.getBuffedEffect(SecondaryStat.BMageDeath), player));
			}
		}

		if (player.getSkillLevel(12141000) > 0) { // 오비탈 플레임 VI 구현
			if (attack.allDamage.size() > 0) {
				if (!player.skillisCooling(12141006)) {
					ArrayList<RangeAttack> skills = new ArrayList<RangeAttack>();
					skills.add(new RangeAttack(12141006, player.getPosition(), player.isFacingLeft() ? 1 : 0, 0, 1));
					player.getClient().getSession().writeAndFlush(CField.rangeAttack(12141000, skills));

					player.addCoolTime(12141006, 15000);
				}
			}
		}

		if (player.getBuffedEffect(22201501) != null) { // 오리진 조디악 버스트 구현
			if (attack.allDamage.size() > 0) {
				if (!player.skillisCooling(22201501)) {
					int[] delays = { 90, 300, 510, 690, 870, 1080, 1200, 1350, 1500 };
					int size = delays.length;
					for (int i = 0; i < size; i++) {
						ArrayList<RangeAttack> skills = new ArrayList<RangeAttack>();
						skills.add(new RangeAttack(22201501, monster.getPosition(), player.isFacingLeft() ? 1 : 0,
								delays[i], 1));
						player.getClient().getSession().writeAndFlush(CField.rangeAttack(22201500, skills));
					}

					player.addCoolTime(22201501, 3000);
				}
			}
		}

		if (player.getSkillLevel(27141001) > 0) { // 엔드리스 다크니스 구현
			if (player.getBuffedValue(20040217) || player.getBuffedValue(20040219) || player.getBuffedValue(20040220)) {
				if (GameConstants.isDarkSkills(attack.skill) || GameConstants.isEquilibriumSkills(attack.skill)) {
					if (!player.skillisCooling(27141003)) {
						ArrayList<RangeAttack> skills = new ArrayList<RangeAttack>();
						skills.add(new RangeAttack(27141003, monster.getPosition(), player.isFacingLeft() ? 1 : 0, 300,
								1));
						player.getClient().getSession().writeAndFlush(CField.rangeAttack(27140002, skills));

						player.addCoolTime(27141003, 2000);
					}
				}
			}
		}

		if (player.getSkillLevel(31141002) > 0) { // 데모닉 스피어 구현
			if (GameConstants.isDemonSlash(attack.skill)) {
				if (attack.allDamage.size() > 0) {
					if (!player.skillisCooling(31141007)) {
						ArrayList<RangeAttack> skills = new ArrayList<RangeAttack>();
						skills.add(new RangeAttack(31141006, player.getPosition(),
								((attack.facingleft >>> 4 & 0xF) == 0) ? 0 : 1, 90, 1));
						player.getClient().getSession().writeAndFlush(CField.rangeAttack(31141006, skills));
					}
				}
			}
		}

		if (player.getBuffedValue(32141000)) { // 데스 VI 구현
			if (attack.skill == 32121054 || attack.skill == 32141004) {
				if (monster.getStats().isBoss()) {
					player.unionAuraBossAttackCount++;

					if (player.unionAuraBossAttackCount >= 3) {
						if (player.hexaMasterySkillStack < 48) {
							player.hexaMasterySkillStack += 12;
						}

						player.unionAuraBossAttackCount = 0;
					}
				} else if (!monster.isAlive()) {
					player.unionAuraKillCount++;

					if (player.unionAuraKillCount >= 12) {
						if (player.hexaMasterySkillStack < 48) {
							player.hexaMasterySkillStack += 12;
						}

						player.unionAuraKillCount = 0;
					}
				}
			}

			Map<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<>();
			statups.put(SecondaryStat.BMageDeath, new Pair<>(Integer.valueOf(player.getDeath()), Integer.valueOf(0)));
			player.getClient().getSession().writeAndFlush(
					CWvsContext.BuffPacket.giveBuff(statups, player.getBuffedEffect(SecondaryStat.BMageDeath), player));
		}

		if (player.getBuffedValue(33141503)) { // 오리진 네이쳐스 빌리프 구현
			if (SkillFactory.getSkill(33141501).getSkillList().contains(attack.skill)) {
				if (attack.allDamage.size() > 0) {
					if (!player.skillisCooling(33141501)) {
						List<Triple<Integer, Integer, Integer>> mobList = new ArrayList<>();

						player.getClient().getSession()
								.writeAndFlush(CField.bonusAttackRequest(33141501, mobList, true, 780, 0));

						player.addCoolTime(33141501, 2000);
					}
				}
			}
		}

		if (attack.skill == 33141000 || attack.skill == 33141001) { // 와일드 발칸 VI 구현
			if (attack.allDamage.size() > 0) {
				if (monster != null && monster.isBuffed(MonsterStatus.MS_JaguarBleeding)) {
					if (player.hexaMasterySkillStack < 50) {
						player.hexaMasterySkillStack++;

						SkillFactory.getSkill(33141004).getEffect(player.getSkillLevel(33141000)).applyTo(player,
								false);
					} else {
						player.hexaMasterySkillStack = 0;

						SkillFactory.getSkill(33141004).getEffect(player.getSkillLevel(33141000)).applyTo(player,
								false);
						SkillFactory.getSkill(33141004).getEffect(player.getSkillLevel(33141000)).applyTo(player, true);
					}
				}
			}
		}

		if (SkillFactory.getSkill(63110011).getSkillList().contains(attack.skill)) { // 카인 6차 발현 스킬 포제션 해제 구현
			player.cancelEffectFromBuffStat(SecondaryStat.Possession);
		}

		if (player.getBuffedValue(63141503)) { // 오리진 어나일레이션 구현
			if (attack.skill == 63141505) {
				if (attack.allDamage.size() > 0) {
					if (monster != null) {
						if (!player.skillisCooling(63141502)) {
							for (Integer skillId : SkillFactory.getSkill(63141502).getSkillList()) {
								if (player.skillisCooling(skillId)) {
									player.changeCooldown(skillId, -1000);
								}
							}

							List<Triple<Integer, Integer, Integer>> mobList = new ArrayList<>();

							mobList.add(new Triple<>(0, monster.getObjectId(), 0));

							player.getClient().getSession().writeAndFlush(
									CField.bonusAttackRequest(63141502, mobList, true, 0, new int[] { 0, 0 }));

							player.addCoolTime(63141502, 250);
						}
					}
				}
			}
		}

		if (player.getBuffedValue(155141501)) { // 오리진 가장 오래된 심연 구현
			if (attack.allDamage.size() >= 0) {
				if (!player.skillisCooling(155141501)) {
					ArrayList<Triple<Integer, Integer, Integer>> mobList = new ArrayList<>();

					player.getClient().getSession()
							.writeAndFlush(CField.bonusAttackRequest(155141501, mobList, true, 0, null));

					player.addCoolTime(155141501, 7000);
				}
			}
		}
	}

	public static void applyAttack(final AttackInfo attack, Skill theSkill, final MapleCharacter player,
			double maxDamagePerMonster, SecondaryStatEffect effect, AttackType attack_type, boolean BuffAttack,
			boolean energy) {
		MapleSummon summon;
		SecondaryStatEffect combatRecovery;
		MapleMonster monster;
		boolean afterimageshockattack;
		int multikill;
		PlayerStats stats;
		long hpMob;
		long totDamageToOneMonster;
		long totDamage;
		block858: {
			block859: {
				SecondaryStatEffect bytalsteal;
				SecondaryStatEffect stst;
				if (attack.summonattack == 0) {
					// empty if block
				}

				if (player.isGM()) {
					player.dropMessageGM(6, "공격스킬 : " + attack.skill + " | " + SkillFactory.getSkillName(attack.skill));
				}
				player.checkSpecialCoreSkills("prob", 0, effect);
				if (attack.skill != 0) {
					player.checkSpecialCoreSkills("cooltime", 0, effect);
					if (effect == null) {
						player.getClient().getSession().writeAndFlush((Object) CWvsContext.enableActions(player));
						return;
					}
					if (GameConstants.isMulungSkill(attack.skill)) {
						if (player.getMapId() / 10000 != 92502) {
							return;
						}
						if (player.getMulungEnergy() < 10000) {
							return;
						}
					} else if (GameConstants.isPyramidSkill(attack.skill) ? player.getMapId() / 1000000 != 926
							: GameConstants.isInflationSkill(attack.skill)
									&& player.getBuffedValue(SecondaryStat.Inflation) == null) {
						return;
					}
				}
				totDamage = 0L;
				MapleMap map = player.getMap();
				totDamageToOneMonster = 0L;
				hpMob = 0L;
				stats = player.getStat();
				multikill = 0;
				afterimageshockattack = false;
				monster = null;
				ArrayList<Triple<Integer, Integer, Integer>> finalMobList = new ArrayList<Triple<Integer, Integer, Integer>>();

				for (Object oned : attack.allDamage) {
					SecondaryStatEffect mark;
					monster = map.getMonsterByOid(((AttackPair) oned).objectId);

					if (monster == null || monster.getLinkCID() > 0) {
						continue;
					}

					totDamageToOneMonster = 0L;
					hpMob = monster.getMobMaxHp();
					MapleMonsterStats monsterstats = monster.getStats();
					long fixeddmg = monsterstats.getFixedDamage();
					if (monster.getId() >= 9833070 && monster.getId() <= 9833074) {
						continue;
					}

					applyHexaSkill(player, monster, attack, effect);

					// 쉐도우 배트 [S]
					boolean isHexaSkill = (player.getSkillLevel(14141004) > 0);
					int batId = isHexaSkill ? 14141005 : 14000027;
					int batAttackId = isHexaSkill ? 14141007 : 14000029;
					int enhanceBatId = isHexaSkill ? 14141008 : 14110033;
					int enhanceBatAttackId = isHexaSkill ? 14141010 : 14110035;
					int enhanceBatRangeAttackFirstId = isHexaSkill ? 14141009 : 14110034;
					int enhanceBatRangeAttackSecondId = isHexaSkill ? 14141011 : 14111036;

					// 추가타 (강화 쉐도우 배트)
					if (attack.skill == enhanceBatAttackId) {
						ArrayList<RangeAttack> skills = new ArrayList<RangeAttack>();

						skills.add(new RangeAttack(enhanceBatRangeAttackSecondId, monster.getPosition(), 0, 0, 1));

						player.getClient().getSession()
								.writeAndFlush(CField.rangeAttack(enhanceBatRangeAttackFirstId, skills));
					}

					if (player.getBuffedValue(14001027) || player.getBuffedValue(14141004)) {
						int attackChange = 50;
						int limitBatCount = 2;

						int[] skills = { 14100027, 14110029, 14120008 };
						for (int skill : skills) {
							if (player.getSkillLevel(skill) > 0) {
								attackChange += SkillFactory.getSkill(skill).getEffect(player.getSkillLevel(skill))
										.getProp();
								limitBatCount += SkillFactory.getSkill(skill).getEffect(player.getSkillLevel(skill))
										.getY();
							}
						}

						MapleSummon bat = null;
						int batSummonCount = 0;
						for (MapleSummon sum : player.getSummons()) {
							if (sum.getSkill() == batId) {
								if (bat == null) {
									bat = sum;
								}

								batSummonCount++;
							}
						}

						MapleSummon enhanceBat = null;
						int enhanceBatSummonCount = 0;
						for (MapleSummon sum : player.getSummons()) {
							if (sum.getSkill() == enhanceBatId) {
								if (enhanceBat == null) {
									enhanceBat = sum;
								}

								enhanceBatSummonCount++;
							}
						}

						// 사출 (쉐도우 배트)
						if (bat != null && batSummonCount > 0 && attack.skill != batAttackId
								&& attack.skill != enhanceBatAttackId && attack.skill != enhanceBatRangeAttackFirstId
								&& attack.skill != enhanceBatRangeAttackSecondId) {
							if (Randomizer.isSuccess(attackChange)) {
								MapleAtom atom = new MapleAtom(false, player.getId(), 15, true, batAttackId,
										player.getTruePosition().x, player.getTruePosition().y);
								ForceAtom forceAtom = new ForceAtom(15, 1, 5, Randomizer.rand(45, 90), 500);

								List<MapleMapObject> objects = player.getMap().getMapObjectsInRange(
										player.getTruePosition(), 50000.0,
										Arrays.asList(new MapleMapObjectType[] { MapleMapObjectType.MONSTER }));
								boolean already = false;
								for (MapleMapObject object : objects) {
									MapleMonster mob = (MapleMonster) object;

									if (mob.getStats().isBoss()) {
										atom.setDwFirstTargetId(mob.getObjectId());
										already = true;
									}
								}

								if (!already && objects.size() > 0) {
									atom.setDwFirstTargetId(
											objects.get(Randomizer.rand(0, objects.size() - 1)).getObjectId());
								}

								forceAtom.setnAttackCount(2);
								forceAtom.setnInc(2);

								atom.addForceAtom(forceAtom);
								player.getMap().spawnMapleAtom(atom);

								player.getMap().broadcastMessage(CField.SummonPacket.updateSummon(bat, 99));
								player.getMap().broadcastMessage(CField.SummonPacket.removeSummon(bat, true));
								player.removeSummon(bat);
							}
						}

						// 사출 (강화 쉐도우 배트)
						if (enhanceBat != null && enhanceBatSummonCount > 0 && attack.skill != batAttackId
								&& attack.skill != enhanceBatAttackId && attack.skill != enhanceBatRangeAttackFirstId
								&& attack.skill != enhanceBatRangeAttackSecondId) {
							if (Randomizer.isSuccess(attackChange)) {
								MapleAtom atom = new MapleAtom(false, player.getId(), 15, true, enhanceBatAttackId,
										player.getTruePosition().x, player.getTruePosition().y);
								ForceAtom forceAtom = new ForceAtom(15, 1, 5, Randomizer.rand(45, 90), 500);

								List<MapleMapObject> objects = player.getMap().getMapObjectsInRange(
										player.getTruePosition(), 50000.0,
										Arrays.asList(new MapleMapObjectType[] { MapleMapObjectType.MONSTER }));
								boolean already = false;
								for (MapleMapObject object : objects) {
									MapleMonster mob = (MapleMonster) object;

									if (mob.getStats().isBoss()) {
										atom.setDwFirstTargetId(mob.getObjectId());
										already = true;
									}
								}

								if (!already && objects.size() > 0) {
									atom.setDwFirstTargetId(
											objects.get(Randomizer.rand(0, objects.size() - 1)).getObjectId());
								}

								forceAtom.setnAttackCount(2);
								forceAtom.setnInc(2);

								atom.addForceAtom(forceAtom);
								player.getMap().spawnMapleAtom(atom);

								player.getMap().broadcastMessage(CField.SummonPacket.updateSummon(enhanceBat, 99));
								player.getMap().broadcastMessage(CField.SummonPacket.removeSummon(enhanceBat, true));
								player.removeSummon(enhanceBat);
							}
						}

						// 스폰 (쉐도우 배트)
						if (batSummonCount < limitBatCount && attack.skill != batAttackId
								&& attack.skill != enhanceBatAttackId && attack.skill != enhanceBatRangeAttackFirstId
								&& attack.skill != enhanceBatRangeAttackSecondId) {
							player.attackCountBat++;

							if (player.attackCountBat >= 3) {
								player.attackCountBat = 0;

								if (player.spawnCountBat < 5) { // 쉐도우 배트
									player.spawnCountBat++;

									SecondaryStatEffect effBat = SkillFactory.getSkill(batId)
											.getEffect(player.getSkillLevel(batId));
									effBat.applyTo(player, player.getPosition(), false, 60000);
								} else { // 강화 쉐도우 배트
									player.spawnCountBat = 0;

									SecondaryStatEffect effBat = SkillFactory.getSkill(enhanceBatId)
											.getEffect(player.getSkillLevel(enhanceBatId));
									effBat.applyTo(player, player.getPosition(), false, 60000);
								}
							}
						}
					}
					// 쉐도우 배트 [E]

					// 래버너스 배트 [S]
					isHexaSkill = (player.getSkillLevel(14141012) > 0);
					int ravenousBatId = isHexaSkill ? 14141013 : 14120017;
					int ravenousBatAttackId = isHexaSkill ? 14141014 : 14120018;
					int enhanceRavenousBatId = isHexaSkill ? 14141015 : 14120019;
					int enhanceRavenousBatAttackId = isHexaSkill ? 14141016 : 14120020;

					// 회복 (래버너스 배트)
					if (attack.skill == ravenousBatAttackId) {
						player.addHP(player.getStat().getMaxHp() / 100 * 1);
					}

					// 회복 (강화 래버너스 배트)
					if (attack.skill == enhanceRavenousBatAttackId) {
						player.addHP(player.getStat().getMaxHp() / 100 * 5);
					}

					if (player.getBuffedValue(14121016) || player.getBuffedValue(14141012)) {
						int attackChange = 50;
						int limitBatCount = 2;

						int[] skills = { 14100027, 14110029, 14120008 };
						for (int skill : skills) {
							if (player.getSkillLevel(skill) > 0) {
								attackChange += SkillFactory.getSkill(skill).getEffect(player.getSkillLevel(skill))
										.getProp();
								limitBatCount += SkillFactory.getSkill(skill).getEffect(player.getSkillLevel(skill))
										.getY();
							}
						}

						MapleSummon ravenousBat = null;
						int ravenousBatSummonCount = 0;
						for (MapleSummon sum : player.getSummons()) {
							if (sum.getSkill() == ravenousBatId) {
								if (ravenousBat == null) {
									ravenousBat = sum;
								}

								ravenousBatSummonCount++;
							}
						}

						MapleSummon enhanceRavenousBat = null;
						int enhanceRavenousBatSummonCount = 0;
						for (MapleSummon sum : player.getSummons()) {
							if (sum.getSkill() == enhanceRavenousBatId) {
								if (enhanceRavenousBat == null) {
									enhanceRavenousBat = sum;
								}

								enhanceRavenousBatSummonCount++;
							}
						}

						// 사출 (래버너스 배트)
						if (ravenousBat != null && ravenousBatSummonCount > 0 && attack.skill != ravenousBatAttackId
								&& attack.skill != enhanceRavenousBatAttackId) {
							if (Randomizer.isSuccess(attackChange)) {
								MapleAtom atom = new MapleAtom(false, player.getId(), 15, true, ravenousBatAttackId,
										player.getTruePosition().x, player.getTruePosition().y);
								ForceAtom forceAtom = new ForceAtom(15, 1, 5, Randomizer.rand(45, 90), 500);

								List<MapleMapObject> objects = player.getMap().getMapObjectsInRange(
										player.getTruePosition(), 50000.0,
										Arrays.asList(new MapleMapObjectType[] { MapleMapObjectType.MONSTER }));
								boolean already = false;
								for (MapleMapObject object : objects) {
									MapleMonster mob = (MapleMonster) object;

									if (mob.getStats().isBoss()) {
										atom.setDwFirstTargetId(mob.getObjectId());
										already = true;
									}
								}

								if (!already && objects.size() > 0) {
									atom.setDwFirstTargetId(
											objects.get(Randomizer.rand(0, objects.size() - 1)).getObjectId());
								}

								forceAtom.setnAttackCount(2);
								forceAtom.setnInc(2);

								atom.addForceAtom(forceAtom);
								player.getMap().spawnMapleAtom(atom);

								player.getMap().broadcastMessage(CField.SummonPacket.updateSummon(ravenousBat, 99));
								player.getMap().broadcastMessage(CField.SummonPacket.removeSummon(ravenousBat, true));
								player.removeSummon(ravenousBat);
							}
						}

						// 사출 (강화 래버너스 배트)
						if (enhanceRavenousBat != null && enhanceRavenousBatSummonCount > 0
								&& attack.skill != ravenousBatAttackId && attack.skill != enhanceRavenousBatAttackId) {
							if (Randomizer.isSuccess(attackChange)) {
								MapleAtom atom = new MapleAtom(false, player.getId(), 15, true,
										enhanceRavenousBatAttackId, player.getTruePosition().x,
										player.getTruePosition().y);
								ForceAtom forceAtom = new ForceAtom(15, 1, 5, Randomizer.rand(45, 90), 500);

								List<MapleMapObject> objects = player.getMap().getMapObjectsInRange(
										player.getTruePosition(), 50000.0,
										Arrays.asList(new MapleMapObjectType[] { MapleMapObjectType.MONSTER }));
								boolean already = false;
								for (MapleMapObject object : objects) {
									MapleMonster mob = (MapleMonster) object;

									if (mob.getStats().isBoss()) {
										atom.setDwFirstTargetId(mob.getObjectId());
										already = true;
									}
								}

								if (!already && objects.size() > 0) {
									atom.setDwFirstTargetId(
											objects.get(Randomizer.rand(0, objects.size() - 1)).getObjectId());
								}

								forceAtom.setnAttackCount(2);
								forceAtom.setnInc(2);

								atom.addForceAtom(forceAtom);
								player.getMap().spawnMapleAtom(atom);

								player.getMap()
										.broadcastMessage(CField.SummonPacket.updateSummon(enhanceRavenousBat, 99));
								player.getMap()
										.broadcastMessage(CField.SummonPacket.removeSummon(enhanceRavenousBat, true));
								player.removeSummon(enhanceRavenousBat);
							}
						}

						// 스폰 (래버너스 배트)
						if (ravenousBatSummonCount < limitBatCount && attack.skill != ravenousBatAttackId
								&& attack.skill != enhanceRavenousBatAttackId) {
							player.attackCountBat++;

							if (player.attackCountBat >= 3) {
								player.attackCountBat = 0;

								if (player.spawnCountBat < 5) { // 래버너스 배트
									player.spawnCountBat++;

									SecondaryStatEffect effBat = SkillFactory.getSkill(ravenousBatId)
											.getEffect(player.getSkillLevel(ravenousBatId));
									effBat.applyTo(player, player.getPosition(), false, 60000);
								} else { // 강화 래버너스 배트
									player.spawnCountBat = 0;

									SecondaryStatEffect effBat = SkillFactory.getSkill(enhanceRavenousBatId)
											.getEffect(player.getSkillLevel(enhanceRavenousBatId));
									effBat.applyTo(player, player.getPosition(), false, 60000);
								}
							}
						}
					}
					// 래버너스 배트 [E]

					if (player.getBuffedEffect(SecondaryStat.Origin_Nightmare) != null) { // 오리진 나이트메어 구현
						if (!player.skillisCooling(31141502)) {
							int count = 0;
							for (AttackPair tempOned : attack.allDamage) {
								if (count >= 3) {
									break;
								}

								MapleMonster mob = player.getMap().getMonsterByOid(tempOned.objectId);

								ArrayList<Triple<Integer, Integer, Integer>> mobList = new ArrayList<>();

								mobList.add(new Triple<Integer, Integer, Integer>(0, mob.getObjectId(), 0));

								player.getClient().getSession()
										.writeAndFlush(CField.bonusAttackRequest(31141502, mobList, attack.skill));

								player.handleForceGain(mob.getObjectId(), 31141502);

								count++;
							}

							player.addCoolTime(31141502, 300);
						}
					}

					int attackId = 80002889;
					if (player.getBuffedValue(SecondaryStat.BeyondNextAttackProb) != null && attack.skill != attackId
							&& !player.skillisCooling(attackId)) {
						player.getClient().getSession().writeAndFlush(CField.rangeAttack(attackId,
								Arrays.asList(new RangeAttack(attackId, player.getTruePosition(), 0, 0, 1))));

						player.addCoolTime(attackId, 5000);
					}

					if (effect != null && effect.getSourceId() == 37121004) {
						player.getMap().broadcastMessage(CField.RebolvingBunk(player.getId(), monster.getObjectId(),
								monster.getId(), monster.getPosition()));
					}

					for (Pair<Long, Boolean> eachde : ((AttackPair) oned).attack) {
						long eachd = (Long) eachde.left;
						if (fixeddmg != -1L) {
							eachd = monsterstats.getOnlyNoramlAttack() ? (attack.skill != 0 ? 0L : fixeddmg) : fixeddmg;
						}
						totDamageToOneMonster += eachd;
						player.checkSpecialCoreSkills("attackCount", monster.getObjectId(), effect);
						player.checkSpecialCoreSkills("attackCountMob", monster.getObjectId(), effect);
					}
					totDamage += totDamageToOneMonster;

					if (!player.gethottimebossattackcheck()) {
						player.sethottimebossattackcheck(true);
					}

					if (monster.getId() != 8900002 && monster.getId() != 8900102) {
						player.checkMonsterAggro(monster);
					}
					if (!(attack.skill == 0 || SkillFactory.getSkill(attack.skill).isChainAttack() || effect.isMist()
							|| effect.getSourceId() == 400021030 || GameConstants.isLinkedSkill(attack.skill)
							|| GameConstants.isNoApplySkill(attack.skill) || GameConstants.isNoDelaySkill(attack.skill)
							|| monster.getStats().isBoss()
							|| !(player.getTruePosition().distanceSq(monster.getTruePosition()) > GameConstants
									.getAttackRange(effect, player.getStat().defRange)))) {
						player.dropMessageGM(-5, "타겟이 범위를 벗어났습니다.");
					}
					if (monster != null && player.getBuffedValue(SecondaryStat.PickPocket) != null) {
						SecondaryStatEffect eff = player.getBuffedEffect(SecondaryStat.PickPocket);
						switch (attack.skill) {
						case 0:
						case 4001334:
						case 4201004:
						case 4201005:
						case 4201012:
						case 4211002:
						case 4211004:
						case 4211011:
						case 4221007:
						case 4221010:
						case 4221014:
						case 4221016:
						case 4221017:
						case 4221052:
						case 400041002:
						case 400041003:
						case 400041004:
						case 400041005:
						case 400041025:
						case 400041026:
						case 400041027:
						case 400041039:
						case 4241000:
						case 4241001:
						case 4241002:
						case 4241003:
						case 4241500:
						case 4241501:
						case 4241502: {
							int skillId = player.getSkillLevel(4241006) > 0 ? 4241006 : 4211006;
							int i;
							int max = SkillFactory.getSkill(skillId).getEffect(player.getSkillLevel(skillId))
									.getBulletCount();
							int rand = eff.getProp();
							int suc = 0;
							if (player.getSkillLevel(4220045) > 0) {
								rand += SkillFactory.getSkill(4220045).getEffect(player.getSkillLevel(4220045))
										.getProp();
								max += SkillFactory.getSkill(4220045).getEffect(player.getSkillLevel(4220045))
										.getBulletCount();
							}
							if (attack.skill == 4221007) {
								rand /= 2;
							}
							for (i = 0; i < attack.hits; ++i) {
								if (!Randomizer.isSuccess(rand)) {
									continue;
								}
								++suc;
							}
							for (i = 0; i < suc; ++i) {
								int 기준;
								Point pos = new Point(monster.getTruePosition().x, monster.getTruePosition().y);
								int delay = 208;
								int plus = 120 * i;
								delay += plus;
								if (suc % 2 == 0) {
									기준 = suc / 2;
									if (i < 기준) {
										pos.x -= 18 * (기준 - i);
									} else if (i >= 기준) {
										pos.x += 18 * (i - 기준);
									}
								} else {
									기준 = suc / 2;
									if (i < 기준) {
										pos.x -= 18 * (기준 - i);
									} else if (i > 기준) {
										pos.x += 18 * (i - 기준);
									}
								}
								if (player.getPickPocket().size() >= max) {
									continue;
								}
								player.getMap().spawnMesoDrop(1,
										player.getMap().calcDropPos(pos, monster.getTruePosition()), monster, player,
										false, (byte) 0, delay);
								player.getClient().getSession().writeAndFlush(
										(Object) CWvsContext.BuffPacket.giveBuff(eff.getStatups(), eff, player));
							}
							break;
						}
						}
					}
					// 플래시 미라주
					if (GameConstants.isBowMaster(player.getJob())) {
						if (player.getBuffedValue(3111015)
								&& (SkillFactory.getSkill(3111015).getSkillList().contains(attack.skill)
										|| SkillFactory.getSkill(3111015).getSkillList2().contains(attack.skill))) {
							Skill eff = SkillFactory.getSkill(3111015);
							int max = eff.getEffect(player.getSkillLevel(3111015)).getU();
							if (player.getSkillLevel(3120021) > 0) {
								max = SkillFactory.getSkill(3120021).getEffect(player.getSkillLevel(3120021)).getU();
							}
							boolean charge = false;
							if (eff.getSkillList().contains(attack.skill)) {
								charge = true;
							} else if (eff.getSkillList2().contains(attack.skill)) {
								player.AgonyBattackCount += 1;
								if (player.AgonyBattackCount >= eff.getEffect(player.getSkillLevel(3111015)).getU2()) {
									player.AgonyBattackCount = 0;
									charge = true;
								}
							}
							if (charge) {
								player.addSkillCustomInfo(3111015, 1);
								if (player.getSkillCustomValue0(3111015) >= max) {
									player.getClient().send(CField
											.FlashMirage(player.getSkillLevel(3120021) > 0 ? 4 : 3, new ArrayList<>()));
									player.removeSkillCustomInfo(3111015);
									eff.getEffect(player.getSkillLevel(3111015)).applyTo(player);
								} else {
									eff.getEffect(player.getSkillLevel(3111015)).applyTo(player);
								}
							}
						}
					}

					Integer[] 격투스킬 = new Integer[] { 5101012, 5111002, 5121016 };
					if (player.getBuffedValue(SecondaryStat.SeaSerpent) != null) {
						MapleCharacter chr = player;
						SecondaryStatEffect SerpentStone = SkillFactory.getSkill(5111017).getEffect(1);
						int skillid = 0;
						int max = SerpentStone.getU();
						boolean isHexa = chr.getSkillLevel(5140004) > 0;
						if (Arrays.asList(격투스킬).contains(attack.skill)) {
							if (chr.getSkillLevel(5110016) > 0) {
								skillid = 5111021;
								if (chr.getSkillLevel(5120029) > 0) {
									skillid = 5121023;
								}
								if (chr.getSkillLevel(5120029) > 0) {
									skillid = 5121023;
								}
							}
							if (!chr.skillisCooling(skillid) && !chr.getBuffedValue(5110020)) {
								List<RangeAttack> attacks = new ArrayList<>();
								if (isHexa) { // 씨 서펜트 VI 구현
									attacks.add(new RangeAttack(5141008, chr.getPosition(),
											((attack.facingleft >>> 4 & 0xF) == 0) ? 0 : 1, 0, 1));
								} else {
									attacks.add(new RangeAttack(5121023, chr.getPosition(),
											((attack.facingleft >>> 4 & 0xF) == 0) ? 0 : 1, 0, 1));
								}
								chr.getClient().getSession().writeAndFlush(CField.rangeAttack(5101017, attacks));
								if (chr.서펜트스톤 < max) {
									chr.서펜트스톤++;
									chr.수룡의의지++;
									SerpentStone.applyTo(chr, false, false);
								}
							} else if (chr.getBuffedValue(5110020)) {
								chr.cancelEffectFromBuffStat(SecondaryStat.IndieSummon, 5110020);
								chr.getClient().getSession()
										.writeAndFlush(CField.rangeAttack(5101017,
												Arrays.asList(new RangeAttack(5121023, chr.getPosition(),
														((attack.facingleft >>> 4 & 0xF) == 0) ? 0 : 1, 0, 1))));
								chr.getClient().getSession()
										.writeAndFlush(CField.rangeAttack(5110018,
												Arrays.asList(new RangeAttack(5111019, chr.getPosition(),
														((attack.facingleft >>> 4 & 0xF) == 0) ? 0 : 1, 0, 1))));
							}
						} else if (attack.skill == 5121007 || attack.skill == 5141000 || attack.skill == 5141001) { // 피스트
																													// 인레이지
																													// VI
																													// 구현
							if (!chr.skillisCooling(5121025) && !chr.getBuffedValue(5110020)) {
								List<RangeAttack> attacks = new ArrayList<>();
								if (isHexa) { // 씨 서펜트 VI 구현
									attacks.add(new RangeAttack(5141006, chr.getPosition(),
											((attack.facingleft >>> 4 & 0xF) == 0) ? 0 : 1, 0, 1));
								} else {
									attacks.add(new RangeAttack(5121025, chr.getPosition(),
											((attack.facingleft >>> 4 & 0xF) == 0) ? 0 : 1, 0, 1));
								}
								chr.getClient().getSession().writeAndFlush(CField.rangeAttack(5101017, attacks));
								if (chr.서펜트스톤 < max) {
									chr.서펜트스톤++;
									chr.수룡의의지++;
									SerpentStone.applyTo(chr, false, false);
								}
							} else if (chr.getBuffedValue(5110020)) {
								chr.cancelEffectFromBuffStat(SecondaryStat.IndieSummon, 5110020);
								final List<SecondAtom> atoms = new ArrayList<SecondAtom>();
								atoms.add(new SecondAtom(57, player.getId(), 0, 5121027, 5000, 0, 15,
										new Point(monster.getPosition().x, monster.getPosition().y), Arrays.asList(0)));
								player.spawnSecondAtom(atoms);
							}
						}
					}

					// 신궁 패시브 처리
					if (GameConstants.isMarksMan(player.getJob())) {

						SecondaryStatEffect concentration = SkillFactory.getSkill(3220021)
								.getEffect(player.getSkillLevel(3220021));
						if (player.getConcentration() < 100) {
							player.setConcentration((byte) (player.getConcentration() + concentration.getX()));
						}
						// System.err.println("test - 작동?");
						if (attack.skill == 3221019 || attack.skill == 3221027) {
							// System.err.println("test - 작동22?");
							SkillFactory.getSkill(3221023).getEffect(player.getSkillLevel(3221023)).applyTo(player);
						}

					}

					// [othello] - 윈드브레이커 마나소모 임시처리
					if (GameConstants.isWindBreaker(player.getJob())) {
						if (attack.skill == 400031068 || attack.skill == 400031004 || attack.skill == 400031003) {
							if (monster.getStats().isBoss()) {
								player.addMP(2000L);
							}

							player.addMP(500L);
						} else if (attack.skill == 13121017) {
							player.addMP(70L);
						} else if (attack.skill == 13121055) {
							player.addMP(200L);
						}
					}
					// 언카운터블 애로우 임시처리
					if (attack.skill == 3121015) {
						player.addMP(200L);
					}

					// 400021122 라라 마나소모 임시처리
					if (attack.skill == 400021122) {
						player.addMP(500L);
					}
					// [othello] - 칼리 마나소모 임시처리
					if (GameConstants.isKhali(player.getJob())) {
						if (attack.skill == 400041086) {
							player.addMP((int) (player.getStat().getCurrentMaxMp(player)) / 3);
						} else if (attack.skill == 400041088) {
							player.addMP(500L);
						}
					}

					// [othello] - 바이퍼 수룡의 의지
					if (GameConstants.isViper(player.getJob())) {
						if (player.getBuffedValue(5121052)) {
							SecondaryStatEffect SerpentStone = SkillFactory.getSkill(5121052)
									.getEffect(player.getSkillLevel(5121052));
							SkillFactory.getSkill(5121055).getEffect(player.getSkillLevel(5121055)).applyTo(player,
									player.getPosition());
						}
						// [othello] - 바이퍼 하울링 피스트 무적
						if (attack.skill == 400051070) {
							SkillFactory.getSkill(400051071).getEffect(player.getSkillLevel(400051070)).applyTo(player,
									false, false);
						}
					}
					// 메기도 플레임 재생성
					if (GameConstants.isFPMage(player.getJob())) {
						if (attack.skill == 2121052) {
							List<MapleMonster> mobs = new ArrayList<>();
							for (MapleMonster mob : player.getMap().getAllMonster()) {
								if (effect.calculateBoundingBox(monster.getPosition(), true).contains(mob.getPosition())
										|| effect.calculateBoundingBox(monster.getPosition(), false)
												.contains(mob.getPosition())) {
									mobs.add(mob);
								}
							}

							SecondAtom2 at = SkillFactory.getSkill(2121052).getSecondAtoms().get(0);
							for (int i = 0; i < 2; i++) {
								if (player.getSkillCustomValue0(2121052) < effect.getX()) {
									player.addSkillCustomInfo(2121052, 1);
									if (!mobs.isEmpty()) {
										at.setTarget(mobs.get(0).getObjectId());
									}
									player.createSecondAtom(at,
											new Point(monster.getPosition().x + 150, monster.getPosition().y + 150),
											true);
								}
							}
						}
					}

					if (player.getSkillLevel(400051015) > 0 && GameConstants.isViper(player.getJob())) {
						if (monster != null) {
							if (player.서펜트스크류 == 1 && !player.skillisCooling(400051015)
									&& !player.getBuffedValue(400051015)) {
								HashMap<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
								// player.dropMessageGM(5, "서펜트 값 1이고 쿨타임 0이라 재실행");
								statups.put(SecondaryStat.SerpentScrew,
										new Pair<Integer, Integer>((int) player.서펜트스크류, 0));
							}
							if (monster.getStats().isBoss()) {
								if (monster.isAlive()) {
									player.Serpent = player.Serpent + 1;
								}
							} else if (!monster.isAlive()) {
								player.Serpent2 = player.Serpent2 + 1;
								player.Serpent = player.Serpent + 3;
							} else if (monster.isAlive()) {
								player.Serpent = player.Serpent + 3;
							}
							if (player.Serpent2 > 40) {
								player.Serpent2 = 0;
								player.changeCooldown(400051015, -1000);
							}
							if (player.Serpent > 300) {
								player.Serpent = 0;
								if (player.skillisCooling(400051015)) {
									// player.dropMessageGM(5, "서펜트 쿨타임 있어서 종료");
									// player.handleSerpent(1);
								} else {
									// player.dropMessageGM(5, "서펜트 쿨타임없어서 재사용됌");
									player.addCooldown(400051015, System.currentTimeMillis(),
											SkillFactory.getSkill(400051015).getEffect(player.getSkillLevel(400051015))
													.getCooldown(player));
									player.getClient().getSession().writeAndFlush(
											(Object) CField.skillCooldown(400051015, SkillFactory.getSkill(400051015)
													.getEffect(player.getSkillLevel(400051015)).getCooldown(player)));
								}

							}
						}
					}

					// 작업 - 바이퍼 서펜트스톤 오토스킬
					if (GameConstants.isViper(player.getJob())) {
						if (player.isAutoSkill(5111017) && player.서펜트스톤 >= 5) {
							SecondaryStatEffect SerpentStone = SkillFactory.getSkill(5111017)
									.getEffect(player.getSkillLevel(5111017));
							player.서펜트스톤 = 0;
							player.cancelEffect(SerpentStone);
							SkillFactory.getSkill(5110020).getEffect(player.getSkillLevel(5110020)).applyTo(player,
									player.getPosition());
						}
					}

					if (player.skillisCooling(1321015)) {
						if (SkillFactory.getSkill(1321015).getSkillList().contains(attack.skill)) {
							player.changeCooldown(1321015, -300);
						}
					}

					// 리인카네이션
					if (GameConstants.isDarkKnight(player.getJob())) {
						if (player.skillisCooling(1321015)) {
							if (SkillFactory.getSkill(1321015).getSkillList().contains(attack.skill)) {
								player.changeCooldown(1321015, -300);
							}
						}
						if (player.getBuffedValue(SecondaryStat.Reincarnation) != null && monster != null) {
							if (player.getReinCarnation() > 0 && monster.getStats().isBoss()) {
								player.setReinCarnation(0);
								if (player.getReinCarnation() == 0) {
									SecondaryStatEffect eff = SkillFactory.getSkill(1320016)
											.getEffect(player.getSkillLevel(1320016));
									int reduce = 0;
									switch ((int) player.getSkillCustomValue0(1321020)) {
									case 1: {
										reduce = eff.getY();
										break;
									}
									case 2: {
										reduce = eff.getW2();
										break;
									}
									case 3: {
										reduce = eff.getS2();
										break;
									}
									}
									player.changeCooldown(1320019, -(reduce * 1000));
									player.getClient().send(CField.EffectPacket.showEffect(player, 0, 1320016, 10, 0, 0,
											(byte) (player.isFacingLeft() ? 1 : 0), true, null, null, null));
									player.getMap().broadcastMessage(player,
											CField.EffectPacket.showEffect(player, 0, 1320016, 10, 0, 0,
													(byte) (player.isFacingLeft() ? 1 : 0), false, null, null, null),
											false);
								}

								// Buff (1)
								List<Pair<SecondaryStat, Integer>> buffSkillid = new ArrayList<>();
								int skillid = (int) (1320020 + player.getSkillCustomValue0(1321020));
								buffSkillid.add(new Pair<>(SecondaryStat.Reincarnation, skillid));

								// Buff (2)
								Map<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<>();
								statups.put(SecondaryStat.Reincarnation, new Pair<>(player.getReinCarnation(),
										(int) player.getBuffLimit(SecondaryStat.Reincarnation, 1320019)));
								player.getClient().send(CWvsContext.BuffPacket.giveBuff(statups,
										player.getBuffedEffect(SecondaryStat.Reincarnation), player, buffSkillid));
							}
						}
					} else if (GameConstants.isCannon(player.getJob())) {
						if ((attack.skill == 5321000 || attack.skill == 5321012 || attack.skill == 5341000
								|| attack.skill == 5341001) && monster != null && attack.targets > 0) { // 캐논 버스터 VI 구현
																										// / 캐논 바주카 VI
																										// 구현
							boolean isHexa = player.getSkillLevel(5341002) > 0;
							long count = player.getSkillCustomValue(isHexa ? 5341002 : 5311013);
							if (player.isAutoSkill(isHexa ? 5341002 : 5311013)
									&& !player.skillisCooling(isHexa ? 5341002 : 5311013) && count > 0) {

								List<RangeAttack> skills = new ArrayList<>();
								skills.add(new RangeAttack(isHexa ? 5341002 : 5311015, monster.getPosition(), 1, 0, 0));
								player.getClient().send(CField.rangeAttack(isHexa ? 5341002 : 5311013, skills));

								player.setSkillCustomInfo(isHexa ? 5341002 : 5311013, count - 1, 0);
								Map<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<>();
								statups.put(SecondaryStat.MiniCannonBall, new Pair<>((int) count - 1, 0));
								player.getClient().getSession()
										.writeAndFlush(CWvsContext.BuffPacket.giveBuff(statups, effect, player));
								SecondaryStatEffect eff = SkillFactory.getSkill(isHexa ? 5341002 : 5311013)
										.getEffect(player.getSkillLevel(isHexa ? 5341002 : 5311013));
								player.getClient().send(
										CField.skillCooldown(isHexa ? 5341002 : 5311013, eff.getCooldown(player)));
								player.addCooldown(isHexa ? 5341002 : 5311013, System.currentTimeMillis(),
										eff.getCooldown(player));
							}
						}
					}

					// 스나이핑 무적
					if (GameConstants.isMarksMan(player.getPlayer().getJob())) {
						if (attack.skill == 400031010) {
							SkillFactory.getSkill(400031010).getEffect(player.getPlayer().getSkillLevel(400031010))
									.applyTo(player.getPlayer(), false, false);
						}
					}

					// 메카닉 전탄발사 무적
					if (GameConstants.isMechanic(player.getPlayer().getJob())) {
						if (attack.skill == 400051041) {
							SkillFactory.getSkill(400051094).getEffect(player.getPlayer().getSkillLevel(400051041))
									.applyTo(player.getPlayer(), false, false);
						}
					}
					// 엔버 소울 레조넌스 무적
					if (GameConstants.isAngelicBuster(player.getPlayer().getJob())) {
						if (attack.skill == 65121003) {
							SkillFactory.getSkill(65121012).getEffect(player.getPlayer().getSkillLevel(65121012))
									.applyTo(player.getPlayer(), false, false);
						}
					}

					// [othello] 코스믹 버스트
					if (GameConstants.isSoulMaster(player.getJob())) {
						if (player.isAutoSkill(11121018) && player.getCosmicCount() >= 5) {
							player.getClient().getSession().writeAndFlush(CField.rangeAttack(11121018,
									Arrays.asList(new RangeAttack(11121018, player.getTruePosition(), 1, 330, 1))));
						}
					}

					if (player.getSkillLevel(5120028) > 0) {
						SkillFactory.getSkill(5120028).getEffect(player.getSkillLevel(5120028)).applyTo(player, false,
								false);
					}
					int probability = 9998;

					if (player.getSkillLevel(400051015) > 0 && attack.skill == 400051015 && attack.targets > 0
							&& player.getBuffedValue(SecondaryStat.SerpentScrew) != null) {
						SecondaryStatEffect eff = SkillFactory.getSkill(400051015)
								.getEffect(player.getSkillLevel(400051015));
						if (player.energy < 9000) { // 100스택까지 쌓임
							player.energy++;
						} else {
							player.energy = 0;
							player.cancelEffectFromBuffStat(SecondaryStat.SerpentScrew, 400051015);
						}
					}

					if (monster != null) {
						monster.setNextSkill(260);
						monster.setNextSkillLvl(3);
					}
					if (attack.skill == 2100001) {
						ArrayList<Pair<MonsterStatus, MonsterStatusEffect>> applys = new ArrayList<Pair<MonsterStatus, MonsterStatusEffect>>();
						SecondaryStatEffect eff = SkillFactory.getSkill(2100001)
								.getEffect(player.getSkillLevel(2100001));
						applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_Burned,
								new MonsterStatusEffect(attack.skill, eff.getDOTTime(), (long) eff.getDOT()
										* totDamageToOneMonster / (long) attack.allDamage.size() / 10000L)));
						monster.applyStatus(player.getClient(), applys, effect);
					}
					if (attack.skill == 1221009 || attack.skill == 1241000 || attack.skill == 1241502) { // 신성 낙인 구현,
																											// 오리진 세이크리드
																											// 바스티온 구현
						monster.stigmaStack++;

						ArrayList<MapleMonster> attackMonsters = new ArrayList<>();

						attackMonsters.add(monster);

						if (monster.stigmaStack >= 5) {
							monster.stigmaStack = 0;

							player.getMap()
									.broadcastMessage(SkillPacket.stackAttackMonster(attackMonsters, 1221023, 5, 202));
						}

						ArrayList<MapleMonster> monsters = new ArrayList<>();

						for (MapleMonster mob : player.getMap().getAllMonster()) {
							if (mob.stigmaStack > 0) {
								monsters.add(mob);
							}
						}

						player.getMap()
								.broadcastMessage(SkillPacket.stackMonster(monsters, 1220022, 1221023, 5, 30000));
					}
					if (attack.skill == 1121015) {
						ArrayList<Pair<MonsterStatus, MonsterStatusEffect>> applys = new ArrayList<Pair<MonsterStatus, MonsterStatusEffect>>();
						SecondaryStatEffect eff = SkillFactory.getSkill(1121015)
								.getEffect(player.getSkillLevel(1121015));
						applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_Burned,
								new MonsterStatusEffect(attack.skill, 60000, (long) eff.getDOT() * totDamageToOneMonster
										/ (long) attack.allDamage.size() / 10000L)));

						monster.applyStatus(player.getClient(), applys, effect);
					}
					if (player.getBuffedValue(SecondaryStat.QuiverCatridge) != null && attack.skill != 400031021
							&& attack.skill != 95001000 && attack.skill != 3111013 && attack.skill != 3100010
							&& attack.skill != 400031000) {
						boolean adquiver = player.getBuffedValue(SecondaryStat.AdvancedQuiver) != null;
						boolean quiverFoolburst = player.getBuffedValue(SecondaryStat.QuiverFullBurst) != null;
						boolean reset = false;
						SecondaryStatEffect effect2 = SkillFactory.getSkill(3101009)
								.getEffect(player.getSkillLevel(3101009));
						SecondaryStatEffect quiverEff = SkillFactory.getSkill(3101009)
								.getEffect(player.getSkillLevel(3101009));
						SecondaryStatEffect adq = SkillFactory.getSkill(3120022)
								.getEffect(player.getSkillLevel(3120022));
						if (player.getSkillLevel(3121016) > 0) {
							quiverEff = SkillFactory.getSkill(3121016).getEffect(player.getSkillLevel(3121016));
						}
						if (quiverFoolburst) {
							SecondaryStatEffect quiverFoolEff = player.getBuffedEffect(SecondaryStat.QuiverFullBurst);
							if (Randomizer.isSuccess(quiverEff.getW())
									&& player.getBuffedEffect(SecondaryStat.DebuffIncHp) == null) {
								player.addHP((int) ((double) player.getStat().getCurrentMaxHp()
										* ((double) adq.getX() / 100.0)));
							}
							// monster.applyStatus(player.getClient(), MonsterStatus.MS_Burned, new
							// MonsterStatusEffect(quiverEff.getSourceId(), quiverEff.getDuration()),
							// quiverEff.getDOT(), effect);
							MapleAtom atom = new MapleAtom(false, player.getId(), 10, true, 3100010,
									player.getTruePosition().x, player.getTruePosition().y);
							atom.setDwFirstTargetId(0);
							ForceAtom forceAtom = new ForceAtom(0, Randomizer.rand(10, 20), Randomizer.rand(5, 10),
									Randomizer.rand(4, 301), (short) Randomizer.rand(20, 48));
							atom.addForceAtom(forceAtom);
							player.getMap().spawnMapleAtom(atom);
							if (System.currentTimeMillis() - player.lastFireArrowTime >= 2000L) {
								player.lastFireArrowTime = System.currentTimeMillis();
								MapleAtom atom2 = new MapleAtom(false, player.getId(), 50, true, 400031029,
										monster.getTruePosition().x, monster.getTruePosition().y);
								for (int i = 0; i < quiverFoolEff.getY(); ++i) {
									atom2.addForceAtom(new ForceAtom(1, Randomizer.rand(30, 60), 10,
											Randomizer.nextBoolean() ? Randomizer.rand(10, 15)
													: Randomizer.rand(190, 195),
											(short) (i * 100)));
								}
								atom2.setDwFirstTargetId(0);
								player.getMap().spawnMapleAtom(atom2);
							}
						} else {
							switch (player.getQuiverType()) {
							case 1: {
								if (!Randomizer.isSuccess(
										attack.skill == 400030002 ? quiverEff.getU() * 2 : quiverEff.getU())) {
									break;
								}

								MapleAtom atom = new MapleAtom(false, player.getId(), 10, true, 3100010,
										player.getTruePosition().x, player.getTruePosition().y);
								atom.setDwFirstTargetId(0);
								ForceAtom forceAtom = new ForceAtom(0, Randomizer.rand(10, 20), Randomizer.rand(5, 10),
										Randomizer.rand(4, 301), (short) Randomizer.rand(20, 48));
								atom.addForceAtom(forceAtom);
								player.getMap().spawnMapleAtom(atom);
							}
							case 2: {
								if (player.getBuffedEffect(SecondaryStat.DebuffIncHp) != null) {
									break;
								}

								player.addHP(player.getStat().getCurrentMaxHp() * adq.getX() / 100L);
								break;
							}
							}
						}
						if (player.getQuiverType() == 0) {
							player.setQuiverType((byte) 1);
						}
						if (player.getRestArrow()[player.getQuiverType() - 1] == 0) {
							if (player.getRestArrow()[0] == 0 && player.getRestArrow()[1] == 0) {
								reset = true;
							} else {
								player.setQuiverType(
										(byte) (player.getQuiverType() == 2 ? 1 : player.getQuiverType() + 1));
								player.getClient().getSession()
										.writeAndFlush((Object) CField.EffectPacket.showEffect(player, 0, 3101009, 57,
												player.getQuiverType() - 1, 0, (byte) (player.isFacingLeft() ? 1 : 0),
												true, player.getPosition(), null, null));
								player.getMap().broadcastMessage(player,
										CField.EffectPacket.showEffect(player, 0, 3101009, 57,
												player.getQuiverType() - 1, 0, (byte) (player.isFacingLeft() ? 1 : 0),
												false, player.getPosition(), null, null),
										false);
							}
						}
						if (!adquiver && !quiverFoolburst) {
							effect2.applyTo(player, reset, false);
						}
					}

					if ((GameConstants.isDarkAtackSkill(attack.skill) || attack.summonattack != 0)
							&& attack.skill != 14000027 && attack.skill != 14000028 && attack.skill != 14000029) {
						player.changeCooldown(14121003, -500);
					}

					int skillLevelId = player.getSkillLevel(14141004) > 0 ? 14141004 : 14000027;
					if (attack.skill == skillLevelId + 1) {
						player.addHP(player.getStat().getCurrentMaxHp() / 100L);
					}

					if (GameConstants.isAngelicBuster(player.getJob())) {
						MapleSummon summon3;
						MapleSummon s;
						if (player.getBuffedValue(400051011) && attack.skill != 60011216
								&& (s = player.getSummon(400051011)) != null) {
							MapleAtom atom = new MapleAtom(true, monster.getObjectId(), 29, true, 400051011,
									monster.getTruePosition().x, monster.getTruePosition().y);
							atom.setDwUserOwner(player.getId());
							atom.setDwFirstTargetId(monster.getObjectId());
							atom.addForceAtom(new ForceAtom(Randomizer.rand(1, 3), Randomizer.rand(30, 50),
									Randomizer.rand(3, 10), Randomizer.rand(4, 301), 0));
							atom.addForceAtom(new ForceAtom(Randomizer.rand(1, 3), Randomizer.rand(30, 50),
									Randomizer.rand(3, 10), Randomizer.rand(4, 301), 0));
							player.getMap().spawnMapleAtom(atom);
							player.setEnergyBurst(player.getEnergyBurst() + 1);
							if (player.getEnergyBurst() == 50) {
								player.setBuffedValue(SecondaryStat.EnergyBurst, 3);
								HashMap<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
								statups.put(SecondaryStat.EnergyBurst,
										new Pair<Integer, Integer>(player.getBuffedValue(SecondaryStat.EnergyBurst),
												(int) player.getBuffLimit(400051011)));
								player.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket
										.giveBuff(statups, player.getBuffedEffect(SecondaryStat.EnergyBurst), player));
								player.getClient().getSession()
										.writeAndFlush((Object) CField.SummonPacket.updateSummon(s, 15));
							} else if (player.getEnergyBurst() == 25) {
								player.setBuffedValue(SecondaryStat.EnergyBurst, 2);
								HashMap<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
								statups.put(SecondaryStat.EnergyBurst,
										new Pair<Integer, Integer>(player.getBuffedValue(SecondaryStat.EnergyBurst),
												(int) player.getBuffLimit(400051011)));
								player.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket
										.giveBuff(statups, player.getBuffedEffect(SecondaryStat.EnergyBurst), player));
								player.getClient().getSession()
										.writeAndFlush((Object) CField.SummonPacket.updateSummon(s, 14));
							}
						}
						if (player.getBuffedValue(400051046) && player.getSkillCustomValue(400051046) == null
								&& (summon3 = player.getSummon(400051046)) != null) {
							player.getClient().getSession().writeAndFlush(
									(Object) CField.SummonPacket.DeathAttack(summon3, Randomizer.rand(8, 9)));
						}
					}
					if (GameConstants.isKadena(player.getJob())) {
						ArrayList<Pair<Integer, Integer>> WeponList = new ArrayList<Pair<Integer, Integer>>();
						WeponList.add(new Pair<Integer, Integer>(0, 64121002));
						WeponList.add(new Pair<Integer, Integer>(1, 64001002));
						WeponList.add(new Pair<Integer, Integer>(1, 64001013));
						WeponList.add(new Pair<Integer, Integer>(2, 64101002));
						WeponList.add(new Pair<Integer, Integer>(2, 64101008));
						WeponList.add(new Pair<Integer, Integer>(3, 64101001));
						WeponList.add(new Pair<Integer, Integer>(4, 64111002));
						WeponList.add(new Pair<Integer, Integer>(5, 64111003));
						WeponList.add(new Pair<Integer, Integer>(6, 64111004));
						WeponList.add(new Pair<Integer, Integer>(6, 64111012));
						WeponList.add(new Pair<Integer, Integer>(7, 64121021));
						WeponList.add(new Pair<Integer, Integer>(7, 64121022));
						WeponList.add(new Pair<Integer, Integer>(7, 64121023));
						WeponList.add(new Pair<Integer, Integer>(7, 64121024));
						WeponList.add(new Pair<Integer, Integer>(8, 64121003));
						WeponList.add(new Pair<Integer, Integer>(8, 64121011));
						WeponList.add(new Pair<Integer, Integer>(8, 64121016));
						boolean attackc = false;
						boolean givebuff = false;
						int attackid = 0;
						if (attack.skill == 64121002 || attack.skill == 64001002 || attack.skill == 64001013
								|| attack.skill == 64101002 || attack.skill == 64101008 || attack.skill == 64101001
								|| attack.skill == 64111002 || attack.skill == 64111003 || attack.skill == 64111004
								|| attack.skill == 64111012 || attack.skill == 64121021 || attack.skill == 64121022
								|| attack.skill == 64121023 || attack.skill == 64121024 || attack.skill == 64121003
								|| attack.skill == 64121011 || attack.skill == 64121016) {
							for (Pair info : WeponList) {
								if (attack.skill != (Integer) info.getRight()) {
									continue;
								}
								if (player.getBuffedEffect(SecondaryStat.WeaponVariety) == null) {
									player.weaponChanges1.clear();
									player.removeSkillCustomInfo(6412);
								}
								if ((Integer) info.left != 0 && !player.weaponChanges1.containsKey(info.left)
										&& player.weaponChanges1.size() < 8) {
									player.weaponChanges1.put((Integer) info.left, (Integer) info.right);
									attackc = true;
									givebuff = true;
								}
								if (player.getSkillCustomValue0(6412) != (long) ((Integer) info.getLeft()).intValue()) {
									givebuff = true;
									attackc = true;
								}
								if (player.weaponChanges1.size() == 1) {
									attackc = false;
								}
								if (givebuff) {
									if (player.getSkillLevel(64120006) > 0) {
										SkillFactory.getSkill(64120006).getEffect(player.getSkillLevel(64120006))
												.applyTo(player, false);
										attackid = 64120006;
									} else if (player.getSkillLevel(64110005) > 0) {
										SkillFactory.getSkill(64110005).getEffect(player.getSkillLevel(64110005))
												.applyTo(player, false);
										attackid = 64110005;
									} else if (player.getSkillLevel(64100004) > 0) {
										SkillFactory.getSkill(64100004).getEffect(player.getSkillLevel(64100004))
												.applyTo(player, false);
										attackid = 64100004;
									}
								}
								if (attackc && System.currentTimeMillis() - player.lastBonusAttckTime > 500L) {
									player.lastBonusAttckTime = System.currentTimeMillis();
									player.getClient().getSession()
											.writeAndFlush(
													(Object) CField
															.bonusAttackRequest(
																	attackid == 64120006 ? 64121020
																			: (attackid == 64110005 ? 64111013
																					: 64101009),
																	finalMobList, true, 0, new int[0]));
								}
								if (!givebuff) {
									continue;
								}
								player.setSkillCustomInfo(6412, ((Integer) info.getLeft()).intValue(), 0L);
							}
						}
						if (player.getBuffedValue(64121053) && attack.skill != 64121055
								&& player.getSkillCustomValue(64121055) == null) {
							ArrayList<Triple<Integer, Integer, Integer>> mobList = new ArrayList<Triple<Integer, Integer, Integer>>();
							mobList.add(new Triple<Integer, Integer, Integer>(monster.getObjectId(), 60, 0));
							player.getClient().getSession().writeAndFlush(
									(Object) CField.bonusAttackRequest(64121055, mobList, false, 0, new int[0]));
							player.setSkillCustomInfo(64121055, 0L, 100L);
						}
						if ((attack.skill == 64121002 || attack.skill == 64121052 || attack.skill == 64121012
								|| attack.skill == 400041036) && player.getSkillCustomValue(400441774) != null
								&& player.getSkillCustomTime(400441774) > 0) {
							int cooltime;
							SecondaryStatEffect effect2 = SkillFactory.getSkill(400041074)
									.getEffect(player.getSkillLevel(400041074));
							int n = cooltime = attack.skill == 400041036 ? effect2.getZ() * 1000 : effect2.getSubTime();
							if (player.getSkillCustomTime(400441774) - cooltime <= 0) {
								player.setSkillCustomInfo(400441774, 0L, 5L);
							} else {
								player.setSkillCustomInfo(400441774, 0L,
										player.getSkillCustomTime(400441774) - cooltime);
							}
						}
					}
					if (GameConstants.isFusionSkill(attack.skill) && attack.targets > 0
							&& player.getSkillCustomValue(22170070) == null) {
						SecondaryStatEffect magicWreck = player.getSkillLevel(22170070) > 0
								? SkillFactory.getSkill(22170070).getEffect(player.getSkillLevel(22170070))
								: SkillFactory.getSkill(22141017).getEffect(player.getSkillLevel(22141017));
						if (player.getMap().getWrecks().size() < 15) {
							int x = Randomizer.rand(-100, 150);
							int y = Randomizer.rand(-50, 70);
							MapleMagicWreck mw = new MapleMagicWreck(player, magicWreck.getSourceId(),
									new Point(monster.getTruePosition().x + x, monster.getTruePosition().y + y), 20000);
							player.getMap().spawnMagicWreck(mw);
							player.setSkillCustomInfo(22170070, 0L, player.getSkillLevel(22170070) > 0 ? 400L : 600L);
						}
					}
					if (!(player.getBuffedValue(15121054) || attack.skill != 15111022 && attack.skill != 15120003
							|| player.lightning <= 2
							|| player.getBuffedEffect(SecondaryStat.CygnusElementSkill, 15001022) == null)) {
						SecondaryStatEffect lightning = SkillFactory.getSkill(attack.skill)
								.getEffect(attack.skilllevel);
						lightning.applyTo(player, false);
						player.cancelEffectFromBuffStat(SecondaryStat.IgnoreTargetDEF, 15001022);
						player.cancelEffectFromBuffStat(SecondaryStat.IndiePmdR, 15001022);
					}
					if (player.getSkillLevel(3110001) > 0 && attack.skill != 95001000 && attack.skill != 3100010
							&& attack.skill != 400031029 && !GameConstants.is_forceAtom_attack_skill(attack.skill)) {
						SkillFactory.getSkill(3110001).getEffect(player.getSkillLevel(3110001)).applyTo(player, false,
								false);
					}
					if (player.getSkillLevel(3210001) > 0 && attack.skill != 95001000 && attack.skill != 3100010
							&& attack.skill != 400031029) {
						SkillFactory.getSkill(3210001).getEffect(player.getSkillLevel(3210001)).applyTo(player, false,
								false);
					}
					if (player.getSkillLevel(3110012) > 0 && attack.skill != 95001000 && attack.skill != 3100010
							&& attack.skill != 400031029) {
						SecondaryStatEffect concentration = SkillFactory.getSkill(3110012)
								.getEffect(player.getSkillLevel(3110012));
						if (System.currentTimeMillis() - player.lastConcentrationTime >= (long) concentration.getY()) {
							player.lastConcentrationTime = System.currentTimeMillis();
							if (player.getConcentration() < 100) {
								player.setConcentration((byte) (player.getConcentration() + concentration.getX()));
							}
							if (player.getBuffedValue(3110012)
									&& player.getConcentration() < player.getSkillLevel(3110012)
									|| !player.getBuffedValue(3110012)) {
								concentration.applyTo(player, false, false);
							}
						}
					}
					if (player.getJob() == 112) {
						if ((attack.skill == 1101011 || attack.skill == 1111010 || attack.skill == 1121008
								|| attack.skill == 1121015) && attack.targets > 0 && player.getBuffedValue(1121054)
								&& player.발할라검격 >= 1) {
							player.발할라검격--;

							MapleSummon summon4 = new MapleSummon(player, 1121055, monster.getPosition(),
									SummonMovementType.STATIONARY, (byte) 0, 30000);
							player.getMap().spawnSummon(summon4, 30000);
							player.addSummon(summon4);

							SecondaryStatEffect eff = SkillFactory.getSkill(1121054)
									.getEffect(player.getSkillLevel(1121054));
							HashMap<SecondaryStat, Pair<Integer, Integer>> statups3 = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
							statups3.put(SecondaryStat.Stance,
									new Pair<Integer, Integer>(100, (int) player.skillcool(1121054) - 90000));
							player.getClient().getSession()
									.writeAndFlush(CWvsContext.BuffPacket.giveBuff(statups3, eff, player));
						}
					}

					if (attack.skill == 3301008 && attack.targets > 0) {
						MapleAtom atom = new MapleAtom(true, player.getId(), 58, true, 3301009,
								player.getTruePosition().x, player.getTruePosition().y);
						atom.setDwUserOwner(player.getId());
						ArrayList<Integer> monsters = new ArrayList<Integer>();
						monsters.add(0);
						monsters.add(0);
						atom.addForceAtom(new ForceAtom(1, Randomizer.rand(21, 25), Randomizer.rand(2, 4),
								Randomizer.rand(17, 21), 120, player.getTruePosition()));
						atom.addForceAtom(new ForceAtom(1, Randomizer.rand(21, 25), Randomizer.rand(2, 4),
								Randomizer.rand(17, 21), 120, player.getTruePosition()));
						atom.setSearchX1(650);
						atom.setSearchY1(250);
						atom.setnDuration(2);
						atom.setSearchX(560);
						atom.setSearchY(2);
						atom.setDwTargets(monsters);
						player.getMap().spawnMapleAtom(atom);
					}
					if (attack.skill == 3321036 && attack.targets > 0 && Randomizer.isSuccess(30)) {
						MapleAtom atom = new MapleAtom(true, player.getId(), 58, true, 3321037,
								player.getTruePosition().x, player.getTruePosition().y);
						atom.setDwUserOwner(player.getId());
						ArrayList<Integer> monsters = new ArrayList<Integer>();
						monsters.add(0);
						monsters.add(0);
						atom.addForceAtom(new ForceAtom(3, Randomizer.rand(21, 25), Randomizer.rand(2, 4),
								Randomizer.rand(17, 21), 120,
								new Point(monster.getTruePosition().x + Randomizer.rand(-150, 150),
										monster.getTruePosition().y + Randomizer.rand(-200, 100))));
						atom.addForceAtom(new ForceAtom(3, Randomizer.rand(21, 25), Randomizer.rand(2, 4),
								Randomizer.rand(17, 21), 120,
								new Point(monster.getTruePosition().x + Randomizer.rand(-150, 150),
										monster.getTruePosition().y + Randomizer.rand(-200, 100))));
						atom.setDwTargets(monsters);
						atom.setSearchX1(650);
						atom.setSearchY1(250);
						atom.setnDuration(2);
						atom.setSearchX(560);
						atom.setSearchY(2);
						player.getMap().spawnMapleAtom(atom);
					}
					if (GameConstants.isKaiser(player.getJob()) && attack.skill != 61120018 && attack.skill != 0
							&& !player.getBuffedValue(61111008) && !player.getBuffedValue(61120008)
							&& !player.getBuffedValue(61121053)) {
						player.handleKaiserCombo(attack.skill);
					}
					if (totDamageToOneMonster <= 0L && attack.skill != 1221011 && attack.skill != 21120006
							&& attack.skill != 164001001) {
						continue;
					}
					// 에르다 샤워 쿨타임 감소
					if (attack.skill == 400001036 && attack.targets > 0) {
						player.setSkillCustomValue(400001036, attack.targets);
					}
					if (GameConstants.isDemonSlayer(player.getJob()) && attack.skill != 31101002) {
						if (attack.skill != 31101002 && player.getSkillLevel(30010111) > 0) {
							if (Randomizer.isSuccess(1)) {
								totDamageToOneMonster *= 2L;
								player.addHP((long) ((double) player.getStat().getCurrentMaxHp() * 0.05));
							}
							if (monster.getHp() <= totDamageToOneMonster) {
								player.handleForceGain(monster.getObjectId(), 30010111);
							}
						}
						player.handleForceGain(monster.getObjectId(), attack.skill,
								monster.getStats().isBoss() ? 1 : 0);
					}
					if (GameConstants.isPhantom(player.getJob())
							&& (player.getSkillLevel(24120002) > 0 || player.getSkillLevel(24100003) > 0)) {
						Skill noir = SkillFactory.getSkill(24120002);
						Skill blanc = SkillFactory.getSkill(24100003);
						SecondaryStatEffect ceffect = null;
						int advSkillLevel = player.getTotalSkillLevel(noir);
						boolean active = true;
						if (advSkillLevel > 0) {
							ceffect = noir.getEffect(advSkillLevel);
						} else if (player.getSkillLevel(blanc) > 0) {
							ceffect = blanc.getEffect(player.getTotalSkillLevel(blanc));
						} else {
							active = false;
						}
						if (attack.skill == 24120055 || attack.skill == noir.getId() || attack.skill == blanc.getId()
								|| attack.skill == 24121011) {
							active = false;
						}
						if (attack.skill == 400041010) {
							active = true;
						}
						if (active) {
							if (player.getCardStack() < (advSkillLevel > 0 ? (byte) 40 : 20)) {
								player.setCardStack((byte) (player.getCardStack() + 1));
								player.getClient().getSession()
										.writeAndFlush((Object) CField.updateCardStack(false, player.getCardStack()));
							}
							MapleAtom atom = new MapleAtom(false, player.getId(), 1, true,
									advSkillLevel > 0 ? 24120002 : 24100003, player.getTruePosition().x,
									player.getTruePosition().y);
							atom.setDwFirstTargetId(monster.getObjectId());
							atom.addForceAtom(new ForceAtom(2, Randomizer.rand(15, 29), Randomizer.rand(7, 11),
									Randomizer.rand(0, 9), 0));
							player.getMap().spawnMapleAtom(atom);
						}
						if (advSkillLevel > 0 && SkillFactory.getSkill(24121011).getSkillList().contains(attack.skill)
								&& player.getSkillCustomValue(24121011) == null) {
							SecondaryStatEffect eff = SkillFactory.getSkill(24121011)
									.getEffect(player.getSkillLevel(24120002));
							ArrayList<Integer> objid = new ArrayList<Integer>();
							ArrayList<Integer> attackk = new ArrayList<Integer>();
							int i = 0;
							for (AttackPair oned1 : attack.allDamage) {
								objid.add(oned1.objectId);
							}
							for (Object mob : player.getMap().getAllMonster()) {
								boolean attacked = true;
								for (Object a2 : objid) {
									if (((Integer) a2).intValue() != ((MapleMapObject) mob).getObjectId()) {
										continue;
									}
									attacked = false;
									break;
								}
								if (!attacked || !eff
										.calculateBoundingBox(player.getPosition(),
												(attack.facingleft >>> 4 & 0xF) == 0)
										.contains(((MapleMapObject) mob).getPosition())) {
									continue;
								}
								attackk.add(((MapleMapObject) mob).getObjectId());
								if (++i < ceffect.getY()) {
									continue;
								}
								break;
							}
							if (!attackk.isEmpty()) {
								final MapleAtom atom6 = new MapleAtom(false, player.getId(), 73, true, 24121011,
										player.getTruePosition().x, player.getTruePosition().y);
								atom6.setDwTargets(attackk);
								for (final Integer objectId : attackk) {
									atom6.addForceAtom(new ForceAtom(1, Randomizer.rand(15, 21), Randomizer.rand(7, 11),
											Randomizer.rand(0, 9), 0));
								}
								player.getMap().spawnMapleAtom(atom6);
								player.setSkillCustomInfo(24121011, 0L, ceffect.getW() * 1000);
							}
						}
						if (player.getSkillLevel(400041040) > 0) {
							SecondaryStatEffect eff = SkillFactory.getSkill(400041040)
									.getEffect(player.getSkillLevel(400041040));
							if (SkillFactory.getSkill(400041040).getSkillList().contains(attack.skill)
									|| attack.skill / 10000 != 2400 && attack.skill / 10000 != 2410
											&& attack.skill / 10000 != 2411 && attack.skill / 10000 != 2412
											&& attack.skill < 400000000) {
								if (attack.skill == 24001000 || attack.skill == 24111000) {
									player.setMarkOfPhantomOid(monster.getObjectId());
									eff.applyTo(player, false);
								} else if (attack.skill == 24121000 || attack.skill == 24121005
										|| attack.skill == 400041055 || attack.skill == 400041056) {
									player.setUltimateDriverCount(player.getUltimateDriverCount() + 1);
									if (player.getUltimateDriverCount() >= eff.getY()) {
										player.setMarkOfPhantomOid(monster.getObjectId());
										player.setUltimateDriverCount(0);
										eff.applyTo(player, false);
									}
								} else {
									player.addSkillCustomInfo(400341040, 1L);
									if (player.getSkillCustomValue0(400341040) >= (long) eff.getW()) {
										player.setMarkOfPhantomOid(monster.getObjectId());
										player.removeSkillCustomInfo(400341040);
										eff.applyTo(player, false);
									}
								}
							}
						}
					}
					if (player.getSkillLevel(80002762) > 0) {
						if (player.getBuffedEffect(SecondaryStat.EmpiricalKnowledge) != null
								&& player.empiricalKnowledge != null) {
							if (map.getMonsterByOid(player.empiricalKnowledge.getObjectId()) != null) {
								if (monster.getObjectId() != player.empiricalKnowledge.getObjectId()
										&& monster.getMobMaxHp() > player.empiricalKnowledge.getMobMaxHp()) {
									player.empiricalStack = 0;
									player.empiricalKnowledge = monster;
								}
							} else {
								player.empiricalStack = 0;
								player.empiricalKnowledge = monster;
							}
						} else if (player.empiricalKnowledge != null) {
							if (monster.getMobMaxHp() > player.empiricalKnowledge.getMobMaxHp()) {
								player.empiricalKnowledge = monster;
							}
						} else {
							player.empiricalKnowledge = monster;
						}
					}
					boolean debinrear = false;
					if (player.getSkillLevel(101120207) > 0) {
						SecondaryStatEffect stunMastery = SkillFactory.getSkill(101120207)
								.getEffect(player.getSkillLevel(101120207));
						if (player.getGender() == 0 && stunMastery.makeChanceResult()) {
							debinrear = true;
							player.getClient().getSession()
									.writeAndFlush((Object) CField.EffectPacket.showEffect(player, 0, 101120207, 4, 0,
											0, (byte) (player.isFacingLeft() ? 1 : 0), true, player.getTruePosition(),
											null, null));
							player.getMap().broadcastMessage(player,
									CField.EffectPacket.showEffect(player, 0, 101120207, 4, 0, 0,
											(byte) (player.isFacingLeft() ? 1 : 0), false, player.getTruePosition(),
											null, null),
									false);
							player.addHP(player.getStat().getCurrentMaxHp() / 100L * (long) stunMastery.getX());
						}
					}

					monster.damage(player, totDamageToOneMonster, true, attack.skill);
					applyAddAttack(player, monster, attack);

					SecondaryStatEffect markOf = player.getBuffedEffect(SecondaryStat.MarkofNightLord);

					if (player.getBuffedEffect(SecondaryStat.Mark_Of_Assassin) != null) {
						markOf = player.getBuffedEffect(SecondaryStat.Mark_Of_Assassin);
					}

					Item nk = player.getInventory(MapleInventoryType.USE).getItem(attack.slot);
					if (markOf != null && nk != null) {
						if (player.getSkillLevel(4120018) > 0) {
							markOf = SkillFactory.getSkill(4120018).getEffect(player.getSkillLevel(4120018));
						}
						if (player.getSkillLevel(4141002) > 0) {
							markOf = SkillFactory.getSkill(4141003).getEffect(player.getSkillLevel(4141002));
						}
						int bulletCount = markOf.getBulletCount();
						if (attack.skill != 400041038 && attack.skill != 4100012 && attack.skill != 4120019
								&& attack.skill != 4141004) {
							if (monster.isBuffed(markOf.getSourceId())
									|| !monster.isAlive() && Randomizer.isSuccess(markOf.getProp())) {
								if (attack.skill != 400041020 || attack.skill == 400041020
										&& Randomizer.isSuccess(SkillFactory.getSkill(400041020)
												.getEffect(player.getSkillLevel(400041020)).getW())) {
									int i;
									ArrayList<Integer> monsters = new ArrayList<Integer>();
									ArrayList<MapleMonster> mobs = (ArrayList<MapleMonster>) player.getMap()
											.getAllMonster();
									for (i = 0; i < bulletCount; ++i) {
										for (int x = 0; x < mobs.size(); x++) {
											for (int y = 1; y < mobs.size(); y++) {
												if (mobs.get(x).getMobMaxHp() < mobs.get(y).getMobMaxHp()) {
													MapleMonster tempMob = mobs.get(x);
													mobs.set(x, mobs.get(y));
													mobs.set(y, tempMob);
												}
											}
										}
									}
									if (mobs.isEmpty()) {
										for (i = 0; i < bulletCount; ++i) {
											monsters.add(0);
										}
									} else {
										for (i = 0; i < bulletCount; ++i) {
											monsters.add(mobs.get(0).getObjectId());
										}
									} // Unk_726
									MapleAtom atom;
									if (player.getSkillLevel(4141002) > 0) {
										atom = new MapleAtom(true, monster.getObjectId(), 11, true,
												monster.isBuffed(4141003) ? 4141004 : 4100012,
												monster.getTruePosition().x, monster.getTruePosition().y);
									} else {
										atom = new MapleAtom(true, monster.getObjectId(), 11, true,
												monster.isBuffed(4120018) ? 4120019 : 4100012,
												monster.getTruePosition().x, monster.getTruePosition().y);
									}
									atom.setDwUserOwner(player.getId());
									atom.setDwTargets(monsters);
									atom.setnItemId(
											player.getV("csstar") != null ? Integer.parseInt(player.getV("csstar"))
													: nk.getItemId());
									if (monsters.size() > 0) {
										for (Integer objectId : monsters) {
											ForceAtom forceAtom = new ForceAtom(2, Randomizer.rand(41, 44),
													Randomizer.rand(3, 4), Randomizer.rand(67, 292), 200);
											atom.addForceAtom(forceAtom);
										}
										player.getMap().spawnMapleAtom(atom);
									}
									if (monster.isBuffed(markOf.getSourceId())) {
										monster.cancelSingleStatus(monster.getBuff(markOf.getSourceId()),
												markOf.getSourceId());
									}
								}
							} else if (attack.skill != 4111003) {
								if (player.getSkillLevel(4141002) > 0) {
									markOf = SkillFactory.getSkill(4141003).getEffect(1);
									markOf.setDuration(20000);
								} else if (player.getSkillLevel(4120018) > 0) {
									markOf = SkillFactory.getSkill(4120018).getEffect(1);
									markOf.setDuration(20000);
								} else {
									markOf = SkillFactory.getSkill(4100011).getEffect(1);
									markOf.setDuration(20000);
								}
								if (markOf.makeChanceResult() && monster.isAlive()) {
									ArrayList<Pair<MonsterStatus, MonsterStatusEffect>> applys = new ArrayList<Pair<MonsterStatus, MonsterStatusEffect>>();
									applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_Burned,
											new MonsterStatusEffect(markOf.getSourceId(), markOf.getDOTTime(), 0L)));
									monster.applyStatus(player.getClient(), applys, markOf);
								}
							}
						}
					}

					if (monster.getId() >= 9500650 && monster.getId() <= 9500654 && totDamageToOneMonster > 0L
							&& player.getGuild() != null) {
						player.getGuild().updateGuildScore(totDamageToOneMonster);
					}
					if (monster.isBuffed(MonsterStatus.MS_PCounter)
							&& player.getBuffedEffect(SecondaryStat.IgnorePImmune) == null
							&& player.getBuffedEffect(SecondaryStat.IgnorePCounter) == null
							&& player.getBuffedEffect(SecondaryStat.IgnoreAllCounter) == null
							&& player.getBuffedEffect(SecondaryStat.IgnoreAllImmune) == null
							&& !SkillFactory.getSkill(attack.skill).isIgnoreCounter() && !energy) {
						player.addHP(-monster.getBuff(MonsterStatus.MS_PCounter).getValue());
					}
					if (SkillFactory.getSkill(164101003).getSkillList().contains(attack.skill)
							&& player.getBuffedEffect(SecondaryStat.Alterego) != null
							&& System.currentTimeMillis() - player.lastAltergoTime >= 1500L) {
						List<MapleMapObject> objs = player.getMap().getMapObjectsInRange(player.getTruePosition(),
								1000000.0, Arrays.asList(new MapleMapObjectType[] { MapleMapObjectType.MONSTER }));
						player.lastAltergoTime = System.currentTimeMillis();
						MapleAtom atom = new MapleAtom(false, player.getId(), 60, true, 164101004,
								player.getTruePosition().x, player.getTruePosition().y);
						ArrayList<Integer> monsters = new ArrayList<Integer>();
						int fora = player.getBuffedValue(400041048) ? 12 : 3;
						int i = 0;
						for (MapleMapObject o : objs) {
							monsters.add(o.getObjectId());
							if (++i < fora) {
								continue;
							}
							break;
						}
						while (i < fora) {
							monsters.add(monster.getObjectId());
							++i;
						}
						for (Integer m : monsters) {
							atom.addForceAtom(new ForceAtom(player.getBuffedValue(400041048) ? 1 : 0,
									Randomizer.rand(40, 49), 3, Randomizer.rand(45, 327), 0));
						}
						atom.setDwTargets(monsters);
						player.getMap().spawnMapleAtom(atom);
					}
					if (!monster.isAlive()) {
						multikill = (byte) (multikill + 1);
					}
					if (player.getBuffedValue(400001050) && player.getSkillCustomValue0(400001050) == 400001055L) {
						SecondaryStatEffect effect6 = SkillFactory.getSkill(400001050)
								.getEffect(player.getSkillLevel(400001050));
						player.getClient().getSession().writeAndFlush((Object) CField.bonusAttackRequest(400001055,
								new ArrayList<Triple<Integer, Integer, Integer>>(), true, 0, new int[0]));
						player.removeSkillCustomInfo(400001050);
						long duration = player.getBuffLimit(400001050);
						effect6.applyTo(player, false, (int) duration);
					}
					if (attack.skill == 164001001) {
						MapleAtom atom = new MapleAtom(true, monster.getObjectId(), 63, true, 164001001,
								monster.getTruePosition().x, monster.getTruePosition().y);
						atom.setDwUserOwner(player.getId());
						atom.setDwFirstTargetId(player.getId());
						atom.setDwTargetId(monster.getObjectId());
						atom.addForceAtom(new ForceAtom(1, 5, 30, 0, 0));
						player.getMap().spawnMapleAtom(atom);
					}
					if (attack.skill == 164001002 && monster != null && monster.getBuff(164001001) != null) {
						monster.cancelSingleStatus(monster.getBuff(164001001));
					}
					if (player.getBuffedEffect(SecondaryStat.ButterflyDream) != null
							&& System.currentTimeMillis() - player.lastButterflyTime >= (long) (player
									.getBuffedEffect(SecondaryStat.ButterflyDream).getX() * 1000)) {
						player.lastButterflyTime = System.currentTimeMillis();
						MapleAtom atom = new MapleAtom(false, player.getObjectId(), 63, true, 164001001,
								player.getTruePosition().x, player.getTruePosition().y);
						atom.setDwFirstTargetId(0);
						atom.addForceAtom(new ForceAtom(1, 42, 3, 136, 0));
						player.getMap().spawnMapleAtom(atom);
					}
					if (attack.skill == 400011047 && player.getBuffedValue(400011047)) {
						player.getMap().broadcastMessage(
								MobPacket.skillAttackEffect(monster.getObjectId(), attack.skill, player.getId()));
						player.setGraveTarget(player.getObjectId());
						player.createSecondAtom(SkillFactory.getSkill(400011047).getSecondAtoms(),
								monster.getPosition());
					}

					if (effect != null && monster.isAlive()) {
						SecondaryStatEffect eff;
						Iterator stunMastery;
						ArrayList<Triple<MonsterStatus, MonsterStatusEffect, Long>> statusz = new ArrayList<Triple<MonsterStatus, MonsterStatusEffect, Long>>();
						ArrayList<Triple<MonsterStatus, MonsterStatusEffect, Long>> statusz1 = new ArrayList<Triple<MonsterStatus, MonsterStatusEffect, Long>>();
						ArrayList<Triple<MonsterStatus, MonsterStatusEffect, Long>> statusz2 = new ArrayList<Triple<MonsterStatus, MonsterStatusEffect, Long>>();
						ArrayList statusz3 = new ArrayList();
						ArrayList<Pair<MonsterStatus, MonsterStatusEffect>> applys = new ArrayList<Pair<MonsterStatus, MonsterStatusEffect>>();
						ArrayList<Pair<MonsterStatus, MonsterStatusEffect>> applys1 = new ArrayList<Pair<MonsterStatus, MonsterStatusEffect>>();
						ArrayList<Pair<MonsterStatus, MonsterStatusEffect>> applys2 = new ArrayList<Pair<MonsterStatus, MonsterStatusEffect>>();
						switch (attack.skill) {
						case 1101012: {
							if (monster.getStats().isBoss()) {
								break;
							}

							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun,
									new MonsterStatusEffect(attack.skill,
											effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
									1L));
							break;
						}
						case 1121015: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Incizing,
									new MonsterStatusEffect(attack.skill,
											effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
									1L));
							break;
						}
						case 1201011: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Burned,
									new MonsterStatusEffect(attack.skill, effect.getDOTTime()), (long) effect.getDOT()
											* totDamageToOneMonster / (long) attack.allDamage.size() / 10000L));
							break;
						}
						case 1201012: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Speed,
									new MonsterStatusEffect(attack.skill,
											effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
									Long.valueOf(effect.getX())));
							break;
						}
						case 1201013: {
							if (!monster.getStats().isBoss() && Randomizer.isSuccess(80)) {
								statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun,
										new MonsterStatusEffect(attack.skill,
												effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
										1L));
							}

							break;
						}
						case 1201015: {
							if (!monster.getStats().isBoss() && Randomizer.isSuccess(90)) {
								statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun,
										new MonsterStatusEffect(attack.skill,
												effect.getSubTime() > 0 ? effect.getSubTime() : 6000),
										1L));
							}

							break;
						}
						case 1211008: {
							if (monster.getStats().isBoss()) {
								break;
							}

							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun,
									new MonsterStatusEffect(attack.skill,
											effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
									1L));
							break;
						}
						case 1221004: {
							if (monster.getStats().isBoss()) {
								break;
							}

							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Seal,
									new MonsterStatusEffect(attack.skill,
											effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
									Long.valueOf(effect.getDuration())));
							break;
						}
						case 1221019:
						case 1221021:
						case 1241004: {
							if (monster.getStats().isBoss()) {
								break;
							}

							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Blind,
									new MonsterStatusEffect(attack.skill,
											effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
									Long.valueOf(effect.getDuration())));
							break;
						}
						case 51141000: // 샤이닝 크로스 VI 구현
						case 51141001: { // 샤이닝 크로스-어썰트 VI 구현
							if (monster.getStats().isBoss()) {
								break;
							}

							if (Randomizer.isSuccess(30)) {
								statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Blind,
										new MonsterStatusEffect(attack.skill, 10000), 10000L));
							}

							break;
						}
						case 51141500:
						case 51141501:
						case 51141502: { // 오리진 듀란달 구현
							if (monster.getStats().isBoss()) {
								break;
							}

							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Blind,
									new MonsterStatusEffect(attack.skill, 10000), 10000L));
							break;
						}
						case 1301012: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun,
									new MonsterStatusEffect(attack.skill, 1000), 1L));
							break;
						}
						case 2301010: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_IndieMdr,
									new MonsterStatusEffect(attack.skill, 60000), (long) effect.getX()));
							break;
						}
						case 2201004:
						case 2201008:
						case 2201009:
						case 2211002:
						case 2211006:
						case 2211010:
						case 2220014:
						case 2221003:
						case 2221011:
						case 2221012:
						case 2221054:
						case 400020002: {
							if (attack.skill != 2221011) {
								statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Speed,
										new MonsterStatusEffect(attack.skill, effect.getDuration()),
										Long.valueOf(effect.getV())));
								if (monster.getBuff(MonsterStatus.MS_Speed) == null
										&& monster.getFreezingOverlap() > 0) {
									monster.setFreezingOverlap(0);
								}
								if (monster.getFreezingOverlap() < 5) {
									monster.setFreezingOverlap((byte) (monster.getFreezingOverlap() + 1));
								}
							}
							if (attack.skill != 2221011) {
								statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(
										MonsterStatus.MS_Freeze, new MonsterStatusEffect(attack.skill, 13000), 1L));
							}
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_IndiePdr,
									new MonsterStatusEffect(attack.skill, 13000), Long.valueOf(effect.getX())));
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_IndieMdr,
									new MonsterStatusEffect(attack.skill, 13000), Long.valueOf(effect.getY())));
							break;
						}
						case 2201005:
						case 2211003:
						case 2211011:
						case 2221006: {
							if (monster.getBuff(MonsterStatus.MS_Speed) == null && monster.getFreezingOverlap() > 0) {
								monster.setFreezingOverlap(0);
							}
							if (monster.getFreezingOverlap() > 0) {
								monster.setFreezingOverlap((byte) (monster.getFreezingOverlap() - 1));
								if (monster.getFreezingOverlap() <= 0) {
									monster.cancelStatus(MonsterStatus.MS_Speed, monster.getBuff(2201008));
								} else {
									statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(
											MonsterStatus.MS_Speed, new MonsterStatusEffect(2201008, 8000), -75L));
								}
							}
							if (attack.skill != 2221006) {
								break;
							}
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun,
									new MonsterStatusEffect(attack.skill, effect.getDuration()), 1L));
							break;
						}
						case 2221052:
						case 400021031:
						case 400021094: {
							if (monster.getBuff(MonsterStatus.MS_Speed) == null && monster.getFreezingOverlap() > 0) {
								monster.setFreezingOverlap(0);
							}
							if (attack.skill == 400021094) {
								monster.addSkillCustomInfo(400021094, 1L);
								if (monster.getFreezingOverlap() > 0) {
									if (attack.skill == 400021094 && monster.getCustomValue0(400021094) >= 5L) {
										monster.removeCustomInfo(400021094);
										monster.setFreezingOverlap((byte) (monster.getFreezingOverlap() - 1));
										statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(
												MonsterStatus.MS_Speed, new MonsterStatusEffect(2201008, 8000), -75L));
									}
								} else {
									monster.cancelStatus(MonsterStatus.MS_Speed, monster.getBuff(2201008));
								}
							}
							if (monster.getFreezingOverlap() > 0) {
								monster.setFreezingOverlap((byte) (monster.getFreezingOverlap() - 1));
								statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Speed,
										new MonsterStatusEffect(2201008, 8000), -75L));
								break;
							}
							if (monster.getFreezingOverlap() > 0) {
								break;
							}
							monster.cancelStatus(MonsterStatus.MS_Speed, monster.getBuff(2201008));
							break;
						}
						case 2111007: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun,
									new MonsterStatusEffect(attack.skill, effect.getDOTTime()), 1L));
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Burned,
									new MonsterStatusEffect(attack.skill, effect.getDOTTime()), (long) effect.getDOT()
											* totDamageToOneMonster / (long) attack.allDamage.size() / 10000L));
							break;
						}
						case 2121052:
						case 2121054:
						case 2121005:
						case 2121055: { // 불독 도트뎀
							player.setDotDamage((long) effect.getDOT() * totDamageToOneMonster
									/ (long) attack.allDamage.size() / 10000L);
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Showdown,
									new MonsterStatusEffect(attack.skill, effect.getDuration()), 1L));
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Speed,
									new MonsterStatusEffect(attack.skill, effect.getDuration()),
									Long.valueOf(effect.getX())));
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Burned,
									new MonsterStatusEffect(attack.skill, effect.getDOTTime()), player.getDotDamage()));
							player.setFlameHeiz(monster.getTruePosition());
							boolean heiz = true;
							break;
						}
						case 131001213: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Burned,
									new MonsterStatusEffect(attack.skill,
											SkillFactory.getSkill(131001013).getEffect(1).getV2() * 1000),
									(long) SkillFactory.getSkill(131001013).getEffect(1).getW() * totDamageToOneMonster
											/ (long) attack.allDamage.size() / 10000L));
							break;
						}
						case 131001313: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Blind,
									new MonsterStatusEffect(attack.skill,
											SkillFactory.getSkill(131001013).getEffect(1).getPsdJump() * 1000),
									Long.valueOf(SkillFactory.getSkill(131001013).getEffect(1).getY())));
							break;
						}
						case 2211007:
						case 2311007: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun,
									new MonsterStatusEffect(attack.skill, effect.getDuration()), 1L));
							break;
						}
						case 3101005: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun,
									new MonsterStatusEffect(attack.skill,
											effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
									1L));
							break;
						}
						case 3111003: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Burned,
									new MonsterStatusEffect(attack.skill, effect.getDOTTime()), (long) effect.getDOT()
											* totDamageToOneMonster / (long) attack.allDamage.size() / 10000L));
							break;
						}
						case 3121014: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(
									MonsterStatus.MS_DebuffHealing,
									new MonsterStatusEffect(attack.skill,
											effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
									Long.valueOf(effect.getX())));
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Speed,
									new MonsterStatusEffect(attack.skill,
											effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
									Long.valueOf(effect.getW())));
							break;
						}
						// 작업 - 보마 튕김
						case 3121052: {
							// statusz.add(new Triple<MonsterStatus, MonsterStatusEffect,
							// Long>(MonsterStatus.MS_sl, new MonsterStatusEffect(attack.skill,
							// effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
							// Long.valueOf(effect.getS())));
							break;
						}
						case 3201008: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Speed,
									new MonsterStatusEffect(attack.skill,
											effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
									Long.valueOf(effect.getZ())));
							break;
						}
						case 4111003: {
							if (monster.getStats().isBoss()) {
								break;
							}
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Burned,
									new MonsterStatusEffect(attack.skill, effect.getDuration()), (long) effect.getDOT()
											* totDamageToOneMonster / (long) attack.allDamage.size() / 10000L));
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Web,
									new MonsterStatusEffect(attack.skill,
											effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
									1L));
							break;
						}
						case 4121016: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Burned,
									new MonsterStatusEffect(attack.skill, effect.getDOTTime()), (long) effect.getDOT()
											* totDamageToOneMonster / (long) attack.allDamage.size() / 10000L));
							break;
						}
						case 4121017: {
							int data = effect.getX();
							if (player.getSkillLevel(0x3EDDED) > 0) {
								data += SkillFactory.getSkill(0x3EDDED).getEffect(1).getX();
							}
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Showdown,
									new MonsterStatusEffect(attack.skill,
											effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
									Long.valueOf(data)));
							break;
						}
						case 4201004: {
							if (monster.getStats().isBoss()) {
								break;
							}
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun,
									new MonsterStatusEffect(attack.skill,
											effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
									1L));
							break;
						}
						case 4221010: {
							statusz1.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Burned,
									new MonsterStatusEffect(attack.skill, effect.getDOTTime()), (long) effect.getDOT()
											* totDamageToOneMonster / (long) attack.allDamage.size() / 10000L));
							break;
						}
						case 4321002: {
							statusz2.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(
									MonsterStatus.MS_AdddamParty,
									new MonsterStatusEffect(attack.skill,
											effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
									10L));
							break;
						}
						case 4321004: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(
									MonsterStatus.MS_RiseByToss,
									new MonsterStatusEffect(attack.skill,
											effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
									100L));
							break;
						}
						case 4331006: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun,
									new MonsterStatusEffect(attack.skill,
											effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
									1L));
							break;
						}
						case 4341011: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Burned,
									new MonsterStatusEffect(attack.skill, effect.getDOTTime()), (long) effect.getDOT()
											* totDamageToOneMonster / (long) attack.allDamage.size() / 10000L));
							break;
						}
						case 5011002: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Speed,
									new MonsterStatusEffect(attack.skill,
											effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
									Long.valueOf(effect.getZ())));
							break;
						}
						case 37110002: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(
									MonsterStatus.MS_BlessterDamage,
									new MonsterStatusEffect(37110002,
											SkillFactory.getSkill(37110001)
													.getEffect(GameConstants.getLinkedSkill(37110002)).getDuration()
													* 2),
									Long.valueOf(SkillFactory.getSkill(37110001)
											.getEffect(GameConstants.getLinkedSkill(37110002)).getX())));
							break;
						}
						case 5121001: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(
									MonsterStatus.MS_DragonStrike,
									new MonsterStatusEffect(attack.skill,
											effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
									Long.valueOf(effect.getX())));
							break;
						}
						case 5111002: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun,
									new MonsterStatusEffect(attack.skill,
											effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
									1L));
							break;
						}
						case 5311002: {
							if (attack.charge != 1000) {
								break;
							}
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun,
									new MonsterStatusEffect(attack.skill,
											effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
									1L));
							break;
						}
						case 5310011: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Burned,
									new MonsterStatusEffect(attack.skill, effect.getDOTTime()), (long) effect.getDOT()
											* totDamageToOneMonster / (long) attack.allDamage.size() / 10000L));
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(
									MonsterStatus.MS_AdddamSkill,
									new MonsterStatusEffect(attack.skill,
											effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
									Long.valueOf(effect.getZ())));
							break;
						}
						case 5311010: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Burned,
									new MonsterStatusEffect(attack.skill, effect.getDOTTime()), (long) effect.getDOT()
											* totDamageToOneMonster / (long) attack.allDamage.size() / 10000L));
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Puriaus,
									new MonsterStatusEffect(attack.skill, effect.getDOTTime()),
									Long.valueOf(effect.getZ())));
							break;
						}
						case 13121052:
						case 400031022: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Burned,
									new MonsterStatusEffect(attack.skill, effect.getDOTTime()), (long) effect.getDOT()
											* totDamageToOneMonster / (long) attack.allDamage.size() / 10000L));
							break;
						}
						case 21100002: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun,
									new MonsterStatusEffect(attack.skill,
											effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
									1L));
							break;
						}
						case 21100013: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun,
									new MonsterStatusEffect(attack.skill,
											effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
									1L));
							break;
						}
						case 21101016: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(
									MonsterStatus.MS_RiseByToss,
									new MonsterStatusEffect(attack.skill,
											effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
									100L));
							break;
						}
						case 21110011:
						case 21110024:
						case 21110025:
						case 21111017: {
							if (monster.getStats().isBoss()) {
								break;
							}
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Freeze,
									new MonsterStatusEffect(attack.skill,
											effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
									1L));
							break;
						}
						case 23111002: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Puriaus,
									new MonsterStatusEffect(attack.skill,
											effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
									Long.valueOf(effect.getX())));
							break;
						}
						case 23121002: {
							int minus = effect.getY();
							if (player.getSkillLevel(23120050) > 0) {
								SecondaryStatEffect subeffect = SkillFactory.getSkill(23120050).getEffect(1);
								minus -= subeffect.getX();
							}
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_IndiePdr,
									new MonsterStatusEffect(attack.skill,
											effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
									Long.valueOf(-minus)));
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_IndieMdr,
									new MonsterStatusEffect(attack.skill,
											effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
									Long.valueOf(-minus)));
							break;
						}
						case 23141005: // 레전드리 스피어 VI 구현
						case 23141006: { // 레전드리 스피어 VI : 스피릿 인챈트 구현
							if (monster.isBuffed(23141005) || monster.isBuffed(23141006)) {
								break;
							}

							int minus = effect.getY();
							if (player.getSkillLevel(23120050) > 0) {
								SecondaryStatEffect subeffect = SkillFactory.getSkill(23120050).getEffect(1);
								minus -= subeffect.getX();
							}
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_IndiePdr,
									new MonsterStatusEffect(attack.skill,
											effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
									Long.valueOf(-minus)));
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_IndieMdr,
									new MonsterStatusEffect(attack.skill,
											effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
									Long.valueOf(-minus)));
							break;
						}
						case 23121003: {
							statusz.add(
									new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_AdddamSkill2,
											new MonsterStatusEffect(23121000, effect.getDuration()),
											Long.valueOf(effect.getX())));
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(
									MonsterStatus.MS_DodgeBodyAttack,
									new MonsterStatusEffect(attack.skill, effect.getDuration()), 1L));
							break;
						}
						case 23120013: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun,
									new MonsterStatusEffect(attack.skill,
											effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
									1L));
							break;
						}
						case 25100011:
						case 25101003:
						case 25101004:
						case 25111004: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(
									MonsterStatus.MS_BahamutLightElemAddDam,
									new MonsterStatusEffect(25100011,
											effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
									Long.valueOf(effect.getX())));
							break;
						}
						case 25111206: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Freeze,
									new MonsterStatusEffect(attack.skill, effect.getDuration()), 1L));
							break;
						}
						case 25120003: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun,
									new MonsterStatusEffect(attack.skill, effect.getDuration()), 1L));
							break;
						}
						case 25121006: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Burned,
									new MonsterStatusEffect(attack.skill, effect.getDOTTime()), (long) effect.getDOT()
											* totDamageToOneMonster / (long) attack.allDamage.size() / 10000L));
							break;
						}
						case 25121007: {
							if (monster.getStats().getCategory() == 1 || monster.getId() == 8880502
									|| monster.getId() == 8880522 || monster.getId() == 8644650
									|| monster.getId() == 8644655 || monster.getId() == 8880342
									|| monster.getId() == 8880302) {
								statusz.add(
										new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_IndieUNK,
												new MonsterStatusEffect(attack.skill, effect.getDuration()),
												Long.valueOf(effect.getLevel())));
								break;
							}
							if (monster.getBuff(MonsterStatus.MS_SeperateSoulC) != null
									|| monster.getBuff(MonsterStatus.MS_SeperateSoulP) != null) {
								break;
							}
							statusz.add(
									new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_SeperateSoulP,
											new MonsterStatusEffect(attack.skill, effect.getDuration()),
											Long.valueOf(effect.getLevel())));
							break;
						}
						case 31101002: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun,
									new MonsterStatusEffect(attack.skill, effect.getDuration()), 1L));
							break;
						}
						case 31111001: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun,
									new MonsterStatusEffect(attack.skill,
											effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
									1L));
							break;
						}
						case 31111005: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Burned,
									new MonsterStatusEffect(attack.skill, effect.getDOTTime()), (long) effect.getDOT()
											* totDamageToOneMonster / (long) attack.allDamage.size() / 10000L));
							break;
						}
						case 31121001: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Speed,
									new MonsterStatusEffect(attack.skill,
											effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
									Long.valueOf(effect.getX())));
							break;
						}
						case 31101003: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun,
									new MonsterStatusEffect(attack.skill,
											effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
									1L));
							break;
						}
						case 31121000: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(
									MonsterStatus.MS_RiseByToss,
									new MonsterStatusEffect(attack.skill,
											effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
									100L));
							break;
						}
						case 31121003: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_IndiePdr,
									new MonsterStatusEffect(attack.skill,
											effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
									Long.valueOf(-effect.getX())));
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_IndieMdr,
									new MonsterStatusEffect(attack.skill,
											effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
									Long.valueOf(-effect.getX())));
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Pad,
									new MonsterStatusEffect(attack.skill,
											effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
									Long.valueOf(-effect.getX())));
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Mad,
									new MonsterStatusEffect(attack.skill,
											effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
									Long.valueOf(-effect.getX())));
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Blind,
									new MonsterStatusEffect(attack.skill,
											effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
									Long.valueOf(-effect.getZ())));
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Showdown,
									new MonsterStatusEffect(attack.skill,
											effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
									Long.valueOf(effect.getW())));
							break;
						}
						case 31211011: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun,
									new MonsterStatusEffect(attack.skill,
											effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
									1L));
							break;
						}
						case 31221002: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_IndiePdr,
									new MonsterStatusEffect(attack.skill,
											effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
									Long.valueOf(-effect.getY())));
							break;
						}
						case 33101215: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun,
									new MonsterStatusEffect(attack.skill,
											effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
									1L));
							break;
						}
						case 36001000:
						case 36101000:
						case 36101001:
						case 36101008:
						case 36101009:
						case 36111000:
						case 36111001:
						case 36111002:
						case 36111009:
						case 36111010:
						case 36121000:
						case 36121001:
						case 36121011:
						case 36121012:
						case 400041007: {
							SecondaryStatEffect effect2;
							if (player.getSkillLevel(36110005) <= 0) {
								break;
							}
							if (monster.getBuff(MonsterStatus.MS_Indie_Unk4) == null && monster.getAirFrame() > 0) {
								monster.setAirFrame(0);
							}
							if (!(effect2 = SkillFactory.getSkill(36110005).getEffect(player.getSkillLevel(36110005)))
									.makeChanceResult()) {
								break;
							}
							monster.setAirFrame(monster.getAirFrame() + 1);
							if (monster.getAirFrame() >= 4) {
								monster.setAirFrame(0);
								monster.cancelSingleStatus(monster.getBuff(36110005), 36110005);
								map.broadcastMessage(CField.ignitionBomb(36110005, monster.getObjectId(),
										monster.getTruePosition()));
								break;
							}
							statusz.add(
									new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Indie_Unk4,
											new MonsterStatusEffect(36110005, effect2.getDuration()),
											Long.valueOf(monster.getAirFrame())));
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Eva,
									new MonsterStatusEffect(36110005, effect2.getDuration()),
									(long) (-effect2.getX()) * (long) monster.getAirFrame()));
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Blind,
									new MonsterStatusEffect(36110005, effect2.getDuration()),
									(long) (-effect2.getX()) * (long) monster.getAirFrame()));
							break;
						}
						case 51120057:
						case 51121007:
						case 51121009: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Blind,
									new MonsterStatusEffect(attack.skill, attack.skill == 51120057 ? 10000
											: (effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration())),
									Long.valueOf(-effect.getX())));
							break;
						}
						case 51121052: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(
									MonsterStatus.MS_DeadlyCharge,
									new MonsterStatusEffect(attack.skill,
											effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
									Long.valueOf(effect.getX())));
							break;
						}
						case 61101101:
						case 61111217: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun,
									new MonsterStatusEffect(attack.skill,
											effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
									1L));
							break;
						}
						case 61111100:
						case 61111113:
						case 61111218: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Speed,
									new MonsterStatusEffect(attack.skill,
											effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
									Long.valueOf(effect.getZ())));
							break;
						}
						case 61111101:
						case 61111219: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun,
									new MonsterStatusEffect(attack.skill,
											effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
									1L));
							break;
						}
						case 64001000: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Speed,
									new MonsterStatusEffect(attack.skill, effect.getS2()),
									Long.valueOf(-effect.getX())));
							break;
						}
						case 64001001: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Speed,
									new MonsterStatusEffect(attack.skill,
											effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
									Long.valueOf(-effect.getX())));
							break;
						}
						case 64001009:
						case 64001010:
						case 64001011: {
							SecondaryStatEffect Eff = SkillFactory.getSkill(64120000)
									.getEffect(player.getSkillLevel(attack.skill));
							statusz.add(
									new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Speed,
											new MonsterStatusEffect(Eff.getSourceId(),
													Eff.getSubTime() > 0 ? Eff.getSubTime() : Eff.getDuration()),
											-10L));
							break;
						}
						case 64111003: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_IndiePdr,
									new MonsterStatusEffect(attack.skill,
											effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
									Long.valueOf(effect.getW())));
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_IndieMdr,
									new MonsterStatusEffect(attack.skill,
											effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
									Long.valueOf(effect.getW())));
							break;
						}
						case 64121011: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Pad,
									new MonsterStatusEffect(attack.skill,
											effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
									Long.valueOf(-effect.getU())));
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Mad,
									new MonsterStatusEffect(attack.skill,
											effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
									Long.valueOf(-effect.getU())));
							break;
						}
						case 65101100: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Explosion,
									new MonsterStatusEffect(attack.skill,
											effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
									1L));
							break;
						}
						case 164121005: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Burned,
									new MonsterStatusEffect(attack.skill, effect.getDOTTime()), (long) effect.getDOT()
											* totDamageToOneMonster / (long) attack.allDamage.size() / 10000L));
							break;
						}
						case 164111008: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_IndiePdr,
									new MonsterStatusEffect(attack.skill, effect.getDuration()),
									Long.valueOf(-effect.getY())));
							if (monster.getStats().isBoss()) {
								break;
							}
							statusz.add(
									new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_RWLiftPress,
											new MonsterStatusEffect(attack.skill, effect.getDuration()),
											Long.valueOf(2400500 + Randomizer.rand(0, 2))));
							break;
						}
						case 65121002: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(
									MonsterStatus.MS_AdddamParty,
									new MonsterStatusEffect(attack.skill,
											effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
									Long.valueOf(effect.getY())));
							break;
						}
						case 37121004: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Freeze,
									new MonsterStatusEffect(attack.skill,
											effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
									1L));
							break;
						}
						case 65121100: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun,
									new MonsterStatusEffect(attack.skill,
											effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration()),
									1L));
							break;
						}
						case 100001283: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Freeze,
									new MonsterStatusEffect(attack.skill, effect.getDuration()), 1L));
							break;
						}
						case 151111002: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_TimeBomb,
									new MonsterStatusEffect(attack.skill, effect.getDuration()),
									Long.valueOf(effect.getX())));
							break;
						}
						case 164001001: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(
									MonsterStatus.MS_RWChoppingHammer,
									new MonsterStatusEffect(attack.skill, effect.getDuration()), 1L));
							break;
						}
						case 61121100:
						case 400011079:
						case 400011080:
						case 400011081:
						case 400011082: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Speed,
									new MonsterStatusEffect(61121100, 10000), -50L));
							break;
						}
						case 135001012: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_IndieUNK,
									new MonsterStatusEffect(attack.skill, effect.getDuration()), 1L));
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Speed,
									new MonsterStatusEffect(attack.skill, effect.getDuration()), -50L));
							break;
						}
						case 164121044: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Freeze,
									new MonsterStatusEffect(attack.skill, 11000), 1L));
							break;
						}
						case 400021001: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Burned,
									new MonsterStatusEffect(attack.skill, effect.getDOTTime()), (long) effect.getDOT()
											* totDamageToOneMonster / (long) attack.allDamage.size() / 10000L));
							if (player.getBuffedEffect(SecondaryStat.WizardIgnite) == null
									|| !player.getBuffedEffect(SecondaryStat.WizardIgnite).makeChanceResult()) {
								break;
							}
							SkillFactory.getSkill(2100010).getEffect(player.getSkillLevel(2101010)).applyTo(player,
									monster.getTruePosition());
							break;
						}
						case 24121010: {
							SecondaryStatEffect ef = SkillFactory.getSkill(24121003)
									.getEffect(GameConstants.getLinkedSkill(24121003));
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_IndiePdr,
									new MonsterStatusEffect(attack.skill,
											ef.getSubTime() > 0 ? ef.getSubTime() : ef.getDuration()),
									Long.valueOf(-ef.getY())));
							break;
						}
						case 63121006:
						case 63121007:
						case 63141005: { // [처형] 포이즌 니들 VI 구현
							SecondaryStatEffect effect2 = SkillFactory.getSkill(63121006)
									.getEffect(player.getSkillLevel(63121006));
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Burned,
									new MonsterStatusEffect(effect2.getSourceId(), effect2.getDOTTime()),
									(long) effect2.getDOT() * totDamageToOneMonster / (long) attack.allDamage.size()
											/ 10000L));
							break;
						}
						case 1221052:
						case 11121004:
						case 11121013:
						case 14121004:
						case 31121006:
						case 31221003:
						case 36121053:
						case 64121001:
						case 151121040:
						case 155121007:
						case 155121306:
						case 400001008:
						case 400011121: {
							int du = 0; // du , n?
							if (attack.skill == 400011121) {
								monster.setCustomInfo(400011121, 1, 0);
								monster.setCustomInfo(400011122, 0, 10000);
							}
							int n = attack.skill == 400011015 ? effect.getW() * 1000
									: (du = effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration());
							if (attack.skill == 14121004) {
								if ((du += attack.targets * 1000) > effect.getS() * 1000) {
									du = effect.getS() * 1000;
								}
							} else if (attack.skill == 64121001) {
								du = effect.getDuration();
							}
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Freeze,
									new MonsterStatusEffect(attack.skill, du), 1L));
							break;
						}
						case 400011015: {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Freeze,
									new MonsterStatusEffect(400011024, 10000), Long.valueOf(effect.getDuration())));
							break;
						}
						default: {
							SecondaryStatEffect effect2;
							if (player.getSkillLevel(5110000) > 0) {
								SecondaryStatEffect stunMastery2 = SkillFactory.getSkill(5110000)
										.getEffect(player.getSkillLevel(5110000));
								if (!stunMastery2.makeChanceResult()) {
									break;
								}
								applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_Stun,
										new MonsterStatusEffect(5110000, 1000, 1L)));
								break;
							}
							if (player.getBuffedValue(1111003)) {
								applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_Blind,
										new MonsterStatusEffect(1111003, 20000, 1)));
								applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_Pad,
										new MonsterStatusEffect(1111003, 20000, 1)));
								break;
							}
							if (player.getBuffedValue(5311004)) {
								effect2 = SkillFactory.getSkill(5311004).getEffect(player.getSkillLevel(5311004));
								if (player.getSkillCustomValue0(5311004) == 2L) {
									if (!effect2.makeChanceResult()) {
										break;
									}
									applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_Speed,
											new MonsterStatusEffect(effect2.getSourceId(), effect2.getV() * 1000,
													-30L)));
									break;
								}
								if (player.getSkillCustomValue0(5311004) == 4L) {
									applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_Burned,
											new MonsterStatusEffect(effect2.getSourceId(), effect2.getDOTTime(),
													(long) effect2.getDOT() * totDamageToOneMonster
															/ (long) attack.allDamage.size() / 10000L)));
									break;
								}
							}
							// 칼리 파이널어택
							if (player.getBuffedValue(154110005)) {
								effect2 = SkillFactory.getSkill(154110005).getEffect(player.getSkillLevel(154110005));
								if (player.getSkillCustomValue0(154110005) == 1L) {
									if (!effect2.makeChanceResult()) {
										break;
									}
									applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_Speed,
											new MonsterStatusEffect(effect2.getSourceId(), effect2.getV() * 1000,
													-30L)));
									break;
								}
								if (player.getSkillCustomValue0(5311004) == 4L) {
									applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_Burned,
											new MonsterStatusEffect(effect2.getSourceId(), effect2.getDOTTime(),
													(long) effect2.getDOT() * totDamageToOneMonster
															/ (long) attack.allDamage.size() / 10000L)));
									break;
								}
							}
							if (player.getBuffedEffect(SecondaryStat.SnowCharge) != null) {
								applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_Speed,
										new MonsterStatusEffect(player.getBuffSource(SecondaryStat.SnowCharge),
												player.getBuffedEffect(SecondaryStat.SnowCharge).getY()
														* (monster.getStats().isBoss() ? 500 : 1000),
												-player.getBuffedEffect(SecondaryStat.SnowCharge).getQ()
														/ (monster.getStats().isBoss() ? 2 : 1))));
								break;
							}
							if (player.getSkillLevel(25110210) > 0) {
								SecondaryStatEffect weakness = SkillFactory.getSkill(25110210)
										.getEffect(player.getSkillLevel(25110210));
								if (!weakness.makeChanceResult()) {
									break;
								}
								applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_Acc,
										new MonsterStatusEffect(25110210, weakness.getDuration(), -weakness.getX())));
								applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_Eva,
										new MonsterStatusEffect(25110210, weakness.getDuration(), -40L)));
								applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_AdddamSkill2,
										new MonsterStatusEffect(25110210, weakness.getDuration(), weakness.getX())));
								break;
							}
							if (GameConstants.isPathFinder(player.getJob())) {
								effect2 = SkillFactory.getSkill(3320001).getEffect(player.getSkillLevel(3320001));
								if ((attack.skill == 3011004 || attack.skill == 3300002 || attack.skill == 3321003
										|| attack.skill == 3321005) && player.getBuffedValue(3320008) && effect2 != null
										&& SkillFactory.getSkill(3320008).getEffect(player.getSkillLevel(3320008))
												.makeChanceResult()) {
									player.setSkillCustomInfo(3320008, player.getSkillCustomValue0(3320008) - 1L, 0L);
									if (player.getSkillCustomValue0(3320008) == 0L) {
										player.cancelEffectFromBuffStat(SecondaryStat.BonusAttack);
									} else {
										SkillFactory.getSkill(3320008).getEffect(player.getSkillLevel(3320008))
												.applyTo(player, (int) player.getBuffLimit(3320008));
									}
									if (monster.getBuff(MonsterStatus.MS_BossPropPlus) == null) {
										monster.removeCustomInfo(3320008);
									}
									if (monster.getCustomValue0(3320008) < 5L) {
										monster.addSkillCustomInfo(3320008, 1L);
									}
									applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(
											MonsterStatus.MS_BossPropPlus, new MonsterStatusEffect(3320001,
													effect2.getDuration(), monster.getCustomValue0(3320008))));
								}
								switch (attack.skill) {
								case 3321007:
								case 3321016:
								case 3321018: {
									if (monster.getBuff(MonsterStatus.MS_BossPropPlus) == null) {
										monster.removeCustomInfo(3320008);
									}
									if (monster.getCustomValue0(3320008) < 5L) {
										monster.addSkillCustomInfo(3320008, 1L);
									}
									applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(
											MonsterStatus.MS_BossPropPlus, new MonsterStatusEffect(3320001,
													effect2.getDuration(), monster.getCustomValue0(3320008))));
									break;
								}
								case 3321020: {
									monster.setCustomInfo(3320008, 5, 0);
									applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(
											MonsterStatus.MS_BossPropPlus, new MonsterStatusEffect(3320001,
													effect2.getDuration(), monster.getCustomValue0(3320008))));
								}
								}
							}
							if (GameConstants.isKadena(player.getJob())) {
								SecondaryStatEffect eff2 = SkillFactory.getSkill(64120007)
										.getEffect(player.getSkillLevel(64120007));
								if (player.getSkillLevel(64120007) > 0 && eff2 != null && eff2.makeChanceResult()) {
									MonsterStatusEffect monsterStatusEffect = new MonsterStatusEffect(
											eff2.getSourceId(), eff2.getDOTTime(), (int) ((long) eff2.getDOT()
													* totDamageToOneMonster / (long) attack.allDamage.size() / 10000L));
									applys2.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_Burned,
											monsterStatusEffect));
								}
							}
							int resist = SkillFactory.getSkill(101120110).getEffect(player.getSkillLevel(101120110))
									.getW() * 1000;
							if (player.getSkillLevel(101120110) <= 0 || player.getGender() != 1) {
								break;
							}
							if (System.currentTimeMillis() - monster.getLastCriticalBindTime() > (long) resist) {
								if (monster.getStats().isBoss()) {
									statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(
											MonsterStatus.MS_Freeze,
											new MonsterStatusEffect(101120110,
													SkillFactory.getSkill(101120110)
															.getEffect(player.getSkillLevel(101120110)).getDuration()),
											Long.valueOf(SkillFactory.getSkill(101120110)
													.getEffect(player.getSkillLevel(101120110)).getDuration())));
								} else {
									statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(
											MonsterStatus.MS_CriticalBind_N,
											new MonsterStatusEffect(101120110,
													SkillFactory.getSkill(101120110)
															.getEffect(player.getSkillLevel(101120110)).getDuration()),
											1L));
								}
								monster.setLastCriticalBindTime(System.currentTimeMillis());
								break;
							}
							if (player.getSkillCustomValue(101120110) != null || !monster.getStats().isBoss()) {
								break;
							}
							player.setSkillCustomInfo(101120110, 0L, 10000L);
							player.getClient().getSession()
									.writeAndFlush((Object) MobPacket.monsterResist(monster, player,
											(int) (((long) resist
													- (System.currentTimeMillis() - monster.getLastCriticalBindTime()))
													/ 1000L),
											101120110));
							break;
						}
						}

						applyDebuff(player, monster, effect, statusz, attack.skill);

						int sk = 0;
						boolean enhance = false;
						final int[] array2;
						final int[] venoms = array2 = new int[] { 4110011, 4210010, 4320005 };
						for (final int venom : array2) {
							if (player.getSkillLevel(venom) > 0) {
								sk = venom;
							}
						}
						if (sk > 0 && attack.skill != 4111003) {
							final int[] array3;
							final int[] fatals = array3 = new int[] { 4120011, 4220011, 4340012 };
							for (final int fatal : array3) {
								if (player.getSkillLevel(fatal) > 0) {
									enhance = true;
									sk = fatal;
								}
							}
							final SecondaryStatEffect venomEffect = SkillFactory.getSkill(sk)
									.getEffect(player.getSkillLevel(sk));
							final MonsterStatusEffect monsterStatusEffect2 = new MonsterStatusEffect(
									venomEffect.getSourceId(), venomEffect.getDOTTime());
							if (venomEffect.makeChanceResult()) {
								if (monster.isBuffed(MonsterStatus.MS_Burned)) {
									if (monster.getBurnedBuffSize(sk) < (enhance ? venomEffect.getDotSuperpos() : 1)) {
										statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(
												MonsterStatus.MS_Burned, monsterStatusEffect2, venomEffect.getDOT()
														* totDamageToOneMonster / attack.allDamage.size() / 10000L));
									}
								} else {
									statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(
											MonsterStatus.MS_Burned, monsterStatusEffect2, venomEffect.getDOT()
													* totDamageToOneMonster / attack.allDamage.size() / 10000L));
								}
							}
						}
						if (player.getSkillLevel(101110103) > 0) {
							SecondaryStatEffect stunMastery3 = SkillFactory.getSkill(101110103)
									.getEffect(player.getSkillLevel(101110103));
							if (player.getGender() == 1 && Randomizer
									.isSuccess(((SecondaryStatEffect) ((Object) stunMastery3)).getProp())) {
								if (monster.getBuff(101110103) == null && monster.getCustomValue0(101110103) > 0L) {
									monster.removeCustomInfo(101110103);
								}
								if (monster.getCustomValue0(101110103) < 5L) {
									monster.setCustomInfo(101110103, (int) monster.getCustomValue0(101110103) + 1, 0);
								}
								statusz.add(
										new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_MultiPMDR,
												new MonsterStatusEffect(101110103,
														((SecondaryStatEffect) ((Object) stunMastery3)).getDuration()),
												(long) ((SecondaryStatEffect) ((Object) stunMastery3)).getY()
														* monster.getCustomValue0(101110103)));
							}
						}
						if (player.getSkillLevel(101120207) > 0) {
							final SecondaryStatEffect stunMastery3 = SkillFactory.getSkill(101120207)
									.getEffect(player.getSkillLevel(101120207));
							if (player.getGender() == 0 && debinrear) {
								statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(
										MonsterStatus.MS_Burned,
										new MonsterStatusEffect(101120207,
												((SecondaryStatEffect) ((Object) stunMastery3)).getDOTTime()),
										(long) ((SecondaryStatEffect) ((Object) stunMastery3)).getDOT()
												* totDamageToOneMonster / (long) attack.allDamage.size() / 10000L));
							}
						}
						if (player.getBuffedValue(SecondaryStat.BleedingToxin) != null) {
							eff = player.getBuffedEffect(SecondaryStat.BleedingToxin);
							if (eff != null && eff.makeChanceResult()) {
								MonsterStatusEffect monsterStatusEffect = new MonsterStatusEffect(eff.getSourceId(),
										eff.getDOTTime());
								statusz2.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(
										MonsterStatus.MS_Burned, monsterStatusEffect, (long) eff.getDOT()
												* totDamageToOneMonster / (long) attack.allDamage.size() / 10000L));
							}
						} else if (player.getBuffedValue(SecondaryStat.ElementDarkness) != null) {
							int[] passive;
							eff = player.getBuffedEffect(SecondaryStat.ElementDarkness);
							int suc = eff.getProp();
							int dot = eff.getDOT();
							for (int pas : passive = new int[] { 14100026, 14110028, 0xD77447 }) {
								SecondaryStatEffect eff1;
								if (player.getSkillLevel(pas) <= 0 || (eff1 = SkillFactory.getSkill(pas)
										.getEffect(player.getSkillLevel(pas))) == null) {
									continue;
								}
								suc += eff1.getProp();
								dot += eff1.getDOT();
							}
							if (eff != null && Randomizer.isSuccess(suc)) {
								if (monster.getBuff(MonsterStatus.MS_ElementDarkness) == null) {
									monster.setCustomInfo(14001021, 0, 0);
								}
								if (monster.getCustomValue0(14001021) < 5L) {
									monster.addSkillCustomInfo(14001021, 1L);
								}
								if (player.getBuffedValue(14121052)) {
									monster.setCustomInfo(14001021, 5, 0);
								}
								applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_Burned,
										new MonsterStatusEffect(eff.getSourceId(), eff.getDOTTime(),
												(int) ((long) eff.getDOT() * totDamageToOneMonster
														* monster.getCustomValue0(14001021)
														/ (long) attack.allDamage.size() / 1000L))));
								applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(
										MonsterStatus.MS_ElementDarkness, new MonsterStatusEffect(eff.getSourceId(),
												eff.getDOTTime(), monster.getCustomValue0(14001021))));
								if (player.getSkillLevel(14120009) > 0
										&& player.getBuffedEffect(SecondaryStat.Protective) == null) {
									SkillFactory.getSkill(14120009).getEffect(player.getSkillLevel(14120009))
											.applyTo(player, false);
								}
							}
							// 데스 블레싱 스택
						} else if (GameConstants.isCain(player.getJob()) && player.getSkillLevel(63110011) > 0
								&& monster != null) {
							SecondaryStatEffect effect2 = SkillFactory.getSkill(63110011)
									.getEffect(player.getSkillLevel(63110011));
							if (SkillFactory.getSkill(63110011).getSkillList().contains(attack.skill)
									&& monster.isAlive()) {
								boolean cast = true;

								if ((attack.skill == 63101104 || attack.skill == 63121141)
										&& monster.getCustomValue(attack.skill) != null) {
									cast = false;
								}
								if (cast) {
									monster.setCustomInfo(63110011, (int) monster.getCustomValue0(63110011) + 1,
											effect2.getDuration());
									if (monster
											.getCustomValue0(63110011) >= (player.getBuffedValue(63141503) ? 20 : 15)) {
										monster.setCustomInfo(63110011, (player.getBuffedValue(63141503) ? 20 : 15),
												90000);
									}
									ArrayList<MapleMonster> moblist = new ArrayList<MapleMonster>();
									for (MapleMonster mob : player.getMap().getAllMonster()) {
										if (mob.getCustomValue0(63110011) <= 0L) {
											continue;
										}
										moblist.add(mob);
									}
									player.getClient().send(CField.getDeathBlessStack(player, moblist));
									if (attack.skill == 63101104 || attack.skill == 63121141) {
										monster.setCustomInfo(attack.skill, 0, 3000);
									}
								}
							} else if (SkillFactory.getSkill(63110011).getSkillList2().contains(attack.skill)) {
								if (player.getBuffedValue(63111013)) {
									player.handlePossession(10);
								}
								if (monster.isAlive() && monster.getCustomValue0(63110011) > 0L) {
									monster.setCustomInfo(63110011, (int) monster.getCustomValue0(63110011) - 1,
											effect2.getDuration());
									ArrayList<MapleMonster> moblist = new ArrayList<MapleMonster>();
									moblist.add(monster);
									player.handlePossession(2);
									player.getClient().send(CField.getDeathBlessAttack(moblist,
											player.getBuffedValue(63141503) ? 63141505 : 63111012));
									moblist.clear();
									for (MapleMonster mob : player.getMap().getAllMonster()) {
										if (mob.getCustomValue0(63110011) <= 0L) {
											continue;
										}
										moblist.add(mob);
									}
									// System.err.println("getDeathBlessStack");
									player.getClient().send(CField.getDeathBlessStack(player, moblist));
									SkillFactory.getSkill(63111013).getEffect(player.getSkillLevel(63110011))
											.applyTo(player);
								}
								if (player.getSkillLevel(63120001) > 0) {
									if (monster.getStats().isBoss() && monster.isAlive()) {
										player.addHP(player.getStat().getCurrentMaxHp() / 100L * (long) SkillFactory
												.getSkill(63111013).getEffect(player.getSkillLevel(63111013)).getX());
									} else if (!monster.isAlive()) {
										player.addSkillCustomInfo(63111013, 1L);
										if (player.getSkillCustomValue0(63111013) >= (long) SkillFactory
												.getSkill(63111013).getEffect(player.getSkillLevel(63111013)).getV()) {
											player.removeSkillCustomInfo(63111013);
											player.addHP(player.getStat().getCurrentMaxHp() / 100L
													* (long) SkillFactory.getSkill(63111013)
															.getEffect(player.getSkillLevel(63111013)).getV());
										}
									}
								}
							}
						}

						if (player.getSkillLevel(400041000) > 0 && attack.skill != 400041000
								&& Randomizer.isSuccess(50)) {
							SecondaryStatEffect stunMastery3 = SkillFactory.getSkill(400041000)
									.getEffect(player.getSkillLevel(400041000));
							applys2.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_Burned,
									new MonsterStatusEffect(400040000, 8000,
											(int) ((long) ((SecondaryStatEffect) ((Object) stunMastery3)).getDOT()
													* totDamageToOneMonster / (long) attack.allDamage.size()
													/ 10000L))));
						}
						for (Triple status : statusz) {
							if (status.left == null || status.mid == null
									|| ((MonsterStatusEffect) status.mid).shouldCancel(System.currentTimeMillis())) {
								continue;
							}
							if (status.left == MonsterStatus.MS_Burned && (Long) status.right < 0L) {
								status.right = (Long) status.right & 0xFFFFFFFFL;
							}
							if (((MonsterStatusEffect) status.mid).getSkill() == 51121009
									? Randomizer.isSuccess(effect.getY())
									: (((MonsterStatusEffect) status.mid).getSkill() == 64121016
											? Randomizer.isSuccess(effect.getS2())
											: effect.makeChanceResult())) {
								((MonsterStatusEffect) status.mid).setValue((Long) status.right);
								applys.add(new Pair(status.left, status.mid));
								continue;
							}
							if (attack.skill != 1211008) {
								continue;
							}
							((MonsterStatusEffect) status.mid).setValue((Long) status.right);
							applys.add(new Pair(status.left, status.mid));
						}
						for (Triple status : statusz1) {
							if (status.left == null || status.mid == null
									|| ((MonsterStatusEffect) status.mid).shouldCancel(System.currentTimeMillis())) {
								continue;
							}
							if (status.left == MonsterStatus.MS_Burned && (Long) status.right < 0L) {
								status.right = (Long) status.right & 0xFFFFFFFFL;
							}
							if (((MonsterStatusEffect) status.mid).getSkill() == 51121009
									? Randomizer.isSuccess(effect.getY())
									: (((MonsterStatusEffect) status.mid).getSkill() == 64121016
											? Randomizer.isSuccess(effect.getS2())
											: effect.makeChanceResult())) {
								((MonsterStatusEffect) status.mid).setValue((Long) status.right);
								applys1.add(new Pair(status.left, status.mid));
								continue;
							}
							if (attack.skill != 1211008) {
								continue;
							}
							((MonsterStatusEffect) status.mid).setValue((Long) status.right);
							applys1.add(new Pair(status.left, status.mid));
						}
						for (Triple status : statusz2) {
							if (status.left == null || status.mid == null
									|| ((MonsterStatusEffect) status.mid).shouldCancel(System.currentTimeMillis())) {
								continue;
							}
							if (status.left == MonsterStatus.MS_Burned && (Long) status.right < 0L) {
								status.right = (Long) status.right & 0xFFFFFFFFL;
							}
							if (((MonsterStatusEffect) status.mid).getSkill() == 51121009
									? Randomizer.isSuccess(effect.getY())
									: (((MonsterStatusEffect) status.mid).getSkill() == 64121016
											? Randomizer.isSuccess(effect.getS2())
											: effect.makeChanceResult())) {
								((MonsterStatusEffect) status.mid).setValue((Long) status.right);
								applys2.add(new Pair(status.left, status.mid));
								continue;
							}
							if (attack.skill != 1211008) {
								continue;
							}
							((MonsterStatusEffect) status.mid).setValue((Long) status.right);
							applys2.add(new Pair(status.left, status.mid));
						}
						SecondaryStatEffect elementSoul = player.getBuffedEffect(11001022);
						if (elementSoul != null && elementSoul.makeChanceResult()) {
							applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_Stun,
									new MonsterStatusEffect(elementSoul.getSourceId(), elementSoul.getSubTime(),
											elementSoul.getSubTime())));
						}
						if (attack.skill == 13111021 && attack.hits == 2) {
							applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_MultiDamSkill,
									new MonsterStatusEffect(effect.getSourceId(), effect.getDuration(),
											effect.getX())));
						}
						if (monster != null && monster.isAlive()) {
							if (!applys.isEmpty()) {
								monster.applyStatus(player.getClient(), applys, effect);
							}
							if (!applys1.isEmpty()) {
								monster.applyStatus(player.getClient(), applys1, effect);
							}
							if (!applys2.isEmpty()) {
								monster.applyStatus(player.getClient(), applys2, effect);
							}
						}
						if (!applys.isEmpty() && player.getSkillLevel(80002770) > 0) {
							SkillFactory.getSkill(80002770).getEffect(player.getSkillLevel(80002770)).applyTo(player,
									false);
						}
					}
					if (player.getBuffedValue(SecondaryStat.BMageDeath) != null && player.skillisCooling(32001114)
							&& GameConstants.isBMDarkAtackSkill(attack.skill)
							&& player.getBuffedValue(SecondaryStat.AttackCountX) != null) {
						player.changeCooldown(32001114, -500);
					}
					if (player.getBuffedValue(SecondaryStat.BMageDeath) != null
							&& (!monster.isAlive() || monster.getStats().isBoss())
							&& attack.skill != player.getBuffSource(SecondaryStat.BMageDeath)) {
						byte count;
						byte by = player.getBuffedValue(SecondaryStat.AttackCountX) != null ? (byte) 1
								: (player.getLevel() >= 100 ? (byte) 6
										: (count = player.getLevel() > 60 ? (byte) 8 : 10));
						if (player.getDeath() < by) {
							player.setDeath((byte) (player.getDeath() + 1));
							if (player.getDeath() >= by) {
								player.setSkillCustomInfo(32120019, 1L, 0L);
							}
							HashMap<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
							statups.put(SecondaryStat.BMageDeath,
									new Pair<Integer, Integer>(Integer.valueOf(player.getDeath()), 0));
							player.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket
									.giveBuff(statups, player.getBuffedEffect(SecondaryStat.BMageDeath), player));
						}
					}
					if (GameConstants.isHolyAttack(attack.skill)
							&& monster.isBuffed(MonsterStatus.MS_ElementResetBySummon)) {
						monster.cancelStatus(MonsterStatus.MS_ElementResetBySummon,
								monster.getBuff(MonsterStatus.MS_ElementResetBySummon));
					}
					if (monster.isBuffed(MonsterStatus.MS_JaguarBleeding) && attack.targets > 0
							&& monster.getBuff(MonsterStatus.MS_JaguarBleeding) != null
							&& (attack.skill == 33001105 || attack.skill == 33001205 || attack.skill == 33101113
									|| attack.skill == 33101213 || attack.skill == 33111112 || attack.skill == 33111212
									|| attack.skill == 33121114 || attack.skill == 33121214)) {
						ArrayList<Triple<Integer, Integer, Integer>> mobList = new ArrayList<Triple<Integer, Integer, Integer>>();
						mobList.add(new Triple<Integer, Integer, Integer>(monster.getObjectId(), 60, 0));
						player.getClient().getSession().writeAndFlush((Object) CField.bonusAttackRequest(33000036,
								mobList, false, monster.getAnotherByte(), new int[0]));
					}
					if (player.getSkillLevel(400011116) > 0
							&& SkillFactory.getSkill(400011116).getSkillList().contains(attack.skill)) {
						SecondaryStatEffect effect2 = SkillFactory.getSkill(400011116)
								.getEffect(player.getSkillLevel(400011116));
						if (player.getBuffedValue(400011116)) {
							afterimageshockattack = true;
							int i = 0;
							ArrayList<Triple<Integer, Integer, Integer>> mobList = new ArrayList<Triple<Integer, Integer, Integer>>();
							for (AttackPair a : attack.allDamage) {
								mobList.add(new Triple<Integer, Integer, Integer>(a.objectId, 120 + 70 * i, 0));
								++i;
							}
							player.getClient().getSession().writeAndFlush(
									(Object) CField.bonusAttackRequest(400011133, mobList, false, 0, new int[0]));

						} else if (!player.getBuffedValue(400011116) && attack.targets > 0
								&& player.skillisCooling(400011116) && !player.skillisCooling(400011117)) {
							ArrayList<Triple<Integer, Integer, Integer>> mobList = new ArrayList<Triple<Integer, Integer, Integer>>();
							int i = 0;
							for (MapleMapObject o : player.getMap().getMapObjectsInRange(player.getPosition(), 100000.0,
									Arrays.asList(new MapleMapObjectType[] { MapleMapObjectType.MONSTER }))) {
								MapleMonster mon = (MapleMonster) o;
								mobList.clear();
								mobList.add(new Triple<Integer, Integer, Integer>(mon.getObjectId(), 120 + 70 * i, 0));
								if (++i == 7) {
									break;
								}
								player.getClient().getSession().writeAndFlush((Object) CField
										.bonusAttackRequest(400011117, mobList, false, mon.getObjectId(), new int[0]));
							}
							player.addCooldown(400011117, System.currentTimeMillis(), effect2.getX() * 1000);
							player.getClient().getSession()
									.writeAndFlush((Object) CField.skillCooldown(400011117, effect2.getX() * 1000));
						}
					}
					if (player.getBuffedValue(400031000)) {
						player.getMap()
								.broadcastMessage(CField.ForceAtomAttack(1, player.getId(), monster.getObjectId()));
					}
					if (attack.skill != 400041035 && attack.skill != 400041036 && player.getBuffedValue(400041035)
							&& System.currentTimeMillis() - player.lastChainArtsFuryTime >= 1000L) {
						player.lastChainArtsFuryTime = System.currentTimeMillis();
						player.getMap().broadcastMessage(CField.ChainArtsFury(monster.getTruePosition()));
					}
					if (player.getBuffedValue(400011016)) {
						SecondaryStatEffect installMaha = player.getBuffedEffect(400011016);
						if (System.currentTimeMillis()
								- player.lastInstallMahaTime >= (long) (installMaha.getX() * 1000)) {
							player.lastInstallMahaTime = System.currentTimeMillis();
							player.getClient().getSession().writeAndFlush((Object) CField.bonusAttackRequest(400011020,
									new ArrayList<Triple<Integer, Integer, Integer>>(), true, 0, new int[0]));
						}
					}
					if (attack.skill == 31211001 && player.getBuffedEffect(SecondaryStat.DebuffIncHp) == null) {
						player.addHP(player.getStat().getCurrentMaxHp() * (long) effect.getY() / 100L);
					}
					if (totDamage > 0L && attack.skill == 4221016 && player.getSkillLevel(400041025) > 0) {
						if (player.shadowerDebuffOid == 0) {
							player.shadowerDebuff = Math.min(3, player.shadowerDebuff + 1);
							player.shadowerDebuffOid = monster.getObjectId();
						} else if (player.shadowerDebuffOid != monster.getObjectId()) {
							player.shadowerDebuff = 1;
							player.shadowerDebuffOid = monster.getObjectId();
						} else {
							player.shadowerDebuff = Math.min(3, player.shadowerDebuff + 1);
						}
						effect.applyTo(player);
					}
					if (attack.skill == 400041026) {
						player.shadowerDebuff = 0;
						player.shadowerDebuffOid = 0;
						SkillFactory.getSkill(400041026).getEffect(player.getSkillLevel(400041025)).applyTo(player);
						player.cancelEffectFromBuffStat(SecondaryStat.ShadowerDebuff, 4221016);
					}
					if (attack.skill == 5221015) {
						player.guidedBullet = monster.getObjectId();
						if (player.getKeyValue(1544, String.valueOf(5221029)) == 1) {
							MapleSummon summon12 = player.getSummon(5221029);
							if (summon12 != null) {
								player.removeSummon(summon12);
								player.getMap().broadcastMessage(CField.SummonPacket.removeSummon(summon12, true));
							}
							MapleSummon summon4 = new MapleSummon(player, 5221029, monster.getPosition(),
									SummonMovementType.STATIONARY, (byte) 0, 60000);
							player.getMap().spawnSummon(summon4, 60000);
							player.addSummon(summon4);
						}
					}
					if (attack.skill == 151121001) {
						player.graveObjectId = monster.getObjectId();
					}
					if (player.getBuffedValue(400031002) && attack.skill != 400030002
							&& (player.lastArrowRain == 0L || player.lastArrowRain < System.currentTimeMillis())) {
						SecondaryStatEffect arrowRain = player.getBuffedEffect(400031002);
						SkillFactory.getSkill(400030002).getEffect(arrowRain.getLevel()).applyTo(player,
								monster.getTruePosition(), (int) (arrowRain.getT() * 1000.0));
						player.lastArrowRain = System.currentTimeMillis() + (long) (arrowRain.getX() * 1000);
					}
					if (player.getBuffedValue(400041008)
							&& (GameConstants.isDarkAtackSkill(attack.skill) || attack.summonattack != 0)) {
						SecondaryStatEffect a = SkillFactory.getSkill(400040008)
								.getEffect(player.getSkillLevel(400040008));
						MapleMist mist = new MapleMist(
								a.calculateBoundingBox(monster.getPosition(), player.isFacingLeft()), player, a, 2000,
								(byte) (player.isFacingLeft() ? 1 : 0));
						mist.setPosition(monster.getPosition());
						player.getMap().spawnMist(mist, false);
						int mists = 0;
						for (MapleMist mist2 : player.getMap().getAllMistsThreadsafe()) {
							if (mist2.getOwnerId() == player.getId() && mist.getSourceSkill().getId() == 400040008
									&& ++mists == 9) {
								break;
							}
						}
						if (player.getSkillCustomValue(400041019) == null && mists >= 9) {
							player.getMap().broadcastMessage(CField.NightWalkerShadowSpearBig(
									monster.getTruePosition().x, monster.getTruePosition().y));
							player.setSkillCustomInfo(400041019, 0L, 3000L);
						}
					}
					if (player.getSkillLevel(32101009) > 0 && !monster.isAlive()
							&& player.getBuffedEffect(SecondaryStat.DebuffIncHp) == null) {
						player.addHP(player.getStat().getCurrentMaxHp() * (long) SkillFactory.getSkill(32101009)
								.getEffect(player.getSkillLevel(32101009)).getKillRecoveryR() / 100L);
					}
					// 쉐도우 바이트
					if (attack.skill == 400041037) {
						SecondaryStatEffect shadowBite = SkillFactory.getSkill(400041037)
								.getEffect(player.getSkillLevel(400041037));
						if (player.getBuffedValue(SecondaryStat.ShadowBatt) != null) {
							MapleAtom atom = new MapleAtom(false, player.getId(), 15, true, 14000028,
									monster.getTruePosition().x, monster.getTruePosition().y);
							atom.setDwFirstTargetId(monster.getObjectId());
							ForceAtom forceAtom = new ForceAtom(player.getSkillLevel(14120008) > 0 ? 2 : 1, 1, 5,
									Randomizer.rand(45, 90), 500);
							atom.addForceAtom(forceAtom);
							player.getMap().spawnMapleAtom(atom);
						}
						if (!monster.isAlive() || monster.getStats().isBoss()) {
							player.shadowBite = Math.min(shadowBite.getQ(), player.shadowBite
									+ (monster.getStats().isBoss() ? shadowBite.getW() : shadowBite.getY()));
							MapleAtom atom = new MapleAtom(true, monster.getObjectId(), 42, true, 400041037,
									monster.getTruePosition().x, monster.getTruePosition().y);
							atom.setDwUserOwner(player.getId());
							atom.setDwFirstTargetId(0);
							atom.addForceAtom(new ForceAtom(2, 42, 6, 33, (short) Randomizer.rand(2500, 3000)));
							player.getMap().spawnMapleAtom(atom);
							// System.err.println("player.shadowBite = " + player.shadowBite);
							if (player.shadowBite > 0 && !player.getBuffedValue(400041037)) {
								SkillFactory.getSkill(400041037).getEffect(attack.skilllevel).applyTo(player, false);
							}
						}
					}
					if (attack.skill == 155100009 && player.getSkillLevel(155111207) > 0 && Randomizer.isSuccess(
							(mark = SkillFactory.getSkill(155111207).getEffect(player.getSkillLevel(155111207))).getS())
							&& player.getMwSize(155111207) < (player.getKeyValue(1544, "155111207") == 1L ? mark.getY()
									: mark.getZ())) {
						int x = monster.getPosition().x + Randomizer.rand(-100, 100);
						int y = monster.getPosition().y + Randomizer.rand(-70, 30);
						MapleMagicWreck mw = new MapleMagicWreck(player, mark.getSourceId(), new Point(x, y),
								mark.getQ() * 1000);
						player.getMap().spawnMagicWreck(mw);
					}
					if (GameConstants.isIllium(player.getJob()) && monster.getBuff(MonsterStatus.MS_CurseMark) != null
							&& attack.skill != 152001002 && attack.skill != 152120003 && attack.skill != 152120002
							&& attack.skill != 152120016) {
						int skillid = player.getSkillLevel(152120013) > 0 ? 152120013
								: (player.getSkillLevel(152110010) > 0 ? 152110010 : 152100012);
						ArrayList<Triple<Integer, Integer, Integer>> mobList = new ArrayList<Triple<Integer, Integer, Integer>>();
						mobList.add(new Triple<Integer, Integer, Integer>(monster.getObjectId(), 120, 0));
						player.getClient().getSession().writeAndFlush(
								(Object) CField.bonusAttackRequest(skillid, mobList, false, 0, new int[0]));
					}
					if (player.getBuffedValue(400051007) && attack.skill != 400051007 && attack.skill != 400051013
							&& System.currentTimeMillis()
									- player.lastThunderTime >= (long) (player.getBuffedEffect(400051007).getY()
											* 1000)) {
						player.lastThunderTime = System.currentTimeMillis();
						player.getClient().getSession().writeAndFlush((Object) CField
								.lightningUnionSubAttack(attack.skill, 400051007, player.getSkillLevel(400051007)));
					}
					if (player.getBuffedValue(80002890) && attack.skill != 80002890 && attack.skill != 80002890
							&& System.currentTimeMillis() - player.lastThunderTime >= (long) player
									.getBuffedEffect(80002890).getCooldown(player)) {
						player.lastThunderTime = System.currentTimeMillis();
						player.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(80002890,
								Arrays.asList(new RangeAttack(80002890, attack.position, 0, 0, 1))));
					}
					if (GameConstants.isFPMage(player.getJob())) {
						if (SkillFactory.getSkill(2111013).getSkillList2().contains(attack.skill)
								|| attack.skill == 2111014) {
							Rectangle Rectanglebox1 = effect.calculateBoundingBox(attack.position,
									player.isFacingLeft());
							Rectangle Rectanglebox2 = effect.calculateBoundingBox(attack.position,
									player.isFacingLeft());
							if (attack.skill == 2111014) {
								Rectanglebox1 = effect.calculateBoundingBox(
										new Point(attack.position.x, attack.position.y + 170), true, 100);
								Rectanglebox2 = effect.calculateBoundingBox(
										new Point(attack.position.x, attack.position.y + 170), false, 100);
							}
							for (MapleMist mist : player.getMap().getAllMistsThreadsafe()) {
								if (mist.getSourceSkill().getId() == 2111013) {
									if (Rectanglebox1.contains(mist.getPosition())
											|| Rectanglebox2.contains(mist.getPosition())) {
										if (mist.getStartTime() + 1500 < System.currentTimeMillis()) {
											player.getClient().getSession().writeAndFlush(CField.rangeAttackTest(
													2111013, attack.skill, mist.getObjectId(),
													Arrays.asList(new RangeAttack(2111014,
															new Point(mist.getPosition().x, mist.getPosition().y - 170),
															0, 240, 1))));
											server.Timer.MapTimer.getInstance().schedule(() -> {
												player.getMap().removeMist(mist);
											}, 300);
										}
									}
								}
							}
						}
					}
					if (player.getBuffedValue(4221054)) {
						if (player.getFlip() < 5) {
							player.setFlip((byte) (player.getFlip() + 1));
							SkillFactory.getSkill(4221054).getEffect(player.getSkillLevel(4221054)).applyTo(player,
									false, false);
						}
					}
					if (attack.skill == 5311002) {
						player.cancelEffectFromBuffStat(SecondaryStat.KeyDownTimeIgnore, 5310008);
					} else if (player.getSkillLevel(5311002) > 0 && !player.getBuffedValue(5310008)
							&& attack.skill != 400051008) {
						SkillFactory.getSkill(5310008).getEffect(player.getSkillLevel(5311002)).applyTo(player, false);
					}
					if (player.getBuffedValue(SecondaryStat.PinPointRocket) != null && attack.skill != 36001005
							&& System.currentTimeMillis() - player.lastPinPointRocketTime >= (long) (player
									.getBuffedEffect(SecondaryStat.PinPointRocket).getX() * 1000)) {
						player.lastPinPointRocketTime = System.currentTimeMillis();
						MapleAtom atom = new MapleAtom(false, player.getId(), 6, true, 36001005,
								player.getTruePosition().x, player.getTruePosition().y);
						ArrayList<Integer> monsters = new ArrayList<Integer>();
						for (int i2 = 0; i2 < player.getBuffedEffect(SecondaryStat.PinPointRocket)
								.getBulletCount(); ++i2) {
							monsters.add(0);
							atom.addForceAtom(
									new ForceAtom(0, 19, Randomizer.rand(20, 40), Randomizer.rand(40, 200), 0));
						}
						atom.setDwTargets(monsters);
						player.getMap().spawnMapleAtom(atom);
					}
					if (attack.skill != 400011060 || monster == null
							|| player.getBuffedEffect(SecondaryStat.WillofSwordStrike) != null) {
						continue;
					}
					effect.applyTo(player, false, monster.getTruePosition());
				}

				if (player.getKeyValue(21770, "500001000") == 500001001 && !player.skillisCooling(500001001)) { // HEXA
																												// 솔 야누스
																												// 구현
					ArrayList<RangeAttack> attacks = new ArrayList<>();

					attacks.add(new RangeAttack(500001001, player.getPosition(),
							((attack.facingleft >>> 4 & 0xF) == 0) ? 0 : 1, 0, 1));
					player.getClient().getSession().writeAndFlush(CField.rangeAttack(500001001, attacks));

					int count = 0;
					for (MapleMonster mob : player.getMap().getAllMonster()) {
						if (count >= 9) {
							break;
						}

						SecondAtom2 sa = SkillFactory.getSkill(500001001).getSecondAtoms().get(0);
						sa.setTarget(mob.getObjectId());

						player.createSecondAtom(sa, new Point(monster.getPosition().x, monster.getPosition().y), false);

						count++;
					}

					player.addHP(-(player.getStat().getMaxHp() / 100));
				}

				if (attack.skill == 35121016) { // 매시브 파이어 : IRON-B 구현
					if (monster != null) {
						SkillFactory.getSkill(35121019).getEffect(player.getSkillLevel(35121019)).applyTo(player);

						ArrayList<Triple<Integer, Integer, Integer>> mobList = new ArrayList<Triple<Integer, Integer, Integer>>();
						mobList.add(new Triple<Integer, Integer, Integer>(0, 0, 0));
						int dif = (attack.position.x - monster.getPosition().x);
						dif = (dif < 0 ? -dif : dif);
						player.getClient().getSession().writeAndFlush((Object) CField.bonusAttackRequest(35121019,
								mobList, true, 0, (dif * 3), monster.getPosition().x, monster.getPosition().y));
					}
				}

				if (attack.skill == 35141001) { // 매시브 파이어 : IRON-B 구현
					if (monster != null) {
						// SkillFactory.getSkill(35141002).getEffect(player.getSkillLevel(35141002)).applyTo(player);

						ArrayList<Triple<Integer, Integer, Integer>> mobList = new ArrayList<Triple<Integer, Integer, Integer>>();
						mobList.add(new Triple<Integer, Integer, Integer>(0, 0, 0));
						int dif = (attack.position.x - monster.getPosition().x);
						dif = (dif < 0 ? -dif : dif);
						player.getClient().getSession().writeAndFlush((Object) CField.bonusAttackRequest(35141002,
								mobList, true, 0, dif, monster.getPosition().x, monster.getPosition().y));
					}
				}

				if (player.getSkillLevel(155141000) > 0) { // 깨어난 심연 구현
					Skill skill = SkillFactory.getSkill(155141001);
					if (skill.getSkillList().contains(attack.skill)) {
						if (player.hexaMasterySkillStack > 0) {
							player.hexaMasterySkillStack--;

							EnumMap<SecondaryStat, Pair<Integer, Integer>> statups = new EnumMap<SecondaryStat, Pair<Integer, Integer>>(
									SecondaryStat.class);
							statups.put(SecondaryStat.Unk_771,
									new Pair<Integer, Integer>(player.hexaMasterySkillStack, 0));
							SecondaryStatEffect eff = SkillFactory.getSkill(155141001)
									.getEffect(player.getSkillLevel(155141000));
							player.getClient().getSession()
									.writeAndFlush(CWvsContext.BuffPacket.giveBuff(statups, eff, player));

							ArrayList<MapleMonster> monsters = new ArrayList<>();

							monsters.add(monster);

							player.getClient().getSession()
									.writeAndFlush(CField.bonusAttackRequest(155141001, monsters, true, attack.skill));
						}
					}
				}

				if (attack.skill == 5121013 || attack.skill == 5221013 || attack.skill == 400051040) {
					if (player.getSkillLevel(5121013) > 0 && attack.skill == 5121013
							&& player.getSkillLevel(400051040) > 0 && player.getCooldownLimit(400051040) <= 8000L) {
						player.getClient().getSession().writeAndFlush((Object) CField.skillCooldown(400051040, 8000));
						player.addCooldown(400051040, System.currentTimeMillis(), 8000L);
					} else if (player.getSkillLevel(5221013) > 0 && attack.skill == 5221013) {
						if (player.getSkillLevel(400051040) > 0 && player.getCooldownLimit(400051040) <= 8000L) {
							player.getClient().getSession()
									.writeAndFlush((Object) CField.skillCooldown(400051040, 8000));
							player.addCooldown(400051040, System.currentTimeMillis(), 8000L);
						}
						int[] array2;
						int[] reduceSkills = array2 = new int[] { 5210015, 5210016, 5210017, 5210018, 5220014, 5211007,
								5221022, 5220023, 5220024, 5220025, 5241002 }; // 배틀쉽 봄버 VI 구현
						for (final int reduceSkill : array2) {
							if (!player.skillisCooling((int) reduceSkill)) {
								continue;
							}
							player.changeCooldown((int) reduceSkill,
									(int) (-(player.getCooldownLimit((int) reduceSkill) / 2L)));
						}
					} else if (player.getSkillLevel(400051040) > 0 && attack.skill == 400051040
							&& player.getSkillLevel(5121013) > 0 && player.getCooldownLimit(5121013) <= 8000L) {
						player.getClient().getSession().writeAndFlush((Object) CField.skillCooldown(5121013, 8000));
						player.addCooldown(5121013, System.currentTimeMillis(), 8000L);
					} else if (player.getSkillLevel(400051040) > 0 && attack.skill == 400051040
							&& player.getSkillLevel(5221013) > 0 && player.getCooldownLimit(5221013) <= 8000L) {
						player.getClient().getSession().writeAndFlush((Object) CField.skillCooldown(5221013, 8000));
						player.addCooldown(5221013, System.currentTimeMillis(), 8000L);
					}
				}

				if (!finalMobList.isEmpty()) {
					player.getClient().getSession().writeAndFlush(
							(Object) CField.bonusAttackRequest(33000036, finalMobList, false, 0, new int[0]));
				}

				if (totDamage > 0L) {
					Integer[] throw_skillList;
					if (player.getMapId() == 993000500) {
						player.setFWolfDamage(player.getFWolfDamage() + totDamage);
						player.setFWolfAttackCount(player.getFWolfAttackCount() + 1);
						player.dropMessageGM(5, "total damage : " + player.getFWolfDamage());
					}
					if (!GameConstants.isKaiser(player.getJob()) && monster != null) {
						DamageParse.WFinalAttackRequest(player, attack.skill, monster);
					}
					if (attack.skill == 5321013) {// [othello] 공격 스킬 사용시 다른 스킬 쿨타임 감소.
						if (player.skillisCooling(5311004)) {
							player.changeCooldown(5311004, (int) (-(player.getCooldownLimit(5311004) / 2L)));
						}
						if (player.skillisCooling(5311005)) {
							player.changeCooldown(5311005, (int) (-(player.getCooldownLimit(5311005) / 2L)));
						}
						if (player.skillisCooling(5320007)) {
							player.changeCooldown(5320007, (int) (-(player.getCooldownLimit(5320007) / 2L)));
						}
					}
					// 잔영의 시
					if (player.getBuffedValue(400031020)) {
						if (SkillFactory.getSkill(400031020).getSkillList().contains(attack.skill)) {
							player.getClient().send(CField.InHumanAttackTime(player.getId(), true, 30000));
						}
					} else if (player.getSkillLevel(400031020) > 0) {
						if (SkillFactory.getSkill(400031021).getSkillList().contains(attack.skill)) {
							if (player.getCooldownLimit(400031020) > 0) {
								player.setVerseOfRelicsCount(player.getVerseOfRelicsCount() + 1);
								SecondaryStatEffect att = SkillFactory.getSkill(400031021)
										.getEffect(player.getSkillLevel(400031020));
								if (System.currentTimeMillis()
										- player.lastVerseOfRelicsTime >= (att.getSubTime() / 100)
										&& player.getVerseOfRelicsCount() >= 10) {
									player.lastVerseOfRelicsTime = System.currentTimeMillis();
									player.setVerseOfRelicsCount(0);
									att.applyTo(player, false, 1000);
								}
							}
						}
					}
					// 엘리시움 상태에서 크로스 더 스틱스으로 공격시 균열 생김.
					if (attack.skill == 400011056 && player.getBuffedValue(SecondaryStat.Ellision) != null
							&& player.getSkillCustomValue(400011065) == null) {
						MapleSummon ellision = player.getSummon(400011065);
						if (ellision == null) {
							boolean rltype = (attack.facingleft >>> 4 & 0xF) == 8;
							MapleSummon summon4 = new MapleSummon(player, 400011065, attack.position,
									SummonMovementType.STATIONARY, (byte) (rltype ? 1 : 0), effect.getDuration());
							player.getMap().spawnSummon(summon4, effect.getDuration());
							player.addSummon(summon4);
						} else {
							ellision.setEnergy(ellision.getEnergy() + 1);

							if (ellision.getEnergy() % 5 == 0) {
								ellision.setEnergy(0);
								player.EllisionStack++;
								player.cooldownEllision = System.currentTimeMillis();
								player.getMap().broadcastMessage(CField.SummonPacket.transformSummon(ellision, 1));
							}
						}
					}

					if (GameConstants.isLuminous(player.getJob())) {
						if ((player.getBuffedValue(20040216) || player.getBuffedValue(20040219)
								|| player.getBuffedValue(20040220))
								&& (GameConstants.isLightSkills(attack.skill)
										|| (player.getBuffedValue(20040219) || player.getBuffedValue(20040220))
												&& (attack.skill == 27121303 || attack.skill == 27111303))) {
							player.addHP(player.getStat().getMaxHp() / 100L);
						}
						if (!(player.getBuffedValue(20040216) || player.getBuffedValue(20040217)
								|| player.getBuffedValue(20040219) || player.getBuffedValue(20040220))) {
							if (GameConstants.isLightSkills(attack.skill)) {
								player.setLuminusMorphUse(1);
								SkillFactory.getSkill(20040217).getEffect(1).applyTo(player, false);
								player.setLuminusMorph(false);
							} else if (GameConstants.isDarkSkills(attack.skill)) {
								player.setLuminusMorphUse(9999);
								SkillFactory.getSkill(20040216).getEffect(1).applyTo(player, false);
								player.setLuminusMorph(true);
							}
							player.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket
									.LuminusMorph(player.getLuminusMorphUse(), player.getLuminusMorph()));
						} else if (!player.getBuffedValue(20040219) && !player.getBuffedValue(20040220)) {
							if (player.getLuminusMorph()) {
								if (GameConstants.isLightSkills(attack.skill)) {
									if (player.getLuminusMorphUse()
											- GameConstants.isLightSkillsGaugeCheck(attack.skill) <= 0) {
										if (player.getSkillLevel(20040219) > 0) {
											player.cancelEffectFromBuffStat(SecondaryStat.Larkness);
											SkillFactory.getSkill(20040219).getEffect(1).applyTo(player, false);
										} else {
											player.cancelEffectFromBuffStat(SecondaryStat.Larkness);
											player.setLuminusMorph(false);
											SkillFactory.getSkill(20040217).getEffect(1).applyTo(player, false);
										}
									} else {
										player.setLuminusMorphUse(player.getLuminusMorphUse()
												- GameConstants.isLightSkillsGaugeCheck(attack.skill));
									}
									if (!player.getBuffedValue(20040219) && !player.getBuffedValue(20040220)
											&& player.getLuminusMorph()) {
										player.cancelEffectFromBuffStat(SecondaryStat.Larkness);
										SkillFactory.getSkill(20040216).getEffect(1).applyTo(player, false);
									}
								}
							} else if (GameConstants.isDarkSkills(attack.skill)) {
								if (player.getLuminusMorphUse()
										+ GameConstants.isDarkSkillsGaugeCheck(player, attack.skill) >= 10000) {
									if (player.getSkillLevel(20040219) > 0) {
										player.cancelEffectFromBuffStat(SecondaryStat.Larkness);
										SkillFactory.getSkill(20040220).getEffect(1).applyTo(player, false);
									} else {
										player.cancelEffectFromBuffStat(SecondaryStat.Larkness);
										player.setLuminusMorph(true);
										SkillFactory.getSkill(20040216).getEffect(1).applyTo(player, false);
									}
								} else {
									player.setLuminusMorphUse(player.getLuminusMorphUse()
											+ GameConstants.isDarkSkillsGaugeCheck(player, attack.skill));
								}
								if (!(player.getBuffedValue(20040219) || player.getBuffedValue(20040220)
										|| player.getLuminusMorph())) {
									player.cancelEffectFromBuffStat(SecondaryStat.Larkness);
									SkillFactory.getSkill(20040217).getEffect(1).applyTo(player, false);
								}
							}
							player.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket
									.LuminusMorph(player.getLuminusMorphUse(), player.getLuminusMorph()));
						}
					}
					if (attack.skill == 27121303 && player.getSkillLevel(400021071) > 0) {
						boolean give = false;
						if (player.getPerfusion() < SkillFactory.getSkill(400021071)
								.getEffect(player.getSkillLevel(400021071)).getX() - 1) {
							give = true;
						} else if (player
								.getPerfusion() >= SkillFactory.getSkill(400021071)
										.getEffect(player.getSkillLevel(400021071)).getX() - 1
								&& player.skillisCooling(400021071)) {
							give = true;
						}
						if (give) {
							SkillFactory.getSkill(400021071).getEffect(player.getSkillLevel(400021071)).applyTo(player,
									false);
						}
					}
					if (player.getBuffedValue(32101009) && player.getSkillCustomValue(32111119) == null
							&& player.getId() == player.getBuffedOwner(32101009)) {
						player.addHP(totDamage / 100L * (long) player.getBuffedEffect(32101009).getX());
						player.getClient().getSession()
								.writeAndFlush((Object) CField.EffectPacket.showEffect(player, 0, 32101009, 10, 0, 0,
										(byte) (player.isFacingLeft() ? 1 : 0), true, player.getTruePosition(), null,
										null));
						player.getMap().broadcastMessage(player,
								CField.EffectPacket.showEffect(player, 0, 32101009, 10, 0, 0,
										(byte) (player.isFacingLeft() ? 1 : 0), false, player.getTruePosition(), null,
										null),
								false);
						player.setSkillCustomInfo(32111119, 0L, 5000L);
						if (player.getParty() != null) {
							for (MaplePartyCharacter pc : player.getParty().getMembers()) {
								MapleCharacter chr;
								if (pc.getId() == player.getId() || !pc.isOnline()
										|| (chr = player.getClient().getChannelServer().getPlayerStorage()
												.getCharacterById(pc.getId())) == null
										|| !chr.getBuffedValue(32101009) || chr.getId() == player.getId()) {
									continue;
								}
								chr.addHP(totDamage / 100L * (long) player.getBuffedEffect(32101009).getX());
								if (chr.getDisease(SecondaryStat.GiveMeHeal) != null) {
									chr.cancelDisease(SecondaryStat.GiveMeHeal);
								}
								chr.getClient().getSession()
										.writeAndFlush((Object) CField.EffectPacket.showEffect(chr, 0, 32101009, 10, 0,
												0, (byte) (chr.isFacingLeft() ? 1 : 0), true, chr.getTruePosition(),
												null, null));
								chr.getMap().broadcastMessage(chr,
										CField.EffectPacket.showEffect(chr, 0, 32101009, 10, 0, 0,
												(byte) (chr.isFacingLeft() ? 1 : 0), false, chr.getTruePosition(), null,
												null),
										false);
							}
						}
					}
					if (player.getBuffedValue(31121002)
							&& System.currentTimeMillis()
									- player.lastVamTime >= (long) player.getBuffedEffect(31121002).getY()
							&& player.getBuffedEffect(SecondaryStat.DebuffIncHp) == null) {
						player.lastVamTime = System.currentTimeMillis();
						player.addHP(Math.min((long) player.getBuffedEffect(31121002).getW(),
								totDamage * (long) player.getBuffedEffect(31121002).getX() / 100L));
						if (player.getParty() != null) {
							for (MaplePartyCharacter pc : player.getParty().getMembers()) {
								MapleCharacter chr;
								if (pc.getId() == player.getId() || !pc.isOnline()
										|| (chr = player.getClient().getChannelServer().getPlayerStorage()
												.getCharacterById(pc.getId())) == null
										|| !chr.isAlive() || chr.getBuffedEffect(SecondaryStat.DebuffIncHp) != null) {
									continue;
								}
								chr.addHP(totDamage * (long) player.getBuffedEffect(31121002).getX() / 100L);
							}
						}
					}
					if (attack.skill == 400011131) {
						player.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(400011131,
								Arrays.asList(new RangeAttack(400011132, attack.attackPosition2, 0, 0, 1))));
					}
					boolean isHexa = (player.getSkillLevel(65141001) > 0);
					if (monster != null && player.getBuffedValue(isHexa ? 65141002 : 65121011)
							&& attack.skill != 65120011 && attack.skill != 65111007 && attack.skill != 65141003
							&& attack.skill != 65141004 && attack.skill != 65141502 && attack.skill != 60011216
							&& attack.skill < 400000000) {
						int prop = player.getBuffedEffect(isHexa ? 65141002 : 65121011).getProp();
						if (player.getBuffedEffect(SecondaryStat.SoulExalt) != null) {
							prop += player.getBuffedValue(SecondaryStat.SoulExalt).intValue();
						}
						boolean isHexaBuff = player.getBuffedValue(65141501);
						if (Randomizer.isSuccess(prop)) {
							for (int i = 0; i < 2; ++i) {
								MapleAtom atom = new MapleAtom(false, player.getId(), isHexaBuff ? 93 : 25, true,
										isHexa ? 65141004 : 65120011, player.getTruePosition().x,
										player.getTruePosition().y);
								ArrayList<Integer> monsters = new ArrayList<Integer>();
								monsters.add(0);
								atom.addForceAtom(new ForceAtom(1, Randomizer.rand(15, 16), Randomizer.rand(27, 34),
										Randomizer.rand(31, 36), 0));
								atom.setDwFirstTargetId(monster.getObjectId());
								atom.setDwTargets(monsters);
								player.getMap().spawnMapleAtom(atom);
							}
						}
					}
					if (Arrays.asList(throw_skillList = new Integer[] { 4121013, 4121017, 4121052, 4001344, 4101008,
							4111010, 4111015 }).contains(attack.skill)) {
						if (player.getBuffedEffect(SecondaryStat.ThrowBlasting) != null && attack.skill != 400041061
								&& attack.skill != 400041079) {
							SecondaryStatEffect throw_eff = player.getBuffedEffect(SecondaryStat.ThrowBlasting);
							int consume = Randomizer.rand(throw_eff.getS(), throw_eff.getW());
							player.throwBlasting -= consume;
							player.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(400041061,
									Arrays.asList(new RangeAttack(400041079, attack.attackPosition2, 0, 0, 3))));
							if (player.throwBlasting <= 0) {
								player.cancelEffectFromBuffStat(SecondaryStat.ThrowBlasting);
							} else {
								HashMap<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
								statups.put(SecondaryStat.ThrowBlasting, new Pair<Integer, Integer>(
										player.throwBlasting,
										(int) player.getBuffLimit(player.getBuffSource(SecondaryStat.ThrowBlasting))));
								player.getClient().getSession()
										.writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups,
												player.getBuffedEffect(SecondaryStat.ThrowBlasting), player));
							}
						} else if (player.getSkillLevel(400041061) > 0 && attack.skill != 400041061
								&& attack.skill != 400041062) {
							SecondaryStatEffect throw_eff = SkillFactory.getSkill(400041061)
									.getEffect(player.getSkillLevel(400041061));
							if (System.currentTimeMillis()
									- player.lastThrowBlastingTime >= (long) (throw_eff.getU() * 1000)) {
								player.lastThrowBlastingTime = System.currentTimeMillis();
								player.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(400041061,
										Arrays.asList(new RangeAttack(400041079, attack.attackPosition2, 0, 0, 3))));
							}
						}
					}
				}
				if (player.getBuffSource(SecondaryStat.DrainHp) == 20031210) {
					player.addHP(totDamage * (long) player.getBuffedValue(SecondaryStat.DrainHp).intValue() / 100L);
				}
				if (player.getSkillLevel(1200014) > 0) {
					player.elementalChargeHandler(attack.skill, 1);
				}

				if (player.isAutoSkill(400051044)) { // [othello] 뇌신창격 오토모드 이펙트 처리
					SecondaryStatEffect effects = player.getBuffedEffect(400051044);
					if (player.striker3rdStack >= 8 && !player.skillisCooling(400051044)) {
						ArrayList<RangeAttack> skills = new ArrayList<RangeAttack>();
						skills.add(
								new RangeAttack(400051044, player.getPosition(), player.isFacingLeft() ? -1 : 0, 0, 0));
						skills.add(
								new RangeAttack(400051045, player.getPosition(), player.isFacingLeft() ? -1 : 0, 0, 0));
						skills.add(
								new RangeAttack(400051045, player.getPosition(), player.isFacingLeft() ? -1 : 0, 0, 0));
						skills.add(
								new RangeAttack(400051045, player.getPosition(), player.isFacingLeft() ? -1 : 0, 0, 0));
						skills.add(
								new RangeAttack(400051045, player.getPosition(), player.isFacingLeft() ? -1 : 0, 0, 0));
						player.getClient().getSession()
								.writeAndFlush((Object) CField.EffectPacket.showEffect(player, 0, 400051096, 1, 0, 0,
										(byte) (player.isFacingLeft() ? 1 : 0), true, player.getTruePosition(), null,
										null));
						player.getMap().broadcastMessage(player,
								CField.EffectPacket.showEffect(player, 0, 400051096, 1, 0, 0,
										(byte) (player.isFacingLeft() ? 1 : 0), false, player.getTruePosition(), null,
										null),
								false);
						player.getClient().getSession().writeAndFlush(CField.rangeAttack(400051096, skills));
						player.setSkillCustomInfo(400051044, 0L, effects.getCooldown(player));
						player.striker3rdStack = 0;
						player.cancelEffect(effects);
					}
				}
				if (attack.skill == 155121306) {
					if (!player.getBuffedValue(155000007)) {
						SkillFactory.getSkill(155000007).getEffect(1).applyTo(player);
					}
					SkillFactory.getSkill(155121006).getEffect(attack.skilllevel).applyTo(player, monster.getPosition(),
							false);
				}
				// 921170011
				if (!GameConstants.사출기(attack.skill) || attack.targets > 0 && player.getMapId() == 921170011
						|| attack.targets > 0 && player.getMapId() == 921170004
						|| attack.targets > 0 && player.getMapId() == 921170012) {
					if (attack.targets > 0 && player.getKeyValue(99999, "tripling") > 0 && attack.skill != 1311020
							&& !GameConstants.isTryFling(attack.skill) && attack.skill != 400031031
							&& attack.skill != 400031001 && attack.skill != 13111020 && attack.skill != 13121054
							&& attack.skill != 13101022 && attack.skill != 13110022 && attack.skill != 13120003
							&& attack.skill != 13111020 && attack.skill != 400001018 && attack.skill != 500001001
							|| attack.targets > 0 && player.getMapId() == 921170012
							|| attack.targets > 0 && player.getMapId() == 921170011
							|| attack.targets > 0 && player.getMapId() == 921170004
							|| attack.targets > 0 && attack.skill != 1311020
									&& player.getBuffedEffect(SecondaryStat.TryflingWarm) != null
									&& !GameConstants.isTryFling(attack.skill) && attack.skill != 400031031
									&& attack.skill != 400031001 && attack.skill != 13111020 && attack.skill != 13121054
									&& attack.skill != 13101022 && attack.skill != 13110022 && attack.skill != 13120003
									&& attack.skill != 13111020 && attack.skill != 400001018 && attack.targets > 0) {
						int skillid = 0;
						if (player.getSkillLevel(13141001) > 0) { // 트라이플링 윔 VI 구현
							skillid = 13141001;
						} else if (player.getKeyValue(99999, "tripling") > 0 || player.getMapId() == 921170004
								|| player.getMapId() == 921170012 || player.getMapId() == 921170011
								|| player.getKeyValue(1234, "ww_whim") > 0
								|| player.getSkillLevel(SkillFactory.getSkill(13120003)) > 0) {
							skillid = 13120003;
							if (player.getSkillLevel(SkillFactory.getSkill(13120003)) < 30) {
								player.teachSkill(13120003, 30);
							}
						} else if (player.getSkillLevel(SkillFactory.getSkill(13110022)) > 0) {
							skillid = 13110022;
						} else if (player.getSkillLevel(SkillFactory.getSkill(13101022)) > 0) {
							skillid = 13100022;
						}
						if (player.isGM()) {
							// player.dropMessageGM(6, "발동1 : " + attack.skill);
						}

						if (skillid != 0) {
							Skill trskill = SkillFactory.getSkill(skillid);
							int enhanceChance = (skillid == 13100022 ? 5 : (skillid == 13110022 ? 10 : 20));
							if (Randomizer.rand(1,
									100) <= (skillid == 13100022 ? 5 : (skillid == 13110022 ? 10 : 20))) {
								int n = skillid == 13120003 ? 13120010
										: (skillid = skillid == 13110022 ? 13110027 : 13100027);
								if (player.getSkillLevel(skillid) <= 0) {
									player.changeSkillLevel(SkillFactory.getSkill(skillid),
											(byte) player.getSkillLevel(trskill), (byte) player.getSkillLevel(trskill));
								}
							}
							List<MapleMapObject> objs = player.getMap().getMapObjectsInRange(player.getTruePosition(),
									500000.0, Arrays.asList(new MapleMapObjectType[] { MapleMapObjectType.MONSTER }));
							SecondaryStatEffect eff = trskill.getEffect(player.getSkillLevel(skillid));
							int maxcount = eff.getX();
							if (objs.size() > 0) {
								// int trychance = trskill.getEffect(player.getSkillLevel(trskill)).getProp() +
								// (int) (player.getKeyValue(99999, "triplingBonus") > 0 ?
								// (player.getKeyValue(99999, "triplingBonus") * 10) : 0);
								int trychance = 100;
								if (player.getSkillLevel(13120044) > 0) {

									trychance += SkillFactory.getSkill(13120044).getEffect(1).getProp();
								}
								if (attack.skill == 400031004 || attack.skill == 400031003) {
									trychance /= 2;
								}

								if (Randomizer.isSuccess(trychance)) {
									MapleAtom atom = new MapleAtom(false, player.getId(), 7, true, skillid,
											player.getTruePosition().x, player.getTruePosition().y);
									ArrayList<Integer> monsters = new ArrayList<Integer>();
									for (int i = 0; i < Randomizer
											.rand(1 + (int) (player.getKeyValue(99999, "triplingBonus") > 0
													? (player.getKeyValue(99999, "triplingBonus") * 1)
													: 0), maxcount); ++i) {
										boolean upgrade = Randomizer.isSuccess(enhanceChance);
										monsters.add(objs.get(Randomizer.nextInt(objs.size())).getObjectId());

										if (skillid == 13141001) { // 트라이플링 윔 VI 구현
											atom.addForceAtom(
													new ForceAtom(upgrade ? 4 : 3, Randomizer.rand(41, 49),
															Randomizer.rand(4, 8),
															Randomizer.nextBoolean() ? Randomizer.rand(171, 174)
																	: Randomizer.rand(6, 9),
															(short) Randomizer.rand(42, 47)));
										} else {
											atom.addForceAtom(
													new ForceAtom(upgrade ? 2 : 1, Randomizer.rand(41, 49),
															Randomizer.rand(4, 8),
															Randomizer.nextBoolean() ? Randomizer.rand(171, 174)
																	: Randomizer.rand(6, 9),
															(short) Randomizer.rand(42, 47)));
										}
									}
									atom.setDwTargets(monsters);
									player.getMap().spawnMapleAtom(atom);
								}
							}
						}
					}
				}
				if (attack.skill == 400031032 || attack.skill == 400051042) {
					PlayerHandler.Vmatrixstackbuff(player.getClient(), true, null);
				}
				// othello 풀메이커 버프
				if (attack.skill == 400051075) {
					player.setSkillCustomInfo(400051074, player.getSkillCustomValue0(400051074) - 1L, 0L);
					if (attack.targets > 0) {
						int counta = 0;
						for (MapleMist m : player.getMap().getAllMistsThreadsafe()) {
							if (m.getOwnerId() != player.getId()) {
								continue;
							}
							++counta;
						}
						if (counta < 2) {
							final SecondaryStatEffect a5 = SkillFactory.getSkill(400051076)
									.getEffect(player.getSkillLevel(400051074));
							final Rectangle bounds = a5.calculateBoundingBox(
									new Point(attack.plusPosition2.x, attack.plusPosition2.y), player.isFacingLeft());
							final MapleMist mist3 = new MapleMist(bounds, player, a5, 20000, (byte) 0);
							mist3.setPosition(new Point(attack.plusPosition2.x, attack.plusPosition2.y));
							mist3.setDelay(0);
							player.getMap().spawnMist(mist3, false);
						}
					}
					if (player.getSkillCustomValue0(400051074) <= 0L) {
						player.getMap().removeMist(400051076);
						player.getMap().removeMist(400051076);
					}
					player.getClient().send(CField.fullMaker((int) player.getSkillCustomValue0(400051074),
							player.getSkillCustomValue0(400051074) <= 0L ? 0 : 60000));
				}
//                if (totDamage > 0L && player.getSkillLevel(3210013) > 0) {
//                    SecondaryStatEffect bar = SkillFactory.getSkill(3210013).getEffect(player.getSkillLevel(3210013));
//                    player.setBarrier((int) Math.min(player.getStat().getCurrentMaxHp() * (long) bar.getZ() / 100L, totDamage * (long) bar.getY() / 100L));
//                    bar.applyTo(player, false);
//                }
				if (totDamage > 0L && player.getBuffedValue(65101002)) {
					SecondaryStatEffect bar = SkillFactory.getSkill(65101002).getEffect(player.getSkillLevel(65101002));
					player.setBarrier(
							(int) Math.min(player.getStat().getCurrentMaxHp(), totDamage * (long) bar.getY() / 100L));
					player.setBarrier((int) Math.min(player.getStat().getCurrentMaxHp(), 99999L));
					bar.applyTo(player, false, (int) player.getBuffLimit(65101002));
				}
				if (GameConstants.isDemonSlash(attack.skill) && player.getSkillLevel(0x1DADAAD) > 0
						&& !player.getBuffedValue(0x1DADAAD)) {
					SkillFactory.getSkill(0x1DADAAD).getEffect(1).applyTo(player, false);
				}
				if (player.getBuffedValue(400031044) && player.getSkillCustomValue(400031044) == null
						&& monster != null) {
					player.setGraveTarget(player.getObjectId());
					for (int i = 0; i < Randomizer.rand(1, 4); ++i) {
						player.createSecondAtom(SkillFactory.getSkill(400031045).getSecondAtoms(),
								monster.getPosition());
					}
					player.setSkillCustomInfo(400031044, 0L,
							(int) (SkillFactory.getSkill(400031044).getEffect(player.getSkillLevel(400031044)).getT()
									* 1000.0));
				}
				if (GameConstants.isAran(player.getJob()) && attack.skill != 400011122
						&& player.getBuffedValue(400011121)
						&& System.currentTimeMillis() - player.lastBlizzardTempestTime >= 500L) {
					player.lastBlizzardTempestTime = System.currentTimeMillis();
					player.getClient().getSession().writeAndFlush((Object) CField.bonusAttackRequest(400011122,
							new ArrayList<Triple<Integer, Integer, Integer>>(), true, 0, new int[0]));
					ArrayList<Pair<Integer, Integer>> lists = new ArrayList<Pair<Integer, Integer>>() {
						{
							for (AttackPair pair : attack.allDamage) {
								MapleMonster mob = player.getMap().getMonsterByOid(pair.objectId);
								if (mob == null) {
									continue;
								}
								if (mob.blizzardTempest < 6) {
									++mob.blizzardTempest;
								}
								this.add(new Pair<Integer, Integer>(pair.objectId, mob.blizzardTempest));
							}
						}
					};
					player.getClient().getSession()
							.writeAndFlush((Object) CWvsContext.blizzardTempest((List<Pair<Integer, Integer>>) lists));
				}
				if (attack.skill == 4341002) {
					effect.applyTo(player, false);
				}
				if (attack.skill == 4341011 && player.getCooldownLimit(4341002) > 0L) {
					SecondaryStatEffect suddenRade = SkillFactory.getSkill(4341011).getEffect(attack.skilllevel);
					player.changeCooldown(4341002,
							(int) (-player.getCooldownLimit(4341002) * (long) suddenRade.getX() / 100L));
				}
				if (player.getBuffedValue(155101008) && attack.skill != 155121006 && attack.skill != 155121007
						&& attack.skill != 155100009 && attack.skill != 155001000 && attack.skill != 155121004
						&& attack.skill != 400051035 && attack.skill != 400051334
						&& player.getBuffedEffect(SecondaryStat.SpectorTransForm) != null) {
					int i;
					SecondaryStatEffect eff = SkillFactory.getSkill(155101008).getEffect(155101008);
					int max = eff.getZ();
					int count = player.getBuffedValue(400051036) ? player.getBuffedEffect(400051036).getX() : 0;
					ArrayList<Integer> monsters = new ArrayList<Integer>();
					MapleAtom atom = new MapleAtom(false, player.getId(), 47, true, 155100009, 0, 0);
					for (i = 0; i < attack.targets && monsters.size() < max; ++i) {
						monsters.add(0);
						atom.addForceAtom(
								new ForceAtom(0, 1, Randomizer.rand(5, 10), 270, (short) Randomizer.rand(75, 95)));
					}
					for (i = 0; i < count; ++i) {
						monsters.add(0);
						atom.addForceAtom(
								new ForceAtom(0, 1, Randomizer.rand(5, 10), 270, (short) Randomizer.rand(75, 95)));
					}
					atom.setDwTargets(monsters);
					player.getMap().spawnMapleAtom(atom);
				}

				if (player.getBuffedValue(400031030) && System.currentTimeMillis()
						- player.lastWindWallTime >= (long) (player.getBuffedEffect(400031030).getW2() * 1000)) {
					player.lastWindWallTime = System.currentTimeMillis();
					MapleAtom atom = new MapleAtom(false, player.getId(), 51, true, 400031031,
							player.getTruePosition().x, player.getTruePosition().y);
					for (int i = 0; i < player.getBuffedEffect(400031030).getQ2(); ++i) {
						atom.addForceAtom(new ForceAtom(Randomizer.nextBoolean() ? 1 : 3, Randomizer.rand(30, 60), 10,
								Randomizer.nextBoolean() ? Randomizer.rand(0, 5) : Randomizer.rand(180, 185), 0));
					}
					atom.setDwFirstTargetId(0);
					player.getMap().spawnMapleAtom(atom);
				}
				if (player.getSkillLevel(80002762) > 0
						&& (stst = SkillFactory.getSkill(80002762).getEffect(player.getSkillLevel(80002762)))
								.makeChanceResult()) {
					stst.applyTo(player, false);
				}
				if (attack.skill == 5221015 || attack.skill == 151121001) {
					player.cancelEffect(effect);
					SkillFactory.getSkill(attack.skill).getEffect(attack.skilllevel).applyTo(player, false);
				}
				if (player.getBuffedValue(SecondaryStat.Steal) != null && monster != null) {
					monster.handleSteal(player);
					if (Randomizer.isSuccess(10) && monster.getCustomValue0(4201017) == 0L) {
						monster.setCustomInfo(4201017, 1, 0);
						final Item toDrop = new Item(2431835, (short) 1, (short) 1, 0);
						player.getMap().spawnItemDrop(monster, player, toDrop, monster.getPosition(), true, true);
					}
				}
				if (player.getSkillLevel(4330007) > 0 && !GameConstants.is_forceAtom_attack_skill(attack.skill)
						&& (bytalsteal = SkillFactory.getSkill(4330007).getEffect(player.getSkillLevel(4330007)))
								.makeChanceResult()) {
					player.addHP(player.getStat().getCurrentMaxHp() / 100L * (long) bytalsteal.getX());
				}
				if (totDamage > 0L && player.getSkillLevel(4200013) > 0
						&& !GameConstants.is_forceAtom_attack_skill(attack.skill)) {
					SecondaryStatEffect criticalGrowing = SkillFactory.getSkill(4200013)
							.getEffect(player.getSkillLevel(4200013));
					if (player.getSkillLevel(4220015) > 0) {
						criticalGrowing = SkillFactory.getSkill(4220015).getEffect(player.getSkillLevel(4220015));
					}
					if (player.criticalGrowing == 100 && player.criticalDamageGrowing >= criticalGrowing.getQ()) {
						player.criticalGrowing = 0;
						player.criticalDamageGrowing = 0;
						player.setSkillCustomInfo(4220015, 0L, 4000L);
					} else if (player.criticalGrowing + player.getStat().critical_rate >= 100) {
						player.criticalGrowing = 100;
					} else {
						player.criticalGrowing += criticalGrowing.getX();
						player.criticalDamageGrowing = Math.min(player.criticalDamageGrowing + criticalGrowing.getW(),
								criticalGrowing.getQ());
						player.setSkillCustomInfo(4220015, 0L, 4000L);
					}
					criticalGrowing.applyTo(player, false, 0);
				}
				if (totDamage > 0L && player.getSkillLevel(5220055) > 0) {
					SecondaryStatEffect quickDraw = SkillFactory.getSkill(5220055)
							.getEffect(player.getSkillLevel(5220055));
					if (!player.getBuffedValue(5220055)) {
						if (quickDraw.makeChanceResult()) {
							quickDraw.applyTo(player, false);
						}
					} else if (player.getBuffedValue(SecondaryStat.QuickDraw) != null && attack.skill != 5220023
							&& attack.skill != 5220024 && attack.skill != 5220025 && attack.skill != 5220020
							&& attack.skill != 5221004 && attack.skill != 400051006) {
						player.cancelEffectFromBuffStat(SecondaryStat.QuickDraw, 5220055);
					}
				}
				if (player.getBuffedValue(15001022) && attack.skill != 15111022 && attack.skill != 15120003
						&& attack.skill != 400051016) {
					SecondaryStatValueHolder lightning;
					int[] skills = { 15000023, 15100025, 15110026, 15120008 };
					SecondaryStatEffect dkeffect = player.getBuffedEffect(15001022);
					int prop = dkeffect.getProp();
					int maxcount = dkeffect.getV();
					for (int skill : skills) {
						if (player.getSkillLevel(skill) <= 0) {
							continue;
						}
						prop += SkillFactory.getSkill(skill).getEffect(player.getSkillLevel(skill)).getProp();
						maxcount += SkillFactory.getSkill(skill).getEffect(player.getSkillLevel(skill)).getV();
					}
					if (Randomizer.nextInt(100) < prop && player.lightning < maxcount) {
						++player.lightning;

					}
					if (player.lightning < 0) {
						player.lightning = 0;
					}
					if ((lightning = player.checkBuffStatValueHolder(SecondaryStat.CygnusElementSkill)) != null) {
						lightning.effect.applyTo(player, false);
					}
				}
				if (attack.skill == 21000006 || attack.skill == 21000007 || attack.skill == 21001010) {
					if (player.getSkillLevel(21120021) > 0 && player.getSkillCustomValue(21120021) == null) {
						SkillFactory.getSkill(21120021).getEffect(player.getSkillLevel(21120021)).applyTo(player,
								false);
						player.setSkillCustomInfo(21120021, 0L, 3000L);
					} else if (player.getSkillLevel(21100015) > 0 && player.getSkillCustomValue(21120021) == null) {
						SkillFactory.getSkill(21100015).getEffect(player.getSkillLevel(21100015)).applyTo(player,
								false);
						player.setSkillCustomInfo(21120021, 0L, 3000L);
					}
				}
				if (attack.skill == 400011079) {
					player.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(400011079,
							Arrays.asList(new RangeAttack(400011081, player.getTruePosition(), 0, 0, 0))));
				}
				if (attack.skill == 400011080) {
					player.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(400011080,
							Arrays.asList(new RangeAttack(400011082, player.getTruePosition(), 0, 0, 0))));
				}
				if (player.getSkillLevel(400011134) > 0 && attack.targets > 0) {
					SecondaryStatEffect ego_weapon = SkillFactory.getSkill(400011134)
							.getEffect(player.getSkillLevel(400011134));
					if (player.getGender() == 0 && !player.getBuffedValue(400011134)
							&& player.getCooldownLimit(400011134) == 0L) {
						ego_weapon.applyTo(player, false);
					} else if (player.getGender() == 1 && player.getMap().getMist(player.getId(), 400011035) == null
							&& player.getCooldownLimit(400011135) == 0L && !player.skillisCooling(400011135)) {
						SecondaryStatEffect a = SkillFactory.getSkill(400011135)
								.getEffect(player.getSkillLevel(400011134));
						player.getClient().getSession()
								.writeAndFlush((Object) CField.EffectPacket.showEffect(player, 0, 400011135, 1, 0, 0,
										(byte) (player.isFacingLeft() ? 1 : 0), true, player.getTruePosition(), null,
										null));
						player.getMap().broadcastMessage(player,
								CField.EffectPacket.showEffect(player, 0, 400011135, 1, 0, 0,
										(byte) (player.isFacingLeft() ? 1 : 0), false, player.getTruePosition(), null,
										null),
								false);
						MapleMist newmist = new MapleMist(
								a.calculateBoundingBox(monster.getPosition(), player.isFacingLeft()), player, a, 2000,
								(byte) (player.isFacingLeft() ? 1 : 0));
						newmist.setPosition(monster.getPosition());
						player.getMap().spawnMist(newmist, false);
						newmist = new MapleMist(a.calculateBoundingBox(monster.getPosition(), player.isFacingLeft()),
								player, a, 2000, (byte) (player.isFacingLeft() ? 1 : 0));
						newmist.setPosition(monster.getPosition());
						player.getMap().spawnMist(newmist, false);
						newmist = new MapleMist(a.calculateBoundingBox(monster.getPosition(), player.isFacingLeft()),
								player, a, 2000, (byte) (player.isFacingLeft() ? 1 : 0));
						newmist.setPosition(monster.getPosition());
						player.getMap().spawnMist(newmist, false);
						player.addCooldown(400011135, System.currentTimeMillis(), SkillFactory.getSkill(400011134)
								.getEffect(player.getSkillLevel(400011134)).getCooldown(player));
						player.getClient().getSession()
								.writeAndFlush((Object) CField.skillCooldown(400011135, SkillFactory.getSkill(400011134)
										.getEffect(player.getSkillLevel(400011134)).getCooldown(player)));
					}
				}
				if (player.getBuffedEffect(SecondaryStat.CrystalGate) != null) {
					SecondaryStatEffect crystalGate = player.getBuffedEffect(SecondaryStat.CrystalGate);
					if ((double) (System.currentTimeMillis() - player.lastCrystalGateTime) >= crystalGate.getT()
							* 1000.0) {
						player.lastCrystalGateTime = System.currentTimeMillis();
						player.getClient().getSession().writeAndFlush((Object) CField.bonusAttackRequest(400021111,
								new ArrayList<Triple<Integer, Integer, Integer>>(), true, 0, new int[0]));
					}
				}
				if (!GameConstants.isKaiser(player.getJob())) {
					break block858;
				}
				if (monster == null || player.getBuffedValue(400011118) || !player.skillisCooling(400011118)
						|| player.getSkillCustomValue(400011118) != null) {
					break block859;
				}
				SecondaryStatEffect dragonBlaze = SkillFactory.getSkill(400011118)
						.getEffect(player.getSkillLevel(400011118));
				if (attack.skill == 400011119 || attack.skill == 400011120) {
					break block858;
				}
				ArrayList<SecondAtom> atoms = new ArrayList<SecondAtom>();
				atoms.add(new SecondAtom(13, player.getId(), monster.getId(), 360, 400011120, 10000, 18, 1,
						new Point((int) player.getTruePosition().getX(), (int) player.getTruePosition().getY() - 200),
						new ArrayList<Integer>()));
				atoms.add(new SecondAtom(13, player.getId(), monster.getId(), 360, 400011120, 10000, 18, 1,
						new Point((int) player.getTruePosition().getX() - 200,
								(int) player.getTruePosition().getY() - 100),
						new ArrayList<Integer>()));
				atoms.add(new SecondAtom(13, player.getId(), monster.getId(), 360, 400011120, 10000, 18, 1,
						new Point((int) player.getTruePosition().getX(), (int) player.getTruePosition().getY() + 200),
						new ArrayList<Integer>()));
				atoms.add(new SecondAtom(13, player.getId(), monster.getId(), 360, 400011120, 10000, 18, 1,
						new Point((int) player.getTruePosition().getX() + 200,
								(int) player.getTruePosition().getY() - 100),
						new ArrayList<Integer>()));
				atoms.add(new SecondAtom(13, player.getId(), monster.getId(), 360, 400011120, 10000, 18, 1,
						new Point((int) player.getTruePosition().getX() - 200,
								(int) player.getTruePosition().getY() + 100),
						new ArrayList<Integer>()));
				atoms.add(new SecondAtom(13, player.getId(), monster.getId(), 360, 400011120, 10000, 18, 1,
						new Point((int) player.getTruePosition().getX() + 200,
								(int) player.getTruePosition().getY() + 100),
						new ArrayList<Integer>()));
				player.setSkillCustomInfo(400011118, 0L, dragonBlaze.getV2() * 1000);
				player.getMap().spawnSecondAtom(player, atoms, 0);
				break block858;
			}
			if (attack.skill == 400011118) {
				player.getMap().spawnSecondAtom(player, Arrays.asList(new SecondAtom(11, player.getId(), 0, 400011119,
						5000, 0, -1, attack.position, new ArrayList<Integer>())), 0);
			} else if (attack.skill != 400011118 && !player.skillisCooling(400111119)) {
				for (SecondAtom at : player.getMap().getAllSecondAtomsThreadsafe()) {
					if (at.getSkillId() != 400011119) {
						continue;
					}
					player.addCooldown(400111119, System.currentTimeMillis(),
							(int) SkillFactory.getSkill(400011118).getEffect(player.getSkillLevel(400011118)).getT()
									* 1000);
					player.getClient().getSession().writeAndFlush((Object) CField.bonusAttackRequest(400011130,
							new ArrayList<Triple<Integer, Integer, Integer>>(), true, 0, new int[0]));
					break;
				}
			}
		}
		if (attack.skill >= 400051059 && attack.skill <= 400051067 && attack.skill != 400051065
				&& attack.skill != 400051067) {
			SkillFactory.getSkill(400051058).getEffect(player.getSkillLevel(400051058)).applyTo(player);
			if (attack.isLink || attack.skill == 400051066) {
				SkillFactory.getSkill(400051044).getEffect(player.getSkillLevel(400051044)).applyTo(player);
				List<MapleMapObject> objs = player.getMap().getMapObjectsInRange(player.getTruePosition(), 500000.0,
						Arrays.asList(new MapleMapObjectType[] { MapleMapObjectType.MONSTER }));
				if (objs.size() > 0) {
					ArrayList<Triple<Integer, Integer, Integer>> mobList = new ArrayList<Triple<Integer, Integer, Integer>>();
					for (int i = 1; i <= objs.size(); ++i) {
						mobList.add(new Triple<Integer, Integer, Integer>(
								objs.get(Randomizer.nextInt(objs.size())).getObjectId(), 134 + (i - 1) * 70, 0));
						if (i >= (player.getBuffedValue(400051058) ? effect.getW() : effect.getV2())) {
							break;
						}
					}
					player.getClient().getSession()
							.writeAndFlush((Object) CField.bonusAttackRequest(
									attack.skill == 400051066 ? 400051067 : 400051065, mobList, false, 0,
									player.getBuffedValue(400051058) ? 3 : 1, attack.position.x, attack.position.y,
									player.getBuffedValue(400051058) ? 445 : 565,
									player.getBuffedValue(400051058) ? 315 : 510));
				}
			}
		}

		if (attack.skill == 400041069) {
			ArrayList<RangeAttack> skills = new ArrayList<RangeAttack>();
			skills.add(new RangeAttack(400041070, attack.position, -1, 900, 1));
			skills.add(new RangeAttack(400041071, attack.position, -1, 1680, 1));
			skills.add(new RangeAttack(400041072, attack.position, -1, 2460, 1));
			skills.add(new RangeAttack(400041070, attack.position, -1, 3120, 1));
			skills.add(new RangeAttack(400041071, attack.position, -1, 3600, 1));
			skills.add(new RangeAttack(400041072, attack.position, -1, 3960, 1));
			skills.add(new RangeAttack(400041070, attack.position, -1, 4200, 1));
			skills.add(new RangeAttack(400041071, attack.position, -1, 4440, 1));
			skills.add(new RangeAttack(400041072, attack.position, -1, 4620, 1));
			skills.add(new RangeAttack(400041070, attack.position, -1, 4800, 1));
			skills.add(new RangeAttack(400041071, attack.position, -1, 4920, 1));
			skills.add(new RangeAttack(400041073, attack.position, -1, 5370, 1));
			player.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(attack.skill, skills));
		}

		if (player.getSkillLevel(400011048) > 0 && !player.skillisCooling(400011048) && attack.targets > 0
				&& SkillFactory.getSkill(400011048).getSkillList().contains(attack.skill)) {
			SecondaryStatEffect flear = SkillFactory.getSkill(400011048).getEffect(player.getSkillLevel(400011048));
			ArrayList<RangeAttack> skills = new ArrayList<RangeAttack>();
			int attackid = 400011048;
			player.getClient().getSession().writeAndFlush((Object) CField.EffectPacket.showEffect(player, 0, attackid,
					1, 0, 0, (byte) (player.isFacingLeft() ? 1 : 0), true, player.getPosition(), null, null));
			player.getMap().broadcastMessage(player, CField.EffectPacket.showEffect(player, 0, attackid, 1, 0, 0,
					(byte) (player.isFacingLeft() ? 1 : 0), false, player.getPosition(), null, null), false);
			player.getClient().getSession().writeAndFlush(
					CField.rangeAttack(400011048, Arrays.asList(new RangeAttack(400011049, attack.position, 1, 0, 1))));

			int ViperSkilltype = ((attack.facingleft >>> 4 & 0xF) == 0) ? 0 : 1;
			player.getClient().getSession().writeAndFlush(CField.rangeAttack(attackid,
					Arrays.asList(new RangeAttack(attackid, attack.position, ViperSkilltype, 0, 1))));

			SkillFactory.getSkill(400011048).getEffect(player.getSkillLevel(400011048)).applyTo(player,
					player.getPosition());
			player.addCooldown(400011048, System.currentTimeMillis(), flear.getCooldown(player));
			player.getClient().getSession()
					.writeAndFlush((Object) CField.skillCooldown(400011048, flear.getCooldown(player)));

		}

		if (player.getSkillLevel(400041075) > 0 && player.getCooldownLimit(400041075) == 0L
				&& (attack.skill == 4341004 || attack.skill == 4341009 || attack.skill == 4361000)) { // 사라졌?
			SecondaryStatEffect hunted_edge = SkillFactory.getSkill(400041075)
					.getEffect(player.getSkillLevel(400041075));
			if (attack.skill == 4341004) {
				player.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(attack.skill,
						Arrays.asList(new RangeAttack(400041076, attack.position, 0, 0, 6))));
			} else if (attack.skill == 4341009 || attack.skill == 4361000) {
				ArrayList<RangeAttack> skills = new ArrayList<RangeAttack>();
				skills.add(new RangeAttack(400041078, attack.position, 0, 0, 6));
				player.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(attack.skill, skills));
			}
			player.getClient().send(CField.skillCooldown(hunted_edge.getSourceId(), hunted_edge.getCooldown(player)));
			player.addCooldown(hunted_edge.getSourceId(), System.currentTimeMillis(), hunted_edge.getCooldown(player));
		}
		if (player.getSkillLevel(23110004) > 0 && attack.isLink && attack.charge == 0
				&& !player.getBuffedValue(400031017)) {
			int[] linkCooldownSkills = { 23121052, 400031007, 23111002, 23121002 };
			for (int ck : linkCooldownSkills) {
				if (!player.skillisCooling(ck)) {
					continue;
				}
				if (ck == 400031007) {
					player.setSkillCustomInfo(400031007, 0L, player.getSkillCustomTime(400031007) - 1000);
					continue;
				}
				player.changeCooldown(ck, -1000);
			}
			if (!player.getBuffedValue(23110004)) {
				player.removeSkillCustomInfo(23110005);
			}
			if (player.getSkillCustomValue0(23110005) < 10L) {
				player.setSkillCustomInfo(23110005, player.getSkillCustomValue0(23110005) + 1L, 0L);
			}
			SkillFactory.getSkill(23110004).getEffect(player.getSkillLevel(23110004)).applyTo(player);
		}
		if (attack.skill == 23121000 && player.getBuffedValue(23110004) && !player.getBuffedValue(400031017)) {
			SkillFactory.getSkill(23110004).getEffect(player.getSkillLevel(23110004)).applyTo(player, false);
		}
		if (totDamage > 0L && attack.isLink && player.getSkillLevel(400051044) > 0
				&& (attack.skill < 400051059 || attack.skill > 400051067) && attack.skill != 0
				|| attack.skill == 400051096) {
			if (player.getBuffedValue(400051044)) {
				if (player.getBuffedValue(SecondaryStat.Striker3rd) <= 8) {
					if (player.striker3rdStack >= 8 && attack.skill == 400051096) {
						player.striker3rdStack = 0;
					}
					SkillFactory.getSkill(400051044).getEffect(player.getSkillLevel(400051044)).applyTo(player, false);
				}
			} else {
				SkillFactory.getSkill(400051044).getEffect(player.getSkillLevel(400051044)).applyTo(player, false);
			}
		}
		if (GameConstants.isAngelicBuster(player.getJob()) && totDamage > 0L) {
			// player.Recharge(attack.skill);
			if (attack.skill == 65121007 || attack.skill == 65121008) {
				SkillFactory.getSkill(65121101).getEffect(player.getSkillLevel(65121101)).applyTo(player);
			}
		}
		if (player.getBuffedValue(400031006) && attack.skill == 400031010) {
			--player.trueSniping;
			if (player.trueSniping <= 0) {
				player.cancelEffectFromBuffStat(SecondaryStat.TrueSniping);
			} else {
				player.getBuffedEffect(400031006).applyTo(player, false);
			}
		}
		if (attack.skill == 4331003 && (hpMob <= 0L || totDamageToOneMonster < hpMob)) {
			return;
		}
		if (hpMob > 0L && totDamageToOneMonster > 0L) {
			player.afterAttack(attack);
		}
		if (player.getBuffedValue(400031007) && totDamageToOneMonster > 0L
				&& player.getSkillCustomValue(400031007) == null) {
			player.getClient().getSession().writeAndFlush((Object) CField.bonusAttackRequest(400031011,
					new ArrayList<Triple<Integer, Integer, Integer>>(), true, 0, new int[0]));
			player.setSkillCustomInfo(400031007, 0L, player.getBuffedEffect(400031007).getS2() * 1000);
		}
		if ((attack.skill == 400011056 || attack.skill == 11121055 || attack.skill == 400011056)
				&& player.getSkillLevel(400011048) > 0 && player.getCooldownLimit(400011048) > 0L) {
			SecondaryStatEffect flare_slash = SkillFactory.getSkill(400011048)
					.getEffect(player.getSkillLevel(400011048));
			int count = attack.hits;
			if (player.getBuffedEffect(SecondaryStat.Buckshot) != null) {
				count /= 2;
			}
			player.changeCooldown(400011048, -(flare_slash.getZ() / count));
		}

		if (player.getBuffedValue(400051010) && !GameConstants.is_forceAtom_attack_skill(attack.skill)
				&& player.getSkillCustomValue(400051010) == null && attack.skill != 25121055
				&& attack.skill != 25111012) {
			Skill skill = SkillFactory.getSkill(400051010);
			SecondaryStatEffect eff = null;
			ArrayList<Pair<Integer, Integer>> skillList = new ArrayList<Pair<Integer, Integer>>();
			List<RandomSkillEntry> rse = skill.getRSE();
			for (RandomSkillEntry info : rse) {
				if (!Randomizer.isSuccess(info.getProb())) {
					continue;
				}
				if (info.getSkillList().size() > 0) {
					skillList.addAll(info.getSkillList());
				} else {
					skillList.add(new Pair<Integer, Integer>(info.getSkillId(), 0));
				}
				if (info.getSkillId() == 25121055 || info.getSkillId() == 25111012) {
					int skilllevel;
					int n = skilllevel = info.getSkillId() == 25121055 ? 25121030 : 25111012;
					if (player.getSkillLevel(25121055) < 1) {
						player.changeSkillLevel(25121055, (byte) 1, (byte) 1);
					}
					eff = SkillFactory.getSkill(info.getSkillId()).getEffect(skilllevel);
					MapleMist mist = new MapleMist(
							eff.calculateBoundingBox(player.getPosition(), player.isFacingLeft()), player, eff, 3000,
							(byte) (player.isFacingLeft() ? 1 : 0));
					mist.setPosition(player.getPosition());
					player.getMap().spawnMist(mist, false);
				} else {
					player.getClient().getSession().writeAndFlush((Object) CField.SpiritFlow(skillList));
				}
				player.setSkillCustomInfo(400051010, 0L, player.getBuffedEffect(400051010).getX() * 1000);
				break;
			}
		}
		if (GameConstants.isNightWalker(player.getJob())) {
			if (!GameConstants.is_forceAtom_attack_skill(attack.skill)
					&& player.getBuffedEffect(SecondaryStat.DarkSight) != null && attack.summonattack == 0) {
				player.cancelEffectFromBuffStat(SecondaryStat.DarkSight);
			}
			// [othello] 베일 오브 쉐도우 fix
		} else if (!(GameConstants.isKadena(player.getJob()) || GameConstants.isHoyeong(player.getJob())
				|| GameConstants.isKhali(player.getJob()) || player.getBuffedValue(SecondaryStat.DarkSight) == null
				|| player.getBuffedValue(400001023))) {
			int prop = 0;
			if (player.getSkillLevel(4210015) > 0) {
				prop = SkillFactory.getSkill(4210015).getEffect(player.getSkillLevel(4210015)).getProp();
			}
			if (player.getSkillLevel(0x421211) > 0) {
				prop = SkillFactory.getSkill(0x421211).getEffect(player.getSkillLevel(0x421211)).getProp();
			}
			for (MapleMist mist : player.getMap().getAllMistsThreadsafe()) {
				if (mist.getOwnerId() != player.getId() || mist.getSource().getSourceId() != 4221006
						|| !mist.getBox().contains(player.getTruePosition())) {
					continue;
				}
				prop = 100;
				break;
			}
			if (player.getSkillCustomValue0(4221052) != 0L
					&& (long) player.getPosition().x <= player.getSkillCustomValue0(4221052) + 300L
					&& (long) player.getPosition().x >= player.getSkillCustomValue0(4221052) - 300L) {
				prop = 100;
			}
			if (!Randomizer.isSuccess(prop)) {
				player.cancelEffectFromBuffStat(SecondaryStat.DarkSight);
			}
		} else if (!(player.getBuffedValue(SecondaryStat.DarkSight) == null || player.getBuffedValue(400001023)
				|| GameConstants.is_forceAtom_attack_skill(attack.skill)
				|| GameConstants.isDarkSightDispelSkill(attack.skill) || attack.skill >= 400000000)) {
			player.cancelEffectFromBuffStat(SecondaryStat.DarkSight);
		}
		if (player.getSkillLevel(101110205) > 0 && player.getGender() == 0 && totDamageToOneMonster > 0L
				&& (combatRecovery = SkillFactory.getSkill(101110205).getEffect(player.getSkillLevel(101110205)))
						.makeChanceResult()) {
			player.addMP(combatRecovery.getZ());
		}
		if (totDamage > 0L && player.getBuffedValue(400021073) && (summon = player.getSummon(400021073)) != null
				&& summon.getEnergy() < 22) {
			switch (attack.skill) {
			case 22110022:
			case 22110023:
			case 22111012:
			case 22170060:
			case 22170070:
			case 400021012:
			case 400021014:
			case 400021015: {
				MapleAtom atom = new MapleAtom(true, summon.getObjectId(), 29, true, 400021073,
						summon.getTruePosition().x, summon.getTruePosition().y);
				atom.setDwUserOwner(summon.getOwner().getId());
				atom.setDwFirstTargetId(0);
				ForceAtom aatom = new ForceAtom(5, 37, Randomizer.rand(5, 10), 62, 0);
				atom.addForceAtom(aatom);
				player.getMap().spawnMapleAtom(atom);
				summon.setEnergy(
						Math.min(SkillFactory.getSkill(400021073).getEffect(player.getSkillLevel(400021073)).getX(),
								summon.getEnergy() + 1));
				player.getClient().getSession()
						.writeAndFlush((Object) CField.SummonPacket.ElementalRadiance(summon, 2));
				player.getClient().getSession().writeAndFlush((Object) CField.SummonPacket.specialSummon(summon, 2));
				if (summon.getEnergy() < SkillFactory.getSkill(400021073).getEffect(player.getSkillLevel(400021073))
						.getX()) {
					break;
				}
				player.getClient().getSession().writeAndFlush((Object) CField.SummonPacket.damageSummon(summon));
				break;
			}
			case 22110014:
			case 22110024:
			case 22110025:
			case 22111011:
			case 22140014:
			case 22140015:
			case 22140023:
			case 22140024:
			case 22141011:
			case 22170064:
			case 22170065:
			case 22170066:
			case 22170067:
			case 22170093:
			case 22170094:
			case 22171063:
			case 22171083:
			case 22171095:
			case 400021013: {
				if (summon.getMagicSkills().contains(attack.skill)) {
					break;
				}
				summon.getMagicSkills().add(attack.skill);
				summon.setEnergy(Math.min(22, summon.getEnergy() + 3));
				MapleAtom atom = new MapleAtom(true, summon.getObjectId(), 29, true, 400021073,
						summon.getTruePosition().x, summon.getTruePosition().y);
				atom.setDwUserOwner(summon.getOwner().getId());
				ArrayList<Integer> monsters = new ArrayList<Integer>();
				monsters.add(0);
				atom.addForceAtom(new ForceAtom(5, 37, Randomizer.rand(5, 10), 62, 0));
				atom.setDwTargets(monsters);
				player.getMap().spawnMapleAtom(atom);
				player.getClient().getSession()
						.writeAndFlush((Object) CField.SummonPacket.ElementalRadiance(summon, 2));
				player.getClient().getSession().writeAndFlush((Object) CField.SummonPacket.specialSummon(summon, 2));
				if (summon.getEnergy() < 22) {
					break;
				}
				player.getClient().getSession().writeAndFlush((Object) CField.SummonPacket.damageSummon(summon));
				break;
			}
			}
		}
		if (player.getBuffedEffect(SecondaryStat.ComboInstict) != null
				&& (attack.skill == 1120017 || attack.skill == 1121008 || attack.skill == 1141001)) {// 修改記錄？？
			player.getClient().getSession().writeAndFlush((Object) CField.bonusAttackRequest(400011074,
					new ArrayList<Triple<Integer, Integer, Integer>>(), true, 0, new int[0]));
			player.getClient().getSession().writeAndFlush((Object) CField.bonusAttackRequest(400011075,
					new ArrayList<Triple<Integer, Integer, Integer>>(), true, 0, new int[0]));
			player.getClient().getSession().writeAndFlush((Object) CField.bonusAttackRequest(400011076,
					new ArrayList<Triple<Integer, Integer, Integer>>(), true, 0, new int[0]));
		}
		if (player.getBuffedValue(400001037) && attack.skill != 400001038 && System.currentTimeMillis()
				- player.lastAngelTime >= (long) (player.getBuffedEffect(400001037).getZ() * 1000)) {
			player.lastAngelTime = System.currentTimeMillis();
			ArrayList<Triple<Integer, Integer, Integer>> mobList = new ArrayList<Triple<Integer, Integer, Integer>>();
			int i = 0;
			for (AttackPair a : attack.allDamage) {
				mobList.add(new Triple<Integer, Integer, Integer>(a.objectId, 291 + 70 * i, 0));
				++i;
			}
			player.getClient().getSession()
					.writeAndFlush((Object) CField.bonusAttackRequest(400001038, mobList, true, 0, new int[0]));
		}
		if (attack.skill == 155120000 && player.getSkillLevel(400051047) > 0
				&& player.getCooldownLimit(400051047) == 0L) {
			player.getClient().getSession().writeAndFlush((Object) CField.bonusAttackRequest(400051047,
					new ArrayList<Triple<Integer, Integer, Integer>>(), true, 0, new int[0]));
		}
		if (attack.skill == 155120001 && player.getSkillLevel(400051047) > 0
				&& player.getCooldownLimit(400051048) == 0L) {
			player.getClient().getSession().writeAndFlush((Object) CField.bonusAttackRequest(400051048,
					new ArrayList<Triple<Integer, Integer, Integer>>(), true, 0, new int[0]));
		}
		if (attack.skill == 3321014 || attack.skill == 3321016 || attack.skill == 3321018 || attack.skill == 3321020) {
			player.getClient().getSession().writeAndFlush((Object) CField.bonusAttackRequest(attack.skill + 1,
					new ArrayList<Triple<Integer, Integer, Integer>>(), true, 600, new int[0]));
		}
		if (attack.skill == 400041042) {
			player.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(attack.skill,
					Arrays.asList(new RangeAttack(400041043, attack.position, 1, 0, 0))));
		}
		if (player.getBuffedValue(21101005) && totDamageToOneMonster > 0L
				&& player.getBuffedEffect(SecondaryStat.DebuffIncHp) == null) {
			player.addHP(player.getStat().getCurrentMaxHp() * (long) player.getBuffedEffect(21101005).getX() / 100L);
		}
		if (SkillFactory.getSkill(attack.skill) != null && player.getSkillLevel(1310009) > 0
				&& !SkillFactory.getSkill(attack.skill).isFinalAttack() && totDamageToOneMonster > 0L
				&& player.getBuffedEffect(SecondaryStat.DebuffIncHp) == null
				&& SkillFactory.getSkill(1310009).getEffect(player.getSkillLevel(1310009)).makeChanceResult()) {
			player.addHP(player.getStat().getCurrentMaxHp()
					* (long) SkillFactory.getSkill(1310009).getEffect(player.getSkillLevel(1310009)).getX() / 100L);
		}
		if (totDamageToOneMonster > 0L && player.getBuffedEffect(SecondaryStat.DebuffIncHp) == null
				&& player.getBuffedValue(1321054)) {
			player.addHP(player.getStat().getCurrentMaxHp() * (long) player.getBuffedEffect(1321054).getX() / 100L);
		}
		if (player.getSkillLevel(60030241) > 0 || player.getSkillLevel(80003015) > 0) {
			int skillid;
			int n = player.getSkillLevel(60030241) > 0 ? 60030241
					: (skillid = player.getSkillLevel(80003015) > 0 ? 80003015 : 0);
			if (n > 0 && monster != null) { // n
				if (monster.getStats().isBoss()) {
					if (monster.isAlive()) {
						player.handlePriorPrepaRation(n, 2);
					}
				} else if (!monster.isAlive()) {
					player.handlePriorPrepaRation(n, 1);
				}
			}
		}
		if (GameConstants.isHero(player.getJob())) {
			if (monster != null) {
				if (monster.getBuff(1111003) != null) {
					SkillFactory.getSkill(1110009).getEffect(player.getSkillLevel(1110009)).applyTo(player);
				} else {

				}
			}
		}

		if (player.getSkillLevel(150010241) > 0 && player.getSkillCustomValue(80000514) == null) {
			SkillFactory.getSkill(150010241).getEffect(player.getSkillLevel(150010241)).applyTo(player);
			player.setSkillCustomInfo(80000514, 0L, 3000L);
		} else if (player.getSkillLevel(80000514) > 0 && player.getSkillCustomValue(80000514) == null) {
			SkillFactory.getSkill(80000514).getEffect(player.getSkillLevel(80000514)).applyTo(player);
			player.setSkillCustomInfo(80000514, 0L, 3000L);
		}

		if (attack.skill == 154121000 || attack.skill == 154141000) { // 칼리 플러리 쿨타임 감소
			// 일반 구현
			if (player.skillisCooling(154121003)) {
				player.changeCooldown(154121003, -1000);
			}
			if (player.skillisCooling(154121009)) {
				player.changeCooldown(154121009, -1000);
			}

			// 6차 구현
			if (player.skillisCooling(154141003)) {
				player.changeCooldown(154141003, -1000);
			}
			if (player.skillisCooling(154141007)) {
				player.changeCooldown(154141007, -1000);
			}
		}

		if (attack.skill == 154101001 || attack.skill == 154141001) { // 칼리 크레센텀 쿨타임 감소
			// 일반 구현
			if (player.skillisCooling(154121003)) {
				player.changeCooldown(154121003, -5000);
			}
			if (player.skillisCooling(154121009)) {
				player.changeCooldown(154121009, -5000);
			}

			// 6차 구현
			if (player.skillisCooling(154141003)) {
				player.changeCooldown(154141003, -5000);
			}
			if (player.skillisCooling(154141007)) {
				player.changeCooldown(154141007, -5000);
			}
		}

		// 칼리 보이드 러쉬 - 버프 이펙트
		if (MapleSkillManager.isKhaliVoydSkills(attack.skill)) {
			if (player.getSkillLevel(154001003) > 0 && player.getSkillCustomValue(154001003) == null) {
				SkillFactory.getSkill(154121009).getEffect(player.getSkillLevel(154121009)).applyTo(player);
				player.setSkillCustomInfo(154121009, 0L, 3000L);
			}
		}
		// 칼리 디시빙 블레이드 - 버프 이펙트
		if (GameConstants.isKhali(player.getJob())) {

			// 칼리 - 링크스킬 이네이트 기프트
			if (player.getSkillLevel(150030241) > 0 && player.getSkillCustomValue(80003224) == null) {
				SkillFactory.getSkill(150030241).getEffect(player.getSkillLevel(150030241)).applyTo(player);
				player.setSkillCustomInfo(80003224, 0L, 3000L);
			} else if (player.getSkillLevel(80003224) > 0 && player.getSkillCustomValue(80003224) == null) {
				SkillFactory.getSkill(80003224).getEffect(player.getSkillLevel(80003224)).applyTo(player);
				player.setSkillCustomInfo(80003224, 0L, 3000L);
			}

			if (player.getSkillLevel(154110005) > 0 && player.getSkillCustomValue(154110005) == null) {
				SkillFactory.getSkill(154110005).getEffect(player.getSkillLevel(154110005)).applyTo(player);
				player.setSkillCustomInfo(154110005, 0L, 100L);
				player.getClient().getSession().writeAndFlush((Object) CField.bonusAttackRequest(154110005,
						new ArrayList<Triple<Integer, Integer, Integer>>(), true, 2, new int[0]));
			}
		}

		// 칼리
		if (player.getBuffedValue(SecondaryStat.SummonChakri) != null
				&& player.getBuffedValue(SecondaryStat.Ultimatum) == null) { // 서먼차크리만 갖고 있을때
			for (MapleSummon s : player.getMap().getAllSummons(154110010)) {
				if (s != null && s.getOwner().getId() == player.getId()
						&& MapleSkillManager.isKhaliHexSkills(attack.skill) && monster != null) {
					final List<SecondAtom> atoms = new ArrayList<SecondAtom>();
					atoms.add(new SecondAtom(43, player.getId(), monster.getId(), 154110001, 5000, 0, 3,
							new Point(s.getTruePosition().x, s.getTruePosition().y), Arrays.asList(0)));
					player.spawnSecondAtom(atoms);
					s.removeSummon(player.getMap(), false);
					player.getSummons().remove(s);
					player.ChakriSize = 0; // 초기화
				}
			}
		}
		if (player.getBuffedValue(SecondaryStat.Ultimatum) != null) { // 얼티메이텀 있을때
			for (MapleSummon s : player.getMap().getAllSummonsThreadsafe()) {
				if (s != null && s.getOwner().getId() == player.getId()
						&& MapleSkillManager.isKhaliHexSkills(attack.skill) && monster != null) {
					final List<SecondAtom> atoms = new ArrayList<SecondAtom>();
					for (int i = 0; i < 2; ++i) {
						atoms.add(new SecondAtom(44, player.getId(), monster.getId(), 400041091, 5000, 0, 3,
								new Point(s.getTruePosition().x, s.getTruePosition().y), Arrays.asList(0)));
					}
					player.spawnSecondAtom(atoms);
					s.removeSummon(player.getMap(), false);
					player.getSummons().remove(s);
					player.ChakriSize = 0;

				}
			}
		}

		if (player.getBuffedValue(63121044) && player.getSkillCustomValue(80113017) == null
				&& (long) player.getId() != player.getSkillCustomValue0(63121044)) {
			MapleMonster mob = null;
			List<MapleMapObject> objs = player.getMap().getMapObjectsInRange(player.getTruePosition(), 600000.0,
					Arrays.asList(new MapleMapObjectType[] { MapleMapObjectType.MONSTER }));
			if (objs.size() > 0) {
				for (int i = 1; i <= objs.size(); ++i) {
					mob = (MapleMonster) objs.get(Randomizer.nextInt(objs.size()));
					if (mob == null || !mob.isAlive()) {
						continue;
					}
					RangeAttack rg = new RangeAttack(80003017, mob.getTruePosition(), 0, 0, 1);
					rg.getList().add(mob.getObjectId());
					player.getClient().getSession()
							.writeAndFlush((Object) CField.rangeAttack(80003017, Arrays.asList(rg)));
					player.setSkillCustomInfo(80113017, 0L, player.getBuffedEffect(63121044).getY() * 1000);
					break;
				}
			}
		}
		if (attack.targets > 0) {
			int fallingspeed = 30;
			int fallingtime = GameConstants.getFallingTime(attack.skill);
			if (fallingtime != -1) {
				player.getClient().getSession()
						.writeAndFlush((Object) CField.setFallingTime(fallingspeed, fallingtime));
			}
		}
		if (monster != null) {
			if (monster.getBuff(MonsterStatus.MS_DarkLightning) != null && attack.skill != 400021113
					&& attack.skill != 32110020 && attack.skill != 32111016 && attack.skill != 32101001
					&& attack.skill != 32111015 && attack.skill != 400021088) {
				ArrayList<Triple<Integer, Integer, Integer>> mobList = new ArrayList<Triple<Integer, Integer, Integer>>();
				mobList.add(new Triple<Integer, Integer, Integer>(monster.getObjectId(), 0, 0));
				player.getClient().getSession()
						.writeAndFlush((Object) CField.bonusAttackRequest(32110020, mobList, false, 0, new int[0]));
				boolean cancel = true;
				if (player.getBuffedValue(400021087)) {
					int maxcount = SkillFactory.getSkill(400021087).getEffect(player.getSkillLevel(400021087)).getS2();
					if (player.getSkillCustomValue0(410021087) < (long) (maxcount - 1)) {
						player.addSkillCustomInfo(410021087, 1L);
						cancel = false;
					} else {
						player.removeSkillCustomInfo(410021087);
					}
				}
				if (cancel) {
					monster.cancelStatus(MonsterStatus.MS_DarkLightning,
							monster.getBuff(MonsterStatus.MS_DarkLightning));
				}
			}
			if (GameConstants.isAran(player.getJob())) {
				if (SkillFactory.getSkill(400011122).getSkillList().contains(attack.skill)
						&& monster.getCustomValue0(400011121) > 0L && monster.getCustomTime(400011122) != null
						&& player.getBuffedValue(400011123)) {
					ArrayList<Triple<Integer, Integer, Integer>> list = new ArrayList<Triple<Integer, Integer, Integer>>();
					if (monster.getCustomValue0(400011121) < 6L) {
						monster.addSkillCustomInfo(400011121, 1L);
					}
					list.add(new Triple<Integer, Integer, Integer>(monster.getObjectId(),
							(int) monster.getCustomValue0(400011121), monster.getCustomTime(400011122)));
					player.getMap().broadcastMessage(CField.getBlizzardTempest(list));
					ArrayList<Triple<Integer, Integer, Integer>> mobList = new ArrayList<Triple<Integer, Integer, Integer>>();
					player.getClient().getSession()
							.writeAndFlush((Object) CField.bonusAttackRequest(400011122, mobList, true, 0, new int[0]));
				} else if (attack.skill == 400011121) {
					ArrayList<Triple<Integer, Integer, Integer>> list = new ArrayList<Triple<Integer, Integer, Integer>>();
					for (MapleMonster m3 : player.getMap().getAllMonster()) {
						if (m3 == null || m3.getCustomValue0(400011121) <= 0L || m3.getCustomTime(400011122) == null) {
							continue;
						}
						list.add(new Triple<Integer, Integer, Integer>(m3.getObjectId(),
								(int) m3.getCustomValue0(400011121), m3.getCustomTime(400011122)));
					}
					SkillFactory.getSkill(400011123).getEffect(player.getSkillLevel(400011121)).applyTo(player);
					if (!list.isEmpty()) {
						player.getMap().broadcastMessage(CField.getBlizzardTempest(list));
					}
				}
			}
		}
		// 오버로드 마나 - 마나소모량 fix
		if (effect != null && player.getBuffedValue(SecondaryStat.OverloadMana) != null
				&& !GameConstants.is_forceAtom_attack_skill(attack.skill) && !effect.isMist()) {
			if (GameConstants.isKinesis(player.getJob())) {
				player.addHP((int) (-(player.getStat().getCurrentMaxHp()
						* (long) player.getBuffedEffect(SecondaryStat.OverloadMana).getY() / 1000L)));
			} else {
				player.addMP((int) (-(player.getStat().getCurrentMaxMp(player)
						* (long) player.getBuffedEffect(SecondaryStat.OverloadMana).getX() / 1000L)));
			}
		}
		if (GameConstants.isCaptain(player.getJob()) && attack.skill == 400051073) {
			player.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(400051081,
					Arrays.asList(new RangeAttack(400051081, monster.getPosition(), 0, 0, 1))));
		}
		if (GameConstants.isAdel(player.getJob())) {
			boolean isHexa = player.getSkillLevel(151141003) > 0;
			if (player.getBuffedValue(isHexa ? 151141003 : 151101013)
					&& player.getSkillCustomValue(isHexa ? 151141003 : 151101013) == null && attack.targets > 0
					&& (attack.skill == 151101000 || attack.skill == 151111000 || attack.skill == 151121000
							|| attack.skill == 151121002 || attack.skill == 151141000 || attack.skill == 151141001
							|| attack.skill == 151141005)) {
				player.addMP(-player.getBuffedEffect(isHexa ? 151141003 : 151101013).getY());
				player.getMap().broadcastMessage(SkillPacket.CreateSubObtacle(player, isHexa ? 151141002 : 151001001));
				player.setSkillCustomInfo(isHexa ? 151141003 : 151101013, 0L, 8000L);
			}
			if (player.getSkillLevel(151100017) > 0 && attack.targets > 0
					&& (attack.skill == 151101000 || attack.skill == 151111000 || attack.skill == 151121000
							|| attack.skill == 151121002 || attack.skill == 151141000 || attack.skill == 151141001
							|| attack.skill == 151141005)) {
				player.에테르핸들러(player, 12, attack.skill, false);
			}
			if (player.getBuffedValue(151101006) && player.에테르소드 > 0 && player.getSkillCustomValue(151101016) == null
					&& attack.targets > 0
					&& (attack.skill == 151101000 || attack.skill == 151111000 || attack.skill == 151121000
							|| attack.skill == 151121002 || attack.skill == 151141000 || attack.skill == 151141001
							|| attack.skill == 151141005)) {
				for (int i = 1; i <= player.에테르소드; ++i) {
					player.getMap().broadcastMessage(
							SkillPacket.AutoAttackObtacleSword(player, i * 10, i == 1 ? player.에테르소드 : 0));
				}
				if (player.getJob() == 15112) {
					player.setSkillCustomInfo(151101016, 0L, 1500L);
				} else if (player.getJob() == 15111) {
					player.setSkillCustomInfo(151101016, 0L, 5500L);
				} else {
					player.setSkillCustomInfo(151101016, 0L, 9500L);
				}
			}
			if (monster != null && attack != null) {
				switch (attack.skill) {
				case 151111002: {
					if (!Randomizer.isSuccess(40) || attack.targets <= 0) {
						break;
					}
					player.에테르결정(player, monster.getTruePosition(), false);
					break;
				}
				case 151111003:
				case 151141004: { // 오더 VI
					if (!Randomizer.isSuccess(15) || attack.targets <= 0) {
						break;
					}
					player.에테르결정(player, monster.getTruePosition(), false);
					break;
				}
				case 400011108: {
					if (!Randomizer.isSuccess(5) || attack.targets <= 0) {
						break;
					}
					player.에테르결정(player, monster.getTruePosition(), false);
					break;
				}
				case 151121003: {
					if (!Randomizer.isSuccess(50) || attack.targets <= 0) {
						break;
					}
					player.에테르결정(player, monster.getTruePosition(), false);
					break;
				}
				case 151101007:
				case 151101008:
				case 151101009: {
					if (!Randomizer.isSuccess(30) || attack.targets <= 0) {
						break;
					}
					player.에테르결정(player, monster.getTruePosition(), false);
				}
				}
			}
			if (attack.skill == 151101003 || attack.skill == 151101004 && attack.targets > 0) {
				if (!player.getBuffedValue(151101010)) {
					player.removeSkillCustomInfo(151101010);
				}
				SkillFactory.getSkill(151101010).getEffect(player.getSkillLevel(151101003)).applyTo(player);
			} else if (attack.skill == 151121001 && monster != null) {
				player.setSkillCustomInfo(151121001, monster.getObjectId(), 0L);
				if (player.getSkillCustomValue0(151121001) != (long) monster.getObjectId()) {
					player.removeSkillCustomInfo(151121001);
				}
				SkillFactory.getSkill(151121001).getEffect(player.getSkillLevel(151121001)).applyTo(player);
			}
		} else if (GameConstants.isCain(player.getJob()) && attack.targets > 0) {
			if (attack.targets > 0) {
				if (player.getBuffedValue(400031062) && !player.skillisCooling(400031063)) {
					boolean facingleft = (attack.facingleft >>> 4 & 0xF) == 8;
					for (int i = 0; i < player.getBuffedEffect(400031062).getMobCount(); ++i) {
						int x = attack.position.x;
						int y = attack.position.y - Randomizer.rand(50, 210);
						x = facingleft ? (x += Randomizer.rand(50, 200)) : (x -= Randomizer.rand(50, 200));
						player.createSecondAtom(SkillFactory.getSkill(400031063).getSecondAtoms(), new Point(x, y),
								facingleft);
					}
					player.addCooldown(400031063, System.currentTimeMillis(),
							player.getBuffedEffect(400031062).getSubTime());
				}
				if (player.getSkillCustomValue(63101114) == null
						&& (SkillFactory.getSkill(63101001).getSkillList().contains(attack.skill)
								|| SkillFactory.getSkill(63101001).getSkillList2().contains(attack.skill))) {
					player.handlePossession(2);
					if (attack.skill == 63101004) {
						player.setSkillCustomInfo(63101114, 0L, 1000L);
					}
				}
				if (player.getBuffedValue(63101005)) {
					SecondaryStatEffect effect2 = SkillFactory.getSkill(63101005)
							.getEffect(player.getSkillLevel(63101005));
					if (SkillFactory.getSkill(63101005).getSkillList().contains(attack.skill)
							|| SkillFactory.getSkill(63101005).getSkillList2().contains(attack.skill)) {
						if (player.getSkillCustomValue0(63101006) > 0L) {
							for (MapleSecondAtom at : player.getMap().getAllSecondAtoms()) {
								if (at.getSourceId() != 63101006 || at.getChr().getId() != player.getId()
										|| (long) (effect2.getU() * 1000)
												- (System.currentTimeMillis() - at.getLastAttackTime()) > 0L) {
									continue;
								}
								player.getMap()
										.broadcastMessage(SkillPacket.AttackSecondAtom(player, at.getObjectId(), 1));
							}
						}
						player.addSkillCustomInfo(63101005, 1L);
						if (player.getSkillCustomValue0(63101005) >= 7L
								&& player.getSkillCustomValue0(63101006) < (long) effect2.getQ()) {
							player.addSkillCustomInfo(63101006, 1L);
							player.removeSkillCustomInfo(63101005);
							player.createSecondAtom(63101006, player.getPosition(),
									(int) player.getSkillCustomValue0(63101006) - 1);
						}
					}
				}
				if (attack.skill == 63001000 || attack.skill == 63101003 || attack.skill == 63111002
						|| attack.skill == 63141007) {
					RangeAttack rand = new RangeAttack(attack.skill == 63141007 ? 63141008 : 63001001, attack.position,
							player.isFacingLeft() ? 1 : 0, 0, 1);
					ArrayList<RangeAttack> skills = new ArrayList<RangeAttack>();
					skills.add(rand);
					player.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(attack.skill, skills));
				}
				if (player.getBuffedValue(400031062) && player.getSkillCustomValue(400031063) == null
						&& attack.skill != 63111012 && attack.skill != 63101006 && attack.skill != 63111010) {
					SecondaryStatEffect effect2 = SkillFactory.getSkill(400031062)
							.getEffect(player.getSkillLevel(400031062));
					player.addSkillCustomInfo(400031062, 1L);
					if (player.getSkillCustomValue0(400031062) >= (long) effect2.getW()) {
						player.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(effect2.getSourceId(),
								Arrays.asList(new RangeAttack(400031063, monster.getPosition(), 0, 0, 1))));
						player.removeSkillCustomInfo(400031062);
						player.setSkillCustomInfo(400031063, 0L, effect2.getSubTime());
					}
				}
				if (player.getSkillLevel(400031066) > 0 && monster != null) {
					SecondaryStatEffect effect2 = SkillFactory.getSkill(400031066)
							.getEffect(player.getSkillLevel(400031066));
					boolean givebuff = false;
					if (monster.getStats().isBoss()) {
						player.addSkillCustomInfo(400031067, 1L);
						if (player.getSkillCustomValue0(400031067) >= (long) effect2.getQ2()) {
							player.removeSkillCustomInfo(400031067);
							givebuff = true;
						}
					} else if (!monster.isAlive()) {
						player.addSkillCustomInfo(400031068, 1L);
						if (player.getSkillCustomValue0(400031068) >= (long) effect2.getQ2()) {
							player.removeSkillCustomInfo(400031068);
							givebuff = true;
						}
					}
					if (givebuff) {
						player.addSkillCustomInfo(400031066, 1L);
						if (player.getSkillCustomValue0(400031066) >= (long) effect2.getU()) {
							player.setSkillCustomInfo(400031066, effect2.getU(), 0L);
						}
						effect2.applyTo(player);
					}
				}
			}
		} else if (GameConstants.isBlaster(player.getJob())) {
			if (afterimageshockattack) {
				SecondaryStatEffect effect2 = SkillFactory.getSkill(400011116)
						.getEffect(player.getSkillLevel(400011116));
				long duration = player.getBuffLimit(400011116);
				player.setSkillCustomInfo(400011116, player.getSkillCustomValue0(400011116) - 1L, 0L);
				if (player.getSkillCustomValue0(400011116) > 0L) {
					effect2.applyTo(player, false, (int) duration);
				} else {
					player.cancelEffectFromBuffStat(SecondaryStat.AfterImageShock, 400011116);
				}
			}
		} else if (GameConstants.isDemonAvenger(player.getJob())) {
			if (!player.getBuffedValue(30010230)) {
				player.updateExceed(player.getExceed());
			}
			if (attack.skill == 31221052) {
				player.gainExceed((short) 5);
			}
			if (GameConstants.isExceedAttack(attack.skill)) {
				if (player.getSkillLevel(31220044) > 0) {
					if (player.getExceed() < 19) {
						player.gainExceed((short) 1);
					}
				} else if (player.getExceed() < 20) {
					player.gainExceed((short) 1);
				}
				player.handleExceedAttack(attack.skill);
			}
			if (player.getBuffedValue(31221054)) {
				player.addHP(stats.getCurrentMaxHp() / 100L * 5L);
			}
			if (attack.skill == 31211001) {
				player.addHP(stats.getCurrentMaxHp() / 100L * 10L, true, false);
			} else if (attack.skill == 400011062) {
				player.addHP(stats.getCurrentMaxHp() / 100L * 10L, true, false);
				player.getClient().getSession()
						.writeAndFlush((Object) CField.skillCooldown(400011038, effect.getCooldown(player)));
			} else if (attack.skill == 400011063) {
				player.addHP(stats.getCurrentMaxHp() / 100L * 15L, true, false);
				player.getClient().getSession()
						.writeAndFlush((Object) CField.skillCooldown(400011038, effect.getCooldown(player)));
			} else if (attack.skill == 400011064) {
				player.addHP(stats.getCurrentMaxHp() / 100L * 20L, true, false);
				player.getClient().getSession()
						.writeAndFlush((Object) CField.skillCooldown(400011038, effect.getCooldown(player)));
			}
			if (SkillFactory.getSkill(attack.skill) != null && attack.targets > 0 && attack.skill != 31220007
					&& !SkillFactory.getSkill(attack.skill).isFinalAttack() && monster != null) {
				SecondaryStatEffect effect2 = SkillFactory.getSkill(31010002).getEffect(player.getSkillLevel(31010002));
				SecondaryStatEffect Exceed = SkillFactory.getSkill(30010230).getEffect(player.getSkillLevel(30010230));
				int healper = effect2.getX();
				if (player.getSkillLevel(31210006) > 0) {
					healper = SkillFactory.getSkill(31210006).getEffect(player.getSkillLevel(31210006)).getX();
				}
				if (GameConstants.isExceedAttack(attack.skill)) {
					int minusper = 0;
					minusper = player.getExceed() / Exceed.getZ() * Exceed.getY();
					if (player.getSkillLevel(31210005) > 0 && minusper > 0 && (minusper -= SkillFactory
							.getSkill(31210005).getEffect(player.getSkillLevel(31210005)).getX()) < 0) {
						minusper = 0;
					}
					if (minusper > 0) {
						healper -= minusper;
					}
				}
				player.addHP(stats.getCurrentMaxHp() / 100L * (long) healper);
			}
		} else if (GameConstants.isBattleMage(player.getJob())) {
			// 어비셜 라이트닝 null 예외처리
			if (player.getBuffedEffect(SecondaryStat.AbyssalLightning) != null
					&& player.getSkillCustomValue(400021113) == null && monster != null) {
				SecondaryStatEffect ef = SkillFactory.getSkill(400021113).getEffect(1);
				ArrayList<Triple<Integer, Integer, Integer>> mobList = new ArrayList<Triple<Integer, Integer, Integer>>();
				for (MapleMonster m : player.getMap().getAllMonster()) {
					if (!ef.calculateBoundingBox(attack.position, true).contains(m.getPosition())
							&& !ef.calculateBoundingBox(attack.position, false).contains(m.getPosition())) {
						continue;
					}
					mobList.add(new Triple<Integer, Integer, Integer>(m.getObjectId(), 0, 0));
				}
				player.getClient().getSession()
						.writeAndFlush((Object) CField.bonusAttackRequest(400021113, mobList, false, 0, new int[0]));
				player.setSkillCustomInfo(400021113, 0L, 600L);
			}
		} else if (GameConstants.isWildHunter(player.getJob())) {
			if (player.getBuffedValue(SecondaryStat.JaguarSummoned) != null) {
				player.setSkillCustomInfo(33001001, 0L, 10000L);
				player.getClient().send(CField.SummonPacket.JaguarAutoAttack(true));
			}
			if (attack.skill == 33121214 && player.getSkillCustomValue(33121214) == null) {
				SecondaryStatEffect eff = SkillFactory.getSkill(33121214).getEffect(player.getSkillLevel(33121214));
				ArrayList<Integer> objid = new ArrayList<Integer>();
				ArrayList<MapleMonster> attackk = new ArrayList<MapleMonster>();
				int i = 0;
				for (AttackPair oned1 : attack.allDamage) {
					objid.add(oned1.objectId);
				}
				List<MapleMapObject> objs = player.getMap().getMapObjectsInRange(player.getTruePosition(), 400000.0,
						Arrays.asList(new MapleMapObjectType[] { MapleMapObjectType.MONSTER }));
				for (MapleMapObject m : player.getMap().getMapObjectsInRange(player.getTruePosition(), 400000.0,
						Arrays.asList(new MapleMapObjectType[] { MapleMapObjectType.MONSTER }))) {
					MapleMonster mob = (MapleMonster) m;
					if (mob == null) {
						continue;
					}
					boolean attacked = true;
					for (Integer a2 : objid) {
						if (a2.intValue() != mob.getObjectId()) {
							continue;
						}
						attacked = false;
						break;
					}
					if (!attacked) {
						continue;
					}
					attackk.add(mob);
					if (++i < eff.getQ()) {
						continue;
					}
					break;
				}
				if (!attackk.isEmpty()) {
					for (MapleMonster m : attackk) {
						// player.getClient().getSession().writeAndFlush((Object)
						// CField.rangeAttack(attack.skill, Arrays.asList(new RangeAttack(33121019,
						// m.getPosition(), 1, 0, 1))));
					}
					player.setSkillCustomInfo(attack.skill, 0L, eff.getY() * 1000);
				}
			}
		} else if (GameConstants.isMichael(player.getJob())) {
			if (player.skillisCooling(400011032) && player.getSkillCustomValue(400011033) == null) {
				ArrayList<Triple<Integer, Integer, Integer>> mobList = new ArrayList<Triple<Integer, Integer, Integer>>();
				player.getClient().getSession()
						.writeAndFlush((Object) CField.bonusAttackRequest(400011033, mobList, true, 0, new int[0]));
				player.setSkillCustomInfo(400011033, 0L, 5000L);
			}
		} else if (GameConstants.isEunWol(player.getJob())) {
			if (player.getSkillLevel(20050285) > 0) {
				SecondaryStatEffect effect22 = SkillFactory.getSkill(20050285).getEffect(1);
				if (!GameConstants.is_forceAtom_attack_skill(attack.skill)) {
					player.addHP(player.getStat().getCurrentMaxHp() / 100L * (long) effect22.getX());
				}
			}
			if (player.getBuffedValue(25121133) && monster != null) {
				final SecondaryStatEffect effect7 = SkillFactory.getSkill(25121133).getEffect(1);
				if (effect7.makeChanceResult()) {
					final Item toDrop2 = new Item(2434851, (short) 0, (short) 1, 0);
					monster.getMap().spawnItemDrop(monster, player, toDrop2,
							new Point(monster.getTruePosition().x, monster.getTruePosition().y), true, false);
				}
			}
		} else if (GameConstants.isIllium(player.getJob())) {
			if (attack.skill == 152110004) {
				ArrayList<Integer> monsters = new ArrayList<Integer>();
				MapleAtom atom = new MapleAtom(false, player.getObjectId(), 38, true, 152120016, player.getPosition().x,
						player.getPosition().y);
				for (int i2 = 0; i2 < 3; ++i2) {
					monsters.add(monster.getObjectId());
					ForceAtom forceAtom = new ForceAtom(2, Randomizer.rand(40, 48), Randomizer.rand(5, 6),
							Randomizer.rand(14, 256), 0);
					atom.addForceAtom(forceAtom);
				}
				atom.setDwTargets(monsters);
				player.getMap().spawnMapleAtom(atom);
			}
		} else if (GameConstants.isKadena(player.getJob())) {
			if (attack.skill == 64121020 && player.getSkillLevel(400041074) > 0) {
				HashMap<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
				SecondaryStatEffect effect2 = SkillFactory.getSkill(400041074)
						.getEffect(player.getSkillLevel(400041074));
				player.setSkillCustomInfo(64121020, player.getSkillCustomValue0(64121020) + 1L, 0L);
				if (player.getSkillCustomValue0(64121020) > (long) effect2.getW()) {
					player.setSkillCustomInfo(64121020, effect2.getW(), 0L);
				}
				statups.put(SecondaryStat.WeaponVarietyFinale,
						new Pair<Integer, Integer>((int) player.getSkillCustomValue0(400041074), 0));
				player.getClient().getSession()
						.writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, effect2, player));
				if (player.getSkillCustomValue0(400041074) > 0L
						&& player.getSkillCustomValue0(64121020) >= (long) effect2.getW()) {
					player.removeSkillCustomInfo(64121020);
					PlayerHandler.Vmatrixstackbuff(player.getClient(), true, null);
					player.getClient().getSession().writeAndFlush(
							(Object) CField.rangeAttack(400041074, Arrays.asList(new RangeAttack(400041074,
									monster != null ? monster.getPosition() : attack.position, 0, 0, 5))));
				}
			}
		} else if (GameConstants.isYeti(player.getJob())) {
			if (monster != null) {
				if (player.getBuffedValue(135001015) && player.getSkillCustomValue(135001015) == null
						&& SkillFactory.getSkill(135001015).getSkillList().contains(attack.skill)) {
					ArrayList<Integer> monsters = new ArrayList<Integer>();
					MapleAtom atom = new MapleAtom(false, player.getObjectId(), 60, true, 135002015,
							player.getPosition().x, player.getPosition().y);
					for (MapleMonster mob : player.getMap().getAllMonster()) {
						monsters.add(mob.getObjectId());
					}
					for (int i = 0; i < 3; ++i) {
						ForceAtom forceAtom = new ForceAtom(1, 42 + i, 3, Randomizer.rand(59, 131), 0);
						atom.addForceAtom(forceAtom);
						player.getYetiGauge(135001015, 0);
					}
					atom.setDwTargets(monsters);
					player.getMap().spawnMapleAtom(atom);
					player.setSkillCustomInfo(135001015, 0L, player.getBuffedEffect(135001015).getY() * 1000);
				}
				player.getYetiGauge(attack.skill, monster.getStats().isBoss() ? 2 : 0);
			}
			if (attack.skill == 135001007 || attack.skill == 135001010) {
				PlayerHandler.Vmatrixstackbuff(player.getClient(), true, null);
			}
		} else if (GameConstants.isZero(player.getJob())) {
			SecondaryStatEffect eff;
			if (player.getSkillLevel(101110205) > 0 && attack.targets > 0 && player.getGender() == 0
					&& Randomizer.isSuccess((eff = SkillFactory.getSkill(101110205).getEffect(101110205)).getY())) {
				player.getClient().send(CField.getTpAdd(20, eff.getZ()));
				player.getMap().broadcastMessage(player,
						CField.EffectPacket.showEffect(player, 0, 101110205, 4, 0, 0,
								(byte) (player.isFacingLeft() ? 1 : 0), false, player.getTruePosition(), null, null),
						false);
			}
		} else if (GameConstants.isPinkBean(player.getJob()) && monster != null && player.getSkillLevel(131000016) > 0
				&& player.getSkillCustomValue(131000016) == null && attack.skill != 131000016
				&& Randomizer.isSuccess(50)) {
			SecondaryStatEffect eff = SkillFactory.getSkill(131003016).getEffect(1);
			ArrayList<Integer> monsters = new ArrayList<Integer>();
			MapleAtom atom = new MapleAtom(false, player.getObjectId(), 65, true, 131003016, player.getPosition().x,
					player.getPosition().y);
			for (MapleMonster mob : player.getMap().getAllMonster()) {
				if (!eff.calculateBoundingBox(attack.position, (attack.facingleft >>> 4 & 0xF) == 0)
						.contains(mob.getPosition())) {
					continue;
				}
				monsters.add(mob.getObjectId());
			}
			for (int i = 0; i < 4; ++i) {
				ForceAtom forceAtom = new ForceAtom(1, Randomizer.rand(40, 44), Randomizer.rand(3, 4),
						Randomizer.rand(25, 345), 0);
				forceAtom.setnAttackCount(15 + i);
				atom.addForceAtom(forceAtom);
			}
			atom.setDwTargets(monsters);
			player.getMap().spawnMapleAtom(atom);
			player.setSkillCustomInfo(131000016, 0L, 10000L);
		}
		if (player.getBuffedValue(13121054) && attack.skill != 13121054 && !GameConstants.isTryFling(attack.skill)
				&& attack.targets > 0 && Randomizer.isSuccess(30)) {
			MapleAtom atom = new MapleAtom(false, player.getId(), 8, true, 13121054, player.getTruePosition().x,
					player.getTruePosition().y);
			atom.setDwFirstTargetId(0);
			ForceAtom ft = new ForceAtom(1, 1, Randomizer.rand(5, 7), 270, 66);
			ft.setnStartY(ft.getnStartY() + Randomizer.rand(-110, 110));
			atom.addForceAtom(ft);
			player.getMap().spawnMapleAtom(atom);
		}

		// WindBreaker Remaster
		if (player.getBuffedValue(13121017) && attack.skill != 13121017 && !GameConstants.isTryFling(attack.skill)
				&& attack.targets > 0 && Randomizer.isSuccess(30)) {
			MapleAtom atom = new MapleAtom(false, player.getId(), 8, true, 13121017, player.getTruePosition().x,
					player.getTruePosition().y);
			atom.setDwFirstTargetId(0);
			ForceAtom ft = new ForceAtom(1, 1, Randomizer.rand(5, 7), 270, 66);
			ft.setnStartY(ft.getnStartY() + Randomizer.rand(-110, 110));
			atom.addForceAtom(ft);
			player.getMap().spawnMapleAtom(atom);
		}
		if (player.getBuffedValue(13121055) && attack.skill != 13121055 && !GameConstants.isTryFling(attack.skill)
				&& attack.targets > 0 && Randomizer.isSuccess(30)) {
			final List<SecondAtom> atoms = new ArrayList<SecondAtom>();
			atoms.add(new SecondAtom(41, player.getId(), 1, 13121055, 0, 0, 1,
					new Point(player.getPosition().x, player.getPosition().y), Arrays.asList(8)));
			player.spawnSecondAtom(atoms);
		}

		if (attack.skill == 155001000) {// 아크
			SkillFactory.getSkill(155001001).getEffect(attack.skilllevel).applyTo(player, false);
		}
		if (attack.skill == 155101002) {
			SkillFactory.getSkill(155101003).getEffect(attack.skilllevel).applyTo(player, false);
		}
		if (attack.skill == 155111003) {
			SkillFactory.getSkill(155111005).getEffect(attack.skilllevel).applyTo(player, false);
		}
		if (attack.skill == 155121003) {
			SkillFactory.getSkill(155121005).getEffect(attack.skilllevel).applyTo(player, false);
		}
		if (GameConstants.isHoyeong(player.getJob())) {
			player.giveHoyoungGauge(attack.skill);
			if (player.getBuffedValue(SecondaryStat.ButterflyDream) != null
					&& SkillFactory.getSkill(164121007).getSkillList().contains(attack.skill)
					&& player.getSkillCustomValue(164121007) == null) {

				int prop = player.getBuffedEffect(164121007).getProp();
				if (attack.skill == 164120007) {
					prop += player.getBuffedEffect(164121007).getZ();
				}
				if (player.getBuffedEffect(SecondaryStat.ButterflyDream) != null) {
					prop += player.getBuffedValue(SecondaryStat.ButterflyDream).intValue();
				}
				if (Randomizer.isSuccess(prop)) {
					for (int i = 0; i < 5; ++i) {
						MapleAtom atom = new MapleAtom(false, player.getId(), 61, true, 164120007,
								player.getTruePosition().x, player.getTruePosition().y);
						ArrayList<Integer> monsters = new ArrayList<Integer>();
						monsters.add(0);
						atom.addForceAtom(new ForceAtom(i, Randomizer.rand(15, 146), Randomizer.rand(27, 304),
								Randomizer.rand(31, 360), 500));
						atom.addForceAtom(new ForceAtom(1 + i, Randomizer.rand(15, 100), Randomizer.rand(27, 340),
								Randomizer.rand(31, 306), 500));
						atom.addForceAtom(new ForceAtom(2 + i, Randomizer.rand(15, 99), Randomizer.rand(27, 100),
								Randomizer.rand(31, 236), 500));
						atom.setDwTargets(monsters);
						player.getMap().spawnMapleAtom(atom);
					}
				}

			}
			if (player.getBuffedValue(400041052) && attack.targets != 0) {
				player.setInfinity((byte) (player.getInfinity() + 1));
				if (player.getInfinity() == 12) {
					for (MapleSummon summon5 : player.getSummons()) {
						if (summon5.getSkill() != 400041052) {
							summon5.setPosition(player.getPosition());
							continue;
						}
						player.setInfinity((byte) 0);
						player.getClient().getSession().writeAndFlush(
								(Object) CField.SummonPacket.DeathAttack(summon5, Randomizer.rand(8, 10)));
						break;
					}
				}
			}
		} else if (GameConstants.isArk(player.getJob())) {
			if (attack.targets > 0 && (attack.skill == 155001100 || attack.skill == 155101100
					|| attack.skill == 155101101 || attack.skill == 155101112 || attack.skill == 155111102
					|| attack.skill == 155121102 || attack.skill == 155111111 || attack.skill == 155101212
					|| attack.skill == 155111211 || attack.skill == 155101214 || attack.skill == 155121215)) {
				player.addSpell(attack.skill);
			}
			if (player.getSkillCustomValue0(400051080) > 0L) {
				if (attack.skill == 400051080) {
					player.removeSkillCustomInfo(attack.skill);
				}
				if (SkillFactory.getSkill(400051080).getSkillList().contains(attack.skill)) {
					player.getClient().send(CField.getEarlySkillActive(600));
				} else if (SkillFactory.getSkill(400051080).getSkillList2().contains(attack.skill)) {
					player.getClient().send(CField.getEarlySkillActive(180));
				}
			}
			if (player.skillisCooling(400051047) || player.skillisCooling(400051048)) {
				SecondaryStatEffect eff = SkillFactory.getSkill(400051047).getEffect(player.getSkillLevel(400051047));
				if (player.skillisCooling(400051047)) {
					if (SkillFactory.getSkill(400051047).getSkillList().contains(attack.skill)
							&& !player.getWeaponChanges().contains(attack.skill)) {
						player.getWeaponChanges().add(attack.skill);
						player.changeCooldown(400051047, -(eff.getX() * 1000));
					}
				} else if (player.skillisCooling(400051048)
						&& SkillFactory.getSkill(400051048).getSkillList().contains(attack.skill)
						&& !player.getWeaponChanges2().contains(attack.skill)) {
					player.getWeaponChanges2().add(attack.skill);
					player.changeCooldown(400051048, -(eff.getX() * 1000));
				}
			}

			if (attack.isLink) {
				if (player.getBuffedValue(SecondaryStat.FightJazz) == null) {
					player.setSkillCustomInfo(155120015, 0L, 0L);
				}
				if (player.getSkillCustomValue0(155120015) <= 2L) {
					player.setSkillCustomInfo(155120015, player.getSkillCustomValue0(155120015) + 1L, 0L);
				}
				SkillFactory.getSkill(155120014).getEffect(player.getSkillLevel(155120014)).applyTo(player);
			}
			if (attack.skill == 155101100 || attack.skill == 155101101) {
				player.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(attack.skill,
						Arrays.asList(new RangeAttack(155101013, attack.position, 0, 0, 1))));
			} else if (attack.skill == 155101112) {
				player.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(attack.skill,
						Arrays.asList(new RangeAttack(155101015, attack.position, 0, 0, 1))));
			} else if (attack.skill == 155121102) {
				player.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(attack.skill,
						Arrays.asList(new RangeAttack(155121002, attack.position, 0, 0, 1))));
			}

			boolean isLinkAttack = false;
			if (attack.skill == 155141004 || attack.skill == 155141005) { // 스칼렛 차지드라이브 VI 구현
				isLinkAttack = true;

				player.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(attack.skill,
						Arrays.asList(new RangeAttack(155141007, attack.position, 0, 0, 1))));
			} else if (attack.skill == 155141006) { // 스칼렛 차지드라이브 VI 구현
				isLinkAttack = true;

				player.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(attack.skill,
						Arrays.asList(new RangeAttack(155141008, attack.position, 0, 0, 1))));
			} else if (attack.skill == 155141016) { // 어비스 차지드라이브 VI 구현
				isLinkAttack = true;

				player.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(attack.skill,
						Arrays.asList(new RangeAttack(155141017, attack.position, 0, 0, 1))));
			}

			if (isLinkAttack) {
				if (player.getSkillLevel(155141000) > 0) { // 깨어난 심연 구현
					player.hexaMasterySkillStack += 10;

					if (player.hexaMasterySkillStack > 300) {
						player.hexaMasterySkillStack = 300;
					}

					EnumMap<SecondaryStat, Pair<Integer, Integer>> statups = new EnumMap<SecondaryStat, Pair<Integer, Integer>>(
							SecondaryStat.class);
					statups.put(SecondaryStat.Unk_771, new Pair<Integer, Integer>(player.hexaMasterySkillStack, 0));
					SecondaryStatEffect eff = SkillFactory.getSkill(155141001)
							.getEffect(player.getSkillLevel(155141000));
					player.getClient().getSession()
							.writeAndFlush(CWvsContext.BuffPacket.giveBuff(statups, eff, player));
				}
			}

			if (attack.skill == 155121003 && monster != null) {
				SecondaryStatEffect a = SkillFactory.getSkill(155121004)
						.getEffect(GameConstants.getLinkedSkill(155121004));
				MapleMist mist = new MapleMist(a.calculateBoundingBox(monster.getTruePosition(), player.isFacingLeft()),
						player, a, 3000, (byte) (player.isFacingLeft() ? 1 : 0));
				mist.setDelay(0);
				player.getMap().spawnMist(mist, false);
			}
		}
		if (player.getSkillLevel(400041063) > 0 && attack.targets > 0 && !player.skillisCooling(400041067)) {
			ArrayList<Integer> sungi_skills = new ArrayList<Integer>();
			Point pos = null;
			Point pos1 = null;
			for (MapleMonster mob : player.getMap().getAllMonster()) {
				if (!mob.isAlive() || player.getPosition().x + 500 < mob.getPosition().x
						|| player.getPosition().x - 500 > mob.getPosition().x
						|| player.getPosition().y + 300 < mob.getPosition().y
						|| player.getPosition().y - 300 > mob.getPosition().y) {
					continue;
				}
				if (pos == null) {
					pos = mob.getPosition();
					continue;
				}
				if (pos1 != null) {
					continue;
				}
				pos1 = mob.getPosition();
				break;
			}
			if (pos == null) {
				pos = player.getPosition();
			} else if (pos1 == null) {
				pos1 = pos;
			}
			Integer[] skills = new Integer[] { 164001000, 164001002, 164101000, 164111000, 164111003, 164111008,
					164121000, 164121003, 164121005 };
			if (Arrays.asList(skills).contains(attack.skill)) {
				SecondaryStatEffect sungi = SkillFactory.getSkill(400041063).getEffect(player.getSkillLevel(400041063));
				if (!player.useChun) {
					sungi_skills.add(400041064);
				}
				if (!player.useJi) {
					sungi_skills.add(400041065);
				}
				if (!player.useIn) {
					sungi_skills.add(400041066);
				}
				Collections.addAll(sungi_skills, new Integer[0]);
				Collections.shuffle(sungi_skills);
				if (attack.targets > 0 && player.getBuffedEffect(SecondaryStat.SageElementalClone) != null
						&& System.currentTimeMillis() - player.lastSungiAttackTime >= (long) (sungi.getV2() * 1000)) {
					if (!sungi_skills.isEmpty()) {
						player.lastSungiAttackTime = System.currentTimeMillis();
						int i = 0;
						Iterator m = sungi_skills.iterator();
						while (m.hasNext()) {
							int sungi_skill = (Integer) m.next();
							player.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(sungi_skill,
									Arrays.asList(new RangeAttack(sungi_skill, i == 0 ? pos : pos1, 1, 0, 1))));
							++i;
						}
						player.addCooldown(400041067, System.currentTimeMillis(), 2000L);
						player.getClient().getSession().writeAndFlush((Object) CField.skillCooldown(400041067, 2000));
					}
				} else if (attack.targets > 0 && player.getBuffedEffect(SecondaryStat.SageElementalClone) == null
						&& System.currentTimeMillis() - player.lastSungiAttackTime >= (long) (sungi.getQ() * 1000)) {
					int i = 0;
					if (!sungi_skills.isEmpty()) {
						player.lastSungiAttackTime = System.currentTimeMillis();
						int sungi_skill = (Integer) sungi_skills.get(Randomizer.nextInt(sungi_skills.size()));
						player.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(sungi_skill,
								Arrays.asList(new RangeAttack(sungi_skill, i == 0 ? pos : pos1, 1, 0, 1))));
						++i;
					}
					player.addCooldown(400041067, System.currentTimeMillis(), 5000L);
					player.getClient().getSession().writeAndFlush((Object) CField.skillCooldown(400041067, 5000));
				}
			}
		}
		for (MapleMist mist : player.getMap().getAllMistsThreadsafe()) {
			if (mist.getSource() == null || mist.getSourceSkill().getId() != attack.skill) {
				continue;
			}
			return;
		}
		if (attack.targets > 0 && player.getSkillCustomValue0(400031053) > 0L && player.getBuffedValue(400031053)
				&& player.getSkillCustomValue(400031054) == null
				&& !GameConstants.is_forceAtom_attack_skill(attack.skill)) {
			SecondaryStatEffect mirage = player.getBuffedEffect(SecondaryStat.SilhouetteMirage);
			MapleAtom atom = new MapleAtom(false, player.getId(), 56, true, 400031054, player.getTruePosition().x,
					player.getTruePosition().y);
			player.getClient().getSession().writeAndFlush((Object) CField.EffectPacket.showEffect(player, 0, 400031053,
					10, 0, 0, (byte) (player.isFacingLeft() ? 1 : 0), true, player.getPosition(), null, null));
			player.getMap().broadcastMessage(player, CField.EffectPacket.showEffect(player, 0, 400031053, 10, 0, 0,
					(byte) (player.isFacingLeft() ? 1 : 0), false, player.getPosition(), null, null), false);
			for (int i = 0; i < mirage.getBulletCount(); ++i) {
				atom.addForceAtom(new ForceAtom(3, Randomizer.rand(21, 29), Randomizer.rand(15, 16),
						Randomizer.rand(170, 172), (short) (90 + i * 210)));
			}
			player.setSkillCustomInfo(400031054, 0L, (int) player.getBuffedEffect(400031053).getT() * 1000);
			atom.setDwFirstTargetId(0);
			player.getMap().spawnMapleAtom(atom);
		}
		if (attack.skill == 64001009 || attack.skill == 64001010 || attack.skill == 64001011 || attack.skill == 1100012
				|| attack.skill == 3111010 || attack.skill == 3211010) {
			if (attack.skill == 1100012 || attack.skill == 3111010 || attack.skill == 3211010) {
				player.getClient().getSession()
						.writeAndFlush((Object) CField.EffectPacket.showEffect(player, attack.skill, attack.skill, 1, 0,
								monster != null ? monster.getObjectId() : 0,
								(byte) ((attack.facingleft >>> 4 & 0xF) == 8 ? 1 : 0), true,
								monster != null ? monster.getPosition() : attack.position, null, null));
				player.getMap().broadcastMessage(player,
						CField.EffectPacket.showEffect(player, attack.skill, attack.skill, 1, 0,
								monster != null ? monster.getObjectId() : 0,
								(byte) ((attack.facingleft >>> 4 & 0xF) == 8 ? 1 : 0), false,
								monster != null ? monster.getPosition() : attack.position, null, null),
						false);
			} else {
				player.getClient().getSession()
						.writeAndFlush((Object) CField.EffectPacket.showEffect(player, attack.skill, attack.skill, 1, 0,
								0, (byte) ((attack.facingleft >>> 4 & 0xF) == 8 ? 1 : 0), true, attack.chain, null,
								null));
				player.getMap().broadcastMessage(player,
						CField.EffectPacket.showEffect(player, attack.skill, attack.skill, 1, 0, 0,
								(byte) ((attack.facingleft >>> 4 & 0xF) == 8 ? 1 : 0), false, attack.chain, null, null),
						false);
			}
		}
		if (totDamage > 0L && player.getBuffedValue(400031047) && !player.getBuffedValue(400031048)) {
			SkillFactory.getSkill(400031048).getEffect(player.getSkillLevel(400031047)).applyTo(player, false);
		}
		if (attack.skill == 400021092 && player.getBuffedEffect(400021092) != null) {
			SecondaryStatEffect mischief = player.getBuffedEffect(400021092);
			if (player.getSkillCustomValue0(400021093) < (long) mischief.getZ()) {
				player.addSkillCustomInfo(400021093, 1L);
			}
			HashMap<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
			statups.put(SecondaryStat.SalamanderMischief, new Pair<Integer, Integer>(
					(int) player.getSkillCustomValue0(400021093), (int) player.getBuffLimit(400021092)));
			player.getClient().getSession()
					.writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, mischief, player));
		}
		if (attack.targets > 0 && (player.getBuffedValue(400031049) || player.getBuffedValue(400031051))
				&& player.getSkillCustomValue(400031049) == null) {
			for (MapleSummon s : player.getMap().getAllSummonsThreadsafe()) {
				if (s.getOwner().getId() != player.getId() || s.getSkill() != 400031049 && s.getSkill() != 400031051
						|| s == null || attack.skill == 400031049 || attack.skill == 400031050
						|| attack.skill == 400031051) {
					continue;
				}
				player.getClient().getSession()
						.writeAndFlush((Object) CField.SummonPacket.summonRangeAttack(s, s.getSkill()));
				player.setSkillCustomInfo(400031049, 0L, s.getSkill() == 400031051 ? 5000L : 1200L);
			}
		}
		if (GameConstants.isNightLord(player.getJob()) && !GameConstants.is_forceAtom_attack_skill(attack.skill)
				&& attack.skill < 400001001 && Randomizer
						.isSuccess(SkillFactory.getSkill(4110012).getEffect(player.getSkillLevel(4110012)).getProp())) {
			player.getClient().send(CField.getExpertThrow());
			player.setSkillCustomInfo(4110012, 1L, 0L);
		}
		if (player.getBuffedValue(11121005) && attack.summonattack == 0) {
			if (GameConstants.isPollingmoonAttackskill(attack.skill)) {
				SkillFactory.getSkill(11101032).getEffect(20).applyTo(player);
				if (player.getSkillLevel(400011048) > 0 && player.skillisCooling(400011048) && attack.targets > 0) {
					player.changeCooldown(400011048, -300);
				}
			} else if (GameConstants.isRisingsunAttackskill(attack.skill)) {
				SkillFactory.getSkill(11101032).getEffect(20).applyTo(player);
				if (player.getSkillLevel(400011048) > 0 && player.skillisCooling(400011048) && attack.targets > 0) {
					player.changeCooldown(400011048, -300);
				}
			}
		}
		if (multikill > 0) {
			player.CombokillHandler(monster, 1, multikill);
		}

		if (player.getKeyValue(1234, "400051015_on") == 1) {
			if (player.getKeyValue(400051015, "ww_serpent") != -1) {
				if (!(player.getStat().getHp() <= 0)) {
					if (!(player.getSkillLevel(400051015) >= 20)) {
						player.changeSingleSkillLevel(SkillFactory.getSkill(400051015), (byte) 20, (byte) 20);
					}
					if (!player.getBuffedValue(400051015)) {
						SkillFactory.getSkill(400051015).getEffect(1).applyTo(player, 0);
					}
				}
			}
		}

		if (player.getKeyValue(1234, "2121054_on") == 1) {
			if (player.getKeyValue(2121054, "ww_fireaura") != -1) {
				if (!(player.getStat().getHp() <= 0)) {
					if (!(player.getSkillLevel(2121054) >= 20)) {
						player.changeSingleSkillLevel(SkillFactory.getSkill(2121054), (byte) 20, (byte) 20);
					}
					if (!player.getBuffedValue(2121054)) {
						SkillFactory.getSkill(2121054).getEffect(1).applyTo(player, 0);
					}
				}
			}
		}
	}

	private static void parseFinalAttack(MapleCharacter player, AttackInfo attack, MapleMonster monster) {
		int[][] finalAttackReq = new int[][] { { 1100002, 1120013 }, { 1200002, 0 }, { 1300002, 0 },
				{ 2121007, 2120013 }, { 2221007, 2220014 }, { 3100001, 3120008 }, { 3200001, 0 }, { 4341054, 0 },
				{ 5121013, 0 }, { 5220020, 0 }, { 5311004, 1 }, { 11101002, 0 }, { 21100010, 21120012 },
				{ 22000015, 22110021 }, { 23100006, 23120012 }, { 31220007, 0 }, { 32121004, 32121011 },
				{ 33100009, 33120011 }, { 37000007, 0 }, { 51100002, 51120002 } };
		Item weapon = player.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11);
		for (int[] skill : finalAttackReq) {
			Integer value;
			if (weapon == null || attack.skill / 10000 == 8000 || attack.skill / 10000 > player.getJob()) {
				continue;
			}
			int finalSkill = GameConstants.getLinkedSkill(skill[0]);
			int advSkill = GameConstants.getLinkedSkill(skill[1]);
			SecondaryStatEffect eff = null;
			if (SkillFactory.getSkill(advSkill) != null && player.getSkillLevel(advSkill) > 0) {
				eff = SkillFactory.getSkill(advSkill).getEffect(player.getSkillLevel(advSkill));
			} else if (SkillFactory.getSkill(finalSkill) != null && player.getSkillLevel(finalSkill) > 0) {
				eff = SkillFactory.getSkill(finalSkill).getEffect(player.getSkillLevel(finalSkill));
			}
			if (advSkill == attack.skill || finalSkill == attack.skill
					|| (advSkill == 2120013 || advSkill == 2220014 || advSkill == 32121011)
							&& !player.skillisCooling(finalSkill)
					|| finalSkill == 4341054 && player.getBuffedEffect(SecondaryStat.WindBreakerFinal) == null
					|| finalSkill == 5311004
							&& ((value = player.getBuffedValue(SecondaryStat.Roulette)) == null || value != 1)) {
				break;
			}
			if (eff == null) {
				continue;
			}
			int chance = eff.getProp();
			int attackCount = Math.max(eff.getAttackCount(), 1);
			if (player.getBuffedEffect(SecondaryStat.FinalAttackProp) != null) {
				chance += player.getBuffedValue(SecondaryStat.FinalAttackProp).intValue();
			}
			if (eff.getSourceId() == 1120013 && player.getSkillLevel(1120048) > 0) {
				chance += SkillFactory.getSkill(1120048).getEffect(player.getSkillLevel(1120048)).getProp();
			}
			if (player.getBuffedValue(33121054)) {
				chance = 100;
			}

			if (!Randomizer.isSuccess(chance)) {
				break;
			}
			player.getClient().getSession().writeAndFlush((Object) CField.finalAttackRequest(attackCount, attack.skill,
					eff.getSourceId(), (weapon.getItemId() - 1000000) / 10000, monster));
			break;
		}
	}

	public static final void applyAttackMagic(AttackInfo attack, Skill theSkill, MapleCharacter player,
			SecondaryStatEffect effect, double maxDamagePerHit) {
		SecondaryStatEffect stst;
		MapleSummon summon;
		SecondaryStatEffect arcaneAim;
		if (attack.summonattack == 0) {
			// empty if block
		}
		if (ServerConstants.JUPITER_MODE) {
			int skillId = attack.skill;
			String skillName = SkillFactory.getSkillName(skillId);
			player.dropMessageGM(5, "[매직스킬] - " + skillId + " / " + skillName);
		}
		player.checkSpecialCoreSkills("prob", 0, effect);
		if (attack.skill != 0) {
			if (effect == null) {
				player.getClient().getSession().writeAndFlush((Object) CWvsContext.enableActions(player));
				return;
			}
			player.checkSpecialCoreSkills("cooltime", 0, effect);
			if (GameConstants.isMulungSkill(attack.skill)) {
				if (player.getMapId() / 10000 != 92502) {
					return;
				}
				if (player.getMulungEnergy() < 10000) {
					return;
				}
			} else if (GameConstants.isPyramidSkill(attack.skill)) {
				if (player.getMapId() / 1000000 != 926) {
					return;
				}
			} else if (GameConstants.isInflationSkill(attack.skill)) {
				if (player.getBuffedValue(SecondaryStat.Inflation) == null) {
					return;
				}
			} else if (!GameConstants.isNoApplySkill(attack.skill)) {
				Integer plusCount;
				SecondaryStatEffect oldEffect = SkillFactory.getSkill(attack.skill).getEffect(attack.skilllevel);
				int target = oldEffect.getMobCount();
				for (Skill skill : player.getSkills().keySet()) {
					int bonusSkillLevel = player.getSkillLevel(skill);
					if (bonusSkillLevel <= 0 || skill.getId() == attack.skill) {
						continue;
					}
					SecondaryStatEffect bonusEffect = skill.getEffect(bonusSkillLevel);
					target += bonusEffect.getTargetPlus();
					target += bonusEffect.getTargetPlus_5th();
				}
				if (oldEffect.getMobCount() > 0 && player.getSkillLevel(70000047) > 0) {
					target += SkillFactory.getSkill(70000047).getEffect(player.getSkillLevel(70000047)).getTargetPlus();
				}
				boolean useBulletCount = oldEffect.getBulletCount() > 1;
				int attackCount = useBulletCount ? oldEffect.getBulletCount() : oldEffect.getAttackCount();
				int bulletBonus = GameConstants.bullet_count_bonus(attack.skill);
				int attackBonus = GameConstants.attack_count_bonus(attack.skill);
				if (bulletBonus != 0 && useBulletCount) {
					if (player.getSkillLevel(bulletBonus) > 0) {
						attackCount += SkillFactory.getSkill(bulletBonus).getEffect(player.getSkillLevel(bulletBonus))
								.getBulletCount();
					}
				} else if (attackBonus != 0 && !useBulletCount && player.getSkillLevel(attackBonus) > 0) {
					attackCount += SkillFactory.getSkill(attackBonus).getEffect(player.getSkillLevel(attackBonus))
							.getAttackCount();
				}
				if ((plusCount = player.getBuffedValue(SecondaryStat.Buckshot)) != null) {
					attackCount *= plusCount.intValue();
				}
				if (player.getBuffedEffect(SecondaryStat.ShadowPartner) != null
						|| player.getBuffedEffect(SecondaryStat.Larkness) != null) {
					attackCount *= 2;
				}
				if (player.getSkillLevel(3220015) > 0 && attackCount >= 2) {
					attackCount += SkillFactory.getSkill(3220015).getEffect(player.getSkillLevel(3220015)).getX();
				}
				if (player.getBuffedEffect(SecondaryStat.VengeanceOfAngel) != null && attack.skill == 2321007) {
					attackCount += player.getBuffedEffect(SecondaryStat.VengeanceOfAngel).getY();
				}
				Integer attackCountX = player.getBuffedValue(SecondaryStat.AttackCountX);
				int[] blowSkills = new int[] { 32001000, 32101000, 32111002, 32121002, 400021007 };
				if (attackCountX != null) {
					for (int blowSkill : blowSkills) {
						if (attack.skill != blowSkill) {
							continue;
						}
						attackCount += attackCountX.intValue();
					}
				}
				if (attack.targets > target) {
					player.dropMessageGM(-5,
							attack.skill + " 몹 개체수 > 클라이언트 계산 : " + attack.targets + " / 서버 계산 : " + target);
					player.dropMessageGM(-6, "개체수가 계산값보다 많습니다.");
				}
				if (attack.hits > attackCount) {
					player.dropMessageGM(-5,
							attack.skill + " 공격 횟수 > 클라이언트 계산 : " + attack.hits + " / 서버 계산 : " + attackCount);
					player.dropMessageGM(-6, "공격 횟수가 계산값보다 많습니다.");
				}
			}
		}
		MapleMonster monster = null;
		PlayerStats stats = player.getStat();
		// Element element = player.getBuffedValue(SecondaryStat.ElementalReset) != null
		// ? Element.NEUTRAL : theSkill.getElement();
		double MaxDamagePerHit = 0.0;
		long totDamage = 0L;
		boolean heiz = false;
		int multikill = 0;
		short CriticalDamage = stats.critical_rate;
		MapleMap map = player.getMap();
		for (AttackPair oned : attack.allDamage) {
			monster = map.getMonsterByOid(oned.objectId);
			if (monster == null || monster.getLinkCID() > 0) {
				continue;
			}
			boolean Tempest = false;
			long totDamageToOneMonster = 0L;
			MapleMonsterStats monsterstats = monster.getStats();
			long fixeddmg = monsterstats.getFixedDamage();
			int overallAttackCount = 0;
			if (monster.getId() >= 9833070 && monster.getId() <= 9833074) {
				continue;
			}

			// 6차 구현
			switch (attack.skill) {
			default: {
				if (player.getBuffedValue(2241504)) { // 오리진 프로즌 라이트닝 구현
					int attackId = 1241501;

					if (!player.skillisCooling(attackId)) {
						ArrayList<RangeAttack> attacks = new ArrayList<>();

						attacks.add(new RangeAttack(attackId, monster.getPosition(),
								((attack.facingleft >>> 4 & 0xF) == 0) ? 0 : 1, 0, 0));
						player.getClient().getSession().writeAndFlush(CField.rangeAttack(attackId, attacks));

						player.addCoolTime(attackId, 500);
					}
				}

				break;
			}
			}

			if (player.getKeyValue(21770, "500001000") == 500001001 && !player.skillisCooling(500001001)) { // HEXA 솔
																											// 야누스 구현
				ArrayList<RangeAttack> attacks = new ArrayList<>();

				attacks.add(new RangeAttack(500001001, player.getPosition(),
						((attack.facingleft >>> 4 & 0xF) == 0) ? 0 : 1, 0, 1));
				player.getClient().getSession().writeAndFlush(CField.rangeAttack(500001001, attacks));

				int count = 0;
				for (MapleMonster mob : player.getMap().getAllMonster()) {
					if (count >= 9) {
						break;
					}

					SecondAtom2 sa = SkillFactory.getSkill(500001001).getSecondAtoms().get(0);
					sa.setTarget(mob.getObjectId());

					player.createSecondAtom(sa, new Point(monster.getPosition().x, monster.getPosition().y), false);

					count++;
				}
			}

			if (attack.skill == 2121011 || attack.skill == 2141003) { // 플레임 헤이즈, 플레임 헤이즈 VI 구현
				int skillId = 2111003;

				SecondaryStatEffect eff = SkillFactory.getSkill(skillId).getEffect(player.getSkillLevel(skillId));

				MapleMist mist = new MapleMist(eff.calculateBoundingBox(monster.getPosition(), player.isFacingLeft()),
						player, eff, 15000, (byte) 0);
				player.getMap().spawnMist(mist, false);
			}

			if (attack.skill == 2241000) { // 체인 라이트닝 VI 구현
				int skillId = 2241001;

				int count = 0;
				for (MapleMist mist : player.getMap().getAllMistsThreadsafe()) {
					if (mist.getOwnerId() == player.getId()) {
						if (mist.getSourceSkill().getId() == skillId) {
							count++;
						}
					}
				}

				if (count < 4) {
					if (!player.skillisCooling(skillId)) {
						SecondaryStatEffect eff = SkillFactory.getSkill(skillId)
								.getEffect(player.getSkillLevel(skillId));

						MapleMonster mob = player.getMap().getAllMonster()
								.get(Randomizer.rand(0, (player.getMap().getAllMonster().size() - 1)));

						MapleMist mist = new MapleMist(
								eff.calculateBoundingBox(mob.getPosition(), player.isFacingLeft()), player, eff, 4000,
								(byte) 0);
						mist.setPosition(mob.getPosition());
						player.getMap().spawnMist(mist, false);
					} else {
						if (Randomizer.isSuccess(20)) {
							SecondaryStatEffect eff = SkillFactory.getSkill(skillId)
									.getEffect(player.getSkillLevel(skillId));

							MapleMonster mob = player.getMap().getAllMonster()
									.get(Randomizer.rand(0, (player.getMap().getAllMonster().size() - 1)));

							MapleMist mist = new MapleMist(
									eff.calculateBoundingBox(mob.getPosition(), player.isFacingLeft()), player, eff,
									4000, (byte) 0);
							mist.setPosition(mob.getPosition());
							player.getMap().spawnMist(mist, false);
						}
					}

					player.addCoolTime(skillId, 7000);
				}
			}

			for (Pair<Long, Boolean> eachde : oned.attack) {
				long eachd = (Long) eachde.left;
				overallAttackCount = (byte) (overallAttackCount + 1);
				if (fixeddmg != -1L) {
					eachd = monsterstats.getOnlyNoramlAttack() ? 0L : fixeddmg;
				} else if (monsterstats.getOnlyNoramlAttack()) {
					eachd = 0L;
				}
				totDamageToOneMonster += eachd;
				player.checkSpecialCoreSkills("attackCount", monster.getObjectId(), effect);
				player.checkSpecialCoreSkills("attackCountMob", monster.getObjectId(), effect);
			}
			totDamage += totDamageToOneMonster;

			if (!player.gethottimebossattackcheck()) {
				player.sethottimebossattackcheck(true);
			}

			if (monster.getId() != 8900002 && monster.getId() != 8900102) {
				player.checkMonsterAggro(monster);
			}
			if (!(attack.skill == 0 || SkillFactory.getSkill(attack.skill).isChainAttack() || effect.isMist()
					|| effect.getSourceId() == 400021030 || GameConstants.isLinkedSkill(attack.skill)
					|| GameConstants.isNoApplySkill(attack.skill) || GameConstants.isNoDelaySkill(attack.skill)
					|| monster.getStats().isBoss()
					|| !(player.getTruePosition().distanceSq(monster.getTruePosition()) > GameConstants
							.getAttackRange(effect, player.getStat().defRange)))) {
				player.dropMessageGM(-5, "타겟이 범위를 벗어났습니다.");
			}
			if (player.getSkillLevel(80002762) > 0) {
				if (player.getBuffedEffect(SecondaryStat.EmpiricalKnowledge) != null
						&& player.empiricalKnowledge != null) {
					if (map.getMonsterByOid(player.empiricalKnowledge.getObjectId()) != null) {
						if (monster.getObjectId() != player.empiricalKnowledge.getObjectId()
								&& monster.getMobMaxHp() > player.empiricalKnowledge.getMobMaxHp()) {
							player.empiricalStack = 0;
							player.empiricalKnowledge = monster;
						}
					} else {
						player.empiricalStack = 0;
						player.empiricalKnowledge = monster;
					}
				} else if (player.empiricalKnowledge != null) {
					if (monster.getMobMaxHp() > player.empiricalKnowledge.getMobMaxHp()) {
						player.empiricalKnowledge = monster;
					}
				} else {
					player.empiricalKnowledge = monster;
				}
			}
			if (totDamageToOneMonster <= 0L && attack.skill != 27101101) {
				continue;
			}
			// [ ExtremeBoss Damage Reduce]

			/*
			 * if (player.isExtremeMode()) { int reducePercent =
			 * MapleExtremeBoss.getDamReduceByMap(player.getMapId()); long originDamage =
			 * totDamageToOneMonster; long reduceDamage = (totDamageToOneMonster /
			 * reducePercent); totDamageToOneMonster -= reduceDamage; //
			 * System.out.println(player.getName() + " | 기존 데미지 : " +
			 * player.StringToComma(originDamage) + " | 데미지 감소율("+reducePercent+"%, "
			 * +player.StringToComma(reduceDamage) + ") | 감소율 적용 데미지 : " +
			 * player.StringToComma(originDamage-reduceDamage)); }
			 */
			monster.damage(player, totDamageToOneMonster, true, attack.skill);
			applyAddAttack(player, monster, attack);

			if (monster.getId() >= 9500650 && monster.getId() <= 9500654 && totDamageToOneMonster > 0L
					&& player.getGuild() != null) {
				player.getGuild().updateGuildScore(totDamageToOneMonster);
			}

			if (!GameConstants.사출기(attack.skill) || player.getMapId() == 921170004 || player.getMapId() == 921170011
					|| player.getMapId() == 921170012) {
				if (attack.targets > 0 && player.getKeyValue(99999, "tripling") > 0 && attack.skill != 1311020
						&& !GameConstants.isTryFling(attack.skill) && attack.skill != 400031031
						&& attack.skill != 400031001 && attack.skill != 13111020 && attack.skill != 13121054
						&& attack.skill != 13101022 && attack.skill != 13110022 && attack.skill != 13120003
						&& attack.skill != 13111020 && attack.skill != 400001018 && attack.skill != 500001001
						|| player.getMapId() == 921170004 || player.getMapId() == 921170011
						|| player.getMapId() == 921170012
						|| attack.targets > 0 && attack.skill != 1311020
								&& player.getBuffedEffect(SecondaryStat.TryflingWarm) != null
								&& !GameConstants.isTryFling(attack.skill) && attack.skill != 400031031
								&& attack.skill != 400031001 && attack.skill != 13111020 && attack.skill != 13121054
								&& attack.skill != 13101022 && attack.skill != 13110022 && attack.skill != 13120003
								&& attack.skill != 13111020 && attack.skill != 400001018 && attack.targets > 0) {
					int skillid = 0;
					if (player.getSkillLevel(13141001) > 0) { // 트라이플링 윔 VI 구현
						skillid = 13141001;
					} else if (player.getKeyValue(99999, "tripling") > 0
							|| player.getSkillLevel(SkillFactory.getSkill(13120003)) > 0) {
						skillid = 13120003;
						if (player.getSkillLevel(SkillFactory.getSkill(13120003)) < 30) {
							player.teachSkill(13120003, 30);
						}
					} else if (player.getSkillLevel(SkillFactory.getSkill(13110022)) > 0) {
						skillid = 13110022;
					} else if (player.getSkillLevel(SkillFactory.getSkill(13101022)) > 0) {
						skillid = 13100022;
					}

					if (skillid != 0) {
						Skill trskill = SkillFactory.getSkill(skillid);
						if (Randomizer.rand(1, 100) <= (skillid == 13100022 ? 5 : (skillid == 13110022 ? 10 : 20))) {
							int n = skillid == 13120003 ? 13120010
									: (skillid = skillid == 13110022 ? 13110027 : 13100027);
							if (player.getSkillLevel(skillid) <= 0) {
								player.changeSkillLevel(SkillFactory.getSkill(skillid),
										(byte) player.getSkillLevel(trskill), (byte) player.getSkillLevel(trskill));
							}
						}
						List<MapleMapObject> objs = player.getMap().getMapObjectsInRange(player.getTruePosition(),
								500000.0, Arrays.asList(new MapleMapObjectType[] { MapleMapObjectType.MONSTER }));
						SecondaryStatEffect eff = trskill.getEffect(player.getSkillLevel(skillid));
						int maxcount = eff.getX() + (int) (player.getKeyValue(99999, "triplingBonus") > 0
								? (player.getKeyValue(99999, "triplingBonus") * 1)
								: 0);
						if (objs.size() > 0) {
							int trychance = 100;
							// int trychance = trskill.getEffect(player.getSkillLevel(trskill)).getProp() +
							// (int) (player.getKeyValue(99999, "triplingBonus") > 0 ?
							// (player.getKeyValue(99999, "triplingBonus") * 10) : 0);
							if (player.getSkillLevel(13120044) > 0) {

								trychance += SkillFactory.getSkill(13120044).getEffect(1).getProp();
							}
							if (attack.skill == 400031004 || attack.skill == 400031003) {
								trychance /= 2;
							}
							if (Randomizer.isSuccess(trychance) || player.getMapId() == 921170004
									|| player.getMapId() == 921170011 || player.getMapId() == 921170012) {
								MapleAtom atom = new MapleAtom(false, player.getId(), 7, true, skillid,
										player.getTruePosition().x, player.getTruePosition().y);
								ArrayList<Integer> monsters = new ArrayList<Integer>();
								if (player.getMapId() == 921170004 || player.getMapId() == 921170011
										|| player.getMapId() == 921170012) {
									maxcount = 1;
									if (player.자동사냥 < System.currentTimeMillis()) {
										maxcount = 5;
										player.자동사냥 = System.currentTimeMillis() + (30 * 1000);
									}
								}
								for (int i = 0; i < Randomizer
										.rand(1 + (int) (player.getKeyValue(99999, "triplingBonus") > 0
												? (player.getKeyValue(99999, "triplingBonus") * 1)
												: 0), maxcount); ++i) {
									boolean upgrade = Randomizer.isSuccess(eff.getSubprop());
									monsters.add(objs.get(Randomizer.nextInt(objs.size())).getObjectId());
									atom.addForceAtom(new ForceAtom(upgrade ? 3 : 1, Randomizer.rand(41, 49),
											Randomizer.rand(4, 8), Randomizer.nextBoolean() ? Randomizer.rand(171, 174)
													: Randomizer.rand(6, 9),
											(short) Randomizer.rand(42, 47)));
								}
								atom.setDwTargets(monsters);
								player.getMap().spawnMapleAtom(atom);
							}
						}
					}
				}
			}

			// 조디악 레이 fix
			if (player.getBuffedValue(400021073)) {
				ArrayList atoms = new ArrayList();
				MapleSummon s = null;
				for (MapleSummon summon2 : player.getSummons()) {
					if (summon2.getSkill() != 400021073) {
						continue;
					}
					s = summon2;
				}
				if (s == null) {
					player.dropMessage(6, "Zodiac Ray Null Point");
				} else {
					MapleAtom atom = new MapleAtom(true, monster.getObjectId(), 29, true, 400021073,
							monster.getTruePosition().x, monster.getTruePosition().y);
					atom.setDwUserOwner(player.getId());
					atom.setDwFirstTargetId(0);
					atom.addForceAtom(new ForceAtom(5, 37, Randomizer.rand(5, 10), 62, 0));
					player.getMap().spawnMapleAtom(atom);
					player.setEnergyBurst(player.getEnergyBurst() + 5);
					player.getClient().getSession().writeAndFlush((Object) CField.SummonPacket.updateSummon(s, 13));
					HashMap<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
					statups.put(SecondaryStat.IndieSummon, new Pair<Integer, Integer>(player.getEnergyBurst() + 21,
							(int) player.getBuffLimit(400021073)));
					player.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups,
							player.getBuffedEffect(400021073), player));

					// System.err.println("에너지 " + player.getEnergyBurst() );
				}

				if (player.getEnergyBurst() > 22) {
					// 조디악 레이 fix
					player.getClient().getSession().writeAndFlush((Object) CField.SummonPacket.damageSummon(s));
					player.setEnergyBurst(0);
				}

			}
			if (GameConstants.isFusionSkill(attack.skill) && attack.targets > 0
					&& player.getSkillCustomValue(22170070) == null) {
				SecondaryStatEffect magicWreck = player.getSkillLevel(22170070) > 0
						? SkillFactory.getSkill(22170070).getEffect(player.getSkillLevel(22170070))
						: SkillFactory.getSkill(22141017).getEffect(player.getSkillLevel(22141017));
				if (player.getMap().getWrecks().size() < 15) {
					int x = Randomizer.rand(-100, 150);
					int y = Randomizer.rand(-50, 70);
					MapleMagicWreck mw = new MapleMagicWreck(player, magicWreck.getSourceId(),
							new Point(monster.getTruePosition().x + x, monster.getTruePosition().y + y), 20000);
					player.getMap().spawnMagicWreck(mw);
					player.setSkillCustomInfo(22170070, 0L, player.getSkillLevel(22170070) > 0 ? 400L : 600L);
				}
			}
			if (player.getSkillLevel(32101009) > 0 && !monster.isAlive()
					&& player.getBuffedEffect(SecondaryStat.DebuffIncHp) == null) {
				player.addHP(player.getStat().getCurrentMaxHp() * (long) SkillFactory.getSkill(32101009)
						.getEffect(player.getSkillLevel(32101009)).getKillRecoveryR() / 100L);
			}
			if (monster.isBuffed(MonsterStatus.MS_MCounter)
					&& player.getBuffedEffect(SecondaryStat.IgnorePImmune) == null
					&& player.getBuffedEffect(SecondaryStat.IgnorePCounter) == null
					&& player.getBuffedEffect(SecondaryStat.IgnoreAllCounter) == null
					&& player.getBuffedEffect(SecondaryStat.IgnoreAllImmune) == null
					&& !SkillFactory.getSkill(attack.skill).isIgnoreCounter()) {
				player.addHP(-monster.getBuff(MonsterStatus.MS_MCounter).getValue());
			}

			/*
			 * if (attack.targets > 0 && attack.skill != 1311020 &&
			 * player.getSkillLevel(13100022) > 0 && !GameConstants.isTryFling(attack.skill)
			 * && attack.skill != 400031031 && attack.skill != 400031001 && attack.skill !=
			 * 13111020 && attack.skill != 13121054 && attack.skill != 13101022 &&
			 * attack.skill != 13110022 && attack.skill != 13120003 && attack.skill !=
			 * 13111020 && attack.skill != 400001018) { int skillid = 0; if
			 * (player.getSkillLevel(SkillFactory.getSkill(13120003)) > 0) { skillid =
			 * 13120003; } else if (player.getSkillLevel(SkillFactory.getSkill(13110022)) >
			 * 0) { skillid = 13110022; } else if
			 * (player.getSkillLevel(SkillFactory.getSkill(13120010)) > 0) { skillid =
			 * 13120010; } if (skillid != 0) { Skill trskill =
			 * SkillFactory.getSkill(skillid); if (Randomizer.rand(1, 100) <= (skillid ==
			 * 13100022 ? 5 : (skillid == 13110022 ? 10 : 20))) { int n = skillid ==
			 * 13120003 ? 13120010 : (skillid = skillid == 13110022 ? 13110027 : 13100027);
			 * if (player.getSkillLevel(skillid) <= 0) {
			 * player.changeSkillLevel(SkillFactory.getSkill(skillid), (byte)
			 * player.getSkillLevel(trskill), (byte) player.getSkillLevel(trskill)); } }
			 * List<MapleMapObject> objs =
			 * player.getMap().getMapObjectsInRange(player.getTruePosition(), 500000.0,
			 * Arrays.asList(new MapleMapObjectType[]{MapleMapObjectType.MONSTER}));
			 * SecondaryStatEffect eff = trskill.getEffect(player.getSkillLevel(skillid));
			 * int maxcount = eff.getX(); if (objs.size() > 0) { int trychance =
			 * trskill.getEffect(player.getSkillLevel(trskill)).getProp(); if
			 * (player.getSkillLevel(13120044) > 0) { trychance +=
			 * SkillFactory.getSkill(13120044).getEffect(1).getProp(); } if (attack.skill ==
			 * 400031004 || attack.skill == 400031003) { trychance /= 2; } if
			 * (Randomizer.isSuccess(trychance)) { MapleAtom atom = new MapleAtom(false,
			 * player.getId(), 7, true, skillid, player.getTruePosition().x,
			 * player.getTruePosition().y); ArrayList<Integer> monsters = new
			 * ArrayList<Integer>(); for (int i = 0; i < Randomizer.rand(1, maxcount); ++i)
			 * { boolean upgrade = Randomizer.isSuccess(eff.getSubprop());
			 * monsters.add(objs.get(Randomizer.nextInt(objs.size())).getObjectId());
			 * atom.addForceAtom(new ForceAtom(upgrade ? 3 : 1, Randomizer.rand(41, 49),
			 * Randomizer.rand(4, 8), Randomizer.nextBoolean() ? Randomizer.rand(171, 174) :
			 * Randomizer.rand(6, 9), (short) Randomizer.rand(42, 47))); }
			 * atom.setDwTargets(monsters); player.getMap().spawnMapleAtom(atom); } } } }
			 */
			// 작업 - 플레임위자드 엘리멘트
			if (GameConstants.isFlameWizard(player.getJob())) {
				int skillLevel = 0;
				int skillid = 0;

				if (player.getLevel() < 30) { // 엘리멘트 : 플레임 I
					skillid = 12000022;
					skillLevel = player.getSkillLevel(skillid);
					SkillFactory.getSkill(skillid).getEffect(skillLevel).applyTo(player);
				}
				if (player.getLevel() >= 30 && player.getLevel() < 60) { // 엘리멘트 : 플레임 II
					skillid = 12100026;
					skillLevel = player.getSkillLevel(skillid);
					SkillFactory.getSkill(skillid).getEffect(skillLevel).applyTo(player);
				}

				if (player.getLevel() >= 60 && player.getLevel() < 100) { // 엘리멘트 : 플레임 III
					skillid = 12110024;
					skillLevel = player.getSkillLevel(skillid);
					SkillFactory.getSkill(skillid).getEffect(skillLevel).applyTo(player);
				}

				if (player.getLevel() >= 100) { // 엘리멘트 : 플레임 IV
					skillid = 12120007;
					skillLevel = player.getSkillLevel(skillid);
					SkillFactory.getSkill(skillid).getEffect(skillLevel).applyTo(player);
				}
			}

			switch (attack.skill) {
			case 2101004:
			case 2111002:
			case 2121005:
			case 2121006:
			case 2121007:
			case 2141000: // 플레임 스윕 VI 구현
			case 400021001: {
				if (player.getBuffedEffect(SecondaryStat.WizardIgnite) == null
						|| !player.getBuffedEffect(SecondaryStat.WizardIgnite).makeChanceResult()) {
					break;
				}
				SkillFactory.getSkill(2100010).getEffect(player.getSkillLevel(2101010)).applyTo(player,
						monster.getTruePosition());
			}
			}
			if (effect != null && monster.isAlive()) {
				Object ignition;
				ArrayList<Triple<MonsterStatus, MonsterStatusEffect, Long>> statusz = new ArrayList<Triple<MonsterStatus, MonsterStatusEffect, Long>>();
				ArrayList<Pair<MonsterStatus, MonsterStatusEffect>> applys = new ArrayList<Pair<MonsterStatus, MonsterStatusEffect>>();
				boolean suc = effect.makeChanceResult();
				switch (attack.skill) {
				case 2101004:
				case 2111002:

				case 2121006:
				case 2121007:
				case 2141000: { // 플레임 스윕 VI 구현
					if (attack.skill != 2121006 && attack.skill != 2141000) {
						break;
					}

					statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun,
							new MonsterStatusEffect(attack.skill, effect.getDuration()), 1L));
					statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Burned,
							new MonsterStatusEffect(attack.skill, effect.getDOTTime()),
							(long) effect.getDOT() * totDamageToOneMonster / (long) attack.allDamage.size() / 10000L));
					break;
				}
				case 2121052:
				case 2121054:
				case 2121005:
				case 2121055:
				case 2121011: {
					player.setDotDamage(
							(long) effect.getDOT() * totDamageToOneMonster / (long) attack.allDamage.size() / 10000L);
					statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Showdown,
							new MonsterStatusEffect(attack.skill, effect.getDuration()), 1L));
					statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Speed,
							new MonsterStatusEffect(attack.skill, effect.getDuration()), Long.valueOf(effect.getX())));
					statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Burned,
							new MonsterStatusEffect(attack.skill, effect.getDOTTime()), player.getDotDamage()));
					player.setFlameHeiz(monster.getTruePosition());
					heiz = true;
					break;
				}
				case 2101005: {
					statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Burned,
							new MonsterStatusEffect(attack.skill, effect.getDOTTime()),
							(long) effect.getDOT() * totDamageToOneMonster / (long) attack.allDamage.size() / 10000L));
					break;
				}
				case 2111003: {
					if (monster.isBuffed(2121011)) {
						break;
					}
					SecondaryStatEffect bonusTime = null;
					SecondaryStatEffect bonusDam = null;
					if (player.getSkillLevel(2120044) > 0) {
						bonusTime = SkillFactory.getSkill(2120044).getEffect(player.getSkillLevel(2120044));
					}
					if (player.getSkillLevel(2120045) > 0) {
						bonusDam = SkillFactory.getSkill(2120045).getEffect(player.getSkillLevel(2120045));
					}
					player.setDotDamage(
							(long) effect.getDOT() * totDamageToOneMonster / (long) attack.allDamage.size() / 10000L);
					statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Burned,
							new MonsterStatusEffect(attack.skill,
									effect.getDOTTime() + (bonusTime != null ? bonusTime.getDOTTime() : (short) 0)),
							player.getDotDamage()));
					break;
				}

				case 2201004:
				case 2201008:
				case 2201009:
				case 2211002:
				case 2211006:
				case 2211010:
				case 2220014:
				case 2221003:
				case 2221011:
				case 2221012:
				case 2221054:
				case 400020002: {
					if (attack.skill != 2221011) {
						statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Speed,
								new MonsterStatusEffect(attack.skill, 10000), Long.valueOf(effect.getV())));
						if (monster.getBuff(MonsterStatus.MS_Speed) == null && monster.getFreezingOverlap() > 0) {
							monster.setFreezingOverlap(0);
						}
						if (monster.getFreezingOverlap() < 5) {
							monster.setFreezingOverlap((byte) (monster.getFreezingOverlap() + 1));
						}
					}
					if (attack.skill == 2221011) {
						statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Freeze,
								new MonsterStatusEffect(attack.skill, 13000), 1L));
					}
					statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_IndiePdr,
							new MonsterStatusEffect(attack.skill, 13000), Long.valueOf(effect.getX())));
					statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_IndieMdr,
							new MonsterStatusEffect(attack.skill, 13000), Long.valueOf(effect.getY())));
					break;
				}
				case 2201005:
				case 2211003:
				case 2211011:
				case 2221006: {
					if (monster.getBuff(MonsterStatus.MS_Speed) == null && monster.getFreezingOverlap() > 0) {
						monster.setFreezingOverlap(0);
					}
					if (monster.getFreezingOverlap() > 0) {
						monster.setFreezingOverlap((byte) (monster.getFreezingOverlap() - 1));
						if (monster.getFreezingOverlap() <= 0) {
							monster.cancelStatus(MonsterStatus.MS_Speed, monster.getBuff(2201008));
						} else {
							statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Speed,
									new MonsterStatusEffect(2201008, 8000), -75L));
						}
					}
					if (attack.skill != 2221006) {
						break;
					}
					statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun,
							new MonsterStatusEffect(attack.skill, effect.getDuration()), 1L));
					break;
				}
				case 2221052:
				case 400021031:
				case 400021094: {
					if (monster.getBuff(MonsterStatus.MS_Speed) == null && monster.getFreezingOverlap() > 0) {
						monster.setFreezingOverlap(0);
					}
					if (attack.skill == 400021094) {
						monster.addSkillCustomInfo(400021094, 1L);
						if (monster.getFreezingOverlap() > 0) {
							if (attack.skill == 400021094 && monster.getCustomValue0(400021094) >= 5L) {
								monster.removeCustomInfo(400021094);
								monster.setFreezingOverlap((byte) (monster.getFreezingOverlap() - 1));
								statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Speed,
										new MonsterStatusEffect(2201008, 8000), -75L));
							}
						} else {
							monster.cancelStatus(MonsterStatus.MS_Speed, monster.getBuff(2201008));
						}
					}
					if (monster.getFreezingOverlap() > 0) {
						monster.setFreezingOverlap((byte) (monster.getFreezingOverlap() - 1));
						statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Speed,
								new MonsterStatusEffect(2201008, 8000), -75L));
						break;
					}
					if (monster.getFreezingOverlap() > 0) {
						break;
					}
					monster.cancelStatus(MonsterStatus.MS_Speed, monster.getBuff(2201008));
					break;
				}
				case 2311004: {
					statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun,
							new MonsterStatusEffect(attack.skill, effect.getDuration()), 1L));
					break;
				}
				case 2321007: {
					SecondaryStatEffect effect2 = SkillFactory.getSkill(attack.skill)
							.getEffect(player.getSkillLevel(attack.skill));
					if (monster.getBuff(MonsterStatus.MS_IndieUNK) == null) {
						monster.removeCustomInfo(attack.skill);
					}
					if (monster.getCustomValue0(attack.skill) < (long) effect2.getQ()) {// MS_IndiePdr
						monster.addSkillCustomInfo(attack.skill, 1L);
					}
					statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_IndieUNK,
							new MonsterStatusEffect(attack.skill, 20000), (monster.getCustomValue0(attack.skill))));
					// statusz.add(new Triple<MonsterStatus, MonsterStatusEffect,
					// Long>(MonsterStatus.MS_IndieMdr, new MonsterStatusEffect(2320014,
					// effect2.getW() * 1000), -((long)effect2.getX() *
					// monster.getCustomValue0(2320014))));
					break;
				}
				case 27101101: {
					statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun,
							new MonsterStatusEffect(attack.skill, effect.getDuration()), 1L));
					break;
				}
				case 27121052:
				case 162121041: {
					statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Freeze,
							new MonsterStatusEffect(attack.skill, effect.getDuration()),
							Long.valueOf(effect.getDuration())));
					break;
				}
				case 32101001:
				case 32111016:
				case 400021088: {
					if (attack.skill == 32101001) {
						statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(
								MonsterStatus.MS_Stun, new MonsterStatusEffect(attack.skill, SkillFactory
										.getSkill(32111016).getEffect(player.getSkillLevel(32111016)).getDuration()),
								1L));
					}
					statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_DarkLightning,
							new MonsterStatusEffect(32111016, SkillFactory.getSkill(32111016)
									.getEffect(player.getSkillLevel(32111016)).getDuration()),
							1L));
					break;
				}
				case 32121004:
				case 32121011: {
					statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Stun,
							new MonsterStatusEffect(attack.skill, effect.getDuration()), 1L));
					break;
				}
				case 142001000:
				case 142100000:
				case 142100001:
				case 142110000:
				case 142110001: {
					statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Burned,
							new MonsterStatusEffect(142110000, effect.getDOTTime()), 1L));
					break;
				}
				case 142121031: {
					statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Freeze,
							new MonsterStatusEffect(attack.skill,
									attack.skill == 400011015 ? effect.getW() * 1000
											: (effect.getSubTime() > 0 ? effect.getSubTime() : effect.getDuration())),
							Long.valueOf(effect.getDuration())));
					break;
				}
				case 400021028: {
					statusz.add(new Triple<MonsterStatus, MonsterStatusEffect, Long>(MonsterStatus.MS_Burned,
							new MonsterStatusEffect(attack.skill, effect.getDOTTime()),
							(long) effect.getDOT() * totDamageToOneMonster / (long) attack.allDamage.size() / 10000L));
					break;
				}
				case 400021096: {
					applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_Treasure,
							new MonsterStatusEffect(attack.skill, effect.getDuration(), player.getId())));
					break;
				}
				case 142120002: {
					applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_IndieMdr,
							new MonsterStatusEffect(attack.skill, effect.getDuration(), -effect.getX())));
					applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_IndiePdr,
							new MonsterStatusEffect(attack.skill, effect.getDuration(), -effect.getX())));
					break;
				}
				case 12111022: {
					statusz.add(new Triple(MonsterStatus.MS_Freeze,
							new MonsterStatusEffect(attack.skill, effect.getDuration()),
							Long.valueOf(effect.getDuration())));
					break;
				}
				default: {
//                        if (!player.getBuffedValue(12101024) || monster.isBuffed(MonsterStatus.MS_Ember) || !((SecondaryStatEffect) (ignition = player.getBuffedEffect(12101024))).makeChanceResult()) {
//                            break;
//                        }
//                        long dam = (long) ((SecondaryStatEffect) ignition).getDOT() * totDamageToOneMonster / (long) attack.allDamage.size() / 1000L;
//                        player.gainIgnition();
//                        applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_Ember, new MonsterStatusEffect(12101024, ((SecondaryStatEffect) ignition).getDOTTime(), 1L)));
//                        applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(MonsterStatus.MS_Burned, new MonsterStatusEffect(12101024, ((SecondaryStatEffect) ignition).getDOTTime(), player.getSkillLevel(12120050) > 0 ? dam + dam / 100L * 20L : dam)));
//                        break;
				}
				}
				for (final Triple<MonsterStatus, MonsterStatusEffect, Long> status : statusz) {
					if (status.left != null && status.mid != null && suc) {
						if (status.left == MonsterStatus.MS_Burned && status.right < 0L) {
							status.right = ((long) status.right & 0xFFFFFFFFL);
						}
						status.mid.setValue(status.right);
						applys.add(new Pair<MonsterStatus, MonsterStatusEffect>(status.left, status.mid));
					}
				}
				if (monster != null && monster.isAlive()) {
					monster.applyStatus(player.getClient(), applys, effect);
				}
				if (GameConstants.isHolyAttack(attack.skill)
						&& monster.isBuffed(MonsterStatus.MS_ElementResetBySummon)) {
					monster.cancelStatus(MonsterStatus.MS_ElementResetBySummon,
							monster.getBuff(MonsterStatus.MS_ElementResetBySummon));
				}
			}
			if (player.getSkillLevel(60030241) > 0 || player.getSkillLevel(80003015) > 0) {
				int skillid;
				int n = player.getSkillLevel(60030241) > 0 ? 60030241
						: (skillid = player.getSkillLevel(80003015) > 0 ? 80003015 : 0);
				if (n > 0 && monster != null) { // n
					if (monster.getStats().isBoss()) {
						if (monster.isAlive()) {
							player.handlePriorPrepaRation(n, 2);
						}
					} else if (!monster.isAlive()) {
						player.handlePriorPrepaRation(n, 1);
					}
				}
			}
			if (player.getBuffedValue(SecondaryStat.BMageDeath) != null && player.skillisCooling(32001114)
					&& GameConstants.isBMDarkAtackSkill(attack.skill)
					&& player.getBuffedValue(SecondaryStat.AttackCountX) != null) {
				player.changeCooldown(32001114, -500);
			}
			if (player.getBuffedValue(SecondaryStat.BMageDeath) != null
					&& (!monster.isAlive() || monster.getStats().isBoss())
					&& attack.skill != player.getBuffSource(SecondaryStat.BMageDeath)) {
				byte count;
				byte by = player.getBuffedValue(SecondaryStat.AttackCountX) != null ? (byte) 1
						: (player.getLevel() >= 100 ? (byte) 6 : (count = player.getLevel() > 60 ? (byte) 8 : 10));
				if (player.getDeath() < by) {
					player.setDeath((byte) (player.getDeath() + 1));
					if (player.getDeath() >= by) {
						player.setSkillCustomInfo(32120019, 1L, 0L);
					}
					HashMap<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
					statups.put(SecondaryStat.BMageDeath,
							new Pair<Integer, Integer>(Integer.valueOf(player.getDeath()), 0));
					player.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups,
							player.getBuffedEffect(SecondaryStat.BMageDeath), player));
				}
			}
			if (attack.skill == 400021096 && !player.getBuffedValue(400021096)) {
				player.lawOfGravity = monster.getObjectId();
				effect.applyTo(player);
			}
			if (attack.skill == 400021098) {
				player.getMap().broadcastMessage(
						MobPacket.skillAttackEffect(monster.getObjectId(), attack.skill, player.getId()));
			}
			if (attack.skill == 2121003 && monster.getBurnedBuffSize() >= 5) {
				player.changeCooldown(2121003, -2000);
				if (player.getCooldownLimit(2121011) > 0L) {
					player.removeCooldown(2121011);
				}
			}
			if (monster.isAlive()) {
				continue;
			}
			multikill = (byte) (multikill + 1);
		}

		if (attack.skill == 2121011 || attack.skill == 2141003) { // 플레임 헤이즈, 플레임 헤이즈 VI 구현
			if (attack.allDamage.size() == 0) {
				int skillId = 2111003;

				SecondaryStatEffect eff = SkillFactory.getSkill(skillId).getEffect(player.getSkillLevel(skillId));

				MapleMist mist = new MapleMist(eff.calculateBoundingBox(player.getPosition(), player.isFacingLeft()),
						player, eff, 15000, (byte) 0);
				player.getMap().spawnMist(mist, false);
			}
		}

		if (attack.skill == 2341000) { // 엔젤레이 VI 구현
			if (attack.allDamage.size() > 0) {
				player.hexaMasterySkillStack++;

				EnumMap<SecondaryStat, Pair<Integer, Integer>> statups = new EnumMap<SecondaryStat, Pair<Integer, Integer>>(
						SecondaryStat.class);
				statups.put(SecondaryStat.AngelRay_Stack, new Pair<Integer, Integer>(player.hexaMasterySkillStack, 0));
				SecondaryStatEffect eff = SkillFactory.getSkill(attack.skill)
						.getEffect(player.getSkillLevel(attack.skill));
				player.getClient().getSession().writeAndFlush(CWvsContext.BuffPacket.giveBuff(statups, eff, player));

				if (player.hexaMasterySkillStack >= 12) {
					player.hexaMasterySkillStack = 0;

					statups = new EnumMap<SecondaryStat, Pair<Integer, Integer>>(SecondaryStat.class);
					statups.put(SecondaryStat.AngelRay_Stack,
							new Pair<Integer, Integer>(player.hexaMasterySkillStack, 0));
					player.getClient().getSession().writeAndFlush(CWvsContext.BuffPacket.cancelBuff(statups, player));

					ArrayList<Triple<Integer, Integer, Integer>> mobList = new ArrayList<>();

					player.getClient().getSession()
							.writeAndFlush(CField.bonusAttackRequest(2341001, mobList, true, 0, null));
				}
			}
		}

		if (attack.skill == 400021096 && !monster.isAlive()) {
			SecondaryStatEffect a = SkillFactory.getSkill(400021104).getEffect(player.getSkillLevel(400021096));
			MapleMist newmist = new MapleMist(a.calculateBoundingBox(player.getPosition(), player.isFacingLeft()),
					player, a, a.getDuration(), (byte) (player.isFacingLeft() ? 1 : 0));
			newmist.setPosition(monster.getPosition());
			newmist.setDelay(0);
			player.getMap().spawnMist(newmist, false);
		}

		if (GameConstants.isFlameWizard(player.getJob()) && player.getSkillLevel(400021042) > 0) {
			player.gainIgnition();
		}

		// 오버로드 마나 - 마나소모량 fix
		if (player.getBuffedValue(SecondaryStat.OverloadMana) != null
				&& !GameConstants.is_forceAtom_attack_skill(attack.skill) && !effect.isMist()) {
			if (GameConstants.isKinesis(player.getJob())) {
				player.addHP((int) (-(player.getStat().getCurrentMaxHp()
						* (long) player.getBuffedEffect(SecondaryStat.OverloadMana).getY() / 1000L)));
			} else {
				player.addMP((int) (-(player.getStat().getCurrentMaxMp(player)
						* (long) player.getBuffedEffect(SecondaryStat.OverloadMana).getX() / 1000L)));
			}
		}
		if (player.getSkillLevel(2120010) > 0
				&& (arcaneAim = SkillFactory.getSkill(2120010).getEffect(player.getSkillLevel(2120010)))
						.makeChanceResult()) {
			if (player.getArcaneAim() < 5) {
				player.setArcaneAim(player.getArcaneAim() + 1);
			}
			arcaneAim.applyTo(player, false);
		}
		if (player.getSkillLevel(2220010) > 0
				&& (arcaneAim = SkillFactory.getSkill(2220010).getEffect(player.getSkillLevel(2220010)))
						.makeChanceResult()) {
			if (player.getArcaneAim() < 5) {
				player.setArcaneAim(player.getArcaneAim() + 1);
			}
			arcaneAim.applyTo(player, false);
		}
		if (player.getSkillLevel(2320011) > 0
				&& (arcaneAim = SkillFactory.getSkill(2320011).getEffect(player.getSkillLevel(2320011)))
						.makeChanceResult()) {
			if (player.getArcaneAim() < 5) {
				player.setArcaneAim(player.getArcaneAim() + 1);
			}
			arcaneAim.applyTo(player, false);
		}
		if (totDamage > 0L) {
			if (player.getMapId() == 993000500) {
				player.setFWolfDamage(player.getFWolfDamage() + totDamage);
				player.setFWolfAttackCount(player.getFWolfAttackCount() + 1);
			}
			DamageParse.MFinalAttackRequest(player, attack.skill, monster);
			if (attack.skill == 2321007) {
				player.홀리워터++;
				if (player.홀리워터 == 7) {
					player.홀리워터 = 0;
					player.홀리워터스택++;
					if (player.홀리워터스택 > 5) {
						player.홀리워터스택 = 5;
					}
					HashMap<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
					statups.put(SecondaryStat.HolyWater, new Pair<Integer, Integer>((int) player.홀리워터스택, 0));
					player.getClient().getSession()
							.writeAndFlush(CWvsContext.BuffPacket.giveBuff(statups, null, player));
				}
			}
			if (GameConstants.isLuminous(player.getJob())) {
				SecondaryStatEffect effect2;
				SecondaryStatEffect dark;
				if ((player.getBuffedValue(20040216) || player.getBuffedValue(20040219)
						|| player.getBuffedValue(20040220))
						&& (GameConstants.isLightSkills(attack.skill)
								|| (player.getBuffedValue(20040219) || player.getBuffedValue(20040220))
										&& (attack.skill == 27121303 || attack.skill == 27111303))) {
					player.addHP(player.getStat().getMaxHp() / 100L);
				}
				if (player.getSkillLevel(27120005) > 0
						&& (dark = SkillFactory.getSkill(27120005).getEffect(player.getSkillLevel(27120005)))
								.makeChanceResult()) {
					if (player.stackbuff < dark.getX()) {
						++player.stackbuff;
					}
					dark.applyTo(player, false);
				}
				if (player.getBuffedValue(400021105)
						&& (GameConstants.isLightSkills(attack.skill) || attack.skill == 27121303
								|| GameConstants.isDarkSkills(attack.skill))
						&& player.getSkillLevel(400021105) > 0 && player.getSkillCustomValue(400021109) == null) {
					effect2 = SkillFactory.getSkill(400021105).getEffect(player.getSkillLevel(400021105));
					int skillid = 0;
					if (player.getSkillCustomValue0(400021105) == 2L) {
						skillid = 400021110;
					} else if (player.getSkillCustomValue0(400021105) == 1L) {
						skillid = 400021109;
					}
					player.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(400021105,
							Arrays.asList(new RangeAttack(skillid, attack.position, 1, 0, 1))));
					player.setSkillCustomInfo(400021109, 0L, effect2.getU2());
					player.addSkillCustomInfo(400021110, -1L);
					if (player.getSkillCustomValue0(400021110) <= 0L) {
						player.cancelEffect(effect2);
					}
				}
				if (!(player.getBuffedValue(20040216) || player.getBuffedValue(20040217)
						|| player.getBuffedValue(20040219) || player.getBuffedValue(20040220))) {
					if (GameConstants.isLightSkills(attack.skill)) {
						player.setLuminusMorphUse(1);
						SkillFactory.getSkill(20040217).getEffect(1).applyTo(player, false);
						player.setLuminusMorph(false);
					} else if (GameConstants.isDarkSkills(attack.skill)) {
						player.setLuminusMorphUse(9999);
						SkillFactory.getSkill(20040216).getEffect(1).applyTo(player, false);
						player.setLuminusMorph(true);
					}
					player.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket
							.LuminusMorph(player.getLuminusMorphUse(), player.getLuminusMorph()));
				} else if (!player.getBuffedValue(20040219) && !player.getBuffedValue(20040220)) {
					if (player.getLuminusMorph()) {
						if (GameConstants.isLightSkills(attack.skill)) {
							if (!(player.getBuffedValue(20040219) || player.getBuffedValue(20040220)
									|| player.getBuffedValue(400021105) || !GameConstants.isLightSkills(attack.skill)
									|| player.getSkillLevel(400021105) <= 0 || player.skillisCooling(400021106))) {
								effect2 = SkillFactory.getSkill(400021105).getEffect(player.getSkillLevel(400021105));
								if (player.getSkillCustomValue0(400021107) < (long) effect2.getU()) {
									player.setSkillCustomInfo(400021107, player.getSkillCustomValue0(400021107) + 1L,
											0L);
								}
								player.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(400021105,
										Arrays.asList(new RangeAttack(400021107, attack.position, 1, 0, 1))));
								player.addCooldown(400021106, System.currentTimeMillis(), effect2.getX() * 1000);
								player.getClient().getSession()
										.writeAndFlush((Object) CField.skillCooldown(400021106, effect2.getX() * 1000));
								HashMap<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
								statups.put(SecondaryStat.LiberationOrb, new Pair<Integer, Integer>(1, 0));
								player.getClient().getSession().writeAndFlush(
										(Object) CWvsContext.BuffPacket.giveBuff(statups, effect2, player));
							}
							if (player.getLuminusMorphUse()
									- GameConstants.isLightSkillsGaugeCheck(attack.skill) <= 0) {
								if (player.getSkillLevel(20040219) > 0) {
									player.cancelEffectFromBuffStat(SecondaryStat.Larkness);
									SkillFactory.getSkill(20040219).getEffect(1).applyTo(player, false);
									player.setUseTruthDoor(false);
								} else {
									player.cancelEffectFromBuffStat(SecondaryStat.Larkness);
									player.setLuminusMorph(false);
									SkillFactory.getSkill(20040217).getEffect(1).applyTo(player, false);
								}
							} else {
								player.setLuminusMorphUse(player.getLuminusMorphUse()
										- GameConstants.isLightSkillsGaugeCheck(attack.skill));
							}
							if (!player.getBuffedValue(20040219) && !player.getBuffedValue(20040220)
									&& player.getLuminusMorph()) {
								player.cancelEffectFromBuffStat(SecondaryStat.Larkness);
								SkillFactory.getSkill(20040216).getEffect(1).applyTo(player, false);
							}
						}
					} else if (GameConstants.isDarkSkills(attack.skill)) {
						if (!player.getBuffedValue(400021105) && GameConstants.isDarkSkills(attack.skill)
								&& player.getSkillLevel(400021105) > 0 && !player.skillisCooling(400021106)) {
							effect2 = SkillFactory.getSkill(400021105).getEffect(player.getSkillLevel(400021105));
							if (player.getSkillCustomValue0(400021108) < (long) effect2.getU()) {
								player.setSkillCustomInfo(400021108, player.getSkillCustomValue0(400021108) + 1L, 0L);
							}
							player.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(400021105,
									Arrays.asList(new RangeAttack(400021108, attack.position, 1, 0, 1))));
							player.addCooldown(400021106, System.currentTimeMillis(), effect2.getX() * 1000);
							player.getClient().getSession()
									.writeAndFlush((Object) CField.skillCooldown(400021106, effect2.getX() * 1000));
							HashMap<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
							statups.put(SecondaryStat.LiberationOrb, new Pair<Integer, Integer>(1, 0));
							player.getClient().getSession()
									.writeAndFlush((Object) CWvsContext.BuffPacket.giveBuff(statups, effect2, player));
						}
						if (player.getLuminusMorphUse()
								+ GameConstants.isDarkSkillsGaugeCheck(player, attack.skill) >= 10000) {
							if (player.getSkillLevel(20040219) > 0) {
								player.cancelEffectFromBuffStat(SecondaryStat.Larkness);
								SkillFactory.getSkill(20040220).getEffect(1).applyTo(player, false);
								player.setUseTruthDoor(false);
							} else {
								player.cancelEffectFromBuffStat(SecondaryStat.Larkness);
								player.setLuminusMorph(true);
								SkillFactory.getSkill(20040216).getEffect(1).applyTo(player, false);
							}
						} else {
							player.setLuminusMorphUse(player.getLuminusMorphUse()
									+ GameConstants.isDarkSkillsGaugeCheck(player, attack.skill));
						}
						if (!(player.getBuffedValue(20040219) || player.getBuffedValue(20040220)
								|| player.getLuminusMorph())) {
							player.cancelEffectFromBuffStat(SecondaryStat.Larkness);
							SkillFactory.getSkill(20040217).getEffect(1).applyTo(player, false);
						}
					}
					player.getClient().getSession().writeAndFlush((Object) CWvsContext.BuffPacket
							.LuminusMorph(player.getLuminusMorphUse(), player.getLuminusMorph()));
				}
			} else if (GameConstants.isEvan(player.getJob())) {
				if (attack.isLink && player.getSkillLevel(22110016) > 0) {
					SkillFactory.getSkill(22110016).getEffect(player.getSkillLevel(22110016)).applyTo(player);
				}
			} else if (GameConstants.isLara(player.getJob()) && attack.targets > 0
					&& (attack.skill == 162001000 || attack.skill == 162121021)) {
				SecondaryStatEffect effects;
				if (player.getSkillLevel(162000003) > 0 && Randomizer.isSuccess(
						(effects = SkillFactory.getSkill(162000003).getEffect(player.getSkillLevel(162000003)))
								.getProp())) {
					player.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(attack.skill,
							Arrays.asList(new RangeAttack(162001004, attack.position, 0, 0, 1))));
				}
				if (player.getBuffedValue(162121003)) {
					effects = SkillFactory.getSkill(162120002).getEffect(player.getSkillLevel(162120002));
					if (player.getSkillCustomValue(162121004) == null) {
						player.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(162121004,
								Arrays.asList(new RangeAttack(162121004, attack.position, 0, 0, 1))));
						player.setSkillCustomInfo(162121004, 0L, (int) effects.getT() * 1000);
					}
				}
				if (player.getBuffedValue(162121006)) {
					effects = SkillFactory.getSkill(162120005).getEffect(player.getSkillLevel(162120005));
					if (player.getSkillCustomValue(162121007) == null) {
						player.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(162121007,
								Arrays.asList(new RangeAttack(162121007, attack.position, 0, 0, 1))));
						player.setSkillCustomInfo(162121007, 0L, (int) effects.getT() * 1000);
					}
				}
				if (player.getBuffedValue(162121009)) {
					effects = SkillFactory.getSkill(162120008).getEffect(player.getSkillLevel(162120008));
					if (player.getSkillCustomValue(162121010) == null) {
						player.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(162121010,
								Arrays.asList(new RangeAttack(162121010, attack.position, 0, 0, 5))));
						player.setSkillCustomInfo(162121010, 0L, (int) effects.getT() * 1000);
					}
				}
			}
			if (attack.targets > 0 && player.getBuffedEffect(SecondaryStat.Triumph) != null && attack.skill != 2311017
					&& attack.skill != 2341005 && System.currentTimeMillis() - player.TriumphTime >= 2000) {
				List<MapleMapObject> objs = player.getMap().getMapObjectsInRange(player.getTruePosition(), 500000.0,
						Arrays.asList(new MapleMapObjectType[] { MapleMapObjectType.MONSTER }));
				if (objs.size() > 0) {
					final List<SecondAtom> atoms = new ArrayList<SecondAtom>();
					atoms.add(new SecondAtom(0x25, player.getId(), monster.getId(),
							player.getSkillLevel(2341004) > 0 ? 2341005 : 2311017, 4000, 0, 1,
							new Point((int) player.getTruePosition().getX(),
									(int) player.getTruePosition().getY() - 120),
							Arrays.asList(0)));
					atoms.add(new SecondAtom(0x25, player.getId(), monster.getId(),
							player.getSkillLevel(2341004) > 0 ? 2341005 : 2311017, 4000, 0, 1,
							new Point((int) player.getTruePosition().getX() + 20,
									(int) player.getTruePosition().getY() - 116),
							Arrays.asList(0)));
					atoms.add(new SecondAtom(0x25, player.getId(), monster.getId(),
							player.getSkillLevel(2341004) > 0 ? 2341005 : 2311017, 4000, 0, 1,
							new Point((int) player.getTruePosition().getX() - 20,
									(int) player.getTruePosition().getY() - 120),
							Arrays.asList(0)));
					atoms.add(new SecondAtom(0x25, player.getId(), monster.getId(),
							player.getSkillLevel(2341004) > 0 ? 2341005 : 2311017, 4000, 0, 1,
							new Point((int) player.getTruePosition().getX() + 10,
									(int) player.getTruePosition().getY() - 116),
							Arrays.asList(0)));
					atoms.add(new SecondAtom(0x25, player.getId(), monster.getId(),
							player.getSkillLevel(2341004) > 0 ? 2341005 : 2311017, 4000, 0, 1,
							new Point((int) player.getTruePosition().getX() - 10,
									(int) player.getTruePosition().getY() - 120),
							Arrays.asList(0)));
					player.spawnSecondAtom(atoms);
					player.TriumphTime = System.currentTimeMillis();
				}
			}
			if (attack.skill == 27121303 && player.getSkillLevel(400021071) > 0) {
				boolean give = false;
				if (player.getPerfusion() < SkillFactory.getSkill(400021071).getEffect(player.getSkillLevel(400021071))
						.getX() - 1) {
					give = true;
				} else if (player.getPerfusion() >= SkillFactory.getSkill(400021071)
						.getEffect(player.getSkillLevel(400021071)).getX() - 1 && player.skillisCooling(400021071)) {
					give = true;
				}
				if (give) {
					SkillFactory.getSkill(400021071).getEffect(player.getSkillLevel(400021071)).applyTo(player, false);
				}
			} else if (GameConstants.isKinesis(player.getJob())) {
				if (player.getSkillLevel(142110011) > 0 && attack.skill != 142110011 && !attack.allDamage.isEmpty()) {
					switch (attack.skill) {
					case 142001000:
					case 142001002:
					case 142141000: // 얼티메이트-메테리얼 VI 구현
					case 142100000:
					case 142100001:
					case 142101003:
					case 142101009:
					case 142110000:
					case 142110001:
					case 142111007:
					case 142120002:
					case 142120030:
					case 142121005:
					case 142121030: {
						break;
					}
					default: {
						MapleAtom atom = new MapleAtom(false, player.getId(), 22, true, 142110011,
								player.getTruePosition().x, player.getTruePosition().y);
						for (int i = 0; i < attack.targets; ++i) {
							if (!SkillFactory.getSkill(142110011).getEffect(player.getSkillLevel(142110011))
									.makeChanceResult() || attack.skill == 142001000 || attack.skill == 142100000
									|| attack.skill == 142110000) {
								continue;
							}
							atom.addForceAtom(new ForceAtom(0, 21, 9, 68, 960));
						}
						if (atom.getForceAtoms().isEmpty()) {
							break;
						}
						atom.setDwFirstTargetId(0);
						player.getMap().spawnMapleAtom(atom);
						break;
					}
					}
				}
				if (attack.skill == 142121004) {
					int up = 0;
					for (AttackPair att : attack.allDamage) {
						MapleMonster m = MapleLifeFactory.getMonster(att.monsterId);
						if (m == null) {
							continue;
						}
						up += m.getStats().isBoss() ? effect.getW() : (int) effect.getIndiePmdR();
					}
					if (player.getSkillLevel(142120041) > 0) {
						up *= 2;
					}
					player.setSkillCustomInfo(142121004, up, 0L);
					if (player.getSkillCustomValue0(142121004) >= (long) effect.getW()) {
						player.setSkillCustomInfo(142121004, effect.getW(), 0L);
					}
					SkillFactory.getSkill(142121004).getEffect(player.getSkillLevel(142121004)).applyTo(player);
				}
				if (attack.skill == 400021075 && monster != null) {
					player.givePPoint((byte) 1);
				} else if (attack.skill == 142121005) {
					player.givePPoint((byte) -1);
				}
			} else if (GameConstants.isIllium(player.getJob()) && attack.skill == 400021061 && attack.targets > 0) {
				SkillFactory.getSkill(152000009).getEffect(player.getSkillLevel(152000009)).applyTo(player, false);
			}
			if (player.getBuffedValue(32101009) && player.getSkillCustomValue(32111119) == null
					&& player.getId() == player.getBuffedOwner(32101009)) {
				player.addHP(totDamage / 100L * (long) player.getBuffedEffect(32101009).getX());
				player.getClient().getSession()
						.writeAndFlush((Object) CField.EffectPacket.showEffect(player, 0, 32101009, 10, 0, 0,
								(byte) (player.isFacingLeft() ? 1 : 0), true, player.getTruePosition(), null, null));
				player.getMap().broadcastMessage(player,
						CField.EffectPacket.showEffect(player, 0, 32101009, 10, 0, 0,
								(byte) (player.isFacingLeft() ? 1 : 0), false, player.getTruePosition(), null, null),
						false);
				player.setSkillCustomInfo(32111119, 0L, 5000L);
				if (player.getParty() != null) {
					for (MaplePartyCharacter pc : player.getParty().getMembers()) {
						MapleCharacter chr;
						if (pc.getId() == player.getId() || !pc.isOnline()
								|| (chr = player.getClient().getChannelServer().getPlayerStorage()
										.getCharacterById(pc.getId())) == null
								|| !chr.getBuffedValue(32101009) || chr.getId() == player.getId()) {
							continue;
						}
						chr.addHP(totDamage / 100L * (long) player.getBuffedEffect(32101009).getX());
						if (chr.getDisease(SecondaryStat.GiveMeHeal) != null) {
							chr.cancelDisease(SecondaryStat.GiveMeHeal);
						}
						chr.getClient().getSession()
								.writeAndFlush((Object) CField.EffectPacket.showEffect(chr, 0, 32101009, 10, 0, 0,
										(byte) (chr.isFacingLeft() ? 1 : 0), true, chr.getTruePosition(), null, null));
						chr.getMap().broadcastMessage(chr,
								CField.EffectPacket.showEffect(chr, 0, 32101009, 10, 0, 0,
										(byte) (chr.isFacingLeft() ? 1 : 0), false, chr.getTruePosition(), null, null),
								false);
					}
				}
			}
		}
		if (player.getBuffedValue(400021092) && player.getSkillCustomValue0(400021092) != 1L) {
			MapleSummon sum = player.getSummon(400021092);
			MapleMonster mon = null;
			List<MapleMapObject> objs = player.getMap().getMapObjectsInRange(player.getTruePosition(), 800000.0,
					Arrays.asList(new MapleMapObjectType[] { MapleMapObjectType.MONSTER }));
			mon = player.getMap().getMonsterByOid(objs.get(Randomizer.nextInt(objs.size())).getObjectId());
			if (sum != null && mon != null) {
				player.setGraveTarget(mon.getObjectId());
				player.createSecondAtom(SkillFactory.getSkill(400021092).getSecondAtoms(), sum.getPosition());
				player.getMap().broadcastMessage(CField.SummonPacket.updateSummon(sum, 99));
				player.setSkillCustomInfo(400021092, 1L, 0L);
			}
		}
		if (totDamage > 0L && player.getBuffedValue(400021073) && (summon = player.getSummon(400021073)) != null
				&& summon.getEnergy() < 22) {
			switch (attack.skill) {
			case 22110022:
			case 22110023:
			case 22111012:
			case 22170060:
			case 22170070:
			case 400021012:
			case 400021014:
			case 400021015: {
				MapleAtom atom = new MapleAtom(true, summon.getObjectId(), 29, true, 400021073,
						summon.getTruePosition().x, summon.getTruePosition().y);
				atom.setDwUserOwner(summon.getOwner().getId());
				atom.setDwFirstTargetId(0);
				atom.addForceAtom(new ForceAtom(5, 37, Randomizer.rand(5, 10), 62, 0));
				player.getMap().spawnMapleAtom(atom);
				player.getClient().getSession()
						.writeAndFlush((Object) CField.SummonPacket.ElementalRadiance(summon, 2));
				player.getClient().getSession().writeAndFlush((Object) CField.SummonPacket.specialSummon(summon, 2));
				if (summon.getEnergy() < 22) {
					break;
				}
				player.getClient().getSession().writeAndFlush((Object) CField.SummonPacket.damageSummon(summon));
				break;
			}
			case 22110014:
			case 22110024:
			case 22110025:
			case 22111011:
			case 22140014:
			case 22140015:
			case 22140023:
			case 22140024:
			case 22141011:
			case 22170064:
			case 22170065:
			case 22170066:
			case 22170067:
			case 22170093:
			case 22170094:
			case 22171063:
			case 22171083:
			case 22171095:
			case 400021013: {
				if (summon.getMagicSkills().contains(attack.skill)) {
					break;
				}
				summon.getMagicSkills().add(attack.skill);
				summon.setEnergy(Math.min(22, summon.getEnergy() + 3));
				MapleAtom atom = new MapleAtom(true, summon.getObjectId(), 29, true, 400021073,
						summon.getTruePosition().x, summon.getTruePosition().y);
				atom.setDwUserOwner(summon.getOwner().getId());
				atom.setDwFirstTargetId(0);
				atom.addForceAtom(new ForceAtom(5, 37, Randomizer.rand(5, 10), 62, 0));
				player.getMap().spawnMapleAtom(atom);
				player.getClient().getSession()
						.writeAndFlush((Object) CField.SummonPacket.ElementalRadiance(summon, 2));
				player.getClient().getSession().writeAndFlush((Object) CField.SummonPacket.specialSummon(summon, 2));
				if (summon.getEnergy() < 22) {
					break;
				}
				player.getClient().getSession().writeAndFlush((Object) CField.SummonPacket.damageSummon(summon));
				break;
			}
			}
		}
		if (player.getBuffedValue(400001050) && player.getSkillCustomValue0(400001050) == 400001055L) {
			SecondaryStatEffect effect6 = SkillFactory.getSkill(400001050).getEffect(player.getSkillLevel(400001050));
			player.getClient().getSession().writeAndFlush((Object) CField.bonusAttackRequest(400001055,
					new ArrayList<Triple<Integer, Integer, Integer>>(), true, 0, new int[0]));
			player.removeSkillCustomInfo(400001050);
			long duration = player.getBuffLimit(400001050);
			effect6.applyTo(player, false, (int) duration);
		}
		if (attack.skill == 2121003) {
			for (MapleMist mist : player.getMap().getAllMistsThreadsafe()) {
				if (mist.getSource() == null || mist.getSource().getSourceId() != 2111003) {
					continue;
				}
				player.getMap().removeMist(mist.getSource().getSourceId());
				if (player.getCooldownLimit(2121011) <= 0L) {
					continue;
				}
				player.removeCooldown(2121011);
			}
		}
		if (totDamage > 0L && attack.skill >= 400021013 && attack.skill <= 400021016) {
			SkillFactory.getSkill(400021012).getEffect(attack.skilllevel).applyTo(player, false);
		}
		if (player.getSkillLevel(80002762) > 0
				&& (stst = SkillFactory.getSkill(80002762).getEffect(player.getSkillLevel(80002762)))
						.makeChanceResult()) {
			stst.applyTo(player, false);
		}
		if (player.getSkillLevel(150010241) > 0 && player.getSkillCustomValue(80000514) == null) {
			SkillFactory.getSkill(150010241).getEffect(player.getSkillLevel(150010241)).applyTo(player);
			player.setSkillCustomInfo(80000514, 0L, 3000L);
		} else if (player.getSkillLevel(80000514) > 0 && player.getSkillCustomValue(80000514) == null) {
			SkillFactory.getSkill(80000514).getEffect(player.getSkillLevel(80000514)).applyTo(player);
			player.setSkillCustomInfo(80000514, 0L, 3000L);
		}
		if (attack.skill == 152121007 && player.getBuffedEffect(152111003) != null) {
			player.canUseMortalWingBeat = false;
			HashMap<SecondaryStat, Pair<Integer, Integer>> statups = new HashMap<SecondaryStat, Pair<Integer, Integer>>();
			statups.put(SecondaryStat.GloryWing, new Pair<Integer, Integer>(1, (int) player.getBuffLimit(152111003)));
			player.getClient().getSession().writeAndFlush(
					(Object) CWvsContext.BuffPacket.giveBuff(statups, player.getBuffedEffect(152111003), player));
		}
		if (attack.skill == 400021012) {
			ArrayList<RangeAttack> skills = new ArrayList<RangeAttack>();
			skills.add(new RangeAttack(400021013, attack.position, 0, 0, 0));
			skills.add(new RangeAttack(400021014, attack.position, 0, 0, 0));
			skills.add(new RangeAttack(400021015, attack.position, 0, 0, 0));
			player.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(attack.skill, skills));
		}
		if (player.getBuffedEffect(SecondaryStat.CrystalGate) != null) {
			SecondaryStatEffect crystalGate = player.getBuffedEffect(SecondaryStat.CrystalGate);
			if ((double) (System.currentTimeMillis() - player.lastCrystalGateTime) >= crystalGate.getT() * 1000.0) {
				player.lastCrystalGateTime = System.currentTimeMillis();
				player.getClient().getSession().writeAndFlush((Object) CField.bonusAttackRequest(400021111,
						new ArrayList<Triple<Integer, Integer, Integer>>(), true, 0, new int[0]));
			}
		}
		if (player.getBuffedValue(80002890) && attack.skill != 80002890 && attack.skill != 80002890
				&& System.currentTimeMillis()
						- player.lastThunderTime >= (long) player.getBuffedEffect(80002890).getCooldown(player)) {
			player.lastThunderTime = System.currentTimeMillis();
			player.getClient().getSession().writeAndFlush((Object) CField.rangeAttack(80002890,
					Arrays.asList(new RangeAttack(80002890, attack.position, 0, 0, 1))));
		}
		if (GameConstants.isFPMage(player.getJob())) {
			if (SkillFactory.getSkill(2111013).getSkillList2().contains(attack.skill) || attack.skill == 2111014) {
				Rectangle Rectanglebox1 = effect.calculateBoundingBox(attack.position, player.isFacingLeft());
				Rectangle Rectanglebox2 = effect.calculateBoundingBox(attack.position, player.isFacingLeft());
				if (attack.skill == 2111014) {
					Rectanglebox1 = effect.calculateBoundingBox(new Point(attack.position.x, attack.position.y + 170),
							true, 100);
					Rectanglebox2 = effect.calculateBoundingBox(new Point(attack.position.x, attack.position.y + 170),
							false, 100);
				}
				for (MapleMist mist : player.getMap().getAllMistsThreadsafe()) {
					if (mist.getSourceSkill().getId() == 2111013) {
						if (Rectanglebox1.contains(mist.getPosition()) || Rectanglebox2.contains(mist.getPosition())) {
							if (mist.getStartTime() + 1500 < System.currentTimeMillis()) {
								player.getClient().getSession()
										.writeAndFlush(CField.rangeAttackTest(2111013, attack.skill, mist.getObjectId(),
												Arrays.asList(new RangeAttack(2111014,
														new Point(mist.getPosition().x, mist.getPosition().y - 170), 0,
														240, 1))));
								server.Timer.MapTimer.getInstance().schedule(() -> {
									player.getMap().removeMist(mist);
								}, 300);
							}
						}
					}
				}
			}
		}
		if (attack.skill == 400021002) {
			SecondaryStatEffect iceAge = SkillFactory.getSkill(400020002).getEffect(player.getSkillLevel(400021002));
			Rectangle bounds = effect.calculateBoundingBox(player.getTruePosition(), player.isFacingLeft());
			for (int i = 0; i < player.getMap().getFootholds().getAllRelevants().size(); ++i) {
				MapleFoothold fh = player.getMap().getFootholds().getAllRelevants().get(i);
				int rx = fh.getPoint2().x - fh.getPoint1().x;
				if (!bounds.contains(fh.getPoint1()) && !bounds.contains(fh.getPoint2())) {
					continue;
				}
				if (rx / 200 > 1) {
					for (int i2 = 0; i2 <= rx / 200; ++i2) {
						boolean active = true;
						if (!active) {
							continue;
						}
						iceAge.applyTo(player, false, new Point(fh.getPoint1().x + i2 * 200, fh.getPoint1().y + 30));
					}
					continue;
				}
				boolean active = true;
				for (MapleMist mist : player.getMap().getAllMistsThreadsafe()) {
					if (mist.getPosition().x - 200 < fh.getPoint1().x && mist.getPosition().x + 200 > fh.getPoint2().x
							&& fh.getPoint1().y - 70 < mist.getPosition().y
							&& fh.getPoint1().y + 70 > mist.getPosition().y) {
						active = false;
						break;
					}
					if (!mist.getBox().contains(fh.getPoint1()) && !mist.getBox().contains(fh.getPoint2())) {
						continue;
					}
					active = false;
					break;
				}
				if (!active) {
					continue;
				}
				iceAge.applyTo(player, false, new Point(fh.getPoint1().x, fh.getPoint1().y + 30));
			}
		}
		if (multikill > 0) {
			player.CombokillHandler(monster, 1, multikill);
		}
	}

	public static final AttackInfo parseDmgMa(LittleEndianAccessor lea, MapleCharacter chr, boolean chilling,
			boolean orbital) {
		AttackInfo ret = new AttackInfo();
		LittleEndianAccessor data = lea;
		if (orbital) {
			ret.skill = lea.readInt();
			ret.skilllevel = lea.readInt();
			lea.skip(4);
			lea.skip(4);
			lea.skip(4);
		}
		lea.skip(1);
		ret.tbyte = lea.readByte();
		ret.targets = (byte) (ret.tbyte >>> 4 & 0xF);
		ret.hits = (byte) (ret.tbyte & 0xF);
		ret.skill = lea.readInt();
		ret.skilllevel = lea.readInt();

		// 6차 구현
		switch (ret.skill) {
		case 2241503: // 오리진 프로즌 라이트닝 구현
			if (!chr.getBuffedValue(2241504)) {
				SkillFactory.getSkill(2241504).getEffect(2241500).applyTo(chr);
			}
			break;
		}

		try {
			if (orbital) {
				lea.skip(1);
			}

			lea.skip(4);
			lea.skip(4);

			GameConstants.attackBonusRecv(lea, ret);
			GameConstants.calcAttackPosition(lea, ret);

			if (ServerConstants.DEBUG_RECEIVE) {
				switch (ret.skill) {
				default:
					System.err.println("[ParseDmgMa A] " + ret.skill + " / " + lea);
					break;
				}
			}

			if (orbital) {
				if (GameConstants.sub_57D400(ret.skill)) {
					ret.charge = lea.readInt();

				}
			} else if (GameConstants.is_keydown_skill(ret.skill)) {
				ret.charge = lea.readInt();
			}

			// 피닉스 드라이브 fix - OTHELLO
			if (orbital && ret.skill == 12121057 || orbital && ret.skill == 12121059) {
				ret.charge = lea.readInt();
				lea.skip(2);
				lea.skip(2);
				lea.skip(2);
				lea.skip(2);
			}

			ret.isShadowPartner = lea.readByte();
			ret.isBuckShot = lea.readByte();
			ret.display = lea.readByte();
			ret.facingleft = lea.readByte();
			lea.skip(4);
			ret.attacktype = lea.readByte();
			if (GameConstants.is_evan_force_skill(ret.skill)) {
				lea.readByte();
			}
			ret.speed = lea.readByte();
			ret.lastAttackTickCount = lea.readInt();
			int chillingoid = 0;
			if (chilling) {
				chillingoid = lea.readInt();
			}

			lea.readInt();

			if (orbital || ret.skill == 22140024) {
				lea.skip(4);
			}

			// othello 플위 fix
			if (ret.skill == 12120019 || ret.skill == 12120020 || ret.skill == 12120018 || ret.skill == 12120017) {
				lea.skip(4);
			}

			switch (ret.skill) {
			case 12141001:
			case 12141002:
				lea.skip(8);
				break;
			}

			if (ServerConstants.DEBUG_RECEIVE) {
				switch (ret.skill) {
				default:
					System.err.println("[ParseDmgMa B] " + ret.skill + " / " + lea);
					break;
				}
			}

			ret.allDamage = new ArrayList<AttackPair>();
			for (int i = 0; i < ret.targets; ++i) {
				long damage;

				int oid = lea.readInt();

				lea.readByte();
				lea.readByte();
				lea.readByte();
				lea.readByte();
				lea.readByte();

				int monsterId = lea.readInt();

				lea.readByte();

				Point pos1 = lea.readPos();
				Point pos2 = lea.readPos();

				if (!orbital) {
					lea.skip(1);
				}

				ArrayList<Pair<Long, Boolean>> allDamageNumbers = new ArrayList<Pair<Long, Boolean>>();
				if (ret.skill == 80001835) {
					int cc = lea.readByte();
					for (int ii = 0; ii < cc; ++ii) {
						damage = lea.readLong();
						if (damage < 0L) {
							damage &= 0xFFFFFFFFL;
						}
						allDamageNumbers.add(new Pair<Long, Boolean>(damage, false));
					}
				} else {
					lea.readShort();
					lea.skip(4);
					lea.skip(4);
					lea.readByte();

					for (int j = 0; j < ret.hits; ++j) {
						damage = lea.readLong();
						if (damage < 0L) {
							damage &= 0xFFFFFFFFL;
						}

						allDamageNumbers.add(new Pair<Long, Boolean>(damage, false));

					}
				}

				lea.skip(4);
				lea.skip(4);
				lea.skip(4);

				if (ret.skill == 37111005) {
					lea.skip(1);
				}
				if (ret.skill == 142120001 || ret.skill == 142120002 || ret.skill == 142110003) {
					lea.skip(8);
				}

				GameConstants.attackSkeletonImage(lea, ret);

				ret.allDamage.add(new AttackPair(oid, monsterId, pos1, pos2, allDamageNumbers));
			}

			ret.position = lea.readPos();

			if (ret.skill == 32111016) {
				ret.plusPosition3 = lea.readPos();
			}
			if (ret.skill == 22140024) {
				lea.skip(4);
			}

			if (lea.available() == 0) {
				return ret;
			}

			byte posType = lea.readByte();

			if (!orbital && posType != 0) {
				ret.plusPosition = lea.readPos();
				ret.plusPosition2 = lea.readPos();

				if (ret.skill == 12100029) {
					lea.skip(4);
				} else if (ret.skill == 2121003) {
					int size = lea.readByte();
					for (int i = 0; i < size; ++i) {
						lea.skip(4);
					}
				} else if (ret.skill == 2111003 || ret.skill == 12120023 || ret.skill == 12120022) {
					lea.skip(1);
					ret.plusPosition3 = lea.readPos();

				} else {
					ret.isLink = lea.readByte() == 1;
					ret.nMoveAction = lea.readByte();
					ret.bShowFixedDamage = lea.readByte();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			FileoutputUtil.log("Log_Attack.txt", "error in MagicAttack.\r\n ordinary : "
					+ HexTool.toString(data.getByteArray()) + "\r\n error : " + e);
		}
		return ret;
	}

	public static final AttackInfo parseDmgB(LittleEndianAccessor lea, MapleCharacter chr) {
		AttackInfo ret = new AttackInfo();
		LittleEndianAccessor data = lea;
		lea.skip(1);
		ret.tbyte = lea.readByte();
		ret.targets = (byte) (ret.tbyte >>> 4 & 0xF);
		ret.hits = (byte) (ret.tbyte & 0xF);
		ret.skill = lea.readInt();
		ret.skilllevel = lea.readInt();
		try {
			lea.skip(4);
			lea.skip(4);
			GameConstants.attackBonusRecv(lea, ret);
			GameConstants.calcAttackPosition(lea, ret);

			if (ret.skill != 0
					&& (GameConstants.is_keydown_skill(ret.skill) || GameConstants.is_super_nova_skill(ret.skill))) {
				ret.charge = lea.readInt();
			}
			// || ret.skill == 400031004
			if (GameConstants.sub_883680(ret.skill) || ret.skill == 5300007 || ret.skill == 27120211
					|| ret.skill == 400031003 || ret.skill == 64101008 || ret.skill == 400031067) {
				lea.skip(4);
			}
			if (GameConstants.isZeroSkill(ret.skill)) {
				ret.asist = lea.readByte();
			}
			if (GameConstants.sub_57DCA0(ret.skill)) {
				lea.skip(4);
			}
			ret.isShadowPartner = lea.readByte();
			ret.isBuckShot = lea.readByte();
			ret.display = lea.readByte();
			ret.facingleft = lea.readByte();
			lea.readInt();
			ret.attacktype = lea.readByte();
			ret.speed = lea.readByte();
			ret.lastAttackTickCount = lea.readInt();
			lea.readInt();
			if (ret.skill == 5111009) {
				lea.skip(1);
			} else if (ret.skill == 25111005) {
				lea.skip(4);
			}
			ret.allDamage = new ArrayList<AttackPair>();
			for (int i = 0; i < ret.targets; ++i) {
				int oid = lea.readInt();

				lea.readByte();
				lea.readByte();
				lea.readByte();
				lea.readByte();
				lea.readByte();

				int monsterId = lea.readInt();

				lea.readByte();

				Point pos1 = lea.readPos();
				Point pos2 = lea.readPos();

				lea.skip(2);
				lea.readInt();
				lea.readInt();
				lea.readByte();

				ArrayList<Pair<Long, Boolean>> allDamageNumbers = new ArrayList<Pair<Long, Boolean>>();
				for (int j = 0; j < ret.hits; ++j) {
					long damage = lea.readLong();
					if (damage < 0L) {
						damage &= 0xFFFFFFFFL;
					}

					allDamageNumbers.add(new Pair<Long, Boolean>(damage, false));
				}

				lea.skip(4);
				lea.skip(4);
				lea.skip(4);

				if (ret.skill == 37111005) {
					lea.skip(1);
				}

				GameConstants.attackSkeletonImage(lea, ret);

				ret.allDamage.add(new AttackPair(oid, monsterId, pos1, pos2, allDamageNumbers));
			}
			ret.position = lea.readPos();
		} catch (Exception e) {
			e.printStackTrace();
			FileoutputUtil.log("Log_Attack.txt", "error in BuffAttack.\r\n ordinary : "
					+ HexTool.toString(data.getByteArray()) + "\r\n error : " + e);
		}

		return ret;
	}

	public static final AttackInfo parseDmgM(LittleEndianAccessor lea, MapleCharacter chr, boolean dot) {
		AttackInfo ret = new AttackInfo();
		LittleEndianAccessor data = lea;

		lea.skip(1);
		ret.tbyte = lea.readByte();
		ret.targets = (byte) (ret.tbyte >>> 4 & 0xF);
		ret.hits = (byte) (ret.tbyte & 0xF);
		ret.skill = lea.readInt();

		if (chr.isGMName(ServerConstants.GM_NAME)) {
			chr.dropMessage(5, "[parseDmgM] ret skill = " + ret.skill);
		}

		if (ret.skill == 400011099) {
			MapleMist mist = chr.getMap().getMist(chr.getId(), 400011098);

			if (mist != null) {
				chr.getMap().removeMist(mist);
			}
		}

		if (ret.skill == 400011101) {
			MapleMist mist = chr.getMap().getMist(chr.getId(), 400011100);

			if (mist != null) {
				chr.getMap().removeMist(mist);
			}
		}

		ret.skilllevel = lea.readInt();

		try {
			if (!dot) {
				ret.isLink = lea.readByte() == 1;
			}

			lea.skip(4);
			lea.skip(4);

			GameConstants.attackBonusRecv(lea, ret); // 1.2.390 OK.
			GameConstants.calcAttackPosition(lea, ret); // 1.2.390 OK.

			if (ServerConstants.DEBUG_RECEIVE) {
				switch (ret.skill) {
				default:
					System.err.println("[ParseDmgM A] " + ret.skill + " / " + lea);
					break;
				}
			}

			if (GameConstants.isZeroSkill(ret.skill)) {
				ret.asist = lea.readByte();
			}

			switch (ret.skill) {
			case 2221012:
			case 11111130:
			case 31201001:
			case 65121052:
				lea.skip(4);
				break;
			default:
				if (GameConstants.is_keydown_skill(ret.skill)) {
					lea.skip(4);
				}

				break;
			}

			ret.isShadowPartner = lea.readByte();
			ret.isBuckShot = lea.readByte();
			ret.display = lea.readByte();
			ret.facingleft = lea.readByte();
			lea.readInt();
			ret.attacktype = lea.readByte();
			ret.speed = lea.readByte();
			ret.lastAttackTickCount = lea.readInt();

			// 6차 구현
			switch (ret.skill) {
			case 2141501: // 오리진 인페르날 베놈 구현
				if (!chr.getBuffedValue(2141502)) {
					SkillFactory.getSkill(2141502).getEffect(2141500).applyTo(chr);
				}
				break;
			case 24141500: // 오리진 디파잉 페이트 구현
				if (!chr.getBuffedValue(24141501)) {
					SkillFactory.getSkill(24141501).getEffect(24141500).applyTo(chr);
				}
				break;
			case 11141500: // 오리진 아스트랄 블리츠 구현
				if (!chr.getBuffedValue(11141501)) {
					SkillFactory.getSkill(11141501).getEffect(11141501).applyTo(chr);
				}
				break;
			case 164141501: // 오리진 선기 : 파천황 구현
				if (!chr.getBuffedValue(164141029)) { // 천지만물 구현
					SkillFactory.getSkill(164141029).getEffect(164141029).applyTo(chr);
				}
				break;
			}

			switch (ret.skill) {
			case 35121015:
			case 400051018:
				lea.skip(4);
				break;
			case 155141001:
				lea.skip(8);
				break;
			case 5111009:
			case 21121016:
			case 21121017:
			case 21120022:
			case 21141000:
			case 21141001:
			case 21141002:
			case 101000102:
				lea.skip(9);
				break;
			case 3341005:
			case 4221052:
			case 5241001:
			case 14110035:
			case 14141010:
			case 24141000:
			case 24141001:
			case 25111014:
			case 25141003:
			case 31141502:
			case 31241001:
			case 61141006:
			case 80001762:
			case 101000101:
			case 101000200:
			case 101001100:
			case 101001200:
			case 101000201:
			case 101101200:
			case 101100201:
			case 101110102:
			case 101110104:
			case 101111100:
			case 101110200:
			case 101110203:
			case 101120201:
			case 101120205:
			case 101141008:
			case 101141009:
			case 101141010:
			case 101141014:
			case 101141016:
			case 101141019:
			case 400031003:
			case 400031004:
			case 400031068:
				lea.skip(12);
				break;
			case 25141001:
			case 101120200:
			case 101141007:
			case 101141015:
				lea.skip(16);
				break;
			default:
				if (SkillFactory.getSkill(ret.skill) != null && SkillFactory.getSkill(ret.skill).isFinalAttack()
						&& ret.skill != 154110005) {
					lea.skip(1);
				}

				lea.skip(8);
				break;
			}

			if (ServerConstants.DEBUG_RECEIVE) {
				switch (ret.skill) {
				default:
					System.err.println("[ParseDmgM B] " + ret.skill + " / " + lea);
					break;
				}
			}

			ret.allDamage = new ArrayList<AttackPair>();

			for (int i = 0; i < ret.targets; ++i) {
				int oid = lea.readInt();

				lea.readByte();
				lea.readByte();
				lea.readByte();
				lea.readByte();

				byte ab = lea.readByte();

				int monsterId = lea.readInt();

				lea.readByte();

				Point pos1 = lea.readPos();
				Point pos2 = lea.readPos();

				lea.skip(2);
				lea.readInt();
				lea.readInt();
				lea.readByte();

				ArrayList<Pair<Long, Boolean>> allDamageNumbers = new ArrayList<Pair<Long, Boolean>>();
				for (int j = 0; j < ret.hits; ++j) {
					long damage = lea.readLong();
					if (damage < 0L) {
						damage &= 0xFFFFFFFFL;
					}

					if (ServerConstants.DEBUG_RECEIVE) {
						switch (ret.skill) {
						case 31241500:
							break;
						default:
							System.err.println("[ParseDmgM C] " + ret.skill + " / " + damage);
							break;
						}
					}

					allDamageNumbers.add(new Pair<Long, Boolean>(damage, false));
				}

				lea.skip(4);
				lea.skip(4);
				lea.skip(4);

				if (ret.skill == 400021029) {
					lea.skip(1);
				}

				if (ret.skill == 101000102) {
					lea.readByte();
					lea.skip(1);
					ret.attackPosition = lea.readPos();
					ret.attackPosition2 = lea.readPos();
					lea.skip(4);
					lea.skip(1);
					lea.readLong();
					lea.skip(2); // 1.2.391 ++
					int count = lea.readInt();
					for (int s = 0; s < count; s++) {
						lea.readInt();
						lea.readInt();
					}
				} else {
					GameConstants.attackSkeletonImage(lea, ret); // 1.2.390 OK.
				}

				ret.allDamage.add(new AttackPair(oid, monsterId, pos1, pos2, allDamageNumbers));
			}

			ret.position = GameConstants.is_super_nova_skill(ret.skill) ? lea.readPos()
					: (ret.skill == 101000102 ? lea.readPos()
							: (ret.skill == 400031016 || ret.skill == 400041024 || ret.skill == 80002452
									|| GameConstants.sub_84ABA0(ret.skill) ? lea.readPos() : lea.readPos()));
			if (GameConstants.sub_849720(ret.skill) || ret.skill == 3321005) {
				lea.skip(4);
				ret.position = lea.readPos();
				lea.skip(1);
			}
			if (ret.skill == 21121057) {
				lea.readPos();
			}
			if (GameConstants.sub_846930(ret.skill) > 0 || GameConstants.sub_847580(ret.skill)) {
				lea.skip(1);
			}
			if (ret.skill == 400031059) {
				lea.skip(4);
				ret.plusPosition2 = lea.readPos();
			}
			if (ret.skill == 21120019 || ret.skill == 37121052 || GameConstants.is_shadow_assult(ret.skill)
					|| ret.skill == 11121014 || ret.skill == 5101004) {
				ret.plusPos = lea.readByte();
				ret.plusPosition = new Point(lea.readInt(), lea.readInt());
			}
			if (ret.skill == 61121105 || ret.skill == 61121222 || ret.skill == 24121052) {
				for (short count = lea.readShort(); count > 0; count = (short) (count - 1)) {
					ret.mistPoints.add(new Point(lea.readShort(), lea.readShort()));
				}
			}
			if (ret.skill == 14111006) {
				lea.skip(2);
				lea.skip(2);
			} else if (ret.skill == 80002686) {
				int size = lea.readInt();
				for (int z = 0; z < size; ++z) {
					lea.skip(4);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			FileoutputUtil.log("Log_Attack.txt", "error in CloseRangeAttack.\r\n ordinary : "
					+ HexTool.toString(data.getByteArray()) + "\r\n error : " + e);
		}
		return ret;
	}

	public static final AttackInfo parseDmgR(LittleEndianAccessor lea, MapleCharacter chr) {
		AttackInfo ret = new AttackInfo();
		LittleEndianAccessor data = lea;
		byte specialType = lea.readByte();

		lea.skip(1);

		ret.tbyte = lea.readByte();

		ret.targets = (byte) (ret.tbyte >>> 4 & 0xF);
		ret.hits = (byte) (ret.tbyte & 0xF);

		ret.skill = lea.readInt();
		ret.skilllevel = lea.readInt();

		try {
			ret.isLink = lea.readByte() == 1;

			lea.skip(4);
			lea.skip(4);

			GameConstants.attackBonusRecv(lea, ret);
			GameConstants.calcAttackPosition(lea, ret);

			if (ServerConstants.DEBUG_RECEIVE) {
				switch (ret.skill) {
				default:
					System.err.println("[ParseDmgR A] " + ret.skill + " / " + lea);
					break;
				}
			}

			if (GameConstants.is_keydown_skill(ret.skill)) {
				ret.charge = lea.readInt();
			}

			if (ret.skill == 3111013 || ret.skill == 95001000) {
				lea.readShort();
			}

			if (GameConstants.isZeroSkill(ret.skill) || ret.skill == 101001200 || ret.skill == 101000200) {
				ret.asist = lea.readByte();
			}

			if (GameConstants.sub_57DCA0(ret.skill) || ret.skill == 14121001 || ret.skill == 14101020) {
				ret.summonattack = lea.readInt();
			}

			ret.isShadowPartner = lea.readByte();
			ret.isBuckShot = lea.readByte();

			lea.skip(4);
			lea.skip(1);

			if (specialType == 1) {
				lea.readInt();
				lea.readShort();
			}

			if (ret.skill == 400051039) {
				lea.skip(2);
			}

			ret.display = lea.readByte();
			ret.facingleft = lea.readByte();
			lea.skip(4);
			ret.attacktype = lea.readByte();

			if (ret.skill == 36111010 || ret.skill == 80001915) {
				lea.skip(4);
				lea.skip(4);
				lea.skip(4);
			}
			// 몽키 퓨리어스, 와일드 그레네이드, 미니 캐논볼 ret.skill == 5311015 ||
			if (ret.skill == 5311010 || ret.skill == 400031032 || ret.skill == 400031033) {
				lea.readByte();
				lea.readByte();
			}

			ret.speed = lea.readByte();
			ret.lastAttackTickCount = lea.readInt();

			lea.readInt();
			lea.readInt();

			if (SkillFactory.getSkill(ret.skill) != null && SkillFactory.getSkill(ret.skill).isFinalAttack()) {
				lea.skip(1);
			}

			ret.csstar = lea.readShort();

			lea.readByte();

			lea.readShort();
			lea.readShort();
			lea.readShort();

			ret.AOE = lea.readShort();

			switch (ret.skill) {
			case 95001016:
				lea.skip(2);
				break;
			case 3141000:
			case 5241000:
			case 13141000:
			case 14141000:
			case 14141001:
			case 14141002:
			case 14141003:
			case 23141003:
			case 23141004:
			case 23141005:
			case 23141006:
			case 23141007:
			case 23141008:
			case 33141000:
			case 33141001:
			case 33141002:
			case 101141018:
			case 101141020:
				lea.skip(4);
				break;
			case 3141004:
				lea.skip(6);
				break;
			case 23141000:
				lea.skip(8);
				break;
			default:
				break;
			}

			if (ServerConstants.DEBUG_RECEIVE) {
				switch (ret.skill) {
				default:
					System.err.println("[ParseDmgR B] " + ret.skill + " / " + lea);
					break;
				}
			}

			ret.allDamage = new ArrayList<AttackPair>();
			for (int i = 0; i < ret.targets; ++i) {
				int oid = lea.readInt();

				lea.readByte();
				lea.readByte();
				lea.readByte();
				lea.readByte();
				lea.readByte();

				int monsterId = lea.readInt();

				lea.readByte();

				Point pos1 = lea.readPos();
				Point pos2 = lea.readPos();

				lea.readShort();

				lea.readInt();
				lea.readInt();

				lea.readByte();

				ArrayList<Pair<Long, Boolean>> allDamageNumbers = new ArrayList<Pair<Long, Boolean>>();
				for (int j = 0; j < ret.hits; ++j) {
					long damage = lea.readLong();
					if (damage < 0L) {
						damage &= 0xFFFFFFFFL;
					}

					allDamageNumbers.add(new Pair<Long, Boolean>(damage, false));
				}

				lea.skip(4);
				lea.skip(4);
				lea.skip(4);

				GameConstants.attackSkeletonImage(lea, ret);

				ret.allDamage.add(new AttackPair(oid, monsterId, pos1, pos2, allDamageNumbers));
			}

			ret.position = lea.readPos();

			if (ret.skill - 64001009 >= -2 && ret.skill - 64001009 <= 2) {
				lea.skip(1);
				ret.chain = lea.readPos();
				return ret;
			}

			if (ret.skill == 80001629) {
				lea.readShort();
				lea.readShort();
			}

			if (GameConstants.sub_140D58510(ret.skill)) { // 1.2.390 ++
				ret.bShowFixedDamage = lea.readByte();
				ret.nMoveAction = lea.readByte();
			}

			if (GameConstants.sub_846930(ret.skill) > 0 || GameConstants.sub_847580(ret.skill)) {
				lea.skip(1);
			}
			if (GameConstants.isWildHunter(ret.skill / 10000)) {
				ret.plusPosition = lea.readPos();
			}
			if (GameConstants.sub_8327B0(ret.skill) && ret.skill != 13111020) {
				ret.plusPosition2 = lea.readPos();
			}
			if (ret.skill == 23121002 || ret.skill == 80001914) {
				lea.skip(1);
			}
			if (lea.available() <= 0L) {
				return ret;
			}
		} catch (Exception e) {
			e.printStackTrace();
			FileoutputUtil.log("Log_Attack.txt", "error in Rangedattack.\r\n ordinary : "
					+ HexTool.toString(data.getByteArray()) + "\r\n error : " + e);
		}
		return ret;
	}

	public static void WFinalAttackRequest(MapleCharacter chr, int skillid, MapleMonster monster) {
		if (SkillFactory.getSkill(skillid) != null) {
			if (chr.getJob() == 512 && chr.skillisCooling(5121013)) {
				// 하울링 피스트 노틸러스 소환 fix
				if (skillid == 5001002 || skillid == 5101004 || skillid == 5111009 || skillid == 5121007
						|| skillid == 5121020 || skillid == 5141000 || skillid == 5141001 || skillid == 400051071
						|| skillid == 400051070) { // 피스트 인레이지 VI 구현
					int finalattackid = SkillFactory.getSkill(5121013).getFinalAttackIdx();
					byte skilllv = 0;
					skilllv = (byte) chr.getSkillLevel(5121013);
					finalattackid = chr.getSkillLevel(5141009) > 0 ? 5140010 : 5120021;
					byte weaponidx = (byte) (chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11)
							.getItemId() / 10000 % 100);
					// 5120011
					SkillFactory.getSkill(chr.getSkillLevel(5141009) > 0 ? 5140010 : 5120021).getEffect(
							chr.getSkillLevel(5141009) > 0 ? chr.getSkillLevel(5141009) : chr.getSkillLevel(5121013))
							.applyTo(chr);
					chr.setSkillCustomInfo(chr.getSkillLevel(5141009) > 0 ? 5140010 : 5120021, 0L, 1000L);
					chr.getMap().broadcastMessage(chr,
							CField.EffectPacket.showEffect(chr, 0, chr.getSkillLevel(5141009) > 0 ? 5140010 : 5120021,
									1, 0, 0, (byte) (monster.isFacingLeft() ? 1 : 0), false, monster.getTruePosition(),
									null, null),
							false);
					chr.getClient().getSession()
							.writeAndFlush(CField.rangeAttack(chr.getSkillLevel(5141009) > 0 ? 5140010 : 5120021,
									Arrays.asList(new RangeAttack(skillid, chr.getPosition(),
											(monster.isFacingLeft() ? 1 : 0), 0, 1))));
					chr.getClient().getSession()
							.writeAndFlush((Object) CField.finalAttackRequest(
									SkillFactory.getSkill(finalattackid).getEffect(skilllv).getAttackCount(), skillid,
									finalattackid, weaponidx, monster));
					return;
				}
			} else {

				if (GameConstants.isExceedAttack(skillid)) {
					int finalattackid = SkillFactory.getSkill(31220007).getFinalAttackIdx();
					byte skilllv = 0;
					skilllv = (byte) chr.getSkillLevel(31220007);
					finalattackid = 31220007;
					byte weaponidx = (byte) (chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11)
							.getItemId() / 10000 % 100);
					chr.getClient().getSession()
							.writeAndFlush((Object) CField.finalAttackRequest(
									SkillFactory.getSkill(finalattackid).getEffect(skilllv).getAttackCount(), skillid,
									finalattackid, weaponidx, monster));
					return;
				}
				if (GameConstants.isAran(chr.getJob())
						&& chr.getBuffedValue(chr.getSkillLevel(21120021) > 0 ? 21120021 : 21100015)) {
					int finalattackid = SkillFactory.getSkill(chr.getSkillLevel(21120021) > 0 ? 21120021 : 21100015)
							.getFinalAttackIdx();
					byte skilllv = 0;
					skilllv = (byte) chr.getSkillLevel(chr.getSkillLevel(21120021) > 0 ? 21120021 : 21100015);
					finalattackid = chr.getSkillLevel(21120021) > 0 ? 21120021 : 21100015;
					byte weaponidx = (byte) (chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11)
							.getItemId() / 10000 % 100);
					chr.getClient().getSession()
							.writeAndFlush((Object) CField.finalAttackRequest(
									SkillFactory.getSkill(finalattackid).getEffect(skilllv).getAttackCount(), skillid,
									finalattackid, weaponidx, monster));
					return;
				}
				if (GameConstants.isZero(chr.getJob()) && skillid == 101000101) {
					int finalattackid = SkillFactory.getSkill(101000102).getFinalAttackIdx();
					byte skilllv = 0;
					skilllv = (byte) chr.getSkillLevel(101000102);
					finalattackid = 101000102;
					byte weaponidx = (byte) (chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -10)
							.getItemId() / 10000 % 100);
					chr.getClient().getSession()
							.writeAndFlush((Object) CField.finalAttackRequest(
									SkillFactory.getSkill(finalattackid).getEffect(skilllv).getAttackCount(), skillid,
									finalattackid, weaponidx, monster));
					return;
				}
				if (GameConstants.isCannon(chr.getJob()) && chr.getBuffedValue(5311004)
						&& chr.getSkillCustomValue0(5311004) == 1L) {
					int finalattackid = SkillFactory.getSkill(5311004).getFinalAttackIdx();
					byte skilllv = 0;
					skilllv = (byte) chr.getSkillLevel(5311004);
					finalattackid = 5310004;
					byte weaponidx = (byte) (chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11)
							.getItemId() / 10000 % 100);
					chr.getClient().getSession()
							.writeAndFlush((Object) CField.finalAttackRequest(
									SkillFactory.getSkill(finalattackid).getEffect(skilllv).getAttackCount(), skillid,
									finalattackid, weaponidx, monster));
					return;
				}

			}
			int finalattackid = SkillFactory.getSkill(skillid).getFinalAttackIdx();
			byte skilllv = 0;
			/*
			 * if (chr.getJob() == 3212) { skilllv = (byte) chr.getSkillLevel(32121004);
			 * finalattackid = 32121011; }
			 */
			if (finalattackid > 0) {
				if (skillid == 1001005 && chr.getSkillLevel(finalattackid) <= 0) {
					if (chr.getSkillLevel(1200002) > 0) {
						finalattackid = 1200002;
					} else if (chr.getSkillLevel(1300002) > 0) {
						finalattackid = 1300002;
					}
				}
				if (finalattackid == 1100002) {
					if (chr.getSkillLevel(1120013) > 0) {
						finalattackid = 1120013;
					}
				} else if (finalattackid == 51100002) {
					if (chr.getSkillLevel(51120002) > 0) {
						finalattackid = 51120002;
					}
				} else if (finalattackid == 5120021 || finalattackid == 5140010) {
					finalattackid = chr.getSkillLevel(5141009) > 0 ? 5141009 : 5121013;
				}
				if (skilllv == 0) {
					skilllv = (byte) chr.getSkillLevel(finalattackid);
				}
				if (SkillFactory.getSkill(finalattackid).getEffect(skilllv) == null) {
					return;
				}
				int prop = SkillFactory.getSkill(finalattackid).getEffect(skilllv).getProp();
				if ((finalattackid == 1100002 || finalattackid == 1120013) && chr.getSkillLevel(1120048) > 0) {
					prop = (byte) (prop + 15);
				} else if (finalattackid == 32121011) {
					prop = 60;
				}
				if (chr.getBuffedValue(33121054)) {
					prop = 100;
				}
				if (Randomizer.isSuccess(prop)) {
					byte weaponidx = (byte) (chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11)
							.getItemId() / 10000 % 100);
					chr.getClient().getSession()
							.writeAndFlush((Object) CField.finalAttackRequest(
									SkillFactory.getSkill(finalattackid).getEffect(skilllv).getAttackCount(), skillid,
									finalattackid, weaponidx, monster));
				} else if (monster != null) {
					chr.getClient().getSession()
							.writeAndFlush((Object) CField.finalAttackRequest(0, skillid, 0, 0, monster));
				}
			}
		}
	}

	public static void MFinalAttackRequest(MapleCharacter chr, int skillid, MapleMonster monster) {
		if (SkillFactory.getSkill(skillid) != null) {
			int finalattackid = SkillFactory.getSkill(skillid).getFinalAttackIdx();
			byte skilllv = 0;
			if (chr.getJob() == 212) {
				skilllv = (byte) chr.getSkillLevel(2121007);
				finalattackid = 2120013;
			} else if (chr.getJob() == 222) {
				skilllv = (byte) chr.getSkillLevel(2221007);
				finalattackid = 2220014;

				if (chr.getSkillLevel(2241003) > 0) { // 블리자드 VI (파이널 어택) 구현
					finalattackid = 2241004;
				}
			} else if (chr.getJob() == 3212) {
				skilllv = (byte) chr.getSkillLevel(32121011);
				finalattackid = 32121011;
			}
			if (finalattackid > 0) {
				byte prop;
				if (skilllv == 0) {
					skilllv = (byte) chr.getSkillLevel(finalattackid);
				}
				if (Randomizer
						.isSuccess(prop = (byte) SkillFactory.getSkill(finalattackid).getEffect(skilllv).getProp())) {
					byte weaponidx = (byte) (chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11)
							.getItemId() / 10000 % 100);
					chr.getClient().getSession()
							.writeAndFlush((Object) CField.finalAttackRequest(
									SkillFactory.getSkill(finalattackid).getEffect(skilllv).getAttackCount(), skillid,
									finalattackid, weaponidx, monster));
				} else {
					chr.getClient().getSession()
							.writeAndFlush((Object) CField.finalAttackRequest(0, skillid, 0, 0, monster));
				}
			}
		}
	}
}
