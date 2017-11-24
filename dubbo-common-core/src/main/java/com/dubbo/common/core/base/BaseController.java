package com.dubbo.common.core.base;

import com.dubbo.common.core.util.RestApiResponse;


/**
 * controller 基类
 * 
 * @author liangchao.min
 *
 */
public class BaseController {
	protected static final String RETURNMESSAGE = "返回结果 -- code:000000(成功),code:000001(业务异常),code:100000(失败)";
	protected static final String SUCCESS = "success";

	protected <T> RestApiResponse<T> success() {
		return RestApiResponse.success(SUCCESS, null);
	}

	protected <T> RestApiResponse<T> success(T data) {
		return RestApiResponse.success(SUCCESS, data);
	}

	protected <T> RestApiResponse<T> error(Throwable e) {
		e.printStackTrace();
		return RestApiResponse.error(e);
	}
}
