package com.netgroup.event_registration_backend.dto.registration;

import jakarta.validation.constraints.NotBlank;

public record RegistrationEventRequest(
    @NotBlank String name,
    @NotBlank String lastname,
    @NotBlank String personalCode
) {

}