package naegamaja_server.naegamaja.domain.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Getter
@Setter
public class User {

    @Id
    private String email;

    private String nickname;

    private String password;
}
