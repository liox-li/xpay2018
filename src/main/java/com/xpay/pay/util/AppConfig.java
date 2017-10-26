package com.xpay.pay.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AppConfig {

	private static final Logger LOG = LogManager.getLogger(AppConfig.class);
	private Properties properties;
	
	public static final AppConfig MiaoFuConfig = new AppConfig(load("miaofu.config"));
	
	public static final AppConfig SwirfPassConfig = new AppConfig(load("swiftpass.config"));
	
	public static final AppConfig ChinaUmsConfig = new AppConfig(load("chinaums.config"));
	
	public static final AppConfig ChinaUmsV2Config = new AppConfig(load("chinaumsv2.config"));
	
	public static final AppConfig KeFuConfig = new AppConfig(load("kefu.config"));
	
	public static final AppConfig RubiPayConfig = new AppConfig(load("rubipay.config"));
	
	public static final AppConfig BaiFuConfig = new AppConfig(load("baifu.config"));
	
	public static final AppConfig JuZhenConfig = new AppConfig(load("juzhen.config"));
	
	public static final AppConfig XPayConfig = new AppConfig(load("xpay.config"));

	public static final AppConfig kekePayConfig = new AppConfig(load("kekepay.config"));
	
	public static final AppConfig ChinaUmsH5Config = new AppConfig(load("chinaumsh5.config"));
	
	public static final AppConfig ChinaUmsWapConfig = new AppConfig(load("chinaumswap.config"));
	
	public static final AppConfig UPayConfig = new AppConfig(load("upay.config"));

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
