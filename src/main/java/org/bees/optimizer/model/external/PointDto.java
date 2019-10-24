package org.bees.optimizer.model.external;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PointDto {
    private final int index;
    private final long money;

    @JsonCreator
    public PointDto(
            @JsonProperty("p") final int index,
            @JsonProperty("money") final long money
    ) {
        this.index = index;
        this.money = money;
    }
}
