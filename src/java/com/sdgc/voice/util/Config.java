package com.sdgc.voice.util;

/**
 *
 * @author ranjeetr
 */
import java.util.Properties;



public final class Config {
	
		private static Properties props;

	private Config() {
	}
	
	static {
		try {
			props = PropertyLoader.loadProperties("voiceConfig.properties");
		} catch (Exception ex) {			
			System.err.println("Error reading voiceAuthConfig.properties file" + ex);
			throw new RuntimeException(ex);
		}
	}

	public static Properties getProps() {
		return props;
	}

	public static void setProps(final Properties props) {
		Config.props = props;
	}
}