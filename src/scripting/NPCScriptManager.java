package scripting;

import client.MapleClient;
import server.life.MapleLifeFactory;
import tools.FileoutputUtil;
import tools.packet.BossPacket;
import tools.packet.CWvsContext;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import java.util.Map;
import java.util.WeakHashMap;

public class NPCScriptManager extends AbstractScriptManager {

	private final Map<MapleClient, NPCConversationManager> cms = new WeakHashMap<>();

	private static final NPCScriptManager instance = new NPCScriptManager();

	public static final NPCScriptManager getInstance() {
		return instance;
	}

	public final void start(MapleClient c, int npc) {
		start(c, npc, (String) null);
	}

	public final void start(MapleClient c, String script) {
		start(c, 0, script);
	}

	public final boolean UseScript(MapleClient c, int quest) {
		Invocable iv = null;
		iv = getInvocable("quest/" + quest + ".js", c, true);
		return (iv != null);
	}

	public final void startHairRoom(MapleClient c, int npc, String script, byte result, int slot, byte temp) {
		try {
			if (!this.cms.containsKey(c) && c.canClickNPC()) {
				Invocable iv;
				if (script == null) {
					iv = getInvocable("npc/" + npc + ".js", c, true);
				} else {
					iv = getInvocable("npc/" + script + ".js", c);
				}
				if (iv == null) {
					iv = getInvocable("npc/notcoded.js", c, true);
					if (iv == null) {
						dispose(c);
						return;
					}
				}
				ScriptEngine scriptengine = (ScriptEngine) iv;
				NPCConversationManager cm = new NPCConversationManager(c, npc, -1, (byte) -1, iv, script);
				this.cms.put(c, cm);
				scriptengine.put("cm", cm);
				c.getPlayer().setConversation(1);
				c.setClickedNPC();
				try {
					iv.invokeFunction("start",
							new Object[] { Byte.valueOf(result), Integer.valueOf(slot), Byte.valueOf(temp) });
				} catch (NoSuchMethodException noSuchMethodException) {

				}
			}
		} catch (Exception e) {
			System.err.println("執行NPC腳本時出錯，NPC ID : " + npc + "." + e);
			FileoutputUtil.log("Log_Script_Except.txt",
					"執行NPC腳本時出錯，NPC ID : " + npc + " / NPC 腳本 : " + script + " / " + e);
			dispose(c);
		} finally {
		}
	}

	public final void start(MapleClient c, int npc, String script, String method) {
		try {
			if (!this.cms.containsKey(c) && c.canClickNPC()) {
				Invocable iv;
				if (script == null) {
					iv = getInvocable("npc/" + npc + ".js", c, true);
				} else {
					iv = getInvocable("npc/" + script + ".js", c);
				}
				if (iv == null) {
					iv = getInvocable("npc/notcoded.js", c, true);
					if (iv == null) {
						dispose(c);
						return;
					}
				}
				ScriptEngine scriptengine = (ScriptEngine) iv;
				NPCConversationManager cm = new NPCConversationManager(c, npc, -1, (byte) -1, iv, script);
				this.cms.put(c, cm);
				scriptengine.put("cm", cm);
				c.getPlayer().setConversation(1);
				c.setClickedNPC();
				try {
					iv.invokeFunction(method, new Object[0]);
				} catch (NoSuchMethodException nsme) {
					iv.invokeFunction("action",
							new Object[] { Byte.valueOf((byte) 1), Byte.valueOf((byte) 0), Integer.valueOf(0) });
				}
			}
		} catch (Exception e) {
			System.err.println("執行NPC腳本時出錯，NPC ID : " + npc + "." + e);
			FileoutputUtil.log("Log_Script_Except.txt",
					"執行NPC腳本時出錯，NPC ID : " + npc + " / NPC 腳本 : " + script + " / " + e);
			dispose(c);
		} finally {
		}
	}

