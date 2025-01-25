package naegamaja_server.naegamaja.domain.game.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import naegamaja_server.naegamaja.domain.session.repository.CustomRedisSessionRepository;
import naegamaja_server.naegamaja.system.websocket.dto.Message;
import naegamaja_server.naegamaja.system.websocket.model.MessageType;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class RedisStreamGameMessagePublisher {

    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;
    private final String ASYNC_STREAM_KEY = "stream:sync:";
    private final CustomRedisSessionRepository customRedisSessionRepository;

    public void publish(Message.GameMQRequest gameMQRequest) {
        try {
//            String nickname = customRedisSessionRepository.getNicknameBySessionId(authorization);
//            String userRoomId = customRedisSessionRepository.getRoomIdBySessionId(authorization);

            //if(userRoomId.isEmpty()) return;

            Map<String, String> messageMap = objectMapper.convertValue(gameMQRequest, new TypeReference<Map<String, String>>() {
            });

            stringRedisTemplate.opsForStream().add(ASYNC_STREAM_KEY, messageMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
