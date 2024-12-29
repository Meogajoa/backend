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
        private String nickname;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class CreateRoomRequest {
        private String nickname;
        private String roomName;
        private String roomPassword;
        private int roomMaxUser;
    }
}
