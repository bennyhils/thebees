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
    public PointsDto(@JsonProperty("pointList") final List<PointDto> pointList) {
        this.points = pointList;
    }

    @Override
    public String toString() {
        return points.stream().map(p -> p.toString()).collect(Collectors.joining("\n"));
    }
}
