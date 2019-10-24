package org.bees.optimizer.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.bees.optimizer.model.external.GotoDto;
import org.bees.optimizer.model.internal.Car;

import java.util.*;

@Slf4j
public class RouteBuilder {
//    final double[][][]  weightedMatrix;
    private final int[][] routes;
    /*первый элемент откуда стартует, второй куда отвозим деньги*/
    private final long[] points;
    private final double gamma;
    private final int maxRecursionIterations;
    private final List<Car> cars;

    public RouteBuilder(int[][] routes, long[] points, List<Car> cars, double gamma, int maxRecursionIterations) {
        if (!(points.length == routes[0].length && routes.length == points.length)) {
            throw new IllegalArgumentException(String.format("Dimension should be the same routes %d, routes[0] %s, nodes %d, cars %d", routes.length, routes[0].length, points.length, cars.size()));
        }

        this.routes = routes;
        this.points = points;
        this.gamma = gamma;
        this.maxRecursionIterations = maxRecursionIterations;
        this.cars = cars;

        log.debug("initialize routeBuilder model " + toString());

    }

   public List<GotoDto> makeFirstDecision(double[][] trafficJams) {
        List<GotoDto> gotoDtos = new ArrayList<>(cars.size());

        cars.forEach(car-> gotoDtos.add(makeDecision(car, 0, car.getCapacity(), trafficJams)));

        return gotoDtos;
   }

   public synchronized GotoDto makeDecision(Car car, int currentPoint, int remainCapacity, double[][] trafficJams) {

        Solution bestSolution = getBestSolution(car.getCapacity(), currentPoint, remainCapacity, trafficJams, 0);
        synchronized (points) {

        }
        points[bestSolution.point] = 0;

        ///todo add hash to calculated states

        return new GotoDto(bestSolution.point, car.getName());
    }

    public Solution getBestSolution(long carCapacity, int currentPoint, long remainCapacity, double[][] trafficJams, int currentRecursion) {
        log.debug("currentPoint {}, remainCapacity {}, currentRecursion {}", currentPoint, remainCapacity, currentRecursion);
        if (currentRecursion >= maxRecursionIterations) {
            log.debug("current recursion {} more than max {}", currentRecursion, maxRecursionIterations);
            return null;
        }

        /*1 - нода, куда отвозят деньги*/
        double maxValue = valueFunction(trafficJams[currentPoint][1], routes[currentPoint][1], carCapacity - remainCapacity);;
        int bestRoute = 1;

        log.debug("init max value to bank {}, currentPoint {}, remainCapacity {}, currentRecursion {}",maxValue, currentPoint, remainCapacity, currentRecursion);

        ///TODO проверка если осталось мало времени моделированиђ
        //пересчитываем веса у возможных состояний
        for (int j = 2; j < points.length; j++) {
            if (currentPoint == j) {
                continue;
            }

            if (remainCapacity >= points[j]) {
                double pointValue = valueFunction(trafficJams[currentPoint][j], routes[currentPoint][j], points[j]);
                log.debug("value {} from current {} to point {}", pointValue, currentPoint, j);

                Solution predictedPointSolution = getBestSolution(carCapacity,  j, remainCapacity - points[j], trafficJams, ++currentRecursion);
                log.debug("predictedPointSolution {} from current {} to point {}", predictedPointSolution, currentPoint, j);
                if (predictedPointSolution != null) {
                    pointValue = pointValue + Math.pow(gamma, currentRecursion) * predictedPointSolution.value;
                }

                if (maxValue < pointValue) {
                    maxValue = pointValue;
                    bestRoute = j;
                    log.debug("change max value {}, route {}", maxValue, bestRoute);
                }
            }
        }

        return new Solution(bestRoute, maxValue);
    }

    @Getter
    @AllArgsConstructor
    public class Solution {
        final int point;
        final double value;

        @Override
        public String toString() {
            return "Solution{" +
                    "point=" + point +
                    ", value=" + value +
                    '}';
        }
    }

    public double valueFunction(double jam, int route, long money) {
        return (double) money / (route * jam);
    }

    @Override
    public String toString() {
        return "RouteBuilder{" +
                "routes=" + Arrays.toString(routes) +
                ", points=" + Arrays.toString(points) +
                ", gamma=" + gamma +
                ", maxRecursionIterations=" + maxRecursionIterations +
                ", cars=" + cars +
                '}';
    }
}