	public final void start(MapleClient c, int npc, String script) {
		try {
			if (!this.cms.containsKey(c) && c.canClickNPC()) {
				Invocable iv;
				if (script == null) {
					iv = getInvocable("npc/" + npc + ".js", c, true);
					FileoutputUtil.log(FileoutputUtil.NPC對話日誌,
							"[對話NPC] 帳號ID : " + c.getAccID() + " | " + c.getPlayer().getName() + "("
									+ c.getPlayer().getId() + ") 在地圖ID " + c.getPlayer().getMapId() + " 開啟脚本 "
									+ MapleLifeFactory.getNPC(npc).getName() + "(" + npc + ")");
					c.getPlayer().dropMessageGM(6, "OpenNPC(" + npc + ")");
				} else {
					iv = getInvocable("npc/" + script + ".js", c, true);
					FileoutputUtil.log(FileoutputUtil.NPC對話日誌,
							"[對話NPC] 帳號ID : " + c.getAccID() + " | " + c.getPlayer().getName() + "("
									+ c.getPlayer().getId() + ") 在地圖ID " + c.getPlayer().getMapId() + " 開啟腳本: " + script
									+ ")");
					c.getPlayer().dropMessageGM(6, "OpenNPC(" + script + ")");
				}
				if (iv == null) {
					iv = getInvocable("npc/notcoded.js", c, true);
					if (iv == null) {
						dispose(c);
						return;
					}
				}
				ScriptEngine scriptengine = (ScriptEngine) iv;
				NPCConversationManager cm = new NPCConversationManager(c, npc, -1, (byte) -1, iv, script);
				this.cms.put(c, cm);
				scriptengine.put("cm", cm);
				c.getPlayer().setConversation(1);
				c.setClickedNPC();
				try {
					iv.invokeFunction("start", new Object[0]);
				} catch (NoSuchMethodException nsme) {
					iv.invokeFunction("action",
							new Object[] { Byte.valueOf((byte) 1), Byte.valueOf((byte) 0), Integer.valueOf(0) });
				}
			}
		} catch (Exception e) {
			System.err.println("執行物品NPC腳本時出錯，NPC ID : " + npc + "." + e);
			FileoutputUtil.log("Log_Script_Except.txt", "執行物品NPC腳本時出錯，NPC ID : " + npc + "." + e);
			dispose(c);
		} finally {
		}
	}

	public final void startItem(MapleClient c, int npc, String script) {
		try {
			if (!this.cms.containsKey(c) && c.canClickNPC()) {
				Invocable iv = getInvocable("item/" + script + ".js", c);
				if (iv == null) {
					iv = getInvocable("item/notcoded.js", c);
					if (iv == null) {
						dispose(c);
						return;
					}
				}
				ScriptEngine scriptengine = (ScriptEngine) iv;
				NPCConversationManager cm = new NPCConversationManager(c, npc, -1, (byte) -2, iv, script);
				this.cms.put(c, cm);
				scriptengine.put("cm", cm);
				c.getPlayer().setConversation(1);
				c.setClickedNPC();
				try {
					iv.invokeFunction("start", new Object[0]);
				} catch (NoSuchMethodException nsme) {
					iv.invokeFunction("action",
							new Object[] { Byte.valueOf((byte) 1), Byte.valueOf((byte) 0), Integer.valueOf(0) });
				}
			}
		} catch (Exception e) {
			System.err.println("執行物品NPC腳本時出錯，NPC ID : " + npc + "." + e);
			FileoutputUtil.log("Log_Script_Except.txt", "執行物品NPC腳本時出錯，NPC ID : " + npc + "." + e);
			dispose(c);
		}
	}

