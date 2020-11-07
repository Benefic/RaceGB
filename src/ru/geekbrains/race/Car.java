package ru.geekbrains.race;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class Car implements Runnable {
    private static int CARS_COUNT;
    private final Race race;
    private final int speed;
    private final CyclicBarrier startLine;
    private final CountDownLatch prepareLatch;
    private final CountDownLatch finishLatch;
    private final String name;

    public Car(Race race, int speed,
               CyclicBarrier startLine, CountDownLatch prepareLatch, CountDownLatch finishLatch) {
        this.race = race;
        this.speed = speed;
        this.startLine = startLine;
        this.prepareLatch = prepareLatch;
        this.finishLatch = finishLatch;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
    }

    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }

    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int) (Math.random() * 800));
            System.out.println(this.name + " готов");
        } catch (Exception e) {
            e.printStackTrace();
        }
        prepareLatch.countDown();

        try {
            startLine.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < race.getStages().size(); i++) {
            race.getStages().get(i).go(this);
        }

        race.finish(this);

        finishLatch.countDown();
    }
}
