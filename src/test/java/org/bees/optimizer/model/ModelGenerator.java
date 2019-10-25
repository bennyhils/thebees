package org.bees.optimizer.model;

import org.bees.optimizer.model.external.PointDto;
import org.bees.optimizer.model.external.PointsDto;
import org.bees.optimizer.model.external.RouteDto;
import org.bees.optimizer.model.external.RoutesDto;
import org.bees.optimizer.model.external.TrafficDto;
import org.bees.optimizer.model.external.TrafficJamDto;
import org.bees.optimizer.model.internal.Car;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class ModelGenerator {

    private static final Random rng = new Random();

    private static long nextBoundedLong(long minInclusive, long maxExclusive) {
        return Math.abs(rng.nextLong()) % (maxExclusive - minInclusive) + minInclusive;
    }

    /**
     * 0.01 precision!
     *
     * @param minInclusive min val, inclusive
     * @param maxInclusive max val, inclusive
     * @return             value between given two
     */
    private static double nextBoundedDouble(double minInclusive, double maxInclusive) {
        int min = (int) (minInclusive * 100);
        int max = (int) (maxInclusive * 100);

        return (double) (min + rng.nextInt(max - min + 1)) / 100.0;
    }

    private static int nextBoundedInt(int minInclusive, int maxInclusive) {
        return minInclusive + rng.nextInt(maxInclusive - minInclusive + 1);
    }

    /**
     * Генерация начальных точек. Первые две точки (с индексами 0 и 1) не имеют денег.
     *
     * @param numPoints количество точек, не включая начальную
     * @param minValue  минимальное значение деняк, включительно
     * @param maxValue  максимальное значение деняк, не включительно
     * @return          дто с точками
     */
    public static PointsDto generatePoints(
            int numPoints,
            long minValue,
            long maxValue
    ) {
        List<PointDto> generatedPoints = new LinkedList<>();
        IntStream.rangeClosed(0, numPoints)
                 .sequential()
                 .forEach(i -> generatedPoints.add(new PointDto(i, i < 2 ? 0L : (nextBoundedLong(minValue, maxValue)))));

        return new PointsDto(generatedPoints);
    }

    /**
     * Генерация трафика.
     *
     * @param numPoints количество точек, не включая начальную
     * @param minVal    минимальный коэф пробки
     * @param maxVal    максимальный коэф пробки
     * @return          дто с пробками
     */
    public static TrafficDto generateTraffic(
            int numPoints,
            double minVal,
            double maxVal,
            String carName
    ) {
        List<TrafficJamDto> generatedTraffic = new LinkedList<>();
        IntStream.rangeClosed(0, numPoints - 1).forEach(i -> {
            IntStream.rangeClosed(i + 1, numPoints).forEach(j -> {
                generatedTraffic.add(new TrafficJamDto(i, j, nextBoundedDouble(minVal, maxVal)));
            });
        });
        return new TrafficDto(carName, generatedTraffic);
    }

    /**
     * Генерация трафика. Считаем, что генерим <b>не</b> начальный трафик
     *
     * @param numPoints количество точек, не включая начальную
     * @return          дто с пробками
     */
    public static TrafficDto generateTraffic(int numPoints) {
        return generateTraffic(numPoints, 1.0, 2.0, "sb0");
    }

    /**
     * Генерация дорог. Полный рандом!
     *
     * @param numPoints количество точек, не считая начальную
     * @param minTime   минимальное время проезда
     * @param maxTime   максимальное время проезда
     * @return          дто дорог (список ребер)
     */
    public static RoutesDto generateRoutes(
            int numPoints,
            int minTime,
            int maxTime
    ) {
        List<RouteDto> generatedRoutes = new LinkedList<>();
        IntStream.rangeClosed(0, numPoints - 1).forEach(i -> {
            IntStream.rangeClosed(i + 1, numPoints).forEach(j -> {
                generatedRoutes.add(new RouteDto(i, j, nextBoundedInt(minTime, maxTime)));
            });
        });
        return new RoutesDto(generatedRoutes);
    }

    public static List<Car> generateCars(int numCars) {
        List<Car> generatedCars = new LinkedList<>();
        IntStream.rangeClosed(0, numCars -1).forEach(value -> {
            generatedCars.add(new Car(1000000, "test-" + value));
        });

        return generatedCars;
    }

}
