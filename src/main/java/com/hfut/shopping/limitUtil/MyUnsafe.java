package com.hfut.shopping.limitUtil;

import java.lang.reflect.Field;
import sun.misc.Unsafe;

@SuppressWarnings("restriction")
public class MyUnsafe {
	
	public static Unsafe getUnsafe(){
		try {
			Field f = Unsafe.class.getDeclaredField("theUnsafe");
			f.setAccessible(true);
			return (Unsafe) f.get(null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
