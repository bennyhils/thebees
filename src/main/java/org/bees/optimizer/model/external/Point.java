package org.bees.optimizer.model.external;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Point {
    private final int index;
    private final long money;

    @JsonCreator
    public Point(
            @JsonProperty("p") final int index,
            @JsonProperty("money") final long money
    ) {
        this.index = index;
        this.money = money;
    }
}
