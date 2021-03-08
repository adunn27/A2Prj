package org.ecs160.a2;

import com.codename1.charts.ChartComponent;
import com.codename1.components.SpanLabel;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.layouts.mig.Grid;
import com.codename1.ui.plaf.RoundBorder;
import com.codename1.ui.spinner.Picker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.function.Predicate;

import static com.codename1.ui.CN.log;

public class SummaryScreen extends Form {
    Container Header;
    Container TaskList;
    Container StatsList;
    Container graphRow;
    Container timePicker;
    Dialog FilterDialog;

    // FILTERS
    private String sizeFilter;
    private java.util.List<String> tagFilters;
    private java.util.List<Date> timeFilter;

    // TODO: REMOVE?
    private String filterSize;
    private String tempFilterSize = "";

    // TODO: REVIEW
    private java.util.List<String> tagData;
    private String filter;

    private TaskContainer allTaskData;
    private final UINavigator ui;

    public SummaryScreen(UINavigator ui) {
        this.ui = ui;
        initializeFilters();
        createSummaryScreen();
    }

    private void initializeFilters() {
        sizeFilter = "";
        // time filter?
        tagData = ui.backend.getAllTags();
        tagFilters = new ArrayList<>();
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
            createFilterDialog();
            FilterDialog.show();
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

    private void refreshFilterDialog() { // TODO: fix refresh speed
        FilterDialog.dispose();
        createFilterDialog();
        FilterDialog.show();
    }

    private void createFilterDialog() {
        String[] sizeList = {"S","M","L","XL"};
        FilterDialog = new Dialog(BoxLayout.y());
        FilterDialog.setScrollableY(true);

        if (!tagFilters.isEmpty()) {
            Container activeFilters = new Container();
            activeFilters.add("Active Filters");
            for (String tag : tagFilters) {
                UIComponents.ButtonObject tagButton = new UIComponents.ButtonObject();
                tagButton.setMyText(tag);
                tagButton.setMyColor(UITheme.LIGHT_YELLOW);
                tagButton.setMyPadding(UITheme.PAD_1MM);
                tagButton.addActionListener(e->{
                    actionListenerTags(tag, false);
                });
                activeFilters.add(tagButton);
            }
            FilterDialog.add(activeFilters);
        }

        // SIZE
        Container sizeButtons = new Container(new GridLayout(4));
        for (String size : sizeList) {
            UIComponents.SizeLabelObject button = new UIComponents.SizeLabelObject(size);
            button.addPointerPressedListener(e -> {
//                setFilter(size);
//                FilterDialog.dispose();
            });
            sizeButtons.add(button);
            button.getSelectedStyle().setBgColor(UITheme.YELLOW); // SELECTED COLOR
        }

        // TAGS
        Container tagButtons = new Container();
        for (String tag : tagData) {
            UIComponents.ButtonObject tagButton = new UIComponents.ButtonObject();
            tagButton.setMyText(tag);
            tagButton.setMyColor(UITheme.LIGHT_GREEN);
            tagButton.setMyPadding(UITheme.PAD_1MM);
            tagButton.addActionListener(e->{
                actionListenerTags(tag, true);
//                setFilter(tag);
//                FilterDialog.dispose();
            });
            tagButtons.add(tagButton);
        }

        UIComponents.ButtonObject reset = new UIComponents.ButtonObject();
        reset.setMyText("Reset");
        reset.setMyColor(UITheme.YELLOW);
        reset.setMyPadding(UITheme.PAD_3MM);
        reset.addActionListener(c -> {
            setFilter(""); // RESET FILTER
        });

        UIComponents.ButtonObject done = new UIComponents.ButtonObject();
        done.setMyText("Done");
        done.setMyColor(UITheme.LIGHT_GREY);
        done.setMyPadding(UITheme.PAD_3MM);
        done.addActionListener(c -> {
            setFilter(""); // TODO: SET ALL SELECTED FILTERS
        });

        UIComponents.ButtonObject cancel = new UIComponents.ButtonObject();
        cancel.setMyText("Cancel");
        cancel.setMyColor(UITheme.LIGHT_GREY);
        cancel.setMyPadding(UITheme.PAD_3MM);
        cancel.addActionListener(c -> {
            FilterDialog.dispose();
        });

        TimePicker();
        FilterDialog.add(timePicker);
        FilterDialog.add(sizeButtons);
        FilterDialog.add(tagButtons);

        Container foot = new Container(new GridLayout(2));
        foot.addAll(cancel, done);

        FilterDialog.add(reset);
        FilterDialog.add(foot);
    }

    private void actionListenerTags(String name, boolean isFilter)  {
        if (isFilter) {
            tagData.remove(name);
            tagFilters.add(name);
        } else {
            tagData.add(name);
            tagFilters.remove(name);
        }

        System.out.println(tagFilters);
        System.out.println(tagData);

        refreshFilterDialog();
    }


    // TODO: IMPLEMENT THIS
    private void createGraphRow() {
        graphRow = new Container(BoxLayout.y());
//        SummaryGraph summaryGraph = new SummaryGraph(ui);
//        ChartComponent c = summaryGraph.createPieChart();
//        graphRow.add(c);

        graphRow.add(new Label("graph goes here"));
    }

    // TODO: filter!!!
    private void setFilters(String sizeFilter,
                           java.util.ArrayList<String> tagFilters,
                           java.util.ArrayList<Date> timeFilter) {
        this.sizeFilter = sizeFilter;
        this.tagFilters = tagFilters;
        this.timeFilter = timeFilter;

        log("FILTERING BY " + sizeFilter + tagFilters + timeFilter);
        show();
    }

    private TaskContainer getTaskContainer() {
        TaskContainer allTasks = ui.backend.getUnarchivedTasks();
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


    private void TimePicker() {
        timePicker = new Container(new GridLayout(2));

        Picker startDate = new Picker();
        Picker endDate = new Picker();

        startDate.setType(Display.PICKER_TYPE_DATE);
        startDate.getStyle().setBorder(
                RoundBorder.create().rectangle(true).color(UITheme.LIGHT_GREY));
        startDate.setDate(new Date());

        endDate.setType(Display.PICKER_TYPE_DATE);
        endDate.getStyle().setBorder(
                RoundBorder.create().rectangle(true).color(UITheme.LIGHT_GREY));
        endDate.setDate(new Date());

        timePicker.addAll(startDate, endDate);
    }
}