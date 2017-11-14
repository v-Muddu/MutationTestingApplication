package com.oole.hw3.concurrency;

import com.oole.hw3.utility.CSVUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * LauncherThreadExecutorService is a thread executor service
 */
public class LauncherThreadExecutorService {

    /**
     * thread pool of constant size
     */
    public static ExecutorService executorService = Executors.newFixedThreadPool(8);

    /**
     * shuts down the executor service
     */
    public static void shutdown(){
        executorService.shutdown();
        CSVUtils.shutdown();
    }

}
