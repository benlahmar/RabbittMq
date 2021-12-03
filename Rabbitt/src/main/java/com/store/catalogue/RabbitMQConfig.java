package com.store.catalogue;


import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

	@Value("${cap.rabbitmq.queue}")
	String queueName;

	@Value("${cap.rabbitmq.exchange}")
	String exchange;

	@Value("${cap.rabbitmq.routingkey}")
	private String routingkey;

	@Bean
	Queue queue() {
		return new Queue(queueName, false);
	}

	@Bean
	DirectExchange exchange() {
		return new DirectExchange(exchange);
	}

	@Bean
	TopicExchange topicexchage()
	{
		return new TopicExchange("cap.topic1");
	}
	
	@Bean
	Binding binding(Queue queue, DirectExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(routingkey);
	}

	@Bean
	Binding binding2(Queue queue, DirectExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with("key123");
	}
	@Bean
	public MessageConverter jsonMessageConverter() {
		
		return new Jackson2JsonMessageConverter();
	}

	
	@Bean
	public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(jsonMessageConverter());
		
		return rabbitTemplate;
	}
}