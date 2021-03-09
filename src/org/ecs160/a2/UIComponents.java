package org.ecs160.a2;

import com.codename1.components.SpanMultiButton;
import com.codename1.ui.*;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.Border;
import com.codename1.ui.plaf.RoundBorder;
import com.codename1.ui.plaf.Style;

import java.time.LocalDateTime;

import static com.codename1.ui.CN.*;

// Contains Buttons and Components frequently used in UI
public class UIComponents {
    static class ButtonObject extends Button {
        public ButtonObject () {
            getAllStyles().setFgColor(UITheme.BLACK);
            getAllStyles().setMarginUnit(Style.UNIT_TYPE_DIPS);
            getAllStyles().setMargin(UITheme.PAD_1MM,
                                     UITheme.PAD_1MM,
                                     UITheme.PAD_1MM,
                                     UITheme.PAD_1MM);
            getAllStyles().setPaddingUnit(Style.UNIT_TYPE_DIPS);
        }

        public void setAllStyles(String text,
                                 int color,
                                 char icon,
                                 int pad) {
            if (!text.isEmpty())
                this.setText(text);

            this.getAllStyles().setBorder(RoundBorder.create()
                    .rectangle(true).color(color)
            );

            if (icon != ' ')
                this.setIcon(FontImage.createMaterial(icon,getUnselectedStyle()));

            this.getAllStyles().setPadding(pad,pad,pad,pad);
        }

        public void setSelectedColor() {
            this.getAllStyles().setBorder(
                    RoundBorder.create()
                            .rectangle(true)
                            .color(UITheme.COL_SELECTED)
            );
        }

        // pass in UITheme.[color]
        public void setMyColor(int color) {
            this.getAllStyles().setBorder(
                    RoundBorder.create()
                            .rectangle(true)
                            .color(color)
            );
        }

        // pass in FontImage.[icon]
        public void setMyIcon(char icon) {
            this.setIcon(
                FontImage.createMaterial(
                        icon,
                        getUnselectedStyle()
                )
            );
        }

        // pass in String
        public void setMyText(String text) {
            this.setText(text);
        }

        public void setMyMargin(int margin) {
            this.getAllStyles().setMarginUnit(Style.UNIT_TYPE_DIPS);
            this.getAllStyles().setMargin(margin,margin,margin,margin);
        }

        public void setMyPadding(int pad) {
            this.getAllStyles().setPaddingUnit(Style.UNIT_TYPE_DIPS);
            this.getAllStyles().setPadding(pad,pad,pad,pad);
        }
    }

    // args: size
    // used in: StandardTaskObject, SummaryTaskObject
    static class SizeLabelObject extends Label {
        public SizeLabelObject(String size) {
            setText(size);
            getAllStyles().setFgColor(UITheme.WHITE);

            getAllStyles().setPaddingUnit(Style.UNIT_TYPE_DIPS);

            getAllStyles().setPadding(UITheme.PAD_3MM,
                    UITheme.PAD_3MM,
                    UITheme.PAD_3MM,
                    UITheme.PAD_3MM);

            getAllStyles().setMarginUnit(Style.UNIT_TYPE_DIPS);
            getAllStyles().setMargin(UITheme.PAD_3MM,UITheme.PAD_3MM,UITheme.PAD_3MM,UITheme.PAD_3MM);

            getAllStyles().setBorder(RoundBorder.create().color(setColor(size)));

        }
        public void setSelectedColor() {
            this.getAllStyles().setFgColor(UITheme.BLACK);
            this.getAllStyles().setBorder(
                    RoundBorder.create()
                            .color(UITheme.COL_SELECTED)
            );
        }
        private int setColor(String size) {
            if (size.equals("XL")) {
                return UITheme.COL_SIZE_XL;
            } else if (size.equals("L")) {
                return UITheme.COL_SIZE_L;
            } else if (size.equals("M")) {
                return UITheme.COL_SIZE_M;
            } else {
                return UITheme.COL_SIZE_S;
            }
        }
    }

    static class SizeButtonObject extends Container {
        ButtonObject b = new ButtonObject();

        public SizeButtonObject(String size) {
            setLayout(new BorderLayout());
            b.setText(size);

            Dimension d = new Dimension(UITheme.PAD_3MM, UITheme.PAD_3MM);
            b.setSize(d);

            b.setMyColor(setColor(size));
            b.getAllStyles().setFgColor(UITheme.WHITE);
            add(BorderLayout.CENTER, b);
        }

