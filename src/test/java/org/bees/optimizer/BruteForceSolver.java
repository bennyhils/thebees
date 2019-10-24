package org.bees.optimizer;

import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.bees.optimizer.model.ModelConverter;
import org.bees.optimizer.model.external.ArriveDto;
import org.bees.optimizer.model.external.GotoDto;
import org.bees.optimizer.model.external.OverallSum;
import org.bees.optimizer.model.external.PointsDto;
import org.bees.optimizer.model.external.RoutesDto;
import org.bees.optimizer.model.external.TokenDto;
import org.bees.optimizer.model.external.TrafficDto;
import org.bees.optimizer.service.Solver;
import org.bees.optimizer.service.WebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Stack;

@Slf4j
public class BruteForceSolver implements Solver {

    private WebSocketHandler webSocketHandler;

    @Autowired
    public void setWebSocketHandler(WebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    private String carName;
    private int elapsedTime = 0;
    private int depth = 5;
    private int currentPoint = 0;

    private long[] money;
    private double[][] traffic;
    private int[][] times;

    @Override
    public void processPoints(PointsDto pointsDto) {
        this.money = ModelConverter.convertPoints(pointsDto);
    }

    @Override
    public void processTraffic(TrafficDto trafficDto) {
        this.traffic = ModelConverter.convertTraffic(trafficDto);

        try {
            webSocketHandler.sendCar(new GotoDto(
                    calculateNextPoint(currentPoint),
                    "sb0"
            ));
        } catch (IOException e) {
            log.error("! ", e);
        }
    }

    @Override
    public void processRoutes(RoutesDto routesDto) {
        this.times = ModelConverter.convertRoutes(routesDto);
    }

    @Override
    public void processArrive(ArriveDto arriveDto) {
        this.currentPoint = arriveDto.getPoint();
        log.info("CarSum: {}", arriveDto.getCarSum());
    }

    @Override
    public void processOverallSum(OverallSum overallSum) {
        log.info("Got sum {} with time {}", overallSum.getTeamSum(), elapsedTime);
    }

    @Override
    public void processToken(TokenDto tokenDto) {
        this.carName = tokenDto.getCars().get(0);
    }

    private int calculateNextPoint(int curPoint) {
        if (depth == 0) {
            return 1;
        }

        Result result = fun(depth, new Stack<>(), curPoint);
        log.info("CurrentResult: {}", result);
        money[result.firstPoint] = 0;
        --depth;
        this.currentPoint = result.firstPoint;
        return result.firstPoint;
    }

    private Result fun(int depth, Stack<Integer> indexes, int curPoint) {
        if (depth == 0) {
            return calculateResult(indexes, curPoint);
        }
        Result best = new Result(Integer.MAX_VALUE, 0L, -1);
        for (int i = 2; i < money.length; ++i) {
            if (!indexes.contains(i)) {
                indexes.push(i);
                Result result = fun(depth - 1, indexes, curPoint);
                if (result.money > best.money) {
                    best = result;
                }
                indexes.pop();
            }
        }
        return best;
    }

    private Result calculateResult(Stack<Integer> indexes, int curPoint) {
        int accTime = 0;
        long accMoney = 0;
        int lastIndex = curPoint;
        int firstPoint = curPoint;
        for (int i : indexes) {
            accTime += times[lastIndex][i] * traffic[lastIndex][i];
            accMoney += money[i];
            lastIndex = i;
            if (firstPoint == curPoint) firstPoint = i;
        }
        return new Result(accTime, accMoney, firstPoint);
    }

    @ToString
    @AllArgsConstructor
    private static class Result {
        private final int time;
        private final long money;
        private final int firstPoint;
    }


}
