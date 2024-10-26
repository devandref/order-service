package io.github.devandref.order_service.core.repository;

import io.github.devandref.order_service.core.document.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order, String> {
}
