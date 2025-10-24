package com.netgroup.event_registration_backend.unit.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.netgroup.event_registration_backend.controller.RegistrationEventController;
import com.netgroup.event_registration_backend.service.EventRegistrationService;
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

  @Test
  void register_whenAllFieldsFilled_shouldReturnCreated() throws Exception {
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

  @Test
  void register_whenNameIsBlank_shouldReturnBadRequest() throws Exception {
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
        .andExpect(status().isBadRequest());
  }
  @Test
  void register_whenPersonalCodeIsLong_shouldReturnBadRequest() throws Exception {
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
        .andExpect(status().isBadRequest());
  }

  @Test
  void register_whenPersonalCodeIsShort_shouldReturnBadRequest() throws Exception {
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
        .andExpect(status().isBadRequest());
  }
}
