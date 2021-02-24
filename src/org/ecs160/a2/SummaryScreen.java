package org.ecs160.a2;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.GridLayout;

import java.time.LocalDateTime;

import static com.codename1.ui.CN.log;

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
    Container StatsList = new Container(BoxLayout.y());
    Dialog FilterDialog = new Dialog(BoxLayout.y());

    private String filterSize;
    private String tempFilterSize = "";

    private String filterTag;
    private String tempFilterTag = "";

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
        createStatsList();
        createTaskList();

        add(BorderLayout.NORTH, Header);
        add(BorderLayout.CENTER, StatsList);
        add(BorderLayout.SOUTH, TaskList);
    }

    private void createHeader() {
        Header = new Container();
        Header.setLayout(new BorderLayout());
        UIComponents.ButtonObject filterButton = new UIComponents.ButtonObject();
        filterButton.setMyColor(UITheme.YELLOW);
        filterButton.setMyPadding(UITheme.PAD_3MM);
        filterButton.setMyIcon(FontImage.MATERIAL_FILTER_LIST);
        filterButton.addActionListener(e->{
            showFilterDialog();
        });

        UIComponents.ButtonObject backButton = new UIComponents.ButtonObject();
        backButton.setMyColor(UITheme.YELLOW);
        backButton.setMyIcon(FontImage.MATERIAL_ARROW_BACK);
        backButton.setMyPadding(UITheme.PAD_3MM);
        backButton.addActionListener(e-> ui.goBack());

        Header.add(BorderLayout.EAST, filterButton);
        Header.add(BorderLayout.WEST, backButton);
    }

    private void createStatsList() {
        long totalTime = allTaskData.getTotalTime(LocalDateTime.MIN,
                                                  LocalDateTime.MAX);

        long avgTime = allTaskData.getAverageTime(LocalDateTime.MIN,
                                                  LocalDateTime.MAX);

        long minTime = allTaskData.getMinimumTime(LocalDateTime.MIN,
                                                  LocalDateTime.MAX);

        long maxTime = allTaskData.getMinimumTime(LocalDateTime.MIN,
                                                  LocalDateTime.MAX);

        Container total = new Container(new GridLayout(2));
        total.addAll(new Label("Total Time Elapsed"),
                     new Label(String.valueOf(totalTime)));

        Container average = new Container(new GridLayout(2));
        average.addAll(new Label("Average Time Elapsed"),
                       new Label(String.valueOf(avgTime)));

        Container minimum = new Container(new GridLayout(2));
        minimum.addAll(new Label("Minimum Time Elapsed"),
                       new Label(String.valueOf(minTime)));

        Container maximum = new Container(new GridLayout(2));
        maximum.addAll(new Label("Maximum Time Elapsed"),
                       new Label(String.valueOf(maxTime)));

        StatsList.addAll(total,average,minimum,maximum);

    }

    private void createTaskList() {
        TaskList = new Container(BoxLayout.y());
        TaskList.setScrollableY(true);

        if (allTaskData.isEmpty()) {
            TaskList.add("No Tasks to Display");
        } else {
            for (Task taskObj : allTaskData) {
                TaskList.add(new UIComponents.SummaryTaskObject(taskObj, ui));
            }
        }
    }

    private void showFilterDialog() {
        String[] sizeList = {"S","M","L","XL"};
        FilterDialog = new Dialog(BoxLayout.y());
        FilterDialog.setScrollableY(true);

        // SIZE BUTTONS
        Container sizeButtons = new Container(new GridLayout(5));
        for (String size : sizeList) {
            UIComponents.ButtonObject button = new UIComponents.ButtonObject();
            button.setMyText(size);
            button.setMyColor(UITheme.LIGHT_YELLOW);
            button.addActionListener(e->{
                tempFilterSize=size;
                setFilter(size);
                FilterDialog.dispose();
            });
            sizeButtons.add(button);
        }

        // TAGS
        Container tagButtons = new Container();
        java.util.List<String> tagData = ui.backend.getAllTags();
        for (String tag : tagData) {
            UIComponents.ButtonObject tagButton = new UIComponents.ButtonObject();
            tagButton.setMyText(tag);
            tagButton.setMyColor(UITheme.LIGHT_GREEN);
            tagButton.addActionListener(e->{
                tempFilterTag = tag;
                setFilter(tag);
                FilterDialog.dispose();
            });
            tagButtons.add(tagButton);
        }

        UIComponents.ButtonObject none = new UIComponents.ButtonObject();
        none.setMyText("None");
        none.setMyColor(UITheme.YELLOW);
        none.setMyPadding(UITheme.PAD_3MM);
        none.addActionListener(c -> {
            setFilter(""); // RESET FILTER
            FilterDialog.dispose();
        });

        UIComponents.ButtonObject cancel = new UIComponents.ButtonObject();
        cancel.setMyText("Cancel");
        cancel.setMyColor(UITheme.LIGHT_GREY);
        cancel.setMyPadding(UITheme.PAD_3MM);

        cancel.addActionListener(c -> {
            FilterDialog.dispose();
        });

        FilterDialog.add(sizeButtons);
        FilterDialog.add(tagButtons);
        FilterDialog.add(none);
        FilterDialog.add(cancel);
        FilterDialog.show();
    }

    // TODO: filter
    private void setFilter(String filter) {
        if (filter.isEmpty())
            return;
        else if (isSize(filter)) {
            // TODO: filter by size
        } else {
            // TODO: filter by tag
        }
        log("FILTERING BY " + filter);
    }

    private boolean isSize(String s) {
        if (s.equals("S") || s.equals("M") || s.equals("L") || s.equals("XL"))
            return true;
        return false;
    }

    private void setFilterSize(String size) {
        tempFilterSize = size;
    }

}