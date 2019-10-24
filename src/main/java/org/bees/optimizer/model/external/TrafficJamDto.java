package org.bees.optimizer.model.external;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class TrafficJamDto {
    private final int from;
    private final int to;
    private final double jam;

    @JsonCreator
    public TrafficJamDto(
            @JsonProperty("a") final int from,
            @JsonProperty("b") final int to,
            @JsonProperty("jam") final double jam
    ) {
        this.from = from;
        this.to = to;
        this.jam = jam;
    }
}
