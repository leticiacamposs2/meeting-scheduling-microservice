package com.leticiacampos.meetingschedulingmicroservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistrationDTO {

    private long id;

    @NotEmpty
    private String name;

    @NotEmpty
    private LocalDate dateOfRegistration;

    @NotEmpty
    private String registration;
}
