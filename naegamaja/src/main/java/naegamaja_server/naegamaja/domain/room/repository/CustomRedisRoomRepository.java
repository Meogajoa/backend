package naegamaja_server.naegamaja.domain.room.repository;

import lombok.RequiredArgsConstructor;
import naegamaja_server.naegamaja.domain.room.domain.Room;
import naegamaja_server.naegamaja.domain.room.dto.RoomResponse;
import naegamaja_server.naegamaja.system.exception.model.ErrorCode;
import naegamaja_server.naegamaja.system.exception.model.RestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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

    public Room findById(String roomId) {
        String roomKey = ROOM_KEY_PREFIX + roomId;
        String id = (String) stringRedisTemplate.opsForHash().get(roomKey, "id");
        String roomName = (String) stringRedisTemplate.opsForHash().get(roomKey, "roomName");
        String roomPassword = (String) stringRedisTemplate.opsForHash().get(roomKey, "roomPassword");
        String roomOwner = (String) stringRedisTemplate.opsForHash().get(roomKey, "roomOwner");
        String roomMaxUserStr = (String) stringRedisTemplate.opsForHash().get(roomKey, "roomMaxUser");
        String roomCurrentUserStr = (String) stringRedisTemplate.opsForHash().get(roomKey, "roomCurrentUser");
        String roomIsPlayingStr = (String) stringRedisTemplate.opsForHash().get(roomKey, "roomIsPlaying");

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
        String userKey = ROOM_KEY_PREFIX + room.getId() + ":users";
        String roomKey = ROOM_KEY_PREFIX + room.getId();
        stringRedisTemplate.opsForSet().add(userKey, nickname);
        stringRedisTemplate.opsForHash().put(roomKey, "roomCurrentUser", String.valueOf(room.getRoomCurrentUser() + 1));
    }

    public boolean isAlreadyExistRoom(String roomId) {
        return roomId != null && stringRedisTemplate.hasKey(ROOM_KEY_PREFIX + roomId);
    }

    public boolean isUserInRoom(String nickname, Long roomNumber) {
        String roomKey = ROOM_KEY_PREFIX + roomNumber + ":users";
        return Boolean.TRUE.equals(stringRedisTemplate.opsForSet().isMember(roomKey, nickname));
    }

    public void removeUserFromRoom(String authorization, Room room) {
        String userKey = ROOM_KEY_PREFIX + room.getId() + ":users";
        String roomKey = ROOM_KEY_PREFIX + room.getId();
        stringRedisTemplate.opsForSet().remove(userKey, authorization);
        stringRedisTemplate.opsForHash().put(roomKey, "roomCurrentUser", String.valueOf(room.getRoomCurrentUser() - 1));
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

        // Room 정보 저장
        String roomKey = ROOM_KEY_PREFIX + room.getId();
        stringRedisTemplate.opsForHash().put(roomKey, "id", room.getId());
        stringRedisTemplate.opsForHash().put(roomKey, "roomName", room.getRoomName());
        stringRedisTemplate.opsForHash().put(roomKey, "roomPassword", room.getRoomPassword());
        stringRedisTemplate.opsForHash().put(roomKey, "roomOwner", room.getRoomOwner());
        stringRedisTemplate.opsForHash().put(roomKey, "roomMaxUser", String.valueOf(room.getRoomMaxUser()));
        stringRedisTemplate.opsForHash().put(roomKey, "roomCurrentUser", String.valueOf(room.getRoomCurrentUser()));
        stringRedisTemplate.opsForHash().put(roomKey, "roomIsPlaying", String.valueOf(room.isRoomIsPlaying()));
        stringRedisTemplate.opsForSet().add(roomKey + ":users", room.getRoomOwner());

        System.out.println("Room created and roomNumber removed from AVAILABLE: " + room);
    }


    public Page<RoomResponse> getRooms(int pageNum) {

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

        long total = stringRedisTemplate.opsForZSet().zCard(USING_ROOM_LIST_KEY);

        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(roomResponses, pageRequest, total);
    }

    private Room mapToRoom(Map<Object, Object> roomData) {
        Room room = new Room();
        room.setId((String) roomData.get("id"));
        room.setRoomName((String) roomData.get("roomName"));
        room.setRoomPassword((String) roomData.get("roomPassword"));
        room.setRoomOwner((String) roomData.get("roomOwner"));
        room.setRoomMaxUser(Integer.parseInt((String) roomData.get("roomMaxUser")));
        room.setRoomCurrentUser(Integer.parseInt((String) roomData.get("roomCurrentUser")));
        room.setRoomIsPlaying(Boolean.parseBoolean((String) roomData.get("roomIsPlaying")));
        return room;
    }

    public List<String> getUserSessionIdInRoom(Long roomNumber) {
        String roomKey = ROOM_KEY_PREFIX + roomNumber + ":users";
        return new ArrayList<>(stringRedisTemplate.opsForSet().members(roomKey));
    }
}
