package com.microservices.employeeservice.service;

import com.microservices.employeeservice.dto.LoginDto;
import com.microservices.employeeservice.dto.RegisterDto;

public interface AuthService {
    String login(LoginDto loginDto);
    String register(RegisterDto registerDto);
}