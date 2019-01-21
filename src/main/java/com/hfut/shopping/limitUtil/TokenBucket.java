package com.hfut.shopping.limitUtil;

import java.util.concurrent.TimeUnit;

import sun.misc.Unsafe;

@SuppressWarnings("restriction")
public class TokenBucket {
	
	private final int maxValue;
	
	private volatile int value;
	
	private final int addOnce;
	
	private static final Unsafe unsafe = MyUnsafe.getUnsafe();
	
	private static long VALUE_OFFSET;
	
	static {
		try {
			VALUE_OFFSET=unsafe.objectFieldOffset(TokenBucket.class.getDeclaredField("value"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public TokenBucket(int maxValue, int addOnce, TimeUnit timeUtil, int times) {
		this.maxValue = maxValue;
		this.addOnce = addOnce;
		value=maxValue;
		new java.lang.Thread(()->{
			for(;;) {
				add();
				try {
					timeUtil.sleep(times);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}) .start();
	}

	private void add() {
		int oldValue=value;
		int newValue=oldValue+addOnce;
		while(!unsafe.compareAndSwapInt(this, VALUE_OFFSET, oldValue, newValue>maxValue?maxValue:newValue)) {
			oldValue=value;
			newValue=oldValue+addOnce;
		}
	}
	
	public boolean get() {
		int oldValue=value;
		if(oldValue>0) {
			while(!unsafe.compareAndSwapInt(this, VALUE_OFFSET, oldValue, oldValue-1)) {
				if(value<0) {
					return false;
				}
				oldValue=value;
			}
			return true;
		}
		return false;
	}
}
