package org.bees.optimizer.model.external;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class OverallSum {
    private final long teamSum;

    @JsonCreator
    public OverallSum(@JsonProperty("teamsum") final long teamSum) {
        this.teamSum = teamSum;
    }
}
