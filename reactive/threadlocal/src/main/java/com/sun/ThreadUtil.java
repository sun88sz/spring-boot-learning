package com.sun;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.time.DateFormatUtils;

public class ThreadUtil {
	private static ThreadPoolExecutor executor = new ThreadPoolExecutor(5, Integer.MAX_VALUE, 20, TimeUnit.SECONDS,
			new LinkedBlockingQueue(10));;

	public static void submit(Runnable command) {
		if (null != command) {
			executor.submit(command);
		}
	}

	public static int getThreadCount() {
		return Thread.activeCount();
	}

	public static Thread getCurrent() {
		return Thread.currentThread();
	}

	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}


	public static void main(String[] args) {
		
		for (int i = 0; i < 10; i++) {
			ThreadUtil.submit(
					() -> {
						Thread thread = Thread.currentThread();
						String d1 = DateFormatUtils.format(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss");
						System.out.println(thread.getName() + " start : " + d1);
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						String d2 = DateFormatUtils.format(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss");
						System.out.println(thread.getName() + " end  : " + d2);
					}
			);
		}
		
		
	}
}
