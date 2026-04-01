package handling.channel.handler;

import client.*;
import client.inventory.*;
import constants.CubeOption;
import constants.EdiCubeOption;
import constants.GameConstants;
import constants.ServerConstants;
import database.DatabaseConnection;
import handling.RecvPacketOpcode;
import handling.world.MapleParty;
import handling.world.MaplePartyCharacter;
import handling.world.World;
import log.DBLogger;
import log.LogType;
import provider.MapleData;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;
import scripting.EventManager;
import scripting.NPCScriptManager;
import server.Timer;
import server.*;
import server.enchant.EquipmentEnchant;
import server.life.MapleLifeFactory;
import server.life.MapleMonster;
import server.life.MobSkill;
import server.life.MobSkillFactory;
import server.maps.*;
import server.quest.MapleQuest;
import server.shops.MapleShopFactory;
import tools.FileoutputUtil;
import tools.Pair;
import tools.Triple;
import tools.data.LittleEndianAccessor;
import tools.packet.*;

import java.awt.*;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.*;

public class InventoryHandler {

	public static final void ItemMove(final LittleEndianAccessor slea, final MapleClient c) {// 物品移動處理邏輯？？
		try {
			c.getPlayer().setScrolledPosition((short) 0);
			slea.readInt();
			final MapleInventoryType type = MapleInventoryType.getByType(slea.readByte());
			final short src = slea.readShort();
			final short dst = slea.readShort();
			final short quantity = slea.readShort();

			if (src < 0 && dst > 0) {
				MapleInventoryManipulator.unequip(c, src, dst, type);
			} else if (dst < 0) {
				MapleInventoryManipulator.equip(c, src, dst, type);
			} else if (dst == 0) {
				MapleInventoryManipulator.drop(c, type, src, quantity);
			} else {
				MapleInventoryManipulator.move(c, type, src, dst);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void SwitchBag(final LittleEndianAccessor slea, final MapleClient c) {
		c.getPlayer().setScrolledPosition((short) 0);
		slea.readInt();
		final short src = (short) slea.readInt();
		final short dst = (short) slea.readInt();
		if (src < 100 || dst < 100) {
			return;
		}
		MapleInventoryManipulator.move(c, MapleInventoryType.ETC, src, dst);
	}

	public static final void MoveBag(final LittleEndianAccessor slea, final MapleClient c) {
		c.getPlayer().setScrolledPosition((short) 0);
		slea.readInt();
		final boolean srcFirst = slea.readInt() > 0;
		if (slea.readByte() != 4) {
			c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
			return;
		}
		final short dst = (short) slea.readInt();
		final short src = slea.readShort();
		MapleInventoryManipulator.move(c, MapleInventoryType.ETC, srcFirst ? dst : src, srcFirst ? src : dst);
	}

	public static final void ItemSort(final LittleEndianAccessor slea, final MapleClient c) {
		slea.readInt();
		c.getPlayer().setScrolledPosition((short) 0);
		final MapleInventoryType pInvType = MapleInventoryType.getByType(slea.readByte());
		if (pInvType == MapleInventoryType.UNDEFINED) {
			c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
			return;
		}
		final MapleInventory pInv = c.getPlayer().getInventory(pInvType);
		final List<Item> itemMap = new LinkedList<Item>();
		for (final Item item : pInv.list()) {
			itemMap.add(item.copy());
		}
		final List<Pair<Short, Short>> updateSlots = new ArrayList<Pair<Short, Short>>();
		for (int i = 1; i <= pInv.getSlotLimit(); ++i) {
			final Item item2 = pInv.getItem((short) i);
			if (item2 == null) {
				final Item nextItem = pInv.getItem(pInv.getNextItemSlot((short) i));
				if (nextItem != null) {
					final short oldPos = nextItem.getPosition();
					pInv.removeItem(nextItem.getPosition());
					final short nextPos = pInv.addItem(nextItem);
					updateSlots.add(new Pair<Short, Short>(oldPos, nextPos));
					c.getSession().writeAndFlush(
							(Object) CWvsContext.InventoryPacket.moveInventoryItem(pInvType, updateSlots));
					c.getSession().writeAndFlush(
							CWvsContext.InventoryPacket.updateItemSort(pInvType, pInv.getNextFreeSlot()));
				}
			}
		}
		c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.moveInventoryItem(pInvType, updateSlots));
		// c.getSession().writeAndFlush(CWvsContext.InventoryPacket.updateItemSort(pInvType,
		// pInv.getNextFreeSlot()));
		System.out.println("pInvType : " + pInvType + " | d : " + pInv.getNextFreeSlot());
		c.getSession().writeAndFlush((Object) CWvsContext.finishedSort(pInvType.getType()));
	}

	public static final void ItemGather(final LittleEndianAccessor slea, final MapleClient c) {
		slea.readInt();
		c.getPlayer().setScrolledPosition((short) 0);
		final byte mode = slea.readByte();
		final MapleInventoryType invType = MapleInventoryType.getByType(mode);
		final MapleInventory inv = c.getPlayer().getInventory(invType);
		if (mode > MapleInventoryType.UNDEFINED.getType()) {
			final List<Item> itemMap = new LinkedList<Item>();
			for (final Item item : inv.list()) {
				itemMap.add(item.copy());
			}
			for (final Item itemStats : itemMap) {
				MapleInventoryManipulator.removeFromSlot(c, invType, itemStats.getPosition(), itemStats.getQuantity(),
						true, false);
			}
			final List<Item> sortedItems = sortItems(itemMap);
			for (final Item item2 : sortedItems) {
				MapleInventoryManipulator.addFromDrop(c, item2, false, false, false, true);
			}
		}
		c.getSession().writeAndFlush((Object) CWvsContext.finishedGather(mode));
	}

	private static final List<Item> sortItems(final List<Item> passedMap) {
		final List<Integer> itemIds = new ArrayList<Integer>();
		for (final Item item : passedMap) {
			itemIds.add(item.getItemId());
		}
		Collections.sort(itemIds);
		final List<Item> sortedList = new LinkedList<Item>();
		for (final Integer val : itemIds) {
			for (final Item item2 : passedMap) {
				if (val == item2.getItemId()) {
					sortedList.add(item2);
					passedMap.remove(item2);
					break;
				}
			}
		}
		return sortedList;
	}

	public static final boolean UseRewardItem(final short slot, final int itemId, final MapleClient c,
			final MapleCharacter chr) {
		final Item toUse = c.getPlayer().getInventory(GameConstants.getInventoryType(itemId)).getItem(slot);
		c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
		if (toUse != null && toUse.getQuantity() >= 1 && toUse.getItemId() == itemId) {
			if (chr.getInventory(MapleInventoryType.EQUIP).getNextFreeSlot() > -1
					&& chr.getInventory(MapleInventoryType.USE).getNextFreeSlot() > -1
					&& chr.getInventory(MapleInventoryType.SETUP).getNextFreeSlot() > -1
					&& chr.getInventory(MapleInventoryType.ETC).getNextFreeSlot() > -1) {
				final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
				final Pair<Integer, List<StructRewardItem>> rewards = ii.getRewardItem(itemId);
				if (rewards != null && rewards.getLeft() > 0) {
					StructRewardItem reward = null;
					Block_11: while (true) {
						final Iterator<StructRewardItem> iterator = rewards.getRight().iterator();
						while (iterator.hasNext()) {
							reward = iterator.next();
							if (reward.prob > 0 && Randomizer.nextInt(rewards.getLeft()) < reward.prob) {
								break Block_11;
							}
						}
					}
					if (GameConstants.getInventoryType(reward.itemid) == MapleInventoryType.EQUIP) {
						final Item item = ii.getEquipById(reward.itemid);
						if (reward.period > 0L) {
							item.setExpiration(System.currentTimeMillis() + reward.period * 60L * 60L * 10L);
						}
						item.setGMLog("Reward item: " + itemId + " on " + FileoutputUtil.CurrentReadable_Date());
						MapleInventoryManipulator.addbyItem(c, item);
					} else {
						MapleInventoryManipulator.addById(c, reward.itemid, reward.quantity,
								"Reward item: " + itemId + " on " + FileoutputUtil.CurrentReadable_Date());
					}
					MapleInventoryManipulator.removeById(c, GameConstants.getInventoryType(itemId), itemId, 1, false,
							false);
					c.getSession().writeAndFlush(
							(Object) CField.EffectPacket.showRewardItemEffect(chr, reward.itemid, true, reward.effect));
					chr.getMap().broadcastMessage(chr,
							CField.EffectPacket.showRewardItemEffect(chr, reward.itemid, false, reward.effect), false);
					return true;
				}
				if (itemId == 2028154) {
					final int reward2 = 1113097 + Randomizer.rand(1, 31);
					MapleInventoryManipulator.removeFromSlot(c,
							MapleInventoryType.getByType((byte) (toUse.getItemId() / 1000000)), slot, (short) 1, false);
					final Equip item2 = (Equip) ii.getEquipById(reward2);
					MapleInventoryManipulator.addbyItem(c, item2);
					c.getSession().writeAndFlush((Object) CField.EffectPacket.showEffect(c.getPlayer(), reward2, 0, 38,
							1, 0, (byte) 0, true, null, "", null));
					c.getSession().writeAndFlush(
							(Object) CField.EffectPacket.showRewardItemEffect(c.getPlayer(), itemId, true, ""));
					if (item2.getBaseLevel() >= 4 && (reward2 == 1113098 || reward2 == 1113099
							|| (reward2 >= 1113113 && reward2 <= 1113116) || reward2 == 1113122)) {
						World.Broadcast
								.broadcastMessage(CWvsContext.serverMessage(11, c.getChannel(), c.getPlayer().getName(),
										c.getPlayer().getName() + "님이 상자에서 [" + ii.getName(reward2) + "] 아이템을 획득했습니다.",
										true, item2));
					}
					c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
				} else if (itemId == 2028272) {
					final int reward2 = RandomRewards.getTheSeedReward();
					if (reward2 == 0) {
						c.getSession().writeAndFlush((Object) CField.NPCPacket.getNPCTalk(9000155, (byte) 0,
								"아쉽지만, 꽝이 나왔습니다. 다음 기회에 다시 이용해주세요!", "00 00", (byte) 0));
					} else if (reward2 == 1) {
						chr.gainMeso(10000000L, true);
						c.getSession().writeAndFlush(
								CField.NPCPacket.getNPCTalk(9000155, (byte) 0, "1천만메소를 획득하셨습니다!", "00 00", (byte) 0));
					} else {
						int max_quantity = 1;
						switch (reward2) {
						case 4310034: {
							max_quantity = 10;
							break;
						}
						case 4310014: {
							max_quantity = 10;
							break;
						}
						case 4310016: {
							max_quantity = 10;
							break;
						}
						case 4001208:
						case 4001209:
						case 4001210:
						case 4001211:
						case 4001547:
						case 4001548:
						case 4001549:
						case 4001550:
						case 4001551: {
							max_quantity = 1;
							break;
						}
						}
						c.getSession()
								.writeAndFlush((Object) CField.NPCPacket.getNPCTalk(9000155, (byte) 0,
										"축하드립니다!!\r\n돌림판에서 [#b#i" + reward2 + "##z" + reward2 + "#](이)가 나왔습니다.",
										"00 00", (byte) 0));
						c.getPlayer().gainItem(reward2, max_quantity);
						c.getSession().writeAndFlush(
								(Object) CField.EffectPacket.showCharmEffect(c.getPlayer(), reward2, 1, true, ""));
						c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
					}
				} else if (itemId == 2028208 || itemId == 2028209) {
					NPCScriptManager.getInstance().startItem(c, 9000162, "consume_" + itemId);
				} else {
					chr.dropMessage(6, "아이템 보상 정보를 찾을 수 없습니다.");
				}
			} else {
				chr.dropMessage(6, "Insufficient inventory slot.");
			}
		}
		return false;
	}

	public static final void UseItem(final LittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
		if (chr == null || !chr.isAlive() || chr.getBuffedEffect(SecondaryStat.DebuffIncHp) != null
				|| chr.getMap() == null || chr.hasDisease(SecondaryStat.StopPortion)
				|| chr.getBuffedValue(SecondaryStat.StopPortion) != null) {
			c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
			return;
		}
		try {
			final long time = System.currentTimeMillis();
			slea.skip(4);
			final short slot = slea.readShort();
			final int itemId = slea.readInt();
			final Item toUse = chr.getInventory(MapleInventoryType.USE).getItem(slot);
			if (toUse.getItemId() != itemId) {
				c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
				return;
			}

			// 輸出物品 ID
			// System.out.println("玩家 " + chr.getName() + " 雙擊使用了物品 ID: " + itemId);

			if (!FieldLimitType.PotionUse.check(chr.getMap().getFieldLimit())) {
				if (MapleItemInformationProvider.getInstance().getItemEffect(toUse.getItemId()).applyTo(chr, true)) {
					if (toUse.getItemId() != 2000054) {
						MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
					}
					c.getSession().writeAndFlush((Object) CField.potionCooldown());
				}
			} else {
				c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void useCoreSelectValue(final LittleEndianAccessor slea, final MapleClient c) {
		if (c.getPlayer() == null || !c.getPlayer().isAlive() || c.getPlayer().getMap() == null) {
			c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
			return;
		}

		try {
			final int kind = slea.readInt();
			final int quantity = slea.readInt();
			int itemId = -1;

			switch (kind) {
			case 0:
				itemId = 2435719;
				break;
			case 1:
				itemId = 2438411;
				break;
			case 2:
				itemId = 2439279;
				break;
			case 3:
				itemId = 2632972;
				break;
			}

			if (itemId == -1) {
				c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
				return;
			}

			if (!c.getPlayer().haveItem(itemId, quantity)) {
				c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
				return;
			}

			switch (itemId) {
			case 2435719: // 코어 젬스톤
				if (GameConstants.isPinkBean(c.getPlayer().getJob()) || GameConstants.isYeti(c.getPlayer().getJob())) {
					c.getPlayer().dropMessage(1, "粉紅兔與雪怪是不可能的行為.");
					break;
				}

				MatrixHandler.UseCoreJamStone(c, itemId, quantity, kind);
				break;
			case 2438411: // 거울세계의 코어 젬스톤
				if (GameConstants.isPinkBean(c.getPlayer().getJob()) || GameConstants.isYeti(c.getPlayer().getJob())) {
					c.getPlayer().dropMessage(1, "粉紅兔與雪怪是不可能的行為.");
					break;
				}

				MatrixHandler.UseMirrorCoreJamStone(c, itemId, quantity, kind);
				break;
			case 2439279: // 경험의 코어 젬스톤
				if (GameConstants.isPinkBean(c.getPlayer().getJob()) || GameConstants.isYeti(c.getPlayer().getJob())) {
					c.getPlayer().dropMessage(1, "粉紅兔與雪怪是不可能的行為.");
					break;
				}

				MatrixHandler.UseEnforcedCoreJamStone(c, itemId, quantity, kind);
				break;
			case 2632972: // 미트라의 코어 젬스톤
				if (GameConstants.isPinkBean(c.getPlayer().getJob()) || GameConstants.isYeti(c.getPlayer().getJob())) {
					c.getPlayer().dropMessage(1, "粉紅兔與雪怪是不可能的行為.");
					break;
				}

				MatrixHandler.UseCraftCoreJamStone(c, itemId, quantity, kind);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void useItemSelectValue(final LittleEndianAccessor slea, final MapleClient c) {
		if (c.getPlayer() == null || !c.getPlayer().isAlive() || c.getPlayer().getMap() == null) {
			c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
			return;
		}

		try {
			final int itemId = slea.readInt();
			final int quantity = slea.readInt();

			if (!c.getPlayer().haveItem(itemId, quantity)) {
				c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
				return;
			}

			MapleInventoryManipulator.removeById(c, MapleInventoryType.USE, itemId, quantity, false, true);

			switch (itemId) {
			case 2434290: // 무공이 보증한 명예의 훈장
				c.getPlayer().gainHonor(10000 * quantity);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void UseReturnScroll(LittleEndianAccessor slea, MapleClient c, MapleCharacter chr) {
		if (!chr.isAlive() || chr.getMapId() == 749040100 || chr.inPVP()) {
			c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
			return;
		}
		slea.readInt();
		short slot = slea.readShort();
		int itemId = slea.readInt();
		Item toUse = chr.getInventory(MapleInventoryType.USE).getItem(slot);
		if (toUse == null || toUse.getQuantity() < 1 || toUse.getItemId() != itemId) {
			c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
			return;
		}
		FileoutputUtil.log(FileoutputUtil.物品使用日誌,
				"[주문서 사용] 계정 아이디 : " + c.getAccID() + " | " + c.getPlayer().getName() + "이 "
						+ MapleItemInformationProvider.getInstance().getName(toUse.getItemId()) + "를 "
						+ MapleItemInformationProvider.getInstance().getName(itemId) + "에 사용함.");
		if (!FieldLimitType.PotionUse.check(chr.getMap().getFieldLimit())) {
			if (MapleItemInformationProvider.getInstance().getItemEffect(toUse.getItemId()).applyReturnScroll(chr)) {
				MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
			} else {
				c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
			}
		} else {
			c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
		}
	}

	public static void UseMagnify(final LittleEndianAccessor slea, final MapleClient c) {
		try {
			slea.skip(4);
			boolean useGlass = false;
			boolean isEquipped = false;
			final short useSlot = slea.readShort();
			final short equSlot = slea.readShort();
			final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
			Equip equip;
			if (equSlot < 0) {
				equip = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(equSlot);
				isEquipped = true;
			} else {
				equip = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(equSlot);
			}
			final Item glass = c.getPlayer().getInventory(MapleInventoryType.USE).getItem(useSlot);
			if (useSlot != 20000) {
				if (glass == null || equip == null) {
					c.getPlayer().dropMessage(1, "GLASS NULL!");
					c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.getInventoryFull());
					return;
				}
				useGlass = true;
			} else {
				final long price = GameConstants.getMagnifyPrice(equip);
				c.getPlayer().gainMeso(-price, false);
			}
			if (equip.getState() == 1) {
				final int rank = Randomizer.nextInt(100);
				if (equip.getLines() == 0) {
					equip.setLines((byte) 2);
				}
				if (rank < 3) {
					equip.setState((byte) 18);
				} else if (rank < 1) {
					equip.setState((byte) 19);
				} else {
					equip.setState((byte) 17);
				}
			} else {
				equip.setState((byte) (equip.getState() + 16));
			}
			final int level = equip.getState() - 16;
			equip.setPotential1(CubeOption.getRedCubePotentialId(equip.getItemId(), level, 1, new int[0]));
			equip.setPotential2(CubeOption.getRedCubePotentialId(equip.getItemId(), level, 2, new int[0]));
			if (equip.getLines() == 3) {
				equip.setPotential3(CubeOption.getRedCubePotentialId(equip.getItemId(), level, 3, equip.getPotential1(),
						equip.getPotential2()));
			}
			if (GameConstants.isZero(c.getPlayer().getJob())) {
				if (equSlot == -10) {
					final Equip eq2 = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED)
							.getItem((short) (-11));
					if (eq2 != null) {
						eq2.setState(equip.getState());
						eq2.setLines(equip.getLines());
						eq2.setPotential1(equip.getPotential1());
						eq2.setPotential2(equip.getPotential2());
						eq2.setPotential3(equip.getPotential3());
						c.getPlayer().forceReAddItem(eq2, MapleInventoryType.EQUIPPED);
					}
				} else if (equSlot == -11) {
					final Equip eq2 = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED)
							.getItem((short) (-10));
					if (eq2 != null) {
						eq2.setState(equip.getState());
						eq2.setLines(equip.getLines());
						eq2.setPotential1(equip.getPotential1());
						eq2.setPotential2(equip.getPotential2());
						eq2.setPotential3(equip.getPotential3());
						c.getPlayer().forceReAddItem(eq2, MapleInventoryType.EQUIPPED);
					}
				}
			}
			if (useGlass) {
				final MapleInventory useInventory = c.getPlayer().getInventory(MapleInventoryType.USE);
				useInventory.removeItem(useSlot, (short) 1, false);
			}
			c.getSession().writeAndFlush(
					(Object) CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIP, equip));
			c.getPlayer().getTrait(MapleTrait.MapleTraitType.insight).addExp(10, c.getPlayer());
			c.getPlayer().getMap().broadcastMessage(CField.showMagnifyingEffect(c.getPlayer().getId(), equSlot));
			if (isEquipped) {
				c.getPlayer().forceReAddItem_NoUpdate(equip, MapleInventoryType.EQUIPPED);
			} else {
				c.getPlayer().forceReAddItem_NoUpdate(equip, MapleInventoryType.EQUIP);
			}
			c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static int potential(final int itemid, final int level) {
		return potential(itemid, level, false);
	}

	public static int potential(final int itemid, final int level, final boolean editional) {
		final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
		final int itemtype = itemid / 1000;
		return ii.getPotentialOptionID(Math.max(1, level), editional, itemtype);
	}

	public static void UseStamp(final LittleEndianAccessor slea, final MapleClient c) {
		slea.skip(4);
		final short slot = slea.readShort();
		final short dst = slea.readShort();
		boolean sucstamp = false;
		final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
		Equip toStamp;
		if (dst < 0) {
			toStamp = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(dst);
		} else {
			toStamp = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(dst);
		}
		final MapleInventory useInventory = c.getPlayer().getInventory(MapleInventoryType.USE);
		final Item stamp = useInventory.getItem(slot);
		if (GameConstants.isZero(c.getPlayer().getJob())) {
			final Equip toStamp2 = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED)
					.getItem((short) (-11));
			if (Randomizer.isSuccess(ii.getSuccess(toStamp2.getItemId(), c.getPlayer(), toStamp2))) {
				toStamp2.setLines((byte) 3);
				int temp;
				final int level = temp = toStamp2.getState() - 16;
				for (int a = 0; temp > 1; --temp, ++a) {
					if (temp > 1) {
					}
				}
				toStamp2.setPotential3(potential(toStamp2.getItemId(),
						(level == 1 || Randomizer.nextInt(100) < 1) ? level : (level - 1)));
				sucstamp = true;
			}
			c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.updateScrollandItem(stamp, toStamp2));
		}
		if (Randomizer.isSuccess(ii.getSuccess(toStamp.getItemId(), c.getPlayer(), toStamp))) {
			toStamp.setLines((byte) 3);
			int temp2;
			final int level2 = temp2 = toStamp.getState() - 16;
			for (int a2 = 0; temp2 > 1; --temp2, ++a2) {
				if (temp2 > 1) {
				}
			}
			toStamp.setPotential3(potential(toStamp.getItemId(),
					(level2 == 1 || Randomizer.nextInt(100) < 1) ? level2 : (level2 - 1)));
			sucstamp = true;
		}
		useInventory.removeItem(stamp.getPosition(), (short) 1, false);
		c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.updateScrollandItem(stamp, toStamp));
		c.getPlayer().getMap().broadcastMessage(
				CField.showPotentialReset(c.getPlayer().getId(), sucstamp, stamp.getItemId(), toStamp.getItemId()));
		c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
	}

	public static void UseEditionalStamp(final LittleEndianAccessor slea, final MapleClient c) {
		slea.skip(4);
		final short slot = slea.readShort();
		final short dst = slea.readShort();
		boolean sucstamp = false;
		final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
		Equip toStamp;
		if (dst < 0) {
			toStamp = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(dst);
		} else {
			toStamp = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(dst);
		}
		final MapleInventory useInventory = c.getPlayer().getInventory(MapleInventoryType.USE);
		final Item stamp = useInventory.getItem(slot);
		if (GameConstants.isZero(c.getPlayer().getJob())) {
			final Equip toStamp2 = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED)
					.getItem((short) (-11));
			final int level = toStamp2.getState() - 16;
			if (Randomizer.isSuccess(ii.getSuccess(toStamp2.getItemId(), c.getPlayer(), toStamp2))) {
				toStamp2.setPotential6(potential(toStamp2.getItemId(),
						(level == 1 || Randomizer.nextInt(100) < 1) ? level : (level - 1), true));
				sucstamp = true;
			}
			c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.updateScrollandItem(stamp, toStamp));
		}
		if (Randomizer.isSuccess(ii.getSuccess(toStamp.getItemId(), c.getPlayer(), toStamp))) {
			final int level2 = toStamp.getState() - 16;
			toStamp.setPotential6(potential(toStamp.getItemId(),
					(level2 == 1 || Randomizer.nextInt(100) < 1) ? level2 : (level2 - 1), true));
			sucstamp = true;
		}
		useInventory.removeItem(stamp.getPosition(), (short) 1, false);
		c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.updateScrollandItem(stamp, toStamp));
		c.getPlayer().getMap().broadcastMessage(
				CField.showPotentialReset(c.getPlayer().getId(), sucstamp, stamp.getItemId(), toStamp.getItemId()));
		c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
	}

	public static void UseChooseCube(final LittleEndianAccessor slea, final MapleClient c) {
		slea.skip(4);
		final byte type = slea.readByte();
		Equip equip = null;
		Equip zeroequip = null;
		if (c.getPlayer().choicepotential.getPosition() > 0) {
			equip = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP)
					.getItem(c.getPlayer().choicepotential.getPosition());
		} else {
			equip = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED)
					.getItem(c.getPlayer().choicepotential.getPosition());
		}
		final int cube = Integer.parseInt(c.getPlayer().getV("lastCube"));
		if (type == 6) {
			if (c.getPlayer().choicepotential.getPosition() > 0) {
				equip.set(c.getPlayer().choicepotential);
			} else {
				equip.set(c.getPlayer().choicepotential);
			}
		}
		if (GameConstants.isZeroWeapon(c.getPlayer().choicepotential.getItemId())) {
			zeroequip = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-11));
			zeroequip.setState(equip.getState());
			zeroequip.setLines(equip.getLines());
			zeroequip.setPotential1(equip.getPotential1());
			zeroequip.setPotential2(equip.getPotential2());
			zeroequip.setPotential3(equip.getPotential3());
			zeroequip.setPotential4(equip.getPotential4());
			zeroequip.setPotential5(equip.getPotential5());
			zeroequip.setPotential6(equip.getPotential6());
		}
		c.getPlayer().choicepotential = null;
		c.getPlayer().memorialcube = null;
		if (zeroequip != null) {
			c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.updateInventoryItem(false,
					MapleInventoryType.EQUIPPED, equip));
			c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.updateInventoryItem(false,
					MapleInventoryType.EQUIPPED, zeroequip));
			c.getPlayer().forceReAddItem(equip, MapleInventoryType.EQUIPPED);
		} else {
			c.getSession().writeAndFlush(
					(Object) CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIP, equip));
			c.getPlayer().forceReAddItem(equip, MapleInventoryType.EQUIP);
		}
	}

	public static final void addToScrollLog(final int accountID, final int charID, final int scrollID, final int itemID,
			final byte oldSlots, final byte newSlots, final byte viciousHammer, final String result, final boolean ws,
			final boolean ls, final int vega) {
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = DatabaseConnection.getConnection();
			ps = con.prepareStatement("INSERT INTO scroll_log VALUES(DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			ps.setInt(1, accountID);
			ps.setInt(2, charID);
			ps.setInt(3, scrollID);
			ps.setInt(4, itemID);
			ps.setByte(5, oldSlots);
			ps.setByte(6, newSlots);
			ps.setByte(7, viciousHammer);
			ps.setString(8, result);
			ps.setByte(9, (byte) (ws ? 1 : 0));
			ps.setByte(10, (byte) (ls ? 1 : 0));
			ps.setInt(11, vega);
			ps.execute();
			ps.close();
			con.close();
		} catch (SQLException e) {
			FileoutputUtil.outputFileError("Log_Packet_Except.rtf", e);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException se2) {
				se2.printStackTrace();
			}
		}
	}

	public static void useSilverKarma(final LittleEndianAccessor slea, final MapleCharacter chr) {
		slea.skip(4);
		final Item scroll = chr.getInventory(MapleInventoryType.USE).getItem(slea.readShort());
		final Item toScroll = chr.getInventory(MapleInventoryType.getByType((byte) slea.readShort()))
				.getItem(slea.readShort());
		if (scroll.getItemId() == 2720000 || scroll.getItemId() == 2720001) {
			if (!MapleItemInformationProvider.getInstance().isKarmaEnabled(toScroll.getItemId())) {
				chr.dropMessage(5,
						"\uac00\uc704\ub97c \uc0ac\uc6a9\ud560 \uc218 \uc5c6\ub294 \uc544\uc774\ud15c\uc785\ub2c8\ub2e4.");
				return;
			}
			if (toScroll.getType() == 1) {
				final Equip nEquip = (Equip) toScroll;
				if (nEquip.getKarmaCount() > 0) {
					nEquip.setKarmaCount((byte) (nEquip.getKarmaCount() - 1));
				} else if (nEquip.getKarmaCount() == 0) {
					chr.dropMessage(5,
							"\uac00\uc704\ub97c \uc0ac\uc6a9\ud560 \uc218 \uc5c6\ub294 \uc544\uc774\ud15c\uc785\ub2c8\ub2e4.");
					return;
				}
			}
			int flag = toScroll.getFlag();
			if (toScroll.getType() == 1) {
				flag += ItemFlag.KARMA_EQUIP.getValue();
			} else {
				flag += ItemFlag.KARMA_USE.getValue();
			}
			toScroll.setFlag(flag);
		}
		chr.removeItem(scroll.getItemId(), -1);
		chr.getClient().getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.updateInventoryItem(false,
				GameConstants.getInventoryType(toScroll.getItemId()), toScroll));
	}

	// ==============================================================================//
	public static boolean UseUpgradeScroll(final RecvPacketOpcode header, final short slot, final short dst,
			final byte ws, final MapleClient c, final MapleCharacter chr) {
		boolean whiteScroll = false;
		final boolean legendarySpirit = false;
		boolean recovery = false;
		final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
		Equip toScroll = null;
		Equip toScroll2 = null;

		Item scroll = chr.getInventory(MapleInventoryType.USE).getItem(slot);

		if (dst < 0) {
			toScroll = (Equip) chr.getInventory(MapleInventoryType.EQUIPPED).getItem(dst);
			if (GameConstants.isZero(chr.getJob())) {
				if (toScroll.getPosition() == -11) {
					toScroll2 = (Equip) chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-10));
				} else {
					toScroll2 = (Equip) chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-11));
				}
			}
		} else {
			toScroll = (Equip) chr.getInventory(MapleInventoryType.EQUIP).getItem(dst);
		}
		if (ii.getName(scroll != null ? scroll.getItemId() : 0).contains("寵物裝備")) {
			if (dst > 0) {
				toScroll = (Equip) chr.getInventory(MapleInventoryType.CODY).getItem(dst);
				chr.getClient().send(CWvsContext.enableActions(chr));
			}
		}
		if (toScroll == null) {
			return false;
		}
		final byte oldLevel = toScroll.getLevel();
		final byte oldEnhance = toScroll.getEnhance();
		final byte oldState = toScroll.getState();
		final int oldFlag = toScroll.getFlag();
		final byte oldSlots = toScroll.getUpgradeSlots();

		if (scroll == null || header == RecvPacketOpcode.USE_FLAG_SCROLL) {
			scroll = chr.getInventory(MapleInventoryType.CASH).getItem(slot);
		} else if (scroll != null && !GameConstants.isSpecialScroll(scroll.getItemId())
				&& !GameConstants.isCleanSlate(scroll.getItemId()) && !GameConstants.isEquipScroll(scroll.getItemId())
				&& !GameConstants.isPotentialScroll(scroll.getItemId())
				&& !GameConstants.isRebirthFireScroll(scroll.getItemId()) && scroll.getItemId() / 10000 != 204
				&& scroll.getItemId() / 10000 != 272 && scroll.getItemId() / 10000 != 264) {
			scroll = chr.getInventory(MapleInventoryType.CASH).getItem(slot);
		}
		if (scroll == null) {
			c.getSession().writeAndFlush((Object) CWvsContext.enableActions(chr));
			chr.dropMessage(1, "不存在的卷軸.");
			return false;
		}
		final MapleItemInformationProvider ii2 = MapleItemInformationProvider.getInstance();
		if (scroll != null && toScroll != null) {
			FileoutputUtil.log(FileoutputUtil.物品使用日誌,
					"[卷轴使用] 账号ID  : " + c.getAccID() + "| 玩家  " + c.getPlayer().getName() + " 将  "
							+ ii2.getName(scroll.getItemId()) + "使用于 " + ii2.getName(toScroll.getItemId()) + ".");
		}
		if (scroll.getItemId() / 100 == 20496) {
			if (toScroll.getOwner().contains("강")) {
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				c.getPlayer().dropMessage(5, "狩獵/宣傳/贊助的強化物品無法使用純淨卷軸.");
				return false;
			}
			if (toScroll2 != null) {
				if (toScroll2.getOwner().contains(" 強")) {
					c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
					c.getPlayer().dropMessage(5, "狩獵/宣傳/贊助的強化物品無法使用純淨卷軸.");
					return false;
				}
			}

			final Equip origin = (Equip) MapleItemInformationProvider.getInstance().getEquipById(toScroll.getItemId());
			toScroll.setAcc(origin.getAcc());
			toScroll.setAvoid(origin.getAvoid());
			toScroll.setDex(origin.getDex());
			toScroll.setHands(origin.getHands());
			toScroll.setHp(origin.getHp());
			toScroll.setInt(origin.getInt());
			toScroll.setJump(origin.getJump());
			toScroll.setLevel(origin.getLevel());
			toScroll.setLuk(origin.getLuk());
			toScroll.setMatk(origin.getMatk());
			toScroll.setMdef(origin.getMdef());
			toScroll.setMp(origin.getMp());
			toScroll.setSpeed(origin.getSpeed());
			toScroll.setStr(origin.getStr());
			toScroll.setUpgradeSlots(origin.getUpgradeSlots());
			toScroll.setWatk(origin.getWatk());
			toScroll.setWdef(origin.getWdef());
			toScroll.setEnhance((byte) 0);
			toScroll.setViciousHammer((byte) 0);
			chr.getInventory(MapleInventoryType.USE).removeItem(scroll.getPosition());
			c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.updateScrollandItem(scroll, toScroll));
			c.getPlayer().getMap()
					.broadcastMessage(c.getPlayer(), CField.getScrollEffect(c.getPlayer().getId(),
							Equip.ScrollResult.SUCCESS, legendarySpirit, scroll.getItemId(), toScroll.getItemId()),
							true);
			return false;
		}
		if (!GameConstants.isSpecialScroll(scroll.getItemId()) && !GameConstants.isCleanSlate(scroll.getItemId())
				&& !GameConstants.isEquipScroll(scroll.getItemId()) && scroll.getItemId() != 2049360
				&& scroll.getItemId() != 2049361 && !GameConstants.isPotentialScroll(scroll.getItemId())
				&& !GameConstants.isRebirthFireScroll(scroll.getItemId())
				&& !GameConstants.isLuckyScroll(scroll.getItemId())) {

//			  條件說明：
//			  1. 非保護/安全等特殊卷軸 
//			  2. 非白衣卷軸
//			  3. 非裝備強化卷軸 
//			  4. 非潛能賦予卷軸
//			  5. 非安卓機器心臟
//			  6. 非重生火焰
//			  7. 非傳說潛能卷軸且非魔法箭矢 
//			  8. 非ZERO幸運卷軸
//			 
			if (toScroll.getUpgradeSlots() < 1) {
				/*
				 * 업그레이드 슬롯이 없을 때
				 */
				c.getSession().writeAndFlush(CWvsContext.enableActions(chr));
				chr.dropMessage(1, "升級次數不足.");
				return false;
			}
		} else if (GameConstants.isEquipScroll(scroll.getItemId())) {
			/*
			 * 條件說明： 1. 當使用裝備強化卷軸時 2. 且非「驚奇裝備強化卷軸」時 - 裝備剩餘升級次數 ≥1 次 - 或裝備強化等級 ≥15級 -
			 * 或該裝備為現金道具
			 */
			if ((scroll.getItemId() != 2049360 && scroll.getItemId() != 2049361 && toScroll.getUpgradeSlots() >= 1)
					|| ii.isCash(toScroll.getItemId())) {
				c.getSession().writeAndFlush(CWvsContext.enableActions(chr));
				chr.dropMessage(1, "더 이상 강화할 수 없는 아이템입니다.");
				return false;
			}
		} else if (GameConstants.isPotentialScroll(scroll.getItemId())) {
			/*
			 * 條件說明： 1. 當使用潛能賦予卷軸時，且該裝備已具有潛能屬性
			 */
			if (toScroll.getState() >= 1) {
				c.getSession().writeAndFlush(CWvsContext.enableActions(chr));
				chr.dropMessage(1, "該裝備已具有潛能能力.");
				return false;
			}
		}

		if ((scroll.getItemId() == 2049166 || scroll.getItemId() == 2049167)
				&& !GameConstants.isWeapon(toScroll.getItemId())) {
			chr.dropMessage(1, "無法對該道具使用卷軸.");
			chr.getClient().send(CWvsContext.enableActions(chr));
			return false;
		}

		if (toScroll.getItemId() / 1000 != 1672 && !GameConstants.canScroll(toScroll.getItemId())
				&& GameConstants.isChaosScroll(scroll.getItemId())) {
			/*
			 * 條件說明： 當道具屬於不可強化類型（機械裝備/龍魔導士裝備/騎乘寵物裝備） 且同時使用混沌卷軸時
			 */
			c.getSession().writeAndFlush(CWvsContext.enableActions(chr));
			chr.dropMessage(1, "無法使用卷軸的道具.");
			return false;
		}
		if (ii.isCash(toScroll.getItemId())) {
			/*
			 * 現金裝備無法進行強化
			 */
			if (toScroll.getItemId() / 1000 != 1802) {
				c.getSession().writeAndFlush(CWvsContext.enableActions(chr));
				chr.dropMessage(1, "現金道具無法強化.");
				return false;
			} else { // 寵物裝備
				if (!ii.getName(scroll.getItemId()).contains("寵物裝備")) {
					c.getSession().writeAndFlush(CWvsContext.enableActions(chr));
					chr.dropMessage(1, "寵物裝備只能使用寵物裝備專用卷軸.");
					return false;
				}
			}
		}
		if (scroll.getItemId() == 2049135 && (toScroll.getItemId() < 1182000 || toScroll.getItemId() > 1182005)) {
			chr.dropMessage(1, "僅限黎明系列道具使用.");
			chr.getClient().send(CWvsContext.enableActions(chr));
			return false;
		}
		if (GameConstants.isTablet(scroll.getItemId()) && toScroll.getDurability() < 0) {
			c.getSession().writeAndFlush((Object) CWvsContext.enableActions(chr));
			chr.dropMessage(1, "無法使用附魔書的道具.");
			return false;
		}
		Item wscroll = null;
		final List<Integer> scrollReqs = ii.getScrollReqs(scroll.getItemId());
		if (scrollReqs.size() > 0 && !scrollReqs.contains(toScroll.getItemId())) {
			c.getSession().writeAndFlush((Object) CWvsContext.enableActions(chr));
			chr.dropMessage(1, "RETURN 8");
			return false;
		}
		if (whiteScroll) {
			wscroll = chr.getInventory(MapleInventoryType.USE).findById(2340000);
			if (wscroll == null) {
				whiteScroll = false;
			}
		}
		if (scroll.getItemId() == 2041200 && toScroll.getItemId() != 1122000 && toScroll.getItemId() != 1122076) {
			c.getSession().writeAndFlush((Object) CWvsContext.enableActions(chr));
			chr.dropMessage(1, "龍之石僅限用於暗黑龍王項鍊.");
			return false;
		}
		if ((scroll.getItemId() == 2046856 || scroll.getItemId() == 2046857)
				&& (toScroll.getItemId() / 1000 == 1152 || !GameConstants.isAccessory(toScroll.getItemId()))) {
			c.getSession().writeAndFlush((Object) CWvsContext.enableActions(chr));
			chr.dropMessage(1, "僅限飾品使用的卷軸.");
			return false;
		}
		if ((scroll.getItemId() == 2049166 || scroll.getItemId() == 2046991 || scroll.getItemId() == 2046992
				|| scroll.getItemId() == 2046996 || scroll.getItemId() == 2046997)
				&& GameConstants.isTwoHanded(toScroll.getItemId())) {
			c.getSession().writeAndFlush((Object) CWvsContext.enableActions(chr));
			chr.dropMessage(1, "僅限單手武器使用的卷軸.");
			return false;
		}
		if ((scroll.getItemId() == 2049167 || scroll.getItemId() == 2047814 || scroll.getItemId() == 2047818)
				&& !GameConstants.isTwoHanded(toScroll.getItemId()) && toScroll.getItemId() / 1000 != 1672) {
			c.getSession().writeAndFlush((Object) CWvsContext.enableActions(chr));
			chr.dropMessage(1, "僅限雙手武器使用的卷軸.");
			return false;
		}
		if (GameConstants.isAccessoryScroll(scroll.getItemId()) && !GameConstants.isAccessory(toScroll.getItemId())) {
			c.getSession().writeAndFlush((Object) CWvsContext.enableActions(chr));
			chr.dropMessage(1, "無法使用飾品卷軸的道具.");
			return false;
		}
		if (scroll.getQuantity() <= 0) {
			chr.dropMessage(1, "不存在的卷軸無法使用.");
			return false;
		}
		if (toScroll.getUpgradeSlots() > 0) {
			if (scroll.getItemId() >= 2049370 && scroll.getItemId() <= 2049380) {
				c.getSession().writeAndFlush(CWvsContext.enableActions(chr));
				chr.dropMessage(1, "仍有剩餘升級次數.");
				return false;
			}
		}
		if (scroll.getItemId() == 2049099 && (toScroll.getEnhance() < 22 || toScroll.getEnhance() >= 25)) {
			chr.dropMessage(1, "僅限22星至25星道具使用.");
			c.getSession().writeAndFlush(CWvsContext.enableActions(chr));
			return false;
		}

		if (ItemFlag.RETURN_SCROLL.check(toScroll.getFlag())) {
			chr.returnSc = scroll.getItemId();
			chr.returnscroll = (Equip) toScroll.copy();
		}
		if (header == RecvPacketOpcode.USE_BLACK_REBIRTH_SCROLL) {
			final long newRebirth = toScroll.newRebirth(ii.getReqLevel(toScroll.getItemId()), scroll.getItemId(),
					false);
			c.getSession()
					.writeAndFlush((Object) CWvsContext.useBlackRebirthScroll(toScroll, scroll, newRebirth, false));
			MapleInventoryManipulator.removeFromSlot(c, GameConstants.getInventoryType(scroll.getItemId()),
					scroll.getPosition(), (short) 1, false, false);
			c.getSession().writeAndFlush((Object) CWvsContext.blackRebirthResult(true, toScroll.getFire(), toScroll));
			c.getSession().writeAndFlush((Object) CWvsContext.blackRebirthResult(false, newRebirth, toScroll));
			chr.blackRebirth = newRebirth;
			chr.blackRebirthScroll = (Equip) toScroll.copy();
			chr.blackRebirthPos = slot;
			return false;
		}

		// Scroll Success/Failure/Curse
		final Equip scrolled = (Equip) ii.scrollEquipWithId(toScroll, scroll, whiteScroll, chr);
		Equip.ScrollResult scrollSuccess;
		if (scrolled == null) {
			scrollSuccess = Equip.ScrollResult.CURSE;
		} else if (GameConstants.isRebirthFireScroll(scroll.getItemId())) {
			scrollSuccess = Equip.ScrollResult.SUCCESS;
		} else if (scrolled.getLevel() > oldLevel || scrolled.getEnhance() > oldEnhance
				|| scrolled.getState() != oldState || scrolled.getFlag() > oldFlag) {
			scrollSuccess = Equip.ScrollResult.SUCCESS;
		} else if (GameConstants.isCleanSlate(scroll.getItemId()) && scrolled.getUpgradeSlots() > oldSlots) {
			scrollSuccess = Equip.ScrollResult.SUCCESS;
		} else {
			scrollSuccess = Equip.ScrollResult.FAIL;
			if (ItemFlag.RECOVERY_SHIELD.check(toScroll.getFlag())) {
				recovery = true;
			}
		}
		if (recovery) {
			chr.dropMessage(5, "無法使用飾品卷軸的物品.");
		} else if (GameConstants.isZero(chr.getJob()) && toScroll.getPosition() == -11) {
			chr.getInventory(GameConstants.getInventoryType(scroll.getItemId())).removeItem(scroll.getPosition(),
					(short) 1, false);
		} else {
			chr.getInventory(GameConstants.getInventoryType(scroll.getItemId())).removeItem(scroll.getPosition(),
					(short) 1, false);
		}
		if (scrollSuccess == Equip.ScrollResult.SUCCESS) {
			EquipmentEnchant.checkEquipmentStats(c, toScroll);
			if (toScroll2 != null) {
				EquipmentEnchant.checkEquipmentStats(c, toScroll2);
			}
		}
		if (whiteScroll) {
			MapleInventoryManipulator.removeFromSlot(c, GameConstants.getInventoryType(scroll.getItemId()),
					wscroll.getPosition(), (short) 1, false, false);
		}
		if (header != RecvPacketOpcode.USE_FLAG_SCROLL) {
			if (ItemFlag.RECOVERY_SHIELD.check(toScroll.getFlag())) {
				toScroll.setFlag(toScroll.getFlag() - ItemFlag.RECOVERY_SHIELD.getValue());
				if (GameConstants.isZero(chr.getJob()) && toScroll2 != null) {
					toScroll2.setFlag(toScroll2.getFlag() - ItemFlag.RECOVERY_SHIELD.getValue());
				}
			}
			if (ItemFlag.SAFETY_SHIELD.check(toScroll.getFlag())) {
				toScroll.setFlag(toScroll.getFlag() - ItemFlag.SAFETY_SHIELD.getValue());
				if (GameConstants.isZero(chr.getJob()) && toScroll2 != null) {
					toScroll2.setFlag(toScroll2.getFlag() - ItemFlag.SAFETY_SHIELD.getValue());
				}
			}
			if (ItemFlag.PROTECT_SHIELD.check(toScroll.getFlag())) {
				toScroll.setFlag(toScroll.getFlag() - ItemFlag.PROTECT_SHIELD.getValue());
				if (GameConstants.isZero(chr.getJob()) && toScroll2 != null) {
					toScroll2.setFlag(toScroll2.getFlag() - ItemFlag.PROTECT_SHIELD.getValue());
				}
			}
			if (ItemFlag.LUCKY_PROTECT_SHIELD.check(toScroll.getFlag())) {
				toScroll.setFlag(toScroll.getFlag() - ItemFlag.LUCKY_PROTECT_SHIELD.getValue());
				if (GameConstants.isZero(chr.getJob()) && toScroll2 != null) {
					toScroll2.setFlag(toScroll2.getFlag() - ItemFlag.LUCKY_PROTECT_SHIELD.getValue());
				}
			}
		}
		if (scrollSuccess == Equip.ScrollResult.CURSE) {
			c.getSession()
					.writeAndFlush((Object) CWvsContext.InventoryPacket.updateScrollandItem(scroll, toScroll, true));
			if (dst < 0) {
				chr.getInventory(MapleInventoryType.EQUIPPED).removeItem(toScroll.getPosition());
			} else {
				chr.getInventory(MapleInventoryType.EQUIP).removeItem(toScroll.getPosition());
			}
		} else {
			c.getSession()
					.writeAndFlush((Object) CWvsContext.InventoryPacket.updateScrollandItem(scroll, toScroll, false)); // 여기
			if (toScroll2 != null) {
				c.getSession().writeAndFlush(
						(Object) CWvsContext.InventoryPacket.updateScrollandItem(scroll, toScroll2, false));
			}
			if (!GameConstants.isSpecialScroll(scroll.getItemId()) && !GameConstants.isCleanSlate(scroll.getItemId())
					&& !GameConstants.isEquipScroll(scroll.getItemId()) && scroll.getItemId() != 2049360
					&& scroll.getItemId() != 2049361 && !GameConstants.isPotentialScroll(scroll.getItemId())
					&& !GameConstants.isRebirthFireScroll(scroll.getItemId())
					&& !GameConstants.isLuckyScroll(scroll.getItemId()) && c.getPlayer().returnscroll != null
					&& scrollSuccess == Equip.ScrollResult.SUCCESS) {
				c.getSession().writeAndFlush(
						(Object) CWvsContext.returnEffectConfirm(c.getPlayer().returnscroll, scroll.getItemId()));
				c.getSession().writeAndFlush(
						(Object) CWvsContext.returnEffectModify(c.getPlayer().returnscroll, scroll.getItemId()));
			} else if (!GameConstants.isSpecialScroll(scroll.getItemId())
					&& !GameConstants.isCleanSlate(scroll.getItemId())
					&& !GameConstants.isEquipScroll(scroll.getItemId()) && scroll.getItemId() != 2049360
					&& scroll.getItemId() != 2049361 && !GameConstants.isPotentialScroll(scroll.getItemId())
					&& !GameConstants.isRebirthFireScroll(scroll.getItemId())
					&& !GameConstants.isLuckyScroll(scroll.getItemId()) && c.getPlayer().returnscroll != null
					&& scrollSuccess == Equip.ScrollResult.FAIL) {
				c.getPlayer().returnscroll = null;
				toScroll.setFlag(toScroll.getFlag() - ItemFlag.RETURN_SCROLL.getValue());
				toScroll.setUpgradeSlots((byte) (toScroll.getUpgradeSlots() + 1));
				chr.getClient().getSession()
						.writeAndFlush((Object) CWvsContext.InventoryPacket.updateEquipSlot(toScroll));
			}
		}
		if (GameConstants.isZero(chr.getJob()) && toScroll.getPosition() == -11) {
			chr.getMap().broadcastMessage(chr, CField.getScrollEffect(c.getPlayer().getId(), scrollSuccess,
					legendarySpirit, scroll.getItemId(), toScroll.getItemId()), true);
		} else {
			chr.getMap().broadcastMessage(chr, CField.getScrollEffect(c.getPlayer().getId(), scrollSuccess,
					legendarySpirit, scroll.getItemId(), toScroll.getItemId()), true);
		}
		if (scrolled.getShowScrollOption() != null) {
			chr.getClient().getSession().writeAndFlush((Object) CField.showScrollOption(scrolled.getItemId(),
					scroll.getItemId(), scrolled.getShowScrollOption()));
			scrolled.setShowScrollOption(null);
		}
		if (dst < 0 && (scrollSuccess == Equip.ScrollResult.SUCCESS || scrollSuccess == Equip.ScrollResult.CURSE)) {
			chr.equipChanged();
		}
		if (header == RecvPacketOpcode.USE_REBIRTH_SCROLL) {
			chr.getClient().send(CWvsContext.RebirthScrollWindow(scroll.getItemId(), toScroll.getPosition()));
		}
		c.getSession().writeAndFlush((Object) CWvsContext.enableActions(chr));
		return true;
	}

	public static void UseEditionalScroll(final LittleEndianAccessor slea, final MapleClient c) {
		try {
			slea.skip(4);
			final short mode = slea.readShort();
			final Item toUse = c.getPlayer().getInventory(MapleInventoryType.USE).getItem(mode);
			if (toUse.getItemId() >= 2048305 && toUse.getItemId() <= 2048316) {
				final short slot = slea.readShort();
				Item item;
				if (slot < 0) {
					item = c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot);
				} else {
					item = c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(slot);
				}
				Equip zeroEquip = null;
				if (GameConstants.isAlphaWeapon(item.getItemId())) {
					zeroEquip = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-10));
				} else if (GameConstants.isBetaWeapon(item.getItemId())) {
					zeroEquip = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-11));
				}
				if (GameConstants.isZero(c.getPlayer().getJob()) && zeroEquip != null) {
					final Item item2 = c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-10));
					final Item item3 = c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-11));
					final Equip eq1 = (Equip) item2;
					final Equip eq2 = (Equip) item3;
					if (eq1.getState() == 0 || eq2.getState() == 0 || (eq1.getState() == 1 && eq1.getPotential1() == 0)
							|| (eq2.getState() == 1 && eq2.getPotential1() == 0)) {
						c.getPlayer().dropMessage(1, "請先開啓潛能.");
						c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
						return;
					}
					final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
					final boolean succes = Randomizer.isSuccess(ii.getSuccess(item.getItemId(), c.getPlayer(), item));
					if (succes) {
						int alpha_option = 0;
						int alpha_option2 = 0;
						int alpha_option3_sbal = 0;
						final int alpha_level = 1;
						final int beta_option = 0;
						final int beta_option2 = 0;
						final int beta_option3_sbal = 0;
						final int beta_level = 1;
						alpha_option = potential(eq1.getItemId(), alpha_level, true);
						alpha_option2 = potential(eq1.getItemId(), alpha_level, true);
						alpha_option3_sbal = potential(eq1.getItemId(), alpha_level, true);
						if (Randomizer.nextInt(100) < 20 || toUse.getItemId() == 2048306) { // 20퍼센트 확률로 3줄
							eq1.setPotential4(alpha_option);
							eq1.setPotential5(alpha_option2);
							eq1.setPotential6(alpha_option3_sbal);
							eq2.setPotential4(alpha_option);
							eq2.setPotential5(alpha_option2);
							eq2.setPotential6(alpha_option3_sbal);
						} else {
							eq1.setPotential4(alpha_option);
							eq1.setPotential5(alpha_option2);
							eq2.setPotential4(alpha_option);
							eq2.setPotential5(alpha_option2);
						}
					}
					c.getSession()
							.writeAndFlush((Object) CWvsContext.InventoryPacket.updateScrollandItem(toUse, item2));
					c.getSession()
							.writeAndFlush((Object) CWvsContext.InventoryPacket.updateScrollandItem(toUse, item3));
					c.getPlayer().getMap().broadcastMessage(CField.showPotentialReset(c.getPlayer().getId(), succes,
							toUse.getItemId(), item.getItemId()));
					MapleInventoryManipulator.removeById(c, MapleInventoryType.USE, toUse.getItemId(), 1, true, false);
					c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
				} else {
					final Equip eq3 = (Equip) item;
					if (eq3.getState() == 0 || eq3.getPotential1() == 0) {
						c.getPlayer().dropMessage(1, "請先開啓潛能.");
						c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
						return;
					}
					final MapleItemInformationProvider ii2 = MapleItemInformationProvider.getInstance();
					final boolean succes2 = Randomizer.isSuccess(ii2.getSuccess(item.getItemId(), c.getPlayer(), item));
					if (succes2) {
						int option = 0;
						int option2 = 0;
						int option3_sbal = 0;
						final int level = 2;
						option = potential(eq3.getItemId(), level, true);
						option2 = potential(eq3.getItemId(), level, true);
						option3_sbal = potential(eq3.getItemId(), level, true);
						if (Randomizer.nextInt(100) < 20 || toUse.getItemId() == 2048306) {
							eq3.setPotential4(option);
							eq3.setPotential5(option2);
							eq3.setPotential6(option3_sbal);
						} else {
							eq3.setPotential4(option);
							eq3.setPotential5(option2);
						}
					}
					c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.updateScrollandItem(toUse, item));
					c.getPlayer().getMap().broadcastMessage(CField.showPotentialReset(c.getPlayer().getId(), succes2,
							toUse.getItemId(), eq3.getItemId()));
					MapleInventoryManipulator.removeById(c, MapleInventoryType.USE, toUse.getItemId(), 1, true, false);
					c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static final boolean UseSkillBook(final short slot, final int itemId, final MapleClient c,
			final MapleCharacter chr) {
		final Item toUse = chr.getInventory(GameConstants.getInventoryType(itemId)).getItem(slot);
		if (toUse == null || toUse.getQuantity() < 1 || toUse.getItemId() != itemId) {
			return false;
		}
		final Map<String, Integer> skilldata = MapleItemInformationProvider.getInstance()
				.getEquipStats(toUse.getItemId());
		if (skilldata == null) {
			return false;
		}
		boolean canuse = false;
		boolean success = false;
		final int skill = 0;
		final int maxlevel = 0;
		final Integer SuccessRate = skilldata.get("success");
		final Integer ReqSkillLevel = skilldata.get("reqSkillLevel");
		final Integer MasterLevel = skilldata.get("masterLevel");
		byte i = 0;
		while (true) {
			final Integer CurrentLoopedSkillId = skilldata.get("skillid" + i);
			++i;
			if (CurrentLoopedSkillId == null) {
				break;
			}
			if (MasterLevel == null) {
				break;
			}
			final Skill CurrSkillData = SkillFactory.getSkill(CurrentLoopedSkillId);
			if (CurrSkillData != null && CurrSkillData.canBeLearnedBy(chr)
					&& (ReqSkillLevel == null || chr.getSkillLevel(CurrSkillData) >= ReqSkillLevel)
					&& chr.getMasterLevel(CurrSkillData) < MasterLevel) {
				canuse = true;
				if (SuccessRate == null || Randomizer.nextInt(100) <= SuccessRate) {
					success = true;
					chr.changeSingleSkillLevel(CurrSkillData, chr.getSkillLevel(CurrSkillData),
							(byte) (int) MasterLevel);
				} else {
					success = false;
				}
				MapleInventoryManipulator.removeFromSlot(c, GameConstants.getInventoryType(itemId), slot, (short) 1,
						false);
				break;
			}
		}
		c.getPlayer().getMap().broadcastMessage(CWvsContext.useSkillBook(chr, skill, maxlevel, canuse, success));
		c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
		return canuse;
	}

	public static final void UseCatchItem(final LittleEndianAccessor slea, final MapleClient c,
			final MapleCharacter chr) {
		slea.readInt();
		c.getPlayer().setScrolledPosition((short) 0);
		final short slot = slea.readShort();
		final int itemid = slea.readInt();
		final MapleMonster mob = chr.getMap().getMonsterByOid(slea.readInt());
		final Item toUse = chr.getInventory(MapleInventoryType.USE).getItem(slot);
		final MapleMap map = chr.getMap();
		if (toUse != null && toUse.getQuantity() > 0 && toUse.getItemId() == itemid && mob != null
				&& itemid / 10000 == 227
				&& MapleItemInformationProvider.getInstance().getCardMobId(itemid) == mob.getId()) {
			if (!MapleItemInformationProvider.getInstance().isMobHP(itemid) || mob.getHp() <= mob.getMobMaxHp() / 2L) {
				map.broadcastMessage(MobPacket.catchMonster(mob.getObjectId(), (byte) 1));
				map.killMonster(mob, chr, true, false, (byte) 1);
				MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false, false);
				if (MapleItemInformationProvider.getInstance().getCreateId(itemid) > 0) {
					MapleInventoryManipulator.addById(c, MapleItemInformationProvider.getInstance().getCreateId(itemid),
							(short) 1, "Catch item " + itemid + " on " + FileoutputUtil.CurrentReadable_Date());
				}
			} else {
				map.broadcastMessage(MobPacket.catchMonster(mob.getObjectId(), (byte) 0));
				c.getSession().writeAndFlush((Object) CWvsContext.catchMob(mob.getId(), itemid, (byte) 0));
			}
		}
		c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
	}

	public static final void UseMountFood(final LittleEndianAccessor slea, final MapleClient c,
			final MapleCharacter chr) {
		slea.readInt();
		final short slot = slea.readShort();
		final int itemid = slea.readInt();
		final Item toUse = chr.getInventory(MapleInventoryType.USE).getItem(slot);
		final MapleMount mount = chr.getMount();
		if (itemid / 10000 == 226 && toUse != null && toUse.getQuantity() > 0 && toUse.getItemId() == itemid
				&& mount != null) {
			final int fatigue = mount.getFatigue();
			boolean levelup = false;
			mount.setFatigue((byte) (-30));
			if (fatigue > 0) {
				mount.increaseExp();
				final int level = mount.getLevel();
				if (level < 30 && mount.getExp() >= GameConstants.getMountExpNeededForLevel(level + 1)) {
					mount.setLevel((byte) (level + 1));
					levelup = true;
				}
			}
			chr.getMap().broadcastMessage(CWvsContext.updateMount(chr, levelup));
			MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
		}
		c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
	}

	public static final void UseScriptedNPCItem(final LittleEndianAccessor slea, final MapleClient c,
			final MapleCharacter chr) {
		final short slot = slea.readShort();
		final int itemId = slea.readInt();
		final Item toUse = chr.getInventory(GameConstants.getInventoryType(itemId)).getItem(slot);
		long expiration_days = 0L;
		int mountid = 0;
		if (toUse != null && toUse.getQuantity() >= 1 && toUse.getItemId() == itemId && !chr.inPVP()) {
			FileoutputUtil.log(FileoutputUtil.腳本物品使用日誌,
					"[스크립트 아이템 사용] 계정 아이디 : " + c.getAccID() + " | " + c.getPlayer().getName() + "이 "
							+ MapleItemInformationProvider.getInstance().getName(toUse.getItemId()) + "("
							+ toUse.getItemId() + ")을 사용함.");
			switch (toUse.getItemId()) {
			case 2435719:
			case 2435902: {
				if (GameConstants.isPinkBean(c.getPlayer().getJob()) || GameConstants.isYeti(c.getPlayer().getJob())) {
					c.getPlayer().dropMessage(1, "粉紅兔與雪怪是不可能的組合.");
					break;
				}
				MatrixHandler.UseCoreJamStone(c, itemId, 1, 0);
				break;
			}
			case 2439279: {
				if (GameConstants.isPinkBean(c.getPlayer().getJob()) || GameConstants.isYeti(c.getPlayer().getJob())) {
					c.getPlayer().dropMessage(1, "粉紅兔與雪怪是不可能的組合.");
					break;
				}
				MatrixHandler.UseEnforcedCoreJamStone(c, itemId, 1, 2);
				break;
			}
			case 2432290:
			case 2631501: {
				if (c.getPlayer().getMap().MapiaIng) {
					c.send(CWvsContext.serverNotice(1, "", "지금은 사용 할 수 없습니다."));
					c.send(CWvsContext.enableActions(chr));
					return;
				}
				c.getPlayer().getMap().MapiaIng = true;
				for (final MapleCharacter chrs : c.getPlayer().getMap().getAllChracater()) {
					if (chrs.isAlive()) {
						chrs.cancelEffect(MapleItemInformationProvider.getInstance()
								.getItemEffect((toUse.getItemId() == 2631501) ? 2023300 : 2023912));
						MapleItemInformationProvider.getInstance()
								.getItemEffect((toUse.getItemId() == 2631501) ? 2023912 : 2023300).applyTo(chrs);
					}
				}
				c.getPlayer().getMap().broadcastMessage(CField.startMapEffect("길드의 축복이 여러분과 함께 하기를...",
						(toUse.getItemId() == 2631501) ? 5121101 : 5121041, true));
				Timer.BuffTimer.getInstance().schedule(() -> {
					c.getPlayer().getMap().MapiaIng = false;
					c.getPlayer().getMap().broadcastMessage(CField.removeMapEffect());
					return;
				}, 10000L);
				MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
				break;
			}
			case 2433835: {
				NPCScriptManager.getInstance().startItem(c, 1012103, "consume_2433835");
				break;
			}
			case 2430732: {
				int id = 0;
				chr.addCustomItem(id);
				c.getPlayer().dropMessage(5,
						GameConstants.customItems.get(id).getName() + " 를 획득하였습니다. 소비칸에 특수 장비창 -> 인벤토리를 확인해주세요.");
				MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (byte) 1, false);
				break;
			}
			case 2430885: {
				int id = 1;
				chr.addCustomItem(id);
				c.getPlayer().dropMessage(5,
						GameConstants.customItems.get(id).getName() + " 를 획득하였습니다. 소비칸에 특수 장비창 -> 인벤토리를 확인해주세요.");
				MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (byte) 1, false);
				break;
			}
			case 2430886: {
				int id = 2;
				chr.addCustomItem(id);
				c.getPlayer().dropMessage(5,
						GameConstants.customItems.get(id).getName() + " 를 획득하였습니다. 소비칸에 특수 장비창 -> 인벤토리를 확인해주세요.");
				MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (byte) 1, false);
				break;
			}
			case 2430887: {
				int id = 3;
				chr.addCustomItem(id);
				c.getPlayer().dropMessage(5,
						GameConstants.customItems.get(id).getName() + " 를 획득하였습니다. 소비칸에 특수 장비창 -> 인벤토리를 확인해주세요.");
				MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (byte) 1, false);
				break;
			}
			case 2430888: {
				int id = 4;
				chr.addCustomItem(id);
				c.getPlayer().dropMessage(5,
						GameConstants.customItems.get(id).getName() + " 를 획득하였습니다. 소비칸에 특수 장비창 -> 인벤토리를 확인해주세요.");
				MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (byte) 1, false);
				break;
			}
			case 2430889: {
				int id = 5;
				chr.addCustomItem(id);
				c.getPlayer().dropMessage(5,
						GameConstants.customItems.get(id).getName() + " 를 획득하였습니다. 소비칸에 특수 장비창 -> 인벤토리를 확인해주세요.");
				MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (byte) 1, false);
				break;
			}
			case 2430890: {
				int id = 6;
				chr.addCustomItem(id);
				c.getPlayer().dropMessage(5,
						GameConstants.customItems.get(id).getName() + " 를 획득하였습니다. 소비칸에 특수 장비창 -> 인벤토리를 확인해주세요.");
				MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (byte) 1, false);
				break;
			}
			case 2430891: {
				int id = 7;
				chr.addCustomItem(id);
				c.getPlayer().dropMessage(5,
						GameConstants.customItems.get(id).getName() + " 를 획득하였습니다. 소비칸에 특수 장비창 -> 인벤토리를 확인해주세요.");
				MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (byte) 1, false);
				break;
			}
			case 2430892: {
				int id = 8;
				chr.addCustomItem(id);
				c.getPlayer().dropMessage(5,
						GameConstants.customItems.get(id).getName() + " 를 획득하였습니다. 소비칸에 특수 장비창 -> 인벤토리를 확인해주세요.");
				MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (byte) 1, false);
				break;
			}
			case 2430893: {
				int id = 9;
				chr.addCustomItem(id);
				c.getPlayer().dropMessage(5,
						GameConstants.customItems.get(id).getName() + " 를 획득하였습니다. 소비칸에 특수 장비창 -> 인벤토리를 확인해주세요.");
				MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (byte) 1, false);
				break;
			}
			case 2430894: {
				int id = 10;
				chr.addCustomItem(id);
				c.getPlayer().dropMessage(5,
						GameConstants.customItems.get(id).getName() + " 를 획득하였습니다. 소비칸에 특수 장비창 -> 인벤토리를 확인해주세요.");
				MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (byte) 1, false);
				break;
			}
			case 2430945: {
				int id = 11;
				chr.addCustomItem(id);
				c.getPlayer().dropMessage(5,
						GameConstants.customItems.get(id).getName() + " 를 획득하였습니다. 소비칸에 특수 장비창 -> 인벤토리를 확인해주세요.");
				MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (byte) 1, false);
				break;
			}
			case 2430946: {
				int id = 12;
				chr.addCustomItem(id);
				c.getPlayer().dropMessage(5,
						GameConstants.customItems.get(id).getName() + " 를 획득하였습니다. 소비칸에 특수 장비창 -> 인벤토리를 확인해주세요.");
				MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (byte) 1, false);
				break;
			}

			case 2433509: {
				NPCScriptManager.getInstance().startItem(c, 9000162, "consume_2433509");
				break;
			}
			case 2433510: {
				NPCScriptManager.getInstance().startItem(c, 9000162, "consume_2433510");
				break;
			}
			case 2434006: {
				NPCScriptManager.getInstance().startItem(c, 9000162, "consume_2434006");
				break;
			}
			case 5680222: {
				NPCScriptManager.getInstance().startItem(c, 9000216, "mannequin_add");
				break;
			}
			case 5680531: {
				NPCScriptManager.getInstance().startItem(c, 9000216, "mannequin_slotadd");
				break;
			}
			case 2431940: {
				int pirodo = 1000;
				switch (1) {
				case 1: {
					pirodo = 60;
					break;
				}
				case 2: {
					pirodo = 80;
					break;
				}
				case 3: {
					pirodo = 100;
					break;
				}
				case 4: {
					pirodo = 120;
					break;
				}
				case 5: {
					pirodo = 160;
					break;
				}
				case 6: {
					pirodo = 160;
					break;
				}
				case 7: {
					pirodo = 160;
					break;
				}
				case 8: {
					pirodo = 160;
					break;
				}
				}
				long point = c.getPlayer().getKeyValue(123, "pp") + 10;
				if (c.getPlayer().getKeyValue(123, "pp") >= pirodo) {
					c.getPlayer().dropMessage(5, "이미 모든 피로도가 충전되있습니다.");
					c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
					return;
				}
				if (c.getPlayer().getKeyValue(123, "pp") + 10 > pirodo) {
					point = pirodo;
				}
				c.getPlayer().setKeyValue(123, "pp", String.valueOf(point));
				c.getPlayer().dropMessage(5, "피로도가 증가했습니다. 피로도 : " + c.getPlayer().getKeyValue(123, "pp"));
				MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (byte) 1, false);
				break;
			}
			case 2350000: {
				if (c.getCharacterSlots() >= 48) {
					c.getPlayer().dropMessage(5, "현재 캐릭터 슬롯 증가쿠폰을 사용하실 수 없습니다.");
					return;
				}
				MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
				if (!c.gainCharacterSlot()) {
					c.getPlayer().dropMessage(5, "현재 캐릭터 슬롯 증가쿠폰을 사용하실 수 없습니다.");
					break;
				}
				c.getPlayer().dropMessage(1, "已擴展角色欄比特數量.");
				break;
			}
			case 2430008: {
				chr.saveLocation(SavedLocationType.RICHIE);
				boolean warped = false;
				for (int i = 390001000; i <= 390001004; ++i) {
					final MapleMap map = c.getChannelServer().getMapFactory().getMap(i);
					if (map.getCharactersSize() == 0) {
						chr.changeMap(map, map.getPortal(0));
						warped = true;
						break;
					}
				}
				if (warped) {
					MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
					break;
				}
				c.getPlayer().dropMessage(5, "All maps are currently in use, please try again later.");
				break;
			}
			case 2430112: {
				if (c.getPlayer().getInventory(MapleInventoryType.USE).getNumFreeSlot() < 1) {
					c.getPlayer().dropMessage(5, "Please make some space.");
					break;
				}
				if (c.getPlayer().getInventory(MapleInventoryType.USE).countById(2430112) >= 25) {
					if (MapleInventoryManipulator.checkSpace(c, 2049400, 1, "") && MapleInventoryManipulator
							.removeById(c, MapleInventoryType.USE, toUse.getItemId(), 25, true, false)) {
						MapleInventoryManipulator.addById(c, 2049400, (short) 1,
								"Scripted item: " + toUse.getItemId() + " on " + FileoutputUtil.CurrentReadable_Date());
						break;
					}
					c.getPlayer().dropMessage(5, "Please make some space.");
					break;
				} else {
					if (c.getPlayer().getInventory(MapleInventoryType.USE).countById(2430112) < 10) {
						c.getPlayer().dropMessage(5,
								"There needs to be 10 Fragments for a Potential Scroll, 25 for Advanced Potential Scroll.");
						break;
					}
					if (MapleInventoryManipulator.checkSpace(c, 2049400, 1, "") && MapleInventoryManipulator
							.removeById(c, MapleInventoryType.USE, toUse.getItemId(), 10, true, false)) {
						MapleInventoryManipulator.addById(c, 2049401, (short) 1,
								"Scripted item: " + toUse.getItemId() + " on " + FileoutputUtil.CurrentReadable_Date());
						break;
					}
					c.getPlayer().dropMessage(5, "Please make some space.");
					break;
				}
			}
			case 2430481: {
				if (c.getPlayer().getInventory(MapleInventoryType.USE).getNumFreeSlot() < 1) {
					c.getPlayer().dropMessage(5, "Please make some space.");
					break;
				}
				if (c.getPlayer().getInventory(MapleInventoryType.USE).countById(2430481) >= 30) {
					if (MapleInventoryManipulator.checkSpace(c, 2049701, 1, "") && MapleInventoryManipulator
							.removeById(c, MapleInventoryType.USE, toUse.getItemId(), 30, true, false)) {
						MapleInventoryManipulator.addById(c, 2049701, (short) 1,
								"Scripted item: " + toUse.getItemId() + " on " + FileoutputUtil.CurrentReadable_Date());
						break;
					}
					c.getPlayer().dropMessage(5, "Please make some space.");
					break;
				} else {
					if (c.getPlayer().getInventory(MapleInventoryType.USE).countById(2430481) < 20) {
						c.getPlayer().dropMessage(5,
								"There needs to be 20 Fragments for a Advanced Equip Enhancement Scroll, 30 for Epic Potential Scroll 80%.");
						break;
					}
					if (MapleInventoryManipulator.checkSpace(c, 2049300, 1, "") && MapleInventoryManipulator
							.removeById(c, MapleInventoryType.USE, toUse.getItemId(), 20, true, false)) {
						MapleInventoryManipulator.addById(c, 2049300, (short) 1,
								"Scripted item: " + toUse.getItemId() + " on " + FileoutputUtil.CurrentReadable_Date());
						break;
					}
					c.getPlayer().dropMessage(5, "Please make some space.");
					break;
				}
			}
			case 2430691: {
				if (c.getPlayer().getInventory(MapleInventoryType.CASH).getNumFreeSlot() < 1) {
					c.getPlayer().dropMessage(5, "Please make some space.");
					break;
				}
				if (c.getPlayer().getInventory(MapleInventoryType.USE).countById(2430691) < 10) {
					c.getPlayer().dropMessage(5, "There needs to be 10 Fragments for a Nebulite Diffuser.");
					break;
				}
				if (MapleInventoryManipulator.checkSpace(c, 5750001, 1, "") && MapleInventoryManipulator.removeById(c,
						MapleInventoryType.USE, toUse.getItemId(), 10, true, false)) {
					MapleInventoryManipulator.addById(c, 5750001, (short) 1,
							"Scripted item: " + toUse.getItemId() + " on " + FileoutputUtil.CurrentReadable_Date());
					break;
				}
				c.getPlayer().dropMessage(5, "Please make some space.");
				break;
			}
			case 2430748: {
				if (c.getPlayer().getInventory(MapleInventoryType.ETC).getNumFreeSlot() < 1) {
					c.getPlayer().dropMessage(5, "Please make some space.");
					break;
				}
				if (c.getPlayer().getInventory(MapleInventoryType.USE).countById(2430748) < 20) {
					c.getPlayer().dropMessage(5, "There needs to be 20 Fragments for a Premium Fusion Ticket.");
					break;
				}
				if (MapleInventoryManipulator.checkSpace(c, 4420000, 1, "") && MapleInventoryManipulator.removeById(c,
						MapleInventoryType.USE, toUse.getItemId(), 20, true, false)) {
					MapleInventoryManipulator.addById(c, 4420000, (short) 1,
							"Scripted item: " + toUse.getItemId() + " on " + FileoutputUtil.CurrentReadable_Date());
					break;
				}
				c.getPlayer().dropMessage(5, "Please make some space.");
				break;
			}
			case 5680019: {
				final int hair = 32150 + c.getPlayer().getHair() % 10;
				c.getPlayer().setHair(hair);
				c.getPlayer().updateSingleStat(MapleStat.HAIR, hair);
				MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.CASH, slot, (short) 1, false);
				break;
			}
			case 5680020: {
				final int hair = 32160 + c.getPlayer().getHair() % 10;
				c.getPlayer().setHair(hair);
				c.getPlayer().updateSingleStat(MapleStat.HAIR, hair);
				MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.CASH, slot, (short) 1, false);
				break;
			}
			case 3994225: {
				c.getPlayer().dropMessage(5, "Please bring this item to the NPC.");
				break;
			}
			case 2430212: {
				final MapleQuestStatus marr = c.getPlayer().getQuestNAdd(MapleQuest.getInstance(122500));
				if (marr.getCustomData() == null) {
					marr.setCustomData("0");
				}
				final long lastTime = Long.parseLong(marr.getCustomData());
				if (lastTime + 600000L > System.currentTimeMillis()) {
					c.getPlayer().dropMessage(5, "You can only use one energy drink per 10 minutes.");
					break;
				}
				if (c.getPlayer().getFatigue() > 0) {
					MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
					c.getPlayer().setFatigue(c.getPlayer().getFatigue() - 5);
					break;
				}
				break;
			}
			case 2430213: {
				final MapleQuestStatus marr = c.getPlayer().getQuestNAdd(MapleQuest.getInstance(122500));
				if (marr.getCustomData() == null) {
					marr.setCustomData("0");
				}
				final long lastTime = Long.parseLong(marr.getCustomData());
				if (lastTime + 600000L > System.currentTimeMillis()) {
					c.getPlayer().dropMessage(5, "You can only use one energy drink per 10 minutes.");
					break;
				}
				if (c.getPlayer().getFatigue() > 0) {
					MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
					c.getPlayer().setFatigue(c.getPlayer().getFatigue() - 10);
					break;
				}
				break;
			}
			case 2430214:
			case 2430220: {
				if (c.getPlayer().getFatigue() > 0) {
					MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
					c.getPlayer().setFatigue(c.getPlayer().getFatigue() - 30);
					break;
				}
				break;
			}
			case 2430227: {
				if (c.getPlayer().getFatigue() > 0) {
					MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
					c.getPlayer().setFatigue(c.getPlayer().getFatigue() - 50);
					break;
				}
				break;
			}
			case 2430231: {
				final MapleQuestStatus marr = c.getPlayer().getQuestNAdd(MapleQuest.getInstance(122500));
				if (marr.getCustomData() == null) {
					marr.setCustomData("0");
				}
				final long lastTime = Long.parseLong(marr.getCustomData());
				if (lastTime + 600000L > System.currentTimeMillis()) {
					c.getPlayer().dropMessage(5, "You can only use one energy drink per 10 minutes.");
					break;
				}
				if (c.getPlayer().getFatigue() > 0) {
					MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
					c.getPlayer().setFatigue(c.getPlayer().getFatigue() - 40);
					break;
				}
				break;
			}
			case 2430144: {
				final int itemid = Randomizer.nextInt(373) + 2290000;
				if (MapleItemInformationProvider.getInstance().itemExists(itemid)
						&& !MapleItemInformationProvider.getInstance().getName(itemid).contains("Special")
						&& !MapleItemInformationProvider.getInstance().getName(itemid).contains("Event")) {
					MapleInventoryManipulator.addById(c, itemid, (short) 1,
							"Reward item: " + toUse.getItemId() + " on " + FileoutputUtil.CurrentReadable_Date());
					MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
					break;
				}
				break;
			}
			case 2430370: {
				if (MapleInventoryManipulator.checkSpace(c, 2028062, 1, "")) {
					MapleInventoryManipulator.addById(c, 2028062, (short) 1,
							"Reward item: " + toUse.getItemId() + " on " + FileoutputUtil.CurrentReadable_Date());
					MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
					break;
				}
				break;
			}
			case 2430158: {
				if (c.getPlayer().getInventory(MapleInventoryType.ETC).getNumFreeSlot() < 1) {
					c.getPlayer().dropMessage(5, "Please make some space.");
					break;
				}
				if (c.getPlayer().getInventory(MapleInventoryType.ETC).countById(4000630) >= 100) {
					if (MapleInventoryManipulator.checkSpace(c, 4310010, 1, "") && MapleInventoryManipulator
							.removeById(c, MapleInventoryType.USE, toUse.getItemId(), 1, true, false)) {
						MapleInventoryManipulator.addById(c, 4310010, (short) 1,
								"Scripted item: " + toUse.getItemId() + " on " + FileoutputUtil.CurrentReadable_Date());
						break;
					}
					c.getPlayer().dropMessage(5, "Please make some space.");
					break;
				} else {
					if (c.getPlayer().getInventory(MapleInventoryType.ETC).countById(4000630) < 50) {
						c.getPlayer().dropMessage(5,
								"There needs to be 50 Purification Totems for a Noble Lion King Medal, 100 for Royal Lion King Medal.");
						break;
					}
					if (MapleInventoryManipulator.checkSpace(c, 4310009, 1, "") && MapleInventoryManipulator
							.removeById(c, MapleInventoryType.USE, toUse.getItemId(), 1, true, false)) {
						MapleInventoryManipulator.addById(c, 4310009, (short) 1,
								"Scripted item: " + toUse.getItemId() + " on " + FileoutputUtil.CurrentReadable_Date());
						break;
					}
					c.getPlayer().dropMessage(5, "Please make some space.");
					break;
				}
			}
			case 2430159: {
				MapleQuest.getInstance(3182).forceComplete(c.getPlayer(), 2161004);
				MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
				break;
			}
			case 2430200: {
				if (c.getPlayer().getQuestStatus(31152) != 2) {
					c.getPlayer().dropMessage(5, "You have no idea how to use it.");
					break;
				}
				if (c.getPlayer().getInventory(MapleInventoryType.ETC).getNumFreeSlot() < 1) {
					c.getPlayer().dropMessage(5, "Please make some space.");
					break;
				}
				if (c.getPlayer().getInventory(MapleInventoryType.ETC).countById(4000660) < 1
						|| c.getPlayer().getInventory(MapleInventoryType.ETC).countById(4000661) < 1
						|| c.getPlayer().getInventory(MapleInventoryType.ETC).countById(4000662) < 1
						|| c.getPlayer().getInventory(MapleInventoryType.ETC).countById(4000663) < 1) {
					c.getPlayer().dropMessage(5, "There needs to be 1 of each Stone for a Dream Key.");
					break;
				}
				if (MapleInventoryManipulator.checkSpace(c, 4032923, 1, "")
						&& MapleInventoryManipulator.removeById(c, MapleInventoryType.USE, toUse.getItemId(), 1, true,
								false)
						&& MapleInventoryManipulator.removeById(c, MapleInventoryType.ETC, 4000660, 1, true, false)
						&& MapleInventoryManipulator.removeById(c, MapleInventoryType.ETC, 4000661, 1, true, false)
						&& MapleInventoryManipulator.removeById(c, MapleInventoryType.ETC, 4000662, 1, true, false)
						&& MapleInventoryManipulator.removeById(c, MapleInventoryType.ETC, 4000663, 1, true, false)) {
					MapleInventoryManipulator.addById(c, 4032923, (short) 1,
							"Scripted item: " + toUse.getItemId() + " on " + FileoutputUtil.CurrentReadable_Date());
					break;
				}
				c.getPlayer().dropMessage(5, "Please make some space.");
				break;
			}
			case 2430130:
			case 2430131: {
				if (GameConstants.isResist(c.getPlayer().getJob())) {
					MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
					c.getPlayer().gainExp(20000 + c.getPlayer().getLevel() * 50 * c.getChannelServer().getExpRate(),
							true, true, false);
					break;
				}
				c.getPlayer().dropMessage(5, "You may not use this item.");
				break;
			}
			case 2430132:
			case 2430133:
			case 2430134:
			case 2430142: {
				if (c.getPlayer().getInventory(MapleInventoryType.EQUIP).getNumFreeSlot() < 1) {
					c.getPlayer().dropMessage(5, "Make some space.");
					break;
				}
				if (c.getPlayer().getJob() == 3200 || c.getPlayer().getJob() == 3210 || c.getPlayer().getJob() == 3211
						|| c.getPlayer().getJob() == 3212) {
					MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
					MapleInventoryManipulator.addById(c, 1382101, (short) 1,
							"Scripted item: " + itemId + " on " + FileoutputUtil.CurrentReadable_Date());
					break;
				}
				if (c.getPlayer().getJob() == 3300 || c.getPlayer().getJob() == 3310 || c.getPlayer().getJob() == 3311
						|| c.getPlayer().getJob() == 3312) {
					MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
					MapleInventoryManipulator.addById(c, 1462093, (short) 1,
							"Scripted item: " + itemId + " on " + FileoutputUtil.CurrentReadable_Date());
					break;
				}
				if (c.getPlayer().getJob() == 3500 || c.getPlayer().getJob() == 3510 || c.getPlayer().getJob() == 3511
						|| c.getPlayer().getJob() == 3512) {
					MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
					MapleInventoryManipulator.addById(c, 1492080, (short) 1,
							"Scripted item: " + itemId + " on " + FileoutputUtil.CurrentReadable_Date());
					break;
				}
				c.getPlayer().dropMessage(5, "You may not use this item.");
				break;
			}
			case 2430036: {
				mountid = 1027;
				expiration_days = 1L;
				break;
			}
			case 2430053: {
				mountid = 1027;
				expiration_days = 30L;
				break;
			}
			case 2430037: {
				mountid = 1028;
				expiration_days = 1L;
				break;
			}
			case 2430054: {
				mountid = 1028;
				expiration_days = 30L;
				break;
			}
			case 2430038: {
				mountid = 1029;
				expiration_days = 1L;
				break;
			}
			case 2430257: {
				mountid = 1029;
				expiration_days = 7L;
				break;
			}
			case 2430055: {
				mountid = 1029;
				expiration_days = 30L;
				break;
			}
			case 2430039: {
				mountid = 1030;
				expiration_days = 1L;
				break;
			}
			case 2430040: {
				mountid = 1031;
				expiration_days = 1L;
				break;
			}
			case 2430259: {
				mountid = 1031;
				expiration_days = 3L;
				break;
			}
			case 2430225: {
				mountid = 1031;
				expiration_days = 10L;
				break;
			}
			case 2430242: {
				mountid = 1063;
				expiration_days = 10L;
				break;
			}
			case 2430261: {
				mountid = 1064;
				expiration_days = 3L;
				break;
			}
			case 2430243: {
				mountid = 1064;
				expiration_days = 10L;
				break;
			}
			case 2430249: {
				mountid = 80001027;
				expiration_days = 3L;
				break;
			}
			case 2430056: {
				mountid = 1035;
				expiration_days = 30L;
				break;
			}
			case 2430057: {
				mountid = 1033;
				expiration_days = 30L;
				break;
			}
			case 2430072: {
				mountid = 1034;
				expiration_days = 7L;
				break;
			}
			case 2430272: {
				mountid = 80001032;
				expiration_days = 3L;
				break;
			}
			case 2430275: {
				mountid = 80001033;
				expiration_days = 7L;
				break;
			}
			case 2430075: {
				mountid = 1038;
				expiration_days = 15L;
				break;
			}
			case 2430076: {
				mountid = 1039;
				expiration_days = 15L;
				break;
			}
			case 2430077: {
				mountid = 1040;
				expiration_days = 15L;
				break;
			}
			case 2430080: {
				mountid = 1042;
				expiration_days = 20L;
				break;
			}
			case 2430082: {
				mountid = 1044;
				expiration_days = 7L;
				break;
			}
			case 2430260: {
				mountid = 1044;
				expiration_days = 3L;
				break;
			}
			case 2430091: {
				mountid = 1049;
				expiration_days = 10L;
				break;
			}
			case 2430092: {
				mountid = 1050;
				expiration_days = 10L;
				break;
			}
			case 2430263: {
				mountid = 1050;
				expiration_days = 3L;
				break;
			}
			case 2430093: {
				mountid = 1051;
				expiration_days = 10L;
				break;
			}
			case 2430101: {
				mountid = 1052;
				expiration_days = 10L;
				break;
			}
			case 2430102: {
				mountid = 1053;
				expiration_days = 10L;
				break;
			}
			case 2430103: {
				mountid = 1054;
				expiration_days = 30L;
				break;
			}
			case 2430266: {
				mountid = 1054;
				expiration_days = 3L;
				break;
			}
			case 2430265: {
				mountid = 1151;
				expiration_days = 3L;
				break;
			}
			case 2430258: {
				mountid = 1115;
				expiration_days = 365L;
				break;
			}
			case 2430117: {
				mountid = 1036;
				expiration_days = 365L;
				break;
			}
			case 2430118: {
				mountid = 1039;
				expiration_days = 365L;
				break;
			}
			case 2430119: {
				mountid = 1040;
				expiration_days = 365L;
				break;
			}
			case 2430120: {
				mountid = 1037;
				expiration_days = 365L;
				break;
			}
			case 2430271: {
				mountid = 80001191;
				expiration_days = 3L;
				break;
			}
			case 2430149: {
				mountid = 1072;
				expiration_days = 30L;
				break;
			}
			case 2430262: {
				mountid = 1072;
				expiration_days = 3L;
				break;
			}
			case 2430264: {
				mountid = 1019;
				expiration_days = 3L;
				break;
			}
			case 2430179: {
				mountid = 80001026;
				expiration_days = 15L;
				break;
			}
			case 2430283: {
				mountid = 1025;
				expiration_days = 10L;
				break;
			}
			case 2430313: {
				mountid = 1156;
				expiration_days = -1L;
				break;
			}
			case 2430317: {
				mountid = 1121;
				expiration_days = -1L;
				break;
			}
			case 2430319: {
				mountid = 1122;
				expiration_days = -1L;
				break;
			}
			case 2430321: {
				mountid = 1123;
				expiration_days = -1L;
				break;
			}
			case 2430323: {
				mountid = 1124;
				expiration_days = -1L;
				break;
			}
			case 2430325: {
				mountid = 1129;
				expiration_days = -1L;
				break;
			}
			case 2430327: {
				mountid = 1130;
				expiration_days = -1L;
				break;
			}
			case 2430329: {
				mountid = 1063;
				expiration_days = -1L;
				break;
			}
			case 2430331: {
				mountid = 1025;
				expiration_days = -1L;
				break;
			}
			case 2430333: {
				mountid = 1034;
				expiration_days = -1L;
				break;
			}
			case 2430335: {
				mountid = 1136;
				expiration_days = -1L;
				break;
			}
			case 2430337: {
				mountid = 1051;
				expiration_days = -1L;
				break;
			}
			case 2430339: {
				mountid = 1138;
				expiration_days = -1L;
				break;
			}
			case 2430341: {
				mountid = 1139;
				expiration_days = -1L;
				break;
			}
			case 2430343: {
				mountid = 1027;
				expiration_days = -1L;
				break;
			}
			case 2430346: {
				mountid = 1029;
				expiration_days = -1L;
				break;
			}
			case 2430348: {
				mountid = 1028;
				expiration_days = -1L;
				break;
			}
			case 2430350: {
				mountid = 1033;
				expiration_days = -1L;
				break;
			}
			case 2430352: {
				mountid = 1064;
				expiration_days = -1L;
				break;
			}
			case 2430354: {
				mountid = 1096;
				expiration_days = -1L;
				break;
			}
			case 2430356: {
				mountid = 1101;
				expiration_days = -1L;
				break;
			}
			case 2430358: {
				mountid = 1102;
				expiration_days = -1L;
				break;
			}
			case 2430360: {
				mountid = 1054;
				expiration_days = -1L;
				break;
			}
			case 2430362: {
				mountid = 1053;
				expiration_days = -1L;
				break;
			}
			case 2430292: {
				mountid = 1145;
				expiration_days = 90L;
				break;
			}
			case 2430294: {
				mountid = 1146;
				expiration_days = 90L;
				break;
			}
			case 2430296: {
				mountid = 1147;
				expiration_days = 90L;
				break;
			}
			case 2430298: {
				mountid = 1148;
				expiration_days = 90L;
				break;
			}
			case 2430300: {
				mountid = 1149;
				expiration_days = 90L;
				break;
			}
			case 2430302: {
				mountid = 1150;
				expiration_days = 90L;
				break;
			}
			case 2430304: {
				mountid = 1151;
				expiration_days = 90L;
				break;
			}
			case 2430306: {
				mountid = 1152;
				expiration_days = 90L;
				break;
			}
			case 2430308: {
				mountid = 1153;
				expiration_days = 90L;
				break;
			}
			case 2430310: {
				mountid = 1154;
				expiration_days = 90L;
				break;
			}
			case 2430312: {
				mountid = 1156;
				expiration_days = 90L;
				break;
			}
			case 2430314: {
				mountid = 1156;
				expiration_days = 90L;
				break;
			}
			case 2430316: {
				mountid = 1118;
				expiration_days = 90L;
				break;
			}
			case 2430318: {
				mountid = 1121;
				expiration_days = 90L;
				break;
			}
			case 2430320: {
				mountid = 1122;
				expiration_days = 90L;
				break;
			}
			case 2430322: {
				mountid = 1123;
				expiration_days = 90L;
				break;
			}
			case 2430326: {
				mountid = 1129;
				expiration_days = 90L;
				break;
			}
			case 2430328: {
				mountid = 1130;
				expiration_days = 90L;
				break;
			}
			case 2430330: {
				mountid = 1063;
				expiration_days = 90L;
				break;
			}
			case 2430332: {
				mountid = 1025;
				expiration_days = 90L;
				break;
			}
			case 2430334: {
				mountid = 1034;
				expiration_days = 90L;
				break;
			}
			case 2430336: {
				mountid = 1136;
				expiration_days = 90L;
				break;
			}
			case 2430338: {
				mountid = 1051;
				expiration_days = 90L;
				break;
			}
			case 2430340: {
				mountid = 1138;
				expiration_days = 90L;
				break;
			}
			case 2430342: {
				mountid = 1139;
				expiration_days = 90L;
				break;
			}
			case 2430344: {
				mountid = 1027;
				expiration_days = 90L;
				break;
			}
			case 2430347: {
				mountid = 1029;
				expiration_days = 90L;
				break;
			}
			case 2430349: {
				mountid = 1028;
				expiration_days = 90L;
				break;
			}
			case 2430369: {
				mountid = 1049;
				expiration_days = 10L;
				break;
			}
			case 2430392: {
				mountid = 80001038;
				expiration_days = 90L;
				break;
			}
			case 2430232: {
				mountid = 1106;
				expiration_days = 10L;
				break;
			}
			case 2430206: {
				mountid = 1033;
				expiration_days = 7L;
				break;
			}
			case 2430211: {
				mountid = 80001009;
				expiration_days = 30L;
				break;
			}
			case 2430934: {
				mountid = 1042;
				expiration_days = 0L;
				break;
			}
			case 2430458: {
				mountid = 80001044;
				expiration_days = 7L;
				break;
			}
			case 2430521: {
				mountid = 80001044;
				expiration_days = 30L;
				break;
			}
			case 2430506: {
				mountid = 80001082;
				expiration_days = 30L;
				break;
			}
			case 2430507: {
				mountid = 80001083;
				expiration_days = 30L;
				break;
			}
			case 2430508: {
				mountid = 80001175;
				expiration_days = 30L;
				break;
			}
			case 2430518: {
				mountid = 80001090;
				expiration_days = 30L;
				break;
			}
			case 2430908: {
				mountid = 80001175;
				expiration_days = 30L;
				break;
			}
			case 2430927: {
				mountid = 80001183;
				expiration_days = 30L;
				break;
			}
			case 2430727: {
				mountid = 80001148;
				expiration_days = 30L;
				break;
			}
			case 2430938: {
				mountid = 80001148;
				expiration_days = 0L;
				break;
			}
			case 2430937: {
				mountid = 80001193;
				expiration_days = 0L;
				break;
			}
			case 2430939: {
				mountid = 80001195;
				expiration_days = 0L;
				break;
			}
			case 2434290: {
				chr.gainHonor(10000);
				chr.removeItem(2434290, -1);
				break;
			}
			case 2434287: {
				chr.gainHonor(-10000);
				chr.gainItem(2432970, 1);
				chr.removeItem(2434287, -1);
				break;
			}
			case 2434813: {
				chr.gainItem(4001852, 1);
				chr.removeItem(2434813, -1);
				break;
			}
			case 2434814: {
				chr.gainItem(4001853, 1);
				chr.removeItem(2434814, -1);
				break;
			}
			case 2434815: {
				chr.gainItem(4001854, 1);
				chr.removeItem(2434815, -1);
				break;
			}
			case 2434816: {
				chr.gainItem(4001862, 1);
				chr.removeItem(2434816, -1);
				break;
			}
			case 2435122:
			case 2435513:
			case 2436784:
			case 2439631: {
				if (!GameConstants.isZero(chr.getJob())) {
					NPCScriptManager.getInstance().startItem(c, 9010000, "consume_" + toUse.getItemId());
					break;
				}
				chr.dropMessage(5, "제로 직업군은 데미지 스킨을 사용해도 아무 효과도 얻을 수 없다.");
				break;
			}
			case 2432636: {
				if (!GameConstants.isZero(chr.getJob())) {
					NPCScriptManager.getInstance().startItem(c, 9010000, "consume_2411020");
					break;
				}
				chr.dropMessage(5, "제로 직업군은 데미지 스킨을 사용해도 아무 효과도 얻을 수 없다.");
				break;
			}
			case 2430469: {
				chr.gainItem(1122017, (short) 1, false, System.currentTimeMillis() + 604800000L, "정령의 펜던트");
				chr.removeItem(toUse.getItemId(), -1);
				break;
			}
			// case 2432970:
			case 2433808:
			case 2434288:
			case 2631913:
			case 2434021: { // 스페셜 명예의 훈장
				chr.gainHonor(10000);
				chr.removeItem(toUse.getItemId(), -1);
				break;
			}
			case 2431174: {
				chr.gainHonor(Randomizer.rand(1, 50));
				chr.removeItem(toUse.getItemId(), -1);
				break;
			}
			default: {
				NPCScriptManager.getInstance().startItem(c, 9010060, "consume_" + toUse.getItemId());
				break;
			}
			}
			if (GameConstants.getDSkinNum(toUse.getItemId()) != -1) {
				final MapleQuest quest = MapleQuest.getInstance(7291);
				final MapleQuestStatus queststatus = new MapleQuestStatus(quest, 1);
				final int skinnum = GameConstants.getDSkinNum(toUse.getItemId());
				final String skinString = String.valueOf(skinnum);
				queststatus.setCustomData((skinString == null) ? "0" : skinString);
				chr.updateQuest(queststatus, true);

				chr.setKeyValue(7293, "damage_skin", skinnum + "");
				chr.dropMessage(5, "데미지 스킨이 변경되었습니다.");
				chr.getClient().getSession().writeAndFlush(CField.damageSkin391((byte) 4, chr));
				chr.getMap().broadcastMessage(CField.showForeignDamageSkin(chr, skinnum));
				MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
			}
			if (GameConstants.getRidingNum(toUse.getItemId()) != -1) {
				final int skinnum2 = GameConstants.getRidingNum(toUse.getItemId());
				chr.changeSkillLevel(skinnum2, (byte) 1, (byte) 1);
				chr.dropMessage(5,
						MapleItemInformationProvider.getInstance().getName(GameConstants.getRidingItemIdbyNum(skinnum2))
								+ "(\uc774)\uac00 \ub4f1\ub85d \ub418\uc5c8\uc2b5\ub2c8\ub2e4.");
				MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
			} else if (GameConstants.getMountItemEx(toUse.getItemId()) != 0) {
				final int skillid = GameConstants.getMountItemEx(toUse.getItemId());
				chr.changeSkillLevel(skillid, (byte) 1, (byte) 1);
				chr.dropMessage(-1, MapleItemInformationProvider.getInstance().getName(skillid)
						+ "\uc744(\ub97c) \ud68d\ub4dd \ud558\uc600\uc2b5\ub2c8\ub2e4!!");
				MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
			}
		}
		if (mountid > 0) {
			c.getPlayer().getStat();
			mountid = PlayerStats.getSkillByJob(mountid, c.getPlayer().getJob());
			final int fk = GameConstants.getMountItem(mountid, c.getPlayer());
			if (fk > 0 && mountid < 80001000) {
				for (int j = 80001001; j < 80001999; ++j) {
					final Skill skill = SkillFactory.getSkill(j);
					if (skill != null && GameConstants.getMountItem(skill.getId(), c.getPlayer()) == fk) {
						mountid = j;
						break;
					}
				}
			}
			if (c.getPlayer().getSkillLevel(mountid) > 0) {
				c.getPlayer().dropMessage(5, "이미 해당 라이딩스킬이 있습니다.");
			} else if (SkillFactory.getSkill(mountid) == null) {
				c.getPlayer().dropMessage(5, "해당스킬은 얻으실 수 없습니다.");
			} else if (expiration_days > 0L) {
				MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
				c.getPlayer().changeSingleSkillLevel(SkillFactory.getSkill(mountid), 1, (byte) 1,
						System.currentTimeMillis() + expiration_days * 24L * 60L * 60L * 1000L);
				c.getPlayer().dropMessage(-1, "[" + SkillFactory.getSkillName(mountid) + "] 스킬을 얻었습니다.");
			} else if (expiration_days == 0L) {
				MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
				c.getPlayer().changeSkillLevel(SkillFactory.getSkill(mountid), (byte) 1, (byte) 1);
				c.getPlayer().dropMessage(-1, "[" + SkillFactory.getSkillName(mountid) + "] 스킬을 얻었습니다.");
			}
		}
		c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
	}

	public static final void UseSummonBag(final LittleEndianAccessor slea, final MapleClient c,
			final MapleCharacter chr) {
		if (!chr.isAlive() || chr.inPVP()) {
			c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
			return;
		}
		slea.readInt();
		final short slot = slea.readShort();
		final int itemId = slea.readInt();
		final Item toUse = chr.getInventory(MapleInventoryType.USE).getItem(slot);
		if (toUse != null && toUse.getQuantity() >= 1 && toUse.getItemId() == itemId
				&& (c.getPlayer().getMapId() < 910000000 || c.getPlayer().getMapId() > 910000022)) {
			final Map<String, Integer> toSpawn = MapleItemInformationProvider.getInstance().getEquipStats(itemId);
			if (toSpawn == null) {
				c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
				return;
			}
			MapleMonster ht = null;
			final int type = 0;
			for (final Map.Entry<String, Integer> i : toSpawn.entrySet()) {
				if (i.getKey().startsWith("mob") && Randomizer.nextInt(99) <= i.getValue()) {
					ht = MapleLifeFactory.getMonster(Integer.parseInt(i.getKey().substring(3)));
					chr.getMap().spawnMonster_sSack(ht, chr.getPosition(), type);
				}
			}
			if (ht == null) {
				c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
				return;
			}
			MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
		}
		c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
	}

	public static final void UseCashItem(LittleEndianAccessor slea, MapleClient c) {
		int unk;
		short questid;
		Map<MapleStat, Long> statupdate;
		int skill1;
		List<InnerSkillValueHolder> newValues;
		int j;
		Item item3;
		int mapid;
		Item item2;
		Equip equip2;
		Item item1;
		boolean up;
		int i;
		MapleItemInformationProvider mapleItemInformationProvider2;
		int pos;
		MapleItemInformationProvider mapleItemInformationProvider1;
		int[] forbiddenFaces;
		MapleInventoryType mapleInventoryType2;
		int[] wonderblack;
		MapleInventoryType mapleInventoryType1;
		Equip equip1;
		int[] SLabel;
		MapleInventoryType type;
		Equip toScroll;
		Item item;
		String message;
		int tvType;
		long uniqueId, l1;
		Skill skil;
		long uniqueid;
		String nName;
		MapleItemInformationProvider ii;
		short viewSlot;
		int code, npcid, apto, skill2;
		InnerSkillValueHolder ivholder;
		String effect;
		MapleItemInformationProvider mapleItemInformationProvider4;
		MapleMap target;
		MapleItemInformationProvider mapleItemInformationProvider3;
		short s3;
		int n;
		Item item8;
		int m;
		Item item7;
		int k, arrayOfInt2[];
		boolean bool1;
		int[] arrayOfInt1;
		boolean dressUp;
		Item item6;
		int[] luna;
		Item item5;
		int[] 모자;
		Item item4;
		short s2, s1, dst;
		StringBuilder sb;
		boolean ear;
		long toAdd;
		String[] arrayOfString;
		short descSlot;
		MapleQuest quest;
		int apfrom;
		Skill skillSPTo;
		InnerSkillValueHolder ivholder2;
		int days;
		Item item10;
		boolean bool4;
		Item item9;
		int i2;
		boolean bool3;
		int i1;
		boolean isBeta;
		short baseslot;
		int 한벌남[], flag;
		boolean bool2;
		MapleCharacter victim;
		MaplePet maplePet1;
		int color;
		MaplePet pet;
		Equip view_Item;
		Skill skillSPFrom;
		boolean bool5, isAlphaBeta;
		short usingslot;
		int[] 한벌여;
		Item item11;
		String str1;
		int petIndex;
		MaplePet maplePet2;
		long expire;
		int slo;
		Equip desc_Item;
		PlayerStats playerst;
		int ordinaryColor, baseFace;
		Item baseitem;
		int[] 신발;
		MaplePet.PetFlag zz;
		int i3;
		String str2;
		int addColor, i4;
		Item usingitem;
		int 무기[], i6, i5, basegrade, 망토장갑[], newFace;
		List<Pair<Integer, Integer>> random;
		MapleItemInformationProvider mapleItemInformationProvider5;
		boolean event;
		int baseitemid;
		boolean sp;
		short baseitempos;
		MaplePet maplePet3;
		int useitemid;
		Item item12;
		Equip equip3;
		short useitempos;
		int i7;
		Equip equip4;
		int itemidselect;
		Equip equip5;
		int 마라벨나올확률, MItemidselect, Label, own, two, three;
		boolean 마라벨인가;
		if (c.getPlayer() == null || c.getPlayer().getMap() == null || c.getPlayer().inPVP()) {
			c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
			return;
		}
		slea.readInt();
		c.getPlayer().setScrolledPosition((short) 0);

		short slot = slea.readShort();

		int itemId = slea.readInt();

		if (itemId == 5150190) {
			if (!GameConstants.isAngelicBuster(c.getPlayer().getJob())) {
				slea.readByte();
				slea.readByte();
				int select_hair = slea.readInt();
				c.getPlayer().setHair(select_hair);
				c.getPlayer().updateSingleStat(MapleStat.HAIR, select_hair);
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
			} else {
				slea.readByte();
				slea.readByte();
				int select_hair = slea.readInt();
				c.getPlayer().엔젤릭버스터임시 = select_hair;

				c.removeClickedNPC();
				NPCScriptManager.getInstance().dispose(c);
				c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 2007, "hairchoice");

				c.getPlayer().dropMessageGM(6, "선택된 헤어 : " + select_hair);
				// c.getPlayer().setHair(select_hair);
				// c.getPlayer().updateSingleStat(MapleStat.HAIR, select_hair);
				// c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
			}
			return;
		} else if (itemId == 5152259) {
			if (!GameConstants.isAngelicBuster(c.getPlayer().getJob())) {
				slea.readByte();
				slea.readByte();
				int select_face = slea.readInt();
				c.getPlayer().setFace(select_face);
				c.getPlayer().updateSingleStat(MapleStat.FACE, select_face);
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
			} else {
				slea.readByte();
				slea.readByte();
				int select_face = slea.readInt();
				c.getPlayer().엔젤릭버스터임시 = select_face;

				c.removeClickedNPC();
				NPCScriptManager.getInstance().dispose(c);
				c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 2007, "hairchoice");

				c.getPlayer().dropMessageGM(6, "선택된 헤어 : " + select_face);
				// c.getPlayer().setHair(select_hair);
				// c.getPlayer().updateSingleStat(MapleStat.HAIR, select_hair);
				// c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
			}
			return;
		} else if (itemId == 5151036) {
			if (!GameConstants.isAngelicBuster(c.getPlayer().getJob())) {
				slea.readByte();
				slea.readByte();
				int select_haircolor = slea.readInt();
				int haircolor = (c.getPlayer().getHair() - (c.getPlayer().getHair() % 10)) + select_haircolor;
				c.getPlayer().setHair(haircolor);
				c.getPlayer().updateSingleStat(MapleStat.HAIR, haircolor);
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
			} else {
				slea.readByte();
				slea.readByte();
				int select_haircolor = slea.readInt();
				int haircolor = (c.getPlayer().getHair() - (c.getPlayer().getHair() % 10)) + select_haircolor;
				c.getPlayer().엔젤릭버스터임시 = haircolor;
				c.removeClickedNPC();
				NPCScriptManager.getInstance().dispose(c);
				c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 2007, "hairchoice");
			}
			return;
		} else if (itemId == 5152111) {
			if (!GameConstants.isAngelicBuster(c.getPlayer().getJob())) {

				slea.readByte();
				slea.readByte();
				int select_facecolor = slea.readInt();

				int facecolor = 0;
				facecolor = (c.getPlayer().getFace() - c.getPlayer().getFace() % 1000) + c.getPlayer().getFace() % 100
						+ 100 * select_facecolor;
				c.getPlayer().setFace(facecolor);
				c.getPlayer().updateSingleStat(MapleStat.FACE, facecolor);
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
			} else {
				slea.readByte();
				slea.readByte();
				int select_facecolor = slea.readInt();
				int facecolor = 0;
				facecolor = (c.getPlayer().getFace() - c.getPlayer().getFace() % 1000) + c.getPlayer().getFace() % 100
						+ 100 * select_facecolor;
				c.getPlayer().엔젤릭버스터임시 = facecolor;
				c.removeClickedNPC();
				NPCScriptManager.getInstance().dispose(c);
				c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 2007, "hairchoice");
			}
			return;
		} else if (itemId == 5153000) {
			if (!GameConstants.isAngelicBuster(c.getPlayer().getJob())) {

				slea.readByte();
				slea.readByte();
				int select_skincolor = slea.readInt();

				c.getPlayer().setSkinColor((byte) select_skincolor);
				c.getPlayer().updateSingleStat(MapleStat.SKIN, select_skincolor);
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
			} else {
				slea.readByte();
				slea.readByte();
				int select_skincolor = slea.readInt();
				c.getPlayer().엔젤릭버스터임시 = select_skincolor;
				c.removeClickedNPC();
				NPCScriptManager.getInstance().dispose(c);
				c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
				NPCScriptManager.getInstance().start(c, 2007, "hairchoice");
			}
			return;
		}

		if (itemId == 5064000 || itemId == 5064100 || itemId == 5064300 || itemId == 5064400) {
			slea.readShort();
		}

		Item toUse = c.getPlayer().getInventory(MapleInventoryType.CASH).getItem(slot);

		if ((toUse == null || toUse.getItemId() != itemId || toUse.getQuantity() < 1) && itemId != 5153015
				&& itemId != 5150132 && itemId != 5152020) {
			c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
			return;
		}

		boolean used = false, cc = false;
		MapleItemInformationProvider ii3 = MapleItemInformationProvider.getInstance();

		if (itemId != 5062009 && itemId != 5062010 && itemId != 5062500 && itemId != 5153015 && itemId != 5150132
				&& itemId != 5152020) {
			FileoutputUtil.log(FileoutputUtil.物品使用日誌,
					"[캐시 아이템 사용] 계정 아이디 : " + c.getAccID() + " | " + c.getPlayer().getName() + "이 "
							+ ii3.getName(toUse.getItemId()) + "(" + toUse.getItemId() + ")를 사용함.");
		}

		switch (itemId) {
		case 5150132:
		case 5152020:
		case 5153015:
			// unk = slea.readInt();
			slea.skip(2);
			code = slea.readInt();
			if (itemId == 5150132) {
				if (c.getPlayer().getDressup()
						|| (GameConstants.isZero(c.getPlayer().getJob()) && c.getPlayer().getGender() == 1)) {
					c.getPlayer().setSecondHair(code);
					if (c.getPlayer().getDressup()) {
						c.getPlayer().updateAngelicStats();
					} else {
						c.send(CWvsContext.updateZeroSecondStats(c.getPlayer()));
					}
				} else {
					c.getPlayer().setHair(code);
					c.getPlayer().updateSingleStat(MapleStat.HAIR, code);
				}
				c.getPlayer().equipChanged();
			} else if (itemId == 5153015) {
				if (c.getPlayer().getDressup()
						|| (GameConstants.isZero(c.getPlayer().getJob()) && c.getPlayer().getGender() == 1)) {
					c.getPlayer().setSecondSkinColor((byte) code);
					if (c.getPlayer().getDressup()) {
						c.getPlayer().updateAngelicStats();
					} else {
						c.send(CWvsContext.updateZeroSecondStats(c.getPlayer()));
					}
				} else {
					c.getPlayer().setSkinColor((byte) code);
					c.getPlayer().updateSingleStat(MapleStat.SKIN, code);
				}
				c.getPlayer().equipChanged();
			} else if (itemId == 5152020) {
				if (c.getPlayer().getDressup()
						|| (GameConstants.isZero(c.getPlayer().getJob()) && c.getPlayer().getGender() == 1)) {
					c.getPlayer().setSecondFace(code);
					if (c.getPlayer().getDressup()) {
						c.getPlayer().updateAngelicStats();
					} else {
						c.send(CWvsContext.updateZeroSecondStats(c.getPlayer()));
					}
				} else {
					c.getPlayer().setFace(code);
					c.getPlayer().updateSingleStat(MapleStat.FACE, code);
				}
				c.getPlayer().equipChanged();
			}
			c.getPlayer().dropMessage(5, "성공적으로 변경 되었습니다.");
			c.send(CWvsContext.enableActions(c.getPlayer()));
			// return;
			break;
		case 5043000:
		case 5043001:
			questid = slea.readShort();
			npcid = slea.readInt();
			quest = MapleQuest.getInstance(questid);
			if (c.getPlayer().getQuest(quest).getStatus() == 1
					&& quest.canComplete(c.getPlayer(), Integer.valueOf(npcid))) {
				int mapId = MapleLifeFactory.getNPCLocation(npcid);
				if (mapId != -1) {
					MapleMap map = c.getChannelServer().getMapFactory().getMap(mapId);
					if (map.containsNPC(npcid) && !FieldLimitType.VipRock.check(c.getPlayer().getMap().getFieldLimit())
							&& !FieldLimitType.VipRock.check(map.getFieldLimit())) {
						c.getPlayer().changeMap(map, map.getPortal(0));
					}
					used = true;
					break;
				}
				c.getPlayer().dropMessage(1, "Unknown error has occurred.");
			}
			break;
		case 2320000:
		case 5040000:
		case 5040001:
		case 5040002:
		case 5040003:
		case 5040004:
		case 5041000:
		case 5041001:
			used = UseTeleRock(slea, c, itemId);
			break;
		case 5450005:
			c.getPlayer().setConversation(4);
			c.getPlayer().getStorage().sendStorage(c, 1022005);
			break;
		case 5050000:
			statupdate = new EnumMap<>(MapleStat.class);
			apto = slea.readInt();
			apfrom = slea.readInt();

			if (apto == apfrom) {
				break;
			}

			int job = c.getPlayer().getJob();
			playerst = c.getPlayer().getStat();

			used = true;

			switch (apto) {
			case 64:
				if (playerst.getStr() >= 999) {
					used = false;
				}
				break;
			case 128:
				if (playerst.getDex() >= 999) {
					used = false;
				}
				break;
			case 256:
				if (playerst.getInt() >= 999) {
					used = false;
				}
				break;
			case 512:
				if (playerst.getLuk() >= 999) {
					used = false;
				}
				break;
			case 2048:
				if (playerst.getMaxHp() >= 500000L) {
					used = false;
				}
				break;
			case 8192:
				if (playerst.getMaxMp() >= 500000L) {
					used = false;
				}
				break;
			}

			switch (apfrom) {
			case 64:
				if (playerst.getStr() <= 4 || (c.getPlayer().getJob() % 1000 / 100 == 1 && playerst.getStr() <= 35)) {
					used = false;
				}
				break;
			case 128:
				if (playerst.getDex() <= 4 || (c.getPlayer().getJob() % 1000 / 100 == 3 && playerst.getDex() <= 25)
						|| (c.getPlayer().getJob() % 1000 / 100 == 4 && playerst.getDex() <= 25)
						|| (c.getPlayer().getJob() % 1000 / 100 == 5 && playerst.getDex() <= 20)) {
					used = false;
				}
				break;
			case 256:
				if (playerst.getInt() <= 4 || (c.getPlayer().getJob() % 1000 / 100 == 2 && playerst.getInt() <= 20)) {
					used = false;
				}
				break;
			case 512:
				if (playerst.getLuk() <= 4) {
					used = false;
				}
				break;
			case 2048:
				if (c.getPlayer().getHpApUsed() <= 0 || c.getPlayer().getHpApUsed() >= 10000) {
					used = false;
					c.getPlayer().dropMessage(1, "You need points in HP or MP in order to take points out.");
				}
				break;
			case 8192:
				if (c.getPlayer().getHpApUsed() <= 0 || c.getPlayer().getHpApUsed() >= 10000) {
					used = false;
					c.getPlayer().dropMessage(1, "You need points in HP or MP in order to take points out.");
				}
				break;
			}
			if (used) {
				int i8;
				long l2;
				int toSet;
				long maxhp, maxmp;
				switch (apto) {
				case 64:
					i8 = playerst.getStr() + 1;
					playerst.setStr((short) i8, c.getPlayer());
					statupdate.put(MapleStat.STR, Long.valueOf(i8));
					break;
				case 128:
					i8 = playerst.getDex() + 1;
					playerst.setDex((short) i8, c.getPlayer());
					statupdate.put(MapleStat.DEX, Long.valueOf(i8));
					break;
				case 256:
					i8 = playerst.getInt() + 1;
					playerst.setInt((short) i8, c.getPlayer());
					statupdate.put(MapleStat.INT, Long.valueOf(i8));
					break;
				case 512:
					i8 = playerst.getLuk() + 1;
					playerst.setLuk((short) i8, c.getPlayer());
					statupdate.put(MapleStat.LUK, Long.valueOf(i8));
					break;
				case 2048:
					l2 = playerst.getMaxHp();
					if (GameConstants.isBeginnerJob(job)) {
						l2 += Randomizer.rand(4, 8);
					} else if ((job >= 100 && job <= 132) || (job >= 3200 && job <= 3212)
							|| (job >= 1100 && job <= 1112) || (job >= 3100 && job <= 3112)) {
						l2 += Randomizer.rand(36, 42);
					} else if ((job >= 200 && job <= 232) || GameConstants.isEvan(job)
							|| (job >= 1200 && job <= 1212)) {
						l2 += Randomizer.rand(10, 12);
					} else if ((job >= 300 && job <= 322) || (job >= 400 && job <= 434) || (job >= 1300 && job <= 1312)
							|| (job >= 1400 && job <= 1412) || (job >= 3300 && job <= 3312)
							|| (job >= 2300 && job <= 2312)) {
						l2 += Randomizer.rand(14, 18);
					} else if ((job >= 510 && job <= 512) || (job >= 1510 && job <= 1512)) {
						l2 += Randomizer.rand(24, 28);
					} else if ((job >= 500 && job <= 532) || (job >= 3500 && job <= 3512) || job == 1500) {
						l2 += Randomizer.rand(16, 20);
					} else if (job >= 2000 && job <= 2112) {
						l2 += Randomizer.rand(34, 38);
					} else {
						l2 += Randomizer.rand(50, 100);
					}
					l2 = Math.min(500000L, Math.abs(l2));
					c.getPlayer().setHpApUsed((short) (c.getPlayer().getHpApUsed() + 1));
					playerst.setMaxHp(l2, c.getPlayer());
					statupdate.put(MapleStat.MAXHP, Long.valueOf(l2));
					break;
				case 8192:
					maxmp = playerst.getMaxMp();
					if (GameConstants.isBeginnerJob(job)) {
						maxmp += Randomizer.rand(6, 8);
					} else {
						if (job >= 3100 && job <= 3112) {
							break;
						}
						if ((job >= 100 && job <= 132) || (job >= 1100 && job <= 1112)
								|| (job >= 2000 && job <= 2112)) {
							maxmp += Randomizer.rand(4, 9);
						} else if ((job >= 200 && job <= 232) || GameConstants.isEvan(job)
								|| (job >= 3200 && job <= 3212) || (job >= 1200 && job <= 1212)) {
							maxmp += Randomizer.rand(32, 36);
						} else if ((job >= 300 && job <= 322) || (job >= 400 && job <= 434)
								|| (job >= 500 && job <= 532) || (job >= 3200 && job <= 3212)
								|| (job >= 3500 && job <= 3512) || (job >= 1300 && job <= 1312)
								|| (job >= 1400 && job <= 1412) || (job >= 1500 && job <= 1512)
								|| (job >= 2300 && job <= 2312)) {
							maxmp += Randomizer.rand(8, 10);
						} else {
							maxmp += Randomizer.rand(50, 100);
						}
					}
					maxmp = Math.min(500000L, Math.abs(maxmp));
					c.getPlayer().setHpApUsed((short) (c.getPlayer().getHpApUsed() + 1));
					playerst.setMaxMp(maxmp, c.getPlayer());
					statupdate.put(MapleStat.MAXMP, Long.valueOf(maxmp));
					break;
				}
				switch (apfrom) {
				case 64:
					toSet = playerst.getStr() - 1;
					playerst.setStr((short) toSet, c.getPlayer());
					statupdate.put(MapleStat.STR, Long.valueOf(toSet));
					break;
				case 128:
					toSet = playerst.getDex() - 1;
					playerst.setDex((short) toSet, c.getPlayer());
					statupdate.put(MapleStat.DEX, Long.valueOf(toSet));
					break;
				case 256:
					toSet = playerst.getInt() - 1;
					playerst.setInt((short) toSet, c.getPlayer());
					statupdate.put(MapleStat.INT, Long.valueOf(toSet));
					break;
				case 512:
					toSet = playerst.getLuk() - 1;
					playerst.setLuk((short) toSet, c.getPlayer());
					statupdate.put(MapleStat.LUK, Long.valueOf(toSet));
					break;
				case 2048:
					maxhp = playerst.getMaxHp();
					if (GameConstants.isBeginnerJob(job)) {
						maxhp -= 12L;
					} else if ((job >= 200 && job <= 232) || (job >= 1200 && job <= 1212)) {
						maxhp -= 10L;
					} else if ((job >= 300 && job <= 322) || (job >= 400 && job <= 434) || (job >= 1300 && job <= 1312)
							|| (job >= 1400 && job <= 1412) || (job >= 3300 && job <= 3312)
							|| (job >= 3500 && job <= 3512) || (job >= 2300 && job <= 2312)) {
						maxhp -= 15L;
					} else if ((job >= 500 && job <= 532) || (job >= 1500 && job <= 1512)) {
						maxhp -= 22L;
					} else if ((job >= 100 && job <= 132) || (job >= 1100 && job <= 1112)
							|| (job >= 3100 && job <= 3112)) {
						maxhp -= 32L;
					} else if ((job >= 2000 && job <= 2112) || (job >= 3200 && job <= 3212)) {
						maxhp -= 40L;
					} else {
						maxhp -= 20L;
					}
					c.getPlayer().setHpApUsed((short) (c.getPlayer().getHpApUsed() - 1));
					playerst.setMaxHp(maxhp, c.getPlayer());
					statupdate.put(MapleStat.MAXHP, Long.valueOf(maxhp));
					break;
				case 8192:
					maxmp = playerst.getMaxMp();
					if (GameConstants.isBeginnerJob(job)) {
						maxmp -= 8L;
					} else {
						if (job >= 3100 && job <= 3112) {
							break;
						}
						if ((job >= 100 && job <= 132) || (job >= 1100 && job <= 1112)) {
							maxmp -= 4L;
						} else if ((job >= 200 && job <= 232) || (job >= 1200 && job <= 1212)) {
							maxmp -= 30L;
						} else if ((job >= 500 && job <= 532) || (job >= 300 && job <= 322)
								|| (job >= 400 && job <= 434) || (job >= 1300 && job <= 1312)
								|| (job >= 1400 && job <= 1412) || (job >= 1500 && job <= 1512)
								|| (job >= 3300 && job <= 3312) || (job >= 3500 && job <= 3512)
								|| (job >= 2300 && job <= 2312)) {
							maxmp -= 10L;
						} else if (job >= 2000 && job <= 2112) {
							maxmp -= 5L;
						} else {
							maxmp -= 20L;
						}
					}
					c.getPlayer().setHpApUsed((short) (c.getPlayer().getHpApUsed() - 1));
					playerst.setMaxMp(maxmp, c.getPlayer());
					statupdate.put(MapleStat.MAXMP, Long.valueOf(maxmp));
					break;
				}
				c.getSession().writeAndFlush(CWvsContext.updatePlayerStats(statupdate, false, c.getPlayer()));
			}
			break;
		case 5050001:
		case 5050002:
		case 5050003:
		case 5050004:
		case 5050005:
		case 5050006:
		case 5050007:
		case 5050008:
		case 5050009:
			if (itemId >= 5050005 && !GameConstants.isEvan(c.getPlayer().getJob())) {
				c.getPlayer().dropMessage(1, "This reset is only for Evans.");
				break;
			}
			if (itemId < 5050005 && GameConstants.isEvan(c.getPlayer().getJob())) {
				c.getPlayer().dropMessage(1, "This reset is only for non-Evans.");
				break;
			}
			skill1 = slea.readInt();
			skill2 = slea.readInt();
			for (int i8 : GameConstants.blockedSkills) {
				if (skill1 == i8) {
					c.getPlayer().dropMessage(1, "You may not add this skill.");
					return;
				}
			}
			skillSPTo = SkillFactory.getSkill(skill1);
			skillSPFrom = SkillFactory.getSkill(skill2);

			if (skillSPTo.isBeginnerSkill() || skillSPFrom.isBeginnerSkill()) {
				c.getPlayer().dropMessage(1, "You may not add beginner skills.");
				break;
			}
			if (GameConstants.getSkillBookForSkill(skill1) != GameConstants.getSkillBookForSkill(skill2)) {
				c.getPlayer().dropMessage(1, "You may not add different job skills.");
				break;
			}
			if (c.getPlayer().getSkillLevel(skillSPTo) + 1 <= skillSPTo.getMaxLevel()
					&& c.getPlayer().getSkillLevel(skillSPFrom) > 0 && skillSPTo.canBeLearnedBy(c.getPlayer())) {
				if (skillSPTo.isFourthJob()
						&& c.getPlayer().getSkillLevel(skillSPTo) + 1 > c.getPlayer().getMasterLevel(skillSPTo)) {
					c.getPlayer().dropMessage(1, "You will exceed the master level.");
					break;
				}
				if (itemId >= 5050005) {
					if (GameConstants.getSkillBookForSkill(skill1) != (itemId - 5050005) * 2
							&& GameConstants.getSkillBookForSkill(skill1) != (itemId - 5050005) * 2 + 1) {
						c.getPlayer().dropMessage(1, "You may not add this job SP using this reset.");
						break;
					}
				} else {
					int theJob = GameConstants.getJobNumber(skill2);
					switch (skill2 / 10000) {
					case 430:
						theJob = 1;
						break;
					case 431:
					case 432:
						theJob = 2;
						break;
					case 433:
						theJob = 3;
						break;
					case 434:
						theJob = 4;
						break;
					}
					if (theJob != itemId - 5050000) {
						c.getPlayer().dropMessage(1,
								"You may not subtract from this skill. Use the appropriate SP reset.");
						break;
					}
				}
				Map<Skill, SkillEntry> sa = new HashMap<>();
				sa.put(skillSPFrom, new SkillEntry((byte) (c.getPlayer().getSkillLevel(skillSPFrom) - 1),
						c.getPlayer().getMasterLevel(skillSPFrom), SkillFactory.getDefaultSExpiry(skillSPFrom)));
				sa.put(skillSPTo, new SkillEntry((byte) (c.getPlayer().getSkillLevel(skillSPTo) + 1),
						c.getPlayer().getMasterLevel(skillSPTo), SkillFactory.getDefaultSExpiry(skillSPTo)));
				c.getPlayer().changeSkillsLevel(sa);
				used = true;
			}
			break;
		case 5062800:
		case 5062801:
			int preset = slea.readByte();

			newValues = new LinkedList<>();
			ivholder = null;
			ivholder2 = null;
			for (InnerSkillValueHolder isvh : c.getPlayer().getInnerSkills().get(preset)) {
				if (ivholder == null) {
					int nowrank = -1;
					int rand = Randomizer.nextInt(100);
					if (isvh.getRank() == 3) {
						nowrank = 3;
					} else if (isvh.getRank() == 2) {
						if (rand < 5) {
							nowrank = 3;
						} else {
							nowrank = 2;
						}
					} else if (isvh.getRank() == 1) {
						if (rand < 10) {
							nowrank = 2;
						} else {
							nowrank = 1;
						}
					} else if (rand < 40) {
						nowrank = 1;
					} else {
						nowrank = 0;
					}
					ivholder = InnerAbillity.getInstance().renewSkill(nowrank, true, preset);
					while (isvh.getSkillId() == ivholder.getSkillId()) {
						ivholder = InnerAbillity.getInstance().renewSkill(nowrank, true, preset);
					}
					newValues.add(ivholder);
					continue;
				}
				if (ivholder2 == null) {
					ivholder2 = InnerAbillity.getInstance()
							.renewSkill((ivholder.getRank() == 0) ? 0 : (ivholder.getRank() - 1), true, preset);
					while (isvh.getSkillId() == ivholder2.getSkillId()
							|| ivholder.getSkillId() == ivholder2.getSkillId()) {
						ivholder2 = InnerAbillity.getInstance()
								.renewSkill((ivholder.getRank() == 0) ? 0 : (ivholder.getRank() - 1), true, preset);
					}
					newValues.add(ivholder2);
					continue;
				}
				InnerSkillValueHolder ivholder3 = InnerAbillity.getInstance()
						.renewSkill((ivholder.getRank() == 0) ? 0 : (ivholder.getRank() - 1), true, preset);
				while (isvh.getSkillId() == ivholder3.getSkillId() || ivholder.getSkillId() == ivholder3.getSkillId()
						|| ivholder2.getSkillId() == ivholder3.getSkillId()) {
					ivholder3 = InnerAbillity.getInstance()
							.renewSkill((ivholder.getRank() == 0) ? 0 : (ivholder.getRank() - 1), true, preset);
				}
				newValues.add(ivholder3);
			}

			c.getPlayer().innerCirculator = newValues;

			c.getSession().writeAndFlush(CWvsContext.MiracleCirculator(newValues, itemId, preset));

			used = true;

			break;
		case 5060048:
			if (c.getPlayer().getInventory(MapleInventoryType.EQUIP).getNumFreeSlot() > 0
					&& c.getPlayer().getInventory(MapleInventoryType.USE).getNumFreeSlot() >= 2
					&& c.getPlayer().getInventory(MapleInventoryType.SETUP).getNumFreeSlot() > 0
					&& c.getPlayer().getInventory(MapleInventoryType.ETC).getNumFreeSlot() > 0) {
				boolean fromSpecial = Randomizer.isSuccess(ServerConstants.SgoldappleSuc);

				List<Triple<Integer, Integer, Integer>> list = ServerConstants.goldapple;
				if (fromSpecial) {
					list = ServerConstants.Sgoldapple;
				}
				int itemid = 0, count = 0;
				double bestValue = Double.MAX_VALUE;

				for (Triple<Integer, Integer, Integer> element : list) {
					double a = ((Integer) element.getRight()).intValue();
					double r = a / 10000.0D;
					double value = -Math.log(Randomizer.nextDouble()) / r;
					if (value < bestValue) {
						bestValue = value;
						itemid = ((Integer) element.getLeft()).intValue();
						count = ((Integer) element.getMid()).intValue();
					}
				}
				if (itemid > 0 && count > 0) {
					Item item13 = new Item(itemid, (short) 0, (short) count);
					MapleItemInformationProvider mapleItemInformationProvider = MapleItemInformationProvider
							.getInstance();

					if (GameConstants.isPet(itemid)) {
						item13 = MapleInventoryManipulator.addId_Item(c, itemid, (short) 1, "",
								MaplePet.createPet(itemid, -1L), 30L, "", false);
						break;
					}
					if (GameConstants.getInventoryType(itemid) == MapleInventoryType.EQUIP) {
						item13 = (Equip) mapleItemInformationProvider.getEquipById(itemid);
					}
					if (MapleItemInformationProvider.getInstance().isCash(itemid)) {
						item13.setUniqueId(MapleInventoryIdentifier.getInstance());
					}
					MapleInventoryManipulator.addbyItem(c, item13);
					c.getSession().writeAndFlush(CWvsContext.goldApple(item13, toUse));
					c.getPlayer().gainItem(2435458, 1);
					used = true;
					if (fromSpecial) {
						World.Broadcast
								.broadcastMessage(CWvsContext.serverMessage(11, c.getPlayer().getClient().getChannel(),
										"", c.getPlayer().getName() + "님이 골드애플에서 {} 을 획득하였습니다.", true, item13));
					}
				}
			}
			break;
		case 5155000:
		case 5155004:
		case 5155005:
			j = slea.readInt();
			effect = "";
			switch (j) {
			case 0:
				if (GameConstants.isMercedes(c.getPlayer().getJob())) {
					j = 1;
				}
				if (GameConstants.isIllium(c.getPlayer().getJob())) {
					j = 2;
				}
				if (GameConstants.isAdel(c.getPlayer().getJob()) || GameConstants.isArk(c.getPlayer().getJob())) {
					j = 3;
				}
				effect = "Effect/BasicEff.img/JobChanged";
				break;
			case 1:
				if (GameConstants.isMercedes(c.getPlayer().getJob())) {
					j = 0;
				}
				effect = "Effect/BasicEff.img/JobChangedElf";
				break;
			case 2:
				if (GameConstants.isIllium(c.getPlayer().getJob())) {
					j = 0;
				}
				effect = "Effect/BasicEff.img/JobChangedIlliumFront";
				break;
			case 3:
				if (GameConstants.isAdel(c.getPlayer().getJob()) || GameConstants.isArk(c.getPlayer().getJob())) {
					j = 0;
				}
				effect = "Effect/BasicEff.img/JobChangedArkFront";
				break;
			}
			c.getPlayer().setKeyValue(7784, "sw", String.valueOf(j));
			c.getSession().writeAndFlush(CField.EffectPacket.showEffect(c.getPlayer(), 0, itemId, 38, 2, 0, (byte) 0,
					true, null, effect, null));
			used = true;
			break;
		case 5155001:
			if (GameConstants.isKaiser(c.getPlayer().getJob())) {
				if (c.getPlayer().getKeyValue(7786, "sw") == 0L) {
					c.getPlayer().setKeyValue(7786, "sw", "1");
				} else {
					c.getPlayer().setKeyValue(7786, "sw", "0");
				}
				c.getPlayer().dropMessage(5, "드래곤 테일 쉬프트의 신비로운 힘으로 모습이 바뀌었습니다.");
				used = true;
				break;
			}
			c.getPlayer().dropMessage(5, "드래곤 테일 쉬프트는 카이저에게만 효과가 있는것 같다.");
			break;
		case 5500000:
			item3 = c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slea.readShort());
			mapleItemInformationProvider4 = MapleItemInformationProvider.getInstance();
			days = 1;
			if (item3 != null && !GameConstants.isAccessory(item3.getItemId()) && item3.getExpiration() > -1L
					&& !mapleItemInformationProvider4.isCash(item3.getItemId())
					&& System.currentTimeMillis() + 8640000000L > item3.getExpiration() + 86400000L) {
				boolean change = true;
				for (String z : GameConstants.RESERVED) {
					if (c.getPlayer().getName().indexOf(z) != -1 || item3.getOwner().indexOf(z) != -1) {
						change = false;
					}
				}
				if (change) {
					item3.setExpiration(item3.getExpiration() + 86400000L);
					c.getPlayer().forceReAddItem(item3, MapleInventoryType.EQUIPPED);
					used = true;
					break;
				}
				c.getPlayer().dropMessage(1, "It may not be used on this item.");
			}
			break;
		case 5500001:
			item3 = c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slea.readShort());
			mapleItemInformationProvider4 = MapleItemInformationProvider.getInstance();
			days = 7;
			if (item3 != null && !GameConstants.isAccessory(item3.getItemId()) && item3.getExpiration() > -1L
					&& !mapleItemInformationProvider4.isCash(item3.getItemId())
					&& System.currentTimeMillis() + 8640000000L > item3.getExpiration() + 604800000L) {
				boolean change = true;
				for (String z : GameConstants.RESERVED) {
					if (c.getPlayer().getName().indexOf(z) != -1 || item3.getOwner().indexOf(z) != -1) {
						change = false;
					}
				}
				if (change) {
					item3.setExpiration(item3.getExpiration() + 604800000L);
					c.getPlayer().forceReAddItem(item3, MapleInventoryType.EQUIPPED);
					used = true;
					break;
				}
				c.getPlayer().dropMessage(1, "It may not be used on this item.");
			}
			break;

		case 5500002:
			item3 = c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slea.readShort());
			mapleItemInformationProvider4 = MapleItemInformationProvider.getInstance();
			days = 20;
			if (item3 != null && !GameConstants.isAccessory(item3.getItemId()) && item3.getExpiration() > -1L
					&& !mapleItemInformationProvider4.isCash(item3.getItemId())
					&& System.currentTimeMillis() + 8640000000L > item3.getExpiration() + 1728000000L) {
				boolean change = true;
				for (String z : GameConstants.RESERVED) {
					if (c.getPlayer().getName().indexOf(z) != -1 || item3.getOwner().indexOf(z) != -1) {
						change = false;
					}
				}
				if (change) {
					item3.setExpiration(item3.getExpiration() + 1728000000L);
					c.getPlayer().forceReAddItem(item3, MapleInventoryType.EQUIPPED);
					used = true;
					break;
				}
				c.getPlayer().dropMessage(1, "It may not be used on this item.");
			}
			break;
		case 5500005:
			item3 = c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slea.readShort());
			mapleItemInformationProvider4 = MapleItemInformationProvider.getInstance();
			days = 50;
			if (item3 != null && !GameConstants.isAccessory(item3.getItemId()) && item3.getExpiration() > -1L
					&& !mapleItemInformationProvider4.isCash(item3.getItemId())
					&& System.currentTimeMillis() + 8640000000L > item3.getExpiration() + 4320000000L) {
				boolean change = true;
				for (String z : GameConstants.RESERVED) {
					if (c.getPlayer().getName().indexOf(z) != -1 || item3.getOwner().indexOf(z) != -1) {
						change = false;
					}
				}
				if (change) {
					item3.setExpiration(item3.getExpiration() + 25032704L);
					c.getPlayer().forceReAddItem(item3, MapleInventoryType.EQUIPPED);
					used = true;
					break;
				}
				c.getPlayer().dropMessage(1, "It may not be used on this item.");
			}
			break;
		case 5044000:
		case 5044001:
		case 5044002:
		case 5044006:
		case 5044007:
			slea.readByte();
			mapid = slea.readInt();
			if (mapid == 180000000) {
				c.getPlayer().warp(ServerConstants.warpMap);
				c.getPlayer().dropMessage(1, "無法移動到該位置.");
				return;
			}
			target = c.getChannelServer().getMapFactory().getMap(mapid);
			c.getPlayer().changeMap(target, target.getPortal(0));
			if (ItemFlag.KARMA_USE.check(toUse.getFlag())) {
				toUse.setFlag(toUse.getFlag() - ItemFlag.KARMA_USE.getValue() + ItemFlag.UNTRADEABLE.getValue());
				c.getSession().writeAndFlush(
						CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.CASH, toUse));
			}
			break;
		case 5500006:
			item2 = c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slea.readShort());
			mapleItemInformationProvider3 = MapleItemInformationProvider.getInstance();
			days = 99;
			if (item2 != null && !GameConstants.isAccessory(item2.getItemId()) && item2.getExpiration() > -1L
					&& !mapleItemInformationProvider3.isCash(item2.getItemId())
					&& System.currentTimeMillis() + 8640000000L > item2.getExpiration() + 8553600000L) {
				boolean change = true;
				for (String z : GameConstants.RESERVED) {
					if (c.getPlayer().getName().indexOf(z) != -1 || item2.getOwner().indexOf(z) != -1) {
						change = false;
					}
				}
				if (change) {
					item2.setExpiration(item2.getExpiration() + -36334592L);
					c.getPlayer().forceReAddItem(item2, MapleInventoryType.EQUIPPED);
					used = true;
					break;
				}
				c.getPlayer().dropMessage(1, "It may not be used on this item.");
			}
			break;
		case 5064200:
			slea.skip(4);
			s3 = slea.readShort();
			if (s3 < 0) {
				equip2 = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(s3);
			} else {
				equip2 = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(s3);
			}
			if (equip2 != null) {
				Equip origin = (Equip) MapleItemInformationProvider.getInstance().getEquipById(equip2.getItemId());
				equip2.setAcc(origin.getAcc());
				equip2.setAvoid(origin.getAvoid());
				equip2.setDex(origin.getDex());
				equip2.setHands(origin.getHands());
				equip2.setHp(origin.getHp());
				equip2.setInt(origin.getInt());
				equip2.setJump(origin.getJump());
				equip2.setLevel(origin.getLevel());
				equip2.setLuk(origin.getLuk());
				equip2.setMatk(origin.getMatk());
				equip2.setMdef(origin.getMdef());
				equip2.setMp(origin.getMp());
				equip2.setSpeed(origin.getSpeed());
				equip2.setStr(origin.getStr());
				equip2.setUpgradeSlots(origin.getUpgradeSlots());
				equip2.setWatk(origin.getWatk());
				equip2.setWdef(origin.getWdef());
				equip2.setEnhance((byte) 0);
				equip2.setViciousHammer((byte) 0);
				c.getSession().writeAndFlush(CWvsContext.InventoryPacket.updateInventoryItem(false,
						MapleInventoryType.EQUIP, (Item) equip2));
				c.getPlayer().getMap().broadcastMessage(c.getPlayer(), CField.getScrollEffect(c.getPlayer().getId(),
						Equip.ScrollResult.SUCCESS, cc, toUse.getItemId(), equip2.getItemId()), true);
				used = true;
				break;
			}
			c.getPlayer().dropMessage(1, "發生NULL錯誤，請在錯誤公告板詳細說明您使用的物品.");
			break;
		case 5060000:
			item1 = c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slea.readShort());
			if (item1 != null && item1.getOwner().equals("")) {
				boolean change = true;
				for (String z : GameConstants.RESERVED) {
					if (c.getPlayer().getName().indexOf(z) != -1) {
						change = false;
					}
				}
				if (change) {
					item1.setOwner(c.getPlayer().getName());
					c.getPlayer().forceReAddItem(item1, MapleInventoryType.EQUIPPED);
					used = true;
				}
			}
			break;
		case 5062500:
			up = false;
			n = slea.readInt();
			item10 = c.getPlayer().getInventory((n < 0) ? MapleInventoryType.EQUIPPED : MapleInventoryType.EQUIP)
					.getItem((short) n);
			if (item10 != null && c.getPlayer().getInventory(MapleInventoryType.USE).getNumFreeSlot() >= 1) {
				Equip equip = (Equip) item10;
				if (equip.getPotential4() <= 0) {
					c.getPlayer().dropMessage(1, "未賦予附加潛能.");
					c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
					return;
				}
				if (c.getPlayer().getInventory(MapleInventoryType.USE).getNumFreeSlot() >= 1) {
					int level = 0;
					level = (equip.getPotential4() >= 10000) ? (equip.getPotential4() / 10000)
							: (equip.getPotential4() / 100);
					if (level >= 4) {
						level = 4;
					}
					int rate = (level == 3) ? 3 : ((level == 2) ? 5 : ((level == 1) ? 10 : 0));
					if (Randomizer.nextInt(100) < rate) {
						up = true;
						level++;
					}
					int temp = level;
					int a = 0;
					while (temp > 1) {
						if (temp > 1) {
							temp--;
							a++;
						}
					}
					equip.setPotential4(EdiCubeOption.getEdiCubePotentialId(item10.getItemId(), level, 1, new int[0]));
					equip.setPotential5(EdiCubeOption.getEdiCubePotentialId(item10.getItemId(), level, 2, new int[0]));
					if (equip.getPotential6() > 0) {
						equip.setPotential6(EdiCubeOption.getEdiCubePotentialId(item10.getItemId(), level, 3,
								new int[] { equip.getPotential4(), equip.getPotential5() }));
						while (!GameConstants.getPotentialCheck(equip.getPotential6(), equip.getPotential4(),
								equip.getPotential5())) {
							equip.setPotential6(EdiCubeOption.getEdiCubePotentialId(item10.getItemId(), level, 3,
									new int[] { equip.getPotential4(), equip.getPotential5() }));
						}
					}
					if (GameConstants.isZeroWeapon(equip.getItemId())) {
						Equip zeroequip = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED)
								.getItem((short) -11);
						zeroequip.setPotential4(equip.getPotential4());
						zeroequip.setPotential5(equip.getPotential5());
						zeroequip.setPotential6(equip.getPotential6());
						if (zeroequip != null) {
							c.getSession().writeAndFlush(CWvsContext.InventoryPacket.updateInventoryItem(false,
									MapleInventoryType.EQUIP, (Item) zeroequip));
						}
					}
					c.getSession().writeAndFlush(CField.getEditionalCubeStart(c.getPlayer(), item10, up, itemId,
							c.getPlayer().itemQuantity(toUse.getItemId()) - 1));
					c.getSession().writeAndFlush(
							CField.showPotentialReset(c.getPlayer().getId(), true, itemId, equip.getItemId()));
					c.getPlayer().forceReAddItem(item10,
							(n < 0) ? MapleInventoryType.EQUIPPED : MapleInventoryType.EQUIP);
					MapleInventoryManipulator.addById(c, 2430915, (short) 1, null, null, 0L,
							"Reward item: " + itemId + " on " + FileoutputUtil.CurrentReadable_Date());
					used = true;
					c.getPlayer().gainMeso(-GameConstants.getCubeMeso(equip.getItemId()), false);
					break;
				}
				c.getPlayer().dropMessage(5, "소비 아이템 여유 공간이 부족하여 잠재능력 재설정을 실패하였습니다.");
			}
			break;
		case 5062503:
			i = slea.readInt();

			if (i < 0) {
				item8 = c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) i);
			} else {
				item8 = c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem((short) i);
			}

			if (GameConstants.isZero(c.getPlayer().getJob()) && item8 == null) {
				item8 = c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) i);
			}

			bool4 = false;

			if (item8 != null && c.getPlayer().getInventory(MapleInventoryType.USE).getNumFreeSlot() >= 1) {
				Equip eq = (Equip) item8;
				Equip neq = (Equip) eq.copy();
				c.getPlayer().addKV("lastCube", itemId + "");

				if (neq.getPotential4() >= 0) {
					int level = 0;
					level = (neq.getPotential4() >= 10000) ? (neq.getPotential4() / 10000)
							: (neq.getPotential4() / 100);
					if (level >= 4) {
						level = 4;
					}
					int rate = (level == 3) ? 5 : ((level == 2) ? 7 : ((level == 1) ? 12 : 0));

					if (Randomizer.nextInt(100) < rate) {
						bool4 = true;
						level++;
					}
					MapleInventoryManipulator.addById(c, 2434782, (short) 1, null, null, 0, "");
					neq.setPotential4(EdiCubeOption.getEdiCubePotentialId(item8.getItemId(), level, 1, new int[0]));
					neq.setPotential5(EdiCubeOption.getEdiCubePotentialId(item8.getItemId(), level, 2, new int[0]));
					if (neq.getPotential6() > 0) {
						neq.setPotential6(EdiCubeOption.getEdiCubePotentialId(item8.getItemId(), level, 3,
								new int[] { neq.getPotential4(), neq.getPotential5() }));
						while (!GameConstants.getPotentialCheck(neq.getPotential6(), neq.getPotential4(),
								neq.getPotential5())) {
							neq.setPotential6(EdiCubeOption.getEdiCubePotentialId(item8.getItemId(), level, 3,
									new int[] { neq.getPotential4(), neq.getPotential5() }));
						}
					}
					c.getPlayer().gainMeso(-GameConstants.getCubeMeso(neq.getItemId()), false);
					c.getSession().writeAndFlush(CField.getWhiteCubeStart(c.getPlayer(), (Item) neq, bool4, 5062503,
							slot, c.getPlayer().itemQuantity(toUse.getItemId()) - 1, 1));
					c.getPlayer().getMap().broadcastMessage(
							CField.getBlackCubeEffect(c.getPlayer().getId(), bool4, 5062503, neq.getItemId()));
					(c.getPlayer()).choicepotential = neq;
					if ((c.getPlayer()).memorialcube == null) {
						(c.getPlayer()).memorialcube = toUse.copy();
					}
					used = true;
					break;
				}
				c.getPlayer().dropMessage(5, "請確認裝備是否已賦予潜在能力.");
				break;
			}
			c.getPlayer().dropMessage(5, "消耗品背包空間不足，無法進行潜在能力設定.");
			break;

		case 5062009: { // 레드 큐브
			ii = MapleItemInformationProvider.getInstance();
			pos = slea.readInt();
			item = c.getPlayer().getInventory(pos < 0 ? MapleInventoryType.EQUIPPED : MapleInventoryType.EQUIP)
					.getItem((short) pos);
			up = false;
			if (item != null && c.getPlayer().getInventory(MapleInventoryType.USE).getNumFreeSlot() >= 1) {
				Equip eq = (Equip) item;
				int rand = Randomizer.nextInt(100);
				MapleInventoryManipulator.addById(c, 2431893, (short) 1, null, null, 0L,
						"Reward item: " + itemId + " on " + FileoutputUtil.CurrentReadable_Date());
				if (eq.getState() == 1 || eq.getState() == 17) {
					if (rand < 10) {
						up = true;
						eq.setState((byte) 18);
					} else {
						eq.setState((byte) 17);
					}
				} else if (eq.getState() == 18 && !ii.isCash(eq.getItemId())) {
					if (rand < 5) {
						up = true;
						eq.setState((byte) 19);
					} else {
						eq.setState((byte) 18);
					}
				} else if (eq.getState() == 19) {
					if (rand < 3) {
						up = true;
						eq.setState((byte) 20);
					} else {
						eq.setState((byte) 19);
					}
				}
				int level = eq.getState() - 16;
				eq.setPotential1(potential(item.getItemId(), level));
				eq.setPotential2(potential(item.getItemId(),
						((level == 1) || (Randomizer.nextInt(100) < 2)) ? level : (level - 1)));
				eq.setPotential3(
						eq.getPotential3() != 0
								? potential(item.getItemId(),
										((level == 1) || (Randomizer.nextInt(100) < 1)) ? level : (level - 1))
								: 0);
				eq.setLines((byte) (eq.getPotential3() > 0 ? 3 : 2));
				c.getPlayer().gainMeso(-GameConstants.getCubeMeso(eq.getItemId()), false);

				if (GameConstants.isZeroWeapon(eq.getItemId())) {
					Equip zeroequip = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED)
							.getItem((short) -11);
					zeroequip.setState(eq.getState());
					zeroequip.setLines(eq.getLines());
					zeroequip.setPotential1(eq.getPotential1());
					zeroequip.setPotential2(eq.getPotential2());
					zeroequip.setPotential3(eq.getPotential3());
					if (zeroequip != null) {
						c.getSession().writeAndFlush(CWvsContext.InventoryPacket.updateInventoryItem(false,
								MapleInventoryType.EQUIP, zeroequip));
					}
				}

				c.getPlayer().forceReAddItem(item, pos < 0 ? MapleInventoryType.EQUIPPED : MapleInventoryType.EQUIP);
				c.getSession()
						.writeAndFlush(CField.showPotentialReset(c.getPlayer().getId(), true, itemId, eq.getItemId()));
				c.getSession().writeAndFlush(CField.getRedCubeStart(c.getPlayer(), item, up, itemId,
						c.getPlayer().itemQuantity(toUse.getItemId()) - 1));
				// c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				used = true;
			} else {
				c.getPlayer().dropMessage(5, "消耗品背包空間不足，無法進行潜能設定.");
			}
			break;
		}
		case 5062010:
			mapleItemInformationProvider1 = MapleItemInformationProvider.getInstance();
			int tt = 0;
			k = slea.readInt();
			item9 = c.getPlayer().getInventory((k < 0) ? MapleInventoryType.EQUIPPED : MapleInventoryType.EQUIP)
					.getItem((short) k);
			bool5 = false;
			if (item9 != null && c.getPlayer().getInventory(MapleInventoryType.USE).getNumFreeSlot() >= 1) {
				Equip eq = (Equip) item9;
				int rand = Randomizer.nextInt(100);
				MapleInventoryManipulator.addById(c, 2431894, (short) 1, null, null, 0L, "");
				c.getPlayer().addKV("lastCube", itemId + "");
				Equip neq = (Equip) eq.copy();
				if (neq.getState() >= 17) {
					if (neq.getState() == 1 || neq.getState() == 17) {
						if (rand < 10) {
							bool5 = true;
							neq.setState((byte) 18);
						} else {
							neq.setState((byte) 17);
						}
					} else if (neq.getState() == 18 && !mapleItemInformationProvider1.isCash(neq.getItemId())) {
						if (rand < 5) {
							bool5 = true;
							neq.setState((byte) 19);
						} else {
							neq.setState((byte) 18);
						}
					} else if (neq.getState() == 19) {
						if (rand < 3) {
							bool5 = true;
							neq.setState((byte) 20);
						} else {
							neq.setState((byte) 19);
						}
					}
					int level = neq.getState() - 16;
					neq.setPotential1(CubeOption.getBlackCubePotentialId(item9.getItemId(), level, 1, new int[0]));
					neq.setPotential2(CubeOption.getBlackCubePotentialId(item9.getItemId(), level, 2, new int[0]));
					if (neq.getPotential3() > 0) {
						neq.setPotential3(CubeOption.getBlackCubePotentialId(item9.getItemId(), level, 3,
								new int[] { neq.getPotential1(), neq.getPotential2() }));
						while (!GameConstants.getPotentialCheck(neq.getPotential3(), neq.getPotential1(),
								neq.getPotential2())) {
							neq.setPotential3(CubeOption.getBlackCubePotentialId(item9.getItemId(), level, 3,
									new int[] { neq.getPotential1(), neq.getPotential2() }));
						}
					}

					neq.setLines((byte) ((neq.getPotential3() > 0) ? 3 : 2));
					c.getPlayer().gainMeso(-GameConstants.getCubeMeso(neq.getItemId()), false);
					c.getSession()
							.writeAndFlush(CField.getBlackCubeStart(c.getPlayer(), (Item) neq, bool5, 5062010,
									toUse.getPosition(), c.getPlayer().itemQuantity(toUse.getItemId()) - 1,
									c.getPlayer().getCubeStack(tt)));
					c.getPlayer().getMap().broadcastMessage(
							CField.getBlackCubeEffect(c.getPlayer().getId(), bool5, 5062010, neq.getItemId()));
					(c.getPlayer()).choicepotential = neq;

					if ((c.getPlayer()).memorialcube == null) {
						(c.getPlayer()).memorialcube = toUse.copy();
					}
					used = true;
					break;
				}
				c.getPlayer().dropMessage(5, "장비에 잠재능력이 부여되어 있는지 확인하십시오.");
				break;
			}
			c.getPlayer().dropMessage(5, "소비 인벤토리의 공간이 부족하여 잠재 설정을 할 수 없습니다.");
			break;

		case 5062005:// 어메이징 미라클 큐브
			pos = slea.readInt();
			item7 = c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem((short) pos);
			if (GameConstants.isZero(c.getPlayer().getJob()) && item7 == null) {
				item7 = c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) pos);
			}
			//
			if (item7 != null && c.getPlayer().getInventory(MapleInventoryType.USE).getNumFreeSlot() >= 1) {
				Equip eq = (Equip) item7;
				int level = eq.getState() - 16;
				if (level < 4) {
					c.getPlayer().dropMessage(1, "僅限傳説等級物品使用.");
					c.send(CWvsContext.enableActions(c.getPlayer()));
					return;
				} else {
					MapleInventoryManipulator.addById(c, 2430759, (short) 1, null, null, 0, "");
				}
				int poten = CubeOption.getPlatinumUnlimitiedCubePotentialId(item7.getItemId(), level, 1, new int[0]);
				eq.setPotential1(poten);
				eq.setPotential2(poten);
				eq.setPotential3(poten);
				c.getPlayer().getMap().broadcastMessage(
						CField.showPotentialReset(c.getPlayer().getId(), true, itemId, eq.getItemId()));
				c.getPlayer().forceReAddItem_NoUpdate(item7, MapleInventoryType.EQUIP);
				used = true;

				if (GameConstants.isZeroWeapon(eq.getItemId())) {
					Equip zeroequip = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED)
							.getItem((short) -11);
					zeroequip.setState(eq.getState());
					zeroequip.setLines(eq.getLines());
					zeroequip.setPotential1(eq.getPotential1());
					zeroequip.setPotential2(eq.getPotential2());
					zeroequip.setPotential3(eq.getPotential3());
					if (zeroequip != null) {
						c.getSession().writeAndFlush(CWvsContext.InventoryPacket.updateInventoryItem(false,
								MapleInventoryType.EQUIP, (Item) zeroequip));
					}
				}
				c.getPlayer().getMap().broadcastMessage(
						CField.showPotentialReset(c.getPlayer().getId(), true, itemId, eq.getItemId()));
				c.getPlayer().forceReAddItem_NoUpdate(item7, MapleInventoryType.EQUIP);
				c.send(CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIP, (Item) eq));
				used = true;
				break;
			}
			c.getPlayer().getMap().broadcastMessage(CField.showPotentialReset(c.getPlayer().getId(), false, itemId, 0));
			break;
		case 5062006:
			pos = slea.readInt();
			item7 = c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem((short) pos);

			if (GameConstants.isZero(c.getPlayer().getJob()) && item7 == null) {
				item7 = c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) pos);
			}
			if (item7 != null && c.getPlayer().getInventory(MapleInventoryType.USE).getNumFreeSlot() >= 1) {
				Equip eq = (Equip) item7;
				int level = eq.getState() - 16;

				if (level < 4) {
					c.getPlayer().dropMessage(1, "僅限傳説等級物品使用.");
					c.send(CWvsContext.enableActions(c.getPlayer()));
					return;
				}
				eq.setPotential1(
						CubeOption.getPlatinumUnlimitiedCubePotentialId(item7.getItemId(), level, 1, new int[0]));
				eq.setPotential2(
						CubeOption.getPlatinumUnlimitiedCubePotentialId(item7.getItemId(), level, 2, new int[0]));

				if (eq.getPotential3() > 0) {
					eq.setPotential3(CubeOption.getPlatinumUnlimitiedCubePotentialId(item7.getItemId(), level, 3,
							new int[] { eq.getPotential1(), eq.getPotential2() }));
					while (!GameConstants.getPotentialCheck(eq.getPotential3(), eq.getPotential1(),
							eq.getPotential2())) {
						eq.setPotential3(CubeOption.getPlatinumUnlimitiedCubePotentialId(item7.getItemId(), level, 3,
								new int[] { eq.getPotential1(), eq.getPotential2() }));
					}
				}
				if (GameConstants.isZeroWeapon(eq.getItemId())) {
					Equip zeroequip = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED)
							.getItem((short) -11);
					zeroequip.setState(eq.getState());
					zeroequip.setLines(eq.getLines());
					zeroequip.setPotential1(eq.getPotential1());
					zeroequip.setPotential2(eq.getPotential2());
					zeroequip.setPotential3(eq.getPotential3());

					if (zeroequip != null) {
						c.getSession().writeAndFlush(CWvsContext.InventoryPacket.updateInventoryItem(false,
								MapleInventoryType.EQUIP, (Item) zeroequip));
					}
				}
				c.getPlayer().getMap().broadcastMessage(
						CField.showPotentialReset(c.getPlayer().getId(), true, itemId, eq.getItemId()));
				c.getPlayer().forceReAddItem_NoUpdate(item7, MapleInventoryType.EQUIP);
				c.send(CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIP, (Item) eq));
				used = true;
				break;
			}
			c.getPlayer().getMap().broadcastMessage(CField.showPotentialReset(c.getPlayer().getId(), false, itemId, 0));
			break;
		case 5152300:
			forbiddenFaces = new int[] { 22100, 22200, 22300, 22400, 22500, 22600, 22700, 22800 };
			for (int face : forbiddenFaces) {
				if (c.getPlayer().getFace() == face) {
					used = false;
					c.getPlayer().dropMessage(1, "無法進行混合瞳孔整形.");
					return;
				}
			}
			bool1 = false;
			bool3 = false;
			isAlphaBeta = false;
			slea.skip(2);
			ordinaryColor = slea.readInt();
			// addColor = slea.readInt();
			i6 = (c.getPlayer().getFace() < 100000) ? c.getPlayer().getFace() : (c.getPlayer().getFace() / 1000);

			if (bool1 || bool3) {
				i6 = (c.getPlayer().getSecondFace() < 100000) ? c.getPlayer().getSecondFace()
						: (c.getPlayer().getSecondFace() / 1000);
			}
			addColor = i6 - i6 % 1000 + i6 % 100 + ordinaryColor / 10000 * 100;
			newFace = addColor * 1000 + (ordinaryColor % 10000 / 1000 * 100) + (ordinaryColor % 100);

			if (bool1) {
				c.getPlayer().setSecondFace(newFace);
				c.getPlayer().updateAngelicStats();
			} else if (bool3) {
				c.getPlayer().setSecondFace(newFace);
				c.getPlayer().updateZeroStats();
			} else if (isAlphaBeta) {
				c.getPlayer().setFace(newFace);
				c.getPlayer().setSecondFace(newFace);
				c.getPlayer().updateSingleStat(MapleStat.FACE, newFace);
			} else {
				c.getPlayer().setFace(newFace);
				c.getPlayer().updateSingleStat(MapleStat.FACE, newFace);
			}
			c.getSession()
					.writeAndFlush(CWvsContext.mixLense(itemId, i6, newFace, bool1, bool3, isAlphaBeta, c.getPlayer()));
			c.getPlayer().equipChanged();
			used = true;
			break;
		case 5152301:
			forbiddenFaces = new int[] { 22100, 22200, 22300, 22400, 22500, 22600, 22700, 22800 };

			for (int face : forbiddenFaces) {
				if (c.getPlayer().getFace() == face) {
					used = false;
					c.getPlayer().dropMessage(1, "無法進行混合瞳孔整形.");
					return;
				}
			}

			dressUp = false;
			isBeta = false;
			isAlphaBeta = false;
			baseFace = (c.getPlayer().getFace() < 100000) ? c.getPlayer().getFace() : (c.getPlayer().getFace() / 1000);
			i4 = Randomizer.nextInt(8);
			i5 = Randomizer.nextInt(8);

			while (i5 == i4) {
				i5 = Randomizer.nextInt(8);
			}
			addColor = baseFace - baseFace % 1000 + baseFace % 100 + i4 * 100;
			newFace = addColor * 1000 + i5 * 100 + 50;
			c.getSession().writeAndFlush(
					CWvsContext.mixLense(itemId, baseFace, newFace, dressUp, isBeta, isAlphaBeta, c.getPlayer()));

			if (dressUp) {
				c.getPlayer().setSecondFace(newFace);
				if (c.getPlayer().getDressup()) {
					c.getPlayer().updateSingleStat(MapleStat.FACE, newFace);
				}

			} else if (isBeta) {
				c.getPlayer().setSecondFace(newFace);
				if (c.getPlayer().getGender() == 1) {
					c.getPlayer().updateSingleStat(MapleStat.FACE, newFace);
				}
			} else if (isAlphaBeta) {
				c.getPlayer().setFace(newFace);
				c.getPlayer().updateSingleStat(MapleStat.FACE, newFace);
			} else {
				c.getPlayer().setFace(newFace);
				c.getPlayer().updateSingleStat(MapleStat.FACE, newFace);
			}
			c.getPlayer().equipChanged();
			used = true;
			break;
		case 5521000:
			mapleInventoryType2 = MapleInventoryType.getByType((byte) slea.readInt());
			item6 = c.getPlayer().getInventory(mapleInventoryType2).getItem((short) slea.readInt());

			if (item6 != null && !ItemFlag.TRADEABLE_ONETIME_EQUIP.check(item6.getFlag())
					&& MapleItemInformationProvider.getInstance().isShareTagEnabled(item6.getItemId())) {
				int i8 = item6.getFlag();
				if (mapleInventoryType2 == MapleInventoryType.EQUIP) {
					i8 += ItemFlag.TRADEABLE_ONETIME_EQUIP.getValue();
				} else {
					return;
				}
				item6.setFlag(i8);
				c.getPlayer().forceReAddItem_NoUpdate(item6, mapleInventoryType2);
				c.getSession().writeAndFlush(
						CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIP, item6));
				used = true;
			}
			break;
//5000930, 5000931, 5000932,5002079,5002080,5002081,5002254,5002255,5002256
		case 5069100:// 루나 크리스탈
			wonderblack = new int[] { 5000762, 5000763, 5000764, 5000790, 5000791, 5000792, 5000918, 5000919, 5000920,
					5000933, 5000934, 5000935, 5000963, 5000964, 5000965, 5002033, 5002034, 5002035, 5002082, 5002083,
					5002084, 5002137, 5002138, 5002139, 5002161, 5002162, 5002163, 5002186, 5002187, 5002188, 5002200,
					5002201, 5002202, 5002226, 5002227, 5002228 };
			luna = new int[] { 5000930, 5000931, 5000932, 5002079, 5002080, 5002081, 5002254, 5002255, 5002256 };
			slea.skip(4);
			baseslot = slea.readShort();
			slea.skip(12);
			usingslot = slea.readShort();
			baseitem = c.getPlayer().getInventory(MapleInventoryType.CASH).getItem(baseslot);
			usingitem = c.getPlayer().getInventory(MapleInventoryType.CASH).getItem(usingslot);
			basegrade = baseitem.getPet().getWonderGrade();
			if (baseitem == null || usingitem == null) {
				return;
			}

			MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.CASH, baseitem.getPosition(), (short) 1,
					false);
			MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.CASH, usingitem.getPosition(), (short) 1,
					false);

			random = new ArrayList<>();

			event = (Calendar.getInstance().get(7) == 3);
			sp = false;

			maplePet3 = null;
			item12 = null;

			if (basegrade == 1) {
				random.add(new Pair(Integer.valueOf(1), Integer.valueOf(9140 + (event ? -500 : 0))));
				random.add(new Pair(Integer.valueOf(2), Integer.valueOf(664 + (event ? 500 : 0))));
				random.add(new Pair(Integer.valueOf(3), Integer.valueOf(196)));
			} else if (basegrade == 4) {
				random.add(new Pair(Integer.valueOf(1), Integer.valueOf(8460 + (event ? -500 : 0))));
				random.add(new Pair(Integer.valueOf(2), Integer.valueOf(1140 + (event ? 500 : 0))));
				random.add(new Pair(Integer.valueOf(3), Integer.valueOf(400)));
			}

			i7 = GameConstants.getWeightedRandom(random);
			itemidselect = 0;

			if (i7 == 1 || i7 == 2) {
				itemidselect = wonderblack[Randomizer.rand(0, wonderblack.length - 1)];
				if (i7 == 2) {
					itemidselect = luna[Randomizer.rand(0, luna.length - 1)];
					sp = true;
				}
				maplePet3 = MaplePet.createPet(itemidselect, -1);
				if (basegrade == 1 && i7 == 1) {
					maplePet3.setWonderGrade(4);
				} else if (i7 == 1) {
					maplePet3.setWonderGrade(5);
				}
				Connection con = null;

				try {
					con = DatabaseConnection.getConnection();
					maplePet3.saveToDb(con);
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					try {
						if (con != null) {
							con.close();
						}
					} catch (Exception exception) {
					}
				}
				item12 = MapleInventoryManipulator.addId_Item(c, itemidselect, (short) 1, "", maplePet3, 30L, "",
						false);

				if (item12 != null) {
					c.getSession().writeAndFlush(CSPacket.LunaCrystal((Item) item12));
					if (sp) {
						World.Broadcast.broadcastMessage(
								CWvsContext.serverMessage(11, c.getPlayer().getClient().getChannel(), "",
										c.getPlayer().getName() + "님이 루나 크리스탈에서 {} 을 획득하였습니다.", true, (Item) item12));
					}
					used = true;
				}
				break;
			}
		case 5068305:
		case 5068301:
			if (c.getPlayer().getInventory(MapleInventoryType.CASH).getNumFreeSlot() >= 1
					&& c.getPlayer().getInventory(MapleInventoryType.EQUIP).getNumFreeSlot() >= 1
					&& c.getPlayer().getInventory(MapleInventoryType.USE).getNumFreeSlot() >= 1) {
				final List<Pair<Integer, Integer>> random3 = new ArrayList<Pair<Integer, Integer>>();
				final int[] UniqueItem = { 5000762, 5000763, 5000764, 5000790, 5000791, 5000792, 5000918, 5000919,
						5000920, 5000933, 5000934, 5000935, 5000963, 5000964, 5000965, 5002033, 5002034, 5002035,
						5002082, 5002083, 5002084, 5002137, 5002138, 5002139, 5002161, 5002162, 5002163, 5002186,
						5002187, 5002188, 5002200, 5002201, 5002202, 5002226, 5002227, 5002228 };
				int itemid = 0;
				final int count = 1;
				final int rand3 = Randomizer.rand(0, UniqueItem.length - 1);
				itemid = UniqueItem[rand3];
				final MapleItemInformationProvider ii7 = MapleItemInformationProvider.getInstance();
				// Item item6;
				if (GameConstants.isPet(itemid)) {
					item6 = MapleInventoryManipulator.addId_Item(c, itemid, (short) 1, "",
							MaplePet.createPet(itemid, -1L), 30L, "", false);
				} else {
					if (GameConstants.getInventoryType(itemid) == MapleInventoryType.EQUIP) {
						item6 = ii7.getEquipById(itemid);
					} else {
						item6 = new Item(itemid, (short) 0, (short) count);
					}
					if (MapleItemInformationProvider.getInstance().isCash(itemid)) {
						item6.setUniqueId(MapleInventoryIdentifier.getInstance());
					}
					MapleInventoryManipulator.addbyItem(c, item6);
				}
				if (item6 != null) {
					World.Broadcast
							.broadcastMessage(CWvsContext.serverMessage(11, c.getPlayer().getClient().getChannel(), "",
									c.getPlayer().getName() + "님이 위습의 블랙베리에서 {} 을 획득하였습니다.", true, item6));
					c.getSession().writeAndFlush((Object) CSPacket.WonderBerry((byte) 1, item6, toUse.getItemId()));
				}
				used = true;
				break;
			}
			c.getPlayer().dropMessage(5, "소비, 캐시, 장비 여유 공간이 각각 한칸이상 부족합니다.");
			break;
		case 5068302:
			if (c.getPlayer().getInventory(MapleInventoryType.CASH).getNumFreeSlot() >= 1
					&& c.getPlayer().getInventory(MapleInventoryType.EQUIP).getNumFreeSlot() >= 1
					&& c.getPlayer().getInventory(MapleInventoryType.USE).getNumFreeSlot() >= 1) {
				Item item13;
				int[][] itemid = { { 1113070, 1 }, { 1152155, 1 }, { 1032216, 1 }, { 2435755, 1 }, { 2439653, 1 },
						{ 2046996, 1 }, { 2046997, 1 }, { 2047818, 1 }, { 5000930, 1 }, { 5000931, 1 }, { 5000932, 1 },
						{ 2591659, 1 }, { 2438686, 1 }, { 2591640, 1 }, { 2591676, 1 }, { 1113063, 1 }, { 1113064, 1 },
						{ 1022232, 1 }, { 1672077, 1 }, { 1113063, 1 }, { 1113064, 1 }, { 1113065, 1 }, { 1113066, 1 },
						{ 1112663, 1 }, { 1112586, 1 }, { 2438686, 1 }, { 5002079, 1 }, { 5002080, 1 }, { 5002081, 1 },
						{ 2438686, 1 } };
				int i8 = Randomizer.nextInt(itemid.length);
				MapleItemInformationProvider mapleItemInformationProvider = MapleItemInformationProvider.getInstance();
				if (GameConstants.isPet(itemid[i8][0])) {
					item13 = MapleInventoryManipulator.addId_Item(c, itemid[i8][0], (short) itemid[i8][1], "",
							MaplePet.createPet(itemid[i8][0], -1L), 30L, "", false);
				} else {
					if (GameConstants.getInventoryType(itemid[i8][0]) == MapleInventoryType.EQUIP) {
						item13 = (Equip) mapleItemInformationProvider.getEquipById(itemid[i8][0]);
					} else {
						item13 = new Item(itemid[i8][0], (short) 0, (short) itemid[i8][1],
								(byte) ItemFlag.UNTRADEABLE.getValue());
					}
					if (MapleItemInformationProvider.getInstance().isCash(itemid[i8][0])) {
						item13.setUniqueId(MapleInventoryIdentifier.getInstance());
					}
					MapleInventoryManipulator.addbyItem(c, item13);
				}
				if (item13 != null) {
					c.getSession().writeAndFlush(CSPacket.WonderBerry((byte) 1, item13, toUse.getItemId()));
				}
				used = true;
				break;
			}
			c.getPlayer().dropMessage(5, "소비, 캐시, 장비 여유 공간이 각각 한칸이상 부족합니다.");
			break;
		case 5068300:
			if (c.getPlayer().getInventory(MapleInventoryType.CASH).getNumFreeSlot() >= 1
					&& c.getPlayer().getInventory(MapleInventoryType.EQUIP).getNumFreeSlot() >= 1
					&& c.getPlayer().getInventory(MapleInventoryType.USE).getNumFreeSlot() >= 1) {
				Item item13;
				List<Pair<Integer, Integer>> list = new ArrayList<>();

				int[][] NormalItem = { { 5068300, 1 } };
				int[] HighuerItem = { 5000765, 5000766, 5000767, 5000768, 5000769, 5000793, 5000794, 5000795, 5000796,
						5000797, 5000921, 5000922, 5000923, 5000924, 5000925, 5000936, 5000937, 5000938, 5000939,
						5000940, 5000966, 5000967, 5000968, 5000965, 5000970, 5002036, 5002037, 5002038, 5002039,
						5002040, 5002085, 5002086, 5002140, 5002141, 5002164, 5002165, 5002189, 5002190, 5002203,
						5002204, 5002229, 5002230, 5002231 };
				int[] UniqueItem = { 5000762, 5000763, 5000764, 5000790, 5000791, 5000792, 5000918, 5000919, 5000920,
						5000933, 5000934, 5000935, 5000963, 5000964, 5000965, 5002033, 5002034, 5002035, 5002082,
						5002083, 5002084, 5002137, 5002138, 5002139, 5002161, 5002162, 5002163, 5002186, 5002187,
						5002188, 5002200, 5002201, 5002202, 5002226, 5002227, 5002228 };

				boolean bool = (Calendar.getInstance().get(7) == 2);

				list.add(new Pair(Integer.valueOf(1), Integer.valueOf(3004)));
				list.add(new Pair(Integer.valueOf(2), Integer.valueOf(6000 + (bool ? -500 : 0))));
				list.add(new Pair(Integer.valueOf(3), Integer.valueOf(996 + (bool ? 500 : 0))));

				int itemid = 0;
				int count = 1;
				int i8 = GameConstants.getWeightedRandom(list);
				if (i8 == 1) {
					int rand = Randomizer.rand(0, NormalItem.length - 1);
					itemid = NormalItem[rand][0];
					count = NormalItem[rand][1];
				} else if (i8 == 2) {
					int rand = Randomizer.rand(0, HighuerItem.length - 1);
					itemid = HighuerItem[rand];
				} else if (i8 == 3) {
					int rand = Randomizer.rand(0, UniqueItem.length - 1);
					itemid = UniqueItem[rand];
				}
				MapleItemInformationProvider mapleItemInformationProvider = MapleItemInformationProvider.getInstance();
				if (GameConstants.isPet(itemid)) {
					item13 = MapleInventoryManipulator.addId_Item(c, itemid, (short) 1, "",
							MaplePet.createPet(itemid, -1L), 30L, "", false);
				} else {
					if (GameConstants.getInventoryType(itemid) == MapleInventoryType.EQUIP) {
						item13 = (Equip) mapleItemInformationProvider.getEquipById(itemid);
					} else {
						item13 = new Item(itemid, (short) 0, (short) count);
					}
					if (MapleItemInformationProvider.getInstance().isCash(itemid)) {
						item13.setUniqueId(MapleInventoryIdentifier.getInstance());
					}
					MapleInventoryManipulator.addbyItem(c, item13);
				}
				if (item13 != null) {
					if (i8 == 3) {
						World.Broadcast
								.broadcastMessage(CWvsContext.serverMessage(11, c.getPlayer().getClient().getChannel(),
										"", c.getPlayer().getName() + "님이 위습의 원더베리에서 {} 을 획득하였습니다.", true, item13));
					}
					c.getSession().writeAndFlush(CSPacket.WonderBerry((byte) 1, item13, toUse.getItemId()));
				}
				used = true;
				break;
			}
			c.getPlayer().dropMessage(5, "소비, 캐시, 장비 여유 공간이 각각 한칸이상 부족합니다.");
			break;
		case 5520000:
		case 5520001:
			mapleInventoryType1 = MapleInventoryType.getByType((byte) slea.readInt());
			item5 = c.getPlayer().getInventory(mapleInventoryType1).getItem((short) slea.readInt());

			if (item5 != null && !ItemFlag.KARMA_EQUIP.check(item5.getFlag())
					&& !ItemFlag.KARMA_USE.check(item5.getFlag())
					&& ((itemId == 5520000
							&& MapleItemInformationProvider.getInstance().isKarmaEnabled(item5.getItemId()))
							|| (itemId == 5520001 && MapleItemInformationProvider.getInstance()
									.isPKarmaEnabled(item5.getItemId())))) {
				int i8 = item5.getFlag();

				if (mapleInventoryType1 == MapleInventoryType.EQUIP) {
					i8 += ItemFlag.KARMA_EQUIP.getValue();
				} else {
					i8 += ItemFlag.KARMA_USE.getValue();
				}
				if (item5.getType() == 1) {
					Equip eq = (Equip) item5;
					if (eq.getKarmaCount() > 0) {
						eq.setKarmaCount((byte) (eq.getKarmaCount() - 1));
					}
				}
				item5.setFlag(i8);
				c.getPlayer().forceReAddItem_NoUpdate(item5, mapleInventoryType1);
				c.getSession().writeAndFlush(
						CWvsContext.InventoryPacket.updateInventoryItem(false, mapleInventoryType1, item5));
				used = true;
			}
			break;
		case 5570000:
			slea.readInt();
			equip1 = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem((short) slea.readInt());

			if (equip1 != null) {
				if (GameConstants.canHammer(equip1.getItemId())
						&& MapleItemInformationProvider.getInstance().getSlots(equip1.getItemId()) > 0
						&& equip1.getViciousHammer() < 2) {
					equip1.setViciousHammer((byte) (equip1.getViciousHammer() + 1));
					equip1.setUpgradeSlots((byte) (equip1.getUpgradeSlots() + 1));
					c.getPlayer().forceReAddItem((Item) equip1, MapleInventoryType.EQUIP);
					c.getSession().writeAndFlush(CSPacket.ViciousHammer(true, true));
					used = true;
					break;
				}
				c.getPlayer().dropMessage(5, "You may not use it on this item.");
				c.getSession().writeAndFlush(CSPacket.ViciousHammer(true, false));
			}
			break;
		case 5069000:
		case 5069001:

			SLabel = new int[] { 1003548, 1003549, 1050234, 1051284, 1702357, 1102420, 1003831, 1052605, 1702415,
					1072808, 1082520, 1003867, 1042264, 1060182, 1061206, 1702424, 1082527, 1003892, 1050285, 1051352,
					1702433, 1072831, 1102583, 1003909, 1050291, 1051357, 1702442, 1072836, 1102593, 1003945, 1050296,
					1051362, 1702451, 1072852, 1102608, 1003957, 1003958, 1050300, 1051367, 1702457, 1072862, 1102619,
					1003971, 1003972, 1051369, 1050302, 1702464, 1072868, 1102621, 1004002, 1050305, 1051373, 1702473,
					1070057, 1071074, 1102632, 1003998, 1050304, 1051372, 1702468, 1072876, 1082565, 1000069, 1050311,
					1051383, 1702486, 1072901, 1102667, 1000072, 1001095, 1050310, 1051382, 1702485, 1072897, 1102669,
					1000074, 1001097, 1050319, 1051390, 1702503, 1071076, 1102674, 1004158, 1050322, 1051392, 1702512,
					1070061, 1071078, 1102688, 1004180, 1042319, 1062207, 1702523, 1072934, 1004192, 1050335, 1051405,
					1702528, 1072943, 1102706, 1004213, 1050337, 1051406, 1702535, 1072951, 1102712, 1004279, 1050341,
					1051410, 1702540, 1072998, 1102748, 1004327, 1050346, 1051415, 1702549, 1073011, 1102758, 1000079,
					1001100, 1050351, 1051420, 1702553, 1070064, 1100004, 1101000, 1004411, 1050353, 1051422, 1702561,
					1070065, 1081014, 1004453, 1050359, 1051429, 1702570, 1070067, 1071083, 1102811, 1004468, 1050362,
					1051432, 1702574, 1073050, 1102816, 1004486, 1050364, 1051434, 1702586, 1073056, 1102822, 1004527,
					1050368, 1051437, 1702591, 1071085, 1070069, 1102831, 1004541, 1050370, 1051439, 1702595, 1073075,
					1102836, 1004568, 1050372, 1051441, 1702607, 1073079, 1102844, 1000088, 1001110, 1050380, 1051450,
					1702620, 1073084, 1102848, 1004590, 1050386, 1051456, 1073088, 1702625, 1102859, 1004602, 1050389,
					1051459, 1702628, 1070072, 1071089, 1102864, 1000091, 1001113, 1050392, 1051463, 1702631, 1070073,
					1071090, 1102869, 1004602, 1050394, 1051465, 1702637, 1073105, 1102876, 1002447, 1004690, 1050414,
					1051483, 1702654, 1073127, 1102900, 1004701, 1050417, 1051486, 1702668, 1073128, 1102907, 1004716,
					1050422, 1051490, 1070078, 1071095, 1102915, 1702676, 1004739, 1050423, 1051491, 1702681, 1070079,
					1071096, 1102920, 1004774, 1004775, 1050424, 1051492, 1702687, 1073148, 1102928, 1004797, 1050430,
					1051498, 1702696, 1073152, 1102936, 1004794, 1050429, 1051497, 1702694, 1070082, 1071099, 1102934,
					1004814, 1050432, 1051500, 1702706, 1070083, 1071100, 1102951, 1004845, 1004846, 1050435, 1051503,
					1702715, 1073170, 1073171, 1102959, 1102960, 1004852, 1050438, 1051506, 1702717, 1073175, 1073176,
					1102964, 1004873, 1050441, 1051509, 1702726, 1073183, 1073184, 1102974, 1004881, 1004882, 1003460,
					1050442, 1051510, 1702728, 1070084, 1071101, 1102976, 1004894, 1004895, 1050444, 1051512, 1070085,
					1071102, 1102992, 1004923, 1050452, 1051519, 1702744, 1073200, 1102998, 1004947, 1004948, 1050454,
					1051521, 1702755, 1073212, 1103010, 1004954, 1004955, 1050456, 1051523, 1702759, 1070088, 1071105,
					1103013, 1004965, 1050461, 1051528, 1702766, 1070089, 1071106, 1103018, 1004975, 1050463, 1051530,
					1702770, 1073226, 1103023, 1004988, 1050464, 1051531, 1702774, 1070090, 1071107, 1103029, 1005000,
					1050468, 1051535, 1702779, 1073237, 1103035, 1005032, 1005033, 1050470, 1051537, 1702790, 1073246,
					1103050, 1005043, 1050474, 1051541, 1702795, 1070093, 1071110, 1103055, 1005065, 1005066, 1053257,
					1702804, 1073254, 1103067, 1005083, 1005084, 1050477, 1051544, 1702807, 1073255, 1103072, 1005092,
					1050481, 1051548, 1702810, 1073258, 1103074, 1005111, 1050484, 1051551, 1702815, 1070097, 1071114,
					1103079, 1005143, 1050486, 1051553, 1702826, 1073271, 1103094, 1005152, 1053305, 1702830, 1073273,
					1103096, 1005166, 1050491, 1051559, 1702837, 1073280, 1103101, 1005184, 1005185, 1050492, 1051560,
					1073290, 1702844, 1103114, 1005193, 1050495, 1051563, 1702850, 1073298, 1103118, 1005217, 1005218,
					1050499, 1051567, 1702858, 1073302, 1103130, 1005231, 1005232, 1053351, 1053352, 1702865, 1073308,
					1103138, 1005243, 1005244, 1050503, 1051573, 1702870, 1070103, 1103144, 1005260, 1005261, 1050505,
					1051575, 1702876, 1070105, 1071121, 1103148, 1005272, 1050507, 1051577, 1702882, 1073322, 1103152,
					1005280, 1005281, 1050509, 1051579, 1702887, 1070107, 1071123, 1103157, 1005319, 1050514, 1051584,
					1702901, 1073335, 1103171, 1005324, 1050516, 1051586, 1702905, 1070110, 1071126, 1103175, 1005327,
					1053416, 1702907, 1073342, 1103177, 1005354, 1053435, 1702918, 1073355, 1103185, 1005368, 1005369,
					1050523, 1051593, 1702928, 1070113, 1071129, 1082744, 1005386, 1050525, 1051595, 1702937, 1070114,
					1071130, 1103202, 1005399, 1050530, 1051601, 1702945, 1073378, 1103212, 1005412, 1050531, 1051602,
					1103219, 1073362, 1702951, 1005419, 1005420, 1050534, 1051605, 1073390, 1103221, 1702956, 1005437,
					1005438, 1050535, 1051606, 1073394, 1103224, 1702961, 1005458, 1053516, 1073402, 1103232, 1702970,
					1005477, 1005478, 1050538, 1051609, 1073415, 1103235, 1702973, 1005499, 1053543, 1073428, 1103243,
					1702981 };

			모자 = new int[] { 1000070, 1000076, 1004897, 1001093, 1001098, 1004898, 1003955, 1004450, 1004591, 1004592,
					1004777, 1005037, 1005038, 1005209, 1005210, 1005356, 1005495 };

			한벌남 = new int[] { 1050299, 1050312, 1050339, 1050356, 1050385, 1050427, 1050445, 1050472, 1050497, 1050520,
					1050542 };

			한벌여 = new int[] { 1051366, 1051384, 1051408, 1051426, 1051455, 1051495, 1051513, 1051539, 1051565, 1051589,
					1051613 };

			신발 = new int[] { 1070071, 1070080, 1070086, 1070091, 1070100, 1070111, 1071088, 1071097, 1071103, 1071108,
					1071117, 1071127, 1072860, 1072908, 1072978, 1073041, 1073425 };

			무기 = new int[] { 1702456, 1702488, 1702538, 1702565, 1702624, 1702689, 1702736, 1702786, 1702856, 1702919,
					1702976 };

			망토장갑 = new int[] { 1102729, 1102809, 1102858, 1102932, 1102988, 1103053, 1103126, 1103127, 1103187, 1103241,
					1082555, 1082580 };

			mapleItemInformationProvider5 = MapleItemInformationProvider.getInstance();
			baseitemid = slea.readInt();
			baseitempos = slea.readShort();
			slea.skip(8);
			useitemid = slea.readInt();
			useitempos = slea.readShort();
			slea.skip(8);
			equip4 = (Equip) c.getPlayer().getInventory(MapleInventoryType.CODY).getItem(baseitempos);
			equip5 = (Equip) c.getPlayer().getInventory(MapleInventoryType.CODY).getItem(useitempos);
			마라벨나올확률 = (equip4.getEquipmentType() == 1) ? 7 : (ServerConstants.ServerTest ? 100 : 5);
			MItemidselect = 0;
			Label = 0;
			own = 0;
			two = 0;
			three = 0;
			마라벨인가 = false;
			try {
				if (Randomizer.isSuccess(마라벨나올확률)) {
					if (GameConstants.isCap(baseitemid)) {
						boolean a = true;
						int Random = (int) Math.floor(Math.random() * 모자.length - 1.0D);
						MItemidselect = 모자[Random];
						if (c.getPlayer().getGender() == 0) {
							while (a) {
								if (GameConstants.여자모자(MItemidselect)) {
									Random = (int) Math.floor(Math.random() * 모자.length - 1.0D);
									MItemidselect = 모자[Random];
									continue;
								}
								a = false;
							}
						} else {
							while (a) {
								if (GameConstants.남자모자(MItemidselect)) {
									Random = (int) Math.floor(Math.random() * 모자.length - 1.0D);
									MItemidselect = 모자[Random];
									continue;
								}
								a = false;
							}
						}
					} else if (GameConstants.isLongcoat(baseitemid)) {
						if (c.getPlayer().getGender() == 0) {
							int Random = (int) Math.floor(Math.random() * 한벌남.length - 1.0D);
							MItemidselect = 한벌남[Random];
						} else {
							int Random = (int) Math.floor(Math.random() * 한벌여.length - 1.0D);
							MItemidselect = 한벌여[Random];
						}
					} else if (GameConstants.isCape(baseitemid) || GameConstants.isGlove(baseitemid)) {
						int Random = (int) Math.floor(Math.random() * 망토장갑.length - 1.0D);
						MItemidselect = 망토장갑[Random];
					} else if (GameConstants.isShoes(baseitemid)) {
						boolean a = true;
						int Random = (int) Math.floor(Math.random() * 신발.length - 1.0D);
						MItemidselect = 신발[Random];
						if (c.getPlayer().getGender() == 0) {
							while (a) {
								if (GameConstants.여자신발(MItemidselect)) {
									Random = (int) Math.floor(Math.random() * 신발.length - 1.0D);
									MItemidselect = 신발[Random];
									continue;
								}
								a = false;
							}
						} else {
							while (a) {
								if (GameConstants.남자신발(MItemidselect)) {
									Random = (int) Math.floor(Math.random() * 신발.length - 1.0D);
									MItemidselect = 신발[Random];
									continue;
								}
								a = false;
							}

						}

					} else if (GameConstants.isWeapon(baseitemid)) {
						int Random = (int) Math.floor(Math.random() * 무기.length - 1.0D);
						MItemidselect = 무기[Random];
					}
					마라벨인가 = true;
				} else {
					if (equip4.getEquipmentType() == 1) {
						Label = 2;
					} else {
						Label = 1;
					}
					if (GameConstants.isCap(baseitemid)) {
						boolean a = true;
						int Random = (int) Math.floor(Math.random() * SLabel.length);
						MItemidselect = SLabel[Random];

						if (c.getPlayer().getGender() == 0) {
							while (a) {
								if (!GameConstants.isCap(MItemidselect)) {
									Random = (int) Math.floor(Math.random() * SLabel.length);
									MItemidselect = SLabel[Random];
									continue;
								}
								if (GameConstants.여자모자(MItemidselect)) {
									Random = (int) Math.floor(Math.random() * SLabel.length);
									MItemidselect = SLabel[Random];
									continue;
								}
								a = false;
							}
						} else {
							while (a) {
								if (!GameConstants.isCap(MItemidselect)) {
									Random = (int) Math.floor(Math.random() * SLabel.length);
									MItemidselect = SLabel[Random];
									continue;
								}
								if (GameConstants.남자모자(MItemidselect)) {
									Random = (int) Math.floor(Math.random() * SLabel.length);
									MItemidselect = SLabel[Random];
									continue;
								}
								a = false;
							}
						}
					} else if (GameConstants.isLongcoat(baseitemid)) {
						boolean a = true;
						int Random = (int) Math.floor(Math.random() * SLabel.length);
						MItemidselect = SLabel[Random];

						if (c.getPlayer().getGender() == 0) {
							while (a) {

								if (!GameConstants.isLongcoat(MItemidselect)) {
									Random = (int) Math.floor(Math.random() * SLabel.length);
									MItemidselect = SLabel[Random];
									continue;
								}

								if (GameConstants.여자한벌(MItemidselect)) {
									Random = (int) Math.floor(Math.random() * SLabel.length);
									MItemidselect = SLabel[Random];
									continue;
								}
								a = false;
							}
						} else {
							while (a) {
								if (!GameConstants.isLongcoat(MItemidselect)) {
									Random = (int) Math.floor(Math.random() * SLabel.length);
									MItemidselect = SLabel[Random];
									continue;
								}
								if (GameConstants.남자한벌(MItemidselect)) {
									Random = (int) Math.floor(Math.random() * SLabel.length);
									MItemidselect = SLabel[Random];
									continue;
								}
								a = false;
							}
						}
					} else if (GameConstants.isCape(baseitemid)) {
						boolean a = true;
						int Random = (int) Math.floor(Math.random() * SLabel.length);
						MItemidselect = SLabel[Random];

						while (a) {
							if (!GameConstants.isCape(MItemidselect) && !GameConstants.isGlove(baseitemid)) {
								Random = (int) Math.floor(Math.random() * SLabel.length);
								MItemidselect = SLabel[Random];
								continue;
							}
							a = false;
						}
					} else if (GameConstants.isGlove(baseitemid)) {
						boolean a = true;
						int Random = (int) Math.floor(Math.random() * SLabel.length);
						MItemidselect = SLabel[Random];

						while (a) {
							if (!GameConstants.isGlove(MItemidselect) && !GameConstants.isCape(MItemidselect)) {
								Random = (int) Math.floor(Math.random() * SLabel.length);
								MItemidselect = SLabel[Random];
								continue;
							}
							a = false;
						}
					} else if (GameConstants.isShoes(baseitemid)) {
						boolean a = true;
						int Random = (int) Math.floor(Math.random() * SLabel.length);
						MItemidselect = SLabel[Random];
						if (c.getPlayer().getGender() == 0) {
							while (a) {
								if (!GameConstants.isShoes(MItemidselect)) {
									Random = (int) Math.floor(Math.random() * SLabel.length);
									MItemidselect = SLabel[Random];
									continue;
								}
								if (GameConstants.여자신발(MItemidselect)) {
									Random = (int) Math.floor(Math.random() * SLabel.length);
									MItemidselect = SLabel[Random];
									continue;
								}
								a = false;
							}

						} else {
							while (a) {
								if (!GameConstants.isShoes(MItemidselect)) {
									Random = (int) Math.floor(Math.random() * SLabel.length);
									MItemidselect = SLabel[Random];
									continue;
								}
								if (GameConstants.남자신발(MItemidselect)) {
									Random = (int) Math.floor(Math.random() * SLabel.length);
									MItemidselect = SLabel[Random];
									continue;
								}
								a = false;
							}
						}
					} else if (GameConstants.isWeapon(baseitemid)) {
						boolean a = true;
						while (a) {
							if (!GameConstants.isWeapon(MItemidselect)) {
								int Random = (int) Math.floor(Math.random() * SLabel.length);
								MItemidselect = SLabel[Random];
								continue;
							}
							a = false;
						}
					}
				}
				if (MItemidselect <= 0) {
					c.getPlayer().dropMessage(1, "大師裝備未準備就緒，請重試.");
					MapleMap mapleMap = c.getChannelServer().getMapFactory().getMap(c.getPlayer().getMapId());
					c.getPlayer().changeMap(mapleMap, mapleMap.getPortal(0));
					c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
					return;
				}
				Equip equip6 = (Equip) mapleItemInformationProvider5.getEquipById(MItemidselect);
				if (equip6 == null) {
					c.getPlayer().dropMessage(1, "大師裝備未準備就緒，請重試.");
					MapleMap mapleMap = c.getChannelServer().getMapFactory().getMap(c.getPlayer().getMapId());
					c.getPlayer().changeMap(mapleMap, mapleMap.getPortal(0));
					c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
					return;
				}
				Equip equip = equip6;
				equip.setEquipmentType(Label);
				if (GameConstants.isWeapon(equip.getItemId())) {
					boolean 공 = false, suc = false;
					int attack = 0, stat = 0;
					attack = Randomizer.isSuccess(50) ? 21 : 22;
					if (attack == 21) {
						공 = true;
					}
					own = attack * 1000 + (마라벨인가 ? 60 : Randomizer.rand(25, 35));
					if (Randomizer.isSuccess(60)) {
						suc = true;
						two = (공 ? 22 : 21) * 1000 + (마라벨인가 ? 60 : Randomizer.rand(25, 35));
					}
					if (Randomizer.isSuccess(10)) {
						three = (Randomizer.isSuccess(50) ? 21 : 22) * 1000 + (마라벨인가 ? 60 : Randomizer.rand(25, 35));
						if (!suc) {
							three = (공 ? 22 : 21) * 1000 + (마라벨인가 ? 60 : Randomizer.rand(25, 35));

						}
					}
				} else {
					boolean suc = false;
					int stat = 0, stat1 = 0, stat2 = 0;
					stat = Randomizer.rand(11, 14);
					own = stat * 1000 + (마라벨인가 ? 60 : Randomizer.rand(25, 35));
					if (Randomizer.isSuccess(60)) {
						suc = true;
						stat1 = Randomizer.rand(11, 14);
						while (stat == stat1) {
							stat1 = Randomizer.rand(11, 14);
						}
						two = stat1 * 1000 + (마라벨인가 ? 60 : Randomizer.rand(25, 35));
					}
					if (마라벨인가 && Randomizer.isSuccess(30)) {
						if (!suc) {
							stat1 = Randomizer.rand(11, 14);
							while (stat == stat1) {
								stat1 = Randomizer.rand(11, 14);
							}
							three = stat1 * 1000 + (마라벨인가 ? 60 : Randomizer.rand(25, 35));
						} else {
							stat2 = Randomizer.rand(11, 14);
							while (stat1 == stat2 || stat == stat2) {
								stat2 = Randomizer.rand(11, 14);
							}
							three = stat2 * 1000 + (마라벨인가 ? 60 : Randomizer.rand(25, 35));
						}
					}
				}
				if (c.getPlayer().getQuestStatus(50008) == 1) {
					c.getPlayer().setKeyValue(50008, "1", "1");
				}
				equip.setCoption1(own);
				equip.setCoption2(two);
				equip.setCoption3(three);

				int i8 = equip6.getFlag();
				if (itemId == 5069001) {
					i8 |= ItemFlag.KARMA_EQUIP.getValue();
					i8 |= ItemFlag.CHARM_EQUIPED.getValue();
				}
				equip.setUniqueId(MapleInventoryIdentifier.getInstance());
				equip.setFlag(i8);
				equip.setOptionExpiration(System.currentTimeMillis()
						+ (Randomizer.isSuccess(10) ? 60L : (Randomizer.isSuccess(30) ? 28L : 14L)) * 24L * 60L * 60L
								* 1000L);
				equip.setKarmaCount((byte) -1);
				MapleInventoryManipulator.addbyItem(c, (Item) equip);
				MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.CODY, equip4.getPosition(), (short) 1,
						false);
				MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.CODY, equip5.getPosition(), (short) 1,
						false);
				c.getSession().writeAndFlush(CSPacket.LunaCrystal((Item) equip));
				if (마라벨인가) {
					World.Broadcast
							.broadcastMessage(CWvsContext.serverMessage(11, c.getPlayer().getClient().getChannel(), "",
									c.getPlayer().getName() + "님이 프리미엄 마스터피스에서 {} 을 획득하였습니다.", true, (Item) equip));
				}
				used = true;
			} catch (Exception e) {
				c.getPlayer().dropMessage(1, "大師裝備未準備就緒，請重試.");
				MapleMap mapleMap = c.getChannelServer().getMapFactory().getMap(c.getPlayer().getMapId());
				c.getPlayer().changeMap(mapleMap, mapleMap.getPortal(0));
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				return;
			}
			break;
		case 5060001:
			type = MapleInventoryType.getByType((byte) slea.readInt());
			item4 = c.getPlayer().getInventory(type).getItem((short) slea.readInt());
			if (item4 != null && item4.getExpiration() == -1L) {
				int i8 = item4.getFlag();
				i8 |= ItemFlag.LOCK.getValue();
				item4.setFlag(i8);
				c.getSession().writeAndFlush(
						CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIP, item4));
				used = true;
			}
			break;
		case 5061000:
			type = MapleInventoryType.getByType((byte) slea.readInt());
			item4 = c.getPlayer().getInventory(type).getItem((short) slea.readInt());
			if (item4 != null && item4.getExpiration() == -1L) {
				int i8 = item4.getFlag();
				i8 |= ItemFlag.LOCK.getValue();
				item4.setFlag(i8);
				item4.setExpiration(System.currentTimeMillis() + 604800000L);
				c.getSession().writeAndFlush(
						CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIP, item4));
				used = true;
			}
			break;
		case 5061001:
			type = MapleInventoryType.getByType((byte) slea.readInt());
			item4 = c.getPlayer().getInventory(type).getItem((short) slea.readInt());
			if (item4 != null && item4.getExpiration() == -1L) {
				int i8 = item4.getFlag();
				i8 |= ItemFlag.LOCK.getValue();
				item4.setFlag(i8);
				item4.setExpiration(System.currentTimeMillis() + -1702967296L);
				c.getSession().writeAndFlush(
						CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIP, item4));
				used = true;
			}
			break;

		case 5061002:
			type = MapleInventoryType.getByType((byte) slea.readInt());
			item4 = c.getPlayer().getInventory(type).getItem((short) slea.readInt());

			if (item4 != null && item4.getExpiration() == -1L) {
				int i8 = item4.getFlag();
				i8 |= ItemFlag.LOCK.getValue();
				item4.setFlag(i8);
				item4.setExpiration(System.currentTimeMillis() + -813934592L);
				c.getSession().writeAndFlush(
						CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIP, item4));
				used = true;
			}
			break;
		case 5061003:
			type = MapleInventoryType.getByType((byte) slea.readInt());
			item4 = c.getPlayer().getInventory(type).getItem((short) slea.readInt());
			if (item4 != null && item4.getExpiration() == -1L) {
				int i8 = item4.getFlag();
				i8 |= ItemFlag.LOCK.getValue();
				item4.setFlag(i8);
				item4.setExpiration(System.currentTimeMillis() + 1471228928L);
				c.getSession().writeAndFlush(
						CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIP, item4));
				used = true;
			}
			break;
		case 5063000:
			type = MapleInventoryType.getByType((byte) slea.readInt());
			item4 = c.getPlayer().getInventory(type).getItem((short) slea.readInt());
			if (item4 != null && item4.getType() == 1) {
				int i8 = item4.getFlag();
				i8 |= ItemFlag.LUCKY_PROTECT_SHIELD.getValue();
				item4.setFlag(i8);
				c.getSession().writeAndFlush(
						CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIP, item4));
				used = true;
			}
			break;
		case 5064000:
			s2 = slea.readShort();
			if (s2 < 0) {
				toScroll = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(s2);
			} else {
				toScroll = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(s2);
			}
			if (toScroll.getEnhance() >= 12) {
				break;
			}
			flag = toScroll.getFlag();
			flag |= ItemFlag.PROTECT_SHIELD.getValue();
			toScroll.setFlag(flag);
			c.getSession().writeAndFlush(
					CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIP, (Item) toScroll));
			c.getPlayer().getMap().broadcastMessage(c.getPlayer(), CField.getScrollEffect(c.getPlayer().getId(),
					Equip.ScrollResult.SUCCESS, false, toUse.getItemId(), toScroll.getItemId()), true);
			used = true;
			break;
		case 5064100:
			s1 = slea.readShort();
			if (s1 < 0) {
				toScroll = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(s1);
			} else {
				toScroll = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(s1);
			}
			if (toScroll.getUpgradeSlots() == 0) {
				break;
			}
			flag = toScroll.getFlag();
			flag |= ItemFlag.SAFETY_SHIELD.getValue();
			toScroll.setFlag(flag);
			c.getSession().writeAndFlush(
					CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIP, (Item) toScroll));
			c.getPlayer().getMap().broadcastMessage(c.getPlayer(), CField.getScrollEffect(c.getPlayer().getId(),
					Equip.ScrollResult.SUCCESS, false, toUse.getItemId(), toScroll.getItemId()), true);
			used = true;
			break;
		case 5064300:
			dst = slea.readShort();
			if (dst < 0) {
				toScroll = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(dst);
			} else {
				toScroll = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(dst);
			}
			flag = toScroll.getFlag();
			flag |= ItemFlag.RECOVERY_SHIELD.getValue();
			toScroll.setFlag(flag);
			c.getSession().writeAndFlush(
					CWvsContext.InventoryPacket.addInventorySlot(MapleInventoryType.EQUIP, (Item) toScroll));
			c.getSession().writeAndFlush(
					CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIP, (Item) toScroll));
			c.getPlayer().getMap().broadcastMessage(c.getPlayer(), CField.getScrollEffect(c.getPlayer().getId(),
					Equip.ScrollResult.SUCCESS, false, toUse.getItemId(), toScroll.getItemId()), true);
			used = true;
			break;
		case 5064400:
			dst = slea.readShort();
			if (dst < 0) {
				toScroll = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(dst);
			} else {
				toScroll = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(dst);
			}
			flag = toScroll.getFlag();
			flag |= ItemFlag.RETURN_SCROLL.getValue();
			toScroll.setFlag(flag);
			c.getSession().writeAndFlush(
					CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIP, (Item) toScroll));
			c.getPlayer().getMap().broadcastMessage(c.getPlayer(), CField.getScrollEffect(c.getPlayer().getId(),
					Equip.ScrollResult.SUCCESS, false, toUse.getItemId(), toScroll.getItemId()), true);
			used = true;
			break;
		case 5060003:
		case 5060004:
			item = c.getPlayer().getInventory(MapleInventoryType.ETC).findById((itemId == 5060003) ? 4170023 : 4170024);
			if (item == null || item.getQuantity() <= 0) {
				return;
			}
			break;
		case 5070000:
			if (c.getPlayer().getLevel() < 10) {
				c.getPlayer().dropMessage(5, "Must be level 10 or higher.");
				break;
			}
			if (c.getPlayer().getMapId() == 180000002) {
				c.getPlayer().dropMessage(5, "Cannot be used here.");
				break;
			}
			if (!c.getChannelServer().getMegaphoneMuteState()) {
				String str = slea.readMapleAsciiString();

				if (str.length() > 65) {
					break;
				}
				StringBuilder stringBuilder = new StringBuilder();
				addMedalString(c.getPlayer(), stringBuilder);
				stringBuilder.append(c.getPlayer().getName());
				stringBuilder.append(" : ");
				stringBuilder.append(str);
				c.getPlayer().getMap().broadcastMessage(
						CWvsContext.serverNotice(2, c.getPlayer().getName(), stringBuilder.toString()));
				DBLogger.getInstance().logChat(LogType.Chat.Megaphone, c.getPlayer().getId(), c.getPlayer().getName(),
						str, "채널 : " + c.getChannel());
				used = true;
				break;
			}
			c.getPlayer().dropMessage(5, "The usage of Megaphone is currently disabled.");
			break;
		case 5071000:
			if (c.getPlayer().getLevel() < 10) {
				c.getPlayer().dropMessage(5, "Must be level 10 or higher.");
				break;
			}
			if (c.getPlayer().getMapId() == 180000002) {
				c.getPlayer().dropMessage(5, "Cannot be used here.");
				break;
			}
			if (!c.getChannelServer().getMegaphoneMuteState()) {
				String str = slea.readMapleAsciiString();

				if (str.length() > 65) {
					break;
				}
				StringBuilder stringBuilder = new StringBuilder();
				addMedalString(c.getPlayer(), stringBuilder);
				stringBuilder.append(c.getPlayer().getName());
				stringBuilder.append(" : ");
				stringBuilder.append(str);

				if (System.currentTimeMillis() - World.Broadcast.chatDelay >= 3000L) {
					World.Broadcast.chatDelay = System.currentTimeMillis();
					c.getChannelServer().broadcastSmegaPacket(
							CWvsContext.serverNotice(2, c.getPlayer().getName(), stringBuilder.toString()));
					DBLogger.getInstance().logChat(LogType.Chat.Megaphone, c.getPlayer().getId(),
							c.getPlayer().getName(), str, "채널 : " + c.getChannel());
					used = true;
					break;
				}
				c.getSession().writeAndFlush(CWvsContext.serverNotice(1, "", "전체 채팅은 3초마다 하실 수 있습니다."));
				break;
			}
			c.getPlayer().dropMessage(5, "The usage of Megaphone is currently disabled.");
			break;
		case 5077000:
			if (c.getPlayer().getLevel() < 10) {
				c.getPlayer().dropMessage(5, "Must be level 10 or higher.");
				break;
			}
			if (c.getPlayer().getMapId() == 180000002) {
				c.getPlayer().dropMessage(5, "Cannot be used here.");
				break;
			}
			if (!c.getChannelServer().getMegaphoneMuteState()) {
				byte numLines = slea.readByte();
				if (numLines > 3) {
					return;
				}
				List<String> messages = new LinkedList<>();

				for (int i8 = 0; i8 < numLines; i8++) {
					String str = slea.readMapleAsciiString();

					if (str.length() > 65) {
						break;
					}
					DBLogger.getInstance().logChat(LogType.Chat.Megaphone, c.getPlayer().getId(),
							c.getPlayer().getName(), str, "채널 : " + c.getChannel());
					messages.add(c.getPlayer().getName() + " : " + str);
				}

				boolean bool = (slea.readByte() > 0);
				if (System.currentTimeMillis() - World.Broadcast.chatDelay >= 3000L) {
					World.Broadcast.chatDelay = System.currentTimeMillis();
					World.Broadcast.broadcastSmega(
							CWvsContext.tripleSmega(c.getPlayer().getName(), messages, bool, c.getChannel()));
					used = true;
					break;
				}
				c.getSession().writeAndFlush(CWvsContext.serverNotice(1, "", "전체 채팅은 3초마다 하실 수 있습니다."));
				break;
			}
			c.getPlayer().dropMessage(5, "The usage of Megaphone is currently disabled.");
			break;
		case 5079004:
			if (c.getPlayer().getLevel() < 10) {
				c.getPlayer().dropMessage(5, "Must be level 10 or higher.");
				break;
			}
			if (c.getPlayer().getMapId() == 180000002) {
				c.getPlayer().dropMessage(5, "Cannot be used here.");
				break;
			}
			if (!c.getChannelServer().getMegaphoneMuteState()) {
				String str = slea.readMapleAsciiString();

				if (str.length() > 65) {
					break;
				}
				if (System.currentTimeMillis() - World.Broadcast.chatDelay >= 3000L) {
					World.Broadcast.chatDelay = System.currentTimeMillis();
					World.Broadcast.broadcastSmega(CWvsContext.echoMegaphone(c.getPlayer().getName(), str));
					DBLogger.getInstance().logChat(LogType.Chat.Megaphone, c.getPlayer().getId(),
							c.getPlayer().getName(), str, "채널 : " + c.getChannel());
					used = true;
					break;
				}
				c.getSession().writeAndFlush(CWvsContext.serverNotice(1, "", "전체 채팅은 3초마다 하실 수 있습니다."));
				break;
			}
			c.getPlayer().dropMessage(5, "The usage of Megaphone is currently disabled.");
			break;
		case 5073000:
			if (c.getPlayer().getLevel() < 10) {
				c.getPlayer().dropMessage(5, "Must be level 10 or higher.");
				break;
			}
			if (c.getPlayer().getMapId() == 180000002) {
				c.getPlayer().dropMessage(5, "Cannot be used here.");
				break;
			}
			if (!c.getChannelServer().getMegaphoneMuteState()) {
				String str = slea.readMapleAsciiString();

				if (str.length() > 65) {
					break;
				}
				StringBuilder stringBuilder = new StringBuilder();
				addMedalString(c.getPlayer(), stringBuilder);
				stringBuilder.append(c.getPlayer().getName());
				stringBuilder.append(" : ");
				stringBuilder.append(str);
				boolean bool = (slea.readByte() != 0);

				if (System.currentTimeMillis() - World.Broadcast.chatDelay >= 3000L) {
					World.Broadcast.chatDelay = System.currentTimeMillis();
					DBLogger.getInstance().logChat(LogType.Chat.Megaphone, c.getPlayer().getId(),
							c.getPlayer().getName(), str, "채널 : " + c.getChannel());
					World.Broadcast.broadcastSmega(CWvsContext.serverNotice(9, c.getChannel(), c.getPlayer().getName(),
							stringBuilder.toString(), bool));
					used = true;
					break;
				}
				c.getSession().writeAndFlush(CWvsContext.serverNotice(1, "", "전체 채팅은 3초마다 하실 수 있습니다."));
				break;
			}
			c.getPlayer().dropMessage(5, "The usage of Megaphone is currently disabled.");
			break;
		case 5074000:
			if (c.getPlayer().getLevel() < 10) {
				c.getPlayer().dropMessage(5, "Must be level 10 or higher.");
				break;
			}
			if (c.getPlayer().getMapId() == 180000002) {
				c.getPlayer().dropMessage(5, "Cannot be used here.");
				break;
			}
			if (!c.getChannelServer().getMegaphoneMuteState()) {
				String str = slea.readMapleAsciiString();

				if (str.length() > 65) {
					break;
				}
				StringBuilder stringBuilder = new StringBuilder();
				addMedalString(c.getPlayer(), stringBuilder);
				stringBuilder.append(c.getPlayer().getName());
				stringBuilder.append(" : ");
				stringBuilder.append(str);

				boolean bool = (slea.readByte() != 0);

				if (System.currentTimeMillis() - World.Broadcast.chatDelay >= 3000L) {
					World.Broadcast.chatDelay = System.currentTimeMillis();
					DBLogger.getInstance().logChat(LogType.Chat.Megaphone, c.getPlayer().getId(),
							c.getPlayer().getName(), str, "채널 : " + c.getChannel());
					World.Broadcast.broadcastSmega(CWvsContext.serverNotice(22, c.getChannel(), c.getPlayer().getName(),
							stringBuilder.toString(), bool));
					used = true;
					break;
				}
				c.getSession().writeAndFlush(CWvsContext.serverNotice(1, "", "전체 채팅은 3초마다 하실 수 있습니다."));
				break;
			}
			c.getPlayer().dropMessage(5, "The usage of Megaphone is currently disabled.");
			break;
		case 5072000:
			if (c.getPlayer().getLevel() < 10) {
				c.getPlayer().dropMessage(5, "Must be level 10 or higher.");
				break;
			}
			if (c.getPlayer().getMapId() == 180000002) {
				c.getPlayer().dropMessage(5, "Cannot be used here.");
				break;
			}
			if (!c.getChannelServer().getMegaphoneMuteState()) {
				String str = slea.readMapleAsciiString();
				if (str.length() > 65) {
					break;
				}
				StringBuilder stringBuilder = new StringBuilder();
				addMedalString(c.getPlayer(), stringBuilder);
				stringBuilder.append(c.getPlayer().getName());
				stringBuilder.append(" : ");
				stringBuilder.append(str);

				boolean bool = (slea.readByte() != 0);

				if (System.currentTimeMillis() - World.Broadcast.chatDelay >= 3000L) {
					World.Broadcast.chatDelay = System.currentTimeMillis();
					DBLogger.getInstance().logChat(LogType.Chat.Megaphone, c.getPlayer().getId(),
							c.getPlayer().getName(), str, "채널 : " + c.getChannel());
					World.Broadcast.broadcastSmega(CWvsContext.serverNotice(3, c.getChannel(), c.getPlayer().getName(),
							stringBuilder.toString(), bool));
					used = true;
					break;
				}
				c.getSession().writeAndFlush(CWvsContext.serverNotice(1, "", "전체 채팅은 3초마다 하실 수 있습니다."));
				break;
			}
			c.getPlayer().dropMessage(5, "The usage of Megaphone is currently disabled.");
			break;
		case 5076000:
			if (c.getPlayer().getLevel() < 10) {
				c.getPlayer().dropMessage(5, "Must be level 10 or higher.");
				break;
			}

			if (c.getPlayer().getMapId() == 180000002) {
				c.getPlayer().dropMessage(5, "Cannot be used here.");
				break;
			}

			if (!c.getChannelServer().getMegaphoneMuteState()) {
				String str = slea.readMapleAsciiString();

				if (str.length() > 65) {
					break;
				}

				StringBuilder stringBuilder = new StringBuilder();
				addMedalString(c.getPlayer(), stringBuilder);
				stringBuilder.append(c.getPlayer().getName());
				stringBuilder.append(" : ");
				stringBuilder.append(str);

				boolean bool = (slea.readByte() > 0);

				Item item13 = null;
				if (slea.readByte() == 1) {
					byte invType = (byte) slea.readInt();
					byte b1 = (byte) slea.readInt();
					if (b1 <= 0) {
						invType = -1;
					}
					item13 = c.getPlayer().getInventory(MapleInventoryType.getByType(invType)).getItem((short) b1);
				}
				if (System.currentTimeMillis() - World.Broadcast.chatDelay >= 3000L) {
					World.Broadcast.chatDelay = System.currentTimeMillis();
					DBLogger.getInstance().logChat(LogType.Chat.Megaphone, c.getPlayer().getId(),
							c.getPlayer().getName(), str, "채널 : " + c.getChannel());
					World.Broadcast.broadcastSmega(CWvsContext.itemMegaphone(c.getPlayer().getName(),
							stringBuilder.toString(), bool, c.getChannel(), item13, itemId));
					used = true;
					break;
				}
				c.getSession().writeAndFlush(CWvsContext.serverNotice(1, "", "전체 채팅은 3초마다 하실 수 있습니다."));
				break;
			}
			c.getPlayer().dropMessage(5, "The usage of Megaphone is currently disabled.");
			break;
		case 5076100: {
			message = slea.readMapleAsciiString();
			sb = new StringBuilder();
			addMedalString(c.getPlayer(), sb);
			sb.append(c.getPlayer().getName());
			sb.append(" : ");
			sb.append(message);
			ear = slea.readByte() > 0;
			item = null;
			if (slea.readInt() == 1) { // item
				byte invType = (byte) slea.readInt();
				byte pos2 = (byte) slea.readInt();
				if (pos2 <= 0) {
					invType = -1;
				}
				item = c.getPlayer().getInventory(MapleInventoryType.getByType(invType)).getItem(pos2);
			}
			World.Broadcast.broadcastSmega(CWvsContext.HyperMegaPhone(sb.toString(), c.getPlayer().getName(), message,
					c.getChannel(), ear, item));
			used = true;
			break;
		}
		case 5075003:
		case 5075004:
		case 5075005:
			if (c.getPlayer().getLevel() < 10) {
				c.getPlayer().dropMessage(5, "Must be level 10 or higher.");
				break;
			}
			if (c.getPlayer().getMapId() == 180000002) {
				c.getPlayer().dropMessage(5, "Cannot be used here.");
				break;
			}
			tvType = itemId % 10;
			if (tvType == 3) {
				slea.readByte();
			}
			ear = (tvType != 1 && tvType != 2 && slea.readByte() > 1);
			victim = (tvType == 1 || tvType == 4) ? null
					: c.getChannelServer().getPlayerStorage().getCharacterByName(slea.readMapleAsciiString());
			if (tvType == 0 || tvType == 3) {
				victim = null;
			} else if (victim == null) {
				c.getPlayer().dropMessage(1, "That character is not in the channel.");
				break;
			}
			str1 = slea.readMapleAsciiString();

			if (System.currentTimeMillis() - World.Broadcast.chatDelay >= 3000L) {
				World.Broadcast.chatDelay = System.currentTimeMillis();
				DBLogger.getInstance().logChat(LogType.Chat.Megaphone, c.getPlayer().getId(), c.getPlayer().getName(),
						str1, "채널 : " + c.getChannel());
				World.Broadcast.broadcastSmega(CWvsContext.serverNotice(3, c.getChannel(), c.getPlayer().getName(),
						c.getPlayer().getName() + " : " + str1, ear));
				used = true;
				break;
			}
			c.getSession().writeAndFlush(CWvsContext.serverNotice(1, "", "전체 채팅은 3초마다 하실 수 있습니다."));
			break;
		case 5100000:
			c.getPlayer().getMap().broadcastMessage(CField.musicChange("Jukebox/Congratulation"));
			used = true;
			break;
		case 5190000:
		case 5190001:
		case 5190002:
		case 5190003:
		case 5190004:
		case 5190005:
		case 5190006:
		case 5190010:
		case 5190011:
		case 5190012:
		case 5190013:
			uniqueId = slea.readLong();
			maplePet1 = null;
			petIndex = c.getPlayer().getPetIndex(uniqueId);
			if (petIndex >= 0) {
				maplePet1 = c.getPlayer().getPet(petIndex);
			} else {
				maplePet1 = c.getPlayer().getInventory(MapleInventoryType.CASH).findByUniqueId(uniqueId).getPet();
			}
			if (maplePet1 == null) {
				c.getPlayer().dropMessage(1, "尋找寵物失敗!");
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				return;
			}
			zz = MaplePet.PetFlag.getByAddId(itemId);
			maplePet1.setFlags(maplePet1.getFlags() | zz.getValue());
			c.getPlayer().getMap().broadcastMessage(PetPacket.updatePet(c.getPlayer(), maplePet1,
					c.getPlayer().getInventory(MapleInventoryType.CASH).getItem(maplePet1.getInventoryPosition()),
					false, c.getPlayer().getPetLoot()));
			used = true;
			break;
		case 5191000:
		case 5191001:
		case 5191002:
		case 5191003:
		case 5191004:
			uniqueId = slea.readLong();
			maplePet1 = null;
			petIndex = c.getPlayer().getPetIndex(uniqueId);

			if (petIndex >= 0) {
				maplePet1 = c.getPlayer().getPet(petIndex);
			} else {
				maplePet1 = c.getPlayer().getInventory(MapleInventoryType.CASH).findByUniqueId(uniqueId).getPet();
			}
			if (maplePet1 == null) {
				c.getPlayer().dropMessage(1, "尋找寵物失敗!");
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				return;
			}
			zz = MaplePet.PetFlag.getByAddId(itemId);
			maplePet1.setFlags(maplePet1.getFlags() - zz.getValue());
			c.getPlayer().getMap().broadcastMessage(PetPacket.updatePet(c.getPlayer(), maplePet1,
					c.getPlayer().getInventory(MapleInventoryType.CASH).getItem(maplePet1.getInventoryPosition()),
					false, c.getPlayer().getPetLoot()));
			used = true;
			break;
		case 5781002:
			l1 = slea.readLong();
			color = slea.readInt();
			maplePet2 = c.getPlayer().getPet(0L);
			i3 = 0;
			if (maplePet2 == null) {
				break;
			}
			if (maplePet2.getUniqueId() != l1) {
				maplePet2 = c.getPlayer().getPet(1L);
				i3 = 1;
				if (maplePet2 != null) {
					if (maplePet2.getUniqueId() != l1) {
						maplePet2 = c.getPlayer().getPet(2L);
						i3 = 2;
						if (maplePet2 == null || maplePet2.getUniqueId() != l1) {
							break;
						}
					}
				} else {
					break;
				}
			}
			maplePet2.setColor(color);
			c.getPlayer().getMap().broadcastMessage(c.getPlayer(),
					PetPacket.showPet(c.getPlayer(), maplePet2, false, false), true);
			break;
		case 5501001:
		case 5501002:
			skil = SkillFactory.getSkill(slea.readInt());
			if (skil == null || skil.getId() / 10000 != 8000 || c.getPlayer().getSkillLevel(skil) <= 0
					|| !skil.isTimeLimited() || GameConstants.getMountItem(skil.getId(), c.getPlayer()) <= 0) {
				break;
			}
			toAdd = (((itemId == 5501001) ? 30 : 60) * 24 * 60 * 60) * 1000L;
			expire = c.getPlayer().getSkillExpiry(skil);
			if (expire < System.currentTimeMillis() || expire + toAdd >= System.currentTimeMillis() + 31536000000L) {
				break;
			}
			c.getPlayer().changeSingleSkillLevel(skil, c.getPlayer().getSkillLevel(skil),
					c.getPlayer().getMasterLevel(skil), expire + toAdd);
			used = true;
			break;
		case 5170000:
			uniqueid = slea.readLong();
			pet = c.getPlayer().getPet(0L);
			slo = 0;
			if (pet == null) {
				break;
			}
			if (pet.getUniqueId() != uniqueid) {
				pet = c.getPlayer().getPet(1L);
				slo = 1;
				if (pet != null) {
					if (pet.getUniqueId() != uniqueid) {
						pet = c.getPlayer().getPet(2L);
						slo = 2;
						if (pet == null || pet.getUniqueId() != uniqueid) {
							break;
						}
					}
				} else {
					break;
				}
			}
			str2 = slea.readMapleAsciiString();
			for (String z : GameConstants.RESERVED) {
				if (pet.getName().indexOf(z) != -1 || str2.indexOf(z) != -1) {
					break;
				}
			}
			pet.setName(str2);
			c.getSession()
					.writeAndFlush(PetPacket.updatePet(c.getPlayer(), pet,
							c.getPlayer().getInventory(MapleInventoryType.CASH).getItem(pet.getInventoryPosition()),
							false, c.getPlayer().getPetLoot()));
			c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
			c.getPlayer().getMap().broadcastMessage(CSPacket.changePetName(c.getPlayer(), str2, slo));
			used = true;
			break;
		case 5700000:
			slea.skip(8);
			if (c.getPlayer().getAndroid() == null) {
				c.getPlayer().dropMessage(1, "未裝備安卓機器人，無法命名.");
				break;
			}
			nName = slea.readMapleAsciiString();
			for (String z : GameConstants.RESERVED) {
				if (c.getPlayer().getAndroid().getName().indexOf(z) != -1 || nName.indexOf(z) != -1) {
					break;
				}
			}
			c.getPlayer().getAndroid().setName(nName);
			c.getPlayer().setAndroid(c.getPlayer().getAndroid());
			used = true;
			break;
		case 5240000:
		case 5240001:
		case 5240002:
		case 5240003:
		case 5240004:
		case 5240005:
		case 5240006:
		case 5240007:
		case 5240008:
		case 5240009:
		case 5240010:
		case 5240011:
		case 5240012:
		case 5240013:
		case 5240014:
		case 5240015:
		case 5240016:
		case 5240017:
		case 5240018:
		case 5240019:
		case 5240020:
		case 5240021:
		case 5240022:
		case 5240023:
		case 5240024:
		case 5240025:
		case 5240026:
		case 5240027:
		case 5240028:
		case 5240029:
		case 5240030:
		case 5240031:
		case 5240032:
		case 5240033:
		case 5240034:
		case 5240035:
		case 5240036:
		case 5240037:
		case 5240038:
		case 5240039:
		case 5240040:
		case 5240088:
			for (MaplePet maplePet : c.getPlayer().getPets()) {
				if (maplePet != null && !maplePet.canConsume(itemId)) {
					int petindex = c.getPlayer().getPetIndex(maplePet);
					maplePet.setFullness(100);
					if (maplePet.getCloseness() < 30000) {
						if (maplePet.getCloseness() + 100 > 30000) {
							maplePet.setCloseness(30000);
						} else {
							maplePet.setCloseness(maplePet.getCloseness() + 100);
						}
						if (maplePet.getCloseness() >= GameConstants
								.getClosenessNeededForLevel(maplePet.getLevel() + 1)) {
							maplePet.setLevel(maplePet.getLevel() + 1);
							c.getSession().writeAndFlush(CField.EffectPacket.showPetLevelUpEffect(c.getPlayer(),
									maplePet.getPetItemId(), true));
							c.getPlayer().getMap().broadcastMessage(CField.EffectPacket
									.showPetLevelUpEffect(c.getPlayer(), maplePet.getPetItemId(), false));
						}
					}
					c.getSession()
							.writeAndFlush(PetPacket.updatePet(
									c.getPlayer(), maplePet, c.getPlayer().getInventory(MapleInventoryType.CASH)
											.getItem(maplePet.getInventoryPosition()),
									false, c.getPlayer().getPetLoot()));
					c.getPlayer().getMap().broadcastMessage(c.getPlayer(),
							PetPacket.commandResponse(c.getPlayer().getId(), (byte) 1, (byte) petindex, true), true);
				}
			}
			used = true;
			break;
		case 5370000:
		case 5370001:
			c.getPlayer().setChalkboard(slea.readMapleAsciiString());
			break;
		case 5079000:
		case 5079001:
		case 5390000:
		case 5390001:
		case 5390002:
		case 5390003:
		case 5390004:
		case 5390005:
		case 5390006:
		case 5390007:
		case 5390008:
		case 5390009:
		case 5390010:
		case 5390011:
		case 5390012:
		case 5390013:
		case 5390014:
		case 5390015:
		case 5390016:
		case 5390017:
		case 5390018:
		case 5390019:
		case 5390020:
		case 5390021:
		case 5390022:
		case 5390023:
		case 5390024:
		case 5390025:
		case 5390026:
		case 5390027:
		case 5390028:
		case 5390029:
		case 5390030:
		case 5390031:
		case 5390032:
		case 5390033:
			if (c.getPlayer().getLevel() < 10) {
				c.getPlayer().dropMessage(5, "10 레벨 이상이어야합니다.");
				break;
			}
			if (c.getPlayer().getMapId() == 180000002) {
				c.getPlayer().dropMessage(5, "여기에서는 사용하실 수 없습니다.");
				break;
			}
			if (!c.getChannelServer().getMegaphoneMuteState()) {
				List<String> lines = new LinkedList<>();
				StringBuilder stringBuilder = new StringBuilder();
				for (int i8 = 0; i8 < 4; i8++) {
					String text = slea.readMapleAsciiString();
					if (text.length() > 55) {
						lines.add("");
					} else {
						lines.add(text);
						stringBuilder.append(text);
					}
				}
				boolean bool = (slea.readByte() != 0);
				if (System.currentTimeMillis() - World.Broadcast.chatDelay >= 3000L) {
					World.Broadcast.chatDelay = System.currentTimeMillis();
					DBLogger.getInstance().logChat(LogType.Chat.Megaphone, c.getPlayer().getId(),
							c.getPlayer().getName(), stringBuilder.toString(), "채널 : " + c.getChannel());
					World.Broadcast.broadcastSmega(
							CWvsContext.getAvatarMega(c.getPlayer(), c.getChannel(), itemId, lines, bool));
					used = true;
					break;
				}
				c.getSession().writeAndFlush(CWvsContext.serverNotice(1, "", "전체 채팅은 3초마다 하실 수 있습니다."));
				break;
			}
			c.getPlayer().dropMessage(5, "The usage of Megaphone is currently disabled.");
			break;
		case 5450000:
		case 5450003:
		case 5452001:
			for (int i8 : GameConstants.blockedMaps) {
				if (c.getPlayer().getMapId() == i8) {
					c.getPlayer().dropMessage(5, "You may not use this command here.");
					c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
					return;
				}
			}
			if (c.getPlayer().getLevel() < 10) {
				c.getPlayer().dropMessage(5, "You must be over level 10 to use this command.");
				break;
			}
			if ((c.getPlayer().getMapId() >= 680000210 && c.getPlayer().getMapId() <= 680000502)
					|| (c.getPlayer().getMapId() / 1000 == 980000 && c.getPlayer().getMapId() != 980000000)
					|| c.getPlayer().getMapId() / 100 == 1030008 || c.getPlayer().getMapId() / 100 == 922010
					|| c.getPlayer().getMapId() / 10 == 13003000) {
				c.getPlayer().dropMessage(5, "You may not use this command here.");
				break;
			}
			MapleShopFactory.getInstance().getShop(61).sendShop(c);
			break;
		case 5300000:
		case 5300001:
		case 5300002:
			ii = MapleItemInformationProvider.getInstance();
			ii.getItemEffect(itemId).applyTo(c.getPlayer(), true);
			used = true;
			break;
		case 5330000:
			c.getPlayer().setConversation(2);
			c.getSession().writeAndFlush(CField.sendDuey((byte) 9, null, null));
			break;
		case 5062400:
		case 5062402:
		case 5062405:
			viewSlot = (short) slea.readInt();
			descSlot = (short) slea.readInt();
			view_Item = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(viewSlot);
			desc_Item = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(descSlot);
			if (view_Item.getMoru() != 0) {
				desc_Item.setMoru(view_Item.getMoru());
			} else {
				String lol = Integer.valueOf(view_Item.getItemId()).toString();
				String ss = lol.substring(3, 7);
				desc_Item.setMoru(Integer.parseInt(ss));
			}
			c.getPlayer().forceReAddItem((Item) desc_Item, MapleInventoryType.EQUIP);
			used = true;
			break;
		case 5130000:
			if (c.getPlayer().getKeyValue(210416, "TotalDeadTime") <= 0L) {
				c.getPlayer().dropMessage(5, "因角色死亡獲得經驗埴,只有在適用掉落率降低效果時才能使用.");
				c.send(CWvsContext.enableActions(c.getPlayer()));
				break;
			}
			c.send(CField.ExpDropPenalty(false, 0, 0, 0, 0));
			c.getPlayer().removeKeyValue(210416);
			used = true;
			break;
		case 5155006:
			if (c.getPlayer().getKeyValue(7786, "sw") == 0L) {
				c.getPlayer().setKeyValue(7786, "sw", "1");
				c.getPlayer().dropMessage(5, "신비한 힘으로 심연의 세례가 드러났습니다.");
			} else {
				c.getPlayer().setKeyValue(7786, "sw", "0");
				c.getPlayer().dropMessage(5, "신비한 힘으로 심연의 세례가 감춰졌습니다.");
			}
			used = true;
			break;
		case 5155002:
			if (c.getPlayer().getKeyValue(7786, "sw") == 0L) {
				c.getPlayer().setKeyValue(7786, "sw", "1");
				c.getPlayer().dropMessage(5, "신비한 힘으로 제너레이트 마크가 드러났습니다.");
			} else {
				c.getPlayer().setKeyValue(7786, "sw", "0");
				c.getPlayer().dropMessage(5, "신비한 힘으로 제너레이트 마크가 감춰졌습니다.");
			}
			used = true;
			break;
		case 5155003:
			if (c.getPlayer().getKeyValue(7786, "sw") == 0L) {
				c.getPlayer().setKeyValue(7786, "sw", "1");
				c.getPlayer().dropMessage(5, "신비한 힘으로 마족의 표식이 드러났습니다.");
			} else {
				c.getPlayer().setKeyValue(7786, "sw", "0");
				c.getPlayer().dropMessage(5, "신비한 힘으로 마족의 표식이 감춰졌습니다.");
			}
			used = true;
			break;
		default:
			if (itemId / 10000 == 512 || itemId == 2432290) {
				MapleItemInformationProvider mapleItemInformationProvider = MapleItemInformationProvider.getInstance();

				String msg = mapleItemInformationProvider.getMsg(itemId);

				String ourMsg = slea.readMapleAsciiString();

				c.getPlayer().getMap().startMapEffect(ourMsg, itemId);

				int buff = mapleItemInformationProvider.getStateChangeItem(itemId);

				if (buff != 0) {

					for (MapleCharacter mChar : c.getPlayer().getMap().getCharactersThreadsafe()) {

						mapleItemInformationProvider.getItemEffect(buff).applyTo(mChar, true);
					}
				}

				used = true;
				break;
			}
			if (itemId / 10000 == 510) {
				c.getPlayer().getMap().startJukebox(c.getPlayer().getName(), itemId);
				used = true;
				break;
			}
			if (itemId / 10000 == 562) {
				if (UseSkillBook(slot, itemId, c, c.getPlayer())) {
					c.getPlayer().gainSP(1);
				}
				break;
			}
			if (itemId / 10000 == 553) {
				UseRewardItem(slot, itemId, c, c.getPlayer());
				break;

			}
			if (itemId / 10000 == 524) {
				for (MaplePet maplePet : c.getPlayer().getPets()) {
					if (maplePet != null && maplePet.canConsume(itemId)) {
						int petindex = c.getPlayer().getPetIndex(maplePet);
						maplePet.setFullness(100);
						if (maplePet.getCloseness() < 30000) {
							if (maplePet.getCloseness() + 100 > 30000) {
								maplePet.setCloseness(30000);
							} else {
								maplePet.setCloseness(maplePet.getCloseness() + 100);
							}
							if (maplePet.getCloseness() >= GameConstants
									.getClosenessNeededForLevel(maplePet.getLevel() + 1)) {
								maplePet.setLevel(maplePet.getLevel() + 1);
								c.getSession().writeAndFlush(CField.EffectPacket.showPetLevelUpEffect(c.getPlayer(),
										maplePet.getPetItemId(), true));
								c.getPlayer().getMap().broadcastMessage(CField.EffectPacket
										.showPetLevelUpEffect(c.getPlayer(), maplePet.getPetItemId(), false));
							}
						}
						c.getSession()
								.writeAndFlush(PetPacket.updatePet(
										c.getPlayer(), maplePet, c.getPlayer().getInventory(MapleInventoryType.CASH)
												.getItem(maplePet.getInventoryPosition()),
										false, c.getPlayer().getPetLoot()));
						c.getPlayer().getMap().broadcastMessage(c.getPlayer(),
								PetPacket.commandResponse(c.getPlayer().getId(), (byte) 1, (byte) petindex, true),
								true);
					}
				}
				used = true;
				break;
			}
			if (itemId / 10000 != 519) {
				// System.out.println("Unhandled CS item : " + itemId);
				// System.out.println(slea.toString(true));
			}
			break;
		}
		if (used) {
			if (ItemFlag.KARMA_USE.check(toUse.getFlag())) {
				toUse.setFlag(toUse.getFlag() - ItemFlag.KARMA_USE.getValue() + ItemFlag.UNTRADEABLE.getValue());
				c.getSession().writeAndFlush(
						CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.CASH, toUse));
			}
			MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.CASH, slot, (short) 1, false, true);
		}
		c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));

		if (cc) {
			if (!c.getPlayer().isAlive() || c.getPlayer().getEventInstance() != null
					|| FieldLimitType.ChannelSwitch.check(c.getPlayer().getMap().getFieldLimit())) {
				c.getPlayer().dropMessage(1, "Auto relog failed.");
				return;
			}
			c.getPlayer().dropMessage(5, "Auto relogging. Please wait.");
			c.getPlayer().fakeRelog();
		}
	}

	public static final void Pickup_Player(LittleEndianAccessor slea, MapleClient c, MapleCharacter chr) {
		MapleParty party_player = c.getPlayer().getParty();
		if (party_player != null) {
			if (party_player.getPartyDrop() == 1) {
				if (party_player.getLeader().getId() != chr.getId()) {
					c.getPlayer().dropMessage(5, "아이템 습득 권한이 파티장으로 설정되어 파티원은 드롭할 수 없습니다.");
					c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
					return;
				}
			}
		}
		slea.readInt();
		slea.skip(1);
		slea.skip(4); // 1.2.390 ++
		Point Client_Reportedpos = slea.readPos();
		if (chr == null || chr.getMap() == null) {
			return;
		}
		chr.setScrolledPosition((short) 0);
		MapleMapObject ob = chr.getMap().getMapObject(slea.readInt(), MapleMapObjectType.ITEM);
		if (ob == null) {
			c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
			return;
		}
		MapleMapItem mapitem = (MapleMapItem) ob;
		try {
			mapitem.getLock().lock();
			if (mapitem.isPickedUp()) {
				c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
				return;
			}
			// 명예의 훈장
			if (mapitem.getItemId() == 2432970) {
				c.getPlayer().gainHonor(10000);
				c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.getInventoryStatus(true));
				InventoryHandler.removeItem(chr, mapitem, ob);
				return;
			}

			if (mapitem.getItemId() == 2439545) {
				c.getPlayer().gainHonor(10000);
				c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.getInventoryStatus(true));
				InventoryHandler.removeItem(chr, mapitem, ob);
				return;
			}

			if (mapitem.getItemId() == 2431174) {
				int rand = Randomizer.rand(1, 10) * 10;
				c.getPlayer().gainHonor(rand);
				c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.getInventoryStatus(true));
				InventoryHandler.removeItem(chr, mapitem, ob);
				return;
			}

			if (mapitem.getItemId() == 2636420) { // 희미한 솔 에르다의 기운
				int rand = Randomizer.rand(10, 20);
				c.getPlayer().gainSolErdaStrength(rand);
				c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.getInventoryStatus(true));
				InventoryHandler.removeItem(chr, mapitem, ob);
				return;
			}

			if (mapitem.getItemId() == 2636421) { // 솔 에르다의 기운
				int rand = Randomizer.rand(200, 400);
				c.getPlayer().gainSolErdaStrength(rand);
				c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.getInventoryStatus(true));
				InventoryHandler.removeItem(chr, mapitem, ob);
				return;
			}

			if (mapitem.getItemId() == 2636422) { // 짙은 솔 에르다의 기운
				int rand = Randomizer.rand(500, 1000);
				c.getPlayer().gainSolErdaStrength(rand);
				c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.getInventoryStatus(true));
				InventoryHandler.removeItem(chr, mapitem, ob);
				return;
			}

			if (mapitem.getItemId() == 2433103) {
				c.getPlayer().gainHonor(10000);
				c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.getInventoryStatus(true));
				InventoryHandler.removeItem(chr, mapitem, ob);
				return;
			}

			if (mapitem.getItemId() == 2433019) {
				int ranmeso = Randomizer.rand(1000000, 100000000);
				chr.gainMeso(ranmeso, true);
				InventoryHandler.removeItem(chr, mapitem, ob);
				c.getPlayer().dropMessage(-8, "메소럭키백에서 메소를 " + ranmeso + "메소 만큼 휙득 하였습니다.");
				return;
			}
			if (mapitem.getItemId() == 2433979) {
				int ranmeso = Randomizer.rand(1000000, 100000000);
				chr.gainMeso(ranmeso, true);
				InventoryHandler.removeItem(chr, mapitem, ob);
				c.getPlayer().dropMessage(-8, "메소럭키백에서 메소를 " + ranmeso + "메소 만큼 휙득 하였습니다.");
				return;
			}
			if (mapitem.getItemId() == 4001169 && c.getPlayer().getMap().getMonstermarble() != 20) {
				c.getPlayer().getMap().setMonstermarble(c.getPlayer().getMap().getMonstermarble() + 1);
				c.getSession().writeAndFlush(CWvsContext.InventoryPacket.getInventoryStatus(true));
				c.getPlayer().getMap().broadcastMessage(
						CWvsContext.getTopMsg("몬스터구슬 " + c.getPlayer().getMap().getMonstermarble() + " / 20"));
				removeItem(chr, mapitem, ob);
				return;
			}
			if (mapitem.getItemId() == 4001169) {
				removeItem(chr, mapitem, ob);
				return;
			}
			// 파티퀘스트 시작
			if (mapitem.getItemId() == 4001101 && c.getPlayer().getMap().getMoonCake() != 80) {
				c.getPlayer().getMap().setMoonCake(c.getPlayer().getMap().getMoonCake() + 1);
				c.getSession().writeAndFlush(CWvsContext.InventoryPacket.getInventoryStatus(true));
				c.getPlayer().getMap()
						.broadcastMessage(CWvsContext.getTopMsg("어흥이를 위해 월묘의 떡 " + c.getPlayer().getMap().getMoonCake()
								+ "개를 모았습니다.  앞으로 " + (80 - c.getPlayer().getMap().getMoonCake()) + "개 더!"));
				removeItem(chr, mapitem, ob);
				return;
			}
			if (mapitem.getItemId() == 4001101) {
				removeItem(chr, mapitem, ob);
				return;
			}
			if (mapitem.getItemId() == 4000884) {
				c.getPlayer().getMap().broadcastMessage(CField.startMapEffect("달맞이꽃 씨앗을 되찾았습니다.", 5120016, true));
			}
			if (mapitem.getItemId() >= 2432139 && mapitem.getItemId() < 2432149) {
				EventManager em = c.getChannelServer().getEventSM().getEventManager("KerningPQ");
				String stage4 = em.getProperty("stage4r");
				String stage4M = c.getPlayer().getEventInstance().getProperty("stage4M");
				int getPQ = Integer.parseInt(stage4M);

				for (int i = 1; i < 10; i++) {
					if (mapitem.getItemId() == 2432139 + i) {
						if (getPQ != c.getPlayer().getMap().getKerningPQ()) {
							if (stage4 == "0") {
								c.getPlayer().getMap().setKerningPQ(i);
							} else {

								int a;
								switch (stage4) {
								case "1":
									c.getPlayer().dropMessage(5, "" + i + "/" + stage4);
									c.getPlayer().getMap().setKerningPQ(c.getPlayer().getMap().getKerningPQ() + i);
									c.getPlayer().dropMessage(5, "" + c.getPlayer().getMap().getKerningPQ());
									break;
								case "2":
									if (stage4 == "0") {
										break;
									}
									c.getPlayer().getMap().setKerningPQ(c.getPlayer().getMap().getKerningPQ() - i);
									break;
								case "3":
									if (stage4 == "0") {
										break;
									}
									c.getPlayer().getMap().setKerningPQ(c.getPlayer().getMap().getKerningPQ() * i);
									break;
								case "4":
									if (stage4 == "0") {
										break;
									}
									a = (int) Math.floor((c.getPlayer().getMap().getKerningPQ() / i));
									c.getPlayer().getMap().setKerningPQ(a);
									break;
								}
							}
							c.getPlayer().getMap().broadcastMessage(
									CWvsContext.getTopMsg("현재 숫자 : " + c.getPlayer().getMap().getKerningPQ()));
							c.getPlayer().getMap()
									.broadcastMessage(CField.startMapEffect("목표 숫자 : " + stage4M, 5120017, true));

							if (getPQ == c.getPlayer().getMap().getKerningPQ()) {
								c.getPlayer().getMap().broadcastMessage(CField.achievementRatio(75));
								c.getPlayer().getMap().broadcastMessage(
										CField.environmentChange("Map/Effect.img/quest/party/clear", 4));
								c.getPlayer().getMap().broadcastMessage(CField.environmentChange("gate", 2));
							}
						}
						c.getSession().writeAndFlush(CWvsContext.InventoryPacket.getInventoryStatus(true));
						removeItem(chr, mapitem, ob);
						return;
					}
				}
			}

			if (mapitem.getItemId() == 4001022) {
				c.getPlayer().getMap().setRPTicket(c.getPlayer().getMap().getRPTicket() + 1);
				c.getSession().writeAndFlush(CWvsContext.InventoryPacket.getInventoryStatus(true));
				c.getPlayer().getMap().broadcastMessage(
						CWvsContext.getTopMsg("통행증 " + c.getPlayer().getMap().getRPTicket() + "장을 모았습니다."));

				if (c.getPlayer().getMap().getRPTicket() == 20) {
					c.getPlayer().getMap().broadcastMessage(CField.achievementRatio(10));
					c.getPlayer().getMap()
							.broadcastMessage(CField.environmentChange("Map/Effect.img/quest/party/clear", 4));
					c.getPlayer().getMap().broadcastMessage(
							CField.startMapEffect("통행증을 모두 모았습니다. 레드 벌룬에게 말을 걸어 다음 단계로 이동해 주세요.", 5120018, true));
				}
				removeItem(chr, mapitem, ob);
				return;

			}
			if (mapitem.getItemId() == 4001022) {
				removeItem(chr, mapitem, ob);
				return;
			}

			if (mapitem.getItemId() == 2023484 || mapitem.getItemId() == 2023494 || mapitem.getItemId() == 2023495
					|| mapitem.getItemId() == 2023669 || mapitem.getItemId() == 2023927) {
				if (mapitem.getDropper() instanceof MapleMonster) {
					if (chr.getClient().getKeyValue("combokill") == null) {
						chr.getClient().setKeyValue("combokill", "1");
						chr.getClient().send(CField.ImageTalkNpc(9010049, 10000,
								"#b[안내] 콤보킬 퍼레이드#k\r\n\r\n콤보를 50단위씩 쌓아서 얻는 #b[콤보킬 퍼레이드]#k를 획득 하셨어요!\r\n\r\n#b[콤보킬 퍼레이드]#k에 접촉 시 #b추가 보너스 경험치#k를 획득할 수 있어요!"));
					}
					int bonus = mapitem.getItemId() % 100 == 84 ? 5
							: (mapitem.getItemId() % 100 == 94 ? 7 : (mapitem.getItemId() % 100 == 95 ? 10 : 12));
					if (chr.getSkillLevel(20000297) > 0) {
						bonus += SkillFactory.getSkill(20000297).getEffect(chr.getSkillLevel(20000297)).getX() / 100;
					} else if (chr.getSkillLevel(80000370) > 0) {
						bonus += SkillFactory.getSkill(80000370).getEffect(chr.getSkillLevel(80000370)).getX() / 100;
					}
					if (mapitem.getItemId() == 2023927) {
						bonus = 30;
					}
					MapleMonster mob = (MapleMonster) mapitem.getDropper();
					long exp = mob.getMobExp() * (long) bonus;
					chr.gainExp(exp, true, true, false);
					c.send(CField.EffectPacket.gainExp(exp));
				}
				c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.getInventoryStatus(true));
				InventoryHandler.removeItem(chr, mapitem, ob);
				return;
			}
			if (mapitem.getItemId() == 2434851) {
				if (!chr.getBuffedValue(25121133)) {
					c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
					return;
				}
				SecondaryStatEffect effect = SkillFactory.getSkill(25121133).getEffect(1);
				long duration = chr.getBuffLimit(25121133);
				if ((duration += 4000L) >= (long) effect.getCoolRealTime()) {
					duration = effect.getCoolRealTime();
				}
				effect.applyTo(chr, chr, false, chr.getPosition(), (int) duration, (byte) 0, false);
				c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.getInventoryStatus(true));
				InventoryHandler.removeItem(chr, mapitem, ob);
				return;
			}
			if (mapitem.getItemId() == 2002058) {
				if (c.getPlayer().hasDisease(SecondaryStat.DeathMark)) {
					c.getPlayer().cancelDisease(SecondaryStat.DeathMark);
				}
				c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.getInventoryStatus(true));
				InventoryHandler.removeItem(chr, mapitem, ob);
				return;
			}

			if (mapitem.getItemId() == 4001849 || mapitem.getItemId() == 4001847) {
				if (chr.getBattleGroundChr() != null) {
					chr.setSkillCustomInfo(80001741, chr.getSkillCustomValue0(80001741) + 1L, 0L);
					chr.getBattleGroundChr().setTeam(2);
					chr.getMap().broadcastMessage(chr, BattleGroundPacket.UpdateAvater(chr.getBattleGroundChr(),
							GameConstants.BattleGroundJobType(chr.getBattleGroundChr())), false);
					chr.getBattleGroundChr().setTeam(1);
					chr.getClient().send(BattleGroundPacket.UpdateAvater(chr.getBattleGroundChr(),
							GameConstants.BattleGroundJobType(chr.getBattleGroundChr())));
				}
				c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
				InventoryHandler.removeItem(chr, mapitem, ob);
				return;
			}
			if (mapitem.getItemId() >= 4034942 && mapitem.getItemId() <= 4034958) {
				if (((Integer) c.getPlayer().getRecipe().left).intValue() == mapitem.getItemId()) {
					c.getPlayer().setRecipe(new Pair<Integer, Integer>(mapitem.getItemId(),
							(Integer) c.getPlayer().getRecipe().right + 1));
				} else {
					c.getPlayer().setRecipe(new Pair<Integer, Integer>(mapitem.getItemId(), 1));
				}
				chr.getMap().broadcastMessage(CField.addItemMuto(chr));
				c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.getInventoryStatus(true));
				InventoryHandler.removeItem(chr, mapitem, ob);
				return;
			}
			boolean touchitem = false;
			switch (mapitem.getItemId()) {
			case 2633304:
			case 2633609: {
				String[] bossname = new String[] { "이지 시그너스", "하드 힐라", "카오스 핑크빈", "노멀 시그너스", "카오스 자쿰", "카오스 피에르",
						"카오스 반반", "카오스 블러디퀸", "하드 매그너스", "카오스 벨룸", "카오스 파풀라투스", "노멀 스우", "노멀 데미안", "이지 루시드", "노멀 루시드",
						"노멀 윌", "노멀 더스크", "노멀 듄켈", "하드 데미안", "하드 스우", "하드 루시드", "하드 윌", "카오스 더스크", "하드 듄켈", "진 힐라",
						"세렌" };
				String[] wishCoinCheck = null;
				if (ServerConstants.Event_Blooming) {
					wishCoinCheck = chr.getClient().getKeyValue("WishCoin").split("");
				} else if (ServerConstants.Event_MapleLive) {
					wishCoinCheck = chr.getClient().getCustomKeyValueStr(501468, "reward").split("");
				}
				String NewKeyvalue = "";
				int i = 0;
				for (Pair<Integer, Integer> list : ServerConstants.NeoPosList) {
					MapleMonster mob = (MapleMonster) mapitem.getDropper();
					if (list.getLeft().intValue() == mob.getId()) {
						int questid;
						int coincount = list.getRight();
						if (ServerConstants.Event_Blooming) {
							if (Randomizer.isSuccess(50)) {
								// chr.getClient().send(CField.enforceMsgNPC(9062515, 3000, "자아, #r" +
								// bossname[i] + "#k 처치~\r\n위시 코인 #b" + coincount + "#k개 찾았지"));
							} else {
								// chr.getClient().send(CField.enforceMsgNPC(9062515, 3000, "우와! #r" +
								// bossname[i] + "#k 처치~\r\n위시 코인 #b" + coincount + "#k개 찾았다~"));
							}
						} else if (ServerConstants.Event_MapleLive) {
							questid = 501471 + i;
							if (chr.getClient().getCustomKeyValue(questid, "state") == 1L
									|| chr.getClient().getCustomKeyValue(questid, "state") == 2L) {
								chr.getClient().send(CField.enforceMsgNPC(9062558, 3000,
										"이런, 이미 이번주에 #r" + bossname[i] + "#k를 처치했군."));
								break;
							}
							if (Randomizer.isSuccess(50)) {
								chr.getClient().send(CField.enforceMsgNPC(9062558, 3000,
										"호오, #r" + bossname[i] + "#k를 처치했군.\r\n검은콩 #b" + coincount + "#k개를 찾았다."));
							} else {
								chr.getClient().send(CField.enforceMsgNPC(9062558, 3000,
										"좋아, #r" + bossname[i] + "#k를 처치했군.\r\n검은콩 #b" + coincount + "#k개를 찾았어."));
							}
						}
						wishCoinCheck[i] = "1";
						for (int to = 0; to < wishCoinCheck.length; ++to) {
							NewKeyvalue = NewKeyvalue + wishCoinCheck[to];
						}
						if (!ServerConstants.Event_MapleLive) {
							break;
						}
						questid = 501471 + i;
						chr.getClient().setCustomKeyValue(501468, "reward", NewKeyvalue);
						chr.getClient().setCustomKeyValue(questid, "clear", "1");
						chr.getClient().setCustomKeyValue(questid, "state", "1");
						break;
					}
					++i;
				}
				touchitem = true;
				break;
			}
			case 2633343:
				if (chr.getBuffedValue(80003046)) {
					int added = 0;
					int adddd = (chr.getQuestStatus(100801) == 2) ? 1 : ((chr.getQuestStatus(100802) == 2) ? 2 : 0);

					for (int j = 0; j < adddd; j++) {
						if (Randomizer.isSuccess(50)) {
							added++;
						}
					}

					int now = (int) chr.getKeyValue(100803, "count");
					if (now < 0) {
						now = 0;
					}

					chr.setKeyValue(100803, "count", (now + 1) + "");
					chr.getClient().send(SLFCGPacket.EventSkillOnFlowerEffect(7, 1));

					if (chr.getKeyValue(100803, "count") == 5L) {
						chr.getClient().send(CField.enforceMsgNPC(9062527, 1300, "꽃의 보석에 햇살의 힘이 #r절반#k이나 모였어요!"));
					} else if (chr.getKeyValue(100803, "count") == 10L) {
						chr.getClient().send(SLFCGPacket.EventSkillOn(1, 1 + added));
						chr.getClient().send(CField.enforceMsgNPC(9062527, 1000, "햇살의 힘으로 #r꽃씨#k를 펑!펑!"));
						chr.getClient().send(CField.UIPacket.detailShowInfo("하나의 꽃씨 뿌리기로 블루밍 코인 "
								+ ((Calendar.getInstance().get(7) == 7 || Calendar.getInstance().get(7) == 1) ? 40 : 20)
								+ "개를 획득했습니다.", 3, 20, 20));

						if (added >= 1 && chr.getQuestStatus(100801) == 2) {
							Timer.BuffTimer.getInstance().schedule(() -> {
								chr.getClient().send(CField.enforceMsgNPC(9062528, 1000, "#r꽃비#k가 쏴아아!\r\n너무 예뻐!"));
								chr.getClient()
										.send(CField.UIPacket.detailShowInfo("두나의 꽃비로 블루밍 코인 "
												+ ((Calendar.getInstance().get(7) == 7
														|| Calendar.getInstance().get(7) == 1) ? 20 : 10)
												+ "개를 획득했습니다.", 3, 20, 20));
							}, 2500L);
						}
						if (added == 2 && chr.getQuestStatus(100802) == 2) {
							Timer.BuffTimer.getInstance().schedule(() -> {
								// chr.AddBloomingCoin((Calendar.getInstance().get(7) == 7 ||
								// Calendar.getInstance().get(7) == 1) ? 40 : 20, chr.getPosition());
								chr.getClient()
										.send(CField.enforceMsgNPC(9062529, 1000, "#r햇살#k이 샤라랑!\r\n꽃들아 잘 자라라~!"));
								chr.getClient()
										.send(CField.UIPacket.detailShowInfo("세나의 햇살 비추기로 블루밍 코인 "
												+ ((Calendar.getInstance().get(7) == 7
														|| Calendar.getInstance().get(7) == 1) ? 40 : 20)
												+ "개를 획득했습니다.", 3, 20, 20));
							}, 4500L);
						}
					}
					if (chr.getKeyValue(100711,
							"today") >= ((Calendar.getInstance().get(7) == 7 || Calendar.getInstance().get(7) == 1)
									? 6000L
									: 3000L)) {
						chr.getClient().send(CField.enforceMsgNPC(9062527, 1000, "오늘은 이만하면 됐어요."));
						chr.getClient().getSession().writeAndFlush(CField.UIPacket.closeUI(1297));
						chr.getClient().send(SLFCGPacket.FollowNpctoSkill(false, 9062524, 0));
						chr.getClient().send(SLFCGPacket.FollowNpctoSkill(false, 9062525, 0));
						chr.getClient().send(SLFCGPacket.FollowNpctoSkill(false, 9062526, 0));
						chr.cancelEffect(chr.getBuffedEffect(SecondaryStat.EventSpecialSkill));
					}
				}
				touchitem = true;
				break;
			case 2432391: {
				int ranexp = Randomizer.rand(1000000, 3000000);
				chr.gainExp(ranexp, true, true, true);
				touchitem = true;
				break;
			}
			case 2432392: {
				int ranexp = Randomizer.rand(1000000, 3000000);
				chr.gainExp(ranexp * 2, true, true, true);
				touchitem = true;
				break;
			}
			case 2432393: {
				int ranmeso = Randomizer.rand(50000, 70000);
				chr.gainMeso(ranmeso, true);
				touchitem = true;
				break;
			}
			case 2432394: {
				int ranmeso = Randomizer.rand(50000, 100000);
				chr.gainMeso(ranmeso, true);
				touchitem = true;
				break;
			}
			case 2432395: {
				int[] itemlist = new int[] { 2000005, 2001556, 2001554, 2001530 };
				int Random2 = (int) Math.floor(Math.random() * (double) itemlist.length);
				int finalitemid = itemlist[Random2];
				chr.gainItem(finalitemid, Randomizer.rand(1, 10));
				touchitem = true;
				break;
			}
			case 2432396: {
				int[] itemlist = new int[] { 1082608, 1082609, 1082610, 1082611, 1082612, 1072967, 1072968, 1072969,
						1072970, 1072971, 1052799, 0x101080, 0x101081, 1052802, 1052803, 1004229, 1004230, 1004231,
						1004232, 1004233, 1102718, 1102719, 1102720, 1102721, 1102722 };
				int Random3 = (int) Math.floor(Math.random() * (double) itemlist.length);
				int finalitemid = itemlist[Random3];
				Equip Item2 = (Equip) MapleItemInformationProvider.getInstance().getEquipById(finalitemid);
				if (Item2 == null) {
					InventoryHandler.removeItem(chr, mapitem, ob);
					c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
					return;
				}
				byte state = 1;
				state = Randomizer.isSuccess(50) ? (byte) 2 : (Randomizer.isSuccess(30) ? (byte) 3 : 1);
				Item2.setState(state);
				MapleInventoryManipulator.addbyItem(c, Item2);
				touchitem = true;
				break;
			}
			case 2432397: {
				int[] itemlist = new int[] { 1212101, 1222095, 1232095, 1242102, 1242133, 1262011, 1272013, 1282013,
						1292014, 1302315, 1312185, 1322236, 1332260, 1342100, 1362121, 1372207, 1382245, 1402236,
						1412164, 1422171, 1432200, 1442254, 1452238, 1462225, 1472247, 1482202, 1492212, 1522124,
						1532130, 1582011, 1592016 };
				int Random4 = (int) Math.floor(Math.random() * (double) itemlist.length);
				int finalitemid = itemlist[Random4];
				Equip Item3 = (Equip) MapleItemInformationProvider.getInstance().getEquipById(finalitemid);
				if (Item3 == null) {
					InventoryHandler.removeItem(chr, mapitem, ob);
					c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
					return;
				}
				byte state = 1;
				state = Randomizer.isSuccess(50) ? (byte) 2 : (Randomizer.isSuccess(30) ? (byte) 3 : 1);
				Item3.setState(state);
				MapleInventoryManipulator.addbyItem(c, Item3);
				touchitem = true;
				break;
			}
			case 2432398: {
				int rand;
				ArrayList<Pair<Integer, Integer>> random = new ArrayList<Pair<Integer, Integer>>();
				int[][] NormalItem = new int[][] { { 5062009, 5 }, { 5062009, 10 }, { 5062500, 10 }, { 2435719, 1 },
						{ 2435719, 2 }, { 2435719, 3 }, { 4001832, 100 }, { 4001832, 150 }, { 4001832, 200 },
						{ 4310012, 15 }, { 4310012, 20 }, { 4310012, 25 }, { 4310012, 30 }, { 4310012, 35 } };
				int[][] HighuerItem = new int[][] { { 2048716, 1 }, { 2048716, 2 }, { 2048716, 3 }, { 2048717, 1 },
						{ 2048717, 2 }, { 2049752, 1 }, { 4310005, 1 }, { 4310005, 2 }, { 5069000, 1 } };
				int[][] UniqueItem = new int[][] { { 5062503, 10 }, { 5062503, 15 }, { 2049153, 1 }, { 2049153, 2 },
						{ 2049153, 3 }, { 5068300, 1 }, { 5069001, 1 }, { 2048753, 1 }, { 2049370, 1 },
						{ 5062503, 5 } };
				random.add(new Pair<Integer, Integer>(1, 6400));
				random.add(new Pair<Integer, Integer>(2, 3500));
				random.add(new Pair<Integer, Integer>(3, 100));
				int itemid = 0;
				int count = 0;
				int type = GameConstants.getWeightedRandom(random);
				if (type == 1) {
					rand = Randomizer.rand(0, NormalItem.length - 1);
					itemid = NormalItem[rand][0];
					count = NormalItem[rand][1];
				} else if (type == 2) {
					rand = Randomizer.rand(0, HighuerItem.length - 1);
					itemid = HighuerItem[rand][0];
					count = HighuerItem[rand][1];
				} else if (type == 3) {
					rand = Randomizer.rand(0, UniqueItem.length - 1);
					itemid = UniqueItem[rand][0];
					count = UniqueItem[rand][1];
				}
				chr.gainItem(itemid, count);
				chr.dropMessage(5, "[" + MapleItemInformationProvider.getInstance().getName(itemid) + "]를 " + count
						+ "개 획득 했습니다.");
				touchitem = true;
				break;
			}
			}
			if (mapitem.getItemId() >= 2437659 && mapitem.getItemId() <= 2437664) {
				int plushour = 0;
				int plusmit = 0;
				switch (mapitem.getItemId()) {
				case 2437659: {
					plusmit = 10;
					break;
				}
				case 2437660: {
					plusmit = 30;
					break;
				}
				case 2437661: {
					plusmit = 50;
					break;
				}
				case 2437662: {
					plushour = 2;
					break;
				}
				case 2437663: {
					plushour = 4;
					break;
				}
				case 2437664: {
					plushour = 9;
				}
				}
				c.getPlayer().getMap().Papullatushour += plushour;
				if (c.getPlayer().getMap().Papullatushour > 12) {
					c.getPlayer().getMap().Papullatushour -= 12;
				}
				c.getPlayer().getMap().Papullatusminute += plusmit;
				if (c.getPlayer().getMap().Papullatusminute >= 60) {
					++c.getPlayer().getMap().Papullatushour;
					c.getPlayer().getMap().Papullatusminute -= 60;
				}
				c.getPlayer().getMap().broadcastMessage(
						CField.startMapEffect("파풀라투스의 시계가 움직입니다. 차원의 포탈을 통해 시간을 봉인하세요.", 5120177, true));
				c.getPlayer().getMap().broadcastMessage(
						MobPacket.BossPapuLatus.PapulLatusTimePatten(1, plushour, plusmit, 0, new int[0]));
				InventoryHandler.removeItem(chr, mapitem, ob);
				c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
				return;
			}

			if (mapitem.getItemId() == 2437606 || mapitem.getItemId() == 2437607) {
				MobSkill ms1 = MobSkillFactory.getMobSkill(241, 3);
				int duration = 0;
				if (chr.getSkillCustomTime(241) != null) {
					duration = mapitem.getItemId() == 2437606 ? chr.getSkillCustomTime(241) * 2
							: chr.getSkillCustomTime(241) / 2;
					chr.cancelDisease(SecondaryStat.PapulCuss);
					if (duration > 60000) {
						duration = 60000;
					}
					if (duration > 0) {
						ms1.setDuration(duration);
						chr.setSkillCustomInfo(241, 0L, duration);
						chr.giveDebuff(SecondaryStat.PapulCuss, ms1);
					}
				}
				InventoryHandler.removeItem(chr, mapitem, ob);
				c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
				return;
			}
			if (touchitem) {
				InventoryHandler.removeItem(chr, mapitem, ob);
				c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
				return;
			}
			if (mapitem.getQuest() > 0 && chr.getQuestStatus(mapitem.getQuest()) != 1) {
				c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
				return;
			}
			if (mapitem.getOwner() != chr.getId() && (!mapitem.isPlayerDrop() && mapitem.getDropType() == 0
					|| mapitem.isPlayerDrop() && chr.getMap().getEverlast())) {
				c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
				return;
			}
			if (!(mapitem.isPlayerDrop() || mapitem.getDropType() != 1 || mapitem.getOwner() == chr.getId()
					|| chr.getParty() != null && chr.getParty().getMemberById(mapitem.getOwner()) != null)) {
				c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
				return;
			}
			if (mapitem.getMeso() > 0) {
				if (chr.getParty() != null && mapitem.getOwner() != chr.getId()) {
					LinkedList<MapleCharacter> toGive = new LinkedList<MapleCharacter>();
					int splitMeso = mapitem.getMeso() * 40 / 100;
					for (MaplePartyCharacter z : chr.getParty().getMembers()) {
						MapleCharacter m = chr.getMap().getCharacterById(z.getId());
						if (m == null || m.getId() == chr.getId()) {
							continue;
						}
						toGive.add(m);
					}
					for (MapleCharacter m : toGive) {
						int mesos = splitMeso / toGive.size()
								+ (m.getStat().hasPartyBonus ? (int) ((double) mapitem.getMeso() / 20.0) : 0);
						if (mapitem.getDropper() instanceof MapleMonster && m.getStat().incMesoProp > 0) {
							mesos = (int) ((double) mesos
									+ Math.floor((float) (m.getStat().incMesoProp * mesos) / 100.0f));
						}
						m.gainMeso(mesos, true, false, false, true);
					}
					int mesos = mapitem.getMeso() - splitMeso;
					if (mapitem.getDropper() instanceof MapleMonster && chr.getStat().incMesoProp > 0) {
						mesos = (int) ((double) mesos
								+ Math.floor((float) (chr.getStat().incMesoProp * mesos) / 100.0f));
					}
					chr.gainMeso(mesos, true, false, false, true);
				} else {
					int mesos = mapitem.getMeso();
					if (mapitem.getDropper() instanceof MapleMonster && chr.getStat().incMesoProp > 0) {
						mesos = (int) ((double) mesos
								+ Math.floor((float) (chr.getStat().incMesoProp * mesos) / 100.0f));
					}
					chr.gainMeso(mesos, true, false, false, true);
				}
				if (mapitem.getDropper().getType() == MapleMapObjectType.MONSTER) {
					c.getSession().writeAndFlush((Object) CWvsContext.onMesoPickupResult(mapitem.getMeso()));
				}
				InventoryHandler.removeItem(chr, mapitem, ob);
			} else if (MapleItemInformationProvider.getInstance().isPickupBlocked(mapitem.getItemId())) {
				c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
				c.getPlayer().dropMessage(5, "This item cannot be picked up.");
			} else if (c.getPlayer().inPVP()
					&& Integer.parseInt(c.getPlayer().getEventInstance().getProperty("ice")) == c.getPlayer().getId()) {
				c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.getInventoryFull());
				c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.getShowInventoryFull());
				c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
			} else if (mapitem.getItemId() == 2431835) {
				MapleItemInformationProvider.getInstance().getItemEffect(2002093).applyTo(chr, true);
				InventoryHandler.removeItem(chr, mapitem, ob);
			} else if (mapitem.getItemId() / 10000 != 291 && MapleInventoryManipulator.checkSpace(c,
					mapitem.getItemId(), mapitem.getItem().getQuantity(), mapitem.getItem().getOwner())) {
				Equip equip;
				if (mapitem.getItem().getQuantity() >= 50 && mapitem.getItemId() == 2340000) {
					c.setMonitored(true);
				}
				if (mapitem.getEquip() != null && mapitem.getDropper().getType() == MapleMapObjectType.MONSTER
						&& mapitem.getEquip().getState() > 0) {
					c.getSession().writeAndFlush((Object) CField.EffectPacket.showEffect(chr, 0, 0, 65, 0, 0, (byte) 0,
							true, null, null, mapitem.getItem()));
				}
				if (GameConstants.isArcaneSymbol(mapitem.getItemId())) {
					Equip equip2 = (Equip) mapitem.getItem();
					equip2.setArc((short) 30);
					equip2.setArcLevel(1);
					equip2.setArcEXP(1);
					if (GameConstants.isXenon(c.getPlayer().getJob())) {
						equip2.setStr((short) 117);
						equip2.setDex((short) 117);
						equip2.setLuk((short) 117);
					} else if (GameConstants.isDemonAvenger(c.getPlayer().getJob())) {
						equip2.setHp((short) 525);
					} else if (GameConstants.isWarrior(c.getPlayer().getJob())) {
						equip2.setStr((short) 300);
					} else if (GameConstants.isMagician(c.getPlayer().getJob())) {
						equip2.setInt((short) 300);
					} else if (GameConstants.isArcher(c.getPlayer().getJob())
							|| GameConstants.isCaptain(c.getPlayer().getJob())
							|| GameConstants.isMechanic(c.getPlayer().getJob())
							|| GameConstants.isAngelicBuster(c.getPlayer().getJob())) {
						equip2.setDex((short) 300);
					} else if (GameConstants.isThief(c.getPlayer().getJob())) {
						equip2.setLuk((short) 300);
					} else if (GameConstants.isPirate(c.getPlayer().getJob())) {
						equip2.setStr((short) 300);
					}
				} else if (GameConstants.isAuthenticSymbol(mapitem.getItemId())
						&& (equip = (Equip) mapitem.getItem()).getArcLevel() == 0) {
					equip.setArc((short) 10);
					equip.setArcLevel(1);
					equip.setArcEXP(1);
					if (GameConstants.isXenon(c.getPlayer().getJob())) {
						equip.setStr((short) 317);
						equip.setDex((short) 317);
						equip.setLuk((short) 317);
					} else if (GameConstants.isDemonAvenger(c.getPlayer().getJob())) {
						equip.setHp((short) 725);
					} else if (GameConstants.isWarrior(c.getPlayer().getJob())) {
						equip.setStr((short) 500);
					} else if (GameConstants.isMagician(c.getPlayer().getJob())) {
						equip.setInt((short) 500);
					} else if (GameConstants.isArcher(c.getPlayer().getJob())
							|| GameConstants.isCaptain(c.getPlayer().getJob())
							|| GameConstants.isMechanic(c.getPlayer().getJob())
							|| GameConstants.isAngelicBuster(c.getPlayer().getJob())) {
						equip.setDex((short) 500);
					} else if (GameConstants.isThief(c.getPlayer().getJob())) {
						equip.setLuk((short) 500);
					} else if (GameConstants.isPirate(c.getPlayer().getJob())) {
						equip.setStr((short) 500);
					}
				}
				if (mapitem.getItem().getItemId() == 4001886) {
					if (mapitem.getItem().getBossid() != 0) {
						int party = chr.getParty() != null ? chr.getParty().getMembers().size() : 1;
						int meso = 0, mobid = 0;
						for (Triple<Integer, Integer, Integer> list : BossRewardMeso.getLists()) {
							int bossid = BossRewardMeso.RewardBossId(list.getMid());
							if (bossid == 0) {
								bossid = list.getLeft();
							}
							if (bossid == mapitem.getItem().getBossid()
									|| list.getMid() == mapitem.getItem().getBossid()) {
								meso = list.getRight();
								mobid = list.getMid();
								break;
							}
						}
						// Boss Reward Management
						mapitem.getItem().setReward(new BossReward(
								chr.getInventory(MapleInventoryType.ETC).countById(4001886) + 1, mobid, party, meso));
						mapitem.getItem().setExpiration(System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000));
					} else {
						mapitem.getItem().setReward(new BossReward(
								chr.getInventory(MapleInventoryType.ETC).countById(4001886) + 1, 100100, 1, 1));
					}
				}
				FileoutputUtil.log(FileoutputUtil. 物品獲得日誌, 
					    "[玩家拾取] 帳號編號 : " + chr.getClient().getAccID()  
					    + " | " + chr.getName()  + " 在 " + c.getPlayer().getMapId()  + " 地圖 "
					    + "獲得 " + MapleItemInformationProvider.getInstance().getName(mapitem.getItem().getItemId())  
					    + "(" + mapitem.getItem().getItemId()  + ") " 
					    + mapitem.getItem().getQuantity()  + " 個");
				MapleInventoryManipulator.addFromDrop(c, mapitem.getItem(), true,
						mapitem.getDropper() instanceof MapleMonster, false);
				InventoryHandler.removeItem(chr, mapitem, ob);
			} else {
				c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.getInventoryFull());
				c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.getShowInventoryFull());
				c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
			}
		} finally {
			mapitem.getLock().unlock();
		}
	}

	public static final void Pickup_Pet(final LittleEndianAccessor slea, final MapleClient c,
			final MapleCharacter chr) {
		if (chr == null) {
			return;
		}
		if (c.getPlayer().inPVP()) {
			return;
		}
		MapleParty party_player = c.getPlayer().getParty();
		if (party_player != null) {
			if (party_player.getPartyDrop() == 1) {
				if (party_player.getLeader().getId() != chr.getId()) {
					c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
					return;
				}
			}
		}
		c.getPlayer().setScrolledPosition((short) 0);
		final byte petz = (byte) slea.readInt();
		final MaplePet pet = chr.getPet(petz);
		slea.skip(1);
		slea.readInt();
		slea.readInt(); // 1.2.391 ++
		final Point Client_Reportedpos = slea.readPos();
		final MapleMapObject ob = chr.getMap().getMapObject(slea.readInt(), MapleMapObjectType.ITEM);
		if (ob == null || pet == null) {
			return;
		}
		final MapleMapItem mapitem = (MapleMapItem) ob;
		try {
			mapitem.getLock().lock();
			if (mapitem.isPickedUp()) {
				return;
			}
			if ((mapitem.getOwner() != chr.getId() && mapitem.isPlayerDrop()) || mapitem.getItemId() == 2023484
					|| mapitem.getItemId() == 2023494 || mapitem.getItemId() == 2023495
					|| mapitem.getItemId() == 2023669 || mapitem.getItemId() == 2023927) {
				return;
			}

			// 명예의 훈장
			if (mapitem.getItemId() == 2432970) {
				c.getPlayer().gainHonor(10000);
				c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.getInventoryStatus(true));
				InventoryHandler.removeItem(chr, mapitem, ob);
				return;
			}

			if (mapitem.getItemId() == 2439545) {
				c.getPlayer().gainHonor(10000);
				c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.getInventoryStatus(true));
				InventoryHandler.removeItem(chr, mapitem, ob);
				return;
			}

			if (mapitem.getItemId() == 2431174) {
				final int rand = Randomizer.rand(1, 10) * 10;
				c.getPlayer().gainHonor(rand);
				removeItem_Pet(chr, mapitem, petz, pet.getPetItemId());
				return;
			}

			if (mapitem.getItemId() == 2636420) { // 희미한 솔 에르다의 기운
				int rand = Randomizer.rand(10, 20);
				c.getPlayer().gainSolErdaStrength(rand);
				c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.getInventoryStatus(true));
				InventoryHandler.removeItem(chr, mapitem, ob);
				return;
			}

			if (mapitem.getItemId() == 2636421) { // 솔 에르다의 기운
				int rand = Randomizer.rand(200, 400);
				c.getPlayer().gainSolErdaStrength(rand);
				c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.getInventoryStatus(true));
				InventoryHandler.removeItem(chr, mapitem, ob);
				return;
			}

			if (mapitem.getItemId() == 2636422) { // 짙은 솔 에르다의 기운
				int rand = Randomizer.rand(500, 1000);
				c.getPlayer().gainSolErdaStrength(rand);
				c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.getInventoryStatus(true));
				InventoryHandler.removeItem(chr, mapitem, ob);
				return;
			}

			if (mapitem.getItemId() == 2433103) {
				final int rand = Randomizer.rand(1, 10) * 10;
				c.getPlayer().gainHonor(10000);
				removeItem_Pet(chr, mapitem, petz, pet.getPetItemId());
				return;
			}

			if (mapitem.getItemId() == 4001169 && c.getPlayer().getMap().getMonstermarble() != 20) {
				c.getPlayer().getMap().setMonstermarble(c.getPlayer().getMap().getMonstermarble() + 1);
				c.getPlayer().getMap().broadcastMessage(
						CWvsContext.getTopMsg("몬스터구슬 " + c.getPlayer().getMap().getMonstermarble() + " / 20"));
				removeItem_Pet(chr, mapitem, petz, pet.getPetItemId());
				return;
			}
			if (mapitem.getItemId() == 4001169) {
				removeItem_Pet(chr, mapitem, petz, pet.getPetItemId());
				return;
			}
			// 파티퀘스트 시작
			if (mapitem.getItemId() == 4001101 && c.getPlayer().getMap().getMoonCake() != 80) {
				c.getPlayer().getMap().setMoonCake(c.getPlayer().getMap().getMoonCake() + 1);
				c.getSession().writeAndFlush(CWvsContext.InventoryPacket.getInventoryStatus(true));
				c.getPlayer().getMap()
						.broadcastMessage(CWvsContext.getTopMsg("어흥이를 위해 월묘의 떡 " + c.getPlayer().getMap().getMoonCake()
								+ "개를 모았습니다.  앞으로 " + (80 - c.getPlayer().getMap().getMoonCake()) + "개 더!"));
				removeItem(chr, mapitem, ob);
				return;
			}
			if (mapitem.getItemId() == 4001101) {
				removeItem(chr, mapitem, ob);
				return;
			}
			if (mapitem.getItemId() == 4000884) {
				c.getPlayer().getMap().broadcastMessage(CField.startMapEffect("달맞이꽃 씨앗을 되찾았습니다.", 5120016, true));
			}
			if (mapitem.getItemId() == 4001022) {
				c.getPlayer().getMap().setRPTicket(c.getPlayer().getMap().getRPTicket() + 1);
				c.getSession().writeAndFlush(CWvsContext.InventoryPacket.getInventoryStatus(true));
				c.getPlayer().getMap().broadcastMessage(
						CWvsContext.getTopMsg("통행증 " + c.getPlayer().getMap().getRPTicket() + "장을 모았습니다."));
				if (c.getPlayer().getMap().getRPTicket() == 20) {
					c.getPlayer().getMap().broadcastMessage(CField.achievementRatio(10));
					c.getPlayer().getMap()
							.broadcastMessage(CField.environmentChange("Map/Effect.img/quest/party/clear", 4));
					c.getPlayer().getMap().broadcastMessage(
							CField.startMapEffect("통행증을 모두 모았습니다. 레드 벌룬에게 말을 걸어 다음 단계로 이동해 주세요.", 5120018, true));
				}
				removeItem(chr, mapitem, ob);
				return;
			}
			if (mapitem.getItemId() == 4001022) {
				removeItem(chr, mapitem, ob);
				return;
			}
			if (mapitem.getItemId() == 2633304 || mapitem.getItemId() == 2633609) {
				final String[] bossname = { "이지 시그너스", "하드 힐라", "카오스 핑크빈", "노멀 시그너스", "카오스 자쿰", "카오스 피에르", "카오스 반반",
						"카오스 블러디퀸", "하드 매그너스", "카오스 벨룸", "카오스 파풀라투스", "노멀 스우", "노멀 데미안", "이지 루시드", "노멀 루시드", "노멀 윌",
						"노멀 더스크", "노멀 듄켈", "하드 데미안", "하드 스우", "하드 루시드", "하드 윌", "카오스 더스크", "하드 듄켈", "진 힐라", "세렌" };
				String[] wishCoinCheck = null;
				if (ServerConstants.Event_Blooming) {
					wishCoinCheck = chr.getClient().getKeyValue("WishCoin").split("");
				} else if (ServerConstants.Event_MapleLive) {
					wishCoinCheck = chr.getClient().getCustomKeyValueStr(501468, "reward").split("");
				}
				String NewKeyvalue = "";
				int i = 0;
				for (final Pair<Integer, Integer> list : ServerConstants.NeoPosList) {
					final MapleMonster mob = (MapleMonster) mapitem.getDropper();
					if (list.getLeft() == mob.getId()) {
						int coincount = list.getRight();
						if (ServerConstants.Event_Blooming) {
							if (Randomizer.isSuccess(50)) {
								// chr.getClient().send(CField.enforceMsgNPC(9062515, 3000, "자아, #r" +
								// bossname[i] + "#k 처치~\r\n위시 코인 #b" + coincount + "#k개 찾았지"));
							} else {
								// chr.getClient().send(CField.enforceMsgNPC(9062515, 3000, "우와! #r" +
								// bossname[i] + "#k 처치~\r\n위시 코인 #b" + coincount + "#k개 찾았다~"));
							}
						} else if (ServerConstants.Event_MapleLive) {
							final int questid = 501471 + i;
							if (chr.getClient().getCustomKeyValue(questid, "state") == 1L
									|| chr.getClient().getCustomKeyValue(questid, "state") == 2L) {
								chr.getClient().send(CField.enforceMsgNPC(9062558, 3000,
										"이런, 이미 이번주에 #r" + bossname[i] + "#k를 처치했군."));
								break;
							}
							if (Randomizer.isSuccess(50)) {
								chr.getClient().send(CField.enforceMsgNPC(9062558, 3000,
										"호오, #r" + bossname[i] + "#k를 처치했군.\r\n검은콩 #b" + coincount + "#k개를 찾았다."));
							} else {
								chr.getClient().send(CField.enforceMsgNPC(9062558, 3000,
										"좋아, #r" + bossname[i] + "#k를 처치했군.\r\n검은콩 #b" + coincount + "#k개를 찾았어."));
							}
						}
						wishCoinCheck[i] = "1";
						if (ServerConstants.Event_Blooming) {
							chr.getClient().removeKeyValue("WishCoin");
						}
						for (int to = 0; to < wishCoinCheck.length; ++to) {
							NewKeyvalue += wishCoinCheck[to];
						}
						if (ServerConstants.Event_Blooming) {
							chr.getClient().setKeyValue("WishCoin", NewKeyvalue);
							if (Integer.parseInt(chr.getClient().getKeyValue("WishCoinWeekGain")) >= 400) {
								chr.getClient().send(
										CField.enforceMsgNPC(9062515, 3000, "이번 주차 위시 코인 #r400개#k\r\n 모두 획득~ 냐냐냐~"));
								return;
							}
							if (Integer.parseInt(chr.getClient().getKeyValue("WishCoinWeekGain")) + coincount >= 400) {
								coincount = Integer.parseInt(chr.getClient().getKeyValue("WishCoinWeekGain"))
										+ coincount - 400;
							}
							chr.getClient().setKeyValue("WishCoinGain",
									Integer.parseInt(chr.getClient().getKeyValue("WishCoinGain")) + coincount + "");
							chr.getClient().setKeyValue("WishCoinWeekGain",
									Integer.parseInt(chr.getClient().getKeyValue("WishCoinWeekGain")) + coincount + "");
							break;
						} else {
							if (ServerConstants.Event_MapleLive) {
								final int questid = 501471 + i;
								chr.getClient().setCustomKeyValue(501468, "reward", NewKeyvalue);
								chr.getClient().setCustomKeyValue(questid, "state", "1");
								break;
							}
							break;
						}
					} else {
						++i;
					}
				}
				removeItem_Pet(chr, mapitem, petz, pet.getPetItemId());
				return;
			}
			if (mapitem.getItemId() == 2633343) {
				if (chr.getBuffedValue(80003046)) {
					int added = 0;
					for (int adddd = (chr.getQuestStatus(100801) == 2) ? 1
							: ((chr.getQuestStatus(100802) == 2) ? 2 : 0), j = 0; j < adddd; ++j) {
						if (Randomizer.isSuccess(50)) {
							++added;
						}
					}
					int now = (int) chr.getKeyValue(100803, "count");
					if (now < 0) {
						now = 0;
					}
					chr.setKeyValue(100803, "count", now + 1 + "");
					// chr.AddBloomingCoin((Calendar.getInstance().get(7) == 7 ||
					// Calendar.getInstance().get(7) == 1) ? 2 : 1, mapitem.getPosition());
					chr.getClient().send(SLFCGPacket.EventSkillOnFlowerEffect(7, 1));
					if (chr.getKeyValue(100803, "count") == 5L) {
						chr.getClient().send(CField.enforceMsgNPC(9062527, 1300, "꽃의 보석에 햇살의 힘이 #r절반#k이나 모였어요!"));
					} else if (chr.getKeyValue(100803, "count") == 10L) {
						chr.setKeyValue(100803, "count", "0");
						// chr.AddBloomingCoin((Calendar.getInstance().get(7) == 7 ||
						// Calendar.getInstance().get(7) == 1) ? 40 : 20, chr.getPosition());
						chr.getClient().send(SLFCGPacket.EventSkillOn(1, 1 + added));
						chr.getClient().send(CField.enforceMsgNPC(9062527, 1000, "햇살의 힘으로 #r꽃씨#k를 펑!펑!"));
						chr.getClient().send(CField.UIPacket.detailShowInfo("하나의 꽃씨 뿌리기로 블루밍 코인 "
								+ ((Calendar.getInstance().get(7) == 7 || Calendar.getInstance().get(7) == 1) ? 40 : 20)
								+ "개를 획득했습니다.", 3, 20, 20));
						if (added >= 1 && chr.getQuestStatus(100801) == 2) {
							Timer.BuffTimer.getInstance().schedule(() -> {
								// chr.AddBloomingCoin((Calendar.getInstance().get(7) == 7 ||
								// Calendar.getInstance().get(7) == 1) ? 20 : 10, chr.getPosition());
								chr.getClient().send(CField.enforceMsgNPC(9062528, 1000, "#r꽃비#k가 쏴아아!\r\n너무 예뻐!"));
								chr.getClient()
										.send(CField.UIPacket.detailShowInfo("두나의 꽃비로 블루밍 코인 "
												+ ((Calendar.getInstance().get(7) == 7
														|| Calendar.getInstance().get(7) == 1) ? 20 : 10)
												+ "개를 획득했습니다.", 3, 20, 20));
								return;
							}, 2500L);
						}
						if (added == 2 && chr.getQuestStatus(100802) == 2) {
							Timer.BuffTimer.getInstance().schedule(() -> {
								// chr.AddBloomingCoin((Calendar.getInstance().get(7) == 7 ||
								// Calendar.getInstance().get(7) == 1) ? 40 : 20, chr.getPosition());
								chr.getClient()
										.send(CField.enforceMsgNPC(9062529, 1000, "#r햇살#k이 샤라랑!\r\n꽃들아 잘 자라라~!"));
								chr.getClient()
										.send(CField.UIPacket.detailShowInfo("세나의 햇살 비추기로 블루밍 코인 "
												+ ((Calendar.getInstance().get(7) == 7
														|| Calendar.getInstance().get(7) == 1) ? 40 : 20)
												+ "개를 획득했습니다.", 3, 20, 20));
								return;
							}, 4500L);
						}
					}
				}
				removeItem_Pet(chr, mapitem, petz, pet.getPetItemId());
				return;
			}
			if (mapitem.getItemId() == 2002058) {
				if (c.getPlayer().hasDisease(SecondaryStat.DeathMark)) {
					c.getPlayer().cancelDisease(SecondaryStat.DeathMark);
				}
				c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.getInventoryStatus(true));
				removeItem_Pet(chr, mapitem, petz, pet.getPetItemId());
				return;
			}
			if (mapitem.getItemId() == 2434851) {
				if (!chr.getBuffedValue(25121133)) {
					c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
					return;
				}
				final SecondaryStatEffect effect = SkillFactory.getSkill(25121133).getEffect(1);
				long duration = chr.getBuffLimit(25121133);
				duration += 4000L;
				if (duration >= effect.getCoolRealTime()) {
					duration = effect.getCoolRealTime();
				}
				effect.applyTo(chr, chr, false, chr.getPosition(), (int) duration, (byte) 0, false);
				c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.getInventoryStatus(true));
				removeItem_Pet(chr, mapitem, petz, pet.getPetItemId());
			} else {
				if (mapitem.getOwner() != chr.getId() && ((!mapitem.isPlayerDrop() && mapitem.getDropType() == 0)
						|| (mapitem.isPlayerDrop() && chr.getMap().getEverlast()))) {
					return;
				}
				if (!mapitem.isPlayerDrop() && mapitem.getDropType() == 1 && mapitem.getOwner() != chr.getId()
						&& (chr.getParty() == null || chr.getParty().getMemberById(mapitem.getOwner()) == null)) {
					return;
				}
				if (GameConstants.isArcaneSymbol(mapitem.getItemId())) {
					final Equip equip = (Equip) mapitem.getItem();
					equip.setArc((short) 30);
					equip.setArcLevel(1);
					equip.setArcEXP(1);
					if (GameConstants.isXenon(c.getPlayer().getJob())) {
						equip.setStr((short) 117);
						equip.setDex((short) 117);
						equip.setLuk((short) 117);
					} else if (GameConstants.isDemonAvenger(c.getPlayer().getJob())) {
						equip.setHp((short) 525);
					} else if (GameConstants.isWarrior(c.getPlayer().getJob())) {
						equip.setStr((short) 300);
					} else if (GameConstants.isMagician(c.getPlayer().getJob())) {
						equip.setInt((short) 300);
					} else if (GameConstants.isArcher(c.getPlayer().getJob())
							|| GameConstants.isCaptain(c.getPlayer().getJob())
							|| GameConstants.isMechanic(c.getPlayer().getJob())
							|| GameConstants.isAngelicBuster(c.getPlayer().getJob())) {
						equip.setDex((short) 300);
					} else if (GameConstants.isThief(c.getPlayer().getJob())) {
						equip.setLuk((short) 300);
					} else if (GameConstants.isPirate(c.getPlayer().getJob())) {
						equip.setStr((short) 300);
					}
				} else if (GameConstants.isAuthenticSymbol(mapitem.getItemId())) {
					final Equip equip = (Equip) mapitem.getItem();
					if (equip.getArcLevel() == 0) {
						equip.setArc((short) 10);
						equip.setArcLevel(1);
						equip.setArcEXP(1);
						if (GameConstants.isXenon(c.getPlayer().getJob())) {
							equip.setStr((short) 317);
							equip.setDex((short) 317);
							equip.setLuk((short) 317);
						} else if (GameConstants.isDemonAvenger(c.getPlayer().getJob())) {
							equip.setHp((short) 725);
						} else if (GameConstants.isWarrior(c.getPlayer().getJob())) {
							equip.setStr((short) 500);
						} else if (GameConstants.isMagician(c.getPlayer().getJob())) {
							equip.setInt((short) 500);
						} else if (GameConstants.isArcher(c.getPlayer().getJob())
								|| GameConstants.isCaptain(c.getPlayer().getJob())
								|| GameConstants.isMechanic(c.getPlayer().getJob())
								|| GameConstants.isAngelicBuster(c.getPlayer().getJob())) {
							equip.setDex((short) 500);
						} else if (GameConstants.isThief(c.getPlayer().getJob())) {
							equip.setLuk((short) 500);
						} else if (GameConstants.isPirate(c.getPlayer().getJob())) {
							equip.setStr((short) 500);
						}
					}
				}
				if (mapitem.isPickpoket()) {
					return;
				}
				if (mapitem.getMeso() > 0) {
					chr.gainMeso(mapitem.getMeso(), true, false, true, true);
					removeItem_Pet(chr, mapitem, petz, pet.getPetItemId());
				} else {
					if (MapleItemInformationProvider.getInstance().isPickupBlocked(mapitem.getItemId())
							|| mapitem.getItemId() / 10000 == 291) {
						return;
					}
					if (useItem(c, mapitem.getItemId())) {
						removeItem_Pet(chr, mapitem, petz, pet.getPetItemId());
					} else if (mapitem.getItemId() == 2431835) {
						MapleItemInformationProvider.getInstance().getItemEffect(2002093).applyTo(chr, true);
						removeItem_Pet(chr, mapitem, petz, pet.getPetItemId());
					} else if (MapleInventoryManipulator.checkSpace(c, mapitem.getItemId(),
							mapitem.getItem().getQuantity(), mapitem.getItem().getOwner())) {
						if (mapitem.getItem().getItemId() == 4001886) {
							if (mapitem.getItem().getBossid() != 0) {
								int party = chr.getParty() != null ? chr.getParty().getMembers().size() : 1;
								int meso = 0, mobid = 0;
								for (Triple<Integer, Integer, Integer> list : BossRewardMeso.getLists()) {
									int bossid = BossRewardMeso.RewardBossId(list.getMid());
									if (bossid == 0) {
										bossid = list.getLeft();
									}

									if (bossid == mapitem.getItem().getBossid()
											|| list.getMid() == mapitem.getItem().getBossid()) {
										meso = list.getRight();
										mobid = list.getMid();
										break;
									}
								}
								mapitem.getItem().setExpiration(System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000));
								mapitem.getItem()
										.setReward(new BossReward(
												chr.getInventory(MapleInventoryType.ETC).countById(4001886) + 1, mobid,
												party, meso));
							} else {
								mapitem.getItem().setReward(new BossReward(
										chr.getInventory(MapleInventoryType.ETC).countById(4001886) + 1, 100100, 1, 1));
							}
						}
						MapleInventoryManipulator.addFromDrop(c, mapitem.getItem(), false,
								mapitem.getDropper() instanceof MapleMonster, true);
						removeItem_Pet(chr, mapitem, petz, pet.getPetItemId());
						if (mapitem.getEquip() != null && mapitem.getDropper().getType() == MapleMapObjectType.MONSTER
								&& mapitem.getEquip().getState() > 0) {
							c.getSession().writeAndFlush((Object) CField.EffectPacket.showEffect(chr, 0, 0, 65, 0, 0,
									(byte) 0, true, null, null, mapitem.getItem()));
						}
					}
				}
			}
		} finally {
			mapitem.getLock().unlock();
		}
	}

	public static final boolean useItem(final MapleClient c, final int id) {
		if (GameConstants.isUse(id)) {
			final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
			final SecondaryStatEffect eff = ii.getItemEffect(id);
			if (eff == null) {
				return false;
			}
			if (id / 10000 == 291) {
				boolean area = false;
				for (final Rectangle rect : c.getPlayer().getMap().getAreas()) {
					if (rect.contains(c.getPlayer().getTruePosition())) {
						area = true;
						break;
					}
				}
				if (!c.getPlayer().inPVP() || (c.getPlayer().getTeam() == id - 2910000 && area)) {
					return false;
				}
			}
			final int consumeval = eff.getConsume();
			if (consumeval > 0) {
				if (c.getPlayer().getMapId() == 109090300) {
					for (final MapleCharacter chr : c.getPlayer().getMap().getCharacters()) {
						if (chr != null && ((id == 2022163 && c.getPlayer().isCatched == chr.isCatched)
								|| ((id == 2022165 || id == 2022166) && c.getPlayer().isCatched != chr.isCatched))) {
							if (id == 2022163) {
								ii.getItemEffect(id).applyTo(chr);
							} else if (id == 2022166) {
								chr.giveDebuff(SecondaryStat.Stun, MobSkillFactory.getMobSkill(123, 1));
							} else {
								if (id != 2022165) {
									continue;
								}
								chr.giveDebuff(SecondaryStat.Slow, MobSkillFactory.getMobSkill(126, 1));
							}
						}
					}
					c.getSession()
							.writeAndFlush((Object) CWvsContext.InfoPacket.getShowItemGain(id, (short) (-1), true));
					return true;
				}
				consumeItem(c, eff);
				consumeItem(c, ii.getItemEffectEX(id));
				c.getSession().writeAndFlush((Object) CWvsContext.InfoPacket.getShowItemGain(id, (short) (-1), true));
				return true;
			}
		}
		return false;
	}

	public static final void consumeItem(final MapleClient c, final SecondaryStatEffect eff) {
		if (eff == null) {
			return;
		}
		if (eff.getConsume() == 2) {
			if (c.getPlayer().getParty() != null && c.getPlayer().isAlive()) {
				for (final MaplePartyCharacter pc : c.getPlayer().getParty().getMembers()) {
					final MapleCharacter chr = c.getPlayer().getMap().getCharacterById(pc.getId());
					if (chr != null && chr.isAlive()) {
						eff.applyTo(chr, true);
					}
				}
			} else {
				eff.applyTo(c.getPlayer(), true);
			}
		} else if (c.getPlayer().isAlive()) {
			eff.applyTo(c.getPlayer(), true);
		}
	}

	public static final void removeItem_Pet(final MapleCharacter chr, final MapleMapItem mapitem, final int index,
			final int id) {
		mapitem.setPickedUp(true);
		chr.getMap().broadcastMessage(CField.removeItemFromMap(mapitem.getObjectId(), 5, chr.getId(), index));
		chr.getMap().removeMapObject(mapitem);
		if (mapitem.isRandDrop()) {
			chr.getMap().spawnRandDrop();
		}
	}

	public static final void pickupItem(MapleMapObject ob, MapleClient c, MapleCharacter chr) {
		final MapleMapItem mapitem = (MapleMapItem) ob;
		try {
			mapitem.getLock().lock();
			if (mapitem.isPickedUp()) {
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				return;
			}

			if (mapitem.getItemId() == 2431174 || mapitem.getItemId() == 2434021) { // 명예의 훈장.
				int rand = Randomizer.rand(10, 40);
				c.getPlayer().gainHonor(rand);
				c.getSession().writeAndFlush(CWvsContext.InventoryPacket.getInventoryStatus(true));
				removeItem(chr, mapitem, ob);
				return;
			}

			if (mapitem.getItemId() == 2636420) { // 희미한 솔 에르다의 기운
				int rand = Randomizer.rand(10, 20);
				c.getPlayer().gainSolErdaStrength(rand);
				c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.getInventoryStatus(true));
				InventoryHandler.removeItem(chr, mapitem, ob);
				return;
			}

			if (mapitem.getItemId() == 2636421) { // 솔 에르다의 기운
				int rand = Randomizer.rand(200, 400);
				c.getPlayer().gainSolErdaStrength(rand);
				c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.getInventoryStatus(true));
				InventoryHandler.removeItem(chr, mapitem, ob);
				return;
			}

			if (mapitem.getItemId() == 2636422) { // 짙은 솔 에르다의 기운
				int rand = Randomizer.rand(500, 1000);
				c.getPlayer().gainSolErdaStrength(rand);
				c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.getInventoryStatus(true));
				InventoryHandler.removeItem(chr, mapitem, ob);
				return;
			}

			if (mapitem.getItemId() == 2432393) {
				chr.gainMeso(Randomizer.rand(10000, 30000), true, false, false, false);
				removeItem(chr, mapitem, ob);
				return;
			}
			if (mapitem.getItemId() == 2432394) {
				chr.gainMeso(Randomizer.rand(30000, 50000), true, false, false, false);
				removeItem(chr, mapitem, ob);
				return;
			}
			if (mapitem.getItemId() == 2432395) {
				mapitem.getItem().setItemId(2000005);
			}
			if (mapitem.getItemId() == 2023484 || mapitem.getItemId() == 2023494 || mapitem.getItemId() == 2023495
					|| mapitem.getItemId() == 2023669) {
				if (mapitem.getDropper() instanceof MapleMonster) {
					int bonus;
					if (mapitem.getItemId() % 100 == 84) {
						bonus = 1;
					} else if (mapitem.getItemId() % 100 == 94) {
						bonus = 1;
					} else if (mapitem.getItemId() % 100 == 95) {
						bonus = 1;
					} else {
						bonus = 1;
					}

					if (chr.getSkillLevel(20000297) > 0) {
						bonus *= SkillFactory.getSkill(20000297).getEffect(chr.getSkillLevel(20000297)).getX();
						bonus /= 100;
					} else if (chr.getSkillLevel(80000370) > 0) {
						bonus *= SkillFactory.getSkill(80000370).getEffect(chr.getSkillLevel(80000370)).getX();
						bonus /= 100;
					}

					bonus *= 20;

					MapleMonster mob = (MapleMonster) mapitem.getDropper();
					chr.gainExp(mob.getMobExp() * c.getChannelServer().getExpRate() * bonus, true, true, false);
					chr.getClient().getSession()
							.writeAndFlush(CField.EffectPacket.showEffect(chr, 0,
									(int) mob.getMobExp() * c.getChannelServer().getExpRate() * bonus, 25, 0, 0,
									(byte) 0, true, null, "", null));
				}
				c.getSession().writeAndFlush(CWvsContext.InventoryPacket.getInventoryStatus(true));
				removeItem(chr, mapitem, ob);
				return;
			}

			if (mapitem.getItemId() >= 4034942 && mapitem.getItemId() <= 4034958) {
				if (c.getPlayer().getRecipe().left == mapitem.getItemId()) {
					c.getPlayer().setRecipe(new Pair<>(mapitem.getItemId(), c.getPlayer().getRecipe().right + 1));
				} else {
					c.getPlayer().setRecipe(new Pair<>(mapitem.getItemId(), 1));
				}
				chr.getMap().broadcastMessage(CField.addItemMuto(chr));
				c.getSession().writeAndFlush(CWvsContext.InventoryPacket.getInventoryStatus(true));
				removeItem(chr, mapitem, ob);
				return;
			}
			if (mapitem.getQuest() > 0 && chr.getQuestStatus(mapitem.getQuest()) != 1) {
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				return;
			}
			if (mapitem.getOwner() != chr.getId() && ((!mapitem.isPlayerDrop() && mapitem.getDropType() == 0)
					|| (mapitem.isPlayerDrop() && chr.getMap().getEverlast()))) {
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				return;
			}
			if (!mapitem.isPlayerDrop() && mapitem.getDropType() == 1 && mapitem.getOwner() != chr.getId()
					&& (chr.getParty() == null || chr.getParty().getMemberById(mapitem.getOwner()) == null)) {
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				return;
			}
			if (mapitem.getMeso() > 0) {
				if (chr.getParty() != null && mapitem.getOwner() != chr.getId()) {
					final List<MapleCharacter> toGive = new LinkedList<MapleCharacter>();
					final int splitMeso = mapitem.getMeso() * 40 / 100;
					for (MaplePartyCharacter z : chr.getParty().getMembers()) {
						MapleCharacter m = chr.getMap().getCharacterById(z.getId());
						if (m != null && m.getId() != chr.getId()) {
							toGive.add(m);
						}
					}
					for (final MapleCharacter m : toGive) {
						int mesos = splitMeso / toGive.size()
								+ (m.getStat().hasPartyBonus ? (int) (mapitem.getMeso() / 20.0) : 0);
						if (mapitem.getDropper() instanceof MapleMonster && m.getStat().incMesoProp > 0) {
							mesos += Math.floor((m.getStat().incMesoProp * mesos) / 100.0f);
						}
						m.gainMeso(mesos, true, false, false, false);
					}
					int mesos = mapitem.getMeso() - splitMeso;
					if (mapitem.getDropper() instanceof MapleMonster && chr.getStat().incMesoProp > 0) {
						mesos += Math.floor((chr.getStat().incMesoProp * mesos) / 100.0f);
					}
					chr.gainMeso(mesos, true, false, false, false);
				} else {
					int mesos = mapitem.getMeso();
					if (mapitem.getDropper() instanceof MapleMonster && chr.getStat().incMesoProp > 0) {
						mesos += Math.floor((chr.getStat().incMesoProp * mesos) / 100.0f);
					}
					chr.gainMeso(mesos, true, false, false, false);
				}
				if (mapitem.getDropper().getType() == MapleMapObjectType.MONSTER) {
					c.getSession().writeAndFlush(CWvsContext.onMesoPickupResult(mapitem.getMeso()));
				}
				removeItem(chr, mapitem, ob);
			} else if (MapleItemInformationProvider.getInstance().isPickupBlocked(mapitem.getItemId())) {
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
				c.getPlayer().dropMessage(5, "This item cannot be picked up.");
			} else if (c.getPlayer().inPVP()
					&& Integer.parseInt(c.getPlayer().getEventInstance().getProperty("ice")) == c.getPlayer().getId()) {
				c.getSession().writeAndFlush(CWvsContext.InventoryPacket.getInventoryFull());
				c.getSession().writeAndFlush(CWvsContext.InventoryPacket.getShowInventoryFull());
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
			} else if (mapitem.getItemId() == 2431850) {
				MapleItemInformationProvider.getInstance().getItemEffect(2002093).applyTo(chr, true);
				removeItem(chr, mapitem, ob);
			} else if (mapitem.getItemId() / 10000 != 291 && MapleInventoryManipulator.checkSpace(c,
					mapitem.getItemId(), mapitem.getItem().getQuantity(), mapitem.getItem().getOwner())) {
				if (mapitem.getItem().getQuantity() >= 50 && mapitem.getItemId() == 2340000) {
					c.setMonitored(true); // hack check
				}
				for (int id : GameConstants.showEffectDropItems) {
					if (id == mapitem.getItemId()) {
						c.getSession().writeAndFlush(CField.EffectPacket.showEffect(chr, 0, 0, 65, 0, 0, (byte) 0, true,
								null, null, mapitem.getItem()));
						break;
					}
				}
				if (GameConstants.isArcaneSymbol(mapitem.getItemId())) {
					final Equip equip = (Equip) mapitem.getEquip();
					equip.setArc((short) 30);
					equip.setArcLevel((byte) 1);
					if (equip.getArcEXP() == 0) {
						equip.setArcEXP(1);
					}

					if (GameConstants.isXenon(c.getPlayer().getJob())) {
						equip.setStr((short) 117);
						equip.setDex((short) 117);
						equip.setLuk((short) 117);
					} else if (GameConstants.isDemonAvenger(c.getPlayer().getJob())) {
						equip.setHp((short) 525);
					} else if (GameConstants.isWarrior(c.getPlayer().getJob())) {
						equip.setStr((short) 300);
					} else if (GameConstants.isMagician(c.getPlayer().getJob())) {
						equip.setInt((short) 300);
					} else if (GameConstants.isArcher(c.getPlayer().getJob())
							|| GameConstants.isCaptain(c.getPlayer().getJob())
							|| GameConstants.isMechanic(c.getPlayer().getJob())
							|| GameConstants.isAngelicBuster(c.getPlayer().getJob())) {
						equip.setDex((short) 300);
					} else if (GameConstants.isThief(c.getPlayer().getJob())) {
						equip.setLuk((short) 300);
					} else if (GameConstants.isPirate(c.getPlayer().getJob())) {
						equip.setStr((short) 300);
					}
				}

				if (GameConstants.isAuthenticSymbol(mapitem.getItemId())) {
					final Equip equip = (Equip) mapitem.getEquip();
					if (equip.getArcLevel() == 0) {
						equip.setArc((short) 10);
						equip.setArcLevel((byte) 1);
						equip.setArcEXP(1);
						if (GameConstants.isXenon(c.getPlayer().getJob())) {
							equip.setStr((short) 195);
							equip.setDex((short) 195);
							equip.setLuk((short) 195);
						} else if (GameConstants.isDemonAvenger(c.getPlayer().getJob())) {
							equip.setHp((short) 8750);
						} else if (GameConstants.isWarrior(c.getPlayer().getJob())) {
							equip.setStr((short) 500);
						} else if (GameConstants.isMagician(c.getPlayer().getJob())) {
							equip.setInt((short) 500);
						} else if (GameConstants.isArcher(c.getPlayer().getJob())
								|| GameConstants.isCaptain(c.getPlayer().getJob())
								|| GameConstants.isMechanic(c.getPlayer().getJob())
								|| GameConstants.isAngelicBuster(c.getPlayer().getJob())) {
							equip.setDex((short) 500);
						} else if (GameConstants.isThief(c.getPlayer().getJob())) {
							equip.setLuk((short) 500);
						} else if (GameConstants.isPirate(c.getPlayer().getJob())) {
							equip.setStr((short) 500);
						}
					}
				}
				if (mapitem.getItem().getItemId() == 4001886) {
					if (mapitem.getItem().getBossid() != 0) {
						int party = chr.getParty() != null ? chr.getParty().getMembers().size() : 1;
						int meso = 0, mobid = 0;
						for (Triple<Integer, Integer, Integer> list : BossRewardMeso.getLists()) {
							int bossid = BossRewardMeso.RewardBossId(list.getMid());
							if (bossid == 0) {
								bossid = list.getLeft();
							}
							if (bossid == mapitem.getItem().getBossid()
									|| list.getMid() == mapitem.getItem().getBossid()) {
								meso = list.getRight();
								mobid = list.getMid();
								break;
							}
						}
						// Boss Reward Management
						mapitem.getItem().setReward(new BossReward(
								chr.getInventory(MapleInventoryType.ETC).countById(4001886) + 1, mobid, party, meso));
						mapitem.getItem().setExpiration(System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000));
					} else {
						mapitem.getItem().setReward(new BossReward(
								chr.getInventory(MapleInventoryType.ETC).countById(4001886) + 1, 100100, 1, 1));
					}
				}
				MapleInventoryManipulator.addFromDrop(c, mapitem.getItem(), true,
						mapitem.getDropper() instanceof MapleMonster, false);
				removeItem(chr, mapitem, ob);
			} else {
				c.getSession().writeAndFlush(CWvsContext.InventoryPacket.getInventoryFull());
				c.getSession().writeAndFlush(CWvsContext.InventoryPacket.getShowInventoryFull());
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
			}
		} finally {
			mapitem.getLock().unlock();
		}
	}

	public static final void removeItem(final MapleCharacter chr, final MapleMapItem mapitem, final MapleMapObject ob) {
		mapitem.setPickedUp(true);
		chr.getMap().broadcastMessage(CField.removeItemFromMap(mapitem.getObjectId(), 2, chr.getId()),
				mapitem.getPosition());
		chr.getMap().removeMapObject(mapitem);
		if (mapitem.isRandDrop()) {
			chr.getMap().spawnRandDrop();
		}
	}

	public static final void addMedalString(final MapleCharacter c, final StringBuilder sb) {
		final Item medal = c.getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-21));
		if (medal != null) {
			sb.append("<");
			if (medal.getItemId() == 1142257 && GameConstants.isAdventurer(c.getJob())) {
				final MapleQuestStatus stat = c.getQuestNoAdd(MapleQuest.getInstance(111111));
				if (stat != null && stat.getCustomData() != null) {
					sb.append(stat.getCustomData());
					sb.append("'s Successor");
				} else {
					sb.append(MapleItemInformationProvider.getInstance().getName(medal.getItemId()));
				}
			} else {
				sb.append(MapleItemInformationProvider.getInstance().getName(medal.getItemId()));
			}
			sb.append("> ");
		}
	}

	public static final void TeleRock(final LittleEndianAccessor slea, final MapleClient c) {
		final short slot = slea.readShort();
		final int itemId = slea.readInt();
		final Item toUse = c.getPlayer().getInventory(MapleInventoryType.USE).getItem(slot);
		if (toUse == null || toUse.getQuantity() < 1 || toUse.getItemId() != itemId || itemId / 10000 != 232) {
			c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
			return;
		}
		final boolean used = UseTeleRock(slea, c, itemId);
		if (used) {
			MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
		}
		c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
	}

	public static final boolean UseTeleRock(final LittleEndianAccessor slea, final MapleClient c, final int itemId) {
		boolean used = false;
		if (slea.readByte() == 0) {
			final MapleMap target = c.getChannelServer().getMapFactory().getMap(slea.readInt());
			if (target != null && ((itemId == 5041000 && c.getPlayer().isRockMap(target.getId()))
					|| ((itemId == 5040000 || itemId == 5040001) && c.getPlayer().isRegRockMap(target.getId()))
					|| ((itemId == 5040004 || itemId == 5041001) && (c.getPlayer().isHyperRockMap(target.getId())
							|| GameConstants.isHyperTeleMap(target.getId()))))) {
				if (!FieldLimitType.VipRock.check(c.getPlayer().getMap().getFieldLimit())
						&& !FieldLimitType.VipRock.check(target.getFieldLimit())) {
					c.getPlayer().changeMap(target, target.getPortal(0));
					used = true;
				} else {
					c.getPlayer().dropMessage(1, "You cannot go to that place.");
				}
			} else {
				c.getPlayer().dropMessage(1, "You cannot go to that place.");
			}
		} else {
			final String name = slea.readMapleAsciiString();
			final MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(name);
			if (victim != null && !victim.isIntern() && c.getPlayer().getEventInstance() == null
					&& victim.getEventInstance() == null) {
				if (!FieldLimitType.VipRock.check(c.getPlayer().getMap().getFieldLimit()) && !FieldLimitType.VipRock
						.check(c.getChannelServer().getMapFactory().getMap(victim.getMapId()).getFieldLimit())) {
					if (itemId == 5041000 || itemId == 5040004 || itemId == 5041001
							|| victim.getMapId() / 100000000 == c.getPlayer().getMapId() / 100000000) {
						c.getPlayer().changeMap(victim.getMap(),
								victim.getMap().findClosestPortal(victim.getTruePosition()));
						used = true;
					} else {
						c.getPlayer().dropMessage(1, "You cannot go to that place.");
					}
				} else {
					c.getPlayer().dropMessage(1, "You cannot go to that place.");
				}
			} else {
				c.getPlayer().dropMessage(1,
						"(" + name + ") is currently difficult to locate, so the teleport will not take place.");
			}
		}
		return used;
	}

	public static void UsePetLoot(final LittleEndianAccessor slea, final MapleClient c) {
		slea.readInt();
		final short mode = slea.readShort();
		c.getPlayer().setPetLoot(mode == 1);
		for (int i = 0; i < c.getPlayer().getPets().length; ++i) {
			if (c.getPlayer().getPet(i) != null) {
				c.getSession()
						.writeAndFlush((Object) PetPacket.updatePet(c.getPlayer(), c.getPlayer().getPet(i),
								c.getPlayer().getInventory(MapleInventoryType.CASH)
										.getItem(c.getPlayer().getPet(i).getInventoryPosition()),
								false, c.getPlayer().getPetLoot()));
			}
		}
		c.getSession().writeAndFlush((Object) PetPacket.updatePetLootStatus(mode));
	}

	public static void UsePetSkill(final LittleEndianAccessor slea, final MapleClient c) {
		slea.readInt();
		slea.readInt();
		final short mode = slea.readByte();
		if (mode == 1) {
			c.setCustomData(12334, "preventAutoBuff", "1");
		} else {
			c.setCustomData(12334, "preventAutoBuff", "0");
		}
	}

	public static void SelectPQReward(final LittleEndianAccessor slea, final MapleClient c) {
		slea.skip(1);
		final int randval = RandomRewards.getRandomReward();
		final short quantity = (short) Randomizer.rand(1, 10);
		MapleInventoryManipulator.addById(c, randval, quantity,
				"Reward item: " + randval + " on " + FileoutputUtil.CurrentReadable_Date());
		if (c.getPlayer().getMapId() == 100000203) {
			final MapleMap map = c.getChannelServer().getMapFactory().getMap(960000000);
			c.getPlayer().changeMap(map, map.getPortal(0));
		} else {
			c.getPlayer().fakeRelog();
		}
		c.getSession().writeAndFlush((Object) CField.EffectPacket.showCharmEffect(c.getPlayer(), randval, 1, true, ""));
	}

	public static void resetZeroWeapon(final MapleCharacter chr) {
		final Equip newa = (Equip) MapleItemInformationProvider.getInstance()
				.getEquipById(chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-11)).getItemId());
		final Equip newb = (Equip) MapleItemInformationProvider.getInstance()
				.getEquipById(chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-10)).getItemId());
		((Equip) chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-11))).set(newa);
		((Equip) chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-10))).set(newb);
		chr.getClient().getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.updateInventoryItem(false,
				MapleInventoryType.EQUIP, chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-11))));
		chr.getClient().getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.updateInventoryItem(false,
				MapleInventoryType.EQUIP, chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-10))));
		chr.dropMessage(5,
				"\uc81c\ub85c\uc758 \uc7a5\ube44\ub294 \ud30c\uad34\ub418\ub294\ub300\uc2e0 \ucc98\uc74c \uc0c1\ud0dc\ub85c \ub418\ub3cc\uc544\uac11\ub2c8\ub2e4.");
	}

	public static void UseNameChangeCoupon(final LittleEndianAccessor slea, final MapleClient c) {
		final short slot = slea.readShort();
		final int itemId = slea.readInt();
		final Item toUse = c.getPlayer().getInventory(MapleInventoryType.USE).getItem(slot);
		if (toUse == null || toUse.getQuantity() < 1 || toUse.getItemId() != itemId) {
			c.getSession().writeAndFlush((Object) CWvsContext.nameChangeUI(false));
			c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
			return;
		}
		c.setNameChangeEnable((byte) 1);
		MapleCharacter.updateNameChangeCoupon(c);
		c.getSession().writeAndFlush((Object) CWvsContext.nameChangeUI(true));
		MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
	}

	public static void UseKaiserColorChange(final LittleEndianAccessor slea, final MapleClient c) {
		final short slot = slea.readShort();
		slea.skip(2);
		final int itemId = slea.readInt();
		final Item toUse = c.getPlayer().getInventory(MapleInventoryType.USE).getItem(slot);
		if (toUse == null || toUse.getQuantity() < 1 || toUse.getItemId() != itemId) {
			return;
		}
		final int[] colors = { 841, 842, 843, 758, 291, 317, 338, 339, 444, 445, 446, 458, 461, 447, 450, 454, 455, 456,
				457, 459, 460, 462, 463, 464, 289, 4, 34, 35, 64, 9, 10, 12, 11, 16, 17, 22, 24, 53, 61, 62, 63, 67, 68,
				109, 110, 111, 112, 113, 114, 115, 116, 117, 121, 125, 128, 129, 145, 150 };
		if (itemId == 2350004) {
			c.getPlayer().setKeyValue(12860, "extern", colors[Randomizer.nextInt(colors.length)] + "");

		} else if (itemId == 2350005) {
			c.getPlayer().setKeyValue(12860, "inner", colors[Randomizer.nextInt(colors.length)] + "");
		} else if (itemId == 2350006) {
			c.getPlayer().setKeyValue(12860, "extern", "842");
		} else if (itemId == 2350007) {
			c.getPlayer().setKeyValue(12860, "premium", "0");
			c.getPlayer().setKeyValue(12860, "inner", "0");
			c.getPlayer().setKeyValue(12860, "extern", "0");
		}
		if (c.getPlayer().getKeyValue(12860, "extern") == -1L) {
			c.getPlayer().setKeyValue(12860, "extern", "0");
		}
		if (c.getPlayer().getKeyValue(12860, "inner") == -1L) {
			c.getPlayer().setKeyValue(12860, "inner", "0");
		}
		if (c.getPlayer().getKeyValue(12860, "premium") == -1L) {
			c.getPlayer().setKeyValue(12860, "premium", "0");
		}
		c.getPlayer().getMap()
				.broadcastMessage(CField.KaiserChangeColor(c.getPlayer().getId(),
						(c.getPlayer().getKeyValue(12860, "extern") == -1L) ? 0
								: ((int) c.getPlayer().getKeyValue(12860, "extern")),
						(c.getPlayer().getKeyValue(12860, "inner") == -1L) ? 0
								: ((int) c.getPlayer().getKeyValue(12860, "inner")),
						(byte) ((c.getPlayer().getKeyValue(12860, "premium") == -1L) ? 0
								: ((byte) c.getPlayer().getKeyValue(12860, "premium")))));
		MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
		c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
	}

	public static final void UseSoulEnchanter(final LittleEndianAccessor slea, final MapleClient c,
			final MapleCharacter chr) {
		slea.skip(4);
		final short useslot = slea.readShort();
		final short slot = slea.readShort();
		final MapleInventory useInventory = c.getPlayer().getInventory(MapleInventoryType.USE);
		final Item enchanter = useInventory.getItem(useslot);
		Item equip;
		if (slot == -11) {
			equip = c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-11));
		} else {
			equip = c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(slot);
		}
		final Equip nEquip = (Equip) equip;
		nEquip.setSoulEnchanter((short) 9);
		c.getSession().writeAndFlush(
				(Object) CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIP, nEquip));
		chr.getMap().broadcastMessage(chr, CField.showEnchanterEffect(chr.getId(), (byte) 1), true);
		MapleInventoryManipulator.removeById(chr.getClient(), MapleInventoryType.USE, enchanter.getItemId(), 1, true,
				false);
		c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
	}

	public static final void UseSoulScroll(final LittleEndianAccessor rh, final MapleClient c,
			final MapleCharacter chr) {
		rh.skip(4);
		final short useslot = rh.readShort();
		final short slot = rh.readShort();
		final MapleInventory useInventory = c.getPlayer().getInventory(MapleInventoryType.USE);
		final Item soul = useInventory.getItem(useslot);
		final int soula = soul.getItemId() - 2590999;
		final int soulid = soul.getItemId();
		boolean great = false;
		final MapleDataProvider sourceData = MapleDataProviderFactory.getDataProvider(new File("wz/Item.wz"));
		final MapleData dd = sourceData.getData("SkillOption.img");
		final int skillid = MapleDataTool.getIntConvert(dd.getChildByPath("skill/" + soula + "/skillId"));
		Item equip;
		if (slot == -11) {
			equip = c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-11));
		} else {
			equip = c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(slot);
		}
		if (slot == -11) {
			chr.setSoulMP((Equip) equip);
		}
		if (dd.getChildByPath("skill/" + soula + "/tempOption/1/id") != null) {
			great = true;
		}
		short statid = 0;
		if (great) {
			statid = (short) MapleDataTool.getIntConvert(
					dd.getChildByPath("skill/" + soula + "/tempOption/" + Randomizer.nextInt(7) + "/id"));
		} else {
			statid = (short) MapleDataTool.getIntConvert(dd.getChildByPath("skill/" + soula + "/tempOption/0/id"));
		}
		final Equip nEquip = (Equip) equip;
		if (SkillFactory.getSkill(nEquip.getSoulSkill()) != null) {
			chr.changeSkillLevel(nEquip.getSoulSkill(), (byte) (-1), (byte) 0);
		}
		nEquip.setSoulName(GameConstants.getSoulName(soulid));
		nEquip.setSoulPotential(statid);
		nEquip.setSoulSkill(skillid);
		Equip zeros = null;
		if (GameConstants.isZero(c.getPlayer().getJob())) {
			if (slot == -11) {
				zeros = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-10));
			} else if (slot == -10) {
				zeros = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-11));
			}
		}
		if (zeros != null) {
			if (SkillFactory.getSkill(zeros.getSoulSkill()) != null) {
				chr.changeSkillLevel(zeros.getSoulSkill(), (byte) (-1), (byte) 0);
			}
			zeros.setSoulName(nEquip.getSoulName());
			zeros.setSoulPotential(nEquip.getSoulPotential());
			zeros.setSoulSkill(nEquip.getSoulSkill());
			c.getSession().writeAndFlush(
					(Object) CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIP, zeros));
		}
		chr.changeSkillLevel(skillid, (byte) 1, (byte) 1);
		c.getSession().writeAndFlush(
				(Object) CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIP, nEquip));
		chr.getMap().broadcastMessage(chr, CField.showSoulScrollEffect(chr.getId(), (byte) 1, false, nEquip), true);
		MapleInventoryManipulator.removeById(chr.getClient(), MapleInventoryType.USE, soulid, 1, true, false);
		c.getSession().writeAndFlush((Object) CWvsContext.enableActions(chr));
	}

	public static void UseCube(final LittleEndianAccessor slea, final MapleClient c) {
		int pos = 0;
		final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
		final Item cube = c.getPlayer().getInventory(MapleInventoryType.USE).getItem(slea.readShort());
		if (cube.getItemId() >= 2730000 && cube.getItemId() <= 2730005) {
			final int itemid = slea.readInt();
			if (itemid != cube.getItemId()) {
				c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
				return;
			}
		}
		pos = slea.readShort();
		Equip eq = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem((short) pos);
		switch (cube.getItemId()) {
		case 2436499:
		case 2711000:
		case 2711001:
		case 2711009:
		case 2711011: {
			if (GameConstants.isZero(c.getPlayer().getJob()) && eq == null) {
				eq = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-10));
				final Equip eq2 = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED)
						.getItem((short) (-11));
				final int rand = Randomizer.nextInt(100);
				boolean up = false;
				if (c.getPlayer().getMeso() < GameConstants.getCubeMeso(eq.getItemId())) {
					c.getPlayer().dropMessage(6, "\uba54\uc18c\uac00 \ubd80\uc871\ud569\ub2c8\ub2e4.");
					return;
				}
				if (eq.getState() == 17) {
					if (rand < 2) {
						eq.setState((byte) 18);
						eq2.setState((byte) 18);
						up = true;
					} else {
						eq.setState((byte) 17);
						eq2.setState((byte) 17);
					}
				} else if (eq.getState() == 18) {
					eq.setState((byte) 18);
					eq2.setState((byte) 18);
				}
				final int level = eq.getState() - 16;
				final int potential1 = potential(eq.getItemId(), level);
				final int potential2 = potential(eq.getItemId(),
						(level == 1 || Randomizer.isSuccess(2, 200)) ? level : (level - 1));
				final int potential3 = (eq.getPotential3() != 0)
						? potential(eq.getItemId(), (level == 1 || Randomizer.isSuccess(1, 200)) ? level : (level - 1))
						: 0;
				eq.setPotential1(potential1);
				eq.setPotential2(potential2);
				eq.setPotential3(potential3);
				eq2.setPotential1(potential1);
				eq2.setPotential2(potential2);
				eq2.setPotential3(potential3);
				c.getPlayer().gainMeso(-GameConstants.getCubeMeso(eq.getItemId()), false);
				c.getSession().writeAndFlush((Object) CField.showPotentialReset(c.getPlayer().getId(), true,
						cube.getItemId(), eq.getItemId()));
				c.getSession().writeAndFlush(
						(Object) CField.getCubeStart(c.getPlayer(), eq, up, cube.getItemId(), cube.getQuantity()));
				c.getPlayer().forceReAddItem(eq, MapleInventoryType.EQUIPPED);
				c.getPlayer().forceReAddItem(eq2, MapleInventoryType.EQUIPPED);
				break;
			} else {
				final int rand2 = Randomizer.nextInt(100);
				boolean up2 = false;
				if (c.getPlayer().getMeso() < GameConstants.getCubeMeso(eq.getItemId())) {
					c.getPlayer().dropMessage(6, "\uba54\uc18c\uac00 \ubd80\uc871\ud569\ub2c8\ub2e4.");
					return;
				}
				if (eq.getState() == 17) {
					if (rand2 < 2) {
						eq.setState((byte) 18);
						up2 = true;
					} else {
						eq.setState((byte) 17);
					}
				} else if (eq.getState() == 18) {
					eq.setState((byte) 18);
				}
				final int level2 = eq.getState() - 16;
				eq.setPotential1(potential(eq.getItemId(), level2));
				eq.setPotential2(potential(eq.getItemId(),
						(level2 == 1 || Randomizer.isSuccess(2, 200)) ? level2 : (level2 - 1)));
				eq.setPotential3(
						(eq.getPotential3() != 0)
								? potential(eq.getItemId(),
										(level2 == 1 || Randomizer.isSuccess(1, 200)) ? level2 : (level2 - 1))
								: 0);
				c.getPlayer().gainMeso(-GameConstants.getCubeMeso(eq.getItemId()), false);
				c.getSession().writeAndFlush((Object) CField.showPotentialReset(c.getPlayer().getId(), true,
						cube.getItemId(), eq.getItemId()));
				c.getSession().writeAndFlush(
						(Object) CField.getCubeStart(c.getPlayer(), eq, up2, cube.getItemId(), cube.getQuantity()));
				c.getPlayer().forceReAddItem(eq, MapleInventoryType.EQUIP);
				break;
			}
		}
		case 2730000:
		case 2730001:
		case 2730002:
		case 2730004:
		case 2730005: {
			final int rand2 = Randomizer.nextInt(100);
			boolean up2 = false;
			if (eq == null || c.getPlayer().getInventory(MapleInventoryType.USE).getNumFreeSlot() < 1) {
				break;
			}
			if (eq.getPotential4() <= 0) {
				c.getPlayer().dropMessage(1,
						"\uc5d0\ub514\uc154\ub110 \uc7a0\uc7ac\ub2a5\ub825\uc774 \ubd80\uc5ec\ub418\uc9c0 \uc54a\uc558\uc2b5\ub2c8\ub2e4.");
				c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
				return;
			}
			if (c.getPlayer().getInventory(MapleInventoryType.USE).getNumFreeSlot() >= 1) {
				if (GameConstants.isZero(c.getPlayer().getJob())) {
					final Item item2 = c.getPlayer()
							.getInventory((pos < 0) ? MapleInventoryType.EQUIPPED : MapleInventoryType.EQUIP)
							.getItem((short) (-11));
					final Equip eq3 = (Equip) item2;
					if (eq3 != null) {
						eq3.setState((byte) (eq.getState() + 32));
						c.getPlayer().forceReAddItem_NoUpdate(item2, MapleInventoryType.EQUIPPED);
					}
				}
				int level2 = 0;
				level2 = ((eq.getPotential4() >= 10000) ? (eq.getPotential4() / 10000) : (eq.getPotential4() / 100));
				if (level2 >= 4) {
					level2 = 4;
				}
				final int rate = (level2 == 1) ? 3 : 0;
				if (Randomizer.nextInt(100) < rate) {
					up2 = true;
					++level2;
				}
				if (eq.getPotential6() > 0) {
					eq.setPotential4(potential(eq.getItemId(), level2, true));
					eq.setPotential5(potential(eq.getItemId(),
							(level2 == 1 || Randomizer.isSuccess(2, 200)) ? level2 : (level2 - 1), true));
					eq.setPotential6(potential(eq.getItemId(),
							(level2 == 1 || Randomizer.isSuccess(2, 200)) ? level2 : (level2 - 1), true));
				} else {
					eq.setPotential4(potential(eq.getItemId(), level2, true));
					eq.setPotential5(potential(eq.getItemId(),
							(level2 == 1 || Randomizer.isSuccess(2, 200)) ? level2 : (level2 - 1), true));
				}
				c.getSession().writeAndFlush(
						(Object) CField.getCubeStart(c.getPlayer(), eq, up2, cube.getItemId(), cube.getQuantity()));
				c.getSession().writeAndFlush((Object) CField.showPotentialReset(c.getPlayer().getId(), true,
						cube.getItemId(), eq.getItemId()));
				c.getPlayer().forceReAddItem(eq, MapleInventoryType.EQUIP);
				c.getPlayer().gainMeso(-GameConstants.getCubeMeso(eq.getItemId()), false);
				break;
			}
			c.getPlayer().dropMessage(5,
					"\uc18c\ube44 \uc544\uc774\ud15c \uc5ec\uc720 \uacf5\uac04\uc774 \ubd80\uc871\ud558\uc5ec \uc7a0\uc7ac\ub2a5\ub825 \uc7ac\uc124\uc815\uc744 \uc2e4\ud328\ud558\uc600\uc2b5\ub2c8\ub2e4.");
			break;
		}
		case 2711003:
		case 2711005:
		case 2711012: {
			if (GameConstants.isZero(c.getPlayer().getJob()) && eq == null) {
				eq = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-10));
				final Equip eq2 = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED)
						.getItem((short) (-11));
				final int rand = Randomizer.nextInt(100);
				boolean up = false;
				if (c.getPlayer().getMeso() < GameConstants.getCubeMeso(eq.getItemId())) {
					c.getPlayer().dropMessage(6, "\uba54\uc18c\uac00 \ubd80\uc871\ud569\ub2c8\ub2e4.");
					return;
				}
				if (eq.getState() == 17) {
					if (Randomizer.isSuccess(2, 200)) {
						eq.setState((byte) 18);
						eq2.setState((byte) 18);
						up = true;
					} else {
						eq.setState((byte) 17);
						eq2.setState((byte) 17);
					}
				} else if (eq.getState() == 18) {
					if (Randomizer.isSuccess(1, 200)) {
						up = true;
						eq.setState((byte) 19);
						eq2.setState((byte) 19);
					} else {
						eq.setState((byte) 18);
					}
				} else if (eq.getState() == 19) {
					eq.setState((byte) 19);
				}
				final int level = eq.getState() - 16;
				final int potential1 = potential(eq.getItemId(), level);
				final int potential2 = potential(eq.getItemId(),
						(level == 1 || Randomizer.isSuccess(2, 200)) ? level : (level - 1));
				final int potential3 = (eq.getPotential3() != 0)
						? potential(eq.getItemId(), (level == 1 || Randomizer.isSuccess(1, 200)) ? level : (level - 1))
						: 0;
				eq.setPotential1(potential1);
				eq.setPotential2(potential2);
				eq.setPotential3(potential3);
				eq2.setPotential1(potential1);
				eq2.setPotential2(potential2);
				eq2.setPotential3(potential3);
				c.getPlayer().gainMeso(-GameConstants.getCubeMeso(eq.getItemId()), false);
				c.getSession().writeAndFlush((Object) CField.showPotentialReset(c.getPlayer().getId(), true,
						cube.getItemId(), eq.getItemId()));
				c.getSession().writeAndFlush(
						(Object) CField.getCubeStart(c.getPlayer(), eq, up, cube.getItemId(), cube.getQuantity()));
				c.getPlayer().forceReAddItem(eq, MapleInventoryType.EQUIPPED);
				c.getPlayer().forceReAddItem(eq2, MapleInventoryType.EQUIPPED);
				break;
			} else {
				final int rand2 = Randomizer.nextInt(100);
				boolean up2 = false;
				if (c.getPlayer().getMeso() < GameConstants.getCubeMeso(eq.getItemId())) {
					c.getPlayer().dropMessage(6, "\uba54\uc18c\uac00 \ubd80\uc871\ud569\ub2c8\ub2e4.");
					return;
				}
				if (eq.getState() == 17) {
					if (Randomizer.isSuccess(2, 200)) {
						eq.setState((byte) 18);
						up2 = true;
					} else {
						eq.setState((byte) 17);
					}
				} else if (eq.getState() == 18) {
					if (Randomizer.isSuccess(1, 200)) {
						up2 = true;
						eq.setState((byte) 19);
					} else {
						eq.setState((byte) 18);
					}
				} else if (eq.getState() == 19) {
					eq.setState((byte) 19);
				}
				final int level2 = eq.getState() - 16;
				eq.setPotential1(potential(eq.getItemId(), level2));
				eq.setPotential2(potential(eq.getItemId(),
						(level2 == 1 || Randomizer.isSuccess(2, 200)) ? level2 : (level2 - 1)));
				eq.setPotential3(
						(eq.getPotential3() != 0)
								? potential(eq.getItemId(),
										(level2 == 1 || Randomizer.isSuccess(1, 200)) ? level2 : (level2 - 1))
								: 0);
				c.getPlayer().gainMeso(-GameConstants.getCubeMeso(eq.getItemId()), false);
				c.getSession().writeAndFlush((Object) CField.showPotentialReset(c.getPlayer().getId(), true,
						cube.getItemId(), eq.getItemId()));
				c.getSession().writeAndFlush(
						(Object) CField.getCubeStart(c.getPlayer(), eq, up2, cube.getItemId(), cube.getQuantity()));
				c.getPlayer().forceReAddItem(eq, MapleInventoryType.EQUIP);
				break;
			}
		}
		case 2711004:
		case 2711006:
		case 2711013:
		case 2711017: {
			if (GameConstants.isZero(c.getPlayer().getJob()) && eq == null) {
				eq = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-10));
				final Equip eq2 = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED)
						.getItem((short) (-11));
				final int rand = Randomizer.nextInt(100);
				boolean up = false;
				if (c.getPlayer().getMeso() < GameConstants.getCubeMeso(eq.getItemId())) {
					c.getPlayer().dropMessage(6, "\uba54\uc18c\uac00 \ubd80\uc871\ud569\ub2c8\ub2e4.");
					return;
				}
				if (eq.getState() == 17) {
					if (Randomizer.isSuccess(2, 200)) {
						eq.setState((byte) 18);
						eq2.setState((byte) 18);
						up = true;
					} else {
						eq.setState((byte) 17);
						eq2.setState((byte) 17);
					}
				} else if (eq.getState() == 18) {
					if (Randomizer.isSuccess(2, 200)) {
						up = true;
						eq.setState((byte) 19);
						eq2.setState((byte) 19);
					} else {
						eq.setState((byte) 18);
					}
				} else if (eq.getState() == 19) {
					if (Randomizer.isSuccess(1, 200)) {
						up = true;
						eq.setState((byte) 20);
						eq2.setState((byte) 20);
					} else {
						eq.setState((byte) 19);
					}
				}
				final int level = eq.getState() - 16;
				final int potential1 = potential(eq.getItemId(), level);
				final int potential2 = potential(eq.getItemId(),
						(level == 1 || Randomizer.isSuccess(2, 200)) ? level : (level - 1));
				final int potential3 = (eq.getPotential3() != 0)
						? potential(eq.getItemId(), (level == 1 || Randomizer.isSuccess(1, 200)) ? level : (level - 1))
						: 0;
				eq.setPotential1(potential1);
				eq.setPotential2(potential2);
				eq.setPotential3(potential3);
				eq2.setPotential1(potential1);
				eq2.setPotential2(potential2);
				eq2.setPotential3(potential3);
				c.getPlayer().gainMeso(-GameConstants.getCubeMeso(eq.getItemId()), false);
				c.getSession().writeAndFlush((Object) CField.showPotentialReset(c.getPlayer().getId(), true,
						cube.getItemId(), eq.getItemId()));
				c.getSession().writeAndFlush(
						(Object) CField.getCubeStart(c.getPlayer(), eq, up, cube.getItemId(), cube.getQuantity()));
				c.getPlayer().forceReAddItem(eq, MapleInventoryType.EQUIPPED);
				c.getPlayer().forceReAddItem(eq2, MapleInventoryType.EQUIPPED);
				break;
			} else {
				final int rand2 = Randomizer.nextInt(100);
				boolean up2 = false;
				if (c.getPlayer().getMeso() < GameConstants.getCubeMeso(eq.getItemId())) {
					c.getPlayer().dropMessage(6, "\uba54\uc18c\uac00 \ubd80\uc871\ud569\ub2c8\ub2e4.");
					return;
				}
				if (eq.getState() == 17) {
					if (Randomizer.isSuccess(3, 200)) {
						eq.setState((byte) 18);
						up2 = true;
					} else {
						eq.setState((byte) 17);
					}
				} else if (eq.getState() == 18) {
					if (Randomizer.isSuccess(2, 200)) {
						up2 = true;
						eq.setState((byte) 19);
					} else {
						eq.setState((byte) 18);
					}
				} else if (eq.getState() == 19) {
					if (Randomizer.isSuccess(1, 200)) {
						up2 = true;
						eq.setState((byte) 20);
					} else {
						eq.setState((byte) 19);
					}
				}
				final int level2 = eq.getState() - 16;
				eq.setPotential1(potential(eq.getItemId(), level2));
				eq.setPotential2(potential(eq.getItemId(),
						(level2 == 1 || Randomizer.isSuccess(2, 200)) ? level2 : (level2 - 1)));
				eq.setPotential3(
						(eq.getPotential3() != 0)
								? potential(eq.getItemId(),
										(level2 == 1 || Randomizer.isSuccess(1, 200)) ? level2 : (level2 - 1))
								: 0);
				c.getPlayer().gainMeso(-GameConstants.getCubeMeso(eq.getItemId()), false);
				c.getSession().writeAndFlush((Object) CField.showPotentialReset(c.getPlayer().getId(), true,
						cube.getItemId(), eq.getItemId()));
				c.getSession().writeAndFlush(
						(Object) CField.getCubeStart(c.getPlayer(), eq, up2, cube.getItemId(), cube.getQuantity()));
				c.getPlayer().forceReAddItem(eq, MapleInventoryType.EQUIP);
				break;
			}
		}
		}
		c.getPlayer().removeItem(cube.getItemId(), -1);
	}

	public static void UseGoldenHammer(final LittleEndianAccessor rh, final MapleClient c) {
		c.getPlayer().vh = false;
		rh.skip(4);
		final byte slot = (byte) rh.readInt();
		final int itemId = rh.readInt();
		rh.skip(4);
		final byte victimslot = (byte) rh.readInt();
		final Item toUse = c.getPlayer().getInventory(MapleInventoryType.USE).getItem(slot);
		Equip victim = null;
		Equip victim_ = null;
		if (victimslot < 0) {
			victim = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(victimslot);
			if (GameConstants.isZero(c.getPlayer().getJob())) {
				if (victim.getPosition() == -10) {
					victim_ = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-11));
				} else {
					victim_ = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-10));
				}
			}
		} else {
			victim = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(victimslot);
		}
		if (victim == null || toUse == null || toUse.getItemId() != itemId || toUse.getQuantity() < 1) {
			c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
			return;
		}
		c.getSession().writeAndFlush((Object) CSPacket.ViciousHammer(true, c.getPlayer().vh));
		victim.setViciousHammer((byte) 1);
		if (victim_ != null) {
			victim_.setViciousHammer((byte) 1);
		}
		if ((itemId == 2470001 || itemId == 2470002) && Randomizer.nextInt(100) > 50) {
			victim.setUpgradeSlots((byte) (victim.getUpgradeSlots() + 1));
			if (victim_ != null) {
				victim_.setUpgradeSlots((byte) (victim.getUpgradeSlots() + 1));
			}
			c.getPlayer().vh = true;
		} else if (itemId == 2470000) {
			victim.setUpgradeSlots((byte) (victim.getUpgradeSlots() + 1));
			if (victim_ != null) {
				victim_.setUpgradeSlots((byte) (victim_.getUpgradeSlots() + 1));
			}
			c.getPlayer().vh = true;
		} else if (itemId == 2470003) {
			victim.setUpgradeSlots((byte) (victim.getUpgradeSlots() + 1));
			if (victim_ != null) {
				victim_.setUpgradeSlots((byte) (victim_.getUpgradeSlots() + 1));
			}
			c.getPlayer().vh = true;
		} else if (itemId == 2470007) {
			victim.setUpgradeSlots((byte) (victim.getUpgradeSlots() + 1));
			if (victim_ != null) {
				victim_.setUpgradeSlots((byte) (victim_.getUpgradeSlots() + 1));
			}
			c.getPlayer().vh = true;
		} else if (itemId == 2470010) {
			victim.setUpgradeSlots((byte) (victim.getUpgradeSlots() + 1));
			if (victim_ != null) {
				victim_.setUpgradeSlots((byte) (victim_.getUpgradeSlots() + 1));
			}
			c.getPlayer().vh = true;
		} else if (itemId == 2470021) {// 황망 5회
			victim.setUpgradeSlots((byte) (victim.getUpgradeSlots() + 5));
			if (victim_ != null) {
				victim_.setUpgradeSlots((byte) (victim_.getUpgradeSlots() + 5));
			}
			c.getPlayer().vh = true;
		}
		MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
		c.getSession().writeAndFlush(
				(Object) CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIP, victim));
		if (victim_ != null) {
			c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.updateInventoryItem(false,
					MapleInventoryType.EQUIPPED, victim));
			c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.updateInventoryItem(false,
					MapleInventoryType.EQUIPPED, victim_));
			c.getPlayer().getMap().broadcastMessage(CField.getScrollEffect(c.getPlayer().getId(),
					Equip.ScrollResult.SUCCESS, false, itemId, victim_.getItemId()));
		}
	}

	public static void Todd(final LittleEndianAccessor slea, final MapleClient c) {
	}

	public static void returnScrollResult(LittleEndianAccessor slea, MapleClient c) {
		slea.skip(4);
		byte type = slea.readByte();
		Equip equip = null;
		Equip zeroequip = null;

		MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();

		if (c.getPlayer().returnscroll == null) {
			c.getPlayer().dropMessage(1, "使用返回卷軸時發生錯誤.");
			return;
		}

		equip = c.getPlayer().returnscroll.getPosition() > 0
				? (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP)
						.getItem(c.getPlayer().returnscroll.getPosition())
				: (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED)
						.getItem(c.getPlayer().returnscroll.getPosition());
		if (equip == null) {
			c.getPlayer().dropMessage(1, "使用返回卷軸時發生錯誤.");
			return;
		}
		if (type == 1) {
			if (c.getPlayer().returnscroll.getPosition() > 0) {
				equip.set(c.getPlayer().returnscroll);
			} else {
				equip.set(c.getPlayer().returnscroll);
			}
		}
		equip.setFlag(equip.getFlag() - ItemFlag.RETURN_SCROLL.getValue());
		if (GameConstants.isZeroWeapon(c.getPlayer().returnscroll.getItemId())) {
			zeroequip = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11);
			zeroequip.set(c.getPlayer().returnscroll);
			zeroequip.setFlag(equip.getFlag() - ItemFlag.RETURN_SCROLL.getValue());
		}
		c.getSession().writeAndFlush(
				(Object) CWvsContext.InventoryPacket.updateInventoryItem(false, MapleInventoryType.EQUIP, equip));
		if (zeroequip != null) {
			c.getSession().writeAndFlush((Object) CWvsContext.InventoryPacket.updateInventoryItem(false,
					MapleInventoryType.EQUIP, zeroequip));
		}
		if (type == 1) {
			c.getSession()
					.writeAndFlush((Object) CField.getGameMessage(11, "리턴 주문서의 힘으로 " + ii.getName(equip.getItemId())
							+ "가 " + ii.getName(c.getPlayer().returnSc) + " 사용 이전 상태로 돌아왔습니다."));
		} else {
			c.getSession().writeAndFlush((Object) CField.getGameMessage(11, "리턴 주문서의 효과가 사라졌습니다."));
		}
		c.getSession().writeAndFlush((Object) CWvsContext.returnEffectModify(null, 0));
		c.getPlayer().returnscroll = null;
		c.getPlayer().returnSc = 0;
	}

	public static void ArcaneCatalyst(final LittleEndianAccessor slea, final MapleClient c) {
		slea.skip(4);
		final int slot = slea.readInt();
		final Equip equip = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem((short) slot).copy();
		equip.setEquipmentType(equip.getEquipmentType() | 0x4000);
		equip.setArcLevel(1);
		int totalexp = 0;
		for (int i = 1; i < equip.getArcLevel(); ++i) {
			totalexp += GameConstants.ArcaneNextUpgrade(i);
		}
		totalexp += equip.getArcEXP();
		equip.setArcEXP((int) Math.floor(totalexp * 0.8));
		if (GameConstants.isXenon(c.getPlayer().getJob())) {
			equip.setStr((short) 117);
			equip.setDex((short) 117);
			equip.setLuk((short) 117);
		} else if (GameConstants.isDemonAvenger(c.getPlayer().getJob())) {
			equip.setHp((short) 4200);
		} else if (GameConstants.isWarrior(c.getPlayer().getJob())) {
			equip.setStr((short) 300);
		} else if (GameConstants.isMagician(c.getPlayer().getJob())) {
			equip.setInt((short) 300);
		} else if (GameConstants.isArcher(c.getPlayer().getJob()) || GameConstants.isCaptain(c.getPlayer().getJob())
				|| GameConstants.isMechanic(c.getPlayer().getJob())
				|| GameConstants.isAngelicBuster(c.getPlayer().getJob())) {
			equip.setDex((short) 300);
		} else if (GameConstants.isThief(c.getPlayer().getJob())) {
			equip.setLuk((short) 300);
		} else if (GameConstants.isPirate(c.getPlayer().getJob())) {
			equip.setStr((short) 300);
		}
		c.getSession().writeAndFlush((Object) CWvsContext.ArcaneCatalyst(equip, slot));
	}

	public static void ArcaneCatalyst2(final LittleEndianAccessor slea, final MapleClient c) {
		slea.skip(4);
		final int slot = slea.readInt();
		final Equip equip = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem((short) slot);
		if ((equip.getEquipmentType() & 0x4000) == 0x0) {
			equip.setArc((short) 30);
			int totalexp = 0;
			for (int i = 1; i < equip.getArcLevel(); ++i) {
				totalexp += GameConstants.ArcaneNextUpgrade(i);
			}
			totalexp += equip.getArcEXP();
			equip.setArcEXP((int) Math.floor(totalexp * 0.8));
			equip.setArcLevel(1);
			if (GameConstants.isXenon(c.getPlayer().getJob())) {
				equip.setStr((short) 117);
				equip.setDex((short) 117);
				equip.setLuk((short) 117);
			} else if (GameConstants.isDemonAvenger(c.getPlayer().getJob())) {
				equip.setHp((short) 4200);
			} else if (GameConstants.isWarrior(c.getPlayer().getJob())) {
				equip.setStr((short) 300);
			} else if (GameConstants.isMagician(c.getPlayer().getJob())) {
				equip.setInt((short) 300);
			} else if (GameConstants.isArcher(c.getPlayer().getJob()) || GameConstants.isCaptain(c.getPlayer().getJob())
					|| GameConstants.isMechanic(c.getPlayer().getJob())
					|| GameConstants.isAngelicBuster(c.getPlayer().getJob())) {
				equip.setDex((short) 300);
			} else if (GameConstants.isThief(c.getPlayer().getJob())) {
				equip.setLuk((short) 300);
			} else if (GameConstants.isPirate(c.getPlayer().getJob())) {
				equip.setStr((short) 300);
			}
			equip.setEquipmentType(equip.getEquipmentType() | 0x4000);
		} else {
			equip.setEquipmentType(equip.getEquipmentType() - 16384);
		}
		c.getSession().writeAndFlush((Object) CWvsContext.ArcaneCatalyst2(equip));
		c.getPlayer().removeItem(2535000, -1);
		c.getPlayer().forceReAddItem(equip, MapleInventoryType.EQUIP);
	}

	public static void ArcaneCatalyst3(final LittleEndianAccessor slea, final MapleClient c) {
		final int slot = slea.readInt();
		final Equip equip = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem((short) slot).copy();
		equip.setEquipmentType(equip.getEquipmentType() - 16384);
		c.getSession().writeAndFlush((Object) CWvsContext.ArcaneCatalyst(equip, slot));
	}

	public static void ArcaneCatalyst4(final LittleEndianAccessor slea, final MapleClient c) {
		final int slot = slea.readInt();
		final Equip equip = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem((short) slot);
		equip.setEquipmentType(equip.getEquipmentType() - 16384);
		c.getSession().writeAndFlush((Object) CWvsContext.ArcaneCatalyst2(equip));
		c.getPlayer().forceReAddItem(equip, MapleInventoryType.EQUIP);
	}

	public static void ReturnSynthesizing(final LittleEndianAccessor slea, final MapleClient c) {
		slea.skip(4);
		final int scrollId = slea.readInt();
		slea.skip(4);
		final int eqpId = slea.readInt();
		final int eqpslot = slea.readInt();
		final Equip equip = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem((short) eqpslot);
		if (equip.getItemId() == eqpId) {
			equip.setMoru(0);
			c.getPlayer().forceReAddItem(equip, MapleInventoryType.EQUIP);
			final StringBuilder msg = new StringBuilder("[");
			msg.append(MapleItemInformationProvider.getInstance().getName(equip.getItemId()));
			msg.append(
					"]\uc758 \uc678\ud615\uc774 \uc6d0\ub798\ub300\ub85c \ubcf5\uad6c\ub418\uc5c8\uc2b5\ub2c8\ub2e4.");
			c.getSession().writeAndFlush((Object) CWvsContext.showPopupMessage(msg.toString()));
			c.getPlayer().gainItem(scrollId, -1);
		}
	}

	public static void blackRebirthResult(final LittleEndianAccessor slea, final MapleClient c) {
		final int result = slea.readInt();
		final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
		Equip eq = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP)
				.getItem(c.getPlayer().blackRebirthScroll.getPosition());
		if (eq == null) {
			eq = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED)
					.getItem(c.getPlayer().blackRebirthScroll.getPosition());
			if (eq == null) {
				return;
			}
		}
		Equip zeroequip = null;
		if (eq.getPosition() == -11) {
			zeroequip = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-10));
		} else if (eq.getPosition() == -10) {
			zeroequip = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) (-10));
		}
		if (result == 2) {
			eq.resetRebirth(ii.getReqLevel(eq.getItemId()));
			final int[] rebirth = new int[4];
			final String fire = String.valueOf(c.getPlayer().blackRebirth);
			final Equip ordinary = (Equip) MapleItemInformationProvider.getInstance().getEquipById(eq.getItemId(),
					false);
			Equip ordinary2 = null;
			int ordinaryPad2 = 0;
			int ordinaryMad2 = 0;
			if (zeroequip != null) {
				zeroequip.resetRebirth(ii.getReqLevel(zeroequip.getItemId()));
				ordinary2 = (Equip) MapleItemInformationProvider.getInstance().getEquipById(zeroequip.getItemId(),
						false);
				ordinaryPad2 = ((ordinary2.getWatk() > 0) ? ordinary2.getWatk() : ordinary2.getMatk());
				ordinaryMad2 = ((ordinary2.getMatk() > 0) ? ordinary2.getMatk() : ordinary2.getWatk());
			}
			final int ordinaryPad3 = (ordinary.getWatk() > 0) ? ordinary.getWatk() : ordinary.getMatk();
			final int ordinaryMad3 = (ordinary.getMatk() > 0) ? ordinary.getMatk() : ordinary.getWatk();
			if (fire.length() == 12) {
				rebirth[0] = Integer.parseInt(fire.substring(0, 3));
				rebirth[1] = Integer.parseInt(fire.substring(3, 6));
				rebirth[2] = Integer.parseInt(fire.substring(6, 9));
				rebirth[3] = Integer.parseInt(fire.substring(9));
			} else if (fire.length() == 11) {
				rebirth[0] = Integer.parseInt(fire.substring(0, 2));
				rebirth[1] = Integer.parseInt(fire.substring(2, 5));
				rebirth[2] = Integer.parseInt(fire.substring(5, 8));
				rebirth[3] = Integer.parseInt(fire.substring(8));
			} else if (fire.length() == 10) {
				rebirth[0] = Integer.parseInt(fire.substring(0, 1));
				rebirth[1] = Integer.parseInt(fire.substring(1, 4));
				rebirth[2] = Integer.parseInt(fire.substring(4, 7));
				rebirth[3] = Integer.parseInt(fire.substring(7));
			}
			eq.setFire(c.getPlayer().blackRebirth);
			if (zeroequip != null) {
				zeroequip.setFire(c.getPlayer().blackRebirth);
			}
			for (int i = 0; i < rebirth.length; ++i) {
				final int value = rebirth[i] - rebirth[i] / 10 * 10;
				eq.setFireOption(rebirth[i] / 10, ii.getReqLevel(eq.getItemId()), value, ordinaryPad3, ordinaryMad3);
				if (zeroequip != null && ordinaryPad2 != 0) {
					zeroequip.setFireOption(rebirth[i] / 10, ii.getReqLevel(zeroequip.getItemId()), value, ordinaryPad2,
							ordinaryMad2);
				}
			}
			c.getPlayer().forceReAddItem(eq, MapleInventoryType.EQUIP);
			if (zeroequip != null) {
				c.getPlayer().forceReAddItem(zeroequip, MapleInventoryType.EQUIP);
			}
		} else if (result == 3) {
			final Item scroll = c.getPlayer().getInventory(MapleInventoryType.USE)
					.getItem(c.getPlayer().blackRebirthPos);
			if (scroll != null) {
				final Equip neweqs = (Equip) eq.copy();
				neweqs.resetRebirth(ii.getReqLevel(neweqs.getItemId()));
				neweqs.setFire(neweqs.newRebirth(ii.getReqLevel(neweqs.getItemId()), scroll.getItemId(), true));
				final long newRebirth = neweqs.getFire();
				c.getSession().writeAndFlush((Object) CWvsContext.useBlackRebirthScroll(eq, scroll, newRebirth, false));
				MapleInventoryManipulator.removeFromSlot(c, GameConstants.getInventoryType(scroll.getItemId()),
						scroll.getPosition(), (short) 1, false, false);
				c.getSession().writeAndFlush((Object) CWvsContext.blackRebirthResult(true, eq.getFire(), eq));
				c.getSession().writeAndFlush((Object) CWvsContext.blackRebirthResult(false, newRebirth, neweqs));
				c.getPlayer().blackRebirth = newRebirth;
				c.getPlayer().blackRebirthScroll = (Equip) eq.copy();
			}
		}
		if (result == 1 || result == 2) {
			c.getSession().writeAndFlush((Object) CWvsContext.useBlackRebirthScroll(eq, null, 0L, true));
		}
		c.getSession().writeAndFlush((Object) CWvsContext.enableActions(c.getPlayer()));
	}

	public static void UseCirculator(LittleEndianAccessor slea, MapleClient c) {
		// 00 B7 3A 29 00 16 00 00 00 // 블랙
		// 00 B3 3A 29 00 19 00 00 00 // 카오스
		int preset = slea.readByte();
		int itemId = slea.readInt();
		int slot = slea.readInt();
		Item item = c.getPlayer().getInventory(MapleInventoryType.USE).getItem((short) slot);
		if (item.getItemId() == itemId) {

			List<InnerSkillValueHolder> newValues = new LinkedList<InnerSkillValueHolder>();
			InnerSkillValueHolder ivholder = null;
			InnerSkillValueHolder ivholder2 = null;
			int nowrank = -1;
			switch (itemId) {
			case 2702003: // 카오스 서큘레이터
			case 2702004: { // 카오스 서큘레이터
				if (c.getPlayer().getInnerSkills().get(preset).size() > 0) {
					nowrank = c.getPlayer().getInnerSkills().get(preset).get(0).getRank();
				}

				int count = 0;
				for (InnerSkillValueHolder isvh : c.getPlayer().getInnerSkills().get(preset)) {
					newValues.add(InnerAbillity.getInstance().renewLevel((count == 0 ? nowrank : (nowrank - 1)),
							isvh.getSkillId(), preset));

					c.getPlayer().changeSkillLevel_Inner(SkillFactory.getSkill(isvh.getSkillId()), (byte) 0, (byte) 0);

					count++;
				}
				break;
			}
			case 2702006: { // 레전드리 서큘레이터
				nowrank = 3;
				for (InnerSkillValueHolder isvh : c.getPlayer().getInnerSkills().get(preset)) {
					if (ivholder == null) {
						ivholder = InnerAbillity.getInstance().renewSkill(nowrank, false, preset);
						while (isvh.getSkillId() == ivholder.getSkillId()) {
							ivholder = InnerAbillity.getInstance().renewSkill(nowrank, false, preset);
						}
						newValues.add(ivholder);
					} else if (ivholder2 == null) {
						ivholder2 = InnerAbillity.getInstance()
								.renewSkill(ivholder.getRank() == 0 ? 0 : ivholder.getRank(), false, preset);
						while (isvh.getSkillId() == ivholder2.getSkillId()
								|| ivholder.getSkillId() == ivholder2.getSkillId()) {
							ivholder2 = InnerAbillity.getInstance()
									.renewSkill(ivholder.getRank() == 0 ? 0 : ivholder.getRank(), false, preset);
						}
						newValues.add(ivholder2);
					} else {
						InnerSkillValueHolder ivholder3 = InnerAbillity.getInstance()
								.renewSkill(ivholder.getRank() == 0 ? 0 : ivholder.getRank(), false, preset);
						while (isvh.getSkillId() == ivholder3.getSkillId()
								|| ivholder.getSkillId() == ivholder3.getSkillId()
								|| ivholder2.getSkillId() == ivholder3.getSkillId()) {
							ivholder3 = InnerAbillity.getInstance()
									.renewSkill(ivholder.getRank() == 0 ? 0 : ivholder.getRank(), false, preset);
						}
						newValues.add(ivholder3);
					}
					c.getPlayer().changeSkillLevel_Inner(SkillFactory.getSkill(isvh.getSkillId()), (byte) 0, (byte) 0);
				}
				break;
			}
			case 2702007: // 블랙 서큘레이터
			case 2702008: { // 블랙 서큘레이터
				newValues = new LinkedList<>();

				if (c.getPlayer().getInnerSkills().get(preset).size() > 0) {
					nowrank = c.getPlayer().getInnerSkills().get(preset).get(0).getRank();
				}

				int count = 0;
				for (InnerSkillValueHolder isvh : c.getPlayer().getInnerSkills().get(preset)) {
					newValues.add(InnerAbillity.getInstance().renewLevel((count == 0 ? nowrank : (nowrank - 1)),
							isvh.getSkillId(), preset));

					count++;
				}

				c.getPlayer().innerCirculator = newValues;

				c.getSession().writeAndFlush(CWvsContext.MiracleCirculator(newValues, itemId, preset));
				break;
			}
			}
			if (newValues.size() == 3) {
				if (itemId != 2702007 && itemId != 2702008) {
					c.getPlayer().getInnerSkills().get(preset).clear();

					for (InnerSkillValueHolder isvh : newValues) {
						c.getPlayer().getInnerSkills().get(preset).add(isvh);
						c.getPlayer().changeSkillLevel_Inner(SkillFactory.getSkill(isvh.getSkillId()),
								isvh.getSkillLevel(), isvh.getSkillLevel());
					}

					c.getPlayer().getClient().getSession().writeAndFlush(
							CField.updateInnerAbility(c.getPlayer().getInnerSkills().get(preset), preset));
					c.getPlayer().getStat().recalcLocalStats(c.getPlayer());
				}

				MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, (short) slot, (short) 1, false);
			}
		}
	}

	public static final void MixHair(LittleEndianAccessor slea, MapleClient c) {
		short slot = slea.readShort();
		int itemId = slea.readInt();
		slea.skip(2);
		int mixColor = slea.readInt();
		Item toUse = c.getPlayer().getInventory(MapleInventoryType.USE).getItem(slot);
		int basehair = 0;
		int newhair = 0;
		if (c.getPlayer().getDressup()
				|| (GameConstants.isZero(c.getPlayer().getJob()) && c.getPlayer().getGender() == 1)) {
			basehair = c.getPlayer().getSecondHair() < 100000 ? c.getPlayer().getSecondHair()
					: c.getPlayer().getSecondHair() / 1000;
			newhair = (basehair - basehair % 10 + mixColor / 10000) * 1000 + mixColor % 10000 / 1000 * 100
					+ mixColor % 100;
			c.getPlayer().setSecondHair(newhair);
			if (c.getPlayer().getDressup()) {
				c.getPlayer().updateAngelicStats();
			} else {
				c.send(CWvsContext.updateZeroSecondStats(c.getPlayer()));
			}
		} else {
			basehair = c.getPlayer().getHair() < 100000 ? c.getPlayer().getHair() : c.getPlayer().getHair() / 1000;
			newhair = (basehair - basehair % 10 + mixColor / 10000) * 1000 + mixColor % 10000 / 1000 * 100
					+ mixColor % 100;
			c.getPlayer().setHair(newhair);
			c.getPlayer().updateSingleStat(MapleStat.HAIR, newhair);
		}
		c.getPlayer().equipChanged();
		MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false, true);
		c.getSession()
				.writeAndFlush(CWvsContext.mixLense(itemId, basehair, newhair, false, false, false, c.getPlayer()));
		c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
	}
}
