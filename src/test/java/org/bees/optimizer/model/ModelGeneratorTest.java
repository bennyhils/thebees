package org.bees.optimizer.model;

import org.bees.optimizer.model.external.PointsDto;
import org.bees.optimizer.model.external.RoutesDto;
import org.bees.optimizer.model.external.TrafficDto;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

public class ModelGeneratorTest {

    @Test
    public void validatePointsGenerator() {
        PointsDto pointsDto = ModelGenerator.generatePoints(500, 100L, 200L);

        Assert.assertEquals(501, pointsDto.getPoints().size());

        Set<Integer> p = new HashSet<>();
        pointsDto.getPoints()
                 .forEach(pointDto -> {
                     long money = pointDto.getMoney();
                     int index = pointDto.getIndex();
                     if (index > 1) {
                         Assert.assertTrue(money >= 100L && money < 200L);
                     } else {
                         Assert.assertEquals(0, money);
                     }
                     p.add(index);
                 });
        IntStream.rangeClosed(0, 500).forEach(i -> Assert.assertTrue(p.contains(i)));
    }

    @Test
    public void validateInitialTrafficGenerator() {
        TrafficDto trafficDto = ModelGenerator.generateTraffic(500);

        Assert.assertEquals(500 * 501 / 2, trafficDto.getTraffic().size());

        trafficDto.getTraffic()
                  .forEach(trafficJamDto -> Assert.assertEquals(1.5, trafficJamDto.getJam(), 0.5));
    }

    @Test
    public void validateRoutesGenerator() {
        RoutesDto routesDto = ModelGenerator.generateRoutes(500, 1, 50);

        Assert.assertEquals(500 * 501 / 2, routesDto.getRoutes().size());

        routesDto.getRoutes()
                 .forEach(routeDto -> {
                     int time = routeDto.getTime();
                     Assert.assertTrue(time >= 1 && time <= 50);
                 });

    }

}