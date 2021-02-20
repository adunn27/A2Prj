package org.ecs160.a2;

import com.codename1.components.InteractionDialog;
import com.codename1.components.MultiButton;
import com.codename1.components.SpanLabel;
import com.codename1.ui.*;
import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Label;
import com.codename1.ui.TextArea;
import com.codename1.ui.TextComponent;
import com.codename1.ui.TextField;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.plaf.Border;
import com.codename1.ui.plaf.RoundBorder;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;
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
        Button tagDelete = new Button(); // needs event listener
        tagDelete.setIcon(FontImage.createMaterial(FontImage.MATERIAL_CLOSE, tagDelete.getUnselectedStyle()));
        tagDelete.getAllStyles().setFgColor(UITheme.RED);

        add(BorderLayout.CENTER, tagName);
        add(BorderLayout.EAST, tagDelete);
        getAllStyles().setBorder(Border.createDashedBorder(6, UITheme.LIGHT_GREY));
        getAllStyles().setMarginUnit(Style.UNIT_TYPE_DIPS);
        getAllStyles().setMargin(UITheme.PAD_3MM,UITheme.PAD_3MM,UITheme.PAD_3MM,UITheme.PAD_3MM);
    }

}

class tagEditObject extends Container {
    String name = "tagXX";
    public tagEditObject() {
        // todo: implement tagEditObject()
//        add(tagEdit);
    }
}


class EditHeader extends Container {
    public EditHeader() {
        setLayout(new BorderLayout());
        UIComponents.ButtonObject doneButton = new UIComponents.ButtonObject();
        doneButton.setMyColor(UITheme.YELLOW);
        doneButton.setMyText("Done");

        add(BorderLayout.EAST, doneButton);
    }
}
class EditFooter extends Container {
    public EditFooter() {
        setLayout(new BorderLayout());
        UIComponents.ButtonObject deleteButton = new UIComponents.ButtonObject();
        deleteButton.setMyColor(UITheme.RED);
        deleteButton.setMyText("Delete");
        deleteButton.setMyIcon(FontImage.MATERIAL_DELETE);
        deleteButton.setMyPadding(UITheme.PAD_3MM);

        add(BorderLayout.EAST, deleteButton);

        // delete button functionality
        deleteButton.addActionListener(e -> {
            Dialog d = new Dialog();

            UIComponents.ButtonObject confirm = new UIComponents.ButtonObject();
            confirm.setMyColor(UITheme.RED);
            confirm.setMyPadding(UITheme.PAD_1MM);
            confirm.setMyText("Confirm");

            confirm.addActionListener(conf -> {
                // TODO: DELETE!
            });

            UIComponents.ButtonObject cancel = new UIComponents.ButtonObject();
            cancel.setMyColor(UITheme.LIGHT_GREY);
            cancel.setMyPadding(UITheme.PAD_3MM);
            cancel.setMyText("Cancel");


            cancel.addActionListener(canc -> {
                d.dispose();
            });


            d.setLayout(BoxLayout.y());
            d.addComponent(new SpanLabel("Permanently delete this task?"));
            d.addComponent(confirm);
            d.addComponent(cancel);
            d.showPopupDialog(deleteButton);
        });
    }
}

class SizeMultiButton extends MultiButton {
    final String[] sizeOptions = {"S", "M", "L", "XL"};
    public SizeMultiButton(String size) {
        setText(size);
        getUnselectedStyle().setMarginUnit(Style.UNIT_TYPE_DIPS);
        getUnselectedStyle().setMargin(UITheme.PAD_1MM,
                                       UITheme.PAD_1MM,
                                       UITheme.PAD_1MM,
                                       UITheme.PAD_1MM);

        getAllStyles().setBorder(
                RoundBorder.create()
                        .rectangle(true)
                        .color(UITheme.LIGHT_GREY)
        );

        this.addActionListener(e -> {
            Dialog d = new Dialog();
            d.setLayout(BoxLayout.y());
            d.getContentPane().setScrollableY(false);
            for(int iter = 0; iter < sizeOptions.length ; iter++) {
                MultiButton mb = new MultiButton(sizeOptions[iter]);
                d.add(mb);
                mb.addActionListener(ee -> {
                    this.setTextLine1(mb.getTextLine1());
                    this.setTextLine2(mb.getTextLine2());
                    d.dispose();
                    this.revalidate();
                });
            }
            d.showPopupDialog(this);
        });

    }
}

public class editTask extends Form {

    public editTask() {
        setLayout(new BorderLayout());
        setTitle("Edit Task");

        Container header = new EditHeader();
        Container footer = new EditFooter();
        Container body = new Container(new BoxLayout(BoxLayout.Y_AXIS));

        // title row
        Container titleRow = new Container(new BorderLayout());
        titleRow.add(BorderLayout.CENTER,
                     new TextComponent().label("Name"));

        titleRow.add(BorderLayout.EAST,
                     new SizeMultiButton("Size"));
        body.add(titleRow);

        // description row
        TextComponent descRow = new TextComponent().label("Description").multiline(true);
        body.add(descRow);

        // tag row
        Container tagRow = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        Container tagList = new Container();
        UIComponents.ButtonObject addButton = new UIComponents.ButtonObject();
        addButton.setMyIcon(FontImage.MATERIAL_ADD);
        addButton.setMyColor(UITheme.GREEN);

        for (int i = 0; i < 9; i++) {
            tagList.add(new tagEditObject());
        }
        tagList.add(addButton);

        tagRow.add(new Label("Tags"));
        tagRow.add(tagList);
        body.add(tagRow);

        add(BorderLayout.NORTH, header);
        add(BorderLayout.CENTER, body);
        add(BorderLayout.SOUTH, footer);

    }

    private int mmToPixels(double mm) {
        double pixelsPerMM = ((double)Display.getInstance().convertToPixels(10, true)) / 10.0;
        return (int)(mm * pixelsPerMM);
    }

    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

}
