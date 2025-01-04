package naegamaja_server.naegamaja.domain.auth.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import naegamaja_server.naegamaja.domain.session.entity.UserSession;
import naegamaja_server.naegamaja.domain.session.repository.CustomRedisSessionRepository;
import naegamaja_server.naegamaja.domain.session.repository.RedisSessionRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
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
        customRedisSessionRepository.saveNicknameToSession(userSession.getNickname(), userSession.getSessionId());
    }

    public void deleteSessionId(String sessionId) {
        customRedisSessionRepository.deleteSessionId(sessionId);
    }

    public UserSession trackUserSession(String sessionId) {
        return customRedisSessionRepository.trackUserSession(sessionId);
    }


    public boolean isUserSessionActive(String nickname) {
        return (customRedisSessionRepository.isUserSessionActive(nickname));
    }

    public String getSessionIdByNickname(String nickname) {
        return customRedisSessionRepository.getNicknameToSessionId(nickname);
    }

}
