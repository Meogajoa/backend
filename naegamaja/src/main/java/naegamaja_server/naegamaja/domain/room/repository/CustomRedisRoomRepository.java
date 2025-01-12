package naegamaja_server.naegamaja.domain.room.repository;

import lombok.RequiredArgsConstructor;
import naegamaja_server.naegamaja.domain.room.domain.Room;
import naegamaja_server.naegamaja.domain.room.dto.RoomPageResponse;
import naegamaja_server.naegamaja.domain.room.dto.RoomResponse;
import naegamaja_server.naegamaja.system.exception.model.ErrorCode;
import naegamaja_server.naegamaja.system.exception.model.RestException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class CustomRedisRoomRepository {
    private final StringRedisTemplate stringRedisTemplate;

    private static final String ROOM_KEY_PREFIX = "room:";
    private static final String AVAILABLE_ROOM_LIST_KEY = "availableRoomList:";

    private static final String USING_ROOM_LIST_KEY = "usingRoomList:";

    public void saveUserToRoom(String nickname, Room room) {
        String userKey = ROOM_KEY_PREFIX + room.getId() + ":users";
        String roomKey = ROOM_KEY_PREFIX + room.getId();
        stringRedisTemplate.opsForSet().add(userKey, nickname);
        stringRedisTemplate.opsForHash().put(roomKey, "currentUser", String.valueOf(room.getCurrentUser() + 1));
    }

    public boolean isAlreadyExistRoom(String roomId) {
        return roomId != null && stringRedisTemplate.hasKey(ROOM_KEY_PREFIX + roomId);
    }

    public boolean isUserInRoom(String nickname, Long roomNumber) {
        String roomKey = ROOM_KEY_PREFIX + roomNumber + ":users";
        return Boolean.TRUE.equals(stringRedisTemplate.opsForSet().isMember(roomKey, nickname));
    }

    public Long getAvailableRoomNumber() {
        Set<String> result = stringRedisTemplate.opsForZSet().range(AVAILABLE_ROOM_LIST_KEY, 0, 0);

        assert result != null;
        if(result.isEmpty()) {
            throw new RestException(ErrorCode.NO_AVAILABLE_ROOM);
        }



        String roomNumberStr = result.iterator().next();
        try {
            return Long.parseLong(roomNumberStr);
        } catch (NumberFormatException e) {
            throw new RestException(ErrorCode.INVALID_ROOM_NUMBER);
        }
    }

    public void createRoom(Room room, Long roomNumber) {
        String roomNumberStr = String.valueOf(roomNumber);

        // AVAILABLE ZSet에서 roomNumber 제거
        Long removed = stringRedisTemplate.opsForZSet().remove(AVAILABLE_ROOM_LIST_KEY, roomNumberStr);
        if (removed == null || removed == 0) {
            throw new RestException(ErrorCode.FAILED_TO_REMOVE_AVAILABLE_ROOM);
        }

        //차후 동시성 문제 고려 필요
        stringRedisTemplate.opsForZSet().add(USING_ROOM_LIST_KEY, roomNumberStr, roomNumber);

        String roomKey = ROOM_KEY_PREFIX + room.getId();
        boolean isLocked = room.getPassword() != null && !room.getPassword().isEmpty();
        stringRedisTemplate.opsForHash().put(roomKey, "id", room.getId());
        stringRedisTemplate.opsForHash().put(roomKey, "name", room.getName());
        stringRedisTemplate.opsForHash().put(roomKey, "password", room.getPassword());
        stringRedisTemplate.opsForHash().put(roomKey, "owner", room.getOwner());
        stringRedisTemplate.opsForHash().put(roomKey, "maxUser", String.valueOf(room.getMaxUser()));
        stringRedisTemplate.opsForHash().put(roomKey, "currentUser", String.valueOf(room.getCurrentUser()));
        stringRedisTemplate.opsForHash().put(roomKey, "isLocked", String.valueOf(room.isLocked()));
        stringRedisTemplate.opsForHash().put(roomKey, "isPlaying", String.valueOf(room.isPlaying()));
        stringRedisTemplate.opsForSet().add(roomKey + ":users", room.getOwner());

        System.out.println("Room created and roomNumber removed from AVAILABLE: " + room);
    }


    public RoomPageResponse getRooms(int pageNum) {

        int page = (pageNum <= 0) ? 0 : pageNum - 1;
        int size = 10;

        long start = (long) page * size;
        long end = start + size - 1;

        Set<String> roomNumbers = stringRedisTemplate.opsForZSet()
                .range(USING_ROOM_LIST_KEY, start, end);
        if (roomNumbers == null) {
            roomNumbers = Collections.emptySet();
        }

        List<RoomResponse> roomResponses = new ArrayList<>();
        for (String roomNumber : roomNumbers) {
            String roomKey = ROOM_KEY_PREFIX + roomNumber;
            Map<Object, Object> roomMap = stringRedisTemplate.opsForHash().entries(roomKey);
            if (roomMap != null && !roomMap.isEmpty()) {
                Room room = mapToRoom(roomMap);
                RoomResponse response = RoomResponse.from(room);
                roomResponses.add(response);
            }
        }

        Long total = stringRedisTemplate.opsForZSet().size(USING_ROOM_LIST_KEY);
        if(total == null) {
            total = 0L;
        }

        boolean isLast = (start + size) >= total;

        PageRequest pageRequest = PageRequest.of(page, size);
        return new RoomPageResponse(roomResponses, isLast);
    }

    private Room mapToRoom(Map<Object, Object> roomData) {
        Room room = new Room();
        room.setId((String) roomData.get("id"));
        room.setName((String) roomData.get("name"));
        room.setPassword((String) roomData.get("password"));
        room.setOwner((String) roomData.get("owner"));
        room.setMaxUser(Integer.parseInt((String) roomData.get("maxUser")));
        room.setCurrentUser(Integer.parseInt((String) roomData.get("currentUser")));
        room.setPlaying(Boolean.parseBoolean((String) roomData.get("isPlaying")));
        room.setLocked(Boolean.parseBoolean((String) roomData.get("isLocked")));
        return room;
    }

    public List<String> getUserNicknameInRoom(Long roomNumber) {
        String roomKey = ROOM_KEY_PREFIX + roomNumber + ":users";
        return new ArrayList<>(stringRedisTemplate.opsForSet().members(roomKey));
    }

    public List<String> getUserSessionIdInRoom(Long roomNumber) {
        List<String> nicknames = getUserNicknameInRoom(roomNumber);
        List<String> sessionIds = new ArrayList<>();
        for (String nickname : nicknames) {
            String sessionId = stringRedisTemplate.opsForValue().get("nicknameToSessionId:" + nickname);
            if (sessionId != null) {
                sessionIds.add(sessionId);
            }
        }
        return sessionIds;
    }
}
