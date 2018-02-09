package com.xpay.pay.util;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {
	public final static ObjectMapper jsonMapper = getObjectMapper();

	public static <T> String toJson(T t) {
		if(t == null) {
			return null;
		}
		try {
			return jsonMapper.writeValueAsString(t);
		} catch (Exception e) {
			return "";
		}
	}

	public static <T> String toPrettyJson(T t) {
		if(t == null) {
			return null;
		}
		try {
			return jsonMapper.writerWithDefaultPrettyPrinter()
					.writeValueAsString(t);
		} catch (Exception e) {
			return "";
		}
	}

	public static <T> T fromJson(String jsonStr, Class<T> clazz) {
		if(StringUtils.isBlank(jsonStr)) {
			return null;
		}
		try {
			return jsonMapper.readValue(jsonStr, clazz);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static <T> List<T> fromJsonArray(String jsonArr, Class<T> clazz) {
		if(StringUtils.isBlank(jsonArr)) {
			return null;
		}
		try {
			List<T> entities = jsonMapper.readValue(jsonArr, jsonMapper
					.getTypeFactory()
					.constructCollectionType(List.class, clazz));
			return entities;
		} catch (Exception e) {
			return Collections.emptyList();
		}
	}
	
	private static ObjectMapper getObjectMapper() {
		ObjectMapper instance = new ObjectMapper();
		instance.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		// instance.enable(SerializationFeature.WRITE_ENUMS_USING_INDEX);
		instance.setSerializationInclusion(Include.NON_NULL);
		instance.setSerializationInclusion(Include.NON_EMPTY);
		instance.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ"));
		instance.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return instance;
	}
}
