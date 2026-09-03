package com.domainsugester.domain_finder.mail.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;

@Configuration
public class RabbitMQConfig {
    public static final String EXCHANGE = "email.exchange";
    public static final String QUEUE = "email.queue";

    public static final String BINDING_KEY = "batch.finished";

    public static final String DLX = "email.dlx";
    public static final String DLQ = "email.queue.dlq";
    public static final String DLQ_ROUTING_KEY = "email.dlq";

    @Bean
    public Queue emailQueue(){
        return QueueBuilder.durable(QUEUE)
                .withArgument("x-dead-letter-exchange", DLX)
                .withArgument("x-dead-letter-routing-key", DLQ_ROUTING_KEY)
                .build();
    }

    @Bean
    public JacksonJsonMessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
    @Bean
    public TopicExchange userExchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }
    @Bean
    public Queue emailDeadLetterQueue() {
        return QueueBuilder.durable(DLQ).build();
    }

    @Bean
    public DirectExchange emailDeadLetterExchange() {
        return new DirectExchange(DLX, true, false);
    }
    @Bean
    public Binding emailBinding(Queue emailQueue, TopicExchange userExchange) {
        return BindingBuilder.bind(emailQueue).to(userExchange).with(BINDING_KEY);
    }

    @Bean
    public Binding emailDeadLetterBinding(Queue emailDeadLetterQueue, DirectExchange emailDeadLetterExchange) {
        return BindingBuilder.bind(emailDeadLetterQueue).to(emailDeadLetterExchange).with(DLQ_ROUTING_KEY);
    }
}