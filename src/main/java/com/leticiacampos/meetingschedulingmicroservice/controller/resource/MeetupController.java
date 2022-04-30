package com.leticiacampos.meetingschedulingmicroservice.controller.resource;

import com.leticiacampos.meetingschedulingmicroservice.controller.dto.MeetupDTO;
import com.leticiacampos.meetingschedulingmicroservice.model.entity.Meetup;
import com.leticiacampos.meetingschedulingmicroservice.service.MeetupService;
import com.leticiacampos.meetingschedulingmicroservice.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public Page<MeetupDTO> find(MeetupDTO dto, Pageable pageRequest) {
        Meetup filter = modelMapper.map(dto, Meetup.class);
        Page<Meetup> result = meetupService.find(filter, pageRequest);

        List<MeetupDTO> list = result.getContent()
                .stream()
                .map(entity -> modelMapper.map(entity, MeetupDTO.class))
                .collect(Collectors.toList());

        return new PageImpl<MeetupDTO>(list, pageRequest, result.getTotalElements());
    }

}
