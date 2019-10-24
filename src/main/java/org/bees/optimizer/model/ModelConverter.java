package org.bees.optimizer.model;

import org.bees.optimizer.model.external.PointsDto;
import org.bees.optimizer.model.external.RoutesDto;
import org.bees.optimizer.model.external.TokenDto;
import org.bees.optimizer.model.external.TrafficDto;
import org.bees.optimizer.model.internal.Car;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ModelConverter {

    /**
     * Конвертирует дто трафика в двумерный массив double.
     * Коэффициент на главной диагонали равен 1.0.
     */
    public static double[][] convertTraffic(TrafficDto traffic) {
        int size = traffic.getTraffic()
                          .stream()
                          .flatMap(t -> Stream.of(t.getTo(), t.getFrom()))
                          .max(Comparator.naturalOrder())
                          .get() + 1;

        double[][] ans = new double[size][size];

        IntStream.range(0, size).forEach(i -> ans[i][i] = 1.0);

        traffic.getTraffic().forEach(t -> {
            ans[t.getFrom()][t.getTo()] = t.getJam();
            ans[t.getTo()][t.getFrom()] = t.getJam();
        });


        return ans;
    }

    /**
     * Конвертирует дто путей в двумерный массив int.
     * Значения на главной диагонали равны -1 (невозможно доехать).
     */
    public static int[][] convertRoutes(RoutesDto routes) {
        int size = routes.getRoutes()
                .stream()
                .flatMap(r -> Stream.of(r.getFrom(), r.getTo()))
                .max(Comparator.naturalOrder())
                .get() + 1;

        int[][] ans = new int[size][size];

        routes.getRoutes().forEach(r -> {
            int a = r.getFrom();
            int b = r.getTo();
            if (a == 0) {
                ans[a][b] = r.getTime();
                ans[b][a] = -1;
            } else if (b == 0) {
                ans[a][b] = -1;
                ans[b][a] = r.getTime();
            } else {
                ans[a][b] = ans[b][a] = r.getTime();
            }
        });

        IntStream.range(0, size).forEach(i -> ans[i][i] = -1);

        return ans;
    }

    /**
     * Конвертирует дто точек в одномерный массив long.
     */
    public static long[] convertPoints(PointsDto points) {
        int size = points.getPoints().size();

        long[] ans = new long[size];

        points.getPoints().forEach(p -> {
            ans[p.getIndex()] = p.getMoney();
        });

        return ans;
    }

    public static List<Car> convertTokenToCar(TokenDto tokenDto) {
        List<Car> cars = new LinkedList<>();
        tokenDto.getCars().stream().forEach(s -> cars.add(new Car(s)));
        return cars;
    }
}
