package org.stjs.bridge.cometd;

import org.stjs.javascript.annotation.SyntheticType;

@SyntheticType
public class CometdConfiguration {

	/**
	 * Parameter Name	Required	Default Value	Parameter Description
	 url	yes	 	The URL of the Bayeux server this client will connect to.
	 logLevel	no	info	The log level. Possible values are: "warn", "info", "debug". Output to window.console if available.
	 maxConnections	no	2	The maximum number of connections used to connect to the Bayeux server. Change this value only if you know exactly the client's connection limit and what "request queued behind long poll" means.
	 backoffIncrement	no	1000	The number of milliseconds that the backoff time increments every time a connection with the Bayeux server fails. CometD attempts to reconnect after the backoff time elapses.
	 maxBackoff	no	60000	The maximum number of milliseconds of the backoff time after which the backoff time is not incremented further.
	 reverseIncomingExtensions	no	true	Controls whether the incoming extensions are called in reverse order with respect to the registration order.
	 maxNetworkDelay	no	10000	The maximum number of milliseconds to wait before considering a request to the Bayeux server failed.
	 requestHeaders	no	{}	An object containing the request headers to be sent for every Bayeux request (for example, {"My-Custom-Header":"MyValue"}).
	 appendMessageTypeToURL	no	true	Determines whether or not the Bayeux message type (handshake, connect, disconnect) is appended to the URL of the Bayeux server (see above).
	 autoBatch	no	false	Determines whether multiple publishes that get queued are sent as a batch on the first occasion, without requiring explicit batching.
	 connectTimeout	no	0	The maximum number of milliseconds to wait for a WebSocket connection to be opened. It does not apply to HTTP connections. A timeout value of 0 means to wait forever.
	 stickyReconnect	no	true	Only applies to the WebSocket transport. Determines whether to stick using the WebSocket transport when a WebSocket transport failure has been detected after the WebSocket transport was able to successfully connect to the server.
	 */
}
