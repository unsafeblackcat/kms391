package server.control;

import client.MapleCharacter;
import client.SecondaryStat;
import client.SkillFactory;
import constants.GameConstants;
import handling.channel.ChannelServer;
import java.util.ArrayList;
import tools.CurrentTime1;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import tools.Pair;

public class MapleEtcControl implements Runnable {

	public long lastClearDropTime = 0, lastResetTimerTime = 0;
	public int date;
	public long lastCoreTime = 0;
	public static List<Pair<String, Long>> lastHeartBeatTime = new ArrayList<>();

	public MapleEtcControl() {
		lastClearDropTime = System.currentTimeMillis();
		date = CurrentTime1.요일();
		System.out.println("[加載已完成] 啟動控制台");
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
				if (chr.isAlive()) {
					List<Integer> prevEffects = chr.getPrevBonusEffect();
					List<Integer> curEffects = chr.getBonusEffect();
					for (int i = 0; i < curEffects.size(); i++) {
						if (!prevEffects.get(i).equals(curEffects.get(i))) {
							chr.cancelEffectFromBuffStat(SecondaryStat.IndieDamR, 80002419);
							chr.cancelEffectFromBuffStat(SecondaryStat.IndieExp, 80002419);
							chr.cancelEffectFromBuffStat(SecondaryStat.DropRate, 80002419);
							chr.cancelEffectFromBuffStat(SecondaryStat.MesoUp, 80002419);
							chr.cancelEffectFromBuffStat(SecondaryStat.IndieCD, 80002419);
							chr.cancelEffectFromBuffStat(SecondaryStat.IndieBDR, 80002419);
							chr.cancelEffectFromBuffStat(SecondaryStat.IndieAllStatR, 80002419);
							chr.cancelEffectFromBuffStat(SecondaryStat.IndiePmdR, 80002419);
							SkillFactory.getSkill(80002419).getEffect(1).applyTo(chr);
							chr.getStat().recalcLocalStats(chr);
							break;
						}
					}
				}

				if (chr.getMapId() == 180000000 && !chr.isGM()) {
					// 假设这里是 chr.ban，因为 chr.getPlayer() 可能导致空指针问题
					chr.ban("운영자맵 입장", true, true, true);
				}

				if (chr.getMapId() == 261020700 || chr.getMapId() == 261010103) {
					long ppp = chr.getKeyValue(124, "ppp");
					ppp += 1000;
					chr.setKeyValue(124, "ppp", String.valueOf(ppp));

					int pp = (int) chr.getKeyValue(123, "pp");
					if (pp <= 0) {
						chr.warp(120043000);
						chr.dropMessage(5, "沒有疲勞度，回到村子裏.");
					} else {
						if (ppp > 60000) {
							pp -= 2;
							if (pp < 0) {
								pp = 0;
							}
							chr.setKeyValue(123, "pp", String.valueOf(pp));
							chr.setKeyValue(124, "ppp", "0");
							chr.dropMessage(5, "疲勞度减少。 剩餘的疲勞度 : " + pp);
						}
					}
				}
				Calendar cal = Calendar.getInstance();
				if (cal.get(Calendar.HOUR_OF_DAY) == 0 && cal.get(Calendar.MINUTE) < 1) {
					chr.setKeyValue(125, "date", String.valueOf(GameConstants.getCurrentDate_NoTime()));
					int pirodo = 0;
					int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
					switch (dayOfWeek) {
					case Calendar.MONDAY:
						pirodo = 100;
						break;
					case Calendar.TUESDAY:
						pirodo = 70;
						break;
					case Calendar.WEDNESDAY:
						pirodo = 90;
						break;
					case Calendar.THURSDAY:
						pirodo = 110;
						break;
					case Calendar.FRIDAY:
						pirodo = 130;
						break;
					case Calendar.SATURDAY:
						pirodo = 150;
						break;
					case Calendar.SUNDAY:
						pirodo = 170;
						break;
					default:
						pirodo = 0;
						break;
					}
					chr.setKeyValue(123, "pp", String.valueOf(pirodo));
					// chr.dropMessage(5, "자정이지나 피로도가 초기화 되었습니다.");
				}

			}
		}
	}
}