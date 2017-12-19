package com.demo.rabbitmq.serviceImpl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import com.demo.rabbitmq.pojo.Result;
import com.demo.rabbitmq.pojo.RetryCode;
import com.demo.rabbitmq.pojo.ShortMessage;
import com.demo.rabbitmq.util.Cont;

@Component
public class DealMethod {
	
	static Logger logger = LoggerFactory.getLogger(DealMethod.class);
	
	@Retryable(maxAttempts=RetryCode.SPRINGRETRY_MAXATTEMPTS,
			   backoff = @Backoff(
				delay = RetryCode.SPRINGRETRY_DELAY,
				maxDelay=RetryCode.SPRINGRETRY_MAXDELAY,
				multiplier = RetryCode.SPRINGRETRY_MULTIPLIER))	
	public Result method(ShortMessage shortMessage) throws Exception{
		Result rs = new Result();
		switch (shortMessage.getType()) {
			case 1:
				logger.info("开始处理消息,type=1,ie="+shortMessage.getIe());				
				rs.setResult(true);
				rs.setCode(Cont.CODE_SUCCESS);
				break;
			case 2:
				logger.info("开始处理消息,type=2,ie="+shortMessage.getIe());
				rs.setResult(true);
				rs.setCode(Cont.CODE_SUCCESS);
				break;
			default:
				logger.info("开始处理消息,type不匹配,ie="+shortMessage.getIe());
				rs.setResult(false);
				rs.setCode(Cont.CODE_FAILURE);
				break;
		}
		
		return rs;
	}
	
	

}
