package org.ecs160.a2;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Iterator;
import java.util.LongSummaryStatistics;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TaskContainer implements Iterable<Task>{
    private final Set<Task> taskSet;

    public TaskContainer() {
        taskSet = new HashSet<>();
    }

    public Iterator<Task> iterator() {
        return taskSet.iterator();
    }

    public int getNumberOfTasks() {
        return taskSet.size();
    }

    private TaskContainer(Set<Task> newTaskSet) {
        taskSet = newTaskSet;
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
        return find(aTask -> taskName.equals(aTask.getName()));
    }

    public Task getTaskById(int taskId) {
        return find(aTask -> aTask.getId() == taskId);
    }

    public Task getActiveTask() {
        return find(Task::isActive);
    }

    public Task find(Predicate<Task> selector) {
        return taskSet.stream()
                .filter(selector)
                .findFirst()
                .orElse(null);
    }

    public TaskContainer getInactiveTasks() {
        return filter(task -> !task.isActive());
    }

    public TaskContainer getArchivedTasks() {
        return filter(Task::isArchived);
    }

    public TaskContainer getUnarchivedTasks() {
        return filter(task -> !task.isArchived());
    }

    public TaskContainer getTasksBySize(TaskSize taskSize) {
        return filter(task -> task.getTaskSize() == taskSize);
    }

    public TaskContainer getTasksWithTag(String tag) {
        return filter(task -> task.hasTag(tag));
    }

    public TaskContainer getTasksThatOccurred(LocalDate start,
                                              LocalDate stop) {
        return filter(task -> task.occurredBetween(
                Utility.getStartOfDay(start),
                Utility.getEndOfDay(stop)));
    }

    public TaskContainer filter(Predicate<Task> selector) {
        Set<Task> filteredSet = taskSet.stream()
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

    public Long getTotalTime(LocalDateTime start, LocalDateTime stop) {

        LongSummaryStatistics stats = getTimeStatistics(start, stop);

        return stats.getSum();
    }

    public Long getMinimumTime(LocalDateTime start, LocalDateTime stop) {
        if(getTotalTime(start, stop) == 0) return 0L;
        LongSummaryStatistics stats = getTimeStatistics(start, stop);
        return stats.getMin();
    }

    public Long getAverageTime(LocalDateTime start, LocalDateTime stop) {

        LongSummaryStatistics stats = getTimeStatistics(start, stop);
        return Math.round(stats.getAverage());
    }

    public Long getMaximumTime(LocalDateTime start, LocalDateTime stop) {
        if(getTotalTime(start, stop) == 0) return 0L;
        LongSummaryStatistics stats = getTimeStatistics(start, stop);
        return stats.getMax();
    }

    public List<String> getAllTags() {
        Set<String> setOfAllTags = new HashSet<>();
        for (Task aTask: taskSet) {
            setOfAllTags.addAll(aTask.getTags());
        }
        return setOfAllTags.stream().sorted().collect(Collectors.toList());
    }
}
