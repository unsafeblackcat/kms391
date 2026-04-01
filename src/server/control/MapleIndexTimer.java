package server.control;

import client.MapleCharacter;
import constants.GameConstants;
import handling.channel.ChannelServer;
import handling.channel.handler.InventoryHandler;
import java.util.Iterator;
import java.util.List;
import server.MapleInventoryManipulator;
import server.maps.MapleMapItem;
import server.maps.MapleMapObject;
import tools.packet.CWvsContext;

public class MapleIndexTimer implements Runnable {

	public long lastClearDropTime = 0;

	private MapleMapItem mapitem;

	public MapleIndexTimer() {
		lastClearDropTime = System.currentTimeMillis();
		System.out.println("[加載已完成] 啟動冒險島索引計時器");
	}

	@Override
	public void run() {
		long time = System.currentTimeMillis();
		Iterator<ChannelServer> channels = ChannelServer.getAllInstances().iterator();
		while (channels.hasNext()) {
			ChannelServer cs = channels.next();
			Iterator<MapleCharacter> chrs = cs.getPlayerStorage().getAllCharacters().values().iterator();
			while (chrs.hasNext()) {
				MapleCharacter chr = chrs.next();
				AutoRoot(chr);
			}
		}
	}

	public void AutoRoot(MapleCharacter chr) {
		if (chr.getKeyValue(12345, "AutoRoot") > 0 && !GameConstants.보스맵(chr.getMapId())) {
			List<MapleMapObject> objs = chr.getMap().getItemsInRange(chr.getPosition(), Double.MAX_VALUE);
			for (MapleMapObject ob : objs) {
				MapleMapItem mapitem = (MapleMapItem) ob;
				if (mapitem.getItem() != null && !MapleInventoryManipulator.checkSpace(chr.getClient(),
						mapitem.getItemId(), mapitem.getItem().getQuantity(), "")) {
					continue;
				}
				if (mapitem.isPickpoket()) {
					continue;
				}
				// 명예의 훈장
				if (mapitem.getItemId() == 2432970) {
					chr.gainHonor(10000);
					chr.getClient().getSession()
							.writeAndFlush((Object) CWvsContext.InventoryPacket.getInventoryStatus(true));
					InventoryHandler.removeItem(chr, mapitem, ob);
					return;
				}
				if (!mapitem.isPlayerDrop()) {
					InventoryHandler.pickupItem(ob, chr.getClient(), chr);
				}
			}
		}
	}
}
