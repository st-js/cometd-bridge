package org.stjs.bridge.cometd;

public class BayeuxMessage {
	public static class Data {
		public String content;
	}

	public boolean successful;

	public Data data;
}
