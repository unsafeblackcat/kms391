package client.damage;

import client.*;
import client.inventory.Equip;
import client.inventory.Item;
import client.inventory.MapleInventoryType;
import client.inventory.MapleWeaponType;
import constants.GameConstants;
import handling.world.PlayerBuffValueHolder;
import server.MapleItemInformationProvider;
import server.SecondaryStatEffect;
import server.StructSetItem;
import tools.Pair;

import java.util.*;

public class VerifyDamage {

	/**
	 * 计算玩家的各项属性统计信息，最终得出最大基础伤害
	 * 
	 * @param player 玩家角色对象
	 * @return 最大基础伤害值
	 */
	public static double CalculatePlayerStats(MapleCharacter player) {
		// 获取物品信息提供者实例，用于获取装备的相关信息
		final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();

		// 初始化各种属性变量，用于存储玩家的各项属性值
		int base_str = 0, base_dex = 0, base_int = 0, base_luk = 0, add_str = 0, add_dex = 0, add_int = 0, add_luk = 0,
				char_str = 0, char_dex = 0, char_int = 0, char_luk = 0, per_str = 0, per_dex = 0, per_int = 0,
				per_luk = 0, final_str = 0, final_dex = 0, final_int = 0, final_luk = 0, total_str = 0, total_dex = 0,
				total_int = 0, total_luk = 0, base_mhp = 0, base_mmp = 0, add_mhp = 0, add_mmp = 0, char_mhp = 0,
				char_mmp = 0, per_mhp = 0, per_mmp = 0, final_mhp = 0, final_mmp = 0, total_mhp = 0, total_mmp = 0,
				inc_meso = 0, inc_drop = 0, base_watk = 0, base_matk = 0, add_watk = 0, add_matk = 0, char_watk = 0,
				char_matk = 0, per_watk = 0, per_matk = 0, final_watk = 0, final_matk = 0, total_watk = 0,
				total_matk = 0, total_damage = 0, base_boss_damage = 0, per_boss_damage = 0, base_ignore = 0,
				per_ignore = 0, reduce_cooltime = 0, main_stat = 0, second_stat = 0, joker_item_id = 0;
		double total_final_damage = 1;
		// 存储套装选项信息，键为套装 ID，值为该套装的物品数量
		Map<Integer, Byte> SetOptions = new HashMap<>();
		// 存储装备的潜在属性信息
		List<Pair<Integer, Integer>> potentials = new ArrayList<>();

		// ====================================== Calculating Equipment Option
		// ======================================
		// 同步访问玩家装备栏，确保数据一致性
		synchronized (player.getInventory(MapleInventoryType.EQUIPPED)) {
			// 获取装备栏物品迭代器
			final Iterator<Item> itera = player.getInventory(MapleInventoryType.EQUIPPED).newList().iterator();
			while (itera.hasNext()) {
				// 获取当前装备
				final Equip equip = (Equip) itera.next();

				// 检查是否为安卓心脏装备，需要穿戴安卓设备才能生效
				if (equip.getItemId() / 1000 == 1672) {
					final Item android = player.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -27);
					if (android == null) {
						continue;
					}
				}

				// 计算装备潜在属性等级
				int potlevel = (ii.getReqLevel(equip.getItemId()) / 10) - 1;
				// 添加装备的潜在属性到列表中
				potentials.add(new Pair<>(equip.getPotential1(), potlevel));
				potentials.add(new Pair<>(equip.getPotential2(), potlevel));
				potentials.add(new Pair<>(equip.getPotential3(), potlevel));
				potentials.add(new Pair<>(equip.getPotential4(), potlevel));
				potentials.add(new Pair<>(equip.getPotential5(), potlevel));
				potentials.add(new Pair<>(equip.getPotential6(), potlevel));

				// 获取装备所属套装 ID
				final Integer set = ii.getSetItemID(equip.getItemId());
				if (set != null && set > 0) {
					byte value = 1;
					if (SetOptions.containsKey(set)) {
						value += SetOptions.get(set);
					}
					// 更新套装选项信息
					SetOptions.put(set, value);
				}

				// 累加装备提供的百分比最大生命值属性
				if (ii.getEquipStats(equip.getItemId()) != null) {
					if (ii.getEquipStats(equip.getItemId()).get("MHPr") != null) {
						per_mhp += ii.getEquipStats(equip.getItemId()).get("MHPr");
					}
					if (ii.getEquipStats(equip.getItemId()).get("MMPr") != null) {
						per_mmp += ii.getEquipStats(equip.getItemId()).get("MMPr");
					}
				}

				// 记录 Joker 物品 ID
				if (ii.isJokerToSetItem(equip.getItemId()) && joker_item_id > equip.getItemId()) {
					joker_item_id = equip.getItemId();
				}

				// 累加属性百分比
				per_dex += equip.getAllStat();
				per_int += equip.getAllStat();
				per_str += equip.getAllStat();
				per_luk += equip.getAllStat();

				// 累加总伤害和首领伤害
				total_damage += equip.getTotalDamage();
				base_boss_damage += equip.getBossDamage();

				// 累加属性值
				add_mhp += equip.getHp();
				add_mmp += equip.getMp();
				add_dex += equip.getDex();
				add_int += equip.getInt();
				add_str += equip.getStr();
				add_luk += equip.getLuk();
				add_watk += equip.getWatk();
				add_matk += equip.getMatk();
			}
		}
		// ====================================== 计算装备套装选项
		// ======================================
		for (Integer setId : SetOptions.keySet()) {
			// 获取套装信息
			final StructSetItem set = ii.getSetItem(setId);
			if (set != null) {
				// 获取套装中的物品信息
				final Map<Integer, StructSetItem.SetItem> itemz = set.getItems();
				// 处理 Joker 物品
				if (set.jokerPossible && joker_item_id > 0 && SetOptions.get(setId) < set.completeCount) {
					for (int itemId : set.itemIDs) {
						if (GameConstants.isWeapon(itemId) && GameConstants.isWeapon(joker_item_id)) {
							SetOptions.put(setId, (byte) (SetOptions.get(setId) + 1));
							break;
						} else if (!GameConstants.isWeapon(itemId) && !GameConstants.isWeapon(joker_item_id)
								&& itemId / 10000 == joker_item_id / 10000
								&& player.getInventory(MapleInventoryType.EQUIPPED).findById(itemId) == null) {
							SetOptions.put(setId, (byte) (SetOptions.get(setId) + 1));
							break;
						}
					}
				}
				// 遍历套装中的每个物品
				for (Map.Entry<Integer, StructSetItem.SetItem> ent : itemz.entrySet()) {
					if (ent.getKey() <= SetOptions.get(setId)) {
						StructSetItem.SetItem se = ent.getValue();
						// 累加属性值
						add_str += se.incSTR + se.incAllStat;
						add_dex += se.incDEX + se.incAllStat;
						add_int += se.incINT + se.incAllStat;
						add_luk += se.incLUK + se.incAllStat;
						add_watk += se.incPAD;
						add_matk += se.incMAD;
						// 处理套装选项
						int[][] options = new int[][] { { se.option1, se.option1Level },
								{ se.option2, se.option2Level } };
						for (int[] option : options) {
							if (ii.getPotentialInfo(option[0]) != null) {
								potentials.add(new Pair<>(option[0], option[1]));
							}
						}
						// 累加属性值
						add_mhp += se.incMHP;
						add_mmp += se.incMMP;
						per_mhp += se.incMHPr;
						per_mmp += se.incMMPr;
					}
				}
			}
		}
		// ==============================================================================================================

