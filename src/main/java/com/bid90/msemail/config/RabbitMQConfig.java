package com.bid90.msemail.config;

import com.bid90.msemail.UtilClass;
import com.bid90.msemail.dto.EmailRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

    /**
     * Name of the RabbitMQ queue.
     */
    public static final String QUEUE_NAME = "email_queue";

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private int port;

    @Value("${spring.rabbitmq.username}")
    private String username;
    @Value("${rmq.password.file}")
    private String rmqPasswordFile;
    @Value("${spring.rabbitmq.password}")
    private String rmqPassword;

    @Value("${spring.rabbitmq.virtual-host}")
    private String virtualHost;


    /**
     * Configures the RabbitMQ connection factory.
     *
     * @return a configured {@link ConnectionFactory} instance.
     */
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory(host, port);
        factory.setUsername(username);
        factory.setVirtualHost(virtualHost);
        factory.setPassword(UtilClass.getPassword(rmqPassword,rmqPasswordFile));
        return factory;
    }

    /**
     * Defines a new queue with the specified name.
     *
     * @return a new {@link Queue} instance.
     */
    @Bean
    public Queue myQueue() {
        return new Queue(QUEUE_NAME, false);
    }

    /**
     * Defines a message converter for converting messages to JSON format.
     *
     * @return a new {@link Jackson2JsonMessageConverter} instance.
     */
    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter jsonConverter = new Jackson2JsonMessageConverter();
        jsonConverter.setClassMapper(classMapper());
        return jsonConverter;
    }

    @Bean
    public DefaultClassMapper classMapper() {
        DefaultClassMapper classMapper = new DefaultClassMapper();
        Map<String, Class<?>> idClassMapping = new HashMap<>();
        idClassMapping.put("com.bid90.login.models.dtos.EmailRequest", EmailRequest.class);
        classMapper.setIdClassMapping(idClassMapping);
        return classMapper;
    }

    /**
     * Configures a message listener container with a connection factory and a listener adapter.
     *
     * @param connectionFactory the connection factory for creating connections to RabbitMQ.
     * @param listenerAdapter the listener adapter for handling messages.
     * @return a configured {@link SimpleMessageListenerContainer} instance.
     */
    @Bean
    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                                    MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(QUEUE_NAME);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    /**
     * Configures a message listener adapter with a receiver and a message converter.
     *
     * @param receiver the receiver for processing messages.
     * @param converter the message converter for converting messages.
     * @return a configured {@link MessageListenerAdapter} instance.
     */
    @Bean
    public MessageListenerAdapter listenerAdapter(EmailReceiver receiver, Jackson2JsonMessageConverter converter) {
        MessageListenerAdapter adapter = new MessageListenerAdapter(receiver, "receiveMessage");
        adapter.setMessageConverter(converter);
        return adapter;
    }

    /**
     * Configures a RabbitTemplate with a connection factory and a message converter.
     *
     * @param connectionFactory the connection factory for creating connections to RabbitMQ.
     * @param messageConverter the message converter for converting messages.
     * @return a configured {@link RabbitTemplate} instance.
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

}