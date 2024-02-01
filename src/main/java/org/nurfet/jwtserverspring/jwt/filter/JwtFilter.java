package org.nurfet.jwtserverspring.jwt.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nurfet.jwtserverspring.exception.ApiError;
import org.nurfet.jwtserverspring.jwt.provider.JwtProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {
    private static final String AUTHORIZATION = "Authorization";

    private final JwtProvider jwtProvider;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        final String token = getTokenFromRequest((HttpServletRequest) servletRequest);

        try {
            if (token != null && jwtProvider.validateAccessToken(token)) {
                final Authentication auth  = jwtProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (JwtException e) {
            HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setContentType("application/json");

            ApiError apiError = new ApiError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    ((HttpServletRequest) servletRequest).getRequestURL().toString(),
                    DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
                            .withZone(ZoneId.systemDefault()).format(Instant.now()),
                    e.getMessage()
            );

            String jsonError = convertApiErrorToJson(apiError);

            httpResponse.getWriter().write(jsonError);
            httpResponse.getWriter().flush();
        }
    }
    private String getTokenFromRequest(HttpServletRequest request) {
        final String bearer = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }

    private String convertApiErrorToJson(ApiError apiError) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(apiError);
        } catch (JsonProcessingException e) {
            log.error("сериализации в JSON");
        }
        return "{}";
    }
}
