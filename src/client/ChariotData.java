package client;

import constants.GameConstants;
import tools.packet.CWvsContext;

public class ChariotData {

	private MapleClient c;

	public ChariotData(MapleClient client) {
		this.c = client;

		if (c.getCustomData(252, "count") == null) {
			c.setCustomData(252, "count", "0");
		}
		if (c.getCustomData(252, "T") == null) {
			c.setCustomData(252, "T", GameConstants.getCurrentFullDate());
		}

		if (c.getCustomData(253, "day") == null) {
			c.setCustomData(253, "day", "0");
		}

		if (c.getCustomData(253, "complete") == null) {
			c.setCustomData(253, "complete", "0");
		}

		if (c.getCustomData(253, "bMaxDay") == null) {
			c.setCustomData(253, "bMaxDay", "135");
		}

		if (c.getCustomData(253, "cMaxDay") == null) {
			c.setCustomData(253, "cMaxDay", "135");
		}

		if (c.getCustomData(253, "lastDate") == null) {
			c.setCustomData(253, "lastDate", "21/04/09");
		}

		if (c.getCustomData(253, "passCount") == null) {
			c.setCustomData(253, "passCount", "0");
		}
	}

	public boolean getIsReadyForAttendence() {
		String bTime = String.valueOf(getLastAttendenceTime());
		String cTime = GameConstants.getCurrentFullDate();
		// 20210226195831
		// 20210226231610
		int bH = Integer.parseInt(bTime.substring(8, 10)); // 3시
		int bM = Integer.parseInt(bTime.substring(10, 12)); // 47분
		int cH = Integer.parseInt(cTime.substring(8, 10));
		int cM = Integer.parseInt(cTime.substring(10, 12));
		if ((cH - bH == 1 && cM >= bM) || (cH - bH > 1)) {
			return true;
		}
		return false;
	}

	public long getLastAttendenceTime() {
		return Long.parseLong(c.getCustomData(252, "T"));
	}

	public int getCurrentDay() {
		return Integer.parseInt(c.getCustomData(253, "day"));
	}

	public boolean getIsComplete() {
		return c.getCustomData(253, "complete").equals("1");
	}

	public int getGoldenPassCount() {
		return Integer.parseInt(c.getCustomData(253, "passCount"));
	}

	public int getCurrentMaxDay() {
		return Integer.parseInt(c.getCustomData(253, "cMaxDay"));
	}

	public int getBeforeMaxDay() {
		return Integer.parseInt(c.getCustomData(253, "bMaxDay"));
	}

	public byte[] encodeAttendanceData() {
		return CWvsContext.InfoPacket.updateClientInfoQuest(252,
				"count=" + c.getCustomData(252, "count") + ";T=" + getLastAttendenceTime());
	}

	public byte[] encodeChariotData() {
		return CWvsContext.InfoPacket.updateClientInfoQuest(253,
				"complete=" + getIsComplete() + ";day=" + getCurrentDay() + ";passCount=" + getGoldenPassCount()
						+ ";bMaxDay=" + getBeforeMaxDay() + ";lastDate=" + c.getCustomData(253, "lastDate")
						+ ";cMaxDay=" + getCurrentMaxDay());
	}

	public void refreshChariotInfo() {
		c.getSession().writeAndFlush(encodeAttendanceData());
		c.getSession().writeAndFlush(encodeChariotData());
	}

	public void setComplete(boolean b) {
		c.setCustomData(253, "complete", b ? "1" : "0");
	}

	public void setLastAttendenceTime(Long T) {
		c.setCustomData(252, "T", T.toString());
	}

	public void setCurrentDay(Integer Day) {
		c.setCustomData(253, "day", Day.toString());
	}

	public void setGoldenPassCount(Integer passCount) {
		c.setCustomData(253, "passCount", passCount.toString());
	}

	public void setCurrentMaxDay(Integer cMaxDay) {
		c.setCustomData(253, "cMaxDay", cMaxDay.toString());
	}

	public void setBeforeMaxDay(Integer bMaxDay) {
		c.setCustomData(253, "bMaxDay", bMaxDay.toString());
	}
}
