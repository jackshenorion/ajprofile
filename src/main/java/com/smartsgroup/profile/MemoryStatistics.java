package com.smartsgroup.profile;

public class MemoryStatistics {
    public String signature;
    public long count = 0;
    public long heapSize = 0;
    public long heapMaxSize = 0;
    public long heapFreeSize = 0;

    public MemoryStatistics(String signature, long count, long heapSize, long heapMaxSize, long heapFreeSize) {
        this.signature = signature;
        this.count = count;
        this.heapSize = heapSize;
        this.heapMaxSize = heapMaxSize;
        this.heapFreeSize = heapFreeSize;
    }

    @Override
    public String toString() {
        return "MemoryStatistics{" +
                "signature='" + signature + '\'' +
                ", count=" + count +
                ", heapSize=" + heapSize +
                ", heapMaxSize=" + heapMaxSize +
                ", heapFreeSize=" + heapFreeSize +
                '}';
    }
}
