package org.ecs160.a2;

import com.codename1.components.MultiButton;
import com.codename1.components.SpanLabel;
import com.codename1.ui.*;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.TextComponent;
import com.codename1.ui.TextField;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.plaf.RoundBorder;
import com.codename1.ui.plaf.Style;

import static com.codename1.ui.CN.*;
import static org.ecs160.a2.UITheme.*;
import static org.ecs160.a2.UIComponents.*;

public class EditTaskScreen extends Form {
    private Container Footer;
    private Container TitleRow;
    private Container TagRow;

    // EDIT FIELDS
    private TextField nameField;
    private SizeMultiButton sizeButton;
    private Container tagField;
    private TextComponent descField;

    // DATA
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
        String title = isNewTask ? "New Task" : "Edit Task";
        setTitle(title);
        setLayout(new BorderLayout());

        Container body = new Container(BoxLayout.y());

        createTitleRow();
        createTagRow();
        createDescField();
        createFooter();

        body.addAll(TitleRow, TagRow, descField);
        add(BorderLayout.CENTER, body);

        if (!isNewTask)
            add(BorderLayout.SOUTH, Footer);
    }

    private void createDescField() {
        // description row
        descField = new TextComponent();
        descField.label("Description").multiline(true);
        descField.onTopMode(true);
        descField.text(descriptionData);
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
        ButtonObject addButton = new ButtonObject();
        addButton.setAllStyles("", GREEN, ICON_NEW, PAD_3MM);
        addButton.addActionListener(e->newTagPrompt());

        for (String tag : tagsData) {
            ButtonObject tagButton = new ButtonObject();
            tagButton.setAllStyles(tag, COL_SELECTED, ICON_CLOSE, PAD_3MM);
            tagButton.addActionListener(e-> deleteTagPrompt(tagButton, tag));
            tagField.add(tagButton);
        }

        TagRow.add(BoxLayout.encloseX(
                new TitleObject("Tags"), addButton)
        );
        TagRow.add(tagField);
    }

    private void saveChanges() {
        nameData = nameField.getText();
        sizeData = sizeButton.getText();
        descriptionData = descField.getText();
        log("tags: " + tagsData);

        //TODO check if name already taken
        if (nameData.isEmpty()) {
            new showWarningDialog("Please enter a task name");
            return;
        }

        if (isNewTask) {
            ui.backend.saveTask(task);
            isNewTask = false;
        }

        task.setName(nameData);
        task.setTaskSize(sizeData);
        task.addAllTags(tagsData);
        task.setDescription(descriptionData);
        ui.goBack();
    }

    private void createToolbar() {
        if (isNewTask)
            getToolbar().addMaterialCommandToLeftBar("",
                    ICON_BACK, PAD_6MM, e->ui.goBack());

        getToolbar().addMaterialCommandToRightBar("Save",
                ' ', PAD_6MM, e->saveChanges());
    }

    private void createFooter() {
        Footer = new Container();
        Footer.setLayout(new BorderLayout());
        ButtonObject deleteButton = new ButtonObject();
        deleteButton.setAllStyles("Delete", RED, ICON_DELETE, PAD_3MM);
        deleteButton.addActionListener(e -> deletePrompt(deleteButton));
        Footer.add(BorderLayout.EAST, deleteButton);
    }
    private void deletePrompt(ButtonObject b) {
        Dialog d = new Dialog();
        d.setLayout(BoxLayout.y());

        // DELETE
        ButtonObject confirm = new ButtonObject();
        confirm.setAllStyles("Confirm", UITheme.RED, ' ', UITheme.PAD_3MM);
        confirm.addActionListener(e -> ui.goDelete(task));

        // CANCEL
        ButtonObject cancel = new ButtonObject();
        cancel.setAllStyles("Cancel", UITheme.LIGHT_GREY, ' ', UITheme.PAD_3MM);
        cancel.addActionListener(y -> { d.dispose(); });

        d.addComponent(FlowLayout.encloseCenterMiddle(
                new SpanLabel("Permanently delete this task?")));
        d.addComponent(confirm);
        d.addComponent(cancel);
        d.showPopupDialog(b);
    }

    private void saveSelectedTag(String tagName) {
        ButtonObject tagButton = new ButtonObject();
        tagButton.setAllStyles(tagName, COL_SELECTED,
                FontImage.MATERIAL_CLOSE, PAD_3MM);
        tagButton.addActionListener(event -> deleteTagPrompt(tagButton, tagName));

        tagField.add(tagButton);
        tagsData.add(tagName);
        task.addTag(tagName);
    }
    private void newTagPrompt() {
        Dialog d = new Dialog();
        d.setLayout(BoxLayout.y());
        d.addComponent(FlowLayout.encloseCenterMiddle(
                new SpanLabel("Enter a new tag name")));
        TextField tagNameField = new TextField("", "New name");
        tagNameField.setWidth(12);
        d.add(tagNameField);

        Container availableTags = FlowLayout.encloseCenterMiddle();
        java.util.List<String> allTags = ui.backend.getAllTags();
        for (String tag : allTags) {
            if (!tagsData.contains(tag)) {
                ButtonObject tagSelect = new ButtonObject();
                tagSelect.setAllStyles(tag, LIGHT_GREEN, ' ', PAD_3MM);
                availableTags.addComponent(tagSelect);

                tagSelect.addActionListener(e -> {
                    saveSelectedTag(tag);
                    d.dispose();
                });

            }
        }

        if (!availableTags.getChildrenAsList(false).isEmpty()) {
            d.addComponent(FlowLayout.encloseCenterMiddle(
                    new SpanLabel("Available tags")));
            d.addComponent(availableTags);
        }

        ButtonObject confirm = new ButtonObject();
        confirm.setAllStyles("Confirm", COL_SELECTED, ' ', PAD_3MM);
        confirm.addActionListener(e -> {
            String newTagName = tagNameField.getText();

            if (newTagName.isEmpty()) {
                new showWarningDialog(
                        "Please enter a tag name or pick an existing tag");

            } else if (task.hasTag(newTagName)) {
                new showWarningDialog("Task already has this tag");

            } else {
                saveSelectedTag(newTagName);
                d.dispose();
            }
        });

        ButtonObject cancel = new ButtonObject();
        cancel.setAllStyles("Cancel", LIGHT_GREY,
                ' ',PAD_3MM);
        cancel.addActionListener(y -> { d.dispose(); });
        d.add(GridLayout.encloseIn(2, cancel, confirm));
        d.show();

    }
    private void deleteTagPrompt(Component deletedComponent, String name) {
        Dialog d = new Dialog();
        d.setLayout(BoxLayout.y());
        d.addComponent(FlowLayout.encloseCenterMiddle(
                new SpanLabel("Remove \'" + name + "\'?")));

        ButtonObject confirmButton = new ButtonObject();
        confirmButton.setAllStyles("Confirm", RED, ' ', PAD_3MM);

        ButtonObject cancelButton = new ButtonObject();
        cancelButton.setAllStyles("Cancel", COL_UNSELECTED, ' ', PAD_3MM);

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
        getUnselectedStyle().setMargin(UITheme.PAD_1MM, UITheme.PAD_1MM,
                UITheme.PAD_1MM, UITheme.PAD_1MM);

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