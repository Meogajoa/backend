package naegamaja_server.naegamaja.domain.room.controller;

import lombok.RequiredArgsConstructor;
import naegamaja_server.naegamaja.domain.chat.entity.ChatLog;
import naegamaja_server.naegamaja.domain.chat.service.ChatLogService;
import naegamaja_server.naegamaja.domain.room.dto.RoomCreationDto;
import naegamaja_server.naegamaja.domain.room.dto.RoomRequest;
import naegamaja_server.naegamaja.domain.room.dto.RoomResponse;
import naegamaja_server.naegamaja.domain.room.service.RoomService;
import naegamaja_server.naegamaja.system.websocket.dto.Message;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/room")
public class RoomRestController {

    private final RoomService roomService;
    private final ChatLogService chatLogService;

    @PostMapping("/join")
    public List<ChatLog> joinRoom(@RequestHeader String authorization, @RequestBody RoomRequest.JoinRoomRequest request) {
        roomService.joinRoom(request, authorization);
        return chatLogService.getRoomMessages(request.getRoomId());
    }

    @PostMapping("/create")
    public ResponseEntity<?> createRoom(@RequestHeader String authorization, @RequestBody RoomRequest.CreateRoomRequest request) {
        roomService.createRoom(request, authorization);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/pages/{pageNum}")
    public Page<RoomResponse> getRooms(@PathVariable int pageNum) {
        return roomService.getRooms(pageNum);
    }

    @GetMapping("/lobby")
    public Page<RoomResponse> getLobby(){
        return roomService.getRooms(1);
    }



}
