package org.ecs160.a2;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import static com.codename1.ui.CN.log;

public class HomeScreen extends Form{
    private Container Footer = new Container();
    private Container TaskMenu = new Container();

    private Task activeTask;
    private TaskContainer unarchivedTasks;
    private final UINavigator ui;

    public HomeScreen(UINavigator ui) {
        this.ui = ui;
    }

    @Override
    public void show() {
        createHomeScreen();
        super.show();
    }

    @Override
    public void showBack() {
        createHomeScreen();
        super.showBack();
    }

    private void createHomeScreen() {
        removeAll();
        setTitle("Home");
        setLayout(new BorderLayout());

        this.activeTask = ui.backend.getActiveTask();
        this.unarchivedTasks = ui.backend.getUnarchivedTasks();

        createFooter();
        createTaskMenu();

        add(BorderLayout.SOUTH, Footer);
        add(BorderLayout.CENTER, TaskMenu);
    }


    private void createFooter() {
        Footer = new Container();
        Footer.setLayout(new BorderLayout());

        UIComponents.ButtonObject summary = new UIComponents.ButtonObject();
        summary.setAllStyles("Summary", UITheme.YELLOW,
                FontImage.MATERIAL_LEADERBOARD, UITheme.PAD_3MM);
        summary.addActionListener(e-> ui.goSummary());

        UIComponents.ButtonObject archived = new UIComponents.ButtonObject();
        archived.setAllStyles("", UITheme.YELLOW,
                FontImage.MATERIAL_INBOX, UITheme.PAD_3MM);
        archived.addActionListener(e-> ui.goArchive());

        UIComponents.ButtonObject addTask = new UIComponents.ButtonObject();
        addTask.setAllStyles("", UITheme.YELLOW,
                FontImage.MATERIAL_ADD, UITheme.PAD_3MM);
        addTask.addActionListener(e-> ui.goNew());

        Footer.add(BorderLayout.WEST, archived);
        Footer.add(BorderLayout.EAST, addTask);
        Footer.add(BorderLayout.CENTER, summary);
    }

    private void createTaskMenu() {
        TaskMenu = new Container();
        TaskMenu.setLayout(BoxLayout.y());
        TaskMenu.setScrollableY(true);

        if (activeTask != null) {
            UIComponents.TitleObject activeHeader = new UIComponents.TitleObject("Now Playing");
            activeHeader.setSize(Font.SIZE_MEDIUM);
            TaskMenu.add(activeHeader);

            UIComponents.TaskObject t = new UIComponents.TaskObject(activeTask, ui);
            TaskMenu.add(t);
            getComponentForm().registerAnimated(t);
        }

        if (!unarchivedTasks.isEmpty()) {
            UIComponents.TitleObject inactiveHeader =new UIComponents.TitleObject("My Tasks");
            inactiveHeader.setSize(Font.SIZE_MEDIUM);
            TaskMenu.add(inactiveHeader);
        }

        for (Task taskObj: unarchivedTasks) {
            UIComponents.TaskObject task = new UIComponents.TaskObject(taskObj, ui);
            TaskMenu.add(task);
        }
    }
}
