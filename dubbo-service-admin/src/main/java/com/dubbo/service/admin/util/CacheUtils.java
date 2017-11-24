package com.dubbo.service.admin.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * 通用缓存工具类
 * 
 * @author liangchao.min
 *
 */
@Component
public class CacheUtils {

	private static CacheUtils CACHE_UTILS;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@PostConstruct
	// 关键二 通过@PostConstruct 和 @PreDestroy 方法 实现初始化和销毁bean之前进行的操作
	public void init() {
		CACHE_UTILS = this;
		CACHE_UTILS.redisTemplate = this.redisTemplate;
		CACHE_UTILS.stringRedisTemplate = this.stringRedisTemplate;
	}

	/**
	 * 删除缓存<br>
	 * 根据key精确匹配删除
	 * 
	 * @param key
	 */
	public static void del(String... key) {
		if (key != null && key.length > 0) {
			if (key.length == 1) {
				CACHE_UTILS.redisTemplate.delete(key[0]);
			} else {
				@SuppressWarnings("unchecked")
				List<String> keys = CollectionUtils.arrayToList(key);
				CACHE_UTILS.redisTemplate.delete(keys);
			}
		}
	}

	/**
	 * 批量删除<br>
	 * （该操作会执行模糊查询，请尽量不要使用，以免影响性能或误删）
	 * 
	 * @param pattern
	 */
	public static void batchDel(String... pattern) {
		for (String kp : pattern) {
			CACHE_UTILS.redisTemplate.delete(CACHE_UTILS.redisTemplate.keys(kp + "*"));
		}
	}

	/**
	 * 取得缓存（int型）
	 * 
	 * @param key
	 * @return
	 */
	public static Integer getInt(String key) {
		String value = CACHE_UTILS.stringRedisTemplate.boundValueOps(key).get();
		if (StringUtils.isNotBlank(value)) {
			return Integer.valueOf(value);
		}
		return null;
	}

	/**
	 * 取得缓存（字符串类型）
	 * 
	 * @param key
	 * @return
	 */
	public static String getStr(String key) {
		return CACHE_UTILS.stringRedisTemplate.boundValueOps(key).get();
	}

	/**
	 * 取得缓存（字符串类型）
	 * 
	 * @param key
	 * @return
	 */
	public static String getStr(String key, boolean retain) {
		String value = CACHE_UTILS.stringRedisTemplate.boundValueOps(key).get();
		if (!retain) {
			CACHE_UTILS.redisTemplate.delete(key);
		}
		return value;
	}

	/**
	 * 获取缓存<br>
	 * 注：基本数据类型(Character除外)，请直接使用get(String key, Class<T> clazz)取值
	 * 
	 * @param key
	 * @return
	 */
	public static Object getObj(String key) {
		return CACHE_UTILS.redisTemplate.boundValueOps(key).get();
	}

	/**
	 * 获取缓存<br>
	 * 注：java 8种基本类型的数据请直接使用get(String key, Class<T> clazz)取值
	 * 
	 * @param key
	 * @param retain
	 *            是否保留
	 * @return
	 */
	public static Object getObj(String key, boolean retain) {
		Object obj = CACHE_UTILS.redisTemplate.boundValueOps(key).get();
		if (!retain) {
			CACHE_UTILS.redisTemplate.delete(key);
		}
		return obj;
	}

	/**
	 * 获取缓存<br>
	 * 注：该方法暂不支持Character数据类型
	 * 
	 * @param key
	 *            key
	 * @param clazz
	 *            类型
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T get(String key, Class<T> clazz) {
		return (T) CACHE_UTILS.redisTemplate.boundValueOps(key).get();
	}

	/**
	 * 获取缓存json对象<br>
	 * 
	 * @param key
	 *            key
	 * @param clazz
	 *            类型
	 * @return
	 */
	public static <T> T getJson(String key, Class<T> clazz) {
		String json = CACHE_UTILS.stringRedisTemplate.boundValueOps(key).get();
		return JsonMapper.json2Obj(json, clazz);
	}

