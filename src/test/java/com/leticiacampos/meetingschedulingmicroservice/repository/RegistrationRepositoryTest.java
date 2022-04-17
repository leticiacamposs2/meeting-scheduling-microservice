package com.leticiacampos.meetingschedulingmicroservice.repository;

import com.leticiacampos.meetingschedulingmicroservice.model.entity.Registration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class RegistrationRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    RegistrationRepository repository;

    @Test
    @DisplayName("Should return true when exists an registration already created.")
    public void returnTrueWhenRegistrationExists() {
        String registration = "123";
        Registration registration_attributte = createNewRegistration(registration);
        entityManager.persist(registration_attributte);

        boolean exists = repository.existsByRegistration(registration);

        assertThat(exists).isTrue();
    }

    private Registration createNewRegistration(String registration) {
        return Registration.builder()
                .name("Leticia Campos")
                .dateOfRegistration("17/04/2022")
                .registration(registration)
                .build();
    }

}
