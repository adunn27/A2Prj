package org.ecs160.a2;

import com.codename1.ui.*;
import com.codename1.ui.animations.CommonTransitions;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.plaf.RoundBorder;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.spinner.Picker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import static com.codename1.ui.CN.*;
import static org.ecs160.a2.UITheme.*;
import static org.ecs160.a2.UIComponents.*;


public class SummaryScreen extends Form {
    Container Header;
    Container FilterHeader;
    Container TaskList;
    Container StatsList;
    Container graphRow;
    Container timePicker;
    Picker startDate;
    Picker endDate;
    Dialog FilterDialog;

    // FILTERS
    private java.util.List<String> sizeFilters;
    private java.util.List<String> tagFilters;
    private Date startDateFilter;
    private Date endDateFilter;

    // DATA
    private java.util.List<String> sizeData;
    private java.util.List<String> tagData;
    private TaskContainer allTaskData;
    private final UINavigator ui;

    public SummaryScreen(UINavigator ui) {
        this.ui = ui;
        initializeDataAndFilters();
        createToolbar();
        createSummaryScreen();
    }

    private void initializeDataAndFilters() {
        sizeData = new ArrayList<>(); // TODO: fix sizeData initialization
        sizeData.add("S");
        sizeData.add("M");
        sizeData.add("L");
        sizeData.add("XL");
        tagData = ui.backend.getAllTags();

        resetFilters();
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
        removeAll(); // reset
        setTitle("Summary");
        setLayout(BoxLayout.y());

        getTaskContainer();
        createFilterDisplay();
        createStatsList();
        createTaskList();
        createGraphRow();

        if (allTaskData.isEmpty()) {
            addAll(FilterHeader,TaskList);
        } else {
            addAll(FilterHeader,StatsList,graphRow,TaskList);
        }
    }

    private void createFilterDisplay() {
        String dateHeader = dateToString(startDateFilter) + " - " +
                            dateToString(endDateFilter);

        FilterHeader = new Container(BoxLayout.y());
        TextObject startEndDates = new TextObject(
                dateHeader, BLACK, 0, SIZE_LARGE);

        FilterHeader.add(FlowLayout.encloseCenterMiddle(startEndDates));

        Container filters = new Container();
        filters.add(new Label("Showing: "));
        for (String size : sizeFilters) {
            SizeButtonObject b = new SizeButtonObject(size);
            filters.add(b);
        }
        for (String tag : tagFilters) {
            ButtonObject b = new ButtonObject();
            b.setAllStyles(tag, COL_TAG,' ', PAD_1MM);
            filters.add(b);
        }

        if (!sizeFilters.isEmpty() || !tagFilters.isEmpty()) {
            FilterHeader.add(FlowLayout.encloseCenterMiddle(filters));
        }
    }
    private void getTaskContainer() {
        allTaskData = ui.backend.getUnarchivedTasks();

        for (String size : sizeFilters) {
            allTaskData = allTaskData.getTasksBySize(TaskSize.parse(size));
        }

        for (String tag : tagFilters) {
            allTaskData = allTaskData.getTasksWithTag(tag);
        }

        allTaskData = allTaskData.getTasksThatOccurred(
                Utility.convertToLocalDate(startDateFilter),
                Utility.convertToLocalDate(endDateFilter));
    }
    private void createToolbar() {
        getToolbar().addMaterialCommandToLeftBar("",
                ICON_BACK, PAD_6MM, e->ui.goBack());

        getToolbar().addMaterialCommandToRightBar("",
                FontImage.MATERIAL_FILTER_LIST, PAD_6MM,
                e->{
                    createFilterDialog();
                    FilterDialog.show();
                });
    }

