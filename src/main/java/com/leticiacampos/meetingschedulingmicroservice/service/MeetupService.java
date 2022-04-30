package com.leticiacampos.meetingschedulingmicroservice.service;

import com.leticiacampos.meetingschedulingmicroservice.model.entity.Meetup;
import com.leticiacampos.meetingschedulingmicroservice.model.entity.Registration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface MeetupService {

    Meetup save(Meetup meetup);

    Optional<Meetup> getById(Integer id);

    Meetup update(Meetup loan);

    Page<Meetup> find(Meetup filter, Pageable pageRequest);

    Page<Meetup> getRegistrationsByMeetup(Registration registration, Pageable pageable);

    void delete(Integer id);
}
