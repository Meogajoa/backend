package naegamaja_server.naegamaja.domain.room.dto;

import lombok.*;


public class RoomRequest {

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class JoinRoomRequest {
        private String roomId;
        private String roomPassword;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class CreateRoomRequest {
        private String roomName;
        private String roomPassword;
    }
}
