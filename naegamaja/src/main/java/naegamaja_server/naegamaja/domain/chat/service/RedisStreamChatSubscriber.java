package naegamaja_server.naegamaja.domain.chat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import naegamaja_server.naegamaja.system.websocket.dto.Message;
import naegamaja_server.naegamaja.system.websocket.model.MessageType;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.StreamReceiver;
import org.springframework.data.redis.stream.Subscription;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RedisStreamChatSubscriber {

    private final StreamMessageListenerContainer<String, MapRecord<String, Object, Object>> container;
    private final ObjectMapper objectMapper;
    private final String ROOM_CHAT_STREAM_KEY = "stream:chat:room:";

    @PostConstruct
    public void startListenChatStream() {

        String roomId = "room1";

        Subscription subscription = container.receive(
                StreamOffset.create(ROOM_CHAT_STREAM_KEY + roomId, ReadOffset.latest()),
                (MapRecord<String, Object, Object> message) -> {

                    Map<Object, Object> value = message.getValue();

                    Message.Request chatMessage = objectMapper.convertValue(value, Message.Request.class);

                    if(chatMessage.getType() == MessageType.CHAT) {
                        System.out.println("새로운 채팅 메시지 수신: " + chatMessage.getContent());
                    }
                }
        );

    }
}
