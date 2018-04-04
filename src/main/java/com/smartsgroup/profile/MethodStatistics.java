package com.smartsgroup.profile;

public class MethodStatistics {
    public String signature;
    public long firstStartTimeStamp = 0;
    public long firstEndTimeStamp = 0;
    public long lastStartTimeStamp = 0;
    public long lastEndTimeStamp = 0;
    public long executionCount = 0;
    public long totalExecutionTime = 0;
    public long longestExecutionTime = 0;

    @Override
    public String toString() {
        return "MethodStatistics{" +
                "signature='" + signature + '\'' +
                ", firstStartTimeStamp=" + firstStartTimeStamp +
                ", firstEndTimeStamp=" + firstEndTimeStamp +
                ", lastStartTimeStamp=" + lastStartTimeStamp +
                ", lastEndTimeStamp=" + lastEndTimeStamp +
                ", executionCount=" + executionCount +
                ", totalExecutionTime=" + totalExecutionTime +
                ", longestExecutionTime=" + longestExecutionTime +
                '}';
    }
}
