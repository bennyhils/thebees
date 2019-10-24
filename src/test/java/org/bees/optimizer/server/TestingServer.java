package org.bees.optimizer.server;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.bees.optimizer.knapsack.SackPoint;
import org.bees.optimizer.knapsack.TimeKnapsack;
import org.bees.optimizer.knapsack.TimeKnapsack.Find;
import org.bees.optimizer.model.external.*;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Stack;

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

        Cacher cacher = new Cacher(routesDto, pointsDto, trafficDto);
        PointExtractor pointExtractor = new PointExtractor(cacher);

        Stack<Integer> pointStack = new Stack<>();
        pointStack.add(0);

        List<SackPoint> sackPoints = pointExtractor.extractPoints(routesDto, pointsDto, trafficDto, 3, pointStack);
        for (SackPoint sackPoint : sackPoints) {
            System.out.println(sackPoint);
        }

        Find find = TimeKnapsack.find(sackPoints, 200);
        find.display();
    }

}