package org.stjs.bridge.cometd;

import org.stjs.javascript.annotation.STJSBridge;

/**
 * By default, CometD does not enforce a strict order on server-to-client message delivery, nor it provides the guarantee that messages sent by
 * the server are received by the clients.
 * <p/>
 * The message acknowledgment extension provides message ordering and message reliability to the Bayeux protocol for messages sent from server
 * to
 * client. This extension requires both a client-side extension and a server-side extension. The server-side extension is available in Java.
 * <p/>
 * In both Dojo and jQuery extension bindings, the extension is registered on the default cometd object under the name ack.
 * <p/>
 * Furthermore, you can programmatically disable/enable the extension before initialization by setting the ackEnabled boolean field on the
 * cometd
 * object:
 * <pre>
 * // Disables the ack extension during handshake
 * cometd.ackEnabled = false;
 * cometd.init(cometdURL);
 * </pre>
 * <p/>
 * To enable message acknowledgement, both client and server must indicate that they support message acknowledgement. This is negotiated during
 * handshake. On handshake, the client sends {"ext":{"ack": "true"}} to indicate that it supports message acknowledgement. If the server also
 * supports message acknowledgement, it likewise replies with {"ext":{"ack": "true"}}.
 * <p/>
 * The extension does not insert ack IDs in every message, as this would impose a significant burden on the server for messages sent to multiple
 * clients (which would need to be reserialized to JSON for each client). Instead the ack ID is inserted in the ext field of the /meta/connect
 * messages that are associated with message delivery. Each /meta/connect request contains the ack ID of the last received ack response:
 * "ext":{"ack": 42}. Similarly, each /meta/connect response contains an ext ack ID that uniquely identifies the batch of responses sent.
 * <p/>
 * If a /meta/connect message is received with an ack ID lower that any unacknowledged messages held by the extension, then these messages are
 * requeued prior to any more recently queued messages and the /meta/connect response sent with a new ack ID.
 * <p/>
 * It is important to note that message acknowledgement is guaranteed from server to client only, and not from client to server. This means that
 * the ack extension guarantees that messages sent by the server to the clients will be resent in case of temporary network failures that produce
 * loss of messages. It does not guarantee however, that messages sent by the client will reach the server.
 */
@STJSBridge(sources = "webjar:AckExtension.js")
public class AckExtension implements CometdExtension {
	public native void outgoing(BayeuxMessage message);
	public native void incoming(BayeuxMessage message);
	public native void registered(String name, Cometd cometd);
	public native void unregistered();
}
