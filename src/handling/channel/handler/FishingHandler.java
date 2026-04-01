package handling.channel.handler;

import client.MapleClient;
import client.inventory.MapleInventoryType;
import server.Randomizer;
import tools.data.LittleEndianAccessor;
import tools.packet.CField;

public class FishingHandler {

	public static int FishingMap = 993000750;

	public static int FishingChair = 3015394;

	public static int[] items = new int[] { 3801250, 3801251, 3801252, 3801253, 3801254, 3801250, 3801251, 3801252,
			3801253, 3801254, 3801255, 3801256, 3801257, 3801258, 3801259, 3801260, 3801256, 3801257, 3801258, 3801259,
			3801260, 3801261, 3801262, 3801263, 3801264, 3801265, 3801267, 3801268, 3801269, 3801270, 3801271, 3801272,
			3801273, 3801275, 3801276, 3801277, 3801278 };

	public static void fishing(LittleEndianAccessor slea, MapleClient c) {
		int i, itemId, result, item, j, type = slea.readInt();
		switch (type) {
		case 0:
			for (i = 2; i <= 5; i++) {
				if (i != 3) {
					if (c.getPlayer().getInventory((byte) i).getNextFreeSlot() == -1) {
						c.getPlayer().dropMessage(1, "背包空間不足.");
						return;
					}
				}
			}
			itemId = slea.readInt();
			if (c.getPlayer().getInventory(MapleInventoryType.ETC).countById(itemId) == 0) {
				return;
			}
			c.getPlayer().removeItem(itemId, -1);
			c.getPlayer().setKeyValue(100393, "progress", "1");
			c.getPlayer().setKeyValue(100393, "4035000", String.valueOf(c.getPlayer().getItemQuantity(4035000, false)));
			c.getSession().writeAndFlush(CField.fishing(1));
			c.getSession().writeAndFlush(CField.fishing(2));
			break;
		case 1:
			result = slea.readInt();
			item = items[Randomizer.nextInt(items.length)];
			if (result == 1) {
				c.getSession().writeAndFlush(CField.fishing(3));
				c.getPlayer().getMap().broadcastMessage(CField.fishingResult(c.getPlayer().getId(), item));
				if (c.getPlayer().getV("fishing") != null) {
					int k = Integer.parseInt(c.getPlayer().getV("fishing")) + 1;
					c.getPlayer().addKV("fishing", "" + k);
				} else {
					c.getPlayer().addKV("fishing", "1");
				}
				c.getPlayer().gainItem(item, 1);
			} else {
				c.getSession().writeAndFlush(CField.fishing(4));
			}
			c.getPlayer().setKeyValue(100393, "progress", "0");
			c.getPlayer().setKeyValue(100393, "4035000", String.valueOf(c.getPlayer().getItemQuantity(4035000, false)));
			c.getSession().writeAndFlush(CField.fishing(0));
			for (j = 2; j <= 5; j++) {
				if (j != 3) {
					if (c.getPlayer().getInventory((byte) j).getNextFreeSlot() == -1) {
						c.getPlayer().dropMessage(1, "背包空間不足.");
						return;
					}
				}
			}
			break;
		}
	}

	public static void fishingEnd(MapleClient c) {
		PlayerHandler.CancelChair((short) -1, c, c.getPlayer());
	}
}
