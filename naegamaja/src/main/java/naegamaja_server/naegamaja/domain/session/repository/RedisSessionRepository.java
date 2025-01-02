package naegamaja_server.naegamaja.domain.session.repository;

import lombok.RequiredArgsConstructor;
import naegamaja_server.naegamaja.domain.session.entity.UserSession;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Repository
public class RedisSessionRepository {
    private final StringRedisTemplate stringRedisTemplate;
    private final RedisTemplate<String, Object> redisTemplate;

    private final String SESSION_PREFIX = "session:";

    public boolean isValidSessionId(String sessionId) {
        try {
            Boolean exists = redisTemplate.hasKey(SESSION_PREFIX + sessionId);
            if(!exists) {
                System.out.println("세션아이디가 유효하지 않다");
            }

            return exists;
        } catch (Exception e) {
            System.out.println("Redis 예외 발생: " + e.getMessage());
            return false;
        }
    }

    public void saveSessionId(String sessionId, String email) {
        redisTemplate.opsForValue().set(sessionId, email);
    }

    public UserSession saveSession(UserSession userSession) {
        String key = SESSION_PREFIX + userSession.getSessionId();

        Map<String, Object> sessionMap = new HashMap<>();
        sessionMap.put("nickname", userSession.getNickname());
        sessionMap.put("state", userSession.getState());
        sessionMap.put("roomNumber", userSession.getRoomNumber());
        sessionMap.put("isInGame", userSession.isInGame());
        sessionMap.put("isInRoom", userSession.isInRoom());

        redisTemplate.opsForHash().putAll(key, sessionMap);
    }

    public void deleteSessionId(String sessionId) {
        if(sessionId == null) return;
        Boolean result = redisTemplate.delete(sessionId);
    }

    public UserSession trackUserSession(String sessionId) {
        String key = SESSION_PREFIX + sessionId;
        Map<Object, Object> sessionMap = redisTemplate.opsForHash().entries(key);

        return UserSession.of(
                (String) sessionMap.get("nickname"),
                (String) sessionMap.get("state"),
                (String) sessionMap.get("sessionId"),
                (Long) sessionMap.get("roomNumber"),
                sessionMap.get("isInGame") != null && Boolean.parseBoolean(sessionMap.get("isInGame").toString()),
                sessionMap.get("isInRoom") != null && Boolean.parseBoolean(sessionMap.get("isInRoom").toString())
        );
    }
}
