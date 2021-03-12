package org.ecs160.a2;

import java.time.LocalDateTime;
import java.util.List;

public class BusinessLogic {
    private final TaskContainer everyTask;
    public final LogFile logfile;
    private int nextTaskId;

    public BusinessLogic() {
        logfile = new LogFile();
        everyTask = logfile.getRetrievedTasks();
        nextTaskId = logfile.getLastTaskId() + 1;
    }

    public List<String> getAllTags() {
        return everyTask.getAllTags();
    }

    public void saveTask(Task newTask) {
        everyTask.addTask(newTask);
        newTask.setId(nextTaskId);
        nextTaskId++;
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
        if(task.getName().isEmpty()) return;
        System.out.println(task.getName());
        everyTask.removeTask(task);
        logfile.delete_task(task);
    }

    public void stopTask(Task activeTask) {
        LocalDateTime time = LocalDateTime.now();
        activeTask.stop(time);
        logfile.stopTask(activeTask,time);
    }
}
