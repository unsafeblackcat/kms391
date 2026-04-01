package handling.channel.handler;

import client.MapleCharacter;
import client.MapleClient;
import client.inventory.Item;
import client.inventory.MapleInventoryType;
import client.messages.CommandProcessor;
import constants.ServerConstants;
import constants.programs.AdminTool;
import handling.RecvPacketOpcode;
import handling.channel.ChannelServer;
import handling.world.MapleMessenger;
import handling.world.MapleMessengerCharacter;
import handling.world.World;
import log.DBLogger;
import log.LogType;
import server.MapleItemInformationProvider;
import tools.FileoutputUtil;
import tools.data.LittleEndianAccessor;
import tools.packet.CField;
import tools.packet.CWvsContext;

import java.util.ArrayList;
import java.util.List;

public class ChatHandler {

	public static final void GeneralChat(final String text, final byte unk, final MapleClient c,
			final MapleCharacter chr, final LittleEndianAccessor slea, final RecvPacketOpcode recv) {
		boolean check = false;

		if (text.length() > 0 && chr != null && chr.getMap() != null
				&& !CommandProcessor.processCommand(c, text, ServerConstants.CommandType.NORMAL)) {
			if (!chr.isIntern() && text.length() >= 80) {
				return;
			}
			Item item = null;
			if (recv == RecvPacketOpcode.GENERAL_CHAT_ITEM && unk != 11) {
				slea.readInt();
				final byte invType = (byte) slea.readInt();
				final short pos = (short) slea.readInt();
				item = c.getPlayer().getInventory(MapleInventoryType.getByType((byte) ((pos > 0) ? invType : -1)))
						.getItem(pos);
			}
			if (c.getPlayer().getV("chatReq") != null) {
				int i = Integer.parseInt(c.getPlayer().getV("chatReq")) + 1;
				c.getPlayer().addKV("chatReq", "" + i);
			} else {
				c.getPlayer().addKV("chatReq", "1");
			}
			StringBuilder sb = new StringBuilder();
			InventoryHandler.addMedalString(c.getPlayer(), sb);
			sb.append(c.getPlayer().getName());
			sb.append(" : ");
			if (unk != 11) {
				sb.append(text.substring(1));
			}

			int type = -1;
			int emoticon = -1;

			if (unk == 11) {
				type = slea.readInt();
				emoticon = slea.readInt();
			}
			if (c.getChatBlockedTime() == 0L) {
				if (chr.isHidden()) {
					if (chr.isIntern() && !chr.isSuperGM() && unk == 0) {
						chr.getMap().broadcastGMMessage(chr,
								CField.getChatText(chr, text, false, 1, item, type, emoticon), true);
						if (unk == 0) {
							World.Broadcast.broadcastSmega(CWvsContext.HyperMegaPhone(sb.toString(),
									c.getPlayer().getName(), sb.toString(), c.getChannel(), true, null));
						}
					} else {
						chr.getMap().broadcastGMMessage(chr,
								CField.getChatText(chr, text, c.getPlayer().isSuperGM(), unk, item, type, emoticon),
								true);
					}
				} else if (chr.isIntern() && !chr.isSuperGM() && unk == 0) {
					if (unk == 0 || text.startsWith("~")) {
						if (chr.getHgrade() >= 1) {
							if (item != null) {
								if (chr.getMeso() >= 5000000L) {
									chr.gainMeso(-5000000L, false);
									World.Broadcast.broadcastSmega(CWvsContext.HyperMegaPhone(sb.toString(),
											c.getPlayer().getName(), sb.toString(), c.getChannel(), true, item));
								} else {
									chr.dropMessage(1, "需要500萬.");
								}
							} else {
								AdminTool.addMessage(1, "[" + chr.getClient().getChannel() + "채널] " + chr.getName()
										+ " : " + text.replaceAll("~", ""));
								FileoutputUtil.log(FileoutputUtil.全體聊天日誌, "[전체] [" + chr.getClient().getChannel()
										+ "채널] " + chr.getName() + " : " + text.replaceAll("~", ""));
								World.Broadcast.broadcastSmega(CField.getGameMessage(24, sb.toString()));
							}
						} else if (item != null) {
							if (chr.getMeso() >= 5000000L) {
								chr.gainMeso(-5000000L, false);
								World.Broadcast.broadcastSmega(CWvsContext.HyperMegaPhone(sb.toString(),
										c.getPlayer().getName(), sb.toString(), c.getChannel(), true, item));
								AdminTool.addMessage(1,
										"[" + chr.getClient().getChannel() + "채널] " + chr.getName() + " : ["
												+ MapleItemInformationProvider.getInstance().getName(item.getItemId())
												+ "]" + text.replaceAll("~", ""));
							} else {
								chr.dropMessage(1, "需要500萬.");
							}
						} else {
							World.Broadcast.broadcastSmega(CField.getGameMessage(18, sb.toString()));
						}
					} else {
						AdminTool.addMessage(0, "[" + chr.getClient().getChannel() + "채널] " + chr.getName() + " : "
								+ text.replaceAll("~", ""));
						FileoutputUtil.log(FileoutputUtil.一般聊天日誌,
								"[일반] [" + chr.getClient().getChannel() + "채널] " + chr.getName() + " : " + text);
						chr.getMap().broadcastMessage(CField.getChatText(chr, text, false, 1, item, type, emoticon),
								c.getPlayer().getTruePosition());
					}
				} else if (text.startsWith("~")) {
					if (chr.getHgrade() >= 1) {
						if (item != null) {
							if (chr.getMeso() >= 5000000L) {
								chr.gainMeso(-5000000L, false);
								World.Broadcast.broadcastSmega(CWvsContext.HyperMegaPhone(sb.toString(),
										c.getPlayer().getName(), sb.toString(), c.getChannel(), true, item));
							} else {
								chr.dropMessage(1, "需要500萬.");
							}
						} else {
							World.Broadcast.broadcastSmega(CField.getGameMessage(24, sb.toString()));
						}
					} else if (item != null) {
						if (chr.getMeso() >= 5000000L) {
							chr.gainMeso(-5000000L, false);
							World.Broadcast.broadcastSmega(CWvsContext.HyperMegaPhone(sb.toString(),
									c.getPlayer().getName(), sb.toString(), c.getChannel(), true, item));
							AdminTool.addMessage(1,
									"[" + chr.getClient().getChannel() + "채널] " + chr.getName() + " : ["
											+ MapleItemInformationProvider.getInstance().getName(item.getItemId()) + "]"
											+ text.replaceAll("~", ""));
							FileoutputUtil.log(FileoutputUtil.全體聊天日誌, "[전체] [" + chr.getClient().getChannel() + "채널] "
									+ chr.getName() + " : " + text.replaceAll("~", ""));
						} else {
							chr.dropMessage(1, "需要500萬.");
						}
					} else {
						World.Broadcast.broadcastSmega(CWvsContext.HyperMegaPhone(sb.toString(),
								c.getPlayer().getName(), sb.toString(), c.getChannel(), true, null));
						AdminTool.addMessage(1, "[" + chr.getClient().getChannel() + "채널] " + chr.getName() + " : "
								+ text.replaceAll("~", ""));
						FileoutputUtil.log(FileoutputUtil.全體聊天日誌, "[전체] [" + chr.getClient().getChannel() + "채널] "
								+ chr.getName() + " : " + text.replaceAll("~", ""));
					}
				} else {
					AdminTool.addMessage(0, "[" + chr.getClient().getChannel() + "채널] " + chr.getName() + " : "
							+ text.replaceAll("~", ""));
					FileoutputUtil.log(FileoutputUtil.一般聊天日誌,
							"[일반] [" + chr.getClient().getChannel() + "채널] " + chr.getName() + " : " + text);
					chr.getMap().broadcastMessage(
							CField.getChatText(chr, text, c.getPlayer().isSuperGM(), unk, item, type, emoticon),
							c.getPlayer().getTruePosition());
				}
				DBLogger.getInstance().logChat(LogType.Chat.General, c.getPlayer().getId(), c.getPlayer().getName(),
						sb.length() > 0 ? sb.toString() : text, c.getPlayer().getMap().getStreetName() + " - "
								+ c.getPlayer().getMap().getMapName() + " (" + c.getPlayer().getMap().getId() + ")");
			} else {
				c.getSession().writeAndFlush((Object) CWvsContext.serverNotice(6, "", "대화 금지 상태이므로 채팅이 불가능합니다."));
			}
		}
	}

