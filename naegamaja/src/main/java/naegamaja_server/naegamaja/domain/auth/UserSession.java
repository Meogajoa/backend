package naegamaja_server.naegamaja.domain.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Builder
public class UserSession implements Serializable {
    private String username;
    private String state;
    private Long roomNumber;



}
