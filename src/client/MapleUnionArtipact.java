package client;

import java.util.ArrayList;

public class MapleUnionArtipact {

	private long id;
	private int index;
	private int level;
	private ArrayList<Integer> skills;
	private long expirationTime;

	public static int[] skillStorage = { 80003516, 80003517, 80003518, 80003519, 80003520, 80003521, 80003522, 80003523,
			80003524, 80003525, 80003526, 80003527, 80003528, 80003529, 80003530, 80003531 };

	public static int[][] settings = { { 10100, 2500, 1 }, { 10200, 2550, 2 }, { 10300, 2600, 3 }, { 10400, 2650, 4 },
			{ 10500, 2700, 6 }, { 10600, 2750, 7 }, { 10700, 2800, 8 }, { 10800, 2850, 9 }, { 10900, 2900, 10 },
			{ 11000, 2950, 12 }, { 11100, 3000, 13 }, { 11200, 3050, 14 }, { 11300, 3100, 15 }, { 11400, 3150, 16 },
			{ 11500, 3200, 18 }, { 11600, 3250, 19 }, { 11700, 3300, 20 }, { 11800, 3350, 21 }, { 11900, 3400, 22 },
			{ 12000, 3450, 24 }, { 12100, 3500, 25 }, { 12200, 3550, 26 }, { 12300, 3600, 27 }, { 12400, 3700, 28 },
			{ 12500, 3800, 30 }, { 12600, 3900, 31 }, { 12700, 4000, 32 }, { 12800, 4500, 33 }, { 12900, 5000, 34 },
			{ 13000, 5500, 36 }, { 13200, 6000, 37 }, { 13400, 6500, 38 }, { 13600, 7000, 39 }, { 13800, 7500, 40 },
			{ 14000, 8000, 42 }, { 14200, 8500, 43 }, { 14400, 9000, 44 }, { 14600, 9500, 45 }, { 14800, 10000, 46 },
			{ 15000, 12000, 48 }, { 15200, 14000, 49 }, { 15400, 16000, 50 }, { 15600, 18000, 51 },
			{ 15800, 20000, 52 }, { 16000, 22000, 54 }, { 16200, 24000, 55 }, { 16400, 26000, 56 },
			{ 16600, 28000, 57 }, { 16800, 30000, 58 }, { 17000, 50000, 60 }, { 17300, 55000, 61 },
			{ 17600, 60000, 62 }, { 17900, 65000, 63 }, { 18200, 70000, 64 }, { 18500, 100000, 66 },
			{ 18800, 110000, 67 }, { 19100, 120000, 68 }, { 19400, 130000, 69 }, { 19700, 200000, 70 },
			{ 20000, 0, 72 } };

	public MapleUnionArtipact(int index, int level, ArrayList<Integer> skills, long expirationTime) {
		this.index = index;
		this.level = level;
		this.skills = skills;
		this.expirationTime = expirationTime;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void levelUp() {
		if (level >= 5) {
			return;
		}

		this.level++;
	}

	public ArrayList<Integer> getSkills() {
		return skills;
	}

	public void setSkills(ArrayList<Integer> skills) {
		this.skills = skills;
	}

	public long getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(long expirationTime) {
		this.expirationTime = expirationTime;
	}
}