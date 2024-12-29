package naegamaja_server.naegamaja.domain.room.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Builder
public class Room {

    @Id
    private String roomId;

    private String roomName;

    private String roomPassword;

    private String roomOwner;

    private int roomMaxUser;

    private int roomCurrentUser;

    private boolean roomIsPlaying;


}
