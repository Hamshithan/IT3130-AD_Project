package com.ridelink.account.service;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ridelink.account.dto.LoginRequest;
import com.ridelink.account.dto.LoginResponse;
import com.ridelink.account.dto.RegisterRequest;
import com.ridelink.account.dto.UpdateUserRequest;
import com.ridelink.account.dto.UserResponse;
import com.ridelink.account.exception.EmailAlreadyExistsException;
import com.ridelink.account.exception.InvalidPasswordException;
import com.ridelink.account.exception.UserNotFoundException;
import com.ridelink.account.model.Role;
import com.ridelink.account.model.User;
import com.ridelink.account.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserService userService;

    private User sampleUser;
    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        sampleUser = new User("John Doe", "john@example.com", "encodedPassword", "0771234567", Role.PASSENGER);
        sampleUser.setId(1L);

        registerRequest = new RegisterRequest();
        registerRequest.setName("John Doe");
        registerRequest.setEmail("john@example.com");
        registerRequest.setPassword("rawPassword");
        registerRequest.setPhone("0771234567");
        registerRequest.setRole(Role.PASSENGER);

        loginRequest = new LoginRequest();
        loginRequest.setEmail("john@example.com");
        loginRequest.setPassword("rawPassword");
    }

    @Test
    void register_Success() {
        when(userRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(passwordEncoder.encode("rawPassword")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);

        User created = userService.register(registerRequest);

        assertNotNull(created);
        assertEquals("john@example.com", created.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_DuplicateEmail_ThrowsException() {
        when(userRepository.existsByEmail("john@example.com")).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> userService.register(registerRequest));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void login_Success() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(sampleUser));
        when(passwordEncoder.matches("rawPassword", "encodedPassword")).thenReturn(true);
        when(jwtService.generateToken(1L, "john@example.com", "PASSENGER")).thenReturn("mockJwtToken");

        LoginResponse response = userService.login(loginRequest);

        assertNotNull(response);
        assertEquals("mockJwtToken", response.getToken());
        assertEquals("john@example.com", response.getEmail());
    }

    @Test
    void login_InvalidPassword_ThrowsException() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(sampleUser));
        when(passwordEncoder.matches("rawPassword", "encodedPassword")).thenReturn(false);

        assertThrows(InvalidPasswordException.class, () -> userService.login(loginRequest));
    }

    @Test
    void login_UnknownUser_ThrowsException() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.login(loginRequest));
    }

    @Test
    void getUserById_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));

        UserResponse response = userService.getUserById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("John Doe", response.getName());
    }

    @Test
    void getUserById_NotFound_ThrowsException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(99L));
    }

    @Test
    void updateUser_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);

        UpdateUserRequest updateReq = new UpdateUserRequest();
        updateReq.setName("Jane Doe");
        updateReq.setPhone("0779999999");

        UserResponse response = userService.updateUser(1L, updateReq);

        assertNotNull(response);
        verify(userRepository).save(sampleUser);
    }

    @Test
    void deleteUser_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));

        userService.deleteUser(1L);

        verify(userRepository).delete(sampleUser);
    }

    @Test
    void deleteUser_NotFound_ThrowsException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(99L));
    }
}
