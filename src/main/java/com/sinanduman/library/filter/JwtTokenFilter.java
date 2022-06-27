package com.sinanduman.library.filter;

import com.sinanduman.library.auth.TokenManager;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@RequiredArgsConstructor
@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenFilter.class);

    private final TokenManager tokenManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = tokenManager.extractTokenFrom(request);
            if (token == null) {
                filterChain.doFilter(request, response);
                return;
            }

            if (StringUtils.hasText(token) && tokenManager.isTokenValid(token)) {
                String principal = tokenManager.getUserIdFrom(token);
                Collection<? extends GrantedAuthority> roles = tokenManager.getRolesFrom(token);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        principal, null, roles);
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                filterChain.doFilter(request, response);
                return;
            } else {
                if (isBasicAuthRequest(request)) {
                    SecurityContextHolder.clearContext();
                    filterChain.doFilter(request, response);
                    return;
                }
                prepareInvalidAuthResponse(response);
                return;
            }
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
        filterChain.doFilter(request, response);
    }

    private boolean isBasicAuthRequest(HttpServletRequest request) {
        String authType = "Basic ";
        String data = request.getHeader(HttpHeaders.AUTHORIZATION);
        return (StringUtils.hasText(data) && data.startsWith(authType));
    }

    private void prepareInvalidAuthResponse(HttpServletResponse response) {
        SecurityContextHolder.clearContext();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.addHeader("INVALID-AUTH", "Invalid authentication attempt!");
    }
}
