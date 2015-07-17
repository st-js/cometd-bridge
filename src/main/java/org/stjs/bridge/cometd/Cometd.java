package org.stjs.bridge.cometd;

import org.stjs.javascript.Map;
import org.stjs.javascript.annotation.Namespace;
import org.stjs.javascript.functions.Callback1;
import org.stjs.javascript.functions.Callback4;

/**
 * The CometD 2 JavaScript library is a portable JavaScript implementation with bindings for the major JavaScript toolkits, currently Dojo and
 * jQuery. This means that the CometD Bayeux JavaScript implementation is written in pure JavaScript with no dependencies on the toolkits, and
 * that the toolkit bindings add the syntactic sugar that makes the Bayeux APIs feel like they are native to the toolkit.
 * <p/>
 * <h3>Configuring and Initializing</h3>
 * <p/>
 * The complete API is available through a single object prototype named <tt>org.cometd.Cometd</tt>. The Dojo toolkit has one instance of this
 * object available under the name <tt>dojox.cometd</tt>, while for jQuery it is available under the name <tt>$.cometd</tt>. This default cometd
 * object has been instantiated and configured with the default values and it has not yet started any Bayeux communication.
 * <p/>
 * Before the cometd object can start Bayeux communication it needs a mandatory parameter: the URL of the Bayeux server. The URL parameter can
 * be passed by calling the <tt>configure()</tt> method.
 * <p/>
 * After you have configured the cometd object, the Bayeux communication does not start until you call <tt>handshake()</tt>
 * <p/>
 * Previous users of the JavaScript CometD implementation called a method named <tt>init()</tt>. This method still exists, and it is a shorthand
 * for calling <tt>configure()</tt> followed by <tt>handshake()</tt>
 * <p/>
 * <h3>Handshaking</h3>
 * In order to initiate the communication with the Bayeux server, you must call either the <tt>handshake()</tt> or the <tt>init()</tt> methods
 * on
 * the cometd object. The <tt>init()</tt> function is a shorthand for a call to <tt>configure()</tt> followed by a call to <tt>handshake()</tt>.
 * <p/>
 * <h3>Subscribing and Unsubscribing</h3>
 * <p/>
 * Depending on the type of channel, subscribing and unsubscribing to a channel have different meanings.
 * <h4>Meta Channels</h4>
 * It is not possible to subscribe to meta channels: the server replies with an error message. It is possible to listen to meta channels. You
 * cannot (and it makes no sense to) publish messages to meta channels: only the Bayeux protocol implementation creates and sends messages on
 * meta channels. Meta channels are useful on the client to listen for error messages like handshake errors (for example, because the client did
 * not provide the correct credentials) or network errors (for example, to know when the connection with the server has broken or when it has
 * been re-established).
 * <h4>Service Channels</h4>
 * Service channels are used in the case of request/response style of communication between client and server (as opposed to the
 * publish/subscribe style of communication on broadcast channels). While subscribing to service channels yields no errors, this is a
 * no-operation for the server: the server ignores the subscription request. It is possible to publish to service channels, with the semantic of
 * a communication between a specific client (the one that is publishing the message on the service channel) and the server. Service channels
 * are useful to implement, for example, private chat messages: in a chat with userA, userB and userC, userA can publish a private message to
 * userC (without userB knowing about it) using service channels.
 * <h4>Broadcast Channels</h4>
 * Broadcast channels have the semantic of a messaging topic and are used in the case of publish/subscribe style of communication. Usually, it
 * is possible to subscribe to broadcast channels and to publish to broadcast channels; this can only be forbidden using a security policy on
 * the Bayeux server or by using authorizers. Broadcast channels are useful to broadcast messages to all subscribed clients, for example in case
 * of a stock price change.
 * <h4>Subscribers versus Listeners</h4>
 * The JavaScript CometD API has two APIs to work with channel subscriptions:
 * <ol>
 * <li><tt>addListener()</tt> and the correspondent <tt>removeListener()</tt></li>
 * <li><tt>subscribe()</tt> and the correspondent <tt>unsubscribe()</tt></li>
 * </ol>
 * <p/>
 * The major difference between listeners and subscribers is that subscribers are automatically removed upon re-handshake, while listeners are
 * not modified by a re-handshake. When a client subscribes to a channel, the server maintains a client-specific server-side subscription state.
 * If the server requires a re-handshake, it means that it lost the state for that client, and therefore also the server-side subscription
 * state.
 * In order to maintain the client-side state consistent with that of the server, subscriptions - but not listeners - are automatically removed
 * upon re-handshake.
 * <p/>
 * A good place in the code to perform subscriptions is in a <tt>/meta/handshake</tt> function. Since <tt>/meta/handshake</tt> listeners are
 * invoked in both explicit handshakes the client performs and in re-handshakes the server triggers, it is guaranteed that your subscriptions
 * are
 * always performed properly and kept consistent with the server state.
 * <p/>
 * Equivalently, a callback function passed to the handshake method behaves exactly like a <tt>/meta/handshake</tt> listener, and therefore can
 * be used to perform subscriptions.
 * <p/>
 * Applications do not need to unsubscribe in case of re-handshake; the CometD library takes care of removing all subscriptions upon
 * re-handshake, so that when the <tt>/meta/handshake</tt> function executes again the subscriptions are correctly restored (and not
 * duplicated).
 * <p/>
 * For the same reason, you should never add listeners inside a <tt>/meta/handshake</tt> function, because this will add another listener
 * without removing the previous one, resulting in multiple notifications of the same messages.
 * <p/>
 * <h4>Dynamic Resubscription</h4>
 * <p/>
 * Often times, applications need to perform dynamic subscriptions and unsubscriptions, for example when a user clicks on a user interface
 * element, you want to subscribe to a certain channel. In this case the subscription object returned upon subscription is stored to be able to
 * dynamically unsubscribe from the channel upon user demand.
 * <p/>
 * In case of a re-handshake, dynamic subscriptions are cleared (like any other subscription) and the application needs to figure out which
 * dynamic subscription must be performed again. This information is already known to CometD at the moment cometd.subscribe(...) was called,
 * so applications can just call <tt>resubscribe()</tt> using the subscription object obtained from <tt>subscribe()</tt>
 * <p/>
 * <h4>Listeners and Subscribers Exception Handling</h4>
 * <p/>
 * If a listener or subscriber function throws an exception (for example, calls a method on an undefined object), the error message is logged
 * at level "debug". However, there is a way to intercept these errors by defining the global listener exception handler that is invoked
 * every time a listener or subscriber throws an exception, by assigning a callback to <tt>onListenerException</tt>.
 * <p/>
 * <h4>Wildcard Subscriptions</h4>
 * <p/>
 * It is possible to subscribe to several channels simultaneously using wildcards:
 * <pre>cometd.subscribe("/chatrooms/*", message -> ...);</pre>
 * <p/>
 * A single asterisk has the meaning of matching a single channel segment; in the example above it matches channels <tt>/chatrooms/12</tt> and
 * <tt>/chatrooms/15</tt>, but not <tt>/chatrooms/12/upload</tt>. To match multiple channel segments, use the double asterisk:
 * <pre>cometd.subscribe("/events/**", message -> ...);</pre>
 * With the double asterisk, the channels <tt>/events/stock/FOO</tt> and <tt>/events/forex/EUR</tt> match, as well as <tt>/events/feed</tt> and
 * <tt>/events/feed/2009/08/03</tt>.
 * <p/>
 * The wildcard mechanism works also for listeners, so it is possible to listen to all meta channels as follows:
 * <pre>cometd.addListener("/meta/*", message -> ...);</pre>
 * <p/>
 * By default, subscriptions to the global wildcards <tt>/*</tt> and <tt>/**</tt> result in an error, but you can change this behavior by
 * specifying a custom security policy on the Bayeux server.
 */
