package me.student;

public class MyMemoryUsage {
    private Runtime runtime;
    private long    usedMemoryBefore;
    private long    usedMemoryAfter;

    MyMemoryUsage() {
        this.runtime = Runtime.getRuntime();
        this.usedMemoryBefore = runtime.totalMemory() - runtime.freeMemory();
        this.usedMemoryAfter  = runtime.totalMemory() - runtime.freeMemory();
    }

    public long getMaxUsedMemmory() { return usedMemoryAfter - usedMemoryBefore; }

    private void updateUsedMemmory(long mem) { this.usedMemoryAfter = mem; }

    public void updateMaxUsedMemmory() {
        long mem_now = runtime.totalMemory() - runtime.freeMemory();
        if(mem_now > usedMemoryAfter) updateUsedMemmory(mem_now);
    }

}
