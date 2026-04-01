package client;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import tools.Pair;
import tools.data.MaplePacketLittleEndianWriter;

public class MapleKeyLayout implements Serializable {
	private static final long serialVersionUID = 9179541993413738569L;
	private boolean changed = false;
	private Map<Integer, Pair<Byte, Integer>> keymap;

	public MapleKeyLayout() {
		this.keymap = new HashMap<>();
	}

	public MapleKeyLayout(Map<Integer, Pair<Byte, Integer>> keys) {
		this.keymap = keys;
	}

	public final Map<Integer, Pair<Byte, Integer>> Layout() {
		this.changed = true;
		return this.keymap;
	}

	public final void unchanged() {
		this.changed = false;
	}

	public final void writeData(MaplePacketLittleEndianWriter mplew, MapleCharacter chr) {
		for (int x = 0; x < 99; x++) {
			Pair<Byte, Integer> binding = this.keymap.get(x);
			if (binding != null) {
				mplew.write(binding.getLeft());
				mplew.writeInt(binding.getRight());
			} else {
				mplew.write(0);
				mplew.writeInt(0);
			}
		}

		if (chr.getKeyValue(3, "newChar") <= 1) {
			mplew.write(1);
			mplew.write(1);
			chr.setKeyValue(3, "newChar", String.valueOf(chr.getKeyValue(3, "newChar") + 1));
		}
	}

	public final void saveKeys(Connection con, int charid) throws SQLException {
		if (!this.changed) {
			return;
		}
		try (PreparedStatement ps = con.prepareStatement("DELETE  FROM keymap WHERE characterid = ?")) {
			ps.setInt(1, charid);
			ps.execute();
		}

		if (this.keymap.isEmpty()) {
			return;
		}

		try (PreparedStatement ps = con
				.prepareStatement("INSERT INTO keymap (`characterid`, `key`, `type`, `action`) VALUES (?, ?, ?, ?)")) {
			ps.setInt(1, charid);
			for (Map.Entry<Integer, Pair<Byte, Integer>> keybinding : this.keymap.entrySet()) {
				ps.setInt(2, keybinding.getKey());
				Pair<Byte, Integer> pair = keybinding.getValue();
				ps.setInt(3, pair.getLeft());
				ps.setInt(4, pair.getRight());
				ps.addBatch();
			}
			ps.executeBatch();
		}
	}
}