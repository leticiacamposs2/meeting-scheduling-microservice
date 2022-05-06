package com.leticiacampos.meetingschedulingmicroservice.repository;

import com.leticiacampos.meetingschedulingmicroservice.model.entity.Meetup;
import com.leticiacampos.meetingschedulingmicroservice.model.entity.Registration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Optional;

public interface MeetupRepository extends JpaRepository<Meetup, Integer> {

    @Transactional
    Integer deleteMeetupById(Integer id);

    @Query( value = " select l from Meetup as l " +
            "join l.registrations as b " +
            "where b.personId = :personId " +
            "or l.event = :event ")
    Page<Meetup>findByPersonIdOnMeetup(
            @Param("personId") String personId,
            @Param("event") String event,
            Pageable pageable
    );

    @Query(value = " select l from Meetup as l " +
            "where l.event = :event and l.meetupDate = :meetupDate ")
    Optional<Meetup> findByMeetupExistent(
            @Param("event") String event,
            @Param("meetupDate") String meetupDate
    );

    Page<Meetup> findByRegistrations(Registration registration, Pageable pageable );
}