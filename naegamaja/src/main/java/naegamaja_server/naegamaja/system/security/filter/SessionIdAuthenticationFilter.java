package naegamaja_server.naegamaja.system.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import naegamaja_server.naegamaja.domain.redis.service.RedisService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SessionIdAuthenticationFilter extends OncePerRequestFilter {

    private final RedisService redisService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String sessionId = request.getHeader("DdingjiSessionId");
        if (sessionId != null && !sessionId.isBlank() && redisService.isValidSessionId(sessionId)) {
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(sessionId, null, null);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }else{
            response.sendRedirect("/auth/login");
        }
        filterChain.doFilter(request, response);
    }
}
