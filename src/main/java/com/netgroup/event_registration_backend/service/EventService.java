package com.netgroup.event_registration_backend.service;

import com.netgroup.event_registration_backend.dto.event.EventRequest;
import com.netgroup.event_registration_backend.dto.event.EventResponse;
import com.netgroup.event_registration_backend.exception.DuplicateEventException;
import com.netgroup.event_registration_backend.mapper.EventMapper;
import com.netgroup.event_registration_backend.repository.EventRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EventService {

  private final EventRepository repository;

  public void create(EventRequest request) {
    if (repository.existsByNameAndEventTime(request.name(), request.eventTime())) {
      throw new DuplicateEventException();
    }
    var event = EventMapper.toEntity(request);
    repository.save(event);
  }

  public List<EventResponse> findAll() {
    return repository.findAll()
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
