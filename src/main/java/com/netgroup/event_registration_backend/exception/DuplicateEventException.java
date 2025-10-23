package com.netgroup.event_registration_backend.exception;

public class DuplicateEventException extends RuntimeException {

  public DuplicateEventException() {
    super("Event already created with this name and time");
  }
}
