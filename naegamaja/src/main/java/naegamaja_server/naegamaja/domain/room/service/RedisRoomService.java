package naegamaja_server.naegamaja.domain.room.service;

import lombok.RequiredArgsConstructor;
import naegamaja_server.naegamaja.domain.room.dto.RoomRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RedisRoomService {

    private final RedisTemplate<String, Object> redisTemplate;

    public void joinRoom(RoomRequest.JoinRoomRequest request) {
        redisTemplate.opsForValue().set(request.getRoomId(), request);
    }



}
