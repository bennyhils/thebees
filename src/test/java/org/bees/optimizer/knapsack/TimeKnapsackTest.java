package org.bees.optimizer.knapsack;

import java.util.ArrayList;
import java.util.List;

import static org.bees.optimizer.knapsack.TimeKnapsack.find;

public class TimeKnapsackTest {
    public static void main(String[] args) {
        List<SackPoint> sackPointList = getPointList();
        display(sackPointList);
        find(sackPointList, 4).display();
    }

    private static List<SackPoint> getPointList() {
        List<SackPoint> sackPointList = new ArrayList<>();
//        sackPointList.add(new SackPoint("A", 100, 5));
//        sackPointList.add(new SackPoint("B", 20, 4));
//        sackPointList.add(new SackPoint("C", 30, 1));
//        sackPointList.add(new SackPoint("D", 15, 1));

        return sackPointList;
    }

    private static void display(List<SackPoint> sackPointList) {
        sackPointList.forEach(sackPoint -> System.out.println(sackPoint.toString()));
    }
}
