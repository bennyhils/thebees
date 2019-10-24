package org.bees.optimizer.server;

import lombok.Getter;
import org.bees.optimizer.model.external.PointDto;
import org.bees.optimizer.model.external.PointsDto;
import org.bees.optimizer.model.external.RoutesDto;
import org.bees.optimizer.model.external.TrafficDto;

import java.util.HashMap;
import java.util.Map;

@Getter
public class Cacher {
    private RoutesDto routesDto;
    private PointsDto pointsDto;
    private TrafficDto trafficDto;

    Map<Integer, PointDto> intPointMap = new HashMap<>();

    public Cacher(RoutesDto routesDto, PointsDto pointsDto, TrafficDto trafficDto) {
        this.routesDto = routesDto;
        this.pointsDto = pointsDto;
        this.trafficDto = trafficDto;

        createRoutePointCache();
    }

    private void createRoutePointCache() {
        routesDto.getRoutes().forEach(routeDto -> pointsDto.getPoints().forEach(pointDto -> {
            if (routeDto.getFrom() == pointDto.getIndex()) {
                putToRoutePointMap(routeDto.getFrom(), pointDto);
            }
            if (routeDto.getTo() == pointDto.getIndex()) {
                putToRoutePointMap(routeDto.getTo(), pointDto);
            }
        }));
    }

    private void putToRoutePointMap(int key, PointDto pointDto) {
        if(!intPointMap.containsKey(key)) {
            intPointMap.put(key, pointDto);
        }
    }
}