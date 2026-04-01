// CRC 소스

package security.crc;

import handling.RecvPacketOpcode;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import tools.data.ByteArrayByteStream;
import tools.data.LittleEndianAccessor;

public class CRCNettyHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelActive(final ChannelHandlerContext ctx) {
		String address = ctx.channel().remoteAddress().toString().split(":")[0];

		// System.out.println("[Security CRC Server] " + address + " 연결");

		CRCClient client = new CRCClient(ctx.channel(), true);

		client.checkCRCHeartBeat();

		CRCServer.crcClients.put(address, client);

		ctx.channel().attr(CRCClient.CRCKEY).set(client);
	}

	@Override
	public void channelInactive(final ChannelHandlerContext ctx) {
		String address = ctx.channel().remoteAddress().toString().split(":")[0];

		// System.out.println("[Security CRC Server] " + address + " 연결 해제");

		CRCClient client = ctx.channel().attr(CRCClient.CRCKEY).get();

		if (client != null) {
			client.cancelCRCHeartBeat();

			if (client.getMapleClient() != null) {
				client.getMapleClient().disconnect(true, false);
				client.getMapleClient().getSession().close();
			}

			CRCServer.crcClients.remove(address);
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		CRCClient client = ctx.channel().attr(CRCClient.CRCKEY).get();

		LittleEndianAccessor lea = new LittleEndianAccessor(new ByteArrayByteStream(msg.toString().getBytes()));

		short header = lea.readShort();

		for (final RecvPacketOpcode recv : RecvPacketOpcode.values()) {
			if (recv.getValue() == header) {
				CRCServerHandler.handlePacket(recv, lea, client);
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