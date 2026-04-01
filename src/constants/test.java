package constants;

import client.MatrixSkill;
import client.SecondaryStat;
import client.SecondaryStatValueHolder;
import handling.channel.handler.AttackInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import server.SecondaryStatEffect;
import tools.HexTool;
import tools.Pair;
import tools.data.ByteArrayByteStream;
import tools.data.LittleEndianAccessor;

public class test {

	public static void main(String[] args) {
		byte[] msg = HexTool.getByteArrayFromHexString(
				"3A FD D7 17 01 00 00 00 A1 EA 9B 21 01 05 00 00 00 FF FF FF FF D0 06 00 00 02 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 1F 02 31 01 00 9E C1 1A 60 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 66 8F F2 D2 B8 AA 01 A7 C3 C3 33 F2 01 00 00 00 3A FD D7 17 01 00 00 00 09 00 00 00 00 00 00 00 00 00 ED 01 13 01 00 00 00 00 1E 00 00 00 01 00 00");
		LittleEndianAccessor slea = new LittleEndianAccessor(new ByteArrayByteStream(msg));
		SecondaryStatValueHolder lightning;
		SecondaryStatEffect eff;
		Map<SecondaryStat, Pair<Integer, Integer>> statups;
		int skillid = slea.readInt();
		int level = slea.readInt();
		AttackInfo ret = new AttackInfo();
		ret.skill = skillid;
		ret.skilllevel = level;
		slea.skip(4);
		byte aa = slea.readByte();
		int k = -1;
		if (aa > 0) {

			slea.skip(4);
			do {
				byte x;
				k = slea.readInt();
				switch (k) {
				case 1: // 1.2.390 OK.
					x = slea.readByte();

					if (x > 0) {
						slea.readInt();
						slea.readInt();
						slea.readInt();
						slea.readByte();
						slea.readByte();
					}

					break;
				case 2: // 1.2.390 OK.
					x = slea.readByte();

					if (x > 0) {
						slea.skip(19);
					}

					break;
				case 3: // 1.2.390 OK.
					x = slea.readByte();

					if (x > 0) {
						ret.isLink = (slea.readByte() == 1);

						slea.readInt();
					}

					break;
				case 4: // 1.2.390 OK.
					x = slea.readByte();

					if (x > 0) {
						slea.skip(16);
						slea.skip(8);
						slea.skip(4);
					}

					break;
				case 5: // 1.2.390 OK.
					slea.readByte();
					break;
				case 6: // 1.2.390 OK.
					slea.readByte();
					break;
				case 7: // 1.2.390 OK.
					x = slea.readByte();

					if (x > 0) {
						ret.plusPosition2 = new Point(slea.readInt(), slea.readInt());
						ret.rlType = slea.readByte();
						slea.readByte();
						slea.readByte();
						slea.readByte();
						slea.readByte();
					}

					break;
				case 8: // 1.2.390 OK.
					ret.across = (slea.readByte() != 0);

					if (ret.across) {
						ret.acrossPosition = new Rectangle(slea.readInt(), slea.readInt(), slea.readInt(),
								slea.readInt());
					}

					break;
				case 9: // 1.2.390 OK.
					ret.across = (slea.readByte() != 0);

					if (ret.across) {
						ret.acrossPosition = new Rectangle(slea.readInt(), slea.readInt(), slea.readInt(),
								slea.readInt());

						slea.readInt();
					}

					break;
				case 10: // 1.2.390 OK.
					slea.readByte();
					break;
				case 11: // 1.2.390 OK.
					slea.readByte();
					break;
				case 12: // 1.2.390 OK.
					slea.readByte();
					break;
				case 13: // 1.2.390 OK.
					slea.readByte();
					break;
				case 14: // 1.2.390 OK.
					slea.readByte();
					break;
				case 15: // 1.2.390 OK.
					x = slea.readByte();

					if (x > 0) {
						int size = slea.readInt();

						for (int i = 0; i < size; i++) {
							slea.skip(16);
						}

						slea.skip(16);
					}

					break;
				case 19: // 1.2.390 OK.
					x = slea.readByte();

					if (x > 0) {
						slea.skip(4);
						slea.skip(4);
						slea.skip(4);
					}

					break;
				case 20: // 1.2.390 OK.
					slea.readByte();
					break;
				case 22: // 1.2.390 OK.
					slea.readByte();
					break;
				case 23: // 1.2.390 OK.
					slea.readByte();
					break;
				case 24: // 1.2.390 OK.
					x = slea.readByte();

					if (x > 0) {
						ret.mistPoints.add(new Point(slea.readInt(), slea.readInt()));
						ret.mistPoints.add(new Point(slea.readInt(), slea.readInt()));
						ret.mistPoints.add(new Point(slea.readInt(), slea.readInt()));
						ret.mistPoints.add(new Point(slea.readInt(), slea.readInt()));
					}

					break;
				case 25: // 1.2.390 OK.
					x = slea.readByte();

					if (x > 0) {
						int size = slea.readInt();

						for (int i = 0; i < size; i++) {
							slea.readInt();

							ret.mistPoints.add(new Point(slea.readInt(), slea.readInt()));
						}
					}

					break;
				case 29: // 1.2.390 OK.
					x = slea.readByte();

					if (x > 0) {
						slea.readInt();
					}

					break;
				case 34: // 1.2.390 OK.
					x = slea.readByte();

					if (x > 0) {
						int size = slea.readInt();

						for (int i = 0; i < size; i++) {
							ret.attackObjects
									.add(new Pair(Integer.valueOf(slea.readInt()), Integer.valueOf(slea.readInt())));
						}
					}

					break;
				case 37: // 1.2.390 OK.
					x = slea.readByte();

					if (x > 0) {
						slea.readInt();

						slea.skip(39);
					}

					break;
				case 39: // 1.2.390 OK.
					x = slea.readByte();
					if (x > 0) {
						ret.count = slea.readInt();

						slea.readInt();
					}

					break;
				case 42: // 1.2.390 OK.
					x = slea.readByte();

					if (x > 0) {
						int size = slea.readInt();

						for (int i = 0; i < size; i++) {
							slea.readInt();
						}
					}

					break;
				case 43: // 1.2.390 OK.
					x = slea.readByte();

					if (x > 0) {
						slea.readInt();
					}

					break;
				case 45: // 1.2.390 OK.
					x = slea.readByte();

					if (x > 0) {
						slea.readInt();
						slea.readInt();

						slea.readByte();

						slea.readInt();
						slea.readInt();
					}

					break;
				case 48: // 1.2.390 OK.
					x = slea.readByte();

					if (x > 0) {
						int size = slea.readInt();

						for (int i = 0; i < size; i++) {
							slea.skip(8);
						}
					}

					break;
				case 49: // 1.2.390 OK.
					x = slea.readByte();

					if (x > 0) {
						int size = slea.readInt();

						for (int i = 0; i < size; i++) {
							slea.skip(8);
						}
					}

					break;
				}
			} while (k != -1);
		}

		int unk1 = slea.readInt();
		int unk2 = slea.readInt();
		int bullet = slea.readInt();
		slea.skip(1); // 1.2.373
		slea.skip(4);
		slea.skip(1);
		slea.skip(4);
		slea.skip(1);
		slea.skip(1); // 1.2.388

		slea.readPos();
		slea.skip(1);
		if (skillid == 400051008) {
			slea.skip(1); // 빅 휴즈 기간틱 캐논 볼
		}
		slea.readPos();
		slea.skip(4);
		slea.skip(4);
		if (skillid == 3321004 || skillid == 400031034 || skillid == 3321038 || skillid == 400051016
				|| skillid == 400021028 || skillid == 400051003 || skillid == 27121201 || skillid == 3001004
				|| skillid == 63111003 || skillid == 63111103 || skillid == 64111004 || skillid == 3301008
				|| skillid == 400041018 || skillid == 37111006 || skillid == 152120003 || skillid == 400011081
				|| skillid == 400011082 || skillid == 400021070 || skillid == 152121004 || skillid == 400011004
				|| skillid == 400041043 || skillid == 400041020 || skillid == 63101004 || skillid == 63101100
				|| skillid == 12120022 || skillid == 12120023 || skillid == 400031026 || skillid == 400031058) { // 에인션트
																													// 아스트라
			slea.skip(27);
		} else if (skillid == 154121001) {
			slea.readInt();
			slea.readInt();
			slea.readInt();
			slea.readByte();
			slea.readByte();
			slea.readPos();
			slea.readPos();
			slea.readPos();
			slea.skip(3);
		} else if (skillid == 3311010) {
			slea.skip(19);
		} else {
			slea.skip(2);
		}
		List<Integer> data = new ArrayList<>();
		boolean enable2 = (slea.readByte() == 1);
		if (enable2) {
			data.add(slea.readInt());
			data.add(slea.readInt());
			data.add(slea.readInt());
			data.add(slea.readInt());
			data.add(slea.readInt());
			data.add(slea.readInt());
			data.add((int) slea.readByte()); // othello

		}
		if (skillid == 2341000 // 엔젤레이 VI 구현
				|| skillid == 3241006 // 인핸스 피어싱 VI 구현
				|| skillid == 3241007 // 얼티밋 피어싱 VI 구현
				|| skillid == 63141009 // 스캐터링 샷 VI 구현
		) {
			slea.skip(55); // 1.2.391 Changed.
		}

		int count = slea.readInt();
		ArrayList<MatrixSkill> skills = new ArrayList<MatrixSkill>();
		for (int i = 0; i < count; ++i) {
			MatrixSkill skill = new MatrixSkill(slea.readInt(), slea.readInt(), slea.readInt(), slea.readShort(),
					slea.readPos(), slea.readInt(), slea.readByte());
			byte unk5 = slea.readByte();
			int x = 0;
			int y = 0;
			if (unk5 > 0) {
				x = slea.readInt();
				y = slea.readInt();
			}
			skill.setUnk5(unk5, x, y);
			byte unk6 = slea.readByte();

			int x2 = 0;
			int y2 = 0;
			if (unk6 > 0) {
				x2 = slea.readInt();
				y2 = slea.readInt();
			}
			skill.setUnk6(unk6, x2, y2);
			skills.add(skill);
		}
		System.out.println(slea.toString());
		System.out.println("skillid : " + skillid + " level : " + level + " aa : " + aa + " k : " + k + " unk1 : "
				+ unk1 + " unk2 : " + unk2 + " bullet : " + bullet);
		/*
		 * int buffstat = 0x100; int pos = 10;
		 * 
		 * for (int flag = 0; flag < 999; flag++) { if ((1 << (31 - (flag % 32))) ==
		 * buffstat && pos == (byte) Math.floor(flag / 32)) { System.out.println(flag);
		 * } if ((1 << (31 - (flag % 32))) == buffstat && pos == (byte) (4 -
		 * Math.floor(flag / 32))) { System.out.println("mob " + flag); }
		 * 
		 * }
		 */
	}
}
