package com.xpay.pay.util;

import static com.xpay.pay.ApplicationConstants.CODE_ERROR_JSON;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xpay.pay.exception.DBException;

public class JsonArrayTypeHandler<T extends Object> extends BaseTypeHandler<T> {

	protected ObjectMapper objectMapper = JsonUtils.jsonMapper;

	 private Class<T> clazz = null;
	
	public JsonArrayTypeHandler(Class<T> clazz) {
	 if (clazz == null)
	 throw new IllegalArgumentException("Type argument cannot be null");
	 this.clazz = clazz;
	 }

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType)
			throws SQLException {
		try {
			String jsonString = objectMapper.writeValueAsString(parameter);
			ps.setString(i, jsonString);
		} catch (JsonProcessingException e) {
			throw new DBException(CODE_ERROR_JSON, e.toString());
		}
	}

	@Override
	public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
		String jsonData = rs.getString(columnName);
		if (jsonData == null) {
			return null;
		}
		try {
			return objectMapper.readValue(jsonData,
					objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
		} catch (IOException e) {
			throw new DBException(CODE_ERROR_JSON, e.toString());
		}
	}

	@Override
	public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		String jsonData = rs.getString(columnIndex);
		if (jsonData == null) {
			return null;
		}
		try {
			return objectMapper.readValue(jsonData,
					objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
		} catch (IOException e) {
			throw new DBException(CODE_ERROR_JSON, e.toString());
		}
	}

	@Override
	public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		String jsonData = cs.getString(columnIndex);
		if (jsonData == null) {
			return null;
		}
		try {
			return objectMapper.readValue(jsonData,
					objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
		} catch (IOException e) {
			throw new DBException(CODE_ERROR_JSON, e.toString());
		}
	}

}
