package com.demo.rabbitmq.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.alibaba.fastjson.JSON;
import com.demo.rabbitmq.pojo.ShortMessage;

public class MyLog {

	static Logger publishLog = LoggerFactory.getLogger("publishLog");
	static Logger subscribeLog = LoggerFactory.getLogger("subscribeLog");
	static Logger subscribeFalseLog = LoggerFactory.getLogger("subscribeFalseLog");
	static Logger publishFalseLog = LoggerFactory.getLogger("publishFalseLog");
	
	public static void pubInfo(ShortMessage shortMessage,Exception e){
		publishLog.info("{}|{}|{}|{}",
				null==e?true:false,
				Cont.pubMsgName[shortMessage.getType()],
				JSON.toJSONString(shortMessage),
				null==e?"":e.getMessage());
		if(null!=e){
			pubFalseInfo(Cont.pubMsgName[shortMessage.getType()], shortMessage, e.getMessage());
		}
	}
	public static void pubFalseInfo(String methodExplain,ShortMessage shortMessage,Object result){
		publishFalseLog.info("{}|{}|{}",
				methodExplain,
				JSON.toJSONString(shortMessage),
				JSON.toJSONString(result));	
	}
	public static void subErrorInfo(String explain,Object message,Exception e){
		subscribeLog.info("{}|{}|{}",
				explain,
				JSON.toJSONString(message),
				e.getMessage());
		subscribeFalseLog.info("{}|{}|{}",
				explain,
				JSON.toJSONString(message),
				e.getMessage());
	}
	public static void subInfo(ShortMessage shortMessage,Object result,boolean rs){
		subscribeLog.info("{}|{}|{}|{}",
				rs,
				Cont.subMsgName[shortMessage.getType()]+(rs==true?"":"返回false"),
				JSON.toJSONString(shortMessage),
				JSON.toJSONString(result));	
		if(!rs){
			subFalseInfo(Cont.subMsgName[shortMessage.getType()]+"返回false",shortMessage, result);
		}
	}
	public static void subFalseInfo(String methodExplain,ShortMessage shortMessage,Object result){
		subscribeFalseLog.info("{}|{}|{}",
				methodExplain,
				JSON.toJSONString(shortMessage),
				JSON.toJSONString(result));	
	}
	public static void subException(ShortMessage shortMessage,Exception e,boolean toQueue){
		subscribeLog.info("{}|{}|{}|{}",
				false,
				Cont.subMsgName[shortMessage.getType()]+"接口异常"+(true==toQueue?",消息重新加入队列":""),
				JSON.toJSONString(shortMessage),
				e.getMessage());
		subFalseInfo(Cont.subMsgName[shortMessage.getType()]+"接口异常",shortMessage, e.getMessage());
	}
	
	
}
