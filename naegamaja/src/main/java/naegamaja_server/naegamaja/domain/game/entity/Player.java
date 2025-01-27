package naegamaja_server.naegamaja.domain.game.entity;

import lombok.*;
import naegamaja_server.naegamaja.domain.game.model.TeamColor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Data
public class Player {
    private Long number;
    private String nickname;
    private TeamColor teamColor;
    private Long money;
    private Boolean isSpy;
}
