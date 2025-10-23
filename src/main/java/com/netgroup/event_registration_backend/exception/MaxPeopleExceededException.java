package com.netgroup.event_registration_backend.exception;

public class MaxPeopleExceededException extends RuntimeException {

  public MaxPeopleExceededException() {
    super("Maximum amount of participants exceeded");
  }
}
