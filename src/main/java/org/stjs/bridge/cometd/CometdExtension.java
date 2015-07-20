package org.stjs.bridge.cometd;

/**
 * The CometD implementation includes the ability to add/remove extensions. An extension is a function that CometD calls; it allows you to modify
 * a message just before sending it (an outgoing extension) or just after receiving it (an incoming extension).
 * <p/>
 * An extension normally adds fields to the message being sent or received in the ext object that the Bayeux protocol specification defines.
 * <p/>
 * An extension is not a way to add business fields to a message, but rather a way to process all messages, including the meta messages the
 * Bayeux protocol uses, and to extend the Bayeux protocol itself. An extension should address concerns that are orthogonal to the business, but
 * that provide value to the application. Typical examples of such concerns is to guarantee message ordering, to guarantee message delivery, to
 * make sure that if the user navigates to another CometD page or reloads the same CometD page in the browser, the same CometD session is used
 * without having to go through a disconnect/handshake cycle, to add to every message metadata fields such as the timestamp, to detect whether
 * the client and the server have a time offset (for example when only one of them is synchronized with NTP), etc.
 * <p/>
 * If you do not have such concerns or requirements, you should not use the extensions, as they add a minimal overhead without bringing value. On
 * the other hand, if you have such orthogonal concerns for your business (for example, to cryptographically sign every message), extensions are
 * the right way to do it.
 * <p/>
 * You should look at the available extensions, understand the features they provide, and figure out whether they are needed for your use cases
 * or not. If you truly have an orthogonal concern and the extension is not available out of the box, you can write your own, following the
 * indications that follow.
 * <p/>
 * Normally you set up extensions on both the client and the server, since fields the client adds usually need a special processing by the
 * server, or viceversa; it is possible that an extension is only client-side or only server-side, but most of the time both client and server
 * need them. When an extension does not behave as expected, it’s often because the extension is missing on one of the two sides.
 */
public class CometdExtension {

	/**
	 * Called just before a message is sent
	 *
	 * @param message the message about to be sent
	 */
	public native void outgoing(BayeuxMessage message);

	/**
	 * Called just after a message is received
	 *
	 * @param message the message that was just received
	 */
	public native void incoming(BayeuxMessage message);

	/**
	 * Called when the extension is registered
	 *
	 * @param name   the name under which the extension was registered
	 * @param cometd the CometD client in which the extension was registered
	 */
	public native void registered(String name, Cometd cometd);

	/**
	 * Called when the extension is unregistered
	 */
	public native void unregistered();

}
