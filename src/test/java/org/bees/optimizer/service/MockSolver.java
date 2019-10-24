package org.bees.optimizer.service;

import lombok.extern.slf4j.Slf4j;
import org.bees.optimizer.model.external.ArriveDto;
import org.bees.optimizer.model.external.GotoDto;
import org.bees.optimizer.model.external.OverallSum;
import org.bees.optimizer.model.external.PointsDto;
import org.bees.optimizer.model.external.RoutesDto;
import org.bees.optimizer.model.external.TokenDto;
import org.bees.optimizer.model.external.TrafficDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class MockSolver implements Solver {

    private WebSocketHandler server;

    private AtomicInteger sendTimes = new AtomicInteger();

    @Autowired
    public void setServer(WebSocketHandler webSocketHandler) {
        this.server = webSocketHandler;
    }

    @Override
    public void processPoints(PointsDto pointsDto) {
        log.info("Got points: {}", pointsDto);
    }

    @Override
    public void processTraffic(TrafficDto trafficDto) {
        log.info("Got traffic: {}", trafficDto);
        try {
            synchronized (sendTimes) {switch (sendTimes.get()) {
                case 0:
                    server.sendCar(new GotoDto(2, "sb0"));
                    sendTimes.incrementAndGet();
                    break;
                case 1:
                    server.sendCar(new GotoDto(1, "sb0"));
                    sendTimes.incrementAndGet();
                    break;
                default:
                    // do nothing
            }}
        } catch (IOException e) {
            log.error("ВСЁ СЛОМАЛОСЬ БЛЯ");
        }
    }

    @Override
    public void processRoutes(RoutesDto routesDto) {
        log.info("Got routes: {}", routesDto);
    }

    @Override
    public void processArrive(ArriveDto arriveDto) {
        log.info("Got arrive: {}", arriveDto);
    }

    @Override
    public void processOverallSum(OverallSum overallSum) {
        log.info("Got sum: {}", overallSum);
    }

    @Override
    public void processToken(TokenDto tokenDto) {
        log.info("Got token: {}", tokenDto);
    }
}
