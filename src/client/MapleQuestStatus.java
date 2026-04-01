package client;

import constants.GameConstants;
import server.life.MapleLifeFactory;
import server.quest.MapleQuest;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class MapleQuestStatus implements Serializable {

	private static final long serialVersionUID = 91795419934134L;
	private transient MapleQuest quest;
	private byte status;
	private Map<Integer, Integer> killedMobs = null;
	private int npc;
	private long completionTime;
	private int forfeited = 0;
	private String customData;

	public MapleQuestStatus(MapleQuest quest, int status) {
		this.quest = quest;
		setStatus((byte) status);
		this.completionTime = System.currentTimeMillis();
		if (status == 1 && !quest.getRelevantMobs().isEmpty()) {
			registerMobs();
		}
	}

	public MapleQuestStatus(MapleQuest quest, byte status, int npc) {
		this.quest = quest;
		setStatus(status);
		setNpc(npc);

		this.completionTime = System.currentTimeMillis();

		if (status == 1 && !quest.getRelevantMobs().isEmpty()) {
			registerMobs();
		}
	}

	public final void setQuest(int qid) {
		this.quest = MapleQuest.getInstance(qid);
	}

	public final MapleQuest getQuest() {
		return this.quest;
	}

	public final byte getStatus() {
		return this.status;
	}

	public final void setStatus(byte status) {
		this.status = status;
	}

	public final int getNpc() {
		return this.npc;
	}

	public final void setNpc(int npc) {
		this.npc = npc;
	}

	public boolean isCustom() {
		return GameConstants.isCustomQuest(this.quest.getId());
	}

	private final void registerMobs() {
		this.killedMobs = new LinkedHashMap<>();
		for (Iterator<Integer> iterator = this.quest.getRelevantMobs().keySet().iterator(); iterator.hasNext();) {
			int i = ((Integer) iterator.next()).intValue();
			this.killedMobs.put(Integer.valueOf(i), Integer.valueOf(0));
		}
	}

	private final int maxMob(int mobid) {
		for (Map.Entry<Integer, Integer> qs : (Iterable<Map.Entry<Integer, Integer>>) this.quest.getRelevantMobs()
				.entrySet()) {
			if (((Integer) qs.getKey()).intValue() == mobid) {
				return ((Integer) qs.getValue()).intValue();
			}
		}
		return 0;
	}

	public final boolean mobKilled(int id, int skillID, MapleCharacter chr) {
		if (this.quest != null && this.quest.getSkillID() > 0 && this.quest.getSkillID() != skillID) {
			return false;
		}
		Integer mob = this.killedMobs.get(Integer.valueOf(id));
		if (mob != null) {
			int mo = maxMob(id);
			if (mob.intValue() >= mo) {
				return false;
			}
			this.killedMobs.put(Integer.valueOf(id), Integer.valueOf(Math.min(mob.intValue() + 1, mo)));
			return true;
		}
		for (Map.Entry<Integer, Integer> mo : this.killedMobs.entrySet()) {
			if (questCount(((Integer) mo.getKey()).intValue(), id)) {
				int mobb = maxMob(((Integer) mo.getKey()).intValue());
				if (((Integer) mo.getValue()).intValue() >= mobb) {
					return false;
				}
				if (((Integer) mo.getKey()).intValue() == 9101025) {
					int reqLevel = MapleLifeFactory.getMonster(id).getStats().getLevel();
					if (reqLevel >= chr.getLevel() - 20 && reqLevel <= chr.getLevel() + 20) {
						this.killedMobs.put(mo.getKey(),
								Integer.valueOf(Math.min(((Integer) mo.getValue()).intValue() + 1, mobb)));
					}

				} else if (((Integer) mo.getKey()).intValue() == 9101067) {
					int scale = MapleLifeFactory.getMonster(id).getScale();
					if (scale > 100) {
						this.killedMobs.put(mo.getKey(),
								Integer.valueOf(Math.min(((Integer) mo.getValue()).intValue() + 1, mobb)));
					}
				} else {
					this.killedMobs.put(mo.getKey(),
							Integer.valueOf(Math.min(((Integer) mo.getValue()).intValue() + 1, mobb)));
				}
				return true;
			}
		}
		return false;
	}

	private final boolean questCount(int mo, int id) {
		if (MapleLifeFactory.getQuestCount(mo) != null) {
			for (Iterator<Integer> iterator = MapleLifeFactory.getQuestCount(mo).iterator(); iterator.hasNext();) {
				int i = ((Integer) iterator.next()).intValue();
				if (i == id || mo == 9101025) {
					return true;
				}
			}
		}
		return false;
	}

	public final void setMobKills(int id, int count) {
		if (this.killedMobs == null) {
			registerMobs();
		}
		this.killedMobs.put(Integer.valueOf(id), Integer.valueOf(count));
	}

	public final boolean hasMobKills() {
		if (this.killedMobs == null) {
			return false;
		}
		return (this.killedMobs.size() > 0);
	}

	public final int getMobKills(int id) {
		Integer mob = this.killedMobs.get(Integer.valueOf(id));

		if (mob == null) {
			return 0;
		}
		return mob.intValue();
	}

	public final Map<Integer, Integer> getMobKills() {
		return this.killedMobs;
	}

	public final long getCompletionTime() {
		return this.completionTime;
	}

	public final void setCompletionTime(long completionTime) {
		this.completionTime = completionTime;
	}

	public final int getForfeited() {
		return this.forfeited;
	}

	public final void setForfeited(int forfeited) {
		if (forfeited >= this.forfeited) {
			this.forfeited = forfeited;
		} else {
			throw new IllegalArgumentException("Can't set forfeits to something lower than before.");
		}
	}

	public final void setCustomData(String customData) {
		this.customData = customData;
	}

	public final String getCustomData() {
		return this.customData;
	}
}
