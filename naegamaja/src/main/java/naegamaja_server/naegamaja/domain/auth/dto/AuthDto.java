package naegamaja_server.naegamaja.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import naegamaja_server.naegamaja.domain.user.dto.UserDto;
import naegamaja_server.naegamaja.domain.user.entity.User;
import org.hibernate.Session;
import org.springframework.cglib.core.Local;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class SignInRequest{
        private String email;
        private String password;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class SignUpRequest{
        private String email;

        private String nickName;

        private String password;

        public User toEntity(PasswordEncoder encoder){
            return User.builder()
                    .email(email)
                    .nickName(nickName)
                    .password(encoder.encode(password))
                    .build();
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class SessionIdResponse{
        private UserDto.UserResponse user;
        private String sessionId;

        public static SessionIdResponse of(User user, String sessionId){
            return SessionIdResponse.builder()
                    .user(UserDto.UserResponse.from(user))
                    .sessionId(sessionId)
                    .build();
        }

    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class LogoutRequest{
        private String sessionId;
    }

}
