package com.leticiacampos.meetingschedulingmicroservice.service;

import com.leticiacampos.meetingschedulingmicroservice.exception.BusinessException;
import com.leticiacampos.meetingschedulingmicroservice.model.entity.Meetup;
import com.leticiacampos.meetingschedulingmicroservice.model.entity.Registration;
import com.leticiacampos.meetingschedulingmicroservice.repository.MeetupRepository;
import com.leticiacampos.meetingschedulingmicroservice.service.impl.MeetupServiceImpl;
import org.assertj.core.api.Assertions;
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
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

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

    @Test
    @DisplayName("Should throw business error when thy to save a new meetup with a meetup duplicated")
    public void shouldNotSaveAsMeetupDuplicated() {

        Meetup meetup = createValidMeetup();
        Mockito.when(repository.existsById(Mockito.any())).thenReturn(true);

        Throwable exception = Assertions.catchThrowable(() -> meetupService.save(meetup));

        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Meetup already created");

        Mockito.verify(repository, Mockito.never()).save(meetup);
    }

    @Test
    @DisplayName("Should get an Meetup by Id")
    public void getByMeetupIdTest() {

        // cenario
        Integer id = 101;
        Meetup meetup = createValidMeetup();
        meetup.setId(101);
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(meetup));

        // execucao
        Optional<Meetup> findMeetup = meetupService.getById(id);

        // verificacao
        assertThat(findMeetup.isPresent()).isTrue();
        assertThat(findMeetup.get().getId()).isEqualTo(id);
        assertThat(findMeetup.get().getEvent()).isEqualTo(meetup.getEvent());
        assertThat(findMeetup.get().getMeetupDate()).isEqualTo(meetup.getMeetupDate());
        assertThat(findMeetup.get().getOwnerId()).isEqualTo(meetup.getOwnerId());
    }

    @Test
    @DisplayName("Should return empty when get an meetup by id when doesn't exists")
    public void meetupNotFoundByIdTest() {

        Integer id = 101;

        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

        Optional<Meetup> meetup = meetupService.getById(id);

        assertThat(meetup.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Should delete an meetup")
    public void deleteMeetupTest() {

        Integer id = 11;

        Meetup meetup = Meetup.builder().id(1).build();

        assertDoesNotThrow(() -> meetupService.delete(id));

        Mockito.verify(repository, Mockito.times(1)).delete(meetup);
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
