package server;

import client.InnerSkillValueHolder;
import client.SkillFactory;
import database.DatabaseConnection;
import tools.Triple;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Syon
 */
public class InnerAbillity {

	private static InnerAbillity instance = null;

	private static List<Triple<Byte, Integer, int[]>> rarelists = new ArrayList<>();
	private static List<Triple<Byte, Integer, int[]>> epiclists = new ArrayList<>();
	private static List<Triple<Byte, Integer, int[]>> uniquelists = new ArrayList<>();
	private static List<Triple<Byte, Integer, int[]>> legendlists = new ArrayList<>();

	public static InnerAbillity getInstance() {
		if (instance == null) {
			instance = new InnerAbillity();
		}
		return instance;
	}

	public final void load() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = DatabaseConnection.getConnection();
			ps = con.prepareStatement("SELECT * FROM `abilityinfotable`");
			rs = ps.executeQuery();
			while (rs.next()) {
				final String[] sp = rs.getString("skillpoint").split(",");
				int[] sps = new int[sp.length];
				for (int i = 0; i < sp.length; i++) {
					sps[i] = Integer.parseInt(sp[i]);
				}
				if (rs.getInt("rare") == 0) {
					rarelists.add(new Triple<>((byte) rs.getInt("rare"), rs.getInt("skillid"), sps));
				} else if (rs.getInt("rare") == 1) {
					epiclists.add(new Triple<>((byte) rs.getInt("rare"), rs.getInt("skillid"), sps));
				} else if (rs.getInt("rare") == 2) {
					uniquelists.add(new Triple<>((byte) rs.getInt("rare"), rs.getInt("skillid"), sps));
				} else if (rs.getInt("rare") == 3) {
					legendlists.add(new Triple<>((byte) rs.getInt("rare"), rs.getInt("skillid"), sps));
				}
			}
			rs.close();
			ps.close();
			con.close();
			System.out.println("[通知]緩存了 " + rarelists.size() + "個稀有内容.");
			System.out.println("[通知]緩存了 " + epiclists.size() + "個應用程序.");
			System.out.println("[通知]緩存了 " + uniquelists.size() + "個獨特的應用程序.");
			System.out.println("[通知]緩存了 " + legendlists.size() + "個傳奇的真實性.");
		} catch (SQLException ex) {
			ex.printStackTrace();
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
			} catch (SQLException ex) {
			}
		}
	}

	public InnerSkillValueHolder renewSkill(int rank, boolean circulator, int preset) {
		int randskillnum = Randomizer.nextInt(rank == 0 ? rarelists.size()
				: rank == 1 ? epiclists.size() : rank == 2 ? uniquelists.size() : legendlists.size());
		Triple<Byte, Integer, int[]> randomSkillTriple = rank == 0 ? rarelists.get(randskillnum)
				: rank == 1 ? epiclists.get(randskillnum)
						: rank == 2 ? uniquelists.get(randskillnum) : legendlists.get(randskillnum);
		int randomSkill = randomSkillTriple.getMid();
		byte skillLevel;
		if (circulator) {
			skillLevel = (byte) randomSkillTriple.getRight()[randomSkillTriple.getRight().length - 1]; // 최고치
		} else {
			skillLevel = (byte) randomSkillTriple.getRight()[Randomizer.nextInt(randomSkillTriple.getRight().length)];
		}
		return new InnerSkillValueHolder(randomSkill, (byte) skillLevel,
				(byte) SkillFactory.getSkill(randomSkill).getMaxLevel(), randomSkillTriple.getLeft(), (byte) preset);
	}

	public InnerSkillValueHolder renewLevel(int rank, int skill, int preset) {
		List<Triple<Byte, Integer, int[]>> list = rank == 0 ? rarelists
				: rank == 1 ? epiclists : rank == 2 ? uniquelists : legendlists;
		for (Triple<Byte, Integer, int[]> data : list) {
			if (data.getMid() == skill) {
				return new InnerSkillValueHolder(skill,
						(byte) data.getRight()[Randomizer.nextInt(data.getRight().length)],
						(byte) SkillFactory.getSkill(skill).getMaxLevel(), data.getLeft(), (byte) preset);
			}
		}
		return null;
	}
}
