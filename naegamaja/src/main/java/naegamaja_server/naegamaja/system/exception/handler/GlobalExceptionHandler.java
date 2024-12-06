package naegamaja_server.naegamaja.system.exception.handler;

import lombok.extern.slf4j.Slf4j;
import naegamaja_server.naegamaja.system.exception.dto.ErrorDto;
import naegamaja_server.naegamaja.system.exception.model.ErrorCode;
import naegamaja_server.naegamaja.system.exception.model.RestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler({HttpMessageConversionException.class})
    public ResponseEntity<ErrorDto.ErrorResponse> handleRestException(HttpMessageConversionException e) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorDto.ErrorResponse.from(ErrorCode.GLOBAL_BAD_REQUEST));
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<ErrorDto.ErrorResponse> handleRestException(HttpRequestMethodNotSupportedException e) {

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(ErrorDto.ErrorResponse.from(ErrorCode.GLOBAL_METHOD_NOT_ALLOWED));
    }

    @ExceptionHandler({RestException.class})
    public ResponseEntity<ErrorDto.ErrorResponse> handleRestException(RestException e) {
        log.error("{Rest Exception}: " + e.getErrorCode().getMessage());
        return ResponseEntity
                .status(e.getErrorCode().getStatusCode())
                .body(ErrorDto.ErrorResponse.from(e.getErrorCode()));
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorDto.ErrorResponse> handleException(Exception e) {
        log.error("{Exception}: " + e.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorDto.ErrorResponse.from(ErrorCode.INTERNAL_SERVER_ERROR));
    }

}
