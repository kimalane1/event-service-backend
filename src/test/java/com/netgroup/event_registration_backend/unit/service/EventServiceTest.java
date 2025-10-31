package com.netgroup.event_registration_backend.unit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;
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
import org.junit.jupiter.api.DisplayName;
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

  @DisplayName("Should create event when event is not present")
  @Test
  void create_whenEventIsNotPresent_createsEvent() {
    EventRequest request = new EventRequest("B-day", ZonedDateTime.now(), 10);
    when(eventRepository.existsByNameAndEventTime(request.name(), request.eventTime()))
        .thenReturn(false);
    eventService.create(request);
    verify(eventRepository).save(eventCaptor.capture());
    Event createdEvent = eventCaptor.getValue();
    assertAll(
        () -> assertEquals(request.name(), createdEvent.getName()),
        () -> assertEquals(request.eventTime(), createdEvent.getEventTime()),
        () -> assertEquals(request.maxPeople(), createdEvent.getMaxPeople())
    );
  }

  @DisplayName("Should throw exception when event is present")
  @Test
  void create_whenEventIsPresent_throwsException() {
    EventRequest request = new EventRequest("B-day", ZonedDateTime.now(), 10);
    when(eventRepository.existsByNameAndEventTime(request.name(), request.eventTime()))
        .thenReturn(true);
    assertThrows(DuplicateEventException.class, () -> eventService.create(request));
    verify(eventRepository, never()).save(any());
  }

  @DisplayName("Should return all events when all events are present")
  @Test
  void findAll_whenAllEventsArePresent_returnsEvents() {
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
    assertThat(result)
        .extracting(
            EventResponse::name,
            EventResponse::maxPeople)
        .containsExactly(tuple(
            "B-day",
            10));
    verify(eventRepository).findAll();
  }

  @DisplayName("Should return empty list when no events are present")
  @Test
  void findAll_whenNoEventsArePresent_returnsEmptyList() {
    when(eventRepository.findAll()).thenReturn(List.of());
    var result = eventService.findAll();
    assertEquals(0, result.size());
    verify(eventRepository).findAll();
  }
}
