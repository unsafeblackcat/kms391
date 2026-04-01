package handling.channel.handler;

import client.*;
import client.inventory.*;
import constants.GameConstants;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.Randomizer;
import server.StructSetItem;
import server.maps.FieldLimitType;
import server.movement.LifeMovementFragment;
import tools.data.LittleEndianAccessor;
import tools.packet.CField;
import tools.packet.CWvsContext;
import tools.packet.PetPacket;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PetHandler {

	/**
	 * 召唤宠物的方法
	 * 
	 * @param slea 用于读取数据包的 LittleEndianAccessor 对象
	 * @param c    客户端对象
	 * @param chr  角色对象
	 */
	public static void SpawnPet(final LittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
		// 跳过前 4 个字节的数据
		slea.skip(4);
		// 读取宠物所在的物品栏槽位
		final byte slot = slea.readByte();
		// 跳过 1 个字节的数据
		slea.readByte();
		// 从角色的现金物品栏中获取指定槽位的物品
		final Item item = chr.getInventory(MapleInventoryType.CASH).getItem(slot);
		if (item == null) {
			// 如果物品为空，向客户端发送可操作的消息
			c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
			return;
		}
		// 检查物品是否带有 Karma 使用标记，如果有则移除该标记
		if (ItemFlag.KARMA_USE.check(item.getFlag())) {
			item.setFlag(item.getFlag() - ItemFlag.KARMA_USE.getValue());
		}
		// 从物品中获取宠物对象
		final MaplePet pet = item.getPet();
		if (pet != null) {
			// 检查角色是否已经拥有该宠物
			if (chr.getPetIndex(pet) != -1) {
				// 如果已经拥有，则卸下该宠物
				chr.unequipPet(pet, false, false);
				// 向客户端发送可操作的消息
				c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
				// 更新宠物技能
				updatePetSkills(chr, pet);
				return;
			}
			// 检查物品是否未过期
			if (item.getExpiration() > System.currentTimeMillis()) {
				// 获取角色的当前位置
				final Point pos = chr.getPosition();
				// 设置宠物的位置
				pet.setPos(pos);
				// 如果地图有立足点信息，并且能找到宠物位置下方的立足点
				if (chr.getMap().getFootholds() != null
						&& chr.getMap().getFootholds().findBelow(pet.getPos()) != null) {
					// 设置宠物的立足点 ID
					pet.setFh(chr.getMap().getFootholds().findBelow(pet.getPos()).getId());
				}
				// 设置宠物的姿态
				pet.setStance(0);
				// 将宠物添加到角色的宠物列表中
				chr.addPet(pet);
				// 向地图中的其他玩家广播宠物召唤的消息
				chr.getMap().broadcastMessage(chr, PetPacket.showPet(chr, pet, false, false), true);
				// 向客户端发送更新宠物信息的消息
				c.getSession()
						.writeAndFlush((Object) PetPacket.updatePet(c.getPlayer(), pet,
								c.getPlayer().getInventory(MapleInventoryType.CASH).getItem(pet.getInventoryPosition()),
								false, c.getPlayer().getPetLoot()));
				// 更新宠物技能
				updatePetSkills(chr, null);
			} else {
				// 如果物品已过期，从现金物品栏中移除该物品
				c.getPlayer().getInventory(MapleInventoryType.CASH).removeItem(slot);
				// 向客户端发送清除物品栏槽位的消息
				c.getSession().writeAndFlush(
						(Object) CWvsContext.InventoryPacket.clearInventoryItem(MapleInventoryType.CASH, slot, false));
			}
		}
		// 向客户端发送可操作的消息
		c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
	}

	/**
	 * 宠物自动使用药水的方法
	 * 
	 * @param slea 用于读取数据包的 LittleEndianAccessor 对象
	 * @param c    客户端对象
	 * @param chr  角色对象
	 */
	public static final void Pet_AutoPotion(final LittleEndianAccessor slea, final MapleClient c,
			final MapleCharacter chr) {
		// 跳过 1 个字节的数据
		slea.skip(1);
		// 读取一个整数
		slea.readInt();

		// 检查角色是否满足使用药水的条件
		if (chr == null || !chr.isAlive() || chr.getBuffedEffect(SecondaryStat.DebuffIncHp) != null
				|| chr.getMap() == null || chr.hasDisease(SecondaryStat.StopPortion)
				|| chr.getBuffedValue(SecondaryStat.StopPortion) != null) {
			return;
		}

		// 读取药水所在的物品栏槽位
		final short slot = slea.readShort();
		// 从角色的使用物品栏中获取指定槽位的物品
		final Item toUse = chr.getInventory(MapleInventoryType.USE).getItem(slot);
		// 检查物品是否存在、数量是否足够、物品 ID 是否匹配以及角色是否处于转生状态
		if (toUse == null || toUse.getQuantity() < 1 || toUse.getItemId() != slea.readInt()
				|| chr.getBuffedEffect(SecondaryStat.Reincarnation) != null) {
			return;
		}

		// 获取当前时间
		final long time = System.currentTimeMillis();
		// 检查是否处于药水冷却时间内
		if (chr.getNextConsume() > time) {
			return;
		}

		// 检查地图是否允许使用药水，并且药水效果是否能应用到角色上
		if (!FieldLimitType.PotionUse.check(chr.getMap().getFieldLimit())
				&& MapleItemInformationProvider.getInstance().getItemEffect(toUse.getItemId()).applyTo(chr, true)) {
			// 如果药水 ID 为 2000054，则不进行后续操作
			if (toUse.getItemId() == 2000054) {
				return;
			}
			// 从使用物品栏中移除一个指定的药水
			MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
			// 如果地图有药水冷却时间设置
			if (chr.getMap().getConsumeItemCoolTime() > 0) {
				// 设置角色的下一次可使用药水时间
				chr.setNextConsume(time + chr.getMap().getConsumeItemCoolTime() * 1000);
			}
		}
	}

	/**
	 * 宠物聊天的方法
	 * 
	 * @param petid   宠物的 ID
	 * @param command 宠物的命令
	 * @param text    宠物发送的文本
	 * @param chr     角色对象
	 */
	public static final void PetChat(final int petid, final short command, final String text,
			final MapleCharacter chr) {
		// 检查角色、地图和宠物是否存在
		if (chr == null || chr.getMap() == null || chr.getPet(petid) == null) {
			return;
		}
		// 向地图中的其他玩家广播宠物聊天的消息
		chr.getMap().broadcastMessage(chr, PetPacket.petChat(chr.getId(), command, text, (byte) petid), true);
	}

	/**
	 * 宠物执行命令的方法
	 * 
	 * @param pet        宠物对象
	 * @param petCommand 宠物命令对象
	 * @param c          客户端对象
	 * @param chr        角色对象
	 */
	public static final void PetCommand(final MaplePet pet, final PetCommand petCommand, final MapleClient c,
			final MapleCharacter chr) {
		// 检查宠物命令是否为空
		if (petCommand == null) {
			return;
		}
		// 获取宠物在角色宠物列表中的索引
		final byte petIndex = (byte) chr.getPetIndex(pet);
		boolean success = false;
		// 根据宠物命令的概率判断是否执行成功
		if (Randomizer.nextInt(99) <= petCommand.getProbability()) {
			success = true;
			// 检查宠物的亲密度是否小于 30000
			if (pet.getCloseness() < 30000) {
				// 计算新的亲密度
				int newCloseness = pet.getCloseness() + petCommand.getIncrease() * c.getChannelServer().getTraitRate();
				if (newCloseness > 30000) {
					newCloseness = 30000;
				}
				// 设置宠物的新亲密度
				pet.setCloseness(newCloseness);
				// 检查新亲密度是否达到升级所需的亲密度
				if (newCloseness >= GameConstants.getClosenessNeededForLevel(pet.getLevel() + 1)) {
					// 宠物升级
					pet.setLevel(pet.getLevel() + 1);
					// 向客户端发送宠物升级效果的消息
					c.getSession().writeAndFlush(
							(Object) CField.EffectPacket.showPetLevelUpEffect(c.getPlayer(), pet.getPetItemId(), true));
					// 向地图中的其他玩家广播宠物升级效果的消息
					chr.getMap().broadcastMessage(
							CField.EffectPacket.showPetLevelUpEffect(c.getPlayer(), pet.getPetItemId(), false));
				}
				// 向客户端发送更新宠物信息的消息
				c.getSession()
						.writeAndFlush((Object) PetPacket.updatePet(chr, pet,
								chr.getInventory(MapleInventoryType.CASH).getItem(pet.getInventoryPosition()), false,
								chr.getPetLoot()));
			}
		}
		// 向地图中的其他玩家广播宠物命令执行结果的消息
		chr.getMap().broadcastMessage(
				PetPacket.commandResponse(chr.getId(), (byte) petCommand.getSkillId(), petIndex, success));
	}

	/**
	 * 宠物喂食的方法
	 * 
	 * @param slea 用于读取数据包的 LittleEndianAccessor 对象
	 * @param c    客户端对象
	 * @param chr  角色对象
	 */
	public static void PetFood(final LittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
		// 初始化宠物的初始饱食度为 100
		int previousFullness = 100;
		// 向客户端发送可操作的消息
		c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
		// 遍历角色的所有宠物
		for (final MaplePet pet : chr.getPets()) {
			if (pet != null) {
				// 找到饱食度最低的宠物
				if (pet.getFullness() < previousFullness) {
					previousFullness = pet.getFullness();
					// 跳过 6 个字节的数据
					slea.skip(6);
					// 读取喂食物品的 ID
					final int itemId = slea.readInt();
					boolean gainCloseness = false;
					// 以 50% 的概率判断是否增加亲密度
					if (Randomizer.nextInt(99) <= 50) {
						gainCloseness = true;
					}
					// 检查宠物的饱食度是否小于 100
					if (pet.getFullness() < 100) {
						// 计算新的饱食度
						int newFullness = pet.getFullness() + 30;
						if (newFullness > 100) {
							newFullness = 100;
						}
						// 设置宠物的新饱食度
						pet.setFullness(newFullness);
						// 获取宠物在角色宠物列表中的索引
						final int index = chr.getPetIndex(pet);
						// 检查是否增加亲密度且宠物亲密度小于 30000
						if (gainCloseness && pet.getCloseness() < 30000) {
							// 计算新的亲密度
							int newCloseness = pet.getCloseness() + 1;
							if (newCloseness > 30000) {
								newCloseness = 30000;
							}
							// 设置宠物的新亲密度
							pet.setCloseness(newCloseness);
							// 检查新亲密度是否达到升级所需的亲密度
							if (newCloseness >= GameConstants.getClosenessNeededForLevel(pet.getLevel() + 1)) {
								// 宠物升级
								pet.setLevel(pet.getLevel() + 1);
								// 向客户端发送宠物升级效果的消息
								c.getSession().writeAndFlush((Object) CField.EffectPacket
										.showPetLevelUpEffect(c.getPlayer(), pet.getPetItemId(), true));
								// 向地图中的其他玩家广播宠物升级效果的消息
								chr.getMap().broadcastMessage(CField.EffectPacket.showPetLevelUpEffect(c.getPlayer(),
										pet.getPetItemId(), false));
							}
						}
						// 向客户端发送更新宠物信息的消息
						c.getSession()
								.writeAndFlush((Object) PetPacket.updatePet(chr, pet,
										chr.getInventory(MapleInventoryType.CASH).getItem(pet.getInventoryPosition()),
										false, chr.getPetLoot()));
						// 向地图中的其他玩家广播宠物喂食成功的消息
						chr.getMap().broadcastMessage(c.getPlayer(),
								PetPacket.commandResponse(chr.getId(), (byte) 1, (byte) index, true), true);
					} else {
						// 如果宠物饱食度已满，且需要减少亲密度
						if (gainCloseness) {
							// 计算新的亲密度
							int newCloseness2 = pet.getCloseness() - 1;
							if (newCloseness2 < 0) {
								newCloseness2 = 0;
							}
							// 设置宠物的新亲密度
							pet.setCloseness(newCloseness2);
							// 检查新亲密度是否低于当前等级所需的亲密度
							if (newCloseness2 < GameConstants.getClosenessNeededForLevel(pet.getLevel())) {
								// 宠物降级
								pet.setLevel(pet.getLevel() - 1);
							}
						}
						// 向客户端发送更新宠物信息的消息
						c.getSession()
								.writeAndFlush((Object) PetPacket.updatePet(chr, pet,
										chr.getInventory(MapleInventoryType.CASH).getItem(pet.getInventoryPosition()),
										false, chr.getPetLoot()));
						// 向地图中的其他玩家广播宠物喂食失败的消息
						chr.getMap().broadcastMessage(chr,
								PetPacket.commandResponse(chr.getId(), (byte) 1, (byte) chr.getPetIndex(pet), false),
								true);
					}
					// 从使用物品栏中移除一个指定的喂食物品
					MapleInventoryManipulator.removeById(c, MapleInventoryType.USE, itemId, 1, true, false);
					return;
				}
			}
		}
	}

	/**
	 * 宠物移动的方法
	 * 
	 * @param slea 用于读取数据包的 LittleEndianAccessor 对象
	 * @param chr  角色对象
	 */
	public static final void MovePet(final LittleEndianAccessor slea, final MapleCharacter chr) {
		// 读取宠物的 ID
		final int petId = slea.readInt();
		// 跳过 13 个字节的数据
		slea.skip(13);
		// 解析宠物的移动数据
		final List<LifeMovementFragment> res = MovementParse.parseMovement(slea, 3);
		// 检查移动数据、角色、移动数据列表和地图是否存在
		if (res != null && chr != null && res.size() != 0 && chr.getMap() != null) {
			// 根据宠物 ID 获取宠物对象
			final MaplePet pet = chr.getPet(petId);
			if (pet == null) {
				return;
			}
			// 更新宠物的位置
			pet.updatePosition(res);
			// 向地图中的其他玩家广播宠物移动的消息
			chr.getMap().broadcastMessage(chr,
					PetPacket.movePet(chr.getId(), pet.getUniqueId(), (byte) petId, res, pet.getPos()), false);
			// 检查角色的拾取范围是否为 0 或者是否处于 PVP 状态
			if (chr.getStat().pickupRange <= 0.0 || chr.inPVP()) {
				return;
			}
			// 设置角色的滚动位置为 0
			chr.setScrolledPosition((short) 0);
		}
	}

	/**
	 * 该方法用于处理更改宠物增益技能的逻辑
	 * 
	 * @param slea 用于读取数据包的小端字节访问器
	 * @param chr  玩家角色对象
	 */
	/**
	 * 该方法用于处理更改宠物增益技能的逻辑
	 * 
	 * @param slea 用于读取数据包的小端字节访问器
	 * @param chr  玩家角色对象
	 */

	public static void ChangePetBuff(final LittleEndianAccessor slea, final MapleCharacter chr) {
		// 新增：检查玩家当前血量是否低于50%
		if (chr.getStat().getHp() < chr.getStat().getMaxHp() * 0.5) {
			// 向玩家发送消息，提示血量不足
			chr.dropMessage(1, "血量低于50%，无法使用宠物增益BUFF。");
			// 向客户端发送允许玩家操作的消息
			chr.getClient().getSession().writeAndFlush((Object) CWvsContext.enableActions(chr));
			return;
		}

		// 从数据包中读取宠物类型
		final int type = slea.readInt();
		// 从数据包中读取技能数量
		int skillsize = slea.readInt();
		// 从数据包中读取技能ID
		final int skillId = slea.readInt();
		// 从数据包中读取操作模式
		final int mode = slea.readByte();
		// 根据宠物类型获取玩家的宠物对象
		final MaplePet pet = chr.getPet(type);

		// 检查宠物是否存在，如果不存在则提示玩家并结束方法
		if (pet == null) {
			// 向玩家发送消息，提示宠物不存在
			chr.dropMessage(1, "寵物不存在.");
			// 向客户端发送允许玩家操作的消息
			chr.getClient().getSession().writeAndFlush((Object) CWvsContext.enableActions(chr));
			return;
		}

		// 检查玩家是否已经拥有该技能的增益效果，如果有则结束方法
		if (chr.getBuffedValue(skillId)) {
			// 向客户端发送允许玩家操作的消息
			chr.getClient().getSession().writeAndFlush((Object) CWvsContext.enableActions(chr));
			return;
		}

		// 检查玩家的技能ID存储值是否为初始值 -1L，如果是则将其设置为默认值 "0"
		if (chr.getKeyValue(9999, "skillid") == -1L) {
			chr.setKeyValue(9999, "skillid", "0");
		}
		if (chr.getKeyValue(9999, "skillid2") == -1L) {
			chr.setKeyValue(9999, "skillid2", "0");
		}
		if (chr.getKeyValue(9999, "skillid3") == -1L) {
			chr.setKeyValue(9999, "skillid3", "0");
		}
		if (chr.getKeyValue(9999, "skillid4") == -1L) {
			chr.setKeyValue(9999, "skillid4", "0");
		}
		if (chr.getKeyValue(9999, "skillid5") == -1L) {
			chr.setKeyValue(9999, "skillid5", "0");
		}
		if (chr.getKeyValue(9999, "skillid6") == -1L) {
			chr.setKeyValue(9999, "skillid6", "0");
		}

		// 根据不同的条件设置宠物的增益技能ID和玩家的技能ID存储值
		if (type == 0 && mode == 0 && skillsize == 0) {
			// 设置宠物的第一个增益技能ID
			pet.setBuffSkillId(skillId);
			// 更新玩家的技能ID存储值
			chr.setKeyValue(9999, "skillid", "" + skillId);
		} else if (type == 0 && skillsize == 1) {
			// 设置宠物的第二个增益技能ID
			pet.setBuffSkillId2(skillId);
			// 更新玩家的技能ID存储值
			chr.setKeyValue(9999, "skillid2", "" + skillId);
		} else if (type == 1 && skillsize == 0) {
			// 设置宠物的第一个增益技能ID
			pet.setBuffSkillId(skillId);
			// 更新玩家的技能ID存储值
			chr.setKeyValue(9999, "skillid3", "" + skillId);
		} else if (type == 1 && skillsize == 1) {
			// 设置宠物的第二个增益技能ID
			pet.setBuffSkillId2(skillId);
			// 更新玩家的技能ID存储值
			chr.setKeyValue(9999, "skillid4", "" + skillId);
		} else if (type == 2 && skillsize == 0) {
			// 设置宠物的第一个增益技能ID
			pet.setBuffSkillId(skillId);
			// 更新玩家的技能ID存储值
			chr.setKeyValue(9999, "skillid5", "" + skillId);
		} else if (type == 2 && skillsize == 1) {
			// 设置宠物的第二个增益技能ID
			pet.setBuffSkillId2(skillId);
			// 更新玩家的技能ID存储值
			chr.setKeyValue(9999, "skillid6", "" + skillId);
		}

		// 向客户端发送显示宠物技能的消息
		chr.getClient().getSession().writeAndFlush(CWvsContext.InfoPacket.showPetSkills(0,
				"0=" + chr.getKeyValue(9999, "skillid") + ";1=" + chr.getKeyValue(9999, "skillid2")));
		chr.getClient().getSession().writeAndFlush(CWvsContext.InfoPacket.showPetSkills(1,
				"10=" + chr.getKeyValue(9999, "skillid3") + ";11=" + chr.getKeyValue(9999, "skillid4")));
		chr.getClient().getSession().writeAndFlush(CWvsContext.InfoPacket.showPetSkills(2,
				"20=" + chr.getKeyValue(9999, "skillid5") + ";21=" + chr.getKeyValue(9999, "skillid6")));

		// 向客户端发送更新宠物信息的消息
		chr.getClient().getSession()
				.writeAndFlush((Object) PetPacket.updatePet(chr, pet,
						chr.getInventory(MapleInventoryType.CASH).getItem(pet.getInventoryPosition()), false,
						chr.getPetLoot()));
	}

	/**
	 * 设置宠物例外列表的方法
	 * 
	 * @param slea 用于读取数据包的 LittleEndianAccessor 对象
	 * @param c    客户端对象
	 * @param chr  角色对象
	 */
	public static void petExceptionList(final LittleEndianAccessor slea, final MapleClient c,
			final MapleCharacter chr) {
		// 读取宠物的索引
		final int petindex = slea.readInt();
		// 读取例外列表的大小
		final byte size = slea.readByte();
		// 根据宠物索引获取宠物对象
		final MaplePet pet = chr.getPet(petindex);
		if (pet == null) {
			// 如果宠物不存在，向角色发送提示消息
			chr.dropMessage(1, "寵物不存在.");
			// 向客户端发送可操作的消息
			c.getSession().writeAndFlush((Object) CWvsContext.enableActions(chr));
			return;
		}
		// 清空宠物的例外列表
		pet.setExceptionList("");
		String list = "";
		int i = 0;
		// 读取例外列表中的物品 ID
		while (i < size) {
			list = list + slea.readInt() + "";
			++i;
			if (size > 1 && size != i) {
				list += ",";
			}
		}
		// 设置宠物的例外列表
		pet.setExceptionList(list);
		// 向客户端发送更新宠物例外列表的消息
		c.getSession().writeAndFlush((Object) PetPacket.petExceptionList(chr, pet));
	}

	/**
	 * 更新宠物技能的方法
	 * 
	 * @param player  角色对象
	 * @param unequip 卸下的宠物对象，如果为 null 表示更新所有宠物的技能
	 */
	public static void updatePetSkills(final MapleCharacter player, final MaplePet unequip) {
		// 获取物品信息提供者实例
		final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
		// 用于存储新的技能列表
		final Map<Skill, SkillEntry> newL = new HashMap<Skill, SkillEntry>();
		// 用于存储宠物物品的 ID 列表
		final List<Integer> petItemIds = new ArrayList<Integer>();
		// 遍历角色的所有宠物
		for (int i = 0; i < 3; ++i) {
			if (player.getPet(i) != null) {
				petItemIds.add(player.getPet(i).getPetItemId());
			}
		}
		if (unequip != null) {
			int level = 0;
			// 获取卸下宠物的套装信息
			final StructSetItem setItem = ii.getSetItem(ii.getSetItemID(unequip.getPetItemId()));
			// 计算套装等级
			for (final int petId : petItemIds) {
				if (ii.getSetItemID(petId) == ii.getSetItemID(unequip.getPetItemId())) {
					++level;
				}
			}
			if (setItem != null) {
				// 遍历套装的所有物品
				for (final Map.Entry<Integer, StructSetItem.SetItem> set : setItem.items.entrySet()) {
					if (set.getKey() <= level) {
						// 如果套装等级满足条件，添加技能到新的技能列表中
						for (final Map.Entry<Integer, Byte> skill : set.getValue().activeSkills.entrySet()) {
							newL.put(SkillFactory.getSkill(skill.getKey()), new SkillEntry(skill.getValue(),
									(byte) SkillFactory.getSkill(skill.getKey()).getMasterLevel(), -1L));
							// 根据技能 ID 更改角色的技能等级
							switch (skill.getKey()) {
							case 80000589:
							case 80001535:
							case 80001536:
							case 80001537:
							case 80001538:
							case 80001539: {
								player.changeSkillLevel(skill.getKey(), (byte) 1, (byte) 1);
								continue;
							}
							}
						}
					} else {
						// 如果套装等级不满足条件，移除技能
						for (final Map.Entry<Integer, Byte> skill : set.getValue().activeSkills.entrySet()) {
							newL.put(SkillFactory.getSkill(skill.getKey()), new SkillEntry(-1, (byte) 0, -1L));
							// 根据技能 ID 更改角色的技能等级
							switch (skill.getKey()) {
							case 80000589:
							case 80001535:
							case 80001536:
							case 80001537:
							case 80001538:
							case 80001539: {
								player.changeSkillLevel(skill.getKey(), (byte) (-1), (byte) 0);
								continue;
							}
							}
						}
					}
				}
			}
		} else {
			// 如果没有卸下的宠物，遍历所有宠物的物品 ID
			for (final int petId2 : petItemIds) {
				int level2 = 0;
				// 获取宠物物品的套装信息
				final StructSetItem setItem2 = ii.getSetItem(ii.getSetItemID(petId2));
				if (setItem2 != null) {
					// 计算套装等级
					for (final int setItemId : setItem2.itemIDs) {
						if (petItemIds.contains(setItemId)) {
							++level2;
						}
					}
					// 遍历套装的所有物品
					for (final Map.Entry<Integer, StructSetItem.SetItem> set2 : setItem2.items.entrySet()) {
						if (set2.getKey() <= level2) {
							// 如果套装等级满足条件，添加技能到新的技能列表中
							for (final Map.Entry<Integer, Byte> skill2 : set2.getValue().activeSkills.entrySet()) {
								newL.put(SkillFactory.getSkill(skill2.getKey()), new SkillEntry(skill2.getValue(),
										(byte) SkillFactory.getSkill(skill2.getKey()).getMasterLevel(), -1L));
								// 根据技能 ID 更改角色的技能等级
								switch (skill2.getKey()) {
								case 80000589:
								case 80001535:
								case 80001536:
								case 80001537:
								case 80001538:
								case 80001539: {
									player.changeSkillLevel(skill2.getKey(), (byte) 1, (byte) 1);
									continue;
								}
								}
							}
						} else {
							// 如果套装等级不满足条件，移除技能
							for (final Map.Entry<Integer, Byte> skill2 : set2.getValue().activeSkills.entrySet()) {
								newL.put(SkillFactory.getSkill(skill2.getKey()), new SkillEntry(-1, (byte) 0, -1L));
								// 根据技能 ID 更改角色的技能等级
								switch (skill2.getKey()) {
								case 80000589:
								case 80001535:
								case 80001536:
								case 80001537:
								case 80001538:
								case 80001539: {
									player.changeSkillLevel(skill2.getKey(), (byte) (-1), (byte) 0);
									continue;
								}
								}
							}
						}
					}
				}
			}
		}
		// 如果新的技能列表不为空，向客户端发送更新技能的消息
		if (!newL.isEmpty()) {
			player.getClient().getSession().writeAndFlush((Object) CWvsContext.updateSkills(newL));
		}
	}
}