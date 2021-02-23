package org.ecs160.a2;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Task {
    private static final TaskSize DEFAULT_TASK_SIZE = TaskSize.S;

    private String name;
    private TaskSize size;
    private String description = "";
    private Boolean isArchived = false;
    private Boolean isActive = false;
    private List<TimeSpan> allTimes; //TODO better name
    private Set<String> tags; //TODO does order matter? Do tags need color also?

    private void construct(String newTaskName, TaskSize newTaskSize) {
        this.name = newTaskName;
        this.size = newTaskSize;
        this.tags = new HashSet<>();
        this.allTimes = new Vector<TimeSpan>();

    }

    public Task(String newTaskName, TaskSize newTaskSize) {
        construct(newTaskName, newTaskSize);
    }

    public Task(String newTaskName) {
        construct(newTaskName, DEFAULT_TASK_SIZE);
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

    public void setTaskSize(String newTaskSizeString) {
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
        assert (isArchived == false): "Cannot archive an already archived task";
        isArchived = true;
    }

    public void unarchive() {
        assert (isArchived == true): "Cannot unarchive a " +
                                     "task that isn't archived";
        isArchived = false;
    }

    public void start() {
        assert (isActive == false): "Cannot start an already active task";
        assert (isArchived == false): "Cannot start an archived task";

        allTimes.add(new TimeSpan(LocalDateTime.now())); //TODO make all now have same time
        isActive = true;
    }

    public void stop() {
        assert (isActive == true): "Cannot stop an inactive task";
        allTimes.get(allTimes.size() - 1).setEndTime(LocalDateTime.now());
        isActive = false;
    }

    public Boolean hasTag(String tag) {
        return tags.contains(tag);
    }

    public void addTag(String tag) {
        tags.add(tag);
    }

    public void removeTag(String tag) {
        tags.remove(tag);
    }

    public List<String> getTags() {
        return tags.stream().sorted().collect(Collectors.toList());
    }

    public Duration getTimeBetween(LocalDateTime start, LocalDateTime stop) { //TODO return type?
        Duration totalTime = Duration.ofMillis(0);
        for (TimeSpan timeSpan: allTimes) {
            totalTime = totalTime.plus(
                    timeSpan.getTimeSpanDurationBetween(start, stop)
            );
        }
        return totalTime;
    }

    public String getTotalTimeString() {
        return getTimeBetween(LocalDateTime.MIN, LocalDateTime.MAX).toString();
    }

    public String getTotalTimeTodayString() {
        return getTimeBetween(LocalDateTime.MIN, LocalDateTime.MAX).toString();
    }

    public Boolean occurredBetween(LocalDateTime start, LocalDateTime stop) {
        return !getTimeBetween(start, stop).isZero();
    }
}
