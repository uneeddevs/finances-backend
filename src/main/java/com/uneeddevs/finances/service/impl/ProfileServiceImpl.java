package com.uneeddevs.finances.service.impl;

import com.uneeddevs.finances.model.Profile;
import com.uneeddevs.finances.repository.ProfileRepository;
import com.uneeddevs.finances.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;

    @Override
    public Profile findById(Long id) {
        log.info("Searching profile by id {}", id);
        return profileRepository.findById(id)
                .orElseThrow(() -> {
                    String message = String.format("No profile founded with id %s", id);
                    log.info(message);
                    return new NoResultException(message);
                });
    }
}
