package org.bees.optimizer.knapsack;

import java.util.ArrayList;
import java.util.List;

public class TimeKnapsack {
    public static Find find(List<SackPoint> sackPoints, int timeCapacity) {
        int[][] matrix = new int[sackPoints.size() + 1][timeCapacity + 1];

        for (int i = 0; i <= timeCapacity; i++) {
            matrix[0][i] = 0;
        }

        for (int i = 1; i <= sackPoints.size(); i++) {
            for (int j = 0; j <= timeCapacity; j++) {
                int pointTime = sackPoints.get(i - 1).time;

                if (pointTime > j) {
                    matrix[i][j] = matrix[i - 1][j];
                } else {
                    matrix[i][j] = Math.max(matrix[i - 1][j], matrix[i - 1][j - pointTime] + sackPoints.get(i - 1).money);
                }
            }
        }

        return createSolution(sackPoints, timeCapacity, matrix);
    }

    private static Find createSolution(List<SackPoint> sackPoints, int timeCapacity, int[][] matrix) {
        int res = matrix[sackPoints.size()][timeCapacity];
        int w = timeCapacity;
        List<SackPoint> solutionSackPoints = new ArrayList<>();

        for (int i = sackPoints.size(); i > 0 && res > 0; i--) {
            if (res != matrix[i - 1][w]) {
                solutionSackPoints.add(sackPoints.get(i-1));
                res -= sackPoints.get(i - 1).money;
                w -= sackPoints.get(i - 1).time;
            }
        }

        return new Find(solutionSackPoints, matrix[sackPoints.size()][timeCapacity]);
    }

    public static class Find {
        private List<SackPoint> sackPointList;
        private int money;

        Find(List<SackPoint> sackPoints, int money) {
            this.sackPointList = sackPoints;
            this.money = money;
        }

        public void display() {
            if (sackPointList != null && !sackPointList.isEmpty()) {
                System.out.println("Gathered money = " + money);
                System.out.println("Points to pick :");

                for (SackPoint sackPoint : sackPointList) {
                    System.out.println("- " + sackPoint.toString());
                }
            }
        }

    }
}