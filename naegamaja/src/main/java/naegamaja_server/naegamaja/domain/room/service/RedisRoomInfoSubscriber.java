package naegamaja_server.naegamaja.domain.room.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import naegamaja_server.naegamaja.domain.room.dto.RoomUserInfo;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static java.lang.Thread.sleep;

@Service
@RequiredArgsConstructor
public class RedisRoomInfoSubscriber {

    private final StreamMessageListenerContainer<String, MapRecord<String, String, String>> listenerContainer;
    private final StompRoomService stompRoomService;
    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;
    private final String ROOM_INFO_STREAM_KEY = "stream:room:info:";
    private final String GROUP_NAME = "roomInfo-group";

    @PostConstruct
    public void startListening() {
        String streamKey = ROOM_INFO_STREAM_KEY;

        try {
            stringRedisTemplate.opsForStream().createGroup(streamKey, GROUP_NAME);
        } catch (Exception e) {
            if (!e.getMessage().contains("BUSYGROUP")) {
                throw e;
            }
        }

        String consumerName = "Consumer";

        Consumer consumer = Consumer.from(GROUP_NAME, consumerName);

        StreamOffset<String> streamOffset = StreamOffset.create(streamKey, ReadOffset.lastConsumed());

        listenerContainer.receive(
                consumer,
                streamOffset,
                this::handleMessage
        );
    }

    public void handleMessage(MapRecord<String, String, String> record) {
        try {
            Map<String, String> message = record.getValue();
            String roomId = message.get("roomId");
            String usersJson = message.get("users");

            List<String> users = objectMapper.readValue(usersJson, new TypeReference<List<String>>() {});

            RoomUserInfo roomUserInfo = new RoomUserInfo(roomId, users);

            System.out.println("1월 15일 테스트 : ");
            System.out.println(roomUserInfo);

            stompRoomService.sendRoomInfo(roomUserInfo.getRoomId(), roomUserInfo);

            stringRedisTemplate.opsForStream().acknowledge(record.getStream(), GROUP_NAME, record.getId());
            stringRedisTemplate.opsForStream().delete(record.getStream(), record.getId());

        } catch (Exception e) {
            System.err.println("Error processing message: " + e.getMessage());
            e.printStackTrace();
        }

    }


}
