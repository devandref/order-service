package io.github.devandref.order_service.core.repository;

import io.github.devandref.order_service.core.document.Event;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventRepository extends MongoRepository<Event, String> {
}
