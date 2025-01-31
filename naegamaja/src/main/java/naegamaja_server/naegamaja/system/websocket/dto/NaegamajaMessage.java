package naegamaja_server.naegamaja.system.websocket.dto;

import lombok.*;
import naegamaja_server.naegamaja.domain.chat.entity.ChatLog;
import naegamaja_server.naegamaja.domain.room.dto.RoomUserInfo;
import naegamaja_server.naegamaja.system.websocket.model.MessageType;

import java.time.LocalDateTime;
import java.util.List;

public class NaegamajaMessage {

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class GameMQRequest {
        private MessageType type;
        private String sender;
        private String gameId;
        private String content;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class GameSystemResponse {
        private String sender;
        private String id;
        private MessageType type;
        private String content;
        private LocalDateTime sendTime;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class GameDayOrNightResponse {
        private String gameId;
        private String sender;
        private MessageType type;
        private int day;
        private String dayOrNight;
        private LocalDateTime sendTime;
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
    public static class ChatMQRequest {
        private MessageType type;
        private String id;
        private String content;
        private String sender;

        public static ChatMQRequest of(NaegamajaMessage.Request request, String id, String sender) {
            return ChatMQRequest.builder()
                    .type(request.getType())
                    .id(id)
                    .content(request.getContent())
                    .sender(sender)
                    .build();
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class GameChatMQRequest {
        private MessageType type;
        private String id;
        private String content;
        private String sender;

        public static GameChatMQRequest of(NaegamajaMessage.Request request, String id, String sender) {
            return GameChatMQRequest.builder()
                    .type(request.getType())
                    .id(id)
                    .content(request.getContent())
                    .sender(sender)
                    .build();
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class ChatPubSubResponse {
        String id;
        ChatLog chatLog;

        public static ChatPubSubResponse of(String id, ChatLog chatLog) {
            return ChatPubSubResponse.builder()
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

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class MiniGameNoticeResponse {
        private MessageType type;
        private String id;
        private String miniGameType;
        private String scheduledTime;
        private String sender;
        private LocalDateTime sendTime;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class ButtonGameStatusResponse {
        private MessageType type;
        private String id;
        private String sender;
        private List<Long> twentyButtons;
        private List<Long> fiftyButtons;
        private List<Long> hundredButtons;
    }
}
