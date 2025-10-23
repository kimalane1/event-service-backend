package com.netgroup.event_registration_backend.controller;

import com.netgroup.event_registration_backend.dto.event.EventRequest;
import com.netgroup.event_registration_backend.dto.event.EventResponse;
import com.netgroup.event_registration_backend.service.EventService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class EventController {

  private final EventService eventService;

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/events")
  public ResponseEntity<Void> create(@Valid @RequestBody EventRequest request
  ) {
    eventService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @GetMapping("/events")
  public ResponseEntity<List<EventResponse>> findAll() {
    List<EventResponse> response = eventService.findAll();
    return ResponseEntity.ok(response);
  }
}
