package handling.channel.handler;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import client.MapleClient;
import tools.FileoutputUtil;
import tools.packet.BossPacket;

//文件路径：handling/channel/handler/BossUIHandler.java 
public class BossUIHandler {
	// NPCID与BOSS编号的静态映射表（线程安全）
	private static final Map<Integer, Integer> NPC_BOSS_MAP = Collections.synchronizedMap(new HashMap<>());

	public static Object getRegisteredCount() {

		return NPC_BOSS_MAP.size();
	}

}