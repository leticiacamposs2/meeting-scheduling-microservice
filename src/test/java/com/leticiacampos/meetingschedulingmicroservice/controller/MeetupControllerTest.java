package com.leticiacampos.meetingschedulingmicroservice.controller;

import com.leticiacampos.meetingschedulingmicroservice.controller.resource.MeetupController;
import com.leticiacampos.meetingschedulingmicroservice.exception.BusinessException;
import com.leticiacampos.meetingschedulingmicroservice.controller.dto.MeetupDTO;
import com.leticiacampos.meetingschedulingmicroservice.model.entity.Meetup;
import com.leticiacampos.meetingschedulingmicroservice.model.entity.Registration;
import com.leticiacampos.meetingschedulingmicroservice.service.MeetupService;
import com.leticiacampos.meetingschedulingmicroservice.service.RegistrationService;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = {MeetupController.class})
@AutoConfigureMockMvc
public class MeetupControllerTest {

    static final String MEETUP_API = "/api/meetups";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private RegistrationService registrationService;

    @MockBean
    private MeetupService meetupService;

    @Test
    @DisplayName("Should register on a meetup")
    public void createMeetupTest() throws Exception {

        MeetupDTO dto = MeetupDTO.builder()
                .event("Womakerscode Dados")
                .date("20/02/1991")
                .ownerId(1)
                .registrations(null)
                .build();

        String json = new ObjectMapper().writeValueAsString(dto);

        Registration registration = Registration.builder().id(11).personId("123").build();

        Meetup meetup = Meetup.builder()
                .id(11)
                .event("Womakerscode Dados")
                .registrations(List.of(registration))
                .meetupDate("10/10/2021")
                .ownerId(1)
                .build();

        BDDMockito.given(meetupService.save(Mockito.any(Meetup.class))).willReturn(meetup);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(MEETUP_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        // Aqui o que retorna é o id do registro no meetup
        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(content().string("11"));

    }

    @Test
    @DisplayName("Should return error when try to register a registration already register on a meetup")
    public void meetupRegistrationErrorOnCreateMeetupTest() throws Exception {

        MeetupDTO dto = MeetupDTO.builder()
                .event("Womakerscode Dados")
                .date("20/02/1991")
                .ownerId(1)
                .registrations(null)
                .build();

        String json = new ObjectMapper().writeValueAsString(dto);

        // procura na base se ja tem algum registration pra esse meetup
        BDDMockito.given(meetupService.save(Mockito.any(Meetup.class)))
                .willThrow(new BusinessException("Meetup already enrolled"));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(MEETUP_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should get an event by id with success")
    public void getMeetupByIdTest() throws Exception {

        Integer id = 1;

        Meetup meetup = createNewMeetup();

        BDDMockito.given(meetupService.getById(anyInt()))
                .willReturn(Optional.of(meetup));

        MockHttpServletRequestBuilder request  = MockMvcRequestBuilders
                .get(MEETUP_API.concat("/" + id))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("event").value(createNewMeetup().getEvent()))
                .andExpect(jsonPath("meetupDate").value(createNewMeetup().getMeetupDate()))
                .andExpect(jsonPath("ownerId").value(createNewMeetup().getOwnerId()))
                .andExpect(jsonPath("registrations").value(createNewMeetup().getRegistrations()));
    }

    private Meetup createNewMeetup() {
        return Meetup.builder()
                .event("Womakerscode Dados")
                .meetupDate("20/02/1991")
                .ownerId(1)
                .registrations(null)
                .build();
    }

}