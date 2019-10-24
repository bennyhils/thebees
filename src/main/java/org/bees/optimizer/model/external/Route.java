package org.bees.optimizer.model.external;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Route {
    private final int from;
    private final int to;
    private final int time;

    @JsonCreator
    public Route(
            @JsonProperty("a") final int from,
            @JsonProperty("b") final int to,
            @JsonProperty("time") final int time
    ) {
        this.from = from;
        this.to = to;
        this.time = time;
    }
}
