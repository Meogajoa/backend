package naegamaja_server.naegamaja.domain.chat.service;

import lombok.RequiredArgsConstructor;
import naegamaja_server.naegamaja.domain.chat.entity.ChatLog;
import naegamaja_server.naegamaja.domain.chat.repository.CustomRedisChatLogRepository;
import naegamaja_server.naegamaja.domain.room.repository.CustomRedisRoomRepository;
import naegamaja_server.naegamaja.domain.session.repository.CustomRedisSessionRepository;
import naegamaja_server.naegamaja.system.exception.model.ErrorCode;
import naegamaja_server.naegamaja.system.exception.model.RestException;
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


    public ChatLog roomChat(Long roomNumber, Message.Request message, String authorization) {
        String userNickname = customRedisSessionRepository.getNicknameBySessionId(authorization);

        if(customRedisRoomRepository.isUserInRoom(userNickname, roomNumber)) {
            return customRedisChatLogRepository.saveChatLog(message.getContent(), roomNumber, userNickname);
        } else {
            System.out.println("user is not in room");
            throw new RestException(ErrorCode.USER_NOT_IN_ROOM);
        }
    }

    public List<ChatLog> getRoomMessages(String roomId) {
        return customRedisChatLogRepository.getRoomChatLogs(roomId);
    }
}
