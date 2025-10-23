package com.netgroup.event_registration_backend.dto.event;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.time.ZonedDateTime;

public record EventRequest(
    @NotBlank String name,
    @Future ZonedDateTime eventTime,
    @Min(1) Integer maxPeople
) {

}