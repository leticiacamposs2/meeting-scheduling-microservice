package com.leticiacampos.meetingschedulingmicroservice.repository;

import com.leticiacampos.meetingschedulingmicroservice.model.entity.Meetup;
import com.leticiacampos.meetingschedulingmicroservice.model.entity.Registration;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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

    private Registration createNewRegistration() {
        return Registration.builder()
                .name("Leticia Campos")
                .dateOfRegistration("17/04/2022")
                .personId("323")
                .build();
    }

}
