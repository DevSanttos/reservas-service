package com.udesc.reservas.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${reservas.rabbit.exchange}")
    private String exchangeName;

    @Value("${reservas.rabbit.routing-key-confirmada}")
    private String routingKeyConfirmada;

    public static final String QUEUE_NOTIFICACOES = "notificacoes.queue";

    @Bean
    public DirectExchange reservasExchange() {
        return new DirectExchange(exchangeName);
    }

    @Bean
    public Queue notificacoesQueue() {
        return new Queue(QUEUE_NOTIFICACOES, true);
    }

    @Bean
    public Binding bindingNotificacoes(Queue notificacoesQueue, DirectExchange reservasExchange) {
        return BindingBuilder.bind(notificacoesQueue).to(reservasExchange).with(routingKeyConfirmada);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
