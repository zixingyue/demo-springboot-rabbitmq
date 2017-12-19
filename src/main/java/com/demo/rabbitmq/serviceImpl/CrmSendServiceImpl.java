package com.demo.rabbitmq.serviceImpl;

import java.util.UUID;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.rabbitmq.pojo.Result;
import com.demo.rabbitmq.pojo.ShortMessage;
import com.demo.rabbitmq.pojo.SimpleMessage;
import com.demo.rabbitmq.producer.SimpleProducer;
import com.demo.rabbitmq.service.CrmSendService;

@Service("crmSendService")
public class CrmSendServiceImpl implements CrmSendService{
	
	static Logger logger = LoggerFactory.getLogger(CrmSendServiceImpl.class);

	@Autowired
	SimpleProducer pro;
	
	@Override
	public Result produceMsg(SimpleMessage msg) {
		ShortMessage shortMessage=new ShortMessage();
		shortMessage.setContent(msg.getContent());
		shortMessage.setIe(UUID.randomUUID().toString().replaceAll("-", ""));
		shortMessage.setType(msg.getType());
		return pro.produceMsg(shortMessage);
	} 
	
	

}
