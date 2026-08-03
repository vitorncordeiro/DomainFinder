package com.domainsugester.domain_finder.batch.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    private final String DOMAIN_QUEUE = "domain.queue";
    private final String DOMAIN_EXCHANGE = "domain.exchange";
    private final String DOMAIN_BINDING_KEY = "domain.requested";
    private final String DOMAIN_DLQ = "domain.queue.dlq";
    private final String DOMAIN_DLX = "domain.exchange.dlx";
    private final String DOMAIN_DLQ_BINDING_KEY = "domain.requested.dlq";

    @Bean
    public TopicExchange domainExchange(){
        return new TopicExchange(DOMAIN_EXCHANGE, true, false);
    }
    @Bean
    public Queue domainQueue(){
        return QueueBuilder.durable(DOMAIN_QUEUE)
                .withArgument("x-dead-letter-exchange", DOMAIN_DLX)
                .withArgument("x-dead-letter-routing-key", DOMAIN_DLQ_BINDING_KEY)
                .build();
    }
    @Bean
    public Binding domainBinding(Queue domainQueue, TopicExchange domainExchange){
        return BindingBuilder.bind(domainQueue).to(domainExchange).with(DOMAIN_BINDING_KEY);
    }

    @Bean
    public Queue domainDeadLetterQueue(){
        return QueueBuilder.durable(DOMAIN_DLQ).build();
    }
    @Bean
    public DirectExchange domainDeadLetterExchange(){
        return new DirectExchange(DOMAIN_DLX, true, false);
    }
    @Bean
    public Binding domainDeadLetterBinding(Queue domainDeadLetterQueue, DirectExchange domainDeadLetterExchange){
        return BindingBuilder.bind(domainDeadLetterQueue).to(domainDeadLetterExchange).with(DOMAIN_DLQ_BINDING_KEY);
    }
}
