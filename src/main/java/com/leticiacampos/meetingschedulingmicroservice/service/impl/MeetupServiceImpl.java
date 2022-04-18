package com.leticiacampos.meetingschedulingmicroservice.service.impl;

import com.leticiacampos.meetingschedulingmicroservice.controller.dto.MeetupFilterDTO;
import com.leticiacampos.meetingschedulingmicroservice.model.entity.Meetup;
import com.leticiacampos.meetingschedulingmicroservice.model.entity.Registration;
import com.leticiacampos.meetingschedulingmicroservice.repository.MeetupRepository;
import com.leticiacampos.meetingschedulingmicroservice.service.MeetupService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MeetupServiceImpl implements MeetupService {

    private MeetupRepository repository;

    public MeetupServiceImpl(MeetupRepository repository) {
        this.repository = repository;
    }

    @Override
    public Meetup save(Meetup meetup) {
        return repository.save(meetup);
    }

    @Override
    public Optional<Meetup> getById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Meetup update(Meetup loan) {
        return repository.save(loan);
    }

    @Override
    public Page<Meetup> find(MeetupFilterDTO filterDTO, Pageable pageable) {
        return repository.findByRegistrationOnMeetup( filterDTO.getRegistration(), filterDTO.getEvent(), pageable );
    }

    @Override
    public Page<Meetup> getRegistrationsByMeetup(Registration registration, Pageable pageable) {
        return repository.findByRegistration(registration, pageable);
    }

}