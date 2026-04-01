// CONNECTOR 소스

package security.connector;

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

public class ConnectorServer {

	public static final int PORT = 29870;

	public static Map<String, ConnectorClient> connectorClients = new HashMap<>();
	public static Map<String, MapleClient> mapleClients = new HashMap<>();

	public static boolean on;
	public static String serverFileHash;

	public static void run_startup_configurations() {
		on = ServerProperties.getProperty("connector.on").equals("true");
		serverFileHash = ServerProperties.getProperty("connector.hash");

		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			ServerBootstrap b = new ServerBootstrap();

			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {

						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast("frame", new DelimiterBasedFrameDecoder(1024,
									Unpooled.copiedBuffer("</EP>", Charset.forName("UTF-8"))));
							ch.pipeline().addLast("decoder", new StringDecoder(Charset.forName("UTF-8")));
							ch.pipeline().addLast("encoder", new StringEncoder(Charset.forName("UTF-8")));
							ch.pipeline().addLast("handler", new ConnectorNettyHandler());
						}
					}).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.TCP_NODELAY, true)
					.childOption(ChannelOption.SO_SNDBUF, 4096 * 1024).childOption(ChannelOption.SO_KEEPALIVE, true);

			b.bind(PORT).sync();

			System.out.println("[安全連接器服務器] " + PORT + " 埠已打開");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
