package org.stjs.bridge.cometd;

import org.stjs.javascript.Map;
import org.stjs.javascript.functions.Callback1;

public class Cometd {

	/**
	 * Unregisters the transport with the specified name. This can be useful to force the use of
	 * only one transport (for example, for testing purposes), or to disable certain transports that might be
	 * unreliable.
	 * @param transportName the name of the transport to un-register
	 */
	public native void unregisterTransport(String transportName);

	/**
	 * Sets the URL of the Bayeux server. The URL of the server must be absolute (and therefore include the scheme,
	 * host, optionally the port and the path of the server). The scheme of the URL must always be either "http" or
	 * "https". The CometD JavaScript implementation will transparently take care of converting the scheme to "ws"
	 * or "wss" in case of usage of the WebSocket protocol.
	 * <pre>
	 * cometd.configure('http://localhost:8080/cometd');
	 * </pre>
	 * @param url the URL of the Bayeux server
	 */
	public native void configure(String url);

	public native void configure(CometdConfiguration configuration);

	public	 void disconnect() {
		throw new UnsupportedOperationException();
	}

	public boolean isDisconnected() {
		throw new UnsupportedOperationException();
	}

	public Object addListener(String string, Callback1<BayeuxMessage> callback1) {
		throw new UnsupportedOperationException();
	}

	public void removeListener(Object subscription) {
		throw new UnsupportedOperationException();
	}

	public void clearListeners() {
		throw new UnsupportedOperationException();
	}

	public void init(Map<String, String> $map) {
		throw new UnsupportedOperationException();
	}

	public Object subscribe(String channel, Callback1<BayeuxMessage> callback1, Map<String, Map<String, Map<String, String>>> $map) {
		throw new UnsupportedOperationException();
	}

	public void unsubscribe(Object subscription) {
		throw new UnsupportedOperationException();
	}

	public void clearSubscriptions() {
		throw new UnsupportedOperationException();
	}

	public void startBatch() {
		throw new UnsupportedOperationException();
	}

	public void endBatch() {
		throw new UnsupportedOperationException();
	}

}
