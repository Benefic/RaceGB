package ru.geekbrains.race;


import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainClass {
    public static final int CARS_COUNT = 4;

    public static void main(String[] args) {
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");

        CyclicBarrier startLine = new CyclicBarrier(CARS_COUNT);
        CountDownLatch prepareLatch = new CountDownLatch(CARS_COUNT);
        CountDownLatch finishLatch = new CountDownLatch(CARS_COUNT);
        // готовим гонку
        Race race = new Race(new Road(60), new Tunnel(), new Road(40));

        // готовим машины, ждём всех
        Car[] cars = new Car[CARS_COUNT];
        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + (int) (Math.random() * 10), startLine, prepareLatch, finishLatch);
        }

        ExecutorService executorService = Executors.newFixedThreadPool(CARS_COUNT);

        for (Car car : cars) {
            executorService.execute(new Thread(car));
        }

        try {
            prepareLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");

        try {
            finishLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");

        executorService.shutdown();
    }
}
