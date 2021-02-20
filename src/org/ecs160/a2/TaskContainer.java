package org.ecs160.a2;

import jdk.nashorn.internal.objects.SetIterator;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LongSummaryStatistics;
import java.util.Set;
import java.util.stream.Collectors;

public class TaskContainer implements Iterable<Task>{
    private Set<Task> taskSet;

    public TaskContainer() {
        taskSet = new HashSet<>();
    }

    private TaskContainer(Set<Task> newTaskSet) {
        taskSet = newTaskSet;
    }

    public Iterator<Task> iterator() {
        return taskSet.iterator(); //TODO check
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

    public TaskContainer getTasksBySize(TaskSize taskSize) {
        Set filteredSet = taskSet.stream()
                .filter(task -> task.getTaskSize() == taskSize)
                .collect(Collectors.toSet());

        return new TaskContainer(filteredSet);
    }

    public TaskContainer getTasksWithTag(String tag) {
        Set filteredSet = taskSet.stream()
                .filter(task -> task.hasTag(tag))
                .collect(Collectors.toSet());

        return new TaskContainer(filteredSet);
    }

    private LongSummaryStatistics getTimeStatistics() {
        return taskSet.stream()
                .mapToLong(task -> task.getTotalTime())
                .summaryStatistics();
    }

    public Long getTotalTime() { //TODO pick return type
        LongSummaryStatistics stats = getTimeStatistics();

        return stats.getSum();
    }

    public Long getMinimumTime() { //TODO pick return type
        LongSummaryStatistics stats = getTimeStatistics();

        return stats.getMin();
    }

    public Long getAverageTime() { //TODO pick return type
        LongSummaryStatistics stats = getTimeStatistics();

        return Math.round(stats.getAverage());
    }

    public Long getMaximumTime() { //TODO pick return type
        LongSummaryStatistics stats = getTimeStatistics();

        return stats.getMax();
    }
}
