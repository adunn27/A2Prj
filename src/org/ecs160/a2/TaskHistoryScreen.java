package org.ecs160.a2;

import static com.codename1.ui.CN.*;

import com.codename1.ui.Form;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;

import java.util.ArrayList;


public class TaskHistoryScreen extends Form {
    Form currentPage;
    Form prevPage;

    Container Header = new Container();
    Container Footer = new Container();
    Container TaskList = new Container();

    private String name;
    private String size;
    private java.util.List<String> tags = new ArrayList<>();
    private java.util.List<String> times = new ArrayList<>();

    Task taskData;

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



    public TaskHistoryScreen(Task task){
        prevPage = Display.getInstance().getCurrent();

        taskData = task;
        initData(taskData);

        currentPage = new Form("Task History");
        currentPage.setLayout(new BorderLayout());

        createHeader();
        createTaskList();

        currentPage.add(NORTH, Header);
        currentPage.add(CENTER, TaskList);

        currentPage.show();
    }

    private void createTaskList(){
        TaskList.setLayout(BoxLayout.y());
        TaskList.setScrollableY(true);

        for (int i = 0 ; i < 20; i++){
            UIComponents.HistoryTaskObject newHTO = new UIComponents.HistoryTaskObject(times.get(0), times.get(1));
            TaskList.add(newHTO);
        }
    }

    private void createHeader() {
        Header.setLayout(new BorderLayout());
        UIComponents.ButtonObject backButton = new UIComponents.ButtonObject();
        backButton.setMyIcon(FontImage.MATERIAL_ARROW_BACK);
        backButton.setMyColor(UITheme.YELLOW);
        backButton.setMyPadding(UITheme.PAD_3MM);

        backButton.addActionListener(e-> UINavigator.goBack(prevPage));
        Header.add(BorderLayout.WEST, backButton);
    }

}
