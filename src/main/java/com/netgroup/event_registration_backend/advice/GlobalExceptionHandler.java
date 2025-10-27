package com.netgroup.event_registration_backend.advice;

import com.netgroup.event_registration_backend.exception.DuplicateEventException;
import com.netgroup.event_registration_backend.exception.DuplicateRegistrationException;
import com.netgroup.event_registration_backend.exception.EventNotFoundException;
import com.netgroup.event_registration_backend.exception.MaxPeopleExceededException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {


  @ExceptionHandler(EventNotFoundException.class)
  public ResponseEntity<String> handleEventNotFound(EventNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
  }

  @ExceptionHandler(DuplicateRegistrationException.class)
  public ResponseEntity<String> handleDuplicateRegistration(DuplicateRegistrationException ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
  }

  @ExceptionHandler(DuplicateEventException.class)
  public ResponseEntity<String> handleDuplicateEvent(DuplicateEventException ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
  }

  @ExceptionHandler(MaxPeopleExceededException.class)
  public ResponseEntity<String> handleMaxPeopleExceeded(MaxPeopleExceededException ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleValidationErrors(
      MethodArgumentNotValidException ex) {

    List<Map<String, String>> errors = ex.getFieldErrors()
        .stream()
        .map(error -> Map.of(
            "field", error.getField(),
            "message", Objects.requireNonNull(error.getDefaultMessage())
        ))
        .toList();

    return ResponseEntity.badRequest().body(Map.of("errors", errors));
  }

}
