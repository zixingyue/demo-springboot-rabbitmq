package com.demo.rabbitmq.serviceImpl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.demo.rabbitmq.pojo.Result;
import com.demo.rabbitmq.pojo.ShortMessage;
import com.demo.rabbitmq.producer.SimpleProducer;
import com.demo.rabbitmq.util.MyLog;
import com.rabbitmq.client.Channel;


@Service
public  class ConsumerImpl implements ChannelAwareMessageListener {
	static Logger logger = LoggerFactory.getLogger(ConsumerImpl.class);
	
	
	
	@Autowired
	DealMethod dm;
	@Autowired
	SimpleProducer pro;
	
	
	public void onMessage(Message message, Channel channel) {
		byte[] msg = message.getBody();
		String obj = "";
		
		boolean ex = false;//是否发生异常
		Exception e = null;//异常对象
		
		try {
			obj = new String(msg,"utf-8");
		} catch (UnsupportedEncodingException e1) {
			ex = true;
			e = e1;
		} 
		
		if(ex){
			try {
				//确认消息
				channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
				MyLog.subErrorInfo("消息格式转换失败,从MQ接收消息处理失败", obj, e);
			} catch (IOException e1) {
				logger.error("{}|{}","确认消息异常",e1.getMessage());
			}
		}else{
			ShortMessage shortMessage=JSON.parseObject(obj,  ShortMessage.class );
			try {
				Result result = null;
				result = dm.method(shortMessage);				
				//处理消息完毕,写入日志
				MyLog.subInfo(shortMessage, result, result.isResult());						
			} catch (Exception e1) {
				ex = true;
				e = e1;
			}
			try {
				//确认消息
				channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
				if(ex){
					//记录日志
					MyLog.subException(shortMessage, e,true);
					//重新发送消息到队列
					pro.produceMsg(shortMessage);
				}
			} catch (IOException e1) {
				logger.error("{}|{}","确认消息异常",e1.getMessage());
			}
		}
	}
}
