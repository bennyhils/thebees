package org.bees.optimizer.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bees.optimizer.model.external.GotoDto;
import org.bees.optimizer.model.internal.Car;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class RouteBuilder2 {
//    final double[][][]  weightedMatrix;
    private final int[][] routes;
    /*первый элемент откуда стартует, второй куда отвозим деньги*/
    private final long[] points;
    private final double gamma;
    private final int maxRecursionIterations;
    private final List<Car> cars;

    final double[] weightedMatrix;
    boolean[][] closedNodes;

    private double averageAlgorithmTime = 0;

    Value[][] values;
    double timeModeling = 480;

    @AllArgsConstructor
    class Value {
        double currentValue;
        double expectedValue;
        double sum;

        public void init() {
            sum = currentValue + expectedValue;
        }
    }

    public RouteBuilder2(int[][] routes, long[] points, List<Car> cars, double gamma, int maxRecursionIterations) {
        if (!(points.length == routes[0].length && routes.length == points.length)) {
            throw new IllegalArgumentException(String.format("Dimension should be the same routes %d, routes[0] %s, nodes %d, cars %d", routes.length, routes[0].length, points.length, cars.size()));
        }

        this.routes = routes;
        this.points = points;
        this.gamma = gamma;
        this.maxRecursionIterations = maxRecursionIterations;
        this.cars = cars;

        values = new Value[points.length][cars.size()];
        weightedMatrix = new double[points.length];
        closedNodes = new boolean[points.length][cars.size()];

        log.debug("initialize routeBuilder model " + toString());

    }

   public List<GotoDto> makeFirstDecision(double[][] trafficJams) {
        List<GotoDto> gotoDtos = new ArrayList<>(cars.size());

//        Arrays.deepHashCode(trafficJams);

        cars.forEach(car-> gotoDtos.add(makeDecision(0, car, 0, car.getCapacity(), trafficJams)));

        return gotoDtos;
   }

   public GotoDto makeDecision(double passedTime, String carName, int currentPoint, long remainCapacity, double[][] trafficJams) {
       Car car = cars.stream().filter(value -> value.getName().equals(carName)).findAny().get();
       return makeDecision(passedTime, car, currentPoint, remainCapacity, trafficJams);
   }

   public GotoDto makeDecision(double passedTime, Car car, int currentPoint, long remainCapacity, double[][] trafficJams) {
       long beforeTimeMillis = System.currentTimeMillis();
       log.debug("state before {}, currentPoint {}, car {}, remainCapacity {}, currentTimeInMillis {}", toString(), currentPoint, car.getName(), remainCapacity, beforeTimeMillis);

       getBestSolution(passedTime, cars.indexOf(car), car.getCapacity(),  currentPoint, remainCapacity, trafficJams, 0);

       int maxIndex = 0;
       synchronized (weightedMatrix) {
           double max = weightedMatrix[0];
           for(int i = 0; i < weightedMatrix.length; ++i) {
               if (weightedMatrix[i] > max && remainCapacity >= points[i] && i != currentPoint) {
                   max = weightedMatrix[i];
                   maxIndex = i;
               }
           }

           points[maxIndex] = 0;
       }

       long spentTime = System.currentTimeMillis() - beforeTimeMillis;
       if (averageAlgorithmTime == 0) {
           averageAlgorithmTime = spentTime;
       } else {
           averageAlgorithmTime = (averageAlgorithmTime + spentTime) / 2;
       }
       log.info("make decision, state after! car {}, currentPoint {}, decisionPoint {}, remainCapacity {}, pointMoney {}, spentTime {}, average {}", car.getName(), currentPoint, maxIndex, remainCapacity - points[maxIndex], points[maxIndex], spentTime, averageAlgorithmTime);

        ///todo add hash to calculated states

       return new GotoDto(maxIndex, car.getName());
    }

    public double getBestSolution(double passedTime, int carIndex, long capacity, int currentPoint, long remainCapacity, double[][] trafficJams, int currentRecursion) {
        log.trace("currentPoint {}, remainCapacity {}, currentRecursion {}", currentPoint, remainCapacity, currentRecursion);
        if (currentRecursion >= maxRecursionIterations) {
            log.trace("current recursion {} more than max {}", currentRecursion, maxRecursionIterations);
            return -1;
        }

        /*1 - нода, куда отвозят деньги*/
        double maxValue = valueFunction(trafficJams[currentPoint][1], routes[currentPoint][1], capacity - remainCapacity);
        double k;
        if (timeModeling - passedTime < 40) {
            k = 100;
        } else {
            k = 1/ (timeModeling - passedTime);
        }

        weightedMatrix[1] = maxValue + maxValue * k;

//        values[1][carIndex] = new Value(maxValue, k);
//        weightedMatrix[currentPoint][1][cars.indexOf(car)] = maxValue;
        int bestRoute = 1;

        log.trace("init max value to bank {}, currentPoint {}, remainCapacity {}, currentRecursion {}", maxValue, currentPoint, remainCapacity, currentRecursion);

        ///TODO проверка если осталось мало времени моделированиђ
        //пересчитываем веса у возможных состояний
        for (int j = 2; j < points.length; j++) {
            if (currentPoint == j) {
                continue;
            }

            if (remainCapacity >= points[j] && !closedNodes[j][carIndex]) {
                double pointValue = valueFunction(trafficJams[currentPoint][j], routes[currentPoint][j], points[j]);
                log.trace("value {} from current {} to point {}", pointValue, currentPoint, j);

                closedNodes[currentPoint][carIndex] = true;

                double predictedPointSolution = getBestSolution(passedTime, carIndex, capacity,  j, remainCapacity - points[j], trafficJams, currentRecursion + 1);

                closedNodes[currentPoint][carIndex] = false;
                log.trace("predictedPointSolution {} from current {} to point {}", predictedPointSolution, currentPoint, j);
                if (predictedPointSolution == -1) {
                    pointValue = pointValue + Math.pow(gamma, currentRecursion) * predictedPointSolution;
                }

                weightedMatrix[currentPoint] = pointValue;


                if (maxValue < pointValue) {
                    maxValue = pointValue;
                    bestRoute = j;
                    log.trace("change max value {}, route {}", maxValue, bestRoute);
                }
            }
        }

        return maxValue;
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
