package naegamaja_server.naegamaja.domain.room.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import naegamaja_server.naegamaja.domain.chat.entity.ChatLog;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoomJoinResponse {
    List<ChatLog> chatLogs;
    String name;
    String owner;
}
