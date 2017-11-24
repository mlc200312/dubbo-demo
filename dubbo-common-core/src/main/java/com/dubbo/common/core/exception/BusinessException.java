package com.dubbo.common.core.exception;

/**
 * 业务异常
 * 
 * @author liangchao.min
 *
 */
@SuppressWarnings("serial")
public class BusinessException extends RuntimeException {
	private String message;

	public BusinessException() {
		super();
	}

	public BusinessException(final String message) {
		this.message = message;
	}

	public BusinessException(final Throwable cause) {
		super(cause);
	}

	public String getMessage() {
		return message;
	}
}