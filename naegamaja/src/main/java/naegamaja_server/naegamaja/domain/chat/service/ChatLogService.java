package naegamaja_server.naegamaja.domain.chat.service;

import lombok.RequiredArgsConstructor;
import naegamaja_server.naegamaja.domain.chat.entity.ChatLog;
import naegamaja_server.naegamaja.domain.chat.repository.CustomRedisChatLogRepository;
import naegamaja_server.naegamaja.domain.room.repository.CustomRedisRoomRepository;
import naegamaja_server.naegamaja.domain.session.repository.CustomRedisSessionRepository;
import naegamaja_server.naegamaja.system.websocket.dto.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatLogService {

    private final CustomRedisChatLogRepository customRedisChatLogRepository;
    private final CustomRedisRoomRepository customRedisRoomRepository;
    private final CustomRedisSessionRepository customRedisSessionRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;


    public void roomChat(Message.RoomMQRequest message) {
        ChatLog chatLog = customRedisChatLogRepository.saveChatLog(message.getContent(), message.getRoomId(), message.getSender());

        simpMessagingTemplate.convertAndSend("/topic/room/" + message.getRoomId(), chatLog);
        System.out.println("chatLog 보냈음");
    }

    public List<ChatLog> getRoomMessages(String roomId) {
        return customRedisChatLogRepository.getRoomChatLogs(roomId);
    }
}
