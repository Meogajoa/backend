package naegamaja_server.naegamaja.domain.room.dto;

import lombok.*;

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
}
