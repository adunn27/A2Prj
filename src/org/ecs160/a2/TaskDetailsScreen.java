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

public class TaskDetailsScreen extends Form {
    private Container titleRow;
    private Container graphRow;
    private Container descRow;
    private Container tagRow;
    private Container timeRow;
    private Container Header;
    private Container Footer;
    private Dialog FilterDialog;
    private Picker startDatePicker;
    private Picker endDatePicker;

    private String allTime;
    private String weekTime;
    private String dayTime;

    private Date startDateFilter;
    private Date endDateFilter;

    private Task taskData;
    private UINavigator ui;

    long lastRenderedTime;
    private SpanLabel timeData;
    boolean graphIsOpen;

    TaskDetailsScreen(Task task, UINavigator ui) {
        registerAnimated(this);

        taskData =  task;
        this.ui = ui;
        lastRenderedTime = taskData.getTimeBetween(LocalDateTime.MIN,
                LocalDateTime.MAX).toMillis();
        graphIsOpen = false;

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
            if (graphIsOpen) {
                log("graph is open");
                refreshChartDialog();
            } else
                log("graph is not open");
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

    private void createDetailsScreen() {
        setTitle("Details");
        setLayout(new BorderLayout());

        // create body
        Container Body = createBody();

        // create header, footer
        createHeader();
        createFooter();

        // add components
        add(BorderLayout.NORTH, Header);
        add(BorderLayout.SOUTH, Footer);
        add(BorderLayout.CENTER, Body);
    }

    private Container createBody() {
        Container Body = new Container(BoxLayout.y());
        Body.setScrollableY(true);

        if (taskData != null) {
            // add rows to body
            createTitleRow();
            createGraphRow();
            createTimeRow();
            createTagRow();
            createDescRow();
            Body.add(titleRow);
            Body.add(timeRow);

            if (taskData.occurredBetween(LocalDateTime.MIN, LocalDateTime.MAX))
                Body.add(graphRow);

            if (!taskData.getTags().isEmpty())
                Body.add(tagRow);
            if (!taskData.getDescription().isEmpty())
                Body.add(descRow);
        } else {
            Body.add("Task not found...");
        }
        return Body;
    }

    // create rows
    private void createDescRow() {
        descRow = new Container();
        descRow.setLayout(BoxLayout.y());

        UIComponents.TitleObject descTitle = new UIComponents.TitleObject("Description");
        descTitle.setSize(SIZE_SMALL);

        SpanLabel descData = new SpanLabel();
        descData.setText(taskData.getDescription());
        Style descDataStyle = descData.getTextAllStyles();
        descDataStyle.setFgColor(UITheme.BLACK);
        descDataStyle.setMarginUnit(Style.UNIT_TYPE_DIPS);
        descDataStyle.setMargin(Component.LEFT, UITheme.PAD_3MM);

        descRow.add(descTitle);
        descRow.add(descData);
    }
    private void createTitleRow() {
        titleRow = new Container();
        titleRow.setLayout(BoxLayout.x());
        titleRow.setScrollableY(false);
        titleRow.getAllStyles().setMargin(Component.LEFT, UITheme.PAD_3MM);

        // task name
        Label nameLabel = new Label(taskData.getName());
        Style nameStyle = nameLabel.getAllStyles();
        nameStyle.setFont((Font.createSystemFont(FACE_SYSTEM, STYLE_BOLD, SIZE_LARGE)));
        nameStyle.setFgColor(UITheme.BLACK);
        nameStyle.setMarginUnit(Style.UNIT_TYPE_DIPS);
        nameStyle.setMargin(Component.LEFT, UITheme.PAD_3MM);

        // task size
        UIComponents.SizeLabelObject sizeLabel = new UIComponents.SizeLabelObject(taskData.getTaskSizeString());

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

        UIComponents.TitleObject tagTitle = new UIComponents.TitleObject("Tags");
        tagTitle.setSize(SIZE_SMALL);

        // add tags
        Container tagObject = new Container();
        for (String tag : taskData.getTags()) {
            tagObject.add(new UIComponents.TagObject(tag));
        }

        tagRow.add(tagTitle);
        tagRow.add(tagObject);
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
        UIComponents.TitleObject timeTitle = new UIComponents.TitleObject("Time Elapsed");
        timeTitle.setSize(SIZE_SMALL);

        timeData = new SpanLabel();
        timeData.setText(getStringTimeStats());
        timeData.getTextAllStyles().setFgColor(UITheme.BLACK);
        timeData.getAllStyles().setMarginUnit(Style.UNIT_TYPE_DIPS);
        timeData.getAllStyles().setMargin(Component.LEFT, UITheme.PAD_3MM);

        timeRow.add(timeTitle);
        timeRow.add(timeData);

        getComponentForm().registerAnimated(timeRow);
//        timeRow.animate();
    }

    // TODO: IMPLEMENT THIS
    private void createGraphRow() {
        graphRow = new Container(BoxLayout.y());
        UIComponents.ButtonObject dateButton = new UIComponents.ButtonObject();
        dateButton.setAllStyles("View activity chart", COL_SELECTED,
                ICON_CHART, UITheme.PAD_3MM);

        dateButton.addActionListener(e->{
            log("open graph");
            graphIsOpen = true;
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
                new UIComponents.showWarningDialog("Please select a start date before or on end date");
            else
                refreshChartDialog();

        });

        // DONE BUTTON
        UIComponents.ButtonObject doneButton = new UIComponents.ButtonObject();
        doneButton.setAllStyles("Done", UITheme.LIGHT_GREY, ' ', UITheme.PAD_3MM);
        doneButton.addActionListener(e -> {
            log("closing graph");
            graphIsOpen = false;
            FilterDialog.dispose();
        });

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

    // header/footer
    private void createHeader() {
        Header = new Container();
        Header.setLayout(new BorderLayout());

        // back button
        UIComponents.ButtonObject backButton = new UIComponents.ButtonObject();
        backButton.setMyColor(UITheme.YELLOW);
        backButton.setMyIcon(FontImage.MATERIAL_ARROW_BACK);
        backButton.setMyPadding(UITheme.PAD_3MM);
        backButton.addActionListener(e-> ui.goBack());

        // edit button
        UIComponents.ButtonObject editButton = new UIComponents.ButtonObject();
        editButton.setMyColor(UITheme.YELLOW);
        editButton.setMyIcon(FontImage.MATERIAL_MODE_EDIT);
        editButton.setMyPadding(UITheme.PAD_3MM);
        editButton.addActionListener(e-> ui.goEdit(taskData.getName()));

        Header.add(BorderLayout.EAST, editButton);
        Header.add(BorderLayout.WEST, backButton);
    }
    private void createFooter() {
        Footer = new Container();
        Footer.setLayout(new GridLayout(1,2));
        Footer.setScrollableY(false);

        // history
        UIComponents.ButtonObject historyButton = new UIComponents.ButtonObject();
        historyButton.setMyText("History");
        historyButton.setMyIcon(FontImage.MATERIAL_HISTORY);
        historyButton.setMyColor(UITheme.LIGHT_GREY);
        historyButton.setMyPadding(UITheme.PAD_3MM);
        historyButton.addActionListener(e-> {
            if (taskData.isActive()){
                System.out.println("This task is currently running");
                Dialog errorMessage = new Dialog();
                errorMessage.setLayout(BoxLayout.y());
                errorMessage.add("This task is currently running");

                Button closeDialog = new Button("Close");
                closeDialog.addActionListener(event -> {
                    errorMessage.dispose();
                });

                errorMessage.add(closeDialog);

                errorMessage.show();
            } else {
                ui.goHistory(taskData.getName());
            }
        });

        // archive
        UIComponents.ButtonObject archiveButton = new UIComponents.ButtonObject();
        String archiveText = (taskData.isArchived()) ? "Unarchive" : "Archive";
        archiveButton.setMyText(archiveText);
        archiveButton.setMyIcon(FontImage.MATERIAL_SAVE);
        archiveButton.setMyColor(UITheme.LIGHT_GREY);
        archiveButton.setMyPadding(UITheme.PAD_3MM);
        archiveButton.addActionListener(e-> {
            if (taskData.isArchived()) {
                ui.backend.getTaskByName(taskData.getName()).unarchive();
            } else {
                ui.backend.getTaskByName(taskData.getName()).archive();
            }
            ui.goBack();
        });

        // add to container
        Footer.add(historyButton);
        Footer.add(archiveButton);
    }


}