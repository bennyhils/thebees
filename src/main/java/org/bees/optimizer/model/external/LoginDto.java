package org.bees.optimizer.model.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class LoginDto {
    @JsonProperty("team")
    private final String team;

    public LoginDto(final String team) {
        this.team = team;
    }
}
