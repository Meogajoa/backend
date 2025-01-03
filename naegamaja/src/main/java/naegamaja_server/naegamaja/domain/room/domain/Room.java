package naegamaja_server.naegamaja.domain.room.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import naegamaja_server.naegamaja.domain.session.entity.UserSession;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.util.List;

@RedisHash("room")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Room implements Serializable {

    @Id
    private String id;

    @Indexed
    private String roomName;

    private String roomPassword;

    private String roomOwner;

    private int roomMaxUser;

    private int roomCurrentUser;

    private boolean roomIsPlaying;

    List<String> userSessions;

}
