package org.bees.optimizer.model.external;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class TrafficDto {
    private final List<TrafficJamDto> traffic;

    @JsonCreator
    public TrafficDto(@JsonProperty("traffic") final List<TrafficJamDto> traffic) {
        this.traffic = traffic;
    }


    @Override
    public String toString() {
        return traffic.stream().map(TrafficJamDto::toString).collect(Collectors.joining("\n"));
    }
}
