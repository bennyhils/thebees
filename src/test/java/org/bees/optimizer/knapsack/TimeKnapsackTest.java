package org.bees.optimizer.knapsack;

import java.util.ArrayList;
import java.util.List;

import static org.bees.optimizer.knapsack.TimeKnapsack.find;

public class TimeKnapsackTest {
    public static void main(String[] args) {
        List<Point> pointList = getPointList();
        display(pointList);
        find(pointList, 4).display();
    }

    private static List<Point> getPointList() {
        List<Point> pointList = new ArrayList<>();
        pointList.add(new Point("A", 100, 5));
        pointList.add(new Point("B", 20, 4));
        pointList.add(new Point("C", 30, 1));
        pointList.add(new Point("D", 15, 1));

        return pointList;
    }

    private static void display(List<Point> pointList) {
        pointList.forEach(point -> System.out.println(point.toString()));
    }
}
