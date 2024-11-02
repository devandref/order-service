package io.github.devandref.order_service.core.service;

import io.github.devandref.order_service.config.exeception.ValidationException;
import io.github.devandref.order_service.core.document.Event;
import io.github.devandref.order_service.core.dto.EventFilter;
import io.github.devandref.order_service.core.repository.EventRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;


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

    public List<Event> findAll() {
        return eventRepository.findAllByOrderByCreatedAtDesc();
    }

    public Event findByFilters(EventFilter filter) {
        validateEmptyFilters(filter);
        if(!StringUtils.isEmpty(filter.getOrderId())) {
            return findByOrderId(filter.getOrderId());
        } else {
            return findByTransactionId(filter.getTransactionId());
        }
    }

    private Event findByOrderId(String orderId) {
        return eventRepository
                .findTop1ByOrderIdOrderByCreatedAtDesc(orderId)
                .orElseThrow(() -> new ValidationException("Event not found by orderID."));

    }

    private Event findByTransactionId(String transactionId) {
        return eventRepository
                .findTop1BytransactionalIdOrderByCreatedAtDesc(transactionId)
                .orElseThrow(() -> new ValidationException("Event not found by transactionID."));

    }

    private void validateEmptyFilters(EventFilter filter) {
        if (StringUtils.isEmpty(filter.getOrderId()) && StringUtils.isEmpty(filter.getTransactionId())) {
            throw new ValidationException("OrderID or TransactionID must be informed.");
        }
    }

    public Event save(Event event) {
        return eventRepository.save(event);
    }
}
