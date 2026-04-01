package client;

public class HEXASkill implements HEXACore {

	private int coreId;
	private int level;

	public HEXASkill(int coreId, int level) {
		this.coreId = coreId;
		this.level = level;
	}

	@Override
	public int getCoreId() {
		return coreId;
	}

	@Override
	public void setCoreId(int coreId) {
		this.coreId = coreId;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
}