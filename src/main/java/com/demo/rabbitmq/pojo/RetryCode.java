package com.demo.rabbitmq.pojo;

public class RetryCode {

	public static final int SPRINGRETRY_MAXATTEMPTS = 5;//最大尝试次数
	public static final long SPRINGRETRY_DELAY = 100l;//
	public static final long SPRINGRETRY_MAXDELAY = 1000l;//
	public static final int SPRINGRETRY_MULTIPLIER = 2;
	
	
	public static final int SPRINGRETRY_MAXATTEMPTS_SENDMSG = 5;//最大尝试次数
	public static final long SPRINGRETRY_DELAY_SENDMSG = 100l;//
}
