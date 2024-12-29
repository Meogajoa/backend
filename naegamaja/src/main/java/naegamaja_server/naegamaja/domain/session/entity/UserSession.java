package naegamaja_server.naegamaja.domain.session.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Builder
public class UserSession implements Serializable {
    private String nickname;
    private String state;
    private String sessionId;
    private Long roomNumber;
    private boolean isInGame;
    private boolean isInRoom;

    public static UserSession of(String nickname, String state, String sessionId, Long roomNumber, boolean isInGame, boolean isInRoom){
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
