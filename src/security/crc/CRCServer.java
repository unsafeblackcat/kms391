// CRC 소스

package security.crc;

import client.MapleClient;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import server.ServerProperties;

public class CRCServer {

	public static final int PORT = 3375;

	public static Map<String, CRCClient> crcClients = new HashMap<>();
	public static Map<String, MapleClient> mapleClients = new HashMap<>();

	public static boolean on;
	public static String serverMemoryHash;

	public static void run_startup_configurations() {
		on = ServerProperties.getProperty("crc.on").equals("true");
		serverMemoryHash = ServerProperties.getProperty("crc.hash");

		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			ServerBootstrap b = new ServerBootstrap();

			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {

						@Override
						public void initChannel(SocketChannel ch) {
							ch.pipeline().addLast("frame", new DelimiterBasedFrameDecoder(1024,
									Unpooled.copiedBuffer("</EP>", Charset.forName("UTF-8"))));
							ch.pipeline().addLast("decoder", new StringDecoder(Charset.forName("UTF-8")));
							ch.pipeline().addLast("encoder", new StringEncoder(Charset.forName("UTF-8")));
							ch.pipeline().addLast("handler", new CRCNettyHandler());
						}
					}).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.TCP_NODELAY, true)
					.childOption(ChannelOption.SO_SNDBUF, 4096 * 1024).childOption(ChannelOption.SO_KEEPALIVE, true);

			b.bind(PORT).sync();

			System.out.println("[安全CRC服務器] " + PORT + " 埠已打開");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}