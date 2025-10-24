package com.netgroup.event_registration_backend.dto.registration;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistrationEventRequest(
    @NotBlank String name,
    @NotBlank String lastname,
    @Size(min = 11, max = 11, message = "Personal code must be exactly 11 characters")
    @NotBlank String personalCode
) {

}