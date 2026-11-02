package com.udesc.reservas.messaging;

import com.udesc.reservas.dto.ReservaConfirmadaEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ReservaEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final String exchangeName;
    private final String routingKeyConfirmada;

    public ReservaEventPublisher(RabbitTemplate rabbitTemplate,
                                  @Value("${reservas.rabbit.exchange}") String exchangeName,
                                  @Value("${reservas.rabbit.routing-key-confirmada}") String routingKeyConfirmada) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchangeName = exchangeName;
        this.routingKeyConfirmada = routingKeyConfirmada;
    }

    public void publicarReservaConfirmada(ReservaConfirmadaEvent evento) {
        rabbitTemplate.convertAndSend(exchangeName, routingKeyConfirmada, evento);
    }
}