		// ========================== 计算装备潜在属性选项，特殊套装选项 ==========================
		for (Pair<Integer, Integer> potential : potentials) {
			int lv = potential.right;
			if (lv < 0) {
				lv = 0;
			}
			// 检查潜在属性是否有效
			if (potential.left == 0 || ii.getPotentialInfo(potential.left) == null
					|| ii.getPotentialInfo(potential.left).get(lv) == null) {
				continue;
			}
			// 获取潜在属性信息
			StructPotentialItem pot = ii.getPotentialInfo(potential.left).get(lv);
			// 累加属性值
			add_mhp += pot.incMHP / (GameConstants.isDemonAvenger(player.getJob()) ? 2 : 1);
			add_mmp += pot.incMMP;
			per_mhp += pot.incMHPr / (GameConstants.isDemonAvenger(player.getJob()) ? 2 : 1);
			per_mmp += pot.incMMPr;
			inc_meso += pot.incMesoProp;
			inc_drop += pot.incRewardProp;
			reduce_cooltime += pot.reduceCooltime;

			add_dex += pot.incDEX;
			add_int += pot.incINT;
			add_str += pot.incSTR;
			add_luk += pot.incLUK;

			per_dex += pot.incDEXr;
			per_int += pot.incINTr;
			per_str += pot.incSTRr;
			per_luk += pot.incLUKr;

			per_watk += pot.incPADr;
			per_matk += pot.incMADr;

			if (!pot.boss) {
				total_damage += pot.incDAMr;
			} else {
				per_boss_damage += pot.incDAMr;
			}
			per_ignore += pot.ignoreTargetDEF;
		}
		// ==============================================================================================================

