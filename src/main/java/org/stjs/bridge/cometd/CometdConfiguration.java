package org.stjs.bridge.cometd;

import org.stjs.javascript.Map;
import org.stjs.javascript.annotation.SyntheticType;

/**
 * A configuration object that can be passed to <tt>Cometd.configure()</tt> or <tt>Cometd.init()</tt>
 */
@SyntheticType
public class CometdConfiguration {

	/**
	 * The URL of the Bayeux server this client will connect to. he URL of the server must be absolute (and therefore include the scheme,
	 * host, optionally the port and the path of the server). The scheme of the URL must always be either "http" or
	 * "https". The CometD JavaScript implementation will transparently take care of converting the scheme to "ws"
	 * or "wss" in case of usage of the WebSocket protocol.
	 */
	public String url;

	/**
	 * The log level. Possible values are: "warn", "info", "debug". Output to window.console if available.
	 * The default value is "info"
	 */
	public String logLevel;

	/**
	 * The maximum number of connections used to connect to the Bayeux server. Change this value only if you know exactly
	 * the client's connection limit and what "request queued behind long poll" means.
	 * The default value is 2
	 */
	public int maxConnections;

	/**
	 * The number of milliseconds that the backoff time increments every time a connection with the Bayeux server fails. CometD attempts
	 * to reconnect after the backoff time elapses.
	 * The default value is 1000 (1 second)
	 */
	public long backoffIncrement;

	/**
	 * The maximum number of milliseconds of the backoff time after which the backoff time is not incremented further.
	 * The default value is 60000 (1 minute)
	 */
	public long maxBackoff;

	/**
	 * Controls whether the incoming extensions are called in reverse order with respect to the registration order.
	 * The default value is true
	 */
	public boolean reverseIncomingExtensions;

	/**
	 * The maximum number of milliseconds to wait before considering a request to the Bayeux server failed.
	 * The default value is 10000 (10 seconds)
	 */
	public long maxNetworkDelay;

	/**
	 * A JS map containing the request headers to be sent for every Bayeux request (for example, {"My-Custom-Header":"MyValue"}).
	 * The default value is the empty map
	 */
	public Map<String, String> requestHeaders;

	/**
	 * Determines whether or not the Bayeux message type (handshake, connect, disconnect) is appended to the URL of the Bayeux server (see above).
	 * The default value is true
	 */
	public boolean appendMessageTypeToURL;

	/**
	 * Determines whether multiple publishes that get queued are sent as a batch on the first occasion, without requiring explicit batching.
	 * The default value is false
	 */
	public boolean autoBatch;

	/**
	 * The maximum number of milliseconds to wait for a WebSocket connection to be opened. It does not apply to HTTP connections. A timeout
	 * value of 0 means to wait forever.
	 * The default value is 0
	 */
	public long connectTimeout;

	/**
	 * Only applies to the WebSocket transport. Determines whether to stick using the WebSocket transport when a WebSocket transport failure
	 * has been detected after the WebSocket transport was able to successfully connect to the server.
	 * The default value is true
	 */
	public boolean stickyReconnect;

}
