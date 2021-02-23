package org.ecs160.a2;

import com.codename1.components.MultiButton;
import com.codename1.components.SpanLabel;
import com.codename1.ui.*;
import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Label;
import com.codename1.ui.TextComponent;
import com.codename1.ui.TextField;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.Border;
import com.codename1.ui.plaf.RoundBorder;
import com.codename1.ui.plaf.Style;

import java.util.ArrayList;
import java.util.Vector;

import static com.codename1.ui.CN.log;
import static com.codename1.ui.CN.setDarkMode;


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

public class EditTaskScreen extends Form {
    Form prevPage;
    Form currentPage;

    private Container TitleRow = new Container();
    private Container TagRow = new Container();
    private Container Header = new Container();
    private Container Footer = new Container();

    // TODO: get NAME, SIZE, DESCRIPTION
//    private String name = "[Name]";
//    private String size = "[Size]";
//    private String description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."; // TODO: get description data
//    private String[] tags = {"tag1", "tag2", "tag3","tag4", "tag5", "tag6","tag7", "tag8", "tag9"};

    // IMPORTANT DATA FIELDS
    private TextField nameField;
    private TextComponent descField = new TextComponent();
    private Container tagList;
    private java.util.List<UIComponents.TagObject> tagObjs;


    private Task taskData;
    private String nameData;
    private String sizeData;
    private String descriptionData;
    private java.util.List<String> tagsData = new ArrayList<>();
    private Boolean isNewTask = false;

    private void initData(Task task) {
        if (task == null) {
            nameData = "";
            sizeData = "S";
            descriptionData = "";
            isNewTask = true;
        } else {
            nameData = task.getName();
            sizeData = task.getTaskSizeString();
            descriptionData = task.getDescription();
            tagsData = taskData.getTags();
        }
    }


    EditTaskScreen(Task task) {
        tagObjs = new Vector<>();
        taskData = task;
        prevPage = Display.getInstance().getCurrent();

        createEditTaskScreen();
    }

    private void createEditTaskScreen() {
        initData(taskData);

        currentPage = new Form("Edit Task");
        currentPage.setLayout(new BorderLayout());
        setDarkMode(true);

        createHeader();
        createFooter();
        Container body = new Container(BoxLayout.y());

        createTitleRow();
        createTagRow();
        // description row
        descField.label("Description").multiline(true);
        descField.onTopMode(true);
        descField.text(descriptionData);

        body.addAll(TitleRow, TagRow, descField);

        currentPage.add(BorderLayout.NORTH, Header);
        currentPage.add(BorderLayout.SOUTH, Footer);
        currentPage.add(BorderLayout.CENTER, body);

        currentPage.show();
    }

    private void createTitleRow() {
        // title row
        TitleRow.setLayout(new BorderLayout());
        nameField = new TextField(nameData, "Name");

        TitleRow.add(BorderLayout.CENTER,nameField);
        TitleRow.add(BorderLayout.EAST, new SizeMultiButton(sizeData));
    }

    private void createTagRow() {
        // tag row
        TagRow.setLayout(BoxLayout.y());

        tagList = new Container();
        UIComponents.ButtonObject addButton = new UIComponents.ButtonObject();
        addButton.setMyIcon(FontImage.MATERIAL_ADD);
        addButton.setMyColor(UITheme.GREEN);
        addButton.setMyMargin(UITheme.PAD_1MM);
        addButton.setMyPadding(UITheme.PAD_3MM);
        addButton.addActionListener(e->newTagPrompt());

        for (String tag : tagsData) {
            UIComponents.TagObject tagObj = new UIComponents.TagObject(tag);
            tagObj.addPointerPressedListener(e->{ new Dialog("Delete " + tag); });
            tagList.add(tagObj);
            tagObjs.add(tagObj);
        }
        tagList.add(addButton);

        TagRow.add(new UIComponents.TitleObject("Tags"));
        TagRow.add(tagList);
    }

    private void createHeader() {
        Header.setLayout(new BorderLayout());
        UIComponents.ButtonObject doneButton = new UIComponents.ButtonObject();
        doneButton.setMyColor(UITheme.YELLOW);
        doneButton.setMyText("Done");
        doneButton.setMyPadding(UITheme.PAD_3MM);

        doneButton.addActionListener(e -> {
            log(descField.getText());
            nameData = nameField.getText();
            sizeData = "S"; //TODO
            descriptionData = descField.getText();
            tagsData = new Vector<String>();
            for (UIComponents.TagObject tagButton : tagObjs) {
                tagsData.add(tagButton.getName());
            }

            if (isNewTask) {
                if (nameData == "") {
                    UINavigator.goBack(prevPage);
                    return;
                }
                UINavigator.backend.newTask(
                        nameData,
                        sizeData,
                        descriptionData,
                        tagsData
                );
            }
            UINavigator.goBackAndSave(prevPage);

        });

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
        deleteButton.addActionListener(e -> deletePrompt(deleteButton));
    }
    private void deletePrompt(UIComponents.ButtonObject b) {
        Dialog d = new Dialog();
        d.setLayout(BoxLayout.y());

        UIComponents.ButtonObject confirm = new UIComponents.ButtonObject();

        // CONFIRM (GO BACK AND SAVE)
        confirm.setMyColor(UITheme.RED);
        confirm.setMyPadding(UITheme.PAD_1MM);
        confirm.setMyText("Confirm");
        confirm.addActionListener(e -> UINavigator.goDelete(prevPage));

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

    private void newTagPrompt() {
        Dialog d = new Dialog();
        d.setLayout(BoxLayout.y());
        TextField tagNameField = new TextField("Name");
        d.add(tagNameField);


        // CONFIRM (GO BACK AND SAVE)
        UIComponents.ButtonObject confirm = new UIComponents.ButtonObject();
        confirm.setMyColor(UITheme.YELLOW);
        confirm.setMyPadding(UITheme.PAD_1MM);
        confirm.setMyText("Confirm");
        confirm.addActionListener(e -> {
            String newTagName = tagNameField.getText();
            UIComponents.TagObject newTagObj = new UIComponents.TagObject(newTagName);

            Button deleteButton = new Button("Delete");

            deleteButton.addActionListener(event -> {
                RemoveTag(newTagObj);
            });

            newTagObj.add(BorderLayout.EAST, deleteButton);

            tagList.add(newTagObj);
            tagObjs.add(newTagObj);
            d.dispose();
        });

        // CANCEL
        UIComponents.ButtonObject cancel = new UIComponents.ButtonObject();
        cancel.setMyColor(UITheme.LIGHT_GREY);
        cancel.setMyPadding(UITheme.PAD_3MM);
        cancel.setMyText("Cancel");
        cancel.addActionListener(y -> { d.dispose(); });



        d.add(confirm);
        d.add(cancel);
        d.show();

    }

    private void RemoveTag(Component deletedComponent) {
        Dialog d = new Dialog();
        d.setLayout(BoxLayout.y());
        d.add("Are you sure?");

        Button confirmButton = new Button("Confirm");
        Button cancelButton = new Button("Cancel");

        confirmButton.addActionListener(e -> {
            System.out.println("REMOVING TAG");
            tagObjs.remove(deletedComponent);
            tagList.removeComponent(deletedComponent);
            d.dispose();
        });

        cancelButton.addActionListener(e -> {
            System.out.println("CANCEL DELETE");
            d.dispose();
        });

        d.add(confirmButton);
        d.add(cancelButton);

        d.show();
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