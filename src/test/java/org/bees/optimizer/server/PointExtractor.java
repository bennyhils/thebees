package org.bees.optimizer.server;

import lombok.AllArgsConstructor;
import org.bees.optimizer.knapsack.SackPoint;
import org.bees.optimizer.model.external.*;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
public class PointExtractor {
    private static Map<Integer, SackPoint> pointDtoMap = new HashMap<>();
    private Cacher cacher;

    public List<SackPoint> extractPoints(
            RoutesDto routesDto,
            PointsDto pointsDto,
            TrafficDto trafficDto,
            int depth,
            Stack<Integer> pointStack
    ) {
        List<RouteDto> collect = routesDto.getRoutes().stream().filter(routeDto -> routeDto.getFrom() == pointStack.peek()).collect(Collectors.toList());

        return extractPointsInner(collect, pointsDto, trafficDto, new ArrayList<>(), depth, pointStack);
    }

    public List<SackPoint> extractPointsInner(
            List<RouteDto> routesDto,
            PointsDto pointsDto,
            TrafficDto trafficDto,
            List<SackPoint> sackPointList,
            int depth,
            Stack<Integer> pointStack) {

        routesDto.forEach(routeDto -> {
            PointDto pointDto = cacher.getIntPointMap().get(routeDto.getTo());
            pointDtoMap.put(routeDto.getTo(), new SackPoint(pointDto.getIndex() + "", pointDto.getMoney(), routeDto.getTime()));
            pointStack.add(routeDto.getTo());

            doCreate(routesDto, pointsDto, trafficDto, pointStack, depth, sackPointList, routeDto, pointDto);
        });

        return sackPointList;
    }

    private void doCreate(
            List<RouteDto> routeDtoList,
            PointsDto pointsDto,
            TrafficDto trafficDto,
            Stack<Integer> pointStack,
            int depth,
            List<SackPoint> sackPointList,
            RouteDto routeDto,
            PointDto pointDto) {

        if (depth > 1) {
            extractPointsInner(routeDtoList, pointsDto, trafficDto, sackPointList, depth - 1, pointStack);
        } else {
            createPoint(pointStack, sackPointList);
        }

        pointStack.pop();
    }

    private void createPoint(
            Stack<Integer> pointStack,
            List<SackPoint> sackPointList) {
        int money = 0;
        int time = 0;
        StringBuilder pointNameBuilder = new StringBuilder();

        for (Integer pointNum : pointStack) {
            if(pointNum != 0) {
                pointNameBuilder.append(pointNum).append("_");
                money += pointDtoMap.get(pointNum).money;
                time += pointDtoMap.get(pointNum).time;
            }
        }

        sackPointList.add(new SackPoint(pointNameBuilder.toString(), money, time));
    }
}
