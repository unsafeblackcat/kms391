// CONNECTOR 소스

package security.connector;

import client.MapleClient;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import java.util.concurrent.ScheduledFuture;
import server.Timer;

public class ConnectorClient {

	public static final AttributeKey<ConnectorClient> CONNECTORKEY = AttributeKey.valueOf("connector_netty");

	private transient Channel session;

	private ScheduledFuture<?> connectorHeartBeatTask;
	private boolean connectorHeartBeatStop;
	private boolean connectorHeartBeat;

	public ConnectorClient(Channel session, boolean connectorHeartBeatStop) {
		this.session = session;

		this.connectorHeartBeat = connectorHeartBeatStop;
	}

	public Channel getSession() {
		return session;
	}

	public MapleClient getMapleClient() {
		return ConnectorServer.mapleClients.get(session.remoteAddress().toString().split(":")[0]);
	}

	public boolean isConnectorHeartBeat() {
		return connectorHeartBeat;
	}

	public void setConnectorHeartBeat(boolean ConnectorHeartBeat) {
		this.connectorHeartBeat = ConnectorHeartBeat;
	}

	public void cancelConnectorHeartBeat() {
		if (this.connectorHeartBeatTask != null) {
			this.connectorHeartBeatTask.cancel(true);
			this.connectorHeartBeatTask = null;
		}

		this.setConnectorHeartBeatStop(true);
	}

	public boolean isConnectorHeartBeatStop() {
		return connectorHeartBeatStop;
	}

	public void setConnectorHeartBeatStop(boolean connectorHeartBeatStop) {
		this.connectorHeartBeatStop = connectorHeartBeatStop;
	}

	public void checkConnectorHeartBeat() {
		connectorHeartBeatTask = Timer.PingTimer.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (connectorHeartBeatTask != null) {
					if (!isConnectorHeartBeatStop()) {
						if (!isConnectorHeartBeat()) {
							cancelConnectorHeartBeat();

							System.err.println("[Connector Server] " + session.remoteAddress().toString().split(":")[0]
									+ " 하트비트 우회 감지, 접속 끊기");

							getMapleClient().disconnect(true, false);
							getMapleClient().getSession().close();

							getSession().close();
							return;
						}

						setConnectorHeartBeat(false);
					}

					checkConnectorHeartBeat();
				}
			}
		}, 15000);
	}
}