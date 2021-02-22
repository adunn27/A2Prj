package org.ecs160.a2;

import com.codename1.ui.Form;

import static com.codename1.ui.CN.log;

public class UINavigator {
    private BusinessLogic backend;

    public UINavigator(BusinessLogic backendLogic) {
        backend = backendLogic;

        Task activeTask = backend.getActiveTask();
        TaskContainer unarchivedTasks = backend.getUnarchivedTasks();

        new HomeScreen(activeTask, unarchivedTasks);
    }

    // navigation
    public static void goBack(Form prevPage) {
        log("go back");
        prevPage.showBack();
    }

    public static void goBackAndSave(Form prevPage) {
        log("go back and save");
        prevPage.showBack();
    }

    public static void goDelete(Form prevPage) {
        log("go back and delete");
        new HomeScreen();
    }

    public static void goStart(String taskName) {
        log("start " + taskName);
    }

    public static void goDetails() {
        log("go details");
        new TaskDetailsScreen();
    }

    public static void goEdit() {
        log("go edit");
        new EditTaskScreen();
    }

    public static void goNew() {
        log("go new");
        new EditTaskScreen(); // new
    }

    public static void goArchive() {
        log("go archive");
        new ArchiveScreen();
    }

    public static void goHistory() {
        log("go history");
        new TaskHistoryScreen();
    }

    public static void goSummary() {
        log("go summary");
        new SummaryScreen();
    }
}
