package org.bees.optimizer.model.external;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class Token {

    private final String token;
    private final List<String> cars;
    private final int level;

    @JsonCreator
    public Token(
            @JsonProperty("token") final String token,
            @JsonProperty("cars") final List<String> cars,
            @JsonProperty("level") final int level
    ) {
        this.token = token;
        this.cars = cars;
        this.level = level;
    }
}
