package org.ecs160.a2;
import static org.ecs160.a2.UITheme.*;

import com.codename1.components.SpanLabel;
import com.codename1.components.SpanMultiButton;
import com.codename1.ui.*;
import com.codename1.ui.animations.CommonTransitions;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.plaf.RoundBorder;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.Label;
import com.codename1.ui.spinner.Picker;

import java.time.LocalDateTime;
import java.util.Date;

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

            if (color >= 0) {
                this.getAllStyles().setBorder(RoundBorder.create()
                        .rectangle(true).color(color)
                );
            }

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
            switch (TaskSize.parse(size)) {
                case XL: return COL_SIZE_XL;
                case L: return COL_SIZE_L;
                case M: return COL_SIZE_M;
                case S: return COL_SIZE_S;
                default: return -1;
            }
        }
    }

    static class TextObject extends SpanLabel {
        private final int size;
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
        private final SpanMultiButton taskContainer;

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

            StringBuilder tags = new StringBuilder();
            for (String t : taskData.getTags()) {
                tags.append(t).append("   ");
            }
            if (tags.length() > 0)
                taskContainer.setTextLine3(tags.toString());

            // LISTENERS
            taskContainer.addActionListener(e-> shortPressEvent());
            taskContainer.addLongPressListener(e-> longPressEvent());

            // OPTIONS container
            ButtonObject edit = new ButtonObject();
            edit.setAllStyles("", COL_SELECTED,
                    FontImage.MATERIAL_MODE_EDIT,PAD_3MM);
            edit.addActionListener(e-> ui.goEdit(taskData.getName()));

            ButtonObject archive = new ButtonObject();
            archive.setAllStyles("",
                    COL_UNSELECTED,ICON_ARCHIVE,PAD_3MM);
            if (taskData.isArchived())
                archive.setAllStyles("",
                        COL_UNSELECTED,ICON_UNARCHIVE,PAD_3MM);

            archive.addActionListener(e->{
                if (taskData.isArchived()) {
                    ui.backend.unarchiveTask(taskData);
                    ui.refreshScreen();

                } else if (taskData.isActive()) {
                    Dialog areYouSure = new Dialog(BoxLayout.y());
                    areYouSure.add("Archive this currently running task?");
                    ButtonObject yesB = new ButtonObject();
                    yesB.setAllStyles("Yes", COL_UNSELECTED,' ',PAD_3MM);
                    yesB.addActionListener(yes -> {
                        ui.backend.archiveTask(taskData);
                        areYouSure.setTransitionOutAnimator(
                                        CommonTransitions.createEmpty());
                        areYouSure.dispose();
                        ui.refreshScreen();
                    });

                    ButtonObject noB = new ButtonObject();
                    noB.setAllStyles("Cancel", COL_SELECTED,' ',PAD_3MM);
                    noB.addActionListener(cancel -> areYouSure.dispose());


                    areYouSure.add(GridLayout.encloseIn(2,noB, yesB));
                    areYouSure.showPopupDialog(archive);
                } else {
                    ui.backend.archiveTask(taskData);
                    ui.refreshScreen();
                }
                log("archived/unarchived task");
            });
            Container options = new Container(BoxLayout.x());
            options.addAll(edit, archive);

            // taskPanel: TASK + OPTIONS
            SwipeableContainer taskPanel =
                            new SwipeableContainer(options, taskContainer);
            add(taskPanel);
            getAllStyles().setMarginUnit(Style.UNIT_TYPE_DIPS);
            getAllStyles().setMargin(PAD_1MM,PAD_1MM,PAD_1MM,PAD_1MM);
        }

        private void longPressEvent() {
            if (taskData.isArchived()) {
                ui.backend.unarchiveTask(taskData);
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
                ui.backend.stopTask(taskData);
            } else {
                ui.backend.startTask(taskData);
            }
            ui.refreshScreen();
        }

        @Override
        public boolean animate() {
            taskContainer.setTextLine2(taskData.getTotalTimeString());
            return taskData.isActive();
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
                            taskObj.getTimeBetween(startTime, endTime)));

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

    static class DatePickerObject extends Picker {
        public DatePickerObject(Date date) {
            setType(Display.PICKER_TYPE_CALENDAR);
            getStyle().setBorder(
                    RoundBorder.create().rectangle(true).
                            color(UITheme.LIGHT_GREY));
            getStyle().setPaddingUnit(Style.UNIT_TYPE_DIPS);
            getStyle().setPadding(UITheme.PAD_3MM,UITheme.PAD_3MM,
                    UITheme.PAD_3MM, UITheme.PAD_3MM);
            setDate(date);
        }
    }

    static class StartEndPickers extends Container {
        public StartEndPickers(Picker start, Picker end) {
            setLayout(BoxLayout.y());
            addAll(FlowLayout.encloseCenterMiddle(
                    new Label("Start"), start,
                    end, new Label("End"))
            );
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
