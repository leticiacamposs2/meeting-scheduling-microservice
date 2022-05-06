package com.leticiacampos.meetingschedulingmicroservice.service;

import com.leticiacampos.meetingschedulingmicroservice.model.entity.Meetup;
import com.leticiacampos.meetingschedulingmicroservice.repository.MeetupRepository;
import com.leticiacampos.meetingschedulingmicroservice.service.impl.MeetupServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

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

        // cenario
        Meetup meetup = createValidMeetup();

        // proxima versão validar se o meetup existe
        Mockito.when(repository.findByMeetupExistent(Mockito.anyString(), Mockito.any())).thenReturn(Optional.empty());
        Mockito.when(repository.save(meetup)).thenReturn(createValidMeetup());

        Meetup savedMeetup = meetupService.save(meetup);

        // assert
        assertThat(savedMeetup.getId()).isEqualTo(101);
        assertThat(savedMeetup.getEvent()).isEqualTo("Experiência 24/7: a importância do UX na construção e manutenção do produto");
        assertThat(savedMeetup.getMeetupDate()).isEqualTo("05/05/2022");
        assertThat(savedMeetup.getOwnerId()).isEqualTo(3);
    }

    private Meetup createValidMeetup() {
        return Meetup.builder()
                .id(101)
                .event("Experiência 24/7: a importância do UX na construção e manutenção do produto")
                .meetupDate("05/05/2022")
                .ownerId(3)
                .build();
    }
}
