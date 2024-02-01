package org.nurfet.jwtserverspring.jwt.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
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
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.security.SignatureException;
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
        }  catch (ExpiredJwtException e) {
            sendErrorResponse((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse,
                    e.getMessage(), "Срок действия JWT токена истек");
        } catch (MalformedJwtException e) {
            sendErrorResponse((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse,
                    e.getMessage(), "Неправильно сформированный JWT токен");
        }
        catch (JwtException e) {
            sendErrorResponse((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse,
                    e.getMessage(), "Недействительный JWT токен");
        }
    }

    private void sendErrorResponse(HttpServletRequest request, HttpServletResponse response, String logMessage, String clientMessage) throws IOException {
        log.error("Exception Details: {}", logMessage);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String reasonPhrase = HttpStatus.UNAUTHORIZED.getReasonPhrase();
        ApiError apiError = new ApiError(
                HttpServletResponse.SC_UNAUTHORIZED,
                reasonPhrase,
                request.getRequestURL().toString(),
                DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm").withZone(ZoneId.systemDefault()).format(Instant.now()),
                clientMessage
        );

        String jsonError = convertApiErrorToJson(apiError);

        response.getWriter().write(jsonError);
        response.getWriter().flush();
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
            log.error("Cериализации в JSON завершилась ошибкой {}", e.getMessage());
        }
        return "{}";
    }
}
