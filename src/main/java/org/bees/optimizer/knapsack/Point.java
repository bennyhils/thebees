package org.bees.optimizer.knapsack;

public class Point {
    // пох на инкапсуляцию, в нашей команде нечего скрывать
    public String name;
    public int money;
    public int time;

    public Point(String name, long money, int time) {
        this.name = name;
        this.money = (int) money; // IMPORTANT
        this.time = time;
    }

    @Override
    public String toString() {
        return name + " [money = " + money + ", time = " + time + "]";
    }
}