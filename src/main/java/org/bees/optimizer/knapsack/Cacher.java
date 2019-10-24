package org.bees.optimizer.knapsack;

import lombok.Getter;
import org.bees.optimizer.model.external.PointDto;
import org.bees.optimizer.model.external.PointsDto;
import org.bees.optimizer.model.external.RouteDto;
import org.bees.optimizer.model.external.RoutesDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class Cacher {
    private List<RouteDto> routesDto;
    private PointsDto pointsDto;
    private Map<Integer, PointDto> intPointMap = new HashMap<>();

    public Cacher(List<RouteDto> routesDto, PointsDto pointsDto) {
        this.routesDto = routesDto;
        this.pointsDto = pointsDto;

        createRoutePointCache();
    }

    private void createRoutePointCache() {
        routesDto.forEach(routeDto -> pointsDto.getPoints().forEach(pointDto -> {
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