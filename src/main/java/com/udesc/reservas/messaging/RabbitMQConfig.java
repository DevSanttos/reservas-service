package com.udesc.notificacao.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "reservas.exchange";
    public static final String ROUTING_KEY_CONFIRMADA = "reserva.confirmada";
    public static final String QUEUE_NOTIFICACOES = "notificacoes.queue";

    @Bean
    public DirectExchange reservasExchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Queue notificacoesQueue() {
        return new Queue(QUEUE_NOTIFICACOES, true);
    }

    @Bean
    public Binding bindingNotificacoes(Queue notificacoesQueue, DirectExchange reservasExchange) {
        return BindingBuilder.bind(notificacoesQueue)
                .to(reservasExchange)
                .with(ROUTING_KEY_CONFIRMADA);
    }
}