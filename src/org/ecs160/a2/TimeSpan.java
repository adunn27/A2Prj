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
            (LocalDateTime startDate, LocalDateTime endDate){
        LocalDateTime trueStartTime = startDate;
        LocalDateTime trueEndTime = endDate;
        if (startTime.isAfter(startDate)){
            trueStartTime = startTime;
        }

        LocalDateTime tempEndTime;
        if (endTime == null)
            tempEndTime = LocalDateTime.now();
        else
            tempEndTime = endTime;

        if (tempEndTime.isBefore(endDate)){
            trueEndTime = tempEndTime;
        }
        return Duration.between(trueStartTime, trueEndTime);
    }

    public static LocalDateTime getStartOfDay(LocalDateTime present) {
        return present.with(LocalTime.MIN);
    }

    public static LocalDateTime getEndOfDay(LocalDateTime present) {
        return present.with(LocalTime.MAX);
    }

    // Decided that since this tool will be mainly for work related purposes
    // a week should start with the work week on Monday rather than the calendar
    // week on Sunday
    public static LocalDateTime getStartOfWeek(LocalDateTime present) {
        return present.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .with(LocalDateTime.MIN);
    }

    public static LocalDateTime getEndOfWeek(LocalDateTime present) {
        return present.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
                .with(LocalDateTime.MAX);
    }

    public static void main(String[] args){
        LocalDateTime start = LocalDateTime.now().minusSeconds(5);
        LocalDateTime end = LocalDateTime.of(2021,2,21,3,0);
        TimeSpan span = new TimeSpan(start);

    }
}
