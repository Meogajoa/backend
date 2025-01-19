package naegamaja_server.naegamaja.domain.room.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import naegamaja_server.naegamaja.domain.room.dto.RoomUserInfo;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisRoomInfoPublisher {
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    private final String ASYNC_STREAM_KEY = "stream:async:";

    public void publishRoomInfo(String roomId, RoomUserInfo roomUserInfo) {
        try {
            String users = objectMapper.writeValueAsString(roomUserInfo.getUsers());

            Map<String, String> messageMap = Map.of(
                    "type", "ROOM_INFO",
                    "roomId", roomId,
                    "users", users
            );

            redisTemplate.opsForStream().add(ASYNC_STREAM_KEY, messageMap);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