        private int setColor(String size) {
            if (size == "XL") {
                return UITheme.COL_SIZE_XL;
            } else if (size == "L") {
                return UITheme.COL_SIZE_L;
            } else if (size == "M") {
                return UITheme.COL_SIZE_M;
            } else {
                return UITheme.COL_SIZE_S;
            }
        }
    }

    static class TitleObject extends Label {
        public TitleObject(String title)  {
            setText(title);
            getAllStyles().setFgColor(UITheme.GREY);
            getAllStyles().setMarginUnit(Style.UNIT_TYPE_DIPS);
            getAllStyles().setMargin(Component.LEFT, UITheme.PAD_3MM);
        }

        public void setSize(int size) {
            getAllStyles().setFont((Font.createSystemFont(FACE_SYSTEM, STYLE_PLAIN, size)));
        }

        public void setMyColor(int color) {
            getAllStyles().setFgColor(color);
        }

        public void removePadding() {
            getAllStyles().setMargin(Component.LEFT, 0);
        }
    }

    // args: tagName
    // used in: taskDetails, homeScreen, archivePage
    static class TagObject extends Container {
        Button tagLabel;
        String name;

        public String getName() {
            return name;
        }

        public TagObject (String tagName) {
            name = tagName;
            setLayout(new BorderLayout());
            tagLabel = new Button(tagName);
            tagLabel.getAllStyles().setFgColor(UITheme.BLACK);

            Font smallFont = Font.createSystemFont(FACE_MONOSPACE, STYLE_PLAIN, SIZE_SMALL);
            tagLabel.getAllStyles().setFont(smallFont);

            tagLabel.getAllStyles().setMarginUnit(Style.UNIT_TYPE_DIPS);
            tagLabel.getAllStyles().setMargin(Component.LEFT, UITheme.PAD_3MM);
            tagLabel.getAllStyles().setMargin(Component.BOTTOM, UITheme.PAD_1MM);
            tagLabel.getAllStyles().setBorder(
                    RoundBorder.create().rectangle(true).color(UITheme.YELLOW)
            );
            add(BorderLayout.CENTER, tagLabel);
        }
    }

    static class TaskObject extends Container {
        Task taskData;
        UINavigator ui;
        private SpanMultiButton taskContainer;

        public TaskObject(Task task, UINavigator ui) {
            setLayout(BoxLayout.y());
            this.taskData = task;
            this.ui = ui;

            // TASK container
            taskContainer = new SpanMultiButton(taskData.getName() + " (" + taskData.getTaskSizeString() + ')');
            taskContainer.setTextLine2(taskData.getTotalTimeString());

            taskContainer.getSelectedStyle().setBgColor(UITheme.BLACK);

            if (taskData.isActive()) {
                taskContainer.setEmblem(
                        FontImage.createMaterial(FontImage.MATERIAL_ALARM_ON,
                                taskContainer.getUnselectedStyle())
                );
            }

            String tags = "";
            for (String t : taskData.getTags()) {
                tags += t + '\t';
            }
            if (!tags.isEmpty())
                taskContainer.setTextLine3(tags);

            // LISTENERS
            taskContainer.addActionListener(e-> shortPressEvent());
            taskContainer.addLongPressListener(e-> longPressEvent());

            // OPTIONS container
            ButtonObject edit = new ButtonObject();
            edit.setMyIcon(FontImage.MATERIAL_MODE_EDIT);
            edit.setMyColor(UITheme.YELLOW);
            edit.addActionListener(e->{ui.goEdit(taskData.getName());});
            ButtonObject archive = new ButtonObject();
            archive.setMyIcon(FontImage.MATERIAL_SAVE);
            archive.setMyColor(UITheme.LIGHT_GREY);
            archive.addActionListener(e->{
                if (taskData.isArchived())
                    ui.backend.getTaskByName(taskData.getName()).unarchive();
                else
                    ui.backend.getTaskByName(taskData.getName()).archive();
//                currPage.animate();
                ui.refreshScreen();
                log("archived/unarchived task");
            });
            Container options = new Container(BoxLayout.x());
            options.addAll(edit, archive);

            // taskPanel: TASK + OPTIONS
            SwipeableContainer taskPanel = new SwipeableContainer(options, taskContainer);
            add(taskPanel);
            getAllStyles().setMarginUnit(Style.UNIT_TYPE_DIPS);
            getAllStyles().setMargin(UITheme.PAD_1MM,UITheme.PAD_1MM,UITheme.PAD_1MM,UITheme.PAD_1MM);
        }

