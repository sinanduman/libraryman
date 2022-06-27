package com.sinanduman.library.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sinanduman.library.config.ConfigProperties;
import com.sinanduman.library.model.Role;
import com.sinanduman.library.utility.DateUtility;
import com.sinanduman.library.utility.RoleUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class TokenManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenManager.class);

    private final ConfigProperties configProperties;


    public byte[] generateKey() {
        return Base64.getUrlEncoder().encode(configProperties.getSecretKey().getBytes());
    }

    public String generateToken(UserDetails userDetails, String userId) {

        List<String> claims = new ArrayList<>();

        Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();
        for (GrantedAuthority role : roles) {
            claims.add(role.getAuthority());
        }

        LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
        Date from = DateUtility.fromLocalDateTime(now);
        Date to = DateUtility.fromLocalDateTime(now.plusDays(1));

        try {
            Algorithm algorithm = Algorithm.HMAC256(configProperties.getSecretKey());
            return JWT.create()
                    .withIssuer(configProperties.getIssuer())
                    .withAudience(configProperties.getAudiance())
                    .withSubject(userId)
                    .withClaim("scopes", claims)
                    .withIssuedAt(from)
                    .withExpiresAt(to)
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            LOGGER.error(ExceptionUtils.getStackTrace(exception));
            throw exception;
        }
    }

    public Boolean isTokenValid(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(configProperties.getSecretKey());
            DecodedJWT decodedToken = JWT.decode(token);
            algorithm.verify(decodedToken);
            return true;
        } catch (JWTVerificationException exception) {
            LOGGER.error(ExceptionUtils.getStackTrace(exception));
            return false;
        }
    }

    public String getUserIdFrom(String token) {
        if (isTokenValid(token)) {
            DecodedJWT decoded = JWT.decode(token);
            return decoded.getSubject();
        }
        return null;
    }


    public String extractTokenFrom(HttpServletRequest request) {
        String authType = "Bearer ";
        String data = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(data) && data.startsWith(authType)) {
            return data.substring(authType.length());
        }
        return null;
    }

    public Collection<? extends GrantedAuthority> getRolesFrom(String token) {
        if (isTokenValid(token)) {
            DecodedJWT decoded = JWT.decode(token);
            List<String> claimList = decoded.getClaims().get("scopes").asList(String.class);
            if (claimList == null || claimList.isEmpty()) {
                return Collections.emptyList();
            }
            return RoleUtility.getAuthoritiesFromStringList(claimList);
        }
        return Collections.emptyList();
    }

    public Set<Role> getUserRolesFrom(String token) {
        Set<Role> roleSet = new HashSet<>();
        if (isTokenValid(token)) {
            DecodedJWT decoded = JWT.decode(token);
            List<String> claimList = decoded.getClaims().get("scopes").asList(String.class);
            if (claimList == null || claimList.isEmpty()) {
                return Collections.emptySet();
            }
            for (String str : claimList) {
                Role role = Role.findByRoleName(str);
                if (role != null)
                    roleSet.add(role);
            }
            return roleSet;
        }
        return Collections.emptySet();
    }

    public boolean isAuthorizedForTheAction(String token, Set<Role> roles) {
        if (isTokenValid(token)) {
            DecodedJWT decoded = JWT.decode(token);
            List<String> claimList = decoded.getClaims().get("scopes").asList(String.class);
            for (String role : claimList) {
                Optional<Role> roleOpt = roles.stream().filter(r -> r.getRoleName().equals(role)).findFirst();
                if (roleOpt.isPresent())
                    return true;
            }
        }
        return false;
    }
}
