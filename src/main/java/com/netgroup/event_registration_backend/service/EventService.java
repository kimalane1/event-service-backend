package com.netgroup.event_registration_backend.service;

import com.netgroup.event_registration_backend.domain.Event;
import com.netgroup.event_registration_backend.dto.event.EventRequest;
import com.netgroup.event_registration_backend.dto.event.EventResponse;
import com.netgroup.event_registration_backend.exception.DuplicateEventException;
import com.netgroup.event_registration_backend.repository.EventRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EventService {

  private final EventRepository eventRepository;

  public void create(EventRequest request) {
    if (eventRepository.existsByNameAndEventTime(request.name(), request.eventTime())) {
      throw new DuplicateEventException();
    }
    Event event = Event.builder()
        .name(request.name())
        .eventTime(request.eventTime())
        .maxPeople(request.maxPeople())
        .build();

    eventRepository.save(event);
  }

  public List<EventResponse> findAll() {
    return eventRepository.findAll()
        .stream()
        .map(e -> new EventResponse(
            e.getId(),
            e.getName(),
            e.getEventTime(),
            e.getMaxPeople()
        ))
        .toList();
  }
}
