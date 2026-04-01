package server.life;

import server.Randomizer;
import server.maps.MapleMap;
import tools.packet.CWvsContext;

import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class SpawnPointAreaBoss extends Spawns {

	private MapleMonsterStats monster;

	private Point pos1;

	private Point pos2;

	private Point pos3;

	private long nextPossibleSpawn;

	private int mobTime;

	private int fh;

	private int f;

	private int id;

	private AtomicBoolean spawned = new AtomicBoolean(false);

	private String msg;

	public SpawnPointAreaBoss(MapleMonster monster, Point pos1, Point pos2, Point pos3, int mobTime, String msg,
			boolean shouldSpawn) {
		this.monster = monster.getStats();
		this.id = monster.getId();
		this.fh = monster.getFh();
		this.f = monster.getF();
		this.pos1 = pos1;
		this.pos2 = pos2;
		this.pos3 = pos3;
		this.mobTime = (mobTime < 0) ? -1 : (mobTime * 1000);
		this.msg = msg;
		this.nextPossibleSpawn = System.currentTimeMillis() + (shouldSpawn ? 0L : this.mobTime);
	}

	public final int getF() {
		return this.f;
	}

	public final int getFh() {
		return this.fh;
	}

	public final MapleMonsterStats getMonster() {
		return this.monster;
	}

	public final byte getCarnivalTeam() {
		return -1;
	}

	public final int getCarnivalId() {
		return -1;
	}

	public final boolean shouldSpawn(long time) {
		if (this.mobTime < 0 || this.spawned.get()) {
			return false;
		}
		return (this.nextPossibleSpawn <= time);
	}

	public final Point getPosition() {
		int rand = Randomizer.nextInt(3);
		return (rand == 0) ? this.pos1 : ((rand == 1) ? this.pos2 : this.pos3);
	}
	// ======================================================================
	// ======================================================================

	public final MapleMonster spawnMonster(MapleMap map) {// BOSS怪物死亡和生成的處理方法
		Point pos = getPosition();// 获取怪物的生成位置
		MapleMonster mob = new MapleMonster(this.id, this.monster); // 创建一个新的怪物对象，使用当前对象存储的怪物ID和怪物统计信息
		mob.setPosition(pos); // 设置怪物的位置
		mob.setCy(pos.y); // 设置怪物的垂直坐标
		mob.setRx0(pos.x - 50); // 设置怪物的左边界坐标
		mob.setRx1(pos.x + 50); // 设置怪物的右边界坐标
		mob.setFh(this.fh); // 设置怪物的楼层高度
		mob.setF(this.f); // 设置怪物的朝向
		this.spawned.set(true); // 标记该生成点已经有怪物生成
		mob.addListener(new MonsterListener() { // 为怪物添加一个监听器，当怪物被击杀时触发相应逻辑

			public void monsterKilled() { // 当怪物被击杀时调用此方法。
				SpawnPointAreaBoss.this.nextPossibleSpawn = System.currentTimeMillis();// 更新下一次可能的生成时间为当前时间
				if (SpawnPointAreaBoss.this.mobTime > 0) { // 如果设置了怪物生成时间间隔（mobTime > 0）
					SpawnPointAreaBoss.this.nextPossibleSpawn = SpawnPointAreaBoss.this.nextPossibleSpawn// 下一次可能的生成时间为当前时间加上生成时间间隔
							+ SpawnPointAreaBoss.this.mobTime;
				}
				SpawnPointAreaBoss.this.spawned.set(false); // 标记该生成点不再有怪物生成
			}
		});
		map.spawnMonster(mob, -2); // 在指定地图上生成怪物，并指定生成效果参数为 -2
		if (this.msg != null) {// 如果设置了生成怪物时要广播的消息
			map.broadcastMessage(CWvsContext.serverNotice(6, "", this.msg)); // 在地图上广播消息，消息类型为 6，标题为空，内容为 msg
		}
		return mob; // 返回生成的怪物对象
	}
	// ======================================================================
	// ======================================================================

	public final int getMobTime() {
		return this.mobTime;
	}
}
