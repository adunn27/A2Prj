package org.ecs160.a2;

import com.codename1.components.SpanLabel;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.plaf.RoundBorder;
import com.codename1.ui.plaf.Style;

import static com.codename1.ui.CN.*;

public class taskDetails extends Form {
    Form prevPage;
    Form currentPage;
    Form editPage;
    Form archivePage;

    private Container titleRow;
    private Container descRow;
    private Container tagRow;
    private Container timeRow;
    private Container header;
    private Container footer;

    // TODO: SET NAME, SIZE, DESCRIPTION, ARRAY of TAGS
    private String nameTemp = "[Task Name]";
    private String sizeTemp = "S";
    private String descriptionTemp = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."; // TODO: get description data
    private String[] tagsTemp = {"tag1", "tag2", "tag3","tag4", "tag5", "tag6","tag7", "tag8", "tag9"};

    private String allTimeTemp = "[HH:mm:ss]"; // TODO: get allTimeData
    private String weekTimeTemp = "[HH:mm:ss]"; // TODO: get weekTimeData
    private String dayTimeTemp = "[HH:mm:ss]"; // TODO: get dayTimeData

    public taskDetails() {
        prevPage = Display.getInstance().getCurrent();

        currentPage.setLayout(new BorderLayout());
        currentPage.setTitle("Details");

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
        currentPage.add(BorderLayout.NORTH, header);
        currentPage.add(BorderLayout.SOUTH, footer);
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
        nameStyle.setFont((Font.createSystemFont(FACE_SYSTEM, STYLE_BOLD, SIZE_MEDIUM)));
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
        tagRow.setLayout(BoxLayout.x());

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
        timeRow.setLayout(BoxLayout.x());

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
        header.setLayout(new BorderLayout());

        // back button
        UIComponents.ButtonObject backButton = new UIComponents.ButtonObject();
        backButton.setMyColor(UITheme.YELLOW);
        backButton.setMyIcon(FontImage.MATERIAL_ARROW_BACK);
        backButton.addActionListener(e->goBack());

        // edit button
        UIComponents.ButtonObject editButton = new UIComponents.ButtonObject();
        editButton.setMyColor(UITheme.YELLOW);
        editButton.setMyIcon(FontImage.MATERIAL_MODE_EDIT);
        editButton.addActionListener(e->goEdit());

        header.add(BorderLayout.EAST, editButton);
        header.add(BorderLayout.WEST, backButton);
    }
    private void createFooter() {
        footer.setLayout(new GridLayout(1,2));
        footer.setScrollableY(false);

        // history
        UIComponents.ButtonObject historyButton = new UIComponents.ButtonObject();
        historyButton.setMyText("History");
        historyButton.setMyIcon(FontImage.MATERIAL_HISTORY);
        historyButton.setMyColor(UITheme.DARK_GREEN);
        historyButton.setMyPadding(UITheme.PAD_3MM);
        historyButton.addActionListener(e->goHistory());

        // archive
        UIComponents.ButtonObject archiveButton = new UIComponents.ButtonObject();
        archiveButton.setMyText("Archive");
        archiveButton.setMyIcon(FontImage.MATERIAL_SAVE);
        archiveButton.setMyColor(UITheme.DARK_GREEN);
        archiveButton.setMyPadding(UITheme.PAD_3MM);
        archiveButton.addActionListener(e->goArchive());

        // add to container
        footer.add(historyButton);
        footer.add(archiveButton);
    }

    // button interaction
    private void goBack() {
        prevPage.showBack();
    }
    private void goEdit() {
        new editTask();
    }
    private void goHistory() {
        new taskHistory();
    }
    private void goArchive() {
        // TODO: archive task
    }
}

class TitleRow extends Container {
    public TitleRow() {
        setLayout(new BoxLayout(BoxLayout.X_AXIS));
        setScrollableY(false);

        // task name
        Label nameLabel = new Label("[Task Name]"); // TODO: NEEDS TASK NAME
        Style nameStyle = nameLabel.getAllStyles();
        nameStyle.setFont((Font.createSystemFont(FACE_SYSTEM, STYLE_BOLD, SIZE_MEDIUM)));
        nameStyle.setFgColor(UITheme.BLACK);
        nameStyle.setMarginUnit(Style.UNIT_TYPE_DIPS);
        nameStyle.setMargin(Component.LEFT, UITheme.PAD_3MM);

        // task size
        Label sizeLabel = new Label("S"); // TODO: NEEDS TASK SIZE
        Style sizeStyle = sizeLabel.getAllStyles();
        sizeStyle.setPadding(UITheme.PAD_3MM,
                             UITheme.PAD_3MM,
                             UITheme.PAD_3MM,
                             UITheme.PAD_3MM);

        sizeStyle.setFont((Font.createSystemFont(FACE_SYSTEM, STYLE_BOLD, SIZE_MEDIUM)));
        sizeStyle.setFgColor(UITheme.BLACK);
        sizeStyle.setBorder(
                RoundBorder.create().
                color(UITheme.YELLOW)
        );

        add(nameLabel);
        add(sizeLabel);

        getAllStyles().setMargin(Component.LEFT, UITheme.PAD_3MM); // TODO: FIX MARGIN
    }
}
class DescRow extends Container {
//    String descriptionData = "Lorem Ipsum"; // TODO: get description data
    String descriptionData = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."; // TODO: get description data
    public DescRow() {
        setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        Label descTitle = new Label("Description");
        Style  descTitleStyle = descTitle.getAllStyles();
        descTitleStyle.setFgColor(UITheme.GREY);

        descTitleStyle.setFont((Font.createSystemFont(FACE_SYSTEM, STYLE_PLAIN, SIZE_SMALL)));
        descTitleStyle.setMarginUnit(Style.UNIT_TYPE_DIPS);
        descTitleStyle.setMargin(Component.LEFT, UITheme.PAD_3MM);

        SpanLabel descData = new SpanLabel();

        descData.setText(descriptionData);

        Style descDataStyle = descData.getTextAllStyles();
        descDataStyle.setFgColor(UITheme.BLACK);
        descDataStyle.setMarginUnit(Style.UNIT_TYPE_DIPS);
        descDataStyle.setMargin(Component.LEFT, UITheme.PAD_3MM);

        add(descTitle);
        add(descData);
    }
}

