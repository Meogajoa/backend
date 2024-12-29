package naegamaja_server.naegamaja.domain.room.service;

import lombok.RequiredArgsConstructor;
import naegamaja_server.naegamaja.domain.room.domain.Room;
import naegamaja_server.naegamaja.domain.room.dto.RoomCreationDto;
import naegamaja_server.naegamaja.domain.room.dto.RoomRequest;
import naegamaja_server.naegamaja.domain.room.dto.RoomResponse;
import naegamaja_server.naegamaja.domain.room.repository.RedisRoomRepository;
import naegamaja_server.naegamaja.system.exception.model.ErrorCode;
import naegamaja_server.naegamaja.system.exception.model.RestException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RoomService {

    private final RedisRoomRepository redisRoomRepository;

    public void joinRoom(RoomRequest.JoinRoomRequest request) {
        Room room = redisRoomRepository.findById(request.getRoomId());

        if(room.getRoomCurrentUser() == room.getRoomMaxUser()) {
            throw new RestException(ErrorCode.ROOM_FULL);
        }

        redisRoomRepository.saveUserToRoom(request.getNickname(), room);
    }

    public boolean isAlreadyExistRoom(String roomId) {
        return redisRoomRepository.isAlreadyExistRoom(roomId);
    }

    public int createRoom(RoomRequest.CreateRoomRequest request) {
        int roomNumber = redisRoomRepository.getAvailableRoomNumber();

        Room room = Room.builder()
                .id(String.valueOf(roomNumber))
                .roomOwner(request.getNickname())
                .roomName(request.getRoomName())
                .roomPassword(request.getRoomPassword())
                .roomMaxUser(request.getRoomMaxUser())
                .roomCurrentUser(1)
                .roomIsPlaying(false)
                .build();

        redisRoomRepository.createRoom(room, roomNumber);

        return roomNumber;
    }




}
