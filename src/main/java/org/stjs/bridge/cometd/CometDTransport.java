package org.stjs.bridge.cometd;

import org.stjs.javascript.Array;
import org.stjs.javascript.TimeoutHandler;
import org.stjs.javascript.functions.Callback0;

public class CometDTransport {

	/**
	 * Function invoked just after a transport has been successfully registered.
	 *
	 * @param type   the type of transport (for example 'long-polling')
	 * @param cometd the cometd object this transport has been registered to
	 * @see #unregistered()
	 */
	public native void registered(String type, CometD cometd);

	/**
	 * Function invoked just after a transport has been successfully unregistered.
	 *
	 * @see #registered(String, CometD)
	 */
	public native void unregistered();

	public native CometDConfig getConfiguration();

	public native BayeuxAdvice getAdvice();

	public native TimeoutHandler setTimeout(Callback0 funktion, long delay);

	public native void clearTimeout(TimeoutHandler handle);

	/**
	 * Converts the given response into an array of bayeux messages
	 *
	 * @param response the response to convert
	 * @return an array of bayeux messages obtained by converting the response
	 */
	public native Array<BayeuxMessage> convertToMessages(String response);

	public native Array<BayeuxMessage> convertToMessages(Array<BayeuxMessage> response);

	public native Array<BayeuxMessage> convertToMessages(BayeuxMessage response);

	/**
	 * Returns whether this transport can work for the given version and cross domain communication case.
	 *
	 * @param version     a string indicating the transport version
	 * @param crossDomain a boolean indicating whether the communication is cross domain
	 * @return true if this transport can work for the given version and cross domain communication case,
	 * false otherwise
	 */
	public native boolean accept(String version, boolean crossDomain, String url);

	/**
	 * Returns the type of this transport.
	 *
	 * @see #registered(String, CometD)
	 */
	public native String getType();

	/**
	 * Send the specified message to the Bayeux server
	 * @param envelope The transport specific message format that wraps a standard Bayeux message.
	 * @param metaConnect true if the message in the envelope is for the /meta/connect channel.
	 */
	public native void send(Object envelope, boolean metaConnect);

	public native void reset();

	public native void abort();

}
