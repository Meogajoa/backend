package naegamaja_server.naegamaja.system.websocket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import naegamaja_server.naegamaja.system.websocket.model.MessageType;

public class Message {

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Request {
        private MessageType type;
        private String roomId;
        private String content;
    }

}
