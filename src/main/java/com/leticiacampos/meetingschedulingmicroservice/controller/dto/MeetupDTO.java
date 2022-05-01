package com.leticiacampos.meetingschedulingmicroservice.controller.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeetupDTO {

    private String event;

    //data do evento
    private String date;

    //id do usuário que criou o meetup
    private Integer ownerId;

    private List<RegistrationDTO> registrations;
}
