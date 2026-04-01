// 
// Decompiled by Procyon v0.5.36
// 
package handling.channel.handler;

import client.MapleCharacter;
import client.Skill;
import client.SkillFactory;
import client.messages.commands.SuperGMCommand.map;
import constants.GameConstants;
import server.SecondaryStatEffect;
import server.life.MapleMonster;
import server.maps.MapleMap;
import tools.AttackPair;
import tools.Pair;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AttackInfo {

	public int skill;
	public int charge;
	public int lastAttackTickCount;
	public List<AttackPair> allDamage;
	public Point position;
	public Point chain;
	public Point plusPosition;
	public Point plusPosition2;
	public Point plusPosition3;
	public Point attackPosition;
	public Point attackPosition2;
	public Point attackPosition3;
	public Rectangle acrossPosition;
	public int display;
	public int facingleft;
	public int count;
	public int subAttackType;
	public int subAttackUnk;
	public byte hits;
	public byte targets;
	public byte tbyte;
	public byte speed;
	public byte animation;
	public byte plusPos;
	public short AOE;
	public short slot;
	public short csstar;
	public boolean real;
	public boolean across;
	public boolean Aiming;
	public byte attacktype;
	public boolean isLink;
	public byte isBuckShot;
	public byte isShadowPartner;
	public byte nMoveAction;
	public byte rlType;
	public byte bShowFixedDamage;
	public int item;
	public int skilllevel;
	public int asist;
	public int summonattack;
	public List<Point> mistPoints;
	public List<Pair<Integer, Integer>> attackObjects;
	private MapleMonster targetMonster;// 檢查怪物血量

	public AttackInfo() {
		this.charge = 0;
		this.plusPosition = new Point();
		this.display = 0;
		this.facingleft = 0;
		this.count = 0;
		this.tbyte = 0;
		this.speed = 0;
		this.real = true;
		this.across = false;
		this.Aiming = false;
		this.attacktype = 0;
		this.isLink = false;
		this.isBuckShot = 0;
		this.isShadowPartner = 0;
		this.nMoveAction = -1;
		this.bShowFixedDamage = 0;
		this.skilllevel = 0;
		this.mistPoints = new ArrayList<Point>();
		this.attackObjects = new ArrayList<Pair<Integer, Integer>>();
	}

	public final SecondaryStatEffect getAttackEffect(final MapleCharacter chr, final int skillLevel,
			final Skill skill_) {
		if (GameConstants.isLinkedSkill(this.skill)) {
			final Skill skillLink = SkillFactory.getSkill(GameConstants.getLinkedSkill(this.skill));
			return skillLink.getEffect(skillLevel);
		}
		return skill_.getEffect(skillLevel);
	}

	public MapleMonster getTargetMonster() {// 怪物血量對象
		return this.targetMonster;
	}

	public void setTargetMonster(MapleMonster monster) {// 怪物血量對象
		this.targetMonster = monster;
	}

	// 方案1：通过玩家对象直接获取
	public void setTargetMonster(int monsterId, MapleMap map, MapleCharacter player) {
		this.targetMonster = map.getMonsterByOid(monsterId);
		if (this.targetMonster == null) {
			System.out.printf("[WARN]  怪物不存在: ID=%d | 玩家坐标: %s%n", monsterId, player.getPosition().toString()); // 使用玩家对象的getPosition()
		}
	}

	// 仅负责检测血量
	public boolean isTargetMonsterDead() {
		if (this.targetMonster == null) {
			System.out.println("[DEBUG]  目标未绑定");
			return false;
		}
		return this.targetMonster.getHp() <= 0;
	}

	public boolean TargetMonsterDead() {
		boolean isDead = this.targetMonster.getHp() <= 0;
		System.out.printf("[DEBUG]  怪物血量檢測: HP=%d, 是否死亡=%b%n", this.targetMonster.getHp(), isDead);
		return isDead;
	}

}
