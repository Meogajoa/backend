package naegamaja_server.naegamaja.domain.room.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import naegamaja_server.naegamaja.domain.room.dto.RoomUserInfo;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisRoomInfoPublisher {
    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;

    public void publishRoomInfo(RoomUserInfo roomUserInfo) {
        try {
            stringRedisTemplate.convertAndSend("pubsub:roomInfo", objectMapper.writeValueAsString(roomUserInfo));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
