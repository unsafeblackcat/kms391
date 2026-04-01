package constants.Connector;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 *
 * @author KoreaDev <koreadev2@nate.com> & sakura24<x-s-s-@nate.com>
 *
 */
public class KoreaNettyHandler extends SimpleChannelInboundHandler<ByteBuf> {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		final String address = ctx.channel().remoteAddress().toString().split(":")[0];
		final String port = ctx.channel().remoteAddress().toString().split(":")[1];
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
		String str = msg.toString(CharsetUtil.UTF_8);
		String[] buf = str.split(",");
		handlePacket(buf, ctx);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	private void handlePacket(String[] header, ChannelHandlerContext ctx) {
		switch (header[0]) {
		case "0": {
			KoreaHandler.LoginRequest(header, ctx);
			break;
		}
		case "1": {
			KoreaHandler.RegisterRequest(header, ctx);
			break;
		}
		case "2": {
			KoreaHandler.CRCSkill(header, ctx);
			break;
		}
		case "3": {
			KoreaHandler.CRCCharacter(header, ctx);
			break;
		}
		case "4": {
			KoreaHandler.CRCItem(header, ctx);
			break;
		}
		case "5": {
			KoreaHandler.CRCEffect(header, ctx);
			break;
		}
		case "6": {
			KoreaHandler.GameStart(header, ctx);
			break;
		}
		case "7": {
			KoreaHandler.Notice(header, ctx);
			break;
		}
		case "8": {
			KoreaHandler.HeartBeat(header, ctx);
			break;
		}
		case "9": {
			KoreaHandler.CRCEtc(header, ctx);
			break;
		}
		}
	}
}
