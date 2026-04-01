package client;

import java.util.ArrayList;
import tools.Pair;

public class HEXAStat implements HEXACore {

	private int coreId;
	private ArrayList<Pair<Integer, Integer>> statDatas = new ArrayList<>();

	public HEXAStat(int coreId, Pair<Integer, Integer> statData1, Pair<Integer, Integer> statData2,
			Pair<Integer, Integer> statData3) {
		this.coreId = coreId;

		this.statDatas.add(statData1);
		this.statDatas.add(statData2);
		this.statDatas.add(statData3);
	}

	@Override
	public int getCoreId() {
		return coreId;
	}

	@Override
	public void setCoreId(int coreId) {
		this.coreId = coreId;
	}

	public ArrayList<Pair<Integer, Integer>> getStatDatas() {
		return statDatas;
	}

	public void setStatDatas(ArrayList<Pair<Integer, Integer>> statDatas) {
		this.statDatas = statDatas;
	}
}