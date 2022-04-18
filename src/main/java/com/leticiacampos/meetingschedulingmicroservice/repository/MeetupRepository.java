package com.leticiacampos.meetingschedulingmicroservice.repository;

import com.leticiacampos.meetingschedulingmicroservice.model.entity.Meetup;
import com.leticiacampos.meetingschedulingmicroservice.model.entity.Registration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MeetupRepository extends JpaRepository<Meetup, Integer> {

    @Query( value = " select l from Meetup as l join l.registration as b where b.registration = :registration or l.event =:event ")
    Page<Meetup> findByRegistrationOnMeetup(
            @Param("registration") String registration,
            @Param("event") String event,
            Pageable pageable
    );

    //    @Query( value = " select case when ( (cont(l.id) >0)) then true else false end from Meetup l where l.event = :event and (l. registered is null l. registered is false) ")
    //    Page<Meetup> findByRegistrationOnMeetup(
    //            @Param("registration") String registration,
    //            @Param("event") String event,
    //            Pageable pageable
    //    );

    Page<Meetup> findByRegistration(Registration registration, Pageable pageable );
}