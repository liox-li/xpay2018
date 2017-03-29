package com.xpay.pay.dao;

import java.util.List;

public interface BaseMapper<E> {
	public E findById(long id);

	public List<E> findAll();

	boolean insert(E record);

	boolean updateById(E record);

	boolean deleteById(long id);
}
