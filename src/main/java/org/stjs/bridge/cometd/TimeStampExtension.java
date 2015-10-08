package org.stjs.bridge.cometd;

import org.stjs.javascript.annotation.STJSBridge;

/**
 * The timestamp extension adds a timestamp to the message object for every message the client and/or server sends. It is a non-standard
 * extension because it does not add the additional fields to the ext field, but to the message object itself. This extension requires both a
 * client-side extension and a server-side extension. The server-side extension is available in Java.
 * <p/>
 * In both Dojo and jQuery extension bindings, the extension is registered on the default cometd object under the name "timestamp".
 */
@STJSBridge(sources = "webjar:/TimeStampExtension.js")
public class TimeStampExtension extends CometDExtension {
}
