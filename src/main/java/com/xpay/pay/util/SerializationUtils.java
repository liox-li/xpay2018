package com.xpay.pay.util;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;


public class SerializationUtils {
	@SuppressWarnings("unchecked")
	public static final <E> E toOject(byte[] byteArr) {
		try {
	        Kryo kryo = new Kryo();
	        Input input = new Input(byteArr);
	        return (E)kryo.readClassAndObject(input);
	    } catch (Exception e) {
	        return null;
	    }
	}
	
	public static final <E> byte[] toByteArray(E obj) {
		try {
            Kryo kryo = new Kryo();
            byte[] buffer = new byte[2048];
            Output output = new Output(buffer);
            kryo.writeClassAndObject(output, obj);
            return output.toBytes();
        } catch (Exception e) {
            return null;
        }
	}
}
