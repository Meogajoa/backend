package naegamaja_server.naegamaja.system.security.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import naegamaja_server.naegamaja.domain.user.entity.User;
import org.springframework.security.core.AuthenticatedPrincipal;

@SuperBuilder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDetail implements AuthenticatedPrincipal {
    private User user;

    private String email;
    private String nickName;

    @JsonIgnore
    @Override
    public String getName() {
        return email;
    }

    public static UserDetail from(User user) {
        return UserDetail.builder()
                .user(user)
                .email(user.getEmail())
                .nickName(user.getNickName())
                .build();
    }





}
