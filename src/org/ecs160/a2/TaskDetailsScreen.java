package org.ecs160.a2;

import com.codename1.components.SpanLabel;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.plaf.Style;

import static com.codename1.ui.CN.*;

public class TaskDetailsScreen extends Form {
    Form prevPage;
    Form currentPage;

    private Container titleRow = new Container();
    private Container descRow = new Container();
    private Container tagRow = new Container();
    private Container timeRow = new Container();
    private Container Header = new Container();
    private Container Footer = new Container();

    // TODO: SET NAME, SIZE, DESCRIPTION, ARRAY of TAGS
    private String nameTemp = "[Task Name]";
    private String sizeTemp = "S";
    private String descriptionTemp = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."; // TODO: get description data
    private String[] tagsTemp = {"tag1", "tag2", "tag3","tag4", "tag5", "tag6","tag7", "tag8", "tag9"};

    private String allTimeTemp = "[HH:mm:ss]"; // TODO: get allTimeData
    private String weekTimeTemp = "[HH:mm:ss]"; // TODO: get weekTimeData
    private String dayTimeTemp = "[HH:mm:ss]"; // TODO: get dayTimeData

    TaskDetailsScreen() {
        prevPage = Display.getInstance().getCurrent();

        currentPage = new Form("Details");
        currentPage.setLayout(new BorderLayout());

        // create header, footer
        createHeader();
        createFooter();

        // create body
        Container Body = new Container(BoxLayout.y());
        Body.setScrollableY(true);

        // add rows to body
        createTitleRow(nameTemp, sizeTemp);
        createTimeRow(allTimeTemp, weekTimeTemp, dayTimeTemp);
        createTagRow(tagsTemp);
        createDescRow(descriptionTemp);
        Body.addAll(titleRow, timeRow, tagRow, descRow);

        // add components
        currentPage.add(BorderLayout.NORTH, Header);
        currentPage.add(BorderLayout.SOUTH, Footer);
        currentPage.add(BorderLayout.CENTER, Body);

        currentPage.show();
    }

    // create rows
    private void createDescRow(String description) {
        descRow.setLayout(BoxLayout.y());

        UIComponents.TitleObject descTitle = new UIComponents.TitleObject("Description");
        descTitle.setSize(SIZE_SMALL);

        SpanLabel descData = new SpanLabel();
        descData.setText(description);
        Style descDataStyle = descData.getTextAllStyles();
        descDataStyle.setFgColor(UITheme.BLACK);
        descDataStyle.setMarginUnit(Style.UNIT_TYPE_DIPS);
        descDataStyle.setMargin(Component.LEFT, UITheme.PAD_3MM);

        descRow.add(descTitle);
        descRow.add(descData);
    }
    private void createTitleRow(String name, String size) {
        titleRow.setLayout(BoxLayout.x());
        titleRow.setScrollableY(false);
        titleRow.getAllStyles().setMargin(Component.LEFT, UITheme.PAD_3MM);

        // task name
        Label nameLabel = new Label(name);
        Style nameStyle = nameLabel.getAllStyles();
        nameStyle.setFont((Font.createSystemFont(FACE_SYSTEM, STYLE_BOLD, SIZE_LARGE)));
        nameStyle.setFgColor(UITheme.BLACK);
        nameStyle.setMarginUnit(Style.UNIT_TYPE_DIPS);
        nameStyle.setMargin(Component.LEFT, UITheme.PAD_3MM);

        // task size
        UIComponents.SizeLabelObject sizeLabel = new UIComponents.SizeLabelObject(size);

        // add components
        titleRow.add(nameLabel);
        titleRow.add(sizeLabel);
    }
    private void createTagRow(String[] tags) {
        tagRow.setLayout(BoxLayout.y());

        UIComponents.TitleObject tagTitle = new UIComponents.TitleObject("Tags");
        tagTitle.setSize(SIZE_SMALL);

        // add tags
        Container tagObject = new Container();
        for (int i = 0; i < tags.length; i++) {
            tagObject.add(new UIComponents.TagObject(tags[i]));
        }

        tagRow.add(tagTitle);
        tagRow.add(tagObject);
    }
    private void createTimeRow(String allTime, String weekTime, String dayTime) {
        timeRow.setLayout(BoxLayout.y());

        // time title
        UIComponents.TitleObject timeTitle = new UIComponents.TitleObject("Time Elapsed");
        timeTitle.setSize(SIZE_SMALL);

        // times
        SpanLabel timeData = new SpanLabel(
                "All Time:\t" +
                        allTime + "\n"+
                        "This Week:\t" +
                        weekTime + "\n" +
                        "Today:\t" +
                        dayTime
        );

        timeData.getTextAllStyles().setFgColor(UITheme.BLACK);
        timeData.getAllStyles().setMarginUnit(Style.UNIT_TYPE_DIPS);
        timeData.getAllStyles().setMargin(Component.LEFT, UITheme.PAD_3MM);

        timeRow.add(timeTitle);
        timeRow.add(timeData);
    }

    // header
    private void createHeader() {
        Header.setLayout(new BorderLayout());

        // back button
        UIComponents.ButtonObject backButton = new UIComponents.ButtonObject();
        backButton.setMyColor(UITheme.YELLOW);
        backButton.setMyIcon(FontImage.MATERIAL_ARROW_BACK);
        backButton.setMyPadding(UITheme.PAD_3MM);
        backButton.addActionListener(e-> UINavigator.goBack(prevPage));

        // edit button
        UIComponents.ButtonObject editButton = new UIComponents.ButtonObject();
        editButton.setMyColor(UITheme.YELLOW);
        editButton.setMyIcon(FontImage.MATERIAL_MODE_EDIT);
        editButton.setMyPadding(UITheme.PAD_3MM);
        editButton.addActionListener(e-> UINavigator.goEdit());

        Header.add(BorderLayout.EAST, editButton);
        Header.add(BorderLayout.WEST, backButton);
    }
    private void createFooter() {
        Footer.setLayout(new GridLayout(1,2));
        Footer.setScrollableY(false);

        // history
        UIComponents.ButtonObject historyButton = new UIComponents.ButtonObject();
        historyButton.setMyText("History");
        historyButton.setMyIcon(FontImage.MATERIAL_HISTORY);
        historyButton.setMyColor(UITheme.LIGHT_GREY);
        historyButton.setMyPadding(UITheme.PAD_3MM);
        historyButton.addActionListener(e-> UINavigator.goHistory());

        // archive
        UIComponents.ButtonObject archiveButton = new UIComponents.ButtonObject();
        archiveButton.setMyText("Archive");
        archiveButton.setMyIcon(FontImage.MATERIAL_SAVE);
        archiveButton.setMyColor(UITheme.LIGHT_GREY);
        archiveButton.setMyPadding(UITheme.PAD_3MM);
        archiveButton.addActionListener(e-> UINavigator.goArchive());

        // add to container
        Footer.add(historyButton);
        Footer.add(archiveButton);
    }
}