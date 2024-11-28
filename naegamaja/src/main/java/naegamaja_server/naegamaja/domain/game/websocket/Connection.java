package naegamaja_server.naegamaja.domain.game.websocket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.socket.WebSocketSession;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Connection {
    private WebSocketSession session;

    public static Connection of(WebSocketSession session) {
        return Connection.builder()
                .session(session)
                .build();
    }
}
