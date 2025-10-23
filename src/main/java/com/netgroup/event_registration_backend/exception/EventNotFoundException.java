package com.netgroup.event_registration_backend.exception;

public class EventNotFoundException extends RuntimeException {

  public EventNotFoundException(Long id) {
    super("Event not found: " + id);
  }
}
