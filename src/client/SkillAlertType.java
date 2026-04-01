package client;

import java.util.Arrays;

public enum SkillAlertType {
	NOT_DEFINED(-1), ADD(0), UPDATE_STATE(1), REMOVE(2), CHANGE_POSITION(3);

	int type;

	private SkillAlertType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public static SkillAlertType getFromType(int type) {
		return Arrays.stream(SkillAlertType.values()).filter(skillAlertType -> skillAlertType.getType() == type)
				.findFirst().orElse(SkillAlertType.NOT_DEFINED);
	}
}
