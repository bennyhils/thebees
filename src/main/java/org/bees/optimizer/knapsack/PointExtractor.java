package org.bees.optimizer.knapsack;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bees.optimizer.model.external.*;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
public class PointExtractor {
    private static Map<Integer, SackPoint> sackPointMap = new HashMap<>();
    private Cacher cacher;
    private int fromPoint;

    public List<SackPoint> extractPoints(
            List<RouteDto> routesDto,
            PointsDto pointsDto,
            List<TrafficJamDto> trafficDto,
            int depth,
            Stack<Integer> pointStack
    ) {
        return extractPointsInner(
                getJamedRouteDtoList(
                        filterByFirstPoint(routesDto, pointStack), trafficDto), pointsDto, new ArrayList<>(), depth, pointStack);
    }

    private List<RouteDto> filterByFirstPoint(List<RouteDto> routesDto, Stack<Integer> pointStack) {
        return routesDto.stream().filter(routeDto -> routeDto.getFrom() == pointStack.peek()).collect(Collectors.toList());
    }

    public List<SackPoint> extractPointsInner(
            List<RouteDto> routesDto,
            PointsDto pointsDto,
            List<SackPoint> sackPointList,
            int depth,
            Stack<Integer> pointStack) {

        routesDto.stream().filter(routeDto -> !isValueInStack(routeDto.getTo(), pointStack)).forEach(routeDto -> {
            PointDto pointDto = getPointDto(routeDto);
            sackPointMap.put(routeDto.getTo(), new SackPoint(Collections.singletonList(pointDto.getIndex()), pointDto.getMoney(), routeDto.getTime()));
            pointStack.add(routeDto.getTo());

            doCreate(routesDto, pointsDto, pointStack, depth, sackPointList);
        });

        return sackPointList;
    }

    private void doCreate(
            List<RouteDto> routeDtoList,
            PointsDto pointsDto,
            Stack<Integer> pointStack,
            int depth,
            List<SackPoint> sackPointList) {

        if (depth > 1) {
            extractPointsInner(routeDtoList, pointsDto, sackPointList, depth - 1, pointStack);
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
        for (Integer pointNum : pointStack) {
            if (pointNum != fromPoint) {
                money += sackPointMap.get(pointNum).money;
                time += sackPointMap.get(pointNum).time;
            }
        }

        sackPointList.add(new SackPoint(new ArrayList<>(pointStack), money, time));
    }

    // TODO map in stream
    private List<RouteDto> getJamedRouteDtoList(List<RouteDto> routeDtoList, List<TrafficJamDto> trafficDto) {
        List<RouteDto> newRouteDtoList = new ArrayList<>();
        routeDtoList.forEach(routeDto -> trafficDto.forEach(trafficJamDto -> {
            int from = routeDto.getFrom();
            int to = routeDto.getTo();

            if (from == trafficJamDto.getFrom() && to == trafficJamDto.getTo()) {
                newRouteDtoList.add(new RouteDto(from, to, (int) (routeDto.getTime() * trafficJamDto.getJam())));
            }
        }));
        return newRouteDtoList;
    }

    private PointDto getPointDto(RouteDto routeDto) {
        return cacher.getIntPointMap().get(routeDto.getTo());
    }

    private boolean isValueInStack(int value, Stack<Integer> stack) {
        for (Integer integer : stack) {
            if (integer.equals(value)) {
                return true;
            }
        }
        return false;
    }
}