package com.xpay.pay.util;

import java.io.IOException;
import java.lang.reflect.Type;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

public class JacksonHttpMessageConverter extends MappingJackson2HttpMessageConverter {
    
   public JacksonHttpMessageConverter() {
        super();
		setObjectMapper(JsonUtils.jsonMapper);
    }

    @Override
    protected Long getContentLength(Object t, MediaType contentType) throws IOException {
        byte[] responseBytes = getObjectMapper().writeValueAsBytes(t);
        return ArrayUtils.isNotEmpty(responseBytes)?Long.valueOf(responseBytes.length):Long.valueOf(-1l);
    }

	@Override
	public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException {
		Object obj  = null;
		try{
			obj = super.read(type, contextClass, inputMessage);
		}catch(HttpMessageNotReadableException ex){
			throw ex;
		}
		
		return obj;
	}
    
}
