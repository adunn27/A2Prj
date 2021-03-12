package org.ecs160.a2;

import com.codename1.ui.Display;
import com.codename1.ui.Form;

import java.util.ArrayDeque;
import java.util.Deque;

import static com.codename1.ui.CN.log;

public class UINavigator {
    public BusinessLogic backend;
    private final Deque<Form> previousForm;

    public UINavigator(BusinessLogic backendLogic) {
        backend = backendLogic;

        (new HomeScreen(this)).show();
        previousForm = new ArrayDeque<>();
        previousForm.push(Display.getInstance().getCurrent());
    }

    // navigation

    public void refreshScreen() {
        Display.getInstance().getCurrent().show();
    }

    public void goBack() {
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
        goHome();
    }

    public void goDetails(String taskName) {
        previousForm.push(Display.getInstance().getCurrent());
        (new TaskDetailsScreen(backend.getTaskByName(taskName), this)).show();
    }

    public void goEdit(String taskName) {
        previousForm.push(Display.getInstance().getCurrent());
        (new EditTaskScreen(backend.getTaskByName(taskName), this)).show();
    }

    public void goNew() {
        previousForm.push(Display.getInstance().getCurrent());
        (new EditTaskScreen(null, this)).show(); // new
    }

    public void goArchive() {
        previousForm.push(Display.getInstance().getCurrent());
        (new ArchiveScreen(this)).showBack();
    }

    public void goHistory(String taskName) {
        previousForm.push(Display.getInstance().getCurrent());
        (new TaskHistoryScreen(backend.getTaskByName(taskName), this)).show();
    }

    public void goSummary() {
        previousForm.push(Display.getInstance().getCurrent());
        (new SummaryScreen(this)).show();
    }

    public void goForward() {
        previousForm.pop().show();
    }
}
