package com.zzh.util;

import java.util.Properties;

/**
 * 资源文件类
 * @author yuanhaibo
 *
 */
public class Property {
	private static Properties property=new Properties();

	private Property() {
	}

	static void init(Properties props) {
		property = props;
	}

	public static String getProperty(String key) {
		return property.getProperty(key);
	}

	public static String getProperty(String key, String defaultValue) {
		return property.getProperty(key, defaultValue);

	}
}
