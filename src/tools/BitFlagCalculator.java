package tools;

import client.SecondaryStat;
import constants.GameConstants;
import tools.data.ByteArrayByteStream;
import tools.data.LittleEndianAccessor;
import tools.data.MaplePacketLittleEndianWriter;
import tools.packet.PacketHelper;

import java.util.ArrayList;
import java.util.Scanner;

public class BitFlagCalculator {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("패킷 넣기 : ");
		String giveBuffPacket = scanner.nextLine();
		// String giveBuffPacket = "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
		// 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
		// 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
		// 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 04 00 00 00 00 00
		// 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
		// 00 00 00 00 00 00 00 00 00 00 00 00 B7 7B 1D 00 29 8E F7 01 00 00 00 00 00 00
		// 00 00 00 01 01 01 01 00 00 00 00 01";

		LittleEndianAccessor giveBuffStream = new LittleEndianAccessor(
				new ByteArrayByteStream(HexTool.getByteArrayFromHexString(giveBuffPacket)));
		StringBuilder extractedInformation = new StringBuilder();

		extractedInformation.append("총 크기:").append(giveBuffStream.available()).append("\n");

		extractedInformation.append("더미 int * 2 제거크기:").append(giveBuffStream.available()).append("\n\n");

		// 버프마스크

		for (int i = GameConstants.MAX_BUFFSTAT; i >= 1; i--) {
			int buffMask = giveBuffStream.readInt();

			if (buffMask > 0) {
				extractedInformation.append("포지션: ").append(i).append(" ");
				extractedInformation.append("버프마스크: ").append(buffMask);
				for (int j = 0; j <= 31; j++) {
					int bitMask = 1 << j;
					if ((buffMask & bitMask) == bitMask) {
						int bitFlag = getBitFlag(i, j);

						extractedInformation.append("\n  ");
						extractedInformation.append("이름: " + SecondaryStat.getByFlag(bitFlag)).append(" ");
						extractedInformation.append("플래그: ").append(31 - j).append(" ");
						extractedInformation.append("비트 플래그:").append(bitFlag);
					}
				}
				extractedInformation.append("\n").append("\n");
			}
		}
		extractedInformation.append("비트마스킹 이후 크기: ").append(giveBuffStream.available());
		System.out.println(extractedInformation);
		System.out.println(giveBuffStream.toString());
	}

	public static int getBitFlag(int position, int buffMaskFlag) {
		return (31 - position) * 32 + (31 - buffMaskFlag);
	}

	public static String makeDummyData(int dummyQuantity) {
		MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
		ArrayList<SecondaryStat> dummySecondaryStat = new ArrayList<>();

		mplew.writeInt(0);
		mplew.writeInt(0);
		for (int i = 0; i < dummyQuantity; i++) {
			SecondaryStat secondaryStat = SecondaryStat.getRandom();
			System.out.println(secondaryStat + " 더미 생성됨");
			dummySecondaryStat.add(secondaryStat);
		}
		PacketHelper.writeBuffMask(mplew, dummySecondaryStat);
		System.out.println("");
		return mplew.toString();
	}
}