class TagRow extends Container {
    String[] tags = {"tag1", "tag2", "tag3","tag4", "tag5", "tag6","tag7", "tag8", "tag9"}; // TODO: get array of tags
    public TagRow() {
        setLayout(new BoxLayout(BoxLayout.Y_AXIS));

        Label tagTitle = new Label("Tags");
        Style  tagTitleStyle = tagTitle.getAllStyles();
        tagTitleStyle.setFgColor(UITheme.GREY);

        tagTitleStyle.setFont((Font.createSystemFont(FACE_SYSTEM, STYLE_PLAIN, SIZE_SMALL)));
        tagTitleStyle.setMarginUnit(Style.UNIT_TYPE_DIPS);
        tagTitleStyle.setMargin(Component.LEFT, UITheme.PAD_3MM);

        add(tagTitle);

        Container tagObject = new Container();

        for (int i = 0; i < tags.length; i++) {
            tagObject.add(new UIComponents.TagObject(tags[i]));
        }

        add(tagObject);

    }
}
class TimeRow extends Container {
    String allTimeData = "[HH:mm:ss]"; // TODO: get allTimeData
    String weekTimeData = "[HH:mm:ss]"; // TODO: get weekTimeData
    String dayTimeData = "[HH:mm:ss]"; // TODO: get dayTimeData

    // NOTE: if task active, show all 3. else, only show allTimeData
    public TimeRow() {
        setLayout(new BoxLayout (BoxLayout.Y_AXIS));

        // time title
        Label timeTitle = new Label("Time Elapsed");
        Style  timeTitleStyle = timeTitle.getAllStyles();
        timeTitleStyle.setFgColor(UITheme.GREY);
        timeTitleStyle.setFont((Font.createSystemFont(FACE_SYSTEM, STYLE_PLAIN, SIZE_SMALL)));
        timeTitleStyle.setMarginUnit(Style.UNIT_TYPE_DIPS);
        timeTitleStyle.setMargin(Component.LEFT, UITheme.PAD_3MM);

        //
        SpanLabel allTime = new SpanLabel(
                "All Time:\t" +
                allTimeData + "\n"+
                "This Week:\t" +
                weekTimeData + "\n" +
                "Today:\t" +
                dayTimeData
        );

        allTime.getTextAllStyles().setFgColor(UITheme.BLACK);
        allTime.getAllStyles().setMarginUnit(Style.UNIT_TYPE_DIPS);
        allTime.getAllStyles().setMargin(Component.LEFT, UITheme.PAD_3MM);

        add(timeTitle);
        add(allTime);

    }
}

class DetailsHeader extends Container {
    public DetailsHeader() {
        setLayout(new BorderLayout());

        UIComponents.ButtonObject backButton = new UIComponents.ButtonObject();
        backButton.setMyColor(UITheme.YELLOW);
        backButton.setMyIcon(FontImage.MATERIAL_ARROW_BACK);

        UIComponents.ButtonObject editButton = new UIComponents.ButtonObject();


        NavigationCommand editPage = new NavigationCommand("Go To Edit Page");
        editPage.setNextForm(new editTask());

        editButton.addActionListener(e -> {
            editPage.getNextForm().show();
        });


        editButton.setMyColor(UITheme.YELLOW);
        editButton.setMyIcon(FontImage.MATERIAL_MODE_EDIT);

        add(BorderLayout.EAST, editButton);
        add(BorderLayout.WEST, backButton);
    }
}
class DetailsFooter extends Container {
    public DetailsFooter() {
        setLayout(new GridLayout(1,2));
        setScrollableY(false);

        // history
        Button historyButton = new Button("History");
        historyButton.getAllStyles().setFgColor(UITheme.WHITE);
        historyButton.getAllStyles().setMarginUnit(Style.UNIT_TYPE_DIPS);
        historyButton.getAllStyles().setMargin(UITheme.PAD_3MM,UITheme.PAD_3MM,UITheme.PAD_3MM,UITheme.PAD_3MM);
        historyButton.getAllStyles().setBorder(RoundBorder.create().
                rectangle(true).
                color(UITheme.DARK_GREEN));

        historyButton.setIcon(
                FontImage.createMaterial(
                        FontImage.MATERIAL_HISTORY,
                        historyButton.getUnselectedStyle()
                )
        );

        // archive
        Button archiveButton = new Button("Archive");
        archiveButton.getAllStyles().setFgColor(UITheme.WHITE);
        archiveButton.getAllStyles().setMarginUnit(Style.UNIT_TYPE_DIPS);
        archiveButton.getAllStyles().setMargin(UITheme.PAD_3MM,UITheme.PAD_3MM,UITheme.PAD_3MM,UITheme.PAD_3MM);
        archiveButton.getAllStyles().setBorder(RoundBorder.create().
                rectangle(true).
                color(UITheme.DARK_GREEN));

        archiveButton.setIcon(
                FontImage.createMaterial(
                        FontImage.MATERIAL_SAVE,
                        archiveButton.getUnselectedStyle()
                )
        );


        // add to container
        add(historyButton);
        add(archiveButton);
    }
}


