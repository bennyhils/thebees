package org.bees.optimizer.model.external;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class Traffic {
    private final List<TrafficJam> traffic;

    @JsonCreator
    public Traffic(@JsonProperty("traffic") @JsonAlias("trafficjam") final List<TrafficJam> traffic) {
        this.traffic = traffic;
    }


    @Override
    public String toString() {
        return traffic.stream().map(t -> t.toString()).collect(Collectors.joining("\n"));
    }
}
