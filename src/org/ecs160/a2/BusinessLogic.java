package org.ecs160.a2;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BusinessLogic {
    private TaskContainer everyTask;
    public LogFile logfile;
    public TaskContainer logTask;
    public int TaskId;

    public BusinessLogic() {
        logfile = new LogFile();

        everyTask = logfile.retrieveTask;

        TaskId= logfile.TaskId + 1;
    }

    public List<String> getAllTags() {
        return everyTask.getAllTags();
    }

    public Task newTask(String name,
                        String size,
                        String description,
                        Boolean isArchive,
                        Boolean isActive,
                        int taskid,
                        List<TimeSpan> alltimes,
                        List<String> tags) {
        assert (everyTask.getTaskByName(name) == null): "Task already exists!";

        Task aNewTask = new Task(name, TaskSize.parse(size));
        aNewTask.setAllTimeSpans(alltimes);
        aNewTask.setDescription(description);
        if (isArchive){

            aNewTask.archive();

        }
        if(isActive){
            aNewTask.setActive();
        }
        for(String aTag: tags) {
            aNewTask.addTag(aTag);
        }
        aNewTask.setId(taskid);
        everyTask.addTask(aNewTask);
        return aNewTask;
    }

    public void saveTask(Task newTask) {
        everyTask.addTask(newTask);
        newTask.setId(TaskId);
        TaskId++;
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
