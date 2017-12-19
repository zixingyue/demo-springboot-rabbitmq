package com.demo.rabbitmq.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory.CacheMode;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.SimpleRoutingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.demo.rabbitmq.serviceImpl.ConsumerImpl;
import com.demo.rabbitmq.util.FastJsonMessageConverter;





@Configuration
public class RabbitConfig {
	@Value("${mq.concurrentConsumers}")
	private int consumerNum;
	@Value("${mq.prefetchCount}")
	private int prefetchCount;
	
	/*@Value("${mq.retry.initialInterval}") 
	private long initialInterval;
	@Value("${mq.retry.maxInterval}") 
	private long maxInterval; 
	@Value("${mq.retry.multiplier}") 
	private double multiplier; 
	@Value("${mq.retry.maxAttempts}") 
	private int maxAttempts;*/
	
	/**********connectionFactory************************/
	 @Bean(name="connectionFactoryCrm")
	 @Primary
	 public ConnectionFactory connectionFactory(
	                                            @Value("${mq.host}") String host, 
	                                            @Value("${mq.port}") int port,
	                                            @Value("${mq.username}") String username,
	                                            @Value("${mq.password}") String password,
	                                            @Value("${mq.vhost.crm}") String vhost,
	                                            @Value("${mq.channel.cache.size}") int sessionCacheSize){
	        return getConnectionFactory(host, port, username, password, vhost, sessionCacheSize, CacheMode.CHANNEL);
	 }
	 @Bean(name="connectionFactoryCommon")
	 public ConnectionFactory connectionFactoryCommon(
	                                            @Value("${mq.host}") String host, 
	                                            @Value("${mq.port}") int port,
	                                            @Value("${mq.username}") String username,
	                                            @Value("${mq.password}") String password,
	                                            @Value("${mq.vhost.common}") String vhost,
	                                            @Value("${mq.channel.cache.size}") int sessionCacheSize
	                                            ){
	        return getConnectionFactory(host, port, username, password, vhost, sessionCacheSize, CacheMode.CHANNEL);
	 }
	 public ConnectionFactory getConnectionFactory(
            String host,  int port,String username,String password,
            String vhost, int sessionCacheSize,CacheMode cacheMode){
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
		connectionFactory.setHost(host);
		connectionFactory.setPort(port);
		connectionFactory.setUsername(username);
		connectionFactory.setPassword(password);
		connectionFactory.setVirtualHost(vhost);
		connectionFactory.setChannelCacheSize(sessionCacheSize);
		connectionFactory.setCacheMode(cacheMode);
		return connectionFactory;
	 }
	 /************routingConnectionFactory***********************/
	 @Bean (name="routeConnectionFactory")
	 ConnectionFactory routingConnectionFactory(
	 		@Qualifier("connectionFactoryCrm")  ConnectionFactory crmConnectionFactory,
			@Qualifier("connectionFactoryCommon")  ConnectionFactory commonConnectionFactory
	 ){
		 SimpleRoutingConnectionFactory rf = new SimpleRoutingConnectionFactory();

		 Map<Object,ConnectionFactory> connectionFactoryMap = new HashMap<Object,ConnectionFactory>();
		 connectionFactoryMap.put(crmConnectionFactory.getVirtualHost(),crmConnectionFactory);
		 connectionFactoryMap.put(commonConnectionFactory.getVirtualHost(),commonConnectionFactory);
         rf.setTargetConnectionFactories(connectionFactoryMap);
		 return rf;
	 }
	 /************rabbitTemplate***********************/
	 @Bean(name="rabbitTemplate")
	 public RabbitTemplate rabbitTemplate(
			 @Qualifier("routeConnectionFactory") ConnectionFactory routingConnectionFactory){
		 return getRabbitTemplate(routingConnectionFactory);
	}
	public RabbitTemplate getRabbitTemplate(ConnectionFactory connectionFactory){
		//json转换器，消息可以自动根据转换器转换格式，不配置时默认为java序列化，可以自行配置
		FastJsonMessageConverter messageConverter = new FastJsonMessageConverter();
		
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(messageConverter);
		rabbitTemplate.setChannelTransacted(true);
//		rabbitTemplate.setRetryTemplate(getRetryTemplate());
		return rabbitTemplate;
	}
	
	/*public RetryTemplate getRetryTemplate(){
		
		//重试策略
		RetryTemplate retryTemplate = new RetryTemplate();
		
		//指数退避策略
		ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
		backOffPolicy.setInitialInterval(initialInterval );//初始休眠时间，默认100毫秒
		backOffPolicy.setMaxInterval(maxInterval);//最大休眠时间，默认30秒
		backOffPolicy.setMultiplier(multiplier);//乘数，即下一次休眠时间为当前休眠时间*multiplier
		backOffPolicy.setSleeper(new ThreadWaitSleeper());
		retryTemplate.setBackOffPolicy(backOffPolicy);
		
		//固定次数重试策略，默认重试最大次数为3次，RetryTemplate默认使用的策略；
		SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy();
		simpleRetryPolicy.setMaxAttempts(maxAttempts);
		retryTemplate.setRetryPolicy(simpleRetryPolicy);
		return retryTemplate;
	}*/

	 /************factory***********************/
	@Bean(name="factoryCrm")
    public SimpleRabbitListenerContainerFactory factoryCrm(
           SimpleRabbitListenerContainerFactoryConfigurer configurer,
           @Qualifier("connectionFactoryCrm") ConnectionFactory connectionFactory) {
		return getFactory(configurer, connectionFactory);
    }

