package com.dubbo.common.core.util;

import java.io.Serializable;

import com.dubbo.common.core.exception.BusinessException;

/***
 * rest api 接口定义实体
 * 
 * @author liangchao.min
 *
 * @param <T>
 */
@SuppressWarnings("serial")
public class RestApiResponse<T> implements Serializable {

	public final static String SUCCESS = "000000";
	public final static String ERROR = "000001";
	public final static String EXCEPTION_BUSINESS = "100000";

	private String code;
	private String messge;
	private Object data;

	public RestApiResponse() {
		super();
	}

	public RestApiResponse(String code, String messge, T data) {
		super();
		this.code = code;
		this.messge = messge;
		this.data = data;
	}

	public static <T> RestApiResponse<T> response(String code, String messge, T data) {
		return new RestApiResponse<T>(code, messge, data);
	}

	public static <T> RestApiResponse<T> success(String message, T data) {
		return response(SUCCESS, message, data);
	}

	public static <T> RestApiResponse<T> error(Throwable e) {
		if (e instanceof BusinessException) {
			String message = ((BusinessException) e).getMessage();
			return response(EXCEPTION_BUSINESS, message, null);
		}
		return error(e.toString());
	}

	public static <T> RestApiResponse<T> error(String message) {
		return response(ERROR, message, null);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessge() {
		return messge;
	}

	public void setMessge(String messge) {
		this.messge = messge;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