	/**
	 * 将value对象写入缓存
	 * 
	 * @param key
	 * @param value
	 * @param time
	 *            失效时间(秒)
	 */
	public static void set(String key, Object value, long timeout) {
		if (value.getClass().equals(String.class)) {
			CACHE_UTILS.stringRedisTemplate.opsForValue().set(key, value.toString());
		} else if (value.getClass().equals(Integer.class)) {
			CACHE_UTILS.stringRedisTemplate.opsForValue().set(key, value.toString());
		} else if (value.getClass().equals(Double.class)) {
			CACHE_UTILS.stringRedisTemplate.opsForValue().set(key, value.toString());
		} else if (value.getClass().equals(Float.class)) {
			CACHE_UTILS.stringRedisTemplate.opsForValue().set(key, value.toString());
		} else if (value.getClass().equals(Short.class)) {
			CACHE_UTILS.stringRedisTemplate.opsForValue().set(key, value.toString());
		} else if (value.getClass().equals(Long.class)) {
			CACHE_UTILS.stringRedisTemplate.opsForValue().set(key, value.toString());
		} else if (value.getClass().equals(Boolean.class)) {
			CACHE_UTILS.stringRedisTemplate.opsForValue().set(key, value.toString());
		} else {
			CACHE_UTILS.redisTemplate.opsForValue().set(key, value);
		}
		if (timeout > 0) {
			CACHE_UTILS.redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
		}
	}

	/**
	 * 将value对象以JSON格式写入缓存
	 * 
	 * @param key
	 * @param value
	 * @param time
	 *            失效时间(秒)
	 */
	public static void setJson(String key, Object value, long timeout) {
		CACHE_UTILS.stringRedisTemplate.opsForValue().set(key, JsonMapper.obj2Json(value));
		if (timeout > 0) {
			CACHE_UTILS.stringRedisTemplate.expire(key, timeout, TimeUnit.SECONDS);
		}
	}

	/**
	 * 更新key对象field的值
	 * 
	 * @param key
	 *            缓存key
	 * @param field
	 *            缓存对象field
	 * @param value
	 *            缓存对象field值
	 */
	public static void setJsonField(String key, String field, String value) {
		String json = CACHE_UTILS.stringRedisTemplate.boundValueOps(key).get();
		Map<String, String> result = JsonMapper.getInstance().fromJson(json, JsonMapper.getInstance().createCollectionType(HashMap.class, String.class, String.class));
		result.put(field, value);
		CACHE_UTILS.stringRedisTemplate.opsForValue().set(key, JsonMapper.obj2Json(result));
	}

	/**
	 * 递减操作
	 * 
	 * @param key
	 * @param by
	 * @return
	 */
	public static double decr(String key, double by) {
		return CACHE_UTILS.redisTemplate.opsForValue().increment(key, -by);
	}

	/**
	 * 递增操作
	 * 
	 * @param key
	 * @param by
	 * @return
	 */
	public static double incr(String key, double by) {
		return CACHE_UTILS.redisTemplate.opsForValue().increment(key, by);
	}

	/**
	 * 获取double类型值
	 * 
	 * @param key
	 * @return
	 */
	public static double getDouble(String key) {
		String value = CACHE_UTILS.stringRedisTemplate.boundValueOps(key).get();
		if (StringUtils.isNotBlank(value)) {
			return Double.valueOf(value);
		}
		return 0d;
	}

	/**
	 * 设置double类型值
	 * 
	 * @param key
	 * @param value
	 * @param time
	 *            失效时间(秒)
	 */
	public static void setDouble(String key, double value, long timeout) {
		CACHE_UTILS.stringRedisTemplate.opsForValue().set(key, String.valueOf(value));
		if (timeout > 0) {
			CACHE_UTILS.stringRedisTemplate.expire(key, timeout, TimeUnit.SECONDS);
		}
	}

	/**
	 * 设置double类型值
	 * 
	 * @param key
	 * @param value
	 * @param time
	 *            失效时间(秒)
	 */
	public static void setInt(String key, int value, long timeout) {
		CACHE_UTILS.stringRedisTemplate.opsForValue().set(key, String.valueOf(value));
		if (timeout > 0) {
			CACHE_UTILS.stringRedisTemplate.expire(key, timeout, TimeUnit.SECONDS);
		}
	}

	/**
	 * 将map写入缓存
	 * 
	 * @param key
	 * @param map
	 * @param time
	 *            失效时间(秒)
	 */
	public static <T> void setMap(String key, Map<String, T> map, long timeout) {
		CACHE_UTILS.redisTemplate.opsForHash().putAll(key, map);
	}

	/**
	 * 将map写入缓存
	 * 
	 * @param key
	 * @param map
	 * @param time
	 *            失效时间(秒)
	 */
	public static <T> void setMap(String key, T obj, long timeout) {
		String json = JsonMapper.obj2Json(obj);
		Map<String, String> map = (Map<String, String>) JsonMapper.json2Map(json, String.class, String.class);
		CACHE_UTILS.redisTemplate.opsForHash().putAll(key, map);
	}

