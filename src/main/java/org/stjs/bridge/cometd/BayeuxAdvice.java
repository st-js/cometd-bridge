package org.stjs.bridge.cometd;

import org.stjs.javascript.Array;
import org.stjs.javascript.Map;
import org.stjs.javascript.annotation.SyntheticType;

/**
 * The advice field provides a way for servers to inform clients of their preferred mode of client operation so that in conjunction with
 * server-enforced limits, Bayeux implementations can prevent resource exhaustion and inelegant failure modes.
 * <p/>
 * The advice field is a JSON encoded object containing general and transport specific values that indicate modes of operation, timeouts and
 * other potential transport specific parameters. Fields may occur either in the top level of a message or within a transport specific section.
 * <p/>
 * Unless otherwise specified in sections 5 and 6, any Bayeux response message may contain an advice field. Advice received always superceeds
 * any
 * previous received advice.
 * <p/>
 * An example advice field is:
 * <p/>
 * <pre>
 * "advice": {
 *     "reconnect": "retry",
 *     "interval": 1000,
 *     "callback-polling": {
 *         "reconnect": "handshake"
 *     }
 * }
 * </pre>
 */
@SyntheticType
public class BayeuxAdvice extends Map<String, Object> {

	/**
	 * The reconnect advice field is a string that indicates how the client should act in the case of a failure to connect. Defined reconnect
	 * values are:
	 * <dl>
	 * <dt>retry</dt>
	 * <dd>a client MAY attempt to reconnect with a /meta/connect after the interval (as defined by "interval" advice or client-default
	 * backoff), and with the same credentials.</dd>
	 * <dt>handshake</dt>
	 * <dd>the server has terminated any prior connection status and the client MUST reconnect with a /meta/handshake message.
	 * A client MUST NOT automatically retry if handshake reconnect has been received.</dd>
	 * <dt>none</dt>
	 * <dd>hard failure for the connect attempt. Do not attempt to reconnect at all.</dd>
	 * </dl>
	 * <p/>
	 * A client MUST respect reconnect advice of none and MUST NOT automatically retry or handshake.
	 * <p/>
	 * Any client that does not implement all defined values of reconnect MUST NOT automatically retry or handshake.
	 */
	public String reconnect;

	/**
	 * An integer representing the minimum period in milliseconds for a client to delay subsequent requests to the /meta/connect channel.
	 * A negative period indicates that the message should not be retried.
	 * <p/>
	 * A client MUST implement interval support, but a client MAY exceed the interval provided by the server. A client SHOULD implement a
	 * backoff
	 * strategy to increase the interval if requests to the server fail without new advice being received from the server.
	 */
	public long interval;

	/**
	 * If present indicates a list of host names or IP addresses that MAY be used as alternate servers
	 * with which the client may connect. If a client receives advice to re-handshake and the current server is not included in a supplied hosts
	 * list, then the client SHOULD try the hosts in order until a successful connection is establish. Advice received during handshakes with
	 * hosts in the list supercedes any previously received advice.
	 */
	public Array<String> hosts;
}
