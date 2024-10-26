package io.github.devandref.order_service.core.service;

import io.github.devandref.order_service.core.document.Event;
import io.github.devandref.order_service.core.repository.EventRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@AllArgsConstructor
public class EventService {

    private final EventRepository eventRepository;


    public void notifyEnding(Event event) {
        event.setOrderId(event.getOrderId());
        event.setCreatedAt(LocalDateTime.now());
        save(event);
        log.info("Order {} with saga notified! TransactionId: {}", event.getOrderId(), event.getTransactionalId());
    }

    public Event save(Event event) {
        return eventRepository.save(event);
    }
}