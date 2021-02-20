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
    public taskDetails() {
        setLayout(new BorderLayout());

        setTitle("Details");

        // create components
        Container Header = new HeaderSeth();
        Container Body = new Container();
        Body.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        Body.setScrollableY(true);
        Body.add(new TitleRow())
                .add(new TimeRow())
                .add(new TagRow())
                .add(new DescRow());

        Container Footer = new FooterSeth();


        // add components
        add(BorderLayout.NORTH, Header);
        add(BorderLayout.CENTER, Body);
        add(BorderLayout.SOUTH, Footer);


    }
}

class TitleRow extends Container {
    public TitleRow() {
        setLayout(new BoxLayout(BoxLayout.X_AXIS));
        setScrollableY(false);

        Stroke borderStroke = new Stroke(5, Stroke.CAP_SQUARE, Stroke.JOIN_MITER, 1);

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

        Container tagContainer = new Container();

        for (int i = 0; i < tags.length; i++) {
            tagContainer.add(new UIComponents.TagObject(tags[i]));
        }

        add(tagContainer);

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

class HeaderSeth extends Container {
    public HeaderSeth() {
        setLayout(new BorderLayout());
        Button editButton = new Button();
        Style editStyle = editButton.getAllStyles();
        editStyle.setFgColor(UITheme.BLACK);

        // set edit icon
        editButton.setIcon(
                FontImage.createMaterial(
                        FontImage.MATERIAL_MODE_EDIT,
                        editButton.getUnselectedStyle()
                )
        );

        // set edit button background and shape
        editStyle.setBorder(
                RoundBorder.create().
                        rectangle(true).
                        color(UITheme.LIGHT_YELLOW)
        );

        Button backButton = new Button();
        Style backStyle = backButton.getAllStyles();
        backStyle.setFgColor(UITheme.BLACK);

        // set edit icon
        backButton.setIcon(
                FontImage.createMaterial(
                        FontImage.MATERIAL_ARROW_BACK,
                        backButton.getUnselectedStyle()
                )
        );

        // set edit button background and shape
        backStyle.setBorder(
                RoundBorder.create().
                        rectangle(true).
                        color(UITheme.LIGHT_YELLOW)
        );

        add(BorderLayout.WEST, backButton);
        add(BorderLayout.EAST, editButton);
    }
}
class FooterSeth extends Container {
    public FooterSeth() {
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