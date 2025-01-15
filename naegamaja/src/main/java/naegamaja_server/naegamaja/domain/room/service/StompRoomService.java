package naegamaja_server.naegamaja.domain.room.service;


import lombok.RequiredArgsConstructor;
import naegamaja_server.naegamaja.domain.room.dto.RoomUserInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StompRoomService {
    private final SimpMessagingTemplate simpMessagingTemplate;

    public void sendRoomInfo(String roomId, RoomUserInfo roomUserInfo) {
        simpMessagingTemplate.convertAndSend("/topic/room/" + roomId, roomUserInfo.getUsers());
        System.out.println("유저 정보 보내기 완료슝!!");
    }
}
