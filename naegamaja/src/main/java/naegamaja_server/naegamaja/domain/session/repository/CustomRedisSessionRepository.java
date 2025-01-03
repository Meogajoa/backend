package naegamaja_server.naegamaja.domain.session.repository;

import lombok.RequiredArgsConstructor;
import naegamaja_server.naegamaja.domain.session.entity.UserSession;
import naegamaja_server.naegamaja.domain.session.state.State;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@RequiredArgsConstructor
@Repository
public class CustomRedisSessionRepository {
    private final StringRedisTemplate stringRedisTemplate;

    private final String SESSION_PREFIX = "session:";

    public boolean isValidSessionId(String sessionId) {
        try {
            Boolean exists = stringRedisTemplate.hasKey(SESSION_PREFIX + sessionId);
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
        stringRedisTemplate.opsForValue().set(sessionId, email);
    }

    public void deleteSessionId(String sessionId) {
        if(sessionId == null) return;
        Boolean result = stringRedisTemplate.delete(sessionId);
    }

    public UserSession trackUserSession(String sessionId) {
        String key = SESSION_PREFIX + sessionId;
        Map<Object, Object> sessionMap = stringRedisTemplate.opsForHash().entries(key);

        return UserSession.of(
                (String) sessionMap.get("nickname"),
                State.valueOf((String) sessionMap.get("state")),
                (String) sessionMap.get("sessionId"),
                (Long) sessionMap.get("roomNumber"),
                sessionMap.get("isInGame") != null && Boolean.parseBoolean(sessionMap.get("isInGame").toString()),
                sessionMap.get("isInRoom") != null && Boolean.parseBoolean(sessionMap.get("isInRoom").toString())
        );
    }

    public void setUserSessionState(String authorization, State state, Long roomNumber) {
        stringRedisTemplate.opsForHash().put(SESSION_PREFIX + authorization, "state", state.toString());
        stringRedisTemplate.opsForHash().put(SESSION_PREFIX + authorization, "roomNumber", roomNumber.toString());
        stringRedisTemplate.opsForHash().put(SESSION_PREFIX + authorization, "isInRoom", "true");

    }
}
