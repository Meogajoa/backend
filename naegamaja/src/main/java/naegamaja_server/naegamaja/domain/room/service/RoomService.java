package naegamaja_server.naegamaja.domain.room.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import naegamaja_server.naegamaja.domain.chat.repository.CustomRedisChatLogRepository;
import naegamaja_server.naegamaja.domain.room.domain.Room;
import naegamaja_server.naegamaja.domain.room.dto.RoomPageResponse;
import naegamaja_server.naegamaja.domain.room.dto.RoomRequest;
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
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
@Slf4j
public class RoomService {

    private final CustomRedisRoomRepository customRedisRoomRepository;
    private final CustomRedisSessionRepository customRedisSessionRepository;
    private final RedisRoomRepository redisRoomRepository;
    private final RedisSessionRepository redisSessionRepository;
    private final RedissonClient redissonClient;
    private final String userSessionLockKey = "lock:userSession:";

    public void joinRoom(RoomRequest.JoinRoomRequest request, String authorization) {
        final String roomId = request.getId();

        String userNickname = customRedisSessionRepository.getNicknameBySessionId(authorization);
        if (userNickname == null) {
            throw new RestException(ErrorCode.USER_NOT_FOUND);
        }

        if (customRedisRoomRepository.isUserInRoom(userNickname, roomId)) {
            return;
        }

        Room room = redisRoomRepository.findById(roomId)
                .orElseThrow(() -> new RestException(ErrorCode.ROOM_NOT_FOUND));

        if (room.getCurrentUser() >= room.getMaxUser()) {
            throw new RestException(ErrorCode.ROOM_FULL);
        }

        String joinRoomLockKey = "lock:joinRoom:";
        String roomJoinLockKey = joinRoomLockKey + roomId;
        RLock roomJoinLock = redissonClient.getLock(roomJoinLockKey);
        RLock userSessionLock = redissonClient.getLock(userSessionLockKey + authorization);

        boolean isJoinRoomLocked = false;
        boolean isUserSessionLocked = false;

        try {
            isJoinRoomLocked = roomJoinLock.tryLock(10, 10, TimeUnit.SECONDS);
            isUserSessionLocked = userSessionLock.tryLock(10, 10, TimeUnit.SECONDS);

            if (isJoinRoomLocked && isUserSessionLocked) {
                Room currentRoom = redisRoomRepository.findById(roomId)
                        .orElseThrow(() -> new RestException(ErrorCode.ROOM_NOT_FOUND));

                if (currentRoom.getCurrentUser() >= currentRoom.getMaxUser()) {
                    throw new RestException(ErrorCode.ROOM_FULL);
                }
                if (customRedisRoomRepository.isUserInRoom(userNickname, roomId)) {
                    throw new RestException(ErrorCode.ROOM_ALREADY_JOINED);
                }

                customRedisSessionRepository.setUserSessionState(authorization, State.IN_ROOM, roomId);
                customRedisRoomRepository.saveUserToRoom(userNickname, currentRoom);

            } else {
                if (!isJoinRoomLocked) {
                    log.warn("Failed to acquire lock: {}", roomJoinLockKey);
                }
                if (!isUserSessionLocked) {
                    log.warn("Failed to acquire lock: {}", userSessionLockKey + authorization);
                }
                throw new RestException(ErrorCode.LOCK_ACQUIRE_FAILED);
            }
        } catch(RestException e){
            switch(e.getErrorCode()) {
                case ROOM_FULL:
                    throw new RestException(ErrorCode.ROOM_FULL);
                case ROOM_ALREADY_JOINED:
                    throw new RestException(ErrorCode.ROOM_ALREADY_JOINED);
                default:
                    throw new RestException(ErrorCode.INTERNAL_SERVER_ERROR);
            }
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        } finally {
            if (isUserSessionLocked) {
                userSessionLock.unlock();
            }
            if (isJoinRoomLocked) {
                roomJoinLock.unlock();
            }
        }
    }

    public String createRoom(RoomRequest.CreateRoomRequest request, String authorization) {
        String userNickname = customRedisSessionRepository.getNicknameBySessionId(authorization);
        if (userNickname == null) {
            throw new RestException(ErrorCode.USER_NOT_FOUND);
        }

        UserSession userSession = redisSessionRepository.findByNickname(userNickname)
                .orElseThrow(() -> new RestException(ErrorCode.USER_NOT_FOUND));
        if (userSession.isInRoom()) {
            throw new RestException(ErrorCode.USER_ALREADY_IN_ROOM);
        }

        String roomCreateLockKey = "lock:createRoom";
        RLock roomCreateLock = redissonClient.getLock(roomCreateLockKey);
        RLock userSessionLock = redissonClient.getLock(userSessionLockKey + authorization);

        boolean isCreateRoomLocked = false;
        boolean isUserSessionLocked = false;
        String roomNumber = "0";

        try {
            isCreateRoomLocked = roomCreateLock.tryLock(10, 10, TimeUnit.SECONDS);
            isUserSessionLocked = userSessionLock.tryLock(10, 10, TimeUnit.SECONDS);

            if (isCreateRoomLocked && isUserSessionLocked) {
                roomNumber = customRedisRoomRepository.getAvailableRoomNumber();

                UserSession checkSession = redisSessionRepository.findByNickname(userNickname)
                        .orElseThrow(() -> new RestException(ErrorCode.USER_NOT_FOUND));
                if (checkSession.isInRoom()) {
                    throw new RestException(ErrorCode.USER_ALREADY_IN_ROOM);
                }

                customRedisSessionRepository.setUserSessionState(authorization, State.IN_ROOM, roomNumber);

                Room room = Room.builder()
                        .id(String.valueOf(roomNumber))
                        .owner(userNickname)
                        .name(request.getName())
                        .password(request.getPassword())
                        .maxUser(8)
                        .currentUser(1)
                        .isPlaying(false)
                        .isLocked(request.getPassword() != null && !request.getPassword().isEmpty())
                        .build();

                return customRedisRoomRepository.createRoom(room, roomNumber);

            } else {
                if (!isCreateRoomLocked) {
                    log.warn("Failed to acquire lock: {}", roomCreateLockKey);
                }
                if (!isUserSessionLocked) {
                    log.warn("Failed to acquire lock: {}", userSessionLockKey + authorization);
                }
                throw new RestException(ErrorCode.LOCK_ACQUIRE_FAILED);
            }
        } catch(RestException e) {
            switch(e.getErrorCode()) {
                case NO_AVAILABLE_ROOM:
                    throw new RestException(ErrorCode.NO_AVAILABLE_ROOM);
                case INVALID_ROOM_NUMBER:
                    throw new RestException(ErrorCode.INVALID_ROOM_NUMBER);
                default:
                    throw new RestException(ErrorCode.INTERNAL_SERVER_ERROR);
            }

        } catch(InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        } finally{
            if (isUserSessionLocked) {
                userSessionLock.unlock();
            }
            if (isCreateRoomLocked) {
                roomCreateLock.unlock();
            }
        }
    }


    public RoomPageResponse getRooms(int pageNum) {
        return customRedisRoomRepository.getRooms(pageNum);
    }
}
