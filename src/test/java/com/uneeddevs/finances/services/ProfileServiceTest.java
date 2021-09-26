package com.uneeddevs.finances.services;

import com.uneeddevs.finances.mocks.ProfileMock;
import com.uneeddevs.finances.model.Profile;
import com.uneeddevs.finances.repository.ProfileRepository;
import com.uneeddevs.finances.service.ProfileService;
import com.uneeddevs.finances.service.impl.ProfileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.NoResultException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ProfileServiceTest {

    private ProfileService profileService;
    private ProfileRepository profileRepository;

    @BeforeEach
    void setup(){
        profileRepository = mock(ProfileRepository.class);
        profileService = new ProfileServiceImpl(profileRepository);
    }

    @Test
    void  findByIdExpectedSuccess() throws Exception{
        Profile profile = ProfileMock.mock();
        long id = 1L;
        when(profileRepository.findById(id)).thenReturn(Optional.of(profile));

        Profile profileReturn = profileService.findById(id);
        assertNotNull(profileReturn, "Return cannot be null");

        verify(profileRepository).findById(id);
    }

    @Test
    void  findByIdExpectedNoResultException() {
        long id = 1L;
        when(profileRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(NoResultException.class, () -> profileService.findById(id) ,
                "Expected no result Exception");

        verify(profileRepository).findById(id);
    }
}
