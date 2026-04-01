package handling.world.guild;

import client.MapleCharacter;
import client.MapleCharacterUtil;
import client.MapleClient;
import client.SkillFactory;
import client.inventory.Item;
import client.inventory.MapleInventoryType;
import constants.GameConstants;
import database.DatabaseConnection;
import handling.RecvPacketOpcode;
import handling.channel.ChannelServer;
import handling.world.World;
import log.DBLogger;
import log.LogType;
import server.SecondaryStatEffect;
import tools.Pair;
import tools.data.LittleEndianAccessor;
import tools.data.MaplePacketLittleEndianWriter;
import tools.packet.CField;
import tools.packet.CWvsContext;
import tools.packet.PacketHelper;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.*;
import java.util.*;

public class MapleGuild implements Comparable<MapleGuild> {

	public static final long serialVersionUID = 6322150443228168192L;
	private final List<MapleGuildCharacter> members = new ArrayList<MapleGuildCharacter>();
	private final List<MapleGuildCharacter> requests = new ArrayList<MapleGuildCharacter>();
	private final Map<Integer, MapleGuildSkill> guildSkills = new HashMap<Integer, MapleGuildSkill>();
	private final String[] rankTitles = new String[10];
	private final int[] rankRoles = new int[10];
	private String name;
	private String notice;
	private double guildScore = 0.0;
	private int id;
	private int gp;
	private int logo;
	private int logoColor;
	private int leader;
	private int capacity;
	private int logoBG;
	private int logoBGColor;
	private int signature;
	private int level;
	private int noblessskillpoint;
	private int guildlevel;
	private int fame;
	private int beforeattance;
	private int afterattance;
	private int lastResetDay;
	private int weekReputation;
	private boolean bDirty = true;
	private boolean proper = true;
	private int allianceid = 0;
	private int invitedid = 0;
	private byte[] customEmblem;
	private boolean init = false;
	private boolean changed_skills = false;
	private boolean changed_requests = false;

	private enum BCOp {
		NONE, DISBAND, EMBELMCHANGE;
	}

