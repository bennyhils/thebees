package org.bees.optimizer.server;

import lombok.AllArgsConstructor;
import org.bees.optimizer.knapsack.SackPoint;
import org.bees.optimizer.model.external.*;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
public class PointExtractor {
    private static Map<Integer, SackPoint> sackPointMap = new HashMap<>();
    private Cacher cacher;

    public List<SackPoint> extractPoints(
            RoutesDto routesDto,
            PointsDto pointsDto,
            TrafficDto trafficDto,
            int depth,
            Stack<Integer> pointStack
    ) {
        return extractPointsInner(
                getJamedRouteDtoList(
                        exceptFirstPoint(routesDto, pointStack), trafficDto), pointsDto, new ArrayList<>(), depth, pointStack);
    }

    private List<RouteDto> exceptFirstPoint(RoutesDto routesDto, Stack<Integer> pointStack) {
        return routesDto.getRoutes().stream().filter(routeDto -> routeDto.getFrom() == pointStack.peek()).collect(Collectors.toList());
    }

    public List<SackPoint> extractPointsInner(
            List<RouteDto> routesDto,
            PointsDto pointsDto,
            List<SackPoint> sackPointList,
            int depth,
            Stack<Integer> pointStack) {

        routesDto.stream().filter(routeDto -> !isValueInStack(routeDto.getTo(), pointStack)).forEach(routeDto -> {
            PointDto pointDto = getPointDto(routeDto);
            sackPointMap.put(routeDto.getTo(), new SackPoint(pointDto.getIndex() + "", pointDto.getMoney(), routeDto.getTime()));
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
        StringBuilder pointNameBuilder = new StringBuilder();

        for (Integer pointNum : pointStack) {
            if (pointNum != 0) {
                pointNameBuilder.append(pointNum).append("_");
                money += sackPointMap.get(pointNum).money;
                time += sackPointMap.get(pointNum).time;
            }
        }

        sackPointList.add(new SackPoint(pointNameBuilder.toString(), money, time));
    }

    // TODO map in stream
    private List<RouteDto> getJamedRouteDtoList(List<RouteDto> routeDtoList, TrafficDto trafficDto) {
        List<RouteDto> newRouteDtoList = new ArrayList<>();
        routeDtoList.forEach(routeDto -> trafficDto.getTraffic().forEach(trafficJamDto -> {
            int to = routeDto.getTo();
            int from = routeDto.getFrom();
            if (to == trafficJamDto.getTo() && from == trafficJamDto.getFrom()) {
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