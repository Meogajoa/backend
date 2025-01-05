package naegamaja_server.naegamaja.domain.session.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import naegamaja_server.naegamaja.domain.session.state.State;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

@RedisHash("session")
@AllArgsConstructor
@Getter
@Builder
public class UserSession implements Serializable {
    @Id
    private String sessionId;

    @Indexed
    private String nickname;
    private State state;
    private Long roomNumber;
    private boolean isInGame;
    private boolean isInRoom;

    public static UserSession of(String nickname, State state, String sessionId, Long roomNumber, boolean isInGame, boolean isInRoom){
        return UserSession.builder()
                .nickname(nickname)
                .state(state)
                .sessionId(sessionId)
                .roomNumber(roomNumber)
                .isInGame(isInGame)
                .isInRoom(isInRoom)
                .build();
    }

}
