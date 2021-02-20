package org.ecs160.a2;

import java.util.HashSet;
import java.util.Set;

public class TaskContainer {
    private Set<Task> taskSet;

    public TaskContainer() {
        taskSet = new HashSet<>();
    }

    public void addTask(Task newTask) {
        taskSet.add(newTask);
    }

    public void removeTask(Task newTask) {
        taskSet.remove(newTask);
    }
}
