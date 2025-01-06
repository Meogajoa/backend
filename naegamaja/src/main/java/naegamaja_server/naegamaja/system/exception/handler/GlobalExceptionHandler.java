package naegamaja_server.naegamaja.system.exception.handler;


import lombok.extern.slf4j.Slf4j;
import naegamaja_server.naegamaja.system.exception.dto.ErrorDto;
import naegamaja_server.naegamaja.system.exception.model.RestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(RestException.class)
    public ResponseEntity<ErrorDto.ErrorResponse> handleRestException(RestException exception) {
        log.error("{Rest Exception}: " + exception.getErrorCode().getMessage());
        return ResponseEntity.status(exception.getErrorCode().getStatusCode()).body(ErrorDto.ErrorResponse.from(exception.getErrorCode()));

    }
}
