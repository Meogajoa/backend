package naegamaja_server.naegamaja.domain.game.Service;

import lombok.RequiredArgsConstructor;
import naegamaja_server.naegamaja.domain.room.repository.CustomRedisRoomRepository;
import naegamaja_server.naegamaja.domain.room.service.RoomService;
import naegamaja_server.naegamaja.domain.session.repository.CustomRedisSessionRepository;
import naegamaja_server.naegamaja.domain.session.service.SessionService;
import naegamaja_server.naegamaja.system.exception.model.ErrorCode;
import naegamaja_server.naegamaja.system.exception.model.RestException;
import naegamaja_server.naegamaja.system.websocket.dto.NaegamajaMessage;
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

    public void startGame(String gameId, String sessionId) {
        if(!roomService.getRoomOwner(gameId).equals(sessionService.getNicknameBySessionId(sessionId))) {
            throw new RestException(ErrorCode.NOT_ROOM_OWNER);
        }

        NaegamajaMessage.GameMQRequest gameMQRequest = NaegamajaMessage.GameMQRequest.builder()
                .type(MessageType.GAME_START)
                .sender(customRedisSessionRepository.getNicknameBySessionId(sessionId))
                .gameId(gameId)
                .build();

        System.out.println("로직 서버로 메시지 날렸음");

        redisStreamGameMessagePublisher.publish(gameMQRequest);
    }

    public void test(String gameId, String sessionId, NaegamajaMessage.Request message) {
        String nickname = customRedisSessionRepository.getNicknameBySessionId(sessionId);
        if(!customRedisRoomRepository.isUserInRoom(nickname, gameId)){
            return;
        }

        NaegamajaMessage.GameMQRequest gameMQRequest = NaegamajaMessage.GameMQRequest.builder()
                .type(message.getType())
                .sender(nickname)
                .gameId(gameId)
                .content(message.getContent())
                .build();

        redisStreamGameMessagePublisher.publish(gameMQRequest);

    }
}
