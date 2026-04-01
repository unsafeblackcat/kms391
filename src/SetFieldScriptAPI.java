import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import tools.data.ByteArrayByteStream;
import tools.data.LittleEndianAccessor;

public class SetFieldScriptAPI {

	private static int checkSize = 0;
	private static boolean isPrivate = false;
	private static LittleEndianAccessor slea;
	private static LittleEndianAccessor slea2;
	private static LittleEndianAccessor sleaReal;
	private static byte[] packet;
	private static byte[] packet2;
	private static String temp = "0x00 04 2E 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 04 00 00 00 00 04 01 00 00 00 00 00 00 00 00 04 13 00 00 00 00 00 00 00 00 04 0F 00 00 00 00 00 00 00 00 05 34 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 04 11 00 00 00 04 0B 00 00 00 04 08 00 00 00 04 03 00 00 00 04 14 00 00 00 04 1A 00 00 00 04 10 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 05 32 00 00 00 05 33 00 00 00 04 02 00 00 00 04 1F 00 00 00 04 1D 00 00 00 04 05 00 00 00 04 07 00 00 00 04 2D 00 00 00 04 04 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 05 35 00 00 00 05 36 00 00 00 00 00 00 00 00 04 27 00 00 00 04 D0 00 00 00 04 D1 00 00 00 04 1B 00 00 00 04 16 00 00 00 04 19 00 00 00 04 31 00 00 00 04 2A 00 00 00 04 23 00 00 00 04 0E 00 00 00 00 00 00 00 00 04 2F 00 00 00 04 0C 00 00 00 00 00 00 00 00 04 0D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 04 17 00 00 00 00 00 00 00 00 00 00 00 00 00 04 0A 00 00 00 04 12 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 04 09 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 2A 00 00 00 52 00 00 00 47 00 00 00 49 00 00 00 1D 00 00 00 53 00 00 00 4F 00 00 00 51 00 00 00 02 00 00 00 03 00 00 00 04 00 00 00 05 00 00 00 10 00 00 00 11 00 00 00 12 00 00 00 13 00 00 00 06 00 00 00 07 00 00 00 08 00 00 00 09 00 00 00 14 00 00 00 1E 00 00 00 1F 00 00 00 20 00 00 00 0A 00 00 00 0B 00 00 00 21 00 00 00 22 00 00 00 25 00 00 00 26 00 00 00 31 00 00 00 32 00 00 00";
	private static String temp2 = "0x00 04 2E 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 04 00 00 00 00 04 01 00 00 00 00 00 00 00 00 04 13 00 00 00 00 00 00 00 00 04 0F 00 00 00 00 00 00 00 00 05 34 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 04 11 00 00 00 04 0B 00 00 00 04 08 00 00 00 04 03 00 00 00 04 14 00 00 00 04 1A 00 00 00 04 10 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 05 32 00 00 00 05 33 00 00 00 04 02 00 00 00 04 1F 00 00 00 04 1D 00 00 00 04 05 00 00 00 04 07 00 00 00 04 2D 00 00 00 04 04 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 05 35 00 00 00 05 36 00 00 00 00 00 00 00 00 04 27 00 00 00 04 D0 00 00 00 04 D1 00 00 00 04 1B 00 00 00 04 16 00 00 00 04 19 00 00 00 04 31 00 00 00 04 2A 00 00 00 04 23 00 00 00 04 0E 00 00 00 00 00 00 00 00 04 2F 00 00 00 04 0C 00 00 00 00 00 00 00 00 04 0D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 04 17 00 00 00 00 00 00 00 00 00 00 00 00 00 04 0A 00 00 00 04 12 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 04 09 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 2A 00 00 00 52 00 00 00 47 00 00 00 49 00 00 00 1D 00 00 00 53 00 00 00 4F 00 00 00 51 00 00 00 02 00 00 00 03 00 00 00 04 00 00 00 05 00 00 00 10 00 00 00 11 00 00 00 12 00 00 00 13 00 00 00 06 00 00 00 07 00 00 00 08 00 00 00 09 00 00 00 14 00 00 00 1E 00 00 00 1F 00 00 00 20 00 00 00 0A 00 00 00 0B 00 00 00 21 00 00 00 22 00 00 00 25 00 00 00 26 00 00 00 31 00 00 00 32 00 00 00";
	private static FileReader reader;

	public static void main(String[] args) throws FileNotFoundException, IOException {
		reader = new FileReader("packet.txt");

		int ch;
		while ((ch = reader.read()) != -1) {
			temp += (char) ch;
		}

		temp = temp.replaceAll(" ", " 0x");
		temp2 = temp2.replaceAll(" ", " 0x");

		String[] strList = temp.split(" ");
		String[] strList2 = temp2.split(" ");

		packet = new byte[strList.length];
		packet2 = new byte[strList2.length];

		for (int i = 0; i < strList.length; i++) {
			packet[i] = Integer.decode(strList[i]).byteValue();
		}

		for (int i = 0; i < strList2.length; i++) {
			packet2[i] = Integer.decode(strList2[i]).byteValue();
		}

		slea = new LittleEndianAccessor(new ByteArrayByteStream(packet));
		slea2 = new LittleEndianAccessor(new ByteArrayByteStream(packet2));

		if (isPrivate) {
			sleaReal = slea;
		} else {
			sleaReal = slea2;
		}

		System.out.println("Available : " + sleaReal.available());

		/*
		 * int size = AddInt("size"); for (int i = 0; i < size; i++) { AddInt("1");
		 * AddByte("2"); AddByte("3"); AddLong("4"); int type = AddInt("5"); if (type ==
		 * 42) { AddString("6"); } else { AddLong("7"); } }
		 */

		for (int i = 0; i < 99; i++) {
			if (i % 99 == 0 && i != 0) {
				AddByte("0");
			}
			AddByte("a");
			AddInt("b");
		}

		System.out.println(sleaReal);

		System.out.println("Available : " + sleaReal.available());
	}

	public static byte AddByte(String name) {
		setCheckSize(getCheckSize() + 1);

		return sleaReal.readByte();
	}

	public static short AddShort(String name) {
		setCheckSize(getCheckSize() + 2);

		return sleaReal.readShort();
	}

	public static int AddInt(String name) {
		setCheckSize(getCheckSize() + 4);

		return sleaReal.readInt();
	}

	public static long AddLong(String name) {
		setCheckSize(getCheckSize() + 8);

		return sleaReal.readLong();
	}

	public static String AddString(String name) {
		String str = sleaReal.readMapleAsciiString();

		setCheckSize(getCheckSize() + (2 + str.length()));

		return str;
	}

	public static void AddField(String name, int loop) {
		for (int i = 0; i < loop; i++) {
			sleaReal.readByte();

			setCheckSize(getCheckSize() + 1);
		}
	}

	public static int getCheckSize() {
		return checkSize;
	}

	public static void setCheckSize(int checkSize) {
		SetFieldScriptAPI.checkSize = checkSize;
	}
}
