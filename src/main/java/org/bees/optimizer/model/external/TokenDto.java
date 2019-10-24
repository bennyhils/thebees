package org.bees.optimizer.model.external;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class TokenDto {

    private final String token;
    private final List<String> cars;
    private final int level;

    @JsonCreator
    public TokenDto(
            @JsonProperty("token") final String token,
            @JsonProperty("cars") final List<String> cars,
            @JsonProperty("level") final int level
    ) {
        this.token = token;
        this.cars = cars;
        this.level = level;
    }
}
