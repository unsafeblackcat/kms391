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

public class KalosPattern {

	public MapleMonster monster;

	public ScheduledFuture<?> patternHandler;

	public boolean[] stackOnList;
	public int[] stackValueList;

	public long lastRandomStackTime;

	public long lastExplosionTime;
	public long lastCryTime;

	public KalosPattern(MapleMonster monster) {
		this.monster = monster;

		this.stackOnList = new boolean[] { false, false, false, false };
		this.stackValueList = new int[] { 0, 0, 0, 0 };

		this.lastRandomStackTime = System.currentTimeMillis() + 60000L;

		this.lastExplosionTime = System.currentTimeMillis() + 15000L;
		this.lastCryTime = System.currentTimeMillis() + 150000L;
	}

	public void kalosEnter() {
		MapleClient c = kalosFindTarget();

		if (c == null) {
			return;
		}

		monster.getMap().broadcastMessage(BossPacket.patternKalos(c, null, 5, 25, 0, 0));

		ArrayList<Point> positions = new ArrayList<>();
		positions.add(new Point(-612, -233));
		positions.add(new Point(1626, -233));
		positions.add(new Point(630, -553));
		positions.add(new Point(0, 0));

		monster.getMap().broadcastMessage(BossPacket.patternKalos(c, positions, 1, 1, 0, 0));

		positions = new ArrayList<>();
		positions.add(new Point(-800, -800));
		positions.add(new Point(-400, -800));
		positions.add(new Point(0, -800));
		positions.add(new Point(400, -800));
		positions.add(new Point(800, -800));
		positions.add(new Point(1200, -800));
		positions.add(new Point(1600, -800));

		monster.getMap().broadcastMessage(BossPacket.patternKalos(c, positions, 5, 26, 0, 0));

		positions = new ArrayList<>();
		positions.add(new Point(1690, 534));
		positions.add(new Point(1209, 534));
		positions.add(new Point(609, 534));
		positions.add(new Point(9, 534));
		positions.add(new Point(-609, 534));

		monster.getMap().broadcastMessage(BossPacket.patternKalos(c, positions, 5, 27, 0, 0));

		kalosHandler();
	}

