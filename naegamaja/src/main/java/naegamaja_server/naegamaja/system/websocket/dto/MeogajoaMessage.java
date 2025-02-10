package naegamaja_server.naegamaja.system.websocket.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import naegamaja_server.naegamaja.domain.chat.entity.ChatLog;
import naegamaja_server.naegamaja.domain.room.dto.RoomUserInfo;
import naegamaja_server.naegamaja.system.websocket.model.MessageType;

import java.time.LocalDateTime;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MeogajoaMessage {

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
        private String id;
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

        public static ChatMQRequest of(MeogajoaMessage.Request request, String id, String sender) {
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

        public static GameChatMQRequest of(MeogajoaMessage.Request request, String id, String sender) {
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
    public static class ChatPubSubResponseToUser{
        String id;
        PersonalChatLog personalChatLog;
        String receiver;
        String sender;

        public static ChatPubSubResponseToUser of(String id, PersonalChatLog personalChatLog, String receiver, String sender) {
            return ChatPubSubResponseToUser.builder()
                    .id(id)
                    .personalChatLog(personalChatLog)
                    .receiver(receiver)
                    .sender(sender)
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

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class GameUserListResponse {
        private MessageType type;
        private String id;
        private List<Long> blackTeam;
        private List<Long> whiteTeam;
        private List<Long> redTeam;
        private List<Long> eliminated;
    }



    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class ChatLogResponse {
        private MessageType type;
        private String id;
        private List<ChatLog> chatLogs;

    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class PersonalChatLog {
        private String id;
        private String sender;
        private String receiver;
        private String content;
        private LocalDateTime sendTime;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class PersonalChatLogResponse {
        private MessageType type;
        private String id;
        private String receiver;
        private List<PersonalChatLog> personalChatLogs;
    }


}
