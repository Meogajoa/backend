package naegamaja_server.naegamaja.domain.auth.service;

import lombok.RequiredArgsConstructor;
import naegamaja_server.naegamaja.domain.session.entity.UserSession;
import naegamaja_server.naegamaja.domain.session.repository.CustomRedisSessionRepository;
import naegamaja_server.naegamaja.domain.session.repository.RedisSessionRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisAuthService {

    private final CustomRedisSessionRepository customRedisSessionRepository;
    private final RedisSessionRepository redisSessionRepository;

    public boolean isValidSessionId(String sessionId) {
        return customRedisSessionRepository.isValidSessionId(sessionId);
    }


    public void saveSessionId(String sessionId, String email) {
        customRedisSessionRepository.saveSessionId(sessionId, email);
    }

    public void saveSession(UserSession userSession) {
        redisSessionRepository.save(userSession);
    }

    public void deleteSessionId(String sessionId) {
        customRedisSessionRepository.deleteSessionId(sessionId);
    }

    public UserSession trackUserSession(String sessionId) {
        return customRedisSessionRepository.trackUserSession(sessionId);
    }



}
