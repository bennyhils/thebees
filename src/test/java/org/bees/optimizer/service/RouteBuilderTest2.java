package org.bees.optimizer.service;

import org.bees.optimizer.RouteOptimizerConfig;
import org.bees.optimizer.model.ModelConverter;
import org.bees.optimizer.model.ModelGenerator;
import org.bees.optimizer.model.external.GotoDto;
import org.bees.optimizer.model.internal.Car;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RouteOptimizerConfig.class)
public class RouteBuilderTest2 {
    RouteBuilder2 routeBuilder;


//    @Test
//    public void deepRecursion() {
//        int numCount = 10;
//        int[][] routes = ModelConverter.convertRoutes(ModelGenerator.generateRoutes(numCount, 1, 10));
//        long[] points = ModelConverter.convertPoints(ModelGenerator.generatePoints(numCount, 1000, 1000000));
//        List<Car> cars = ModelGenerator.generateCars(2);
//        double[][] traffic = ModelConverter.convertTraffic(ModelGenerator.generateTraffic(numCount));
//
//        routeBuilder = new RouteBuilder(routes, points, cars, 1, 3);
//        Assert.assertNull(routeBuilder.getBestSolution(100, 0, 100, traffic, 4));
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void differentSize() {
//        int numCount = 10;
//        int differentNumCount = 5;
//        int[][] routes = ModelConverter.convertRoutes(ModelGenerator.generateRoutes(numCount, 1, 10));
//        long[] points = ModelConverter.convertPoints(ModelGenerator.generatePoints(differentNumCount, 1000, 1000000));
//        List<Car> cars = ModelGenerator.generateCars(1);
//
//        routeBuilder = new RouteBuilder(routes, points, cars, 1, 3);
//    }

    @Test
    public void getSolution() {
        int numCount = 10;
        int[][] routes = ModelConverter.convertRoutes(ModelGenerator.generateRoutes(numCount, 1, 10));
        long[] points = ModelConverter.convertPoints(ModelGenerator.generatePoints(numCount, 1000, 1000000));
        List<Car> cars = ModelGenerator.generateCars(2);
        double[][] traffic = ModelConverter.convertTraffic(ModelGenerator.generateTraffic(numCount));

        routeBuilder = new RouteBuilder2(routes, points, cars, 1, 3);
        double sss  = routeBuilder.getBestSolution(900000, 0, 1000000, 0, 1000000, traffic, 0);
        System.out.println(sss);
    }

    @Test
    public void getSolutionLargeData() {
        int numCount = 500;
        int[][] routes = ModelConverter.convertRoutes(ModelGenerator.generateRoutes(numCount, 1, 31));
        long[] points = ModelConverter.convertPoints(ModelGenerator.generatePoints(numCount, 0, 100000));
        List<Car> cars = ModelGenerator.generateCars(5);

        double gamma = 0.8;
        int maxRecursionLength = 3;

        routeBuilder = new RouteBuilder2(routes, points, cars, gamma, maxRecursionLength);

        double[][] traffic = ModelConverter.convertTraffic(ModelGenerator.generateTraffic(numCount));
        List<State> models = routeBuilder.makeFirstDecision(traffic).stream().map(gotoDto -> {
            Car car = cars.stream().filter(value -> value.getName().equals(gotoDto.getCarName())).findAny().get();
            long capacity;
            if (gotoDto.getPoint() == 0) {
                capacity = 0;
            } else {
                capacity = car.getCapacity() - points[gotoDto.getPoint()];
            }
            return new State(gotoDto, capacity);
        }).collect(Collectors.toList());



        for (int i = 0; i < 5; i++) {
            randomChange(traffic);
            models.forEach(model -> {
                    GotoDto gotoDto = routeBuilder.makeDecision(10000, model.gotoDto.getCarName(), model.gotoDto.getPoint(), model.remainingMoney, traffic);
                    model.gotoDto = gotoDto;
                long capacity;
                if (gotoDto.getPoint() == 0) {
                    capacity = 0;
                } else {
                    capacity = model.remainingMoney - points[gotoDto.getPoint()];
                }
                    model.remainingMoney = capacity;
            });
        }
    }

    class State {
        GotoDto gotoDto;
        long remainingMoney;

        public State(GotoDto gotoDto, long remainingMoney) {
            this.gotoDto = gotoDto;
            this.remainingMoney = remainingMoney;
        }
    }

    private void randomChange(double[][] trafficMatrix) {
        for (int i = 0; i < trafficMatrix.length; i++) {
            for (int j =0; j < trafficMatrix[i].length; j++) {
                Random random = new Random();
                int rand = random.nextInt(10);
                if (rand < 3) {
                    trafficMatrix[i][j] = 1.0 + (2.0 - 1.0) * random.nextDouble();
                }
            }
        }

    }

}