@Namespace("org.cometd")
public class Cometd {

	/**
	 * Unregisters the transport with the specified name. This can be useful to force the use of
	 * only one transport (for example, for testing purposes), or to disable certain transports that might be
	 * unreliable.
	 *
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
	 *
	 * @param url the URL of the Bayeux server
	 */
	public native void configure(String url);

	/**
	 * Configures all the parameters of the Bayeux client. The <tt>url</tt> parameter is required
	 * <pre>
	 * cometd.configure(new CometdConfiguration(){{
	 *     url = 'http://localhost:8080/cometd';
	 *     maxConnections = 2;
	 * }});
	 * </pre>
	 *
	 * @param configuration The full configuration of this client
	 */
	public native void configure(CometdConfiguration configuration);

	/**
	 * Equivalent to calling <tt>handshake(null, null)</tt>
	 */
	public native void handshake();

	/**
	 * Equivalent to calling <tt>handshake(null, onHandshakeCompleted)</tt>
	 *
	 * @param onHandshakeCompleted a callback function that will be invoked when the handshake message reply from the server arrives to the
	 *                             client
	 */
	public native void handshake(Callback1<BayeuxMessage> onHandshakeCompleted);

	/**
	 * Sends a handshake message request to the server, and the Bayeux protocol requires that the server sends to the client a handshake
	 * message reply.
	 * <p/>
	 * The Bayeux handshake creates a network communication with the Bayeux server, negotiates the type of transport to use, and negotiates a
	 * number of protocol parameters to be used in subsequent communications.
	 * <p/>
	 * As with several methods of the JavaScript CometD API, <tt>handshake()</tt> is an asynchronous function: it returns immediately, well
	 * before the Bayeux handshake steps have completed.
	 * <p/>
	 * It is possible to invoke the <tt>handshake()</tt> function passing as parameter a callback function that will be invoked when the
	 * handshake message reply from the server arrives to the client (or, in case the server does not reply, when the client detects a
	 * handshake failure):
	 * <p/>
	 * <pre>
	 * cometd.handshake(handshakeReply -> {
	 *     if (handshakeReply.successful) {
	 *         // Successfully connected to the server.
	 *         // Now it is possible to subscribe or send messages
	 *     } else {
	 *         // Cannot handshake with the server, alert user.
	 *     }
	 * });
	 * </pre>
	 * Passing a callback function to <tt>handshake()</tt> is equivalent to register a <tt>/meta/handshake</tt> listener
	 * The handshake might fail for several reasons:
	 * <ul>
	 * <li>You mistyped the server URL.</li>
	 * <li>The transport could not be negotiated successfully.</li>
	 * <li>The server denied the handshake (for example, the authentication credentials were wrong).</li>
	 * <li>The server crashed.</li>
	 * <li>There was a network failure.</li>
	 * </ul>
	 * In case of a handshake failure, applications should not try to call handshake() again: the CometD library will do this on
	 * behalf of the application. A corollary of this is that applications should usually only ever call handshake() once in their code.
	 * <p/>
	 * If you want to pass additional information to the handshake message (for example, authentication credentials) you can pass an additional
	 * object to the handshake() function:
	 * <pre>
	 * YourConfig additional = new YourConfig(){{
	 *     credentials = new YourCredentials(){{
	 *     user = "cometd";
	 *         token: 'xyzsecretabc';
	 *     }}
	 * }};
	 *
	 * cometd.handshake(additional, handshakeReply -> {
	 *     if (handshakeReply.successful) {
	 *         // Your logic here.
	 *     }
	 * });
	 * </pre>
	 * The additional object will be merged into the handshake message, and the server will be able to access the additional information in a
	 * <tt>/meta/handshake</tt> listener,
	 * <p/>
	 * The additional object must not be used to tamper the handshake message by using reserved fields defined by the Bayeux protocol. Instead,
	 * you should use field names that are unique to your application, better yet when fully qualified like <tt>com.acme.credentials"</tt>
	 * <p/>
	 * You should never add listeners inside a <tt>/meta/handshake</tt> function, because this will add another listener without removing
	 * the previous one, resulting in multiple notifications of the same messages.
	 *
	 * @param additional           An object containing additional informations/fields that will be merged into the handshake message
	 * @param onHandshakeCompleted a callback function that will be invoked when the handshake message reply from the server arrives to the
	 *                             client
	 */
	public native void handshake(Object additional, Callback1<BayeuxMessage> onHandshakeCompleted);

