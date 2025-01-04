package naegamaja_server.naegamaja.domain.chat.repository;

import lombok.RequiredArgsConstructor;
import naegamaja_server.naegamaja.domain.chat.entity.ChatLog;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class CustomRedisChatLogRepository {
    private final StringRedisTemplate stringRedisTemplate;
    private final static String CHAT_LOG_KEY = "chat_log";
    private final RedisTemplate<String, Object> redisTemplate;

    public void saveChatLog(String content, Long roomNumber, String sender) {
        ChatLog chatLog = ChatLog.builder()
                .content(content)
                .sender(sender)
                .sendTime(LocalDateTime.now())
                .build();

        redisTemplate.opsForList().rightPush(CHAT_LOG_KEY + ":" + roomNumber, chatLog);
    }

}
