package naegamaja_server.naegamaja.domain.room.repository;

import lombok.RequiredArgsConstructor;
import naegamaja_server.naegamaja.domain.room.domain.Room;
import naegamaja_server.naegamaja.domain.room.dto.RoomRequest;
import naegamaja_server.naegamaja.domain.room.dto.RoomResponse;
import naegamaja_server.naegamaja.system.exception.model.ErrorCode;
import naegamaja_server.naegamaja.system.exception.model.RestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class RedisRoomRepository {
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String ROOM_KEY_PREFIX = "room:";
    private static final String AVAILABLE_ROOM_LIST_KEY = "availalbeRoomList";

    public Room findById(String roomId) {
        Room room = (Room) redisTemplate.opsForHash().get(ROOM_KEY_PREFIX + roomId, roomId);

        if(room == null) {
            throw new RestException(ErrorCode.ROOM_NOT_FOUND);
        }

        return room;
    }

    public void saveUserToRoom(String nickname, Room room) {
        String key = ROOM_KEY_PREFIX + room.getId() + ":users";
        redisTemplate.opsForSet().add(key, nickname);
    }

    public boolean isAlreadyExistRoom(String roomId) {
        return roomId != null && redisTemplate.hasKey(ROOM_KEY_PREFIX + roomId);
    }

    public int getAvailableRoomNumber() {
        String key = AVAILABLE_ROOM_LIST_KEY;
        Set<Object> result = redisTemplate.opsForZSet().range(key, 0, 0);

        if(result == null || result.isEmpty()) {
            throw new RestException(ErrorCode.NO_AVAILABLE_ROOM);
        }

        return (int) result.iterator().next();
    }





}
