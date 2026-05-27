package com.bty.karaoke.mememusicboxservice.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Bean
    public MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(
            ConnectionFactory connectionFactory,
            MessageConverter messageConverter
    ) {

        RabbitTemplate rabbitTemplate =
                new RabbitTemplate(connectionFactory);

        rabbitTemplate.setMessageConverter(messageConverter);

        return rabbitTemplate;
    }

    @Bean(name = "bookingQueue")
    public Queue bookingQueue() {
        return new Queue("booking.queue");
    }

    @Bean(name = "registrationOTPReceivedEmailQueue")
    public Queue registrationOTPReceivedEmailQueue() {
        return new Queue("registrationOTPReceivedEmail.queue");
    }

    @Bean(name = "memeExchange")
    public DirectExchange memeExchange() {
        return new DirectExchange("meme.exchange");
    }

    @Bean
    public Binding bookingBinding(
            @Qualifier("bookingQueue") Queue bookingQueue,
            @Qualifier("memeExchange") DirectExchange memeExchange
    ) {
        return BindingBuilder.bind(bookingQueue).to(memeExchange).with("booking.queue");
    }

    @Bean
    public Binding registrationOTPReceivedEmailBinding(
            @Qualifier("registrationOTPReceivedEmailQueue") Queue registrationOTPReceivedEmailQueue,
            @Qualifier("memeExchange") DirectExchange memeExchange
    ) {
        return BindingBuilder.bind(registrationOTPReceivedEmailQueue).to(memeExchange).with("registrationOTPReceivedEmail.queue");
    }
}
