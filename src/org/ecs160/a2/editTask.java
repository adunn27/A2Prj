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

public class editTask extends Form {
    Form prevPage;
    Form currentPage;

    private Container TitleRow = new Container();
    private Container TagRow = new Container();
    private Container DescRow = new Container();
    private Container Header = new Container();
    private Container Footer = new Container();

    // TODO: get NAME, SIZE, DESCRIPTION
    private String nameTemp = "[Name]";
    private String sizeTemp = "[Size]";
    private String descriptionTemp = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."; // TODO: get description data
    private String[] tagsTemp = {"tag1", "tag2", "tag3","tag4", "tag5", "tag6","tag7", "tag8", "tag9"};

    editTask() {
        prevPage = Display.getInstance().getCurrent();

        currentPage = new Form("Edit Task");
        currentPage.setLayout(new BorderLayout());

        createHeader();
        createFooter();
        Container body = new Container(BoxLayout.y());

        createTitleRow(nameTemp, sizeTemp);
        createTagRow(tagsTemp);

        // description row
        TextComponent descRow = new TextComponent().label("Description").multiline(true);

        body.addAll(TitleRow, TagRow, descRow);

        currentPage.add(BorderLayout.NORTH, Header);
        currentPage.add(BorderLayout.SOUTH, Footer);
        currentPage.add(BorderLayout.CENTER, body);

        currentPage.show();
    }

    private void createTitleRow(String name, String size) {
        // title row
        TitleRow.setLayout(new BorderLayout());
        TextField nameField = new TextField(name, "Name");

        TitleRow.add(BorderLayout.CENTER,nameField);
        TitleRow.add(BorderLayout.EAST, new SizeMultiButton(size));
    }
    private void createTagRow(String[] tags) {
        // tag row
        TagRow.setLayout(BoxLayout.y());

        Container tagList = new Container();
        UIComponents.ButtonObject addButton = new UIComponents.ButtonObject();
        addButton.setMyIcon(FontImage.MATERIAL_ADD);
        addButton.setMyColor(UITheme.GREEN);
        addButton.setMyMargin(UITheme.PAD_1MM);
        addButton.setMyPadding(UITheme.PAD_3MM);

        for (int i = 0; i < tags.length; i++) {
            tagList.add(new tagEditObject());
        }
        tagList.add(addButton);

        TagRow.add(new Label("Tags"));
        TagRow.add(tagList);
    }

    private void createHeader() {
        Header.setLayout(new BorderLayout());
        UIComponents.ButtonObject doneButton = new UIComponents.ButtonObject();
        doneButton.setMyColor(UITheme.YELLOW);
        doneButton.setMyText("Done");
        doneButton.setMyPadding(UITheme.PAD_3MM);

        doneButton.addActionListener(e -> goDone());

        Header.add(BorderLayout.EAST, doneButton);
    }
    private void createFooter() {
        Footer.setLayout(new BorderLayout());
        UIComponents.ButtonObject deleteButton = new UIComponents.ButtonObject();
        deleteButton.setMyColor(UITheme.RED);
        deleteButton.setMyText("Delete");
        deleteButton.setMyIcon(FontImage.MATERIAL_DELETE);
        deleteButton.setMyPadding(UITheme.PAD_3MM);

        Footer.add(BorderLayout.EAST, deleteButton);

        // listener
        deleteButton.addActionListener(e -> goDelete(deleteButton));
    }

    private void goDone() {
        prevPage.showBack();
    }

    private void goDelete(UIComponents.ButtonObject b) {
        Dialog d = new Dialog();
        d.setLayout(BoxLayout.y());

        UIComponents.ButtonObject confirm = new UIComponents.ButtonObject();

        // CONFIRM
        confirm.setMyColor(UITheme.RED);
        confirm.setMyPadding(UITheme.PAD_1MM);
        confirm.setMyText("Confirm");
        confirm.addActionListener(x -> {
            // TODO: DELETE!
            prevPage.showBack();
        });

        // CANCEL
        UIComponents.ButtonObject cancel = new UIComponents.ButtonObject();
        cancel.setMyColor(UITheme.LIGHT_GREY);
        cancel.setMyPadding(UITheme.PAD_3MM);
        cancel.setMyText("Cancel");
        cancel.addActionListener(y -> { d.dispose(); });

        d.addComponent(new SpanLabel("Permanently delete this task?"));
        d.addComponent(confirm);
        d.addComponent(cancel);
        d.showPopupDialog(b);
    }
}

class EditHeader extends Container {
    public EditHeader() {
        setLayout(new BorderLayout());
        UIComponents.ButtonObject doneButton = new UIComponents.ButtonObject();
        doneButton.setMyColor(UITheme.YELLOW);
        doneButton.setMyText("Done");

        NavigationCommand doneNav = new NavigationCommand("Back to Details Page");
        doneNav.setNextForm(new taskDetails());

        doneButton.addActionListener(e -> {
            doneNav.getNextForm().show();
        });

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


            NavigationCommand confirmNav = new NavigationCommand("Deleting Task");
            confirmNav.setNextForm(new homeScreen());

            confirm.addActionListener(conf -> {
                // TODO: DELETE!
                confirmNav.getNextForm().show();
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


