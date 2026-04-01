package handling.cashshop;

import constants.ServerType;
import handling.channel.PlayerStorage;
import handling.netty.MapleNettyDecoder;
import handling.netty.MapleNettyEncoder;
import handling.netty.MapleNettyHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import server.ServerProperties;

public class CashShopServer {

	private static String ip;
	private static final int PORT = Integer.parseInt(ServerProperties.getProperty("ports.cashshop"));

	private static PlayerStorage players;
	private static boolean finishedShutdown = false;
	private static ServerBootstrap bootstrap;

	public static final void run_startup_configurations() {
		players = new PlayerStorage();
		ip = ServerProperties.getProperty("world.host") + ":" + PORT;
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast("decoder", new MapleNettyDecoder());
							ch.pipeline().addLast("encoder", new MapleNettyEncoder());
							ch.pipeline().addLast("handler", new MapleNettyHandler(ServerType.CASHSHOP, -1));
						}
					}).option(ChannelOption.SO_BACKLOG, 0x80).childOption(ChannelOption.SO_KEEPALIVE, true);
			ChannelFuture f = bootstrap.bind(PORT).sync();
			System.out.println("[通知]緩存商店服務器已成功打開 " + PORT + " 埠.");
		} catch (InterruptedException e) {
			System.err.println("[錯誤]緩存商店服務器無法打開 " + PORT + " 埠.");
		}
	}

	public static final String getIP() {
		return ip;
	}

	public static final PlayerStorage getPlayerStorage() {
		return players;
	}

	public static final void shutdown() {
		if (finishedShutdown) {
			return;
		}
		System.out.println("Saving all connected clients (CS)...");
		players.disconnectAll();
		System.out.println("Shutting down CS...");
		finishedShutdown = true;
	}

	public static boolean isShutdown() {
		return finishedShutdown;
	}
}
