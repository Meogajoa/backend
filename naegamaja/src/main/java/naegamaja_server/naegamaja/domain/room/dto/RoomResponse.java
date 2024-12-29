package naegamaja_server.naegamaja.domain.room.dto;

import lombok.*;
import naegamaja_server.naegamaja.domain.room.domain.Room;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoomResponse {
    private String roomId;
    private String roomName;
    private String roomOwner;
    private int roomMaxUser;
    private int roomCurrentUser;
    private boolean roomIsPlaying;

    public static RoomResponse of(String roomId, String roomName, String roomOwner, int roomMaxUser, int roomCurrentUser, boolean roomIsPlaying){
        return RoomResponse.builder()
                .roomId(roomId)
                .roomName(roomName)
                .roomOwner(roomOwner)
                .roomMaxUser(roomMaxUser)
                .roomCurrentUser(roomCurrentUser)
                .roomIsPlaying(roomIsPlaying)
                .build();
    }

    public static RoomResponse from(Room room){
        return RoomResponse.builder()
                .roomId(room.getId())
                .roomName(room.getRoomName())
                .roomOwner(room.getRoomOwner())
                .roomMaxUser(room.getRoomMaxUser())
                .roomCurrentUser(room.getRoomCurrentUser())
                .roomIsPlaying(room.isRoomIsPlaying())
                .build();
    }
}
