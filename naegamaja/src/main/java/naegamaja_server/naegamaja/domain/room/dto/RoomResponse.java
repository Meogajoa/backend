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

    public static RoomResponse of(Long roomId){
        return RoomResponse.builder()
                .roomId(roomId.toString())
                .build();
    }
}
