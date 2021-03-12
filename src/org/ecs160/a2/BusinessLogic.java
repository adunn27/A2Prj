package org.ecs160.a2;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class BusinessLogic {
    private TaskContainer everyTask;
    private LogFile logfile;
    private int nextTaskId;

    public BusinessLogic() {
        try {
            logfile = new LogFile();
            everyTask = logfile.getRetrievedTasks();
            nextTaskId = logfile.getLastTaskId() + 1;
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public List<String> getAllTags() {
        return everyTask.getAllTags();
    }

    public void saveNewTask(Task newTask) {
        newTask.setId(nextTaskId);
        everyTask.addTask(newTask);
        logfile.addTask(newTask);
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

    public void startTask(Task task) {
        if (getActiveTask() != null)
            stopTask(getActiveTask());
        LocalDateTime time = LocalDateTime.now();
        task.start(time);
        logfile.startTask(task, time);
    }

    public void stopTask(Task activeTask) {
        LocalDateTime time = LocalDateTime.now();
        activeTask.stop(time);
        logfile.stopTask(activeTask, time);
    }

    public void removeTag(Task task, String tag) {
        if (!task.hasTag(tag)) return;
        task.removeTag(tag);
        logfile.delete_tag(task, tag);
    }

    public void archiveTask(Task task) {
        if (task.isActive())
            stopTask(task);
        getTaskByName(task.getName()).archive();
        logfile.archiveTask(task);
    }

    public void unarchiveTask(Task task) {
        getTaskByName(task.getName()).unarchive();
        logfile.unarchiveTask(task);
    }

    public void removeTimeSpan(Task task, TimeSpan deletedTimeSpan) {
        logfile.delete_time(task,
                task.getIndexOfTimeSpan(deletedTimeSpan));
        task.removeTimeSpan(deletedTimeSpan);
    }

    public void editTimeSpan(Task task, TimeSpan timeSpan,
                              LocalDateTime startDateTime,
                              LocalDateTime endDateTime) {
        logfile.edit_time(task,
                task.getIndexOfTimeSpan(timeSpan),
                startDateTime, endDateTime);

        timeSpan.setStartTime(startDateTime);
        timeSpan.setEndTime(endDateTime);
    }

    public void editTask(Task task, String nameData, String sizeData,
                         String descriptionData, List<String> tagsData) {
        task.setName(nameData);
        task.setTaskSizeWithString(sizeData);
        task.setDescription(descriptionData);
        task.addAllTags(tagsData);
        logfile.editTask(task);
    }
}
