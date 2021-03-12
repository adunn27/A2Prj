package org.ecs160.a2;
import static org.ecs160.a2.UITheme.*;

import com.codename1.components.SpanLabel;
import com.codename1.components.SpanMultiButton;
import com.codename1.ui.*;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.Border;
import com.codename1.ui.plaf.RoundBorder;
import com.codename1.ui.plaf.Style;
import javafx.scene.paint.Material;

import java.time.LocalDateTime;
import static com.codename1.ui.CN.*;

public class UIComponents {
    static class ButtonObject extends Button {
        public ButtonObject () {
            getAllStyles().setFgColor(UITheme.BLACK);
            getAllStyles().setMarginUnit(Style.UNIT_TYPE_DIPS);
            getAllStyles().setMargin(PAD_1MM, PAD_1MM, PAD_1MM, PAD_1MM);
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
                this.setIcon(FontImage.createMaterial(icon,
                        getUnselectedStyle()));

            this.getAllStyles().setPadding(pad,pad,pad,pad);
        }

        public void setSelectedColor() {
            this.getAllStyles().setBorder(
                    RoundBorder.create()
                            .rectangle(true)
                            .color(COL_SELECTED)
            );
        }

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

//         pass in String
        public void setMyText(String text) {
            this.setText(text);
        }

        public void setMyPadding(int pad) {
            this.getAllStyles().setPaddingUnit(Style.UNIT_TYPE_DIPS);
            this.getAllStyles().setPadding(pad,pad,pad,pad);
        }
    }

    static class SizeLabelObject extends Label {
        public SizeLabelObject(String size) {
            setText(size);
            getAllStyles().setFgColor(WHITE);

            getAllStyles().setPaddingUnit(Style.UNIT_TYPE_DIPS);

            getAllStyles().setPadding(PAD_3MM, PAD_3MM, PAD_3MM, PAD_3MM);

            getAllStyles().setMarginUnit(Style.UNIT_TYPE_DIPS);
            getAllStyles().setMargin(PAD_3MM, PAD_3MM, PAD_3MM, PAD_3MM);

            getAllStyles().setBorder(RoundBorder.create()
                    .color(setSizeColor(size)));
        }

        public void setSelectedColor() {
            this.getAllStyles().setFgColor(BLACK);
            this.getAllStyles().setBorder(
                    RoundBorder.create().color(COL_SELECTED)
            );
        }
        private int setSizeColor(String size) {
            switch (size) {
                case "XL":
                    return COL_SIZE_XL;
                case "L":
                    return COL_SIZE_L;
                case "M":
                    return COL_SIZE_M;
                default:
                    return COL_SIZE_S;
            }
        }
    }
    static class SizeButtonObject extends Container {
        ButtonObject b = new ButtonObject();

        public SizeButtonObject(String size) {
            setLayout(new BorderLayout());
            b.setText(size);

            Dimension d = new Dimension(PAD_3MM, PAD_3MM);
            b.setSize(d);

            b.setMyColor(setColor(size));
            b.getAllStyles().setFgColor(WHITE);
            add(BorderLayout.CENTER, b);
        }

        private int setColor(String size) {
            if (size == "XL") {
                return COL_SIZE_XL;
            } else if (size == "L") {
                return COL_SIZE_L;
            } else if (size == "M") {
                return COL_SIZE_M;
            } else {
                return COL_SIZE_S;
            }
        }
    }

    static class TextObject extends SpanLabel {
        private int size;
        public TextObject(String title, int color, int margin, int size) {
            setText(title);
            getTextAllStyles().setFgColor(color);
            getTextAllStyles().setMarginUnit(Style.UNIT_TYPE_DIPS);
            getTextAllStyles().setMargin(Component.LEFT, margin);
            getTextAllStyles().setFont((Font.createSystemFont(FACE_SYSTEM,
                    STYLE_PLAIN, size)));
            this.size = size;
        }

