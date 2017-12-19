package com.demo.rabbitmq.pojo;

import java.io.Serializable;

public class ShortMessage implements Serializable {
	
	/** 
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = -4335601705580186797L;

	/** 信息内容 */
	private String content;
	
	/** 信息编号 */
	private String ie;
	
	/** 信息类型*/
	private int type;
	
	public ShortMessage( ){
	}
	public ShortMessage(String content,String ie,int type){
		this.content = content;
		this.ie = ie;
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getIe() {
		return ie;
	}

	public void setIe(String ie) {
		this.ie = ie;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}

}
