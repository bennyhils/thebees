package org.bees.optimizer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.bees.optimizer.model.external.ArriveDto;
import org.bees.optimizer.model.external.OverallSum;
import org.bees.optimizer.model.external.PointsDto;
import org.bees.optimizer.model.external.RoutesDto;
import org.bees.optimizer.model.external.TokenDto;
import org.bees.optimizer.model.external.TrafficDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class MessageDispatcher {

    private final ObjectMapper mapper = new ObjectMapper();
    private Solver solver;

    @Autowired
    public void setSolver(Solver solver) {
        this.solver = solver;
    }

    public void dispatchMessage(String message) {
        log.info("Got message: {}", message);
        try {
            ArriveDto arriveDto = mapper.readValue(message, ArriveDto.class);
            new Thread(() -> solver.processArrive(arriveDto)).start();
            return;
        } catch (IOException e) {
            log.debug("It's not ArriveDto");
        }

        try {
            TrafficDto trafficDto = mapper.readValue(message, TrafficDto.class);
            new Thread(() -> solver.processTraffic(trafficDto)).start();
            return;
        } catch (IOException e) {
            log.debug("It's not TrafficDto");
        }

        try {
            PointsDto pointsDto = mapper.readValue(message, PointsDto.class);
            new Thread(() -> solver.processPoints(pointsDto)).start();
            return;
        } catch (IOException e) {
            log.debug("It's not PointsDto");
        }

        try {
            RoutesDto routesDto = mapper.readValue(message, RoutesDto.class);
            new Thread(() -> solver.processRoutes(routesDto)).start();
            return;
        } catch (IOException e) {
            log.debug("It's not RoutesDto");
        }

        try {
            TokenDto tokenDto = mapper.readValue(message, TokenDto.class);
            new Thread(() -> solver.processToken(tokenDto)).start();
            return;
        } catch (IOException e) {
            log.debug("It's not OverallSum");
        }

        try {
            OverallSum overallSum = mapper.readValue(message, OverallSum.class);
            new Thread(() -> solver.processOverallSum(overallSum)).start();
            return;
        } catch (IOException e) {
            log.debug("It's not OverallSum");
        }

        log.error("No one dto matched!!!");
    }

}