    private void createStatsList() {
        StatsList = new Container(BoxLayout.y());
        TextObject statTitle = new TextObject("Stats", GREY, PAD_3MM, SIZE_LARGE);
        StatsList.add(statTitle);
        LocalDateTime startTime = Utility.getStartOfDay(
                Utility.convertToLocalDate(startDateFilter));
        LocalDateTime endTime = Utility.getEndOfDay(
                Utility.convertToLocalDate(endDateFilter));
        long totalTime = allTaskData.getTotalTime(startTime, endTime);

        long avgTime = allTaskData.getAverageTime(startTime, endTime);

        long minTime = allTaskData.getMinimumTime(startTime, endTime);

        long maxTime = allTaskData.getMaximumTime(startTime, endTime);

        Container total = FlowLayout.encloseCenterMiddle();
        total.addAll(new Label("Total"),
                     new Label(formatDuration(totalTime)));

        Container average = FlowLayout.encloseCenterMiddle();
        average.addAll(new Label("Average"),
                       new Label(formatDuration(avgTime)));

        Container minimum = FlowLayout.encloseCenterMiddle();
        minimum.addAll(new Label("Minimum"),
                       new Label(formatDuration(minTime)));

        Container maximum = FlowLayout.encloseCenterMiddle();
        maximum.addAll(new Label("Maximum"),
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
        TextObject taskTitle = new TextObject("Tasks", GREY, PAD_3MM,SIZE_LARGE);

        LocalDateTime startTime = Utility.getStartOfDay( //TODO Dry violation
                Utility.convertToLocalDate(startDateFilter));
        LocalDateTime endTime = Utility.getEndOfDay(
                Utility.convertToLocalDate(endDateFilter));
        if (allTaskData.isEmpty()) {
            Container noTasks = FlowLayout.encloseCenterMiddle();
            noTasks.add("No Tasks to Display");
            TaskList.add(noTasks);
        } else {
            TaskList.add(taskTitle);
            for (Task taskObj : allTaskData) {
                TaskList.add(new SummaryTaskObject(taskObj, startTime, endTime, ui));
            }
        }
    }
    private void refreshFilterDialog() { // TODO: fix refresh speed
        FilterDialog.setTransitionOutAnimator(CommonTransitions.createEmpty());
        FilterDialog.dispose();
        createFilterDialog();
        FilterDialog.setTransitionInAnimator(CommonTransitions.createEmpty());
        FilterDialog.show();
    }
    private void createFilterDialog() {
        FilterDialog = new Dialog(BoxLayout.y());
        FilterDialog.setTitle("Filter");

        // TODO: refactor
        Container sizeButtons = new Container(new GridLayout(4));
        for (String size : sizeData) {
            SizeLabelObject button = new SizeLabelObject(size);
            if (sizeFilters.contains(size))
                button.setSelectedColor();
            button.addPointerPressedListener(e ->
                updateSizeFilter(size, sizeFilters.contains(size)));
            sizeButtons.add(button);
        }

        Container tagButtons = FlowLayout.encloseCenterMiddle();
        for (String tag : tagData) {
            ButtonObject tagButton = new ButtonObject();
            tagButton.setAllStyles(tag, COL_TAG, ' ', PAD_3MM);
            if (tagFilters.contains(tag))
                tagButton.setSelectedColor();

            tagButton.addActionListener(e->{
                updateTagsFilter(tag, tagFilters.contains(tag));
            });

            tagButtons.add(tagButton);
        }

        ButtonObject reset = new ButtonObject();
        reset.setAllStyles("Reset",COL_UNSELECTED,' ',PAD_3MM);
        reset.addActionListener(e -> {
            resetFilters();
            refreshFilterDialog();
        });

        ButtonObject done = new ButtonObject();
        done.setAllStyles("Done",COL_UNSELECTED,' ',PAD_3MM);
        done.addActionListener(e -> {
            startDateFilter = startDate.getDate();
            endDateFilter = endDate.getDate();
            show();
        });

        TimePicker();
        FilterDialog.add(timePicker);
        FilterDialog.add(sizeButtons);

        Container tagTitle = FlowLayout.encloseCenterMiddle();
        tagTitle.add("Tags");
        FilterDialog.add(tagTitle);
        FilterDialog.add(tagButtons);

        Container foot = new Container(new GridLayout(2));
        foot.addAll(reset, done);

        FilterDialog.add(foot);
    }
    private void resetFilters() {
        sizeFilters = new ArrayList<>();
        tagFilters = new ArrayList<>();
        startDateFilter = Utility.convertToDate(Utility.getStartOfCurrentWeek());
        endDateFilter = new Date();
    }
    private String dateToString(Date date) {
        String pattern = "MM/dd/yy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }

    // TODO: refactor
    private void updateSizeFilter(String size, boolean wasFilter) {
        if (wasFilter) {
            sizeFilters.remove(size);
        } else {
            sizeFilters = new ArrayList<>();
            sizeFilters.add(size);
        }
        refreshFilterDialog();
    }
    private void updateTagsFilter(String name, boolean wasFilter)  {
        if (wasFilter) {
            tagFilters.remove(name);

        } else {
            tagFilters.add(name);
        }
        refreshFilterDialog();
    }

    private void createGraphRow() {
        graphRow = new Container(BoxLayout.y());

//        SummaryGraph summaryGraph = new SummaryGraph(ui);
//        ChartComponent c = summaryGraph.createPieChart();
//        graphRow.add(c);

        graphRow.add(new Label("graph goes here"));
    }

    private Picker createPicker(Date date) {
        Picker datePicker = new Picker();
        datePicker.setType(Display.PICKER_TYPE_CALENDAR);
        datePicker.getStyle().setBorder(
                RoundBorder.create().rectangle(true).color(COL_UNSELECTED));
        datePicker.getStyle().setPaddingUnit(Style.UNIT_TYPE_DIPS);
        datePicker.getStyle().setPadding(PAD_3MM,PAD_3MM, PAD_3MM,PAD_3MM);
        datePicker.setDate(date);
        return datePicker;
    }
    private void TimePicker() {
        timePicker = FlowLayout.encloseCenterMiddle();

        startDate = createPicker(startDateFilter);
        endDate = createPicker(endDateFilter);

        timePicker.addAll(new Label("Start"), startDate,
                          endDate, new Label("End"));
    }
}