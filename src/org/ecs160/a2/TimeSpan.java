package org.ecs160.a2;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

public class TimeSpan {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private final DateTimeFormatter timeFormat =
            DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");

    public TimeSpan(LocalDateTime startTime){
        this.startTime = startTime;
    }

    public LocalDateTime getstarttime(){
        return this.startTime;
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
    public String getFormattedTimeAsString(LocalDateTime time){
        return time.format(timeFormat);
    }

    public String getStartTimeAsString(){
        return startTime.format(timeFormat);
    }

    public String getEndTimeAsString(){
        if (endTime == null){
            return LocalDateTime.now().format(timeFormat);
        }
        return endTime.format(timeFormat);
    }

    public String getStartEndTimeAsString(){
        String startTime = this.getStartTimeAsString();
        String endTime = this.getEndTimeAsString();
        return "Start:" + startTime + " - " + "End:" + endTime;
    }

    public Duration getTimeSpanDuration(){
        if (endTime == null){
            LocalDateTime nowTime = LocalDateTime.now();
            return Duration.between(startTime, nowTime);
        }
        return Duration.between(startTime, endTime);
    }

    public LocalDateTime getStartTimeAsDate(){
        return startTime;
    }

    public LocalDateTime getEndTimeAsDate(){
        return endTime;
    }

    public Duration getTimeSpanDurationBetween
            (LocalDateTime startOfTimeWindow, LocalDateTime endOfTimeWindow){

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

