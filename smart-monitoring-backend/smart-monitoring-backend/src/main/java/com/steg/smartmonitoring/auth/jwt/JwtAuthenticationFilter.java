package com.steg.smartmonitoring.auth.jwt;


import com.steg.smartmonitoring.auth.security.CustomUserDetailsService;
import com.steg.smartmonitoring.common.exception.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import org.springframework.stereotype.Component;

import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;


@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private final JwtService jwtService;

    private final CustomUserDetailsService userDetailsService;



    @Override
    protected void doFilterInternal(

            HttpServletRequest request,

            HttpServletResponse response,

            FilterChain filterChain

    ) throws ServletException, IOException {

        // Skip JWT validation for OPTIONS preflight requests
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader =
                request.getHeader("Authorization");


        final String jwt;

        final String userEmail;



        if(authHeader == null || !authHeader.startsWith("Bearer ")){

            filterChain.doFilter(request,response);

            return;

        }



        jwt = authHeader.substring(7);


        try {

            userEmail = jwtService.extractUsername(jwt);

            if(userEmail != null &&

                    SecurityContextHolder.getContext()
                            .getAuthentication() == null){


                UserDetails userDetails =
                        userDetailsService
                                .loadUserByUsername(userEmail);



                if(jwtService.isTokenValid(jwt,userDetails)){


                    UsernamePasswordAuthenticationToken authToken =

                            new UsernamePasswordAuthenticationToken(

                                    userDetails,

                                    null,

                                    userDetails.getAuthorities()

                            );


                    authToken.setDetails(

                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)

                    );


                    SecurityContextHolder.getContext()
                            .setAuthentication(authToken);

                }

            }

        } catch (JwtException ex) {

            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token invalide ou expiré");

            return;

        } catch (Exception ex) {

            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Erreur d'authentification");

            return;

        }


        filterChain.doFilter(request,response);

    }

}