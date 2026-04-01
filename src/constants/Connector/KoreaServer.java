package constants.Connector;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import java.net.InetSocketAddress;

/**
 *
 * @author KoreaDev <koreadev2@nate.com> & sakura24<x-s-s-@nate.com>
 *
 */
public class KoreaServer {

	public static final int PORT = 18081;
	private static ServerBootstrap bootstrap;

	public static void run_startup_configurations() {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			bootstrap = new ServerBootstrap();
			bootstrap.group(group).channel(NioServerSocketChannel.class).localAddress(new InetSocketAddress(PORT))
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(new KoreaNettyHandler());
						}
					});
			ChannelFuture f = bootstrap.bind().sync();
			System.out.println("通知]連接器服務器已成功打開 " + PORT + " 埠.");
		} catch (InterruptedException e) {
			System.err.println("[錯誤]連接器服務器無法打開 " + PORT + " 埠.");
			e.printStackTrace();
		}
	}
}
