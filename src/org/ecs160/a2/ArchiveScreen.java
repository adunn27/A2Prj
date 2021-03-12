package org.ecs160.a2;

import static com.codename1.ui.CN.*;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.plaf.*;

import com.codename1.ui.layouts.BoxLayout;

public class ArchiveScreen extends Form {
    Container TaskList;
    Container Footer;

    private TaskContainer tasks;
    private final UINavigator ui;

    public ArchiveScreen(UINavigator ui) {
        createToolbar();
        this.ui = ui;
    }

    @Override
    public void show() {
        createArchiveScreen();
        super.showBack();
    }

    @Override
    public void showBack() {
        createArchiveScreen();
        super.show();
    }

    public void createArchiveScreen() {
        tasks = ui.backend.getArchivedTasks();

        // EXIT if no archived tasks
        if (tasks.isEmpty()) {
            ui.goForward();
        }

        setLayout(new BorderLayout());
        setTitle("Archive");
        createTaskList();
        add(CENTER, TaskList);
    }

    private void createTaskList(){
        TaskList = new Container();
        TaskList.setLayout(BoxLayout.y());
        TaskList.setScrollableY(true);

        for (Task taskObj : tasks) {
            UIComponents.TaskObject taskObject = new UIComponents.TaskObject(taskObj, ui);
            TaskList.add(taskObject);
        }
    }

    private void createToolbar() {
        getToolbar().addMaterialCommandToRightBar("",
                UITheme.ICON_FORWARD, UITheme.PAD_6MM,
                e->ui.goForward());
    }
}
