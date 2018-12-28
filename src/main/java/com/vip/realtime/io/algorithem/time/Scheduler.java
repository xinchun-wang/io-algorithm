package com.vip.realtime.io.algorithem.time;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Scheduler {
    /**
     *
     * @param interval every 5 min
     * @param timeUnit align TimeUnit.HOURS
     * @return
     */
    protected long triggerTime(long interval, TimeUnit timeUnit){
        LocalDateTime localDateTime = LocalDateTime.now();
        long currentMillis = System.currentTimeMillis();

        long aligendTriggerMillis = getAlignedTimeByTriggerUnit(localDateTime, timeUnit);

        while(aligendTriggerMillis <= currentMillis){
            long nextAligendTriggerMillis = aligendTriggerMillis + interval;
            if(nextAligendTriggerMillis > currentMillis){
                return nextAligendTriggerMillis;
            }
            aligendTriggerMillis = nextAligendTriggerMillis;
        }
        return  0L;
    }

    protected long getAlignedTimeByTriggerUnit(LocalDateTime localDateTime, TimeUnit timeUnit){
        switch (timeUnit) {
            case MILLISECONDS:
                localDateTime = localDateTime.withNano(0);
                break;
            case SECONDS:
                localDateTime = localDateTime.with(ChronoField.MILLI_OF_SECOND, 0).withNano(0);
                break;
            case MINUTES:
                localDateTime = localDateTime.withSecond(0).with(ChronoField.MILLI_OF_SECOND, 0).withNano(0);
                break;
            case HOURS:
                localDateTime = localDateTime.withMinute(0).withSecond(0).with(ChronoField.MILLI_OF_SECOND, 0).withNano(0);
                break;
            case DAYS:
                localDateTime = localDateTime.withHour(0).withMinute(0).withSecond(0).with(ChronoField.MILLI_OF_SECOND, 0).withNano(0);
                break;
            default:
                break;
        }
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return instant.toEpochMilli();
    }

    public static void main(String[] args){
        Scheduler scheduler = new Scheduler();
        long triggerTime = scheduler.triggerTime(10 * 1000, TimeUnit.MINUTES);
        Date date = new Date(triggerTime);
        System.out.println(date);
    }

}
