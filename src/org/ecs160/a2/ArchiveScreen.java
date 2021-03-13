package org.ecs160.a2;

import com.codename1.ui.Container;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.BorderLayout;

import com.codename1.ui.layouts.BoxLayout;

public class ArchiveScreen extends Form {
    Container TaskList;

    private TaskContainer tasks;
    private final UINavigator ui;

    public ArchiveScreen(UINavigator ui) {
        createToolbar();
        this.ui = ui;
    }

    @Override
    public void show() {
        createArchiveScreen();
        super.show();
    }

    @Override
    public void showBack() {
        createArchiveScreen();
        super.showBack();
    }

    public void createArchiveScreen() {
        tasks = ui.backend.getArchivedTasks();
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
            UIComponents.TaskObject taskObject =
                    new UIComponents.TaskObject(taskObj, ui);
            TaskList.add(taskObject);
        }
    }

    private void createToolbar() {
        getToolbar().addMaterialCommandToLeftBar("",
                UITheme.ICON_BACK, UITheme.PAD_6MM,
                e->ui.goBack());
    }
}
