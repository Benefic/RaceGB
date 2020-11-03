package ru.geekbrains.race;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class Race {
    private final ArrayList<Stage> stages;
    private final AtomicInteger finishPlace = new AtomicInteger(1);
    private volatile Car winner;

    public Race(Stage... stages) {
        this.stages = new ArrayList<>(Arrays.asList(stages));
    }

    public ArrayList<Stage> getStages() {
        return stages;
    }

    public void finish(Car car) {
        finishPlace.getAndIncrement();
        if (winner == null) {
            winner = car;
            System.out.println(car.getName() + " - WIN!");
        } else {
            System.out.println(car.getName() + " финишировал на позиции " + finishPlace);
        }
    }
}