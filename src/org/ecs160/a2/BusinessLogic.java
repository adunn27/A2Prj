package org.ecs160.a2;

import java.util.List;

public class BusinessLogic {
    private TaskContainer everyTask;

    public BusinessLogic() {
        //TODO load it in somehow
        everyTask = new TaskContainer();
    }

    public void newTask(String name,
                        String size,
                        String description,
                        List<String> tags) {
        Task aNewTask = new Task(name, TaskSize.parse(size));
        aNewTask.setDescription(description);
        for(String aTag: tags) {
            aNewTask.addTag(aTag);
        }
        everyTask.addTask(aNewTask);
    }

    public Task getActiveTask() {
        return everyTask.getActiveTask();
    }

    public TaskContainer getUnarchivedTasks() {
        return everyTask.getUnarchivedTasks().getInactiveTasks();
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
