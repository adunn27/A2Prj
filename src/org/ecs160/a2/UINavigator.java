package org.ecs160.a2;

import com.codename1.ui.Display;
import com.codename1.ui.Form;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import static com.codename1.ui.CN.log;

public class UINavigator {
    private BusinessLogic backend;
    private Deque<Form> previousForm;

    public UINavigator(BusinessLogic backendLogic) {
        backend = backendLogic;

        (new HomeScreen(this)).show();
        previousForm = new ArrayDeque<>();
        previousForm.push(Display.getInstance().getCurrent());
    }

    // navigation
    public void refreshScreen() {
        log("go back");
        Display.getInstance().getCurrent().show();
    }

    public void goBack() {
        log("go back");
        previousForm.pop().showBack();
    }

    public void goHome() {
        for (int i = 1; i < previousForm.size(); i++) {
            previousForm.pop();
        }
        previousForm.pop().showBack();
    }

    public void goDelete(Task task) { //TODO
        backend.deleteTask(task);
        goHome();
    }

    public void goDetails(String taskName) {
        previousForm.push(Display.getInstance().getCurrent());
        log("go details");
        (new TaskDetailsScreen(backend.getTaskByName(taskName), this)).show();
    }

    public void goEdit(String taskName) {
        previousForm.push(Display.getInstance().getCurrent());
        log("go edit");
        (new EditTaskScreen(backend.getTaskByName(taskName), this)).show();
    }

    public void goNew() {
        previousForm.push(Display.getInstance().getCurrent());
        log("go new");
        // TODO: implement create edit
        (new EditTaskScreen(null, this)).show(); // new
    }

    public void goArchive() {
        previousForm.push(Display.getInstance().getCurrent());
        log("go archive");
        (new ArchiveScreen(this)).show();
    }

    public void goHistory(String taskName) {
        previousForm.push(Display.getInstance().getCurrent());
        log("go history");
        //TODO
        (new TaskHistoryScreen(backend.getTaskByName(taskName), this)).show();
    }

    public void goSummary() {
        previousForm.push(Display.getInstance().getCurrent());
        log("go summary");
        (new SummaryScreen(this)).show();
    }

    public void saveNewTask(Task newTask) {
        backend.saveTask(newTask);
    }

    public Task getTaskByName(String name) {
        return backend.getTaskByName(name);
    }

    public Task getActiveTask() {
        return backend.getActiveTask();
    }

    public TaskContainer getHomeTasks() {
        return backend.getUnarchivedTasks();
    }

    public List<String> getAllTags() {
        return backend.getAllTags(); //TODO expand to include colors?
    }

    public TaskContainer getArchiveTasks() {
        return backend.getArchivedTasks();
    }

    public TaskContainer filterTasks(TaskContainer allTasks, String filter) {
        return backend.filterTasks(allTasks, filter);
    }
}
