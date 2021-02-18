package org.ecs160.a2;

import com.codename1.components.MultiButton;
import com.codename1.components.SpanLabel;
import com.codename1.ui.*;
import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Label;
import com.codename1.ui.TextComponent;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.plaf.Border;
import com.codename1.ui.plaf.RoundBorder;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.validation.LengthConstraint;
import com.codename1.ui.validation.Validator;

import java.awt.*;


// TODO: in tagElement(), add event listener for delete button

class tagElement extends Container {
    int color_lightGrey = 0xc4c4c4;
    int color_black = 0x000000;
    int color_red = 0xbe0000;

    public tagElement(String name) {
        this.setLayout(new BorderLayout());

        // name label
        Label tagName = new Label(name);
        tagName.getAllStyles().setFgColor(color_black);

        // delete button
        Button tagDelete = new Button("x"); // needs event listener
        tagDelete.getAllStyles().setFgColor(color_red);

        add(BorderLayout.CENTER, tagName);
        add(BorderLayout.EAST, tagDelete);
        getAllStyles().setBorder(Border.createDashedBorder(6, color_lightGrey));
        getAllStyles().setMargin(25,25,25,25);
    }

}

public class editTaskUI extends Form {
    String[] sizeOptions = {"S", "M", "L", "XL"};
    String[] deleteOptions = {"Yes, delete permanently", "Cancel"};

    String[] sampleTags = {"Sample 1", "Sample 2", "Sample 3"};

    int color_lightGrey = 0xc4c4c4;
    int color_black = 0x000000;
    int color_red = 0xbe0000;

    int pixels = mmToPixels(1.5);

    public editTaskUI() {
//        setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        setTitle("Edit Task");

        // init containers
        Container taskNameSizeContainer = new Container(new BorderLayout());
        Container tagsContainer = new Container(new FlowLayout());
        tagsContainer.getAllStyles().setBorder(Border.createLineBorder(4));

        // --- tags
        // add existing tags
        for (int i = 0; i < sampleTags.length; i++) {
            Container tag = new tagElement(sampleTags[i]);
            tagsContainer.add(tag);
        }

        // add (new tag) button with action listener
        tagsContainer.setScrollableY(true);
        Button addTagButton = new Button("+");
        tagsContainer.add(addTagButton);

        addTagButton.addActionListener(e -> {
            Container tag = new tagElement("New Tag");
            tag.getAllStyles().setFgColor(0x000000);
            tagsContainer.addComponent(0, tag);
            tag.setWidth(getWidth());
            tag.setY(getHeight());
            tagsContainer.animateLayout(125);
        });

        // init elements
        TextComponent nameField = new TextComponent().label("Name");
        MultiButton sizeButton = new MultiButton("Size");
        Button deleteButton = new Button("Delete");
        TextComponent descriptionField = new TextComponent().label("Description").multiline(true);

        // --- header: name, size
        // name style
        nameField.text("My Task");

        Style nameStyle = nameField.getAllStyles();
        Validator val = new Validator();
        val.addConstraint(nameField, new LengthConstraint(1));

        // size style
        Style sizeStyle = sizeButton.getAllStyles();
        sizeStyle.setPaddingUnit(Style.UNIT_TYPE_PIXELS);
        sizeStyle.setPadding(pixels, pixels, pixels, pixels);

        // source: https://www.codenameone.com/blog/tip-dont-use-combobox.html
        sizeButton.addActionListener(e -> {
            Dialog d = new Dialog();
            d.setLayout(BoxLayout.y());
            d.getContentPane().setScrollableY(false);
            for(int iter = 0; iter < sizeOptions.length ; iter++) {
                MultiButton mb = new MultiButton(sizeOptions[iter]);
                d.add(mb);
                mb.addActionListener(ee -> {
                    sizeButton.setTextLine1(mb.getTextLine1());
                    sizeButton.setTextLine2(mb.getTextLine2());
                    d.dispose();
                    sizeButton.revalidate();
                });
            }
            d.showPopupDialog(sizeButton);
        });

        // put elements in header container
        taskNameSizeContainer.add(BorderLayout.CENTER, nameField);
        taskNameSizeContainer.add(BorderLayout.EAST, sizeButton);

        // description style
        descriptionField.text("My Task's description.\nCan have multiple lines!");

        // delete style
        Style deleteStyle = deleteButton.getAllStyles();
        deleteStyle.setFgColor(0xffffff);
        deleteStyle.setBorder(null);
        deleteStyle.setBgColor(0xbe0000);
        deleteStyle.setBgTransparency(255);
        deleteStyle.setPaddingUnit(Style.UNIT_TYPE_PIXELS);
        deleteStyle.setPadding(pixels,pixels,pixels,pixels);

        deleteButton.addActionListener(e -> {
            Dialog d = new Dialog();
            d.setLayout(BoxLayout.y());
            d.addComponent(new SpanLabel("Are you sure you want to permanently delete this task?"));
            d.addComponent(new Button("Proceed"));
            d.addComponent(new Button("Cancel"));
            d.showPopupDialog(deleteButton);
        });

        // add containers to form
        add(taskNameSizeContainer);
        addComponent(descriptionField);

        addComponent(tagsContainer);
        add(deleteButton);
    }

    private int mmToPixels(double mm) {
        double pixelsPerMM = ((double)Display.getInstance().convertToPixels(10, true)) / 10.0;
        return (int)(mm * pixelsPerMM);
    }

    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

}
