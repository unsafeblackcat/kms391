// CONNECTOR 소스

package security.connector;

import handling.RecvPacketOpcode;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import tools.data.ByteArrayByteStream;
import tools.data.LittleEndianAccessor;

public class ConnectorNettyHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelActive(final ChannelHandlerContext ctx) {
		String address = ctx.channel().remoteAddress().toString().split(":")[0];

		// System.out.println("[Security Connector Server] " + address + " 연결");

		ConnectorClient client = new ConnectorClient(ctx.channel(), true);

		client.checkConnectorHeartBeat();

		ConnectorServer.connectorClients.put(address, client);

		ctx.channel().attr(ConnectorClient.CONNECTORKEY).set(client);
	}

	@Override
	public void channelInactive(final ChannelHandlerContext ctx) {
		String address = ctx.channel().remoteAddress().toString().split(":")[0];

		// System.out.println("[Security Connector Server] " + address + " 연결 해제");

		ConnectorClient client = ctx.channel().attr(ConnectorClient.CONNECTORKEY).get();

		if (client != null) {
			client.cancelConnectorHeartBeat();

			if (client.getMapleClient() != null) {
				client.getMapleClient().disconnect(true, false);
				client.getMapleClient().getSession().close();
			}

			ConnectorServer.connectorClients.remove(address);
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ConnectorClient client = ctx.channel().attr(ConnectorClient.CONNECTORKEY).get();

		LittleEndianAccessor lea = new LittleEndianAccessor(new ByteArrayByteStream(msg.toString().getBytes()));

		short header = lea.readShort();

		for (final RecvPacketOpcode recv : RecvPacketOpcode.values()) {
			if (recv.getValue() == header) {
				ConnectorServerHandler.handlePacket(recv, lea, client);
				return;
			}
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		// cause.printStackTrace();
		ctx.close();
	}
}