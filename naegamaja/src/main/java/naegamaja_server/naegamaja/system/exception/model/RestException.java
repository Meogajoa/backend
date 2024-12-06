package naegamaja_server.naegamaja.system.exception.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RestException extends RuntimeException{
    private ErrorCode errorCode;
}
