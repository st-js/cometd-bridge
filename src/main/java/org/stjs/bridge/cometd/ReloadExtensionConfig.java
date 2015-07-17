package org.stjs.bridge.cometd;

import org.stjs.javascript.annotation.SyntheticType;

/**
 * The JavaScript cometd object is normally already configured with the default reload extension configuration. To reconfigure the reload
 * extension:
 * <p/>
 * Reconfigure the extension at startup:
 * <p/>
 * <pre>
 * cometd.getExtension("reload").configure(new ReloadExtensionConfiguration(){{
 *     cookieMaxAge = 15;
 * }});
 * </pre>
 * <p/>
 * Reconfigure the extension every time the cometd.reload() function is invoked:
 * <p/>
 * <pre>
 * cometd.reload(new ReloadExtensionConfiguration(){{
 *     cookieMaxAge: 15
 * }});
 * </pre>
 * <p/>
 * If you increase the maxCookieAge, be aware that you should probably increase the value of maxInterval too (see also the server configuration
 * section), to avoid the server expiring the session while the page is reloading.
 * <p/>
 * The reload extension is useful to allow users to reload CometD pages, or to navigate to other CometD pages, without going through a disconnect
 * and handshake cycle, thus resuming an existing CometD session on the reloaded or on the new page.
 * <p/>
 * When reloading or navigating away from a page, browsers will destroy the JavaScript context associated to that page, and interrupt the
 * connection to the server too. On the reloaded or on the new page, the JavaScript context is recreated anew by the browser, but the CometD
 * JavaScript library has lost all the CometD session details that were established in the previous page. In absence of the reload extension,
 * application need to go through another handshake step to recreate the CometD session details needed.
 * <p/>
 * The reload extension gives the ability to resume the CometD session in the new page, by re-establishing the previously successful CometD
 * session. This is useful especially when the server builds a stateful context for the CometD session that is not to be lost just because the
 * user refreshed the page, or navigated to another part of the same CometD web application. A typical example of this stateful context is when
 * the server needs to guarantee message delivery (see also the acknowledge extension section). In this case, the server has a list of messages
 * that have not been acknowledged by the client, and if the client reloads the page, without the reload extension this list of messages will be
 * lost, causing the client to potentially loose important messages. With the reload extension, instead, the CometD session is resumed and it
 * will appear to the server as if it was never interrupted, thus allowing the server to deliver to the client the unacknowledged messages.
 * <p/>
 * The reload extension works in this way: on page load, the application configures the CometD object, registers channel listeners and finally
 * calls cometd.handshake(). This handshake normally contacts the server and establishes a new CometD session, and the reload extension tracks
 * this successful handshake. On page reload, or when the page is navigated to another CometD page, the application code must call
 * cometd.reload() (for example, on the page unload event handler, see note below). When cometd.reload() is called, the reload extension saves a
 * short-lived cookie with the CometD session state details. When the new page loads up, it will execute the same code executed on the first page
 * load, namely the code that configured CometD, that registered channel listeners, and that finally called cometd.handshake(). The reload
 * extension is invoked upon the new handshake, sees that the short lived cookie saved previously is there, and re-establishes the CometD session
 * with the information stored in the short lived cookie.
 * <p/>
 * Function cometd.reload() should be called from the page unload event handler.
 * <p/>
 * Over the years, browsers, platforms and specifications have tried to clear the confusion around what actions really trigger an unload event,
 * and whether there are different events triggered for a single user action such as closing the browser window, hitting the browser back button
 * or clicking on a link.
 * <p/>
 * As a rule of thumb, function cometd.reload() should be called from an event handler that allows to write cookies.
 * <p/>
 * Typically the window.onbeforeunload event is a good place to call cometd.reload(), but historically the window.onunload event worked too in
 * most browser/platform combinations. More recently, the window.onpagehide event was defined (although with a slightly different semantic) and
 * should work too.
 * <p/>
 * Applications should start binding the cometd.reload() call to the window.onbeforeunload event and then test/experiment if that is the right
 * event. You should verify the behaviour for your use case, for your browser/platform combinations, for actions that may trigger the event (for
 * example: download links, javascript: links, etc.).
 * <p/>
 * Unfortunately the confusion about an unload event is not completely cleared yet, so you are advised to test this feature very carefully in a
 * variety of conditions.
 */
@SyntheticType
public class ReloadExtensionConfig {

	/**
	 * The name of the short-lived cookie the reload extension uses to save the connection state details.
	 * <p/>
	 * The default value is "org.cometd.reload"
	 */
	public String cookieName;

	/**
	 * The path of the short-lived cookie the reload extension uses.
	 * <p/>
	 * The default value is "/"
	 */
	public String cookiePath;

	/**
	 * The max age, in seconds, of the short-lived cookie the reload extension uses
	 * <p/>
	 * The default value is 5
	 */
	public long cookieMaxAge;

}
