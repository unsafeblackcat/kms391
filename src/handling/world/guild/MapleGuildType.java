/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package handling.world.guild;

public enum MapleGuildType {
	// 1.2.356 [-2]
	showInfo(1),
	/*
	 * lookInfo(130), InfoRes(9), getInfo(10), newInfo(11), newGuildMember(12),
	 * Request(13), delayRequest(14), // 1.2.356 (-1) memberLeft(88), Expelled(91),
	 * Disband(94), DenyReq(99), cancelRequest(100), InviteDeny(101), Invite(103),
	 * CapacityChange(111), memberUpdate(115), memberOnline(116),
	 * rankTitleChange(122), rankChange(128), Contribution(130), CustomEmblem(132),
	 * // 1.2.356 (-1) Notice(142), Setting(144), updatePoint(152),
	 * rankRequest(153), removeDisband(158), Skill(160), UseNoblessSkill(163),
	 * ChangeLeader(175), Attendance(183),
	 */

	;

	private final int type;

	private MapleGuildType(int i) {
		this.type = i;
	}

	public final int getType() {
		return type;
	}

}
