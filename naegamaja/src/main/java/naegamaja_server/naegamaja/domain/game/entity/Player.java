package naegamaja_server.naegamaja.domain.game.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("spy")
    private Boolean isSpy;

    @JsonProperty("eliminated")
    private Boolean isEliminated;
}
