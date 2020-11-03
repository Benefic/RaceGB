package ru.geekbrains.race;

import java.util.concurrent.Semaphore;

public class Tunnel extends Stage {

    private final Semaphore semaphore = new Semaphore(MainClass.CARS_COUNT / 2);
    private final boolean[] tunnelLines = new boolean[MainClass.CARS_COUNT / 2];

    public Tunnel() {
        this.length = 80;
        this.description = "Тоннель " + length + " метров";
    }

    @Override
    public void go(Car c) {
        try {
            try {
                System.out.println(c.getName() + " готовится к этапу(ждет): " + description);
                semaphore.acquire();
                int controlNum = -1;
                synchronized (tunnelLines) {
                    for (int i = 0;
                         i < tunnelLines.length; i++)
                        if (!tunnelLines[i]) {
                            tunnelLines[i] = true;
                            controlNum = i;
                            break;
                        }
                }
                System.out.println(c.getName() + " начал этап: " + description);
                Thread.sleep(length / c.getSpeed() * 1000);
                synchronized (tunnelLines) {
                    tunnelLines[controlNum] = false;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println(c.getName() + " закончил этап: " + description);
                semaphore.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