		// 处理技能和 buff 效果
		{
			final List<SecondaryStatEffect> effects = new ArrayList<>();
			final Set<Skill> skills = player.getSkills().keySet();
			// 遍历玩家的技能
			for (Skill skill : skills) {
				int skillLevel = player.getTotalSkillLevel(skill);
				if (skill.getPsd() == 1) {
					effects.add(skill.getEffect(skillLevel));
				}
			}
			// 遍历玩家的 buff
			for (PlayerBuffValueHolder skill : player.getAllBuffs()) {
				effects.add(skill.effect);
			}

			// 遍历所有效果
			for (SecondaryStatEffect effect : effects) {
				Skill skill = SkillFactory.getSkill(effect.getSourceId());
				if (effect.getLevel() > 0) {
					if (skill.getPsdSkills() != null && skill.getPsdSkills().size() == 0) {
						// 处理特殊技能效果
						for (int i = 0; i < (String.valueOf(skill.getId()).startsWith("7000") ? 2 : 1); i++) {
							// 累加属性值
							add_str += effect.getStrX() + effect.getStr();
							add_dex += effect.getDexX() + effect.getDex();
							add_int += effect.getIntX() + effect.getInt();
							add_luk += effect.getLukX() + effect.getLuk();

							add_mhp += effect.getMaxHpX();
							add_mmp += effect.getMaxMpX();

							per_mhp += effect.getPercentHP();
							per_mmp += effect.getPercentMP();

							per_watk += effect.getWatk();
							per_matk += effect.getMatk();

							add_watk += effect.getAttackX();
							add_matk += effect.getMagicX();

							add_watk += effect.getWatk();
							add_matk += effect.getMatk();

							final_str += effect.getStrFX();
							final_dex += effect.getDexFX();
							final_int += effect.getIntFX();
							final_luk += effect.getLukFX();

							total_damage += effect.getDAMRate();
							total_final_damage *= 1 + (effect.getPdR() / 100.0);
							total_final_damage *= 1 + (effect.getMdR() / 100.0);
							per_mhp += effect.getHpFX();

							// 处理特殊技能
							switch (skill.getId()) {
							case 12110025:
								total_final_damage *= 1 + (effect.getZ() / 100.0);
								break;
							case 12100027:
							case 12120009:
								add_matk += effect.getX();
							}
						}
					}
				}
			}
		}

		// 获取玩家的基础属性
		base_str = player.getStat().getStr();
		base_dex = player.getStat().getDex();
		base_int = player.getStat().getInt();
		base_luk = player.getStat().getLuk();
		base_mhp = (int) player.getStat().getMaxHp();
		base_mmp = (int) player.getStat().getMaxMp();

		// 计算角色的属性值
		char_str = base_str + add_str;
		char_dex = base_dex + add_dex;
		char_int = base_int + add_int;
		char_luk = base_luk + add_luk;
		char_mhp = base_mhp + add_mhp;
		char_mmp = base_mmp + add_mmp;
		char_watk = base_watk + add_watk;
		char_matk = base_matk + add_matk;

		// 计算总属性值
		total_str = (int) (char_str * (1.0 + (per_str / 100.0))) + final_str;
		total_dex = (int) (char_dex * (1.0 + (per_dex / 100.0))) + final_dex;
		total_int = (int) (char_int * (1.0 + (per_int / 100.0))) + final_int;
		total_luk = (int) (char_luk * (1.0 + (per_luk / 100.0))) + final_luk;
		total_mhp = (int) (char_mhp * (1.0 + (per_mhp / 100.0))) + final_mhp;
		total_mmp = (int) (char_mmp * (1.0 + (per_mmp / 100.0))) + final_mmp;
		total_watk = (int) (char_watk * (1.0 + (per_watk / 100.0))) + final_watk;
		total_matk = (int) (char_matk * (1.0 + (per_matk / 100.0))) + final_matk;

