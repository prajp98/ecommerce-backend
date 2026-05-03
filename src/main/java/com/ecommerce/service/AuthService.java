package com.ecommerce.service;

import com.ecommerce.dto.request.LoginRequest;
import com.ecommerce.dto.request.RegisterRequest;
import com.ecommerce.dto.response.LoginResponse;
import com.ecommerce.dto.response.RegisterResponse;

public interface AuthService {

    RegisterResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);
}