    @Bean(name="factoryCommon")
    public SimpleRabbitListenerContainerFactory factoryCommon(
        SimpleRabbitListenerContainerFactoryConfigurer configurer,
        @Qualifier("connectionFactoryCommon") ConnectionFactory connectionFactory) {
       return getFactory(configurer, connectionFactory);
    }

    public SimpleRabbitListenerContainerFactory getFactory(
    		SimpleRabbitListenerContainerFactoryConfigurer configurer,
    		ConnectionFactory connectionFactory){
    	 SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
         configurer.configure(factory, connectionFactory);
         return factory;
    }
    /************admin***********************/
    @Bean(name="adminCrm")
    public RabbitAdmin adminCrm(@Qualifier("connectionFactoryCrm") ConnectionFactory connectionFactory){
    	RabbitAdmin admin = new RabbitAdmin(connectionFactory);
    	return admin;
    }
    @Bean(name="adminCommon")
    public RabbitAdmin adminCommon(@Qualifier("connectionFactoryCommon") ConnectionFactory connectionFactory){
    	RabbitAdmin admin = new RabbitAdmin(connectionFactory);
    	return admin;
    }
	 /************exchange***********************/
    @Bean(name="exchange1")
    public TopicExchange exchange1(@Value("${mq.demo.topic.exchange1}") String exchangeName,
    		@Qualifier("adminCrm") RabbitAdmin admin){
    	//auto_delete: 当所有绑定队列都不再使用时，是否自动删除该交换机。
    	TopicExchange topicExchange = new TopicExchange(exchangeName,true,false);
    	topicExchange.setAdminsThatShouldDeclare(admin);
    	topicExchange.setShouldDeclare(true);
    	return topicExchange;
    }
    @Bean(name="exchange2")
    public TopicExchange exchange2(@Value("${mq.demo.topic.exchange2}") String exchangeName,
    		@Qualifier("adminCommon") RabbitAdmin admin){
    	//auto_delete: 当所有绑定队列都不再使用时，是否自动删除该交换机。
    	TopicExchange topicExchange = new TopicExchange(exchangeName,true,false);
    	topicExchange.setAdminsThatShouldDeclare(admin);
    	topicExchange.setShouldDeclare(true);
    	return topicExchange;
    }
    
	 /************queue***********************/
    @Bean(name="queue1")
    public Queue queue1(@Value("${mq.demo.queue1}") String queueName,
    		@Qualifier("adminCrm") RabbitAdmin admin){
    	//exclusive: 仅创建者可以使用的私有队列，断开后自动删除。
    	//auto_delete: 当所有消费客户端连接断开后，是否自动删除队列。
        Queue queue = new Queue(queueName,true,false,false);
        queue.setAdminsThatShouldDeclare(admin);
        queue.setShouldDeclare(true);
        return queue;
    }
    @Bean(name="queue2")
    public Queue queue2(@Value("${mq.demo.queue2}") String queueName,
    		@Qualifier("adminCommon") RabbitAdmin admin){
        Queue queue = new Queue(queueName,true,false,false);
        queue.setAdminsThatShouldDeclare(admin);
        queue.setShouldDeclare(true);
        return queue;
    }    
	 /************bindings***********************/
    @Bean
    Binding binding1(@Qualifier("queue1") Queue queue,
    		@Qualifier("exchange1") TopicExchange exchange,
    		@Qualifier("adminCrm") RabbitAdmin admin,
    		@Value("${mq.demo.queue1.routingKey}") String routingKey){
    	 Binding binding = BindingBuilder.bind(queue).to(exchange).with(routingKey);
	     binding.setAdminsThatShouldDeclare(admin);
	     binding.setShouldDeclare(true);
	     return binding;
    }
    @Bean
    Binding binding2(@Qualifier("queue2") Queue queue,
    		@Qualifier("exchange2") TopicExchange exchange,
    		@Qualifier("adminCommon") RabbitAdmin admin,
    		@Value("${mq.demo.queue2.routingKey}") String routingKey){
    	 Binding binding = BindingBuilder.bind(queue).to(exchange).with(routingKey);
	     binding.setAdminsThatShouldDeclare(admin);
	     binding.setShouldDeclare(true);
	     return binding;
    }
    /************listen container***********************/
    @Bean
    SimpleMessageListenerContainer container1(
    		@Qualifier("connectionFactoryCrm") ConnectionFactory connectionFactory,
    		@Value("${mq.demo.queue1}") String queueName,ConsumerImpl obj){
        return getContainer(connectionFactory, queueName,listenerAdapter(obj));
    }
    @Bean
    SimpleMessageListenerContainer container2(
    		@Qualifier("connectionFactoryCommon") ConnectionFactory connectionFactory,
    		@Value("${mq.demo.queue2}") String queueName,ConsumerImpl obj){
        return getContainer(connectionFactory, queueName,listenerAdapter(obj));
    }
    SimpleMessageListenerContainer getContainer(
    		ConnectionFactory connectionFactory,
    		String queueName,MessageListener obj){
        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
        simpleMessageListenerContainer.setConnectionFactory(connectionFactory);
        simpleMessageListenerContainer.setQueueNames(queueName);
        simpleMessageListenerContainer.setMessageListener(obj);
        simpleMessageListenerContainer.setPrefetchCount(prefetchCount);
        simpleMessageListenerContainer.setConcurrentConsumers(consumerNum);
        simpleMessageListenerContainer.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return simpleMessageListenerContainer;
    }
	 /************listenerAdapter***********************/
    @Bean
    MessageListenerAdapter listenerAdapter(ConsumerImpl obj){
    	 return new MessageListenerAdapter(obj,"onMessage");
    }
}
