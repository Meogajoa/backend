package naegamaja_server.naegamaja.domain.room.repository;

import lombok.RequiredArgsConstructor;
import naegamaja_server.naegamaja.domain.room.domain.Room;
import naegamaja_server.naegamaja.system.exception.model.ErrorCode;
import naegamaja_server.naegamaja.system.exception.model.RestException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class RedisRoomRepository {
    private final RedisTemplate<String, Object> redisTemplate;
    private final StringRedisTemplate stringRedisTemplate;

    private static final String ROOM_KEY_PREFIX = "room:";
    private static final String AVAILABLE_ROOM_LIST_KEY = "availableRoomList:";

    private static final String USING_ROOM_LIST_KEY = "usingRoomList:";

    public Room findById(String roomId) {
        String roomKey = ROOM_KEY_PREFIX + roomId;
        String id = (String) redisTemplate.opsForHash().get(roomKey, "id");
        String roomName = (String) redisTemplate.opsForHash().get(roomKey, "roomName");
        String roomPassword = (String) redisTemplate.opsForHash().get(roomKey, "roomPassword");
        String roomOwner = (String) redisTemplate.opsForHash().get(roomKey, "roomOwner");
        String roomMaxUserStr = (String) redisTemplate.opsForHash().get(roomKey, "roomMaxUser");
        String roomCurrentUserStr = (String) redisTemplate.opsForHash().get(roomKey, "roomCurrentUser");
        String roomIsPlayingStr = (String) redisTemplate.opsForHash().get(roomKey, "roomIsPlaying");

        if(id == null) {
            throw new RestException(ErrorCode.ROOM_NOT_FOUND);
        }

        int roomMaxUser = Integer.parseInt(roomMaxUserStr);
        int roomCurrentUser = Integer.parseInt(roomCurrentUserStr);
        boolean roomIsPlaying = Boolean.parseBoolean(roomIsPlayingStr);

        return Room.builder()
                .id(id)
                .roomName(roomName)
                .roomPassword(roomPassword)
                .roomOwner(roomOwner)
                .roomMaxUser(roomMaxUser)
                .roomCurrentUser(roomCurrentUser)
                .roomIsPlaying(roomIsPlaying)
                .build();
    }

    public void saveUserToRoom(String nickname, Room room) {
        String key = ROOM_KEY_PREFIX + room.getId() + ":users";
        redisTemplate.opsForSet().add(key, nickname);
    }

    public boolean isAlreadyExistRoom(String roomId) {
        return roomId != null && redisTemplate.hasKey(ROOM_KEY_PREFIX + roomId);
    }

    public int getAvailableRoomNumber() {
        Set<String> result = stringRedisTemplate.opsForZSet().range(AVAILABLE_ROOM_LIST_KEY, 0, 0);

        if(result.isEmpty()) {
            throw new RestException(ErrorCode.NO_AVAILABLE_ROOM);
        }



        String roomNumberStr = result.iterator().next();
        try {
            return Integer.parseInt(roomNumberStr);
        } catch (NumberFormatException e) {
            throw new RestException(ErrorCode.INVALID_ROOM_NUMBER);
        }
    }

    public void createRoom(Room room, int roomNumber) {
        String roomNumberStr = String.valueOf(roomNumber);

        // AVAILABLE ZSet에서 roomNumber 제거
        Long removed = stringRedisTemplate.opsForZSet().remove(AVAILABLE_ROOM_LIST_KEY, roomNumberStr);
        if (removed == 0) {
            throw new RestException(ErrorCode.FAILED_TO_REMOVE_AVAILABLE_ROOM);
        }

        //차후 동시성 문제 고려 필요
        stringRedisTemplate.opsForZSet().add(USING_ROOM_LIST_KEY, roomNumberStr, roomNumber);

        // Room 정보 저장
        String roomKey = ROOM_KEY_PREFIX + room.getId();
        stringRedisTemplate.opsForHash().put(roomKey, "id", room.getId());
        stringRedisTemplate.opsForHash().put(roomKey, "roomName", room.getRoomName());
        stringRedisTemplate.opsForHash().put(roomKey, "roomPassword", room.getRoomPassword());
        stringRedisTemplate.opsForHash().put(roomKey, "roomOwner", room.getRoomOwner());
        stringRedisTemplate.opsForHash().put(roomKey, "roomMaxUser", String.valueOf(room.getRoomMaxUser()));
        stringRedisTemplate.opsForHash().put(roomKey, "roomCurrentUser", String.valueOf(room.getRoomCurrentUser()));
        stringRedisTemplate.opsForHash().put(roomKey, "roomIsPlaying", String.valueOf(room.isRoomIsPlaying()));
        redisTemplate.opsForSet().add(roomKey + ":users", room.getRoomOwner());

        System.out.println("Room created and roomNumber removed from AVAILABLE: " + room);
    }
}
