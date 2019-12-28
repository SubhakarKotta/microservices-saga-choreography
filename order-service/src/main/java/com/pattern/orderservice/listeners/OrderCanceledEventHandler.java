package com.pattern.orderservice.listeners;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.pattern.orderservice.converters.Converter;
import com.pattern.orderservice.events.OrderCanceledEvent;
import com.pattern.orderservice.service.OrderService;

@Log4j2
@Component
@AllArgsConstructor
public class OrderCanceledEventHandler {

    private final Converter converter;
    private final OrderService orderService;

    @RabbitListener(queues = { "${queue.order-canceled}" })
    public void onOrderCanceled(@Payload String payload) {
        log.debug("Handling a refund order event {}", payload);
        OrderCanceledEvent event = converter.toObject(payload, OrderCanceledEvent.class);
        orderService.cancelOrder(event.getOrder().getTransactionId(), event.getReason());
    }
}
