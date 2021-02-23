package org.ecs160.a2;

import com.codename1.ui.Form;

import static com.codename1.ui.CN.log;

public class UINavigator {
    public static BusinessLogic backend;
//    private BusinessLogic backend;

    public UINavigator(BusinessLogic backendLogic) {
        backend = backendLogic;

        Task activeTask = backend.getActiveTask();
        TaskContainer unarchivedTasks = backend.getUnarchivedTasks();
        TaskContainer archivedTasks = backend.getUnarchivedTasks();

        new HomeScreen(activeTask, unarchivedTasks);
//        new TaskDetailsScreen(activeTask);
//        new EditTaskScreen(activeTask);
//        new TaskHistoryScreen(activeTask);
//        new ArchiveScreen(archivedTasks);
//        new SummaryScreen(unarchivedTasks);
    }

    // navigation

    public static void goBack(Form prevPage) {
        log("go back");
        prevPage.showBack();
    }

    public static void goBackAndSave(Form prevPage) {
        log("go back and save");
        Task activeTask = backend.getActiveTask();
        TaskContainer unarchivedTasks = backend.getUnarchivedTasks();
        new HomeScreen(activeTask, unarchivedTasks); //TODO
        //prevPage.showBack();
    }

    public static void goDelete(Form prevPage) {
        log("go back and delete");
//        new HomeScreen(activeTask, unarchivedTasks);
    }

    public static void goStart(String taskName) {
        log("start " + taskName);
    }

    public static void goDetails() {
        log("go details");
//        new TaskDetailsScreen();
    }

    public static void goEdit() {
        log("go edit");
//        new EditTaskScreen();
    }

    public static void goNew() {
        log("go new");
        // TODO: implement create edit
        new EditTaskScreen(backend.getActiveTask()); // new
    }

    public static void goArchive() {
        log("go archive");
//        new ArchiveScreen();
    }

    public static void goHistory() {
        log("go history");
//        new TaskHistoryScreen();
    }

    public static void goSummary() {
        log("go summary");
//        new SummaryScreen();
    }
}
