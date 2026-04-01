package client;

import tools.Pair;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class MapleLinkSkillPreset implements Serializable {
	private static final long serialVersionUID = 1L;

	private int position;
	private int skillid;
	private int skillchar;
	private transient Map<Integer, Pair<Integer, Integer>> linkSkillPreset = new ConcurrentHashMap<>();

	// 添加CRUD操作方法
	public void addLinkSkill(int key, int skillId, int charId) {
		this.linkSkillPreset.put(key, new Pair<>(skillId, charId));
	}

	public Pair<Integer, Integer> getLinkSkill(int key) {
		return this.linkSkillPreset.get(key);
	}

	public void removeLinkSkill(int key) {
		this.linkSkillPreset.remove(key);
	}

	// 主构造方法
	public MapleLinkSkillPreset(int pos, int skill, int scharid) {
		this.position = pos;
		this.skillid = skill;
		this.skillchar = scharid;
	}

	// 移除重复的构造方法

	/**
	 * 保存链接技能预设到数据库
	 * 
	 * @param connection  数据库连接
	 * @param characterId 角色ID
	 * @throws SQLException 数据库异常
	 */
	public void saveToDB(Connection connection, int characterId) throws SQLException {
		Objects.requireNonNull(connection, "Database connection cannot be null");

		String sql = "UPDATE LinkPreset SET skillid = ?, skillchar = ?, preset = ? WHERE chrid = ?";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, this.skillid);
			ps.setInt(2, this.skillchar);
			ps.setInt(3, this.position);
			ps.setInt(4, characterId);

			int affectedRows = ps.executeUpdate();
			if (affectedRows == 0) {
				insertNewRecord(connection, characterId);
			}
		}
	}

	private void insertNewRecord(Connection connection, int characterId) throws SQLException {
		String sql = "INSERT INTO LinkPreset (chrid, skillid, skillchar, preset) VALUES (?, ?, ?, ?)";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, characterId);
			ps.setInt(2, this.skillid);
			ps.setInt(3, this.skillchar);
			ps.setInt(4, this.position);
			ps.executeUpdate();
		}
	}

	/**
	 * 从数据库加载链接技能预设
	 * 
	 * @param connection  数据库连接
	 * @param characterId 角色ID
	 * @return 链接技能预设列表
	 */
	public static List<MapleLinkSkillPreset> loadFromDB(Connection connection, int characterId) {
		Objects.requireNonNull(connection, "Database connection cannot be null");
		List<MapleLinkSkillPreset> presets = new ArrayList<>();

		String sql = "SELECT preset, skillid, skillchar FROM LinkPreset WHERE chrid = ?";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, characterId);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					int pos = rs.getInt("preset");
					int skillId = rs.getInt("skillid");
					int skillChar = rs.getInt("skillchar");

					presets.add(new MapleLinkSkillPreset(pos, skillId, skillChar));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return presets;
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

	public int getSkillCharid() {
		return skillchar;
	}

	public void setSkillCharid(int skillchar) {
		this.skillchar = skillchar;
	}

	@Override
	public String toString() {
		return String.format("LinkSkillPreset[position=%d,  skillid=%d, charid=%d]", position, skillid, skillchar);
	}
}