package com.leticiacampos.meetingschedulingmicroservice.controller;

import com.leticiacampos.meetingschedulingmicroservice.model.RegistrationDTO;
import com.leticiacampos.meetingschedulingmicroservice.model.entity.Registration;
import com.leticiacampos.meetingschedulingmicroservice.service.RegistrationService;import org.hamcrest.Matchers;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;


import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = {RegistrationController.class})
@AutoConfigureMockMvc
public class RegistrationControllerTest {

    static String REGISTRATION_API = "/api/registration";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    RegistrationService registrationService;

    @Test
    @DisplayName("Should create a registration with success")
    public void createRegistrationTest() throws Exception {

        // cenário
        RegistrationDTO registrationDTOBuilder = createNewRegistration();
        Registration savedRegistration = Registration.builder()
                .id(101)
                .name("Leticia Campos")
                .dateOfRegistration("15/04/2022")
                .build();

        // execucao
        BDDMockito.given(registrationService.save(any(Registration.class))).willReturn(savedRegistration);

        String json  = new ObjectMapper().writeValueAsString(registrationDTOBuilder);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(REGISTRATION_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        // verificacao, assert....
        mockMvc
                .perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(101))
                .andExpect(jsonPath("name").value(registrationDTOBuilder.getName()))
                .andExpect(jsonPath("dateOfRegistration").value(registrationDTOBuilder.getDateOfRegistration()))
                .andExpect(jsonPath("registration").value(registrationDTOBuilder.getRegistration()));
    }

    @Test
    @DisplayName("Should throw an exception when not have date enough for the test.")
    public void createInvalidRegistrationTest() throws Exception {

        String json  = new ObjectMapper().writeValueAsString(new RegistrationDTO());

        MockHttpServletRequestBuilder request  = MockMvcRequestBuilders
                .post(REGISTRATION_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should get registration information")
    public void getRegistrationTest() throws Exception {

        Integer id = 11;

        Registration registration = Registration.builder()
                .id(id)
                .name(createNewRegistration().getName())
                .dateOfRegistration(createNewRegistration().getDateOfRegistration())
                .registration(createNewRegistration().getRegistration())
                .build();

        BDDMockito.given(registrationService.getRegistrationById(id)).willReturn(Optional.of(registration));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(REGISTRATION_API.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc
                .perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(101))
                .andExpect(jsonPath("name").value(createNewRegistration().getName()))
                .andExpect(jsonPath("dateOfRegistration").value(createNewRegistration().getDateOfRegistration()))
                .andExpect(jsonPath("registration").value(createNewRegistration().getRegistration()));
    }

    private RegistrationDTO createNewRegistration() {
        return RegistrationDTO.builder()
                .id(101)
                .name("Leticia Campos")
                .dateOfRegistration("15/04/2022")
                .build();
    }

}
