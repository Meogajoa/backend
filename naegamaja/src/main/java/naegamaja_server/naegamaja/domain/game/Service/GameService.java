package naegamaja_server.naegamaja.domain.game.Service;

import lombok.RequiredArgsConstructor;
import naegamaja_server.naegamaja.domain.chat.service.RedisStreamChatPublisher;
import naegamaja_server.naegamaja.domain.room.repository.CustomRedisRoomRepository;
import naegamaja_server.naegamaja.domain.room.service.RoomService;
import naegamaja_server.naegamaja.domain.session.repository.CustomRedisSessionRepository;
import naegamaja_server.naegamaja.domain.session.service.SessionService;
import naegamaja_server.naegamaja.system.exception.model.ErrorCode;
import naegamaja_server.naegamaja.system.exception.model.RestException;
import naegamaja_server.naegamaja.system.websocket.dto.MeogajoaMessage;
import naegamaja_server.naegamaja.system.websocket.model.MessageType;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameService {

    private final RoomService roomService;
    private final SessionService sessionService;
    private final RedisStreamGameMessagePublisher redisStreamGameMessagePublisher;
    private final CustomRedisSessionRepository customRedisSessionRepository;
    private final CustomRedisRoomRepository customRedisRoomRepository;
    private final RedisStreamChatPublisher redisStreamChatPublisher;

    public void startGame(String gameId, String sessionId) {
        if(!roomService.getRoomOwner(gameId).equals(sessionService.getNicknameBySessionId(sessionId))) {
            throw new RestException(ErrorCode.NOT_ROOM_OWNER);
        }

        MeogajoaMessage.GameMQRequest gameMQRequest = MeogajoaMessage.GameMQRequest.builder()
                .type(MessageType.GAME_START)
                .sender(customRedisSessionRepository.getNicknameBySessionId(sessionId))
                .gameId(gameId)
                .build();

        System.out.println("로직 서버로 메시지 날렸음");

        redisStreamGameMessagePublisher.syncPublish(gameMQRequest);
    }

    public void test(String gameId, String sessionId, MeogajoaMessage.Request message) {
        String nickname = customRedisSessionRepository.getNicknameBySessionId(sessionId);
        if(!customRedisRoomRepository.isUserInRoom(nickname, gameId)){
            return;
        }

        MeogajoaMessage.GameMQRequest gameMQRequest = MeogajoaMessage.GameMQRequest.builder()
                .type(message.getType())
                .sender(nickname)
                .gameId(gameId)
                .content(message.getContent())
                .build();

        redisStreamGameMessagePublisher.syncPublish(gameMQRequest);

    }

    public void buttonClick(String gameId, String authorization, MeogajoaMessage.Request message) {
        String nickname = customRedisSessionRepository.getNicknameBySessionId(authorization);
        if(!customRedisRoomRepository.isUserInRoom(nickname, gameId)){
            return;
        }

        MeogajoaMessage.GameMQRequest gameMQRequest = MeogajoaMessage.GameMQRequest.builder()
                .type(MessageType.BUTTON_CLICK)
                .sender(nickname)
                .gameId(gameId)
                .content(message.getContent())
                .build();

        redisStreamGameMessagePublisher.syncPublish(gameMQRequest);
    }

    public void userChat(String id, Long number, String authorization, MeogajoaMessage.Request message) {
        if(!customRedisSessionRepository.isValidSessionId(authorization)){
            //System.out.println("진입점 -1");
            return;
        }

        String nickname = customRedisSessionRepository.getNicknameBySessionId(authorization);
        if(!customRedisRoomRepository.isUserInRoom(nickname, id)){
            //System.out.println("진입점 0");
            return;
        }

        if(!customRedisRoomRepository.isPlaying(id)){
            return;
        }

        //System.out.println("진입점 1");
        redisStreamChatPublisher.publishGameChatToUser(id, number, message, authorization);
    }

    public void blackChat(String id, String authorization, MeogajoaMessage.Request message) {
        if(!customRedisSessionRepository.isValidSessionId(authorization)){
            return;
        }

        String nickname = customRedisSessionRepository.getNicknameBySessionId(authorization);
        if(!customRedisRoomRepository.isUserInRoom(nickname, id)){
            return;
        }

        if(!customRedisRoomRepository.isPlaying(id)){
            return;
        }

        redisStreamChatPublisher.publishBlackChat(id, message, authorization);
    }

    public void whiteChat(String id, String authorization, MeogajoaMessage.Request message) {
        if(!customRedisSessionRepository.isValidSessionId(authorization)){
            return;
        }

        String nickname = customRedisSessionRepository.getNicknameBySessionId(authorization);
        if(!customRedisRoomRepository.isUserInRoom(nickname, id)){
            return;
        }

        if(!customRedisRoomRepository.isPlaying(id)){
            return;
        }

        redisStreamChatPublisher.publishWhiteChat(id, message, authorization);
    }

    public void eliminatedChat(String id, String authorization, MeogajoaMessage.Request message) {
        if(!customRedisSessionRepository.isValidSessionId(authorization)){
            return;
        }

        String nickname = customRedisSessionRepository.getNicknameBySessionId(authorization);
        if(!customRedisRoomRepository.isUserInRoom(nickname, id)){
            return;
        }

        if(!customRedisRoomRepository.isPlaying(id)){
            return;
        }

        redisStreamChatPublisher.publishEliminatedChat(id, message, authorization);
    }
}
