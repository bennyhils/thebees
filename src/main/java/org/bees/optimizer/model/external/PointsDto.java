package org.bees.optimizer.model.external;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PointsDto {
    private final List<PointDto> points;

    @JsonCreator
    public PointsDto(@JsonProperty("points") final List<PointDto> points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return points.stream().map(p -> p.toString()).collect(Collectors.joining("\n"));
    }
}
