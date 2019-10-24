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

    private WebSocketHandler webSocketHandler;

    @Autowired
    public void setWebSocketHandler(WebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    public void dispatchMessage(String message) {
        log.info("Got message: {}", message);
        try {
            ArriveDto arriveDto = mapper.readValue(message, ArriveDto.class);
            solver.processArrive(arriveDto);
            return;
        } catch (IOException e) {
            log.debug("It's not ArriveDto");
        }

        try {
            TrafficDto trafficDto = mapper.readValue(message, TrafficDto.class);
            solver.processTraffic(trafficDto);
            return;
        } catch (IOException e) {
            log.debug("It's not TrafficDto");
        }

        try {
            PointsDto pointsDto = mapper.readValue(message, PointsDto.class);
            solver.processPoints(pointsDto);
            return;
        } catch (IOException e) {
            log.debug("It's not PointsDto");
        }

        try {
            RoutesDto routesDto = mapper.readValue(message, RoutesDto.class);
            solver.processRoutes(routesDto);
            return;
        } catch (IOException e) {
            log.debug("It's not RoutesDto");
        }

        try {
            TokenDto tokenDto = mapper.readValue(message, TokenDto.class);
            webSocketHandler.saveToken(tokenDto);
            solver.processToken(tokenDto);
            return;
        } catch (IOException e) {
            log.debug("It's not OverallSum");
        }

        try {
            OverallSum overallSum = mapper.readValue(message, OverallSum.class);
            solver.processOverallSum(overallSum);
            return;
        } catch (IOException e) {
            log.debug("It's not OverallSum");
        }

        log.error("No one dto matched!!!");
    }

}
