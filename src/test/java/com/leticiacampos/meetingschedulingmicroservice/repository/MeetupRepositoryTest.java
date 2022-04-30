package com.leticiacampos.meetingschedulingmicroservice.repository;

import com.leticiacampos.meetingschedulingmicroservice.model.entity.Meetup;
import com.leticiacampos.meetingschedulingmicroservice.model.entity.Registration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class MeetupRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    private MeetupRepository repository;

    private Meetup meetupMock;

    private Registration registrationMock;

    @BeforeEach
    public void setup() {

        meetupMock = Meetup.builder()
                .id(123)
                .event("Data Storytelling – O poder de contar histórias com dados")
                .registration(createNewRegistration())
                .meetupDate("17/04/2022")
                .build();

        registrationMock = Registration.builder()
                .id(11)
                .name("Leticia Campos")
                .dateOfRegistration("17/04/2022")
                .registration("323")
                .build();
    }

    @Test
    @DisplayName("Should return true when there is already a registration linked to the meetup.")
    public void returnTrueWhenMeetupExists() {

        boolean exists = true;

        Page<Meetup> testando = repository.findByRegistrationOnMeetup(
                String.valueOf(registrationMock),
                "Data Storytelling – O poder de contar histórias com dados",
                PageRequest.of(0, 1));

        assertThat(exists).isTrue();
    }

    private Registration createNewRegistration() {
        return Registration.builder()
                .name("Leticia Campos")
                .dateOfRegistration("17/04/2022")
                .registration("323")
                .build();
    }

}
