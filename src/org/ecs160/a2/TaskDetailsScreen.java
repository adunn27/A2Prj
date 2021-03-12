package org.ecs160.a2;

import com.codename1.charts.ChartComponent;
import com.codename1.components.SpanLabel;
import com.codename1.ui.*;
import com.codename1.ui.animations.CommonTransitions;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.spinner.Picker;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import static com.codename1.ui.CN.*;
import static org.ecs160.a2.UITheme.*;
import static org.ecs160.a2.UIComponents.*;

public class TaskDetailsScreen extends Form {
    private Container titleRow;
    private Container graphRow;
    private Container descRow;
    private Container tagRow;
    private Container timeRow;
    private Dialog FilterDialog;
    private Picker startDatePicker;
    private Picker endDatePicker;
    private Container Body;
    private Container Footer;
  
    private String allTime;
    private String weekTime;
    private String dayTime;
  
    private Date startDateFilter;
    private Date endDateFilter;

    private Task taskData;
    private UINavigator ui;
  
    private TextObject timeData;
    long lastRenderedTime;
  
    TaskDetailsScreen(Task task, UINavigator ui) {
        registerAnimated(this);

        taskData =  task;
        this.ui = ui;
        lastRenderedTime = taskData.getTimeBetween(LocalDateTime.MIN,
                LocalDateTime.MAX).toMillis();
  
        createToolbar();
        resetStartEndDate();
        createDetailsScreen();
    }
    @Override
    public void show() {
        removeAll();
        createDetailsScreen();
        super.show();
    }

    @Override
    public void showBack() {
        createDetailsScreen();
        super.showBack();
    }

    @Override
    public boolean animate() {
        if (taskData.isActive() && oneSecondLater()) {
            lastRenderedTime = taskData.getTimeBetween(LocalDateTime.MIN,
                    LocalDateTime.MAX).toMillis();
            log("Update time display");
            timeData.setText(getStringTimeStats());
            return true;
        }
        return false;
    }

    private boolean oneSecondLater() {
        return taskData.getTimeBetween(LocalDateTime.MIN, LocalDateTime.MAX)
                .toMillis() / 1000 > lastRenderedTime / 1000;
    }

    private void resetStartEndDate() {
        startDateFilter = Utility.convertToDate(Utility.getStartOfCurrentWeek());
        endDateFilter = new Date();
    }

    private void createToolbar() {
        getToolbar().addMaterialCommandToLeftBar("", ICON_BACK, PAD_6MM,
                e->ui.goBack());

        getToolbar().addMaterialCommandToRightBar("", ICON_EDIT, PAD_6MM,
                e-> ui.goEdit(taskData.getName()));
    }

    private void createDetailsScreen() {
        removeAll();
        setTitle("Details");
        setLayout(new BorderLayout());

        createBody();
        createFooter();

        // add components
        add(BorderLayout.SOUTH, Footer);
        add(BorderLayout.CENTER, Body);
    }

    private void createBody() {

        Body = new Container(BoxLayout.y());
        Body.setScrollableY(true);

        createTitleRow();
        createTimeRow();
        Body.addAll(titleRow, timeRow);

        if (taskData.occurredBetween(LocalDateTime.MIN, LocalDateTime.MAX)) {
            createGraphRow();
            Body.add(graphRow);
        }
        if (!taskData.getTags().isEmpty()) {
            createTagRow();
            Body.add(tagRow);
        }

        if (!taskData.getDescription().isEmpty()) {
            createDescRow();
            Body.add(descRow);
        }
    }

    private void createDescRow() {
        descRow = new Container();
        descRow.setLayout(BoxLayout.y());

        TextObject descTitle = new TextObject(
                "Description", GREY, PAD_3MM, SIZE_SMALL);

        TextObject descData = new TextObject(
                taskData.getDescription(), BLACK, PAD_3MM, SIZE_SMALL);

        descRow.add(descTitle);
        descRow.add(descData);
    }
      
    private void createTitleRow() {
        titleRow = new Container();
        titleRow.setLayout(BoxLayout.x());

        TextObject nameLabel = new TextObject(
                taskData.getName(), BLACK, PAD_3MM, SIZE_LARGE);
        nameLabel.setBold();

        SizeLabelObject sizeLabel = new SizeLabelObject(taskData.getTaskSizeString());

        // add components
        titleRow.add(nameLabel);
        titleRow.add(sizeLabel);

        // taskActive
        if (taskData.isActive()) {
            UIComponents.ButtonObject active = new UIComponents.ButtonObject();
            active.setAllStyles("Active", -1, ICON_ACTIVE, PAD_3MM);
            titleRow.add(active);
        }
    }
      
    private void createTagRow() {
        if (taskData.getTags().size() == 0) {
            return;
        }

        tagRow = new Container();
        tagRow.setLayout(BoxLayout.y());

        TextObject title = new TextObject("Tags", GREY, PAD_3MM, SIZE_SMALL);
        Container objects = new Container();
        for (String tag : taskData.getTags()) {
            ButtonObject tagObj = new ButtonObject();
            tagObj.setAllStyles(tag, COL_TAG,' ', PAD_3MM);
            objects.add(tagObj);
        }

        tagRow.add(title);
        tagRow.add(objects);
    }

    private String getStringTimeStats() {
        // times
        allTime = taskData.getTotalTimeString();
        weekTime = taskData.getTotalTimeThisWeekString();
        dayTime = taskData.getTotalTimeTodayString();

        return "All Time:\t" + allTime + "\n"+
                "This Week:\t" + weekTime + "\n" +
                "Today:\t" + dayTime;
    }

