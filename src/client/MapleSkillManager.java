package client;

public class MapleSkillManager {

	public static boolean isKhaliAttackSkills(int skillId) {
		switch (skillId) {

		case 154001000: // 藝術：橫切
		case 154101000: // 藝術：雙邊緣
		case 154111002: // 阿茨：三叉戟
		case 154121000: // 藝術：弗勒裏
			return true;
		default:
			return false;
		}

	}

	public static boolean isKhaliHexSkills(int skillId) {
		switch (skillId) {
		case 154111006: // 헥스 : 차크람 스윔
		case 154120033: // 헥스 - 보스 킬러
		case 154120032: // 헥스 - 이그노어 가드
		case 154120031: // 헥스 - 리인포스
		case 400041082: // 헥스 : 판데모니움
		case 400041083: // 헥스 : 판데모니움
		case 154121002: // 헥스 : 차크람 퓨리
		case 154121001: // 헥스 : 차크람 스플릿
			return true;
		default:
			return false;
		}
	}

	public static boolean isKhaliVoydSkills(int skillId) {
		switch (skillId) {
		case 154121003: // 博伊德·布利茨
		case 154121009: // 博伊德·拉什
		case 154121011: // 博伊德·布利茨

			return true;
		default:
			return false;
		}
	}

}
