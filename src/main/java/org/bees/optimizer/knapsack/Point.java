package org.bees.optimizer.knapsack;

class Point {
    // пох на инкапсуляцию, в нашей команде нечего скрывать
    public String name;
    public int money;
    public int time;

    Point(String name, int money, int time) {
        this.name = name;
        this.money = money;
        this.time = time;
    }

    @Override
    public String toString() {
        return name + " [money = " + money + ", time = " + time + "]";
    }

}