	public static final void Others(final LittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr,
			final RecvPacketOpcode recv) {
		final int type = slea.readByte();
		final byte numRecipients = slea.readByte();
		slea.skip(1);
		if (numRecipients <= 0) {
			return;
		}
		final int[] recipients = new int[numRecipients];
		for (byte i = 0; i < numRecipients; ++i) {
			recipients[i] = slea.readInt();
		}
		final String chattext = slea.readMapleAsciiString();
		if (chr == null || !chr.getCanTalk()) {
			c.getSession().writeAndFlush(
					(Object) CWvsContext.serverNotice(6, "", "You have been muted and are therefore unable to talk."));
			return;
		}
		if (c.isMonitored()) {
			String chattype = "Unknown";
			switch (type) {
			case 0: {
				chattype = "Buddy";
				break;
			}
			case 1: {
				chattype = "Party";
				break;
			}
			case 2: {
				chattype = "Guild";
				break;
			}
			case 3: {
				chattype = "Alliance";
				break;
			}
			case 4: {
				chattype = "Expedition";
				break;
			}
			}
		}
		if (chattext.equals("Unknown")
				|| CommandProcessor.processCommand(c, chattext, ServerConstants.CommandType.NORMAL)) {
			return;
		}
		switch (type) {
		case 0: {
			World.Buddy.buddyChat(recipients, chr, chattext, slea, recv);
			AdminTool.addMessage(2, "[" + c.getChannel() + "채널] " + c.getPlayer().getName() + " : " + chattext);
			FileoutputUtil.log(FileoutputUtil.好友聊天日誌,
					"[친구] [" + c.getChannel() + "채널] " + c.getPlayer().getName() + " : " + chattext);
			break;
		}
		case 1: {
			if (chr.getParty() == null) {
				break;
			}
			World.Party.partyChat(chr, chattext, slea, recv);
			AdminTool.addMessage(4, "[" + c.getChannel() + "채널] " + c.getPlayer().getName() + " : " + chattext);
			FileoutputUtil.log(FileoutputUtil.組隊聊天日誌, "[파티] [" + c.getChannel() + "채널] 파티번호 : "
					+ c.getPlayer().getParty().getId() + " | " + c.getPlayer().getName() + " : " + chattext);
			break;
		}
		case 2: {
			if (chr.getGuildId() <= 0) {
				break;
			}
			World.Guild.guildChat(chr, chattext, slea, recv);
			AdminTool.addMessage(3, "[" + c.getChannel() + "채널] " + c.getPlayer().getName() + " : " + chattext);
			FileoutputUtil.log(FileoutputUtil.公會聊天日誌, "[길드] [" + c.getChannel() + "채널] 길드 : "
					+ c.getPlayer().getGuildName() + " | " + c.getPlayer().getName() + " : " + chattext);
			break;
		}
		case 3: {
			if (chr.getGuildId() <= 0) {
				break;
			}
			World.Alliance.allianceChat(chr, chattext, slea, recv);
			AdminTool.addMessage(3, "[" + c.getChannel() + "채널] " + c.getPlayer().getName() + " : " + chattext);
			FileoutputUtil.log(FileoutputUtil.聯盟聊天日誌, "[연합] [" + c.getChannel() + "채널] 길드 : "
					+ c.getPlayer().getGuildName() + " | " + c.getPlayer().getName() + " : " + chattext);
			break;
		}
		}
	}

