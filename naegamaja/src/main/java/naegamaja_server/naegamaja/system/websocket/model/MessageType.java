package naegamaja_server.naegamaja.system.websocket.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MessageType {
    CHAT("CHAT"),
    SYSTEM("SYSTEM"),
    USER_JOIN("USER_JOIN"),
    GAME_START("GAME_START"),
    TEST("TEST"),
    GAME_DAY_OR_NIGHT("GAME_DAY_OR_NIGHT"),
    GAME_END("GAME_END"),
    MINI_GAME_WILL_START_NOTICE("MINI_GAME_WILL_START_NOTICE"),
    GAME_MY_INFO("GAME_MY_INFO"),
    BUTTON_GAME_STATUS("BUTTON_GAME_STATUS"),
    BUTTON_CLICK("BUTTON_CLICK"),
    MINI_GAME_WILL_END_NOTICE("MINI_GAME_WILL_END_NOTICE"),
    GAME_USER_LIST("GAME_USER_LIST"),
    GET_GAME_CHAT("GET_GAME_CHAT"),
    CHAT_LOGS("CHAT_LOGS"),
    PERSONAL_CHAT_LOGS("PERSONAL_CHAT_LOGS"),
    GET_GAME_CHAT_PERSONAL("GET_GAME_CHAT_PERSONAL"),
    GET_GAME_CHAT_BLACK("GET_GAME_CHAT_BLACK"),
    GET_GAME_CHAT_WHITE("GET_GAME_CHAT_WHITE"),
    GET_GAME_CHAT_ELIMINATED("GET_GAME_CHAT_ELIMINATED"),
    GET_GAME_CHAT_RED("GET_GAME_CHAT_RED"),
    VOTE_GAME_STATUS("VOTE_GAME_STATUS"),
    VOTE("VOTE"),
    RE_VOTE_NOTICE("RE_VOTE_NOTICE"),
    VOTE_RESULT("VOTE_RESULT"),
    GAME_USER_INFO("USER_INFO"),
    ELIMINATED_USER("ELIMINATED_USER"),;



    private final String value;

    MessageType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static MessageType fromValue(String value) {
        for (MessageType type : MessageType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown MessageType: " + value);
    }
}
