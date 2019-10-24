package org.bees.optimizer.server;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.bees.optimizer.knapsack.Point;
import org.bees.optimizer.knapsack.TimeKnapsack;
import org.bees.optimizer.knapsack.TimeKnapsack.Find;
import org.bees.optimizer.model.external.*;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.bees.optimizer.server.DataProvider.*;

@Slf4j
public class TestingServer {
    private final static ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testServer() throws IOException {
        start();
    }

    @Test
    public void go() throws IOException {
        ArriveDto arriveDto = mapper.readValue(getCarsum(), ArriveDto.class);
        System.out.println(arriveDto);
    }

    @Test
    public void go_first() throws IOException {
        OverallSum overallSum = mapper.readValue(getTeamSum(), OverallSum.class);
        System.out.println(overallSum);
    }

    private void start() throws IOException {
        RoutesDto routesDto = mapper.readValue(getRoutes(), RoutesDto.class);
        PointsDto pointsDto = mapper.readValue(getPoints(), PointsDto.class);
        TrafficDto trafficDto = mapper.readValue(getTraffic(), TrafficDto.class);

        List<Point> points = createPoints(routesDto, pointsDto, 0);
        Find find = TimeKnapsack.find(points, 200);
        find.display();
    }

    private List<Point> createPoints(RoutesDto routesDto, PointsDto pointsDto, int currentPoint) {
        List<Point> pointList = new ArrayList<>();

        routesDto.getRoutes().forEach(routeDto -> pointsDto.getPoints().forEach(pointDto -> {
            if (routeDto.getFrom() == currentPoint && pointDto.getIndex() == routeDto.getTo()) {
                pointList.add(new Point(routeDto.getFrom() + "_" + routeDto.getTo(), pointDto.getMoney(), routeDto.getTime()));
            }
        }));

        return pointList;
    }
}