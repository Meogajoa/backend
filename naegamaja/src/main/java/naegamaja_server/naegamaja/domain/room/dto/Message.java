package naegamaja_server.naegamaja.domain.room.dto;

import naegamaja_server.naegamaja.system.websocket.model.MessageType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Message {
    MessageType messageType;
    String message;
    String sender;

    LocalDateTime sendTime;


}
