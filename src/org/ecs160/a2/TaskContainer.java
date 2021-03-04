package org.ecs160.a2;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TaskContainer implements Iterable<Task>{
    private Set<Task> taskSet;

    public TaskContainer() {
        taskSet = new HashSet<>();
    }

    public int getNumberOfTasks() {
        return taskSet.size();
    }

    private TaskContainer(Set<Task> newTaskSet) {
        taskSet = newTaskSet;
    }

    public Iterator<Task> iterator() {
        return taskSet.iterator(); //TODO check
    }

    public Boolean isEmpty() {
        return taskSet.isEmpty();
    }

    public void addTask(Task newTask) {
        taskSet.add(newTask);
    }

    public void removeTask(Task newTask) {
        taskSet.remove(newTask);
    }

    public Task getTaskByName(String taskName) {
        for (Task aTask: taskSet) {
            if (taskName.equals(aTask.getName()))
                return aTask;
        }
        return null;
    }

    public Task getActiveTask() {
        for (Task aTask: taskSet) {
            if (aTask.isActive())
                return aTask;
        }
        return null;
    }

    public TaskContainer getInactiveTasks() {
        Set filteredSet = taskSet.stream()
                .filter(task -> !task.isActive())
                .collect(Collectors.toSet());

        return new TaskContainer(filteredSet);
    }

    public TaskContainer getArchivedTasks() {
        Set filteredSet = taskSet.stream()
                .filter(task -> task.isArchived())
                .collect(Collectors.toSet());

        return new TaskContainer(filteredSet);
    }

    public TaskContainer getUnarchivedTasks() {
        Set filteredSet = taskSet.stream()
                .filter(task -> !task.isArchived())
                .collect(Collectors.toSet());

        return new TaskContainer(filteredSet);
    }

    public TaskContainer getTasksBySize(TaskSize taskSize) {
        return filter(task -> task.getTaskSize() == taskSize);
    }

    public TaskContainer getTasksWithTag(String tag) {
        Set filteredSet = taskSet.stream()
                .filter(task -> task.hasTag(tag))
                .collect(Collectors.toSet());

        return new TaskContainer(filteredSet);
    }

    public TaskContainer getTasksThatOccurred(LocalDateTime start,
                                              LocalDateTime stop) {
        Set filteredSet = taskSet.stream()
                .filter(task -> task.occurredBetween(start, stop))
                .collect(Collectors.toSet());

        return new TaskContainer(filteredSet);
    }

    public TaskContainer filter(Predicate<Task> selector) {
        Set filteredSet = taskSet.stream()
                .filter(selector)
                .collect(Collectors.toSet());

        return new TaskContainer(filteredSet);
    }

    private LongSummaryStatistics getTimeStatistics(LocalDateTime start,
                                                    LocalDateTime stop) {
        return taskSet.stream()
                .map(task -> task.getTimeBetween(start, stop))
                .mapToLong(Duration::toMillis)
                .summaryStatistics();
    }

    public Long getTotalTime(LocalDateTime start, LocalDateTime stop) { //TODO pick return type
        LongSummaryStatistics stats = getTimeStatistics(start, stop);

        return stats.getSum();
    }

    public Long getMinimumTime(LocalDateTime start, LocalDateTime stop) { //TODO pick return type
        LongSummaryStatistics stats = getTimeStatistics(start, stop);

        return stats.getMin();
    }

    public Long getAverageTime(LocalDateTime start, LocalDateTime stop) { //TODO pick return type
        LongSummaryStatistics stats = getTimeStatistics(start, stop);

        return Math.round(stats.getAverage());
    }

    public Long getMaximumTime(LocalDateTime start, LocalDateTime stop) { //TODO pick return type
        LongSummaryStatistics stats = getTimeStatistics(start, stop);

        return stats.getMax();
    }

    public List<String> getAllTags() {
        Set<String> setOfAllTags = new HashSet<>();
        for (Task aTask: taskSet) {
            for (String aTag: aTask.getTags()) {
                setOfAllTags.add(aTag); //TODO do this in streams?
            }
        }
        return setOfAllTags.stream().sorted().collect(Collectors.toList());
    }
}
