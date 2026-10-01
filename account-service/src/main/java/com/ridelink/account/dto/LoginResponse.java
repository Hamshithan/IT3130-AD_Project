package com.ridelink.account.dto;

import com.ridelink.account.model.Role;

public class LoginResponse {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private Role role;
    private String token;

    public LoginResponse() {
    }

    public LoginResponse(
            Long id,
            String name,
            String email,
            String phone,
            Role role,
            String token) {

        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public Role getRole() {
        return role;
    }

    public String getToken() {
        return token;
    }
}