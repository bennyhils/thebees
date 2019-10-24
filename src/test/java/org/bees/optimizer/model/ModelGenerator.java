package org.bees.optimizer.model;

import org.bees.optimizer.model.external.PointDto;
import org.bees.optimizer.model.external.PointsDto;
import org.bees.optimizer.model.external.RouteDto;
import org.bees.optimizer.model.external.RoutesDto;
import org.bees.optimizer.model.external.TrafficDto;
import org.bees.optimizer.model.external.TrafficJamDto;

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

    public static PointsDto generatePoints(
            int numPoints,
            long minValue,
            long maxValue
    ) {
        List<PointDto> generatedPoints = new LinkedList<>();
        IntStream.range(0, numPoints)
                 .sequential()
                 .forEach(i -> generatedPoints.add(new PointDto(i, nextBoundedLong(minValue, maxValue))));

        return new PointsDto(generatedPoints);
    }

    public static TrafficDto generateTraffic(
            int numPoints,
            double minVal,
            double maxVal
    ) {
        List<TrafficJamDto> generatedTraffic = new LinkedList<>();
        IntStream.range(0, numPoints - 1).forEach(i -> {
            IntStream.range(i + 1, numPoints).forEach(j -> {
                generatedTraffic.add(new TrafficJamDto(i, j, nextBoundedDouble(minVal, maxVal)));
            });
        });
        return new TrafficDto(generatedTraffic);
    }

    public static TrafficDto generateTraffic(int numPoints) {
        return generateTraffic(numPoints, 1.0, 2.0);
    }

    public static RoutesDto generateRoutes(
            int numPoints,
            int minTime,
            int maxTime
    ) {
        List<RouteDto> generatedRoutes = new LinkedList<>();
        IntStream.range(0, numPoints - 1).forEach(i -> {
            IntStream.range(i + 1, numPoints).forEach(j -> {
                generatedRoutes.add(new RouteDto(i, j, nextBoundedInt(minTime, maxTime)));
            });
        });
        return new RoutesDto(generatedRoutes);
    }

}
