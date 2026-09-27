package com.ridelink.account.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ridelink.account.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}