package client;

import tools.Pair;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MapleHyperStats implements Serializable {
	private static final long serialVersionUID = 1L;

	private int position;
	private int skillid;
	private int skilllevel;
	private transient Map<Integer, Pair<Integer, Integer>> hyperstats = new HashMap<>();

	// 添加使用方法
	public void addHyperStat(int key, int firstValue, int secondValue) {
		this.hyperstats.put(key, new Pair<>(firstValue, secondValue));
	}

	public Pair<Integer, Integer> getHyperStat(int key) {
		return this.hyperstats.get(key);
	}

	// 主构造方法
	public MapleHyperStats(int pos, int skill, int level) {
		this.position = pos;
		this.skillid = skill;
		this.skilllevel = level;
	}

	// 移除重复的构造方法

	/**
	 * 保存超能力统计到数据库
	 * 
	 * @param connection  数据库连接
	 * @param characterId 角色ID
	 * @throws SQLException 数据库异常
	 */
	public void saveToDB(Connection connection, int characterId) throws SQLException {
		Objects.requireNonNull(connection, "Database connection cannot be null");

		String sql = "UPDATE hyperstats SET pos = ?, skillid = ?, skilllevel = ? WHERE chrid = ?";
		try (PreparedStatement hps = connection.prepareStatement(sql)) {
			hps.setInt(1, position);
			hps.setInt(2, skillid);
			hps.setInt(3, skilllevel);
			hps.setInt(4, characterId);

			int affectedRows = hps.executeUpdate();
			if (affectedRows == 0) {
				// 如果没有更新任何行，则插入新记录
				insertNewRecord(connection, characterId);
			}
		}
	}

	private void insertNewRecord(Connection connection, int characterId) throws SQLException {
		String sql = "INSERT INTO hyperstats (chrid, pos, skillid, skilllevel) VALUES (?, ?, ?, ?)";
		try (PreparedStatement hps = connection.prepareStatement(sql)) {
			hps.setInt(1, characterId);
			hps.setInt(2, position);
			hps.setInt(3, skillid);
			hps.setInt(4, skilllevel);
			hps.executeUpdate();
		}
	}

	/**
	 * 从数据库加载超能力统计
	 * 
	 * @param connection  数据库连接
	 * @param characterId 角色ID
	 * @return 超能力统计列表
	 */
	public static List<MapleHyperStats> loadFromDB(Connection connection, int characterId) {
		Objects.requireNonNull(connection, "Database connection cannot be null");
		List<MapleHyperStats> hyperStats = new ArrayList<>();

		String sql = "SELECT pos, skillid, skilllevel FROM hyperstats WHERE chrid = ?";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, characterId);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					int pos = rs.getInt("pos");
					int skillId = rs.getInt("skillid");
					int skillLevel = rs.getInt("skilllevel");

					hyperStats.add(new MapleHyperStats(pos, skillId, skillLevel));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return hyperStats;
	}

	// Getter和Setter方法
	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getSkillid() {
		return skillid;
	}

	public void setSkillid(int skillid) {
		this.skillid = skillid;
	}

	public int getSkillLevel() {
		return skilllevel;
	}

	public void setSkillLevel(int skilllevel) {
		this.skilllevel = skilllevel;
	}

	@Override
	public String toString() {
		return String.format("HyperStat[position=%d,  skillid=%d, level=%d]", position, skillid, skilllevel);
	}
}