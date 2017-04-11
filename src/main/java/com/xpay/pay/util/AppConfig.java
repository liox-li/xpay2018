/**
 * Copyright (c) 2014, Blackboard Inc. All Rights Reserved.
 */
package com.xpay.pay.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * ClassName: CommonPropertiesLoader
 * Function: TODO
 *
 * @Author: lhjiang
 * @Date: Dec 18, 2014 6:10:40 PM
 */
public class AppConfig {

	private static final Logger LOG = LogManager.getLogger(AppConfig.class);
	private Properties properties;
	
	public static final AppConfig MiaoFuConfig = new AppConfig(load("miaofu.config"));
	
	public static final AppConfig SwirfPassConfig = new AppConfig(load("swiftpass.config"));
	
	public static final AppConfig ChinaUmsConfig = new AppConfig(load("chinaums.config"));
	
	public static final AppConfig XPayConfig = new AppConfig(load("xpay.config"));
	
	public AppConfig(Properties properties) {
		this.properties = properties;
	}
	
	
	private static Properties load(String propFileName) {
		Properties props = new Properties();
		try {
			props.load(new FileReader(new File("/usr/local/xpay/" + propFileName)));
		} catch (Exception e) {
			try {
				InputStream is = Thread.currentThread().getContextClassLoader()
						.getResourceAsStream(propFileName);
				props.load(is);
			} catch (IOException e1) {
				LOG.error("Could not find property file "+ propFileName, e1);
			}
		}
		return props;
	}

	public String getProperty(String key) {
		return properties.getProperty(key, "");
	}
	
	public boolean getProperty(String key, boolean defaultValue) {
		String val = properties.getProperty(key, "");
		if ((StringUtils.isNotBlank(val))) {
			return Boolean.valueOf(val);
		}
		return defaultValue;
	}
}
