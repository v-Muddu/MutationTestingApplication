package com.oole.hw3.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LauncherThreadExecutorService {

    public static ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static void shutdown(){
        executorService.shutdown();
    }

}