	/**
	 * Equivalent to calling <tt>subscribe(channel, onMessageReceived, null, null)</tt>
	 *
	 * @param channel           the name of the channel to be subscribed to
	 * @param onMessageReceived a listener callback that is called each time a message is received on the channel
	 * @return a subscription object that can be passed to <tt>unsubscribe()</tt>
	 */
	public native CometdSubscription subscribe(String channel, Callback1<BayeuxMessage> onMessageReceived);

	/**
	 * Equivalent to calling <tt>subscribe(channel, onMessageReceived, null, onSubscribeReply)</tt>
	 *
	 * @param channel           the name of the channel to be subscribed to
	 * @param onMessageReceived a listener callback that is called each time a message is received on the channel
	 * @param onSubscribeReply  a callback that is called once when the subscription is replied to, either successfully or unsuccessfully
	 * @return a subscription object that can be passed to <tt>unsubscribe()</tt>
	 */
	public native CometdSubscription subscribe(String channel, Callback1<BayeuxMessage> onMessageReceived,
			Callback1<BayeuxMessage> onSubscribeReply);

	/**
	 * Asynchronously subscribes to the specified service or broadcast channel.
	 * <p/>
	 * The <tt>subscribe()</tt> method:
	 * <ol>
	 * <li>Must not be used to listen to meta channels messages (if attempted, the server returns an error).</li>
	 * <li>May be used to listen to service channel messages.</li>
	 * <li>Should be used to listen to broadcast channel messages.</li>
	 * <li>Involves a communication with the Bayeux server and as such cannot be called before calling handshake().</li>
	 * <li>Is asynchronous: it returns immediately, well before the Bayeux server has received the subscription request.</li>
	 * </ol>
	 * <p/>
	 * If you want to be certain that the server received your subscription request (or not), you can either register a <tt>/meta/subscribe</tt>
	 * listener, or pass a callback function to <tt>subscribe()</tt>
	 * <pre>
	 * cometd.subscribe("/foo", message -> ... , subscribeReply -> {
	 *     if (subscribeReply.successful) {
	 *         // The server successfully subscribed this client to the "/foo" channel.
	 *     }
	 * });
	 * </pre>
	 * <p/>
	 * Remember that subscriptions may fail on the server (for example, the client does not have enough permissions to subscribe) or on the
	 * client (for example, there is a network failure). In both cases <tt>/meta/subscribe listener</tt>, or the callback function, will be
	 * invoked with an unsuccessful subscribe message reply.
	 * <p/>
	 * You can pass additional information to the subscribe message by passing an additional object to the <tt>subscribe()</tt> function:
	 * <pre>
	 * Map<String, Object> additional = $map();
	 * additional.$put("com.acme.priority", 10);
	 * cometd.subscribe("/foo", message -> ..., additional, subscribeReply -> ...);
	 * </pre>
	 * <p/>
	 * Remember that the subscribe message is sent to the server only when subscribing to a channel for the first time. Additional subscriptions
	 * to the same channel will not result in a message being sent to the server.
	 * <p/>
	 * <tt>subscribe()</tt> returns a subscription object that must be passed to and <tt>unsubscribe()</tt>:
	 * <pre>
	 * // Some initialization code
	 * CometdSubscription subscription = cometd.subscribe("/foo/bar/", message -> ...);
	 * // Some de-initialization code
	 * cometd.unsubscribe(subscription);
	 * </pre>
	 * <p/>
	 * In cases where the Bayeux server is not reachable (due to network failures or because the server crashed), <tt>subscribe()</tt> behaves
	 * as follows:
	 * <p/>
	 * In <tt>subscribe()</tt> CometD first adds the local listener to the list of subscribers for that channel, then attempts the server
	 * communication. If the communication fails, the server does not know that it has to send messages to this client and therefore on the
	 * client, the local listener (although present) is never invoked.
	 *
	 * @param channel           the name of the channel to be subscribed to
	 * @param onMessageReceived a listener callback that is called each time a message is received on the channel
	 * @param additional        An object containing additional informations/fields that will be merged into the subscribe message
	 * @param onSubscribeReply  a callback that is called once when the subscription is replied to, either successfully or unsuccessfully
	 * @return a subscription object that can be passed to <tt>unsubscribe()</tt>
	 */
	public native CometdSubscription subscribe(String channel, Callback1<BayeuxMessage> onMessageReceived, Object additional,
			Callback1<BayeuxMessage> onSubscribeReply);