	public final void action(MapleClient c, byte mode, byte type, int selection) {
		if (mode != -1) {
			NPCConversationManager cm = this.cms.get(c);
			if (cm == null || cm.getLastMsg() > -1) {
				return;
			}
			try {
				if (cm.pendingDisposal) {
					dispose(c);
				} else {
					c.setClickedNPC();
					cm.getIv().invokeFunction("action",
							new Object[] { Byte.valueOf(mode), Byte.valueOf(type), Integer.valueOf(selection) });
				}
			} catch (Exception e) {
				System.err.println("執行NPC腳本時出錯。NPC ID : " + cm.getNpc() + " / NPC 腳本 : " + cm.getScript() + " : " + e);
				dispose(c);
				FileoutputUtil.log("Log_Script_Except.txt",
						"執行NPC腳本時出錯，NPC ID : " + cm.getNpc() + "/" + cm.getScript() + " : " + e);
			}
		}
	}

	public final void zeroaction(MapleClient c, byte mode, byte type, int selection1, int selection2) {
		if (mode != -1) {
			NPCConversationManager cm = this.cms.get(c);
			if (cm == null || cm.getLastMsg() > -1) {
				return;
			}
			try {
				if (cm.pendingDisposal) {
					dispose(c);
				} else {
					c.setClickedNPC();
					cm.getIv().invokeFunction("zeroaction", new Object[] { Byte.valueOf(mode), Byte.valueOf(type),
							Integer.valueOf(selection1), Integer.valueOf(selection2) });
				}
			} catch (Exception e) {
				System.err.println("執行NPC腳本時出錯。NPC ID : " + cm.getNpc() + ":" + e);
				dispose(c);
				FileoutputUtil.log("Log_Script_Except.txt", "執行NPC腳本時出錯，NPC ID : " + cm.getNpc() + "." + e);
			}
		}
	}

	public final void startQuest(MapleClient c, int npc, int quest) {
		try {
			if (quest == 100796 || quest == 100199) {
				c.removeClickedNPC();
				getInstance().dispose(c);
				c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
			}
			if (!this.cms.containsKey(c)) {
				Invocable iv = getInvocable("quest/" + quest + ".js", c, true);
				if (iv == null) {
					dispose(c);
					return;
				}
				ScriptEngine scriptengine = (ScriptEngine) iv;
				NPCConversationManager cm = new NPCConversationManager(c, npc, quest, (byte) 0, iv, null);
				this.cms.put(c, cm);
				scriptengine.put("qm", cm);
				c.getPlayer().setConversation(1);
				c.setClickedNPC();
				FileoutputUtil.log(FileoutputUtil.NPC對話日誌,
						"[任務開啟] 帳號ID : " + c.getAccID() + " | " + c.getPlayer().getName() + "(" + c.getPlayer().getId()
								+ ") 在 " + c.getPlayer().getMapId() + " 地圖開啟任務: " + quest);

				System.out.println("NPCID  已啟動: " + npc + " 開始任務 " + quest);
				iv.invokeFunction("start",
						new Object[] { Byte.valueOf((byte) 1), Byte.valueOf((byte) 0), Integer.valueOf(0) });
			}
		} catch (Exception e) {
			System.err.println("執行任務腳本時出錯。(" + quest + ")..NPCID: " + npc + ":" + e);
			FileoutputUtil.log("Log_Script_Except.txt", "執行任務腳本時出錯。(" + quest + ")..NPCID: " + npc + ":" + e);
			dispose(c);
		}
	}

	public final void startQuest(MapleClient c, byte mode, byte type, int selection) {
		NPCConversationManager cm = this.cms.get(c);
		if (cm == null || cm.getLastMsg() > -1) {
			return;
		}
		try {
			if (cm.pendingDisposal) {
				dispose(c);
			} else {
				c.setClickedNPC();
				cm.getIv().invokeFunction("start",
						new Object[] { Byte.valueOf(mode), Byte.valueOf(type), Integer.valueOf(selection) });
			}
		} catch (Exception e) {
			System.err.println("執行任務腳本時出錯。(" + cm.getQuest() + ")...NPC: " + cm.getNpc() + ":" + e);
			FileoutputUtil.log("Log_Script_Except.txt",
					"執行任務腳本時出錯。(" + cm.getQuest() + ")..NPCID: " + cm.getNpc() + ":" + e);
			dispose(c);
		}
	}

