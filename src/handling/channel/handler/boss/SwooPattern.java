package handling.channel.handler.boss;

import client.MapleCharacter;
import client.MapleClient;
import java.awt.Point;
import java.util.ArrayList;
import java.util.concurrent.ScheduledFuture;
import server.Randomizer;
import server.Timer;
import server.life.MapleMonster;
import tools.data.LittleEndianAccessor;
import tools.packet.BossPacket;
import tools.packet.CWvsContext;

public class SwooPattern {

	public MapleMonster monster;

	public ScheduledFuture<?> patternHandler;

	public boolean[] stackOnList;
	public int[] stackValueList;

	public long lastRandomStackTime;

	public long lastExplosionTime;
	public long lastCryTime;

	public SwooPattern(MapleMonster monster) {
		this.monster = monster;

		this.stackOnList = new boolean[] { false, false, false, false };
		this.stackValueList = new int[] { 0, 0, 0, 0 };

		this.lastRandomStackTime = (System.currentTimeMillis() + 60000L);

		this.lastExplosionTime = (System.currentTimeMillis() + 15000L);
		this.lastCryTime = (System.currentTimeMillis() + 150000L);
	}

	public void swooEnter() {
		MapleClient c = swooFindTarget();

		swooHandler();
	}

	public static void swooRecv(MapleClient c, LittleEndianAccessor slea) {
		if (c.getPlayer().getParty() != null) {
			int type = slea.readInt();
			int subType = slea.readInt();

			switch (type) {

			}
		}
	}

	public MapleClient swooFindTarget() {
		MapleClient c = null;

		while (c == null) {
			int size = monster.getMap().getAllChracater().size();

			if (size == 0) {
				return null;
			}

			c = ((MapleCharacter) monster.getMap().getAllChracater().get(Randomizer.rand(0, (size - 1)))).getClient();
		}

		return c;
	}

	public void swooHandler() {
		if (monster != null) {
			patternHandler = Timer.MobTimer.getInstance().register(new Runnable() {

				@Override
				public void run() {
					try {
						if (monster.isAlive()) {
							MapleClient c = swooFindTarget();

							if (c == null) {
								return;
							}

						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}, 1000L);
		}
	}
}