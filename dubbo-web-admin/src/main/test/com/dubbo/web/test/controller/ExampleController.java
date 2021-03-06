package com.dubbo.web.test.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dubbo.common.core.base.BaseController;
import com.dubbo.common.core.util.RestApiResponse;
import com.dubbo.facade.admin.service.ExampleService;
import com.dubbo.web.admin.util.CacheUtils;
import com.dubbo.web.test.util.TestSynThread;

@Api("ExampleController")
@RestController
@RequestMapping("/example")
public class ExampleController extends BaseController {

	private final static Logger LOGGER = LoggerFactory.getLogger(ExampleController.class);

	@Resource
	private ExampleService exampleService;
	@Resource
	private RedisTemplate<String, Object> redisTemplate;

	@ApiOperation(value = " 测试api", notes = " 测试api")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success") })
	@RequestMapping(value = "/testApi", method = RequestMethod.POST)
	@ResponseBody
	public RestApiResponse<String> testApi() {
		LOGGER.debug("start test Api ...");
		return success();
	}

	@ApiOperation(value = " 测试jdbc", notes = " 测试jdbc")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success") })
	@RequestMapping(value = "/testJdbc", method = RequestMethod.POST)
	@ResponseBody
	public RestApiResponse<Integer> testJdbc() {
		try {
			int num = exampleService.testJdbc();
			LOGGER.debug("start test Jdbc ...");
			return success(num);
		} catch (Exception e) {
			return error(e);
		}
	}

	@ApiOperation(value = " 测试redis", notes = " 测试redis")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success") })
	@RequestMapping(value = "/testRedis", method = RequestMethod.POST)
	@ResponseBody
	public RestApiResponse<Object> testRedis() {
		try {
			double str = CacheUtils.decr("key100", 1);
			LOGGER.debug("start test redis ...");
			return success((Object) str);
		} catch (Exception e) {
			return error(e);
		}
	}

	@ApiOperation(value = " 测试cache", notes = " 测试cache")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success") })
	@RequestMapping(value = "/testCache", method = RequestMethod.POST)
	@ResponseBody
	public RestApiResponse<String> testCache() {
		LOGGER.debug("start test cache ...");
		return success(exampleService.uuid());
	}

	@ApiOperation(value = " 测试logback", notes = " 测试logback")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success") })
	@RequestMapping(value = "/testLogBack", method = RequestMethod.POST)
	@ResponseBody
	public RestApiResponse<String> testLogBack() {
		exampleService.testLog();
		return success();
	}

	@ApiOperation(value = " 测试分布式锁", notes = " 测试分布式锁")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success") })
	@RequestMapping(value = "/testLock", method = RequestMethod.POST)
	@ResponseBody
	public RestApiResponse<String> testLock() {

		Thread thread1 = new Thread(new TestSynThread(exampleService, redisTemplate, "thread1"));
		Thread thread2 = new Thread(new TestSynThread(exampleService, redisTemplate, "thread2"));
		Thread thread3 = new Thread(new TestSynThread(exampleService, redisTemplate, "thread3"));
		Thread thread4 = new Thread(new TestSynThread(exampleService, redisTemplate, "thread4"));

		thread1.start();
		thread2.start();
		thread3.start();
		thread4.start();

		return success();
	}

}