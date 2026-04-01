package handling.channel.handler;

import client.MapleCharacter;
import client.MapleClient;
import client.Skill;
import client.SkillFactory;
import constants.GameConstants;
import constants.KoreaCalendar;
import database.DatabaseConnection;
import handling.channel.ChannelServer;
import handling.world.World;
import handling.world.guild.MapleGuild;
import handling.world.guild.MapleGuildAlliance;
import handling.world.guild.MapleGuildCharacter;
import handling.world.guild.MapleGuildResponse;
import handling.world.guild.MapleGuildSkill;
import server.SecondaryStatEffect;
import tools.data.LittleEndianAccessor;
import tools.packet.CField;
import tools.packet.CWvsContext;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GuildHandler {

	public static final void DenyGuildRequest(String from, MapleClient c) {
		MapleCharacter cfrom = c.getChannelServer().getPlayerStorage().getCharacterByName(from);
		if (cfrom != null) {
			cfrom.getClient().getSession()
					.writeAndFlush(CWvsContext.GuildPacket.denyGuildInvitation(c.getPlayer().getName()));
		}
	}

	private static boolean isGuildNameAcceptable(String name) throws UnsupportedEncodingException {
		if ((name.getBytes("EUC-KR")).length < 2 || (name.getBytes("EUC-KR")).length > 12) {
			return false;
		}
		return true;
	}

	private static void respawnPlayer(MapleCharacter mc) {
		if (mc.getMap() == null) {
			return;
		}

		mc.getMap().broadcastMessage(CField.loadGuildName(mc));
		mc.getMap().broadcastMessage(CField.loadGuildIcon(mc));
	}

	public static final void GuildCancelRequest(LittleEndianAccessor slea, MapleClient c, MapleCharacter chr) {
		if (slea.available() > 4) { // 가입 요청 거절
			slea.skip(1); // 1.2.390 ++

			int charId = slea.readInt();

			MapleGuild guild = chr.getGuild();

			if (c == null || chr == null || guild == null) {
				return;
			}

			MapleCharacter player = null;
			for (ChannelServer cs : ChannelServer.getAllInstances()) {
				MapleCharacter temp = cs.getPlayerStorage().getCharacterById(charId);

				if (temp != null) {
					player = temp;

					break;
				}
			}

			chr.setKeyValue(26015, "name", "");
			chr.setKeyValue(26015, "time", "" + System.currentTimeMillis());
			guild.removeRequest(charId);

			c.getSession().writeAndFlush(CWvsContext.GuildPacket.RequestDeny(charId, guild));

			if (player != null) {
				player.getClient().getSession().writeAndFlush(CWvsContext.GuildPacket.RequestDeny(charId, guild));
			}

			List<MapleGuild> g = new ArrayList<>();
			for (MapleGuild guilds : World.Guild.getGuilds()) {
				if (guilds.getRequest(c.getPlayer().getId()) != null) {
					g.add(guilds);
				}
			}

			// guild.broadcast(CWvsContext.GuildPacket.RequestDenyMaster(charId, guild));
			// c.getSession().writeAndFlush(CWvsContext.GuildPacket.RequestListGuild(g));
			// c.getSession().writeAndFlush(CWvsContext.GuildPacket.RecruitmentGuild(c.getPlayer()));
		} else { // 가입 요청 취소
			int gid = slea.readInt();

			MapleGuild guild = World.Guild.getGuild(gid);

			if (c == null || chr == null || guild == null) {
				return;
			}

			MapleCharacter player = null;
			for (ChannelServer cs : ChannelServer.getAllInstances()) {
				MapleCharacter temp = cs.getPlayerStorage().getCharacterById(guild.getLeaderId());

				if (temp != null) {
					player = temp;

					break;
				}
			}

			chr.setKeyValue(26015, "name", "");
			chr.setKeyValue(26015, "time", "" + System.currentTimeMillis());
			guild.removeRequest(chr.getId());

			c.getSession().writeAndFlush(CWvsContext.GuildPacket.RequestDenyMaster(chr.getId(), guild));

			if (player != null) {
				player.getClient().getSession()
						.writeAndFlush(CWvsContext.GuildPacket.RequestDenyMaster(chr.getId(), guild));
			}

			List<MapleGuild> g = new ArrayList<>();
			for (MapleGuild guilds : World.Guild.getGuilds()) {
				if (guilds.getRequest(c.getPlayer().getId()) != null) {
					g.add(guilds);
				}
			}

			// guild.broadcast(CWvsContext.GuildPacket.RequestDenyMaster(c.getPlayer().getId(),
			// guild));
			// c.getSession().writeAndFlush(CWvsContext.GuildPacket.RequestListGuild(g));
			// c.getSession().writeAndFlush(CWvsContext.GuildPacket.RecruitmentGuild(c.getPlayer()));
		}
	}

	public static final void GuildJoinRequest(LittleEndianAccessor slea, MapleCharacter chr) {
		int gid = slea.readInt();
		String requestss = slea.readMapleAsciiString();
		slea.skip(10);
		if (chr == null || gid <= 0) {
			return;
		}
		/*
		 * if (chr.getKeyValue(26015, "time") + 60000L >= System.currentTimeMillis()) {
		 * chr.getClient().getSession().writeAndFlush(CWvsContext.GuildPacket.
		 * DelayRequest()); return; }
		 */
		MapleGuild g = World.Guild.getGuild(gid);
		MapleGuildCharacter mgc2 = new MapleGuildCharacter(chr);
		mgc2.setGuildId(gid);
		mgc2.setRequest(requestss);
		if (request.get(chr.getId()) == null) {
			if (g.addRequest(mgc2)) {
				chr.getClient().getSession().writeAndFlush(CWvsContext.GuildPacket.requestGuild(g));
				g.broadcast(CWvsContext.GuildPacket.requestGuild2(g, chr, requestss));
			}

			chr.setKeyValue(26015, "name", g.getName());
			chr.setKeyValue(26015, "time", "" + System.currentTimeMillis());
		} else {
			request.remove(chr.getId());
			if (g.addRequest(mgc2)) {
				chr.getClient().getSession().writeAndFlush(CWvsContext.GuildPacket.requestGuild(g));
				g.broadcast(CWvsContext.GuildPacket.requestGuild2(g, chr, requestss));
			}

			chr.setKeyValue(26015, "name", "");
			chr.setKeyValue(26015, "time", "" + System.currentTimeMillis());
		}
		List<MapleGuild> gs = new ArrayList<>();
		for (MapleGuild guilds : World.Guild.getGuilds()) {
			if (guilds.getRequest(chr.getId()) != null) {
				gs.add(guilds);
			}
		}

		g.writeToDB(false);
	}

	public static final void GuildJoinDeny(LittleEndianAccessor slea, MapleCharacter chr) {
		if (chr == null) {
			return;
		}
		byte action = slea.readByte();
		for (int i = 0; i < action; i++) {
			int cid = slea.readInt();
			if (chr.getGuildId() > 0 && chr.getGuildRank() <= 2) {
				MapleGuild g = chr.getGuild();
				if (chr.getGuildRank() <= 2) {
					g.removeRequest(cid);
					int ch = World.Find.findChannel(cid);
					if (ch < 0) {
						return;
					}
					MapleCharacter c = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterById(cid);
					c.getClient().getSession().writeAndFlush(CWvsContext.GuildPacket.RequestDeny(c.getId(), g));
					chr.setKeyValue(26015, "name", "");
					request.put(Integer.valueOf(cid), Long.valueOf(System.currentTimeMillis()));
				} else {
					chr.dropMessage(6, "길드 권한이 부족합니다.");
				}
			}
		}
	}

	public static final void GuildRegisterAccept(LittleEndianAccessor slea, MapleCharacter chr) {
		if (chr == null) {
			return;
		}
		byte action = slea.readByte();
		for (int i = 0; i < action; i++) {
			int cid = slea.readInt();
			if (chr.getGuildId() > 0 && chr.getGuildRank() <= 2) {
				MapleGuild g = chr.getGuild();
				if (chr.getGuildRank() <= 2 && g != null) {
					MapleCharacter c = null;
					for (ChannelServer cs : ChannelServer.getAllInstances()) {
						c = cs.getPlayerStorage().getCharacterById(cid);

						if (c != null) {
							MapleGuildCharacter temp = g.getRequest(cid);

							g.removeRequest(cid);
							g.addGuildMember(temp, chr, g);

							c.setGuildId(g.getId());
							c.setGuildRank((byte) 5);
							c.saveGuildStatus();
							c.setKeyValue(26015, "name", "");

							c.getClient().getSession().writeAndFlush(
									CWvsContext.GuildPacket.showGuildRanks((byte) 5, c.getClient(), null));
							c.getClient().getSession().writeAndFlush(CWvsContext.GuildPacket.newGuildInfo2(c));
							c.getClient().getSession().writeAndFlush(CWvsContext.GuildPacket.newGuildHash());
							c.getClient().getSession().writeAndFlush(CWvsContext.GuildPacket.showGuildInfo(c));

							respawnPlayer(c);

							/*
							 * c.getClient().getSession().writeAndFlush(CWvsContext.GuildPacket.
							 * newGuildInfo2(chr));
							 * c.getClient().getSession().writeAndFlush(CWvsContext.GuildPacket.newGuildHash
							 * ()); c.getClient().send(CWvsContext.GuildPacket.guildLoadAattendance());
							 * c.getClient().getSession().writeAndFlush(CWvsContext.GuildPacket.guildLoad1(c
							 * )); c.getClient().getSession().writeAndFlush(CWvsContext.GuildPacket.
							 * guildLoadAattendance());
							 * c.getClient().getSession().writeAndFlush(CWvsContext.GuildPacket.
							 * showGuildInfo(c));
							 */
							c.dropMessage(5, "`" + g.getName() + "` 길드에 가입 되었습니다.");
							for (MapleGuild guilds : World.Guild.getGuilds()) {
								if (guilds.getRequest(c.getId()) != null) {
									guilds.removeRequest(c.getId());
								}
							}
							break;
						}
					}
					if (c == null) {
						MapleGuildCharacter temp = OfflineMapleGuildCharacter(cid, chr.getGuildId());
						if (temp != null) {
							temp.setOnline(false);
							g.addGuildMember(temp, chr, g);
							MapleGuild.setOfflineGuildStatus(g.getId(), (byte) 5, 0, (byte) 5, cid);
							g.removeRequest(cid);

							for (MapleGuild guilds : World.Guild.getGuilds()) {
								if (guilds.getRequest(temp.getId()) != null) {
									guilds.removeRequest(temp.getId());
								}
							}
						} else {
							chr.dropMessage(5, "존재하지 않는 캐릭터입니다.");
						}
					}
				} else {
					chr.dropMessage(6, "길드 권한이 부족합니다.");
				}
			}
		}
	}

	public static final MapleGuildCharacter OfflineMapleGuildCharacter(int cid, int gid) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = DatabaseConnection.getConnection();
			ps = con.prepareStatement("SELECT * FROM characters where id = ?");
			ps.setInt(1, cid);
			rs = ps.executeQuery();
			if (rs.next()) {
				byte gRank = rs.getByte("guildrank"), aRank = rs.getByte("alliancerank");
				return new MapleGuildCharacter(cid, rs.getShort("level"), rs.getString("name"), (byte) -1,
						rs.getInt("job"), gRank, rs.getInt("guildContribution"), aRank, gid, false, 0);
			}
			ps.close();
			rs.close();
		} catch (SQLException se) {
			System.err.println("Error Laod Offline MapleGuildCharacter");
			se.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException ex) {
					Logger.getLogger(GuildHandler.class.getName()).log(Level.SEVERE, (String) null, ex);
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException ex) {
					Logger.getLogger(GuildHandler.class.getName()).log(Level.SEVERE, (String) null, ex);
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
		return null;
	}

	public static final void GuildRequest(int guildid, MapleCharacter player) {
		// player.dropMessage(1, "현재 이 기능은 사용하실 수 없습니다.");
	}

	public static void cancelGuildRequest(MapleClient c, MapleCharacter player) {
		// player.dropMessage(1, "현재 이 기능은 사용하실 수 없습니다.");
	}

	public static void SendGuild(LittleEndianAccessor slea, MapleClient c) {
		// c.getPlayer().dropMessage(1, "현재 이 기능은 사용하실 수 없습니다.");
	}

	private static class Invited {

		public String name;
		public int gid;
		public long expiration;

		public Invited(String n, int id) {
			this.name = n.toLowerCase();
			this.gid = id;
			this.expiration = System.currentTimeMillis() + 3600000L;
		}

		public boolean equals(Object other) {
			if (!(other instanceof Invited)) {
				return false;
			}
			Invited oth = (Invited) other;
			return (this.gid == oth.gid && this.name.equals(oth));
		}
	}

	private static List<Invited> invited = new LinkedList<>();

	private static Map<Integer, Long> request = new LinkedHashMap<>();

	private static long nextPruneTime = System.currentTimeMillis() + 300000L;

	public static final void Guild(LittleEndianAccessor slea, MapleClient c) {
		if (System.currentTimeMillis() >= nextPruneTime) {
			Iterator<Invited> itr = invited.iterator();

			while (itr.hasNext()) {
				Invited inv = itr.next();
				if (System.currentTimeMillis() >= inv.expiration) {
					itr.remove();
				}
			}
			nextPruneTime += 300000L;
		}
		try {
			String str1;
			int cid;
			String[] arrayOfString1;
			int[] roles;
			String ranks[], notice;
			MapleGuild mapleGuild1;
			List<MapleGuild> g;
			String name;
			MapleGuild guild;
			short mode;
			Skill skilli;
			int sid;
			String guildName, str2;
			byte newRank;
			int i, arrayOfInt1[];
			byte isCustomImage;
			MapleGuildResponse mgr;
			int size;
			String text;
			int eff, guildId, j;
			Invited inv;
			int option;
			SecondaryStatEffect skillid;
			MapleGuild mapleGuild2;
			String[] arrayOfString2;
			int a, k;
			KoreaCalendar kc;
			int action = slea.readInt();
			switch (action) {
			case 1: // 길드 이름 확인 1.2.391 OK.
				str1 = slea.readMapleAsciiString();
				c.getPlayer().setGuildName(str1);
				c.getSession().writeAndFlush(CWvsContext.GuildPacket.genericGuildMessage(4, str1));
				break;
			case 2: // 길드 생성 1.2.391 OK.
				if (c.getPlayer().getGuildId() > 0) {
					c.getPlayer().dropMessage(1, "已加入其他公會，無法創建新公會.");
					return;
				}
				if (c.getPlayer().getMeso() < 5000000L) {
					c.getPlayer().dropMessage(1, "創建公會所需的遊戲幣 [500萬遊戲幣] 不足.");
					return;
				}
				guildName = c.getPlayer().getGuildName();
				if (!isGuildNameAcceptable(guildName)) {
					c.getPlayer().dropMessage(1, "無法使用該公會名稱.");
					return;
				}
				guildId = World.Guild.createGuild(c.getPlayer().getId(), guildName);
				if (guildId == 0) {
					c.getPlayer().dropMessage(1, "請稍後再試.");
					return;
				}
				c.getPlayer().gainMeso(-5000000L, true, true);
				c.getPlayer().setGuildId(guildId);
				c.getPlayer().setGuildRank((byte) 1);
				c.getPlayer().saveGuildStatus();
				mapleGuild2 = World.Guild.getGuild(guildId);
				arrayOfString2 = new String[10];
				arrayOfString2[0] = "마스터";
				arrayOfString2[1] = "부마스터";
				a = 1;
				for (k = 2; k < 10; k++) {
					if (k >= 5) {
						arrayOfString2[k] = "";
						continue;
					}
					arrayOfString2[k] = "길드원" + a;
					a++;
				}
				mapleGuild2.changeRankTitle(c.getPlayer(), arrayOfString2);
				mapleGuild2.setLevel(1);
				mapleGuild2.setGuildGP(500);
				kc = new KoreaCalendar();
				mapleGuild2.setLastResetDay(Integer.parseInt(kc.getYears() + kc.getMonths() + kc.getDays()));
				World.Guild.setGuildMemberOnline(c.getPlayer().getMGC(), true, c.getChannel());
				c.getSession().writeAndFlush(CWvsContext.GuildPacket.newGuildInfo(c.getPlayer())); // 1.2.390 ++
				c.getSession().writeAndFlush(CWvsContext.GuildPacket.newGuildHash()); // 1.2.390 ++
				c.getSession().writeAndFlush(CWvsContext.GuildPacket.updateGP(c.getPlayer().getGuild().getId(),
						c.getPlayer().getGuild().getGP(), c.getPlayer().getGuild().getLevel())); // 1.2.390 ++
				c.getSession()
						.writeAndFlush(CWvsContext.GuildPacket.GainGP(c.getPlayer().getGuild(), c.getPlayer(), 500)); // 1.2.390
																														// ++
				c.getSession().writeAndFlush(CWvsContext.GuildPacket.showGuildInfo(c.getPlayer())); // 1.2.390 ++

				respawnPlayer(c.getPlayer());
				break;
			case 3: // 길드 가입 요청 1.2.391 OK.
				GuildHandler.GuildJoinRequest(slea, c.getPlayer());
				break;
			case 4: // 길드 가입 요청 취소 1.2.391 OK.
				GuildHandler.GuildCancelRequest(slea, c, c.getPlayer());
				break;
			case 6: // 길드 가입 요청 승인 1.2.391 OK.
				GuildHandler.GuildRegisterAccept(slea, c.getPlayer());
				break;
			case 7: // 길드 가입 요청 거절 1.2.391 OK.
				GuildHandler.GuildCancelRequest(slea, c, c.getPlayer());
				break;
			case 8: // 길드 탈퇴 1.2.391 OK.
				cid = slea.readInt();
				str2 = slea.readMapleAsciiString();

				if (cid != c.getPlayer().getId() || !str2.equals(c.getPlayer().getName())
						|| c.getPlayer().getGuildId() <= 0) {
					return;
				}

				World.Guild.leaveGuild(c.getPlayer().getMGC());

				respawnPlayer(c.getPlayer());
				break;
			case 9: // 길드 추방 1.2.391 OK.
				cid = slea.readInt();

				if (c.getPlayer().getGuildRank() > 2 || c.getPlayer().getGuildId() <= 0) {
					return;
				}

				World.Guild.expelMember(c.getPlayer().getMGC(), cid);

				respawnPlayer(c.getPlayer());
				break;
			case 12: // 길드 직위 이동 1.2.391 OK.
				cid = slea.readInt();
				newRank = slea.readByte();

				if (newRank <= 1 || newRank > 5 || c.getPlayer().getGuildRank() > 2
						|| (newRank <= 2 && c.getPlayer().getGuildRank() != 1) || c.getPlayer().getGuildId() <= 0) {
					return;
				}

				World.Guild.changeRank(c.getPlayer().getGuildId(), cid, newRank);
				break;
			case 14: // 길드 직위 이름 변경 1.2.391 OK.
				if (c.getPlayer().getGuildId() <= 0 || c.getPlayer().getGuildRank() != 1) {
					return;
				}

				byte type = (byte) (slea.readByte() - 1);

				String ranks1 = slea.readMapleAsciiString();

				int role = type != 0 ? slea.readInt() : -1;

				World.Guild.changeRankTitleRole(c.getPlayer(), ranks1, role, type);
				break;
			case 15: // 길드 직위 추가 1.2.391 OK.
				byte index = (byte) (slea.readByte() - 1);
				String rankName = slea.readMapleAsciiString();
				int roleData = slea.readInt();

				World.Guild.addRankTitleRole(c.getPlayer(), rankName, roleData, index);
				break;
			case 18: // 길드 엠블럼, 커스텀 길드 엠블럼 1.2.391 OK.
				mapleGuild1 = World.Guild.getGuild(c.getPlayer().getGuildId());
				if (c.getPlayer().getGuildId() <= 0 || c.getPlayer().getGuildRank() != 1) {
					c.getPlayer().dropMessage(1, "尚未加入公會，或並非公會會長.");

					return;
				}

				isCustomImage = slea.readByte();

				if (isCustomImage == 0) {
					short bg = slea.readShort();
					byte bgcolor = slea.readByte();
					short logo = slea.readShort();
					byte logocolor = slea.readByte();

					World.Guild.setGuildEmblem(c.getPlayer(), bg, bgcolor, logo, logocolor);

					c.getSession().writeAndFlush(CWvsContext.GuildPacket.showGuildInfo(c.getPlayer()));
				} else {
					if (mapleGuild1.getGP() >= 250000) {
						mapleGuild1.setGuildGP(mapleGuild1.getGP());
					} else {
						c.getPlayer().dropMessage(1, "[通知] GP不足.");
						c.getPlayer().getClient().send(CWvsContext.enableActions(c.getPlayer()));
						return;
					}
					slea.readByte();
					int m = slea.readInt();
					byte[] imgdata = new byte[m];
					for (int n = 0; n < m; n++) {
						imgdata[n] = slea.readByte();
					}

					World.Guild.setGuildCustomEmblem(c.getPlayer(), imgdata);
				}

				respawnPlayer(c.getPlayer());
				break;
			case 19: // 길드 소개 1.2.391 OK.
				notice = slea.readMapleAsciiString();

				if (notice.length() > 100 || c.getPlayer().getGuildId() <= 0 || c.getPlayer().getGuildRank() > 2) {
					return;
				}

				World.Guild.setGuildNotice(c.getPlayer(), notice);
				break;
			case 31: // 스킬 초기화 1.2.391 OK.
				mapleGuild1 = World.Guild.getGuild(c.getPlayer().getGuildId());
				if (mapleGuild1 == null) {
					return;
				}
				if (mapleGuild1.getGP() < 50000) {
					c.getPlayer().dropMessage(1, "GP不足.");
					c.send(CWvsContext.enableActions(c.getPlayer()));
					return;
				}
				mapleGuild1.setGuildGP(mapleGuild1.getGP() - 50000);

				for (MapleGuildSkill skill : mapleGuild1.getSkills()) {
					World.Guild.removeSkill(c.getPlayer().getGuildId(), skill.skillID, c.getPlayer().getName());
				}

				mapleGuild1.getSkills().clear();

				mapleGuild1.broadcast(CWvsContext.GuildPacket.reloadGuildSkill(mapleGuild1.getId()));

				c.getPlayer().dropMessage(1, "公會技能重置已完成.");
				break;
			case 33: // 길드장 위임 1.2.391 OK.
				cid = slea.readInt();

				if (c.getPlayer().getGuildId() <= 0 || c.getPlayer().getGuildRank() > 1) {
					return;
				}

				World.Guild.setGuildLeader(c.getPlayer().getGuildId(), cid);
				break;
			case 34: // 길드 정보 보기 1.2.391 OK.
				mapleGuild1 = World.Guild.getGuild(slea.readInt());

				c.getSession().writeAndFlush(CWvsContext.GuildPacket.LooksGuildInformation(mapleGuild1));
				break;
			case 35: // 길드 가입 신청 목록 1.2.391 OK.
				g = new ArrayList<>();
				for (MapleGuild mapleGuild : World.Guild.getGuilds()) {
					if (mapleGuild.getRequest(c.getPlayer().getId()) != null) {
						g.add(mapleGuild);
					}
				}

				c.getSession().writeAndFlush(CWvsContext.GuildPacket.RequestListGuild(g));
				break;
			case 36: // 길드 초대 1.2.391 OK.
				if (c.getPlayer().getGuildId() <= 0 || c.getPlayer().getGuildRank() > 2) {
					return;
				}

				name = slea.readMapleAsciiString();
				mgr = MapleGuild.sendInvite(c, name);

				if (mgr != null) {
					c.getSession().writeAndFlush(mgr.getPacket());
					break;
				}

				inv = new Invited(name, c.getPlayer().getGuildId());
				if (!invited.contains(inv)) {
					invited.add(inv);
				}
				break;
			case 37: // 출석 체크 1.2.391 OK.
				guild = World.Guild.getGuild(c.getPlayer().getGuildId());
				size = 0;
				for (MapleGuildCharacter member : guild.getMembers()) {
					if (member.getLastAttendance(member.getId()) == GameConstants.getCurrentDateday()) {
						size++;
					}
				}

				c.getPlayer().setLastAttendance(GameConstants.getCurrentDateday());
				guild.setAfterAttance(guild.getAfterAttance() + 30);
				if (size == 10 || size == 30 || size == 60 || size == 100) {
					int point = (size == 100) ? 2000 : ((size == 60) ? 1000 : ((size == 30) ? 100 : 50));
					guild.setAfterAttance(guild.getAfterAttance() + point);
					guild.setGuildFame(guild.getFame() + point);
					guild.setGuildGP(guild.getGP() + point / 100 * 30);
				}
				c.getPlayer().saveGuildStatus();
				World.Guild.gainContribution(guild.getId(), 30, c.getPlayer().getId());
				GuildBroadCast(CWvsContext.GuildPacket.guildAattendance(guild, c.getPlayer()), guild);
				if (guild.getFame() >= GameConstants.getGuildExpNeededForLevel(guild.getLevel())) {
					guild.setGuildLevel(guild.getLevel() + 1);
					GuildBroadCast(CWvsContext.serverNotice(5, "", "<길드> 길드의 레벨이 상승 하였습니다."), guild);
				}
				break;
			case 41: // 길드 스킬 레벨 업 1.2.391 OK.
				skilli = SkillFactory.getSkill(slea.readInt());
				if (c.getPlayer().getGuildId() <= 0 || skilli == null || skilli.getId() < 91000000) {
					return;
				}
				eff = World.Guild.getSkillLevel(c.getPlayer().getGuildId(), skilli.getId()) + 1;
				if (eff > skilli.getMaxLevel()) {
					return;
				}
				skillid = skilli.getEffect(eff);
				if (skillid.getReqGuildLevel() < 0 || c.getPlayer().getMeso() < skillid.getPrice()) {
					return;
				}

				World.Guild.purchaseSkill(c.getPlayer().getGuildId(), skillid.getSourceId(), c.getPlayer().getName(),
						c.getPlayer().getId());
				break;
			case 48: // 길드 검색 1.2.391 OK.
				mode = slea.readShort();
				text = slea.readMapleAsciiString();

				if (mode == 4) {
					c.getSession().writeAndFlush(CWvsContext.GuildPacket.RecruitmentGuild(c.getPlayer()));
					break;
				}

				option = slea.readShort();

				slea.skip(2);

				c.getSession().writeAndFlush(CWvsContext.GuildPacket.showSearchGuildInfo(c.getPlayer(),
						World.Guild.getGuildsByName(text, (option == 1), (byte) mode), text, (byte) mode, option));
				break;
			case 50: // 길드 연합 탈퇴 1.2.391 OK.
				MapleGuildAlliance alliance = World.Alliance.getAlliance(c.getPlayer().getGuild().getAllianceId());

				if (alliance.getLeaderId() == c.getPlayer().getId() && alliance.getNoGuilds() > 1) {
					c.getPlayer().dropMessage(1, "聯盟盟主在聯盟內所有公會退出前，無法主動退出聯盟.");
					return;
				}

				alliance.removeGuild(c.getPlayer().getGuild().getId(), true);
				c.getPlayer().getGuild().setAllianceId(0);
				c.getPlayer().getGuild()
						.broadcast(CWvsContext.GuildPacket.exitGuildAlliance(alliance, c.getPlayer().getGuild()));
				c.getSession().writeAndFlush(CWvsContext.GuildPacket.showGuildInfo(c.getPlayer()));
				break;
			case 53: // 스킬 쿨타임 초기화
				if (c.getPlayer().getGuildId() <= 0) {
					return;
				}
				sid = slea.readInt();
				eff = World.Guild.getSkillLevel(c.getPlayer().getGuildId(), sid);
				SkillFactory.getSkill(sid).getEffect(eff).applyTo(c.getPlayer());
				c.getSession().writeAndFlush(CField.skillCooldown(sid, 3600000));
				c.getPlayer().addCooldown(sid, System.currentTimeMillis(), 3600000L);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void guildRankingRequest(byte type, MapleClient c) {
		// c.getSession().writeAndFlush(CWvsContext.GuildPacket.showGuildRanks((byte)
		// type, c, MapleGuildRanking.getInstance()));
	}

	public static void GuildBroadCast(byte[] packet, MapleGuild guild) {
		for (ChannelServer cs : ChannelServer.getAllInstances()) {
			for (MapleCharacter chr : cs.getPlayerStorage().getAllCharacters().values()) {
				if (chr.getGuildId() == guild.getId()) {
					chr.getClient().getSession().writeAndFlush(packet);
				}
			}
		}
	}
}
