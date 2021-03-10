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
    private Container titleRow = new Container();
    private Container graphRow;
    private Container descRow = new Container();
    private Container tagRow = new Container();
    private Container timeRow = new Container();
    private Container Footer = new Container();

    private String allTime; // TODO: get allTimeData
    private String weekTime; // TODO: get weekTimeData
    private String dayTime; // TODO: get dayTimeData

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

        // create body
        Container Body = createBody();

        // create header, footer
//        createHeader();
        createFooter();

        // add components
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
            Body.addAll(titleRow, timeRow);

            if (!taskData.getTimeBetween(LocalDateTime.MIN, LocalDateTime.MAX).isZero())
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
        UIComponents.TitleObject timeTitle = new UIComponents.TitleObject("Time Elapsed");
        timeTitle.setSize(SIZE_SMALL);

        // times
        allTime = taskData.getTotalTimeString(); // end time?;
        weekTime = taskData.getTotalTimeThisWeekString(); // end time?;
        dayTime = taskData.getTotalTimeTodayString(); // end time?;

        SpanLabel timeData = new SpanLabel(
        "All Time:\t" + allTime + "\n"+
            "This Week:\t" + weekTime + "\n" +
            "Today:\t" + dayTime
        );

        timeData.getTextAllStyles().setFgColor(UITheme.BLACK);
        timeData.getAllStyles().setMarginUnit(Style.UNIT_TYPE_DIPS);
        timeData.getAllStyles().setMargin(Component.LEFT, UITheme.PAD_3MM);

        timeRow.add(timeTitle);
        timeRow.add(timeData);
    }

    // TODO: IMPLEMENT THIS
    private void createGraphRow() {
        graphRow = new Container(new BorderLayout());
        SpanLabel graphPlaceHolder = new SpanLabel("Insert Graph of Task's\nStart/Stop Log Durations");
        graphPlaceHolder.getTextAllStyles().setBorder(RoundBorder.create().color(UITheme.LIGHT_GREY).rectangle(true));
        graphRow.add(CENTER, graphPlaceHolder);
    }

    // header/footer
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