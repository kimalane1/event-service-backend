package com.netgroup.event_registration_backend.unit.controller;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.netgroup.event_registration_backend.controller.RegistrationEventController;
import com.netgroup.event_registration_backend.service.EventRegistrationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(RegistrationEventController.class)
@AutoConfigureMockMvc(addFilters = false)
public class EventRegistrationControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockitoBean
  EventRegistrationService registrationService;

  @DisplayName("Should return HTTP 201 Created when registration is successful")
  @Test
  void register_whenAllFieldsFilled_returnsCreated() throws Exception {
    String json = """
        {
            "name": "John",
            "lastname": "Doe",
            "personalCode": "49303061111"
        }
        """;

    mockMvc.perform(post("/events/1/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isCreated());
  }

  @DisplayName("Should return HTTP 400 Bad Request when name is blank")
  @Test
  void register_whenNameIsBlank_returnsBadRequest() throws Exception {
    String json = """
        {
            "name": "",
            "lastname": "Doe",
            "personalCode": "49303061111"
        }
        """;

    mockMvc.perform(post("/events/1/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("Name should not be blank")));
  }

  @DisplayName("Should return HTTP 400 Bad Request when personal code is more than 11 characters")
  @Test
  void register_whenPersonalCodeMoreThanElevenChars_returnsBadRequest() throws Exception {
    String json = """
        {
            "name": "John",
            "lastname": "Doe",
            "personalCode": "493030611111239090"
        }
        """;

    mockMvc.perform(post("/events/1/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("Personal code must be exactly 11 characters")));
  }

  @DisplayName("Should return HTTP 400 Bad Request when personal code is less than 11 characters")
  @Test
  void register_whenPersonalCodeLessThanElevenChars_returnsBadRequest() throws Exception {
    String json = """
        {
            "name": "John",
            "lastname": "Doe",
            "personalCode": "493"
        }
        """;

    mockMvc.perform(post("/events/1/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("Personal code must be exactly 11 characters")));
  }
}
