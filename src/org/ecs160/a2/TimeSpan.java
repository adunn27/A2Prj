package org.ecs160.a2;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.Duration;
public class TimeSpan {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");

    public TimeSpan(LocalDateTime startTime){
        this.startTime = startTime;
    }

    public void setStartTime(LocalDateTime startTime){
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime){
        this.endTime = endTime;
    }

    //Unnecessary abstraction?
    public String getTime(String whichTime){
        if (whichTime.equals("startTime")) {
            return startTime.format(timeFormat);
        } else if (whichTime.equals("endTime")){
            return endTime.format(timeFormat);
        } else {
            return startTime.format(timeFormat);
        }
    }

    public String getStartTime(){
        return startTime.format(timeFormat);
    }

    public String getEndTime(){
        return endTime.format(timeFormat);
    }

    public String getStartEndTime(){
        String startTime = this.getStartTime();
        String endTime = this.getEndTime();
        return "Start:" + startTime + " - " + "End:" + endTime;
    }

    public Duration getDuration(){
        //this function should return error if no end time
        if (endTime == null){
            LocalDateTime endTime = LocalDateTime.now();
            return Duration.between(startTime, endTime);
        }
        return Duration.between(startTime, endTime);
    }

    public Duration getDurationBetween(LocalDateTime startDate, LocalDateTime endDate){
        LocalDateTime trueStartTime = startDate;
        LocalDateTime trueEndTime = endDate;
        // if startTime > StartDate
        if (startTime.isAfter(startDate)){
            trueStartTime = startTime;
        }
        if (endTime.isBefore(endDate)){
            trueEndTime = endTime;
        }
        return Duration.between(trueStartTime, trueEndTime);
    }

    public static void main(String[] args){

        //Checking Duration
        //LocalDateTime start = LocalDateTime.of(2021,2,20,23,0);
        LocalDateTime start = LocalDateTime.now().minusSeconds(5);
        //startTime at 20th 11PM
        LocalDateTime end = LocalDateTime.of(2021,2,21,3,0);
        //endTime at 21st 3AM

        TimeSpan span = new TimeSpan(start);
//        span.setEndTime(end);

        //Check between
//        LocalDateTime startDate = LocalDateTime.of(2021,2,21,0,0);
        //startDate at 21st 12AM
//        LocalDateTime endDate = LocalDateTime.of(2021, 2, 23, 1, 0);
        //endDate at 23rd 1AM

        //span.getDurationBetween()
//        System.out.print(span.getDurationBetween(startDate,endDate).toHours());
        System.out.println(span.getDuration().toSeconds());
    }
}
