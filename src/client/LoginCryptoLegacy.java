package client;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class LoginCryptoLegacy {
	// 随机数生成器
	private static final Random rand = new Random();
	// Base64 编码字符集
	private static final char[] iota64 = new char[64];

	static {
		int i = 0;
		iota64[i++] = '.';
		iota64[i++] = '/';
		for (char c = 'A'; c <= 'Z'; c++) {
			iota64[i++] = c;
		}
		for (char c = 'a'; c <= 'z'; c++) {
			iota64[i++] = c;
		}
		for (char c = '0'; c <= '9'; c++) {
			iota64[i++] = c;
		}
	}

	/**
	 * 对密码进行哈希处理
	 * 
	 * @param password 要哈希的密码
	 * @return 哈希后的密码
	 */
	public static String hashPassword(String password) {
		byte[] randomBytes = new byte[6];
		rand.setSeed(System.currentTimeMillis());
		rand.nextBytes(randomBytes);
		String salt = genSalt(randomBytes);
		try {
			return myCrypt(password, salt);
		} catch (NoSuchAlgorithmException | java.io.UnsupportedEncodingException e) {
			throw new RuntimeException("Error hashing password", e);
		}
	}

	/**
	 * 检查密码是否匹配
	 * 
	 * @param password 要检查的密码
	 * @param hash     存储的哈希密码
	 * @return 如果密码匹配返回 true，否则返回 false
	 */
	public static boolean checkPassword(String password, String hash) {
		try {
			return myCrypt(password, hash).equals(hash);
		} catch (NoSuchAlgorithmException | java.io.UnsupportedEncodingException e) {
			throw new RuntimeException("Error checking password", e);
		}
	}

	/**
	 * 检查哈希密码是否为旧格式
	 * 
	 * @param hash 要检查的哈希密码
	 * @return 如果是旧格式返回 true，否则返回 false
	 */
	public static boolean isLegacyPassword(String hash) {
		return hash.startsWith("$H$");
	}

	/**
	 * 对密码进行加密处理
	 * 
	 * @param password 要加密的密码
	 * @param seed     盐值
	 * @return 加密后的密码
	 * @throws NoSuchAlgorithmException             如果不支持指定的哈希算法
	 * @throws java.io.UnsupportedEncodingException 如果不支持指定的字符编码
	 */
	private static String myCrypt(String password, String seed)
			throws NoSuchAlgorithmException, java.io.UnsupportedEncodingException {
		if (!seed.startsWith("$H$")) {
			byte[] randomBytes = new byte[6];
			rand.nextBytes(randomBytes);
			seed = genSalt(randomBytes);
		}
		String salt = seed.substring(4, 12);
		if (salt.length() != 8) {
			throw new IllegalArgumentException("Error hashing password - Invalid seed.");
		}

		MessageDigest digester = MessageDigest.getInstance("SHA-1");
		byte[] saltBytes = salt.getBytes("iso-8859-1");
		byte[] passwordBytes = password.getBytes("iso-8859-1");

		digester.update(saltBytes);
		digester.update(passwordBytes);
		byte[] sha1Hash = digester.digest();

		int count = 8;
		while (count > 0) {
			byte[] combinedBytes = new byte[sha1Hash.length + passwordBytes.length];
			System.arraycopy(sha1Hash, 0, combinedBytes, 0, sha1Hash.length);
			System.arraycopy(passwordBytes, 0, combinedBytes, sha1Hash.length, passwordBytes.length);
			sha1Hash = digester.digest(combinedBytes);
			count--;
		}

		return seed.substring(0, 12) + encode64(sha1Hash);
	}

	/**
	 * 生成盐值
	 * 
	 * @param arrayOfByte 随机字节数组
	 * @return 生成的盐值
	 */
	private static String genSalt(byte[] arrayOfByte) {
		StringBuilder salt = new StringBuilder("$H$");
		salt.append(iota64[30]);
		salt.append(encode64(arrayOfByte));
		return salt.toString();
	}

	/**
	 * 将字节数组转换为十六进制字符串
	 * 
	 * @param data 要转换的字节数组
	 * @return 转换后的十六进制字符串
	 */
	private static String convertToHex(byte[] data) {
		StringBuilder buf = new StringBuilder();
		for (byte b : data) {
			buf.append(String.format("%02x", b));
		}
		return buf.toString();
	}

	/**
	 * 对文本进行 SHA-1 编码
	 * 
	 * @param text 要编码的文本
	 * @return 编码后的十六进制字符串
	 * @throws NoSuchAlgorithmException             如果不支持 SHA-1 算法
	 * @throws java.io.UnsupportedEncodingException 如果不支持指定的字符编码
	 */
	public static String encodeSHA1(String text) throws NoSuchAlgorithmException, java.io.UnsupportedEncodingException {
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		byte[] textBytes = text.getBytes("iso-8859-1");
		md.update(textBytes);
		return convertToHex(md.digest());
	}

	/**
	 * 对字节数组进行 Base64 编码
	 * 
	 * @param input 要编码的字节数组
	 * @return 编码后的字符串
	 */
	private static String encode64(byte[] input) {
		int iLen = input.length;
		int oDataLen = (iLen * 4 + 2) / 3;
		int oLen = (iLen + 2) / 3 * 4;
		char[] out = new char[oLen];
		int ip = 0;
		int op = 0;
		while (ip < iLen) {
			int i0 = input[ip++] & 0xFF;
			int i1 = ip < iLen ? input[ip++] & 0xFF : 0;
			int i2 = ip < iLen ? input[ip++] & 0xFF : 0;
			int o0 = i0 >>> 2;
			int o1 = (i0 & 0x3) << 4 | i1 >>> 4;
			int o2 = (i1 & 0xF) << 2 | i2 >>> 6;
			int o3 = i2 & 0x3F;
			out[op++] = iota64[o0];
			out[op++] = iota64[o1];
			out[op] = op < oDataLen ? iota64[o2] : '=';
			op++;
			out[op] = op < oDataLen ? iota64[o3] : '=';
			op++;
		}
		return new String(out);
	}
}