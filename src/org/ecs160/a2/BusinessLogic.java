package org.ecs160.a2;

import java.util.ArrayList;
import java.util.List;

public class BusinessLogic {
    private TaskContainer everyTask;

    public BusinessLogic() {
        //TODO load it in somehow
        everyTask = new TaskContainer();
        everyTask.addTask(new Task("temp"));
    }

    public List<String> getAllTags() {
        java.util.List<String> allTags = new ArrayList<>();
        for (Task task : everyTask) { // TODO: archive vs unarchived?
            for (String tagName : task.getTags()) {
                if (!allTags.contains(tagName)) {
                    allTags.add(tagName);
                }

            }
        }
        return allTags;
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

    /*
    newTask(name, size, description, tags)

    getTaskName
    setTaskName

    getTaskSize
    setTaskSize(size)

    getTags(taskName)
    addTag(tagName)
    removeTag(tagName)

    getTaskDescription
    updateTaskDescription(desc)
    setTaskDescription(desc)

    getTaskHistory
    updateTaskHistory(?)

    getTotalTime
    getTodayTime
    getWeekTime

    getTasks
    getArchiveTasks
    getTasksWithSize(size)
    getTasksWithTag(tagName)

    deleteTask
     */
}
