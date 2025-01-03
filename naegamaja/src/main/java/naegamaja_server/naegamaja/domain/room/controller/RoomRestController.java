package naegamaja_server.naegamaja.domain.room.controller;

import lombok.RequiredArgsConstructor;
import naegamaja_server.naegamaja.domain.room.dto.RoomCreationDto;
import naegamaja_server.naegamaja.domain.room.dto.RoomRequest;
import naegamaja_server.naegamaja.domain.room.dto.RoomResponse;
import naegamaja_server.naegamaja.domain.room.service.RoomService;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/room")
public class RoomRestController {

    private final RoomService roomService;

    @PostMapping("/join")
    public ResponseEntity<?> joinRoom(@RequestHeader String authorization, @RequestBody RoomRequest.JoinRoomRequest request) {
        roomService.joinRoom(request, authorization);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/create")
    public Long createRoom(@RequestHeader String authorization, @RequestBody RoomRequest.CreateRoomRequest request) {
        System.out.println("request = " + request);
        return roomService.createRoom(request, authorization);
    }

    @GetMapping("/pages/{pageNum}")
    public Page<RoomResponse> getRooms(@PathVariable int pageNum) {
        return roomService.getRooms(pageNum);
    }



}