	public static void KalosRecv(MapleClient c, LittleEndianAccessor slea) {
		if (c.getPlayer() == null || c.getPlayer().getParty() == null) {
			return;
		}
		int type = slea.readInt();
		int subType = slea.readInt();

		switch (type) {
		case 1: { // 입장
			switch (subType) {
			case 3: {
				c.getPlayer().getMap().broadcastMessage(BossPacket.patternKalos(c, null, type, subType, 0, 0));
				break;
			}
			}
			break;
		}
		case 3: { // 約束之眼，深淵之眼攻擊
			switch (subType) {
			case 4: { // 炮擊戰鬥機雷射
				int value = slea.readInt();
				int unk = slea.readInt();
				Point position = slea.readIntPos();

				ArrayList<Point> positions = new ArrayList<>();
				positions.add(position);
				positions.add(new Point(176, -657));
				positions.add(new Point(-720, -553));
				positions.add(new Point(1785, -553));

				c.getPlayer().getMap().broadcastMessage(BossPacket.patternKalos(c, positions, type, 10, value, 0));
				break;
			}
			case 5: { // 約束眼睛雷射
				int value = slea.readInt();

				ArrayList<Point> positions = new ArrayList<>();
				positions.add(c.getPlayer().getPosition());

				c.getPlayer().getMap().broadcastMessage(BossPacket.patternKalos(c, positions, type, 18, value, 0));
				break;
			}
			case 6: { // 深淵之眼箭
				int value = slea.readInt();

				c.getPlayer().getMap().broadcastMessage(BossPacket.patternKalos(c, null, type, 22, value, 0));
				break;
			}
			case 7: { // 深淵之眼箭遇襲
				int value = slea.readInt();

				c.getPlayer().getMap().broadcastMessage(BossPacket.patternKalos(c, null, type, 24, value, 0));
				break;
			}
			}
			break;
		}
		case 5: { // 卡洛斯的意志
			switch (subType) {
			case 9: { // 卡洛斯的意志發射
				Point position = slea.readIntPos();

				ArrayList<Point> positions = new ArrayList<>();
				positions.add(position);

				int angle = slea.readInt();

				c.getPlayer().getMap()
						.broadcastMessage(BossPacket.patternKalos(c, positions, type, 28, subType, angle));
				break;
			}
			case 10: { // 以上衛星命中
				int unk = slea.readInt();
				int value = slea.readInt();

				c.getPlayer().getMap().broadcastMessage(BossPacket.patternKalos(c, null, type, 29, value, 0));
				break;
			}
			case 11: { // 以下衛星命中
				int unk = slea.readInt();
				int value = slea.readInt();

				c.getPlayer().getMap().broadcastMessage(BossPacket.patternKalos(c, null, type, 30, value, 0));
				break;
			}
			case 12: { // 球速之眼命中
				int unk = slea.readInt();

				c.getPlayer().getMap().broadcastMessage(BossPacket.patternKalos(c, null, 3, 19,
						--c.getPlayer().getParty().kalosPattern.stackValueList[0], 0));

				if (c.getPlayer().getParty().kalosPattern.stackValueList[0] == 0) {
					c.getPlayer().getMap().broadcastMessage(BossPacket.patternKalos(c, null, 3, 16, 0, 0));

					c.getPlayer().getParty().kalosPattern.stackOnList[0] = false;
				}
				break;
			}
			case 13: { // 深淵之眼命中
				int unk = slea.readInt();

				c.getPlayer().getMap().broadcastMessage(BossPacket.patternKalos(c, null, 3, 23,
						--c.getPlayer().getParty().kalosPattern.stackValueList[1], 0));

				if (c.getPlayer().getParty().kalosPattern.stackValueList[1] == 0) {
					c.getPlayer().getMap().broadcastMessage(BossPacket.patternKalos(c, null, 3, 20, 0, 0));

					c.getPlayer().getParty().kalosPattern.stackOnList[1] = false;
				}
				break;
			}
			case 14: { // 炮擊戰鬥機命中
				int value = slea.readInt();
				int unk = slea.readInt();
				Point position = slea.readIntPos();

				ArrayList<Point> positions = new ArrayList<>();
				positions.add(position);

				c.getPlayer().getMap().broadcastMessage(BossPacket.patternKalos(c, null, 3, 12, value,
						--c.getPlayer().getParty().kalosPattern.stackValueList[2]));

				if (c.getPlayer().getParty().kalosPattern.stackValueList[2] == 0) {
					c.getPlayer().getMap().broadcastMessage(BossPacket.patternKalos(c, positions, 3, 9, 0, 0));

					c.getPlayer().getParty().kalosPattern.stackOnList[2] = false;
				}
				break;
			}
			case 15: { // 奧迪烏姆的球體命中
				int value = slea.readInt();
				int unk = slea.readInt();

				c.getPlayer().getMap().broadcastMessage(BossPacket.patternKalos(c, null, 3, 15, value,
						--c.getPlayer().getParty().kalosPattern.stackValueList[3]));

				if (c.getPlayer().getParty().kalosPattern.stackValueList[3] == 0) {
					c.getPlayer().getMap().broadcastMessage(BossPacket.patternKalos(c, null, 3, 13, 0, 0));

					c.getPlayer().getParty().kalosPattern.stackOnList[3] = false;
				}
				break;
			}
			}
			break;
		}
		}
	}

	public MapleClient kalosFindTarget() {
		MapleClient c = null;

		if (monster == null || monster.getMap() == null) {
			return null;
		}

		while (c == null) {
			int size = monster.getMap().getAllCharactersThreadsafe().size();

			if (size == 0) {
				return null;
			}

			int randomIndex = Randomizer.rand(0, size - 1);
			MapleCharacter character = monster.getMap().getAllCharactersThreadsafe().get(randomIndex);
			if (character != null) {
				c = character.getClient();
			}
		}

		return c;
	}

