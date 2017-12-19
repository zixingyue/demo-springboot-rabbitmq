package com.demo.rabbitmq.producerImpl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.SimpleResourceHolder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import com.demo.rabbitmq.pojo.RetryCode;
import com.demo.rabbitmq.pojo.ShortMessage;

@Component
public class MqSendMessage {
	
	static Logger logger = LoggerFactory.getLogger(MqSendMessage.class);
	
	@Autowired
	RabbitTemplate amqpTemplate;
	
	@Retryable(maxAttempts=RetryCode.SPRINGRETRY_MAXATTEMPTS_SENDMSG,
			   backoff = @Backoff(
					delay = RetryCode.SPRINGRETRY_DELAY_SENDMSG,
					maxDelay=RetryCode.SPRINGRETRY_MAXDELAY,
					multiplier = RetryCode.SPRINGRETRY_MULTIPLIER))	
	public boolean mqSendMessage(String vhost,String exchange, 
			String routingKey, ShortMessage shortMessage) throws Exception{		
		SimpleResourceHolder.bind(amqpTemplate.getConnectionFactory(), vhost);
		amqpTemplate.convertAndSend(exchange, routingKey, shortMessage);
		SimpleResourceHolder.unbind(amqpTemplate.getConnectionFactory());
		return true;
	}
}
