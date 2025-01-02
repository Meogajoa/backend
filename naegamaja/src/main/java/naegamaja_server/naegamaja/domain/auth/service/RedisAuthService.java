package naegamaja_server.naegamaja.domain.auth.service;

import lombok.RequiredArgsConstructor;
import naegamaja_server.naegamaja.domain.session.entity.UserSession;
import naegamaja_server.naegamaja.domain.session.repository.RedisSessionRepository;
import naegamaja_server.naegamaja.system.exception.model.RestException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RedisAuthService {

    private final RedisSessionRepository redisSessionRepository;

    public boolean isValidSessionId(String sessionId) {
        return redisSessionRepository.isValidSessionId(sessionId);
    }


    public void saveSessionId(String sessionId, String email) {
        redisSessionRepository.saveSessionId(sessionId, email);
    }

    public void saveSession(UserSession userSession) {

    }

    public void deleteSessionId(String sessionId) {
        redisSessionRepository.deleteSessionId(sessionId);
    }

    public UserSession trackUserSession(String sessionId) {
        return redisSessionRepository.trackUserSession(sessionId);
    }



}
