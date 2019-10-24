package org.bees.optimizer.model.external;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class Points {
    private final List<Point> points;

    @JsonCreator
    public Points(@JsonProperty("points") final List<Point> points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return points.stream().map(p -> p.toString()).collect(Collectors.joining("\n"));
    }
}
