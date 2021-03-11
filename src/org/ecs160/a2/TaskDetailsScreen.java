package org.ecs160.a2;

import com.codename1.charts.ChartComponent;
import com.codename1.charts.models.XYMultipleSeriesDataset;
import com.codename1.charts.models.XYSeries;
import com.codename1.charts.renderers.XYMultipleSeriesRenderer;
import com.codename1.charts.views.LineChart;
import com.codename1.components.SpanLabel;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.plaf.RoundBorder;
import com.codename1.ui.plaf.Style;

import java.time.Duration;
import java.time.LocalDateTime;

import static com.codename1.ui.CN.*;

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

    TaskDetailsScreen(Task task, UINavigator ui) {
        taskData =  task;
        this.ui = ui;
        createToolbar();
        createDetailsScreen();
    }

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

    private void createDetailsScreen() {
        removeAll();
        setTitle("Details");
        setLayout(new BorderLayout());

        Container Body = createBody();
        createFooter();

        // add components
        add(BorderLayout.SOUTH, Footer);
        add(BorderLayout.CENTER, Body);
    }

    private Container createBody() {
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

    // create rows
    private void createDescRow() {
        descRow = new Container();
        descRow.setLayout(BoxLayout.y());

        UIComponents.TextObject descTitle = new UIComponents.TextObject(
                "Description", UITheme.GREY, UITheme.PAD_3MM, SIZE_SMALL
        );

        UIComponents.TextObject descData = new UIComponents.TextObject(
                taskData.getDescription(),
                UITheme.BLACK, UITheme.PAD_3MM, SIZE_SMALL
        );

        descRow.add(descTitle);
        descRow.add(descData);
    }
    private void createTitleRow() {
        titleRow = new Container();
        titleRow.setLayout(BoxLayout.x());

        // task name
        UIComponents.TextObject nameLabel = new UIComponents.TextObject(
                taskData.getName(), UITheme.BLACK, UITheme.PAD_3MM, SIZE_LARGE
        );
        nameLabel.setBold();

        // task size
        UIComponents.SizeLabelObject sizeLabel = new
                UIComponents.SizeLabelObject(taskData.getTaskSizeString());

        // add components
        titleRow.add(nameLabel);
        titleRow.add(sizeLabel);
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
    private void createTimeRow() {
        timeRow = new Container();
        timeRow.setLayout(BoxLayout.y());

        // time title
        UIComponents.TextObject timeTitle = new UIComponents.TextObject(
                "Time Elapsed", UITheme.GREY, UITheme.PAD_3MM, SIZE_SMALL
        );

        // times
        allTime = taskData.getTotalTimeString();
        weekTime = taskData.getTotalTimeThisWeekString();
        dayTime = taskData.getTotalTimeTodayString();

        String timeText = "All Time:\t" + allTime + "\n"+
                "This Week:\t" + weekTime + "\n" +
                "Today:\t" + dayTime;
        UIComponents.TextObject timeData = new UIComponents.TextObject(
                timeText, UITheme.BLACK, UITheme.PAD_3MM, SIZE_SMALL
        );

        timeRow.add(timeTitle);
        timeRow.add(timeData);
    }

    // TODO: IMPLEMENT THIS
    private void createGraphRow() {
        graphRow = new Container(new BorderLayout());
        SpanLabel graphPlaceHolder = new SpanLabel("Insert Graph of Task's\nStart/Stop Log Durations");
        graphRow.add(CENTER, graphPlaceHolder);
    }

    private void createToolbar() {
        getToolbar().addMaterialCommandToLeftBar("",
                FontImage.MATERIAL_ARROW_BACK, UITheme.PAD_6MM, e->ui.goBack());

        getToolbar().addMaterialCommandToRightBar("",
                FontImage.MATERIAL_MODE_EDIT, UITheme.PAD_6MM,
                e-> ui.goEdit(taskData.getName())
        );
    }

    private void createFooter() {
        Footer = new Container();
        Footer.setLayout(new GridLayout(1,2));
        Footer.setScrollableY(false);

        // history
        UIComponents.ButtonObject historyButton = new UIComponents.ButtonObject();
        historyButton.setAllStyles("History", UITheme.LIGHT_GREY,
                FontImage.MATERIAL_HISTORY, UITheme.PAD_3MM);

        // archive
        UIComponents.ButtonObject archiveButton = new UIComponents.ButtonObject();
        String archiveText = (taskData.isArchived()) ? "Unarchive" : "Archive";
        archiveButton.setAllStyles(archiveText, UITheme.LIGHT_GREY,
                FontImage.MATERIAL_SAVE, UITheme.PAD_3MM);

        // action listeners
        historyButton.addActionListener(e-> {
            if (taskData.isActive())
                new UIComponents.showWarningDialog("This task is currently running");
            else
                ui.goHistory(taskData.getName());
        });

        archiveButton.addActionListener(e-> {
            if (taskData.isArchived())
                ui.backend.getTaskByName(taskData.getName()).unarchive();
            else
                ui.backend.getTaskByName(taskData.getName()).archive();
            ui.goBack();
        });

        // add to container
        Footer.add(historyButton);
        Footer.add(archiveButton);
    }


}