		// 获取玩家装备的武器
		final Item weapon_item = player.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11);
		// 获取玩家的职业
		final int job = player.getJob();
		// 获取武器类型
		final MapleWeaponType weapon = weapon_item == null ? MapleWeaponType.NOT_A_WEAPON
				: GameConstants.getWeaponType(weapon_item.getItemId());
		// 根据武器类型确定主属性和副属性
		switch (weapon) {
		case STAFF:
		case WAND:
			main_stat = total_int;
			second_stat = total_luk;
			break;
		case BOW:
		case CROSSBOW:
		case GUN:
			main_stat = total_dex;
			second_stat = total_str;
			break;
		case CLAW:
		case DAGGER:
		case KATARA:
			if ((job >= 400 && job <= 434) || (job >= 1400 && job <= 1412)) {
				main_stat = total_luk;
				second_stat = total_dex + total_str;
			} else { // Non Thieves
				main_stat = total_str;
				second_stat = total_dex;
			}
			break;
		case KNUCKLE:
		case TUNER:
			main_stat = total_str;
			second_stat = total_dex;
			break;
		case NOT_A_WEAPON:
			if ((job >= 500 && job <= 522) || (job >= 1500 && job <= 1512) || (job >= 3500 && job <= 3512)) {
				main_stat = total_str;
				second_stat = total_dex;
			}
			break;
		case ACIENTBOW:
		case AXE1H:
		case AXE2H:
		case BIG_SWORD:
		case BLUNT1H:
		case BLUNT2H:
		case CANE:
		case CHAIN:
		case DESPERADO:
		case DUAL_BOW:
		case ENERGYSWORD:
		case ESPLIMITER:
		case FAN:
		case GUNTLETREVOLVER:
		case HANDCANNON:
		case LONG_SWORD:
		case MAGICGUNTLET:
		case MAGIC_ARROW:
		case PLANE:
		case POLE_ARM:
		case SOULSHOOTER:
		case SPEAR:
		case SWORD:
		case SWORD1H:
		case SWORD2H:
		case TEDO:
		default:
			main_stat = total_str;
			second_stat = total_dex;
			break;
		}

		// 判断玩家是否为魔法师职业
		final boolean magician = (job >= 200 && job <= 232) || (job >= 1200 && job <= 1212)
				|| (job >= 2200 && job <= 2218) || (job >= 3200 && job <= 3212);
		// 判断玩家是否为火焰巫师职业
		boolean is_mage_constant = (job >= 200 && job <= 232) || GameConstants.isFlameWizard(job);
		// 根据职业确定常量
		double constant_job = is_mage_constant ? 1.2 : GameConstants.isXenon(job) ? 0.875 : 1.0;

		// 计算最大基础伤害
		double maxbasedamage = ((main_stat * 4.0) + second_stat) * 0.01 * (magician ? char_matk : char_watk)
				* weapon.getMaxDamageMultiplier() * constant_job * (1 + ((magician ? per_matk : per_watk) / 100.0))
				* (1 + (total_damage / 100.0)) * total_final_damage;

		return maxbasedamage;
	}

	/**
	 * 获取玩家的最大基础伤害
	 * 
	 * @param player 玩家角色对象
	 * @return 最大基础伤害值
	 */
	public static float getMaxBaseDamage(MapleCharacter player) {
		// 获取玩家的属性对象
		PlayerStats stat = player.getStat();
		// 重新计算本地属性
		stat.recalcLocalStats(player);
		// 返回当前最大基础伤害
		return stat.getCurrentMaxBaseDamage();
	}

	/**
	 * 获取玩家的最小基础伤害
	 * 
	 * @param player 玩家角色对象
	 * @return 最小基础伤害值
	 */
	public static float getMinBaseDamage(MapleCharacter player) {
		// 获取玩家的属性对象
		PlayerStats stat = player.getStat();
		// 重新计算本地属性
		stat.recalcLocalStats(player);
		// 计算并返回最小基础伤害
		return stat.calculateMinBaseDamage(player);
	}
}