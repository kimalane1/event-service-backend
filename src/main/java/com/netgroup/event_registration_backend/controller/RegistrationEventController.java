package com.netgroup.event_registration_backend.controller;

import com.netgroup.event_registration_backend.dto.registration.RegistrationEventRequest;
import com.netgroup.event_registration_backend.service.EventRegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class RegistrationEventController {

  private final EventRegistrationService registrationService;


  @PostMapping("/events/{eventId}/register")
  public ResponseEntity<Void> register(
      @PathVariable Long eventId,
      @Valid @RequestBody RegistrationEventRequest request) {

    registrationService.register(eventId, request);

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .build();
  }

}
