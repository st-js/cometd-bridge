package org.stjs.bridge.cometd;

import org.stjs.javascript.Array;
import org.stjs.javascript.annotation.STJSBridge;
import org.stjs.javascript.functions.Callback0;
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
 * <p/>
 * <h3>Publishing</h3>
 * <p/>
 * The <tt>publish()</tt> method allows you to publish data onto a certain channel:
 * <p/>
 * <pre>cometd.publish("/mychannel", new MyMessage("foo", 2));</pre>
 * <p/>
 * You cannot (and it makes no sense to) publish to a meta channel, but you can publish to a service or broadcast channel even if you are not
 * subscribed to that channel. However, you have to handshake before you can publish.
 * <p/>
 * <h3>Disconnecting</h3>
 * <p/>
 * The JavaScript CometD implementation performs automatic reconnect in case of network or Bayeux server failures.
 * <p/>
 * Calling the JavaScript CometD API <tt>disconnect()</tt> results in a message being sent to the Bayeux server so that it can clean up any
 * state associated with that client. As with all methods that involve a communication with the Bayeux server, it is an asynchronous method: it
 * returns immediately, well before the Bayeux server has received the disconnect request. If the server is unreachable (because it is down or
 * because
 * of network failures), the JavaScript CometD implementation stops any reconnection attempt and cleans up any local state. It is normally safe
 * to
 * ignore if the disconnect() call has been successful or not: the client is in any case disconnected, its local state cleaned up, and if the
 * server has not been reached it eventually times out the client and cleans up any server-side state for that client.
 * <p/>
 * <h4>Short Network Failures</h4>
 * <p/>
 * In case of temporary network failures, the client is notified through the <tt>/meta/connect</tt> channel with messages that have the
 * successful field set to <tt>false</tt>. However, the Bayeux server might be able to keep the client's state, and when the network resumes the
 * Bayeux server might behave as if nothing happened. The client in this case just re-establishes the long poll, but any message the client
 * publishes during the network failure is not automatically re-sent (though it is possible to be notified, through the <tt>/meta/publish</tt>
 * channel or better yet through callback functions, of the failed publishes).
 * <p/>
 * <h4></h4>Long Network Failures or Server Failures</h4>
 * <p/>
 * If the network failure is long enough, the Bayeux server times out the lost client, and deletes the state associated with it. The same
 * happens  when the Bayeux server crashes (except that the state of all clients is lost). In this case, the reconnection mechanism on the
 * client
 * performs the following steps:
 * <ol>
 * <li>A long poll is re-attempted, but the server rejects it with a 402::Unknown client error message.</li>
 * <li>A handshake is attempted, and the server normally accepts it and allocates a new client.</li>
 * <li>Upon the successful re-handshake, a long poll is re-established.</li>
 * </ol>
 * <p/>
 * If you register meta channels listener, or if you use callback functions, be aware of these steps, since a reconnection might involve more
 * than one message exchange with the server.
 * <p/>
 * <h3>Message Batching</h3>
 * <p/>
 * Often an application needs to send several messages to different channels. A naive way of doing it follows:
 * <p/>
 * <pre>
 * // Warning: non-optimal code
 * cometd.publish("/channel1", new Message1("foo"));
 * cometd.publish("/channel2", new Message2("all"));
 * cometd.publish("/channel3", new Message3("false"));
 * </pre>
 * <p/>
 * You might think that the three publishes leave the client one after the other, but that is not the case. Remember that <tt>publish()</tt> is
 * asynchronous (it returns immediately), so the three <tt>publish()</tt> calls in sequence likely return well before a single byte reaches the
 * network. The first <tt>publish()</tt> executes immediately, and the other two are in a queue, waiting for the first <tt>publish()</tt> to
 * complete. A <tt>publish()</tt> is complete when the server receives it, sends back the meta response, and the client receives the meta
 * response for that publish. When the first publish completes, the second publish is executed and waits to complete. After that, the third
 * <tt>publish()</tt> finally executes.
 * <p/>
 * If you set the configuration parameter called <tt>autoBatch</tt> to <tt>true</tt>, the implementation automatically batches messages that
 * have been queued. In the example above, the first <tt>publish()</tt> executes immediately, and when it completes, the implementation batches
 * the second and third <tt>publish()</tt> into one request to the server. The autoBatch feature is interesting for those systems where events
 * received asynchronously and unpredictably – either at a fast rate or in bursts – end up generating a <tt>publish()</tt> to the server: in
 * such
 * cases, using the batching API is not effective (as each event would generate only one <tt>publish()</tt>). A burst of events on the client
 * generates a burst of <tt>publish()</tt> to the server, but the <tt>autobatch</tt> mechanism batches them automatically, making the
 * communication more efficient.
 * <p/>
 * The queueing mechanism avoids queueing a <tt>publish()</tt> behind a long poll. If not for this mechanism, the browser would receive three
 * publish
 * requests but it has only two connections available, and one is already occupied by the long poll request. Therefore, the browser might decide
 * to round-robin the publish requests, so that the first publish goes on the second connection, which is free, and it is actually sent over the
 * network, (remember that the first connection is already busy with the long poll request), schedule the second publish to the first connection
 * (after the long poll returns), and schedule the third publish again to the second connection, after the first publish returns. The result is
 * that if you have a long poll timeout of five minutes, the second publish request might arrive at the server five minutes later than the first
 * and the third publish request.
 * <p/>
 * You can optimize the three publishes using batching, which is a way to group messages together so that a single Bayeux message actually
 * carries the three publish messages.
 * <p/>
 * <pre>
 * cometd.batch(() -> {
 *     cometd.publish("/channel1", new Message1("foo"));
 *     cometd.publish("/channel2", new Message2("all"));
 *     cometd.publish("/channel3", new Message3("false"));
 * });
 *
 * // Alternatively, but not recommended:
 * cometd.startBatch()
 * cometd.publish("/channel1", new Message1("foo"));
 * cometd.publish("/channel2", new Message2("all"));
 * cometd.publish("/channel3", new Message3("false"));
 * cometd.endBatch()
 * </pre>
 * <p/>
 * Notice how the three <tt>publish()</tt> calls are now within a function passed to batch().
 * <p/>
 * Alternatively, but less recommended, you can surround the three <tt>publish()</tt> calls between <tt>startBatch()</tt> and
 * <tt>endBatch()</tt>.
 * <p/>
 * Warning: Remember to call <tt>endBatch()</tt> after calling <tt>startBatch()</tt>. If you don't - for example, because an exception is thrown
 * in the middle of the batch - your messages continue to queue, and your application does not work as expected.
 * <p/>
 * If you still want to risk using the <tt>startBatch()</tt> and <tt>endBatch()</tt> calls, remember that you must do so from the same context
 * of
 * execution;  message batching has not been designed to span multiple user interactions. For example, it would be wrong to start a batch in
 * functionA (triggered by user interaction), and ending the batch in functionB (also triggered by user interaction and not called by
 * functionA).
 * Similarly, it would be wrong to start a batch in functionA and then schedule (using <tt>setTimeout()</tt>) the execution of functionB to end
 * the batch. Function <tt>batch()</tt> already does the correct batching for you (also in case of errors), so it is the recommended way to do
 * message batching.
 * <p/>
 * When a batch starts, subsequent API calls are not sent to the server, but instead queued until the batch ends. The end of the batch packs up
 * all the queued messages into one single Bayeux message and sends it over the network to the Bayeux server.
 * <p/>
 * Message batching allows efficient use of the network: instead of making three request/response cycles, batching makes only one
 * request/response cycle.
 * <p/>
 * Batches can consist of different API calls:
 * <p/>
 * CometdSubscription subscription;
 * cometd.batch(() -> {
 * cometd.unsubscribe(subscription);
 * subscription = cometd.subscribe("/foo", message -> ...);
 * cometd.publish("/bar", anotherMessage);
 * });
 * <p/>
 * The Bayeux server processes batched messages in the order they are sent.
 */
