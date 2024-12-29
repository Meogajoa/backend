package naegamaja_server.naegamaja.domain.auth.service;

import lombok.RequiredArgsConstructor;
import naegamaja_server.naegamaja.domain.session.entity.UserSession;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisAuthService {

    private final RedisTemplate<String, Object> redisTemplate;

    private final String SESSION_PREFIX = "session:";

    public boolean isValidSessionId(String sessionId) {
        try {
            Boolean exists = redisTemplate.hasKey(SESSION_PREFIX + sessionId);
            return exists != null && exists;
        } catch (Exception e) {
            System.out.println("Redis 예외 발생: " + e.getMessage());
            return false;
        }
    }


    public void saveSessionId(String sessionId, String email) {
        redisTemplate.opsForValue().set(sessionId, email);
    }

    public void saveSession(UserSession userSession) {
//        redisTemplate.opsForHash().
    }

    public void deleteSessionId(String sessionId) {
        if(sessionId == null) return;
        Boolean result = redisTemplate.delete(sessionId);
    }



}
