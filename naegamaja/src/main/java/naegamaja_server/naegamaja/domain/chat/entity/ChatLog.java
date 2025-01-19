package naegamaja_server.naegamaja.domain.chat.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import naegamaja_server.naegamaja.system.websocket.dto.Message;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.time.LocalDateTime;

@RedisHash(value = "chat_log")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ChatLog {

    @Id
    private String id;

    private String content;

    @Indexed
    private String sender;

    private LocalDateTime sendTime;

    public static ChatLog of(String id, String content, String sender, LocalDateTime sendTime){
        return ChatLog.builder()
                .id(id)
                .content(content)
                .sender(sender)
                .sendTime(sendTime)
                .build();
    }
}
