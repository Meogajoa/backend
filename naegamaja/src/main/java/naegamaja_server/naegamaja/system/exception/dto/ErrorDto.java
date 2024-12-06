package naegamaja_server.naegamaja.system.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import naegamaja_server.naegamaja.system.exception.model.ErrorCode;
import org.springframework.web.ErrorResponse;

public class ErrorDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class ErrorResponse{
        private Integer statusCode;
        private String message;
        private String codeName;

        public static ErrorResponse from(ErrorCode errorCode){
            return ErrorResponse.builder()
                    .statusCode(errorCode.getStatusCode())
                    .message(errorCode.getMessage())
                    .codeName(errorCode.name())
                    .build();
        }

        public static ErrorResponse of(Integer statusCode,ErrorCode errorCode){
            return ErrorResponse.builder()
                    .statusCode(statusCode)
                    .message(errorCode.getMessage())
                    .build();
        }

    }
}
