package naegamaja_server.naegamaja.domain.room.controller;

import lombok.RequiredArgsConstructor;
import naegamaja_server.naegamaja.domain.room.dto.RoomCreationDto;
import naegamaja_server.naegamaja.domain.room.dto.RoomRequest;
import naegamaja_server.naegamaja.domain.room.dto.RoomResponse;
import naegamaja_server.naegamaja.domain.room.service.RoomService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/room")
public class RoomRestController {

    private final RoomService roomService;

    @PostMapping("/{roomId}/join")
    public void joinRoom(RoomRequest.JoinRoomRequest request) {
        roomService.joinRoom(request);
    }

    @PostMapping("/create")
    public int createRoom(RoomRequest.CreateRoomRequest request) {
        return roomService.createRoom(request);
    }



}
