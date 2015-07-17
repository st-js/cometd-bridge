package org.stjs.bridge.cometd;

import org.stjs.javascript.annotation.STJSBridge;

/**
 * The reload extension allows CometD to load or reload a page without having to re-handshake in the new (or reloaded) page, thereby resuming
 * the existing CometD connection. This extension requires only the client-side extension.
 * <p/>
 * In both Dojo and jQuery extension bindings, the extension is registered on the default cometd object under the name "reload".
 */
@STJSBridge(sources = "webjar:ReloadExtension.js")
public class ReloadExtension implements CometdExtension {

	/**
	 * Configures this reload extension.
	 *
	 * @param config the configuration to apply
	 */
	public native void configure(ReloadExtensionConfig config);

	public native void outgoing(BayeuxMessage message);

	public native void incoming(BayeuxMessage message);

	public native void registered(String name, Cometd cometd);

	public native void unregistered();
}
