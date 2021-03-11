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
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.plaf.Border;
import com.codename1.ui.plaf.RoundBorder;
import com.codename1.ui.plaf.Style;

import java.util.ArrayList;
import java.util.Vector;

import static com.codename1.ui.CN.*;

public class EditTaskScreen extends Form {
    private Container Footer;
    private Container TitleRow;
    private Container TagRow;

    // IMPORTANT DATA FIELDS
    private TextField nameField;
    private SizeMultiButton sizeButton;
    private Container tagField;
    private TextComponent descField;
//    private java.util.List<UIComponents.ButtonObject> tagObjs;
    private java.util.List<String> tagList;

    private Task task;
    private String nameData;
    private String sizeData;
    private String descriptionData;
    private java.util.List<String> tagsData;
    private Boolean isNewTask = false;

    private UINavigator ui;

    private void initData(Task passedTask) {
        this.task = passedTask;
        if (task == null) {
            this.task = new Task("");
            isNewTask = true;
        }
        nameData = task.getName();
        sizeData = task.getTaskSizeString();
        descriptionData = task.getDescription();
        tagsData = task.getTags();
    }

    @Override
    public void show() {
        createEditTaskScreen();
        super.show();
    }

    @Override
    public void showBack() {
        createEditTaskScreen();
        super.showBack();
    }

    public EditTaskScreen(Task taskObj, UINavigator ui) {
        this.ui = ui;
        initData(taskObj); // save data so it may be rewritten
        createToolbar();
        createEditTaskScreen();
    }

    private void createEditTaskScreen() {
        removeAll();
        setTitle("Edit Task");
        setLayout(new BorderLayout());

        createFooter();
        Container body = new Container(BoxLayout.y());

        createTitleRow();
        createTagRow();
        // description row
        descField = new TextComponent();
        descField.label("Description").multiline(true);
        descField.onTopMode(true);
        descField.text(descriptionData);

        body.addAll(TitleRow, TagRow, descField);

        add(BorderLayout.SOUTH, Footer);
        add(BorderLayout.CENTER, body);
    }

    private void createTitleRow() {
        // title row
        TitleRow = new Container();
        TitleRow.setLayout(new BorderLayout());

        nameField = new TextField(nameData, "Name");
        TitleRow.add(BorderLayout.CENTER,nameField);

        sizeButton = new SizeMultiButton(sizeData);
        TitleRow.add(BorderLayout.EAST, sizeButton);
    }

    private void createTagRow() {
        // tag row
        TagRow = new Container();
        TagRow.setLayout(BoxLayout.y());

        tagField = new Container();
        UIComponents.ButtonObject addButton = new UIComponents.ButtonObject();
        addButton.setAllStyles("", UITheme.GREEN,
                FontImage.MATERIAL_ADD, UITheme.PAD_3MM);
        addButton.addActionListener(e->newTagPrompt());

        for (String tag : tagsData) {
            UIComponents.ButtonObject tagButton = new UIComponents.ButtonObject();
            tagButton.setAllStyles(tag, UITheme.YELLOW,
                    FontImage.MATERIAL_CLOSE, UITheme.PAD_3MM);
            tagButton.addActionListener(e-> RemoveTag(tagButton, tag));

            tagField.add(tagButton);
        }

        TagRow.add(BoxLayout.encloseX(
                new UIComponents.TitleObject("Tags"), addButton)
        );
        TagRow.add(tagField);
    }

    private void saveChanges() {
        nameData = nameField.getText();
        sizeData = sizeButton.getText();
        descriptionData = descField.getText();
        log("tags: " + tagsData);
//        tagsData = tagList;

//        for (String : tagList) {
//            log("saving" + tagComponent);
//            log("saving" + tagComponent.getSelectCommandText());
//            tagsData.add(tagComponent.getSelectCommandText());
//        }

        if (nameData.isEmpty()) { //TODO check if name already taken
            ui.goBack();
            return;
        }

        if (isNewTask) {
            ui.backend.saveTask(task);
            isNewTask = false;
        }
        log("new task data" + tagsData);

        task.setName(nameData);
        task.setTaskSize(sizeData);
        task.addAllTags(tagsData);
        task.setDescription(descriptionData);
        ui.goBack();
    }

