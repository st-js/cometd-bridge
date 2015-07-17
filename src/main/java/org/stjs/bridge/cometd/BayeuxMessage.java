package org.stjs.bridge.cometd;

import org.stjs.javascript.Array;
import org.stjs.javascript.annotation.SyntheticType;

/**
 * A message a specified in the Bayeux protocol specification. Most of the fields of BayeuxMessage are only used by a subset of all the messages
 * defined in the Bayeux protocol. See the Bayeux protocol 1.0.0 specification, sections 4 (Meta Message Definitions) and 5 (Event Message
 * Definitions)  for more details of which fields are used in which message.
 */
@SyntheticType
public class BayeuxMessage {

	/**
	 * The channel message field MUST be included in every Bayeux message to specify the source or destination of the message. In a request, the
	 * channel specifies the destination of the message, and in a response it specifies the source of the message.
	 */
	public String channel;

	/**
	 * The version message field MUST be included in messages to/from the "/meta/handshake" channel to indicate the protocol version expected by
	 * the client/server.
	 */
	public String version;

	/**
	 * The minimumVersion message field MAY be included in messages to/from the "/meta/handshake" channel to indicate the oldest protocol
	 * version
	 * that can be handled by the client/server.
	 */
	public String minimumVersion;

	/**
	 * The supportedConnectionTypes field is included in messages to/from the "/meta/handshake" channel to allow clients and servers to reveal
	 * the transports that are supported. The value is an array of strings, with each string representing a transport name. Defined connection
	 * types include:
	 * <dl>
	 * <dt>long-polling</dt>
	 * <dd>This transport is defined in section 6.1.</dd>
	 * <dt>callback-polling</dt>
	 * <dd>This transport is defined in section 6.2.</dd>
	 * <dt>iframe</dt>
	 * <dd>OPTIONAL transport using the document content of a hidden iframe element.</dd>
	 * <dt>flash</dt>
	 * <dd>OPTIONAL transport using the capabilities of a browser flash plugin.</dd>
	 * </dl>
	 * <p/>
	 * All server and client implementations MUST support the "long-polling" connection type and SHOULD support "callback-polling".
	 * <p/>
	 * All other connection types are OPTIONAL.
	 */
	public Array<String> supportedConnectionTypes;

	/**
	 * * The clientId message field uniquely identifies a client to the Bayeux server. The clientId message field MUST be included in every
	 * message sent to the server except for messages sent to the "/meta/handshake" channel and MAY be omitted in a publish message (see section
	 * 5.1). The clientId field MUST be returned in every message response except for a failed handshake request and for a publish message
	 * response that was send without clientId.
	 */
	public String clientId;

	/**
	 * The advice field provides a way for servers to inform clients of their preferred mode of client operation so that in conjunction with
	 * server-enforced limits, Bayeux implementations can prevent resource exhaustion and inelegant failure modes.
	 * <p/>
	 * The advice field is a JSON encoded object containing general and transport specific values that indicate modes of operation, timeouts and
	 * other potential transport specific parameters. Fields may occur either in the top level of a message or within a transport specific
	 * section.
	 */
	public BayeuxAdvice advice;

	/**
	 * The connectionType message field specifies the type of transport the client requires for communication. The connectionType message field
	 * MUST be included in request messages to the "/meta/connect" channel. Connection types are listed in section 3.4.
	 */
	public String connectionType;

	/**
	 * An id field MAY be included in any Bayeux message with an alpha numeric value:
	 * <p/>
	 * <pre>id   =   alphanum *( alphanum )</pre>
	 * <p/>
	 * Generation of IDs is implementation specific and may be provided by the application. Messages published to /meta/** and /service/**
	 * SHOULD
	 * have id fields that are unique within the the connection.
	 * <p/>
	 * Messages sent in response to messages delivered to /meta/** channels MUST use the same message id as the request message.
	 * <p/>
	 * Messages sent in response to messages delivered to /service/** channels SHOULD use the same message id as the request message or an id
	 * derived from the request message id.
	 */
	public String id;

	/**
	 * The timestamp message field SHOULD be specified in the following ISO 8601 profile (all times SHOULD be sent in GMT time):
	 * <p/>
	 * <pre>YYYY-MM-DDThh:mm:ss.ss</pre>
	 * <p/>
	 * A timestamp message field is OPTIONAL in all Bayeux messages.
	 */
	public String timestamp;

	/**
	 * The data message field is an arbitrary JSON encoded object that contains event information. The data field MUST be included in publish
	 * messages, and a Bayeux server MUST include the data field in an event delivery message.
	 */
	public Object data;

	/**
	 * The successful boolean message field is used to indicate success or failure and MUST be included in responses to the "/meta/handshake",
	 * "/meta/connect", "/meta/subscribe","/meta/unsubscribe", "/meta/disconnect", and publish channels.
	 */
	public Boolean successful;

	/**
	 * The subscription message field specifies the channels the client wishes to subscribe to or unsubscribe from. The subscription message
	 * field MUST be included in requests and responses to/from the "/meta/subscribe" or "/meta/unsubscribe" channels.
	 */
	public String subscription;

	/**
	 * The error message field is OPTIONAL in any Bayeux response.
	 * <p/>
	 * The error message field MAY indicate the type of error that occurred when a request returns with a false successful message. The error
	 * message field should be sent as a string in the following format:
	 * <p/>
	 * <pre>
	 * error            = error_code ":" error_args ":" error_message
	 * | error_code ":" ":" error_message
	 * error_code       = digit digit digit
	 * error_args       = string *( "," string )
	 * error_message    = string
	 * </pre>
	 * <p/>
	 * Example error strings are:
	 * <p/>
	 * <pre>
	 * 401::No client ID
	 * 402:xj3sjdsjdsjad:Unknown Client ID
	 * 403:xj3sjdsjdsjad,/foo/bar:Subscription denied
	 * 404:/foo/bar:Unknown Channel
	 * </pre>
	 */
	public String error;

	/**
	 * An ext field MAY be included in any Bayeux message. Its value SHOULD be a JSON encoded object with top level names distinguished by
	 * implementation names (eg. "org.dojo.Bayeux.field").
	 * <p/>
	 * The contents of ext may be arbitrary values that allow extensions to be negotiated and implemented between server and client
	 * implementations.
	 */
	public Object ext;

	/**
	 * in handshake response messages, this field may be included to support prototype client implementations that required the authSuccessful
	 * field
	 */
	public boolean authSuccessful;

}
