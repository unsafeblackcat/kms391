package server;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ServerProperties {

	private static final Properties props = new Properties();

	static {
		String toLoad = "Properties/channel.properties";
		loadProperties(toLoad);
		toLoad = "Properties/ports.properties";
		loadProperties(toLoad);
		toLoad = "Properties/database.properties";
		loadProperties(toLoad);
		toLoad = "Properties/world.properties";
		loadProperties(toLoad);
		toLoad = "Properties/connector.properties"; // CONNECTOR 소스
		loadProperties(toLoad);
		toLoad = "Properties/crc.properties"; // CRC 소스
		loadProperties(toLoad);
	}

	public static void loadProperties(String s) {
		try {
			FileReader fr = new FileReader(s);
			props.load(fr);
			fr.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static String getProperty(String s) {
		return props.getProperty(s);
	}

	public static void setProperty(String prop, String newInf) {
		props.setProperty(prop, newInf);
	}

	public static String getProperty(String s, String def) {
		return props.getProperty(s, def);
	}
}
