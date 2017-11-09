package com.xpay.pay.util;

import java.util.AbstractList;
import java.util.Iterator;
import java.util.LinkedList;

public class RoundRobinList<E> extends AbstractList<E> {
	
	private int size;
	private LinkedList<E> list;
	
	public RoundRobinList(int size) {
		this.size = size;
		this.list = new LinkedList<E>();
	}

	@Override
	public E get(int index) {
		return this.list.get(index);
	}
	
	@Override 
	public boolean add(E e) {
		if(this.list.size()>=size) {
			this.list.removeFirst();
		}
		this.list.add(e);
		return true;
	}
	
	@Override
	public boolean contains(Object o) {
		return this.list.contains(o);
	}

	@Override
	public int size() {
		return this.list.size();
	}
	
	@Override
	public Iterator<E> iterator() {
	     return list.iterator();
	}	
	
}
