package com.microservice.organizationservice.service;


import com.microservice.organizationservice.dto.LoginDto;
import com.microservice.organizationservice.dto.RegisterDto;

public interface AuthService {
    String login(LoginDto loginDto);
    String register(RegisterDto registerDto);
}