package org.ecs160.a2;

import com.codename1.components.SpanLabel;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import static com.codename1.ui.CN.log;
import static org.ecs160.a2.UITheme.*;
import static org.ecs160.a2.UIComponents.*;

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

        ButtonObject summary = new ButtonObject();
        summary.setAllStyles("Summary", COL_SELECTED,
                ICON_SUMMARY, UITheme.PAD_3MM);
        summary.addActionListener(e-> ui.goSummary());

        ButtonObject archived = new ButtonObject();
        archived.setAllStyles("", COL_SELECTED, ICON_ARCHIVE, PAD_3MM);
        archived.addActionListener(e-> {
            if (ui.backend.getArchivedTasks().isEmpty())
                showWarning(archived, "No tasks are archived");
            else
                ui.goArchive();
        });

        UIComponents.ButtonObject addTask = new UIComponents.ButtonObject();
        addTask.setAllStyles("", COL_SELECTED, ICON_NEW, PAD_3MM);
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
            TextObject activeTitle = new TextObject(
                    "Now Playing", GREY, PAD_3MM, Font.SIZE_MEDIUM);
            TaskMenu.add(activeTitle);

            TaskObject t = new TaskObject(activeTask, ui);
            TaskMenu.add(t);
            getComponentForm().registerAnimated(t);
        }

        if (!unarchivedTasks.isEmpty()) {
            TextObject inactiveTitle = new TextObject(
                    "My Tasks", GREY, PAD_3MM, Font.SIZE_MEDIUM);
            TaskMenu.add(inactiveTitle);
        }

        for (Task taskObj: unarchivedTasks) {
            TaskObject task = new TaskObject(taskObj, ui);
            TaskMenu.add(task);
        }
    }

    private void showWarning(Component button, String warning) {
        Dialog d = new Dialog();
        d.setLayout(BoxLayout.y());
        d.addComponent(BorderLayout.center(new SpanLabel(warning)));
        d.showPopupDialog(button);
    }


}
