package org.bees.optimizer.knapsack;

import java.util.ArrayList;
import java.util.List;

import static org.bees.optimizer.knapsack.TimeKnapsack.solve;

public class TimeKnapsackTest {
    public static void main(String[] args) {
        List<Point> pointList = getPointList();
        display(pointList);
        solve(pointList, 23).display();
    }

    private static List<Point> getPointList() {
        List<Point> pointList = new ArrayList<>();
        pointList.add(new Point("A", 4, 12));
        pointList.add(new Point("B", 2, 1));
        pointList.add(new Point("C", 2, 2));
        pointList.add(new Point("D", 1, 1));
        pointList.add(new Point("E", 10, 4));
        pointList.add(new Point("F", 10, 5));

        return pointList;
    }

    private static void display(List<Point> pointList) {
        pointList.forEach(point -> System.out.println(point.toString()));
    }
}
