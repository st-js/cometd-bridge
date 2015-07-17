package org.stjs.bridge.cometd;

import org.stjs.javascript.annotation.SyntheticType;

@SyntheticType
public class TimeSyncExtensionConfig {

	/**
	 * Configures the size of the sliding average of the offsets received used to smooth over any transient fluctuations. By default this is
	 * over 10 messages, but you can change this value by passing a configuration object during the creation of the extension:
	 * <p/>
	 * <pre>
	 *   // Unregister the default timesync extension
	 *   cometd.unregisterExtension("timesync");
	 *
	 *   // Re-register with different configuration
	 *   cometd.registerExtension("timesync", new TimeSyncExtension(new TimeSyncExtensionConfig(){{ maxSamples = 20 }}));
	 * </pre>
	 */
	public int maxSamples;

}
