package org.ecs160.a2;
import jdk.jshell.execution.Util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

public class TimeSpan {
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public TimeSpan(LocalDateTime startTime){
        this.startTime = startTime;
    }

    public Boolean isActive() {
        return endTime == null;
    }

    public void setStartTime(LocalDateTime startTime){
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime){
        this.endTime = endTime;
    }

    public LocalDateTime getStartTime(){
        return startTime;
    }
    public LocalDateTime getEndTime(){
        return endTime;
    }

    public String getStartTimeAsString(){
        return startTime.format(Utility.timeFormatter12hr);
    }

    public String getEndTimeAsString(){
        assert (endTime != null): "Time Span is still active!";
        return endTime.format(Utility.timeFormatter12hr);
    }

    public Duration getTimeSpanDuration(){
        if (endTime == null)
            return Duration.between(startTime, LocalDateTime.now());
        return Duration.between(startTime, endTime);
    }

    public LocalDateTime getStartTimeAsDate(){
        return startTime;
    }

    public LocalDateTime getEndTimeAsDate(){
        return endTime;
    }

    public Duration getTimeSpanDurationBetween(LocalDateTime startOfTimeWindow,
                                               LocalDateTime endOfTimeWindow){

        LocalDateTime trueStartTime = startOfTimeWindow;
        if (startTime.isAfter(startOfTimeWindow))
            trueStartTime = startTime;

        LocalDateTime tempEndTime = getEndTimeElseNow();
        LocalDateTime trueEndTime = endOfTimeWindow;
        if (tempEndTime.isBefore(endOfTimeWindow))
            trueEndTime = tempEndTime;

        Duration timeBetween = Duration.between(trueStartTime, trueEndTime);

        // Need to check if the timeframe was outside
        if (timeBetween.isNegative()) return Duration.ofMillis(0);
        return Duration.between(trueStartTime, trueEndTime);
    }

    private LocalDateTime getEndTimeElseNow() {
        if (endTime == null)
            return LocalDateTime.now();
        return endTime;
    }

    public static void main(String[] args){
        LocalDateTime start = LocalDateTime.now().minusSeconds(5);
        LocalDateTime end = LocalDateTime.of(2021,2,21,3,0);
        TimeSpan span = new TimeSpan(start);
    }
}