        public void setBold() {
            getTextAllStyles().setFont((Font.createSystemFont(
                    FACE_SYSTEM, STYLE_BOLD, size))
            );
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
            taskContainer = new SpanMultiButton(taskData.getName() +
                    " (" + taskData.getTaskSizeString() + ')');
            taskContainer.setTextLine2(taskData.getTotalTimeString());

            taskContainer.getSelectedStyle().setBgColor(BLACK);

            if (taskData.isActive()) {
                taskContainer.setEmblem(
                        FontImage.createMaterial(FontImage.MATERIAL_ALARM_ON,
                                taskContainer.getUnselectedStyle())
                );
            }

            String tags = "";
            for (String t : taskData.getTags()) {
                tags += t + "   ";
            }
            if (!tags.isEmpty())
                taskContainer.setTextLine3(tags);

            // LISTENERS
            taskContainer.addActionListener(e-> shortPressEvent());
            taskContainer.addLongPressListener(e-> longPressEvent());

            // OPTIONS container
            ButtonObject edit = new ButtonObject();
            edit.setAllStyles("", COL_SELECTED, FontImage.MATERIAL_MODE_EDIT,PAD_3MM);
            edit.addActionListener(e->{ui.goEdit(taskData.getName());});

            ButtonObject archive = new ButtonObject();
            archive.setAllStyles("", COL_UNSELECTED,ICON_ARCHIVE,PAD_3MM);
            if (taskData.isArchived())
                archive.setAllStyles("", COL_UNSELECTED,ICON_UNARCHIVE,PAD_3MM);

            archive.addActionListener(e->{
                if (taskData.isArchived()){
                    ui.backend.getTaskByName(taskData.getName()).unarchive();
                    ui.backend.logfile.unarchiveTask(taskData);
                }
                else if (taskData.isActive()){
                    LocalDateTime time= taskData.stop();
                    ui.backend.logfile.stopTask(taskData, time);
                    ui.backend.logfile.archiveTask(taskData);
                    ui.backend.getTaskByName(taskData.getName()).archive();
                }
                else{
                    ui.backend.getTaskByName(taskData.getName()).archive();
                    ui.backend.logfile.archiveTask(taskData);
                }
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
            getAllStyles().setMargin(PAD_1MM,PAD_1MM,PAD_1MM,PAD_1MM);
        }

        private void longPressEvent() {
            if (taskData.isArchived()) {
                taskData.unarchive();
                ui.backend.logfile.unarchiveTask(this.taskData);
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
                LocalDateTime time = taskData.stop();
                ui.backend.logfile.stopTask(taskData,time);

            } else {
                Task activeTask = ui.backend.getActiveTask();
                if (activeTask != null) {
                    LocalDateTime time= activeTask.stop();
                    ui.backend.logfile.stopTask(activeTask,time);
                }
                LocalDateTime time= taskData.start();
                ui.backend.logfile.startTask(taskData,time);
            }
            ui.refreshScreen();
        }

        @Override
        public boolean animate() {
            taskContainer.setTextLine2(taskData.getTotalTimeString());
            return true; //TODO what does this mean
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
            nameLabel.getAllStyles().setFgColor(BLACK);

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
            Border simpleBorder = Border.createLineBorder(1,BLACK);
            getAllStyles().setBorder(simpleBorder);

            Label startLabel = new Label("Start: " + startTime);
            Label stopLabel = new Label("Stop: " + stopTime);


            UIComponents.ButtonObject Delete = new UIComponents.ButtonObject();
            Delete.setAllStyles("", RED, ICON_DELETE, PAD_3MM);

            UIComponents.ButtonObject Edit = new UIComponents.ButtonObject();
            Delete.setAllStyles("", RED, ICON_EDIT, PAD_3MM);
            Edit.setMyIcon(FontImage.MATERIAL_EDIT);
            Edit.getAllStyles().setBorder(RoundBorder.create().rectangle(true).color(COL_UNSELECTED));

            Container EastSide = new Container(BoxLayout.x());

            EastSide.add(stopLabel);
            EastSide.add(Edit);
            EastSide.add(Delete);

            add(WEST, startLabel);
            add(EAST, EastSide);
        }
    }

    static class showWarningDialog extends Dialog {
        public showWarningDialog(String warning) {
            setLayout(BoxLayout.y());
            setTitle("Wait!");
            add(new SpanLabel(warning));
            ButtonObject okButton = new ButtonObject();
            okButton.setAllStyles("Ok", COL_SELECTED, ' ', PAD_3MM);
            add(okButton);
            okButton.addActionListener(e->this.dispose());
            show();
        }
    }
}
