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

    private final String ROOM_INFO_STREAM_KEY = "stream:room:info:";

    public void publishRoomInfo(String roomId, RoomUserInfo roomUserInfo) {
        try {
            String json = objectMapper.writeValueAsString(roomUserInfo.getUsers());

            Map<String, String> messageMap = Map.of(
                    "roomId", roomId,
                    "users", json
            );

            redisTemplate.opsForStream().add(ROOM_INFO_STREAM_KEY + roomId, messageMap);
            
            log.info("레전드 pub 상황 발생!!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
