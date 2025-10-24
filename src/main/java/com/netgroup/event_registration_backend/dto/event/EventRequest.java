package com.netgroup.event_registration_backend.dto.event;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.time.ZonedDateTime;

public record EventRequest(
    @NotBlank(message = "Name should not be blank") String name,
    @Future(message = "Event time should be in the future") ZonedDateTime eventTime,
    @Min(value = 1, message = "Amount of people should be positive") Integer maxPeople
) {

}