package org.bees.optimizer.model.external;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class Routes {
    private final List<Route> routes;

    @JsonCreator
    public Routes(@JsonProperty("routes") final List<Route> routes) {
        this.routes = routes;
    }

    @Override
    public String toString() {
        return routes.stream().map(r -> r.toString()).collect(Collectors.joining("\n"));
    }
}
