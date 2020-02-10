package ru.otus.hw03;

public class GcInfo {
    private int old_count = 0;
    private int young_count = 0;
    private long old_duration = 0;
    private long young_duration = 0;

    public int getOld_count() {
        return old_count;
    }

    public void incrementOld_count() {
        this.old_count++;
    }

    public int getYoung_count() {
        return young_count;
    }

    public void incrementYoung_count() {
        this.young_count++;
    }

    public long getOld_duration() {
        return old_duration;
    }

    public void addToOld_duration(long old_duration) {
        this.old_duration += old_duration;
    }

    public long getYoung_duration() {
        return young_duration;
    }

    public void addToYoung_duration(long young_duration) {
        this.young_duration += young_duration;
    }
}
