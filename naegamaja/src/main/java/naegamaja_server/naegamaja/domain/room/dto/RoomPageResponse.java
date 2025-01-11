package naegamaja_server.naegamaja.domain.room.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class RoomPageResponse {
    private List<RoomResponse> rooms;
    private boolean isLast;

}
