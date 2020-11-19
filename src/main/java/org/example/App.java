package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


public class App 
{
    public static void main( String[] args )
    {
        var tasks = Arrays.asList("one", "two", "three", "four", "five");

        List<String> tasks2 = new ArrayList<String>();

        for (Integer i = 0; i <10000; i++) {
            tasks2.add(i.toString());
        }

        executeInParallel(tasks);
        executeInParallel(tasks2);
        System.out.println( "Completed tasks successfully" );
    }

    public static void executeInParallel(List<String> tasks) {

        var start = System.nanoTime();

        var executorService = Executors.newFixedThreadPool(tasks.size());

        var futures = tasks.stream()
                            .map(t -> CompletableFuture.supplyAsync(() -> work(t), executorService))
                            .collect(Collectors.toList());

        var results = futures.stream()
                                        .map(CompletableFuture::join)
                                        .collect(Collectors.toList());

        System.out.println(results);
        executorService.shutdown();

        var end = (System.nanoTime() - start)/1_000_000;
        System.out.println("Processed " + tasks.size() + " tasks in " + end + " ms");

    }

    public static String work(String task)  {
        try {
            Thread.sleep(2000);
        } catch(InterruptedException ex) {
            ex.printStackTrace();
        }
        return  task + "Done";
    }
}
