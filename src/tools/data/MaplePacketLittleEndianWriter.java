package tools.data;

import tools.HexTool;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

public class MaplePacketLittleEndianWriter {

	private final ByteArrayOutputStream baos;

	private static final Charset ASCII = Charset.forName("MS949");

	private int byteCount = 0;

	private byte[] bytes = new byte[8];

	public MaplePacketLittleEndianWriter() {
		this(32);
	}

	public MaplePacketLittleEndianWriter(int size) {
		this.baos = new ByteArrayOutputStream(size);
	}

	public final byte[] getPacket() {
		return this.baos.toByteArray();
	}

	public final String toString() {
		return HexTool.toString(this.baos.toByteArray());
	}

	public final void writeZeroBytes(int i) {
		for (int x = 0; x < i; x++) {
			this.baos.write(0);
		}
	}

	public final void writeBit(int b, int bit) {
		for (int i = 0; i < bit; i++) {
			this.bytes[this.byteCount] = (byte) (b >>> i & 0xFF & 0x1);
			this.byteCount++;
			if (this.byteCount == 8) {
				byte data = 0;
				for (int a = 0; a < 8; a++) {
					data = (byte) (data + (this.bytes[a] << a));
				}
				this.baos.write(data);
				this.byteCount = 0;
			}
		}
	}

	public final void write(byte[] b) {
		for (int x = 0; x < b.length; x++) {
			this.baos.write(b[x]);
		}
	}

	public final void write(boolean b) {
		this.baos.write(b ? 1 : 0);
	}

	public void write(byte b) {
		this.baos.write(b);
	}

	public void write(int b) {
		if (b != -88888) {
			this.baos.write((byte) b);
		}
	}

	public final void writeShort(int i) {
		this.baos.write((byte) (i & 0xFF));
		this.baos.write((byte) (i >>> 8 & 0xFF));
	}

	public final void writeInt(int i) {
		if (i != -88888) {
			this.baos.write((byte) (i & 0xFF));
			this.baos.write((byte) (i >>> 8 & 0xFF));
			this.baos.write((byte) (i >>> 16 & 0xFF));
			this.baos.write((byte) (i >>> 24 & 0xFF));
		}
	}

	public void writeInt(long i) {
		this.baos.write((byte) (int) (i & 0xFFL));
		this.baos.write((byte) (int) (i >>> 8L & 0xFFL));
		this.baos.write((byte) (int) (i >>> 16L & 0xFFL));
		this.baos.write((byte) (int) (i >>> 24L & 0xFFL));
	}

	public final void writeAsciiString(String s) {
		write(s.getBytes(ASCII));
	}

	public final void writeAsciiString(String s, int max) {
		if ((s.getBytes(ASCII)).length > max) {
			s = s.substring(0, max);
		}
		write(s.getBytes(ASCII));
		for (int i = (s.getBytes(ASCII)).length; i < max; i++) {
			write(0);
		}
	}
	
	public void writeFT(long expiration) throws IOException {
	    // 处理无过期时间的情况（-1L）
	    if (expiration == -1L) {
	        writeLong(-1L);  // 或其他特殊标记值 
	        return;
	    }
	    
	    // 校验并处理有效时间（假设需要非负）
	    if (expiration < 0) {
	        throw new IllegalArgumentException("Invalid expiration time: " + expiration);
	    }
	    
	    // 转换为目标格式（示例：直接写入long或转为FILETIME结构）
	    writeLong(expiration);  // 原始值写入 
	    // 或按Windows FILETIME格式处理（100纳秒单位，从1601-01-01计算）
	    // long fileTime = (expiration * 10_000) + 116444736000000000L;
	    // writeLong(fileTime);
	}

	public final void writeMapleAsciiString(String s) {
		writeShort((short) (s.getBytes(ASCII)).length);
		writeAsciiString(s);
	}

	public final void writePos(Point s) {
		writeShort(s.x);
		writeShort(s.y);
	}

	public void writePosInt(Point s) {
		writeInt(s.x);
		writeInt(s.y);
	}

	public final void writeNRect(Rectangle s) {
		writeInt(s.x);
		writeInt(s.y);
		writeInt(s.width);
		writeInt(s.height);
	}

	public final void writeRect(Rectangle s) {
		writeInt(s.x);
		writeInt(s.y);
		writeInt(s.x + s.width);
		writeInt(s.y + s.height);
	}

	public final void writeMapleAsciiString2(String s) {
		writeShort((short) (s.getBytes(Charset.forName("UTF-8"))).length);
		write(s.getBytes(Charset.forName("UTF-8")));
	}

	public final void writeLong(long l) {
		this.baos.write((byte) (int) (l & 0xFFL));
		this.baos.write((byte) (int) (l >>> 8L & 0xFFL));
		this.baos.write((byte) (int) (l >>> 16L & 0xFFL));
		this.baos.write((byte) (int) (l >>> 24L & 0xFFL));
		this.baos.write((byte) (int) (l >>> 32L & 0xFFL));
		this.baos.write((byte) (int) (l >>> 40L & 0xFFL));
		this.baos.write((byte) (int) (l >>> 48L & 0xFFL));
		this.baos.write((byte) (int) (l >>> 56L & 0xFFL));
	}

	public final void writeReversedLong(long l) {
		this.baos.write((byte) (int) (l >>> 32L & 0xFFL));
		this.baos.write((byte) (int) (l >>> 40L & 0xFFL));
		this.baos.write((byte) (int) (l >>> 48L & 0xFFL));
		this.baos.write((byte) (int) (l >>> 56L & 0xFFL));
		this.baos.write((byte) (int) (l & 0xFFL));
		this.baos.write((byte) (int) (l >>> 8L & 0xFFL));
		this.baos.write((byte) (int) (l >>> 16L & 0xFFL));
		this.baos.write((byte) (int) (l >>> 24L & 0xFFL));
	}

	public final void writeDouble(double d) {
		writeLong(Double.doubleToLongBits(d));
	}
}
