package naegamaja_server.naegamaja.system.websocket.dto;

import lombok.*;
import naegamaja_server.naegamaja.system.websocket.model.MessageType;

public class Message {

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Request {
        private MessageType type;
        private Long roomId;
        private String content;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class MQRequest {
        private MessageType type;
        private Long roomId;
        private String content;
        private String sender;

        public static MQRequest of(Message.Request request, String sender) {
            return MQRequest.builder()
                    .type(request.getType())
                    .roomId(request.getRoomId())
                    .content(request.getContent())
                    .sender(sender)
                    .build();
        }
    }

}
