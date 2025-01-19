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
        private String content;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class RoomMQRequest {
        private MessageType type;
        private String roomId;
        private String content;
        private String sender;

        public static RoomMQRequest of(Message.Request request, String roomId, String sender) {
            return RoomMQRequest.builder()
                    .type(request.getType())
                    .roomId(roomId)
                    .content(request.getContent())
                    .sender(sender)
                    .build();
        }
    }

}
