package org.ecs160.a2;

import java.time.Duration;
import java.time.LocalDate;
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
        assert(newTask.getName() != null): "Need a task name";
        taskSet.add(newTask);
    }

    public void removeTask(Task newTask) {
        taskSet.remove(newTask);
    }

    public Task getTaskByName(String taskName) {
        return find(aTask -> taskName.equals(aTask.getName()));
    }

    public Task getTaskById(int taskid) {
        for (Task task : taskSet){
            if(task.getId()==taskid){
                return task;
            }
        }
        return null;
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
        if(getTotalTime(start, stop) == 0) return Long.valueOf(0);
        LongSummaryStatistics stats = getTimeStatistics(start, stop);
        return stats.getMin();
    }

    public Long getAverageTime(LocalDateTime start, LocalDateTime stop) { //TODO pick return type

        LongSummaryStatistics stats = getTimeStatistics(start, stop);
        return Math.round(stats.getAverage());
    }

    public Long getMaximumTime(LocalDateTime start, LocalDateTime stop) { //TODO pick return type
        if(getTotalTime(start, stop) == 0) return Long.valueOf(0);
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
