package com.dubbo.service.admin.service.impl;

import java.util.UUID;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dubbo.facade.admin.service.ExampleService;
import com.dubbo.service.admin.mapper.ExampleMapper;

@Service("exampleService")
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class ExampleServiceImpl implements ExampleService {

	private final static Logger LOGGER = LoggerFactory.getLogger(ExampleServiceImpl.class);

	@Resource
	private ExampleMapper exampleMapper;

	@Override
	public int testJdbc() {
		return exampleMapper.testJdbc();
	}

	@Override
	@Cacheable(value = "uuid")
	public String uuid() {
		return UUID.randomUUID().toString();
	}

	@Override
	public void testLog() {
		try {
			LOGGER.debug("logback start ...");
			int i = 1 / 0;
			System.out.println(i);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage());
			LOGGER.debug("logback end ...");
		}
		int[] arr = { 0, 1, 2, 3 };
		int temp = arr[0];
		arr[3] = arr[0];
		arr[2] = arr[1];
		arr[1] = arr[2];
		arr[0] = temp;
		int out = arr[4];
		System.out.println(out);
	}

}
