package naegamaja_server.naegamaja.domain.chat.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import naegamaja_server.naegamaja.domain.session.repository.CustomRedisSessionRepository;
import naegamaja_server.naegamaja.system.websocket.dto.MeogajoaMessage;
import naegamaja_server.naegamaja.system.websocket.model.MessageType;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class RedisStreamChatPublisher {

    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;
    private final String ASYNC_STREAM_KEY = "stream:async:";
    private final CustomRedisSessionRepository customRedisSessionRepository;

    public void publishRoomChatMessage(String roomId, MeogajoaMessage.Request message, String authorization) {
        try {
            if (!MessageType.CHAT.equals(message.getType())) return;
            String nickname = customRedisSessionRepository.getNicknameBySessionId(authorization);
            String userRoomId = customRedisSessionRepository.getRoomIdBySessionId(authorization);

            if(userRoomId.isEmpty()) return;

            MeogajoaMessage.ChatMQRequest chatMqRequest = MeogajoaMessage.ChatMQRequest.of(message, userRoomId, nickname);

            Map<String, String> messageMap = objectMapper.convertValue(chatMqRequest, new TypeReference<Map<String, String>>() {
            });

            messageMap.put("type", "ROOM_CHAT");

            stringRedisTemplate.opsForStream().add(ASYNC_STREAM_KEY, messageMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void publishGameChatMessage(String gameId, MeogajoaMessage.Request message, String authorization) {
        try {
            if (!MessageType.CHAT.equals(message.getType())) return;
            String nickname = customRedisSessionRepository.getNicknameBySessionId(authorization);
            String userRoomId = customRedisSessionRepository.getRoomIdBySessionId(authorization);

            if(userRoomId.isEmpty()) return;

            MeogajoaMessage.ChatMQRequest chatMqRequest = MeogajoaMessage.ChatMQRequest.of(message, userRoomId, nickname);

            Map<String, String> messageMap = objectMapper.convertValue(chatMqRequest, new TypeReference<Map<String, String>>() {
            });

            messageMap.put("type", "GAME_CHAT");

            stringRedisTemplate.opsForStream().add(ASYNC_STREAM_KEY, messageMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
