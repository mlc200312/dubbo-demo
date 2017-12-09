package com.dubbo.web.test.util;

import java.util.concurrent.locks.ReentrantLock;

public class TestLockThread implements Runnable {

	private String name;

	public TestLockThread(String name) {
		super();
		this.name = name;
	}

	private static ReentrantLock lock = new ReentrantLock();

	@Override
	public void run() {
		try {
			lock.lock();
			method();
		} finally {
			lock.unlock();
		}
	}

	private void method() {
		Constant.count++;
		try {
			Thread.sleep(3 * 1000l);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(name + "打印了--------------------------------" + Constant.count);
	}

	public static void main(String[] args) {

		Thread thread1 = new Thread(new TestLockThread("thread1"));
		Thread thread2 = new Thread(new TestLockThread("thread2"));
		Thread thread3 = new Thread(new TestLockThread("thread3"));
		Thread thread4 = new Thread(new TestLockThread("thread4"));

		thread1.start();
		thread2.start();
		thread3.start();
		thread4.start();
	}

}
