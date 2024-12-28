package naegamaja_server.naegamaja.domain.redis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public boolean isValidSessionId(String sessionId) {
        try {
            Boolean exists = redisTemplate.hasKey(sessionId);
            return exists != null && exists;
        } catch (Exception e) {
            System.out.println("Redis 예외 발생: " + e.getMessage());
            return false;
        }
    }


    public void saveSessionId(String sessionId, String email) {
        redisTemplate.opsForValue().set(sessionId, email);
    }

    public void deleteSessionId(String sessionId) {
        Boolean result = redisTemplate.delete(sessionId);
    }



}
