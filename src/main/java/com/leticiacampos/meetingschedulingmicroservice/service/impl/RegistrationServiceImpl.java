package com.leticiacampos.meetingschedulingmicroservice.service.impl;

import com.leticiacampos.meetingschedulingmicroservice.exception.BusinessException;
import com.leticiacampos.meetingschedulingmicroservice.model.entity.Registration;
import com.leticiacampos.meetingschedulingmicroservice.repository.RegistrationRepository;
import com.leticiacampos.meetingschedulingmicroservice.service.RegistrationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    RegistrationRepository repository;

    public RegistrationServiceImpl(RegistrationRepository repository) {
        this.repository = repository;
    }

    public Registration save(Registration registration) {
        if (repository.existsByRegistration(registration.getRegistration())) {
            throw new BusinessException("Registration already created");
        }

        return repository.save(registration);
    }

    @Override
    public Optional<Registration> getRegistrationById(Integer id) {
        return Optional.empty();
    }

    @Override
    public void delete(Registration registration) {

    }

    @Override
    public Registration update(Registration registration) {
        return null;
    }

    @Override
    public Page<Registration> find(Registration filter, Pageable pageRequest) {
        return null;
    }

    @Override
    public Optional<Registration> getRegistrationByRegistrationAttribute(String registrationAttribute) {
        return Optional.empty();
    }
}
