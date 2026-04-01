package handling.netty;

import client.MapleClient;
import constants.ServerConstants;
import handling.SendPacketOpcode;
import static handling.SendPacketOpcode.MOVE_MONSTER_RESPONSE;
import static handling.SendPacketOpcode.SPAWN_MONSTER;
import static handling.SendPacketOpcode.SPAWN_MONSTER_CONTROL;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import tools.HexTool;
import tools.MapleAESOFB;

import java.util.concurrent.locks.Lock;
import tools.data.ByteArrayByteStream;
import tools.data.LittleEndianAccessor;

public class MapleNettyEncoder extends MessageToByteEncoder<byte[]> {

	protected void encode(ChannelHandlerContext ctx, byte[] msg, ByteBuf buffer) throws Exception {
		MapleClient client = (MapleClient) ctx.channel().attr(MapleClient.CLIENTKEY).get();
		if (client != null) {
			Lock mutex = client.getLock();
			mutex.lock();
			try {
				LittleEndianAccessor slea = new LittleEndianAccessor(new ByteArrayByteStream(msg));
				int header_num = slea.readShort();
				if (header_num != SendPacketOpcode.SPAWN_MONSTER.getValue()
						&& header_num != SendPacketOpcode.SPAWN_MONSTER_CONTROL.getValue()
						&& header_num != SendPacketOpcode.BLOCK_ATTACK.getValue()
						&& header_num != SendPacketOpcode.MOVE_MONSTER_RESPONSE.getValue()
						&& header_num != SendPacketOpcode.MOVE_MONSTER.getValue()
						&& header_num != SendPacketOpcode.UPDATE_STATS.getValue()
						&& header_num != SendPacketOpcode.SHOW_STATUS_INFO.getValue()
						&& header_num != SendPacketOpcode.UPDATE_PARTYMEMBER_HP.getValue()
						&& header_num != SendPacketOpcode.NPC_ACTION.getValue()
						&& header_num != SendPacketOpcode.REMOVE_ITEM_FROM_MAP.getValue()
						&& header_num != SendPacketOpcode.GIVE_BUFF.getValue()
						&& header_num != SendPacketOpcode.COOLDOWN.getValue()
						&& header_num != SendPacketOpcode.CANCEL_BUFF.getValue()
						&& header_num != SendPacketOpcode.INGAME_PING.getValue()) {
				}
				if (ServerConstants.DEBUG_SEND) {
					int opcode = ((int) (msg[1] & 0xFF) * 0x100) + (int) (msg[0] & 0xFF);

					if (MOVE_MONSTER_RESPONSE.getValue() != opcode && SPAWN_MONSTER.getValue() != opcode
							&& SPAWN_MONSTER_CONTROL.getValue() != opcode) {
						client.lastPacketSendOpcode = SendPacketOpcode.getOpcodeName(opcode) + " / "
								+ HexTool.toString(msg);

					}
				}

				MapleAESOFB send_crypto = client.getSendCrypto();
				buffer.writeBytes(send_crypto.getPacketHeader(msg.length));
				buffer.writeBytes(send_crypto.crypt(msg));
				ctx.flush();
			} finally {
				mutex.unlock();
			}
		} else {
			buffer.writeBytes(msg);
		}
	}
}
