package naegamaja_server.naegamaja.domain.chat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import naegamaja_server.naegamaja.system.websocket.dto.Message;
import naegamaja_server.naegamaja.system.websocket.model.MessageType;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisStreamChatSubscriber {

    private final StreamMessageListenerContainer<String, MapRecord<String, String, String>> listenerContainer;
    private final ChatLogService chatLogService;
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final String ROOM_CHAT_STREAM_KEY = "stream:room:";
    private final String GROUP_NAME = "chat-group";

    @PostConstruct
    public void startListening() {
        for(int i = 1; i <= 10; i++) {
            String streamKey = ROOM_CHAT_STREAM_KEY + i;

            try {
                redisTemplate.opsForStream().createGroup(streamKey, GROUP_NAME);
            } catch (Exception e) {
                if (!e.getMessage().contains("BUSYGROUP")) {
                    throw e;
                }
            }

            String consumerName = "consumer-" + i;

            Consumer consumer = Consumer.from(GROUP_NAME, consumerName);

            StreamOffset<String> streamOffset = StreamOffset.create(streamKey, ReadOffset.lastConsumed());

            listenerContainer.receive(
                    consumer,
                    streamOffset,
                    this::handleMessage
            );
        }

        listenerContainer.start();
    }

    private void handleMessage(MapRecord<String, String, String> record) {
        Message.MQRequest request = objectMapper.convertValue(record.getValue(), Message.MQRequest.class);

        if (!MessageType.CHAT.equals(request.getType())) return;

        chatLogService.roomChat(request);

        System.out.println("1월 12일 테스트");
        System.out.println(request);

    }
}
