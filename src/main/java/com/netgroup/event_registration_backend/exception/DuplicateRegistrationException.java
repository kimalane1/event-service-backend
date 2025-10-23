package com.netgroup.event_registration_backend.exception;

public class DuplicateRegistrationException extends RuntimeException {

  public DuplicateRegistrationException() {
    super("User is already registered for this event");
  }
}
