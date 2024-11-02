package io.github.devandref.order_service.core.controller;

import io.github.devandref.order_service.core.document.Event;
import io.github.devandref.order_service.core.dto.EventFilter;
import io.github.devandref.order_service.core.service.EventService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/event")
public class EventController {

    private final EventService eventService;

    @GetMapping
    public Event findByFilters(EventFilter eventFilters) {
        return eventService.findByFilters(eventFilters);
    }

    @GetMapping("all")
    public List<Event> findAll() {
        return eventService.findAll();
    }

}
