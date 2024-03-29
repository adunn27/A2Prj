package org.ecs160.a2;

import java.time.Duration;
import java.time.LocalDateTime;

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

    public Duration getTimeSpanDuration(){
        return getTimeSpanDurationBetween(LocalDateTime.MIN, LocalDateTime.MAX);
    }

    public LocalDateTime getStartTimeAsDate(){
        return startTime;
    }

    public LocalDateTime getEndTimeAsDate(){
        return endTime;
    }

    public Duration getTimeSpanDurationBetween(LocalDateTime startOfTimeWindow,
                                               LocalDateTime endOfTimeWindow){

        LocalDateTime trueStartTime =
                findConstrictingLowerBoundOfWindow(startOfTimeWindow);

        LocalDateTime trueEndTime =
                findConstrictingUpperBoundOfWindow(endOfTimeWindow);

        Duration timeBetween = Duration.between(trueStartTime, trueEndTime);
        // Necessary to check as if the timespan had no overlap with the
        // given time window, then this will return a negative value
        if (timeBetween.isNegative()) return Duration.ofMillis(0);
        return timeBetween;
    }

    private LocalDateTime findConstrictingLowerBoundOfWindow(
                                            LocalDateTime startOfTimeWindow) {
        LocalDateTime trueStartTime = startOfTimeWindow;
        if (startTime.isAfter(startOfTimeWindow))
            trueStartTime = startTime;
        return trueStartTime;
    }

    private LocalDateTime findConstrictingUpperBoundOfWindow(
                                            LocalDateTime endOfTimeWindow) {
        LocalDateTime trueEndTime = endOfTimeWindow;
        LocalDateTime tempEndTime = getEndTimeElseNow();
        if (tempEndTime.isBefore(endOfTimeWindow))
            trueEndTime = tempEndTime;
        return trueEndTime;
    }

    private LocalDateTime getEndTimeElseNow() {
        if (endTime == null)
            return LocalDateTime.now();
        return endTime;
    }
}

