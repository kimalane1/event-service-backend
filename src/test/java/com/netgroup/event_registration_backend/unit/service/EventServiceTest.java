package com.netgroup.event_registration_backend.unit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.netgroup.event_registration_backend.domain.Event;
import com.netgroup.event_registration_backend.dto.event.EventRequest;
import com.netgroup.event_registration_backend.dto.event.EventResponse;
import com.netgroup.event_registration_backend.exception.DuplicateEventException;
import com.netgroup.event_registration_backend.repository.EventRepository;
import com.netgroup.event_registration_backend.service.EventService;
import java.time.ZonedDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

  @Mock
  private EventRepository eventRepository;

  @InjectMocks
  private EventService eventService;

  @Captor
  private ArgumentCaptor<Event> eventCaptor;

  @Test
  void createEvent_whenEventIsNotPresent_shouldCreateEvent() {
    EventRequest request = new EventRequest("B-day", ZonedDateTime.now(), 10);
    when(eventRepository.existsByNameAndEventTime(request.name(), request.eventTime()))
        .thenReturn(false);
    eventService.create(request);
    verify(eventRepository).save(eventCaptor.capture());
    Event createdEvent = eventCaptor.getValue();
    assertEquals(request.name(), createdEvent.getName());
    assertEquals(request.eventTime(), createdEvent.getEventTime());
    assertEquals(request.maxPeople(), createdEvent.getMaxPeople());
  }

  @Test
  void createEvent_whenEventIsPresent_shouldThrowException() {
    EventRequest request = new EventRequest("B-day", ZonedDateTime.now(), 10);
    when(eventRepository.existsByNameAndEventTime(request.name(), request.eventTime()))
        .thenReturn(true);
    assertThrows(DuplicateEventException.class, () -> eventService.create(request));
    verify(eventRepository, never()).save(any());
  }

  @Test
  void findAll_whenAllEventsArePresent_shouldReturnEvents() {
    Event event = Event.builder()
        .id(1L)
        .name("B-day")
        .eventTime(ZonedDateTime.now())
        .maxPeople(10)
        .build();
    when(eventRepository.findAll())
        .thenReturn(List.of(event));
    var result = eventService.findAll();
    assertEquals(1, result.size());
    assertThat(result).extracting(EventResponse::name).containsExactly("B-day");
    assertThat(result).extracting(EventResponse::maxPeople).containsExactly(10);
    verify(eventRepository).findAll();
  }

  @Test
  void findAll_whenNoEventsArePresent_shouldReturnEmptyList() {
    when(eventRepository.findAll()).thenReturn(List.of());
    var result = eventService.findAll();
    assertEquals(0, result.size());
    verify(eventRepository).findAll();
  }
}
