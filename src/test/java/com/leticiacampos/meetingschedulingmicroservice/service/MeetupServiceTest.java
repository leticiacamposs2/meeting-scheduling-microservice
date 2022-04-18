package com.leticiacampos.meetingschedulingmicroservice.service;

import com.leticiacampos.meetingschedulingmicroservice.model.entity.Meetup;
import com.leticiacampos.meetingschedulingmicroservice.model.entity.Registration;
import com.leticiacampos.meetingschedulingmicroservice.repository.MeetupRepository;
import com.leticiacampos.meetingschedulingmicroservice.service.impl.MeetupServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class MeetupServiceTest {

    MeetupService meetupService;

    @MockBean
    MeetupRepository repository;

    @BeforeEach
    public void setUp() {
        this.meetupService = new MeetupServiceImpl(repository);
    }

    @Test
    @DisplayName("Should save an meetup")
    public void saveMeetup() {

        Registration registration = Registration.builder()
                .id(11)
                .build();

        // cenário
        Meetup meetup = createValidMeetup(registration);

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        PageRequest pageable = (PageRequest) pageableCaptor.getValue();

        // execução
        //Mockito.when(repository.findByRegistrationOnMeetup("101", "Data Storytelling – O poder de contar histórias com dados", pageable)).thenReturn(false);
        //Mockito.when(repository.findByRegistration(registration, pageable)).thenReturn(meetup);

        Meetup savedMeetup = meetupService.save(meetup);

        // assert
        assertThat(savedMeetup.getId()).isEqualTo(11);
        assertThat(savedMeetup.getEvent()).isEqualTo("Data Storytelling – O poder de contar histórias com dados");
        assertThat(savedMeetup.getRegistration()).isEqualTo(registration);
        assertThat(savedMeetup.getMeetupDate()).isEqualTo("17/04/2022");
        
    }

    private Meetup createValidMeetup(Registration registration) {
        return Meetup.builder()
                .id(101)
                .event("Data Storytelling – O poder de contar histórias com dados")
                .registration(registration)
                .meetupDate("17/04/2022")
                .build();
    }

}
