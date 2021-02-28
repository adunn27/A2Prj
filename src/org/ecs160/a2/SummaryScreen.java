package org.ecs160.a2;

import com.codename1.components.SpanLabel;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.plaf.RoundBorder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.TimeZone;

import static com.codename1.ui.CN.log;

public class SummaryScreen extends Form {
    Container Header;
    Container TaskList;
    Container StatsList;
    Container graphRow;
    Dialog FilterDialog;

    private String filterSize;
    private String tempFilterSize = "";

    private String filterTag;
    private String tempFilterTag = "";

    private String filter;

    private TaskContainer allTaskData;
    private final UINavigator ui;

    public SummaryScreen(UINavigator ui) {
        this.filter = "";
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
        allTaskData = getTaskContainer();

        setTitle("Summary");
        setLayout(new BorderLayout());

        createHeader();
        createStatsList();
        createTaskList();
        createGraphRow();

        add(BorderLayout.NORTH, Header);
        add(BorderLayout.CENTER, BoxLayout.encloseY(StatsList,graphRow,TaskList));
    }

    private void createHeader() {
        Header = new Container();
        Header.setLayout(new BorderLayout());
        UIComponents.ButtonObject filterButton = new UIComponents.ButtonObject();
        filterButton.setMyColor(UITheme.YELLOW);
        filterButton.setMyPadding(UITheme.PAD_3MM);
        filterButton.setMyIcon(FontImage.MATERIAL_FILTER_LIST);
        filterButton.setMyText(filter);
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
        StatsList = new Container(BoxLayout.y());
        long totalTime = allTaskData.getTotalTime(LocalDateTime.MIN,
                                                  LocalDateTime.MAX);

        long avgTime = allTaskData.getAverageTime(LocalDateTime.MIN,
                                                  LocalDateTime.MAX);

        long minTime = allTaskData.getMinimumTime(LocalDateTime.MIN,
                                                  LocalDateTime.MAX);

        long maxTime = allTaskData.getMaximumTime(LocalDateTime.MIN,
                                                  LocalDateTime.MAX);

        Container total = new Container(new GridLayout(2));
        total.addAll(new Label("Total Time Elapsed"),
                     new Label(formatDuration(totalTime)));

        Container average = new Container(new GridLayout(2));
        average.addAll(new Label("Average Time Elapsed"),
                       new Label(formatDuration(avgTime)));

        Container minimum = new Container(new GridLayout(2));
        minimum.addAll(new Label("Minimum Time Elapsed"),
                       new Label(formatDuration(minTime)));

        Container maximum = new Container(new GridLayout(2));
        maximum.addAll(new Label("Maximum Time Elapsed"),
                       new Label(formatDuration(maxTime)));

        StatsList.addAll(total,average,minimum,maximum);

    }

    private String formatDuration(long dur) {
        Date date = new Date(dur);
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String dateFormatted = formatter.format(date);
        return dateFormatted;
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
        java.util.List<String> tagData = ui.getAllTags();
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

    // TODO: IMPLEMENT THIS
    private void createGraphRow() {
        graphRow = new Container(BoxLayout.y());
        SpanLabel graphPlaceHolder = new SpanLabel("Insert Chart of Time Spent\non Tasks by filter");
        graphPlaceHolder.getTextAllStyles().setBorder(RoundBorder.create().color(UITheme.LIGHT_GREY).rectangle(true));
        graphRow.add(graphPlaceHolder);
    }

    // TODO: filter
    private void setFilter(String filter) {
        this.filter = filter;
        log("FILTERING BY " + filter);
        show();
    }

    private TaskContainer getTaskContainer() {
        TaskContainer allTasks = ui.getHomeTasks(); //TODO all tasks
        if (isSize(filter))
            return allTasks.getTasksBySize(TaskSize.parse(filter)); //TODO coupling?
        else if (!filter.isEmpty()) {
            // TODO: filter by size
            return allTasks.getTasksWithTag(filter);
        } else {
            // TODO: filter by tag
            return allTasks;
        }
    }

    private boolean isSize(String s) { //TODO move this to TaskSize enum?
        if (s.equals("S") || s.equals("M") || s.equals("L") || s.equals("XL"))
            return true;
        return false;
    }
}