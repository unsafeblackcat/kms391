package client.serenityRank;

public enum MainRankInfo {
	None(0), Bronze(1), Silver(2), Gold(3), Platinum(4), Diamond(5), Master(6), GrandMaster(7), Challenger(8),
	Overload(9);

	public final int rank;

	MainRankInfo(int rank) {
		this.rank = rank;
	}

	public int getValue() {
		return this.rank;
	}

}