@STJSBridge(sources = "webjar:/cometd.js")
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
	 * Registers the given transport under the given transport type.
	 * The optional index parameter specifies the "priority" at which the
	 * transport is registered (where 0 is the max priority).
	 * If a transport with the same type is already registered, this function
	 * does nothing and returns false.
	 * @param type the transport type
	 * @param transport the transport object
	 * @param index the index at which this transport is to be registered
	 * @return true if the transport has been registered, false otherwise
	 * @see #unregisterTransport(String)
	 */
	public native boolean registerTransport(String type, CometdTransport transport, int index);

	/**
	 * @return an array of all registered transport types
	 */
	public native Array<String> getTransportTypes();

	/**
	 * Unregisters all of the transports from this Cometd instance
	 */
	public native void unregisterTransports();

	/**
	 * Returns the transport with the specified name/type.
	 * @param name the name of the transport to find
	 * @return the instance of the transport if found, null if not found
	 */
	public native CometdTransport findTransport(String name);

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
	public native void configure(CometdConfig configuration);

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
	 * Synchronously adds a listener to the specified meta channel.
	 * The <tt>addListener()</tt> method:
	 * <ul>
	 * <li>Must be used to listen to meta channel messages.</li>
	 * <li>May be used to listen to service channel messages.</li>
	 * <li>Should not be used to listen broadcast channel messages (use subscribe() instead).</li>
	 * <li>Does not involve any communication with the Bayeux server, and as such can be called before calling handshake().</li>
	 * <li>Is synchronous: when it returns, you are guaranteed that the listener has been added.</li>
	 * </ul>
	 * <p/>
	 * These are the meta channels available in the JavaScript CometD implementation:
	 * <ul>
	 * <li>/meta/handshake</li>
	 * <li>/meta/connect</li>
	 * <li>/meta/disconnect</li>
	 * <li>/meta/subscribe</li>
	 * <li>/meta/unsubscribe</li>
	 * <li>/meta/publish</li>
	 * <li>/meta/unsuccessful</li>
	 * </ul>
	 * <p/>
	 * Each meta channel is notified when the JavaScript CometD implementation handles the correspondent Bayeux message. The
	 * <tt>/meta/unsuccessful</tt> channel is notified in case of any failure.
	 * <p/>
	 * By far the most interesting meta channel to subscribe to is <tt>/meta/connect</tt> because it gives the status of the current connection
	 * with the Bayeux server. In combination with <tt>/meta/disconnect</tt>, you can use it, for example, to display a green connected icon or
	 * a red disconnected icon on the page, depending on the connection status with the Bayeux server.
	 * <p/>
	 * Here is a common pattern using the <tt>/meta/connect</tt> and <tt>/meta/disconnect</tt> channels:
	 * <p/>
	 * <pre>
	 * boolean connected = false;
	 * cometd.addListener("/meta/connect", message -> {
	 *     if (cometd.isDisconnected()) {
	 *         return;
	 *     }
	 *
	 *     boolean wasConnected = connected;
	 *     connected = message.successful;
	 *     if (!wasConnected && connected) {
	 *         // Reconnected
	 *     } else if (wasConnected && !connected) {
	 *         // Disconnected
	 *     }
	 * });
	 *
	 * cometd.addListener("/meta/disconnect", message -> {
	 *     if (message.successful) {
	 *         connected = false;
	 *     }
	 * });
	 * </pre>
	 * <p/>
	 * One small caveat with the <tt>/meta/connect</tt> channel is that <tt>/meta/connect</tt> is also used for polling the server. Therefore,
	 * if a disconnect is issued during an active poll, the server returns the active poll and this triggers the <tt>/meta/connect</tt>
	 * listener.
	 * The initial check on the status verifies that is not the case before executing the connection logic.
	 * <p/>
	 * Another interesting use of meta channels is when there is an authentication step during the handshake. In this case the registration to
	 * the <tt>/meta/handshake</tt> channel can give details about, for example, authentication failures.
	 *
	 * @param channel           the name of the meta channel to listen to
	 * @param onMessageReceived a callback function that is invoked any time a message is received on the channel
	 */
	public native CometdSubscription addListener(String channel, Callback1<BayeuxMessage> onMessageReceived);

	/**
	 * Clears the specified listener.
	 *
	 * @param subscription the subscription that is to be cleared.
	 */
	public native void removeListener(CometdSubscription subscription);

	/**
	 * Equivalent to calling <tt>publish(channel, messagePayload, null)</tt>
	 *
	 * @param channel        the name of the channel to which the message must be published
	 * @param messagePayload the payload object that will be sent in the <tt>data.content</tt> field of the published Bayeux message
	 */
	public native void publish(String channel, Object messagePayload);

	/**
	 * Publishes the specified message to the specified channel.
	 * <p/>
	 * As with other JavaScript CometD API, <tt>publish()</tt> involves a communication with the server and it is asynchronous: it returns
	 * immediately, well before the Bayeux server has received the message.
	 * <p/>
	 * <pre>cometd.publish("/mychannel", new MyMessage("foo", 2));</pre>
	 * <p/>
	 * You cannot (and it makes no sense to) publish to a meta channel, but you can publish to a service or broadcast channel even if you are
	 * not subscribed to that channel. However, you have to handshake before you can publish.
	 * <p/>
	 * When the message you published arrives to the server, the server replies to the client with a publish acknowledgment; this allows clients
	 * to be sure that the message reached the server. The publish acknowledgment arrives on the same channel the message was published to, with
	 * the same message id, with a successful field. If the message publish fails for any reason, for example because server cannot be reached,
	 * then a publish failure will be emitted, similarly to publish acknowledgments.
	 * <p/>
	 * For historical reasons, publish acknowledgments and failures are notified on the <tt>/meta/publish</tt> channel (only in the JavaScript
	 * library), even if the <tt>/meta/publish</tt> channel is not part of Appendix C, The Bayeux Protocol Specification v1.0.
	 *
	 * @param channel        the name of the channel to which the message must be published
	 * @param messagePayload the payload object that will be sent in the <tt>data.content</tt> field of the published Bayeux message
	 * @param onPublishReply a callback function that is called when the reply to the publish request is received
	 */
	public native void publish(String channel, Object messagePayload, Callback1<BayeuxMessage> onPublishReply);

	/**
	 * Equivalent to calling <tt>disconnect(null, null)</tt>
	 */
	public native void disconnect();

	/**
	 * Equivalent to calling <tt>disconnect(null, onDisconnectReply)</tt>
	 *
	 * @param onDisconnectReply a callback function that is called when the disconnect reply is received
	 */
	public native void disconnect(Callback1<BayeuxMessage> onDisconnectReply);

	/**
	 * Sends a message to the Bayeux server so that it can clean up any state associated with this client.
	 * <p/>
	 * As with all methods that involve a communication with the Bayeux server, this is an asynchronous method: it returns
	 * immediately, well before the Bayeux server has received the disconnect request. If the server is unreachable (because it is down or
	 * because of network failures), the JavaScript CometD implementation stops any reconnection attempt and cleans up any local state. It is
	 * normally safe to ignore if the <tt>disconnect()</tt> call has been successful or not: the client is in any case disconnected, its local
	 * state cleaned up, and if the server has not been reached it eventually times out the client and cleans up any server-side state for that
	 * client.
	 * <p/>
	 * Tip: If you are debugging your application with Firebug, and you shut down the server, you see in the Firebug console the attempts to
	 * reconnect. To stop those attempts, type in the Firebug command line: <tt>dojox.cometd.disconnect()</tt> (for Dojo) or
	 * <tt>$.cometd.disconnect()</tt> (for jQuery).
	 * <p/>
	 * In case you really want to know whether the server received the disconnect request, you can pass a callback function to the
	 * <tt>disconnect()</tt> function:
	 * <p/>
	 * <pre>
	 * cometd.disconnect(disconnectReply -> {
	 *     if (disconnectReply.successful) {
	 *         // Server truly received the disconnect request
	 *     }
	 * });
	 * </pre>
	 * Like other APIs, also <tt>disconnect()</tt> may take an additional object that is sent to the server:
	 * <p/>
	 * <pre>
	 * MyAdditionalThing additional = new MyAdditionalThing(true);
	 * cometd.disconnect(additional, disconnectReply -> ...);
	 * </pre>
	 *
	 * @param additional        an object containing additional informations/fields that will be merged into the disconnect message
	 * @param onDisconnectReply a callback function that is called when the disconnect reply is received
	 */
	public native void disconnect(Object additional, Callback1<BayeuxMessage> onDisconnectReply);

	/**
	 * Checks if this Cometd client is connected or not. The bayeux client will always set this flag to the most up to date value before
	 * notifying listeners or callback functions.
	 *
	 * @return true if this client is disconnected from the Bayeux server, false if is still connected
	 */
	public native boolean isDisconnected();

	/**
	 * Ensures that all of the Bayeux messages sent from within the specified batch function are sent in the same request to the Bayeux server.
	 * The messages sent in the batch are processed by the Bayeux server in the order in which they were received.
	 *
	 * @param batch a functions that sends an arbitrary number of messages to the Bayeux server
	 */
	public native void batch(Callback0 batch);

	/**
	 * Marks the start of a batch of application messages to be sent to the server in a single request, obtaining a single response containing
	 * (possibly) many application reply messages. Messages are held in a queue and not sent until {@link #endBatch()} is called. If
	 * startBatch()
	 * is called multiple times, then an equal number of endBatch() calls must be made to close and send the batch of messages.
	 *
	 * @see #endBatch()
	 */
	public native void startBatch();

	/**
	 * Marks the end of a batch of application messages to be sent to the server in a single request.
	 *
	 * @see #startBatch()
	 */
	public native void endBatch();

	/**
	 * Equivalent to calling <tt>cometd.configure(url)</tt> followed by <tt>cometd.handshake(handshakeAdditional, null)</tt>
	 *
	 * @param url                 the URL of the Bayeux server
	 * @param handshakeAdditional An object containing additional informations/fields that will be merged into the handshake message
	 */
	public native void init(String url, Object handshakeAdditional);

	/**
	 * Equivalent to calling <tt>cometd.configure(configuration)</tt> followed by <tt>cometd.handshake(handshakeAdditional, null)</tt>
	 *
	 * @param configuration       the full configuration of this client
	 * @param handshakeAdditional An object containing additional informations/fields that will be merged into the handshake message
	 */
	public native void init(CometdConfig configuration, Object handshakeAdditional);

	/**
	 * Registers an extension whose callbacks are called for every incoming message
	 * (that comes from the server to this client implementation) and for every
	 * outgoing message (that originates from this client implementation for the
	 * server).
	 * The format of the extension object is the following:
	 * <pre>
	 * {
	 *     incoming: function(message) { ... },
	 *     outgoing: function(message) { ... }
	 * }
	 * </pre>
	 * Both properties are optional, but if they are present they will be called
	 * respectively for each incoming message and for each outgoing message.
	 *
	 * @param name      the name of the extension
	 * @param extension the extension to register
	 * @return true if the extension was registered, false otherwise
	 * @see #unregisterExtension(String)
	 */
	public native void registerExtension(String name, CometdExtension extension);

	/**
	 * Unregister an extension previously registered with
	 * {@link #registerExtension(String, CometdExtension)}.
	 *
	 * @param name the name of the extension to unregister.
	 * @return true if the extension was unregistered, false otherwise
	 */
	public native void unregisterExtension(String name);

	/**
	 * Find the extension registered with the given name.
	 *
	 * @param name the name of the extension to find
	 * @return the extension found or null if no extension with the given name has been registered
	 */
	public native CometdExtension getExtension(String name);

	/**
	 * Defines the global extension exception handler that is invoked every time an extension throws an exception (for example, calling a
	 * function on an undefined object):
	 * <pre>
	 * cometd.onExtensionException = (exception, extensionName, outgoing, message) -> {
	 *     // Uh-oh, something went wrong, disable this extension
	 *     cometd.unregisterExtension(extensionName);
	 *
	 *     // If the message is going to the server, add the error to the message
	 *     if (outgoing) {
	 *         // Assume you have created the message structure below
	 *         ErrorMessage badExtension = message.ext.badExtensions[extensionName];
	 *         badExtension.exception = exception;
	 *     }
	 * };
	 * </pre>
	 * <p/>
	 * Be very careful to use the CometD object to publish messages within the extension exception handler, or you might end up in an infinite
	 * loop (the extensions process the publish message, which might fail and call again the extension exception handler). If the extension
	 * exception handler itself throws an exception, this exception is logged at level "info" and the CometD implementation does not break.
	 */
	public Callback4<Object, String, Boolean, BayeuxMessage> onExtensionException;

	/**
	 * Enables or disables the AckExtension.
	 */
	public boolean ackEnabled;

	/**
	 * Equivalent to calling <tt>reload(null)</tt>
	 */
	public native void reload();

	/**
	 * When the reload() method is called, the state of the cometd
	 * connection and of the cometd subscriptions is stored in a cookie
	 * with a short max-age.
	 * The reload() method must therefore be called by page unload
	 * handlers, often provided by JavaScript toolkits.
	 *
	 * When the page is (re)loaded, this extension checks the cookie
	 * and restores the cometd connection and the cometd subscriptions.
	 */
	public native void reload(ReloadExtensionConfig config);

	/**
	 * Removes all subscriptions added via {@link #subscribe},
	 * but does not remove the listeners added via {@link #addListener}.
	 */
	public native void clearSubscriptions();

	/**
	 * Removes all listeners registered with {@link #addListener} or
	 * {@link #subscribe}.
	 */
	public native void clearListeners();

	/**
	 * Sends a complete bayeux message.
	 * This method is exposed as a public so that extensions may use it
	 * to send bayeux message directly, for example in case of re-sending
	 * messages that have already been sent but that for some reason must
	 * be resent.
	 */
	public native void send(BayeuxMessage message);

	/**
	 * Receives a message.
	 * This method is exposed as a public so that extensions may inject
	 * messages simulating that they had been received.
	 */
	public native void receive(BayeuxMessage message);

	/**
	 * Returns a string representing the status of the bayeux communication with the Bayeux server.
	 */
	public native String getStatus();

	/**
	 * Sets the backoff period used to increase the backoff time when retrying an unsuccessful or failed message.
	 * Default value is 1 second, which means if there is a persistent failure the retries will happen
	 * after 1 second, then after 2 seconds, then after 3 seconds, etc. So for example with 15 seconds of
	 * elapsed time, there will be 5 retries (at 1, 3, 6, 10 and 15 seconds elapsed).
	 * @param period the backoff period to set
	 * @see #getBackoffIncrement()
	 */
	public native void setBackoffIncrement(long period);

	/**
	 * Returns the backoff period used to increase the backoff time when retrying an unsuccessful or failed message.
	 * @see #setBackoffIncrement(long)
	 */
	public native long getBackoffIncrement();

	/**
	 * Returns the backoff period to wait before retrying an unsuccessful or failed message.
	 */
	public native long getBackoffPeriod();

	/**
	 * Sets the log level for console logging.
	 * Valid values are the strings 'error', 'warn', 'info' and 'debug', from
	 * less verbose to more verbose.
	 * @param level the log level string
	 */
	public native void setLogLevel(String level);

	/**
	 * Returns the name assigned to this Cometd object, or the string 'default'
	 * if no name has been explicitly passed as parameter to the constructor.
	 */
	public native String getName();

	/**
	 * Returns the clientId assigned by the Bayeux server during handshake.
	 */
	public native String getClientId();

	/**
	 * Returns the URL of the Bayeux server.
	 */
	public native String getURL();

	/**
	 * Returns the instance of the transport that is currently in use.
	 */
	public native CometdTransport getTransport();

	public native CometdConfig getConfiguration();

	public native BayeuxAdvice getAdvice();
}
