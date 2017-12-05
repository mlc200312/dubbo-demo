package com.dubbo.web.test.util;

import org.springframework.data.redis.core.RedisTemplate;

import com.dubbo.facade.admin.service.ExampleService;
import com.dubbo.web.admin.util.RedisLock;

public class TestLockThread implements Runnable {

	private ExampleService exampleService;
	private RedisTemplate<String, Object> redisTemplate;
	private String name;

	public TestLockThread(ExampleService exampleService, RedisTemplate<String, Object> redisTemplate, String name) {
		super();
		this.exampleService = exampleService;
		this.redisTemplate = redisTemplate;
		this.name = name;
	}

	private Object syn = "syn";

	@Override
	public void run() {
		RedisLock lock = new RedisLock(redisTemplate, "Lock1");
		try {
			if (lock.lock()) {
				synchronized (syn) {
					method();
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	// private ReentrantLock lock = new ReentrantLock();
	//
	// @Override
	// public void run() {
	// try {
	// lock.lock();
	// method();
	// } finally {
	// lock.unlock();
	// }
	// }

	private void method() {
		Constant.count++;
		try {
			Thread.sleep(3 * 1000l);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		exampleService.insert(Constant.count);
		System.out.println(name + "打印了--------------------------------" + Constant.count);
	}

}
