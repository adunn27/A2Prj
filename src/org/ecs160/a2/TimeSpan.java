package org.ecs160.a2;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.Duration;
import java.util.Date;

public class TimeSpan {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public TimeSpan(LocalDateTime StartTime){
        startTime = StartTime;
    }

    public void setStartTime(LocalDateTime StartTime){
        startTime = StartTime;
    }

    public void setEndTime(LocalDateTime EndTime){
        endTime = EndTime;
    }

    public String getStartTime(){
        String formatStartTime = startTime.format(timeFormat);
        return formatStartTime;
    }

    public String getEndTime(){
        String formatEndTime = endTime.format(timeFormat);
        return formatEndTime;
    }

    public String getStartEndTime(){
        String startTime = this.getStartTime();
        String endTime = this.getEndTime();
        String StartEnd = "Start:" + startTime + " - " + "End:" + endTime;
        return StartEnd;
    }

    public Duration getDuration(){
        //this function should return error if no end time
        if (endTime == null){
            Duration duration = Duration.between(startTime, startTime);
            return duration;
        }
        Duration duration = Duration.between(startTime, endTime);
        return duration;
    }

    public Duration getDurationBetween(LocalDateTime StartDate, LocalDateTime EndDate){
        LocalDateTime trueStartTime = StartDate;
        LocalDateTime trueEndTime = EndDate;
        // if startTime > StartDate
        if (startTime.isAfter(StartDate)){
            trueStartTime = startTime;
        }
        if (endTime.isBefore(EndDate)){
            trueEndTime = endTime;
        }
        Duration duration = Duration.between(trueStartTime, trueEndTime);
        return duration;
    }

    public static void main(String[] args){

        LocalDateTime start = LocalDateTime.of(2021,2,20,23,0);
        //startTime at 20th 11PM
        LocalDateTime end = LocalDateTime.of(2021,2,21,3,0);
        //endTime at 21st 3AM

        TimeSpan span = new TimeSpan(start);
        span.setEndTime(end);

        //Check between
        LocalDateTime startDate = LocalDateTime.of(2021,2,21,0,0);
        //startDate at 21st 12AM
        LocalDateTime endDate = LocalDateTime.of(2021, 2, 23, 1, 0);
        //endDate at 23rd 1AM

        //span.getDurationBetween()
        System.out.print(span.getDurationBetween(startDate,endDate).toHours());
    }
}
