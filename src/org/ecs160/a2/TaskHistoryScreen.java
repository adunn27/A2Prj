package org.ecs160.a2;

import static com.codename1.ui.CN.*;

import com.codename1.ui.Form;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;

import java.util.ArrayList;


public class TaskHistoryScreen extends Form {
    private Container Header;
    private Container Footer;
    private Container TaskList;

    private String name;
    private String size;
    private java.util.List<String> tags = new ArrayList<>();
    private java.util.List<String> times = new ArrayList<>();

    private Task taskData;
    private UINavigator ui;

    private void initData(Task taskData) {
        if (taskData == null) {
            name = "[Task Name]";
            size = "S";
            tags.add("tag1");
            times.add("1:00");
            times.add("2:00");
        } else {
            name = taskData.getName();
            size = taskData.getTaskSizeString();
            tags = taskData.getTags();
//            times = task.getTotalTime(); // TODO: needs total time
            times.add("1:00");
            times.add("2:00");
        }
    }

    public TaskHistoryScreen(Task task, UINavigator ui){
        taskData = task;
        this.ui = ui;
        createTaskHistoryScreen();
    }

    @Override
    public void show() {
        createTaskHistoryScreen();
        super.show();
    }

    @Override
    public void showBack() {
        createTaskHistoryScreen();
        super.showBack();
    }

    private void createTaskHistoryScreen() {
        initData(taskData);

        setTitle("Task History");
        setLayout(new BorderLayout());

        createHeader();
        createTaskList();

        add(NORTH, Header);
        add(CENTER, TaskList);
    }

    private void createTaskList(){
        TaskList = new Container();
        TaskList.setLayout(BoxLayout.y());
        TaskList.setScrollableY(true);

        for (int i = 0 ; i < 20; i++){
            UIComponents.HistoryTaskObject newHTO = new UIComponents.HistoryTaskObject(times.get(0), times.get(1));
            TaskList.add(newHTO);
        }
    }

    private void createHeader() {
        Header = new Container();
        Header.setLayout(new BorderLayout());
        UIComponents.ButtonObject backButton = new UIComponents.ButtonObject();
        backButton.setMyIcon(FontImage.MATERIAL_ARROW_BACK);
        backButton.setMyColor(UITheme.YELLOW);
        backButton.setMyPadding(UITheme.PAD_3MM);

        backButton.addActionListener(e-> ui.goBack());
        Header.add(BorderLayout.WEST, backButton);
    }

}
