package org.stjs.bridge.cometd;

import org.stjs.javascript.Array;
import org.stjs.javascript.Date;
import org.stjs.javascript.annotation.STJSBridge;

/**
 * The timesync extension uses the messages exchanged between a client and a server to calculate the offset between the client’s clock and the
 * server’s clock. This is independent from the timestamp extension section, which uses the local clock for all timestamps. This extension
 * requires both a client-side extension and a server-side extension. The server-side extension is available in Java.
 * <p/>
 * In both Dojo and jQuery extension bindings, the extension is registered on the default cometd object under the name "timesync".
 * <p/>
 * The timesync extension allows the client and server to exchange time information on every handshake and connect message so that the client
 * can
 * calculate an approximate offset from its own clock epoch to that of the server. The algorithm used is very similar to the NTP algorithm.
 * <p/>
 * With each handshake or connect, the extension sends timestamps within the ext field, for example:
 * <p/>
 * <pre>{ext:{timesync:{tc:12345567890,l:23,o:4567},...},...}</pre>
 * <p/>
 * where:
 * <dl>
 * <dt>tc<dt>
 * <dd>the client timestamp in ms since 1970 of when the message was sent</dd>
 * <dt>l</dt>
 * <dd>the network lag that the client has calculated</dd>
 * <dt>o</dt>
 * <dd>the clock offset that the client has calculated</dd>
 * <p/>
 * You can calculate the accuracy of the offset and lag with <tt>tc-now-l-o</tt>, which should be zero if the calculated offset and lag are
 * perfectly accurate. A Bayeux server that supports timesync should respond only if the measured accuracy value is greater than accuracy target.
 * <p/>
 * The response is an ext field, for example:
 * <p/>
 * <pre>{ext:{timesync:{tc:12345567890,ts:1234567900,p:123,a:3},...},...}</pre>
 * <p/>
 * where:
 * <dl>
 * <dt>tc</dt>
 * <dd>the client timestamp of when the message was sent</dd>
 * <dt>ts</dt>
 * <dd>the server timestamp of when the message was received</dd>
 * <dt>p</dt>
 * <dd>the poll duration in ms – ie the time the server took before sending the response</dd>
 * <dt>a</dt>
 * <dd>is the measured accuracy of the calculated offset and lag sent by the client</dd>
 * <p/>
 * </dl>
 * On receipt of the response, the client is able to use current time to determine the total trip time, from which it subtracts p to determine
 * an
 * approximate two way network traversal time. Thus:
 * <p/>
 * <pre>lag = (now-tc-p)/2</pre>
 * <p/>
 * <pre>offset = ts-tc-lag</pre>
 */
@STJSBridge(sources = "webjar:TimeSyncExtension.js")
public class TimeSyncExtension implements CometdExtension {

	/**
	 * Get the estimated offset in ms from the clients clock to the
	 * servers clock.  The server time is the client time plus the offset.
	 */
	public native long getTimeOffset();

	/**
	 * Get an array of multiple offset samples used to calculate
	 * the offset.
	 */
	public native Array<Long> getTimeOffsetSamples();

	/**
	 * Get the estimated network lag in ms from the client to the server.
	 */
	public native long getNetworkLag();

	/**
	 * Get the estimated server time in ms since the epoch.
	 */
	public native long getServerTime();

	/**
	 * Get the estimated server time as a Date object
	 */
	public native Date getServerDate();

	public native void outgoing(BayeuxMessage message);

	public native void incoming(BayeuxMessage message);

	public native void registered(String name, Cometd cometd);

	public native void unregistered();
}
