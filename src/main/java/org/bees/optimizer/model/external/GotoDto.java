package org.bees.optimizer.model.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class GotoDto {
    @JsonProperty("goto")
    private final int point;
    @JsonProperty("car")
    private final String carName;
}
