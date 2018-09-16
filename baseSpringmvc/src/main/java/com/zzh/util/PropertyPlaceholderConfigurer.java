package com.zzh.util;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.Properties;

/**
 * 资源文件配置加载器
 * @author yuanhaibo
 *
 */
public class PropertyPlaceholderConfigurer
		extends org.springframework.beans.factory.config.PropertyPlaceholderConfigurer {

	private static Properties props;
	@Override
	public Properties mergeProperties() throws IOException {
		props = super.mergeProperties();
		Property.init(props);
//		if(StringUtils.isNotBlank(props.getProperty("http.proxyHost"))){
//			System.setProperty("http.proxyHost",props.getProperty("http.proxyHost"));
//			System.setProperty("http.proxyPort",props.getProperty("http.proxyPort"));
//			System.setProperty("https.proxyHost",props.getProperty("https.proxyHost"));
//			System.setProperty("https.proxyPort",props.getProperty("https.proxyPort"));
//
//			/**
//			 * 设置不需要通过代理服务器访问的主机，
//			 * 	可以使用*通配符，多个地址用|分隔
//			 * 	注：它没有https.nonProxyHosts属性，它按照http.nonProxyHosts 中设置的规则访问
//			 * @author ouguangwei
//			 */
//			if(StringUtils.isNotBlank(props.getProperty("http.nonProxyHosts"))){
//				System.setProperty("http.nonProxyHosts", props.getProperty("http.nonProxyHosts"));
//			}
//
//			logger.info("http.proxyHost:"+props.getProperty("http.proxyHost"));
//			logger.info("http.proxyPort:"+props.getProperty("http.proxyPort"));
//			logger.info("https.proxyHost:"+props.getProperty("https.proxyHost"));
//			logger.info("https.proxyPort:"+props.getProperty("https.proxyPort"));
//			logger.info("http.nonProxyHosts:"+props.getProperty("http.nonProxyHosts"));
//		}

		//处理加密数据
		Enumeration<?> keys = props.propertyNames();
		while (keys.hasMoreElements()) {
			String key = (String)keys.nextElement();
			String value = props.getProperty(key);
//			if (key.endsWith(".encryption") && null != value) {
//				props.remove(key);
//				key = key.substring(0, key.length() - 11);
//				try {
//					value = AESUtil.decrypt(value.trim(),null);
//				} catch (ControllerException e) {
//					e.printStackTrace();
//				}
//				props.setProperty(key, value);
//			}
			System.setProperty(key, value);
		}

		return props;
	}


	public static String getProperty(String key) {
		return props.getProperty(key);
	}

	public static String getProperty(String key, String defaultValue) {
		return props.getProperty(key, defaultValue);
	}
	
	public static String getProperty(String key,String[]str){
		return MessageFormat.format(props.getProperty(key),str);
	}
}
