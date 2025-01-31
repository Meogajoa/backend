package naegamaja_server.naegamaja.domain.redis.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import naegamaja_server.naegamaja.domain.chat.entity.ChatLog;
import naegamaja_server.naegamaja.domain.game.entity.Player;
import naegamaja_server.naegamaja.domain.room.dto.RoomUserInfo;
import naegamaja_server.naegamaja.domain.session.repository.CustomRedisSessionRepository;
import naegamaja_server.naegamaja.system.websocket.dto.NaegamajaMessage;
import naegamaja_server.naegamaja.system.websocket.model.MessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RedisPubSubSubscriber {

    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final CustomRedisSessionRepository customRedisSessionRepository;


    public void roomChat(String message, String channel){
        try{
            NaegamajaMessage.ChatPubSubResponse chatPubSubResponse = objectMapper.readValue(message, NaegamajaMessage.ChatPubSubResponse.class);

            ChatLog chatlog = chatPubSubResponse.getChatLog();

            System.out.println("/topic/room/" + chatPubSubResponse.getId() + "로 보냈어");

            simpMessagingTemplate.convertAndSend("/topic/room/" + chatPubSubResponse.getId() + "/chat", chatlog);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void gameChat(String message, String channel){
        try{
            NaegamajaMessage.ChatPubSubResponse chatPubSubResponse = objectMapper.readValue(message, NaegamajaMessage.ChatPubSubResponse.class);

            ChatLog chatlog = chatPubSubResponse.getChatLog();

            System.out.println("/topic/game/" + chatPubSubResponse.getId() + "로 보냈어");

            simpMessagingTemplate.convertAndSend("/topic/game/" + chatPubSubResponse.getId() + "/chat", chatlog);
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
            NaegamajaMessage.GameSystemResponse gameSystemResponse = objectMapper.readValue(message, NaegamajaMessage.GameSystemResponse.class);

            System.out.println("/topic/room/" + gameSystemResponse.getId() + "로 보냈어");

            simpMessagingTemplate.convertAndSend("/topic/room/" + gameSystemResponse.getId() + "/notice/system", gameSystemResponse);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void userInfo(String message, String channel){
        try{
            List<Player> playerList = objectMapper.readValue(message, objectMapper.getTypeFactory().constructCollectionType(List.class, Player.class));

            for(Player player : playerList){
                simpMessagingTemplate.convertAndSend("/topic/user/" + player.getNickname() + "/gameInfo", player);
            }

            System.out.println("유저 개인 정보 출력");
            System.out.println(playerList);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void gameDayOrNight(String message, String channel){
        try{
            NaegamajaMessage.GameDayOrNightResponse gameDayOrNightResponse = objectMapper.readValue(message, NaegamajaMessage.GameDayOrNightResponse.class);
            simpMessagingTemplate.convertAndSend("/topic/game/" + gameDayOrNightResponse.getGameId() + "/notice/system", gameDayOrNightResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void miniGameNotice(String message, String channel){
        try{
            NaegamajaMessage.MiniGameNoticeResponse gameMiniGameNoticeResponse = objectMapper.readValue(message, NaegamajaMessage.MiniGameNoticeResponse.class);
            simpMessagingTemplate.convertAndSend("/topic/game/" + gameMiniGameNoticeResponse.getId() + "/notice/system", gameMiniGameNoticeResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void buttonGameStatus(String message, String channel){
        try{
            NaegamajaMessage.ButtonGameStatusResponse buttonGameStatusResponse = objectMapper.readValue(message, NaegamajaMessage.ButtonGameStatusResponse.class);
            simpMessagingTemplate.convertAndSend("/topic/game/" + buttonGameStatusResponse.getId() + "/notice/system", buttonGameStatusResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

//    public void gameUserList(String message, String channel){
//        try{
//            NaegamajaMessage.GameUserListResponse gameUserListResponse = objectMapper.readValue(message, NaegamajaMessage.GameUserListResponse.class);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//    }


}
