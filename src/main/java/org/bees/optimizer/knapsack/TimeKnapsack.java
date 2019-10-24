package org.bees.optimizer.knapsack;

import java.util.ArrayList;
import java.util.List;

public class TimeKnapsack {
    private Point[] points;
    private int timeCapacity;

    private TimeKnapsack(Point[] points, int timeCapacity) {
        this.points = points;
        this.timeCapacity = timeCapacity;
    }

    private void display() {
        if (points != null && points.length > 0) {
            System.out.println("Time capacity : " + timeCapacity);
            System.out.println("Points :");

            for (Point item : points) {
                System.out.println("- " + item.str());
            }
        }
    }

    private Solution solve() {
        int NB_POINTS = points.length;
        // we use a matrix to store the max value at each n-th item
        int[][] matrix = new int[NB_POINTS + 1][timeCapacity + 1];

        // first line is initialized to 0
        for (int i = 0; i <= timeCapacity; i++) {
            matrix[0][i] = 0;
        }

        // we iterate on points
        for (int i = 1; i <= NB_POINTS; i++) {
            // we iterate on each capacity
            for (int j = 0; j <= timeCapacity; j++) {
                if (points[i - 1].time > j) {
                    matrix[i][j] = matrix[i - 1][j];
                } else {
                    // we maximize value at this rank in the matrix
                    matrix[i][j] = Math.max(matrix[i - 1][j], matrix[i - 1][j - points[i - 1].time] + points[i - 1].money);
                }
            }
        }

        int res = matrix[NB_POINTS][timeCapacity];
        int w = timeCapacity;
        List<Point> solutionPoints = new ArrayList<>();

        for (int i = NB_POINTS; i > 0 && res > 0; i--) {
            if (res != matrix[i - 1][w]) {
                solutionPoints.add(points[i - 1]);
                // we remove points value and money
                res -= points[i - 1].money;
                w -= points[i - 1].time;
            }
        }

        return new Solution(solutionPoints, matrix[NB_POINTS][timeCapacity]);
    }

    public static void main(String[] args) {
        // we take the same instance of the problem displayed in the image
        Point[] points = {
                new Point("A", 4, 12),
                new Point("B", 2, 1),
                new Point("C", 2, 2),
                new Point("D", 1, 1),
                new Point("E", 10, 4),
                new Point("F", 10, 5)
        };

        TimeKnapsack knapsack = new TimeKnapsack(points, 23);
        knapsack.display();

        Solution solution = knapsack.solve();
        solution.display();
    }
}

class Point {
    private String name;
    int money;
    int time;

    Point(String name, int money, int time) {
        this.name = name;
        this.money = money;
        this.time = time;
    }

    String str() {
        return name + " [money = " + money + ", time = " + time + "]";
    }

}

class Solution {
    private List<Point> pointList;
    private int money;

    Solution(List<Point> points, int money) {
        this.pointList = points;
        this.money = money;
    }

    void display() {
        if (pointList != null && !pointList.isEmpty()) {
            System.out.println("Solution\n");
            System.out.println("Gathered money = " + money);
            System.out.println("Points to pick :");

            for (Point point : pointList) {
                System.out.println("- " + point.str());
            }
        }
    }

}