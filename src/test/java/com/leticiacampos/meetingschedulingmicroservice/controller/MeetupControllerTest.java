package com.leticiacampos.meetingschedulingmicroservice.controller;

import com.leticiacampos.meetingschedulingmicroservice.controller.dto.MeetupDTO;
import com.leticiacampos.meetingschedulingmicroservice.controller.resource.MeetupController;
import com.leticiacampos.meetingschedulingmicroservice.exceptions.BusinessException;
import com.leticiacampos.meetingschedulingmicroservice.model.entity.Meetup;
import com.leticiacampos.meetingschedulingmicroservice.model.entity.Registration;
import com.leticiacampos.meetingschedulingmicroservice.service.MeetupService;
import com.leticiacampos.meetingschedulingmicroservice.service.RegistrationService;
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

import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = {MeetupController.class})
@AutoConfigureMockMvc
public class MeetupControllerTest {

    static String MEETUP_API = "/api/meetups";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MeetupService meetupService;

    @MockBean
    RegistrationService registrationService;

    @Test
    @DisplayName("Should register on a meetup")
    public void createMeetupTest() throws Exception {

        // quando enviar uma requisição pra esse registration precisa ser encontrado um valor que tem esse usuário
        MeetupDTO dto = MeetupDTO.builder()
                .registrationAttribute("123")
                .event("Womakerscode Dados")
                .build();

        String json = new ObjectMapper().writeValueAsString(dto);

        Registration registration = Registration.builder()
                .id(11)
                .registration("123")
                .build();

        BDDMockito.given(registrationService.getRegistrationByRegistrationAttribute("123"))
                .willReturn(Optional.of(registration));

        Meetup meetup = Meetup.builder()
                .id(11)
                .event("Womakerscode Dados")
                .registration(registration)
                .meetupDate("17/04/2022")
                .build();

        BDDMockito.given(meetupService.save(Mockito.any(Meetup.class)))
                .willReturn(meetup);

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
    @DisplayName("Should return error when try to register a meetup nonexistent")
    public void invalidRegistrationCreateMeetupTest() throws Exception {

        MeetupDTO dto = MeetupDTO.builder()
                .registrationAttribute("123")
                .event("Womakerscode Dados")
                .build();

        String json = new ObjectMapper().writeValueAsString(dto);

        BDDMockito.given(registrationService.getRegistrationByRegistrationAttribute("123"))
                .willReturn(Optional.empty());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(MEETUP_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return error when try to register a registration already register on a meetup")
    public void meetupRegistrationErrorOnCreateMeetupTest() throws Exception {

        MeetupDTO dto = MeetupDTO.builder()
                .registrationAttribute("123")
                .event("Womakerscode Dados")
                .build();

        String json = new ObjectMapper().writeValueAsString(dto);

        Registration registration = Registration.builder()
                .id(11)
                .name("Leticia Campos")
                .registration("123")
                .build();

        // procura na base se já tem algum registration pra esse meetup
        BDDMockito.given(meetupService.save(Mockito.any(Meetup.class))).willThrow(new BusinessException("Meetup a already enrolled"));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(MEETUP_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }

}
