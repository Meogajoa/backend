package naegamaja_server.naegamaja.domain.room.repository;

import lombok.RequiredArgsConstructor;
import naegamaja_server.naegamaja.domain.room.domain.Room;
import naegamaja_server.naegamaja.system.exception.model.ErrorCode;
import naegamaja_server.naegamaja.system.exception.model.RestException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
@RequiredArgsConstructor
public class RedisRoomRepository {
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String ROOM_KEY_PREFIX = "room:";
    private static final String AVAILABLE_ROOM_LIST_KEY = "availableRoomList:";

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

        Object roomNumberObj = result.iterator().next();
        if (roomNumberObj instanceof Integer) {
            return (Integer) roomNumberObj;
        } else {
            throw new RestException(ErrorCode.INVALID_ROOM_NUMBER);
        }
    }

    public void createRoom(Room room, int roomNumber) {
        String key = AVAILABLE_ROOM_LIST_KEY;
        Integer roomNumberObj = roomNumber;

        // 삭제 시 Java 직렬화된 Integer 객체 전달
        Long removed = redisTemplate.opsForZSet().remove(key, roomNumberObj);
        if (removed == null || removed == 0) {
            throw new RestException(ErrorCode.FAILED_TO_REMOVE_AVAILABLE_ROOM);
        }

        // Room 정보 저장
        String roomKey = ROOM_KEY_PREFIX + room.getId();
        redisTemplate.opsForHash().put(roomKey, "id", room.getId());
        redisTemplate.opsForHash().put(roomKey, "roomName", room.getRoomName());
        redisTemplate.opsForHash().put(roomKey, "roomPassword", room.getRoomPassword());
        redisTemplate.opsForHash().put(roomKey, "roomOwner", room.getRoomOwner());
        redisTemplate.opsForHash().put(roomKey, "roomMaxUser", room.getRoomMaxUser());
        redisTemplate.opsForHash().put(roomKey, "roomCurrentUser", room.getRoomCurrentUser());
        redisTemplate.opsForHash().put(roomKey, "roomIsPlaying", room.isRoomIsPlaying());

        System.out.println(room);
    }
}
