package org.ecs160.a2;

import com.codename1.components.SpanLabel;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.plaf.RoundBorder;
import com.codename1.ui.plaf.Style;

import static com.codename1.ui.CN.*;


class FirstRow extends Container {
    public FirstRow() {
        setLayout(new BorderLayout());
        setScrollableY(false);

        Stroke borderStroke = new Stroke(5, Stroke.CAP_SQUARE, Stroke.JOIN_MITER, 1);

        // task name
        Label nameLabel = new Label("[Task Name]"); // TODO: NEEDS TASK NAME
        Style nameStyle = nameLabel.getAllStyles();
        nameStyle.setFont((Font.createSystemFont(FACE_SYSTEM, STYLE_BOLD, SIZE_MEDIUM)));
        nameStyle.setFgColor(UITheme.BLACK);

        // task size
        Label sizeLabel = new Label("S"); // TODO: NEEDS TASK SIZE
        Style sizeStyle = sizeLabel.getAllStyles();
        sizeStyle.setPadding(3,3,3,3);
        sizeStyle.setFont((Font.createSystemFont(FACE_SYSTEM, STYLE_BOLD, SIZE_MEDIUM)));
        sizeStyle.setFgColor(UITheme.BLACK);
        sizeStyle.setBorder(
                RoundBorder.create().
                color(UITheme.YELLOW)
        );

        add(BorderLayout.WEST, nameLabel);
        add(BorderLayout.EAST, sizeLabel);

//        getAllStyles().setMargin(25,25,25,25); // TODO: FIX MARGIN
//        getAllStyles().setBorder(
//                RoundBorder.create().
//                rectangle(true).
//                color(UITheme.LIGHT_GREY)
//        );
    }
}

class SecondRow extends Container {
    String descriptionData = "Lorem Ipsum";

    public SecondRow() {
        setLayout(new BorderLayout());
        Label descriptionLabel = new Label("Description");
        Style  descriptionStyle = descriptionLabel.getAllStyles();
        descriptionStyle.setFgColor(UITheme.DARK_GREY);
        descriptionStyle.setFont((Font.createSystemFont(FACE_SYSTEM, STYLE_PLAIN, SIZE_SMALL)));


        SpanLabel descriptionData = new SpanLabel("Lorem Ipsum"); // TODO: needs description data
        Style descriptionDataStyle = descriptionData.getAllStyles();
        descriptionDataStyle.setFgColor(UITheme.BLACK);


        getAllStyles().setBorder(
                RoundBorder.create().rectangle(true).color(UITheme.LIGHT_GREY)
        );

        add(BorderLayout.NORTH, descriptionLabel);
        add(BorderLayout.CENTER, descriptionData);
    }
}


class Header extends Container {
    public Header() {
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

        add(BorderLayout.EAST, editButton);
    }
}
class Footer extends Container {
    public Footer() {
        setLayout(new GridLayout(1,2));
        setScrollableY(false);

        // history
        Button historyButton = new Button("History");
        historyButton.getAllStyles().setFgColor(UITheme.WHITE);
        historyButton.getAllStyles().setMargin(25,25,25,25);
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
        archiveButton.getAllStyles().setMargin(25,25,25,25);
        archiveButton.setIcon(
                FontImage.createMaterial(
                        FontImage.MATERIAL_SAVE,
                        archiveButton.getUnselectedStyle()
                )
        );
        archiveButton.getAllStyles().setBorder(RoundBorder.create().
                rectangle(true).
                color(0x565656));

        // add to container
        add(historyButton);
        add(archiveButton);
    }
}

public class taskDetails extends Form {
    public taskDetails() {
        setLayout(new BorderLayout());
        setTitle("Details");

        // create components
        Container Header = new Header();
        Container Body = new Container();
        Body.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        Body.add(new FirstRow());
        Body.add(new SecondRow());

        Container Footer = new Footer();


        // add components
        add(BorderLayout.NORTH, Header);
        add(BorderLayout.CENTER, Body);
        add(BorderLayout.SOUTH, Footer);
    }
}
