package org.ecs160.a2;

import java.util.ArrayList;
import java.util.List;

public class BusinessLogic {
    private TaskContainer everyTask;
    public LogFile logfile;
    public TaskContainer logTask;

    public BusinessLogic() {
        //TODO load it in somehow
        everyTask = new TaskContainer();
        logfile = new LogFile();
        logTask = logfile.retrieveTask;

        for(Task task : logTask){

            newTask(task.getName(),task.getTaskSizeString(),task.getDescription(),task.getTags());
           // System.out.println("here is task time"+ task.getAllTimes().size());

        }



        //TODO tests


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
       // logfile.writeTask(aNewTask);
        return aNewTask;
    }

    public void saveTask(Task newTask) {
        everyTask.addTask(newTask);
       // logfile.editTask(newTask);
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
