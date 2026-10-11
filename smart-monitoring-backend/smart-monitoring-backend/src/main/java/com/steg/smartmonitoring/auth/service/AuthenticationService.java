package com.steg.smartmonitoring.auth.service;

import com.steg.smartmonitoring.auth.dto.AuthenticationResponse;
import com.steg.smartmonitoring.auth.dto.LoginRequest;
import com.steg.smartmonitoring.auth.dto.RegisterRequest;

public interface AuthenticationService {

    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse login(LoginRequest request);

}