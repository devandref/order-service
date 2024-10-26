package io.github.devandref.order_service.core.service;


import io.github.devandref.order_service.core.document.Event;
import io.github.devandref.order_service.core.document.Order;
import io.github.devandref.order_service.core.dto.OrderRequest;
import io.github.devandref.order_service.core.producer.SagaProducer;
import io.github.devandref.order_service.core.repository.OrderRepository;
import io.github.devandref.order_service.core.utils.JsonUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class OrderService {

    private final JsonUtil jsonUtil;
    private SagaProducer sagaProducer;
    private final EventService eventService;
    private final OrderRepository orderRepository;
    private final static String TRANSACTIONAL_ID_PATTERN = "%s_%s";

    public Order createOrder(OrderRequest orderRequest) {
        var order = Order
                .builder()
                .products(orderRequest.getProducts())
                .createdAt(LocalDateTime.now())
                .transactionalId(
                        String.format(TRANSACTIONAL_ID_PATTERN, Instant.now().toEpochMilli(), UUID.randomUUID())
                )
                .build();

        orderRepository.save(order);
        sagaProducer.sendEvent(jsonUtil.toJson(createPayload(order)));
        return order;
    }

    private Event createPayload(Order order) {
        var event = Event
                .builder()
                .orderId(order.getId())
                .transactionalId(order.getTransactionalId())
                .payload(order)
                .createdAt(LocalDateTime.now())
                .build();

        eventService.save(event);
        return event;
    }

}
