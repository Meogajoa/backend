package naegamaja_server.naegamaja.system.websocket.manager;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketConnectionManager {
    private final Map<String, String> sessions = new ConcurrentHashMap<>();

    public void addSession(String nickname, String sessionId) {
        sessions.put(nickname, sessionId);
    }

    public void removeSession(String nickname) {
        sessions.remove(nickname);
    }

    public String getSessionId(String nickname) {
        return sessions.get(nickname);
    }
}