    private void createTimeRow() {
        timeRow = new Container();
        timeRow.setLayout(BoxLayout.y());

        // time title
        TextObject timeTitle = new TextObject(
                "Time Elapsed", GREY, PAD_3MM, SIZE_SMALL);

        timeData = new TextObject("", BLACK, PAD_3MM, SIZE_SMALL);
        timeData.setText(getStringTimeStats());

        timeRow.add(timeTitle);
        timeRow.add(timeData);

        getComponentForm().registerAnimated(timeRow);
    }

    private void createGraphRow() {
        graphRow = new Container(BoxLayout.y());
        UIComponents.ButtonObject dateButton = new UIComponents.ButtonObject();
        dateButton.setAllStyles("View activity chart", COL_SELECTED,
                ICON_CHART, UITheme.PAD_3MM);

        dateButton.addActionListener(e->{
            createChartDialog();
            FilterDialog.show();
        });

        graphRow.add(dateButton);

    }

    private double[] getGraphData() {
        LocalDate startLocalDate = Utility.convertToLocalDate(startDateFilter);
        LocalDate endLocalDate = Utility.convertToLocalDate(endDateFilter);
        java.util.List<Duration> dailyTimes = taskData.getDailyTimesBetween(
                                                        startLocalDate,
                                                        endLocalDate);

        return dailyTimes.stream().mapToDouble(Duration::toMillis).toArray();
    }

    private void createChartDialog() {
        FilterDialog = new Dialog();
        FilterDialog.setLayout(BoxLayout.y());

        startDatePicker = new UIComponents.DatePickerObject(startDateFilter);
        endDatePicker = new UIComponents.DatePickerObject(endDateFilter);
        UIComponents.StartEndPickers startEndPickers = new UIComponents.
                StartEndPickers(startDatePicker, endDatePicker);

        // RESET BUTTON
        UIComponents.ButtonObject resetButton = new UIComponents.ButtonObject();
        resetButton.setAllStyles("Reset", UITheme.LIGHT_GREY, ' ', UITheme.PAD_3MM);
        resetButton.addActionListener(e -> {
            resetStartEndDate();
            refreshChartDialog();
        });

        // REFRESH BUTTON
        UIComponents.ButtonObject refreshButton = new UIComponents.ButtonObject();
        refreshButton.setAllStyles("Update chart",UITheme.COL_SELECTED,ICON_REFRESH,PAD_3MM);
        refreshButton.addActionListener(e -> {
            startDateFilter = startDatePicker.getDate();
            endDateFilter = endDatePicker.getDate();
            if (startDateFilter.compareTo(endDateFilter) > 0)
                new UIComponents.showWarningDialog("Please select a start date on or before the end date");
            else
                refreshChartDialog();
        });

        // DONE BUTTON
        UIComponents.ButtonObject doneButton = new UIComponents.ButtonObject();
        doneButton.setAllStyles("Done", UITheme.LIGHT_GREY, ' ', UITheme.PAD_3MM);
        doneButton.addActionListener(e -> FilterDialog.dispose());

        // ADD TO FILTER
        TaskDetailsGraph graph = new TaskDetailsGraph(getGraphData());
        ChartComponent c = graph.createLineChart();
        FilterDialog.add(c);

        FilterDialog.add(startEndPickers);
        FilterDialog.add(refreshButton);
        FilterDialog.add(GridLayout.encloseIn(2, resetButton, doneButton));
    }

    private void refreshChartDialog() { // TODO: DRY violation (also in summaryscreen)
        FilterDialog.setTransitionOutAnimator(CommonTransitions.createEmpty());
        FilterDialog.dispose();
        createChartDialog();
        FilterDialog.setTransitionInAnimator(CommonTransitions.createEmpty());
        FilterDialog.show();
    }

    private void createFooter() {
        Footer = new Container();
        Footer.setLayout(new GridLayout(1,2));
        Footer.setScrollableY(false);

        // history
        ButtonObject historyButton = new ButtonObject();
        historyButton.setAllStyles("History", COL_UNSELECTED,
                ICON_HISTORY, PAD_3MM);

        // archive
        ButtonObject archiveButton = new ButtonObject();
        String archiveText = (taskData.isArchived()) ? "Unarchive" : "Archive";
        archiveButton.setAllStyles(archiveText, COL_UNSELECTED,
                ICON_ARCHIVE, PAD_3MM);

        // action listeners
        historyButton.addActionListener(e-> {
            if (taskData.isActive())
                new showWarningDialog("This task is currently running");
            else
                ui.goHistory(taskData.getName());
        });

        archiveButton.addActionListener(e-> {
            if (taskData.isArchived()) {
                ui.backend.getTaskByName(taskData.getName()).unarchive();
                ui.backend.logfile.unarchiveTask(taskData);
            } else if (taskData.isActive()) {
                LocalDateTime time = taskData.stop();
                ui.backend.getTaskByName(taskData.getName()).archive();
                ui.backend.logfile.stopTask(taskData, time);
                ui.backend.logfile.archiveTask(taskData);
            } else {
                ui.backend.getTaskByName(taskData.getName()).archive();
                ui.backend.logfile.archiveTask(taskData);
            }
            ui.goBack();
        });

        Footer.add(historyButton);
        Footer.add(archiveButton);
    }
}