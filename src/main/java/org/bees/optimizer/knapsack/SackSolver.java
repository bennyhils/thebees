package org.bees.optimizer.knapsack;

import lombok.extern.slf4j.Slf4j;
import org.bees.optimizer.model.external.*;
import org.bees.optimizer.service.Solver;
import org.bees.optimizer.service.WebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
@Slf4j
public class SackSolver implements Solver {
    private RoutesDto routesDto;
    private ArriveDto arriveDto;
    private OverallSum overallSum;
    private TokenDto tokenDto;
    private PointsDto pointsDto;
    private Cacher cacher;
    private SackPoint nextPoint;
    private Set<Integer> pointSet = new HashSet<Integer>();

    private final int DEPTH = 2;
    private int depthCount = 0;

    @Autowired
    private WebSocketHandler handler;
    private Integer gotoPoint = 0;


    @Override
    public void processPoints(PointsDto pointsDto) {
        this.pointsDto = pointsDto;
    }

    @Override
    public void processTraffic(TrafficDto trafficDto) {
        if (cacher == null) {
            cacher = new Cacher(routesDto, pointsDto);
        }

        if (depthCount % DEPTH == 0) {
            getNewRoute(trafficDto);
        }

        try {
            depthCount++;

            gotoPoint = nextPoint.name.get(depthCount % (DEPTH + 1));

            log.info("goto point " + gotoPoint);

            boolean contains = pointSet.contains(gotoPoint);

            pointSet.add(gotoPoint);

            log.info("point {} no money is {}", gotoPoint, contains);

            handler.sendCar(new GotoDto(gotoPoint, "sb0", contains));
        } catch (IOException e) {
            log.warn("error", e);
            throw new RuntimeException(e);
        }
    }

    private void getNewRoute(TrafficDto trafficDto) {
        depthCount = 0;

        // петли в графах
        PointExtractor pointExtractor = new PointExtractor(cacher, gotoPoint);
        Stack<Integer> pointStack = new Stack<>();
        pointStack.add(gotoPoint);

        List<SackPoint> sackPointList = getSackPoints(trafficDto, pointExtractor, pointStack);
        TimeKnapsack.Find find = TimeKnapsack.find(sackPointList, 100);

        nextPoint = find.getOne();

        log.info("Got point " + nextPoint);
    }

    @Override
    public void processRoutes(RoutesDto routesDto) {
        this.routesDto = routesDto;
    }

    @Override
    public void processArrive(ArriveDto arriveDto) {
        this.arriveDto = arriveDto;
    }

    @Override
    public void processOverallSum(OverallSum overallSum) {
        this.overallSum = overallSum;
    }

    @Override
    public void processToken(TokenDto tokenDto) {
        this.tokenDto = tokenDto;
    }

    private List<SackPoint> getSackPoints(TrafficDto trafficDto, PointExtractor pointExtractor, Stack<Integer> pointStack) {
        List<SackPoint> sackPointList = pointExtractor.extractPoints(routesDto, pointsDto, trafficDto, DEPTH, pointStack);
        for (SackPoint sackPoint : sackPointList) {
            log.debug(sackPoint.toString());
        }
        return sackPointList;
    }
}
