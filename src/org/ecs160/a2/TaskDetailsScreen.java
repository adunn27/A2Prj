package org.ecs160.a2;

import com.codename1.components.SpanLabel;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.plaf.Style;

import java.time.LocalDateTime;

import static com.codename1.ui.CN.*;

public class TaskDetailsScreen extends Form {
    private Container titleRow = new Container();
    private Container descRow = new Container();
    private Container tagRow = new Container();
    private Container timeRow = new Container();
    private Container Header = new Container();
    private Container Footer = new Container();

    private String allTime; // TODO: get allTimeData
    private String weekTime; // TODO: get weekTimeData
    private String dayTime; // TODO: get dayTimeData

    private Task taskData;
    private UINavigator ui;

    TaskDetailsScreen(Task task, UINavigator ui) {
        taskData =  task;
        this.ui = ui;
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
        setTitle("Details");
        setLayout(new BorderLayout());

        // create body
        Container Body = new Container(BoxLayout.y());
        Body.setScrollableY(true);

        if (taskData != null) {
            // add rows to body
            createTitleRow();
            createTimeRow();
            createTagRow();
            createDescRow();
            Body = new Container();
            Body.addAll(titleRow, timeRow, tagRow, descRow);
        } else {
            Body.add("Task not found...");
        }

        // create header, footer
        createHeader();
        createFooter();

        // add components
        add(BorderLayout.NORTH, Header);
        add(BorderLayout.SOUTH, Footer);
        add(BorderLayout.CENTER, Body);
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
        weekTime = taskData.getTotalTimeString(); // end time?;
        dayTime = taskData.getTotalTimeString(); // end time?;

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
        historyButton.addActionListener(e-> ui.goHistory(taskData.getName()));

        // archive
        UIComponents.ButtonObject archiveButton = new UIComponents.ButtonObject();
        archiveButton.setMyText("Archive");
        archiveButton.setMyIcon(FontImage.MATERIAL_SAVE);
        archiveButton.setMyColor(UITheme.LIGHT_GREY);
        archiveButton.setMyPadding(UITheme.PAD_3MM);
        archiveButton.addActionListener(e-> {
            log("archiving task " + taskData.getName()); // TODO: remove log
            taskData.archive();
        });

        // add to container
        Footer.add(historyButton);
        Footer.add(archiveButton);
    }
}