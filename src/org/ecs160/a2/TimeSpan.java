package org.ecs160.a2;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.Duration;
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

    public String getFormattedTimeAsString(LocalDateTime time){
        return time.format(timeFormat);
    }

    public String getTimeAsString(String whichTime){
        if (whichTime.equals("startTime")) {
            return startTime.format(timeFormat);
        } else if (whichTime.equals("endTime")){
            return endTime.format(timeFormat);
        } else {
            return startTime.format(timeFormat);
        }
    }

    public String getStartTimeAsString(){
        return startTime.format(timeFormat);
    }

    public String getEndTimeAsString(){
        return endTime.format(timeFormat);
    }

    public String getStartEndTimeAsString(){
        String startTime = this.getStartTimeAsString();
        String endTime = this.getEndTimeAsString();
        return "Start:" + startTime + " - " + "End:" + endTime;
    }

    public Duration getTimeSpanDuration(){
        if (endTime == null){
            LocalDateTime endTime = LocalDateTime.now();
            return Duration.between(startTime, endTime);
        }
        return Duration.between(startTime, endTime);
    }

    public Duration getTimeSpanDurationBetween
            (LocalDateTime startDate, LocalDateTime endDate){
        LocalDateTime trueStartTime = startDate;
        LocalDateTime trueEndTime = endDate;
        if (startTime.isAfter(startDate)){
            trueStartTime = startTime;
        }
        if (endTime.isBefore(endDate)){
            trueEndTime = endTime;
        }
        return Duration.between(trueStartTime, trueEndTime);
    }

    public static void main(String[] args){
        LocalDateTime start = LocalDateTime.now().minusSeconds(5);
        LocalDateTime end = LocalDateTime.of(2021,2,21,3,0);
        TimeSpan span = new TimeSpan(start);
        System.out.println(span.getTimeSpanDuration().toSeconds());
    }
}
