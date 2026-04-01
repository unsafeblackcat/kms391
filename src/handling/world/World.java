
package handling.world;

import client.BuddyList;
import client.BuddylistEntry;
import client.MapleCharacter;
import client.MapleClient;
import client.inventory.Item;
import client.inventory.MapleInventoryType;
import database.DatabaseConnection;
import handling.RecvPacketOpcode;
import handling.auction.AuctionServer;
import handling.cashshop.CashShopServer;
import handling.channel.ChannelServer;
import handling.channel.PlayerStorage;
import handling.world.guild.MapleGuild;
import handling.world.guild.MapleGuildAlliance;
import handling.world.guild.MapleGuildCharacter;
import log.DBLogger;
import log.LogType;
import tools.CollectionUtil;
import tools.data.LittleEndianAccessor;
import tools.packet.CField;
import tools.packet.CWvsContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class World {

	public static void init() {
		Find.findChannel(0);
		Messenger.getMessenger(0);
		Party.getParty(0);
	}

	public static List<MapleCharacter> getAllCharacters() {
		final List<MapleCharacter> temp = new ArrayList<MapleCharacter>();
		for (final ChannelServer cs : ChannelServer.getAllInstances()) {
			for (final MapleCharacter chr : cs.getPlayerStorage().getAllCharacters().values()) {
				if (!temp.contains(chr)) {
					temp.add(chr);
				}
			}
		}
		for (final MapleCharacter chr2 : CashShopServer.getPlayerStorage().getAllCharacters().values()) {
			temp.add(chr2);
		}
		for (final MapleCharacter chr2 : AuctionServer.getPlayerStorage().getAllCharacters().values()) {
			temp.add(chr2);
		}
		return temp;
	}

	public static MapleCharacter getChar(final int id) {
		for (final ChannelServer cs : ChannelServer.getAllInstances()) {
			for (final MapleCharacter chr : cs.getPlayerStorage().getAllCharacters().values()) {
				if (chr.getId() == id) {
					return chr;
				}
			}
		}
		for (final MapleCharacter csplayer : CashShopServer.getPlayerStorage().getAllCharacters().values()) {
			if (csplayer.getId() == id) {
				return csplayer;
			}
		}
		for (final MapleCharacter csplayer : AuctionServer.getPlayerStorage().getAllCharacters().values()) {
			if (csplayer.getId() == id) {
				return csplayer;
			}
		}
		return null;
	}

	public static String getStatus() {
		final StringBuilder ret = new StringBuilder();
		int totalUsers = 0;
		for (final ChannelServer cs : ChannelServer.getAllInstances()) {
			ret.append("Channel ");
			ret.append(cs.getChannel());
			ret.append(": ");
			final int channelUsers = cs.getConnectedClients();
			totalUsers += channelUsers;
			ret.append(channelUsers);
			ret.append(" users\n");
		}
		ret.append("Total users online: ");
		ret.append(totalUsers);
		ret.append("\n");
		return ret.toString();
	}

	public static Map<Integer, Integer> getConnected() {
		final Map<Integer, Integer> ret = new HashMap<Integer, Integer>();
		int total = 0;
		for (final ChannelServer cs : ChannelServer.getAllInstances()) {
			final int curConnected = cs.getConnectedClients();
			ret.put(cs.getChannel(), curConnected);
			total += curConnected;
		}
		ret.put(0, total);
		return ret;
	}

	public static List<CheaterData> getCheaters() {
		final List<CheaterData> allCheaters = new ArrayList<CheaterData>();
		for (final ChannelServer cs : ChannelServer.getAllInstances()) {
			allCheaters.addAll(cs.getCheaters());
		}
		Collections.sort(allCheaters);
		return CollectionUtil.copyFirst(allCheaters, 20);
	}

	public static List<CheaterData> getReports() {
		final List<CheaterData> allCheaters = new ArrayList<CheaterData>();
		for (final ChannelServer cs : ChannelServer.getAllInstances()) {
			allCheaters.addAll(cs.getReports());
		}
		Collections.sort(allCheaters);
		return CollectionUtil.copyFirst(allCheaters, 20);
	}

	public static boolean isConnected(final String charName) {
		return Find.findChannel(charName) > 0;
	}

	public static void toggleMegaphoneMuteState() {
		for (final ChannelServer cs : ChannelServer.getAllInstances()) {
			cs.toggleMegaphoneMuteState();
		}
	}

	public static void ChannelChange_Data(final CharacterTransfer Data, final int characterid, final int toChannel) {
		getStorage(toChannel).registerPendingPlayer(Data, characterid);
	}

	public static void isCharacterListConnected(final String name, final List<String> charName) {
		for (final ChannelServer cs : ChannelServer.getAllInstances()) {
			for (final String c : charName) {
				if (cs.getPlayerStorage().getCharacterByName(c) != null) {
					cs.getPlayerStorage().deregisterPlayer(cs.getPlayerStorage().getCharacterByName(c));
				}
			}
		}
	}

	public static PlayerStorage getStorage(final int channel) {
		if (channel == -10) {
			return CashShopServer.getPlayerStorage();
		}
		if (channel == -20) {
			return AuctionServer.getPlayerStorage();
		}
		return ChannelServer.getInstance(channel).getPlayerStorage();
	}

	public static int getPendingCharacterSize() {
		int ret = 0;
		for (final ChannelServer cserv : ChannelServer.getAllInstances()) {
			ret += cserv.getPlayerStorage().pendingCharacterSize();
		}
		return ret;
	}

	public static boolean isChannelAvailable(final int ch) {
		return ChannelServer.getInstance(ch) != null && ChannelServer.getInstance(ch).getPlayerStorage() != null
				&& ChannelServer.getInstance(ch).getPlayerStorage().getConnectedClients() < ((ch == 1) ? 600 : 400);
	}

	public static class Party {

		private static Map<Integer, MapleParty> parties;
		private static final AtomicInteger runningPartyId;
		private static final AtomicInteger runningExpedId;

		public static void partyChat(final MapleCharacter chr, final String chattext, final LittleEndianAccessor slea,
				final RecvPacketOpcode recv) {
			partyChat(chr, chattext, 1, slea, recv);
		}

		public static void partyPacket(final int partyid, final byte[] packet, final MaplePartyCharacter exception) {
			final MapleParty party = getParty(partyid);
			if (party == null) {
				return;
			}
			for (final MaplePartyCharacter partychar : party.getMembers()) {
				final int ch = Find.findChannel(partychar.getName());
				if (ch > 0 && (exception == null || partychar.getId() != exception.getId())) {
					final MapleCharacter chr = ChannelServer.getInstance(ch).getPlayerStorage()
							.getCharacterByName(partychar.getName());
					if (chr == null) {
						continue;
					}
					chr.getClient().getSession().writeAndFlush((Object) packet);
				}
			}
		}

		public static void partyChat(final MapleCharacter player, final String chattext, final int mode,
				final LittleEndianAccessor slea, final RecvPacketOpcode recv) {
			final MapleParty party = getParty(player.getParty().getId());
			if (party == null) {
				return;
			}
			Item item = null;
			if (recv == RecvPacketOpcode.PARTYCHATITEM && player != null) {
				final byte invType = (byte) slea.readInt();
				final short pos = (short) slea.readInt();
				item = player.getInventory(MapleInventoryType.getByType((byte) ((pos > 0) ? invType : -1)))
						.getItem(pos);
			}
			for (final MaplePartyCharacter partychar : party.getMembers()) {
				final int ch = Find.findChannel(partychar.getName());
				if (ch > 0) {
					final MapleCharacter chr = ChannelServer.getInstance(ch).getPlayerStorage()
							.getCharacterByName(partychar.getName());
					if (chr == null || chr.getName().equalsIgnoreCase(player.getName())) {
						continue;
					}
					chr.getClient().getSession().writeAndFlush((Object) CField.multiChat(player, chattext, mode, item));
					if (chr.getClient().isMonitored()) {
					}
				}
			}
		}

		public static void partyMessage(final int partyid, final String chattext) {
			final MapleParty party = getParty(partyid);
			if (party == null) {
				return;
			}
			for (final MaplePartyCharacter partychar : party.getMembers()) {
				final int ch = Find.findChannel(partychar.getName());
				if (ch > 0) {
					final MapleCharacter chr = ChannelServer.getInstance(ch).getPlayerStorage()
							.getCharacterByName(partychar.getName());
					if (chr == null) {
						continue;
					}
					chr.dropMessage(5, chattext);
				}
			}
		}

		public static void updateParty(final int partyid, final PartyOperation operation,
				final MaplePartyCharacter target) {
			final MapleParty party = getParty(partyid);
			if (party == null) {
				return;
			}
			final int oldSize = party.getMembers().size();
			final int oldInd = -1;
			switch (operation) {
			case JOIN: {
				party.addMember(target);
				break;
			}
			case EXPEL:
			case LEAVE: {
				party.removeMember(target);
				break;
			}
			case DISBAND: {
				disbandParty(partyid);
				break;
			}
			case SILENT_UPDATE:
			case LOG_ONOFF: {
				party.updateMember(target);
				break;
			}
			case CHANGE_LEADER:
			case CHANGE_LEADER_DC: {
				party.setLeader(target);
				break;
			}
			default: {
				throw new RuntimeException("Unhandeled updateParty operation " + operation.name());
			}
			}
			if (operation == PartyOperation.LEAVE || operation == PartyOperation.EXPEL) {
				final int chz = Find.findChannel(target.getName());
				if (chz > 0) {
					final MapleCharacter chr = World.getStorage(chz).getCharacterByName(target.getName());
					if (chr != null) {
						chr.setParty(null);
						chr.getClient().getSession().writeAndFlush((Object) CWvsContext.PartyPacket
								.updateParty(chr.getClient().getChannel(), party, operation, target, chr));
					}
				}
				if (target.getId() == party.getLeader().getId() && party.getMembers().size() > 0) {
					MaplePartyCharacter lchr = null;
					for (final MaplePartyCharacter pchr : party.getMembers()) {
						if (pchr != null && (lchr == null || lchr.getLevel() < pchr.getLevel())) {
							lchr = pchr;
						}
					}
					if (lchr != null) {
						updateParty(partyid, PartyOperation.CHANGE_LEADER_DC, lchr);
					}
				}
			}
			if (party.getMembers().size() <= 0) {
				disbandParty(partyid);
			}
			for (final MaplePartyCharacter partychar : party.getMembers()) {
				if (partychar == null) {
					continue;
				}
				final int ch = Find.findChannel(partychar.getName());
				if (ch <= 0) {
					continue;
				}
				final MapleCharacter chr2 = World.getStorage(ch).getCharacterByName(partychar.getName());
				if (chr2 == null) {
					continue;
				}
				if (operation == PartyOperation.DISBAND) {
					chr2.setParty(null);
				} else {
					chr2.setParty(party);
				}
				chr2.getClient().getSession().writeAndFlush((Object) CWvsContext.PartyPacket
						.updateParty(chr2.getClient().getChannel(), party, operation, target, chr2));
				chr2.getStat().recalcLocalStats(chr2);
			}
		}

		public static MapleParty createParty(final MaplePartyCharacter chrfor) {
			final MapleParty party = new MapleParty(Party.runningPartyId.getAndIncrement(), chrfor);
			Party.parties.put(party.getId(), party);
			return party;
		}

		public static MapleParty getParty(final int partyid) {
			return Party.parties.get(partyid);
		}

		public static MapleParty disbandParty(final int partyid) {
			final MapleParty ret = Party.parties.remove(partyid);
			if (ret == null) {
				return null;
			}
			ret.disband();
			return ret;
		}

		static {
			Party.parties = new HashMap<Integer, MapleParty>();
			runningPartyId = new AtomicInteger(1);
			runningExpedId = new AtomicInteger(1);
			Connection con = null;
			PreparedStatement ps = null;
			try {
				con = DatabaseConnection.getConnection();
				ps = con.prepareStatement("UPDATE characters SET party = -1, fatigue = 0");
				ps.executeUpdate();
				ps.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (con != null) {
						con.close();
					}
					if (ps != null) {
						ps.close();
					}
				} catch (SQLException se2) {
					se2.printStackTrace();
				}
			}
		}
	}

	public static class Buddy {

		public static void buddyChat(final int[] recipientCharacterIds, final MapleCharacter player,
				final String chattext, final LittleEndianAccessor slea, final RecvPacketOpcode recv) {
			String targets = "";
			Item item = null;
			if (recv == RecvPacketOpcode.PARTYCHATITEM && player != null) {
				final byte invType = (byte) slea.readInt();
				final byte pos = (byte) slea.readInt();
				item = player.getInventory(MapleInventoryType.getByType((byte) ((pos > 0) ? invType : -1)))
						.getItem(pos);
			}
			for (final int characterId : recipientCharacterIds) {
				final int ch = Find.findChannel(characterId);
				if (ch > 0) {
					final MapleCharacter chr = ChannelServer.getInstance(ch).getPlayerStorage()
							.getCharacterById(characterId);
					if (chr != null && player != null && chr.getBuddylist().containsVisible(player.getAccountID())) {
						targets = targets + chr.getName() + ", ";
						chr.getClient().getSession()
								.writeAndFlush((Object) CField.multiChat(player, chattext, 0, item));
						if (chr.getClient().isMonitored()) {
						}
					}
				}
			}
			DBLogger.getInstance().logChat(LogType.Chat.Buddy, player.getId(), player.getName(), chattext,
					"\uc218\uc2e0 : " + targets);
		}

		public static void updateBuddies(final String name, final int characterId, final int channel,
				final int[] buddies, final int accId, final boolean offline) {
			for (final int buddy : buddies) {
				final int ch = Find.findAccChannel(buddy);
				if (ch > 0) {
					final MapleClient c = ChannelServer.getInstance(ch).getPlayerStorage().getClientById(buddy);
					if (c != null && c.getPlayer() != null) {
						final BuddylistEntry ble = c.getPlayer().getBuddylist().get(accId);
						if (ble != null && ble.isVisible()) {
							int mcChannel;
							if (offline) {
								ble.setChannel(-1);
								mcChannel = -1;
							} else {
								ble.setChannel(channel);
								mcChannel = channel - 1;
							}
							ble.setName(name);
							ble.setCharacterId(characterId);
							c.getSession().writeAndFlush((Object) CWvsContext.BuddylistPacket
									.updateBuddyChannel(ble.getCharacterId(), accId, mcChannel, name));
						}
					}
				}
			}
		}

		public static void buddyChanged(final int cid, final int cidFrom, final String name, final int channel,
				final BuddyList.BuddyOperation operation, final int level, final int job, final int accId,
				final String memo) {
			final int ch = Find.findChannel(cid);
			if (ch > 0) {
				final MapleCharacter addChar = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterById(cid);
				if (addChar != null) {
					final BuddyList buddylist = addChar.getBuddylist();
					switch (operation) {
					case ADDED: {
						if (buddylist.contains(accId)) {
							buddylist.put(new BuddylistEntry(name, name, accId, cidFrom,
									"\uadf8\ub8f9 \ubbf8\uc9c0\uc815", channel, true, level, job, memo));
							addChar.getClient().getSession().writeAndFlush((Object) CWvsContext.BuddylistPacket
									.updateBuddyChannel(cidFrom, accId, channel, buddylist.get(accId).getName()));
							break;
						}
						break;
					}
					case DELETED: {
						if (buddylist.contains(accId)) {
							buddylist.put(
									new BuddylistEntry(name, name, accId, cidFrom, "\uadf8\ub8f9 \ubbf8\uc9c0\uc815",
											-1, buddylist.get(accId).isVisible(), level, job, memo));
							addChar.getClient().getSession().writeAndFlush((Object) CWvsContext.BuddylistPacket
									.updateBuddyChannel(cidFrom, accId, -1, buddylist.get(accId).getName()));
							break;
						}
						break;
					}
					}
				}
			}
		}

		public static BuddyList.BuddyAddResult requestBuddyAdd(final String addName, final int accid,
				final int channelFrom, final int cidFrom, final String nameFrom, final int levelFrom, final int jobFrom,
				final String groupName, final String memo) {
			for (final ChannelServer server : ChannelServer.getAllInstances()) {
				final MapleCharacter addChar = server.getPlayerStorage().getCharacterByName(addName);
				if (addChar != null) {
					final BuddyList buddylist = addChar.getBuddylist();
					if (buddylist.isFull()) {
						return BuddyList.BuddyAddResult.BUDDYLIST_FULL;
					}
					if (!buddylist.contains(accid)) {
						buddylist.addBuddyRequest(addChar.getClient(), accid, cidFrom, nameFrom, nameFrom, channelFrom,
								levelFrom, jobFrom, groupName, memo);
					} else {
						if (buddylist.containsVisible(accid)) {
							return BuddyList.BuddyAddResult.ALREADY_ON_LIST;
						}
						continue;
					}
				}
			}
			return BuddyList.BuddyAddResult.OK;
		}

		public static void loggedOn(final String name, final int characterId, final int channel, final int accId,
				final int[] buddies) {
			updateBuddies(name, characterId, channel, buddies, accId, false);
		}

		public static void loggedOff(final String name, final int characterId, final int channel, final int accId,
				final int[] buddies) {
			updateBuddies(name, characterId, channel, buddies, accId, true);
		}
	}

	public static class Messenger {

		private static Map<Integer, MapleMessenger> messengers;
		private static final AtomicInteger runningMessengerId;

		public static MapleMessenger createMessenger(final MapleMessengerCharacter chrfor) {
			final int messengerid = Messenger.runningMessengerId.getAndIncrement();
			final MapleMessenger messenger = new MapleMessenger(messengerid, chrfor);
			Messenger.messengers.put(messenger.getId(), messenger);
			return messenger;
		}

		public static void declineChat(final String target, final String namefrom) {
			final int ch = Find.findChannel(target);
			if (ch > 0) {
				final ChannelServer cs = ChannelServer.getInstance(ch);
				final MapleCharacter chr = cs.getPlayerStorage().getCharacterByName(target);
				if (chr != null) {
					final MapleMessenger messenger = chr.getMessenger();
					if (messenger != null) {
						chr.getClient().getSession().writeAndFlush((Object) CField.messengerNote(namefrom, 5, 0));
					}
				}
			}
		}

		public static MapleMessenger getMessenger(final int messengerid) {
			return Messenger.messengers.get(messengerid);
		}

		public static void leaveMessenger(final int messengerid, final MapleMessengerCharacter target) {
			final MapleMessenger messenger = getMessenger(messengerid);
			if (messenger == null) {
				throw new IllegalArgumentException("No messenger with the specified messengerid exists");
			}
			final int position = messenger.getPositionByName(target.getName());
			messenger.removeMember(target);
			for (final MapleMessengerCharacter mmc : messenger.getMembers()) {
				if (mmc != null) {
					final int ch = Find.findChannel(mmc.getId());
					if (ch <= 0) {
						continue;
					}
					final MapleCharacter chr = ChannelServer.getInstance(ch).getPlayerStorage()
							.getCharacterByName(mmc.getName());
					if (chr == null) {
						continue;
					}
					chr.getClient().getSession().writeAndFlush((Object) CField.removeMessengerPlayer(position));
				}
			}
		}

		public static void silentLeaveMessenger(final int messengerid, final MapleMessengerCharacter target) {
			final MapleMessenger messenger = getMessenger(messengerid);
			if (messenger == null) {
				throw new IllegalArgumentException("No messenger with the specified messengerid exists");
			}
			messenger.silentRemoveMember(target);
		}

		public static void silentJoinMessenger(final int messengerid, final MapleMessengerCharacter target) {
			final MapleMessenger messenger = getMessenger(messengerid);
			if (messenger == null) {
				throw new IllegalArgumentException("No messenger with the specified messengerid exists");
			}
			messenger.silentAddMember(target);
		}

		public static void updateMessenger(final int messengerid, final String namefrom, final int fromchannel) {
			final MapleMessenger messenger = getMessenger(messengerid);
			final int position = messenger.getPositionByName(namefrom);
			for (final MapleMessengerCharacter messengerchar : messenger.getMembers()) {
				if (messengerchar != null && !messengerchar.getName().equals(namefrom)) {
					final int ch = Find.findChannel(messengerchar.getName());
					if (ch <= 0) {
						continue;
					}
					final MapleCharacter chr = ChannelServer.getInstance(ch).getPlayerStorage()
							.getCharacterByName(messengerchar.getName());
					if (chr == null) {
						continue;
					}
					final MapleCharacter from = ChannelServer.getInstance(fromchannel).getPlayerStorage()
							.getCharacterByName(namefrom);
					chr.getClient().getSession().writeAndFlush(
							(Object) CField.updateMessengerPlayer(namefrom, from, position, fromchannel - 1));
				}
			}
		}

		public static void joinMessenger(final int messengerid, final MapleMessengerCharacter target, final String from,
				final int fromchannel) {
			final MapleMessenger messenger = getMessenger(messengerid);
			if (messenger == null) {
				throw new IllegalArgumentException("No messenger with the specified messengerid exists");
			}
			messenger.addMember(target);
			final int position = messenger.getPositionByName(target.getName());
			for (final MapleMessengerCharacter messengerchar : messenger.getMembers()) {
				if (messengerchar != null) {
					final int mposition = messenger.getPositionByName(messengerchar.getName());
					final int ch = Find.findChannel(messengerchar.getName());
					if (ch <= 0) {
						continue;
					}
					final MapleCharacter chr = ChannelServer.getInstance(ch).getPlayerStorage()
							.getCharacterByName(messengerchar.getName());
					if (chr == null) {
						continue;
					}
					if (!messengerchar.getName().equals(from)) {
						final MapleCharacter fromCh = ChannelServer.getInstance(fromchannel).getPlayerStorage()
								.getCharacterByName(from);
						if (fromCh == null) {
							continue;
						}
						chr.getClient().getSession().writeAndFlush(
								(Object) CField.addMessengerPlayer(from, fromCh, position, fromchannel - 1));
						fromCh.getClient().getSession().writeAndFlush((Object) CField.addMessengerPlayer(chr.getName(),
								chr, mposition, messengerchar.getChannel() - 1));
					} else {
						chr.getClient().getSession().writeAndFlush((Object) CField.joinMessenger(mposition));
					}
				}
			}
		}

		public static void messengerChat(final int messengerid, final String charname, final String text,
				final String namefrom) {
			final MapleMessenger messenger = getMessenger(messengerid);
			if (messenger == null) {
				throw new IllegalArgumentException("No messenger with the specified messengerid exists");
			}
			for (final MapleMessengerCharacter messengerchar : messenger.getMembers()) {
				if (messengerchar != null && !messengerchar.getName().equals(namefrom)) {
					final int ch = Find.findChannel(messengerchar.getName());
					if (ch <= 0) {
						continue;
					}
					final MapleCharacter chr = ChannelServer.getInstance(ch).getPlayerStorage()
							.getCharacterByName(messengerchar.getName());
					if (chr == null) {
						continue;
					}
					chr.getClient().getSession().writeAndFlush((Object) CField.messengerChat(charname, text));
				}
			}
		}

		public static void messengerWhisperChat(final int messengerid, final String charname, final String text,
				final String namefrom) {
			final MapleMessenger messenger = getMessenger(messengerid);
			if (messenger == null) {
				throw new IllegalArgumentException("No messenger with the specified messengerid exists");
			}
			for (final MapleMessengerCharacter messengerchar : messenger.getMembers()) {
				if (messengerchar != null && !messengerchar.getName().equals(namefrom)) {
					final int ch = Find.findChannel(messengerchar.getName());
					if (ch <= 0) {
						continue;
					}
					final MapleCharacter chr = ChannelServer.getInstance(ch).getPlayerStorage()
							.getCharacterByName(messengerchar.getName());
					if (chr == null) {
						continue;
					}
					chr.getClient().getSession().writeAndFlush((Object) CField.messengerWhisperChat(charname, text));
				}
			}
		}

		public static void messengerInvite(final String sender, final int messengerid, final String target,
				final int fromchannel, final boolean gm) {
			if (World.isConnected(target)) {
				final int ch = Find.findChannel(target);
				if (ch > 0) {
					final MapleCharacter from = ChannelServer.getInstance(fromchannel).getPlayerStorage()
							.getCharacterByName(sender);
					final MapleCharacter targeter = ChannelServer.getInstance(ch).getPlayerStorage()
							.getCharacterByName(target);
					if (targeter != null && targeter.getMessenger() == null) {
						if (!targeter.isIntern() || gm) {
							targeter.getClient().getSession()
									.writeAndFlush((Object) CField.messengerInvite(sender, messengerid));
							from.getClient().getSession().writeAndFlush((Object) CField.messengerNote(target, 4, 1));
						} else {
							from.getClient().getSession().writeAndFlush((Object) CField.messengerNote(target, 4, 0));
						}
					} else {
						from.getClient().getSession().writeAndFlush((Object) CField.messengerChat(sender,
								" : " + target + " is already using Maple Messenger"));
					}
				}
			}
		}

		static {
			Messenger.messengers = new HashMap<Integer, MapleMessenger>();
			(runningMessengerId = new AtomicInteger()).set(1);
		}
	}

	public static class Guild {

		private static final Map<Integer, MapleGuild> guilds = new ConcurrentHashMap<>();

		public static void addLoadedGuild(MapleGuild f) {
			if (f.isProper()) {
				guilds.put(Integer.valueOf(f.getId()), f);
			}
		}

		public static int createGuild(int leaderId, String name) {
			return MapleGuild.createGuild(leaderId, name);
		}

		public static Collection<MapleGuild> getGuilds() {
			return guilds.values();
		}

		public static MapleGuild getGuild(int id) {
			MapleGuild ret = guilds.get(Integer.valueOf(id));
			if (ret == null) {
				ret = new MapleGuild(id);
				if (ret == null || ret.getId() <= 0 || !ret.isProper()) {
					return null;
				}
				guilds.put(Integer.valueOf(id), ret);
			}
			return ret;
		}

		public static List<MapleGuild> getGuildsByName(String name, int i, byte type) {
			List<MapleGuild> ret = new ArrayList<>();
			for (MapleGuild g : guilds.values()) {
				if (g.getName().matches(name)) {
					ret.add(g);
				}
			}
			return ret;
		}

		public static List<MapleGuild> getGuildsByName(String name, boolean option, byte type) {
			List<MapleGuild> ret = new ArrayList<>();
			for (MapleGuild g : guilds.values()) {
				if (option) {
					if (type == 1) {
						if (g.getName().contains(name)) {
							ret.add(g);
						}
						continue;
					}
					if (type == 2) {
						if (g.getLeaderName().contains(name)) {
							ret.add(g);
						}
						continue;
					}
					if (type == 3) {
						if (g.getName().matches(name)) {
							ret.add(g);
						}
						if (g.getLeaderName().matches(name)) {
							ret.add(g);
						}
					}
					continue;
				}
				if (type == 1) {
					if (g.getName().contains(name)) {
						ret.add(g);
					}
					continue;
				}
				if (type == 2) {
					if (g.getLeaderName().contains(name)) {
						ret.add(g);
					}
					continue;
				}
				if (type == 3) {
					if (g.getName().contains(name)) {
						ret.add(g);
					}
					if (g.getLeaderName().contains(name)) {
						ret.add(g);
					}
				}
			}

			return ret;
		}

		public static List<MapleGuild> getGuilds(int minlevel, int maxlevel, int minsize, int maxsize, int minmlevel,
				int maxmlevel) {
			List<MapleGuild> ret = new ArrayList<>();
			for (MapleGuild guild : guilds.values()) {
				if (guild.getLevel() >= minlevel && guild.getLevel() <= maxlevel && guild.getMembers().size() >= minsize
						&& guild.getMembers().size() <= maxsize && guild.getLevel() >= minmlevel
						&& guild.getLevel() <= maxmlevel) {
					ret.add(guild);
				}
			}
			return ret;
		}

		public static MapleGuild getGuildByName(String guildName) {
			for (MapleGuild g : guilds.values()) {
				if (g.getName().equalsIgnoreCase(guildName)) {
					return g;
				}
			}
			return null;
		}

		public static MapleGuild getGuild(MapleCharacter mc) {
			return getGuild(mc.getGuildId());
		}

		public static void setGuildMemberOnline(MapleGuildCharacter mc, boolean bOnline, int channel) {
			MapleGuild g = getGuild(mc.getGuildId());

			if (g != null) {
				g.setOnline(mc, mc.getId(), bOnline, channel);
			}
		}

		public static void guildPacket(int gid, byte[] message) {
			MapleGuild g = getGuild(gid);

			if (g != null) {
				g.broadcast(message);
			}
		}

		public static int addGuildMember(MapleGuildCharacter mc) {
			MapleGuild g = getGuild(mc.getGuildId());

			if (g != null) {
				// return g.addGuildMember(mc,, g);
			}
			return 0;
		}

		public static void leaveGuild(MapleGuildCharacter mc) {
			MapleGuild g = getGuild(mc.getGuildId());

			if (g != null) {
				g.leaveGuild(mc);
			}
		}

		public static void guildChat(MapleCharacter chr, String msg, LittleEndianAccessor slea, RecvPacketOpcode recv) {
			MapleGuild g = getGuild(chr.getGuildId());
			if (g != null) {
				g.guildChat(chr, msg, slea, recv);
			}
		}

		public static void changeRank(int gid, int cid, int newRank) {
			MapleGuild g = getGuild(gid);

			if (g != null) {
				g.changeRank(cid, newRank);
			}
		}

		public static void expelMember(MapleGuildCharacter initiator, int cid) {
			MapleGuild g = getGuild(initiator.getGuildId());

			if (g != null) {
				g.expelMember(initiator, cid);
			}
		}

		public static void setGuildNotice(MapleCharacter chr, String notice) {
			MapleGuild g = getGuild(chr.getGuildId());

			if (g != null) {
				g.setGuildNotice(chr, notice);
			}
		}

		public static void setGuildLeader(int gid, int cid) {
			MapleGuild g = getGuild(gid);

			if (g != null) {
				g.changeGuildLeader(cid);
			}
		}

		public static int getSkillLevel(int gid, int sid) {
			MapleGuild g = getGuild(gid);
			if (g != null) {
				return g.getSkillLevel(sid);
			}
			return 0;
		}

		public static boolean purchaseSkill(int gid, int sid, String name, int cid) {
			MapleGuild g = getGuild(gid);
			if (g != null) {
				return g.purchaseSkill(sid, name, cid);
			}
			return false;
		}

		public static boolean removeSkill(int gid, int sid, String name) {
			MapleGuild g = getGuild(gid);
			if (g != null) {
				return g.removeSkill(sid, name);
			}
			return false;
		}

		public static boolean activateSkill(int gid, int sid, String name) {
			MapleGuild g = getGuild(gid);
			if (g != null) {
				return g.activateSkill(sid, name);
			}
			return false;
		}

		public static void memberLevelJobUpdate(MapleGuildCharacter mc) {
			MapleGuild g = getGuild(mc.getGuildId());
			if (g != null) {
				g.memberLevelJobUpdate(mc);
			}
		}

		public static void changeRankTitle(MapleCharacter chr, String[] ranks) {
			MapleGuild g = getGuild(chr.getGuildId());
			if (g != null) {
				g.changeRankTitle(chr, ranks);
			}
		}

		public static void changeRankRole(MapleCharacter chr, int[] roles) {
			MapleGuild g = getGuild(chr.getGuildId());
			if (g != null) {
				g.changeRankRole(chr, roles);
			}
		}

		public static void changeRankTitleRole(MapleCharacter chr, String ranks, int roles, byte type) {
			MapleGuild g = getGuild(chr.getGuildId());

			if (g != null) {
				g.changeRankTitleRole(chr, ranks, roles, type);
			}
		}

		public static void addRankTitleRole(MapleCharacter chr, String ranks, int roles, byte type) {
			MapleGuild g = getGuild(chr.getGuildId());

			if (g != null) {
				g.addRankTitleRole(chr, ranks, roles, type);
			}
		}

		public static void setGuildEmblem(MapleCharacter chr, short bg, byte bgcolor, short logo, byte logocolor) {
			MapleGuild g = getGuild(chr.getGuildId());
			if (g != null) {
				g.setGuildEmblem(chr, bg, bgcolor, logo, logocolor);
			}
		}

		public static void setGuildCustomEmblem(MapleCharacter chr, byte[] imgdata) {
			MapleGuild g = getGuild(chr.getGuildId());
			if (g != null) {
				g.setGuildCustomEmblem(chr, imgdata);
			}
		}

		public static void disbandGuild(int gid) {
			MapleGuild g = getGuild(gid);
			if (g != null) {
				g.disbandGuild();
				guilds.remove(Integer.valueOf(gid));
				g.broadcast(CWvsContext.GuildPacket.guildDisband(gid));
				g.broadcast(CWvsContext.GuildPacket.guildDisband2());
			}
		}

		public static void deleteGuildCharacter(int guildid, int charid) {
			MapleGuild g = getGuild(guildid);
			if (g != null) {
				MapleGuildCharacter mc = g.getMGC(charid);
				if (mc != null) {
					if (mc.getGuildRank() > 1) {

						g.leaveGuild(mc);
					} else {
						g.disbandGuild();
					}
				}
			}
		}

		public static boolean increaseGuildCapacity(int gid, boolean b) {
			MapleGuild g = getGuild(gid);
			if (g != null) {
				return g.increaseCapacity(b);
			}
			return false;
		}

		public static void gainContribution(int gid, int amount) {
			MapleGuild g = getGuild(gid);
			if (g != null) {
				g.gainGP(amount);
			}
		}

		public static void gainContribution(int gid, int amount, int cid) {
			MapleGuild g = getGuild(gid);
			if (g != null) {
				g.gainGP(amount, true, cid);
			}
		}

		public static int getGP(int gid) {
			MapleGuild g = getGuild(gid);
			if (g != null) {
				return g.getGP();
			}
			return 0;
		}

		public static int getInvitedId(int gid) {
			MapleGuild g = getGuild(gid);
			if (g != null) {
				return g.getInvitedId();
			}
			return 0;
		}

		public static void setInvitedId(int gid, int inviteid) {
			MapleGuild g = getGuild(gid);
			if (g != null) {
				g.setInvitedId(inviteid);
			}
		}

		public static int getGuildLeader(int guildName) {
			MapleGuild mga = getGuild(guildName);
			if (mga != null) {
				return mga.getLeaderId();
			}
			return 0;
		}

		public static int getGuildLeader(String guildName) {
			MapleGuild mga = getGuildByName(guildName);
			if (mga != null) {
				return mga.getLeaderId();
			}
			return 0;
		}

		public static void save() {
			System.out.println("Saving guilds...");
			for (MapleGuild a : guilds.values()) {
				a.writeToDB(false);
			}
		}

		public static void load() {
			System.out.println("加載公會...");
			Connection con = null;
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				con = DatabaseConnection.getConnection();
				ps = con.prepareStatement("SELECT * FROM guilds");

				rs = ps.executeQuery();

				while (rs.next()) {
					getGuild(rs.getInt("guildid"));
				}
				rs.close();
				ps.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			} finally {
				if (ps != null) {
					try {
						ps.close();
					} catch (SQLException ex) {
						Logger.getLogger(World.class.getName()).log(Level.SEVERE, (String) null, ex);
					}
				}
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException ex) {
						Logger.getLogger(World.class.getName()).log(Level.SEVERE, (String) null, ex);
					}
				}
				if (con != null) {
					try {
						con.close();
					} catch (SQLException e) {

						e.printStackTrace();
					}
				}
			}
		}

		public static void changeEmblem(MapleCharacter chr, int affectedPlayers, MapleGuild mgs) {
			Broadcast.sendGuildPacket(
					affectedPlayers, CWvsContext.GuildPacket.guildEmblemChange(chr, (short) mgs.getLogoBG(),
							(byte) mgs.getLogoBGColor(), (short) mgs.getLogo(), (byte) mgs.getLogoColor()),
					-1, chr.getGuildId());
			setGuildAndRank(affectedPlayers, -1, -1, -1, -1);
		}

		public static void setGuildAndRank(int cid, int guildid, int rank, int contribution, int alliancerank) {
			boolean bDifferentGuild;
			int ch = Find.findChannel(cid);
			if (ch == -1) {
				return;
			}

			MapleCharacter mc = World.getStorage(ch).getCharacterById(cid);
			if (mc == null) {
				return;
			}

			if (guildid == -1 && rank == -1) {
				bDifferentGuild = true;
			} else {
				bDifferentGuild = (guildid != mc.getGuildId());
				mc.setGuildId(guildid);
				mc.setGuildRank((byte) rank);
				mc.setGuildContribution(contribution);
				mc.setAllianceRank((byte) alliancerank);
				mc.saveGuildStatus();
			}

			if (bDifferentGuild && ch > 0) {
				mc.getMap().broadcastMessage(mc, CField.loadGuildIcon(mc), false);
			}
		}
	}

	public static class Broadcast {

		public static long chatDelay;

		public static void broadcastSmega(final byte[] message) {
			for (final ChannelServer cs : ChannelServer.getAllInstances()) {
				cs.broadcastSmega(message);
			}
		}

		public static void broadcastGMMessage(final byte[] message) {
			for (final ChannelServer cs : ChannelServer.getAllInstances()) {
				cs.broadcastGMMessage(message);
			}
		}

		public static void broadcastMessage(final byte[] message) {
			for (final ChannelServer cs : ChannelServer.getAllInstances()) {
				cs.broadcastMessage(message);
			}
		}

		public static void sendPacket(final List<Integer> targetIds, final byte[] packet, final int exception) {
			for (final int i : targetIds) {
				if (i == exception) {
					continue;
				}
				final int ch = Find.findChannel(i);
				if (ch < 0) {
					continue;
				}
				final MapleCharacter c = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterById(i);
				if (c == null) {
					continue;
				}
				c.getClient().getSession().writeAndFlush((Object) packet);
			}
		}

		public static void sendPacket(final int targetId, final byte[] packet) {
			final int ch = Find.findChannel(targetId);
			if (ch < 0) {
				return;
			}
			final MapleCharacter c = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterById(targetId);
			if (c != null) {
				c.getClient().getSession().writeAndFlush((Object) packet);
			}
		}

		public static void sendGuildPacket(final int targetIds, final byte[] packet, final int exception,
				final int guildid) {
			if (targetIds == exception) {
				return;
			}
			final int ch = Find.findChannel(targetIds);
			if (ch < 0) {
				return;
			}
			final MapleCharacter c = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterById(targetIds);
			if (c != null && c.getGuildId() == guildid) {
				c.getClient().getSession().writeAndFlush((Object) packet);
			}
		}

		static {
			Broadcast.chatDelay = 0L;
		}
	}

	public static class Find {

		private static Map<Integer, Integer> idToChannel;
		private static Map<String, Integer> nameToChannel;
		private static Map<Integer, Integer> accIdToChannel;

		public static void forceDeregister(final int id) {
			Find.idToChannel.remove(id);
		}

		public static void forceDeregister(final String id) {
			Find.nameToChannel.remove(id.toLowerCase());
		}

		public static void register(final int id, final int accId, final String name, final int channel) {
			Find.idToChannel.put(id, channel);
			Find.nameToChannel.put(name.toLowerCase(), channel);
			Find.accIdToChannel.put(accId, channel);
		}

		public static void forceAccDeregister(final int id) {
			Find.accIdToChannel.remove(id);
		}

		public static void forceDeregister(final int id, final int accId, final String name) {
			Find.idToChannel.remove(id);
			Find.nameToChannel.remove(name.toLowerCase());
			Find.accIdToChannel.remove(accId);
		}

		public static int findChannel(final int id) {
			final Integer ret = Find.idToChannel.get(id);
			if (ret == null) {
				return -1;
			}
			if (ret != -10 && ChannelServer.getInstance(ret) == null) {
				forceDeregister(id);
				return -1;
			}
			return ret;
		}

		public static int findChannel(final String st) {
			final Integer ret = Find.nameToChannel.get(st.toLowerCase());
			if (ret == null) {
				return -1;
			}
			if (ret != -10 && ChannelServer.getInstance(ret) == null) {
				forceDeregister(st);
				return -1;
			}
			return ret;
		}

		public static int findAccChannel(final int id) {
			final Integer ret = Find.accIdToChannel.get(id);
			if (ret == null) {
				return -1;
			}
			if (ret != -10 && ChannelServer.getInstance(ret) == null) {
				forceAccDeregister(id);
				return -1;
			}
			return ret;
		}

		public static AccountIdChannelPair[] multiBuddyFind(final BuddyList bl, final int charIdFrom,
				final int[] accIds) {
			final List<AccountIdChannelPair> foundsChars = new ArrayList<AccountIdChannelPair>(accIds.length);
			for (final int i : accIds) {
				final int ret = findAccChannel(i);
				if (ret > 0) {
					final MapleClient c = ChannelServer.getInstance(ret).getPlayerStorage().getClientById(i);
					if (bl.contains(i) && c != null) {
						final BuddylistEntry ble = bl.get(i);
						ble.setCharacterId(c.getPlayer().getId());
						ble.setName(c.getPlayer().getName());
					}
				}
				foundsChars.add(new AccountIdChannelPair(i, ret));
			}
			Collections.sort(foundsChars);
			return foundsChars.toArray(new AccountIdChannelPair[foundsChars.size()]);
		}

		static {
			Find.idToChannel = new ConcurrentHashMap<Integer, Integer>();
			Find.nameToChannel = new ConcurrentHashMap<String, Integer>();
			Find.accIdToChannel = new ConcurrentHashMap<Integer, Integer>();
		}
	}

	public static class Alliance {

		private static final Map<Integer, MapleGuildAlliance> alliances;

		public static MapleGuildAlliance getAlliance(final int allianceid) {
			MapleGuildAlliance ret = Alliance.alliances.get(allianceid);
			if (ret == null) {
				ret = new MapleGuildAlliance(allianceid);
				if (ret == null || ret.getId() <= 0) {
					return null;
				}
				Alliance.alliances.put(allianceid, ret);
			}
			return ret;
		}

		public static int getAllianceLeader(final int allianceid) {
			final MapleGuildAlliance mga = getAlliance(allianceid);
			if (mga != null) {
				return mga.getLeaderId();
			}
			return 0;
		}

		public static void updateAllianceRanks(final int allianceid, final String[] ranks) {
			final MapleGuildAlliance mga = getAlliance(allianceid);
			if (mga != null) {
				mga.setRank(ranks);
			}
		}

		public static void updateAllianceNotice(final int allianceid, final String notice) {
			final MapleGuildAlliance mga = getAlliance(allianceid);
			if (mga != null) {
				mga.setNotice(notice);
			}
		}

		public static boolean canInvite(final int allianceid) {
			final MapleGuildAlliance mga = getAlliance(allianceid);
			return mga != null && mga.getCapacity() > mga.getNoGuilds();
		}

		public static boolean changeAllianceLeader(final int allianceid, final int cid) {
			final MapleGuildAlliance mga = getAlliance(allianceid);
			return mga != null && mga.setLeaderId(cid);
		}

		public static boolean changeAllianceLeader(final int allianceid, final int cid, final boolean sameGuild) {
			final MapleGuildAlliance mga = getAlliance(allianceid);
			return mga != null && mga.setLeaderId(cid, sameGuild);
		}

		public static boolean changeAllianceRank(final int allianceid, final int cid, final int change) {
			final MapleGuildAlliance mga = getAlliance(allianceid);
			return mga != null && mga.changeAllianceRank(cid, change);
		}

		public static boolean changeAllianceCapacity(final int allianceid) {
			final MapleGuildAlliance mga = getAlliance(allianceid);
			return mga != null && mga.setCapacity();
		}

		public static boolean disbandAlliance(final int allianceid) {
			final MapleGuildAlliance mga = getAlliance(allianceid);
			return mga != null && mga.disband();
		}

		public static boolean addGuildToAlliance(final int allianceid, final int gid) {
			final MapleGuildAlliance mga = getAlliance(allianceid);
			return mga != null && mga.addGuild(gid);
		}

		public static boolean removeGuildFromAlliance(final int allianceid, final int gid, final boolean expelled) {
			final MapleGuildAlliance mga = getAlliance(allianceid);
			return mga != null && mga.removeGuild(gid, expelled);
		}

		public static void sendGuild(final int allianceid) {
			final MapleGuildAlliance alliance = getAlliance(allianceid);
			if (alliance != null) {
				sendGuild(CWvsContext.AlliancePacket.getAllianceUpdate(alliance), -1, allianceid);
				sendGuild(CWvsContext.AlliancePacket.getGuildAlliance(alliance), -1, allianceid);
			}
		}

		public static void sendGuild(final byte[] packet, final int exceptionId, final int allianceid) {
			final MapleGuildAlliance alliance = getAlliance(allianceid);
			if (alliance != null) {
				System.out.println(alliance.getId());
				for (int i = 0; i < alliance.getNoGuilds(); i++) {
					final int gid = alliance.getGuildId(i);
					if (gid > 0 && gid != exceptionId) {
						System.out.println(gid);
						Guild.guildPacket(gid, packet);
					}
				}
			}
		}

		public static boolean createAlliance(final String alliancename, final int cid, final int cid2, final int gid,
				final int gid2) {
			final int allianceid = MapleGuildAlliance.createToDb(cid, alliancename, gid, gid2);
			if (allianceid <= 0) {
				return false;
			}
			final MapleGuild g = Guild.getGuild(gid);
			final MapleGuild g_ = Guild.getGuild(gid2);
			g.setAllianceId(allianceid);
			g_.setAllianceId(allianceid);
			g.changeARank(true);
			g_.changeARank(false);
			final MapleGuildAlliance alliance = getAlliance(allianceid);
			sendGuild(CWvsContext.GuildPacket.createGuildAlliance(alliance), -1, allianceid);
			return true;
		}

		public static void allianceChat(final MapleCharacter player, final String msg, final LittleEndianAccessor slea,
				final RecvPacketOpcode recv) {
			final MapleGuild g = Guild.getGuild(player.getGuildId());
			if (g != null) {
				final MapleGuildAlliance ga = getAlliance(g.getAllianceId());
				if (ga != null) {
					for (int i = 0; i < ga.getNoGuilds(); ++i) {
						final MapleGuild g_ = Guild.getGuild(ga.getGuildId(i));
						if (g_ != null) {
							g_.allianceChat(player, msg, slea, recv);
							if (i == 0) {
								DBLogger.getInstance().logChat(LogType.Chat.Guild, player.getId(), player.getName(),
										msg, "[" + ga.getName() + " - \uc5f0\ud569 ]");
							}
						}
					}
				}
			}
		}

		public static void setNewAlliance(final int gid, final int allianceid) {
			final MapleGuildAlliance alliance = getAlliance(allianceid);
			final MapleGuild guild = Guild.getGuild(gid);
			if (alliance != null && guild != null) {
				for (int i = 0; i < alliance.getNoGuilds(); ++i) {
					if (gid == alliance.getGuildId(i)) {
						guild.setAllianceId(allianceid);
						guild.broadcast(CWvsContext.AlliancePacket.getAllianceInfo(alliance));
						guild.broadcast(CWvsContext.AlliancePacket.getGuildAlliance(alliance));
						guild.broadcast(CWvsContext.AlliancePacket.changeAlliance(alliance, true));
						guild.changeARank();
						guild.writeToDB(false);
					} else {
						final MapleGuild g_ = Guild.getGuild(alliance.getGuildId(i));
						if (g_ != null) {
							g_.broadcast(CWvsContext.AlliancePacket.addGuildToAlliance(alliance, guild));
							g_.broadcast(CWvsContext.AlliancePacket.changeGuildInAlliance(alliance, guild, true));
						}
					}
				}
			}
		}

		public static void setOldAlliance(final int gid, final boolean expelled, final int allianceid) {
			final MapleGuildAlliance alliance = getAlliance(allianceid);
			final MapleGuild g_ = Guild.getGuild(gid);
			if (alliance != null) {
				for (int i = 0; i < alliance.getNoGuilds(); ++i) {
					final MapleGuild guild = Guild.getGuild(alliance.getGuildId(i));
					if (guild == null) {
						if (gid != alliance.getGuildId(i)) {
							alliance.removeGuild(gid, false, true);
						}
					} else if (g_ == null || gid == alliance.getGuildId(i)) {
						guild.changeARank(5);
						guild.setAllianceId(0);
						guild.broadcast(CWvsContext.AlliancePacket.disbandAlliance(allianceid));
					} else if (g_ != null) {
						guild.broadcast(CWvsContext.serverNotice(5, "", "[" + g_.getName() + "] 길드가 연합에서 탈퇴 하였습니다."));
						guild.broadcast(CWvsContext.AlliancePacket.changeGuildInAlliance(alliance, g_, false));
						guild.broadcast(CWvsContext.AlliancePacket.removeGuildFromAlliance(alliance, g_, expelled));
					}
				}
			}
			if (gid == -1) {
				Alliance.alliances.remove(allianceid);
			}
		}

		public static List<byte[]> getAllianceInfo(final int allianceid, final boolean start) {
			final List<byte[]> ret = new ArrayList<byte[]>();
			final MapleGuildAlliance alliance = getAlliance(allianceid);
			if (alliance != null) {
				if (start) {
					ret.add(CWvsContext.AlliancePacket.getAllianceInfo(alliance));
					ret.add(CWvsContext.AlliancePacket.getGuildAlliance(alliance));
				}
				ret.add(CWvsContext.AlliancePacket.getAllianceUpdate(alliance));
			}
			return ret;
		}

		public static void save() {
			System.out.println("Saving alliances...");
			for (final MapleGuildAlliance a : Alliance.alliances.values()) {
				a.saveToDb();
			}
		}

		static {
			alliances = new ConcurrentHashMap<Integer, MapleGuildAlliance>();
			final Collection<MapleGuildAlliance> allGuilds = MapleGuildAlliance.loadAll();
			for (final MapleGuildAlliance g : allGuilds) {
				Alliance.alliances.put(g.getId(), g);
			}
		}
	}
}
