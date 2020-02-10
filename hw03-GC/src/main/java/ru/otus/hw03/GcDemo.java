package ru.otus.hw03;

import com.sun.management.GarbageCollectionNotificationInfo;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.List;
import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;

public class GcDemo {

    public static void main(String... args) throws Exception {
        System.out.println("Starting pid: " + ManagementFactory.getRuntimeMXBean().getName());
        GcInfo gcInfo = new GcInfo();
        switchOnMonitoring(gcInfo);
        long beginTime = System.currentTimeMillis();
        int loopCounter = 1000 * 1000 * 1000;
        Benchmark benchmark = new Benchmark(loopCounter);
        benchmark.run();
        System.out.println("time:" + (System.currentTimeMillis() - beginTime) / 1000);

    }

    private static void switchOnMonitoring(GcInfo gcInfo) {

        List<GarbageCollectorMXBean> gcbeans = java.lang.management.ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gcbean : gcbeans) {
            System.out.println("GC name:" + gcbean.getName());
            NotificationEmitter emitter = (NotificationEmitter) gcbean;
            NotificationListener listener = (notification, handback) -> {
                if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                    GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());
                    String gcName = info.getGcName();
                    String gcAction = info.getGcAction();
                    String gcCause = info.getGcCause();
                    long startTime = info.getGcInfo().getStartTime();
                    long duration = info.getGcInfo().getDuration();

                    if(gcAction.contains("minor"))
                    {
                        gcInfo.incrementYoung_count();
                        gcInfo.addToYoung_duration(duration);
                    }
                    else if(gcAction.contains("major"))
                    {
                        gcInfo.incrementOld_count();
                        gcInfo.addToOld_duration(duration);
                    }

                    System.out.println("start:" + startTime + " Name:" + gcName + ", action:" + gcAction + ", gcCause:" + gcCause + "(" + duration + " ms)");
                    System.out.println("Total Young Generation: " + gcInfo.getYoung_count() + "; Time total: " + gcInfo.getYoung_duration() + " ms; " + ((double) gcInfo.getYoung_duration())/startTime*1000*60 + " ms per minute");
                    System.out.println("Total Old Generation: " + gcInfo.getOld_count() + "; Time total: " + gcInfo.getOld_duration() + " ms; " + ((double) gcInfo.getOld_duration())/startTime*1000*60 + " ms per minute");
                    System.out.println();
                }
            };
            emitter.addNotificationListener(listener, null, null);
        }
    }



}