	public static void Messenger(LittleEndianAccessor slea, MapleClient c) {
		String targeted;
		MapleCharacter target;
		MapleMessenger messenger = c.getPlayer().getMessenger();

		switch (slea.readByte()) {
		case 0:
			if (messenger == null) {
				byte available = slea.readByte();
				int messengerid = slea.readInt();
				if (messengerid == 0) {
					c.getPlayer()
							.setMessenger(World.Messenger.createMessenger(new MapleMessengerCharacter(c.getPlayer())));
					break;
				}
				messenger = World.Messenger.getMessenger(messengerid);
				if (messenger != null) {
					int position = messenger.getLowestPosition();
					if (messenger.getMembers().size() < available) {
						if (position > -1 && position < 7) {
							c.getPlayer().setMessenger(messenger);
							World.Messenger.joinMessenger(messenger.getId(), new MapleMessengerCharacter(c.getPlayer()),
									c.getPlayer().getName(), c.getChannel());
						}
						break;
					}
					c.getPlayer().dropMessage(5, "이미 해당 메신저는 최대 인원 입니다.");
				}
				break;
			}
		case 2:
			if (messenger != null) {
				MapleMessengerCharacter messengerplayer = new MapleMessengerCharacter(c.getPlayer());
				World.Messenger.leaveMessenger(messenger.getId(), messengerplayer);
				c.getPlayer().setMessenger(null);
			}
			break;
		case 3:
			if (messenger != null) {
				int position = messenger.getLowestPosition();
				if (position <= -1 || position >= 7) {
					return;
				}
				String input = slea.readMapleAsciiString();
				MapleCharacter mapleCharacter = c.getChannelServer().getPlayerStorage().getCharacterByName(input);

				if (mapleCharacter != null) {
					if (mapleCharacter.getMessenger() == null) {
						if (!mapleCharacter.isIntern() || c.getPlayer().isIntern()) {
							c.getSession().writeAndFlush(CField.messengerNote(input, 4, 1));
							mapleCharacter.getClient().getSession()
									.writeAndFlush(CField.messengerInvite(c.getPlayer().getName(), messenger.getId()));
							break;
						}
						c.getSession().writeAndFlush(CField.messengerNote(input, 4, 0));
						break;
					}
					c.getSession().writeAndFlush(CField.messengerChat(c.getPlayer().getName(),
							" : " + mapleCharacter.getName() + " is already using Maple Messenger."));
					break;
				}
				if (World.isConnected(input)) {
					World.Messenger.messengerInvite(c.getPlayer().getName(), messenger.getId(), input, c.getChannel(),
							c.getPlayer().isIntern());
					break;
				}
				c.getSession().writeAndFlush(CField.messengerNote(input, 4, 0));
			}
			break;
		case 5:
			targeted = slea.readMapleAsciiString();
			target = c.getChannelServer().getPlayerStorage().getCharacterByName(targeted);
			if (target != null) {
				if (target.getMessenger() != null) {
					target.getClient().getSession().writeAndFlush(CField.messengerNote(c.getPlayer().getName(), 5, 0));
				}
				break;
			}
			if (!c.getPlayer().isIntern()) {
				World.Messenger.declineChat(targeted, c.getPlayer().getName());
			}
			break;
		case 6:
			if (messenger != null) {
				String charname = slea.readMapleAsciiString();
				String text = slea.readMapleAsciiString();
				if (!c.getPlayer().isIntern() && text.length() >= 1000) {
					return;
				}
				String chattext = charname + "" + text;
				World.Messenger.messengerChat(messenger.getId(), charname, text, c.getPlayer().getName());
				if (!messenger.isMonitored() || chattext.length() > c.getPlayer().getName().length() + 3)
					;
				AdminTool.addMessage(5, "[" + c.getChannel() + "채널] " + c.getPlayer().getName() + " : " + text);
				FileoutputUtil.log(FileoutputUtil.即時通聊天日誌, "[메신저] [" + c.getChannel() + "채널] 채팅방번호 : "
						+ messenger.getId() + " | " + c.getPlayer().getName() + " : " + text);
			}
			break;
		case 9:
			if (messenger != null) {
				short like = slea.readShort();
				String charname = slea.readMapleAsciiString();
				MapleCharacter character = c.getChannelServer().getPlayerStorage().getCharacterByName(charname);
				c.getSession().writeAndFlush(CField.messengerCharInfo(character));
			}
			break;
		case 10:
			if (messenger != null) {
				slea.readByte();
				String charname = slea.readMapleAsciiString();
				String str1 = slea.readMapleAsciiString();
			}
			break;
		case 12:
			if (messenger != null) {
				String charname = slea.readMapleAsciiString();
				MapleCharacter character = c.getChannelServer().getPlayerStorage().getCharacterByName(charname);
				c.getSession().writeAndFlush(CField.messengerCharInfo(character));
			}
			break;
		case 15:
			if (messenger != null) {
				String charname = slea.readMapleAsciiString();
				String text = slea.readMapleAsciiString();
				slea.readByte();

				if (!c.getPlayer().isIntern() && text.length() >= 1000) {
					return;
				}
				String chattext = charname + "" + text;
				World.Messenger.messengerWhisperChat(messenger.getId(), charname, text, c.getPlayer().getName());

				if (!messenger.isMonitored() || chattext.length() > c.getPlayer().getName().length() + 3)
					;
			}
			break;
		}
	}

