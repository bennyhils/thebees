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

    private double averageAlgorithmTime = 0;

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

   public GotoDto makeDecision(String carName, int currentPoint, long remainCapacity, double[][] trafficJams) {
       Car car = cars.stream().filter(value -> value.getName().equals(carName)).findAny().get();
       return makeDecision(car, currentPoint, remainCapacity, trafficJams);
   }

   public synchronized GotoDto makeDecision(Car car, int currentPoint, long remainCapacity, double[][] trafficJams) {
       long beforeTimeMillis = System.currentTimeMillis();
       log.debug("state before {}, currentPoint {}, car {}, remainCapacity {}, currentTimeInMillis {}", toString(), currentPoint, car.getName(), remainCapacity, beforeTimeMillis);

       Solution bestSolution = getBestSolution(car.getCapacity(), currentPoint, remainCapacity, trafficJams, 0);

       long spentTime = System.currentTimeMillis() - beforeTimeMillis;
       if (averageAlgorithmTime == 0) {
           averageAlgorithmTime = spentTime;
       } else {
           averageAlgorithmTime = (averageAlgorithmTime + spentTime) / 2;
       }
       log.info("make decision, state after! car {}, currentPoint {}, decisionPoint {}, remainCapacity {}, pointMoney {}, spentTime {}, average {}", car.getName(), currentPoint, bestSolution.point, remainCapacity - points[bestSolution.point], points[bestSolution.point], spentTime, averageAlgorithmTime);
       points[bestSolution.point] = 0;

        ///todo add hash to calculated states

       return new GotoDto(bestSolution.point, car.getName());
    }

    public Solution getBestSolution(long carCapacity, int currentPoint, long remainCapacity, double[][] trafficJams, int currentRecursion) {
        log.trace("currentPoint {}, remainCapacity {}, currentRecursion {}", currentPoint, remainCapacity, currentRecursion);
        if (currentRecursion >= maxRecursionIterations) {
            log.trace("current recursion {} more than max {}", currentRecursion, maxRecursionIterations);
            return null;
        }

        /*1 - нода, куда отвозят деньги*/
        double maxValue = valueFunction(trafficJams[currentPoint][1], routes[currentPoint][1], carCapacity - remainCapacity);;
        int bestRoute = 1;

        log.trace("init max value to bank {}, currentPoint {}, remainCapacity {}, currentRecursion {}",maxValue, currentPoint, remainCapacity, currentRecursion);

        ///TODO проверка если осталось мало времени моделированиђ
        //пересчитываем веса у возможных состояний
        for (int j = 2; j < points.length; j++) {
            if (currentPoint == j) {
                continue;
            }

            if (remainCapacity >= points[j] && points[j] != -1) {
                double pointValue = valueFunction(trafficJams[currentPoint][j], routes[currentPoint][j], points[j]);
                log.trace("value {} from current {} to point {}", pointValue, currentPoint, j);

                long oldValue = points[currentPoint];
                points[currentPoint] = -1;

                Solution predictedPointSolution = getBestSolution(carCapacity,  j, remainCapacity - points[j], trafficJams, currentRecursion + 1);

                points[currentPoint] = oldValue;
                log.trace("predictedPointSolution {} from current {} to point {}", predictedPointSolution, currentPoint, j);
                if (predictedPointSolution != null) {
                    pointValue = pointValue + Math.pow(gamma, currentRecursion) * predictedPointSolution.value;
                }

                if (maxValue < pointValue) {
                    maxValue = pointValue;
                    bestRoute = j;
                    log.trace("change max value {}, route {}", maxValue, bestRoute);
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
                ", points=" + Arrays.toString(points) +
                ", gamma=" + gamma +
                ", maxRecursionIterations=" + maxRecursionIterations +
                ", cars=" + cars +
                '}';
    }
}
