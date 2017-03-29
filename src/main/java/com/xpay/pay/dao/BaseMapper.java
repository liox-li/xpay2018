package com.xpay.pay.dao;


public interface BaseMapper<E> {
	public E findById(long id);

	boolean insert(E record);

	boolean updateById(E record);

	boolean deleteById(long id);
}
