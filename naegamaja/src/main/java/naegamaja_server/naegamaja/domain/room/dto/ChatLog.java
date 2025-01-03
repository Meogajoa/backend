package naegamaja_server.naegamaja.domain.room.dto;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import naegamaja_server.naegamaja.system.websocket.model.MessageType;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RedisHash(value = "message")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ChatLog {

    @Id
    private String id;

    String message;

    @Indexed
    String sender;

    LocalDateTime sendTime;


}
