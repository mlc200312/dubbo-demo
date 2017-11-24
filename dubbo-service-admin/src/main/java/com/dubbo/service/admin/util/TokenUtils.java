package com.dubbo.service.admin.util;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * token 工具类
 * 
 * @author liangchao.min
 *
 */
@Component
public class TokenUtils {

	public final static String separator = "_";
	public final static String TOKEN_UNIQUE_ID = "TOKEN_UNIQUE_ID";
	public final static String TOKEN_PASSWORD = "TOKENFUFANG0123456";

	private static TokenUtils TOKEN_UTIL;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	private TokenUtils() {
		super();
	}

	@PostConstruct
	// 关键二 通过@PostConstruct 和 @PreDestroy 方法 实现初始化和销毁bean之前进行的操作
	public void init() {
		TOKEN_UTIL = this;
		TOKEN_UTIL.redisTemplate = this.redisTemplate;
	}

	public static TokenUtils Builder() {
		return TOKEN_UTIL;
	}

	/**
	 * set token
	 */
	public String setToken(String token) {
		String key = TOKEN_UNIQUE_ID + separator + token;
		BoundValueOperations<String, String> operations = TOKEN_UTIL.redisTemplate.boundValueOps(key);
		operations.expire(3600, TimeUnit.SECONDS);
		operations.set(token);
		return token;
	}

	/**
	 * 队列取出token
	 */
	public synchronized boolean popToken(String token) {
		String key = TOKEN_UNIQUE_ID + separator + token;
		if (token != null && TOKEN_UTIL.redisTemplate.hasKey(key)) {
			TOKEN_UTIL.redisTemplate.delete(key);
			return true;
		}
		return false;
	}

	/**
	 * has token
	 */
	public boolean hasToken(String token) {
		String key = TOKEN_UNIQUE_ID + separator + token;
		return token != null && TOKEN_UTIL.redisTemplate.hasKey(key);
	}

	/**
	 * is token password
	 */
	public boolean isTokenPassword(String token) {
		return TokenUtils.TOKEN_PASSWORD.equalsIgnoreCase(token);
	}

	/**
	 * is not token password
	 */
	public boolean isNotTokenPassword(String token) {
		return !TokenUtils.TOKEN_PASSWORD.equalsIgnoreCase(token);
	}
}
