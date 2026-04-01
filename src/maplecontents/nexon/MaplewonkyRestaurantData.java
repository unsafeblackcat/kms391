package maplecontents.nexon;

import client.ChariotData;
import client.MapleClient;
import constants.GameConstants;
import tools.packet.CWvsContext;

public class MaplewonkyRestaurantData {
	private static MapleClient c;

	public MaplewonkyRestaurantData(MapleClient client) {
		this.c = client;

		if (c.getCustomData(326, "count") == null) {
			c.setCustomData(326, "count", "0");
		}
		if (c.getCustomData(252, "T") == null) {
			c.setCustomData(252, "T", GameConstants.getCurrentFullDate());
		}

		if (c.getCustomData(325, "day") == null) {
			c.setCustomData(325, "day", "0");
		}

		if (c.getCustomData(325, "complete") == null) {
			c.setCustomData(325, "complete", "0");
		}

		if (c.getCustomData(325, "bMaxDay") == null) {
			c.setCustomData(325, "bMaxDay", "135");
		}

		if (c.getCustomData(325, "cMaxDay") == null) {
			c.setCustomData(325, "cMaxDay", "135");
		}

		if (c.getCustomData(325, "lastDate") == null) {
			c.setCustomData(325, "lastDate", "21/04/09");
		}

		if (c.getCustomData(325, "passCount") == null) {
			c.setCustomData(325, "passCount", "0");
		}
	}

	public static boolean getIsComplete() {
		return c.getCustomData(325, "complete").equals("1");
	}

	public static int getCurrentDay() {
		return Integer.parseInt(c.getCustomData(325, "day"));
	}

	public static int getGoldenPassCount() {
		return Integer.parseInt(c.getCustomData(325, "passCount"));
	}

	public static int getBeforeMaxDay() {
		return Integer.parseInt(c.getCustomData(325, "bMaxDay"));
	}

	public static int getCurrentMaxDay() {
		return Integer.parseInt(c.getCustomData(325, "cMaxDay"));
	}

	public static byte[] encodeChariotData() {
		return CWvsContext.InfoPacket.updateClientInfoQuest(325,
				"complete=" + getIsComplete() + ";day=" + getCurrentDay() + ";passCount=" + getGoldenPassCount()
						+ ";bMaxDay=" + getBeforeMaxDay() + ";lastDate=" + c.getCustomData(325, "lastDate")
						+ ";cMaxDay=" + getCurrentMaxDay());
	}

	public static byte[] encodeAttendanceData() {
		return CWvsContext.InfoPacket.updateClientInfoQuest(326, "count=" + c.getCustomData(326, "count"));
	}

	public void refreshChariotInfo() {
		c.getSession().writeAndFlush(encodeAttendanceData());
		c.getSession().writeAndFlush(encodeChariotData());
	}

	public static int getwonkyRestaurantAmount() {
		if (c.getPlayer().getV("wonkyRestaurant_amount") == null) {
			c.getPlayer().addKV("wonkyRestaurant_amount", String.valueOf(0));
		}
		return Integer.parseInt(String.valueOf(c.getPlayer().getV("wonkyRestaurant_amount")));
	}

	public static void addwonkyRestaurantAmount(int amt) {
		c.getPlayer().addKV("wonkyRestaurant_amount", String.valueOf(getwonkyRestaurantAmount() + amt));
	}

}
