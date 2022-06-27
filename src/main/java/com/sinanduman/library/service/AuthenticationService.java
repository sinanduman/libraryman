package com.sinanduman.library.service;


import com.sinanduman.library.auth.TokenManager;
import com.sinanduman.library.model.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;


@RequiredArgsConstructor
@Service
public class AuthenticationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);

    private final UserService userService;
    private final TokenManager tokenManager;

    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthenticationResponse authenticate(String authorization) {
        String errorMessage = "";

        if (!StringUtils.hasText(authorization)) {
            errorMessage = "INVALID AUTHENTICATION";
            LOGGER.error("Message: {}, Payload: {}", errorMessage, authorization);
            throw new RuntimeException(errorMessage);
        }

        String[] httpBasicAuthPayload = parseHttpBasicPayload(authorization);
        String username = httpBasicAuthPayload[0];
        String password = httpBasicAuthPayload[1];

        Authentication authenticate = authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(username, password)
                );

        User user = (User) authenticate.getPrincipal();
        if (user == null) {
            errorMessage = "USER NOT FOUND";
            LOGGER.error("Message: {}, Authorization: {}", errorMessage, authorization);
            throw new RuntimeException(errorMessage);
        }

        UserDetails userDetails = userService.loadUserByUsername(username);
        String userId = userService.getUserIdFromUserName(username);
        String token = tokenManager.generateToken(userDetails, userId);
        return new AuthenticationResponse(userDetails.getUsername(), token);
    }

    private String[] parseHttpBasicPayload(String authorization) {
        String type = "Basic";
        if (StringUtils.hasText(authorization) && authorization.contains(type)) {
            /*
            0: Authorization
            1: Type
            */
            String base64Credentials = authorization.substring("Basic".length()).trim();
            byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
            String credentials = new String(credDecoded, StandardCharsets.UTF_8);
            return credentials.split(":", 2);
        }
        return null;
    }
}
