package com.demo.rabbitmq.pojo;

import java.io.Serializable;

public class Result implements Serializable, Cloneable{

	/** 
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 6828769954785806743L;
	/** 操作结果 */
	protected boolean result;
	/** 操作结果信息说明 */
	protected String message;
	/** 异常信息 */
	protected String error;
	/** 状态码 */
	protected int code;
	/** 请求返回结果 */
	private Object data;
	
	public Result() {
	}
	
	public boolean isResult() {
		return result;
	}


	public void setResult(boolean result) {
		this.result = result;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public String getError() {
		return error;
	}


	public void setError(String error) {
		this.error = error;
	}


	public int getCode() {
		return code;
	}


	public void setCode(int code) {
		this.code = code;
	}


	public Object getData() {
		return data;
	}


	public void setData(Object data) {
		this.data = data;
	}

	

}
