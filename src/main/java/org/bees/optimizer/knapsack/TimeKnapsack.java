package org.bees.optimizer.knapsack;

import java.util.ArrayList;
import java.util.List;

public class TimeKnapsack {
    public static Find find(List<Point> points, int timeCapacity) {
        int[][] matrix = new int[points.size() + 1][timeCapacity + 1];

        for (int i = 0; i <= timeCapacity; i++) {
            matrix[0][i] = 0;
        }

        for (int i = 1; i <= points.size(); i++) {
            for (int j = 0; j <= timeCapacity; j++) {
                int pointTime = points.get(i - 1).time;

                if (pointTime > j) {
                    matrix[i][j] = matrix[i - 1][j];
                } else {
                    matrix[i][j] = Math.max(matrix[i - 1][j], matrix[i - 1][j - pointTime] + points.get(i - 1).money);
                }
            }
        }

        return createSolution(points, timeCapacity, matrix);
    }

    private static Find createSolution(List<Point> points, int timeCapacity, int[][] matrix) {
        int res = matrix[points.size()][timeCapacity];
        int w = timeCapacity;
        List<Point> solutionPoints = new ArrayList<>();

        for (int i = points.size(); i > 0 && res > 0; i--) {
            if (res != matrix[i - 1][w]) {
                solutionPoints.add(points.get(i-1));
                res -= points.get(i - 1).money;
                w -= points.get(i - 1).time;
            }
        }

        return new Find(solutionPoints, matrix[points.size()][timeCapacity]);
    }

    public static class Find {
        private List<Point> pointList;
        private int money;

        Find(List<Point> points, int money) {
            this.pointList = points;
            this.money = money;
        }

        public void display() {
            if (pointList != null && !pointList.isEmpty()) {
                System.out.println("Gathered money = " + money);
                System.out.println("Points to pick :");

                for (Point point : pointList) {
                    System.out.println("- " + point.toString());
                }
            }
        }

    }
}