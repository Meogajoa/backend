package naegamaja_server.naegamaja.domain.redis.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import naegamaja_server.naegamaja.domain.chat.entity.ChatLog;
import naegamaja_server.naegamaja.domain.room.dto.RoomUserInfo;
import naegamaja_server.naegamaja.system.websocket.dto.Message;
import naegamaja_server.naegamaja.system.websocket.model.MessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisPubSubSubscriber {

    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate simpMessagingTemplate;


    public void roomChat(String message, String channel){
        try{
            Message.RoomChatPubSubResponse roomChatPubSubResponse = objectMapper.readValue(message, Message.RoomChatPubSubResponse.class);

            ChatLog chatlog = roomChatPubSubResponse.getChatLog();

            System.out.println("/topic/room/" + roomChatPubSubResponse.getId() + "로 보냈어");

            simpMessagingTemplate.convertAndSend("/topic/room/" + roomChatPubSubResponse.getId() + "/chat", chatlog);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void roomInfo(String message, String channel){
        try{
            RoomUserInfo roomUserInfo = objectMapper.readValue(message, RoomUserInfo.class);

            Thread.sleep(500);

            simpMessagingTemplate.convertAndSend("/topic/room/" + roomUserInfo.getRoomId() + "/notice/users", roomUserInfo.getUsers());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void gameStart(String message, String channel){
        try{
            String gameId = objectMapper.readValue(message, String.class);
            Message.GameSystemResponse gameSystemResponse = Message.GameSystemResponse.builder()
                    .type(MessageType.GAME_START)
                    .content(gameId)
                    .build();

            System.out.println("/topic/game/" + gameId + "로 보냈어");

            simpMessagingTemplate.convertAndSend("/topic/game/" + gameId, gameSystemResponse);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
