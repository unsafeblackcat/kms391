package maplecontents.nexon;

import client.MapleCharacter;
import client.MapleClient;
import tools.packet.CWvsContext;

public class PinkbeanHapplyNewWeek {

	public static PinkbeanHapplyNewWeek instance = new PinkbeanHapplyNewWeek();

	private Integer primaryQuestInfoKey = 338;
	private Integer primaryUInumber = 1349;

	private String primaryEventKey = "PinkbeanHappyNewYearWeek";
	private String keyValueCoinKey = "Pinkbean_Happy_Coin";

	public static PinkbeanHapplyNewWeek getEventInstance() {
		return instance;
	}

	public Integer getPrimaryUInumber() {
		return this.primaryUInumber;
	}

	public Integer getPrimaryQuestInfoKey() {
		return this.primaryQuestInfoKey;
	}

	public String getPrimaryEventKey() {
		return this.primaryEventKey;
	}

	public void updateCharacterRewardStep(MapleCharacter character, int value, int step) {
		for (int steped = 1; steped <= 5; steped++) {
			if (character.getV(this.keyValueCoinKey + "_" + steped) == null)
				character.addKV(this.keyValueCoinKey + "_" + steped, String.valueOf(0));
		}
		character.addKV(this.keyValueCoinKey + "_" + step, String.valueOf(value));
	}

	public Integer getCharacterRewardStep(MapleCharacter character, int step) {
		return Integer.parseInt(character.getV(this.keyValueCoinKey + "_" + step));
	}

	public Integer getCoinCount(MapleCharacter character) {
		if (character.getV("pinkbean_coinCnt") == null) {
			character.addKV("pinkbean_coinCnt", "0");
		}
		return Integer.parseInt(character.getV("pinkbean_coinCnt"));
	}

	public void incCoinCount(MapleClient client, Integer amount) {
		MapleCharacter character = client.getPlayer();

		client.getSession()
				.writeAndFlush(CWvsContext.InfoPacket.updateClientInfoQuest(this.getPrimaryQuestInfoKey(),
						"rewardEx=0;" + "start=1;" + "summonCnt=0;" + this.rewardCount(character) + "coinCount="
								+ (getCoinCount(character) + amount)));
	}

	public String rewardCount(MapleCharacter character) {
		String data = "";
		for (int rewardCount = 1; rewardCount <= 5; rewardCount++) {
			data += "r" + rewardCount + "=" + getCharacterRewardStep(character, rewardCount) + ";";
		}
		return data;
	}
}
