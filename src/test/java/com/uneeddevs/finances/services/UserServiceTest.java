package com.uneeddevs.finances.services;

import com.uneeddevs.finances.enums.ProfileRole;
import com.uneeddevs.finances.mocks.ProfileMock;
import com.uneeddevs.finances.mocks.UserMock;
import com.uneeddevs.finances.model.User;
import com.uneeddevs.finances.repository.UserRepository;
import com.uneeddevs.finances.security.exception.AuthenticationFailException;
import com.uneeddevs.finances.service.ProfileService;
import com.uneeddevs.finances.service.UserService;
import com.uneeddevs.finances.service.impl.UserServiceImpl;
import com.uneeddevs.finances.util.UserUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserService userService;
    private UserRepository userRepository;
    private ProfileService profileService;

    @BeforeEach
    void setup(){
        userRepository = mock(UserRepository.class);
        profileService = mock(ProfileService.class);
        userService = new UserServiceImpl(userRepository, profileService);
    }

    @Test
    void testFindByEmailExpectedSuccess() {
        try(MockedStatic<UserUtil> mockedUserUtil = mockStatic(UserUtil.class)) {
            mockedUserUtil.when(UserUtil::authenticatedUsername).thenReturn("user@mail.com");
            System.out.println(UserUtil.authenticatedUsername());
            User user = new User("name", "user@mail.com", "password");
            String email = "user@mail.com";
            when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

            var userEmailReturn = userService.findByEmail(email);
            assertNotNull(userEmailReturn, "User return cannot be null");

            verify(userRepository).findByEmail(email);
        }
    }

    @Test
    void testFindByEmailExpectedNoResultException() {
        try(MockedStatic<UserUtil> mockedUserUtil = mockStatic(UserUtil.class)) {
            mockedUserUtil.when(() -> UserUtil.hasAuthority(any(ProfileRole.class))).thenReturn(true);
            String email = "user@mail.com";
            when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

            NoResultException noResultException = assertThrows(NoResultException.class,
                    () -> userService.findByEmail(email),
                    "Expected NoResultException");
            assertEquals(noResultException.getMessage(), "No user with email " + email);
            verify(userRepository).findByEmail(email);
        }
    }

    @Test
    void testFindByUsernameExpectedSuccess() {
        try(MockedStatic<UserUtil> mockedUserUtil = mockStatic(UserUtil.class)) {
            mockedUserUtil.when(UserUtil::authenticatedUsername).thenReturn("user@mail.com");
            User user = new User("name", "user@mail.com", "password");
            String email = "user@mail.com";
            when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

            var userEmailReturn = userService.loadUserByUsername(email);
            assertNotNull(userEmailReturn, "User return cannot be null");

            verify(userRepository).findByEmail(email);
        }
    }

    @Test
    void testFindByUsernameExpectedUsernameNotFoundException() {
        try(MockedStatic<UserUtil> mockedUserUtil = mockStatic(UserUtil.class)) {
            String email = "user@mail.com";
            mockedUserUtil.when(UserUtil::authenticatedUsername).thenReturn(email);
            when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

            UsernameNotFoundException noResultException = assertThrows(UsernameNotFoundException.class,
                    () -> userService.loadUserByUsername(email),
                    "Expected NoResultException");
            assertEquals(noResultException.getMessage(), "No user with email " + email);
            verify(userRepository).findByEmail(email);
        }
    }

    @Test
    void testFindByIdExpectedSuccess() {
        try(MockedStatic<UserUtil> mockedUserUtil = mockStatic(UserUtil.class)) {
            mockedUserUtil.when(() -> UserUtil.hasAuthority(any(ProfileRole.class))).thenReturn(true);
            User user = new User("name", "email@mail.com", "password");
            UUID uuid = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");
            when(userRepository.findById(uuid)).thenReturn(Optional.of(user));

            var userEmailReturn = userService.findById(uuid);
            assertNotNull(userEmailReturn, "User return cannot be null");

            verify(userRepository).findById(uuid);
        }
    }

    @Test
    void testFindByIdExpectedNoResultException() {
        try(MockedStatic<UserUtil> mockedUserUtil = mockStatic(UserUtil.class)) {
            UUID uuid = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");
            mockedUserUtil.when(UserUtil::authenticatedUUID).thenReturn(uuid);
            when(userRepository.findById(uuid)).thenReturn(Optional.empty());

            NoResultException noResultException = assertThrows(NoResultException.class,
                    () -> userService.findById(uuid),
                    "Expected NoResultException");
            assertEquals(noResultException.getMessage(), "No user with UUID " + uuid);
            verify(userRepository).findById(uuid);
        }
    }

    @Test
    void testFindByIdExpectedAuthenticationFailException() {
            UUID uuid = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");
            when(userRepository.findById(uuid)).thenReturn(Optional.empty());

            assertThrows(AuthenticationFailException.class,
                    () -> userService.findById(uuid),
                    "Expected NoResultException");
            verify(userRepository, never()).findById(uuid);
    }

    @Test
    void insertUserExpectedSuccess() throws Exception {
        User user = new User("name", "email@mail.com", "password");
        when(userRepository.save(user)).thenReturn(user);
        when(profileService.findById(anyLong())).thenReturn(ProfileMock.mock());
        userService.insert(user);
        verify(userRepository).save(user);
        verify(profileService).findById(anyLong());
    }

    @Test
    void saveUserExpectedPersistentException() throws Exception{
        User user = new User("name", "email@mail.com", "password");
        when(userRepository.save(user)).thenThrow(RuntimeException.class);
        when(profileService.findById(anyLong())).thenReturn(ProfileMock.mock());
        PersistenceException persistenceException = assertThrows(PersistenceException.class,
                () -> userService.insert(user),
                "Expected PersistenceException");
        assertEquals(persistenceException.getMessage(), "Error on persisting user " + user.toString());
        verify(userRepository).save(user);
        verify(profileService).findById(anyLong());
    }

    @Test
    void testUserPageExpectedSuccess() {
        Pageable pageable = PageRequest.of(0, 20);
        User user = new User("name", "email@mail.com", "password");
        List<User> users = new ArrayList<>();
        users.add(user);
        Page<User> page = new PageImpl<>(users);
        when(userRepository.findAll(any(Pageable.class))).thenReturn(page);
        Page<User> userResponse = userService.findPage(pageable);
        assertFalse(userResponse.isEmpty(), "User response cannot be empty");
        verify(userRepository).findAll(pageable);
    }

    @Test
    void testUserPageExpectedEmptyReturn() {
        Pageable pageable = PageRequest.of(0, 20);
        List<User> users = new ArrayList<>();
        Page<User> page = new PageImpl<>(users);
        when(userRepository.findAll(pageable)).thenReturn(page);
        NoResultException noResultException = assertThrows(NoResultException.class,
                () -> userService.findPage(pageable),
                "Expected NoResultException");
        assertEquals(noResultException.getMessage(), "No users in page: " + pageable);
        verify(userRepository).findAll(pageable);
    }

    @Test
    void testUpdateUserExpectedSuccess() {
        try(MockedStatic<UserUtil> mockedUserUtil = mockStatic(UserUtil.class)) {
            mockedUserUtil.when(() -> UserUtil.hasAuthority(any(ProfileRole.class))).thenReturn(true);
            User user = new User("name", "email@mail.com", "password");
            User userUpdated = new User("name", "email@mail.com", "secret123");

            when(userRepository.findById(any())).thenReturn(Optional.of(user));
            when(userRepository.save(user)).thenReturn(user);

            userService.update(userUpdated);

            verify(userRepository).findById(any());
            verify(userRepository).save(user);
        }
    }

    @Test
    void testUpdateUserExpectedAuthenticationFailException() throws Exception {
        User user = UserMock.mock(false);
        User userUpdated = UserMock.mock(true, true);

        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        assertThrows(AuthenticationFailException.class, () -> userService.update(userUpdated));

        verify(userRepository, never()).findById(any());
        verify(userRepository, never()).save(user);
    }

    @Test
    void testInsertNewUserWithNonExistentProfileExpectedNoResultException() throws Exception {
        User user = UserMock.mock(false);
        when(profileService.findById(anyLong())).thenThrow(NoResultException.class);
        assertThrows(NoResultException.class, () -> userService.insert(user));

        verify(profileService).findById(anyLong());
    }
}