    private void createToolbar() {
        getToolbar().addMaterialCommandToRightBar("Done",
                ' ', UITheme.PAD_6MM, e->saveChanges());
    }

    private void createFooter() {
        Footer = new Container();
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

        // DELETE
        UIComponents.ButtonObject confirm = new UIComponents.ButtonObject();
        confirm.setAllStyles("Confirm", UITheme.RED, ' ', UITheme.PAD_3MM);
        confirm.addActionListener(e -> ui.goDelete(task));

        // CANCEL
        UIComponents.ButtonObject cancel = new UIComponents.ButtonObject();
        cancel.setAllStyles("Cancel", UITheme.LIGHT_GREY, ' ', UITheme.PAD_3MM);
        cancel.addActionListener(y -> { d.dispose(); });

        d.addComponent(new SpanLabel("Permanently delete this task?"));
        d.addComponent(confirm);
        d.addComponent(cancel);
        d.showPopupDialog(b);
    }

    private void newTagPrompt() {
        Dialog d = new Dialog();
        d.setLayout(BoxLayout.y());
        TextField tagNameField = new TextField("", "Name");
        tagNameField.setWidth(12);
        d.add(tagNameField);

        // CONFIRM (ADD TAG)
        UIComponents.ButtonObject confirm = new UIComponents.ButtonObject();
        confirm.setAllStyles("Confirm", UITheme.YELLOW, ' ', UITheme.PAD_3MM);
        confirm.addActionListener(e -> {
            String newTagName = tagNameField.getText();

            UIComponents.ButtonObject tagButton = new UIComponents.ButtonObject();
            tagButton.setAllStyles(newTagName, UITheme.YELLOW,
                    FontImage.MATERIAL_CLOSE, UITheme.PAD_3MM);
            tagButton.addActionListener(event -> RemoveTag(tagButton, newTagName));

            if (tagNameField.getText().isEmpty()) {
                new UIComponents.showWarningDialog(
                        "Please enter a tag name"
                );

            } else if (task.hasTag(tagNameField.getText())) {
                new UIComponents.showWarningDialog(
                        "There is already a tag with this name"
                );

            } else {
                tagField.add(tagButton);
                tagsData.add(tagNameField.getText());
                task.addTag(tagNameField.getText()); //TODO added this
                d.dispose();
            }
        });

        // CANCEL
        UIComponents.ButtonObject cancel = new UIComponents.ButtonObject();
        cancel.setAllStyles("Cancel", UITheme.LIGHT_GREY,
                ' ',UITheme.PAD_3MM);
        cancel.addActionListener(y -> { d.dispose(); });

        d.add(GridLayout.encloseIn(2, cancel, confirm));
        d.show();

    }

    private void RemoveTag(Component deletedComponent, String name) {
        Dialog d = new Dialog();
        d.setLayout(BoxLayout.y());
        d.addComponent(new SpanLabel("Remove \'" + name + "\'?"));

        UIComponents.ButtonObject confirmButton = new UIComponents.ButtonObject();
        confirmButton.setAllStyles("Confirm", UITheme.RED,
                ' ', UITheme.PAD_3MM);

        UIComponents.ButtonObject cancelButton = new UIComponents.ButtonObject();
        cancelButton.setAllStyles("Cancel", UITheme.LIGHT_GREY,
                ' ', UITheme.PAD_3MM);

        confirmButton.addActionListener(e -> {
            System.out.println("REMOVING TAG");
            tagsData.remove(name);
            tagField.removeComponent(deletedComponent);
            task.removeTag(name);
            d.dispose();
        });

        cancelButton.addActionListener(e -> {
            System.out.println("CANCEL DELETE");
            d.dispose();
        });

        d.add(GridLayout.encloseIn(2,cancelButton, confirmButton));
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