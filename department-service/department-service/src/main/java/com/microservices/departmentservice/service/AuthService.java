package com.microservices.departmentservice.service;

import com.microservices.departmentservice.dto.LoginDto;
import com.microservices.departmentservice.dto.RegisterDto;

public interface AuthService {
    String login(LoginDto loginDto);
    String register(RegisterDto registerDto);
}