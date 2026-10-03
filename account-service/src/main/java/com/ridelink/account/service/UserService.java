package com.ridelink.account.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ridelink.account.dto.LoginRequest;
import com.ridelink.account.dto.LoginResponse;
import com.ridelink.account.dto.RegisterRequest;
import com.ridelink.account.dto.UpdateUserRequest;
import com.ridelink.account.dto.UserResponse;
import com.ridelink.account.exception.EmailAlreadyExistsException;
import com.ridelink.account.exception.InvalidPasswordException;
import com.ridelink.account.exception.UserNotFoundException;
import com.ridelink.account.model.User;
import com.ridelink.account.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public User register(RegisterRequest request) {

        String normalizedEmail = request.getEmail() != null ? request.getEmail().trim().toLowerCase() : "";

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new EmailAlreadyExistsException("Email already registered");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(normalizedEmail);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setRole(request.getRole());

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {

        String normalizedEmail = request.getEmail() != null ? request.getEmail().trim().toLowerCase() : "";

        User user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            throw new InvalidPasswordException("Invalid password");
        }

        String token = jwtService.generateToken(
                user.getId(),
                user.getEmail(),
                user.getRole().name()
        );

        return new LoginResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole(),
                token
        );
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with id: " + id));

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole()
        );
    }

    @Transactional
    public UserResponse updateUser(
            Long id,
            UpdateUserRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with id: " + id));

        if (request.getName() != null &&
                !request.getName().isBlank()) {

            user.setName(request.getName());
        }

        if (request.getPhone() != null &&
                !request.getPhone().isBlank()) {

            user.setPhone(request.getPhone());
        }

        User updatedUser = userRepository.save(user);

        return new UserResponse(
                updatedUser.getId(),
                updatedUser.getName(),
                updatedUser.getEmail(),
                updatedUser.getPhone(),
                updatedUser.getRole()
        );
    }

    @Transactional
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with id: " + id));

        userRepository.delete(user);
    }
}