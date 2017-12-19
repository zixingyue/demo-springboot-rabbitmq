package com.demo.rabbitmq.producerImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.demo.rabbitmq.pojo.Result;
import com.demo.rabbitmq.pojo.ShortMessage;
import com.demo.rabbitmq.producer.SimpleProducer;
import com.demo.rabbitmq.util.Cont;
import com.demo.rabbitmq.util.MyLog;

@Service
public class SimpleProducerImpl implements SimpleProducer {
	static Logger logger = LoggerFactory.getLogger(SimpleProducerImpl.class);

	@Value("${mq.vhost.crm}")
	public String vhostCrm;
	@Value("${mq.vhost.common}")
	public String vhostCommon;
	
	@Value("${mq.demo.topic.exchange1}")
	public String topicExchangeName1;
	@Value("${mq.demo.queue1}")
	public String queueName1;
	@Value("${mq.demo.queue1.routingKey}")
	public String queueRoutingKey1;
	
	@Value("${mq.demo.topic.exchange2}")
	public String topicExchangeName2;
	@Value("${mq.demo.queue2}")
	public String queueName2;
	@Value("${mq.demo.queue2.routingKey}")
	public String queueRoutingKey2;
	
	@Autowired
	MqSendMessage mqSendMessage;
	
	public Result sendMessage(ShortMessage shortMessage) {
		Result result=new Result();		
		try {			
			deal(shortMessage);	
			result.setResult(true);
			result.setCode(Cont.CODE_SUCCESS);
			result.setData(shortMessage.getIe());
			result.setMessage("推送消息成功");
			MyLog.pubInfo(shortMessage, null);
		} catch (Exception e) {
			result.setResult(false);
			result.setCode(Cont.CODE_FAILURE);
			result.setMessage("推送消息失败！");
			result.setError(e.getMessage() );
			MyLog.pubInfo(shortMessage, e);
		}
		return result;
	}
	
	//处理
	public boolean deal(ShortMessage shortMessage) throws Exception{
		boolean rs = false;		
		switch (shortMessage.getType()) {
			case 1:
				rs = mqSendMessage.mqSendMessage(vhostCrm,topicExchangeName1, "demo.queue1", shortMessage);
				break;
			case 2:
				rs = mqSendMessage.mqSendMessage(vhostCommon,topicExchangeName2, "demo.queue2", shortMessage);
				break;
			default:
				throw new Exception("type值不匹配");
		}
		return rs;
	}

	@Override
	public Result produceMsg(ShortMessage msg) {
		return sendMessage(msg);
	}
	

}
