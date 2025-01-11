//package naegamaja_server.naegamaja.domain.chat.service;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import naegamaja_server.naegamaja.system.websocket.dto.Message;
//import naegamaja_server.naegamaja.system.websocket.model.MessageType;
//import org.springframework.data.redis.connection.stream.MapRecord;
//import org.springframework.data.redis.connection.stream.ReadOffset;
//import org.springframework.data.redis.connection.stream.StreamOffset;
//import org.springframework.data.redis.stream.StreamMessageListenerContainer;
//import org.springframework.data.redis.stream.StreamReceiver;
//import org.springframework.data.redis.stream.Subscription;
//import org.springframework.stereotype.Service;
//import java.util.Map;
//
//@Service
//@RequiredArgsConstructor
//public class RedisStreamChatSubscriber {
//
//    private final StreamMessageListenerContainer<String, MapRecord<String, Object, Object>> container;
//    private final ObjectMapper objectMapper;
//    private final String ROOM_CHAT_STREAM_KEY = "stream:chat:room:";
//
//    @PostConstruct
//    public void startListenChatStream() {
//
//        String roomId = "room1";
//
//        Subscription subscription = container.receive(
//                StreamOffset.create(ROOM_CHAT_STREAM_KEY + roomId, ReadOffset.latest()),
//                (MapRecord<String, Object, Object> message) -> {
//                    // Redis Stream에서 전달된 메시지를 처리
//                    Map<Object, Object> value = message.getValue();
//
//                    // Jackson을 사용해 다시 DTO 형태로 역직렬화
//                    Message.Request chatMessage = objectMapper.convertValue(value, Message.Request.class);
//
//                    if(chatMessage.getType() == MessageType.CHAT) {
//                        // 원하는 로직 수행 (로그 출력 예시)
//                        System.out.println("새로운 채팅 메시지 수신: " + chatMessage.getContent());
//                    }
//                }
//        );
//
//    }
//}