	public final void endQuest(MapleClient c, int npc, int quest, boolean customEnd) {
		try {
			if (!this.cms.containsKey(c) && c.canClickNPC()) {
				Invocable iv = getInvocable("quest/" + quest + ".js", c, true);
				if (iv == null) {
					dispose(c);
					return;
				}
				ScriptEngine scriptengine = (ScriptEngine) iv;
				NPCConversationManager cm = new NPCConversationManager(c, npc, quest, (byte) 1, iv, null);
				this.cms.put(c, cm);
				scriptengine.put("qm", cm);
				c.getPlayer().setConversation(1);
				c.setClickedNPC();
				System.out.println("NPCID started: " + npc + " endquest " + quest);
				iv.invokeFunction("end",
						new Object[] { Byte.valueOf((byte) 1), Byte.valueOf((byte) 0), Integer.valueOf(0) });
			}
		} catch (Exception e) {
			System.err.println("執行任務腳本時出錯。(" + quest + ")..NPCID: " + npc + ":" + e);
			FileoutputUtil.log("Log_Script_Except.txt", "執行任務腳本時出錯。(" + quest + ")..NPCID: " + npc + ":" + e);
			dispose(c);
		}
	}

	public final void endQuest(MapleClient c, byte mode, byte type, int selection) {
		NPCConversationManager cm = this.cms.get(c);
		if (cm == null || cm.getLastMsg() > -1) {
			return;
		}
		try {
			if (cm.pendingDisposal) {
				dispose(c);
			} else {
				c.setClickedNPC();
				cm.getIv().invokeFunction("end",
						new Object[] { Byte.valueOf(mode), Byte.valueOf(type), Integer.valueOf(selection) });
			}
		} catch (Exception e) {
			System.err.println("執行任務腳本時出錯。(" + cm.getQuest() + ")...NPC: " + cm.getNpc() + ":" + e);
			FileoutputUtil.log("Log_Script_Except.txt",
					"執行任務腳本時出錯。(" + cm.getQuest() + ")..NPCID: " + cm.getNpc() + ":" + e);
			dispose(c);
		}
	}

	public final void dispose(MapleClient c) {
		NPCConversationManager npccm = this.cms.get(c);
		if (npccm != null) {
			this.cms.remove(c);
			if (npccm.getType() == -1) {
				c.removeScriptEngine("scripts/npc/notcoded.js");
				if (npccm.getScript() != null) {
					c.removeScriptEngine("scripts/npc/" + npccm.getScript() + ".js");
				} else {
					c.removeScriptEngine("scripts/npc/" + npccm.getNpc() + ".js");
				}
			} else if (npccm.getType() == -2) {
				c.removeScriptEngine("scripts/item/" + npccm.getScript() + ".js");
			} else {
				c.removeScriptEngine("scripts/quest/" + npccm.getQuest() + ".js");
			}
		}
		if (c.getPlayer() != null && c.getPlayer().getConversation() == 1) {
			c.getPlayer().setConversation(0);
			c.removeClickedNPC();
		}
	}

	public final NPCConversationManager getCM(MapleClient c) {
		return this.cms.get(c);
	}

	public void scriptClear() {
		this.cms.clear();
	}

	public static void debugBossUI(MapleClient c, int bossNum) {// 打開BOSSUI方法
		if (c == null || c.getPlayer() == null)
			return;
		if (c.getPlayer() == null)
			return;

		// 验证Boss编号范围（根据代码中的bossNumber范围）
		if (bossNum < 0 || bossNum > 31) {
			c.getPlayer().dropMessage(5, "无效的Boss编号（0-31）");
			return;
		}

		// 发送BOSSUI数据包
		c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
		c.getSession().writeAndFlush(BossPacket.openBossUI(bossNum));
	}

}
