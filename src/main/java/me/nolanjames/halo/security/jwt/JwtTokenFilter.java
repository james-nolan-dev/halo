package me.nolanjames.halo.security.jwt;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.nolanjames.halo.user.entity.User;
import me.nolanjames.halo.user.security.CustomUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenFilter.class);

    private final JwtUtility jwtUtility;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    HandlerExceptionResolver exceptionResolver;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        if (!hasAuthorisationBearer(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        String bearerToken = getBearerToken(request);
        LOGGER.info("Token: {}", bearerToken);

        try {
            Claims claims = jwtUtility.validateToken(bearerToken);
            UserDetails userDetails = getUserDetails(claims);

            setAuthenticationContext(userDetails, request);
            filterChain.doFilter(request, response);

            clearAuthenticationContext();
        } catch (JwtValidationException e) {
            LOGGER.error(e.getMessage(), e);
            exceptionResolver.resolveException(request, response, null, e);
        }
    }


    private void setAuthenticationContext(UserDetails userDetails, HttpServletRequest request) {
        var authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void clearAuthenticationContext() {
        SecurityContextHolder.clearContext();
    }

    private UserDetails getUserDetails(Claims claims) {
        String subject = (String) claims.get(Claims.SUBJECT);
        String[] claimsArray = subject.split(",");

        Integer userId = Integer.valueOf(claimsArray[0]);
        String username = claimsArray[1];
        String role = (String) claims.get("role");

        User user = User.builder()
                .id(userId)
                .username(username)
                .role(role)
                .build();
        LOGGER.info("User parsed from JWT: {} {} {}", user.getId(), user.getUsername(), user.getRole());

        return new CustomUserDetails(user);
    }

    private boolean hasAuthorisationBearer(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        LOGGER.info("Authorisation Header: {}", header);

        return !ObjectUtils.isEmpty(header) && header.startsWith("Bearer ");
    }

    private String getBearerToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        String[] bearerString = header.split(" ");

        return bearerString.length == 2 ? bearerString[1] : null;
    }
}
