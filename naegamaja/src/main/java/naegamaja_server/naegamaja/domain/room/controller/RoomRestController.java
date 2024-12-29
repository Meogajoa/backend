package naegamaja_server.naegamaja.domain.room.controller;

import lombok.RequiredArgsConstructor;
import naegamaja_server.naegamaja.domain.room.dto.RoomRequest;
import naegamaja_server.naegamaja.domain.room.service.RedisRoomService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RoomRestController {

    private final RedisRoomService redisRoomService;

    @PostMapping("/room/{roomId}/join")
    public void joinRoom(RoomRequest.JoinRoomRequest request) {
        redisRoomService.joinRoom(request);
    }

}
