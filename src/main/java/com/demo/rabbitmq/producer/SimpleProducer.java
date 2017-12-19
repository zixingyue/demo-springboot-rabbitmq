package com.demo.rabbitmq.producer;
import com.demo.rabbitmq.pojo.Result;
import com.demo.rabbitmq.pojo.ShortMessage;


public interface SimpleProducer {
	
	public Result produceMsg(ShortMessage shortMessage);

	
}
