package com.uneeddevs.finances.repository;

import com.uneeddevs.finances.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

}
