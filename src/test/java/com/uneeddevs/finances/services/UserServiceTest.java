package com.uneeddevs.finances.services;

import com.uneeddevs.finances.model.User;
import com.uneeddevs.finances.repository.UserRepository;
import com.uneeddevs.finances.service.UserService;
import com.uneeddevs.finances.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserService userService;
    private UserRepository userRepository;

    @BeforeEach
    void setup(){
        userRepository = mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void testFindByEmailExpectedSuccess() {
        User user = new User("name", "email@mail.com", "password");
        String email = "user@mail.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        var userEmailReturn = userService.findByEmail(email);
        assertNotNull(userEmailReturn, "User return cannot be null");

        verify(userRepository).findByEmail(email);
    }

    @Test
    void testFindByEmailExpectedNoResultException() {
        String email = "user@mail.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        NoResultException noResultException = assertThrows(NoResultException.class,
                () -> userService.findByEmail(email),
                "Expected NoResultException");
        assertEquals(noResultException.getMessage(), "No user with email " + email);
        verify(userRepository).findByEmail(email);
    }

    @Test
    void testFindByIdExpectedSuccess() {
        User user = new User("name", "email@mail.com", "password");
        UUID uuid = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");
        when(userRepository.findById(uuid)).thenReturn(Optional.of(user));

        var userEmailReturn = userService.findById(uuid);
        assertNotNull(userEmailReturn, "User return cannot be null");

        verify(userRepository).findById(uuid);
    }

    @Test
    void testFindByIdExpectedNoResultException() {
        UUID uuid = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");
        when(userRepository.findById(uuid)).thenReturn(Optional.empty());

        NoResultException noResultException = assertThrows(NoResultException.class,
                () -> userService.findById(uuid),
                "Expected NoResultException");
        assertEquals(noResultException.getMessage(), "No user with UUID " + uuid);
        verify(userRepository).findById(uuid);
    }

    @Test
    void saveUserExpectedSuccess() {
        User user = new User("name", "email@mail.com", "password");
        when(userRepository.save(user)).thenReturn(user);
        userService.save(user);
        verify(userRepository).save(user);
    }

    @Test
    void saveUserExpectedPersistentException() {
        User user = new User("name", "email@mail.com", "password");
        when(userRepository.save(user)).thenThrow(RuntimeException.class);
        PersistenceException persistenceException = assertThrows(PersistenceException.class,
                () -> userService.save(user),
                "Expected PersistenceException");
        //
        assertEquals(persistenceException.getMessage(), "Error on persisting user " + user.toString());
        verify(userRepository).save(user);
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
        User user = new User("name", "email@mail.com", "password");
        User userUpdated = new User("name", "email@mail.com", "secret123");

        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        userService.update(userUpdated);

        verify(userRepository).findById(any());
        verify(userRepository).save(user);

    }
}
