package handling.auction.handler;

import client.MapleCharacter;
import client.MapleClient;
import client.inventory.*;
import constants.GameConstants;
import constants.ServerConstants;
import handling.auction.AuctionServer;
import handling.channel.ChannelServer;
import handling.login.LoginServer;
import handling.world.CharacterTransfer;
import handling.world.MapleMessengerCharacter;
import handling.world.PlayerBuffStorage;
import handling.world.World;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.games.BattleReverse;
import server.games.OneCardGame;
import tools.FileoutputUtil;
import tools.StringUtil;
import tools.data.LittleEndianAccessor;
import tools.packet.CField;
import tools.packet.CWvsContext;
import tools.packet.PacketHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AuctionHandler {
	public static void LeaveAuction(MapleClient c, MapleCharacter chr) {
		AuctionServer.getPlayerStorage().deregisterPlayer(chr);
		c.updateLoginState(1, c.getSessionIPAddress());

		try {
			PlayerBuffStorage.addBuffsToStorage(chr.getId(), chr.getAllBuffs());
			PlayerBuffStorage.addCooldownsToStorage(chr.getId(), chr.getCooldowns());
			World.ChannelChange_Data(new CharacterTransfer(chr), chr.getId(), c.getChannel());
			c.getSession().writeAndFlush(CField.getChannelChange(c,
					Integer.parseInt(ChannelServer.getInstance(c.getChannel()).getIP().split(":")[1])));
		} finally {
			String s = c.getSessionIPAddress();
			LoginServer.addIPAuth(s.substring(s.indexOf('/') + 1, s.length()));
			chr.saveToDB(true, false);
			c.setPlayer(null);

			c.setAuction(false);
		}
	}

	public static void EnterAuction(MapleCharacter chr, MapleClient client) {
		chr.changeRemoval();
		ChannelServer ch = ChannelServer.getInstance(client.getChannel());
		if (chr.getMessenger() != null) {
			World.Messenger.silentLeaveMessenger(chr.getMessenger().getId(), new MapleMessengerCharacter(chr));
		}
		PlayerBuffStorage.addBuffsToStorage(chr.getId(), chr.getAllBuffs());
		PlayerBuffStorage.addCooldownsToStorage(chr.getId(), chr.getCooldowns());
		World.ChannelChange_Data(new CharacterTransfer(chr), chr.getId(), -20);
		ch.removePlayer(chr);

		client.setAuction(true);
		client.updateLoginState(3, client.getSessionIPAddress());
		String s = client.getSessionIPAddress();
		LoginServer.addIPAuth(s.substring(s.indexOf('/') + 1, s.length()));
		client.getSession().writeAndFlush(CField.enterAuction(chr));

		chr.saveToDB(true, false);
		chr.getMap().removePlayer(chr);

		/*
		 * if (chr.getClient().getLIEDETECT() != null) {
		 * PlayerHandler.EnterTrueRoom(chr.getClient()); }
		 */
		if (OneCardGame.oneCardMatchingQueue.contains(chr)) {
			OneCardGame.oneCardMatchingQueue.remove(chr);
		}

		if (BattleReverse.BattleReverseMatchingQueue.contains(chr))
			BattleReverse.BattleReverseMatchingQueue.remove(chr);
	}

	public static final void Handle(LittleEndianAccessor slea, MapleClient c) {
		CharacterTransfer transfer;
		int nAuctionType;
		long l1;
		int dwAuctionID;
		long dwInventoryId;
		List<AuctionItem> searchItems;
		int j;
		List<Integer> wishlist;
		int dwAuctionId;
		List<AuctionItem> sellingItems, completeItems;
		MapleCharacter chr;
		int nItemID;
		AuctionItem aItem;
		long nPrice;
		int searchType;
		AuctionItem auctionItem1;
		List<AuctionItem> wishItems;
		AuctionItem item;
		List<AuctionItem> list1;
		int nNumber, k;
		String nameWithSpace;
		int i;
		Iterator<Integer> iterator;
		List<AuctionItem> list2;
		long l2;
		int m, nCount, dwAccountId;
		String nameWithoutSpace;
		List<AuctionItem> marketPriceItems;
		int i1, n;
		AuctionItem auctionItem2;
		int dwCharacterId;
		List<AuctionItem> recentlySellItems;
		int nEndHour, nItemId;
		List<Integer> list3;
		byte nTI;
		int nState;
		List<AuctionItem> list4;
		int nItemPos;
		long l3;
		int i2;
		Item source, target;
		long nBuyTime;
		AuctionItem auctionItem3;
		int deposit, i3, nWorldId;
		AuctionItem auctionItem5, auctionItem4;
		int op = slea.readInt();
		Map<Integer, AuctionItem> items = AuctionServer.getItems();
		MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
		switch (op) {
		case 0:
			transfer = AuctionServer.getPlayerStorage().getPendingCharacter(c.getPlayer().getId());
			chr = MapleCharacter.ReconstructChr(transfer, c, false);

			c.setPlayer(chr);
			c.setAccID(chr.getAccountID());

			if (!c.CheckIPAddress()) {
				c.getSession().close();
				return;
			}
			chr.giveCoolDowns(PlayerBuffStorage.getCooldownsFromStorage(chr.getId()));
			chr.silentGiveBuffs(PlayerBuffStorage.getBuffsFromStorage(chr.getId()));

			World.isCharacterListConnected(c.getPlayer().getName(), c.loadCharacterNames(c.getWorld()));
			c.updateLoginState(2, c.getSessionIPAddress());
			AuctionServer.getPlayerStorage().registerPlayer(chr);

			list1 = new ArrayList<>();
			list2 = new ArrayList<>();
			marketPriceItems = new ArrayList<>();
			recentlySellItems = new ArrayList<>();
			list3 = new ArrayList<>();
			list4 = new ArrayList<>();

			for (i2 = 0; i2 < 10; i2++) {
				String wish = c.getKeyValue("wish" + i2);
				if (wish != null) {
					list3.add(Integer.valueOf(Integer.parseInt(wish)));
				}
			}
			for (Map.Entry<Integer, AuctionItem> itemz : items.entrySet()) {
				AuctionItem auctionItem = itemz.getValue();
				if ((auctionItem.getEndDate() < System.currentTimeMillis() || auctionItem.getState() >= 2)
						&& ((auctionItem.getState() == 2 && auctionItem.getBidUserId() == c.getPlayer().getId())
								|| ((auctionItem.getState() == 3 || auctionItem.getState() == 4)
										&& auctionItem.getAccountId() == c.getAccID()))) {
					list1.add(auctionItem);
				}
				if (auctionItem.getAccountId() == c.getAccID() && auctionItem.getState() == 0) {
					list2.add(auctionItem);
				}
				if (auctionItem.getState() == 0 && recentlySellItems.size() < 1000) {
					recentlySellItems.add(auctionItem);
				}
				if ((auctionItem.getState() == 3 || auctionItem.getState() == 8) && marketPriceItems.size() < 1000) {
					marketPriceItems.add(auctionItem);
				}
				for (Iterator<Integer> iterator1 = list3.iterator(); iterator1.hasNext();) {
					int auctionId = ((Integer) iterator1.next()).intValue();
					if (auctionItem.getAuctionId() == auctionId) {
						list4.add(auctionItem);
					}
				}

			}
			c.getSession().writeAndFlush(CField.AuctionPacket.AuctionCompleteItems(list1));
			c.getSession().writeAndFlush(CField.AuctionPacket.AuctionSellingMyItems(list2));
			c.getSession().writeAndFlush(CField.AuctionPacket.AuctionWishlist(list4)); // ok
			c.getSession().writeAndFlush(CField.AuctionPacket.AuctionOn()); // ok
			c.getSession().writeAndFlush(CField.AuctionPacket.AuctionMarketPrice(marketPriceItems)); // ok
			c.getSession().writeAndFlush(CField.AuctionPacket.AuctionSearchItems(recentlySellItems)); // ok
			FileoutputUtil.log(FileoutputUtil. 拍賣場入場日誌, 
			        "[入場] 帳號編號 : " + c.getAccID()  + " | 角色編號 : "
			        + c.getPlayer().getId()  + " | 角色暱稱 : " + c.getPlayer().getName());  
			break;
		case 10:
			nAuctionType = slea.readInt();
			nItemID = slea.readInt();
			nNumber = slea.readInt();
			l2 = slea.readLong();

			nEndHour = slea.readInt();
			nTI = slea.readByte();
			nItemPos = slea.readInt();

			source = c.getPlayer().getInventory(nTI).getItem((short) nItemPos);

			if (source == null || source.getItemId() != nItemID || source.getQuantity() < nNumber || nNumber < 0
					|| l2 < 0L) {
				System.out.println(c.getPlayer().getName() + " 캐릭터가 경매장에 비정상적인 패킷을 유도함.");
				c.getSession().close();
				return;
			}
			if (nNumber <= 0) {
				System.out.println("quantity 0이하");
				c.getSession().close();
				return;
			}
			if (source.getInventoryId() <= 0L) {
				System.out.println("inventoryId : " + source.getInventoryId());
				return;
			}
			target = source.copy();

			MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.getByType(nTI), (short) nItemPos,
					(short) nNumber, false);

			target.setQuantity((short) nNumber);

			if (target.getInventoryId() <= 0L) {
				System.out.println("inventoryId : " + target.getInventoryId());

				return;
			}
			auctionItem3 = new AuctionItem();
			auctionItem3.setAuctionType(nAuctionType);
			auctionItem3.setItem(target);
			if (GameConstants.getInventoryType(auctionItem3.getItem().getItemId()) == MapleInventoryType.CASH
					&& nNumber > 1) {
				auctionItem3.setPrice(l2);
				auctionItem3.setDirectPrice(l2);
			} else {
				auctionItem3.setPrice(l2);
				auctionItem3.setDirectPrice(l2 * nNumber);
			}
			auctionItem3.setEndDate(System.currentTimeMillis() + (nEndHour * 60 * 60 * 1000));
			auctionItem3.setRegisterDate(System.currentTimeMillis());
			auctionItem3.setAccountId(c.getAccID());
			auctionItem3.setCharacterId(c.getPlayer().getId());
			auctionItem3.setState(0);
			auctionItem3.setWorldId(c.getWorld());
			auctionItem3.setName(c.getPlayer().getName());
			auctionItem3.setAuctionId(AuctionItemIdentifier.getInstance());
			items.put(Integer.valueOf(auctionItem3.getAuctionId()), auctionItem3);
			c.getSession().writeAndFlush(CField.AuctionPacket.AuctionSellItemUpdate(auctionItem3));
			c.getSession().writeAndFlush(CField.AuctionPacket.AuctionSellItem(auctionItem3));
			FileoutputUtil.log(FileoutputUtil.拍賣場銷售登記日誌,
					"[아이템등록] 계정 번호 : " + c.getAccID() + " | 캐릭 번호 : " + c.getPlayer().getId() + " | 캐릭터 닉네임 : "
							+ c.getPlayer().getName() + " | 아이템 이름 : " + ii.getName(auctionItem3.getItem().getItemId())
							+ " | 아이템 코드 : " + auctionItem3.getItem().getItemId() + " | 수량 "
							+ auctionItem3.getItem().getQuantity() + " | 판매 메소 : " + l2);
			break;
		case 11:
			l1 = slea.readLong();
			k = slea.readInt();
			m = slea.readInt();
			i1 = slea.readInt();
			nItemId = slea.readInt();
			nState = slea.readInt();
			l3 = slea.readLong();
			nBuyTime = slea.readLong();
			deposit = slea.readInt();
			deposit = slea.readInt();
			i3 = slea.readInt();
			nWorldId = slea.readInt();

			if (i3 < 0 || i3 > 32767 || l3 < 0L) {
				System.out.println(c.getPlayer().getName() + " 캐릭터가 경매장에 비정상적인 패킷을 유도함.");
				c.getSession().close();
				return;
			}
			auctionItem5 = items.get(Integer.valueOf(k));
			if (auctionItem5 != null && auctionItem5.getItem() != null && auctionItem5.getHistory() != null) {
				AuctionHistory history = auctionItem5.getHistory();
				if (history.getId() != l1) {
					System.out.println("return 1");
					return;
				}
				if (history.getAuctionId() != k) {
					System.out.println("return 2");
					return;
				}
				if (history.getAccountId() != m) {
					System.out.println("return 3");
					return;
				}
				if (history.getCharacterId() != i1) {
					System.out.println("return 4");
					return;
				}
				if (history.getItemId() != nItemId) {
					System.out.println("return 5");
					return;
				}
				if (history.getState() != nState) {
					System.out.println("return 6");
					return;
				}
				if (history.getPrice() != l3) {
					System.out.println("return 7");
					return;
				}
				if (PacketHelper.getTime(history.getBuyTime()) != nBuyTime) {
					System.out.println("return 8");
					return;
				}
				if (history.getDeposit() != deposit) {
					System.out.println("return 9");
					return;
				}
				if (history.getQuantity() != i3) {
					System.out.println("return 10");
					return;
				}
				if (history.getWorldId() != nWorldId) {
					System.out.println("return 11");
					return;
				}
				auctionItem5.setEndDate(System.currentTimeMillis() + 86400000L);
				auctionItem5.setRegisterDate(System.currentTimeMillis());
				auctionItem5.setState(9);
				history.setState(9);
				Item item1 = auctionItem5.getItem();
				c.getSession().writeAndFlush(CField.AuctionPacket.AuctionCompleteItemUpdate(auctionItem5));
				AuctionItem aItem2 = new AuctionItem();
				aItem2.setAuctionType(auctionItem5.getAuctionType());
				aItem2.setItem(item1);
				aItem2.setPrice(auctionItem5.getDirectPrice());
				aItem2.setSecondPrice(0L);
				aItem2.setDirectPrice(auctionItem5.getDirectPrice());
				aItem2.setEndDate(System.currentTimeMillis() + 86400000L);
				aItem2.setRegisterDate(System.currentTimeMillis());
				aItem2.setAccountId(c.getAccID());
				aItem2.setCharacterId(c.getPlayer().getId());
				aItem2.setState(0);
				aItem2.setWorldId(c.getWorld());
				aItem2.setName(c.getPlayer().getName());
				aItem2.setAuctionId(AuctionItemIdentifier.getInstance());
				items.put(Integer.valueOf(aItem2.getAuctionId()), aItem2);
				c.getSession().writeAndFlush(CField.AuctionPacket.AuctionSellItemUpdate(aItem2));
				c.getSession().writeAndFlush(CField.AuctionPacket.AuctionReSellItem(aItem2));
				FileoutputUtil.log(FileoutputUtil.拍賣場銷售登記日誌,
						"[아이템재등록] 계정 번호 : " + c.getAccID() + " | 캐릭 번호 : " + c.getPlayer().getId() + " | 캐릭터 닉네임 : "
								+ c.getPlayer().getName() + " | 아이템 이름 : "
								+ ii.getName(auctionItem5.getItem().getItemId()) + " | 아이템 코드 : "
								+ auctionItem5.getItem().getItemId() + " | 수량 " + auctionItem5.getItem().getQuantity()
								+ " | 판매 메소 : " + auctionItem5.getDirectPrice());
			}
			break;
		case 12:
			dwAuctionID = slea.readInt();
			aItem = items.get(Integer.valueOf(dwAuctionID));
			if (aItem != null && aItem.getItem() != null) {
				if (aItem.getState() != 0) {
					return;
				}
				aItem.setState(4);
				aItem.setPrice(0L);
				aItem.setSecondPrice(-1L);
				AuctionHistory history = new AuctionHistory();
				history.setAuctionId(aItem.getAuctionId());
				history.setAccountId(aItem.getAccountId());
				history.setCharacterId(aItem.getCharacterId());
				history.setItemId(aItem.getItem().getItemId());
				history.setState(aItem.getState());
				history.setPrice(aItem.getPrice());
				history.setBuyTime(System.currentTimeMillis());
				history.setDeposit(aItem.getDeposit());
				history.setQuantity(aItem.getItem().getQuantity());
				history.setWorldId(aItem.getWorldId());
				history.setId(AuctionHistoryIdentifier.getInstance());
				aItem.setHistory(history);

				c.getSession().writeAndFlush(CField.AuctionPacket.AuctionSellItemUpdate(aItem));
				c.getSession().writeAndFlush(CField.AuctionPacket.AuctionCompleteItemUpdate(aItem));
				c.getSession().writeAndFlush(CField.AuctionPacket.AuctionStopSell(aItem));
				FileoutputUtil.log(FileoutputUtil.拍賣場銷售中止日誌,
						"[판매중지] 계정 번호 : " + c.getAccID() + " | 캐릭 번호 : " + c.getPlayer().getId() + " | 캐릭터 닉네임 : "
								+ c.getPlayer().getName() + " | 아이템 이름 : " + ii.getName(aItem.getItem().getItemId())
								+ " | 아이템 코드 : " + aItem.getItem().getItemId() + " | 갯수 : "
								+ aItem.getItem().getQuantity());
			}
			break;
		case 20:
		case 21:
			dwAuctionID = slea.readInt();
			nPrice = slea.readLong();
			nCount = 1;
			if (op == 21) {
				nCount = slea.readInt();
			}

			for (n = 0; n < 10; n++) {
				String wish = c.getKeyValue("wish" + n);
				if (wish != null && wish.equals(String.valueOf(dwAuctionID))) {
					c.removeKeyValue("wish" + n);

					break;
				}
			}
			c.getSession().writeAndFlush(CField.AuctionPacket.AuctionWishlistUpdate(dwAuctionID));

			if (nPrice < 0L || nCount < 0 || nCount > 32767) {
				System.out.println(c.getPlayer().getName() + " 캐릭터가 경매장에 비정상적인 패킷을 유도함.");
				c.getSession().close();
				return;
			}
			if (c.getPlayer().getMeso() < nPrice) {
				if (op == 20) {
					c.getSession().writeAndFlush(CField.AuctionPacket.AuctionBuyEquipResult(106, dwAuctionID));
					break;
				}
				c.getSession().writeAndFlush(CField.AuctionPacket.AuctionBuyItemResult(106, dwAuctionID));
				break;
			}
			auctionItem2 = items.get(Integer.valueOf(dwAuctionID));
			if (op == 20 && auctionItem2.getItem() != null) {
				nCount = auctionItem2.getItem().getQuantity();
			}
			if (auctionItem2 != null && auctionItem2.getItem() != null
					&& auctionItem2.getItem().getQuantity() >= nCount) {
				if (auctionItem2.getAccountId() == c.getAccID()) {
					c.getSession().writeAndFlush(CWvsContext.serverNotice(1, "", "[通知]無法購買自己上傳的物品."));
					return;
				}
				if (auctionItem2.getCharacterId() == c.getPlayer().getId() || auctionItem2.getState() != 0) {
					return;
				}
				if (auctionItem2.getPrice() * nCount != nPrice) {
					c.getSession().writeAndFlush(CWvsContext.serverNotice(1, "", "[通知] 注册價格與購買價格不匹配."));
					return;
				}
				c.getPlayer().gainMeso(-nPrice, false);
				Item item1 = auctionItem2.getItem();
				Item item2 = item1.copy();
				item1.setQuantity((short) (item1.getQuantity() - nCount));
				item2.setQuantity((short) nCount);
				if (item1.getQuantity() <= 0) {
					item1.setQuantity((short) nCount);
					auctionItem2.setState(3);
					auctionItem2.setBidUserId(c.getPlayer().getId());
					auctionItem2.setBidUserName(c.getPlayer().getName());
					auctionItem2.setPrice(nPrice);
					AuctionHistory auctionHistory = new AuctionHistory();
					auctionHistory.setAuctionId(auctionItem2.getAuctionId());
					auctionHistory.setAccountId(auctionItem2.getAccountId());
					auctionHistory.setCharacterId(auctionItem2.getCharacterId());
					auctionHistory.setItemId(auctionItem2.getItem().getItemId());
					auctionHistory.setState(auctionItem2.getState());
					auctionHistory.setPrice(auctionItem2.getPrice());
					auctionHistory.setBuyTime(System.currentTimeMillis());
					auctionHistory.setDeposit(auctionItem2.getDeposit());
					auctionHistory.setQuantity(auctionItem2.getItem().getQuantity());
					auctionHistory.setWorldId(auctionItem2.getWorldId());
					auctionHistory.setId(AuctionHistoryIdentifier.getInstance());
					auctionHistory.setBidUserId(c.getPlayer().getId());
					auctionHistory.setBidUserName(c.getPlayer().getName());
					auctionItem2.setHistory(auctionHistory);
					c.getSession().writeAndFlush(CField.AuctionPacket.AuctionBuyItemUpdate(auctionItem2, false));
					FileoutputUtil.log(FileoutputUtil.拍賣場購買日誌, "[구입] 계정 번호 : " + c.getAccID() + " | 캐릭 번호 : "
							+ c.getPlayer().getId() + " | 캐릭터 닉네임 : " + c.getPlayer().getName() + " | 아이템 이름 : "
							+ ii.getName(auctionItem2.getItem().getItemId()) + " | 아이템 코드 : "
							+ auctionItem2.getItem().getItemId() + " | 수량 " + auctionItem2.getItem().getQuantity()
							+ " | 구매 메소 : " + nPrice + " | 해당 아이템 판매자 이름 : " + auctionItem2.getName());
				} else {
					c.getSession().writeAndFlush(CField.AuctionPacket.AuctionBuyItemUpdate(auctionItem2, true));
					AuctionItem auctionItem6 = new AuctionItem();
					auctionItem6.setAuctionType(auctionItem2.getAuctionType());
					auctionItem6.setItem(item2);
					auctionItem6.setPrice(nPrice);
					auctionItem6.setDirectPrice(nPrice);
					auctionItem6.setEndDate(auctionItem2.getEndDate());
					auctionItem6.setRegisterDate(auctionItem2.getRegisterDate());
					auctionItem6.setAccountId(auctionItem2.getAccountId());
					auctionItem6.setCharacterId(auctionItem2.getCharacterId());
					auctionItem6.setState(3);
					auctionItem6.setWorldId(auctionItem2.getWorldId());
					auctionItem6.setName(auctionItem2.getName());
					auctionItem6.setBidUserId(c.getPlayer().getId());
					auctionItem6.setBidUserName(c.getPlayer().getName());
					auctionItem6.setAuctionId(AuctionItemIdentifier.getInstance());
					AuctionHistory auctionHistory = new AuctionHistory();
					auctionHistory.setAuctionId(auctionItem6.getAuctionId());
					auctionHistory.setAccountId(auctionItem6.getAccountId());
					auctionHistory.setCharacterId(auctionItem6.getCharacterId());
					auctionHistory.setItemId(auctionItem6.getItem().getItemId());
					auctionHistory.setState(auctionItem6.getState());
					auctionHistory.setPrice(auctionItem6.getPrice());
					auctionHistory.setBuyTime(System.currentTimeMillis());
					auctionHistory.setDeposit(auctionItem6.getDeposit());
					auctionHistory.setQuantity(auctionItem6.getItem().getQuantity());
					auctionHistory.setWorldId(auctionItem6.getWorldId());
					auctionHistory.setId(AuctionHistoryIdentifier.getInstance());
					auctionHistory.setBidUserId(c.getPlayer().getId());
					auctionHistory.setBidUserName(c.getPlayer().getName());
					auctionItem6.setHistory(auctionHistory);
					items.put(Integer.valueOf(auctionItem6.getAuctionId()), auctionItem6);
					FileoutputUtil.log(FileoutputUtil.拍賣場購買日誌, "[구입] 계정 번호 : " + c.getAccID() + " | 캐릭 번호 : "
							+ c.getPlayer().getId() + " | 캐릭터 닉네임 : " + c.getPlayer().getName() + " | 아이템 이름 : "
							+ ii.getName(auctionItem2.getItem().getItemId()) + " | 아이템 코드 : "
							+ auctionItem2.getItem().getItemId() + " | 수량 " + auctionItem2.getItem().getQuantity()
							+ " | 구매 메소 : " + nPrice + " | 해당 아이템 판매자 이름 : " + auctionItem2.getName());
				}

				AuctionItem auctionItem = new AuctionItem();
				auctionItem.setAuctionType(auctionItem2.getAuctionType());
				auctionItem.setItem(item2);
				auctionItem.setPrice(nPrice);
				auctionItem.setDirectPrice(auctionItem2.getDirectPrice());
				auctionItem.setEndDate(auctionItem2.getEndDate());
				auctionItem.setRegisterDate(auctionItem2.getRegisterDate());
				auctionItem.setAccountId(auctionItem2.getAccountId());
				auctionItem.setCharacterId(auctionItem2.getCharacterId());
				auctionItem.setState(2);
				auctionItem.setWorldId(auctionItem2.getWorldId());
				auctionItem.setName(auctionItem2.getName());
				auctionItem.setBidUserId(c.getPlayer().getId());
				auctionItem.setBidUserName(c.getPlayer().getName());

				auctionItem.setAuctionId(AuctionItemIdentifier.getInstance());

				AuctionHistory history = new AuctionHistory();

				history.setAuctionId(auctionItem.getAuctionId());
				history.setAccountId(auctionItem.getAccountId());
				history.setCharacterId(auctionItem.getCharacterId());
				history.setItemId(auctionItem.getItem().getItemId());
				history.setState(auctionItem.getState());
				history.setPrice(auctionItem.getPrice());
				history.setBuyTime(System.currentTimeMillis());
				history.setDeposit(auctionItem.getDeposit());
				history.setQuantity(auctionItem.getItem().getQuantity());
				history.setWorldId(auctionItem.getWorldId());
				history.setId(AuctionHistoryIdentifier.getInstance());
				history.setBidUserId(c.getPlayer().getId());
				history.setBidUserName(c.getPlayer().getName());
				auctionItem.setHistory(history);

				items.put(Integer.valueOf(auctionItem.getAuctionId()), auctionItem);

				c.getSession().writeAndFlush(CField.AuctionPacket.AuctionCompleteItemUpdate(auctionItem, item2));
				c.getSession().writeAndFlush(CField.AuctionPacket.AuctionBuyItemResult(0, dwAuctionID));

				int ch = World.Find.findAccChannel(auctionItem2.getAccountId());
				if (ch >= 0) {
					MapleClient ac = AuctionServer.getPlayerStorage().getClientById(auctionItem2.getAccountId());
					if (ac == null) {
						ac = ChannelServer.getInstance(ch).getPlayerStorage()
								.getClientById(auctionItem2.getAccountId());
					}
					if (ac != null) {
						ac.getSession().writeAndFlush(CWvsContext.AlarmAuction(0, auctionItem));
					}
				}
			}
			break;
		case 30:
			dwInventoryId = slea.readLong();
			k = slea.readInt();
			dwAccountId = slea.readInt();
			dwCharacterId = slea.readInt();
			nItemId = slea.readInt();
			nState = slea.readInt();
			l3 = slea.readLong();
			nBuyTime = slea.readLong();
			deposit = slea.readInt();
			deposit = slea.readInt();
			i3 = slea.readInt();
			nWorldId = slea.readInt();

			if (i3 < 0 || i3 > 32767 || l3 < 0L) {
				System.out.println(c.getPlayer().getName() + " 캐릭터가 경매장에 비정상적인 패킷을 유도함.");
				c.getSession().close();
				return;
			}
			auctionItem4 = items.get(Integer.valueOf(k));

			if (auctionItem4 != null && auctionItem4.getItem() != null && auctionItem4.getHistory() != null) {
				AuctionHistory history = auctionItem4.getHistory();

				if (history.getId() != dwInventoryId) {
					System.out.println("return 1");
					return;
				}
				if (history.getAuctionId() != k) {
					System.out.println("return 2");
					return;
				}
				if (history.getAccountId() != dwAccountId) {
					System.out.println("return 3");
					return;
				}
				if (history.getCharacterId() != dwCharacterId) {
					System.out.println("return 4");
					return;
				}
				if (history.getItemId() != nItemId) {
					System.out.println("return 5");
					return;
				}
				if (history.getState() != nState) {
					System.out.println("return 6");
					return;
				}
				if (history.getPrice() != l3) {
					System.out.println("return 7");
					return;
				}
				if (PacketHelper.getTime(history.getBuyTime()) != nBuyTime) {
					System.out.println("return 8");
					return;
				}
				if (history.getDeposit() != deposit) {
					System.out.println("return 9");
					return;
				}
				if (history.getQuantity() != i3) {
					System.out.println("return 10");
					return;
				}
				if (history.getWorldId() != nWorldId) {
					System.out.println("return 11");
					return;
				}
				history.setState(8);
				auctionItem4.setState(8);
				c.getPlayer().gainMeso((long) (l3 * 0.95D), false);
				c.getSession().writeAndFlush(CField.AuctionPacket.AuctionCompleteItemUpdate(auctionItem4));
				c.getSession().writeAndFlush(CField.AuctionPacket.AuctionCompleteMesoResult());
				FileoutputUtil.log(FileoutputUtil.拍賣場款項領取日誌,
						"[대금수령] 계정 번호 : " + c.getAccID() + " | 캐릭 번호 : " + c.getPlayer().getId() + " | 캐릭터 닉네임 : "
								+ c.getPlayer().getName() + " | 아이템 이름 : "
								+ ii.getName(auctionItem4.getItem().getItemId()) + " | 아이템 코드 : "
								+ auctionItem4.getItem().getItemId() + " | 수량 " + auctionItem4.getItem().getQuantity()
								+ " | 받은 메소 : " + (long) (l3 * 0.95D) + " | 해당 아이템 판매자 이름 : " + auctionItem4.getName());
			}
			break;
		case 31: { // 물품 반환
			// 리시브를 그대로 보냄
			dwInventoryId = slea.readLong();
			dwAuctionId = slea.readInt();
			dwAccountId = slea.readInt();
			dwCharacterId = slea.readInt();
			nItemId = slea.readInt();
			nState = slea.readInt();
			nPrice = slea.readLong();
			nBuyTime = slea.readLong();
			deposit = slea.readInt();
			deposit = slea.readInt();
			nCount = slea.readInt();
			nWorldId = slea.readInt();
			if (nCount < 0 || nCount > Short.MAX_VALUE || nPrice < 0) {
				System.out.println(c.getPlayer().getName() + " 캐릭터가 경매장에 비정상적인 패킷을 유도함.");
				c.getSession().close();
				return;
			} else {
				item = items.get(dwAuctionId);
				if (item != null && item.getItem() != null && item.getHistory() != null) {
					Item it = item.getItem().copy();
					AuctionHistory history = item.getHistory();
					if (history.getId() != dwInventoryId) {
						System.out.println("return 1");
						return;
					}
					if (history.getAuctionId() != dwAuctionId) {
						System.out.println("return 2");
						return;
					}
					if (history.getAccountId() != dwAccountId) {
						System.out.println("return 3");
						return;
					}
					if (history.getCharacterId() != dwCharacterId) {
						System.out.println("return 4");
						return;
					}
					if (history.getItemId() != nItemId) {
						System.out.println("return 5");
						return;
					}
					if (history.getState() != nState) {
						System.out.println("return 6");
						return;
					}
					if (history.getPrice() != nPrice) {
						System.out.println("return 7");
						return;
					}
					if (PacketHelper.getTime(history.getBuyTime()) != nBuyTime) {
						System.out.println("return 8");
						return;
					}
					if (history.getDeposit() != deposit) {
						System.out.println("return 9");
						return;
					}
					if (history.getQuantity() != nCount) {
						System.out.println("return 10");
						return;
					}
					if (history.getWorldId() != nWorldId) {
						System.out.println("return 11");
						return;
					}
					if (c.getPlayer().getId() != dwCharacterId) {
						if (ItemFlag.KARMA_EQUIP.check(it.getFlag())) {
							it.setFlag((it.getFlag() - ItemFlag.KARMA_EQUIP.getValue()));
						} else if (ItemFlag.KARMA_USE.check(it.getFlag())) {
							it.setFlag((it.getFlag() - ItemFlag.KARMA_USE.getValue()));
						}
					}
					short slot = c.getPlayer().getInventory(GameConstants.getInventoryType(nItemId)).addItem(it);
					if (slot >= 0) {
						item.setState(item.getState() + 5);
						history.setState(history.getState() + 5);
						it.setGMLog(new StringBuilder().append(StringUtil.getAllCurrentTime()).append("에 ")
								.append("경매장에서 얻은 " + dwCharacterId + "의 아이템.").toString());
						c.getSession().writeAndFlush(CWvsContext.InventoryPacket
								.addInventorySlot(GameConstants.getInventoryType(nItemId), it));
						c.getSession().writeAndFlush(CField.AuctionPacket.AuctionCompleteItemUpdate(item));
						c.getSession().writeAndFlush(CField.AuctionPacket.AuctionCompleteItemResult());
					}
					break;
				}
			}
			break;
		}
		case 40:
		case 41:
			searchItems = new ArrayList<>();
			slea.skip(1);
			searchType = slea.readInt();
			nameWithSpace = slea.readMapleAsciiString();
			nameWithoutSpace = slea.readMapleAsciiString();
			if (searchType == -1) {
				for (AuctionItem auctionItem : items.values()) {
					String name = ii.getName(auctionItem.getItem().getItemId());
					if (name != null
							&& (name.replaceAll(" ", "").contains(nameWithSpace)
									|| name.replaceAll(" ", "").contains(nameWithoutSpace))
							&& ((op == 40 && auctionItem.getState() == 0)
									|| (op == 41 && (auctionItem.getState() == 3 || auctionItem.getState() == 8)))) {
						searchItems.add(auctionItem);
					}
				}
			} else {
				int itemType = slea.readInt();
				int itemSemiType = slea.readInt();
				int lvMin = slea.readInt();
				int lvMax = slea.readInt();
				long priceMin = slea.readLong();
				long priceMax = slea.readLong();
				int potentialType = slea.readInt();
				boolean and = (slea.readByte() == 1);
				int optionalSearchCount = slea.readInt();
				for (int i4 = 0; i4 < optionalSearchCount; i4++) {
					boolean isStarForce = (slea.readInt() == 1);
					int optionType = slea.readInt();
					int i5 = slea.readInt();
				}
				if (searchType <= 1) {
					for (AuctionItem auctionItem : items.values()) {
						if (auctionItem.getItem() != null && auctionItem.getItem().getType() == 1) {
							Equip equip = (Equip) auctionItem.getItem();
							int level = ii.getReqLevel(auctionItem.getItem().getItemId());
							boolean lvLimit = (level >= lvMin && level <= lvMax);
							boolean priceLimit = (auctionItem.getPrice() >= priceMin
									&& auctionItem.getPrice() <= priceMax);
							boolean potentialLimit = (potentialType == -1
									|| (potentialType == 0 && equip.getState() == 0)
									|| (potentialType > 0 && equip.getState() - 16 == potentialType));
							boolean typeLimit = typeLimit(searchType, itemType, itemSemiType, equip.getItemId());
							String name = ii.getName(auctionItem.getItem().getItemId());
							if (typeLimit && lvLimit && priceLimit && potentialLimit
									&& (name.contains(nameWithSpace) || name.contains(nameWithoutSpace)
											|| nameWithoutSpace.isEmpty())
									&& equipOptionTypes() && ((op == 40 && auctionItem.getState() == 0) || (op == 41
											&& (auctionItem.getState() == 3 || auctionItem.getState() == 8)))) {
								searchItems.add(auctionItem);
							}
						}
					}
				} else {
					for (AuctionItem auctionItem : items.values()) {
						int level = ii.getReqLevel(auctionItem.getItem().getItemId());
						boolean lvLimit = (level >= lvMin && level <= lvMax);
						boolean priceLimit = (auctionItem.getPrice() >= priceMin && auctionItem.getPrice() <= priceMax);
						boolean typeLimit = typeLimit(searchType, itemType, itemSemiType,
								auctionItem.getItem().getItemId());

						String name = ii.getName(auctionItem.getItem().getItemId());
						if (typeLimit && lvLimit && priceLimit
								&& (name.contains(nameWithSpace) || name.contains(nameWithoutSpace)
										|| nameWithoutSpace.isEmpty())
								&& ((op == 40 && auctionItem.getState() == 0) || (op == 41
										&& (auctionItem.getState() == 3 || auctionItem.getState() == 8)))) {
							searchItems.add(auctionItem);
						}
					}
				}
			}
			if (op == 40) {
				c.getSession().writeAndFlush(CField.AuctionPacket.AuctionSearchItems(searchItems));
			} else {
				c.getSession().writeAndFlush(CField.AuctionPacket.AuctionMarketPrice(searchItems));
			}
			FileoutputUtil.log(FileoutputUtil.拍賣場物品返還日誌,
					"[검색] 계정 번호 : " + c.getAccID() + " | 캐릭 번호 : " + c.getPlayer().getId() + " | 캐릭터 닉네임 : "
							+ c.getPlayer().getName() + " | 아이템 검색 : " + nameWithoutSpace);
			break;
		case 45:
			j = slea.readInt();
			auctionItem1 = items.get(Integer.valueOf(j));
			if (auctionItem1 != null) {
				for (int i4 = 0; i4 < 10; i4++) {
					if (c.getKeyValue("wish" + i4) == null) {
						c.setKeyValue("wish" + i4, String.valueOf(j));
						c.getSession().writeAndFlush(CField.AuctionPacket.AuctionAddWishlist(auctionItem1));
						c.getSession().writeAndFlush(CField.AuctionPacket.AuctionWishlistResult(auctionItem1));
						break;
					}
				}
			}
			break;
		case 46:
			wishlist = new ArrayList<>();
			wishItems = new ArrayList<>();
			for (i = 0; i < 10; i++) {
				String wish = c.getKeyValue("wish" + i);
				if (wish != null) {
					wishlist.add(Integer.valueOf(Integer.parseInt(wish)));
				}
			}
			for (iterator = wishlist.iterator(); iterator.hasNext();) {
				int i4 = ((Integer) iterator.next()).intValue();
				AuctionItem auctionItem = items.get(Integer.valueOf(i4));
				if (auctionItem != null) {
					wishItems.add(auctionItem);
				}
			}
			c.getSession().writeAndFlush(CField.AuctionPacket.AuctionWishlist(wishItems));
			break;
		case 47:
			dwAuctionId = slea.readInt();
			item = items.get(Integer.valueOf(dwAuctionId));
			if (item != null) {
				for (int i4 = 0; i4 < 10; i4++) {
					if (c.getKeyValue("wish" + i4).equals(String.valueOf(dwAuctionId))) {
						c.removeKeyValue("wish" + i4);
						c.getSession().writeAndFlush(CField.AuctionPacket.AuctionWishlistUpdate(dwAuctionId));
						c.getSession().writeAndFlush(CField.AuctionPacket.AuctionWishlistDeleteResult(dwAuctionId));
						break;
					}
				}
			}
			break;
		case 50:
			sellingItems = new ArrayList<>();
			for (AuctionItem auctionItem : items.values()) {
				if (auctionItem.getAccountId() == c.getAccID() && auctionItem.getState() == 0) {
					sellingItems.add(auctionItem);
				}
			}
			c.getSession().writeAndFlush(CField.AuctionPacket.AuctionSellingMyItems(sellingItems));
			break;
		case 51:
			completeItems = new ArrayList<>();
			for (AuctionItem auctionItem : items.values()) {
				if (((auctionItem.getState() == 2 || auctionItem.getState() == 7)
						&& auctionItem.getBidUserId() == c.getPlayer().getId())
						|| (auctionItem.getState() != 7 && auctionItem.getState() >= 3
								&& auctionItem.getAccountId() == c.getAccID())) {
					completeItems.add(auctionItem);
				}
			}
			c.getSession().writeAndFlush(CField.AuctionPacket.AuctionCompleteItems(completeItems));
			break;
		}
		if (op != 0 && op != 1 && op != 41 && op != 40 && op != 20 && op != 21) {
			completeItems = new ArrayList<>();
			List<AuctionItem> list5 = new ArrayList<>();
			List<AuctionItem> list6 = new ArrayList<>();
			List<Integer> list = new ArrayList<>();
			for (Map.Entry<Integer, AuctionItem> itemz : items.entrySet()) {
				AuctionItem auctionItem = itemz.getValue();
				if ((auctionItem.getEndDate() < System.currentTimeMillis() || auctionItem.getState() >= 2)
						&& ((auctionItem.getState() == 2 && auctionItem.getBidUserId() == c.getPlayer().getId())
								|| ((auctionItem.getState() == 3 || auctionItem.getState() == 4)
										&& auctionItem.getAccountId() == c.getAccID()))) {
					completeItems.add(auctionItem);
				}
				if (auctionItem.getState() == 0 && list6.size() < 1000) {
					list6.add(auctionItem);
				}
				if ((auctionItem.getState() == 3 || auctionItem.getState() == 8) && list5.size() < 1000) {
					list5.add(auctionItem);
				}
			}
			c.getSession().writeAndFlush(CField.AuctionPacket.AuctionCompleteItems(completeItems));
			c.getSession().writeAndFlush(CField.AuctionPacket.AuctionMarketPrice(list5));
			c.getSession().writeAndFlush(CField.AuctionPacket.AuctionSearchItems(list6));
		}
	}

	private static boolean equipOptionTypes() {
		return true;
	}

	private static boolean typeLimit(int searchType, int itemType, int itemSemiType, int itemId) {
		switch (searchType) {
		case 0:
			switch (itemType) {
			case 0:
				return !GameConstants.isWeapon(itemId);
			case 1:
				switch (itemSemiType) {
				case 0:
					return (GameConstants.isWeapon(itemId) || GameConstants.isAccessory(itemId));
				case 1:
					return (itemId / 1000 == 100);
				case 2:
					return (itemId / 1000 == 104);
				case 3:
					return (itemId / 1000 == 105);
				case 4:
					return (itemId / 1000 == 106);
				case 5:
					return (itemId / 1000 == 107);
				case 6:
					return (itemId / 1000 == 108);
				case 7:
					return (itemId / 1000 == 109);
				case 8:
					return (itemId / 1000 == 110);
				}
				break;
			case 2:
				switch (itemSemiType) {
				case 0:
					return !GameConstants.isAccessory(itemId);
				case 1:
					return (itemId / 1000 == 1012);
				case 2:
					return (itemId / 1000 == 1022);
				case 3:
					return (itemId / 1000 == 1032);
				case 4:
					return GameConstants.isRing(itemId);
				case 5:
					return (itemId / 1000 == 1122 || itemId / 1000 == 1123);
				case 6:
					return (itemId / 1000 == 1132);
				case 7:
					return GameConstants.isMedal(itemId);
				case 8:
					return (itemId / 1000 == 1152);
				case 9:
					return (itemId / 1000 == 1162);
				case 10:
					return (itemId / 1000 == 1182);
				}
				break;
			case 3:
				switch (itemSemiType) {
				case 0:
					return (itemId / 1000 >= 1612 && itemId / 1000 <= 1652);
				case 1:
					return (itemId / 1000 == 1662);
				case 2:
					return (itemId / 1000 == 1672);
				case 3:
					return (itemId / 1000 >= 1942 && itemId / 1000 <= 1972);
				}

				break;
			}

		case 1:
			switch (itemType) {
			case 0:
				return GameConstants.isWeapon(itemId);
			case 1:
				switch (itemSemiType) {
				case 0:
					return !GameConstants.isTwoHanded(itemId);
				case 1:
					return (itemId / 1000 == 1212);
				case 2:
					return (itemId / 1000 == 1222);
				case 3:
					return (itemId / 1000 == 1232);
				case 4:
					return (itemId / 1000 == 1242);
				case 5:
					return (itemId / 1000 == 1302);
				case 6:
					return (itemId / 1000 == 1312);
				case 7:
					return (itemId / 1000 == 1322);
				case 8:
					return (itemId / 1000 == 1332);
				case 9:
					return (itemId / 1000 == 1342);
				case 10:
					return (itemId / 1000 == 1362);
				case 11:
					return (itemId / 1000 == 1372);
				case 12:
					return (itemId / 1000 == 1262);
				case 13:
					return (itemId / 1000 == 1272);
				case 14:
					return (itemId / 1000 == 1282);
				case 15:
					return (itemId / 1000 == 1292);
				case 16:
					return (itemId / 1000 == 1213);
				}
				break;
			case 2:
				switch (itemSemiType) {
				case 0:
					return GameConstants.isTwoHanded(itemId);
				case 1:
					return (itemId / 1000 == 1402);
				case 2:
					return (itemId / 1000 == 1412);
				case 3:
					return (itemId / 1000 == 1422);
				case 4:
					return (itemId / 1000 == 1432);
				case 5:
					return (itemId / 1000 == 1442);
				case 6:
					return (itemId / 1000 == 1452);
				case 7:
					return (itemId / 1000 == 1462);
				case 8:
					return (itemId / 1000 == 1472);
				case 9:
					return (itemId / 1000 == 1482);
				case 10:
					return (itemId / 1000 == 1492);
				case 11:
					return (itemId / 1000 == 1522);
				case 12:
					return (itemId / 1000 == 1532);
				case 13:
					return (itemId / 1000 == 1582);
				case 14:
					return (itemId / 1000 == 1592);
				}

				break;
			case 3:
				switch (itemSemiType) {
				case 0:
					return (itemId / 1000 == 1352 || itemId / 1000 == 1353);
				case 1:
					return (itemId / 10 == 135220);
				case 2:
					return (itemId / 10 == 135221);
				case 3:
					return (itemId / 10 == 135222);
				case 4:
					return (itemId / 10 == 135223 || itemId / 10 == 135224 || itemId / 10 == 135225);
				case 5:
					return (itemId / 10 == 135226);
				case 6:
					return (itemId / 10 == 135227);
				case 7:
					return (itemId / 10 == 135228);
				case 8:
					return (itemId / 10 == 135229);
				case 9:
					return (itemId / 10 == 135290);
				case 10:
					return (itemId / 10 == 135291);
				case 11:
					return (itemId / 10 == 135292);
				case 12:
					return (itemId / 10 == 135297);
				case 13:
					return (itemId / 10 == 135293);
				case 14:
					return (itemId / 10 == 135294);
				case 15:
					return (itemId / 10 == 135240);
				case 16:
					return (itemId / 10 == 135201);
				case 17:
					return (itemId / 10 == 135210);
				case 18:
					return (itemId / 10 == 135310);
				case 19:
					return (itemId / 10 == 135295);
				case 20:
					return (itemId / 10 == 135296);
				case 21:
					return (itemId / 10 == 135300);
				case 22:
					return (itemId / 10 == 135270);
				case 23:
					return (itemId / 10 == 135250);
				case 24:
					return (itemId / 10 == 135260);
				case 25:
					return (itemId / 10 == 135320);
				case 26:
					return (itemId / 10 == 135340);
				case 27:
					return (itemId / 10 == 135330);
				case 28:
					return (itemId / 10 == 135350);
				case 29:
					return (itemId / 10 == 135360);
				case 30:
					return (itemId / 10 == 135370);
				case 31:
					return (itemId / 10 == 135380);
				case 32:
					return (itemId / 10 == 135390);
				}
				break;
			}
			break;
		case 2:
			return (itemId / 1000000 == 2);
		case 3:
			return MapleItemInformationProvider.getInstance().isCash(itemId);
		case 4:
			return (itemId / 1000000 == 4 || itemId / 1000000 == 3);
		}
		return false;
	}
}