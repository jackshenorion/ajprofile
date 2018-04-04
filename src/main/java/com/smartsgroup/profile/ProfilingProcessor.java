package com.smartsgroup.profile;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class ProfilingProcessor {
    private static Logger logger = Logger.getLogger(ProfilingProcessor.class.getName());
    private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");

    private static Map<String, MethodStatistics> statisticsMap = new HashMap<>();

    public static void onMethod(String signature, long startTimeStamp, long endTimeStamp) {
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

    public static void logResult() {
        logger.info("==============================================================================");
        logger.info("==                             Profiling Result                             ==");
        logger.info("==============================================================================");
        logger.info(String.format("%15s,%15s,%15s,%15s,%15s,%15s,%15s,%15s,%s",
                "First Start", "First End", "Execution Count", "Total Execution Time", "Average Time", "Longest Time", "Last Start", "Last End", "Method"));
        statisticsMap.values().stream()
                .sorted(Comparator.comparing(stat -> stat.firstStartTimeStamp))
                .forEach(stat -> logger.info(String.format("%15s,%15s,%15s,%15s,%15s,%15s,%15s,%15s,%60s",
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

    public static String toTime(long timestamp) {
        return sdf.format(new Date(timestamp));
    }
}
