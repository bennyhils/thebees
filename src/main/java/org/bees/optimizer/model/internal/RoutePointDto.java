package org.bees.optimizer.model.internal;

import org.bees.optimizer.model.ModelConverter;
import org.bees.optimizer.model.external.PointDto;
import org.bees.optimizer.model.external.PointsDto;
import org.bees.optimizer.model.external.RouteDto;
import org.bees.optimizer.model.external.RoutesDto;

import java.util.List;
import java.util.stream.Collectors;

public class RoutePointDto {
    private final PointDto from;
    private final PointDto to;
    private final int time;

    public RoutePointDto(final PointDto from, final PointDto to, final int time) {
        this.from = from;
        this.to = to;
        this.time = time;
    }

    public static List<RoutePointDto> getPointsFrom(PointsDto pointsDto, RoutesDto routesDto, int from) {
        long[] money = ModelConverter.convertPoints(pointsDto);
        return routesDto.getRoutes()
                 .stream()
                 .filter(r -> r.getFrom() == from || r.getTo() == from)
                    .map(r -> {
                        if (r.getFrom() != from) return new RouteDto(r.getTo(), r.getFrom(), r.getTime());
                        else return r;
                    })
                 .map(r -> new RoutePointDto(
                         new PointDto(r.getFrom(), money[r.getFrom()]),
                         new PointDto(r.getTo(), money[r.getTo()]),
                         r.getTime())
                 ).collect(Collectors.toList());

    }
}
