package com.steg.smartmonitoring.auth.service.impl;

import com.steg.smartmonitoring.auth.dto.AuthenticationResponse;
import com.steg.smartmonitoring.auth.dto.LoginRequest;
import com.steg.smartmonitoring.auth.dto.RegisterRequest;
import com.steg.smartmonitoring.auth.jwt.JwtService;
import com.steg.smartmonitoring.auth.security.CustomUserDetails;
import com.steg.smartmonitoring.auth.service.AuthenticationService;
import com.steg.smartmonitoring.common.exception.ResourceAlreadyExistsException;
import com.steg.smartmonitoring.user.entity.User;
import com.steg.smartmonitoring.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl
        implements AuthenticationService {


    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;


    @Override
    public AuthenticationResponse register(
            RegisterRequest request
    ) {

        // Vérifier que l'utilisateur n'existe pas déjà
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException(
                    "Un utilisateur avec cet email existe déjà"
            );
        }

        User user = User.builder()

                .username(request.getUsername())

                .email(request.getEmail())

                .password(passwordEncoder.encode(
                        request.getPassword()
                ))

                .role(request.getRole())

                .build();

        User savedUser = userRepository.save(user);

        return AuthenticationResponse.builder()

                .id(savedUser.getId())

                .username(savedUser.getUsername())

                .email(savedUser.getEmail())

                .role(savedUser.getRole())

                .token(null)

                .build();

    }

    @Override
    public AuthenticationResponse login(LoginRequest request) {

        try {
            authenticationManager.authenticate(

                    new UsernamePasswordAuthenticationToken(

                            request.getEmail(),

                            request.getPassword()

                    )

            );
        } catch (BadCredentialsException ex) {
            throw new BadCredentialsException("Email ou mot de passe incorrect");
        }

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new UsernameNotFoundException("Utilisateur non trouvé")
                );

        String token = jwtService.generateToken(
                new CustomUserDetails(user)
        );

        return AuthenticationResponse.builder()

                .id(user.getId())

                .username(user.getUsername())

                .email(user.getEmail())

                .role(user.getRole())

                .token(token)

                .build();

    }

}