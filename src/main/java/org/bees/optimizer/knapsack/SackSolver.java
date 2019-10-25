package org.bees.optimizer.knapsack;

import lombok.extern.slf4j.Slf4j;
import org.bees.optimizer.model.external.*;
import org.bees.optimizer.service.Solver;
import org.bees.optimizer.service.WebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.*;

import static org.bees.optimizer.knapsack.graph.ShortPath.getShortestPath;

@Component
@Slf4j
@Profile("sack")
public class SackSolver implements Solver {
    private List<RouteDto> routesDto;
    private List<TrafficJamDto> trafficDtoList;
    private ArriveDto arriveDto;
    private OverallSum overallSum;
    private TokenDto tokenDto;
    private PointsDto pointsDto;
    private Cacher cacher;
    private SackPoint nextPoint;
    private Set<Integer> pointSet = new HashSet<Integer>();
    private List<String> shortestPath = new ArrayList<>();

    private final int DEPTH = 3;
    private int depthCount = 0;
    private int stepCount = 0;

    @Autowired
    private WebSocketHandler handler;
    private Integer gotoPoint = 0;


    @Override
    public void processPoints(PointsDto pointsDto) {
        this.pointsDto = pointsDto;
    }

    @Override
    public void processTraffic(TrafficDto trafficDto) {
        trafficDtoList = trafficDto.getExtendedTraffic().getTraffic();

        if (cacher == null) {
            cacher = new Cacher(routesDto, pointsDto);
        }

        if (depthCount % DEPTH == 0) {
            getNewRoute(trafficDtoList);
        }

        try {
            depthCount++;

            gotoPoint = nextPoint.name.get(depthCount % (DEPTH + 1));

            log.info("goto point " + gotoPoint);

            boolean contains = pointSet.contains(gotoPoint);

            pointSet.add(gotoPoint);

            log.info("point {} no money is {}", gotoPoint, contains);

            handler.sendCar(new GotoDto(gotoPoint, "sb0", contains));
        } catch (Exception e) {
            log.warn("error", e);
            throw new RuntimeException(e);
        }
    }

    private void getNewRoute(List<TrafficJamDto> trafficDto) {
        depthCount = 0;

        // петли в графах
        PointExtractor pointExtractor = new PointExtractor(cacher, gotoPoint);
        Stack<Integer> pointStack = new Stack<>();
        pointStack.add(gotoPoint);

        List<SackPoint> sackPointList = getSackPoints(trafficDto, pointExtractor, pointStack);
        TimeKnapsack.Find find = TimeKnapsack.find(sackPointList, 100);

        nextPoint = find.getOne();

        log.info("Got point " + nextPoint + " step count " + ++stepCount);
    }

    @Override
    public void processRoutes(RoutesDto routesDto) {
        this.routesDto = routesDto.getExtendedRoutes().getRoutes();
    }

    @Override
    public void processArrive(ArriveDto arriveDto) {
        this.arriveDto = arriveDto;
        if (this.arriveDto.getCarSum() >= 600_000) {
            handler.sendCar(new GotoDto(1, "sb0"));
        }
    }


    @Override
    public void processOverallSum(OverallSum overallSum) {
        this.overallSum = overallSum;
    }

    @Override
    public void processToken(TokenDto tokenDto) {
        this.tokenDto = tokenDto;
    }

    private List<SackPoint> getSackPoints(List<TrafficJamDto> trafficDto, PointExtractor pointExtractor, Stack<Integer> pointStack) {
        List<SackPoint> sackPointList = pointExtractor.extractPoints(routesDto, pointsDto, trafficDto, DEPTH, pointStack, pointSet);
        return sackPointList;
    }
}
