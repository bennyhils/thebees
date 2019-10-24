package org.bees.optimizer.model.external;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ArriveDto {
    private final int point;
    private final String carName;

    @JsonCreator
    public ArriveDto(
            @JsonProperty("point") final int point,
            @JsonProperty("car") final String carName
    ) {
        this.point = point;
        this.carName = carName;
    }
}