	public MapleGuild(int guildid) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getConnection();
			ps = con.prepareStatement("SELECT * FROM guilds WHERE guildid = ?");
			ps.setInt(1, guildid);
			rs = ps.executeQuery();
			if (!rs.first()) {
				rs.close();
				ps.close();
				this.id = -1;
				return;
			}
			this.id = guildid;
			this.name = rs.getString("name");
			this.gp = rs.getInt("GP");
			this.fame = rs.getInt("fame");
			this.guildlevel = rs.getInt("level");
			this.logo = rs.getInt("logo");
			this.logoColor = rs.getInt("logoColor");
			this.logoBG = rs.getInt("logoBG");
			this.logoBGColor = rs.getInt("logoBGColor");
			this.capacity = rs.getInt("capacity");
			this.rankTitles[0] = rs.getString("rank1title");
			this.rankTitles[1] = rs.getString("rank2title");
			this.rankTitles[2] = rs.getString("rank3title");
			this.rankTitles[3] = rs.getString("rank4title");
			this.rankTitles[4] = rs.getString("rank5title");
			this.rankTitles[5] = rs.getString("rank6title");
			this.rankTitles[6] = rs.getString("rank7title");
			this.rankTitles[7] = rs.getString("rank8title");
			this.rankTitles[8] = rs.getString("rank9title");
			this.rankTitles[9] = rs.getString("rank10title");
			this.rankRoles[0] = rs.getInt("rank1role");
			this.rankRoles[1] = rs.getInt("rank2role");
			this.rankRoles[2] = rs.getInt("rank3role");
			this.rankRoles[3] = rs.getInt("rank4role");
			this.rankRoles[4] = rs.getInt("rank5role");
			this.rankRoles[5] = rs.getInt("rank6role");
			this.rankRoles[6] = rs.getInt("rank7role");
			this.rankRoles[7] = rs.getInt("rank8role");
			this.rankRoles[8] = rs.getInt("rank9role");
			this.rankRoles[9] = rs.getInt("rank10role");
			this.leader = rs.getInt("leader");
			this.notice = rs.getString("notice");
			this.signature = rs.getInt("signature");
			this.allianceid = rs.getInt("alliance");
			this.guildScore = rs.getInt("score");
			this.beforeattance = rs.getInt("beforeattance");
			this.afterattance = rs.getInt("afterattance");
			this.lastResetDay = rs.getInt("lastResetDay");
			this.weekReputation = rs.getInt("weekReputation");
			this.noblessskillpoint = rs.getInt("noblesspoint");
			Blob custom = rs.getBlob("customEmblem");
			if (custom != null) {
				this.customEmblem = custom.getBytes(1L, (int) custom.length());
			}
			rs.close();
			ps.close();
			MapleGuildAlliance alliance = World.Alliance.getAlliance(this.allianceid);
			if (alliance == null) {
				this.allianceid = 0;
			}
			ps = con.prepareStatement(
					"SELECT id, name, level, job, guildrank, guildContribution, alliancerank, lastattendance FROM characters WHERE guildid = ? ORDER BY guildrank ASC, name ASC",
					1008);
			ps.setInt(1, guildid);
			rs = ps.executeQuery();
			if (!rs.first()) {
				rs.close();
				ps.close();
				con.close();
				this.writeToDB(true);
				this.proper = false;
				return;
			}
			boolean leaderCheck = false;
			int gFix = 0;
			int aFix = 0;
			do {
				int cid = rs.getInt("id");
				byte gRank = rs.getByte("guildrank");
				byte by = rs.getByte("alliancerank");
				if (cid == this.leader) {
					leaderCheck = true;
					if (gRank != 1) {
						gRank = 1;
						gFix = 1;
					}
					if (alliance != null) {
						if (alliance.getLeaderId() == cid && by != 1) {
							by = 1;
							aFix = 1;
						} else if (alliance.getLeaderId() != cid && by != 2) {
							by = 2;
							aFix = 2;
						}
					}
				} else {
					if (gRank == 1) {
						gRank = 2;
						gFix = 2;
					}
					if (by < 3) {
						by = 3;
						aFix = 3;
					}
				}
				this.members.add(new MapleGuildCharacter(cid, rs.getShort("level"), rs.getString("name"), (byte) -1,
						rs.getInt("job"), gRank, rs.getInt("guildContribution"), by, guildid, false,
						rs.getInt("lastattendance")));
			} while (rs.next());
			rs.close();
			ps.close();
			ps = con.prepareStatement("SELECT * FROM guildsrequest WHERE gid = ?", 1008);
			ps.setInt(1, guildid);
			rs = ps.executeQuery();
			ArrayList<Pair<Integer, Integer>> request = new ArrayList<Pair<Integer, Integer>>();
			while (rs.next()) {
				request.add(new Pair<Integer, Integer>(rs.getInt("cid"), rs.getInt("gid")));
			}
			rs.close();
			ps.close();
			for (Pair pair : request) {
				ps = con.prepareStatement(
						"SELECT id, name, level, job, guildrank, guildContribution, alliancerank FROM characters WHERE id = ?",
						1008);
				ps.setInt(1, (Integer) pair.left);
				rs = ps.executeQuery();
				while (rs.next()) {
					byte gRank = rs.getByte("guildrank");
					byte aRank = rs.getByte("alliancerank");
					if ((Integer) pair.left == this.leader) {
						leaderCheck = true;
						if (gRank != 1) {
							gRank = 1;
							gFix = 1;
						}
						if (alliance != null) {
							if (alliance.getLeaderId() == ((Integer) pair.left).intValue() && aRank != 1) {
								aRank = 1;
								aFix = 1;
							} else if (alliance.getLeaderId() != ((Integer) pair.left).intValue() && aRank != 2) {
								aRank = 2;
								aFix = 2;
							}
						}
					} else {
						if (gRank == 1) {
							gRank = 2;
							gFix = 2;
						}
						if (aRank < 3) {
							aRank = 3;
							aFix = 3;
						}
					}
					this.requests.add(new MapleGuildCharacter((Integer) pair.left, rs.getShort("level"),
							rs.getString("name"), (byte) -1, rs.getInt("job"), gRank, rs.getInt("guildContribution"),
							aRank, (Integer) pair.right, false, 0));
				}
				rs.close();
				ps.close();
			}
			if (!leaderCheck) {
				// System.err.println("Leader " + this.leader + " isn't in guild " + this.id +
				// ". Impossible... guild is disbanding.");
				this.writeToDB(true);
				this.proper = false;
				return;
			}
			if (gFix > 0) {
				ps = con.prepareStatement("UPDATE characters SET guildrank = ? WHERE id = ?");
				ps.setByte(1, (byte) gFix);
				ps.setInt(2, this.leader);
				ps.executeUpdate();
				ps.close();
			}
			if (aFix > 0) {
				ps = con.prepareStatement("UPDATE characters SET alliancerank = ? WHERE id = ?");
				ps.setByte(1, (byte) aFix);
				ps.setInt(2, this.leader);
				ps.executeUpdate();
				ps.close();
			}
			ps = con.prepareStatement("SELECT * FROM guildskills WHERE guildid = ?");
			ps.setInt(1, guildid);
			rs = ps.executeQuery();
			while (rs.next()) {
				int sid = rs.getInt("skillid");
				if (sid < 91000000) {
					rs.close();
					ps.close();
					// System.err.println("Skill " + sid + " is in guild " + this.id + ".
					// Impossible... guild is disbanding.");
					this.writeToDB(true);
					this.proper = false;
					return;
				}
				this.guildSkills.put(sid, new MapleGuildSkill(sid, rs.getInt("level"), rs.getLong("timestamp"),
						rs.getString("purchaser"), ""));
			}
			rs.close();
			ps.close();
			this.level = this.calculateLevel();
		} catch (SQLException se) {
			// System.err.println("unable to read guild information from sql");
			se.printStackTrace();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}

	public boolean isProper() {
		return this.proper;
	}

	public final void setGuildLevel(int level) {
		this.guildlevel = level;
		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = DatabaseConnection.getConnection();
			ps = con.prepareStatement("UPDATE guilds SET level = ? WHERE guildid = ?");
			ps.setInt(1, this.guildlevel);
			ps.setInt(2, this.id);
			ps.execute();
			ps.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception exception) {
			}
		}

	}

	public final void setBeforeAttance(int attance) {
		this.beforeattance = attance;
		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = DatabaseConnection.getConnection();
			ps = con.prepareStatement("UPDATE guilds SET beforeattance = ? WHERE guildid = ?");
			ps.setInt(1, this.beforeattance);
			ps.setInt(2, this.id);
			ps.execute();
			ps.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception exception) {
			}
		}

	}

	public final void setAfterAttance(int attance) {
		this.afterattance = attance;
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = DatabaseConnection.getConnection();
			ps = con.prepareStatement("UPDATE guilds SET afterattance = ? WHERE guildid = ?");
			ps.setInt(1, this.afterattance);
			ps.setInt(2, this.id);
			ps.execute();
			ps.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception exception) {
			}
		}

	}

	public final void setGuildFame(int fame) {
		this.fame = fame;
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = DatabaseConnection.getConnection();
			ps = con.prepareStatement("UPDATE guilds SET fame = ? WHERE guildid = ?");
			ps.setInt(1, this.fame);
			ps.setInt(2, this.id);
			ps.execute();
			ps.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception exception) {
			}
		}

	}

	public final void setGuildGP(int gp) {
		this.gp = gp;
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = DatabaseConnection.getConnection();
			ps = con.prepareStatement("UPDATE guilds SET GP = ? WHERE guildid = ?");
			ps.setInt(1, gp);
			ps.setInt(2, this.id);
			ps.execute();
			ps.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception exception) {
			}
		}

		broadcast(CWvsContext.GuildPacket.guildUpdateOnlyGP(this.id, this.gp));
	}

	public final void writeToDB(boolean bDisband) {
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = DatabaseConnection.getConnection();
			if (!bDisband) {
				StringBuilder buf = new StringBuilder(
						"UPDATE guilds SET GP = ?, logo = ?, logoColor = ?, logoBG = ?, logoBGColor = ?, ");
				for (int i = 1; i < 11; i++) {
					buf.append("rank").append(i).append("title = ?, ");
					buf.append("rank").append(i).append("role = ?, ");
				}
				buf.append(
						"capacity = ?, notice = ?, alliance = ?, leader = ?, customEmblem = ?, noblesspoint = ?, score = ?, weekReputation = ? WHERE guildid = ?");
				ps = con.prepareStatement(buf.toString());
				ps.setInt(1, this.gp);
				ps.setInt(2, this.logo);
				ps.setInt(3, this.logoColor);
				ps.setInt(4, this.logoBG);
				ps.setInt(5, this.logoBGColor);
				ps.setString(6, this.rankTitles[0]);
				ps.setInt(7, this.rankRoles[0]);
				ps.setString(8, this.rankTitles[1]);
				ps.setInt(9, this.rankRoles[1]);
				ps.setString(10, this.rankTitles[2]);
				ps.setInt(11, this.rankRoles[2]);
				ps.setString(12, this.rankTitles[3]);
				ps.setInt(13, this.rankRoles[3]);
				ps.setString(14, this.rankTitles[4]);
				ps.setInt(15, this.rankRoles[4]);
				ps.setString(16, this.rankTitles[5]);
				ps.setInt(17, this.rankRoles[5]);
				ps.setString(18, this.rankTitles[6]);
				ps.setInt(19, this.rankRoles[6]);
				ps.setString(20, this.rankTitles[7]);
				ps.setInt(21, this.rankRoles[7]);
				ps.setString(22, this.rankTitles[8]);
				ps.setInt(23, this.rankRoles[8]);
				ps.setString(24, this.rankTitles[9]);
				ps.setInt(25, this.rankRoles[9]);
				ps.setInt(26, this.capacity);
				ps.setString(27, this.notice);
				ps.setInt(28, this.allianceid);
				ps.setInt(29, this.leader);
				Blob blob = null;
				if (this.customEmblem != null) {
					blob = new SerialBlob(this.customEmblem);
				}
				ps.setBlob(30, blob);
				ps.setInt(31, this.noblessskillpoint);
				ps.setInt(32, (int) this.guildScore);
				ps.setInt(33, this.weekReputation);
				ps.setInt(34, this.id);
				ps.executeUpdate();
				ps.close();

				if (this.changed_skills) {
					ps = con.prepareStatement("DELETE FROM guildskills WHERE guildid = ?");
					ps.setInt(1, this.id);
					ps.execute();
					ps.close();

					ps = con.prepareStatement(
							"INSERT INTO guildskills(`guildid`, `skillid`, `level`, `timestamp`, `purchaser`) VALUES(?, ?, ?, ?, ?)");
					ps.setInt(1, this.id);
					for (MapleGuildSkill mapleGuildSkill : this.guildSkills.values()) {
						ps.setInt(2, mapleGuildSkill.skillID);
						ps.setByte(3, (byte) mapleGuildSkill.level);
						ps.setLong(4, mapleGuildSkill.timestamp);
						ps.setString(5, mapleGuildSkill.purchaser);
						ps.execute();
					}
					ps.close();
				}
				this.changed_skills = false;

				if (this.changed_requests) {
					ps = con.prepareStatement("DELETE FROM guildsrequest WHERE gid = ?");
					ps.setInt(1, this.id);
					ps.execute();
					ps.close();

					ps = con.prepareStatement("INSERT INTO guildsrequest(`gid`, `cid`, `request`) VALUES(?, ?, ?)");
					for (MapleGuildCharacter mgc : this.requests) {
						ps.setInt(1, mgc.getGuildId());
						ps.setInt(2, mgc.getId());
						ps.setString(3, mgc.getRequest());
						ps.execute();
					}
					ps.close();
				}
				this.changed_requests = false;
			} else {

				ps = con.prepareStatement("DELETE FROM guildskills WHERE guildid = ?");
				ps.setInt(1, this.id);
				ps.executeUpdate();
				ps.close();

				ps = con.prepareStatement("DELETE FROM guilds WHERE guildid = ?");
				ps.setInt(1, this.id);
				ps.executeUpdate();
				ps.close();
				if (this.allianceid > 0) {
					MapleGuildAlliance alliance = World.Alliance.getAlliance(this.allianceid);
					if (alliance != null) {
						alliance.removeGuild(this.id, false);
					}
				}

				broadcast(CWvsContext.GuildPacket.guildDisband(this.id));
				broadcast(CWvsContext.GuildPacket.guildDisband2());
			}
		} catch (SQLException se) {
			// System.err.println("Error saving guild to SQL");
			se.printStackTrace();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}

	public final int getId() {
		return this.id;
	}

	public final int getLeaderId() {
		return this.leader;
	}

	public final MapleCharacter getLeader(MapleClient c) {
		return c.getChannelServer().getPlayerStorage().getCharacterById(this.leader);
	}

	public final int getGP() {
		return this.gp;
	}

	public final int getLogo() {
		return this.logo;
	}

	public final void setLogo(int l) {
		this.logo = l;
	}

	public final int getLogoColor() {
		return this.logoColor;
	}

	public final void setLogoColor(int c) {
		this.logoColor = c;
	}

	public final int getLogoBG() {
		return this.logoBG;
	}

	public final void setLogoBG(int bg) {
		this.logoBG = bg;
	}

	public final int getLogoBGColor() {
		return this.logoBGColor;
	}

	public final void setLogoBGColor(int c) {
		this.logoBGColor = c;
	}

	public final String getNotice() {
		if (this.notice == null) {
			return "";
		}
		return this.notice;
	}

	public final String getName() {
		return this.name;
	}

	public final int getCapacity() {
		return this.capacity;
	}

	public final int getSignature() {
		return this.signature;
	}

	public final void RankBroadCast(byte[] packet, int rank) {
		for (MapleGuildCharacter mgc : this.members) {
			if (mgc.isOnline() && mgc.getGuildRank() == rank) {
				int ch = World.Find.findChannel(mgc.getId());
				if (ch < 0) {
					return;
				}
				MapleCharacter c = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterById(mgc.getId());
				if (c != null && c.getGuildId() == mgc.getGuildId()) {
					c.getClient().getSession().writeAndFlush(packet);
				}
			}
		}
	}

	public final void broadcast(byte[] packet) {
		broadcast(packet, -1, BCOp.NONE);
	}

	public final void broadcast(byte[] packet, int exception) {
		broadcast(packet, exception, BCOp.NONE);
	}

	public final void broadcast(byte[] packet, int exceptionId, BCOp bcop) {
		broadcast(packet, exceptionId, bcop, null);
	}

	public final void broadcast(byte[] packet, int exceptionId, BCOp bcop, MapleCharacter chr) {
		buildNotifications();

		for (MapleGuildCharacter mgc : this.members) {
			if (bcop == BCOp.DISBAND) {
				if (mgc.isOnline()) {
					World.Guild.setGuildAndRank(mgc.getId(), 0, 5, 0, 5);
					continue;
				}
				setOfflineGuildStatus(0, (byte) 5, 0, (byte) 5, mgc.getId());
				continue;
			}
			if (mgc.isOnline() && mgc.getId() != exceptionId) {
				if (bcop == BCOp.EMBELMCHANGE) {
					World.Guild.changeEmblem(chr, mgc.getId(), this);
					continue;
				}
				World.Broadcast.sendGuildPacket(mgc.getId(), packet, exceptionId, this.id);
			}
		}
	}

	private final void buildNotifications() {
		if (!this.bDirty) {
			return;
		}
		List<Integer> mem = new LinkedList<>();
		Iterator<MapleGuildCharacter> toRemove = this.members.iterator();
		while (toRemove.hasNext()) {
			MapleGuildCharacter mgc = toRemove.next();
			if (!mgc.isOnline()) {
				continue;
			}
			if (mem.contains(Integer.valueOf(mgc.getId())) || mgc.getGuildId() != this.id) {
				this.members.remove(mgc);
				continue;
			}
			mem.add(Integer.valueOf(mgc.getId()));
		}
		this.bDirty = false;
	}

	public void setOnline(MapleGuildCharacter mgc, int cid, boolean online, int channel) {
		boolean bBroadcast = true;
		for (MapleGuildCharacter mg : this.members) {
			if (mg.getGuildId() == this.id && mg.getId() == cid) {
				if (mg.isOnline() == online) {
					bBroadcast = false;
				}
				mg.setOnline(online);
				mg.setChannel((byte) channel);
				break;
			}
		}
		if (bBroadcast) {
			broadcast(CWvsContext.GuildPacket.guildMemberOnline(mgc, this.id, cid, online), cid);
			if (this.allianceid > 0) {
				MapleGuildAlliance allianceData = World.Alliance.getAlliance(this.allianceid);
				int size = allianceData.getNoGuilds();
				for (int i = 0; i < size; i++) {
					int guildId = allianceData.getGuildId(i);
					if (this.id != guildId) {
						MapleGuild guild = World.Guild.getGuild(guildId);

						if (guild != null) {
							guild.broadcast(
									CWvsContext.AlliancePacket.allianceMemberOnline(mgc, this.id, mgc.getId(), online));
						}
					}
				}
			}
		}
		this.bDirty = true;
		this.init = true;
	}

	public final void guildChat(MapleCharacter player, String msg, LittleEndianAccessor slea, RecvPacketOpcode recv) {
		Item item = null;
		if (recv == RecvPacketOpcode.PARTYCHATITEM) {
			slea.readInt();
			byte invType = (byte) slea.readInt();
			byte pos = (byte) slea.readInt();
			item = player.getInventory(MapleInventoryType.getByType((pos > 0) ? invType : -1)).getItem((short) pos);
		}
		DBLogger.getInstance().logChat(LogType.Chat.Guild, player.getId(), this.name, msg, "[" + getName() + "]");

		broadcast(CField.multiChat(player, msg, 2, item), player.getId());
	}

	public final void allianceChat(MapleCharacter player, String msg, LittleEndianAccessor slea,
			RecvPacketOpcode recv) {
		Item item = null;
		if (recv == RecvPacketOpcode.PARTYCHATITEM) {
			slea.readInt();
			byte invType = (byte) slea.readInt();
			byte pos = (byte) slea.readInt();
			item = player.getInventory(MapleInventoryType.getByType((pos > 0) ? invType : -1)).getItem((short) pos);
		}
		broadcast(CField.multiChat(player, msg, 3, item), player.getId());
	}

	public final String getRankTitle(int rank) {
		return this.rankTitles[rank - 1];
	}

	public final int getRankRole(int role) {
		return this.rankRoles[role - 1];
	}

	public final byte[] getCustomEmblem() {
		return this.customEmblem;
	}

	public final void setCustomEmblem(byte[] emblem) {
		this.customEmblem = emblem;
		Connection con = null;
		PreparedStatement ps = null;
		try {
			Blob blob = null;
			if (emblem != null) {
				blob = new SerialBlob(emblem);
			}
			con = DatabaseConnection.getConnection();
			ps = con.prepareStatement("UPDATE guilds SET customEmblem = ? WHERE guildid = ?");
			ps.setBlob(1, blob);
			ps.setInt(2, this.id);
			ps.execute();
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
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}

	public int getAllianceId() {
		return this.allianceid;
	}

	public int getInvitedId() {
		return this.invitedid;
	}

	public void setInvitedId(int iid) {
		this.invitedid = iid;
	}

	public void setAllianceId(int a) {
		this.allianceid = a;
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = DatabaseConnection.getConnection();
			ps = con.prepareStatement("UPDATE guilds SET alliance = ? WHERE guildid = ?");
			ps.setInt(1, a);
			ps.setInt(2, this.id);
			ps.execute();
			ps.close();
			con.close();
		} catch (SQLException e) {
			// System.err.println("Saving allianceid ERROR" + e);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}

	public static final int createGuild(int leaderId, String name) {
		if (name.length() > 12) {
			return 0;
		}
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getConnection();
			ps = con.prepareStatement("SELECT guildid FROM guilds WHERE name = ?");
			ps.setString(1, name);
			rs = ps.executeQuery();

			if (rs.first()) {
				rs.close();
				ps.close();
				con.close();
				return 0;
			}
			ps.close();
			rs.close();

			ps = con.prepareStatement(
					"INSERT INTO guilds (`leader`, `name`, `signature`, `alliance`) VALUES (?, ?, ?, 0)", 1);
			ps.setInt(1, leaderId);
			ps.setString(2, name);
			ps.setInt(3, (int) (System.currentTimeMillis() / 1000L));
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			int ret = 0;
			if (rs.next()) {
				ret = rs.getInt(1);
			}
			rs.close();
			ps.close();
			con.close();
			return ret;
		} catch (SQLException se) {
			// System.err.println("SQL THROW");
			se.printStackTrace();
			return 0;
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}

	public final int addGuildMember(MapleGuildCharacter mgc, MapleCharacter chr, MapleGuild guild) {
		if (this.members.size() >= this.capacity) {
			return 0;
		}
		for (int i = this.members.size() - 1; i >= 0; i--) {
			if (((MapleGuildCharacter) this.members.get(i)).getGuildRank() < 5
					|| ((MapleGuildCharacter) this.members.get(i)).getName().compareTo(mgc.getName()) < 0) {
				this.members.add(i + 1, mgc);
				this.bDirty = true;

				break;
			}
		}

		broadcast(CWvsContext.GuildPacket.newGuildMember(mgc));
		broadcast(CWvsContext.GuildPacket.newGuildMember2(mgc, guild, chr));
		broadcast(CWvsContext.GuildPacket.guildLoad1(chr));
		broadcast(CWvsContext.GuildPacket.guildLoadAattendance());
		broadcast(CWvsContext.GuildPacket.showGuildInfo(chr));

		if (this.allianceid > 0) {
			World.Alliance.sendGuild(this.allianceid);
		}
		return 1;
	}

	public final int addGuildMember(MapleCharacter chr, MapleGuild guild) {
		MapleGuildCharacter chrs = new MapleGuildCharacter(chr);

		this.members.add(chrs);

		broadcast(CWvsContext.GuildPacket.newGuildMember(chrs));
		broadcast(CWvsContext.GuildPacket.newGuildMember2(chrs, guild, chr));

		/*
		 * broadcast(CWvsContext.GuildPacket.guildLoad1(chr));
		 * broadcast(CWvsContext.GuildPacket.guildLoadAattendance());
		 * broadcast(CWvsContext.GuildPacket.showGuildInfo(chr));
		 */
		return 1;
	}

	public final void leaveGuild(MapleGuildCharacter mgc) {
		Iterator<MapleGuildCharacter> itr = this.members.iterator();
		while (itr.hasNext()) {
			MapleGuildCharacter mgcc = itr.next();

			if (mgcc.getId() == mgc.getId()) {
				for (int i = 1; i <= 5; i++) {
					RankBroadCast(CWvsContext.GuildPacket.memberLeft(mgcc, false), i);
				}

				broadcast(CWvsContext.GuildPacket.newGuildHash());

				this.bDirty = true;
				this.members.remove(mgcc);

				if (mgc.isOnline()) {
					World.Guild.setGuildAndRank(mgcc.getId(), 0, 5, 0, 5);
					break;
				}

				setOfflineGuildStatus(0, (byte) 5, 0, (byte) 5, mgcc.getId());

				break;
			}
		}
		if (this.bDirty && this.allianceid > 0) {
			World.Alliance.sendGuild(this.allianceid);
		}
	}

	public final void expelMember(MapleGuildCharacter initiator, int cid) {
		Iterator<MapleGuildCharacter> itr = this.members.iterator();
		while (itr.hasNext()) {
			MapleGuildCharacter mgc = itr.next();

			if (mgc.getId() == cid && initiator.getGuildRank() < mgc.getGuildRank()) {
				broadcast(CWvsContext.GuildPacket.memberLeft(mgc, true));

				this.bDirty = true;

				if (mgc.isOnline()) {
					World.Guild.setGuildAndRank(cid, 0, 5, 0, 5);
				} else {
					MapleCharacterUtil.sendNote(mgc.getName(), initiator.getName(), "길드에서 강퇴당했습니다.", 0, 6,
							initiator.getId());
					setOfflineGuildStatus(0, (byte) 5, 0, (byte) 5, cid);
				}
				this.members.remove(mgc);
				break;
			}
		}

		if (this.bDirty && this.allianceid > 0) {
			World.Alliance.sendGuild(this.allianceid);
		}
	}

	public final void changeARank() {
		changeARank(false);
	}

	public final void changeARank(boolean leader) {
		if (this.allianceid <= 0) {
			return;
		}
		for (MapleGuildCharacter mgc : this.members) {
			byte newRank = 3;
			if (this.leader == mgc.getId()) {
				newRank = (byte) (leader ? 1 : 2);
			}
			if (mgc.isOnline()) {
				World.Guild.setGuildAndRank(mgc.getId(), this.id, mgc.getGuildRank(), mgc.getGuildContribution(),
						newRank);
			} else {
				setOfflineGuildStatus(this.id, mgc.getGuildRank(), mgc.getGuildContribution(), newRank, mgc.getId());
			}
			mgc.setAllianceRank(newRank);
		}
		World.Alliance.sendGuild(this.allianceid);
	}

	public final void changeARank(int newRank) {
		if (this.allianceid <= 0) {
			return;
		}
		for (MapleGuildCharacter mgc : this.members) {
			if (mgc.isOnline()) {
				World.Guild.setGuildAndRank(mgc.getId(), this.id, mgc.getGuildRank(), mgc.getGuildContribution(),
						newRank);
			} else {
				setOfflineGuildStatus(this.id, mgc.getGuildRank(), mgc.getGuildContribution(), (byte) newRank,
						mgc.getId());
			}
			mgc.setAllianceRank((byte) newRank);
		}
		World.Alliance.sendGuild(this.allianceid);
	}

	public final boolean changeARank(int cid, int newRank) {
		if (this.allianceid <= 0) {
			return false;
		}
		for (MapleGuildCharacter mgc : this.members) {
			if (cid == mgc.getId()) {
				if (mgc.isOnline()) {
					World.Guild.setGuildAndRank(cid, this.id, mgc.getGuildRank(), mgc.getGuildContribution(), newRank);
				} else {
					setOfflineGuildStatus(this.id, mgc.getGuildRank(), mgc.getGuildContribution(), (byte) newRank, cid);
				}
				mgc.setAllianceRank((byte) newRank);
				World.Alliance.sendGuild(this.allianceid);
				return true;
			}
		}
		return false;
	}

	public final void changeGuildLeader(int cid) {
		if (changeRank(cid, 1) && changeRank(this.leader, 2)) {
			if (this.allianceid > 0) {
				int aRank = getMGC(this.leader).getAllianceRank();
				if (aRank == 1) {
					World.Alliance.changeAllianceLeader(this.allianceid, cid, true);
				} else {
					changeARank(cid, aRank);
				}
				changeARank(this.leader, 3);
			}

			broadcast(CWvsContext.GuildPacket.guildLeaderChanged(this.id, this.leader, cid, this.allianceid));
			broadcast(CWvsContext.GuildPacket.newGuildHash());

			this.leader = cid;
			Connection con = null;
			PreparedStatement ps = null;
			try {
				con = DatabaseConnection.getConnection();
				ps = con.prepareStatement("UPDATE guilds SET leader = ? WHERE guildid = ?");
				ps.setInt(1, cid);
				ps.setInt(2, this.id);
				ps.execute();
				ps.close();
				con.close();
			} catch (SQLException e) {
				// System.err.println("Saving leaderid ERROR" + e);
			} finally {
				try {
					if (con != null) {
						con.close();
					}
					if (ps != null) {
						ps.close();
					}
				} catch (SQLException se) {
					se.printStackTrace();
				}
			}
		}
	}

	public final boolean changeRank(int cid, int newRank) {
		for (MapleGuildCharacter mgc : this.members) {
			if (cid == mgc.getId()) {
				if (mgc.isOnline()) {
					World.Guild.setGuildAndRank(cid, this.id, newRank, mgc.getGuildContribution(),
							mgc.getAllianceRank());
				} else {
					setOfflineGuildStatus(this.id, (byte) newRank, mgc.getGuildContribution(), mgc.getAllianceRank(),
							cid);
				}

				mgc.setGuildRank((byte) newRank);
				broadcast(CWvsContext.GuildPacket.changeRank(mgc));
				return true;
			}
		}

		return false;
	}

	public final void setGuildNotice(MapleCharacter chr, String notice) {
		this.notice = notice;
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = DatabaseConnection.getConnection();
			ps = con.prepareStatement("UPDATE guilds SET notice = ? WHERE guildid = ?");
			ps.setString(1, notice);
			ps.setInt(2, this.id);
			ps.execute();
			ps.close();
			con.close();
		} catch (SQLException e) {
			// System.err.println("Saving guild notice ERROR");
			e.printStackTrace();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				if (ps != null) {
					ps.close();
				}

			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		broadcast(CWvsContext.GuildPacket.guildNotice(chr, notice));
	}

	public final void memberLevelJobUpdate(MapleGuildCharacter mgc) {
		for (MapleGuildCharacter member : this.members) {
			if (member.getId() == mgc.getId()) {
				int old_level = member.getLevel();
				int old_job = member.getJobId();
				member.setJobId(mgc.getJobId());
				member.setLevel((short) mgc.getLevel());
				if (this.allianceid > 0) {
				}
				break;
			}
		}
	}

	public void setRankTitle(String[] ranks) {
		for (int i = 0; i < 10; i++) {
			this.rankTitles[i] = ranks[i];
		}
	}

	public final void changeRankTitle(MapleCharacter chr, String[] ranks) {
		int[] roles = this.rankRoles;
		for (int i = 0; i < 10; i++) {
			this.rankTitles[i] = ranks[i];
		}
		updateRankRole();
		broadcast(CWvsContext.GuildPacket.rankTitleChange(71, chr, ranks, roles));
	}

	public final void changeRankRole(MapleCharacter chr, int[] roles) {
		String[] ranks = this.rankTitles;
		for (int i = 0; i < 10; i++) {
			this.rankRoles[i] = roles[i];
		}
		updateRankRole();
		broadcast(CWvsContext.GuildPacket.rankTitleChange(73, chr, ranks, roles));
	}

	public final void changeRankTitleRole(MapleCharacter chr, String ranks, int roles, byte type) {
		String[] ranks1 = rankTitles;
		int[] roles1 = rankRoles;

		rankRoles[type] = roles;
		rankTitles[type] = ranks;

		updateRankRole();

		broadcast(CWvsContext.GuildPacket.rankTitleChange(68, chr, ranks1, roles1));
	}

	public final void addRankTitleRole(MapleCharacter chr, String ranks, int roles, byte type) {
		rankRoles[type] = roles;
		rankTitles[type] = ranks;

		updateRankRole();

		broadcast(CWvsContext.GuildPacket.rankTitleChange(70, chr, rankTitles, rankRoles));
	}

	public final void updateRankRole() {
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = DatabaseConnection.getConnection();
			StringBuilder buf = new StringBuilder("UPDATE guilds SET ");
			for (int i = 1; i < 11; i++) {
				buf.append("rank").append(i).append("title = ?, ");
				buf.append("rank").append(i).append("role = ?");
				if (i != 10) {
					buf.append(", ");
				}
			}
			buf.append(" WHERE guildid = ?");
			ps = con.prepareStatement(buf.toString());
			ps.setString(1, this.rankTitles[0]);
			ps.setInt(2, this.rankRoles[0]);
			ps.setString(3, this.rankTitles[1]);
			ps.setInt(4, this.rankRoles[1]);
			ps.setString(5, this.rankTitles[2]);
			ps.setInt(6, this.rankRoles[2]);
			ps.setString(7, this.rankTitles[3]);
			ps.setInt(8, this.rankRoles[3]);
			ps.setString(9, this.rankTitles[4]);
			ps.setInt(10, this.rankRoles[4]);
			ps.setString(11, this.rankTitles[5]);
			ps.setInt(12, this.rankRoles[5]);
			ps.setString(13, this.rankTitles[6]);
			ps.setInt(14, this.rankRoles[6]);
			ps.setString(15, this.rankTitles[7]);
			ps.setInt(16, this.rankRoles[7]);
			ps.setString(17, this.rankTitles[8]);
			ps.setInt(18, this.rankRoles[8]);
			ps.setString(19, this.rankTitles[9]);
			ps.setInt(20, this.rankRoles[9]);
			ps.setInt(21, this.id);
			ps.execute();
			ps.close();
			con.close();
		} catch (SQLException e) {
			// System.err.println("Saving guild rank / roles ERROR");
			e.printStackTrace();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}

	public final void disbandGuild() {
		writeToDB(true);
		broadcast(null, -1, BCOp.DISBAND);
	}

	public final void setGuildEmblem(MapleCharacter chr, short bg, byte bgcolor, short logo, byte logocolor) {
		this.logoBG = bg;
		this.logoBGColor = bgcolor;
		this.logo = logo;
		this.logoColor = logocolor;
		setCustomEmblem(null);
		broadcast(null, -1, BCOp.EMBELMCHANGE, chr);

		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = DatabaseConnection.getConnection();
			ps = con.prepareStatement(
					"UPDATE guilds SET logo = ?, logoColor = ?, logoBG = ?, logoBGColor = ? WHERE guildid = ?");
			ps.setInt(1, logo);
			ps.setInt(2, this.logoColor);
			ps.setInt(3, this.logoBG);
			ps.setInt(4, this.logoBGColor);
			ps.setInt(5, this.id);
			ps.execute();
			ps.close();
			con.close();
		} catch (SQLException e) {
			// System.err.println("Saving guild logo / BG colo ERROR");
			e.printStackTrace();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}

	public final void setGuildCustomEmblem(MapleCharacter chr, byte[] imgdata) {
		this.logoBG = 0;
		this.logoBGColor = 0;
		this.logo = 0;
		this.logoColor = 0;
		setCustomEmblem(imgdata);
		broadcast(CWvsContext.GuildPacket.changeCustomGuildEmblem(chr, imgdata));
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = DatabaseConnection.getConnection();
			ps = con.prepareStatement(
					"UPDATE guilds SET logo = ?, logoColor = ?, logoBG = ?, logoBGColor = ? WHERE guildid = ?");
			ps.setInt(1, this.logo);
			ps.setInt(2, this.logoColor);
			ps.setInt(3, this.logoBG);
			ps.setInt(4, this.logoBGColor);
			ps.setInt(5, this.id);
			ps.execute();
			ps.close();
			con.close();
		} catch (SQLException e) {
			// System.err.println("Saving guild logo / BG colo ERROR");
			e.printStackTrace();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				if (ps != null) {
					ps.close();
				}

			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}

	public final MapleGuildCharacter getMGC(int cid) {
		for (MapleGuildCharacter mgc : this.members) {
			if (mgc.getId() == cid) {
				return mgc;
			}
		}
		return null;
	}

	public final boolean increaseCapacity(boolean trueMax) {
		if (this.capacity >= (trueMax ? 200 : 100) || this.capacity + 10 > (trueMax ? 200 : 100)) {
			return false;
		}
		this.capacity += 10;
		broadcast(CWvsContext.GuildPacket.guildCapacityChange(this.id, this.capacity));

		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = DatabaseConnection.getConnection();
			ps = con.prepareStatement("UPDATE guilds SET capacity = ? WHERE guildid = ?");
			ps.setInt(1, this.capacity);
			ps.setInt(2, this.id);
			ps.execute();
			ps.close();
			con.close();
		} catch (SQLException e) {
			// System.err.println("Saving guild capacity ERROR");
			e.printStackTrace();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return true;
	}

	public final void gainGP(int amount) {
		gainGP(amount, true, -1);
	}

	public final void gainGP(int amount, boolean broadcast) {
		gainGP(amount, broadcast, -1);
	}

	public final void gainGP(int amount, boolean broadcast, int cid) {
		MapleGuildCharacter mg = getMGC(cid);
		MapleCharacter chr = null;
		for (ChannelServer cs : ChannelServer.getAllInstances()) {
			for (MapleCharacter chr2 : cs.getPlayerStorage().getAllCharacters().values()) {
				if (chr2.getId() == cid) {
					chr = chr2;
				}
			}
		}

		if (amount == 0 || chr == null) {
			return;
		}
		if (chr.getKeyValue(210302, "GP") < 10000L) {
			chr.setKeyValue(210302, "GP", "" + (chr.getKeyValue(210302, "GP") + amount));
		} else {
			return;
		}
		mg.setGuildContribution(mg.getGuildContribution() + amount);
		chr.setGuildContribution(mg.getGuildContribution() + amount);
		chr.saveGuildStatus();
		setGuildFame(getFame() + amount);
		int gp = 0;
		if (amount < 100) {
			float gp2 = amount;
			gp = (int) (gp2 / 100.0F * 30.0F);
		} else {
			gp = amount / 100 * 30;
		}
		setGuildGP(getGP() + gp);
		setWeekReputation(getWeekReputation() + gp);
		if (getFame() >= GameConstants.getGuildExpNeededForLevel(getLevel())) {
			setGuildLevel(getLevel() + 1);
			broadcast(CWvsContext.GuildPacket.showGuildInfo(chr));
			broadcast(CWvsContext.serverNotice(5, "", "<길드> 길드의 레벨이 상승 하였습니다."));
		}
		broadcast(CWvsContext.GuildPacket.updateGP(this.id, getFame(), getGP(), getLevel()));
		broadcast(CWvsContext.GuildPacket.GainGP(this, chr, getGP()));

		if (broadcast) {
			broadcast(CWvsContext.InfoPacket.getGPMsg(amount));
			chr.getClient().send(CWvsContext.InfoPacket.getGPContribution(amount));
		}
	}

	public Collection<MapleGuildSkill> getSkills() {
		return this.guildSkills.values();
	}

	public Map<Integer, MapleGuildSkill> getGuildSkills() {
		return this.guildSkills;
	}

	public int getSkillLevel(int sid) {
		if (!this.guildSkills.containsKey(Integer.valueOf(sid))) {
			return 0;
		}
		return ((MapleGuildSkill) this.guildSkills.get(Integer.valueOf(sid))).level;
	}

	public boolean activateSkill(int skill, String name) {
		if (!this.guildSkills.containsKey(Integer.valueOf(skill))) {
			return false;
		}
		MapleGuildSkill ourSkill = this.guildSkills.get(Integer.valueOf(skill));
		SecondaryStatEffect skillid = SkillFactory.getSkill(skill).getEffect(ourSkill.level);
		if (ourSkill.timestamp > System.currentTimeMillis() || skillid.getPeriod() <= 0) {
			return false;
		}
		ourSkill.timestamp = System.currentTimeMillis() + skillid.getPeriod() * 60000L;
		ourSkill.activator = name;
		writeToDB(false);
		broadcast(CWvsContext.GuildPacket.guildSkillPurchased(this.id, skill, ourSkill.level, ourSkill.timestamp,
				ourSkill.purchaser, name));
		return true;
	}

	public boolean purchaseSkill(int skill, String name, int cid) {
		SecondaryStatEffect skillid = SkillFactory.getSkill(skill).getEffect(getSkillLevel(skill) + 1);
		if (skillid.getReqGuildLevel() > getLevel() || skillid.getLevel() <= getSkillLevel(skill)) {
			return false;
		}
		MapleGuildSkill ourSkill = this.guildSkills.get(Integer.valueOf(skill));
		if (ourSkill == null) {
			ourSkill = new MapleGuildSkill(skill, skillid.getLevel(), 0L, name, name);
			this.guildSkills.put(Integer.valueOf(skill), ourSkill);
		} else {
			ourSkill.level = skillid.getLevel();
			ourSkill.purchaser = name;
			ourSkill.activator = name;
		}
		if (skillid.getPeriod() <= 0) {
			ourSkill.timestamp = -1L;
		} else {
			ourSkill.timestamp = System.currentTimeMillis() + skillid.getPeriod() * 60000L;
		}
		this.changed_skills = true;
		writeToDB(false);
		broadcast(CWvsContext.GuildPacket.guildSkillPurchased(this.id, skill, ourSkill.level, ourSkill.timestamp, name,
				name));
		return true;
	}

	public boolean removeSkill(int skill, String name) {
		if (this.guildSkills.containsKey(Integer.valueOf(skill))) {
			this.guildSkills.remove(Integer.valueOf(skill));
		}
		this.changed_skills = true;
		writeToDB(false);
		broadcast(CWvsContext.GuildPacket.guildSkillPurchased(this.id, skill, 0, -1L, name, name));
		return true;
	}

	public int getLevel() {
		return this.guildlevel;
	}

	public final int calculateLevel() {
		for (int i = 1; i < 30; i++) {
			if (this.gp < GameConstants.getGuildExpNeededForLevel(i)) {
				return i;
			}
		}
		return 30;
	}

	public final int calculateGuildPoints() {
		int rgp = this.gp;
		for (int i = 1; i < 30; i++) {
			if (rgp < GameConstants.getGuildExpNeededForLevel(i)) {
				return rgp;
			}
			rgp -= GameConstants.getGuildExpNeededForLevel(i);
		}
		return rgp;
	}

	public final void addMemberData(final MaplePacketLittleEndianWriter mplew) { // 1.2.391 OK.
		mplew.writeShort(this.members.size());

		for (MapleGuildCharacter mgc : this.members) {
			mplew.writeInt(mgc.getId());
			mplew.writeMapleAsciiString(mgc.getName());

			mplew.writeInt(mgc.getJobId());
			mplew.writeInt(mgc.getLevel());
			mplew.writeInt(mgc.getGuildRank());
			mplew.writeInt(3);

			mplew.writeLong(PacketHelper.getTime(mgc.getLastDisconnectTime()));
			mplew.write(mgc.isOnline());
			mplew.writeLong(PacketHelper.getTime(System.currentTimeMillis()));

			mplew.writeInt(mgc.getGuildContribution());
			mplew.writeInt(mgc.getGuildContribution());

			mplew.writeLong(PacketHelper.getTime(-2L));
			mplew.writeInt(mgc.getLastAttendance(mgc.getId()));
			mplew.writeLong(PacketHelper.getTime(-2L));
		}
	}

	public static final MapleGuildResponse sendInvite(MapleClient c, String targetName) {
		MapleCharacter mc = c.getChannelServer().getPlayerStorage().getCharacterByName(targetName);

		if (mc == null) {
			return MapleGuildResponse.NOT_IN_CHANNEL;
		}
		if (mc.getGuildId() > 0) {
			return MapleGuildResponse.ALREADY_IN_GUILD;
		}

		c.getSession().writeAndFlush(CWvsContext.GuildPacket.guildInvite(c.getPlayer().getGuild().getName()));
		mc.getClient().getSession().writeAndFlush(
				CWvsContext.GuildPacket.guildInvite(c.getPlayer().getGuildId(), targetName, c.getPlayer()));
		return null;
	}

	public Collection<MapleGuildCharacter> getMembers() {
		return Collections.unmodifiableCollection(this.members);
	}

	public final boolean isInit() {
		return this.init;
	}

	public boolean hasSkill(int id) {
		return this.guildSkills.containsKey(Integer.valueOf(id));
	}

	public static void setOfflineGuildStatus(int guildid, byte guildrank, int contribution, byte alliancerank,
			int cid) {
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = DatabaseConnection.getConnection();
			ps = con.prepareStatement(
					"UPDATE characters SET guildid = ?, guildrank = ?, guildContribution = ?, alliancerank = ? WHERE id = ?");
			ps.setInt(1, guildid);
			ps.setInt(2, guildrank);
			ps.setInt(3, contribution);
			ps.setInt(4, alliancerank);
			ps.setInt(5, cid);
			ps.executeUpdate();
			ps.close();
			con.close();
		} catch (SQLException se) {
			System.out.println("SQLException: " + se.getLocalizedMessage());
			se.printStackTrace();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}

	public int avergeMemberLevel() {
		int mean = 0, size = 0;
		for (MapleGuildCharacter gc : this.members) {
			mean += gc.getLevel();
			size++;
		}
		if (mean == 0 || size == 0) {
			return 0;
		}
		return mean / size;
	}

	public String getLeaderName() {
		for (MapleGuildCharacter gc : this.members) {
			if (gc.getId() == this.leader) {
				return gc.getName();
			}
		}
		return "없음";
	}

	public boolean addRequest(MapleGuildCharacter mgc) {
		this.changed_requests = true;
		Iterator<MapleGuildCharacter> toRemove = this.requests.iterator();
		while (toRemove.hasNext()) {
			MapleGuildCharacter mgc2 = toRemove.next();

			if (mgc2.getId() == mgc.getId()) {
				return false;
			}
		}
		this.requests.add(mgc);
		return true;
	}

	public void removeRequest(int cid) {
		Iterator<MapleGuildCharacter> toRemove = this.requests.iterator();
		while (toRemove.hasNext()) {
			MapleGuildCharacter mgc = toRemove.next();
			if (mgc.getId() == cid) {
				this.requests.remove(mgc);
				this.changed_requests = true;
				break;
			}
		}
	}

	public MapleGuildCharacter getRequest(int cid) {
		Iterator<MapleGuildCharacter> toRemove = this.requests.iterator();
		while (toRemove.hasNext()) {
			MapleGuildCharacter mgc = toRemove.next();
			if (mgc.getId() == cid) {
				return mgc;
			}
		}
		return null;
	}

	public void setNoblessSkillPoint(int point) {
		this.noblessskillpoint = point;
		writeToDB(false);
	}

	public int getNoblessSkillPoint() {
		return this.noblessskillpoint;
	}

	public void updateGuildScore(long totDamageToOneMonster) {
		double guildScore = this.guildScore;
		double add = totDamageToOneMonster / 1.0E12D;
		this.guildScore += add;
		if (guildScore != this.guildScore) {
			broadcast(CField.updateGuildScore((int) this.guildScore));
		}
	}

	public double getGuildScore() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getConnection();
			ps = con.prepareStatement("SELECT * FROM guilds WHERE guildid = ?");
			ps.setInt(1, this.id);
			rs = ps.executeQuery();

			if (rs.next()) {
				this.guildScore = rs.getInt("score");
			}
			rs.close();
			ps.close();
			con.close();
		} catch (SQLException e) {
			// System.err.println("Error getting character default" + e);
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (rs != null) {
					rs.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception exception) {
			}
		}

		return this.guildScore;
	}

	public void setGuildScore(double guildScore) {
		this.guildScore = guildScore;
		writeToDB(false);
	}

	public final int getFame() {
		return this.fame;
	}

	public final int getBeforeAttance() {
		return this.beforeattance;
	}

	public final int getAfterAttance() {
		return this.afterattance;
	}

	public final void setLevel(int level) {
		this.guildlevel = level;
		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = DatabaseConnection.getConnection();
			ps = con.prepareStatement("UPDATE guilds SET level = ? WHERE guildid = ?");
			ps.setInt(1, this.guildlevel);
			ps.setInt(2, this.id);
			ps.execute();
			ps.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception exception) {
			}
		}

	}

	public int getLastResetDay() {
		return this.lastResetDay;
	}

	public void setLastResetDay(int lastResetDay) {
		this.lastResetDay = lastResetDay;
		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = DatabaseConnection.getConnection();
			ps = con.prepareStatement("UPDATE guilds SET lastresetday = ? WHERE guildid = ?");
			ps.setInt(1, this.lastResetDay);
			ps.setInt(2, this.id);
			ps.execute();
			ps.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception exception) {
			}
		}

	}

	public int getWeekReputation() {
		return this.weekReputation;
	}

	public void setWeekReputation(int weekReputation) {
		this.weekReputation = weekReputation;
	}

	public int compareTo(MapleGuild o) {
		if (getGuildScore() < o.getGuildScore()) {
			return 1;
		}
		if (getGuildScore() > o.getGuildScore()) {
			return -1;
		}
		return 0;
	}
}
