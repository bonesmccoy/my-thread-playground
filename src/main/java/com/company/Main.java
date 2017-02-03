package com.company;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class Main {


    public static void main(String[] args) throws InterruptedException {
        final ArrayList<String> symbolList = new ArrayList<String>();

        symbolList.add("AAPL");
        symbolList.add("CCCC");
        symbolList.add("DDDD");
        symbolList.add("CCCC");

        Scanner scanner = new Scanner(System.in);

        System.out.println("Press Enter to start...");
        scanner.nextLine();

        final Random random = new Random();
        final Publisher publisher = new Publisher("localhost",5672, "MY_QUEUE");

        long startTime = System.currentTimeMillis();
        if (publisher.openConnection()) {
            ExecutorService exe = Executors.newFixedThreadPool(4);
            for (Integer i = 0;i < 10000; i++) {
                exe.submit(new Runnable() {
                    public void run() {
                            String symbol = symbolList.get(random.nextInt(4));
                            publisher.publish("Sending message for symbol " + symbol + " at time " + LocalDateTime.now().toString());
                    }
                });
            }
            exe.shutdown();
            exe.awaitTermination(1, TimeUnit.DAYS);
            publisher.closeConnection();
        }
        System.out.println("Total duration seconds: " + ((System.currentTimeMillis() - startTime)/1000));
    }
}