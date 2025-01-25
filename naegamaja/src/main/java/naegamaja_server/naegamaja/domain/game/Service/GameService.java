package naegamaja_server.naegamaja.domain.game.Service;

import lombok.RequiredArgsConstructor;
import naegamaja_server.naegamaja.domain.room.service.RoomService;
import naegamaja_server.naegamaja.domain.session.service.SessionService;
import naegamaja_server.naegamaja.system.exception.model.ErrorCode;
import naegamaja_server.naegamaja.system.exception.model.RestException;
import naegamaja_server.naegamaja.system.websocket.dto.Message;
import naegamaja_server.naegamaja.system.websocket.model.MessageType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameService {

    private final RoomService roomService;
    private final SessionService sessionService;
    private final RedisStreamGameMessagePublisher redisStreamGameMessagePublisher;


    public void startGame(String gameId, String sessionId) {
        if(!roomService.getRoomOwner(gameId).equals(sessionService.getNicknameBySessionId(sessionId))) {
            throw new RestException(ErrorCode.NOT_ROOM_OWNER);
        }

        Message.GameMQRequest gameMQRequest = Message.GameMQRequest.builder()
                .type(MessageType.GAME_START)
                .gameId(gameId)
                .build();

        redisStreamGameMessagePublisher.publish(gameMQRequest);
    }
}
