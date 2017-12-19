/***********************************************************************
 * Module:  CrmSendService.java
 * Author:  Administrator
 * Purpose: Defines the Interface CrmSendService
 ***********************************************************************/

package com.demo.rabbitmq.service;

import com.demo.rabbitmq.pojo.Result;
import com.demo.rabbitmq.pojo.SimpleMessage;

/**
 * 发送消息服务
 * @ClassName: CrmSendService 
 * @author wenyueguang 
 * @date 2017年12月18日 下午4:02:46
 */
public interface CrmSendService {
	
	public Result produceMsg(SimpleMessage msg);
	
}