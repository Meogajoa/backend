package naegamaja_server.naegamaja.domain.chat.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    String content;

    @Indexed
    String sender;

    LocalDateTime sendTime;


}
