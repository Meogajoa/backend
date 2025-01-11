package naegamaja_server.naegamaja.domain.room.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.domain.Page;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
@Slf4j
public class RoomService {

    private final CustomRedisRoomRepository customRedisRoomRepository;
    private final CustomRedisSessionRepository customRedisSessionRepository;
    private final RedisRoomRepository redisRoomRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final CustomRedisChatLogRepository customRedisChatLogRepository;
    private final RedisSessionRepository redisSessionRepository;
    private final RedissonClient redissonClient;
    private final String userSessionLockKey = "lock:userSession:";

    public void joinRoom(RoomRequest.JoinRoomRequest request, String authorization) {
        String roomJoinLockKey = "lock:joinRoom:" + request.getRoomId();
        RLock roomJoinLock = redissonClient.getLock(roomJoinLockKey);
        RLock userSessionLock = redissonClient.getLock(userSessionLockKey + authorization);
        boolean isJoinRoomLocked = false;
        boolean isUserSessionLocked = false;
        Long roomNumber = Long.parseLong(request.getRoomId());

        try {
            isJoinRoomLocked = roomJoinLock.tryLock(10, 10, TimeUnit.SECONDS);
            isUserSessionLocked = userSessionLock.tryLock(10, 10, TimeUnit.SECONDS);

            if (isJoinRoomLocked && isUserSessionLocked) {
                Room room = redisRoomRepository.findById(request.getRoomId())
                        .orElseThrow(() -> new RestException(ErrorCode.ROOM_NOT_FOUND));

                if (room.getRoomCurrentUser() >= room.getRoomMaxUser()) {
                    throw new RestException(ErrorCode.ROOM_FULL);
                }

                String userNickname = customRedisSessionRepository.getNicknameBySessionId(authorization);

                if (customRedisRoomRepository.isUserInRoom(userNickname, Long.parseLong(request.getRoomId()))) {
                    throw new RestException(ErrorCode.ROOM_ALREADY_JOINED);
                }

                customRedisSessionRepository.setUserSessionState(authorization, State.valueOf("IN_ROOM"), roomNumber);
                customRedisRoomRepository.saveUserToRoom(userNickname, room);

            } else {
                if (!isJoinRoomLocked) {
                    log.warn("Failed to acquire lock with key {}", roomJoinLockKey);
                }
                if (!isUserSessionLocked) {
                    log.warn("Failed to acquire lock with key {}", userSessionLockKey + authorization);
                }
                throw new RestException(ErrorCode.LOCK_ACQUIRE_FAILED);
            }

        } catch (Exception e) {
            throw new RestException(ErrorCode.USER_ALREADY_IN_ROOM);
        } finally {
            if (isUserSessionLocked) {
                userSessionLock.unlock();
            }
            if (isJoinRoomLocked) {
                roomJoinLock.unlock();
            }
        }
    }

    public void createRoom(RoomRequest.CreateRoomRequest request, String authorization) {
        String roomCreateLockKey = "lock:createRoom";
        RLock roomCreateLock = redissonClient.getLock(roomCreateLockKey);
        RLock userSessionLock = redissonClient.getLock(userSessionLockKey + authorization);
        boolean isCreateRoomLocked = false;
        boolean isUserSessionLocked = false;
        Long roomNumber = 0L;
        String userNickname = customRedisSessionRepository.getNicknameBySessionId(authorization);

        try {
            isCreateRoomLocked = roomCreateLock.tryLock(10, 10, TimeUnit.SECONDS);
            isUserSessionLocked = userSessionLock.tryLock(10, 10, TimeUnit.SECONDS);

            if (isCreateRoomLocked && isUserSessionLocked) {
                UserSession userSession = redisSessionRepository.findByNickname(userNickname)
                        .orElseThrow(() -> new RestException(ErrorCode.USER_NOT_FOUND));

                if (userSession.isInRoom()) {
                    throw new RestException(ErrorCode.USER_ALREADY_IN_ROOM);
                }

                roomNumber = customRedisRoomRepository.getAvailableRoomNumber();
                customRedisSessionRepository.setUserSessionState(authorization, State.IN_ROOM, roomNumber);

                Room room = Room.builder()
                        .id(String.valueOf(roomNumber))
                        .roomOwner(userNickname)
                        .roomName(request.getRoomName())
                        .roomPassword(request.getRoomPassword())
                        .roomMaxUser(8)
                        .roomCurrentUser(1)
                        .roomIsPlaying(false)
                        .build();

                customRedisRoomRepository.createRoom(room, roomNumber);

            } else {
                if (!isCreateRoomLocked) {
                    log.warn("Failed to acquire lock with key {}", roomCreateLockKey);
                }
                if (!isUserSessionLocked) {
                    log.warn("Failed to acquire lock with key {}", userSessionLockKey + authorization);
                }
                throw new RestException(ErrorCode.LOCK_ACQUIRE_FAILED);
            }

        } catch (Exception e) {
            throw new RestException(ErrorCode.USER_ALREADY_IN_ROOM);
        } finally {
            if (isUserSessionLocked) {
                userSessionLock.unlock();
            }
            if (isCreateRoomLocked) {
                roomCreateLock.unlock();
            }
        }
    }

    public List<RoomResponse> getRooms(int pageNum) {
        return customRedisRoomRepository.getRooms(pageNum);
    }
}
