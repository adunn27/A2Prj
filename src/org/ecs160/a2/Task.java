package org.ecs160.a2;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class Task {
    private static final TaskSize DEFAULT_TASK_SIZE = TaskSize.S;

    private String name;
    private int taskId;
    private TaskSize size = DEFAULT_TASK_SIZE;
    private String description = "";
    private Boolean isArchived = false;
    private Boolean isActive = false;
    private final List<TimeSpan> allTimes;
    private final Set<String> tags;

    public Task() {
        this.tags = new HashSet<>();
        this.allTimes = new Vector<>();
    }

    public void setId(int taskId){
        this.taskId = taskId;
    }

    public int getId(){
        return taskId;
    }

    public String getName() {
        return name;
    }

    public void setName(String newTaskName) {
        name = newTaskName;
    }

    public TaskSize getTaskSize() {
        return size;
    }

    public String getTaskSizeString() {
        return size.toString();
    }

    public void setTaskSizeWithString(String newTaskSizeString) {
        size = TaskSize.parse(newTaskSizeString);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String newDescription) {
        description = newDescription;
    }

    public Boolean isActive() {
        return this.isActive;
    }

    public Boolean isArchived() {
        return this.isArchived;
    }

    public void archive() {
        assert (!isArchived): "Cannot archive an already archived task";
        assert (!isActive): "Cannot archive an active task";
        isArchived = true;
    }

    public void unarchive() {
        assert (isArchived): "Cannot unarchive a " +
                                     "task that isn't archived";
        isArchived = false;
    }

    public void start(LocalDateTime time) {
        assert (!isActive): "Cannot start an already active task";
        assert (!isArchived): "Cannot start an archived task";
        allTimes.add(new TimeSpan(time));
        isActive = true;
    }

    public void stop(LocalDateTime time) {
        assert (isActive) : "Cannot stop an inactive task";
        allTimes.get(allTimes.size() - 1).setEndTime(time);
        isActive = false;
    }

    public Boolean hasTag(String tag) {
        return tags.contains(tag);
    }

    public void addTag(String tag) {
        if (tag.isEmpty())
            return;
        tags.add(tag);
    }

    public void addAllTags(List<String> tags) {
        for (String t : tags) {
            if (!hasTag(t)) addTag(t);
        }
    }

    public void removeTag(String tag) {
        tags.remove(tag);
    }

    public List<String> getTags() {
        return tags.stream().sorted().collect(Collectors.toList());
    }

    public Duration getTimeBetween(LocalDateTime start, LocalDateTime stop) {
        Duration totalTime = Duration.ofMillis(0);
        for (TimeSpan timeSpan: allTimes) {
            totalTime = totalTime.plus(
                    timeSpan.getTimeSpanDurationBetween(start, stop)
            );
        }
        return totalTime;
    }

    private Duration getTotalTimeOfDay(LocalDate day) {
        return getTimeBetween(day.atStartOfDay(),
                day.atTime(LocalTime.MAX));
    }

    public List<Long> getDailyTimesBetween(LocalDate start, LocalDate stop) {
        LocalDate currDay = start;
        List<Long> dailyTimes = new ArrayList<>();
        while (!currDay.isAfter(stop)) {
            dailyTimes.add(getTotalTimeOfDay(currDay).toMillis());
            currDay = currDay.plusDays(1);
        }
        return dailyTimes;
    }

    public String getTotalTimeTodayString() {
        return Utility.durationToFormattedString(
                getTotalTimeOfDay(LocalDate.now()));
    }

    public String getTotalTimeThisWeekString() {
        Duration duration = getTimeBetween(Utility.getStartOfCurrentWeek(),
                Utility.getEndOfWeek(LocalDateTime.now()));
        return Utility.durationToFormattedString(duration);
    }

    public String getTotalTimeString() {
        Duration duration = getTimeBetween(LocalDateTime.MIN, LocalDateTime.MAX);
        return Utility.durationToFormattedString(duration);
    }

    public List<TimeSpan> getAllTimeSpans() {
        return allTimes;
    }

    public void removeTimeSpan(TimeSpan deletedTimeSpan){
        allTimes.remove(deletedTimeSpan);
    }

    public TimeSpan getTimeSpanByIndex(int index){
        return allTimes.get(index);
    }

    public int getIndexOfTimeSpan(TimeSpan timeSpan){
        for (int i = 0; i < allTimes.size(); i++) {
            if (allTimes.get(i) == timeSpan)
                return i;
        }
        return -1;
    }
}