        private void longPressEvent() {
            if (taskData.isArchived()) {
                taskData.unarchive();
                ui.refreshScreen();
            } else {
                ui.goDetails(taskData.getName());
            }
        }
        private void shortPressEvent() {
            if (taskData.isArchived()) {
                ui.goDetails(taskData.getName());
                return;
            } else if (taskData.isActive()) {
                taskData.stop();
            } else {
                Task activeTask = ui.backend.getActiveTask();
                if (activeTask != null) {
                    activeTask.stop();
                }
                taskData.start();
            }
            ui.refreshScreen();
        }

        @Override
        public boolean animate() {
            taskContainer.setTextLine2(taskData.getTotalTimeString());
            return true; //TODO what does this mean
        }
    }

    // args: N/A
    // used in: homeScreen, archivePage
    static class SearchBoxObject extends Container {
        public SearchBoxObject(){
            setLayout(BoxLayout.xRight());
            getAllStyles().setMarginLeft(100);
            TextField searchBar = new TextField("", "search", 12, TextArea.ANY);
            searchBar.getAllStyles().setBorder(RoundBorder.create().rectangle(true).color(UITheme.LIGHT_GREY));
            searchBar.getAllStyles().setFgColor(UITheme.BLACK);
            add(searchBar);

            Button filterButton = new Button("Filter");
            filterButton.getAllStyles().setFgColor(UITheme.BLACK);
            filterButton.setIcon(FontImage.createMaterial(
                    FontImage.MATERIAL_FILTER_LIST,
                    filterButton.getUnselectedStyle()
            ));
            add(filterButton);
        }
    }

    // args: name, size, duration
    // used in: summaryScreen
    static class SummaryTaskObject extends Container {
        Task taskObj;
        UINavigator ui;
        public SummaryTaskObject(Task task, LocalDateTime startTime,
                                 LocalDateTime endTime, UINavigator ui) {
            this.taskObj = task;
            this.ui = ui;

            setLayout(new BorderLayout());
            // left side (size, name)
            Label sizeLabel = new SizeLabelObject(taskObj.getTaskSizeString());
            Label nameLabel = new Label(taskObj.getName());
            nameLabel.getAllStyles().setFgColor(UITheme.BLACK);

            Container leftContainer = new Container(new BorderLayout());
            leftContainer.add(BorderLayout.WEST, sizeLabel);
            leftContainer.add(BorderLayout.CENTER, nameLabel);

            // right side (time)
            Label durationLabel = new Label(
                    Utility.durationToFormattedString(
                            taskObj.getTimeBetween(startTime, endTime)));//TODO need to restrict by time

            add(BorderLayout.WEST, leftContainer);
            add(BorderLayout.EAST, durationLabel);

            Button myButton = new Button();
            myButton.addActionListener(e->goDetails());
            setLeadComponent(myButton);
        }

        private void goDetails() {
            log("go description: " + taskObj.getName());
            ui.goDetails(taskObj.getName());
        }
    }

    static class HistoryTaskObject extends Container {
        public HistoryTaskObject(String startTime, String stopTime){
            setLayout(new BorderLayout());
            Border simpleBorder = Border.createLineBorder(1,UITheme.BLACK);
            getAllStyles().setBorder(simpleBorder);

            Label startLabel = new Label("Start: " + startTime);
            Label stopLabel = new Label("Stop: " + stopTime);


            UIComponents.ButtonObject Delete = new UIComponents.ButtonObject();
            Delete.setMyIcon(FontImage.MATERIAL_DELETE);
            Delete.setMyColor(UITheme.WHITE);
            Delete.getAllStyles().setBorder(RoundBorder.create().rectangle(true).color(UITheme.RED));

            UIComponents.ButtonObject Edit = new UIComponents.ButtonObject();
            Edit.setMyIcon(FontImage.MATERIAL_EDIT);
            Edit.getAllStyles().setBorder(RoundBorder.create().rectangle(true).color(UITheme.LIGHT_GREY));

            Container EastSide = new Container(BoxLayout.x());

            EastSide.add(stopLabel);
            EastSide.add(Edit);
            EastSide.add(Delete);

            add(WEST, startLabel);
            add(EAST, EastSide);
        }
    }
}
