package naegamaja_server.naegamaja.system.websocket.dto;

import lombok.*;
import naegamaja_server.naegamaja.domain.chat.entity.ChatLog;
import naegamaja_server.naegamaja.domain.room.dto.RoomUserInfo;
import naegamaja_server.naegamaja.system.websocket.model.MessageType;

public class Message {

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class SyncMQRequest {
        private MessageType type;
        private String content;
    }

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
    public static class RoomChatMQRequest {
        private MessageType type;
        private String roomId;
        private String content;
        private String sender;

        public static RoomChatMQRequest of(Message.Request request, String roomId, String sender) {
            return RoomChatMQRequest.builder()
                    .type(request.getType())
                    .roomId(roomId)
                    .content(request.getContent())
                    .sender(sender)
                    .build();
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class RoomChatPubSubResponse {
        String id;
        ChatLog chatLog;

        public static RoomChatPubSubResponse of(String id, ChatLog chatLog) {
            return RoomChatPubSubResponse.builder()
                    .id(id)
                    .chatLog(chatLog)
                    .build();
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class RoomInfoPubSubResponse {
        String id;
        RoomUserInfo roomUserInfo;

        public static RoomInfoPubSubResponse of(String id, RoomUserInfo roomUserInfo) {
            return RoomInfoPubSubResponse.builder()
                    .id(id)
                    .roomUserInfo(roomUserInfo)
                    .build();
        }
    }
}
