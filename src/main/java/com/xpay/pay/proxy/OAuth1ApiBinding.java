/**
 * Copyright (c) 2016, Blackboard Inc. All Rights Reserved.
 */
package com.xpay.pay.proxy;

import org.springframework.social.oauth1.AbstractOAuth1ApiBinding;

/**
 * ClassName: BgOAuth1ApiBinding
 * Function: TODO
 *
 * @Author: cshen
 * @Date: 2016年7月25日 上午9:14:50
 */
public class OAuth1ApiBinding extends AbstractOAuth1ApiBinding {
	public OAuth1ApiBinding(String consumerKey, String consumerSecret) {
		super(consumerKey, consumerSecret, "", "");
	}
	
	public OAuth1ApiBinding(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret) {
		super(consumerKey, consumerSecret, accessToken, accessTokenSecret);
	}
}
