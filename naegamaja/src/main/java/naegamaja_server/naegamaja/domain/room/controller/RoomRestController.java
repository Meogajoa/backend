package naegamaja_server.naegamaja.domain.room.controller;

import lombok.RequiredArgsConstructor;
import naegamaja_server.naegamaja.domain.chat.service.ChatLogService;
import naegamaja_server.naegamaja.domain.room.dto.*;
import naegamaja_server.naegamaja.domain.room.service.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/room")
public class RoomRestController {

    private final RoomService roomService;
    private final ChatLogService chatLogService;

    @PostMapping("/join")
    public ResponseEntity<RoomJoinResponse> joinRoom(@RequestHeader String authorization, @RequestBody RoomRequest.JoinRoomRequest request) {
        roomService.joinRoom(request, authorization);
        RoomJoinResponse response = RoomJoinResponse.builder().chatLogs(chatLogService.getRoomMessages(request.getId())).name(roomService.getRoomName(request.getId())).owner(roomService.getRoomOwner(request.getId())).isPlaying(roomService.isPlaying(request.getId())).build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public RoomCreationDto.RoomCreationResponse createRoom(@RequestHeader String authorization, @RequestBody RoomRequest.CreateRoomRequest request) {
        String id = roomService.createRoom(request, authorization);

        return RoomCreationDto.RoomCreationResponse.builder()
                .id(id)
                .build();
    }

    @GetMapping("/pages/{pageNum}")
    public RoomPageResponse getRooms(@PathVariable int pageNum) {
        return roomService.getRooms(pageNum);
    }

    @GetMapping("/lobby")
    public RoomPageResponse getLobby(){
        return roomService.getRooms(1);
    }



}