	/**
	 * Equivalent to calling unsubscribe(subscription, null, null)
	 *
	 * @param subscription the subscription that is to be cleared.
	 */
	public native void unsubscribe(CometdSubscription subscription);

	/**
	 * Clears the specified subscription.
	 * <p/>
	 * Similarly to <tt>subscribe()</tt>, the unsubscribe message is sent to the server only when unsubscribing from a channel for the last
	 * time.
	 *
	 * @param subscription       the subscription that is to be cleared.
	 * @param additional         An object containing additional informations/fields that will be merged into the unsubscribe message
	 * @param onUnsubscribeReply a callback that is called once when the un-subscription is replied to, either successfully or unsuccessfully
	 */
	public native void unsubscribe(CometdSubscription subscription, Object additional, Callback1<BayeuxMessage> onUnsubscribeReply);

	/**
	 * Re-subscribes a subscription that was previously unsubscribed (either because it was explicitly unsubscribed, or because the Bayeux
	 * client was disconnected and all subscriptions were cleared).
	 *
	 * @param subscription the handle to a subscription that was previous unsubscribed.
	 */
	public native void resubscribe(CometdSubscription subscription);

	/**
	 * A callback function that is called each time a listener or subscriber function throws an exception. By default the error message is
	 * logged at level "debug". However, there is a way to intercept these errors by defining the global listener exception handler that is
	 * invoked every time a listener or subscriber throws an exception:
	 * <pre>
	 * cometd.onListenerException = (exception, subscriptionHandle, isListener, message) -> {
	 *     // Uh-oh, something went wrong, disable this listener/subscriber
	 *     if (isListener) {
	 *         cometd.removeListener(subscriptionHandle);
	 *     } else {
	 *         cometd.unsubscribe(subscriptionHandle);
	 *     }
	 * };
	 * </pre>
	 * <p/>
	 * It is possible to send messages to the server from the listener exception handler. If the listener exception handler itself throws an
	 * exception, this exception is logged at level "info" and the CometD implementation does not break.
	 */
	public Callback4<Object, CometdSubscription, Boolean, BayeuxMessage> onListenerException;

