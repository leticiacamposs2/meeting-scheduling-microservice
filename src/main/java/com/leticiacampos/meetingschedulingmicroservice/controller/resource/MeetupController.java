package com.leticiacampos.meetingschedulingmicroservice.controller.resource;

import com.leticiacampos.meetingschedulingmicroservice.controller.dto.MeetupDTO;
import com.leticiacampos.meetingschedulingmicroservice.controller.dto.MeetupFilterDTO;
import com.leticiacampos.meetingschedulingmicroservice.controller.dto.RegistrationDTO;
import com.leticiacampos.meetingschedulingmicroservice.exception.BusinessException;
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

    @GetMapping
    public Page<MeetupDTO> find(MeetupFilterDTO dto, Pageable pageRequest) {
        Page<Meetup> result = meetupService.find(dto, pageRequest);

        List<MeetupDTO> meetups = result
                .getContent()
                .stream()
                .map(entity -> {
                    List<Registration> registrations = entity.getRegistrations();

                    List<RegistrationDTO> registrationDTOS = registrations.stream()
                            .map(registration -> modelMapper.map(registration, RegistrationDTO.class))
                            .collect(Collectors.toList());

                    MeetupDTO meetupDTO = modelMapper.map(entity, MeetupDTO.class);
                    meetupDTO.setRegistrations(registrationDTOS);

                    return meetupDTO;
                }).collect(Collectors.toList());

        return new PageImpl<MeetupDTO>(meetups, pageRequest, result.getTotalElements());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MeetupDTO get(@PathVariable Integer id) throws BusinessException {
        return meetupService
                .getById(id)
                .map(meetup -> modelMapper.map(meetup, MeetupDTO.class))
                .orElseThrow(() -> new BusinessException("The meetup with the given id could not be found."));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private Integer create(@RequestBody MeetupDTO meetupDTO) {

        Meetup entity = Meetup.builder()
                .event(meetupDTO.getEvent())
                .meetupDate(meetupDTO.getDate().toString())
                .ownerId(meetupDTO.getOwnerId())
                .build();

        entity = meetupService.save(entity);
        return entity.getId();
    }

    @PutMapping("{id}")
    public MeetupDTO update(@PathVariable Integer id, @RequestBody MeetupDTO meetupDTO) {
        return meetupService.getById(id)
                .map(meetup -> {
                    meetup.setEvent(meetupDTO.getEvent());
                    meetup = meetupService.update(meetup);
                    return modelMapper.map(meetup, MeetupDTO.class);
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        meetupService.delete(id);
    }

}

