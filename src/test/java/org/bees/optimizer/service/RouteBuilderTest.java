package org.bees.optimizer.service;

import org.bees.optimizer.RouteOptimizerConfig;
import org.bees.optimizer.model.ModelConverter;
import org.bees.optimizer.model.ModelGenerator;
import org.bees.optimizer.model.internal.Car;
import org.bees.optimizer.server.DataProvider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RouteOptimizerConfig.class)
public class RouteBuilderTest {
    RouteBuilder routeBuilder;

    @Before
    public void init() {
        int numCount = 10;
        int[][] routes = ModelConverter.convertRoutes(ModelGenerator.generateRoutes(numCount, 1, 10));
        long[] points = ModelConverter.convertPoints(ModelGenerator.generatePoints(numCount, 1000, 1000000));
        List<Car> cars = ModelGenerator.generateCars(1);

        routeBuilder = new RouteBuilder(routes, points, cars, 1, 3);
    }

    @Test
    public void deepRecursion() {
        int numCount = 10;
        int[][] routes = ModelConverter.convertRoutes(ModelGenerator.generateRoutes(numCount, 1, 10));
        long[] points = ModelConverter.convertPoints(ModelGenerator.generatePoints(numCount, 1000, 1000000));
        List<Car> cars = ModelGenerator.generateCars(2);
        double[][] traffic = ModelConverter.convertTraffic(ModelGenerator.generateTraffic(numCount));

        routeBuilder = new RouteBuilder(routes, points, cars, 1, 3);
        Assert.assertNull(routeBuilder.getBestSolution(100, 0, 100, traffic, 4));
    }

    @Test(expected = IllegalArgumentException.class)
    public void differentSize() {
        int numCount = 10;
        int differentNumCount = 5;
        int[][] routes = ModelConverter.convertRoutes(ModelGenerator.generateRoutes(numCount, 1, 10));
        long[] points = ModelConverter.convertPoints(ModelGenerator.generatePoints(differentNumCount, 1000, 1000000));
        List<Car> cars = ModelGenerator.generateCars(1);

        routeBuilder = new RouteBuilder(routes, points, cars, 1, 3);
    }

    @Test
    public void firstTest() {
        int numCount = 10;
        int[][] routes = ModelConverter.convertRoutes(ModelGenerator.generateRoutes(numCount, 1, 10));
        long[] points = ModelConverter.convertPoints(ModelGenerator.generatePoints(numCount, 1000, 1000000));
        List<Car> cars = ModelGenerator.generateCars(2);
        double[][] traffic = ModelConverter.convertTraffic(ModelGenerator.generateTraffic(numCount));

        routeBuilder = new RouteBuilder(routes, points, cars, 1, 3);
        RouteBuilder.Solution solution = routeBuilder.getBestSolution(900000, 0, 900000, traffic, 0);
        System.out.println(solution);
    }

}
