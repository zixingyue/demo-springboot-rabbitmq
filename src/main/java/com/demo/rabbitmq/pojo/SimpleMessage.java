/***********************************************************************
 * Module:  OrderParamenterDto.java
 * Author:  Administrator
 * Purpose: Defines the Class OrderParamenterDto
 ***********************************************************************/

package com.demo.rabbitmq.pojo;

import java.io.Serializable;

public class SimpleMessage implements Serializable, Cloneable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3980673065232816649L;
	
	/** 消息内容 */
	private String content;
	/** 消息类型*/
	private int type;
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
}