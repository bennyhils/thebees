package org.bees.optimizer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bees.optimizer.model.ModelConverter;
import org.bees.optimizer.model.external.*;
import org.bees.optimizer.model.internal.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Profile("markov")
public class RouteSolver implements Solver {
    @Value("${org.bees.gamma}")
    private double gamma;
    @Value("${org.bees.max_recursion_number}")
    private int maxRecursionNumber;

    private boolean isFirstExecution = true;

    private PointsDto pointsDto;
    private RoutesDto routesDto;
    private TokenDto tokenDto;

    private RouteBuilder routeBuilder;
    private List<Car> cars;

    private WebSocketHandler server;

    private Map<String, ArriveDto> readyForProcess = new HashMap<>();

    private double timeBegin;
    private long overallSum = 0;

    @Autowired
    public void setServer(WebSocketHandler webSocketHanler) {
        this.server = webSocketHanler;
    }


    @Override
    public void processPoints(PointsDto pointsDto) {
        this.pointsDto = pointsDto;
        log.debug("process points dto! {}, time {}", pointsDto, System.currentTimeMillis() - timeBegin);
        initIfReady();
    }

    private void makeDecision(ArriveDto arriveDto, TrafficDto trafficDto) {
        Car car = cars.stream().filter(value -> value.getName().equals(arriveDto.getCarName())).findAny().get();
        makeDecision(car, arriveDto.getPoint(),car.getCapacity() - arriveDto.getCarSum(), ModelConverter.convertTraffic(trafficDto));
    }

    private void makeDecision(Car car, int currentPoint, long remainCapacity, double[][] trafficJams) {
        GotoDto gotoDto = routeBuilder.makeDecision(car, currentPoint, remainCapacity, trafficJams);

        server.sendCar(gotoDto);
    }

    @Override
    public synchronized void processTraffic(TrafficDto trafficDto) {
        log.debug("process trafficdto isFirstExecution{} {}, time {}", isFirstExecution, trafficDto, System.currentTimeMillis() - timeBegin);
        initIfReady();
        if (isFirstExecution) {
            routeBuilder.makeFirstDecision(ModelConverter.convertTraffic(trafficDto)).forEach(gotoDto -> server.sendCar(gotoDto));
            isFirstExecution = false;
        } else {
            readyForProcess.forEach((s, arriveDto) -> makeDecision(arriveDto, trafficDto));
            readyForProcess.clear();
        }

    }

    @Override
    public void processRoutes(RoutesDto routesDto) {
        log.debug("process routesDto {}, time {}", routesDto, System.currentTimeMillis() - timeBegin);
        this.routesDto = routesDto;
        initIfReady();
    }

    @Override
    public void processArrive(ArriveDto arriveDto) {
        log.debug("process arriveDto {}, time {}", arriveDto, System.currentTimeMillis() - timeBegin);
        readyForProcess.put(arriveDto.getCarName(), arriveDto);
    }

    @Override
    public void processOverallSum(OverallSum overallSum) {
        log.debug("process overallSum {}, time {}", overallSum, System.currentTimeMillis() - timeBegin);
        this.overallSum = overallSum.getTeamSum();
    }

    @Override
    public void processToken(TokenDto tokenDto) {
        log.debug("process tokenDto {}", tokenDto);
        timeBegin = System.currentTimeMillis();

        this.tokenDto = tokenDto;
        this.cars = ModelConverter.convertTokenToCar(tokenDto);
        initIfReady();
    }

    private void initIfReady() {
        if (isReadyForInitialize() && routeBuilder == null) {
            routeBuilder = new RouteBuilder(ModelConverter.convertRoutes(routesDto), ModelConverter.convertPoints(pointsDto), ModelConverter.convertTokenToCar(tokenDto), gamma, maxRecursionNumber);
        }
    }

    private boolean isReadyForInitialize() {
        return pointsDto != null && routesDto != null && tokenDto != null;
    }
}
