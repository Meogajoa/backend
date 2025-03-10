package naegamaja_server.naegamaja.domain.chat.service;

import lombok.RequiredArgsConstructor;
import naegamaja_server.naegamaja.domain.chat.entity.ChatLog;
import naegamaja_server.naegamaja.domain.chat.repository.CustomRedisChatLogRepository;
import naegamaja_server.naegamaja.domain.room.repository.CustomRedisRoomRepository;
import naegamaja_server.naegamaja.domain.session.repository.CustomRedisSessionRepository;
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


    public void roomChat(ChatLog chatLog, String roomId) {
        //ChatLog chatLog = customRedisChatLogRepository.saveChatLog(message.getContent(), message.getRoomId(), message.getSender());

        simpMessagingTemplate.convertAndSend("/topic/room/" + roomId, chatLog);
    }

    public List<ChatLog> getRoomMessages(String roomId) {
        return customRedisChatLogRepository.getRoomChatLogs(roomId);
    }
}
