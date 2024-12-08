package naegamaja_server.naegamaja.domain.session.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Builder
public class UserSession implements Serializable {
    private String username;
    private String state;
    private String sessionId;
    private Long roomNumber;

    public static UserSession of(String username, String state, String sessionId, Long roomNumber){
        return UserSession.builder()
                .username(username)
                .state(state)
                .sessionId(sessionId)
                .roomNumber(roomNumber)
                .build();
    }
}