	/**
	 * The <tt>addListener()</tt> method:
	 * <ul>
	 * <li>Must be used to listen to meta channel messages.</li>
	 * <li>May be used to listen to service channel messages.</li>
	 * <li>Should not be used to listen broadcast channel messages (use subscribe() instead).</li>
	 * <li>Does not involve any communication with the Bayeux server, and as such can be called before calling handshake().</li>
	 * <li>Is synchronous: when it returns, you are guaranteed that the listener has been added.</li>
	 * </ul>
	 * <p/>
	 * <p/>
	 * <p/>
	 * <p/>
	 * <p/>
	 * These are the meta channels available in the JavaScript CometD implementation:
	 * <p/>
	 * /meta/handshake
	 * <p/>
	 * /meta/connect
	 * <p/>
	 * /meta/disconnect
	 * <p/>
	 * /meta/subscribe
	 * <p/>
	 * /meta/unsubscribe
	 * <p/>
	 * /meta/publish
	 * <p/>
	 * /meta/unsuccessful
	 * <p/>
	 * Each meta channel is notified when the JavaScript CometD implementation handles the correspondent Bayeux message. The /meta/unsuccessful
	 * channel is notified in case of any failure.
	 * <p/>
	 * By far the most interesting meta channel to subscribe to is /meta/connect because it gives the status of the current connection with the
	 * Bayeux server. In combination with /meta/disconnect, you can use it, for example, to display a green connected icon or a red disconnected
	 * icon on the page, depending on the connection status with the Bayeux server.
	 * <p/>
	 * Here is a common pattern using the /meta/connect and /meta/disconnect channels:
	 * <p/>
	 * var _connected = false;
	 * <p/>
	 * cometd.addListener('/meta/connect', function(message)
	 * {
	 * if (cometd.isDisconnected())
	 * {
	 * return;
	 * }
	 * <p/>
	 * var wasConnected = _connected;
	 * _connected = message.successful;
	 * if (!wasConnected && _connected)
	 * {
	 * // Reconnected
	 * }
	 * else if (wasConnected && !_connected)
	 * {
	 * // Disconnected
	 * }
	 * });
	 * <p/>
	 * cometd.addListener('/meta/disconnect', function(message)
	 * {
	 * if (message.successful)
	 * {
	 * _connected = false;
	 * }
	 * }
	 * <p/>
	 * One small caveat with the /meta/connect channel is that /meta/connect is also used for polling the server. Therefore, if a disconnect is
	 * issued during an active poll, the server returns the active poll and this triggers the /meta/connect listener. The initial check on the
	 * status verifies that is not the case before executing the connection logic.
	 * <p/>
	 * Another interesting use of meta channels is when there is an authentication step during the handshake. In this case the registration to
	 * the /meta/handshake channel can give details about, for example, authentication failures.
	 */
	public Object addListener(String string, Callback1<BayeuxMessage> callback1) {
		throw new UnsupportedOperationException();
	}

	public void removeListener(Object subscription) {
		throw new UnsupportedOperationException();
	}

	/**
	 * shorthand for calling configure() followed by handshake()
	 */
	public void init(Map<String, String> $map) {
		throw new UnsupportedOperationException();
	}

	public void disconnect() {
		throw new UnsupportedOperationException();
	}

	public boolean isDisconnected() {
		throw new UnsupportedOperationException();
	}

	public void startBatch() {
		throw new UnsupportedOperationException();
	}

	public void endBatch() {
		throw new UnsupportedOperationException();
	}

	public native void registerExtension(String extensionName, CometdExtension extension);
}
