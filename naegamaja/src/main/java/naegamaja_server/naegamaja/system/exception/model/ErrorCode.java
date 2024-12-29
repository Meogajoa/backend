package naegamaja_server.naegamaja.system.exception.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    GLOBAL_BAD_REQUEST(400, "올바르지 않은 요청입니다."),
    GLOBAL_NOT_FOUND(404, "요청한 사항을 찾을 수 없습니다."),
    GLOBAL_ALREADY_EXIST(400, "요청의 대상이 이미 존재합니다."),
    GLOBAL_METHOD_NOT_ALLOWED(405, "허용되지 않는 Method 입니다."),

    INTERNAL_SERVER_ERROR(500, "서버 내부 오류입니다."),
    AUTH_USER_NOT_FOUND(400, "사용자를 찾을 수 없습니다."),
    AUTH_BAD_CREDENTIALS(400, "비밀번호가 일치하지 않습니다."),
    GLOBAL_UNAUTHORIZED(401, "인증되지 않은 사용자입니다."),
    USER_ALREADY_EXIST(400, "이미 존재하는 사용자입니다.");


    private final int statusCode;
    private final String message;


}