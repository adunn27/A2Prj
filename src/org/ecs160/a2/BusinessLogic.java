package org.ecs160.a2;

import java.util.ArrayList;
import java.util.List;

public class BusinessLogic {
    private TaskContainer everyTask;

    public BusinessLogic() {
        //TODO load it in somehow
        everyTask = new TaskContainer();

        //TODO tests
        newTask("task1",
                "S",
                "the first task",
                List.of("tag1","tag2","tag3"));
        newTask("task2",
                "M",
                "the first task",
                List.of("tag1"));
        newTask("task3",
                "M",
                "the first task",
                List.of("tag2","tag3"));
        newTask("task4",
                "L",
                "the first task",
                List.of("tag1","tag3"));
        newTask("task5",
                "XL",
                "the first task",
                List.of());
    }

    public List<String> getAllTags() {
        return everyTask.getAllTags();
    }

    public Task newTask(String name,
                        String size,
                        String description,
                        List<String> tags) {
        Task aNewTask = new Task(name, TaskSize.parse(size));
        aNewTask.setDescription(description);
        for(String aTag: tags) {
            aNewTask.addTag(aTag);
        }
        everyTask.addTask(aNewTask);
        return aNewTask;
    }

    public void saveTask(Task newTask) {
        everyTask.addTask(newTask);
    }

    public Task getActiveTask() {
        return everyTask.getActiveTask();
    }

    public TaskContainer getUnarchivedTasks() {
        return everyTask.getUnarchivedTasks().getInactiveTasks();
    }

    public TaskContainer getArchivedTasks() {
        return everyTask.getArchivedTasks();
    }

    public Task getTaskByName(String taskName) {
        return everyTask.getTaskByName(taskName);
    }

    public void deleteTask(Task task) {
        everyTask.removeTask(task);
    }

    public TaskContainer filterTasks(TaskContainer allTasks, String filter) {
        if (isSize(filter))
            return allTasks.getTasksBySize(TaskSize.parse(filter)); //TODO coupling?
        else if (!filter.isEmpty()) {
            // TODO: filter by size
            return allTasks.getTasksWithTag(filter);
        } else {
            // TODO: filter by tag
            return allTasks;
        }
    }

    private boolean isSize(String s) { //TODO move this to TaskSize enum?
        return  s.equals("S") ||
                s.equals("M") ||
                s.equals("L") ||
                s.equals("XL");
    }
}
