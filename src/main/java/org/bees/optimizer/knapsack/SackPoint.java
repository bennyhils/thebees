package org.bees.optimizer.knapsack;

import java.util.List;

public class SackPoint {
    // пох на инкапсуляцию, в нашей команде нечего скрывать
    public List<Integer> name;
    public int money;
    public int time;

    public SackPoint(List<Integer> name, long money, int time) {
        this.name = name;
        this.money = (int) money; // IMPORTANT
        this.time = time;
    }

    @Override
    public String toString() {
        return name + " [money = " + money + ", time = " + time + "]";
    }
}