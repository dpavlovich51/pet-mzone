package com.mzone.main.security;

import com.mzone.main.exception.JwtAuthenticationException;
import com.mzone.main.registration.entity.EmailValue;
import com.mzone.main.user.service.UserService;
import io.jsonwebtoken.JwtException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

import static org.apache.logging.log4j.util.Strings.isEmpty;

//// TODO: 12/11/2021 need to refactor it
@Component
@AllArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenFilter.class);

    private final JwtProvider jwtTokenUtil;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {
        // Get authorization header and validate
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (isEmpty(header) || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        // Get jwt token and validate
        final String token = header.split(" ")[1].trim();
        try {
            final JwtProvider.JwtData parse = jwtTokenUtil.parse(token);

            //// TODO: 28/05/2022 put to parse.getBody().getUsername() username instead of email and add username to UserEntity
            // Get user identity and set it on the spring security context
            final String username = parse.getBody().getUsername();
            UserDetails userDetails = userService
                    .findByEmail(EmailValue.create(username))
                    .orElseThrow(() -> new JwtAuthenticationException(MessageFormat.format("User with username:{0}  not found.",
                            username)));

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails == null ?
                            List.of() : userDetails.getAuthorities()
            );

            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);

        } catch (JwtAuthenticationException | JwtException | IllegalArgumentException ex) {
            if (response.isCommitted()) {
                LOGGER.trace("Did not write to response since already committed");
                return;
            }
            request.setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, ex);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            LOGGER.warn("Failed to authenticate user.", ex);
        }
    }
}