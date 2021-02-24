package org.ecs160.a2;

import com.codename1.ui.Display;
import com.codename1.ui.Form;

import java.util.ArrayDeque;
import java.util.Deque;

import static com.codename1.ui.CN.log;

public class UINavigator {
    public BusinessLogic backend;
    public Deque<Form> previousForm;
//    private BusinessLogic backend;

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

    public void goDelete(Task task) {
        backend.deleteTask(task);
//        goBack();
        goHome();
    }

    public void goStart(String taskName) {
        log("start " + taskName);
        //TODO
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
}
