package org.bees.optimizer.model.external;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public class RoutesDto {
    private final List<RouteDto> routes;

    @JsonCreator
    public RoutesDto(@JsonProperty("routes") final List<RouteDto> routes) {
        this.routes = routes;
    }

    @Override
    public String toString() {
        return routes.stream().map(r -> r.toString()).collect(Collectors.joining("\n"));
    }

    public List<DestinationDto> getDestinationsFrom(int i) {
        return routes.stream()
                     .filter(r -> r.getFrom() == i && r.getTo() != i)
                     .map(r -> new DestinationDto(r.getTo(), r.getTime()))
                     .collect(Collectors.toList());
    }

    @EqualsAndHashCode
    @AllArgsConstructor
    public static class DestinationDto {
        private final int to;
        private final int time;
    }

    public RoutesDto getExtendedRoutes() {
        return new RoutesDto(
                routes.stream()
                      .flatMap(r -> Stream.of(
                              new RouteDto(r.getFrom(), r.getTo(), r.getTime()),
                              new RouteDto(r.getTo(), r.getFrom(), r.getTime())
                      ))
                      .collect(Collectors.toList())
        );
    }
}
