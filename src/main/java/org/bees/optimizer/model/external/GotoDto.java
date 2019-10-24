package org.bees.optimizer.model.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class GotoDto {
    @JsonProperty("goto")
    private final int point;
    @JsonProperty("car")
    private final String carName;

    public GotoDto(final int point, final String carName) {
        this.point = point;
        this.carName = carName;
    }
}
