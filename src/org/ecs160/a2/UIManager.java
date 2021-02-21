package org.ecs160.a2;

import com.codename1.ui.Form;

import static com.codename1.ui.CN.log;

public class UIManager {
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
        new homeScreen();
    }

    public static void goStart(String taskName) {
        log("start " + taskName);
    }

    public static void goDetails() {
        log("go details");
        new taskDetails();
    }

    public static void goEdit() {
        log("go edit");
        new editTask();
    }

    public static void goNew() {
        log("go new");
        new editTask(); // new
    }

    public static void goArchive() {
        log("go archive");
        new archiveUI();
    }

    public static void goHistory() {
        log("go history");
        new taskHistory();
    }

    public static void goSummary() {
        log("go summary");
        new summaryScreen();
    }
}