	public void kalosHandler() {
		if (monster != null) {
			patternHandler = Timer.MobTimer.getInstance().register(new Runnable() {

				@Override
				public void run() {
					try {
						if (monster.isAlive()) {
							MapleClient c = kalosFindTarget();

							if (c == null) {
								return;
							}

							// 隨機烙印模式（冷卻時間1分鐘）
							if (lastRandomStackTime <= System.currentTimeMillis()) {
								lastRandomStackTime = System.currentTimeMillis() + 60000L;

								if (!stackOnList[0] || !stackOnList[1] || !stackOnList[2] || !stackOnList[3]) {
									int randomStack = Randomizer.rand(0, 3);

									while (stackOnList[randomStack]) {
										randomStack = Randomizer.rand(0, 3);
									}

									switch (randomStack) {
									case 0: { // 啟動左側石碑
										monster.getMap()
												.broadcastMessage(BossPacket.patternKalos(c, null, 3, 17, 0, 0));
										monster.getMap().broadcastMessage(BossPacket.patternKalos(c, null, 1, 2, 0, 0));

										stackOnList[randomStack] = true;
										stackValueList[randomStack] = 3;

										c.getPlayer().getMap().broadcastMessage(
												CWvsContext.serverNotice(5, "", "T-boy的干涉讓拘留的眼睛從睡夢中醒來."));
										break;
									}
									case 1: { // 啟動右側石碑
										monster.getMap()
												.broadcastMessage(BossPacket.patternKalos(c, null, 3, 21, 0, 0));
										monster.getMap().broadcastMessage(BossPacket.patternKalos(c, null, 1, 2, 0, 0));

										stackOnList[randomStack] = true;
										stackValueList[randomStack] = 3;

										c.getPlayer().getMap().broadcastMessage(
												CWvsContext.serverNotice(5, "", "T-boy的干涉讓深淵的眼睛從睡夢中醒來."));
										break;
									}
									case 2: { // 啟動炮擊戰鬥機（無人機）
										ArrayList<Point> positions = new ArrayList<>();
										positions.add(new Point(1520, -657));
										positions.add(new Point(-720, -553));
										positions.add(new Point(1785, -553));

										monster.getMap()
												.broadcastMessage(BossPacket.patternKalos(c, positions, 3, 10, 0, 1));
										monster.getMap().broadcastMessage(BossPacket.patternKalos(c, null, 1, 2, 0, 0));

										stackOnList[randomStack] = true;
										stackValueList[randomStack] = 2;

										c.getPlayer().getMap()
												.broadcastMessage(CWvsContext.serverNotice(5, "", "T-boy的干擾將啟動炮擊戰鬥機."));
										break;
									}
									case 3: { // 啟動球體
										ArrayList<Point> positions = new ArrayList<>();
										positions.add(new Point(1690, 389));
										positions.add(new Point(1509, -553));
										positions.add(new Point(1209, 389));
										positions.add(new Point(909, -553));
										positions.add(new Point(609, 389));
										positions.add(new Point(309, -553));
										positions.add(new Point(9, 389));
										positions.add(new Point(-309, -553));
										positions.add(new Point(-609, 389));

										monster.getMap()
												.broadcastMessage(BossPacket.patternKalos(c, positions, 3, 14, 0, 0));
										monster.getMap().broadcastMessage(BossPacket.patternKalos(c, null, 1, 2, 0, 0));

										stackOnList[randomStack] = true;
										stackValueList[randomStack] = 2;

										c.getPlayer().getMap().broadcastMessage(
												CWvsContext.serverNotice(5, "", "T-boy的干涉，檢測到奧迪姆的球體的敵人."));
										break;
									}
									}

									if (stackOnList[0] && stackOnList[1] && stackOnList[2] && stackOnList[3]) {
										monster.getMap().broadcastMessage(BossPacket.patternKalos(c, null, 1, 7, 0, 0));

										c.getPlayer().getMap().broadcastMessage(
												CWvsContext.serverNotice(5, "", "T-boy的干涉强化，摩天大樓的氣氛高漲."));
									}
								}
							}

							// 爆炸模式（冷卻時間15秒）
							if (lastExplosionTime <= System.currentTimeMillis()) {
								lastExplosionTime = System.currentTimeMillis() + 15000L;

								monster.getMap().broadcastMessage(BossPacket.patternKalosMap());
							}

							// 監視者的咆哮模式（冷卻時間2分30秒）
							if (lastCryTime <= System.currentTimeMillis()) {
								lastCryTime = System.currentTimeMillis() + 150000L;

								ArrayList<Point> positions = new ArrayList<>();
								positions.add(c.getPlayer().getPosition());
								positions.add(c.getPlayer().getPosition());

								monster.getMap().broadcastMessage(BossPacket.patternKalosMsg(0));

								Timer.MobTimer.getInstance().schedule(new Runnable() {

									@Override
									public void run() {
										if (monster != null && monster.isAlive()) {
											monster.getMap().broadcastMessage(
													BossPacket.patternKalosCry1(monster.getObjectId()));
											monster.getMap().broadcastMessage(
													BossPacket.patternKalosCry2(monster.getObjectId()));
											monster.getMap().broadcastMessage(
													BossPacket.patternKalosCry3(monster.getObjectId()));
										}
									}
								}, 5000L);
							}
						} else {
							if (patternHandler != null) {
								patternHandler.cancel(true);
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