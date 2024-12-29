package naegamaja_server.naegamaja.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import naegamaja_server.naegamaja.domain.user.entity.User;

public class UserDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class UserResponse{
        private String email;
        private String nickname;

        public static UserResponse from(User user){
            return UserResponse.builder()
                    .email(user.getEmail())
                    .nickname(user.getNickname())
                    .build();
        }
    }
}
