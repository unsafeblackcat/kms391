package handling.world.guild;

import client.MapleClient;
import server.life.MapleLifeFactory;
import server.life.MapleMonster;
import tools.packet.CField;
import tools.packet.SLFCGPacket;

import java.awt.*;

public class MapleGuildSulo {
	private MapleClient c;

	public MapleGuildSulo(MapleClient client) {
		this.c = client;
	}

	public final void EnterSulo() {
		c.getPlayer().removeSkillCustomInfo(941000200);
		c.getPlayer().removeSkillCustomInfo(941000201);
		c.getPlayer().warp(941000200);
		c.getPlayer().getMap().resetFully();
		c.getPlayer().getMap().broadcastMessage(CField.getClock(0));
		server.Timer.EventTimer.getInstance().schedule(() -> {
			c.getSession().writeAndFlush(
					(Object) CField.environmentChange("Effect/EventEffect.img/ChallengeMission/Count/3", 16));
			c.getSession().writeAndFlush((Object) SLFCGPacket.playSE("Sound/MiniGame.img/multiBingo/3"));
			c.getPlayer().dropMessage(5, "1");
			server.Timer.EventTimer.getInstance().schedule(() -> {
				c.getSession().writeAndFlush(
						(Object) CField.environmentChange("Effect/EventEffect.img/ChallengeMission/Count/2", 16));
				c.getSession().writeAndFlush((Object) SLFCGPacket.playSE("Sound/MiniGame.img/multiBingo/2"));
				c.getPlayer().dropMessage(5, "2");
			}, 1000L);
			server.Timer.EventTimer.getInstance().schedule(() -> {
				c.getSession().writeAndFlush(
						(Object) CField.environmentChange("Effect/EventEffect.img/ChallengeMission/Count/1", 16));
				c.getSession().writeAndFlush((Object) SLFCGPacket.playSE("Sound/MiniGame.img/multiBingo/1"));
				c.getPlayer().dropMessage(5, "3");
			}, 2000L);
			server.Timer.EventTimer.getInstance().schedule(() -> {
				c.getSession().writeAndFlush(
						(Object) CField.environmentChange("Effect/EventEffect.img/ChallengeMission/start", 16));
				c.getSession().writeAndFlush((Object) SLFCGPacket.playSE("Sound/MiniGame.img/multiBingo/start"));
				c.getPlayer().dropMessage(5, "start");
				server.Timer.EventTimer.getInstance().schedule(() -> {
					int i;
					c.send(CField.getNowClock(13)); // 손보기
					c.send(CField.PunchKingPacket(c.getPlayer(), 1, 1));
					c.getPlayer().setSkillCustomInfo(941000200, 0L, 120000L);
					c.getPlayer().setSkillCustomInfo(941000201, 1L, 0L);
					for (i = 9500800; i <= 9500805; ++i) {
						c.getPlayer().removeSkillCustomInfo(i);
					}
					c.getPlayer().setKeyValue(221121, "point", "0");
					MapleMonster boss = MapleLifeFactory.getMonster(9500800);
					boss.setOwner(c.getPlayer().getId());
					c.getPlayer().getMap().spawnMonsterOnGroundBelow(boss, new Point(20, 581));
				}, 1000L);
			}, 3000L);
		}, 2000L);
	}

	public int getSuloPoint() {
		return (int) c.getPlayer().getKeyValue(221121, "point");
	}

	public int getSuloWeekPoint() {
		return (int) c.getPlayer().getKeyValue(221122, "weekspoint");
	}

	public void setSuloPoint(Integer point) {
		c.getPlayer().setKeyValue(221121, "point", point.toString());
	}

	public void setSuloWeekPoint(Integer Weekpoint) {
		c.getPlayer().setKeyValue(221122, "weekspoint", Weekpoint.toString());
	}
}
