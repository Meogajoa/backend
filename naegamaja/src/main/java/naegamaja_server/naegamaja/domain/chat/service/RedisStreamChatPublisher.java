package naegamaja_server.naegamaja.domain.chat.service;

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
public class RedisStreamChatPublisher {

    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;
    private final String ROOM_CHAT_STREAM_KEY = "stream:room:chat:";
    private final CustomRedisSessionRepository customRedisSessionRepository;

    public void publishChatMessage(String roomId, Message.Request message, String authorization) {
        try {
            if (!MessageType.CHAT.equals(message.getType())) return;
            String nickname = customRedisSessionRepository.getNicknameBySessionId(authorization);
            String userRoomId = customRedisSessionRepository.getRoomIdBySessionId(authorization);

            if(userRoomId.isEmpty()) return;

            Message.MQRequest mqRequest = Message.MQRequest.of(message, userRoomId, nickname);

            Map<String, String> messageMap = objectMapper.convertValue(mqRequest, new TypeReference<Map<String, String>>() {
            });
            stringRedisTemplate.opsForStream().add(ROOM_CHAT_STREAM_KEY + roomId, messageMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
