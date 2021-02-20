package org.ecs160.a2;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class TaskContainer {
    private Set<Task> taskSet;

    public TaskContainer() {
        taskSet = new HashSet<>();
    }

    private TaskContainer(Set<Task> newTaskSet) {
        taskSet = newTaskSet;
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
}
