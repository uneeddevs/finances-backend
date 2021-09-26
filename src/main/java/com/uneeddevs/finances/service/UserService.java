package com.uneeddevs.finances.service;

import com.uneeddevs.finances.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserService {

    User insert(User user);
    User findById(UUID uuid);
    User findByEmail(String username);
    Page<User> findPage(Pageable pageable);
    User update(User user);
}
