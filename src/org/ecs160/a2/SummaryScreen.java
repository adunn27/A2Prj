package org.ecs160.a2;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;

// TODO: needs list of tags

//class SummaryHeader extends Container {
//    public SummaryHeader() {
//        setLayout(new BorderLayout());
//        UIComponents.ButtonObject filterButton = new UIComponents.ButtonObject();
//        filterButton.setMyColor(UITheme.YELLOW);
//        filterButton.setMyIcon(FontImage.MATERIAL_FILTER_LIST);
//        filterButton.addActionListener(e->{
//            Dialog fd = new FilterDialogue();
//            fd.show();
//        });
//
//        UIComponents.ButtonObject backButton = new UIComponents.ButtonObject();
//        filterButton.setMyColor(UITheme.YELLOW);
//        filterButton.setMyIcon(FontImage.MATERIAL_ARROW_BACK);
//        filterButton.addActionListener(e->goBack());
//
//        add(BorderLayout.EAST, filterButton);
//        add(BorderLayout.WEST, backButton);
//    }
//}

class FilterDialogue extends Dialog {
    public FilterDialogue() {
        setLayout(BoxLayout.y());
        Container sizeOptions = new Container(BoxLayout.x());
        UIComponents.SizeButtonObject sizeS = new UIComponents.SizeButtonObject("S");
        UIComponents.SizeButtonObject sizeM = new UIComponents.SizeButtonObject("M");
        UIComponents.SizeButtonObject sizeL = new UIComponents.SizeButtonObject("L");
        UIComponents.SizeButtonObject sizeXL = new UIComponents.SizeButtonObject("XL");


        sizeS.addMyListener();
        sizeM.addMyListener();
        sizeL.addMyListener();
        sizeXL.addMyListener();

        sizeOptions.addAll(sizeS, sizeM, sizeL, sizeXL);

        UIComponents.ButtonObject cancel = new UIComponents.ButtonObject();
        cancel.setMyText("Cancel");
        cancel.setMyColor(UITheme.LIGHT_GREY);
        cancel.setMyPadding(UITheme.PAD_3MM);

        cancel.addActionListener(c -> {
            this.dispose();
        });

        add(sizeOptions);
        add(cancel);
    }
}

public class SummaryScreen extends Form {
    Container Header = new Container();
    Container TaskList = new Container(BoxLayout.y());
    Dialog FilterDialog = new Dialog(BoxLayout.y());

    private String name = "Task Name";
    private String size = "S";
    private String duration = "HH:mm:ss"; // TODO: replace with real duration

    private TaskContainer allTaskData;
    private final UINavigator ui;

    public SummaryScreen(UINavigator ui) {
        this.ui = ui;
    }

    @Override
    public void show() {
        createSummaryScreen();
        super.show();
    }

    @Override
    public void showBack() {
        createSummaryScreen();
        super.showBack();
    }

    public void createSummaryScreen() {
        allTaskData = ui.backend.getUnarchivedTasks();

        setTitle("Summary");
        setLayout(new BorderLayout());

        createHeader();
        createTaskList();

        add(BorderLayout.NORTH, Header);
        add(BorderLayout.SOUTH, TaskList);
    }

    private void createHeader() {
        Header.setLayout(new BorderLayout());
        UIComponents.ButtonObject filterButton = new UIComponents.ButtonObject();
        filterButton.setMyColor(UITheme.YELLOW);
        filterButton.setMyIcon(FontImage.MATERIAL_FILTER_LIST);
        filterButton.addActionListener(e->{
            showFilterDialog();
        });

        UIComponents.ButtonObject backButton = new UIComponents.ButtonObject();
        backButton.setMyColor(UITheme.YELLOW);
        backButton.setMyIcon(FontImage.MATERIAL_ARROW_BACK);
        backButton.addActionListener(e-> ui.goBack());

        Header.add(BorderLayout.EAST, filterButton);
        Header.add(BorderLayout.WEST, backButton);
    }

    private void createTaskList() {
        if (allTaskData.isEmpty()) {
            TaskList.add("No Tasks to Display");
        } else {
            for (Task taskObj : allTaskData) {
                //TODO: add all tasks
                TaskList.add(new UIComponents.SummaryTaskObject(taskObj.getName(), taskObj.getTaskSizeString(), duration));
            }
        }
    }

    private void showFilterDialog() {
        Container sizeOptions = new Container(BoxLayout.x());

        UIComponents.ButtonObject cancel = new UIComponents.ButtonObject();
        cancel.setMyText("Cancel");
        cancel.setMyColor(UITheme.LIGHT_GREY);
        cancel.setMyPadding(UITheme.PAD_3MM);

        cancel.addActionListener(c -> {
            FilterDialog.dispose();
        });

        FilterDialog.add(sizeOptions);
        FilterDialog.add(cancel);

        FilterDialog.show();
    }
}