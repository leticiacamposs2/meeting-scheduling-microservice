package com.leticiacampos.meetingschedulingmicroservice.service;

import com.leticiacampos.meetingschedulingmicroservice.exception.BusinessException;
import com.leticiacampos.meetingschedulingmicroservice.model.entity.Registration;
import com.leticiacampos.meetingschedulingmicroservice.repository.RegistrationRepository;
import com.leticiacampos.meetingschedulingmicroservice.service.impl.RegistrationServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class RegistrationServiceTest {

    RegistrationService registrationService;

    @MockBean
    RegistrationRepository repository;

    @BeforeEach
    public void setUp() {
        this.registrationService = new RegistrationServiceImpl(repository);
    }
    
    @Test
    @DisplayName("Should save an registration")
    public void saveStudent() {

        // cenário
        Registration registration = createValidRegistration();

        // execução
        Mockito.when(repository.existsByRegistration(Mockito.anyString())).thenReturn(false);
        Mockito.when(repository.save(registration)).thenReturn(createValidRegistration());

        Registration savedRegistration = registrationService.save(registration);

        // assert
        assertThat(savedRegistration.getId()).isEqualTo(101);
        assertThat(savedRegistration.getName()).isEqualTo("Leticia Campos");
        assertThat(savedRegistration.getDateOfRegistration()).isEqualTo(LocalDate.now());
        assertThat(savedRegistration.getRegistration()).isEqualTo("001");
    }

    private Registration createValidRegistration() {
        return Registration.builder()
                .id(101)
                .name("Leticia Campos")
                .dateOfRegistration(LocalDate.now())
                .registration("001")
                .build();
    }

    @Test
    @DisplayName("Should throw business error when try to save a new registration with a registration duplicated")
        public void shouldNotSaveAsRegistrationDuplicated() {

            Registration registration = createValidRegistration();

            Mockito.when(repository.existsByRegistration(Mockito.any())).thenReturn(true);

            Throwable exception = Assertions.catchThrowable(() -> registrationService.save(registration));

            assertThat(exception)
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("Registration already created");

            Mockito.verify(repository, Mockito.never()).save(registration);
        }
}
