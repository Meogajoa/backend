package naegamaja_server.naegamaja.domain.room.service;

import lombok.RequiredArgsConstructor;
import naegamaja_server.naegamaja.domain.room.domain.Room;
import naegamaja_server.naegamaja.domain.room.dto.RoomRequest;
import naegamaja_server.naegamaja.domain.room.dto.RoomResponse;
import naegamaja_server.naegamaja.domain.room.repository.CustomRedisRoomRepository;
import naegamaja_server.naegamaja.domain.room.repository.RedisRoomRepository;
import naegamaja_server.naegamaja.domain.session.repository.CustomRedisSessionRepository;
import naegamaja_server.naegamaja.domain.session.state.State;
import naegamaja_server.naegamaja.system.exception.model.ErrorCode;
import naegamaja_server.naegamaja.system.exception.model.RestException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RoomService {

    private final CustomRedisRoomRepository customRedisRoomRepository;
    private final CustomRedisSessionRepository customRedisSessionRepository;
    private final RedisRoomRepository redisRoomRepository;


    public void joinRoom(RoomRequest.JoinRoomRequest request, String authorization) {
        Room room = redisRoomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new RestException(ErrorCode.ROOM_NOT_FOUND));

        if(room.getRoomCurrentUser() == room.getRoomMaxUser()) {
            throw new RestException(ErrorCode.ROOM_FULL);
        }

        Long roomNumber = Long.parseLong(request.getRoomId());
        customRedisSessionRepository.setUserSessionState(authorization, State.valueOf("IN_ROOM"), roomNumber);

        customRedisRoomRepository.saveUserToRoom(authorization, room);
    }

    public Long createRoom(RoomRequest.CreateRoomRequest request, String authorization) {
        Long roomNumber = customRedisRoomRepository.getAvailableRoomNumber();

        Room room = Room.builder()
                .id(String.valueOf(roomNumber))
                .roomOwner(authorization)
                .roomName(request.getRoomName())
                .roomPassword(request.getRoomPassword())
                .roomMaxUser(request.getRoomMaxUser())
                .roomCurrentUser(1)
                .roomIsPlaying(false)
                .build();

        customRedisSessionRepository.setUserSessionState(authorization, State.valueOf("IN_ROOM"), roomNumber);

        customRedisRoomRepository.createRoom(room, roomNumber);
        //redisRoomRepository.save(room);

        return roomNumber;
    }

    public Page<RoomResponse> getRooms(int pageNum) {
        return customRedisRoomRepository.getRooms(pageNum);
    }




}
