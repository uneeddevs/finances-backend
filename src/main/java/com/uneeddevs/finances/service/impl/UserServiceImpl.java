package com.uneeddevs.finances.service.impl;

import com.uneeddevs.finances.enums.ProfileRole;
import com.uneeddevs.finances.model.Profile;
import com.uneeddevs.finances.model.User;
import com.uneeddevs.finances.repository.UserRepository;
import com.uneeddevs.finances.service.ProfileService;
import com.uneeddevs.finances.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ProfileService profileService;

    @Override
    public User findByEmail(String username) {
        log.info("Searching user by username: {}", username);
        return userRepository.findByEmail(username)
                .orElseThrow(() -> {
                    String message = String.format("No user with email %s", username);
                    log.info(message);
                    return new NoResultException(message);
                });
    }

    @Override
    public User insert(User user) {
        Profile profile = profileService.findById(ProfileRole.USER.getRoleId());
        user.addProfile(profile);
        return save(user);
    }

    private User save(User user) {
        String userString = user.toString();
        log.info("Perform persist user: {}", userString);
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            log.error("Error on persist user", e);
        }
        throw new PersistenceException(String.format("Error on persisting user %s", userString));
    }

    @Override
    public User findById(UUID uuid) {
        return userRepository.findById(uuid)
                .orElseThrow(() ->{
                    String message = String.format("No user with UUID %s", uuid);
                    log.info(message);
                    return new NoResultException(message);
                });
    }

    @Override
    public Page<User> findPage(Pageable pageable) {
        log.info("Finding users by page {} ", pageable);
        Page<User> userPage = userRepository.findAll(pageable);
        if(!userPage.isEmpty())
            return userPage;
        log.info("No users founded by page {}", pageable);
        throw new NoResultException("No users in page: " + pageable);
    }

    @Override
    public User update(User user) {
        User oldUser = findById(user.getId());
        updateOldUserObject(oldUser, user);
        return save(oldUser);
    }

    void updateOldUserObject(User oldUser, User newUser) {
        oldUser.setName(newUser.getName());
        oldUser.setPassword(newUser.getPassword());
    }
}
