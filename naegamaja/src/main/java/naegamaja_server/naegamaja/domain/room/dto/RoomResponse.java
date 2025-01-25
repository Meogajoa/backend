package naegamaja_server.naegamaja.domain.room.dto;

import lombok.*;
import naegamaja_server.naegamaja.domain.room.entity.Room;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoomResponse {
    private String id;

    private String name;

    private String owner;

    private int maxUser;

    private int currentUser;

    private boolean isPlaying;

    private boolean isLocked;

    public static RoomResponse from(Room room){
        return RoomResponse.builder()
                .id(room.getId())
                .name(room.getName())
                .owner(room.getOwner())
                .maxUser(room.getMaxUser())
                .currentUser(room.getCurrentUser())
                .isPlaying(room.isPlaying())
                .isLocked(room.isLocked())
                .build();
    }
}
