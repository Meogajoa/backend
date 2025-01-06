package naegamaja_server.naegamaja.domain.room.service;

import lombok.RequiredArgsConstructor;
import naegamaja_server.naegamaja.domain.chat.entity.ChatLog;
import naegamaja_server.naegamaja.domain.chat.repository.CustomRedisChatLogRepository;
import naegamaja_server.naegamaja.domain.room.domain.Room;
import naegamaja_server.naegamaja.domain.room.dto.RoomRequest;
import naegamaja_server.naegamaja.domain.room.dto.RoomResponse;
import naegamaja_server.naegamaja.domain.room.repository.CustomRedisRoomRepository;
import naegamaja_server.naegamaja.domain.room.repository.RedisRoomRepository;
import naegamaja_server.naegamaja.domain.session.entity.UserSession;
import naegamaja_server.naegamaja.domain.session.repository.CustomRedisSessionRepository;
import naegamaja_server.naegamaja.domain.session.repository.RedisSessionRepository;
import naegamaja_server.naegamaja.domain.session.state.State;
import naegamaja_server.naegamaja.system.exception.model.ErrorCode;
import naegamaja_server.naegamaja.system.exception.model.RestException;
import naegamaja_server.naegamaja.system.websocket.dto.Message;
import org.springframework.data.domain.Page;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RoomService {

    private final CustomRedisRoomRepository customRedisRoomRepository;
    private final CustomRedisSessionRepository customRedisSessionRepository;
    private final RedisRoomRepository redisRoomRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final CustomRedisChatLogRepository customRedisChatLogRepository;
    private final RedisSessionRepository redisSessionRepository;


    public void joinRoom(RoomRequest.JoinRoomRequest request, String authorization) {
        Room room = redisRoomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new RestException(ErrorCode.ROOM_NOT_FOUND));

        if(room.getRoomCurrentUser() == room.getRoomMaxUser()) {
            throw new RestException(ErrorCode.ROOM_FULL);
        }

        String userNickname = customRedisSessionRepository.getUserNickname(authorization);

        if(customRedisRoomRepository.isUserInRoom(userNickname, Long.parseLong(request.getRoomId()))) {
            throw new RestException(ErrorCode.ROOM_ALREADY_JOINED);
        }

        Long roomNumber = Long.parseLong(request.getRoomId());
        customRedisSessionRepository.setUserSessionState(authorization, State.valueOf("IN_ROOM"), roomNumber);

        customRedisRoomRepository.saveUserToRoom(userNickname, room);
    }

    public RoomResponse createRoom(RoomRequest.CreateRoomRequest request, String authorization) {
        String userNickname = customRedisSessionRepository.getUserNickname(authorization);

        UserSession userSession = redisSessionRepository.findByNickname(userNickname)
                .orElseThrow(() -> new RestException(ErrorCode.USER_NOT_FOUND));

        if(userSession.isInRoom()) {
            throw new RestException(ErrorCode.USER_ALREADY_IN_ROOM);
        }

        Long roomNumber = customRedisRoomRepository.getAvailableRoomNumber();

        Room room = Room.builder()
                .id(String.valueOf(roomNumber))
                .roomOwner(userNickname)
                .roomName(request.getRoomName())
                .roomPassword(request.getRoomPassword())
                .roomMaxUser(8)
                .roomCurrentUser(1)
                .roomIsPlaying(false)
                .build();

        customRedisSessionRepository.setUserSessionState(authorization, State.valueOf("IN_ROOM"), roomNumber);

        customRedisRoomRepository.createRoom(room, roomNumber);
        //redisRoomRepository.save(room);

        return RoomResponse.of(roomNumber);
    }

    public Page<RoomResponse> getRooms(int pageNum) {
        return customRedisRoomRepository.getRooms(pageNum);
    }



}
