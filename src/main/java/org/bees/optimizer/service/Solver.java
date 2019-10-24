package org.bees.optimizer.service;

import org.bees.optimizer.model.external.ArriveDto;
import org.bees.optimizer.model.external.OverallSum;
import org.bees.optimizer.model.external.PointsDto;
import org.bees.optimizer.model.external.RoutesDto;
import org.bees.optimizer.model.external.TokenDto;
import org.bees.optimizer.model.external.TrafficDto;

public interface Solver {
    void processPoints(PointsDto pointsDto);
    void processTraffic(TrafficDto trafficDto);
    void processRoutes(RoutesDto routesDto);
    void processArrive(ArriveDto arriveDto);
    void processOverallSum(OverallSum overallSum);
    void processToken(TokenDto tokenDto);
}
