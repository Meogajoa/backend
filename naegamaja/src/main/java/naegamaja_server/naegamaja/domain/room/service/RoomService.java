package naegamaja_server.naegamaja.domain.room.service;

import lombok.RequiredArgsConstructor;
import naegamaja_server.naegamaja.domain.auth.service.RedisAuthService;
import naegamaja_server.naegamaja.domain.room.domain.Room;
import naegamaja_server.naegamaja.domain.room.dto.RoomCreationDto;
import naegamaja_server.naegamaja.domain.room.dto.RoomRequest;
import naegamaja_server.naegamaja.domain.room.dto.RoomResponse;
import naegamaja_server.naegamaja.domain.room.repository.RedisRoomRepository;
import naegamaja_server.naegamaja.domain.session.entity.UserSession;
import naegamaja_server.naegamaja.domain.session.repository.RedisSessionRepository;
import naegamaja_server.naegamaja.domain.session.state.State;
import naegamaja_server.naegamaja.system.exception.model.ErrorCode;
import naegamaja_server.naegamaja.system.exception.model.RestException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RoomService {

    private final RedisRoomRepository redisRoomRepository;
    private final RedisSessionRepository redisSessionRepository;


    public void joinRoom(RoomRequest.JoinRoomRequest request, String authorization) {
        Room room = redisRoomRepository.findById(request.getRoomId());

        if(room.getRoomCurrentUser() == room.getRoomMaxUser()) {
            throw new RestException(ErrorCode.ROOM_FULL);
        }

        redisRoomRepository.saveUserToRoom(authorization, room);
    }

    public Long createRoom(RoomRequest.CreateRoomRequest request, String authorization) {
        Long roomNumber = redisRoomRepository.getAvailableRoomNumber();

        Room room = Room.builder()
                .id(String.valueOf(roomNumber))
                .roomOwner(authorization)
                .roomName(request.getRoomName())
                .roomPassword(request.getRoomPassword())
                .roomMaxUser(request.getRoomMaxUser())
                .roomCurrentUser(1)
                .roomIsPlaying(false)
                .build();

        redisSessionRepository.setUserSession(authorization, State.valueOf("IN_ROOM"), roomNumber);

        redisRoomRepository.createRoom(room, roomNumber);

        return roomNumber;
    }

    public Page<RoomResponse> getRooms(int pageNum) {
        return redisRoomRepository.getRooms(pageNum);
    }




}
