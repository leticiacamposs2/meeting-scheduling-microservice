package com.leticiacampos.meetingschedulingmicroservice.controller.resource;

import com.leticiacampos.meetingschedulingmicroservice.controller.dto.MeetupDTO;
import com.leticiacampos.meetingschedulingmicroservice.controller.dto.MeetupFilterDTO;
import com.leticiacampos.meetingschedulingmicroservice.controller.dto.RegistrationDTO;
import com.leticiacampos.meetingschedulingmicroservice.model.entity.Meetup;
import com.leticiacampos.meetingschedulingmicroservice.model.entity.Registration;
import com.leticiacampos.meetingschedulingmicroservice.service.MeetupService;
import com.leticiacampos.meetingschedulingmicroservice.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/meetups")
@RequiredArgsConstructor
public class MeetupController {

    private final MeetupService meetupService;
    private final RegistrationService registrationService;
    private final ModelMapper modelMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private MeetupDTO create(@RequestBody MeetupDTO meetupDTO) {
        Meetup entity = modelMapper.map(meetupDTO, Meetup.class);
        entity = meetupService.save(entity);

        return modelMapper.map(entity, MeetupDTO.class);
    }

    @GetMapping
    public Page<MeetupDTO> find(MeetupFilterDTO dto, Pageable pageRequest) {

        Page<Meetup> result = meetupService.find(dto, pageRequest);
        List<MeetupDTO> meetups = result
                .getContent()
                .stream()
                .map(entity -> {
                    Registration registration = entity.getRegistration();
                    RegistrationDTO registrationDTO = modelMapper
                            .map(registration, RegistrationDTO.class);

                    MeetupDTO meetupDTO = modelMapper
                            .map(entity, MeetupDTO.class);
                    meetupDTO.setRegistration(registrationDTO);
                    return meetupDTO;
                }).collect(Collectors.toList());
        return new PageImpl<MeetupDTO>(meetups, pageRequest, result.getTotalElements());
    }
}
