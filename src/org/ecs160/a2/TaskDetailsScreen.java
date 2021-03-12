package org.ecs160.a2;

import com.codename1.components.SpanLabel;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.GridLayout;

import java.time.LocalDateTime;

import static com.codename1.ui.CN.*;
import static org.ecs160.a2.UITheme.*;
import static org.ecs160.a2.UIComponents.*;

public class TaskDetailsScreen extends Form {
    private Container titleRow;
    private Container graphRow;
    private Container descRow;
    private Container tagRow;
    private Container timeRow;
    private Container Body;
    private Container Footer;

    private String allTime;
    private String weekTime;
    private String dayTime;

    private Task taskData;
    private UINavigator ui;

    @Override
    public void show() {
        createDetailsScreen();
        super.show();
    }

    @Override
    public void showBack() {
        createDetailsScreen();
        super.showBack();
    }

    TaskDetailsScreen(Task task, UINavigator ui) {
        // TODO: animate task details screen IF task is active
        registerAnimated(this);
        taskData =  task;
        this.ui = ui;
        createToolbar();
        createDetailsScreen();
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

        if (!taskData.getTimeBetween(LocalDateTime.MIN,
                LocalDateTime.MAX).isZero()) {
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

        TextObject name = new TextObject(
                taskData.getName(), BLACK, PAD_3MM, SIZE_LARGE);
        name.setBold();

        SizeLabelObject size = new SizeLabelObject(taskData.getTaskSizeString());

        titleRow.add(name);
        titleRow.add(size);
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
    private void createTimeRow() {
        timeRow = new Container();
        timeRow.setLayout(BoxLayout.y());

        // time title
        TextObject timeTitle = new TextObject(
                "Time Elapsed", GREY, PAD_3MM, SIZE_SMALL);

        // times
        allTime = taskData.getTotalTimeString();
        weekTime = taskData.getTotalTimeThisWeekString();
        dayTime = taskData.getTotalTimeTodayString();

        String timeText = "All Time:\t" + allTime + "\n"+
                          "This Week:\t" + weekTime + "\n" +
                          "Today:\t" + dayTime;
        TextObject timeData = new TextObject(
                timeText, BLACK, PAD_3MM, SIZE_SMALL);

        timeRow.add(timeTitle);
        timeRow.add(timeData);
    }

    // TODO: IMPLEMENT THIS
    private void createGraphRow() {
        graphRow = new Container(new BorderLayout());
        SpanLabel graphPlaceHolder = new SpanLabel("Insert Graph of Task's\nStart/Stop Log Durations");
        graphRow.add(CENTER, graphPlaceHolder);
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
            ui.goHistory(taskData.getName());
        });

        archiveButton.addActionListener(e-> {
            if (taskData.isArchived()) {
                ui.backend.getTaskByName(taskData.getName()).unarchive();
                ui.backend.logfile.unarchiveTask(taskData);
            } else if (taskData.isActive()) {
                ui.backend.stopTask(taskData);
                ui.backend.getTaskByName(taskData.getName()).archive();
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