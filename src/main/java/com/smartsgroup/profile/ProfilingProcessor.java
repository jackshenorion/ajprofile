package com.smartsgroup.profile;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

public class ProfilingProcessor {
    private static Logger logger = Logger.getLogger(ProfilingProcessor.class.getName());
    private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");

    private static Map<String, MethodStatistics> statisticsMap = new HashMap<>();
    private static List<MemoryStatistics> memoryStatistics = new ArrayList<>();
    private static Map<String, Long> methodInvokeCount = new HashMap<>();

    public static void onMethod(String signature, long startTimeStamp, long endTimeStamp) {
        methodStatistic(signature, startTimeStamp, endTimeStamp);
        memoryStatistic(signature);
    }

    private static void methodStatistic(String signature, long startTimeStamp, long endTimeStamp) {
        long duration = endTimeStamp - startTimeStamp;
        MethodStatistics statistics = statisticsMap.get(signature);
        if (statistics == null) {
            statistics = new MethodStatistics();
            statisticsMap.put(signature, statistics);
            statistics.signature = signature;
            statistics.firstStartTimeStamp = startTimeStamp;
            statistics.firstEndTimeStamp = endTimeStamp;
        }
        statistics.executionCount++;
        statistics.lastStartTimeStamp = startTimeStamp;
        statistics.lastEndTimeStamp = endTimeStamp;
        statistics.totalExecutionTime += duration;
        statistics.longestExecutionTime = statistics.longestExecutionTime > duration ? statistics.longestExecutionTime : duration;
    }

    private static void memoryStatistic(String signature) {
        long invokedCount = methodInvokeCount.getOrDefault(signature, 0L);
        if (invokedCount < 10 || (invokedCount & (invokedCount - 1)) == 0) {
            System.gc();
            long heapSize = Runtime.getRuntime().totalMemory();
            long heapMaxSize = Runtime.getRuntime().maxMemory();
            long heapFreeSize = Runtime.getRuntime().freeMemory();
            memoryStatistics.add(new MemoryStatistics(signature, invokedCount + 1, heapSize, heapMaxSize, heapFreeSize));
        }
        methodInvokeCount.put(signature, invokedCount + 1);
    }

    public static void logResult() {
        logMethod();
        logMemory();
    }

    private static void logMethod() {
        log("==============================================================================");
        log("==                      Method Profiling Result                             ==");
        log("==============================================================================");
        log(String.format("%15s,%15s,%15s,%15s,%15s,%15s,%15s,%15s,%s",
                "First Start", "First End", "Execution Count", "Total Time", "Average Time", "Longest Time", "Last Start", "Last End", "Method"));
        statisticsMap.values().stream()
                .sorted(Comparator.comparing(stat -> stat.firstStartTimeStamp))
                .forEach(stat -> log(String.format("%15s,%15s,%15s,%15s,%15s,%15s,%15s,%15s,%60s",
                        toTime(stat.firstStartTimeStamp),
                        toTime(stat.firstEndTimeStamp),
                        stat.executionCount,
                        stat.totalExecutionTime,
                        ((double) stat.totalExecutionTime) / stat.executionCount,
                        stat.longestExecutionTime,
                        toTime(stat.lastStartTimeStamp),
                        toTime(stat.lastEndTimeStamp),
                        stat.signature))
                );
    }

    private static void logMemory() {
        log("==============================================================================");
        log("==                      Memory Profiling Result                             ==");
        log("==============================================================================");
        log(String.format("%15s,%15s,%15s,%15s,%15s,%s",
                "Heap Size", "Heap Max Size", "Heap Free Size", "Heap Used Size", "Method Calls", "Method"));
        memoryStatistics.stream()
                .forEach(stat -> log(String.format("%15s,%15s,%15s,%15s,%15s,%s",
                        bitFormat(stat.heapSize),
                        bitFormat(stat.heapMaxSize),
                        bitFormat(stat.heapFreeSize),
                        bitFormat(stat.heapSize - stat.heapFreeSize),
                        stat.count,
                        stat.signature))
                );
    }

    private static String bitFormat(long bytes) {
        int unit = 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        exp = Math.min(exp, 2); // MB at most
        String pre = ("KMG").charAt(exp - 1) + "";
        return String.format("%.3f %sB", bytes / ((float) Math.pow(unit, exp)), pre);
    }

    private static void log(String s) {
        System.out.println(s);
    }

    public static String toTime(long timestamp) {
        return sdf.format(new Date(timestamp));
    }
}
