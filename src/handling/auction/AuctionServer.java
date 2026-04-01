package handling.auction;

import client.inventory.*;
import constants.GameConstants;
import constants.ServerType;
import database.DatabaseConnection;
import handling.channel.PlayerStorage;
import handling.netty.MapleNettyDecoder;
import handling.netty.MapleNettyEncoder;
import handling.netty.MapleNettyHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import server.MapleItemInformationProvider;
import server.ServerProperties;
import server.maps.BossReward;
import tools.FileoutputUtil;

import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AuctionServer {

	private static String ip;
	private static InetSocketAddress InetSocketadd;
	private static final int PORT = Integer.parseInt(ServerProperties.getProperty("ports.auction"));
	private static PlayerStorage players;
	private static boolean finishedShutdown = false;
	private static Map<Integer, AuctionItem> items = new ConcurrentHashMap<>();

	private static ServerBootstrap bootstrap;

	public static final void run_startup_configurations() {
		players = new PlayerStorage();
		ip = ServerProperties.getProperty("world.host") + ":" + PORT;

		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast("decoder", new MapleNettyDecoder());
							ch.pipeline().addLast("encoder", new MapleNettyEncoder());
							ch.pipeline().addLast("handler", new MapleNettyHandler(ServerType.AUCTION, -1));
						}
					}).option(ChannelOption.SO_BACKLOG, 0x80).childOption(ChannelOption.SO_KEEPALIVE, true);
			ChannelFuture f = bootstrap.bind(PORT).sync();
			loadItems();
			System.out.println("[通知]拍賣行服務器已成功打開 " + PORT + " 埠.");
		} catch (InterruptedException e) {
			System.err.println("[錯誤]拍賣行服務器無法打開 " + PORT + " 埠.");
		}
	}

	public static final String getIP() {
		return ip;
	}

	public static final PlayerStorage getPlayerStorage() {
		return players;
	}

	public static final void shutdown() {
		if (finishedShutdown) {
			return;
		}
		System.out.println("Saving all connected clients (Auction)...");
		players.disconnectAll();
		System.out.println("Shutting down Auction...");
		finishedShutdown = true;
	}

	public static boolean isShutdown() {
		return finishedShutdown;
	}

	public static void addInventoryItem(Connection con, List<Item> items) {
		try {
			String[] columns = { "inventoryitems", "inventoryitemsuse", "inventoryitemssetup", "inventoryitemsetc",
					"inventoryitemscash", "inventoryitemscody" };

			for (String column : columns) {
				StringBuilder query = new StringBuilder();
				query.append("DELETE FROM `");
				query.append(column);
				query.append("`WHERE `type` = 7");
				PreparedStatement ps = con.prepareStatement(query.toString());
				ps.executeUpdate();
				ps.close();
			}
			if (items == null) {
				return;
			}
			Iterator<Item> iter = items.iterator();

			while (iter.hasNext()) {
				PreparedStatement pse;
				Item item = iter.next();

				if (item == null) {
					continue;
				}

				StringBuilder query_2 = new StringBuilder("INSERT INTO `");
				switch (GameConstants.getInventoryType(item.getItemId()).getType()) {
				case 2:
					query_2.append("inventoryitemsuse");
					break;
				case 3:
					query_2.append("inventoryitemssetup");
					break;
				case 4:
					query_2.append("inventoryitemsetc");
					break;
				case 5:
					query_2.append("inventoryitemscash");
					break;
				case 6:
					query_2.append("inventoryitemscody");
					break;
				default:
					query_2.append("inventoryitems");
					break;
				}
				query_2.append("` (");
				query_2.append(
						"characterid, itemid, inventorytype, position, quantity, owner, GM_Log, uniqueid, expiredate, flag, `type`, sender, marriageId");
				query_2.append(", price, partyid, mobid, objectid");
				query_2.append(") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?");
				query_2.append(", ?, ?, ?, ?, ?");
				query_2.append(")");
				PreparedStatement ps = con.prepareStatement(query_2.toString(), 1);

				if (GameConstants.getInventoryType(item.getItemId()).getType() == 6) {
					pse = con.prepareStatement(
							"INSERT INTO `inventoryequipmentcody` VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
				} else {
					pse = con.prepareStatement(
							"INSERT INTO `inventoryequipment` VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
				}
				ps.setInt(1, -1);
				ps.setInt(2, item.getItemId());
				ps.setInt(3, GameConstants.getInventoryType(item.getItemId()).getType());
				ps.setInt(4, item.getPosition());
				ps.setInt(5, item.getQuantity());
				ps.setString(6, item.getOwner());
				ps.setString(7, item.getGMLog());
				if (item.getPet() != null) {

					ps.setLong(8, Math.max(item.getUniqueId(), item.getPet().getUniqueId()));
				} else {
					ps.setLong(8, item.getUniqueId());
				}
				ps.setLong(9, item.getExpiration());
				if (item.getFlag() < 0) {
					ps.setInt(10,
							(MapleItemInformationProvider.getInstance().getItemInformation(item.getItemId())).flag);
				} else {
					ps.setInt(10, item.getFlag());
				}
				ps.setByte(11, (byte) 7);
				ps.setString(12, item.getGiftFrom());
				ps.setInt(13, item.getMarriageId());
				if (item.getReward() != null) {
					ps.setInt(14, item.getReward().getPrice());
					ps.setInt(15, item.getReward().getPartyId());
					ps.setInt(16, item.getReward().getMobId());
					ps.setInt(17, item.getReward().getObjectId());
				} else {
					ps.setInt(14, 0);
					ps.setInt(15, 0);
					ps.setInt(16, 0);
					ps.setInt(17, 0);
				}
				ps.executeUpdate();

				ResultSet rs = ps.getGeneratedKeys();

				if (!rs.next()) {
					rs.close();
					continue;
				}
				long iid = rs.getLong(1);
				rs.close();

				item.setInventoryId(iid);

				if (item.getItemId() / 1000000 == 1) {

					Equip equip = (Equip) item;

					pse.setLong(1, iid);
					pse.setInt(2, equip.getUpgradeSlots());
					if (equip.getItemId() >= 1113098 && equip.getItemId() <= 1113128) {
						pse.setInt(3, equip.getBaseLevel());
					} else {
						pse.setInt(3, equip.getLevel());
					}
					pse.setInt(4, equip.getStr());
					pse.setInt(5, equip.getDex());
					pse.setInt(6, equip.getInt());
					pse.setInt(7, equip.getLuk());
					pse.setShort(8, equip.getArc());
					pse.setInt(9, equip.getArcEXP());
					pse.setInt(10, equip.getArcLevel());
					pse.setInt(11, equip.getHp());
					pse.setInt(12, equip.getMp());
					pse.setInt(13, equip.getWatk());
					pse.setInt(14, equip.getMatk());
					pse.setInt(15, equip.getWdef());
					pse.setInt(16, equip.getMdef());
					pse.setInt(17, equip.getAcc());
					pse.setInt(18, equip.getAvoid());
					pse.setInt(19, equip.getHands());
					pse.setInt(20, equip.getSpeed());
					pse.setInt(21, equip.getJump());
					pse.setInt(22, equip.getViciousHammer());
					pse.setInt(23, equip.getItemEXP());
					pse.setInt(24, equip.getDurability());
					pse.setByte(25, equip.getEnhance());
					pse.setByte(26, equip.getState());
					pse.setByte(27, equip.getLines());
					pse.setInt(28, equip.getPotential1());
					pse.setInt(29, equip.getPotential2());
					pse.setInt(30, equip.getPotential3());
					pse.setInt(31, equip.getPotential4());
					pse.setInt(32, equip.getPotential5());
					pse.setInt(33, equip.getPotential6());
					pse.setInt(34, equip.getIncSkill());
					pse.setShort(35, equip.getCharmEXP());
					pse.setShort(36, equip.getPVPDamage());
					pse.setShort(37, equip.getEnchantBuff());
					pse.setByte(38, equip.getReqLevel());
					pse.setByte(39, equip.getYggdrasilWisdom());
					pse.setByte(40, (byte) (equip.getFinalStrike() ? 1 : 0));
					pse.setShort(41, equip.getBossDamage());
					pse.setShort(42, equip.getIgnorePDR());
					pse.setByte(43, equip.getTotalDamage());
					pse.setByte(44, equip.getAllStat());
					pse.setByte(45, equip.getKarmaCount());
					pse.setShort(46, equip.getSoulName());
					pse.setShort(47, equip.getSoulEnchanter());
					pse.setShort(48, equip.getSoulPotential());
					pse.setInt(49, equip.getSoulSkill());
					pse.setLong(50, equip.getFire());
					pse.setInt(51, equip.getEquipmentType());
					pse.setInt(52, equip.getMoru());
					pse.setInt(53, equip.getAttackSpeed());
					pse.setLong(54, equip.getOptionExpiration());
					pse.setInt(55, equip.getCoption1());
					pse.setInt(56, equip.getCoption2());
					pse.setInt(57, equip.getCoption3());
					pse.executeUpdate();
					if (equip.getItemId() / 10000 == 166 && equip.getAndroid() != null) {
						equip.getAndroid().saveToDb();
					}
					if (GameConstants.getInventoryType(item.getItemId()).getType() != 6) {
						PreparedStatement ps2 = con.prepareStatement(
								"INSERT INTO inventoryequipenchant VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
						ps2.setLong(1, iid);
						ps2.setShort(2, equip.getEnchantStr());
						ps2.setShort(3, equip.getEnchantDex());
						ps2.setShort(4, equip.getEnchantInt());
						ps2.setShort(5, equip.getEnchantLuk());
						ps2.setShort(6, equip.getEnchantHp());
						ps2.setShort(7, equip.getEnchantMp());
						ps2.setShort(8, equip.getEnchantWatk());
						ps2.setShort(9, equip.getEnchantMatk());
						ps2.setShort(10, equip.getEnchantWdef());
						ps2.setShort(11, equip.getEnchantMdef());
						ps2.setShort(12, equip.getEnchantAcc());
						ps2.setShort(13, equip.getEnchantAvoid());
						ps2.setShort(14, equip.getStarforceStr());
						ps2.setShort(15, equip.getStarforceDex());
						ps2.setShort(16, equip.getStarforceInt());
						ps2.setShort(17, equip.getStarforceLuk());
						ps2.setShort(18, equip.getStarforceWatk());
						ps2.setShort(19, equip.getStarforceMatk());
						ps2.executeUpdate();
						ps2.close();
					}
				}
				ps.close();
				pse.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void addNewItem(AuctionItem item) {
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = DatabaseConnection.getConnection();

			ps = con.prepareStatement(
					"INSERT INTO `auctionitems` (`auctiontype`, `accountid`, `characterid`, `state`, `worldid`, `biduserid`,`price`, `directprice`, `enddate`, `registerdate`, `name`, `inventoryitemid`, `auctionitemid`, `bidusername`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
					1);
			ps.setInt(1, item.getAuctionType());
			ps.setInt(2, item.getAccountId());
			ps.setInt(3, item.getCharacterId());
			ps.setInt(4, item.getState());
			ps.setInt(5, item.getWorldId());
			ps.setInt(6, item.getBidUserId());
			ps.setLong(7, item.getPrice());
			ps.setLong(8, item.getDirectPrice());
			ps.setLong(9, item.getEndDate());
			ps.setLong(10, item.getRegisterDate());
			ps.setString(11, item.getName());
			ps.setLong(12, item.getItem().getInventoryId());
			ps.setInt(13, item.getAuctionId());
			ps.setString(14, (item.getBidUserName() == null) ? "없음" : item.getBidUserName());
			ps.executeUpdate();

			ps.close();

			AuctionHistory history = item.getHistory();

			if (history != null) {
				ps = con.prepareStatement(
						"INSERT INTO `auctionhistories` (`auctionid`, `accountid`, `characterid`, `itemid`, `state`, `price`, `buytime`, `deposit`, `quantity`, `worldid`, `biduserid`, `bidusername`, `id`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
						1);
				ps.setInt(1, history.getAuctionId());
				ps.setInt(2, history.getAccountId());
				ps.setInt(3, history.getCharacterId());
				ps.setInt(4, history.getItemId());
				ps.setInt(5, history.getState());
				ps.setLong(6, history.getPrice());
				ps.setLong(7, history.getBuyTime());
				ps.setInt(8, history.getDeposit());
				ps.setInt(9, history.getQuantity());
				ps.setInt(10, history.getWorldId());
				ps.setInt(11, history.getBidUserId());
				ps.setString(12, (history.getBidUserName() == null) ? "없음" : history.getBidUserName());
				ps.setLong(13, history.getId());
				ps.executeUpdate();

				ps.close();
			}
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

	public static void saveItems() {
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = DatabaseConnection.getConnection();

			System.out.println("正在保存拍卖...");
			con.setTransactionIsolation(1);
			con.setAutoCommit(false);

			ps = con.prepareStatement("DELETE FROM auctionitems");
			ps.executeUpdate();
			ps.close();

			ps = con.prepareStatement("DELETE FROM auctionhistories");
			ps.executeUpdate();
			ps.close();

			List<Item> itemz = new ArrayList<>();
			for (AuctionItem item : items.values()) {
				itemz.add(item.getItem());
			}

			addInventoryItem(con, itemz);

			con.commit();

			for (AuctionItem item : items.values()) {
				addNewItem(item);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				con.rollback();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (con != null) {
					con.setTransactionIsolation(4);
					con.setAutoCommit(true);
					con.close();
				}
			} catch (SQLException e) {
				FileoutputUtil.outputFileError("Log_Packet_Except.rtf", e);
				e.printStackTrace();
			}
		}
	}

	public static void loadItems() {
		int count = 0;
		System.out.println("加載拍賣行物品數據.");
		Connection con = null;
		PreparedStatement ps = null, ps1 = null, ps2 = null;
		ResultSet rs = null, rs1 = null, rs2 = null;

		String[] columns = { "inventoryitems", "inventoryitemsuse", "inventoryitemssetup", "inventoryitemsetc",
				"inventoryitemscash", "inventoryitemscody" };

		try {
			con = DatabaseConnection.getConnection();

			ps = con.prepareStatement("SELECT * FROM `auctionitems` WHERE `state` < 7");
			rs = ps.executeQuery();

			while (rs.next()) {
				AuctionItem aItem = new AuctionItem();
				aItem.setAuctionId(rs.getInt("auctionitemid"));
				aItem.setAuctionType(rs.getInt("auctiontype"));
				aItem.setAccountId(rs.getInt("accountid"));
				aItem.setCharacterId(rs.getInt("characterid"));
				aItem.setState(rs.getInt("state"));
				aItem.setWorldId(rs.getInt("worldid"));
				aItem.setBidUserId(rs.getInt("biduserid"));
				aItem.setNexonOid(rs.getInt("nexonoid"));
				aItem.setDeposit(rs.getInt("deposit"));
				aItem.setsStype(rs.getInt("sstype"));
				aItem.setBidWorld(rs.getInt("bidworld"));
				aItem.setPrice(rs.getLong("price"));
				aItem.setSecondPrice(rs.getLong("secondprice"));
				aItem.setDirectPrice(rs.getLong("directprice"));
				aItem.setEndDate(rs.getLong("enddate"));
				aItem.setRegisterDate(rs.getLong("registerdate"));
				aItem.setName(rs.getString("name"));
				aItem.setBidUserName(rs.getString("bidusername"));

				long inventoryItemId = rs.getLong("inventoryitemid");

				for (String column : columns) {
					StringBuilder query = new StringBuilder();
					query.append("SELECT * FROM `");
					query.append(column);
					query.append("` LEFT JOIN `");
					if (column.equals("inventoryitemscody")) {
						query.append("inventoryequipmentcody");
					} else {
						query.append("inventoryequipment");
					}
					query.append("` USING (`inventoryitemid`) WHERE `type` = 7 AND `inventoryitemid` = ?");
					ps1 = con.prepareStatement(query.toString());
					ps1.setLong(1, inventoryItemId);

					rs1 = ps1.executeQuery();
					if (rs1.next()) {
						MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
						Item item_ = null;

						if (!ii.itemExists(rs1.getInt("itemid"))) {
							continue;
						}
						if (rs1.getInt("itemid") / 1000000 == 1) {
							Equip equip = new Equip(rs1.getInt("itemid"), rs1.getShort("position"),
									rs1.getInt("uniqueid"), rs1.getInt("flag"));
							equip.setQuantity((short) 1);
							equip.setInventoryId(rs1.getLong("inventoryitemid"));
							equip.setOwner(rs1.getString("owner"));
							equip.setExpiration(rs1.getLong("expiredate"));
							equip.setUpgradeSlots(rs1.getByte("upgradeslots"));
							equip.setLevel(rs1.getByte("level"));
							equip.setStr(rs1.getShort("str"));
							equip.setDex(rs1.getShort("dex"));
							equip.setInt(rs1.getShort("int"));
							equip.setLuk(rs1.getShort("luk"));
							equip.setHp(rs1.getShort("hp"));
							equip.setMp(rs1.getShort("mp"));
							equip.setWatk(rs1.getShort("watk"));
							equip.setMatk(rs1.getShort("matk"));
							equip.setWdef(rs1.getShort("wdef"));
							equip.setMdef(rs1.getShort("mdef"));
							equip.setAcc(rs1.getShort("acc"));
							equip.setAvoid(rs1.getShort("avoid"));
							equip.setHands(rs1.getShort("hands"));
							equip.setSpeed(rs1.getShort("speed"));
							equip.setJump(rs1.getShort("jump"));
							equip.setViciousHammer(rs1.getByte("ViciousHammer"));
							equip.setItemEXP(rs1.getInt("itemEXP"));
							equip.setGMLog(rs1.getString("GM_Log"));
							equip.setDurability(rs1.getInt("durability"));
							equip.setEnhance(rs1.getByte("enhance"));
							equip.setState(rs1.getByte("state"));
							equip.setLines(rs1.getByte("line"));
							equip.setPotential1(rs1.getInt("potential1"));
							equip.setPotential2(rs1.getInt("potential2"));
							equip.setPotential3(rs1.getInt("potential3"));
							equip.setPotential4(rs1.getInt("potential4"));
							equip.setPotential5(rs1.getInt("potential5"));
							equip.setPotential6(rs1.getInt("potential6"));
							equip.setGiftFrom(rs1.getString("sender"));
							equip.setIncSkill(rs1.getInt("incSkill"));
							equip.setPVPDamage(rs1.getShort("pvpDamage"));
							equip.setCharmEXP(rs1.getShort("charmEXP"));
							if (equip.getCharmEXP() < 0) {
								equip.setCharmEXP(((Equip) ii.getEquipById(equip.getItemId())).getCharmEXP());
							}
							if (equip.getUniqueId() > -1L) {
								if (GameConstants.isEffectRing(rs1.getInt("itemid"))) {
									MapleRing ring = MapleRing.loadFromDb(equip.getUniqueId(), false);
									if (ring != null) {
										equip.setRing(ring);
									}
								} else if (equip.getItemId() / 10000 == 166) {
									MapleAndroid ring = MapleAndroid.loadFromDb(equip.getItemId(), equip.getUniqueId());
									if (ring != null) {
										equip.setAndroid(ring);
									}
								}
							}
							equip.setEnchantBuff(rs1.getShort("enchantbuff"));
							equip.setReqLevel(rs1.getByte("reqLevel"));
							equip.setYggdrasilWisdom(rs1.getByte("yggdrasilWisdom"));
							equip.setFinalStrike((rs1.getByte("finalStrike") > 0));
							equip.setBossDamage(rs1.getShort("bossDamage"));
							equip.setIgnorePDR(rs1.getShort("ignorePDR"));
							equip.setTotalDamage(rs1.getByte("totalDamage"));
							equip.setAllStat(rs1.getByte("allStat"));
							equip.setKarmaCount(rs1.getByte("karmaCount"));
							equip.setSoulEnchanter(rs1.getShort("soulenchanter"));
							equip.setSoulName(rs1.getShort("soulname"));
							equip.setSoulPotential(rs1.getShort("soulpotential"));
							equip.setSoulSkill(rs1.getInt("soulskill"));
							equip.setFire((rs1.getLong("fire") < 0L) ? 0L : rs1.getLong("fire"));
							equip.setArc(rs1.getShort("arc"));
							equip.setArcEXP(rs1.getInt("arcexp"));
							equip.setArcLevel(rs1.getInt("arclevel"));
							equip.setEquipmentType(rs1.getInt("equipmenttype"));
							equip.setMoru(rs1.getInt("moru"));
							equip.setAttackSpeed(rs1.getInt("attackSpeed"));
							equip.setOptionExpiration(rs1.getLong("optionexpiration"));
							equip.setCoption1(rs1.getInt("coption1"));
							equip.setCoption2(rs1.getInt("coption2"));
							equip.setCoption3(rs1.getInt("coption3"));

							if (!column.equals("inventoryitemscody")) {
								ps2 = con.prepareStatement(
										"SELECT * FROM inventoryequipenchant WHERE inventoryitemid = ?");
								ps2.setLong(1, equip.getInventoryId());
								rs2 = ps2.executeQuery();
								if (rs2.next()) {
									equip.setEnchantStr(rs2.getShort("str"));
									equip.setEnchantDex(rs2.getShort("dex"));
									equip.setEnchantInt(rs2.getShort("int"));
									equip.setEnchantLuk(rs2.getShort("luk"));
									equip.setEnchantHp(rs2.getShort("hp"));
									equip.setEnchantMp(rs2.getShort("mp"));
									equip.setEnchantWatk(rs2.getShort("watk"));
									equip.setEnchantMatk(rs2.getShort("matk"));
									equip.setEnchantWdef(rs2.getShort("wdef"));
									equip.setEnchantMdef(rs2.getShort("mdef"));
									equip.setEnchantAcc(rs2.getShort("acc"));
									equip.setEnchantAvoid(rs2.getShort("avoid"));
									equip.setStarforceStr(rs2.getShort("starforcestr"));
									equip.setStarforceDex(rs2.getShort("starforcedex"));
									equip.setStarforceInt(rs2.getShort("starforceint"));
									equip.setStarforceLuk(rs2.getShort("starforceluk"));
									equip.setStarforceWatk(rs2.getShort("starforcewatk"));
									equip.setStarforceMatk(rs2.getShort("starforcematk"));
								}
								rs2.close();
								ps2.close();
							}
							item_ = equip.copy();
						} else {
							Item item = new Item(rs1.getInt("itemid"), rs1.getShort("position"),
									rs1.getShort("quantity"), rs1.getInt("flag"), rs1.getInt("uniqueid"));
							item.setOwner(rs1.getString("owner"));
							item.setInventoryId(rs1.getLong("inventoryitemid"));
							item.setExpiration(rs1.getLong("expiredate"));
							item.setGMLog(rs1.getString("GM_Log"));
							item.setGiftFrom(rs1.getString("sender"));
							item.setMarriageId(rs1.getInt("marriageId"));
							if (GameConstants.isPet(item.getItemId())) {
								if (item.getUniqueId() > -1L) {
									MaplePet pet = MaplePet.loadFromDb(item.getItemId(), item.getUniqueId(),
											item.getPosition());
									if (pet != null) {
										item.setPet(pet);
									}
								} else {
									item.setPet(MaplePet.createPet(item.getItemId(),
											MapleInventoryIdentifier.getInstance()));
								}
							}
							if (item.getItemId() == 4001886) {
								item.setReward(new BossReward(rs1.getInt("objectid"), rs1.getInt("mobid"),
										rs1.getInt("partyid"), rs1.getInt("price")));
							}
							item_ = item.copy();
						}

						if (item_ != null) {
							aItem.setItem(item_);
							ps2 = con.prepareStatement("SELECT * FROM `auctionhistories` WHERE `auctionid` = ?");
							ps2.setInt(1, aItem.getAuctionId());
							rs2 = ps2.executeQuery();
							if (rs2.next()) {
								AuctionHistory history = new AuctionHistory();
								history.setId(rs2.getLong("id"));
								history.setAuctionId(rs2.getInt("auctionid"));
								history.setAccountId(rs2.getInt("accountid"));
								history.setCharacterId(rs2.getInt("characterid"));
								history.setItemId(rs2.getInt("itemid"));
								history.setState(rs2.getInt("state"));
								history.setPrice(rs2.getLong("price"));
								history.setBuyTime(rs2.getLong("buytime"));
								history.setDeposit(rs2.getInt("deposit"));
								history.setQuantity(rs2.getInt("quantity"));
								history.setWorldId(rs2.getInt("worldid"));
								history.setBidUserId(rs2.getInt("biduserid"));
								history.setBidUserName(rs2.getString("bidusername"));

								aItem.setHistory(history);
								rs2.close();
								ps2.close();
							}

							items.put(Integer.valueOf(aItem.getAuctionId()), aItem);
							count++;
						}
					}

					rs1.close();
					ps1.close();
				}
			}
			ps.close();
			rs.close();
		} catch (SQLException sqlE) {
			sqlE.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {

					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (ps1 != null) {
				try {
					ps1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (ps2 != null) {
				try {
					ps2.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (rs1 != null) {
				try {
					rs1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (rs2 != null) {
				try {
					rs2.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("拍賣行物品 " + count + "個已加載.");
	}

	public static Map<Integer, AuctionItem> getItems() {
		return items;
	}
}