	public static final void Whisper_Find(final LittleEndianAccessor slea, final MapleClient c,
			final RecvPacketOpcode recv) {
		final byte mode = slea.readByte();
		slea.readInt();
		switch (mode) {
		case 34: {
			final String recipient = slea.readMapleAsciiString();
			MapleCharacter player = null;
			for (final ChannelServer cserv : ChannelServer.getAllInstances()) {
				player = cserv.getPlayerStorage().getCharacterByName(recipient);
				if (player != null) {
					break;
				}
			}
			if (player != null) {
				c.getSession()
						.writeAndFlush((Object) CField.getWhisperReply(c.getPlayer().getName(), (byte) 34, (byte) 0));
				break;
			}
			break;
		}
		case 5:
		case 68: {
			final String recipient = slea.readMapleAsciiString();
			MapleCharacter player = c.getChannelServer().getPlayerStorage().getCharacterByName(recipient);
			if (player == null) {
				final int ch = World.Find.findChannel(recipient);
				if (ch > 0) {
					player = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(recipient);
					if (player == null) {
						break;
					}
					if (player != null) {
						if (!player.isIntern() || (c.getPlayer().isIntern() && player.isIntern())) {
							c.getSession()
									.writeAndFlush((Object) CField.getFindReply(recipient, (byte) ch, mode == 68));
						} else {
							c.getSession().writeAndFlush((Object) CField.getWhisperReply(recipient, (byte) 0));
						}
						return;
					}
				}
				if (ch == -10) {
					c.getSession().writeAndFlush((Object) CField.getFindReplyWithCS(recipient, mode == 68));
				} else {
					c.getSession().writeAndFlush((Object) CField.getWhisperReply(recipient, (byte) 0));
				}
				break;
			}
			if (!player.isIntern() || (c.getPlayer().isIntern() && player.isIntern())) {
				c.getSession().writeAndFlush(
						(Object) CField.getFindReplyWithMap(player.getName(), player.getMap().getId(), mode == 68));
				break;
			}
			c.getSession().writeAndFlush((Object) CField.getWhisperReply(recipient, (byte) 0));
			break;
		}
		case 6: { // 귓말 , 귓말 아이템
			if (c.getPlayer() == null || c.getPlayer().getMap() == null) {
				return;
			}
			if (!c.getPlayer().getCanTalk()) {
				c.getSession().writeAndFlush((Object) CWvsContext.serverNotice(6, "",
						"\ucc44\ud305 \uae08\uc9c0 \uc0c1\ud0dc\uc785\ub2c8\ub2e4."));
				return;
			}
			final String recipient = slea.readMapleAsciiString();
			Item item = null;
			if (recv == RecvPacketOpcode.WHISPERITEM) {
				final String item_text = slea.readMapleAsciiString();
			}
			final String text = slea.readMapleAsciiString();
			if (recv == RecvPacketOpcode.WHISPERITEM) {
				slea.readInt();
				final byte invType = (byte) slea.readInt();
				final byte pos = (byte) slea.readInt();
				item = c.getPlayer().getInventory(MapleInventoryType.getByType((byte) ((pos > 0) ? invType : -1)))
						.getItem(pos);
			}
			final int ch2 = World.Find.findChannel(recipient);
			if (ch2 <= 0) {
				c.getSession().writeAndFlush((Object) CField.getWhisperReply(recipient, (byte) 0));
				AdminTool.addMessage(6, "[" + c.getChannel() + "\ucc44\ub110] " + c.getPlayer().getName() + " > "
						+ recipient + " : " + text);
				FileoutputUtil.log(FileoutputUtil.密語聊天日誌, "[귓속말] [" + c.getChannel() + "채널] " + c.getPlayer().getName()
						+ " > " + recipient + " : " + text);
				break;
			}
			final MapleCharacter player2 = ChannelServer.getInstance(ch2).getPlayerStorage()
					.getCharacterByName(recipient);
			if (player2 == null) {
				break;
			}
			player2.getClient().getSession()
					.writeAndFlush((Object) CField.getWhisper(c.getPlayer().getName(), c.getChannel(), text, item, c));
			if (!c.getPlayer().isIntern() && player2.isIntern()) {
				c.getSession().writeAndFlush((Object) CField.getWhisperReply(recipient, (byte) 0));
			} else {
				c.getSession().writeAndFlush((Object) CField.getWhisperReply(recipient, (byte) 1));
			}
			AdminTool.addMessage(6, "[" + c.getChannel() + "\ucc44\ub110] " + c.getPlayer().getName() + " > "
					+ recipient + " : " + text);
			FileoutputUtil.log(FileoutputUtil.密語聊天日誌,
					"[귓속말] [" + c.getChannel() + "채널] " + c.getPlayer().getName() + " > " + recipient + " : " + text);
			break;
		}
		}
	}

	public static void Messengerserch(final LittleEndianAccessor slea, final MapleClient c) {
		final List<MapleCharacter> chrs = new ArrayList<MapleCharacter>();
		for (final MapleCharacter mapchr : c.getPlayer().getMap().getAllCharactersThreadsafe()) {
			if (mapchr.getId() != c.getPlayer().getId()) {
				chrs.add(mapchr);
			}
		}
		c.getSession().writeAndFlush((Object) CField.ChrlistMap(chrs));
	}
}