	/**
	 * 向key对应的map中添加缓存对象
	 * 
	 * @param key
	 * @param map
	 */
	public static <T> void addMap(String key, Map<String, T> map) {
		CACHE_UTILS.redisTemplate.opsForHash().putAll(key, map);
	}

	/**
	 * 向key对应的map中添加缓存对象
	 * 
	 * @param key
	 *            cache对象key
	 * @param field
	 *            map对应的key
	 * @param value
	 *            值
	 */
	public static void addMap(String key, String field, String value) {
		CACHE_UTILS.redisTemplate.opsForHash().put(key, field, value);
	}

	/**
	 * 向key对应的map中添加缓存对象
	 * 
	 * @param key
	 *            cache对象key
	 * @param field
	 *            map对应的key
	 * @param obj
	 *            对象
	 */
	public static <T> void addMap(String key, String field, T obj) {
		CACHE_UTILS.redisTemplate.opsForHash().put(key, field, obj);
	}

	/**
	 * 获取map缓存
	 * 
	 * @param key
	 * @param clazz
	 * @return
	 */
	public static <T> Map<String, T> mget(String key, Class<T> clazz) {
		BoundHashOperations<String, String, T> boundHashOperations = CACHE_UTILS.redisTemplate.boundHashOps(key);
		return boundHashOperations.entries();
	}

	/**
	 * 获取map缓存
	 * 
	 * @param key
	 * @param clazz
	 * @return
	 */
	public static <T> T getMap(String key, Class<T> clazz) {
		BoundHashOperations<String, String, String> boundHashOperations = CACHE_UTILS.redisTemplate.boundHashOps(key);
		Map<String, String> map = boundHashOperations.entries();
		String json = JsonMapper.obj2Json(map);
		return JsonMapper.json2Obj(json, clazz);
	}

	/**
	 * 获取map缓存中的某个对象
	 * 
	 * @param key
	 * @param field
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getMapField(String key, String field, Class<T> clazz) {
		return (T) CACHE_UTILS.redisTemplate.boundHashOps(key).get(field);
	}

	/**
	 * 删除map中的某个对象
	 * 
	 * @author lh
	 * @date 2016年8月10日
	 * @param key
	 *            map对应的key
	 * @param field
	 *            map中该对象的key
	 */
	public void delMapField(String key, String... field) {
		BoundHashOperations<String, String, ?> boundHashOperations = redisTemplate.boundHashOps(key);
		for (int i = 0; i < field.length; i++) {
			boundHashOperations.delete(field[i]);
		}
	}

	/**
	 * 指定缓存的失效时间
	 * 
	 * @author FangJun
	 * @date 2016年8月14日
	 * @param key
	 *            缓存KEY
	 * @param time
	 *            失效时间(秒)
	 */
	public static void expire(String key, long timeout) {
		if (timeout > 0) {
			CACHE_UTILS.redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
		}
	}

	/**
	 * 添加set
	 * 
	 * @param key
	 * @param value
	 */
	public static void sadd(String key, String... value) {
		for (int i = 0; i < value.length; i++) {
			CACHE_UTILS.redisTemplate.boundSetOps(key).add(value[i]);
		}
	}

	/**
	 * 删除set集合中的对象
	 * 
	 * @param key
	 * @param value
	 */
	public static void srem(String key, String... value) {
		for (int i = 0; i < value.length; i++) {
			CACHE_UTILS.redisTemplate.boundSetOps(key).remove(value[i]);
		}
	}

	/**
	 * set重命名
	 * 
	 * @param oldkey
	 * @param newkey
	 */
	public static void srename(String oldkey, String newkey) {
		CACHE_UTILS.redisTemplate.boundSetOps(oldkey).rename(newkey);
	}

	/**
	 * 短信缓存
	 * 
	 * @author fxl
	 * @date 2016年9月11日
	 * @param key
	 * @param value
	 * @param time
	 */
	public static void setIntForPhone(String key, Object value, int time) {
		CACHE_UTILS.stringRedisTemplate.opsForValue().set(key, JsonMapper.obj2Json(value));
		if (time > 0) {
			CACHE_UTILS.stringRedisTemplate.expire(key, time, TimeUnit.SECONDS);
		}
	}

	/**
	 * 模糊查询keys
	 * 
	 * @param pattern
	 * @return
	 */
	public static Set<String> keys(String pattern) {
		return CACHE_UTILS.redisTemplate.keys(pattern);
	}

}
